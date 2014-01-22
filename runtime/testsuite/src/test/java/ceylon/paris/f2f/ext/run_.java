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

package ceylon.paris.f2f.ext;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.ServiceLoader;

import ceylon.paris.f2f.iface.IService;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class run_ {
    @SuppressWarnings("LoopStatementThatDoesntLoop")
    public static void main(String[] args) throws Exception {
        final ClassLoader cl = run_.class.getClassLoader();

        boolean found = false;
        for (IService service : ServiceLoader.load(IService.class, cl)) {
            found = true;
            service.poke();
            break;
        }
        if (found == false) {
            throw new IllegalStateException("Should not be here!");
        }

        String name = "META-INF/services/org/apache/camel/config.properties";
        URL resource = cl.getResource(name);
        if (resource == null) {
            throw new IllegalStateException(String.format("No %s found!", name));
        }
        Properties properties = new Properties();
        try (InputStream is = resource.openStream()) {
            properties.load(is);
        }
        String value = properties.getProperty("some.prop");
        if ("some_value".equals(value) == false) {
            throw new IllegalStateException(String.format("No such prop: %s", value));
        }
    }
}
