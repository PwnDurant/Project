package com.zqq.gateway.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zqq.common.pojo.Result;
import com.zqq.common.utils.JWTUtils;
import com.zqq.gateway.properties.AuthWhiteName;
import io.jsonwebtoken.Claims;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.util.List;

@Slf4j
@Component
public class AuthFilter implements GlobalFilter, Ordered {

//    private List<String> whiteName=List.of("/user/login","/user/register");

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AuthWhiteName authWhiteName;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

//        配置白名单
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        if(match(path,authWhiteName.getUrl())){
            return chain.filter(exchange);
        }

//        从header中获取token
        String userToken=request.getHeaders().getFirst("user_token");
        if(!StringUtils.hasLength(userToken)){
//            被拦截 TODO
            return unauthorizedResponse(exchange,"令牌不能为空");
        }

//        验证用户token
        Claims claims= JWTUtils.parseJWT(userToken);
        if(claims==null){
            log.info("令牌验证失败");
            //TODO
            return unauthorizedResponse(exchange,"令牌过期或者不合法");
        }


        return chain.filter(exchange);
    }

    private boolean match(String path, List<String> url) {
        if(url==null|| url.isEmpty()){
            return false;
        }

        return url.contains(path);
    }

    @SneakyThrows
    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange,String errorMsg)  {
        log.error("用户认证失败,url:{}",exchange.getRequest().getURI().getPath());
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        Result result=Result.fail(errorMsg);
        DataBuffer dataBuffer=response.bufferFactory().wrap(objectMapper.writeValueAsBytes(result));
        return response.writeWith(Mono.just(dataBuffer));
    }

    @Override
    public int getOrder() {
        return -200;  //order值越小优先级越高
    }
}
