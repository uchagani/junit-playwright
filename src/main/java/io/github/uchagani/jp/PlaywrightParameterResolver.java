package io.github.uchagani.jp;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.extension.*;

import java.lang.reflect.InvocationTargetException;

public class PlaywrightParameterResolver implements ParameterResolver {
    static final ExtensionContext.Namespace junitPlaywrightNamespace = ExtensionContext.Namespace.create(PlaywrightParameterResolver.class);
    static final String playwrightId = ".playwright.";

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Class<?> parameterType = parameterContext.getParameter().getType();
        return injectPlaywrightAnnotationPresent(extensionContext) && parameterType.equals(Playwright.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return getPlaywright(extensionContext);
    }

    public static boolean injectPlaywrightAnnotationPresent(ExtensionContext extensionContext) {
        return extensionContext.getRequiredTestClass().isAnnotationPresent(InjectPlaywright.class) ||
                extensionContext.getRequiredTestMethod().isAnnotationPresent(InjectPlaywright.class);
    }

    static void closePlaywright(ExtensionContext extensionContext) {
        Playwright playwright = PlaywrightParameterResolver.getPlaywright(extensionContext);

        if (playwright != null) {
            playwright.close();
        }
    }

    public static void savePlaywrightInStore(ExtensionContext extensionContext, Playwright playwright) {
        extensionContext.getStore(junitPlaywrightNamespace).put(extensionContext.getUniqueId() + playwrightId, playwright);
    }

    public static Playwright getPlaywright(ExtensionContext extensionContext) {
        String id = extensionContext.getUniqueId() + playwrightId;
        Playwright playwright = extensionContext.getStore(junitPlaywrightNamespace).get(id, Playwright.class);

        if (playwright == null) {
            PlaywrightConfig config = getPlaywrightConfig(extensionContext);
            Playwright.CreateOptions options = config.getBrowserConfig().getPlaywrightCreateOptions();
            if (options == null) {
                playwright = Playwright.create();
            } else {
                playwright = Playwright.create(options);
            }
            savePlaywrightInStore(extensionContext, playwright);
        }

        return playwright;
    }

    static PlaywrightConfig getPlaywrightConfig(ExtensionContext extensionContext) {
        PlaywrightConfig config = null;

        if (extensionContext.getRequiredTestClass().isAnnotationPresent(InjectPlaywright.class)) {
            Class<? extends PlaywrightConfig> clazz = extensionContext.getRequiredTestClass().getAnnotation(InjectPlaywright.class).value();
            if (clazz != null) {
                try {
                    config = clazz.getConstructor().newInstance();
                } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
                    throw new RuntimeException("Unable to create an instance of " + clazz.getName(), ex);
                }
            }
        }

        if (extensionContext.getRequiredTestMethod().isAnnotationPresent(InjectPlaywright.class)) {
            Class<? extends PlaywrightConfig> clazz = extensionContext.getRequiredTestMethod().getAnnotation(InjectPlaywright.class).value();
            if (clazz != null) {
                try {
                    config = clazz.getConstructor().newInstance();
                } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
                    throw new RuntimeException("Unable to create an instance of " + clazz.getName(), ex);
                }
            }
        }

        return config;
    }
}


