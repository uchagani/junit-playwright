package io.github.uchagani.jp;

import com.microsoft.playwright.Browser;
import manifold.ext.rt.api.Jailbreak;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@InjectPlaywright(DefaultConfig.class)
public class InjectBrowserTests {
    @Test
    public void shouldInjectBrowserDefinedAtClassLevel(Browser browser) {
        assertThat(browser).isNotNull();
        com.microsoft.playwright.impl.@Jailbreak BrowserImpl br = (com.microsoft.playwright.impl.@Jailbreak BrowserImpl)browser;

        assertThat(br.name()).isEqualTo("chromium");
    }

    @Test
    @InjectPlaywright(OverrideConfig.class)
    public void shouldInjectBrowserDefinedAtTestLevel(Browser browser) {
        assertThat(browser).isNotNull();
        com.microsoft.playwright.impl.@Jailbreak BrowserImpl br = (com.microsoft.playwright.impl.@Jailbreak BrowserImpl)browser;

        assertThat(br.name()).isEqualTo("firefox");
    }

}
