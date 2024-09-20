import client from "@/apis/core/client";
import { UserInfo } from "@/types/userInfo";

const getUserInfo = () => {
  return client.get<UserInfo>({
    url: "/user/me",
  });
};

export default getUserInfo;
