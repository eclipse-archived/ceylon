/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.model.typechecker.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Annotation {
    private String name;
    private Map<String,String> namedArguments = new HashMap<String,String>();
    private List<String> positionalArguments = new ArrayList<String>();

    public Annotation() {}

    public Annotation(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Map<String,String> getNamedArguments() {
        return namedArguments;
    }
    
    public void addNamedArgument(String param, String value) {
        this.namedArguments.put(param, value);
    }
    
    public List<String> getPositionalArguments() {
        return positionalArguments;
    }
    
    public void addPositionalArgument(String value) {
        positionalArguments.add(value);
    }
    
    @Override 
    public String toString() {
        String args = "";
        if (!positionalArguments.isEmpty()) {
            args = positionalArguments.toString()
                    .replace('[', '(')
                    .replace(']', ')');
        }
        else if (!namedArguments.isEmpty()) {
            args = namedArguments.toString()
                    .replace(',', ';');
        }
        return name + args;
    }
    
}
