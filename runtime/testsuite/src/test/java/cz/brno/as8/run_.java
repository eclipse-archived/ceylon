/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
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
