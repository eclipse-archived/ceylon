/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.cmr.api;

import java.util.Comparator;

import org.eclipse.ceylon.common.CeylonVersionComparator;

/**
 * Version comparator that delegates to MavenVersionComparator 
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class VersionComparator {
    public static Comparator<String> INSTANCE = CeylonVersionComparator.INSTANCE;

    public static int compareVersions(String a, String b) {
        return INSTANCE.compare(a, b);
    }

    public static String[] orderVersions(String a, String b) {
        return CeylonVersionComparator.orderVersions(a, b);
    }
}
