import { NestFactory } from '@nestjs/core';
import { AppModule } from '@src/app.module';
import { CustomExceptionFilter } from '@src/common/exception/exception.filter';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  app.useGlobalFilters(new CustomExceptionFilter());
  app.enableCors({
    origin: ["http://localhost:3000", "https://ttalkak.com", "http://localhost:5173"],
    credentials: true,
    methods: "GET,HEAD,PUT,PATCH,POST,DELETE",
  });
  await app.listen(5555);
}
bootstrap();
