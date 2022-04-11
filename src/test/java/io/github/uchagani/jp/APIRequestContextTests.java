package io.github.uchagani.jp;

import com.microsoft.playwright.APIRequestContext;
import io.github.uchagani.jp.configs.DefaultRestConfig;
import io.github.uchagani.jp.configs.OverrideRestConfig;
import io.github.uchagani.jp.configs.SpecificRestConfig;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@UseRestConfig(DefaultRestConfig.class)
public class APIRequestContextTests {
    @Test
    public void shouldInjectAPIRequestContextDefinedAtClassLevel(APIRequestContext request) {
        assertThat(request).isNotNull();
        assertThatThrownBy(() -> request.get("/foo")).hasMessageContaining("Invalid URL");
    }

    @Test
    @UseRestConfig(OverrideRestConfig.class)
    public void shouldInjectAPIRequestContextDefinedAtMethodLevel(APIRequestContext request) {
        assertThat(request).isNotNull();
        assertThat(request.get("/foo").url()).contains("bing.com/foo");
    }

    @Test
    public void shouldInjectAPIRequestContextDefinedAtParameterLevel(@UseRestConfig(
            SpecificRestConfig.class) APIRequestContext specificRequest, @UseRestConfig(
            OverrideRestConfig.class) APIRequestContext overrideRequest) {
        assertThat(specificRequest).isNotNull();
        assertThat(overrideRequest).isNotNull();
        assertThat(specificRequest.get("/foo").url()).contains("google.com/foo");
        assertThat(overrideRequest.get("/foo").url()).contains("bing.com/foo");
    }
}
