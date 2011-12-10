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

package eu.cloud.clazz;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class run {
    public static void main(String[] args) {
        ClassLoader cl = run.class.getClassLoader();
        try {
            cl.loadClass("org.jboss.filtered.spi.SomeSPI");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            cl.loadClass("org.jboss.filtered.api.SomeAPI");
            throw new RuntimeException("Fail, should not be here!");
        } catch (ClassNotFoundException ignored) {
        }
        try {
            cl.loadClass("org.jboss.filtered.impl.SomeImpl");
            throw new RuntimeException("Fail, should not be here!");
        } catch (ClassNotFoundException ignored) {
        }
    }
}

