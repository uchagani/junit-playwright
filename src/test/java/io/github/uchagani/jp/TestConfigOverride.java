package io.github.uchagani.jp;

public class TestConfigOverride implements PlaywrightConfig {
    @Override
    public BrowserConfig createBrowserOptions() {
        return new BrowserConfig().firefox().launch();
    }
}
