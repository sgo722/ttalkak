import { ArgumentsHost, Catch, ExceptionFilter } from '@nestjs/common';
import { CustomException } from './exception';

@Catch(CustomException)
export class CustomExceptionFilter implements ExceptionFilter {
  catch(exception: CustomException, host: ArgumentsHost): void {
    const context = host.switchToHttp();
    const request = context.getRequest();
    const response = context.getResponse();
    const status = exception.errorCode.status;

    response.status(status).json({
      success: false,
      status: status,
      message: exception.message,
      path: request.url,
    });
  }
}
