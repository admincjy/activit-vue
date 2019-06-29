package com.wl.wlp2ploansystem.infrastructures.common.config;

import com.wl.wlp2ploansystem.infrastructures.common.errors.UserFriendlyException;
import com.wl.wlp2ploansystem.infrastructures.common.security.jwt.JWTConfigurer;
import com.wl.wlp2ploansystem.infrastructures.common.security.jwt.TokenProvider;
import com.wl.wlp2ploansystem.infrastructures.common.support.HttpHelper;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.io.UnsupportedEncodingException;
import java.util.Map;

//https://blog.csdn.net/haoyuyang/article/details/53364372

/**
 * WebSocket 消息通知设置
 *
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {
    @Autowired
    private TokenProvider tokenProvider;
    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        stompEndpointRegistry.addEndpoint("/pmtapi/endpointNotification")
                .setAllowedOrigins("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        //表示在topic和user这两个域上可以向客户端发消息
        registry.enableSimpleBroker("/user","/topic");
        registry.setApplicationDestinationPrefixes("/app");
//给指定用户发送一对一的主题前缀是"/user"
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration)  {
        registration.interceptors(new ChannelInterceptorAdapter() {

            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel)   {

                 // 安全限制
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                // 安全
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String jwt = null;

                    String bearerToken = accessor.getFirstNativeHeader(JWTConfigurer.AUTHORIZATION_HEADER);

                    try {

                        final Base64 base64 = new Base64();
                        String decodeBearerToken = new String(base64.decode(bearerToken), "UTF-8");

                        if (StringUtils.hasText(decodeBearerToken) && decodeBearerToken.startsWith("Bearer ")) {
                            jwt = decodeBearerToken.substring(7, decodeBearerToken.length());
                        }
                        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                            Authentication authentication = tokenProvider.getAuthentication(jwt);
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        } else {
                            throw new UserFriendlyException("禁止访问");
                        }
                    }
                    catch (UnsupportedEncodingException  ex){
                        throw new UserFriendlyException("编码错误");
                    }
                }

                return message;
            }
        });
    }
}