import { Module } from '@nestjs/common';
import { ConfigModule } from '@nestjs/config';
import { AuthModule } from '@src/auth/auth.module';
import { PaymentModule } from '@src/payment/payment.module';
import { PrismaService } from '@prisma/prisma.service';
import { PrismaModule } from '@prisma/prisma.module';

@Module({
  imports: [
    ConfigModule.forRoot({
      isGlobal: true,
      envFilePath:
        process.env.NODE_ENV === 'production'
          ? '.env.production'
          : '.env.development',
    }),
    AuthModule,
    PaymentModule,
    PrismaModule,
  ],
  providers: [PrismaService],
  exports: [],
})
export class AppModule {}
