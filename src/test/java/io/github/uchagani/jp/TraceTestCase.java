package io.github.uchagani.jp;

import com.microsoft.playwright.Page;
import io.github.uchagani.jp.configs.TraceConfig;
import io.github.uchagani.jp.configs.TraceConfigAlternateOutputDir;
import io.github.uchagani.jp.configs.TraceConfigSaveOnlyOnFailure;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import static org.assertj.core.api.Assumptions.assumeThat;
import static org.junit.jupiter.api.Assertions.fail;

@Tag("indirect")
public class TraceTestCase {
    @BeforeAll
    public static void clearTestResultsDir() {
        try {
            Files.walk(Paths.get("test-results"))
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (Exception ignored) {
        }

        try {
            Files.walk(Paths.get("test-results-2"))
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (Exception ignored) {
        }
    }

    @Test
    @InjectPlaywright(TraceConfig.class)
    public void traceFile_alwaysCreate_onPass_isCreated(Page ignored) {
        //force pass
        return;
    }

    @Test
    @InjectPlaywright(TraceConfig.class)
    public void traceFile_alwaysCreate_onFail_isCreated(Page ignored) {
        fail("force fail");
    }

    @Test
    @InjectPlaywright(TraceConfig.class)
    public void traceFile_alwaysCreate_onAbort_isCreated(Page ignored) {
        assumeThat(true).isEqualTo(false);
    }

    @Test
    @InjectPlaywright(TraceConfigSaveOnlyOnFailure.class)
    public void traceFile_onPass_isNotCreated(Page ignored) {
        //force pass
        return;
    }

    @Test
    @InjectPlaywright(TraceConfigSaveOnlyOnFailure.class)
    public void traceFile_onAbort_isNotCreated(Page ignored) {
        assumeThat(true).isEqualTo(false);
    }

    @Test
    @InjectPlaywright(TraceConfigSaveOnlyOnFailure.class)
    public void traceFile_onFail_isCreated(Page ignored) {
        fail("force fail");
    }

    @Test
    @InjectPlaywright(TraceConfigAlternateOutputDir.class)
    public void traceFile_inAlternateDir_isCreated() {
        //force pass
        return;
    }

}
