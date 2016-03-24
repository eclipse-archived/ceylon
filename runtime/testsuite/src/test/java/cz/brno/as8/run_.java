/*
 * Copyright 2012 Red Hat inc. and third party contributors as noted
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

package cz.brno.as8;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class run_ {
    public static void main(String[] args) {
        org.jboss.acme.$module_.run(); // should be able to run this
        ping("eu.cloud.clazz.run_");
        ping("org.jboss.filtered.api.SomeAPI");
    }

    private static void ping(String className) {
        try {
            run_.class.getClassLoader().loadClass(className);
            throw new RuntimeException("Should not be here");
        } catch (ClassNotFoundException cnfe) {
            System.out.println("CNFE = " + cnfe);
        }
    }
}
