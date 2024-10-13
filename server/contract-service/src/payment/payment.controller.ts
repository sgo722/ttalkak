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
import { request } from 'http';

interface BaseResponse<T> {
  success: boolean;
  message: string;
  status: number;
  data: T;
}

@Controller('v1/payment')
export class PaymentController {
  constructor(private readonly paymentService: PaymentService) {}

  @UseGuards(JwtAuthGuard)
  @Get('')
  async getPaymentHistory(
    @Request() request,
    @Query()
    query: {
      range: number;
    },
  ): Promise<BaseResponse<Receipt[]>> {
    if (!query.range) {
      return {
        success: true,
        message: 'OK',
        status: 200,
        data: await this.paymentService.getPaymentHistory(
          +request.user.userId,
          7,
        ),
      };
    }
    return {
      success: true,
      message: 'OK',
      status: 200,
      data: await this.paymentService.getPaymentHistory(
        +request.user.userId,
        query.range,
      ),
    };
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
    return {
      success: true,
      message: 'OK',
      status: 200,
      data: await this.paymentService.getPaymentSummary(
        +request.user.userId,
        query.year,
        query.month,
      ),
    };
  }

  @UseGuards(JwtAuthGuard)
  @Post('pay')
  async pay(
    @Request() request,
    @Body()
    body: {
      domain: string;
      serviceId: number;
      serviceType: string;
      senderId: number;
      address: string;
    },
  ): Promise<any> {
    const receipientId = +request.user.userId;
    await this.paymentService.processPayment(
      body.domain,
      body.serviceId,
      body.serviceType,
      body.senderId,
      receipientId,
      body.address,
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
    return {
      success: true,
      message: 'OK',
      status: 200,
      data: await this.paymentService.savePrivateKey({
        userId: +request.user.userId,
        privateKey: body.privateKey,
        address: body.address,
      }),
    };
  }

  @UseGuards(JwtAuthGuard)
  @Get('signature')
  async getSignature(@Request() request) {
    return {
      success: true,
      message: 'OK',
      status: 200,
      data: await this.paymentService.getPrivateKey({
        userId: +request.user.userId,
      }),
    };
  }

  @UseGuards(JwtAuthGuard)
  @Post('confirm')
  async getConfirm(
    @Request() request,
    @Body()
    body: {
      toAddress: string;
      fromAddress: string;
      hash: string;
    },
  ) {
    return {
      success: true,
      message: 'OK',
      status: 200,
      data: await this.paymentService.saveConfirm(
        +request.user.userId,
        body.toAddress,
        body.fromAddress,
        body.hash,
      ),
    };
  }

  @UseGuards(JwtAuthGuard)
  @Get('confirm')
  async getConfirmList(@Request() request) {
    return {
      success: true,
      message: 'OK',
      status: 200,
      data: await this.paymentService.getConfirm(+request.user.userId),
    };
  }

  @UseGuards(JwtAuthGuard)
  @Get('validate')
  async validateUser(@Request() request) {
    return {
      success: true,
      message: 'OK',
      status: 200,
      data: await this.paymentService.validateUser(+request.user.userId),
    };
  }

  @UseGuards(JwtAuthGuard)
  @Get('coin')
  async getToken(@Request() request) {
    return {
      success: true,
      message: 'OK',
      status: 200,
      data: await this.paymentService.getToken(+request.user.userId),
    };
  }
}
