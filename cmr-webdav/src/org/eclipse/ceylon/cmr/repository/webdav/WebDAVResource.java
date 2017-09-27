package org.eclipse.ceylon.cmr.repository.webdav;

import java.util.Date;

import org.eclipse.ceylon.aether.github.sardine.DavResource;

public class WebDAVResource {

	private DavResource res;

	WebDAVResource(DavResource res) {
		this.res = res;
	}

	public String getName() {
		return res.getName();
	}

	public boolean isDirectory() {
		return res.isDirectory();
	}

	public Long getContentLength() {
		return res.getContentLength();
	}

	public Date getModified() {
		return res.getModified();
	}

}
