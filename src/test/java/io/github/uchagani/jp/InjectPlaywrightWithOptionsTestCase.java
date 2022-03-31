package io.github.uchagani.jp;

import com.microsoft.playwright.Playwright;
import io.github.uchagani.jp.configs.PlaywrightOverrideConfig;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Tag("indirect")
public class InjectPlaywrightWithOptionsTestCase {
    @Test
    @InjectPlaywright(PlaywrightOverrideConfig.class)
    void playwrightIsInjected_withOptions(Playwright playwright) {
        assertThat(playwright).isNotNull();
    }
}
