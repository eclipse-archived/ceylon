/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.tools.p2;


class Category {
    final String name, label, description;
    Feature feature;
    boolean allJars;
    
    Category(String name, String label, String description){
        this.name = name;
        this.label = label;
        this.description = description;
    }
    void setFeature(Feature feature){
        this.feature = feature;
    }
    void setAllJars() {
        this.allJars = true;
    }
}