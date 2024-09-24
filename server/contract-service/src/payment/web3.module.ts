import { Module } from '@nestjs/common';
import Web3 from 'web3';

@Module({
  providers: [
    {
      provide: 'WEB3',
      useFactory: () => {
        return new Web3(new Web3.providers.HttpProvider(process.env.BLOCKCHAIN_PROVIDER));
      },
    },
  ],
  exports: ['WEB3'],
})
export class Web3Module {}