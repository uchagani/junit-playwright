package io.github.uchagani.jp;

import com.microsoft.playwright.APIRequest;

public class RestConfig {
    private APIRequest.NewContextOptions apiRequestContextOptions;

    public APIRequest.NewContextOptions getAPIRequestContextOptions() {
        return apiRequestContextOptions;
    }

    public RestConfig setApiRequestContextOptions(APIRequest.NewContextOptions options) {
        apiRequestContextOptions = options;
        return this;
    }
}
