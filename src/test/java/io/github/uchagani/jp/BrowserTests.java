package io.github.uchagani.jp;

import com.microsoft.playwright.Browser;
import io.github.uchagani.jp.configs.DefaultBrowserConfig;
import io.github.uchagani.jp.configs.OverrideBrowserConfig;
import manifold.ext.rt.api.Jailbreak;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@UseBrowserConfig(DefaultBrowserConfig.class)
public class BrowserTests {
    @Test
    public void shouldInjectBrowserDefinedAtClassLevel(Browser browser) {
        assertThat(browser).isNotNull();
        com.microsoft.playwright.impl.@Jailbreak BrowserImpl br = (com.microsoft.playwright.impl.@Jailbreak BrowserImpl)browser;

        assertThat(br.name()).isEqualTo("chromium");
    }

    @Test
    @UseBrowserConfig(OverrideBrowserConfig.class)
    public void shouldInjectBrowserDefinedAtTestLevel(Browser browser) {
        assertThat(browser).isNotNull();
        com.microsoft.playwright.impl.@Jailbreak BrowserImpl br = (com.microsoft.playwright.impl.@Jailbreak BrowserImpl)browser;

        assertThat(br.name()).isEqualTo("firefox");
    }
}
