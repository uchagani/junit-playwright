package io.github.uchagani.jp.configs;

import io.github.uchagani.jp.BrowserConfig;
import io.github.uchagani.jp.PlaywrightConfig;

public class TraceConfigSaveOnlyOnFailure implements PlaywrightConfig {
    @Override
    public BrowserConfig getBrowserConfig() {
        return new BrowserConfig()
                .chromium()
                .launch()
                .enableTracingOnlyOnFailure();
    }
}
