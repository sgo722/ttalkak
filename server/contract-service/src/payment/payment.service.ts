import { Injectable } from '@nestjs/common';
import Web3, { Contract } from 'web3';
import { createCipheriv, createDecipheriv, randomBytes } from 'crypto';
import { ConfigService } from '@nestjs/config';
import { PrismaService } from '@src/prisma/prisma.service';
import { Prisma, TransactionConfrim, TransactionHistory } from '@prisma/client';
import PaymentContract from '@contracts/build/contracts/PaymentContract.json';
import ERC20Contract from '@contracts/build/contracts/ERC20.json';
import { CustomException } from '@src/common/exception/exception';
import {
  ALREADY_PAID,
  ALREADY_TRANSACTION_EXIST,
  INVALID_PAYMENT_CONTRACT,
} from '@src/common/exception/error.code';
import { Decimal } from '@prisma/client/runtime/library';
import { Receipt } from './payment.type';
import { from } from 'rxjs';

const AMOUNT = 10;

@Injectable()
export class PaymentService {
  private web3: Web3;
  private key: Buffer;
  private iv: Buffer;
  private payment: any;
  private token: any;

  constructor(
    private readonly configService: ConfigService,
    private readonly prisma: PrismaService,
  ) {
    this.web3 = new Web3(
      new Web3.providers.HttpProvider(
        this.configService.get<string>('BLOCKCHAIN_PROVIDER'),
      ),
    );
    this.payment = new this.web3.eth.Contract(
      PaymentContract.abi,
      this.configService.get<string>('PAYMENT_CONTRACT_ADDRESS'),
    );
    this.token = new this.web3.eth.Contract(
      ERC20Contract.abi,
      this.configService.get<string>('SSF_TOKEN_CONTRACT_ADDRESS'),
    );

    this.key = Buffer.from(
      this.configService.get<string>('ENCRYPT_SECRET_KEY'),
    );
    this.iv = Buffer.from(this.configService.get<string>('ENCRYPT_IV_KEY'));
  }

  async savePrivateKey({
    userId,
    privateKey,
    address,
  }: {
    userId: number;
    privateKey: string;
    address: string;
  }) {
    const cipher = createCipheriv('aes-256-cbc', this.key, this.iv);
    let encrypted = cipher.update(privateKey, 'utf8', 'hex');
    encrypted += cipher.final('hex');

    await this.prisma.userTransactionKey.upsert({
      where: {
        userId: userId,
      },
      create: {
        userId: userId,
        privateKey: encrypted,
        address: address,
      },
      update: {
        userId: userId,
        privateKey: encrypted,
        address: address,
      },
    });

    return {
      success: true,
    };
  }

  async getPrivateKey({ userId }: { userId: number }) {
    const key = await this.prisma.userTransactionKey.findUnique({
      where: {
        userId: userId,
      },
    });

    if (key) {
      return {
        userId: userId,
        address: key.address,
        hasKey: Boolean(key.privateKey),
      };
    }

    return {
      userId: userId,
      address: '',
      hasKey: false,
    };
  }

  async getPaymentHistory(userId: number, range: number): Promise<any> {
    const histories = await this.prisma.transactionHistory.groupBy({
      by: ['domain', 'serviceType'],
      _sum: {
        amount: true,
      },
      where: {
        senderId: userId,
      },
    });

    return histories.map((history) => {
      return {
        serviceType: history.serviceType,
        domain: history.domain,
        amount: history._sum.amount,
      };
    });
  }

  async getPaymentSummary(
    userId: number,
    year: number,
    month: number,
  ): Promise<any> {
    const startDate = new Date(`${year}-${month}-01`);
    const endDate = new Date(
      new Date(`${year}-${month}-01`).setMonth(startDate.getMonth() + 1),
    );

    const histories = await this.prisma.transactionHistory.groupBy({
      by: ['serviceId', 'serviceType'],
      _sum: {
        amount: true,
      },
      where: {
        receipientId: userId,
        createdAt: {
          gte: startDate,
          lt: endDate,
        },
      },
    });

    if (!histories) {
      return [];
    }

    return histories;
  }

  async processPayment(
    domain: string,
    serviceId: number,
    serviceType: string,
    senderId: number,
    receipientId: number,
    toAddress: string,
  ): Promise<any> {
    const sender = await this.prisma.userTransactionKey.findUnique({
      where: {
        userId: senderId,
      },
    });

    if (!sender) {
      throw new CustomException(
        ALREADY_TRANSACTION_EXIST,
        '트랜잭션 키가 존재하지 않습니다.',
      );
    }

    const decipher = createDecipheriv('aes-256-cbc', this.key, this.iv);
    let decrypted = decipher.update(sender.privateKey, 'hex', 'utf8');
    decrypted += decipher.final('utf8');

    const hasPaied = await this.prisma.transactionHistory.count({
      where: {
        serviceId: serviceId,
        createdAt: {
          gte: new Date(Date.now() - 4 * 60 * 1000),
        },
      },
    });

    if (hasPaied) {
      throw new CustomException(ALREADY_PAID, '이미 결제가 완료되었습니다.');
    }

    try {
      const signedTx = await this.web3.eth.accounts.signTransaction(
        {
          nonce: await this.web3.eth.getTransactionCount(sender.address),
          from: sender.address,
          to: this.configService.get<string>('PAYMENT_CONTRACT_ADDRESS'),
          data: this.payment.methods
            .transfer(
              sender.address,
              this.configService.get<string>('ADMIN_ADDRESS'),
              AMOUNT,
            )
            .encodeABI(),
          gasPrice: 0,
          gasLimit: 50000,
          value: 0,
        },
        '0x' + decrypted.trim(),
      );

      // 서명된 트랜잭션을 블록체인에 전송
      const receipt = await this.web3.eth.sendSignedTransaction(
        signedTx.rawTransaction,
      );

      await this.prisma.transactionHistory.create({
        data: {
          domain: domain,
          fromAddress: receipt.from,
          toAddress: toAddress,
          senderId: senderId,
          receipientId: receipientId,
          blockHash: receipt.blockHash.toString(),
          amount: AMOUNT,
          serviceId: serviceId,
          serviceType: serviceType,
        },
      });
    } catch (error) {
      // TODO: 만약, 결제에 실패한 경우 Instance를 삭제하고, 사용자에게 알림을 보내야 합니다.
      console.log(error);
      if (error.data) {
        console.error(this.web3.utils.hexToUtf8(error.data));
      }
      throw new CustomException(
        INVALID_PAYMENT_CONTRACT,
        '결제가 정상적으로 이루어지지 않았습니다.',
      );
    }

    return {
      success: true,
    };
  }

  async saveConfirm(
    userId: number,
    toAddress: string,
    fromAddress: string,
    hash: string,
  ) {
    const confirm = await this.prisma.transactionConfrim.create({
      data: {
        userId: userId,
        toAddress: toAddress,
        fromAddress: fromAddress,
        hash: hash,
      },
    });

    return confirm;
  }

  async getConfirm(userId: number): Promise<{
    contract: boolean;
    admin: boolean;
  }> {
    const confirm = await this.prisma.transactionConfrim.findMany({
      where: {
        userId: userId,
      },
    });

    return {
      contract:
        confirm.filter(
          (c) =>
            c.toAddress ===
            this.configService.get<string>('PAYMENT_CONTRACT_ADDRESS'),
        ).length > 0,
      admin:
        confirm.filter(
          (c) =>
            c.toAddress === this.configService.get<string>('ADMIN_ADDRESS'),
        ).length > 0,
    };
  }

  async validateUser(userId: number) {
    const confirm = await this.getConfirm(userId);
    const privateKey = await this.getPrivateKey({ userId: userId });

    return {
      hasKey: privateKey.hasKey,
      hasContract: confirm.contract,
      hasAdmin: confirm.admin,
    };
  }

  async getToken(userId: number) {
    const token = await this.prisma.transactionHistory.aggregate({
      where: {
        receipientId: userId,
      },
      _sum: {
        amount: true,
      },
    });

    return {
      coin: token._sum.amount,
    };
  }
}
