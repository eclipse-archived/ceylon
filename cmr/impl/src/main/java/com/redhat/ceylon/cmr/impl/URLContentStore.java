/*
 * Copyright 2011 Red Hat inc. and third party contributors as noted 
 * by the author tags.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.redhat.ceylon.cmr.impl;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.xml.bind.DatatypeConverter;

import com.redhat.ceylon.cmr.api.Logger;
import com.redhat.ceylon.cmr.api.ModuleInfo;
import com.redhat.ceylon.cmr.api.ModuleQuery;
import com.redhat.ceylon.cmr.api.ModuleQuery.Type;
import com.redhat.ceylon.cmr.api.ModuleSearchResult;
import com.redhat.ceylon.cmr.api.ModuleVersionArtifact;
import com.redhat.ceylon.cmr.api.ModuleVersionDetails;
import com.redhat.ceylon.cmr.api.ModuleVersionQuery;
import com.redhat.ceylon.cmr.api.ModuleVersionResult;
import com.redhat.ceylon.cmr.spi.ContentHandle;
import com.redhat.ceylon.cmr.spi.Node;
import com.redhat.ceylon.cmr.spi.OpenNode;
import com.redhat.ceylon.cmr.util.WS;
import com.redhat.ceylon.cmr.util.WS.Link;
import com.redhat.ceylon.cmr.util.WS.Parser;
import com.redhat.ceylon.cmr.util.WS.XMLHandler;

/**
 * URL based content store.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class URLContentStore extends AbstractRemoteContentStore {

    public final static String HERD_COMPLETE_MODULES_REL = "http://modules.ceylon-lang.org/rel/complete-modules";
    public final static String HERD_COMPLETE_VERSIONS_REL = "http://modules.ceylon-lang.org/rel/complete-versions";
    public final static String HERD_SEARCH_MODULES_REL = "http://modules.ceylon-lang.org/rel/search-modules";

    private static final String HERD_ORIGIN = "The Herd";
    
    protected final String root;
    protected String username;
    protected String password;
    private Boolean _isHerd = null;
    private Boolean _isLocalMachine = null;
    private String herdCompleteModulesURL;
    private String herdCompleteVersionsURL;
    private String herdSearchModulesURL;
    private String herdRequestedApi;

    protected URLContentStore(String root, Logger log, boolean offline) {
        this(root, log, offline, null);
    }
    protected URLContentStore(String root, Logger log, boolean offline, String apiVersion) {
        super(log, offline);
        if (root == null)
            throw new IllegalArgumentException("Null root url");
        this.root = root;
        this.herdRequestedApi = apiVersion != null ? apiVersion : "2";
        if(apiVersion != null
                && !apiVersion.equals("1")
                && !apiVersion.equals("2"))
            throw new IllegalArgumentException("Only Herd APIs 1 or 2 are supported: requested API "+apiVersion);
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
            // we support both API 1 and 2
            URL rootURL = getURL("?version="+herdRequestedApi);
            HttpURLConnection con = (HttpURLConnection) rootURL.openConnection();
            try{
                con.setRequestMethod("OPTIONS");
                if(con.getResponseCode() != HttpURLConnection.HTTP_OK)
                    return false;
                String herdVersion = con.getHeaderField("X-Herd-Version");
                log.debug("Herd version: "+herdVersion);
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
//        return "localhost".equals(host) || "127.0.0.1".equals(host) || "::1".equals(host);
        Enumeration<NetworkInterface> interfaces;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface nic = interfaces.nextElement();
                Enumeration<InetAddress> addresses = nic.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if (host.equalsIgnoreCase(address.getHostName()) || host.equalsIgnoreCase(address.getHostAddress())) {
                        return true;
                    }
                }
            }
        } catch (SocketException e) {
            // Ignore error
        }
        return false;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public OpenNode find(Node parent, String child) {
        final String path = getFullPath(parent, child);
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

    protected abstract ContentHandle createContentHandle(Node parent, String child, String path, Node node);

    protected String getUrlAsString(Node node) {
        return getUrlAsString(NodeUtils.getFullPath(node, SEPARATOR));
    }

    protected String getUrlAsString(String path) {
        return root + path;
    }

    protected URL getURL(Node node) {
        return getURL(NodeUtils.getFullPath(node, SEPARATOR));
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
    
    protected long lastModified(final URL url) throws IOException {
        HttpURLConnection con = head(url);
        return con != null ? con.getLastModified() : -1;
    }

    protected HttpURLConnection head(final URL url) throws IOException {
        if (connectionAllowed()) {
            final URLConnection conn = url.openConnection();
            if (conn instanceof HttpURLConnection) {
                HttpURLConnection huc = (HttpURLConnection) conn;
                huc.setRequestMethod("HEAD");
                addCredentials(huc);
                int code = huc.getResponseCode();
                huc.disconnect();
                log.debug("Got " + code + " for url: " + url);
                if (code == 200) {
                    return huc;
                }
            }
        }
        return null;
    }

    protected void addCredentials(HttpURLConnection conn) throws IOException {
        if (username != null && password != null) {
            try {
                String authString = DatatypeConverter.printBase64Binary((username + ":" + password).getBytes());
                conn.setRequestProperty("Authorization", "Basic " + authString);
                conn.connect();
            } catch (Exception e) {
                throw new IOException("Cannot set basic authorization.", e);
            }
        }
    }

    @Override
    public void completeModules(final ModuleQuery query, final ModuleSearchResult result) {
        if(connectionAllowed() && isHerd() && herdCompleteModulesURL != null){
            // let's try Herd
            try{
                WS.getXML(herdCompleteModulesURL,
                          WS.params(WS.param("module", query.getName()),
                                    WS.param("type", getHerdTypeParam(query.getType())),
                                    WS.param("binaryMajor", query.getBinaryMajor()),
                                    WS.param("binaryMinor", query.getBinaryMinor())),
                          new XMLHandler(){
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
        switch(type){
        case JS:
            return "javascript";
        case JVM:
            return "jvm";
        case SRC:
            return "source";
        case ALL:
            // TODO Implement retrieval of various types at at time
            return "jvm";
        default:
            throw new RuntimeException("Missing enum case handling");
        }
    }

    @Override
    public void completeVersions(ModuleVersionQuery lookup, final ModuleVersionResult result) {
        if(connectionAllowed() && isHerd() && herdCompleteVersionsURL != null){
            // let's try Herd
            try{
                WS.getXML(herdCompleteVersionsURL,
                          WS.params(WS.param("module", lookup.getName()),
                                    WS.param("version", lookup.getVersion()),
                                    WS.param("type", getHerdTypeParam(lookup.getType())),
                                    WS.param("binaryMajor", lookup.getBinaryMajor()),
                                    WS.param("binaryMinor", lookup.getBinaryMinor())),
                          new XMLHandler(){
                    @Override
                    public void onOK(Parser p) {
                        parseCompleteVersionsResponse(p, result);
                    }
                });
            }catch(Exception x){
                log.info("Failed to get completion of versions from Herd: "+x.getMessage());
            }
        }
    }

    protected void parseCompleteVersionsResponse(Parser p, ModuleVersionResult result) {
        List<String> authors = new LinkedList<String>();
        List<ModuleInfo> dependencies = new LinkedList<ModuleInfo>();
        List<ModuleVersionArtifact> types = new LinkedList<ModuleVersionArtifact>();
        p.moveToOpenTag("results");
        
        while(p.moveToOptionalOpenTag("module-version")){
            String module = null, version = null, doc = null, license = null;
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
                }else if(p.isOpenTag("license")){
                    license = p.contents();
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
            ModuleVersionDetails newVersion = result.addVersion(version);
            if(newVersion != null){
                if(doc != null && !doc.isEmpty())
                    newVersion.setDoc(doc);
                if(license != null && !license.isEmpty())
                    newVersion.setLicense(license);
                if(!authors.isEmpty())
                    newVersion.getAuthors().addAll(authors);
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

    private ModuleInfo parseDependency(Parser p) {
        String dependencyName = null, dependencyVersion = null;
        boolean dependencyShared = false, dependencyOptional = false;
        while(p.moveToOptionalOpenTag()){
            if(p.isOpenTag("module")){
                dependencyName = p.contents();
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
        if(dependencyName == null || dependencyName.isEmpty())
            throw new RuntimeException("Missing required dependency module name");
        if(dependencyVersion == null || dependencyVersion.isEmpty())
            throw new RuntimeException("Missing required dependency module version");
        return new ModuleInfo(dependencyName, dependencyVersion, dependencyOptional, dependencyShared);
    }

    @Override
    public void searchModules(final ModuleQuery query, final ModuleSearchResult result) {
        if(connectionAllowed() && isHerd() && herdSearchModulesURL != null){
            // let's try Herd
            try{
                WS.getXML(herdSearchModulesURL,
                          WS.params(WS.param("query", query.getName()),
                                    WS.param("type", getHerdTypeParam(query.getType())),
                                    WS.param("start", query.getStart()),
                                    WS.param("count", query.getCount()),
                                    WS.param("binaryMajor", query.getBinaryMajor()),
                                    WS.param("binaryMinor", query.getBinaryMinor())),
                          new XMLHandler(){
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
        SortedSet<ModuleInfo> dependencies = new TreeSet<ModuleInfo>();
        SortedSet<String> types = new TreeSet<String>();

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
            String module = null, doc = null, license = null;
            Integer majorBinVer = null, minorBinVer = null;
            authors.clear();
            versions.clear();
            dependencies.clear();
            types.clear();
            resultCount++;
            
            while(p.moveToOptionalOpenTag()){
                if(p.isOpenTag("name")){
                    module = p.contents();
                }else if(p.isOpenTag("versions")){
                    versions.add(p.contents());
                }else if(p.isOpenTag("doc")){
                    doc = p.contents();
                }else if(p.isOpenTag("license")){
                    license = p.contents();
                }else if(p.isOpenTag("authors")){
                    authors.add(p.contents());
                }else if(p.isOpenTag("dependency")){
                    dependencies.add(parseDependency(p));
                }else if(p.isOpenTag("artifact")){
                    ModuleVersionArtifact artifact = parseArtifact(p);
                    if(artifact.getSuffix().equals(".car")){
                        majorBinVer = artifact.getMajorBinaryVersion();
                        minorBinVer = artifact.getMinorBinaryVersion();
                    }
                    types.add(artifact.getSuffix());
                }else{
                    throw new RuntimeException("Unknown tag: "+p.tagName());
                }
            }
            if(module == null || module.isEmpty())
                throw new RuntimeException("Missing required module name");
            if(versions.isEmpty()){
                log.debug("Ignoring result for " + module + " because it doesn't have a single version");
            }else{
                result.addResult(module, doc, license, authors, versions, dependencies, types, majorBinVer, minorBinVer, true, HERD_ORIGIN);
            }
            p.checkCloseTag();
        }
        p.checkCloseTag();
        
        // see if we have more results
        long realStart = start != null ? start : 0;
        long resultsAfterThisPage = realStart + resultCount;
        result.setHasMoreResults(resultsAfterThisPage < totalResults);
    }
}
