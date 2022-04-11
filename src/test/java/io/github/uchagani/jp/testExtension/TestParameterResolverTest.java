package io.github.uchagani.jp.testExtension;

import io.github.uchagani.jp.UseBrowserConfig;
import io.github.uchagani.jp.UseRestConfig;
import io.github.uchagani.jp.configs.DefaultBrowserConfig;
import io.github.uchagani.jp.configs.DefaultRestConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(TestObjectParameterResolver.class)
@UseBrowserConfig(DefaultBrowserConfig.class)
@UseRestConfig(DefaultRestConfig.class)
public class TestParameterResolverTest {
    @Test
    public void testObjectShouldBeCreated_withNonNullPlaywrightObjects(TestObject testObject) {
        assertThat(testObject).isNotNull();
        assertThat(testObject.playwright).isNotNull();
        assertThat(testObject.browser).isNotNull();
        assertThat(testObject.browserContext).isNotNull();
        assertThat(testObject.page).isNotNull();
        assertThat(testObject.apiRequestContext).isNotNull();
    }
}
