package io.github.uchagani.jp;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;

import java.lang.annotation.Annotation;

public class AnnotationUtils {
    static boolean isAnnotationPresentOnClassOrMethod(ExtensionContext extensionContext, Class<? extends Annotation> annotation) {
        return extensionContext.getRequiredTestClass().isAnnotationPresent(annotation) ||
                extensionContext.getRequiredTestMethod().isAnnotationPresent(annotation);
    }

    static void ensureAnnotationIsPresentOnClassOrMethod(ExtensionContext extensionContext, Class<? extends Annotation> annotation) {
        if (!isAnnotationPresentOnClassOrMethod(extensionContext, annotation)) {
            String message = String.format("Class or Method is not annotated with %s", annotation.getName());
            throw new RuntimeException(message);
        }
    }

    static void ensureAnnotationIsPresentOnClassOrMethodOrParameter(ParameterContext parameterContext, ExtensionContext extensionContext, Class<? extends Annotation> annotation) {
        if (!isAnnotationPresentOnClassOrMethod(extensionContext, annotation) && !parameterContext.isAnnotated(annotation)) {
            String message = String.format("Class or Method or Parameter is not annotated with %s", annotation.getName());
            throw new RuntimeException(message);
        }
    }
}
