package io.github.uchagani.jp;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import io.github.uchagani.jp.configs.DefaultBrowserConfig;
import io.github.uchagani.jp.configs.OverrideBrowserConfig;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@UseBrowserConfig(DefaultBrowserConfig.class)
public class BrowserContextAndPageTests {

    @Test
    public void shouldInjectBrowserContextDefinedAtClassLevel(BrowserContext browserContext, Page page) {
        assertThat(browserContext).isNotNull();
        assertThat(page).isNotNull();
        assertThat(browserContext.pages().size()).isEqualTo(1);
        assertThat(browserContext.pages().get(0)).isEqualTo(page);

        page.navigate("/");
        assertThat(page.url()).isEqualTo("https://www.google.com/");
    }

    @Test
    @UseBrowserConfig(OverrideBrowserConfig.class)
    public void shouldInjectBrowserDefinedAtTestLevel(BrowserContext browserContext, Page page) {
        assertThat(browserContext).isNotNull();
        assertThat(page).isNotNull();
        assertThat(browserContext.pages().size()).isEqualTo(1);
        assertThat(browserContext.pages().get(0)).isEqualTo(page);

        page.navigate("/java");
        assertThat(page.url()).isEqualTo("https://playwright.dev/java/");
    }
}
