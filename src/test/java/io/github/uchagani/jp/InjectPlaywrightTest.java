package io.github.uchagani.jp;

import com.microsoft.playwright.Browser;
import org.junit.jupiter.api.Test;

@InjectPlaywright(TestConfig.class)
public class InjectPlaywrightTest {

    @Test
    public void test(Browser browser) {

    }

}
