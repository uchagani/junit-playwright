package io.github.uchagani.jp.configs;

import io.github.uchagani.jp.PlaywrightRestConfig;
import io.github.uchagani.jp.RestConfig;

public class DefaultRestConfig implements PlaywrightRestConfig {
    @Override
    public RestConfig getRestConfig() {
        return new RestConfig();
    }
}
