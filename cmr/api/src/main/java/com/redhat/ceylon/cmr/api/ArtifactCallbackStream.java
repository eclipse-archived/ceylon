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

package com.redhat.ceylon.cmr.api;

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

    @SuppressWarnings("NullableProblems")
    public int read(byte[] b, int off, int len) throws IOException {
        int read = super.read(b, off, len);
        callback.read(b, (read == -1) ? 0 : read);
        return read;
    }
}
