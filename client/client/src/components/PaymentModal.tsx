import { useState, ChangeEvent, FormEvent } from "react";
import { IoMdClose } from "react-icons/io";
import { PaymentCreateProps } from "@/types/payment";
import useCreatePayment from "@/apis/payment/useCreatePayment";
import useWeb3 from "@/hooks/useWeb3";
import { toast } from "react-toastify";
import useCreateConfirm from "@/apis/payment/useCreateConfirm";

interface PaymentModalProps {
  isOpen: boolean;
  onClose: () => void;
}

export default function PaymentModal({ isOpen, onClose }: PaymentModalProps) {
  const [address, setAddress] = useState<string>("");
  const [privateKey, setPrivateKey] = useState<string>("");
  const [step, setStep] = useState<number>(1);

  const { signToSend, verifyAccount } = useWeb3();
  const { mutate: createPayment } = useCreatePayment();
  const { mutate: createConfirm } = useCreateConfirm();

  // (1/3) 지갑 등록 요청
  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const paymentData: PaymentCreateProps = {
      address,
      privateKey,
    };

    try {
      if (!(await verifyAccount(address, "0x" + privateKey))) {
        toast.error("입력한 값이 유효하지 않습니다.");
        return;
      }
    } catch (error) {
      toast.error("입력한 값이 유효하지 않습니다.");
      return;
    }
    

    createPayment(paymentData, {
      onSuccess: () => {
        setStep(2);
      },
      onError: (error) => {
        console.error("Payment creation failed:", error);
      },
    });
  };

  // (2/3) 권한 승인 1단계 요청
  const handleApproveFirst = async () => {
    const response = await signToSend(process.env.NEXT_PUBLIC_PAYMENT_CONTRACT_ADDRESS!!)
    createConfirm({
      fromAddress: response.from,
      toAddress: process.env.NEXT_PUBLIC_PAYMENT_CONTRACT_ADDRESS!!,
      hash: response.hash,
    }, {
      onSuccess: () => {
        setStep(3);
      }
    })
  };

  // (3/3) 권한 승인 2단계 요청
  const handleApproveSecond = async () => {
    const response = await signToSend(process.env.NEXT_PUBLIC_ADMIN_ADDRESS!!)
    createConfirm({
      fromAddress: response.from,
      toAddress: process.env.NEXT_PUBLIC_ADMIN_ADDRESS!!,
      hash: response.hash,
    }, {
      onSuccess: () => {
        onClose();
      }
    })
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white p-6 rounded-lg shadow-xl relative w-96 h-64">
        <button className="absolute top-4 right-4" onClick={onClose}>
          <IoMdClose />
        </button>

        {step === 1 && (
          <div className="relative">
            <div className="absolute top-[-7px] left-0 text-sm text-[#acacac]">
              {step}/3
            </div>
            <h2 className="text-center text-xl mb-6">지갑 등록</h2>

            <form className="space-y-4" onSubmit={handleSubmit}>
              <div className="flex items-center space-x-2">
                <input
                  type="text"
                  value={address}
                  onChange={(e) => {
                    setAddress(e.target.value);
                  }}
                  placeholder="MetaMask 지갑 주소를 입력해주세요"
                  className="border border-gray-300 px-4 py-2 w-full rounded-md"
                  required
                />
              </div>

              <input
                type="text"
                value={privateKey}
                onChange={(e) => {
                  setPrivateKey(e.target.value);
                }}
                placeholder="MetaMask private Key를 입력해주세요"
                className="border border-gray-300 px-4 py-2 w-full rounded-md"
                required
              />

              <button
                type="submit"
                className="bg-black text-white py-2 flex w-full justify-center rounded-md cursor-pointer"
              >
                다음
              </button>
            </form>
          </div>
        )}

        {step === 2 && (
          <div className="relative">
            <div className="absolute top-[-7px] left-0 text-sm text-[#acacac]">
              {step}/3
            </div>
            <h2 className="text-center text-xl mb-5">송금 권한 승인</h2>
            <div className="space-y-4 flex flex-col">
              <div className="text-center text-[#666666ff] py-10 bg-gradient-to-br from-[#f5f5f5] via-[#F4F4F5] to-[#f1f1ff] text-[#3b3b3b]">
                결제 연동을 위해 권한을 승인해주세요.
              </div>
              <button
                className="bg-black text-white py-2 flex w-full justify-center rounded-md cursor-pointer"
                onClick={handleApproveFirst}
              >
                1단계 인증하기
              </button>
            </div>
          </div>
        )}

        {step === 3 && (
          <div className="relative">
            <div className="absolute top-[-7px] left-0 text-sm text-[#acacac]">
              {step}/3
            </div>
            <h2 className="text-center text-xl mb-5">송금 권한 승인</h2>
            <div className="space-y-4 flex flex-col">
              <div className="text-center text-[#666666ff] py-10 bg-gradient-to-br from-[#f5f5f5] via-[#F4F4F5] to-[#f1f1ff] text-[#3b3b3b]">
                결제 연동을 위해 권한을 승인해주세요.
              </div>
              <button
                className="bg-black text-white py-2 flex w-full justify-center rounded-md cursor-pointer"
                onClick={handleApproveSecond}
              >
                2단계 인증하고 완료하기
              </button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
