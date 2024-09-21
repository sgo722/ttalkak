import { Octokit } from "@octokit/rest";
import useAuthStore from "@/store/useAuthStore";

const userInfo = useAuthStore.getState().userInfo;
const webhookClient = new Octokit({ auth: userInfo?.accessToken });

export default webhookClient;
