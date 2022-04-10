package io.github.uchagani.jp;

import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import static io.github.uchagani.jp.AnnotationUtils.isAnnotationPresentOnClassOrMethod;
import static io.github.uchagani.jp.ExtensionUtils.*;

public class PlaywrightParameterResolver implements ParameterResolver {
    static final String id = ".playwright.";

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Class<?> parameterType = parameterContext.getParameter().getType();
        return isAnnotationPresentOnClassOrMethod(extensionContext, UseBrowserConfig.class) && parameterType.equals(Playwright.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return getPlaywright(extensionContext);
    }

    static void closePlaywright(ExtensionContext extensionContext) {
        Playwright playwright = getPlaywright(extensionContext);
        if (playwright != null) {
            playwright.close();
        }
    }

    public static void savePlaywrightInStore(ExtensionContext extensionContext, Playwright playwright) {
        saveObjectInStore(extensionContext, id, playwright);
    }

    public static Playwright getPlaywright(ExtensionContext extensionContext) {
        Playwright playwright = getObjectFromStore(extensionContext, id, Playwright.class);
        if (playwright == null) {
            BrowserConfig config = getBrowserConfig(extensionContext);
            Playwright.CreateOptions options = config.getPlaywrightCreateOptions();
            playwright = createPlaywright(options);
            savePlaywrightInStore(extensionContext, playwright);
        }
        return playwright;
    }

    private static Playwright createPlaywright(Playwright.CreateOptions options) {
        if (options == null) {
            return Playwright.create();
        }
        return Playwright.create(options);
    }
}
