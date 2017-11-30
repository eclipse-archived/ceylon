/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.gae.ceylon;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class run_ {
    public static void main(String[] args) throws Throwable {
        run_.class.getClassLoader().loadClass("com.google.appengine.api.LifecycleManager");
        try {
            run_.class.getClassLoader().loadClass("javax.mail.Version");
            throw new IllegalStateException("Should not be here!");
        } catch (ClassNotFoundException ignored) {
        }
    }
}
