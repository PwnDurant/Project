import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;

public class TestAi {

    private static final String API_KEY = "4a468343374f46b3a9e8ac0e97cba036.9wLGmoLwelLbGKjx"; // 替换为你自己的
    private static final String API_URL = "https://open.bigmodel.cn/api/paas/v4/chat/completions";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) throws Exception {
        String userCode = """
            public class Solution {
                public int add(int a, int b) {
                    return a + b;
                }
            }
        """;

        String prompt = "请作为一名资深 Java 工程师，分析以下代码的优缺点、可读性、结构，并提出优化建议：\n\n" + userCode;

        Map<String, Object> requestBody = Map.of(
                "model", "glm-4", // 或 glm-3-turbo，glm-4 更强
                "messages", List.of(
                        Map.of("role", "system", "content", "你是一名优秀的 Java 代码审查专家。"),
                        Map.of("role", "user", "content", prompt)
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

        // 打印原始响应内容
        System.out.println("原始响应：\n" + response.body());

        JsonNode root = objectMapper.readTree(response.body());
        if (root.has("choices")) {
            String feedback = root.get("choices").get(0).get("message").get("content").asText();
            System.out.println("\n✅ AI 评价：\n" + feedback);
        } else {
            System.out.println("⚠️ 无法解析响应内容。");
        }
    }
}