package io.github.uchagani.jp.configs;

import com.microsoft.playwright.Playwright;
import io.github.uchagani.jp.BrowserConfig;
import io.github.uchagani.jp.PlaywrightConfig;

import java.util.Map;

public class PlaywrightCreateConfig implements PlaywrightConfig {
    public static final Map<String, String> playwrightOptions = Map.of("PLAYWRIGHT_BROWSERS_PATH", "/some/bad/path",
            "PLAYWRIGHT_SKIP_BROWSER_DOWNLOAD", "1");

    @Override
    public BrowserConfig getBrowserConfig() {
        return new BrowserConfig()
                .setPlaywrightCreateOptions(new Playwright.CreateOptions().setEnv(playwrightOptions))
                .chromium()
                .launch();
    }
}
