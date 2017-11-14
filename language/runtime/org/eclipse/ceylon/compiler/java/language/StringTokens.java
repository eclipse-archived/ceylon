/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.java.language;

import org.eclipse.ceylon.compiler.java.metadata.Ceylon;
import org.eclipse.ceylon.compiler.java.metadata.Class;
import org.eclipse.ceylon.compiler.java.metadata.Ignore;
import org.eclipse.ceylon.compiler.java.metadata.SatisfiedTypes;
import org.eclipse.ceylon.compiler.java.metadata.TypeInfo;
import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;

import org.eclipse.ceylon.compiler.java.language.StringTokens;

import ceylon.language.Boolean;
import ceylon.language.Callable;
import ceylon.language.Character;
import ceylon.language.Integer;
import ceylon.language.Iterator;
import ceylon.language.Null;
import ceylon.language.String;
import ceylon.language.finished_;
import ceylon.language.impl.BaseIterable;
import ceylon.language.impl.BaseIterator;

@Ceylon(major = 8)
@Class(extendsType="ceylon.language::Object", basic=false, identifiable=false)
@SatisfiedTypes("{ceylon.language::String+}")
public class StringTokens 
extends BaseIterable<String,java.lang.Object> {
    
    private static final long serialVersionUID = -8391084560840739585L;
    
    private final java.lang.String str;
    private final Callable<? extends Boolean> separator;
    private final boolean keepSeparators;
    private final boolean groupSeparators;
    private final Integer limit;

    public StringTokens(java.lang.String str, 
            @TypeInfo("ceylon.language::Boolean(ceylon.language::Character)")
            Callable<? extends Boolean> separator,
            boolean keepSeparators, 
            boolean groupSeparators,
            @TypeInfo("ceylon.language::Integer?")
            Integer limit) {
        super(String.$TypeDescriptor$, Null.$TypeDescriptor$);
        this.str = str;
        this.separator = separator;
        this.keepSeparators = keepSeparators;
        this.groupSeparators = groupSeparators;
        this.limit = limit;
    }

    // this one is just here to satisfy the runtime Declaration otherwise the type of separator is lost
    @TypeInfo("ceylon.language::Boolean(ceylon.language::Character)")
    private final Callable<? extends Boolean> getSeparator$priv() {
        return separator;
    }
    
    private class TokenIterator extends BaseIterator<String> {

        private static final long serialVersionUID = 3972342354562630763L;

        public TokenIterator() {
            super(String.$TypeDescriptor$);
        }

        protected int index = 0;
        private boolean first = true;
        private boolean lastTokenWasSeparator = false;
        private int count = 0;
               
        @Override
        public java.lang.Object next() {
            if (!eof()) {
                if ((first || lastTokenWasSeparator)
                        && isSeparator()) {
                    // if we start with a separator, 
                    // or if we returned a separator 
                    // the last time and we are still 
                    // looking at a separator, return 
                    // an empty token once
                    return emptyToken();
                }
                
                // are we looking at a separator
                if (isSeparator()) {
                    int start = index;
                    advance();
                    if (groupSeparators) {
                        // eat them all in one go  
                        // if we group them
                        while (isSeparator()) {
                            advance();
                        }
                    }
                    if (keepSeparators) {
                        // return it
                        return token(start, true);
                    }
                }
                
                int start = index;
                if (limit!=null && count>=limit.longValue()) {
                    advanceToEnd();
                }
                else {
                    // eat until the next separator
                    while (isRegular()) {
                        advance();
                    }
                }
                return token(start, false);
            }
            else if (lastTokenWasSeparator) {
                // we're missing a last empty token before 
                // the EOF because the string ended with a 
                // returned separator
                return emptyToken();
            }
            else {
                return finished_.get_();
            }
        }

        private String token(int start, boolean separator) {
            if (!separator) count++;
            first = false;
            lastTokenWasSeparator = separator;
            return String.instance(str.substring(start, index));
        }

        private String emptyToken() {
            count++;
            first = false;
            lastTokenWasSeparator = false;
            return String.instance("");
        }

        private void advanceToEnd() {
            index = str.length();
        }
        
        private void advance() {
            index += java.lang.Character.charCount(str.codePointAt(index));
        }

        private boolean eof() {
            return index >= str.length();
        }

        private boolean isRegular() {
            if (eof()) return false;
            int codePoint = java.lang.Character.codePointAt(str, index);
            return !separator.$call$(Character.instance(codePoint)).booleanValue();
        }

        private boolean isSeparator() {
            if (eof()) return false;
            int codePoint = java.lang.Character.codePointAt(str, index);
            return separator.$call$(Character.instance(codePoint)).booleanValue();
        }

        @Override
        @Ignore
        public TypeDescriptor $getType$() {
            return TypeDescriptor.klass(TokenIterator.class);
        }
    }

    @Override
    public Iterator<? extends String> iterator() {
        return new TokenIterator();
    }

    @Override
    public boolean getEmpty() {
        return false;
    }
    
    @Override
    @Ignore
    public TypeDescriptor $getType$() {
        return TypeDescriptor.klass(StringTokens.class);
    }
    
}