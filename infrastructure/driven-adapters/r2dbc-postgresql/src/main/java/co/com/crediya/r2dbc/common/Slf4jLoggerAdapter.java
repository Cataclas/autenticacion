package co.com.crediya.r2dbc.common;

import co.com.crediya.model.common.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Slf4jLoggerAdapter implements Logger {
    
    @Override
    public void info(String message, Object... args) {
        log.info(message, args);
    }
    
    @Override
    public void warn(String message, Object... args) {
        log.warn(message, args);
    }
    
    @Override
    public void debug(String message, Object... args) {
        log.debug(message, args);
    }
    
    @Override
    public void error(String message, Object... args) {
        log.error(message, args);
    }
}