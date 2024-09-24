import { Injectable } from '@nestjs/common';
import Payment from '@contracts/build/contracts/PaymentContract.json';
import Web3 from 'web3';
import { ConfigService } from '@nestjs/config';
import { PrismaService } from '@src/prisma/prisma.service';
import { Prisma, Transaction, TransactionHistory } from '@prisma/client';
import { CustomException } from '@src/common/exception/exception';
import {
  ALREADY_PAID,
  INVALID_PAYMENT_CONTRACT,
} from '@src/common/exception/error.code';
import { Decimal } from '@prisma/client/runtime/library';
import { Receipt } from './payment.type';
import { from } from 'rxjs';

@Injectable()
export class PaymentService {
  private web3: Web3;

  constructor(
    private readonly configService: ConfigService,
    private readonly prisma: PrismaService,
  ) {
    this.web3 = new Web3(
      new Web3.providers.HttpProvider(
        this.configService.get<string>('BLOCKCHAIN_PROVIDER'),
      ),
    );
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

  async getPaymentSummary(
    address: string,
    year: number,
    month: number,
  ): Promise<any> {
    const startDate = new Date(`${year}-${month}-01`);
    const endDate = new Date(
      new Date(`${year}-${month}-01`).setMonth(startDate.getMonth() + 1),
    );

    const histories = await this.prisma.transactionHistory.groupBy({
      by: ['deploymentId'],
      _sum: {
        amount: true,
      },
      where: {
        fromAddress: address,
        createdAt: {
          gte: startDate,
          lt: endDate,
        },
      },
    });
    console.log(histories);
  }

  async processPayment(
    deploymentId: number,
    senderId: number,
    receipientId: number,
  ): Promise<any> {
    const transaction = await this.getTransaction(
      deploymentId,
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
          blockHash: response.blockHash.toString(),
          amount: transaction.amount,
          deploymentId: transaction.deploymentId,
          transactionId: transaction.id,
        },
      });

      console.log('트랜잭션이 성공적으로 전송되었습니다.');
    } catch (error) {
      // TODO: 만약, 결제에 실패한 경우 Instance를 삭제하고, 사용자에게 알림을 보내야 합니다.
      console.error(error);
      throw new CustomException(
        INVALID_PAYMENT_CONTRACT,
        '결제가 정상적으로 이루어지지 않았습니다.',
      );
    }
  }

  async saveSignedTransaction(
    transaction: Prisma.TransactionCreateInput,
  ): Promise<any> {
    return this.prisma.transaction.create({
      data: {
        deploymentId: transaction.deploymentId,
        senderId: transaction.senderId,
        receipientId: transaction.receipientId,
        amount: transaction.amount,
        transaction: transaction.transaction,
      },
    });
  }

  private async getTransaction(
    deploymentId: number,
    senderId: number,
    receipientId: number,
  ): Promise<Transaction> {
    return await this.prisma.transaction.findFirst({
      where: {
        deploymentId: deploymentId,
        senderId: senderId,
        receipientId: receipientId,
      },
    });
  }
}
