/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
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
