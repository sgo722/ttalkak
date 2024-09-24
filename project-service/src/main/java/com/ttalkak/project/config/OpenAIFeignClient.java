package com.ttalkak.project.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;

@FeignClient(name = "openai", url = "https://api.openai.com/v1", configuration = OpenAIConfig.class)
public interface OpenAIFeignClient {
    @PostMapping("/chat/completions")
    String getChatCompletion(@RequestBody Map<String, Object> request);
}
