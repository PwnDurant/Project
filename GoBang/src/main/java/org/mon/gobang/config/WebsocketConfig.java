package org.mon.gobang.config;

import org.mon.gobang.api.GameAPI;
import org.mon.gobang.api.MatchAPI;
import org.mon.gobang.api.TestApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocket
public class WebsocketConfig implements WebSocketConfigurer {

    @Autowired
    private TestApi testApi;

    @Autowired
    private MatchAPI matchAPI;

    @Autowired
    private GameAPI gameAPI;
    /**
     * 注册一些handler到框架中
     * @param registry
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(testApi,"/test");
        registry.addHandler(matchAPI,"/findMatch")
                .addInterceptors(new HttpSessionHandshakeInterceptor());
        registry.addHandler(gameAPI,"/game")
                .addInterceptors(new HttpSessionHandshakeInterceptor());
    }

}
