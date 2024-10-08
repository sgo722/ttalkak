import { NestFactory } from '@nestjs/core';
import { AppModule } from '@src/app.module';
import { CustomExceptionFilter } from '@src/common/exception/exception.filter';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  app.useGlobalFilters(new CustomExceptionFilter());
  await app.listen(5555);
}
bootstrap();
