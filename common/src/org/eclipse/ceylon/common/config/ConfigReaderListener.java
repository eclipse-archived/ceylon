/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.common.config;

import java.io.IOException;

public interface ConfigReaderListener {

    void setup() throws IOException;

    void onSection(String section, String text) throws IOException;

    void onOption(String name, String value, String text) throws IOException;

    void onComment(String text) throws IOException;

    void onWhitespace(String text) throws IOException;

    void cleanup() throws IOException;

}
