package io.github.uchagani.jp;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import static io.github.uchagani.jp.AnnotationUtils.isAnnotationPresentOnClassOrMethod;
import static io.github.uchagani.jp.ExtensionUtils.*;

public class APIRequestContextParameterResolver implements ParameterResolver {
    private static final String id = ".apiRequestContext.";

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Class<?> parameterType = parameterContext.getParameter().getType();
        return parameterType.equals(APIRequestContext.class) && (parameterContext.isAnnotated(UseRestConfig.class)
                || isAnnotationPresentOnClassOrMethod(extensionContext, UseRestConfig.class));
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return getAPIRequestContext(parameterContext, extensionContext);
    }

    public static void closeAPIRequestContext(ExtensionContext extensionContext) {
        APIRequestContext apiRequestContext = getObjectFromStore(extensionContext, id, APIRequestContext.class);
        if (apiRequestContext != null) {
            apiRequestContext.dispose();
        }
    }

    private static Playwright getPlaywright(ExtensionContext extensionContext) {
        try {
            return PlaywrightParameterResolver.getPlaywright(extensionContext);
        } catch (Exception ex) {
            Playwright playwright = Playwright.create();
            PlaywrightParameterResolver.savePlaywrightInStore(extensionContext, playwright);
            return playwright;
        }
    }

    public static APIRequestContext getAPIRequestContext(ParameterContext parameterContext, ExtensionContext extensionContext) {
        APIRequestContext apiRequestContext = getObjectFromStore(extensionContext, id, APIRequestContext.class);
        if (apiRequestContext == null) {
            RestConfig restConfig = getRestConfig(parameterContext, extensionContext);
            Playwright playwright = getPlaywright(extensionContext);
            apiRequestContext = createAPIRequestContext(playwright, restConfig);
            saveAPIRequestContextInStore(extensionContext, apiRequestContext);
        }
        return apiRequestContext;
    }

    public static void saveAPIRequestContextInStore(ExtensionContext extensionContext, APIRequestContext apiRequestContext) {
        saveObjectInStore(extensionContext, id, apiRequestContext);
    }

    private static APIRequestContext createAPIRequestContext(Playwright playwright, RestConfig restConfig) {
        APIRequest.NewContextOptions options = restConfig.getAPIRequestContextOptions();
        if (options == null) {
            return playwright.request().newContext();
        }
        return playwright.request().newContext(options);
    }
}
