package io.github.uchagani.jp;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Playwright;

import java.util.Map;

public class OverrideConfig implements PlaywrightConfig {
    public static final Map<String, String> playwrightOptions = Map.of("JP_TEST_TWO", "BAR");

    @Override
    public BrowserConfig getBrowserConfig() {
        return new BrowserConfig()
                .setPlaywrightCreateOptions(new Playwright.CreateOptions().setEnv(playwrightOptions))
                .firefox().launch().setNewContextOptions(new Browser.NewContextOptions().setBaseURL("https://playwright.dev"));
    }
}
