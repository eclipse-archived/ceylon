/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.cmr.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.xml.bind.DatatypeConverter;

import org.eclipse.ceylon.cmr.api.ArtifactContext;
import org.eclipse.ceylon.cmr.api.ModuleDependencyInfo;
import org.eclipse.ceylon.cmr.api.ModuleInfo;
import org.eclipse.ceylon.cmr.api.ModuleQuery;
import org.eclipse.ceylon.cmr.api.ModuleSearchResult;
import org.eclipse.ceylon.cmr.api.ModuleVersionArtifact;
import org.eclipse.ceylon.cmr.api.ModuleVersionDetails;
import org.eclipse.ceylon.cmr.api.ModuleVersionQuery;
import org.eclipse.ceylon.cmr.api.ModuleVersionResult;
import org.eclipse.ceylon.cmr.api.Overrides;
import org.eclipse.ceylon.cmr.api.ModuleQuery.Retrieval;
import org.eclipse.ceylon.cmr.api.ModuleQuery.Type;
import org.eclipse.ceylon.cmr.spi.ContentHandle;
import org.eclipse.ceylon.cmr.spi.Node;
import org.eclipse.ceylon.cmr.spi.OpenNode;
import org.eclipse.ceylon.cmr.spi.SizedInputStream;
import org.eclipse.ceylon.cmr.util.WS;
import org.eclipse.ceylon.cmr.util.WS.Link;
import org.eclipse.ceylon.cmr.util.WS.Parser;
import org.eclipse.ceylon.cmr.util.WS.XMLHandler;
import org.eclipse.ceylon.common.Constants;
import org.eclipse.ceylon.common.ModuleUtil;
import org.eclipse.ceylon.common.log.Logger;

/**
 * URL based content store.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class URLContentStore extends AbstractRemoteContentStore {

    // Those are namespaces, they don't need to use https until Herd changes those namespaces too
    public final static String HERD_COMPLETE_MODULES_REL = "http://modules.ceylon-lang.org/rel/complete-modules";
    public final static String HERD_COMPLETE_VERSIONS_REL = "http://modules.ceylon-lang.org/rel/complete-versions";
    public final static String HERD_SEARCH_MODULES_REL = "http://modules.ceylon-lang.org/rel/search-modules";

    private static final String HERD_ORIGIN = "The Herd";
    
    protected final String root;
    protected final Proxy proxy;
    private final String herdRequestedApi;
    
    protected String username;
    protected String password;
    private Boolean _isHerd = null;
    private Boolean _isLocalMachine = null;
    private String herdCompleteModulesURL;
    private String herdCompleteVersionsURL;
    private String herdSearchModulesURL;
    
    private static int HERD_V1 = 1;
    private static int HERD_V2 = 2;
    private static int HERD_V3 = 3;
    private static int HERD_V4 = 4;
    private static int HERD_V5 = 5;
    private static int HERD_V6 = 6;

    private static int HERD_LATEST = HERD_V6;

    private int herdVersion = HERD_V1; // assume 1 until we find otherwise

    protected URLContentStore(String root, Logger log, boolean offline, int timeout, Proxy proxy) {
        this(root, log, offline, timeout, proxy, null);
    }
    
    protected URLContentStore(String root, Logger log, boolean offline, int timeout, Proxy proxy, String apiVersion) {
        super(log, offline, timeout);
        if (root == null)
            throw new IllegalArgumentException("Null root url");
        this.root = root;
        this.proxy = proxy;
        this.herdRequestedApi = apiVersion != null ? apiVersion : String.valueOf(HERD_LATEST);
        if(apiVersion != null
                && !apiVersion.equals(String.valueOf(HERD_V1))
                && !apiVersion.equals(String.valueOf(HERD_V2))
                && !apiVersion.equals(String.valueOf(HERD_V3))
                && !apiVersion.equals(String.valueOf(HERD_V4))
                && !apiVersion.equals(String.valueOf(HERD_V5))
                && !apiVersion.equals(String.valueOf(HERD_V6)))
            throw new IllegalArgumentException("Only Herd APIs 1 to "+HERD_LATEST+" are supported: requested API "+apiVersion);
    }

    @Override
    public boolean isHerd(){
        if(_isHerd == null){
            synchronized(this){
                if(_isHerd == null){
                    _isHerd = testHerd();
                }
            }
        }
        return _isHerd;
    }

    private boolean testHerd() {
        if (!connectionAllowed()) {
            // We should never come here, but just in case
            return false;
        }
        try{
            // we support both API 1 to 5
            URL rootURL = getURL("?version="+herdRequestedApi);
            HttpURLConnection con;
            if (proxy != null) {
                con = (HttpURLConnection) rootURL.openConnection(proxy);
            } else {
                con = (HttpURLConnection) rootURL.openConnection();
            }
            try{
                con.setConnectTimeout(timeout);
                con.setReadTimeout(timeout * Constants.READ_TIMEOUT_MULTIPLIER);
                con.setRequestMethod("OPTIONS");
                if(con.getResponseCode() != HttpURLConnection.HTTP_OK)
                    return false;
                String herdVersion = con.getHeaderField("X-Herd-Version");
                log.debug("Herd version: "+herdVersion);
                try{
                    this.herdVersion = Integer.parseInt(herdVersion);
                }catch(NumberFormatException x){
                    log.debug("Non-integer Herd version: "+herdVersion);
                }
                boolean ret = herdVersion != null && !herdVersion.isEmpty();
                if(ret){
                    collectHerdLinks(con);
                }
                return ret;
            }finally{
                con.disconnect();
            }
        }catch(Exception x){
            log.debug("Failed to determine if remote host is a Herd repo: "+x.getMessage());
            return false;
        }
    }

    private void collectHerdLinks(HttpURLConnection con) {
        // collect the links
        try{
            List<Link> links = WS.collectLinks(con);
            herdCompleteModulesURL = WS.getLink(links, HERD_COMPLETE_MODULES_REL);
            herdCompleteVersionsURL = WS.getLink(links, HERD_COMPLETE_VERSIONS_REL);
            herdSearchModulesURL = WS.getLink(links, HERD_SEARCH_MODULES_REL);
            log.debug("Got complete-modules link: " + herdCompleteModulesURL);
            log.debug("Got complete-versions link: " + herdCompleteVersionsURL);
            log.debug("Got search-modules link: " + herdSearchModulesURL);
        }catch(Exception x){
            log.debug("Failed to read links from Herd repo: "+x.getMessage());
        }
    }

    
    protected boolean connectionAllowed() {
        return !offline || rootIsLocalMachine();
    }
    
    private boolean rootIsLocalMachine() {
        if (_isLocalMachine == null) {
            URL url = getURL("");
            _isLocalMachine = hostIsLocalMachine(url.getHost());
        }
        return _isLocalMachine;
    }
    
    private boolean hostIsLocalMachine(String host) {
        return "localhost".equals(host) || "127.0.0.1".equals(host) || "::1".equals(host);
//        Enumeration<NetworkInterface> interfaces;
//        try {
//            interfaces = NetworkInterface.getNetworkInterfaces();
//            while (interfaces.hasMoreElements()) {
//                NetworkInterface nic = interfaces.nextElement();
//                Enumeration<InetAddress> addresses = nic.getInetAddresses();
//                while (addresses.hasMoreElements()) {
//                    InetAddress address = addresses.nextElement();
//                    if (host.equalsIgnoreCase(address.getHostName()) || host.equalsIgnoreCase(address.getHostAddress())) {
//                        return true;
//                    }
//                }
//            }
//        } catch (SocketException e) {
//            // Ignore error
//        }
//        return false;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public OpenNode find(Node parent, String child) {
        final String path = compatiblePath(getFullPath(parent, child));
        // only test the URL if we are looking at the child level
        // otherwise, pretend that folders exist, we'll find out soon
        // enough
        if (hasContent(child) && !urlExists(path)) {
            return null;
        }
        final RemoteNode node = createNode(child);
        ContentHandle handle;
        if (hasContent(child))
            handle = createContentHandle(parent, child, path, node);
        else
            handle = DefaultNode.HANDLE_MARKER;
        node.setHandle(handle);
        return node;
    }

    protected String compatiblePath(String fullPath) {
        if (isHerd() && herdVersion == HERD_V3) {
            // For version 3 herd we transform requests for foo/1/module-doc.zip[.sha1]
            // to foo/1/foo-1.doc.zip[sha1] for backwards compatibility
            if (fullPath.endsWith(ArtifactContext.SHA1)) {
                return compatiblePath(fullPath.substring(0, fullPath.length() - 5)) + ArtifactContext.SHA1;
            }
            if (fullPath.endsWith(ArtifactContext.DOCS + ArtifactContext.ZIP)) {
                fullPath = fullPath.substring(0, fullPath.length() - 15);
                String[] parts = fullPath.split("\\/");
                String name = parts[1];
                for (int i = 2; i < parts.length - 1; i++) {
                    name += "." + parts[i];
                }
                String version = parts[parts.length - 1];
                fullPath = fullPath + "/" + name + "-" + version + ".doc.zip";
            }
        }
        return fullPath;
    }
    
    protected abstract ContentHandle createContentHandle(Node parent, String child, String path, Node node);

    protected String getUrlAsString(Node node) {
        return getUrlAsString(compatiblePath(NodeUtils.getFullPath(node, SEPARATOR)));
    }

    protected String getUrlAsString(String path) {
        return root + path;
    }

    protected URL getURL(Node node) {
        return getURL(compatiblePath(NodeUtils.getFullPath(node, SEPARATOR)));
    }

    protected URL getURL(String path) {
        try {
            return new URL(root + path);
        } catch (Exception e) {
            log.warning("Cannot create URL: " + e);
            return null;
        }
    }

    protected boolean urlExists(String path) {
        return urlExists(getURL(path));
    }

    protected boolean urlExists(Node node) {
        return urlExists(getURL(node));
    }

    protected abstract boolean urlExists(URL url);

    @Override
    public String getDisplayString() {
        String name = root;
        if (!connectionAllowed()) {
            name += " (offline)";
        }
        return name;
    }
    
    class Attempts {
        /** The total number of attempts (including the initial one) */
        private final int attempts = 3;
        private int reattemptsLeft = attempts-1;
        public boolean reattempt() {
            return reattemptsLeft-- > 0;
        }
        /** The total number of attempts to be made (including the initial one) */
        public int getAttemptsAllowed() {
            return attempts;
        }
        /** The total number of reattempts not yet made */
        public int getReattemptsLeft() {
            return reattemptsLeft;
        }
        /** The total number of attempts made so far*/
        public int getAttemptsMade() {
            return getAttemptsAllowed()-getReattemptsLeft();
        }
        /**
         * For selected exceptions returns normally if there are 
         * attempts left, otherwise rethrows the given exception. 
         */
        public void giveup(String phase, URL url, IOException e) throws IOException{
            if (e instanceof SocketTimeoutException
                    || e instanceof SocketException) {
                if (reattempt()) {
                    log.debug("Retry download of "+ url + " after " + e + " (" + getReattemptsLeft() + " reattempts left)");
                    return;
                }
            }
            if (e instanceof SocketTimeoutException) {
                // Include url in exception message
                SocketTimeoutException newException = new SocketTimeoutException("Timed out while " +phase+" "+url);
                newException.initCause(e);
                e = newException;
            }
            log.debug("Giving up request to " + url + " (after "+ getAttemptsMade() + " attempts) due to: " + e );
            throw e;
            
        }
    }
    
    protected final long lastModified(final URL url) throws IOException {
        Attempts a = new Attempts();
        while (true) {
            try {
                HttpURLConnection con = head(url);
                return con != null ? con.getLastModified() : -1;
            } catch (IOException e) {
                a.giveup("last modified of", url, e);
            }
        }
    }

    protected final long size(final URL url) throws IOException {
        Attempts a = new Attempts();
        while (true) {
            try {
                HttpURLConnection con = head(url);
                return con != null ? con.getContentLength() : -1;
            } catch (IOException e) {
                a.giveup("size of", url, e);
            }
        }
    }

    protected final HttpURLConnection head(final URL url) throws IOException {
        if (connectionAllowed()) {
            final URLConnection conn;
            if (proxy != null) {
                conn = url.openConnection(proxy);
            } else {
                conn = url.openConnection();
            }
            if (conn instanceof HttpURLConnection) {
                HttpURLConnection huc = (HttpURLConnection) conn;
                huc.setConnectTimeout(timeout);
                huc.setReadTimeout(timeout * Constants.READ_TIMEOUT_MULTIPLIER);
                huc.setRequestMethod("HEAD");
                addCredentials(huc);
                conn.connect();
                int code = huc.getResponseCode();
                log.debug("Connect: " + huc.getHeaderField("Connection"));
                huc.disconnect();
                log.debug("Got " + code + " for url: " + url);
                if (code == 200) {
                    return huc;
                }
            }
        }
        return null;
    }

    /**
     * Adds the {@code Authorization} request header for HTTP basic authentication
     */
    protected void addCredentials(HttpURLConnection conn) throws IOException {
        if (username != null && password != null) {
            try {
                String authString = DatatypeConverter.printBase64Binary((username + ":" + password).getBytes());
                conn.setRequestProperty("Authorization", "Basic " + authString);
            } catch (Exception e) {
                throw new IOException("Cannot set basic authorization.", e);
            }
        }
    }

    @Override
    public boolean canHandleFolders() {
        return !isHerd();
    }

    @Override
    public boolean isSearchable() {
        return connectionAllowed() && isHerd();
    }

    @Override
    public void completeModules(final ModuleQuery query, final ModuleSearchResult result, Overrides overrides) {
        // this is only for non-Maven modules
        if(ModuleUtil.isMavenModule(query.getName()))
            return;
        if(connectionAllowed() && isHerd() && herdCompleteModulesURL != null){
            // let's try Herd
            try{
                List<WS.Param> params = new ArrayList<WS.Param>(10);
                params.add(WS.param("module", query.getName()));
                params.add(WS.param("type", getHerdTypeParam(query.getType())));
                // not strictly correct, but we have to do something about old herds
                params.add(WS.param("binaryMajor", query.getJvmBinaryMajor()));
                params.add(WS.param("binaryMinor", query.getJvmBinaryMinor()));
                if (herdVersion < HERD_V4 && query.getMemberName() != null && !query.getMemberName().isEmpty()) {
                    // Earlier version of the Herd didn't support member searches so let's not pretend they did
                    return;
                }
                if (herdVersion >= HERD_V4) {
                    params.add(WS.param("memberName", query.getMemberName()));
                    params.add(WS.param("memberSearchPackageOnly", query.isMemberSearchPackageOnly()));
                    params.add(WS.param("memberSearchExact", query.isMemberSearchExact()));
                    params.add(WS.param("retrieval", getHerdRetrievalParam(query.getRetrieval())));
                }
                if (herdVersion >= HERD_V5) {
                    params.add(WS.param("jvmBinaryMajor", query.getJvmBinaryMajor()));
                    params.add(WS.param("jvmBinaryMinor", query.getJvmBinaryMinor()));
                    params.add(WS.param("jsBinaryMajor", query.getJsBinaryMajor()));
                    params.add(WS.param("jsBinaryMinor", query.getJsBinaryMinor()));
                }
                WS.getXML(herdCompleteModulesURL, params, new XMLHandler(){
                    @Override
                    public void onOK(Parser p) {
                        parseSearchModulesResponse(p, result, query.getStart());
                    }
                });
            }catch(Exception x){
                log.info("Failed to get completion of modules from Herd: "+x.getMessage());
            }
        }
    }

    private String getHerdTypeParam(Type type) {
        if (herdVersion >= HERD_V4) {
            String[] suffixes = type.getSuffixes();
            StringBuilder arts = new StringBuilder(suffixes[0]);
            for (int i = 1; i < suffixes.length; i++) {
                arts.append(",");
                arts.append(suffixes[i]);
            }
            return arts.toString();
        } else {
            switch(type){
            case JS:
                return "javascript";
            case CAR:
                return "jvm";
            case JAR:
                return "jvm";
            case JVM:
                return "jvm";
            case SRC:
                return "source";
            case CODE:
                if(herdVersion >= HERD_V3)
                    return "code";
                else
                    return "all";
            case CEYLON_CODE:
                if(herdVersion >= HERD_V3)
                    return "code";
                else
                    return "all";
            case ALL:
                // TODO Implement retrieval of various types at at time
                return "all";
            default:
                throw new RuntimeException("Missing enum case handling");
            }
        }
    }

    private String getHerdRetrievalParam(Retrieval retrieval) {
        switch (retrieval) {
        case ANY:
            return "any";
        case ALL:
            return "all";
        default:
            throw new RuntimeException("Missing enum case handling");
        }
    }

    @Override
    public void completeVersions(ModuleVersionQuery query, final ModuleVersionResult result, final Overrides overrides) {
        // this is only for non-Maven modules
        if(ModuleUtil.isMavenModule(query.getName()))
            return;
        if(connectionAllowed() && isHerd() && herdCompleteVersionsURL != null){
            // let's try Herd
            try{
                List<WS.Param> params = new ArrayList<WS.Param>(10);
                params.add(WS.param("module", query.getName()));
                params.add(WS.param("version", query.getVersion()));
                params.add(WS.param("type", getHerdTypeParam(query.getType())));
                // not strictly correct, but we have to do something about old herds
                params.add(WS.param("binaryMajor", query.getJvmBinaryMajor()));
                params.add(WS.param("binaryMinor", query.getJvmBinaryMinor()));
                if (herdVersion < HERD_V4 && query.getMemberName() != null && !query.getMemberName().isEmpty()) {
                    // Earlier version of the Herd didn't support member searches so let's not pretend they did
                    return;
                }
                if (herdVersion >= HERD_V4) {
                    params.add(WS.param("memberName", query.getMemberName()));
                    params.add(WS.param("memberSearchPackageOnly", query.isMemberSearchPackageOnly()));
                    params.add(WS.param("memberSearchExact", query.isMemberSearchExact()));
                    params.add(WS.param("retrieval", getHerdRetrievalParam(query.getRetrieval())));
                }
                if (herdVersion >= HERD_V5) {
                    params.add(WS.param("jvmBinaryMajor", query.getJvmBinaryMajor()));
                    params.add(WS.param("jvmBinaryMinor", query.getJvmBinaryMinor()));
                    params.add(WS.param("jsBinaryMajor", query.getJsBinaryMajor()));
                    params.add(WS.param("jsBinaryMinor", query.getJsBinaryMinor()));
                }
                WS.getXML(herdCompleteVersionsURL, params, new XMLHandler(){
                    @Override
                    public void onOK(Parser p) {
                        parseCompleteVersionsResponse(p, result, overrides);
                    }
                });
            }catch(Exception x){
                log.info("Failed to get completion of versions from Herd: "+x.getMessage());
            }
        }
    }

    protected void parseCompleteVersionsResponse(Parser p, ModuleVersionResult result, Overrides overrides) {
        List<String> authors = new LinkedList<String>();
        Set<ModuleDependencyInfo> dependencies = new HashSet<ModuleDependencyInfo>();
        List<ModuleVersionArtifact> types = new LinkedList<ModuleVersionArtifact>();
        p.moveToOpenTag("results");
        
        while(p.moveToOptionalOpenTag("module-version")){
            String module = null, version = null, doc = null, license = null, label = null, groupId = null, artifactId = null;
            authors.clear();
            dependencies.clear();
            types.clear();
            
            while(p.moveToOptionalOpenTag()){
                if(p.isOpenTag("module")){
                    // ignored
                    module = p.contents();
                }else if(p.isOpenTag("version")){
                    version = p.contents();
                }else if(p.isOpenTag("doc")){
                    doc = p.contents();
                }else if(p.isOpenTag("groupId")){
                    groupId = p.contents();
                }else if(p.isOpenTag("artifactId")){
                    artifactId = p.contents();
                }else if(p.isOpenTag("license")){
                    license = p.contents();
                }else if(p.isOpenTag("label")){
                    label = p.contents();
                }else if(p.isOpenTag("authors")){
                    authors.add(p.contents());
                }else if(p.isOpenTag("dependency")){
                    dependencies.add(parseDependency(p));
                }else if(p.isOpenTag("artifact")){
                    types.add(parseArtifact(p));
                }else{
                    throw new RuntimeException("Unknown tag: "+p.tagName());
                }
            }
            if(version == null || version.isEmpty())
                throw new RuntimeException("Missing required version");
            ModuleVersionDetails newVersion = result.addVersion(null, module, version);
            if(newVersion != null){
                if(groupId != null && !groupId.isEmpty())
                    newVersion.setGroupId(groupId);
                if(artifactId != null && !artifactId.isEmpty())
                    newVersion.setArtifactId(artifactId);
                if(doc != null && !doc.isEmpty())
                    newVersion.setDoc(doc);
                if(license != null && !license.isEmpty())
                    newVersion.setLicense(license);
                if(label != null && !label.isEmpty())
                    newVersion.setLabel(label);
                if(!authors.isEmpty())
                    newVersion.getAuthors().addAll(authors);
                if(overrides != null) {
                    final ModuleInfo info = new ModuleInfo(null, module, version, 
                            groupId, artifactId, null, null, dependencies);
                    dependencies = overrides.applyOverrides(module, version, info).getDependencies();
                }
                if(!dependencies.isEmpty())
                    newVersion.getDependencies().addAll(dependencies);
                if(!types.isEmpty())
                    newVersion.getArtifactTypes().addAll(types);
                newVersion.setRemote(true);
                if (isHerd()) {
                    newVersion.setOrigin(HERD_ORIGIN + " (" + getDisplayString() + ")");
                } else {
                    newVersion.setOrigin(getDisplayString());
                }
            }
            p.checkCloseTag();
        }
        p.checkCloseTag();
    }

    private ModuleVersionArtifact parseArtifact(Parser p) {
        String suffix = null;
        Integer binaryMajor = null, binaryMinor = null;
        while(p.moveToOptionalOpenTag()){
            if(p.isOpenTag("suffix")){
                suffix = p.contents();
            }else if(p.isOpenTag("binaryMajorVersion")){
                binaryMajor = parseInt(p.contents(), "binaryMajorVersion");
            }else if(p.isOpenTag("binaryMinorVersion")){
                binaryMinor = parseInt(p.contents(), "binaryMinorVersion");
            }else{
                throw new RuntimeException("Unknown tag: "+p.tagName());
            }
        }
        if(suffix == null || suffix.isEmpty())
            throw new RuntimeException("Missing required artifact suffix");
        return new ModuleVersionArtifact(suffix, binaryMajor, binaryMinor);
    }

    private int parseInt(String string, String errorTag) {
        try{
            return Integer.parseInt(string);
        }catch(NumberFormatException x){
            throw new RuntimeException("Invalid "+errorTag+" value: "+string);
        }
    }

    private ModuleDependencyInfo parseDependency(Parser p) {
        String dependencyUri = null, dependencyVersion = null;
        boolean dependencyShared = false, dependencyOptional = false;
        while(p.moveToOptionalOpenTag()){
            if(p.isOpenTag("module")){
                dependencyUri = p.contents();
            }else if(p.isOpenTag("version")){
                dependencyVersion = p.contents();
            }else if(p.isOpenTag("shared")){
                dependencyShared = p.contents().equals("true");
            }else if(p.isOpenTag("optional")){
                dependencyOptional = p.contents().equals("true");
            }else if(p.isOpenTag("maven")){
                // ignore for now
                p.contents();
            }else{
                throw new RuntimeException("Unknown tag: "+p.tagName());
            }
        }
        if(dependencyUri == null || dependencyUri.isEmpty())
            throw new RuntimeException("Missing required dependency module name");
        if(dependencyVersion == null || dependencyVersion.isEmpty())
            throw new RuntimeException("Missing required dependency module version");
        String dependencyNamespace = ModuleUtil.getNamespaceFromUri(dependencyUri);
        String dependencyName = ModuleUtil.getModuleNameFromUri(dependencyUri);
        return new ModuleDependencyInfo(dependencyNamespace, dependencyName, dependencyVersion, dependencyOptional, dependencyShared);
    }

    @Override
    public void searchModules(final ModuleQuery query, final ModuleSearchResult result, Overrides overrides) {
        // this is only for non-Maven modules
        if(ModuleUtil.isMavenModule(query.getName()))
            return;
        if(connectionAllowed() && isHerd() && herdSearchModulesURL != null){
            // let's try Herd
            try{
                List<WS.Param> params = new ArrayList<WS.Param>(10);
                params.add(WS.param("query", query.getName()));
                params.add(WS.param("type", getHerdTypeParam(query.getType())));
                params.add(WS.param("start", query.getStart()));
                params.add(WS.param("count", query.getCount()));
                // not strictly correct, but we have to do something about old herds
                params.add(WS.param("binaryMajor", query.getJvmBinaryMajor()));
                params.add(WS.param("binaryMinor", query.getJvmBinaryMinor()));
                if (herdVersion < HERD_V4 && query.getMemberName() != null && !query.getMemberName().isEmpty()) {
                    // Earlier version of the Herd didn't support member searches so let's not pretend they did
                    return;
                }
                if (herdVersion >= HERD_V4) {
                    params.add(WS.param("memberName", query.getMemberName()));
                    params.add(WS.param("memberSearchPackageOnly", query.isMemberSearchPackageOnly()));
                    params.add(WS.param("memberSearchExact", query.isMemberSearchExact()));
                    params.add(WS.param("retrieval", getHerdRetrievalParam(query.getRetrieval())));
                }
                if (herdVersion >= HERD_V5) {
                    params.add(WS.param("jvmBinaryMajor", query.getJvmBinaryMajor()));
                    params.add(WS.param("jvmBinaryMinor", query.getJvmBinaryMinor()));
                    params.add(WS.param("jsBinaryMajor", query.getJsBinaryMajor()));
                    params.add(WS.param("jsBinaryMinor", query.getJsBinaryMinor()));
                }
                WS.getXML(herdSearchModulesURL, params, new XMLHandler(){
                    @Override
                    public void onOK(Parser p) {
                        parseSearchModulesResponse(p, result, query.getStart());
                    }
                });
            }catch(Exception x){
                log.info("Failed to search modules from Herd: "+x.getMessage());
            }
        }
    }

    protected void parseSearchModulesResponse(Parser p, ModuleSearchResult result, Long start) {
        SortedSet<String> authors = new TreeSet<String>();
        SortedSet<String> versions = new TreeSet<String>();
        SortedSet<ModuleDependencyInfo> dependencies = new TreeSet<ModuleDependencyInfo>();
        SortedSet<ModuleVersionArtifact> types = new TreeSet<ModuleVersionArtifact>();

        p.moveToOpenTag("results");
        String total = p.getAttribute("total");
        long totalResults;
        try{
            if(total == null)
                throw new RuntimeException("Missing total from result");
            totalResults = Long.parseLong(total);
        }catch(NumberFormatException x){
            throw new RuntimeException("Invalid total: "+total);
        }
        int resultCount = 0;
        while(p.moveToOptionalOpenTag("module")){
            String module = null, doc = null, license = null, label = null, groupId = null, artifactId = null;
            authors.clear();
            versions.clear();
            dependencies.clear();
            types.clear();
            resultCount++;
            
            while(p.moveToOptionalOpenTag()){
                if(p.isOpenTag("name")){
                    module = p.contents();
                }else if(p.isOpenTag("versions")){
                    // TODO This isn't really the way, we should have version tags
                    // inside the "module" tag containing all the rest of the
                    // information below
                    versions.add(p.contents());
                }else if(p.isOpenTag("groupId")){
                    groupId = p.contents();
                }else if(p.isOpenTag("artifactId")){
                    artifactId = p.contents();
                }else if(p.isOpenTag("doc")){
                    doc = p.contents();
                }else if(p.isOpenTag("license")){
                    license = p.contents();
                }else if(p.isOpenTag("label")){
                    label = p.contents();
                }else if(p.isOpenTag("authors")){
                    authors.add(p.contents());
                }else if(p.isOpenTag("dependency")){
                    dependencies.add(parseDependency(p));
                }else if(p.isOpenTag("artifact")){
                    ModuleVersionArtifact artifact = parseArtifact(p);
                    types.add(artifact);
                }else{
                    throw new RuntimeException("Unknown tag: "+p.tagName());
                }
            }
            if(module == null || module.isEmpty())
                throw new RuntimeException("Missing required module name");
            if(versions.isEmpty()){
                log.debug("Ignoring result for " + module + " because it doesn't have a single version");
            }else{
                // TODO See TODO above
                for (String v : versions) {
                    ModuleVersionDetails mvd = new ModuleVersionDetails(null, module, v, groupId, artifactId);
                    mvd.setDoc(doc);
                    mvd.setLicense(license);
                    mvd.setLabel(label);
                    mvd.getAuthors().addAll(authors);
                    mvd.getDependencies().addAll(dependencies);
                    mvd.getArtifactTypes().addAll(types);
                    mvd.setRemote(true);
                    if (isHerd()) {
                        mvd.setOrigin(HERD_ORIGIN + " (" + getDisplayString() + ")");
                    } else {
                        mvd.setOrigin(getDisplayString());
                    }
                    result.addResult(module, mvd);
                }
            }
            p.checkCloseTag();
        }
        p.checkCloseTag();
        
        // see if we have more results
        long realStart = start != null ? start : 0;
        long resultsAfterThisPage = realStart + resultCount;
        result.setHasMoreResults(resultsAfterThisPage < totalResults);
    }
    
    @SuppressWarnings("serial")
    static class NotGettable extends RuntimeException {
    }
    
    /**
     * A {@link SizedInputStream} that can reconnect some number f times
     */
    class RetryingSizedInputStream extends SizedInputStream {
        
        private final URL url;
        private final Proxy proxy;
        /**
         * The connection timeout for each request
         */
        private final int timeout;
        /** 
         * Whether range requests should be made when 
         * the {@link ReconnectingInputStream} has to reconnect.
         */
        private boolean rangeRequests;
        /** The number of attempts to download the resource */
        private final Attempts attempts = new Attempts();
        /** The <em>current</em> stream: Gets mutated when {@link ReconnectingInputStream} reconnects */
        
        private HttpURLConnection connection = null;
        private InputStream stream = null;
        long bytesRead = 0;
        private final ReconnectingInputStream reconnectingStream;
        private final long contentLength;
        
        public RetryingSizedInputStream(URL url, Proxy proxy, int timeout) throws NotGettable, IOException {
            super(null, 0);
            this.url = url;
            this.proxy = proxy;
            this.timeout = timeout;
            long length = 0;
            connecting: while (true) {
                try{
                    connection = makeConnection(url, -1);
                    int code = connection.getResponseCode();
                    if (code != -1 && code != 200) {
                        log.info("Got " + code + " for url: " + url);
                        NotGettable notGettable = new NotGettable();
                        cleanUpStreams(notGettable);
                        throw notGettable;
                    }
                    String acceptRange = connection.getHeaderField("Accept-Range");
                    rangeRequests = acceptRange == null || !acceptRange.equalsIgnoreCase("none");
                    debug("Connection: "+connection.getHeaderField("Connection"));
                    debug("Got " + code + " for url: " + url);
                    length = connection.getContentLengthLong();
                    stream = connection.getInputStream();
                    break connecting;
                } catch(IOException connectException) {
                    maybeRetry(url, connectException, "connecting to");
                }
            }
            this.contentLength = length;
            this.reconnectingStream = new ReconnectingInputStream();
        }
    
        protected void maybeRetry(URL url, IOException e, String phase) throws IOException {
            cleanUpStreams(e);
            attempts.giveup(phase, url, e);
        }
    
        /**
         * According to https://docs.oracle.com/javase/8/docs/technotes/guides/net/http-keepalive.html
         * we should read the error stream so the connection can be reused.
         */
        protected void cleanUpStreams(Exception inflight) {
            if (stream != null) {
                try {
                    stream.close();
                    stream = null;
                } catch (IOException closeException) {
                    inflight.addSuppressed(closeException);
                }
            }
            
            if (connection != null) {
                byte[] buf = new byte[8*2014];
                InputStream es = connection.getErrorStream();
                if (es != null) {
                    try {
                        try {
                            while (es.read(buf) > 0) {}
                        } finally {
                            es.close();
                        }
                    } catch (IOException errorStreamError) {
                        inflight.addSuppressed(errorStreamError);
                    }
                }
            }
        }
    
        private void debug(String s) {
            log.debug("    " + s);
        }
        
        protected HttpURLConnection makeConnection(URL url, long start)
                throws IOException, SocketTimeoutException, NotGettable {
            URLConnection conn;
            if (proxy != null) {
                conn = url.openConnection(proxy);
            } else {
                conn = url.openConnection();
            }   
            if (!(conn instanceof HttpURLConnection)) {
                throw new NotGettable();
            }
            HttpURLConnection huc = (HttpURLConnection)conn;
            huc.setConnectTimeout(timeout);
            huc.setReadTimeout(timeout * Constants.READ_TIMEOUT_MULTIPLIER);
            boolean useRangeRequest = start > 0;
            if (useRangeRequest) {
                String range = "bytes "+start+"-";
                debug("Using Range request for" + range + " of " + url);
                huc.setRequestProperty("Range", range);
            }
            addCredentials(huc);
            debug("Connecting to " + url);
            conn.connect();
            return huc;
        }
        
        public long getSize() {
            return contentLength;
        }
        
        public InputStream getInputStream() {
            return reconnectingStream;
        }
        
        /**
         * An InputStream that can reconnects on SocketTimeoutException.
         * If it reconnects it makes a {@code Range} request to get just the 
         * remainder of the resource, unless {@link #rangeRequests} is false.
         */
        class ReconnectingInputStream extends InputStream {
            public void close() throws IOException {
                if (stream != null) {
                    stream.close();
                }
            }
            
            public int read(byte[] buf, int offset, int length) throws IOException {
                /*
                 * Overridden because {@link InputStream#read(byte[], int, int)}
                 * behaves badly wrt non-initial {@link #read()}s throwing.
                 */
                while (true) {
                    try {
                        int result = stream.read(buf, offset, length);
                        if (result != -1) {
                            bytesRead+=result;
                        }
                        return result;
                    } catch (IOException readException) {
                        recover(readException);
                    }
                }
            }
            
            @Override
            public int read() throws IOException {
                while (true) {
                    try {
                        int result = stream.read();
                        if (result != -1) {
                            bytesRead++;
                        }
                        return result;
                    } catch (IOException readException) {
                        recover(readException);
                    }
                }
            }
            
            /**
             * Reconnects, reassigning {@link RetryingSizedInputStream#connection} 
             * and {@link RetryingSizedInputStream#stream}, or 
             * throws {@code IOException} if we can't retry.
             */
            protected void recover(IOException readException) throws IOException {
                maybeRetry(url, readException, "reading from");
                // if we maybeRetry didn't propage the exception let's retry...
                reconnect: while (true) {
                    try {
                        // otherwise open another connection...
                        // using a range request unless initial request had Accept-Ranges: none
                        connection = makeConnection(url, rangeRequests ? bytesRead : -1);
                        final int code = connection.getResponseCode();
                        debug("Got " + code + " for reconnection to url: " + url);
                        if (rangeRequests && code == 206) {
                            stream = connection.getInputStream();
                        } else if (code == 200) {
                            if (rangeRequests) {
                                debug("Looks like " + url.getHost() + ":" + url.getPort() + " does support range request, to reading first " + bytesRead + " bytes");
                            }
                            // we didn't make a range request
                            // (or the server didn't understand the Range header)
                            // so spool the appropriate number of bytes
                            stream = connection.getInputStream();
                            try {
                                for (long ii = 0; ii < bytesRead; ii++) {
                                    stream.read();
                                }
                            } catch (IOException spoolException) {
                                maybeRetry(url, spoolException, "spooling");
                                continue reconnect;
                            }
                        } else {
                            throw new IOException("Got HTTP status code " + code + " on reconnect");
                        }
                        debug("Reconnected to url: " + url);
                        break reconnect;
                    } catch (IOException reconnectionException) {
                        maybeRetry(url, reconnectionException, "reconnecting to");
                    }
                }
            }
            
    
        }
    }
}
