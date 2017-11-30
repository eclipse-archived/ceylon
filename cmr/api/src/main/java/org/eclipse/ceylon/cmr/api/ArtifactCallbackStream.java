/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.cmr.api;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Artifact callback stream.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class ArtifactCallbackStream extends FilterInputStream {
    private static final ThreadLocal<ArtifactCallback> TL = new ThreadLocal<>();
    private ArtifactCallback callback;

    /**
     * Set callback for this thread.
     * If callback param is null, it will remove any currently set callback.
     *
     * @param callback the callback
     */
    public static void setCallback(ArtifactCallback callback) {
        if (callback != null) {
            TL.set(callback);
        } else {
            TL.remove();
        }
    }

    public static ArtifactCallback getCallback() {
        return TL.get();
    }

    public ArtifactCallbackStream(ArtifactCallback callback, InputStream in) {
        super(in);
        this.callback = callback;
    }

    public int read() throws IOException {
        return read(new byte[1]);
    }

    public int read(byte[] b, int off, int len) throws IOException {
        int read = super.read(b, off, len);
        callback.read(b, (read == -1) ? 0 : read);
        return read;
    }
}
