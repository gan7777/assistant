package org.gan.assistant.controller;

import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.exception.UploadFileException;
import org.gan.assistant.dto.ScheduleRequest;
import org.gan.assistant.dto.ScheduleResponse;
import org.gan.assistant.service.AIService;
import org.gan.assistant.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/agent")
public class AgentController {

    @Autowired
    private AIService aiService;

    @Autowired
    private ScheduleService scheduleService;

    @PostMapping("/chat")
    public ResponseEntity<?> chat(@RequestBody Map<String, String> request) {
        String userInput = request.get("message");
        if (userInput == null || userInput.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("请输入内容");
        }

        try {
            // 1. 调用 AI 解析用户输入
            String aiResponse = aiService.parseSCHEDULE(userInput);
            System.out.println("AI 原始返回: " + aiResponse);

            // 2. 解析 JSON
            Map<String, String> parsed = parseAIResponse(aiResponse);
            String title = parsed.getOrDefault("title", "待办事项");
            String startTimeStr = parsed.get("startTime");
            String endTimeStr = parsed.get("endTime");

            // 3. 如果 startTime 为空，设置默认时间（明天上午 9 点）
            if (startTimeStr == null || startTimeStr.trim().isEmpty()) {
                LocalDateTime defaultStart = LocalDateTime.now().plusDays(1).withHour(9).withMinute(0).withSecond(0);
                startTimeStr = defaultStart.toString();
                System.out.println("使用默认开始时间: " + startTimeStr);
            }

            LocalDateTime startTime = LocalDateTime.parse(startTimeStr);
            LocalDateTime endTime = null;
            if (endTimeStr != null && !endTimeStr.trim().isEmpty()) {
                endTime = LocalDateTime.parse(endTimeStr);
            }

            // 4. 构造 ScheduleRequest
            ScheduleRequest scheduleRequest = new ScheduleRequest();
            scheduleRequest.setTitle(title);
            scheduleRequest.setDescription(parsed.get("description"));
            scheduleRequest.setStartTime(startTime);
            scheduleRequest.setEndTime(endTime);

            // 5. 调用业务层创建行程
            ScheduleResponse response = scheduleService.createSchedule(scheduleRequest);

            Map<String, Object> result = new HashMap<>();
            result.put("message", "已为您创建行程");
            result.put("schedule", response);
            return ResponseEntity.ok(result);

        } catch (ApiException | NoApiKeyException | UploadFileException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("AI 服务调用失败: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("解析失败: " + e.getMessage());
        }
    }

    // 简单解析 AI 返回的 JSON 字符串
    private Map<String, String> parseAIResponse(String json) {
        Map<String, String> map = new HashMap<>();
        try {
            json = json.trim();
            // 如果包含 markdown 代码块，提取内容
            if (json.startsWith("```json")) {
                json = json.substring(7, json.lastIndexOf("```"));
            }
            json = json.replace("{", "").replace("}", "").replace("\"", "");
            String[] pairs = json.split(",");
            for (String pair : pairs) {
                String[] kv = pair.split(":");
                if (kv.length == 2) {
                    map.put(kv[0].trim(), kv[1].trim());
                }
            }
        } catch (Exception e) {
            map.put("error", "解析失败");
        }
        return map;
    }
}