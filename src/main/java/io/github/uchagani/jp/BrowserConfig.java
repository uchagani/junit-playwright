package io.github.uchagani.jp;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;

import java.nio.file.Path;
import java.nio.file.Paths;

public class BrowserConfig {
    private Playwright.CreateOptions playwrightCreateOptions;
    private BrowserChoice browser;
    private BrowserCreateMethod createMethod;
    private BrowserType.LaunchOptions launchOptions;
    private String wsEndpoint;
    private BrowserType.ConnectOptions connectOptions;
    private String endpointUrl;
    private BrowserType.ConnectOverCDPOptions connectOverCDPOptions;
    private Path userDataDir;
    private BrowserType.LaunchPersistentContextOptions launchPersistentContextOptions;
    private Browser.NewContextOptions newContextOptions;
    private boolean enableTracing = false;
    private boolean saveTraceOnlyOnFailure = false;
    private Path outputDirectory = Paths.get("test-results");

    Path getOutputDirectory() {
        return outputDirectory;
    }

    public BrowserConfig setOutputDirectory(Path outputDirectory) {
        this.outputDirectory = outputDirectory;
        return this;
    }

    boolean getEnableTracing() {
        return enableTracing;
    }

    boolean getSaveTraceOnlyOnFailure() {
        return saveTraceOnlyOnFailure;
    }

    public BrowserConfig enableTracing() {
        enableTracing = true;
        return this;
    }

    public BrowserConfig enableTracingOnlyOnFailure() {
        this.saveTraceOnlyOnFailure = true;
        return enableTracing();
    }

    public BrowserConfig setNewContextOptions(Browser.NewContextOptions options) {
        this.newContextOptions = options;
        return this;
    }

    Browser.NewContextOptions getNewContextOptions() {
        return newContextOptions;
    }

    Playwright.CreateOptions getPlaywrightCreateOptions() {
        return playwrightCreateOptions;
    }

    public BrowserConfig setPlaywrightCreateOptions(Playwright.CreateOptions playwrightCreateOptions) {
        this.playwrightCreateOptions = playwrightCreateOptions;
        return this;
    }

    BrowserChoice getBrowser() {
        return browser;
    }

    public BrowserConfig chromium() {
        this.browser = BrowserChoice.CHROMIUM;
        return this;
    }

    public BrowserConfig firefox() {
        this.browser = BrowserChoice.FIREFOX;
        return this;
    }

    public BrowserConfig webkit() {
        this.browser = BrowserChoice.WEBKIT;
        return this;
    }

    BrowserCreateMethod getCreateMethod() {
        return createMethod;
    }

    String getEndpointUrl() {
        return endpointUrl;
    }

    BrowserType.ConnectOverCDPOptions getConnectOverCDPOptions() {
        return connectOverCDPOptions;
    }

    public BrowserConfig connectOverCDP(String endpointUrl, BrowserType.ConnectOverCDPOptions options) {
        connectOverCDP(endpointUrl);
        this.connectOverCDPOptions = options;
        return this;
    }

    public BrowserConfig connectOverCDP(String endpointUrl) {
        this.endpointUrl = endpointUrl;
        this.createMethod = BrowserCreateMethod.CONNECT_OVER_CDP;
        return this;
    }

    String getWsEndpoint() {
        return wsEndpoint;
    }

    BrowserType.ConnectOptions getConnectOptions() {
        return connectOptions;
    }

    public BrowserConfig connect(String wsEndpoint, BrowserType.ConnectOptions options) {
        connect(wsEndpoint);
        this.connectOptions = options;
        return this;
    }

    public BrowserConfig connect(String wsEndpoint) {
        this.wsEndpoint = wsEndpoint;
        this.createMethod = BrowserCreateMethod.CONNECT;
        return this;
    }

    BrowserType.LaunchOptions getLaunchOptions() {
        return launchOptions;
    }

    public BrowserConfig launch(BrowserType.LaunchOptions options) {
        launch();
        this.launchOptions = options;
        return this;
    }

    public BrowserConfig launch() {
        this.createMethod = BrowserCreateMethod.LAUNCH;
        return this;
    }

    Path getUserDataDir() {
        return userDataDir;
    }

    BrowserType.LaunchPersistentContextOptions getLaunchPersistentContextOptions() {
        return launchPersistentContextOptions;
    }

    public BrowserConfig launchPersistentContext(Path userDataDir) {
        this.userDataDir = userDataDir;
        this.createMethod = BrowserCreateMethod.LAUNCH_PERSISTENT_CONTEXT;
        return this;
    }

    public BrowserConfig launchPersistentContext(Path userDataDir, BrowserType.LaunchPersistentContextOptions options) {
        launchPersistentContext(userDataDir);
        this.launchPersistentContextOptions = options;
        return this;
    }
}
