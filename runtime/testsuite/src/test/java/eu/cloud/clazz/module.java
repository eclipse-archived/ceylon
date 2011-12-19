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

import ceylon.language.Quoted;
import ceylon.language.descriptor.Import;
import ceylon.modules.api.util.JavaToCeylon;
import ceylon.modules.api.util.ModuleVersion;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class module {
    public static ceylon.language.descriptor.Module getModule() {
        Quoted name = JavaToCeylon.toQuoted("eu.cloud.clazz");
        Quoted version = JavaToCeylon.toQuoted(new ModuleVersion(1, 0, 0, "GA").toString());
        // TODO -- maybe used later on
        /*
        PathFilter imports = new PathFilter() {
            public ceylon.language.Boolean accept(ceylon.language.String path) {
                boolean contains = path.contains(JavaToCeylon.toString("spi"));
                return JavaToCeylon.toBoolean(contains);
            }
        };
        */
        Import im = new Import(
                JavaToCeylon.toQuoted("org.jboss.filtered"),
                JavaToCeylon.toQuoted(new ModuleVersion(1, 0, 0, "Alpha1").toString()),
                false,
                false);
        return new ceylon.language.descriptor.Module(name, version, null, null, null, JavaToCeylon.toIterable(im));
    }
}
