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
/*
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
*/
    }
}

