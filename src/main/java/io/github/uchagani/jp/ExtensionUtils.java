package io.github.uchagani.jp;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;

import java.lang.reflect.InvocationTargetException;

public class ExtensionUtils {
    static final ExtensionContext.Namespace namespace = ExtensionContext.Namespace.create(JunitPlaywright.class);

    static void saveObjectInStore(ExtensionContext extensionContext, String id, Object object) {
        extensionContext.getStore(namespace).put(extensionContext.getUniqueId() + id, object);
    }

    static <T> T getObjectFromStore(ExtensionContext extensionContext, String id, Class<T> objectType) {
        return extensionContext.getStore(namespace).get(extensionContext.getUniqueId() + id, objectType);
    }

    public static RestConfig getRestConfig(ParameterContext parameterContext, ExtensionContext extensionContext) {
        RestConfig config = getObjectFromStore(extensionContext, getRestConfigId(parameterContext, extensionContext), RestConfig.class);

        if (config == null) {
            Class<? extends PlaywrightRestConfig> configClass;
            try {
                configClass = parameterContext.getParameter().getAnnotation(UseRestConfig.class).value();
            } catch (Exception ignored) {
                try {
                    configClass = extensionContext.getRequiredTestMethod().getAnnotation(UseRestConfig.class).value();
                } catch (Exception ignored1) {
                    configClass = extensionContext.getRequiredTestClass().getAnnotation(UseRestConfig.class).value();
                }
            }
            config = createInstanceOfConfig(configClass).getRestConfig();
            saveRestConfigInStore(extensionContext, config);
        }

        return config;
    }

    public static void saveRestConfigInStore(ExtensionContext extensionContext, RestConfig config) {
        saveObjectInStore(extensionContext, getRestConfigId(null, extensionContext), config);
    }

    public static void saveBrowserConfigInStore(ExtensionContext extensionContext, BrowserConfig config) {
        saveObjectInStore(extensionContext, getBrowserConfigId(extensionContext), config);
    }

    public static BrowserConfig getBrowserConfig(ExtensionContext extensionContext) {
        BrowserConfig config = getObjectFromStore(extensionContext, getBrowserConfigId(extensionContext), BrowserConfig.class);
        if (config == null) {
            Class<? extends PlaywrightBrowserConfig> configClass;
            try {
                configClass = extensionContext.getRequiredTestMethod().getAnnotation(UseBrowserConfig.class).value();
            } catch (Exception ignored) {
                configClass = extensionContext.getRequiredTestClass().getAnnotation(UseBrowserConfig.class).value();
            }
            config = createInstanceOfConfig(configClass).getBrowserConfig();
            saveBrowserConfigInStore(extensionContext, config);
        }
        return config;
    }

    private static <T> T createInstanceOfConfig(Class<T> configClass) {
        try {
            return configClass.getConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            String message = String.format("Unable to create instance of %s.  Ensure the class has a public default constructor.", configClass.getName());
            throw new RuntimeException(message, ex);
        }
    }

    private static String getBrowserConfigId(ExtensionContext extensionContext) {
        return extensionContext.getUniqueId() + ".browserConfigId.";
    }

    private static String getRestConfigId(ParameterContext parameterContext, ExtensionContext extensionContext) {
        String id = ".restConfigId.";
        if(parameterContext == null) {
            return extensionContext.getUniqueId() + id;
        }
        return extensionContext.getUniqueId() + id + parameterContext.getIndex();
    }
}
