package io.github.uchagani.jp;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import static io.github.uchagani.jp.AnnotationUtils.isAnnotationPresentOnClassOrMethod;
import static io.github.uchagani.jp.ExtensionUtils.getObjectFromStore;
import static io.github.uchagani.jp.ExtensionUtils.saveObjectInStore;

public class PageParameterResolver implements ParameterResolver {
    private static final String id = ".page.";

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Class<?> parameterType = parameterContext.getParameter().getType();
        return isAnnotationPresentOnClassOrMethod(extensionContext, UseBrowserConfig.class) && parameterType.equals(Page.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return getPage(extensionContext);
    }

    public static Page getPage(ExtensionContext extensionContext) {
        Page page = getObjectFromStore(extensionContext, id, Page.class);
        if (page == null) {
            BrowserContext browserContext = BrowserContextParameterResolver.getBrowserContext(extensionContext);
            page = browserContext.newPage();
            savePageInStore(extensionContext, page);
        }
        return page;
    }

    public static void savePageInStore(ExtensionContext extensionContext, Page page) {
        saveObjectInStore(extensionContext, id, page);
    }
}
