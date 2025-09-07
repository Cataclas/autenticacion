package co.com.crediya.model.common;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.context.Context;

class UserContextTest {

    @Test
    void deberiaCrearContextoConUsuario() {
        // Given
        String userId = "user-123";
        String userEmail = "test@test.com";
        
        // When
        Context context = UserContext.withUser(userId, userEmail);
        
        // Then
        StepVerifier.create(
                Mono.just("test")
                    .flatMap(s -> UserContext.getCurrentUserId())
                    .contextWrite(context)
        )
        .expectNext(userId)
        .verifyComplete();
        
        StepVerifier.create(
                Mono.just("test")
                    .flatMap(s -> UserContext.getCurrentUserEmail())
                    .contextWrite(context)
        )
        .expectNext(userEmail)
        .verifyComplete();
    }

    @Test
    void deberiaRetornarSystemCuandoNoHayContexto() {
        // When & Then
        StepVerifier.create(UserContext.getCurrentUserId())
                .expectNext("SYSTEM")
                .verifyComplete();
        
        StepVerifier.create(UserContext.getCurrentUserEmail())
                .expectNext("SYSTEM")
                .verifyComplete();
    }

    @Test
    void deberiaRetornarSystemCuandoContextoEstaVacio() {
        // Given
        Context emptyContext = Context.empty();
        
        // When & Then
        StepVerifier.create(
                Mono.just("test")
                    .flatMap(s -> UserContext.getCurrentUserId())
                    .contextWrite(emptyContext)
        )
        .expectNext("SYSTEM")
        .verifyComplete();
        
        StepVerifier.create(
                Mono.just("test")
                    .flatMap(s -> UserContext.getCurrentUserEmail())
                    .contextWrite(emptyContext)
        )
        .expectNext("SYSTEM")
        .verifyComplete();
    }
}