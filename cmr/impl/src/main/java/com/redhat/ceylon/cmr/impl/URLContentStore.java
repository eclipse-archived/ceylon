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

import com.redhat.ceylon.cmr.api.Logger;
import com.redhat.ceylon.cmr.api.ModuleQuery;
import com.redhat.ceylon.cmr.api.ModuleResult;
import com.redhat.ceylon.cmr.api.ModuleSearchResult;
import com.redhat.ceylon.cmr.api.ModuleVersionDetails;
import com.redhat.ceylon.cmr.api.ModuleVersionQuery;
import com.redhat.ceylon.cmr.api.ModuleVersionResult;
import com.redhat.ceylon.cmr.api.ModuleQuery.Type;
import com.redhat.ceylon.cmr.spi.ContentHandle;
import com.redhat.ceylon.cmr.spi.Node;
import com.redhat.ceylon.cmr.spi.OpenNode;
import com.redhat.ceylon.cmr.util.WS;
import com.redhat.ceylon.cmr.util.WS.Link;
import com.redhat.ceylon.cmr.util.WS.Parser;
import com.redhat.ceylon.cmr.util.WS.XMLHandler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.xml.bind.DatatypeConverter;

/**
 * URL based content store.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class URLContentStore extends AbstractRemoteContentStore {

    public final static String HERD_COMPLETE_MODULES_REL = "http://modules.ceylon-lang.org/rel/complete-modules";
    public final static String HERD_COMPLETE_VERSIONS_REL = "http://modules.ceylon-lang.org/rel/complete-versions";
    public final static String HERD_SEARCH_MODULES_REL = "http://modules.ceylon-lang.org/rel/search-modules";

    protected final String root;
    protected String username;
    protected String password;
    private Boolean _isHerd = null;
    private String herdCompleteModulesURL;
    private String herdCompleteVersionsURL;
    private String herdSearchModulesURL;

    protected URLContentStore(String root, Logger log) {
        super(log);
        if (root == null)
            throw new IllegalArgumentException("Null root url");
        this.root = root;
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
        try{
            URL rootURL = getURL("");
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
        return root;
    }
    
    protected long lastModified(final URL url) throws IOException {
        HttpURLConnection con = head(url);
        return con != null ? con.getLastModified() : -1;
    }

    protected HttpURLConnection head(final URL url) throws IOException {
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
    public void completeModules(ModuleQuery lookup, final ModuleResult result) {
        if(isHerd() && herdCompleteModulesURL != null){
            // let's try Herd
            try{
                WS.getXML(herdCompleteModulesURL,
                          WS.params(WS.param("module", lookup.getName()),
                                    WS.param("type", getHerdTypeParam(lookup.getType()))),
                          new XMLHandler(){
                    @Override
                    public void onOK(Parser p) {
                        parseCompleteModulesResponse(p, result);
                    }
                });
            }catch(Exception x){
                log.info("Failed to get completion of modules from Herd: "+x.getMessage());
            }
        }
    }

    protected void parseCompleteModulesResponse(Parser p, ModuleResult result) {
        p.moveToOpenTag("results");
        while(p.moveToOptionalOpenTag("module")){
            String module = p.contents();
            result.addResult(module);
        }
        p.checkCloseTag();
    }

    private String getHerdTypeParam(Type type) {
        switch(type){
        case JS:
            return "javascript";
        case JVM:
            return "jvm";
        case SRC:
            return "source";
        default:
            throw new RuntimeException("Missing enum case handling");
        }
    }

    @Override
    public void completeVersions(ModuleVersionQuery lookup, final ModuleVersionResult result) {
        if(isHerd() && herdCompleteVersionsURL != null){
            // let's try Herd
            try{
                WS.getXML(herdCompleteVersionsURL,
                          WS.params(WS.param("module", lookup.getName()),
                                    WS.param("version", lookup.getVersion()),
                                    WS.param("type", getHerdTypeParam(lookup.getType()))),
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
        p.moveToOpenTag("results");
        
        while(p.moveToOptionalOpenTag("module-version")){
            String module = null, version = null, doc = null, license = null;
            authors.clear();
            
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
            }
            p.checkCloseTag();
        }
        p.checkCloseTag();
    }

    @Override
    public void searchModules(ModuleQuery query, final ModuleSearchResult result) {
        if(isHerd() && herdSearchModulesURL != null){
            // let's try Herd
            try{
                WS.getXML(herdSearchModulesURL,
                          WS.params(WS.param("query", query.getName()),
                                    WS.param("type", getHerdTypeParam(query.getType())),
                                    WS.param("start", query.getStart()),
                                    WS.param("count", query.getCount())),
                          new XMLHandler(){
                    @Override
                    public void onOK(Parser p) {
                        parseSearchModulesResponse(p, result);
                    }
                });
            }catch(Exception x){
                log.info("Failed to search modules from Herd: "+x.getMessage());
            }
        }
    }

    protected void parseSearchModulesResponse(Parser p, ModuleSearchResult result) {
        SortedSet<String> authors = new TreeSet<String>();
        SortedSet<String> versions = new TreeSet<String>();

        p.moveToOpenTag("results");
        while(p.moveToOptionalOpenTag("module")){
            String module = null, doc = null, license = null;
            authors.clear();
            versions.clear();
            
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
                }else{
                    throw new RuntimeException("Unknown tag: "+p.tagName());
                }
            }
            if(module == null || module.isEmpty())
                throw new RuntimeException("Missing required module name");
            if(versions.isEmpty()){
                log.debug("Ignoring result for " + module + " because it doesn't have a single version");
            }else{
                result.addResult(module, doc, license, authors, versions);
            }
            p.checkCloseTag();
        }
        p.checkCloseTag();
    }
}
