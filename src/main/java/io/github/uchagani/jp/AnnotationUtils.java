package io.github.uchagani.jp;

import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.annotation.Annotation;

public class AnnotationUtils {
    static boolean isAnnotationPresentOnClassOrMethod(ExtensionContext extensionContext, Class<? extends Annotation> annotation) {
        return extensionContext.getRequiredTestClass().isAnnotationPresent(annotation) ||
                extensionContext.getRequiredTestMethod().isAnnotationPresent(annotation);
    }
}
