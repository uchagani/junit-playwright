package io.github.uchagani.jp;

public class TestConfig implements PlaywrightConfig {
    @Override
    public BrowserConfig createBrowserOptions() {
        return new BrowserConfig().chromium().launch();
    }
}
