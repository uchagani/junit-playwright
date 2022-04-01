package io.github.uchagani.jp;

import com.microsoft.playwright.Playwright;
import io.github.uchagani.jp.configs.DefaultConfig;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Tag("indirect")
public class InjectPlaywrightTestCase {
    @Test
    @InjectPlaywright(DefaultConfig.class)
    void playwrightIsInjected(Playwright playwright) {
        assertThat(playwright).isNotNull();
    }
}
