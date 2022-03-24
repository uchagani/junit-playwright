package io.github.uchagani.jp;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Playwright;

import java.util.Map;

public class DefaultConfig implements PlaywrightConfig {
    public static final Map<String, String> playwrightOptions = Map.of("JP_TEST", "FOO");

    @Override
    public BrowserConfig getBrowserConfig() {
        return new BrowserConfig()
                .setPlaywrightCreateOptions(new Playwright.CreateOptions().setEnv(playwrightOptions))
                .chromium()
                .launch()
                .setNewContextOptions(new Browser.NewContextOptions()
                        .setBaseURL("https://google.com"));
    }
}
