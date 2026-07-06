package org.gan.assistant.service;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.GeneralGetParam;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.exception.UploadFileException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class AIService {

    @Value("${ai.qwen.api-key}")
    private String apiKey;


    @Value("${ai.qwen.model}")
    private String model;


    private Generation generation;

    public void init(){
        //初始化生成客户端
        this.generation=new Generation();
    }

    public String parseSCHEDULE(String userInput) throws ApiException, NoApiKeyException, UploadFileException, InputRequiredException {
        //1.构建Prompt
        String systemPrompt= """
                你是一个智能行程助理。你的任务是从用户输入中提取行程信息。
                                                                                           请严格按照以下规则输出 JSON，不要包含任何其他文字：
                
                                                                                           1. title：从输入中提取事件名称（如"开会"、"吃饭"、"健身"）。如果无法提取，默认使用"待办事项"。
                                                                                           2. description：提取额外描述（可选）。
                                                                                           3. startTime：提取开始时间，格式 yyyy-MM-ddTHH:mm:ss。
                                                                                              - "明天下午3点" → 明天的 15:00:00
                                                                                              - "后天上午10点" → 后天的 10:00:00
                                                                                              - "今晚8点" → 今天的 20:00:00
                                                                                           4. endTime：如果用户提到了结束时间，提取并返回；否则返回空字符串。
                
                                                                                           示例：
                                                                                           输入："明天下午3点开会"
                                                                                           输出：{"title":"会议","startTime":"2026-07-07T15:00:00"}
                
                                                                                           输入："今天晚上8点吃饭"
                                                                                           输出：{"title":"吃饭","startTime":"2026-07-06T20:00:00"}
                """;
        Message systemMsg=Message.builder()
                .role(Role.SYSTEM.getValue())
                .content(systemPrompt)
                .build();

        Message userMsg=Message.builder()
                .role(Role.SYSTEM.getValue())
                .content(systemPrompt)
                .build();

        //2.构建请求参数
        GenerationParam param=GenerationParam.builder()
                .apiKey(apiKey)
                .model(model)
                .messages(Arrays.asList(systemMsg,userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .build();

        //3.调用API
        Generation generation=new Generation();
        GenerationResult result=generation.call(param);
        return result.getOutput().getChoices().get(0).getMessage().getContent();

    }

}
