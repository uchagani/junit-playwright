package io.github.uchagani.jp.configs;

import com.microsoft.playwright.APIRequest;
import io.github.uchagani.jp.PlaywrightRestConfig;
import io.github.uchagani.jp.RestConfig;

public class OverrideRestConfig implements PlaywrightRestConfig {
    @Override
    public RestConfig getRestConfig() {
        return new RestConfig()
                .setApiRequestContextOptions(new APIRequest.NewContextOptions().setBaseURL("https://bing.com"));
    }
}
