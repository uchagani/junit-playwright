package io.github.uchagani.jp.configs;

import io.github.uchagani.jp.BrowserConfig;
import io.github.uchagani.jp.PlaywrightBrowserConfig;

public class TraceBrowserConfigSaveOnlyOnFailure implements PlaywrightBrowserConfig {
    @Override
    public BrowserConfig getBrowserConfig() {
        return new BrowserConfig()
                .chromium()
                .launch()
                .enableTracingOnlyOnFailure();
    }
}
