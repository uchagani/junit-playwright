package io.github.uchagani.jp;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Tracing;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class BrowserContextParameterResolver implements ParameterResolver {
    private static final String browserContextId = ".browserContext.";

    public static BrowserContext getBrowserContext(ExtensionContext extensionContext) {
        String id = extensionContext.getUniqueId() + browserContextId;
        BrowserContext browserContext = extensionContext.getStore(PlaywrightParameterResolver.junitPlaywrightNamespace).get(id, BrowserContext.class);

        if (browserContext == null) {
            Browser browser = BrowserParameterResolver.getBrowser(extensionContext);
            BrowserConfig browserConfig = PlaywrightParameterResolver.getPlaywrightConfig(extensionContext).getBrowserConfig();

            if (browserConfig.getCreateMethod() == BrowserCreateMethod.LAUNCH_PERSISTENT_CONTEXT) {
                return getBrowserContext(extensionContext);
            }

            Browser.NewContextOptions newContextOptions = browserConfig.getNewContextOptions();

            if (newContextOptions == null) {
                browserContext = browser.newContext();
            } else {
                browserContext = browser.newContext(newContextOptions);
            }

            if (browserConfig.getEnableTracing()) {
                browserContext.tracing().start(getTraceStartOptions());
            }

            saveBrowserContextInStore(extensionContext, browserContext);
        }

        return browserContext;
    }

    public static void saveBrowserContextInStore(ExtensionContext extensionContext, BrowserContext browserContext) {
        extensionContext.getStore(PlaywrightParameterResolver.junitPlaywrightNamespace).put(extensionContext.getUniqueId() + browserContextId, browserContext);
    }

    private static Tracing.StartOptions getTraceStartOptions() {
        Tracing.StartOptions startOptions = new Tracing.StartOptions()
                .setSnapshots(true)
                .setScreenshots(true);

        if (System.getenv("PLAYWRIGHT_JAVA_SRC") != null) {
            startOptions.setSources(true);
        }

        return startOptions;
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Class<?> parameterType = parameterContext.getParameter().getType();
        return PlaywrightParameterResolver.injectPlaywrightAnnotationPresent(extensionContext) && parameterType.equals(BrowserContext.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return getBrowserContext(extensionContext);
    }
}
