package io.github.uchagani.jp.configs;

import com.microsoft.playwright.Playwright;
import io.github.uchagani.jp.BrowserConfig;
import io.github.uchagani.jp.PlaywrightBrowserConfig;

import java.util.Collections;

public class PlaywrightOverrideBrowserConfig implements PlaywrightBrowserConfig {
    @Override
    public BrowserConfig getBrowserConfig() {
        return new BrowserConfig()
                .setPlaywrightCreateOptions(new Playwright.CreateOptions()
                        .setEnv(Collections.singletonMap("PLAYWRIGHT_BROWSERS_PATH", "/some/bad/path")))
                .chromium()
                .launch();
    }
}
