export interface History {
  id: number;
  fromAddress: string;
  toAddress: string;
  senderId: number;
  receipientId: number;
  blockHash: string;
  amount: string;
  serviceId: number;
  transactionId: number;
  createdAt: string;
  updatedAt: string;
}

export interface PaymentResponse {
  histories: History[];
  amount: number;
}
