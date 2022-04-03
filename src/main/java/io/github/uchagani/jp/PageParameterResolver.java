package io.github.uchagani.jp;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class PageParameterResolver implements ParameterResolver {
    private static final String pageId = ".page.";

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Class<?> parameterType = parameterContext.getParameter().getType();
        return PlaywrightParameterResolver.injectPlaywrightAnnotationPresent(extensionContext) && parameterType.equals(Page.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return getPage(extensionContext);
    }

    public static Page getPage(ExtensionContext extensionContext) {
        String id = extensionContext.getUniqueId() + pageId;
        Page page = extensionContext.getStore(PlaywrightParameterResolver.junitPlaywrightNamespace).get(id, Page.class);

        if (page == null) {
            BrowserContext browserContext = BrowserContextParameterResolver.getBrowserContext(extensionContext);
            page = browserContext.newPage();
            savePageInStore(extensionContext, page);
        }

        return page;
    }

    public static void savePageInStore(ExtensionContext extensionContext, Page page) {
        extensionContext.getStore(PlaywrightParameterResolver.junitPlaywrightNamespace).put(extensionContext.getUniqueId() + pageId, page);
    }
}
