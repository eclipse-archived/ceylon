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

import com.redhat.ceylon.cmr.api.Logger;

/**
 * Simple logger impl.
 *
 * @author Stephane Epardaud
 */
public class JULLogger implements Logger {

    static java.util.logging.Logger log = java.util.logging.Logger.getLogger("com.redhat.ceylon.cmr");

    @Override
    public void error(String str) {
        log.severe(str);
    }

    @Override
    public void warning(String str) {
        log.warning(str);
    }

    @Override
    public void info(String str) {
        log.info(str);
    }

    @Override
    public void debug(String str) {
        log.fine(str);
    }

}
