package io.github.uchagani.jp.testExtension;

import io.github.uchagani.jp.InjectPlaywright;
import io.github.uchagani.jp.configs.DefaultConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(TestObjectParameterResolver.class)
@InjectPlaywright(DefaultConfig.class)
public class TestParameterResolverTest {
    @Test
    public void testObjectShouldBeCreated_withNonNullPlaywrightObjects(TestObject testObject) {
        assertThat(testObject).isNotNull();
        assertThat(testObject.playwright).isNotNull();
        assertThat(testObject.browser).isNotNull();
        assertThat(testObject.browserContext).isNotNull();
        assertThat(testObject.page).isNotNull();
    }
}
