/*
 * Copyright 2011 Red Hat inc. and third party contributors as noted 
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

package com.redhat.ceylon.cmr.impl;

import com.redhat.ceylon.cmr.api.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Simple thread local caching.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public final class LookupCaching {

    private static final ThreadLocal<Boolean> enabled = new ThreadLocal<Boolean>();
    private static final ThreadLocal<Map<Class<? extends Repository>, List<String>>> cached = new ThreadLocal<Map<Class<? extends Repository>, List<String>>>();

    public static void enable() {
        enabled.set(Boolean.TRUE);
    }

    public static boolean isEnabled() {
        return (enabled.get() != null);
    }

    public static void disable() {
        cached.remove();
        enabled.remove();
    }

    public static List<String> getTokens(Class<? extends Repository> repositoryType) {
        Map<Class<? extends Repository>, List<String>> map = cached.get();
        return (map != null) ? map.get(repositoryType) : null;
    }

    public static void setTokens(Class<? extends Repository> repositoryType, List<String> tokens) {
        Map<Class<? extends Repository>, List<String>> map = cached.get();
        if (map == null) {
            map = new HashMap<Class<? extends Repository>, List<String>>();
            cached.set(map);
        }
        map.put(repositoryType, tokens);
    }
}
