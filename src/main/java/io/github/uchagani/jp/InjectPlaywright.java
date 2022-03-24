package io.github.uchagani.jp;

import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@ExtendWith(PlaywrightExtension.class)
public @interface InjectPlaywright {
    Class<? extends PlaywrightConfig> value() default PlaywrightConfig.class;
}
