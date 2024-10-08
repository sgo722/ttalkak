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

  @UseGuards(JwtAuthGuard)
  @Get('summary')
  async getSummary(
    @Request() request,
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
      +request.user.userId,
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
      serviceId: number;
      serviceType: string;
      senderId: number;
    },
  ): Promise<any> {
    const receipientId = +request.user.userId;
    await this.paymentService.processPayment(
      body.serviceId,
      body.serviceType,
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
      privateKey: string;
      address: string;
    },
  ) {
    return this.paymentService.savePrivateKey({
      userId: +request.user.userId,
      privateKey: body.privateKey,
      address: body.address,
    });
  }

  @UseGuards(JwtAuthGuard)
  @Get('signature')
  async getSignature(
    @Request() request,
  ) {
    return this.paymentService.getPrivateKey({
      userId: +request.user.userId
    });
  }

  @Post('sign')
  async signTransaction(
    @Body()
    body: {
      senderId: number;
      serviceType: string;
      serviceId: number;
      receipientId: number;
      address: string;
    },
  ) {
    return await this.paymentService.signTransaction({
      serviceId: body.serviceId,
      serviceType: body.serviceType,
      senderId: body.senderId,
      receipientId: body.receipientId,
      toAddress: body.address,
    });
  }
}
