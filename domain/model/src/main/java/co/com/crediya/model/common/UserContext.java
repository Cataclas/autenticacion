package co.com.crediya.model.common;

import reactor.core.publisher.Mono;
import reactor.util.context.Context;

public class UserContext {
    
    private static final String USER_ID_KEY = "userId";
    private static final String USER_EMAIL_KEY = "userEmail";
    
    public static Context withUser(String userId, String userEmail) {
        return Context.of(USER_ID_KEY, userId, USER_EMAIL_KEY, userEmail);
    }
    
    public static Mono<String> getCurrentUserId() {
        return Mono.deferContextual(ctx -> 
            Mono.justOrEmpty(ctx.getOrEmpty(USER_ID_KEY))
                .cast(String.class)
                .defaultIfEmpty("SYSTEM"));
    }
    
    public static Mono<String> getCurrentUserEmail() {
        return Mono.deferContextual(ctx -> 
            Mono.justOrEmpty(ctx.getOrEmpty(USER_EMAIL_KEY))
                .cast(String.class)
                .defaultIfEmpty("SYSTEM"));
    }
}