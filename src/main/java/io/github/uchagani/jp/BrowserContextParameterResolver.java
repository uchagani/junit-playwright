package io.github.uchagani.jp;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Tracing;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import static io.github.uchagani.jp.AnnotationUtils.isAnnotationPresentOnClassOrMethod;
import static io.github.uchagani.jp.ExtensionUtils.*;

public class BrowserContextParameterResolver implements ParameterResolver {
    private static final String id = ".browserContext.";

    public static BrowserContext getBrowserContext(ExtensionContext extensionContext) {
        BrowserContext browserContext = getObjectFromStore(extensionContext, id, BrowserContext.class);

        if (browserContext == null) {
            Browser browser = BrowserParameterResolver.getBrowser(extensionContext);
            BrowserConfig browserConfig = getBrowserConfig(extensionContext);

            if (browserConfig.getCreateMethod() == BrowserCreateMethod.LAUNCH_PERSISTENT_CONTEXT) {
                return getBrowserContext(extensionContext);
            }

            browserContext = createBrowserContext(browser, browserConfig);

            if (browserConfig.getEnableTracing()) {
                browserContext.tracing().start(getTraceStartOptions());
            }

            saveBrowserContextInStore(extensionContext, browserContext);
        }

        return browserContext;
    }

    public static void saveBrowserContextInStore(ExtensionContext extensionContext, BrowserContext browserContext) {
        saveObjectInStore(extensionContext, id, browserContext);
    }

    private static BrowserContext createBrowserContext(Browser browser, BrowserConfig config) {
        Browser.NewContextOptions newContextOptions = config.getNewContextOptions();
        if (newContextOptions == null) {
            return browser.newContext();
        } else {
            return browser.newContext(newContextOptions);
        }
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
        return isAnnotationPresentOnClassOrMethod(extensionContext, UseBrowserConfig.class) && parameterType.equals(BrowserContext.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return getBrowserContext(extensionContext);
    }
}
