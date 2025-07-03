package org.example.sign_language.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class DeepSeekService {

    private static final Logger logger = LoggerFactory.getLogger(DeepSeekService.class);

    @Value("${deepseek.api.key}")
    private String apiKey;

    @Value("${deepseek.api.url}")
    private String apiUrl;

    private final OkHttpClient client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();;

    public String analyzeSentence(String sentence) {
        String base="请将冒号后的语句，转换为手语语义，并按照手语语序，分解为词组，以[\"\",\"\"]数组的形式给我，只需要给我元组的形式：";
        MediaType mediaType = MediaType.parse("application/json");
        JsonObject jsonBody = new JsonObject();
        jsonBody.addProperty("model", "deepseek-v3");
        JsonObject message = new JsonObject();
        message.addProperty("role", "user");
        message.addProperty("content", base + sentence);
        JsonArray messages = new JsonArray();
        messages.add(message);
        jsonBody.add("messages", messages);
        RequestBody body = RequestBody.create(jsonBody.toString(), mediaType);

//        RequestBody body = RequestBody.create("{\"model\": \"" + "deepseek-v3" + "\",\"messages\":[{\"role\":\"user\",\"content\":\"" + base + sentence + "\"}]}", mediaType);
        Request request = new Request.Builder()
                .url(apiUrl)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + apiKey)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                System.out.println(responseBody);
                JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
                // 获取 choices 数组的第一个元素
                JsonObject choice = jsonObject.getAsJsonArray("choices").get(0).getAsJsonObject();
                // 获取 message 对象
                JsonObject messageAI = choice.getAsJsonObject("message");
                // 获取 content 字段的值
                return messageAI.get("content").getAsString();
            } else {
                logger.error("Failed to analyze sentence with DeepSeek. Response code: {}", response.code());
                System.out.println(response);
            }
        } catch (IOException e) {
            logger.error("Error analyzing sentence with DeepSeek", e);
        }
        return null;
    }
}
