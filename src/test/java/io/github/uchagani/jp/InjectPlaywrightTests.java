package io.github.uchagani.jp;

import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.PlaywrightException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@Execution(ExecutionMode.SAME_THREAD)
@InjectPlaywright(PlaywrightCreateConfig.class)
public class InjectPlaywrightTests {

    @Test
    public void shouldInject_Playwright_definedAtClassScope(Playwright playwright) {
        assertThat(playwright).isNotNull();
        assertThatThrownBy(() -> playwright.chromium().launch())
                .isInstanceOf(PlaywrightException.class)
                .hasMessageContaining("Executable doesn't exist");
    }
}
