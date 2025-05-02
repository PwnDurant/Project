import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public class MyAI {

    private static final String API_KEY = "4a468343374f46b3a9e8ac0e97cba036.9wLGmoLwelLbGKjx";
    private static final String API_URL = "https://open.bigmodel.cn/api/paas/v4/chat/completions";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) throws Exception {

        Map<String, Object> requestBody = Map.of(
                "model", "glm-4", // 或 glm-3-turbo，glm-4 更强
                "messages", List.of(
                        Map.of("role", "system", "content", "你是一名诗人"),
                        Map.of("role", "user", "content", "请写一首关于春天的古诗")
                )
        );

        String requestJson = objectMapper.writeValueAsString(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Authorization", "Bearer " + API_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestJson))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonNode root = objectMapper.readTree(response.body());

        if (root.has("choices")) {
            String feedback = root.get("choices").get(0).get("message").get("content").asText();
            System.out.println("\n AI 评价：\n" + feedback);
        } else {
            System.out.println("无法解析响应内容。");
        }
    }

}
