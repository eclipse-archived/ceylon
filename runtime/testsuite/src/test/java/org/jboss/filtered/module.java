/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
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

package org.jboss.filtered;

import ceylon.language.Quoted;
import ceylon.language.descriptor.PathFilter;
import ceylon.language.descriptor.PathFilters;
import ceylon.modules.api.runtime.ModuleVersion;
import ceylon.modules.api.util.JavaToCeylon;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class module {
    public static ceylon.language.descriptor.Module getModule() {
        Quoted name = JavaToCeylon.toQuoted("org.jboss.filtered");
        Quoted version = JavaToCeylon.toQuoted(new ModuleVersion(1, 0, 0, "Alpha1").toString());
        PathFilter exports = new PathFilter() {
            public ceylon.language.Boolean accept(ceylon.language.String path) {
                return JavaToCeylon.toBoolean(path.startsWith("org/jboss/filtered/spi") || path.startsWith("org/jboss/filtered/api"));
            }
        };
        return new ceylon.language.descriptor.Module(name, version, null, null, null, null, exports, PathFilters.acceptAll());
    }
}
