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

import com.redhat.ceylon.cmr.impl.URLContentStore.Attempts;
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
                    this.stream = connection.getInputStream();
                    int code = connection.getResponseCode();
                    if (code != -1 && code != 200) {
                        log.info("Got " + code + " for url: " + url);
                        throw new NotGettable();
                    }
                    String acceptRange = connection.getHeaderField("Accept-Range");
                    rangeRequests = acceptRange == null || !acceptRange.equalsIgnoreCase("none");
                    debug("Connection: "+connection.getHeaderField("Connection"));
                    debug("Got " + code + " for url: " + url);
                    length = connection.getContentLengthLong();
                    stream = connection.getInputStream();
                    break connecting;
                } catch(IOException e) {
                    maybeRetry(url, e, "connecting to");
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
        protected void cleanUpStreams(Exception inflight) throws IOException {
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
                                    for (long ii = 0; ii < bytesRead; ii++) {
                                        stream.read();
                                    }
                                }
                                debug("Reconnected to url: " + url);
                                break reconnect;
                            } catch (IOException reconnectionException) {
                                maybeRetry(url, reconnectionException, "reonnecting to");
                            }
                        }
                    }
                }
            }
        }
    }

    protected final boolean exists(final URL url) throws IOException {
        Attempts a = new Attempts();
        while (true) {
            try {
                return head(url) != null;
            } catch (IOException e) {
                a.giveup("testing existence of", url, e);
            }
        }
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
