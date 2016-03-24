package com.redhat.ceylon.common.config;


public class Proxies {

    public static class Proxy {
        private String host;
        private int port;
        private String[] nonProxyHosts;
        private String type;
        private Credentials credentials;
        
        public Proxy(String host, int port, String type, 
                String[] nonProxyHosts,
                Credentials credentials) {
            super();
            this.host = host;
            this.port = port;
            this.type = type;
            this.nonProxyHosts = nonProxyHosts;
            this.credentials = credentials;
        }
        /** The hostname of the proxy */
        public String getHost() {
            return host;
        }
        /** The port number of the proxy */
        public int getPort() {
            return port;
        }
        /** The type of the proxy, see {@link java.net.Proxy.Type} */
        public String getType() {
            return type;
        }
        /** The hostnames of hosts which should not be accessed using the proxy */
        public String[] getNonProxyHosts() {
            return nonProxyHosts;
        }
        public Credentials getCredentials() {
            return credentials;
        }
    }

    private final CeylonConfig config;
    
    private Proxies() {
        this(CeylonConfig.get());
    }
    
    private Proxies(CeylonConfig config) {
        this.config = config;
    }
    
    private static Proxies instance;
    
    public static Proxies get() {
        if (instance == null) {
            instance = new Proxies();
        }
        return instance;
    }
    
    public static void set(Proxies proxies) {
        instance = proxies;
    }
    
    public static Proxies withConfig(CeylonConfig config) {
        return new Proxies(config);
    }
    
    /**
     * Gets the {@code [proxy]} configuration
     */
    public Proxy getProxy() {
        String host = config.getOption(proxyKey(ITEM_HOST));
        long port = config.getNumberOption(proxyKey(ITEM_PORT), 8080);
        String type = config.getOption(proxyKey(ITEM_TYPE), DEFAULT_TYPE);
        String[] nonProxyHosts = config.getOptionValues(proxyKey(ITEM_NON_PROXY_HOSTS));
        String user = config.getOption(proxyKey(ITEM_USER));
        String password = config.getOption(proxyKey(ITEM_PASSWORD));
        String ks = config.getOption(proxyKey(ITEM_KEYSTORE));
        String alias = config.getOption(proxyKey(ITEM_ALIAS));
        String prompt = ConfigMessages.msg("proxy.password.prompt", host, String.valueOf(port), type);
        Credentials credentials = Credentials.create(user, password, ks, alias, prompt);
        return new Proxy(host, (int)port, type, nonProxyHosts, credentials);
    }
    
    private static final String SECTION_PROXY = "proxy";
    private static final String ITEM_HOST = "host";
    private static final String ITEM_PORT = "port";
    private static final String ITEM_TYPE = "type";
    private static final String DEFAULT_TYPE = "HTTP";
    private static final String ITEM_NON_PROXY_HOSTS = "non-proxy-hosts";
    private static final String ITEM_USER = "user";
    private static final String ITEM_PASSWORD = "password";
    private static final String ITEM_KEYSTORE = "password-keystore";
    private static final String ITEM_ALIAS = "password-alias";
    
    private String proxyKey(String itemName) {
        return SECTION_PROXY + "." + itemName;
    }
    
}
