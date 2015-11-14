package com.redhat.ceylon.common.test;

import java.io.File;
import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.ProxySelector;
import java.net.URI;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.redhat.ceylon.common.config.Authentication;
import com.redhat.ceylon.common.config.CeylonConfig;
import com.redhat.ceylon.common.config.Proxies;
import com.redhat.ceylon.common.config.Proxies.Proxy;

public class ProxiesTest extends AbstractKeystoreTest {

    private MockPasswordPrompt mockPrompt;

    @Before
    public void before() {
        this.mockPrompt = new MockPasswordPrompt();
        Authentication.setPasswordPrompt(mockPrompt);
        Authenticator.setDefault(null);
        ProxySelector.setDefault(null);
    }
    
    @After
    public void after() {
        this.mockPrompt = null;
        Authentication.setPasswordPrompt(null);
        Authenticator.setDefault(null);
        ProxySelector.setDefault(null);
        System.setProperty("java.net.useSystemProxies", "false");
    }

    public Proxies getProxies(String configName) throws IOException {
        CeylonConfig testConfig = loadTestConfig(configName);
        return getProxies(testConfig);
    }

    private Proxies getProxies(CeylonConfig testConfig) {
        Proxies proxies = Proxies.withConfig(testConfig);
        return proxies;
    }

    private CeylonConfig loadTestConfig(String configName) throws IOException {
        String filename = "test/src/com/redhat/ceylon/common/test/" + configName;
        CeylonConfig testConfig = loadConfig(filename);
        return testConfig;
    }
    
    @Test
    public void testProxy() throws Exception {
        CeylonConfig testConfig = loadTestConfig("proxy.config");
        Proxies proxies = getProxies(testConfig);
        Proxy proxy = proxies.getProxy();
        Assert.assertEquals("myproxy", proxy.getHost());
        Assert.assertEquals(1234, proxy.getPort());
        Authentication auth = Authentication.fromConfig(testConfig);
        Assert.assertNull(auth.getProxyAuthenticator());
        
        auth.installProxy();
        Assert.assertEquals(null, auth.getProxyAuthenticator());
        InetSocketAddress address = (InetSocketAddress)auth.getProxy().address();
        Assert.assertEquals("myproxy", address.getHostName());
        Assert.assertEquals(1234, address.getPort());
        List<java.net.Proxy> selectedProxies = auth.getProxySelector().select(URI.create("anything"));
        Assert.assertEquals(1, selectedProxies.size());
        Assert.assertEquals(java.net.Proxy.Type.HTTP, selectedProxies.get(0).type());
        mockPrompt.assertSeenOnlyGivenPrompts();
    }
    
    @Test
    public void testProxyWithNonProxyHosts() throws Exception {
        CeylonConfig testConfig = loadTestConfig("proxy+non-proxy-hosts.config");
        Proxies proxies = getProxies(testConfig);
        Proxy proxy = proxies.getProxy();
        Assert.assertEquals("myproxy", proxy.getHost());
        Assert.assertEquals(1234, proxy.getPort());
        Authentication auth = Authentication.fromConfig(testConfig);
        Assert.assertNull(auth.getProxyAuthenticator());
        
        auth.installProxy();
        Assert.assertEquals(null, auth.getProxyAuthenticator());
        InetSocketAddress address = (InetSocketAddress)auth.getProxy().address();
        Assert.assertEquals("myproxy", address.getHostName());
        Assert.assertEquals(1234, address.getPort());
        List<java.net.Proxy> selectedProxies = auth.getProxySelector().select(URI.create("http://anything"));
        Assert.assertEquals(1, selectedProxies.size());
        Assert.assertEquals(java.net.Proxy.Type.HTTP, selectedProxies.get(0).type());
        Assert.assertEquals("myproxy", ((InetSocketAddress)selectedProxies.get(0).address()).getHostName());
        Assert.assertEquals(1234, ((InetSocketAddress)selectedProxies.get(0).address()).getPort());
        
        selectedProxies = auth.getProxySelector().select(URI.create("http://foo"));
        Assert.assertEquals(1, selectedProxies.size());
        Assert.assertEquals(java.net.Proxy.Type.DIRECT, selectedProxies.get(0).type());
        Assert.assertNull(selectedProxies.get(0).address());
        mockPrompt.assertSeenOnlyGivenPrompts();
    }
    
    @Test
    public void testProxyWithUser() throws Exception {
        CeylonConfig testConfig = loadTestConfig("proxy+user.config");
        Proxies proxies = getProxies(testConfig);
        Proxy proxy = proxies.getProxy();
        Assert.assertEquals("myproxy", proxy.getHost());
        Assert.assertEquals(1234, proxy.getPort());
        Authentication auth = Authentication.fromConfig(testConfig);
        auth.installProxy();
        Assert.assertNotNull(auth.getProxyAuthenticator());
        this.mockPrompt.prompts.put("Password for HTTP proxy myproxy:1234: ", "wibble");
        PasswordAuthentication up =Authenticator.requestPasswordAuthentication(null, 80, "ceylon-lang.org", "Hello, world", "fnar");
        Assert.assertEquals("me", up.getUserName());
        Assert.assertEquals("wibble", new String(up.getPassword()));
        InetSocketAddress address = (InetSocketAddress)auth.getProxy().address();
        Assert.assertEquals("myproxy", address.getHostName());
        Assert.assertEquals(1234, address.getPort());
        List<java.net.Proxy> selectedProxies = auth.getProxySelector().select(URI.create("anything"));
        Assert.assertEquals(1, selectedProxies.size());
        Assert.assertEquals(java.net.Proxy.Type.HTTP, selectedProxies.get(0).type());
        Assert.assertEquals("myproxy", ((InetSocketAddress)selectedProxies.get(0).address()).getHostName());
        Assert.assertEquals(1234, ((InetSocketAddress)selectedProxies.get(0).address()).getPort());
        mockPrompt.assertSeenOnlyGivenPrompts();
    }
    
    @Test
    public void testProxyWithPassword() throws Exception {
        CeylonConfig testConfig = loadTestConfig("proxy+password.config");
        Proxies proxies = getProxies(testConfig);
        Proxy proxy = proxies.getProxy();
        Assert.assertEquals("myproxy", proxy.getHost());
        Assert.assertEquals(1234, proxy.getPort());
        Authentication auth = Authentication.fromConfig(testConfig);
        auth.installProxy();
        Assert.assertNotNull(auth.getProxyAuthenticator());
        PasswordAuthentication up = Authenticator.requestPasswordAuthentication(null, 80, "ceylon-lang.org", "Hello, world", "fnar");
        Assert.assertEquals("me", up.getUserName());
        Assert.assertEquals("mypassword", new String(up.getPassword()));
        InetSocketAddress address = (InetSocketAddress)auth.getProxy().address();
        Assert.assertEquals("myproxy", address.getHostName());
        Assert.assertEquals(1234, address.getPort());
        List<java.net.Proxy> selectedProxies = auth.getProxySelector().select(URI.create("anything"));
        Assert.assertEquals(1, selectedProxies.size());
        Assert.assertEquals(java.net.Proxy.Type.HTTP, selectedProxies.get(0).type());
        Assert.assertEquals("myproxy", ((InetSocketAddress)selectedProxies.get(0).address()).getHostName());
        Assert.assertEquals(1234, ((InetSocketAddress)selectedProxies.get(0).address()).getPort());
        // Check there were no prompts
        mockPrompt.assertSeenOnlyGivenPrompts();
    }
    
    @Test
    public void testProxyWithAlias() throws Exception {
        CeylonConfig testConfig = loadTestConfig("proxy+alias.config");
        createStore(testConfig, new File("keystore"), null).setPassword("proxy-password", "ffff".toCharArray(), "mypassword".toCharArray());
        Proxies proxies = getProxies(testConfig);
        Proxy proxy = proxies.getProxy();
        Assert.assertEquals("myproxy", proxy.getHost());
        Assert.assertEquals(1234, proxy.getPort());
        Authentication auth = Authentication.fromConfig(testConfig);
        Assert.assertNotNull(auth.getProxyAuthenticator());
        
        auth.installProxy();
        mockPrompt.prompts.put("Password for default keystore (keystore):", "ffff");
        PasswordAuthentication up = Authenticator.requestPasswordAuthentication(null, 80, "ceylon-lang.org", "Hello, world", "fnar");
        Assert.assertEquals("me", up.getUserName());
        Assert.assertEquals("mypassword", new String(up.getPassword()));
        InetSocketAddress address = (InetSocketAddress)auth.getProxy().address();
        Assert.assertEquals("myproxy", address.getHostName());
        Assert.assertEquals(1234, address.getPort());
        List<java.net.Proxy> selectedProxies = auth.getProxySelector().select(URI.create("anything"));
        Assert.assertEquals(1, selectedProxies.size());
        Assert.assertEquals(java.net.Proxy.Type.HTTP, selectedProxies.get(0).type());
        Assert.assertEquals("myproxy", ((InetSocketAddress)selectedProxies.get(0).address()).getHostName());
        Assert.assertEquals(1234, ((InetSocketAddress)selectedProxies.get(0).address()).getPort());
        // Check there were no prompts
        mockPrompt.assertSeenOnlyGivenPrompts();
    }
    
    @Test
    public void testProxyNone() throws Exception {
        CeylonConfig testConfig = loadTestConfig("proxy-none.config");
        createStore(testConfig, new File("keystore"), null).setPassword("proxy-password", "ffff".toCharArray(), "mypassword".toCharArray());
        Proxies proxies = getProxies(testConfig);
        Proxy proxy = proxies.getProxy();
        Assert.assertNull(proxy.getHost());
        Assert.assertEquals(8080, proxy.getPort());
        Authentication auth = Authentication.fromConfig(testConfig);
        Assert.assertNull(auth.getProxyAuthenticator());
        
        auth.installProxy();
        Assert.assertNull(auth.getProxyAuthenticator());
        
        PasswordAuthentication up = Authenticator.requestPasswordAuthentication(null, 80, "ceylon-lang.org", "Hello, world", "fnar");
        Assert.assertNull(up);
        
        Assert.assertNull(auth.getProxy());
        Assert.assertNull(auth.getProxySelector());
        // Check there were no prompts
        mockPrompt.assertSeenOnlyGivenPrompts();
    }
    
    // TODO Extends AuthenticationTest to cover proxy (i.e. auth proxy with multiple auth repos)
    // TODO Refactor char[] -> String
    
}
