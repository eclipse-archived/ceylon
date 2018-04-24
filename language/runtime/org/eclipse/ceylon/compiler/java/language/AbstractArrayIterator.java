/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.java.language;

import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;

import ceylon.language.finished_;
import ceylon.language.impl.BaseIterator;

public abstract class AbstractArrayIterator<Element> 
        extends BaseIterator<Element> {
    
    private final int start;
    private final int len;
    private final int step;
    private int i = 0;

    public AbstractArrayIterator(
            TypeDescriptor $reified$Element, 
            int start, int len, int step) {
        super($reified$Element);
        this.len = len;
        this.step = step;
        this.start = start;
    }
    
    @Override
    public Object next() {
        if (i < len) {
            Element result = get(start+i*step);
            i++;
            return result;
        }
        else {
            return finished_.get_();
        }
    }
    
    protected abstract Element get(int index);
}