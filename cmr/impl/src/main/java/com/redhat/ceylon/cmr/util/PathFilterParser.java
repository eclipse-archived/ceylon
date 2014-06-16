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

package com.redhat.ceylon.cmr.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

import com.redhat.ceylon.cmr.api.PathFilter;
import org.jboss.modules.filter.MultiplePathFilterBuilder;
import org.jboss.modules.filter.PathFilters;
import org.jboss.modules.xml.MXParser;
import org.jboss.modules.xml.XmlPullParser;

import static org.jboss.modules.xml.XmlPullParser.FEATURE_PROCESS_NAMESPACES;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public final class PathFilterParser {
    private static final Method parseFilterList;

    static {
        try {
            Class<?> mxpClass = PathFilterParser.class.getClassLoader().loadClass("org.jboss.modules.ModuleXmlParser");
            parseFilterList = mxpClass.getDeclaredMethod("parseFilterList", new Class[]{XmlPullParser.class, MultiplePathFilterBuilder.class});
            parseFilterList.setAccessible(true);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static PathFilter parse(String filter) throws IOException {
        return parse(new ByteArrayInputStream(filter.getBytes()));
    }

    public static PathFilter parse(InputStream inputStream) throws IOException {
        try {
            final MXParser parser = new MXParser();
            parser.setFeature(FEATURE_PROCESS_NAMESPACES, true);
            parser.setInput(inputStream, null);
            return parseFilter(parser);
        } catch (Exception e) {
            throw new IOException("Error parsing filter.", e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException ignore) {
            }
        }
    }

    private static PathFilter parseFilter(XmlPullParser parser) throws Exception {
        parser.nextTag(); // just skip <filter>

        MultiplePathFilterBuilder builder = PathFilters.multiplePathFilterBuilder(true);
        parseFilterList.invoke(null, parser, builder);

        return new ModulesPathFilter(builder.create());
    }

    private static class ModulesPathFilter implements PathFilter {
        private org.jboss.modules.filter.PathFilter filter;

        private ModulesPathFilter(org.jboss.modules.filter.PathFilter filter) {
            this.filter = filter;
        }

        public boolean accept(String path) {
            return filter.accept(path);
        }
    }
}
