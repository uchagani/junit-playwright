package io.github.uchagani.jp.configs;

import com.microsoft.playwright.Browser;
import io.github.uchagani.jp.BrowserConfig;
import io.github.uchagani.jp.PlaywrightBrowserConfig;

public class OverrideBrowserConfig implements PlaywrightBrowserConfig {

    @Override
    public BrowserConfig getBrowserConfig() {
        return new BrowserConfig()
                .firefox()
                .launch()
                .setNewContextOptions(new Browser.NewContextOptions().setBaseURL("https://playwright.dev"));
    }
}
