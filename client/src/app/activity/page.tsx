"use client";

import { Octokit } from "@octokit/rest";
import { useState, useEffect } from "react";
import useAuthStore from "@/store/useAuthStore";

interface Webhook {
  id: number;
  config: {
    url: string;
  };
}

export default function ActivityPage() {
  const [message, setMessage] = useState<string>("");
  const [webhooks, setWebhooks] = useState<Webhook[]>([]);
  const userInfo = useAuthStore((state) => state.userInfo);

  const octokit = new Octokit({
    auth: userInfo?.accessToken,
  });

  const owner = "ljjunh";
  const repo = "study";
  const webhookUrl =
    "https://webhook.site/57cbbd96-6668-4a9c-a46f-b6b9d6ea00c0";

  useEffect(() => {
    fetchWebhooks();
  }, []);

  const fetchWebhooks = async () => {
    try {
      const response = await octokit.repos.listWebhooks({
        owner,
        repo,
      });
      setWebhooks(response.data as Webhook[]);
      setMessage("웹훅 목록을 가져왔습니다.");
    } catch (error) {
      setMessage("웹훅 목록 가져오기 실패");
      console.error("Error fetching webhooks:", error);
    }
  };

  const setupWebhook = async () => {
    try {
      const response = await octokit.repos.createWebhook({
        owner,
        repo,
        config: {
          url: webhookUrl,
          content_type: "json",
        },
        events: ["push"],
      });
      setMessage("웹훅 설정 성공");
      console.log("웹훅 만들었음", response.data);
      fetchWebhooks();
    } catch (error) {
      setMessage("웹훅 에러남");
      console.error("Error", error);
    }
  };

  const deleteWebhook = async (hookId: number) => {
    try {
      await octokit.repos.deleteWebhook({
        owner,
        repo,
        hook_id: hookId,
      });
      setMessage(`웹훅 삭제 성공 (ID: ${hookId})`);
      fetchWebhooks();
    } catch (error) {
      setMessage(`웹훅 삭제 실패 (ID: ${hookId})`);
      console.error("Error deleting webhook:", error);
    }
  };

  return (
    <div>
      <button className="border mx-5" onClick={setupWebhook}>
        웹훅 생성
      </button>
      <button className="border" onClick={fetchWebhooks}>
        웹훅 목록 새로고침
      </button>
      {message && <p>{message}</p>}
      <h2>웹훅 목록</h2>
      <ul>
        {webhooks.map((webhook: Webhook) => (
          <li key={webhook.id}>
            ID: {webhook.id}, URL: {webhook.config.url}
            <button
              className="border mx-5"
              onClick={() => deleteWebhook(webhook.id)}
            >
              삭제
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
}
