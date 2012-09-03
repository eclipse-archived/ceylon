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

package com.redhat.ceylon.cmr.webdav;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.http.ProtocolException;
import org.apache.http.client.ClientProtocolException;

import com.googlecode.sardine.DavResource;
import com.googlecode.sardine.Sardine;
import com.googlecode.sardine.SardineFactory;
import com.googlecode.sardine.impl.SardineException;
import com.redhat.ceylon.cmr.api.Logger;
import com.redhat.ceylon.cmr.api.ModuleQuery;
import com.redhat.ceylon.cmr.api.ModuleQuery.Type;
import com.redhat.ceylon.cmr.api.ModuleResult;
import com.redhat.ceylon.cmr.api.ModuleSearchResult;
import com.redhat.ceylon.cmr.api.ModuleVersionDetails;
import com.redhat.ceylon.cmr.api.ModuleVersionQuery;
import com.redhat.ceylon.cmr.api.ModuleVersionResult;
import com.redhat.ceylon.cmr.impl.CMRException;
import com.redhat.ceylon.cmr.impl.NodeUtils;
import com.redhat.ceylon.cmr.impl.URLContentStore;
import com.redhat.ceylon.cmr.spi.ContentHandle;
import com.redhat.ceylon.cmr.spi.ContentOptions;
import com.redhat.ceylon.cmr.spi.Node;
import com.redhat.ceylon.cmr.spi.OpenNode;
import com.redhat.ceylon.cmr.util.WS;
import com.redhat.ceylon.cmr.util.WS.Link;
import com.redhat.ceylon.cmr.util.WS.Parser;
import com.redhat.ceylon.cmr.util.WS.XMLHandler;

/**
 * WebDAV content store.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 * @author Stef Epardaud
 */
public class WebDAVContentStore extends URLContentStore {

    public final static String HERD_COMPLETE_MODULES_REL = "http://modules.ceylon-lang.org/rel/complete-modules";
    public final static String HERD_COMPLETE_VERSIONS_REL = "http://modules.ceylon-lang.org/rel/complete-versions";
    public final static String HERD_SEARCH_MODULES_REL = "http://modules.ceylon-lang.org/rel/search-modules";

    private volatile Sardine sardine;
    private boolean _isHerd;
    private boolean forcedAuthenticationForPutOnHerd = false;
    private String herdCompleteModulesURL;
    private String herdCompleteVersionsURL;
    private String herdSearchModulesURL;

    public WebDAVContentStore(String root, Logger log) {
        super(root, log);
    }

    protected Sardine getSardine() {
        if (sardine == null) {
            synchronized (this) {
                if (sardine == null) {
                    sardine = (username == null || password == null) ? SardineFactory.begin() : SardineFactory.begin(username, password);
                    _isHerd = testHerd();
                }
            }
        }
        return sardine;
    }

    @Override
    public boolean isHerd(){
        getSardine();
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
            log.info("Got complete-modules link: " + herdCompleteModulesURL);
            log.info("Got complete-versions link: " + herdCompleteVersionsURL);
            log.info("Got search-modules link: " + herdSearchModulesURL);
        }catch(Exception x){
            log.debug("Failed to read links from Herd repo: "+x.getMessage());
        }
    }

    public OpenNode create(Node parent, String child) {
        try {
            if(!isHerd())
                mkdirs(getSardine(), parent);
            return createNode(child);
        } catch (IOException e) {
            throw convertIOException(e);
        }
    }

    public ContentHandle peekContent(Node node) {
        try {
            final String url = getUrlAsString(node);
            return (getSardine().exists(url) ? new WebDAVContentHandle(url) : null);
        } catch (IOException e) {
            return null;
        }
    }

    public ContentHandle getContent(Node node) throws IOException {
        return new WebDAVContentHandle(getUrlAsString(node));
    }

    public ContentHandle putContent(Node node, InputStream stream, ContentOptions options) throws IOException {
        final Sardine s = getSardine();

        try {
            /*
             * Most disgusting trick ever. Stef failed to set up Sardine to do preemptive auth on all hosts
             * and ports (may only work on port 80, reading the code), so when not using Herd we generate a ton
             * of requests that will trigger auth, but not for Herd. So we start with a PUT and that replies with
             * an UNAUTHORIZED response, which Sardine can't handle because the InputStream is not "restartable".
             * By making an extra HEAD request (restartable because no entity body) we force the auth to happen.
             * Yuk.
             */
            if(isHerd() && !forcedAuthenticationForPutOnHerd){
                s.exists(getUrlAsString(node));
                forcedAuthenticationForPutOnHerd = true;
            }
            final Node parent = NodeUtils.firstParent(node);
            if(!isHerd())
                mkdirs(s, parent);

            final String pUrl = getUrlAsString(parent);
            String token = null;
            if(!isHerd())
                token = s.lock(pUrl); // local parent
            try {
                final String url = getUrlAsString(node);
                s.put(url, stream);
                return new WebDAVContentHandle(url);
            } finally {
                if(!isHerd())
                    s.unlock(pUrl, token);
            }
        } catch (IOException x) {
            throw convertIOException(x);
        }
    }

    public CMRException convertIOException(IOException x) {
        if (x instanceof SardineException) {
            // hide this from callers because its getMessage() is borked
            SardineException sx = (SardineException) x;
            return new CMRException(sx.getMessage() + ": " + sx.getResponsePhrase() + " " + sx.getStatusCode());
        }
        if (x instanceof ClientProtocolException) {
            // in case of protocol exception (invalid response) we get this sort of
            // chain set up with a null message, so unwrap it for better messages
            if (x.getCause() != null && x.getCause() instanceof ProtocolException)
                return new CMRException(x.getCause().getMessage());
        }
        return new CMRException(x);
    }

    protected void mkdirs(Sardine s, Node parent) throws IOException {
        if (parent == null)
            return;

        mkdirs(s, NodeUtils.firstParent(parent));

        final String url = getUrlAsString(parent);
        if (s.exists(url) == false) {
            s.createDirectory(url);
        }
    }

    protected ContentHandle createContentHandle(Node parent, String child, String path, Node node) {
        return new WebDAVContentHandle(root + path);
    }

    public Iterable<? extends OpenNode> find(Node parent) {
        final String url = getUrlAsString(parent);
        try {
            final List<OpenNode> nodes = new ArrayList<OpenNode>();
            final List<DavResource> resources = getSardine().list(url);
            for (DavResource dr : resources) {
                final String label = dr.getName();
                final RemoteNode node = new RemoteNode(label);
                if (dr.isDirectory())
                    node.setContentMarker();
                else
                    node.setHandle(new WebDAVContentHandle(url + label));
                nodes.add(node);
            }
            return nodes;
        } catch (IOException e) {
            log.debug("Failed to list url: " + url);
            return Collections.emptyList();
        }
    }

    @Override
    protected boolean urlExists(String path) {
        try {
            return getSardine().exists(getUrlAsString(path));
        } catch (IOException e) {
            log.debug("Failed to check url: " + path);
            return false;
        }
    }

    protected boolean urlExists(URL url) {
        try {
            return getSardine().exists(url.toExternalForm());
        } catch (IOException e) {
            log.debug("Failed to check url: " + url);
            return false;
        }
    }

    @Override
    public String toString() {
        return "WebDAV content store: " + root;
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
        // FIXME: version comparator
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

    private class WebDAVContentHandle implements ContentHandle {

        private final String url;

        private WebDAVContentHandle(String url) {
            this.url = url;
        }

        public boolean hasBinaries() {
            try {
                final List<DavResource> list = getSardine().list(url);
                return list.size() == 1 && list.get(0).isDirectory() == false;
            } catch (IOException e) {
                log.warning("Cannot list resources: " + url + "; error - " + e);
                return false;
            }
        }

        public InputStream getBinariesAsStream() throws IOException {
            return getSardine().get(url);
        }

        public File getContentAsFile() throws IOException {
            return null;
        }

        public long getLastModified() throws IOException {
            if(isHerd())
                return lastModified(new URL(url));
            final List<DavResource> list = getSardine().list(url);
            if (list.isEmpty() == false && list.get(0).isDirectory() == false) {
                Date modified = list.get(0).getModified();
                if (modified != null) {
                    return modified.getTime();
                }
            }
            return -1L;
        }

        public void clean() {
        }
    }
}
