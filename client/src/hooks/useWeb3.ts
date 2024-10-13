"use client";
import { ethers } from "ethers";
import { useEffect, useState } from "react";
import PaymentContract from "@/contracts/PaymentContract.json";
import ERC20 from "@/contracts/ERC20.json";
import Web3 from "web3";

declare global {
  interface Window {
    ethereum?: any;
  }
}

const GAS_PRICE = 0;
const GAS_LIMIT = 50000;

const useWeb3 = () => {
  const [account, setAccount] = useState<string | undefined>();
  const [provider, setProvider] = useState<ethers.BrowserProvider | null>(null);

  useEffect(() => {
    if (typeof window.ethereum !== "undefined") {
      const browserProvider = new ethers.BrowserProvider(window.ethereum);
      setProvider(browserProvider);
    }
  }, []);

  async function verifyAccount(
    address: string,
    privateKey: string
  ): Promise<boolean> {
    if (!provider) {
      throw new Error("블록체인 서버에 연결이 되어있지 않습니다.");
    }

    const web3 = new Web3(process.env.NEXT_PUBLIC_BLOCKCHAIN_PROVIDER);

    const account = web3.eth.accounts.privateKeyToAccount(privateKey);
    return account.address === address;
  }

  async function signToSend(contractAddress: string): Promise<{
    to: string;
    from: string;
    hash: string;
  }> {
    if (!provider) {
      throw new Error("블록체인 서버에 연결이 되어있지 않습니다.");
    }

    const signer = await provider.getSigner();
    const address = await signer.getAddress();

    const web3 = new Web3(process.env.NEXT_PUBLIC_BLOCKCHAIN_PROVIDER);
    const token = new web3.eth.Contract(
      ERC20.abi,
      process.env.NEXT_PUBLIC_SSF_TOKEN_CONTRACT_ADDRESS
    );

    const balance = await token.methods.balanceOf(address).call();

    const recipent = await signer.sendTransaction({
      from: address,
      to: process.env.NEXT_PUBLIC_SSF_TOKEN_CONTRACT_ADDRESS,
      gasPrice: GAS_PRICE,
      gasLimit: GAS_LIMIT,
      data: token.methods.approve(contractAddress, balance).encodeABI(),
    });

    return {
      to: recipent.to as string,
      from: recipent.from,
      hash: recipent.hash,
    };
  }

  return {
    account,
    verifyAccount,
    signToSend,
  };
};

export default useWeb3;
