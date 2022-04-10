package io.github.uchagani.jp;

import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.InvocationTargetException;

import static io.github.uchagani.jp.AnnotationUtils.ensureAnnotationIsPresentOnClassOrMethod;

public class ExtensionUtils {
    static final ExtensionContext.Namespace namespace = ExtensionContext.Namespace.create(JunitPlaywright.class);

    static void saveObjectInStore(ExtensionContext extensionContext, String id, Object object) {
        extensionContext.getStore(namespace).put(extensionContext.getUniqueId() + id, object);
    }

    static <T> T getObjectFromStore(ExtensionContext extensionContext, String id, Class<T> objectType) {
        return extensionContext.getStore(namespace).get(extensionContext.getUniqueId() + id, objectType);
    }

    static RestConfig getRestConfig(ExtensionContext extensionContext) {
        ensureAnnotationIsPresentOnClassOrMethod(extensionContext, UseRestConfig.class);
        Class<? extends PlaywrightRestConfig> configClass;

        try {
            configClass = extensionContext.getRequiredTestMethod().getAnnotation(UseRestConfig.class).value();
        } catch (NullPointerException ignored) {
            configClass = extensionContext.getRequiredTestClass().getAnnotation(UseRestConfig.class).value();
        }

        return createInstanceOfConfig(configClass).getRestConfig();
    }

    static BrowserConfig getBrowserConfig(ExtensionContext extensionContext) {
        ensureAnnotationIsPresentOnClassOrMethod(extensionContext, UseBrowserConfig.class);
        Class<? extends PlaywrightBrowserConfig> configClass;

        try {
            configClass = extensionContext.getRequiredTestMethod().getAnnotation(UseBrowserConfig.class).value();
        } catch (NullPointerException ignored) {
            configClass = extensionContext.getRequiredTestClass().getAnnotation(UseBrowserConfig.class).value();
        }

        return createInstanceOfConfig(configClass).getBrowserConfig();
    }

    private static <T> T createInstanceOfConfig(Class<T> configClass) {
        try {
            return configClass.getConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            String message = String.format("Unable to create instance of %s.  Ensure the class has a public default constructor.", configClass.getName());
            throw new RuntimeException(message, ex);
        }
    }


}
