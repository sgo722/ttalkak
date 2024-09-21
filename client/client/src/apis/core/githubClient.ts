import axios from "axios";

const githubClient = (githubApiKey: string) => {
  return axios.create({
    baseURL: "https://api.github.com",
    headers: {
      Authorization: `Bearer ${githubApiKey}`,
    },
  });
};

export default githubClient;
