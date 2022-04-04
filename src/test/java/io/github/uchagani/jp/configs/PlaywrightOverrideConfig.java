package io.github.uchagani.jp.configs;

import com.microsoft.playwright.Playwright;
import io.github.uchagani.jp.BrowserConfig;
import io.github.uchagani.jp.PlaywrightConfig;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PlaywrightOverrideConfig implements PlaywrightConfig {
    @Override
    public BrowserConfig getBrowserConfig() {
        return new BrowserConfig()
                .setPlaywrightCreateOptions(new Playwright.CreateOptions()
                        .setEnv(Collections.singletonMap("PLAYWRIGHT_BROWSERS_PATH", "/some/bad/path")))
                .chromium()
                .launch();
    }
}
