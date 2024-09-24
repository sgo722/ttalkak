import { TransactionHistory } from '@prisma/client';

export interface Receipt {
  histories: TransactionHistory[];
  amount: number;
}
