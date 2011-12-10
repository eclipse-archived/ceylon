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

package net.something.xyz;

import ceylon.language.Quoted;
import ceylon.language.descriptor.Import;
import ceylon.modules.api.util.JavaToCeylon;
import ceylon.modules.api.util.ModuleVersion;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class module {
    public static ceylon.language.descriptor.Module getModule() {
        Quoted name = JavaToCeylon.toQuoted("net.something.xyz");
        Quoted version = JavaToCeylon.toQuoted(new ModuleVersion(1, 0, 0, "Final").toString());
        Import im1 = new Import(
                JavaToCeylon.toQuoted("org.jboss.acme"),
                JavaToCeylon.toQuoted(new ModuleVersion(1, 0, 0, "CR1").toString()),
                true,
                true);
        Import im2 = new Import(
                JavaToCeylon.toQuoted("si.alesj.ceylon"),
                JavaToCeylon.toQuoted(new ModuleVersion(1, 0, 0, "GA").toString()),
                true,
                true);
        return new ceylon.language.descriptor.Module(name, version, null, null, null, JavaToCeylon.toIterable(im1, im2));
    }
}
