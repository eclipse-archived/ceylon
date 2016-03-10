/*
 * Copyright 2011 Red Hat inc. and third party contributors as noted 
 * by the author tags.
 * 
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;

import com.redhat.ceylon.cmr.spi.ContentHandle;
import com.redhat.ceylon.cmr.spi.ContentOptions;
import com.redhat.ceylon.cmr.spi.Node;
import com.redhat.ceylon.cmr.spi.OpenNode;
import com.redhat.ceylon.cmr.spi.SizedInputStream;
import com.redhat.ceylon.common.Constants;
import com.redhat.ceylon.common.log.Logger;

/**
 * Remote content store.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class RemoteContentStore extends URLContentStore {

    public RemoteContentStore(String root, Logger log, boolean offline, int timeout, Proxy proxy) {
        super(root, log, offline, timeout, proxy);
    }

    private static class NotGettable extends RuntimeException {
    }
    
    protected SizedInputStream openSizedStream(final URL url) throws IOException {
        if (connectionAllowed()) {
            try {
                return new RetryingSizedInputStream(url, proxy, timeout);
            } catch (NotGettable e) {
                // fall through
            }
        }
        return null;
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
        private final int attempts = 3;
        /** The number of attempts left */
        private int attemptsLeft = attempts;
        /** The <em>current</em> stream: Gets mutated when {@link ReconnectingInputStream} reconnects */
        private InputStream stream;
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
                    HttpURLConnection huc = makeConnection(url, -1);
                    this.stream = huc.getInputStream();
                    int code = huc.getResponseCode();
                    if (code != -1 && code != 200) {
                        log.info("Got " + code + " for url: " + url);
                        throw new NotGettable();
                    }
                    String acceptRange = huc.getHeaderField("Accept-Range");
                    rangeRequests = acceptRange == null || !acceptRange.equalsIgnoreCase("none");
                    debug("Got " + code + " for url: " + url);
                    length = huc.getContentLengthLong();
                    stream = huc.getInputStream();
                    break connecting;
                } catch(IOException e) {
                    giveup(e);
                }
            }
            this.contentLength = length;
            this.reconnectingStream = new ReconnectingInputStream();
        }

        private void debug(String s) {
            log.debug(s);
            System.err.println(s);
        }
        
        /**
         * For selected exceptions returns normally if there are 
         * attempts left, otherwise rethrows the given exception. 
         */
        void giveup(IOException e) throws IOException{
            if (e instanceof SocketTimeoutException) {
                if (attemptsLeft-- <= 0) {
                    debug("Giving up download of " + url + " due to " + e);
                    SocketTimeoutException newException = new SocketTimeoutException("Timed out during connection to "+url);
                    newException.initCause(e);
                    throw newException;
                } else {
                    debug("Retry download of "+ url + " after " + e + ", " + attemptsLeft + " attempts left");
                }
            } else {
                debug("Giving up download of " + url + " due to " + e);
                throw e;
            }
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
            long bytesRead = 0;
            @Override
            public int read() throws IOException {
                while (true) {
                    try {
                        int result = stream.read();
                        if (result != -1) {
                            bytesRead++;
                        }
                        return result;
                    } catch (IOException e) {
                        giveup(e);
                        reconnect: while (true) {
                            try {
                                // otherwise open another connection...
                                // using a range request unless initial request had Accept-Ranges: none
                                HttpURLConnection conn = makeConnection(url, rangeRequests ? bytesRead : -1);
                                final int code = conn.getResponseCode();
                                debug("Got " + code + " for reconnection to url: " + url);
                                if (rangeRequests && code == 206) {
                                    stream = conn.getInputStream();
                                } else if (code == 200) {
                                    // we didn't make a range request
                                    // (or the server didn't understand the Range header)
                                    // so spool the appropriate number of bytes
                                    stream = conn.getInputStream();
                                    for (long ii = 0; ii < bytesRead; ii++) {
                                        stream.read();
                                    }
                                }
                                debug("Reconnected to url: " + url);
                                break reconnect;
                            } catch (IOException e2) {
                                giveup(e);
                            }
                        }
                    }
                }
            }
        }
    }

    protected boolean exists(final URL url) throws IOException {
        return head(url) != null;
    }

    public ContentHandle peekContent(Node node) {
        return urlExists(node) ? createContentHandle(null, null, null, node) : null;
    }

    public ContentHandle getContent(Node node) throws IOException {
        return createContentHandle(null, null, null, node);
    }

    public ContentHandle putContent(Node node, InputStream stream, ContentOptions options) throws IOException {
        return null; // cannot write
    }

    protected RemoteNode createNode(String label) {
        return new ImmutableRemoteNode(label);
    }

    public OpenNode create(Node parent, String child) {
        return null;
    }

    protected ContentHandle createContentHandle(Node parent, String child, String path, Node node) {
        return new RemoteContentHandle(node);
    }

    public Iterable<? extends OpenNode> find(Node parent) {
        return Collections.emptyList(); // cannot find all children
    }

    protected boolean urlExists(URL url) {
        if (url == null)
            return false;

        try {
            return exists(url);
        } catch (IOException ignored) {
            return false;
        }
    }

    @Override
    public String toString() {
        return "RemoteContentStore: " + root;
    }

    private class RemoteContentHandle implements ContentHandle {
        private final Node node;

        private RemoteContentHandle(Node node) {
            this.node = node;
        }

        public boolean hasBinaries() {
            return true; // we assume we have bins behind the url
        }

        public InputStream getBinariesAsStream() throws IOException {
            SizedInputStream ret = getBinariesAsSizedStream();
            return ret != null ? ret.getInputStream() : null;
        }

        public SizedInputStream getBinariesAsSizedStream() throws IOException {
            final URL url = getURL(compatiblePath(NodeUtils.getFullPath(node, SEPARATOR)));
            log.debug("Fetching resource: " + url);
            return openSizedStream(url);
        }

        public File getContentAsFile() throws IOException {
            return null;  // unsupported
        }

        public long getLastModified() throws IOException {
            final URL url = getURL(compatiblePath(NodeUtils.getFullPath(node, SEPARATOR)));
            return lastModified(url);
        }

        public long getSize() throws IOException {
            final URL url = getURL(compatiblePath(NodeUtils.getFullPath(node, SEPARATOR)));
            return size(url);
        }

        public void clean() {
        }
    }

    private static class ImmutableRemoteNode extends RemoteNode {
        private ImmutableRemoteNode(String label) {
            super(label);
        }

        @Override
        public OpenNode addContent(String label, InputStream content, ContentOptions options) throws IOException {
            return null; // cannot add content
        }

        @Override
        public <T extends Serializable> OpenNode addContent(String label, T content, ContentOptions options) throws IOException {
            return null; // cannot add content
        }
    }
}
