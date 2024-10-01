import {
  Controller,
  Post,
  Body,
  HttpException,
  HttpStatus,
  UseGuards,
  Request,
  Get,
  Query,
  Param,
} from '@nestjs/common';
import { JwtAuthGuard } from '@src/auth/auth.guard';
import { PaymentService } from '@src/payment/payment.service';
import { Receipt } from './payment.type';
import { CustomException } from '@src/common/exception/exception';
import { INVALID_INPUT } from '@src/common/exception/error.code';

@Controller('v1/payment')
export class PaymentController {
  constructor(private readonly paymentService: PaymentService) {}

  @UseGuards(JwtAuthGuard)
  @Get('')
  async getPayments(
    @Request() request,
    @Query()
    query: {
      range: number;
    },
  ): Promise<Receipt> {
    if (!query.range) {
      return this.paymentService.getPayments(+request.user.userId, 7);
    }
    return this.paymentService.getPayments(+request.user.userId, query.range);
  }

  @Get('summary/:address')
  async getSummary(
    @Param('address') address: string,
    @Query()
    query: {
      year: number;
      month: number;
    },
  ): Promise<any> {
    if (!query.year || !query.month) {
      throw new CustomException(INVALID_INPUT, '연도 및 월을 입력해주세요');
    }
    return this.paymentService.getPaymentSummary(
      address,
      query.year,
      query.month,
    );
  }

  @UseGuards(JwtAuthGuard)
  @Post('pay')
  async pay(
    @Request() request,
    @Body()
    body: {
      deploymentId: number;
      senderId: number;
    },
  ): Promise<any> {
    const receipientId = +request.user.userId;
    await this.paymentService.processPayment(
      body.deploymentId,
      body.senderId,
      receipientId,
    );
  }

  @UseGuards(JwtAuthGuard)
  @Post('signature')
  async submitSignature(
    @Request() request,
    @Body()
    body: {
      amount: number;
      deploymentId: number;
      transaction: string;
      receipientId: number;
    },
  ) {
    if (!body.transaction) {
      throw new HttpException('No signature provided', HttpStatus.BAD_REQUEST);
    }

    this.paymentService.saveSignedTransaction({
      deploymentId: body.deploymentId,
      senderId: +request.user.userId,
      receipientId: body.receipientId,
      amount: body.amount,
      transaction: body.transaction,
    });
  }
}
