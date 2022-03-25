package io.github.uchagani.jp;

import com.microsoft.playwright.Browser;

public class DefaultConfig implements PlaywrightConfig {

    @Override
    public BrowserConfig getBrowserConfig() {
        return new BrowserConfig()
                .chromium()
                .launch()
                .setNewContextOptions(new Browser.NewContextOptions()
                        .setBaseURL("https://google.com"));
    }
}
