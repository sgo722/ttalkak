import { Module } from '@nestjs/common';
import { PaymentService } from '@src/payment/payment.service';
import { PaymentController } from '@src/payment/payment.controller';

@Module({
  controllers: [PaymentController],
  providers: [PaymentService],
})
export class PaymentModule {}
