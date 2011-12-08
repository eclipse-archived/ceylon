/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package com.redhat.ceylon.cmr.api;

import java.io.Serializable;

/**
 * Artifact lookup context.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class ArtifactContext implements Serializable {
    public static final String CAR = ".car";
    public static final String JAR = ".jar";
    public static final String ZIP = ".zip";

    private String name;
    private String version;
    private String suffix = CAR;
    private boolean localOnly;
    private boolean ignoreSHA;
    private boolean throwErrorIfMissing;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public boolean isLocalOnly() {
        return localOnly;
    }

    public void setLocalOnly(boolean localOnly) {
        this.localOnly = localOnly;
    }

    public boolean isIgnoreSHA() {
        return ignoreSHA;
    }

    public void setIgnoreSHA(boolean ignoreSHA) {
        this.ignoreSHA = ignoreSHA;
    }

    public boolean isThrowErrorIfMissing() {
        return throwErrorIfMissing;
    }

    public void setThrowErrorIfMissing(boolean throwErrorIfMissing) {
        this.throwErrorIfMissing = throwErrorIfMissing;
    }

    @Override
    public String toString() {
        return getName() + "-" + getVersion();
    }
}
