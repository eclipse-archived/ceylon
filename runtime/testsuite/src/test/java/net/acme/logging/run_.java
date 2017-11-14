/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package net.acme.logging;

/**
 * @author <a href="mailto:matejonnet@gmail.com">Matej Lazar</a>
 */
public class run_ {
    public static void main(String[] args) throws ClassNotFoundException {
        run_.class.getClassLoader().loadClass("java.util.logging.Logger");
        // run_.class.getClassLoader().loadClass("org.jboss.logmanager.LogManager");
        System.out.println(run_.class.getName() + ": run_ ...");
    }
}
