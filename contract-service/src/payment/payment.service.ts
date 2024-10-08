import { Injectable } from '@nestjs/common';
import Web3, { Contract } from 'web3';
import { createCipheriv, createDecipheriv, randomBytes } from 'crypto';
import { ConfigService } from '@nestjs/config';
import { PrismaService } from '@src/prisma/prisma.service';
import { Prisma, Transaction, TransactionHistory } from '@prisma/client';
import PaymentContract from '@contracts/build/contracts/PaymentContract.json';
import { CustomException } from '@src/common/exception/exception';
import {
  ALREADY_PAID,
  ALREADY_TRANSACTION_EXIST,
  INVALID_PAYMENT_CONTRACT,
} from '@src/common/exception/error.code';
import { Decimal } from '@prisma/client/runtime/library';
import { Receipt } from './payment.type';

const AMOUNT = 10;

@Injectable()
export class PaymentService {
  private web3: Web3;
  private key: Buffer;
  private iv: Buffer;
  private payment: any;

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

    this.key = Buffer.from(
      this.configService.get<string>('ENCRYPT_SECRET_KEY'),
    );
    this.iv = Buffer.from(this.configService.get<string>('ENCRYPT_IV_KEY'));
  }

  async getPayments(userId: number, range: number): Promise<Receipt> {
    const userTransactions = await this.prisma.transaction.findMany({
      where: {
        OR: [
          {
            senderId: userId,
          },
        ],
        AND: [
          {
            createdAt: {
              gte: new Date(Date.now() - range * 24 * 60 * 60 * 1000),
            },
          },
        ],
      },
      include: {
        transactionHistories: true,
      },
    });

    const histories = userTransactions
      .map((transaction) => transaction.transactionHistories)
      .flat();

    return {
      histories,
      amount: histories.reduce((acc, cur) => {
        return +new Decimal(acc).add(cur.amount).toString();
      }, 0),
    };
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

    await this.prisma.userTransactionKey.create({
      data: {
        userId: userId,
        privateKey: encrypted,
        address: address,
      },
    });

    return {
      success: true,
    };
  }

  async getPrivateKey({
    userId
  }: {
    userId: number;
  }) {
    const key = await this.prisma.userTransactionKey.findUnique({
      where: {
        userId: userId
      }
    });

    return {
      userId: userId,
      address: key.address,
      hasKey: Boolean(key.privateKey)
    };
  }

  async signTransaction({
    serviceId,
    serviceType,
    senderId,
    receipientId,
    toAddress,
  }: {
    serviceId: number;
    serviceType: string;
    senderId: number;
    receipientId: number;
    toAddress: string;
  }) {
    const exist = await this.prisma.transaction.findFirst({
      where: {
        serviceId: serviceId,
        serviceType: serviceType,
        senderId: senderId,
        receipientId: receipientId,
      },
    });

    if (exist) return exist;

    const user = await this.prisma.userTransactionKey.findUnique({
      where: {
        userId: senderId,
      },
    });

    const decipher = createDecipheriv('aes-256-cbc', this.key, this.iv);
    let decrypted = decipher.update(user.privateKey, 'hex', 'utf8');
    decrypted += decipher.final('utf8');

    const signedTx = await this.web3.eth.accounts.signTransaction(
      {
        from: user.address,
        to: this.configService.get<string>('PAYMENT_CONTRACT_ADDRESS'),
        value: 0,
        gasPrice: 0,
        gasLimit: 50000,
        data: this.payment.methods.sendPayment(toAddress, 10).encodeABI(),
      },
      decrypted,
    );

    return this.prisma.transaction.create({
      data: {
        serviceId: serviceId,
        serviceType: serviceType,
        senderId: senderId,
        receipientId: receipientId,
        amount: AMOUNT,
        transaction: signedTx.rawTransaction,
      },
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
      by: ['serviceId'],
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
    serviceId: number,
    serviceType: string,
    senderId: number,
    receipientId: number,
  ): Promise<any> {
    const transaction = await this.getTransaction(
      serviceId,
      serviceType,
      senderId,
      receipientId,
    );

    if (!transaction) {
      throw new CustomException(
        INVALID_PAYMENT_CONTRACT,
        '결제 정보가 존재하지 않습니다.',
      );
    }

    const hasPaied = await this.prisma.transactionHistory.count({
      where: {
        transactionId: transaction.id,
        createdAt: {
          gte: new Date(Date.now() - 5 * 60 * 1000),
        },
      },
    });

    if (hasPaied) {
      throw new CustomException(ALREADY_PAID, '이미 결제가 완료되었습니다.');
    }

    try {
      const response = await this.web3.eth.sendSignedTransaction(
        transaction.transaction,
      );

      await this.prisma.transactionHistory.create({
        data: {
          fromAddress: response.from,
          toAddress: response.to,
          senderId: senderId,
          receipientId: receipientId,
          blockHash: response.blockHash.toString(),
          amount: transaction.amount,
          serviceId: transaction.id,
          transactionId: transaction.id,
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
  }

  private async getTransaction(
    serviceId: number,
    serviceType: string,
    senderId: number,
    receipientId: number,
  ): Promise<Transaction> {
    return await this.prisma.transaction.findFirst({
      where: {
        serviceId: serviceId,
        serviceType: serviceType,
        senderId: senderId,
        receipientId: receipientId,
      },
    });
  }
}
