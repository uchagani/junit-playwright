package io.github.uchagani.jp;

class EmptyRestConfig implements PlaywrightRestConfig {
    @Override
    public RestConfig getRestConfig() {
        return new RestConfig();
    }
}
