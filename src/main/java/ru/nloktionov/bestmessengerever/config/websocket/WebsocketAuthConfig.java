package ru.nloktionov.bestmessengerever.config.websocket;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import ru.nloktionov.bestmessengerever.exceptions.BadMessageRequestException;
import ru.nloktionov.bestmessengerever.exceptions.NotPermittedException;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@RequiredArgsConstructor
public class WebsocketAuthConfig implements WebSocketMessageBrokerConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(WebsocketAuthConfig.class);

    private final JwtDecoder jwtDecoder;

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    List<String> authorization = accessor.getNativeHeader("X-Authorization");
                    logger.debug("X-Authorization: {}", authorization);

                    String accessToken = authorization.get(0).split(" ")[1];
                    Jwt jwt = jwtDecoder.decode(accessToken);
                    JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
                    Authentication authentication = converter.convert(jwt);
                    accessor.setUser(authentication);
                } else if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
                    JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) accessor.getUser();
                    Jwt jwt = (Jwt) authenticationToken.getCredentials();
                    Long currentUserId = (Long) jwt.getClaims().get("userId");
                    String destination = accessor.getDestination();
                    if (destination == null)
                        throw new BadMessageRequestException("Subscribe must have a destination");
                    destination = destination.substring(1);
                    var destinationArray = destination.split("/");

                    Long requestedUserId = Long.valueOf(destinationArray[1]);
                    if (!requestedUserId.equals(currentUserId))
                        throw new NotPermittedException("Subscription destination contains user id " + requestedUserId + ". Currently authed user id: " + currentUserId);
                }
                return message;
            }
        });
    }
}
