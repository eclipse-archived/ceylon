package com.redhat.ceylon.common.config;

import java.io.Console;
import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.security.GeneralSecurityException;
import java.security.UnrecoverableKeyException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.redhat.ceylon.common.config.Keystores.Store;
import com.redhat.ceylon.common.config.Proxies.Proxy;

/**
 * Utility class for making use of {@link Credentials} and {@link Proxies}
 */
public class Authentication {

    public static class UsernamePassword {

        private final String username;
        private final Password password;

        public UsernamePassword(String user, Password p) {
            this.username = user;
            this.password = p;
        }
        
        public String getUser() {
            return username;
        }
        
        public char[] getPassword() {
            return password.getPassword();
        }
    }
    
    /**
     * Provides access to a password
     */
    private interface Password {
        
        /**
         * Gets the password. A {@code char} array is used rather than a 
         * {@code String} because a String would reside in memory until 
         * garbage collected, whereas the caller can zero the returned array 
         * manually once they're finished with it.
         * @return The password
         */
        public char[] getPassword();
    }
    
    /**
     * Implementation of {@link Password} that simply returns a given string
     */
    private static class PlaintextPassword implements Password {
        private final String password;

        private PlaintextPassword(String password) {
            this.password = password;
        }

        public char[] getPassword() {
            return password != null ? password.toCharArray() : null;
        }
    }
    
    /** A way of getting a password interactively from the user */
    public static interface PasswordPrompt {
        public char[] getPassword(String prompt);
    }

    /** Prompts the user for a password on the system console */
    public static class ConsolePasswordPrompt implements PasswordPrompt {

        private final Console console;
        
        public ConsolePasswordPrompt() {
            console = System.console();
            if (console == null) {
                throw new RuntimeException("No console available");
            }
        }
        
        @Override
        public char[] getPassword(String prompt) {
            return console.readPassword("%s: ", prompt);
        }
    }
    
    /**
     * Implementation of {@link Password} which prompts the user for the password 
     * via a pluggable {@link PasswordPrompt}. {@link ConsolePasswordPrompt} 
     * provides an implementation for prompting via the system console. 
     * Applications without access to a system console must provide their own
     * {@link PasswordPrompt}.  
     * @author tom
     */
    private static class PromptedPassword implements Password {
        
        private final String prompt;
        
        public PromptedPassword(String prompt) {
            this.prompt = prompt;
        }
        
        @Override
        public char[] getPassword() {
            return getPasswordPrompt().getPassword(prompt);
        }
    }
    
    /** Implementation of {@link Password} which retrieves a password using 
     * its alias within a given {@link Keystores} 
     */
    private static final class StoredPassword implements Password {
        private final String passwordKeystore;
        private final Keystores.Store store;
        private final String alias;
        private char[] password = null;

        private StoredPassword(String passwordKeystore, Keystores.Store store,
                String alias) {
            this.passwordKeystore = passwordKeystore;
            this.store = store;
            this.alias = alias;
        }
        
        private String msg(String key, Object... args) {
            return ConfigMessages.msg(
                    (passwordKeystore == null ? "keystore.default." :"keystore.named.") + key,
                    args);
        }

        @Override
        public char[] getPassword() {
            if (password != null) {
                // Only prompt for store/entry password once
                return password;
            }
            char[] storePass = null;
            char[] entryPass = null;
            try {
                if (store.getFilename() != null && !store.fileExists()) {
                    throw new RuntimeException(msg("missing", store.getFilename(), passwordKeystore));
                }
                String protection = store.getProtection();
                if (protection.equals("both") || protection.equals("store")) {
                    PromptedPassword storePassword = new PromptedPassword(
                            msg("keystore.password.prompt", store.getFilename(), passwordKeystore));
                    storePass = storePassword.getPassword();
                } else if (protection.equals("entry")) {
                    PromptedPassword entryPassword = new PromptedPassword(
                            msg("entry.password.prompt", store.getFilename(), passwordKeystore));
                    entryPass = entryPassword.getPassword();
                } else if (protection.equals("none")) {
                    // Nothing to do here
                } else {
                    throw new RuntimeException(msg("unknown.protection", store.getFilename(), passwordKeystore, protection));
                }
                if ("both".equals(protection)) {
                    entryPass = storePass;
                }
                password = store.getPassword(alias, storePass, entryPass);
                if (password == null) {
                    throw new RuntimeException(msg("no.alias", store.getFilename(), passwordKeystore, alias));
                }
                return password;
            } catch (UnrecoverableKeyException e) {
                throw new RuntimeException(msg("password.bad", store.getFilename(), passwordKeystore, alias));
            } catch (GeneralSecurityException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                if (storePass != null) {
                    Arrays.fill(storePass, ' ');
                }
                if (entryPass != null) {
                    Arrays.fill(entryPass, ' ');
                }
            }
        }
    }
    
    /**
     * <p>Configures {@code java.net} according to the given Proxy's 
     * settings.</p>
     * <ul>
     * <li>If the Proxy is null then the OS's default 
     *     proxy settings are used if possible by setting the
     *     system property {@code java.net.useSystemProxies} to {@code true}.
     * <li>If the Proxy is has a null nost then no proxy 
     *     is used (the
     *     system property {@code java.net.useSystemProxies} is set to  
     *     {@code false}).</li>
     * <li>If the Proxy and its hosts are not null then an appropriate 
     *     {@code ProxySelector} is installed using 
     *     {@link ProxySelector#setDefault(ProxySelector)}. If the
     *     Proxy has a {@code user} property then an 
     *     appropriate {@link Authenticator} is installed using
     *     {@link Authenticator#setDefault(Authenticator)}
     * </li>
     */
    public void installProxy() {
        if (proxy == null) {
            System.setProperty("java.net.useSystemProxies", "true");
        } if (proxy.getHost() == null) {
            System.setProperty("java.net.useSystemProxies", "false");
        } else {
            ProxySelector.setDefault(getProxySelector());
            Authenticator authenticator = getProxyAuthenticator();
            if (authenticator != null) {
                Authenticator.setDefault(authenticator);
            }
        }
    }
    
    /**
     * Gets a new {@link java.net.Proxy} using the information in the given 
     * Proxy configuration
     * @see #installProxy(Proxy)
     */
    public java.net.Proxy getProxy() {
        if (proxy != null && proxy.getHost() != null) {
            return new java.net.Proxy(java.net.Proxy.Type.valueOf(proxy.getType()), new InetSocketAddress(proxy.getHost(), proxy.getPort()));
        }
        return null;
    }
    
    /**
     * Gets a new {@link java.net.ProxySelector} using the information in the given 
     * Proxy configuration
     * @see #installProxy(Proxy)
     */
    public ProxySelector getProxySelector() {
        ProxySelector selector = null;
        if (proxy != null && proxy.getHost() != null) {
            selector =  new ProxySelector() {
                @Override
                public List<java.net.Proxy> select(URI uri) {
                    String host = uri.getHost();
                    java.net.Proxy netProxy = null;
                    if (proxy.getNonProxyHosts() != null) {
                        for (String nonProxiable : proxy.getNonProxyHosts()) {
                            if (nonProxiable.equals(host)) {
                                netProxy = java.net.Proxy.NO_PROXY;
                            }
                        }
                    }
                    if (netProxy == null) {
                        netProxy = getProxy();
                    }
                    if (netProxy == null) {
                        netProxy = java.net.Proxy.NO_PROXY;
                    }
                    return Collections.singletonList(netProxy);
                }
                @Override
                public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
                    // No nothing
                }
            };
        }
        return selector;
    }
    
    /**
     * Gets a new {@link Authenticator} using the information in the given 
     * Proxy configuration
     * @see #installProxy(Proxy)
     */
    public Authenticator getProxyAuthenticator() {
        Authenticator authenticator = null;
        if (proxy != null 
                && proxy.getCredentials() != null
                && proxy.getCredentials().getUser() != null) {
            authenticator = new Authenticator() {
                public PasswordAuthentication getPasswordAuthentication() {
                    UsernamePassword usernameAndPassword = getUsernameAndPassword(proxy.getCredentials());
                    return new PasswordAuthentication(usernameAndPassword.getUser(), usernameAndPassword.getPassword());
                }
            };
        }
        return authenticator;
    }
    
    public UsernamePassword getUsernameAndPassword(Credentials credentials) {
        Password p;
        if (credentials == null || credentials.getUser() == null) {
            return null;
        }
        if (credentials.getAlias() != null) {
            final String passwordKeystore = credentials.getKeystore();
            final Store store = keystores.getStore(passwordKeystore);
            p = new StoredPassword(passwordKeystore, store, credentials.getAlias());
        } else if (credentials.getUser() != null && credentials.getPassword() == null) {
             p = new PromptedPassword(credentials.getCredentialPrompt());
        } else {
            // else no password, or plain text password
            p = new PlaintextPassword(credentials.getPassword());
        }
        return new UsernamePassword(credentials.getUser(), p);
    }
    
    private static PasswordPrompt passwordPrompt = null;
    
    /**
     * Gets the password prompt for the application, 
     * using a {@link ConsolePasswordPrompt} if not prompt has been configured.
     * @return
     */
    public static synchronized PasswordPrompt getPasswordPrompt() {
        if (passwordPrompt == null) {
            passwordPrompt = new ConsolePasswordPrompt();
        }
        return passwordPrompt;
    }
    
    /**
     * Sets the password prompt
     * @param passwordPrompt
     */
    public static synchronized void setPasswordPrompt(PasswordPrompt passwordPrompt) {
        Authentication.passwordPrompt = passwordPrompt;
    }

    private Keystores keystores;
    private Proxies.Proxy proxy;
    
    public Authentication(Keystores keystores, Proxies.Proxy proxy) {
        this.keystores = keystores;
        this.proxy = proxy;
    }
    
    public static Authentication get() {
        return fromConfig(CeylonConfig.get());
    }
    
    public static Authentication fromConfig(CeylonConfig config) {
        return new Authentication(Keystores.withConfig(config), Proxies.withConfig(config).getProxy());
    }
}
