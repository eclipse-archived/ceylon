/*
 * Copyright 2014 Red Hat inc. and third party contributors as noted
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

import java.io.File;

/**
 * Artifact callback.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public interface ArtifactCallback {
    /**
     * Artifact size callback, or -1 if it cannot be determined.
     * The callback is invoked before streaming.
     * e.g. to be able to display % of downloaded artifact.
     *
     * @param size the size
     */
    void size(long size);

    /**
     * Invoked for every byte stream read.
     *
     * @param bytes  the bytes read
     * @param length the bytes read length
     */
    void read(byte[] bytes, int length);

    /**
     * Invoked once streaming is successfully finished.
     *
     * @param localFile the downloaded file
     */
    void done(File localFile);

    /**
     * For any error during streaming.
     *
     * @param err the cause
     */
    void error(Throwable err);
}
