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

package com.redhat.ceylon.cmr.repository.webdav;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.aether.apache.http.ProtocolException;
import com.redhat.ceylon.aether.apache.http.client.ClientProtocolException;
import com.redhat.ceylon.aether.apache.http.config.Registry;
import com.redhat.ceylon.aether.apache.http.config.SocketConfig;
import com.redhat.ceylon.aether.apache.http.conn.HttpClientConnectionManager;
import com.redhat.ceylon.aether.apache.http.conn.socket.ConnectionSocketFactory;
import com.redhat.ceylon.aether.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import com.redhat.ceylon.aether.github.sardine.DavResource;
import com.redhat.ceylon.aether.github.sardine.impl.SardineException;
import com.redhat.ceylon.aether.github.sardine.impl.SardineImpl;
import com.redhat.ceylon.aether.github.sardine.impl.io.ContentLengthInputStream;

/**
 * WebDAV content store.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 * @author Stef Epardaud
 */
public class WebDAVRepository {

    private volatile SardineImpl sardine;
	private String password;
	private String username;
	private int timeout;

    public WebDAVRepository(int timeout, String username, String password) {
        this.username = username;
        this.password = password;
    	this.timeout = timeout;
    }

    private SardineImpl getSardine() {
        if (sardine == null) {
            synchronized (this) {
                if (sardine == null) {
                    sardine = new SardineImpl(username, password, null) {
                        @Override
                        protected HttpClientConnectionManager createDefaultConnectionManager(Registry<ConnectionSocketFactory> schemeRegistry) {
                            HttpClientConnectionManager connMan = super.createDefaultConnectionManager(schemeRegistry);
                            if (connMan instanceof PoolingHttpClientConnectionManager) {
                                @SuppressWarnings("resource")
                                PoolingHttpClientConnectionManager phccm = (PoolingHttpClientConnectionManager)connMan;
                                SocketConfig config = SocketConfig.custom().setSoTimeout(timeout).build();
                                phccm.setDefaultSocketConfig(config);
                            }
                            return connMan;
                        }
                    };
                }
            }
        }
        return sardine;
    }

	public boolean exists(String url) throws IOException {
		return getSardine().exists(url);
	}

	public void createDirectory(String url) throws IOException {
		getSardine().createDirectory(url);
	}

	public String lock(String url) throws IOException {
		return getSardine().lock(url);
	}

	public void unlock(String url, String token) throws IOException {
		getSardine().unlock(url, token);
	}

	public void put(String url, InputStream stream) throws IOException {
		getSardine().put(url, stream);
	}

	public List<WebDAVResource> list(String url) throws IOException {
		List<DavResource> list = getSardine().list(url);
		ArrayList<WebDAVResource> ret = new ArrayList<>(list.size());
		for(DavResource res : list){
			ret.add(new WebDAVResource(res));
		}
		return ret;
	}

	public WebDAVInputStream get(String url) throws IOException {
		ContentLengthInputStream ret = getSardine().get(url);
		return ret == null ? null : new WebDAVInputStream(ret);
	}

	public String getBetterExceptionMessage(IOException x, String root) {
        if (x instanceof SardineException) {
            // hide this from callers because its getMessage() is borked
            SardineException sx = (SardineException) x;
            if(sx.getStatusCode() == HttpURLConnection.HTTP_FORBIDDEN){
                return "authentication failed on repository "+root;
            }
            return sx.getMessage() + ": " + sx.getResponsePhrase() + " " + sx.getStatusCode();
        }
        if (x instanceof ClientProtocolException) {
            // in case of protocol exception (invalid response) we get this sort of
            // chain set up with a null message, so unwrap it for better messages
            if (x.getCause() != null && x.getCause() instanceof ProtocolException)
                return x.getCause().getMessage();
        }
        return null;
	}
}
