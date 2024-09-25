package com.ttalkak.project.project.application.inputport;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ttalkak.project.common.UseCase;
import com.ttalkak.project.config.OpenAIFeignClient;
import com.ttalkak.project.project.application.outputport.LoadElasticSearchOutputPort;
import com.ttalkak.project.project.application.usecase.GetLLMUseCase;
import com.ttalkak.project.project.framework.web.response.AIMonitoringResponse;
import com.ttalkak.project.project.framework.web.response.MonitoringInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@UseCase
public class GetLLInputPort implements GetLLMUseCase {

    private final LoadElasticSearchOutputPort loadElasticSearchOutputPort;
    private final OpenAIFeignClient openAIFeignClient;

    @Override
    @SuppressWarnings("unchecked")
    public AIMonitoringResponse getMonitoringInfo(String deploymentId) throws Exception {

        MonitoringInfoResponse monitoring = loadElasticSearchOutputPort.getAIMonitoringInfo(deploymentId);
        if(monitoring == null || monitoring.getTotalDocCount() == 0) {
            return AIMonitoringResponse.builder()
                    .monitoringInfoResponse(monitoring)
                    .answer("집계된 데이터가 없습니다.")
                    .build();
        }

        String ipInfo = monitoring.getAccessIpInfos().stream()
                .map(ip -> String.format("%s: %d times", ip.getIp(), ip.getCount()))
                .collect(Collectors.joining(", "));

        String methodInfo = monitoring.getUsedMethodInfos().stream()
                .map(method -> String.format("%s, %d times", method.getMethod(), method.getCount()))
                .collect(Collectors.joining(", "));

        String errorsInfo = monitoring.getErrorCategories().stream()
                .map(category -> {
                    String topPaths = category.getTopPaths().stream()
                            .map(path -> String.format("%s, %d times", path.getPath(), path.getCount()))
                            .collect(Collectors.joining(", "));

                    return String.format("%s %d times\n Key routes: %s",
                            category.toString(), category.getCount(), topPaths);
                })
                .collect(Collectors.joining("\n"));

        long totalErrors = monitoring.getTotalErrors();
        long totalDocCount = monitoring.getTotalDocCount();
        double errorRate = (double) totalErrors / totalDocCount * 100;

        String input = """
            
            "We analyze the monitoring data. We provide an analysis summary of usage, errors, etc. in Korean. 
             We give advice on potential risks and performance improvements. Full letters cannot exceed 600 characters."
            
            Full Overview:
            Total requests: %d
            Average response time: %.2f seconds
            Top Call IPs: %s
            Using the HTTP Methods: %s
            Error Analysis:
            Overall error rate: %.2f%%
            %s
         """.formatted(monitoring.getTotalDocCount(), monitoring.getAvgResponseTime(), ipInfo, methodInfo, errorRate, errorsInfo);


        // 입력 메시지
        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", input);

        Map<String, Object> request = new HashMap<>();
        request.put("model", "gpt-4o-mini");
        request.put("messages", Collections.singletonList(message));

        String response = openAIFeignClient.getChatCompletion(request);

        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        Map<String, Object> answerMap = gson.fromJson(response, type);
        Map<String, Object> choices = (Map<String, Object>) ((List<Object>) answerMap.get("choices")).get(0);
        Map<String, Object> messages = (Map<String, Object>) choices.get("message");
        String content = (String) messages.get("content");

        return AIMonitoringResponse.builder()
                .monitoringInfoResponse(monitoring)
                .answer(content)
                .build();

    }
}
