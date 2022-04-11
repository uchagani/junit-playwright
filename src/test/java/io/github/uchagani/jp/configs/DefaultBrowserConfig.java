package io.github.uchagani.jp.configs;

import com.microsoft.playwright.Browser;
import io.github.uchagani.jp.BrowserConfig;
import io.github.uchagani.jp.PlaywrightBrowserConfig;

public class DefaultBrowserConfig implements PlaywrightBrowserConfig {

    @Override
    public BrowserConfig getBrowserConfig() {
        return new BrowserConfig()
                .chromium()
                .launch()
                .setNewContextOptions(new Browser.NewContextOptions()
                        .setBaseURL("https://google.com"));
    }
}
