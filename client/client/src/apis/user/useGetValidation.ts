import client from "@/apis/core/client";
import { Validation } from "@/types/userInfo";

const getValidation = () => {
  return client.get<Validation>({
    url: "/payment/validate",
  });
};

export default getValidation;
