package com.example.common.logging;

import com.example.common.exception.GlobalExceptionHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@ConditionalOnWebApplication
@Import({
                CorrelationIdFilter.class,
                CorrelationIdInterceptor.class,
                GlobalExceptionHandler.class,
                com.example.common.config.OpenApiConfig.class
})
public class ObservabilityAutoConfiguration {
}
