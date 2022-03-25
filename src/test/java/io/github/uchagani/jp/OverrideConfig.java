package io.github.uchagani.jp;

import com.microsoft.playwright.Browser;

public class OverrideConfig implements PlaywrightConfig {

    @Override
    public BrowserConfig getBrowserConfig() {
        return new BrowserConfig()
                .firefox()
                .launch()
                .setNewContextOptions(new Browser.NewContextOptions().setBaseURL("https://playwright.dev"));
    }
}
