package io.github.uchagani.jp;

import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@ExtendWith(PlaywrightExtension.class)
public @interface InjectPlaywright {
    Class<? extends PlaywrightConfig> value() default PlaywrightConfig.class;
}
