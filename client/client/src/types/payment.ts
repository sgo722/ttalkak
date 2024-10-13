export interface PaymentRegistrationStatus {
  userId: number;
  address: string;
  hasKey: boolean;
}

export interface PaymentCreateProps {
  privateKey: string;
  address: string;
}

export interface PaymentConfirmProps {
  fromAddress: string;
  toAddress: string;
  hash: string;
}

export interface ConfirmResponse {
  contract: boolean;
  admin: boolean;
}

export interface PaymentResult {
  serviceId: number;
  serviceType: string;
  domain: string;
  amount: string;
}
