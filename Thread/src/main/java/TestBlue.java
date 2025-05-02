import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TestBlue {

    public static String vivogpt() throws Exception {

        String appId = "2025498193";
        String appKey = "TFIgXzVvuKbKXTFP";
        String URI = "/vivogpt/completions";
        String DOMAIN = "api-ai.vivo.com.cn";
        String METHOD = "POST";
        UUID requestId = UUID.randomUUID();
        System.out.println("requestId: " + requestId);


        Map<String, Object> map = new HashMap<>();
        map.put("requestId", requestId.toString());
        String queryStr = mapToQueryString(map);

        //构建请求体
        Map<String, String> data = new HashMap<>();
        data.put("prompt", "你是谁");
        data.put("model", "vivo-BlueLM-TB-Pro");
        UUID sessionId = UUID.randomUUID();
        data.put("sessionId", sessionId.toString());
        // 添加人设参数（关键修改点）
        String systemPrompt = "你的名字叫小蓝助手，是一个精通古诗词的AI，回答需用中文，风格模仿李白。";
        data.put("systemPrompt", systemPrompt);
        System.out.println(sessionId);

        HttpHeaders headers = VivoAuth.generateAuthHeaders(appId, appKey, METHOD, URI, queryStr);
        headers.add("Content-Type", "application/json");
        System.out.println(headers);

        String url = String.format("http://%s%s?%s", DOMAIN, URI, queryStr);

        String requsetBodyString = new ObjectMapper().writeValueAsString(data);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setContentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE));
        httpHeaders.addAll(headers);
        HttpEntity<String> requestEntity = new HttpEntity<>(requsetBodyString, httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            RE data1= new ObjectMapper().readValue(response.getBody(),RE.class);
            System.out.println("Response body: " + data1.getData().getContent());
        } else {
            System.out.println("Error response: " + response.getStatusCode());
        }
        return response.getBody();
    }

    public static String mapToQueryString(Map<String, Object> map) {
        if (map.isEmpty()) {
            return "";

        }
        StringBuilder queryStringBuilder = new StringBuilder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (queryStringBuilder.length() > 0) {
                queryStringBuilder.append("&");
            }
            queryStringBuilder.append(entry.getKey());
            queryStringBuilder.append("=");
            queryStringBuilder.append(entry.getValue());
        }
        return queryStringBuilder.toString();
    }


    public static void main(String[] args) throws Exception {
        vivogpt();
    }

}
