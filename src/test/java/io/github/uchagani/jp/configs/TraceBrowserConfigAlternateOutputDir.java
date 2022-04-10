package io.github.uchagani.jp.configs;

import io.github.uchagani.jp.BrowserConfig;
import io.github.uchagani.jp.PlaywrightBrowserConfig;

import java.nio.file.Paths;

public class TraceBrowserConfigAlternateOutputDir implements PlaywrightBrowserConfig {
    @Override
    public BrowserConfig getBrowserConfig() {
        return new BrowserConfig()
                .chromium()
                .launch()
                .enableTracing()
                .setOutputDirectory(Paths.get("test-results-2"));
    }
}
