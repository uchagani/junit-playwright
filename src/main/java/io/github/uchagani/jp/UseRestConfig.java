package io.github.uchagani.jp;

import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.*;

@ExtendWith(APIRequestContextParameterResolver.class)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER})
public @interface UseRestConfig {
    Class<? extends PlaywrightRestConfig> value();
}
