package com.redhat.ceylon.compiler.java.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

import ceylon.language.Boolean;
import ceylon.language.Callable;
import ceylon.language.Character;
import ceylon.language.Iterator;
import ceylon.language.Null;
import ceylon.language.String;
import ceylon.language.finished_;
import ceylon.language.impl.BaseIterable;
import ceylon.language.impl.BaseIterator;

@Ceylon(major = 8)
@Class(extendsType="ceylon.language::Object", basic=false, identifiable=false)
@SatisfiedTypes("ceylon.language::Iterable<ceylon.language::String,ceylon.language::Null>")
public class StringTokens 
extends BaseIterable<String,java.lang.Object> {
    
    private final java.lang.String str;
    private final Callable<? extends Boolean> separator;
    private final boolean keepSeparators;
    private final boolean groupSeparators;
    private final long limit;

    public StringTokens(java.lang.String str, 
            @TypeInfo("ceylon.language::Callable<ceylon.language::Boolean,ceylon.language::Tuple<ceylon.language::Character,ceylon.language::Character,ceylon.language::Empty>>")
            Callable<? extends Boolean> separator,
            boolean keepSeparators, boolean groupSeparators, 
            long limit) {
        super(String.$TypeDescriptor$, Null.$TypeDescriptor$);
        this.str = str;
        this.separator = separator;
        this.keepSeparators = keepSeparators;
        this.groupSeparators = groupSeparators;
        this.limit = limit;
    }

    // this one is just here to satisfy the runtime Declaration otherwise the type of separator is lost
    @TypeInfo("ceylon.language::Callable<ceylon.language::Boolean,ceylon.language::Tuple<ceylon.language::Character,ceylon.language::Character,ceylon.language::Empty>>")
    private final Callable<? extends Boolean> getSeparator$priv() {
        return separator;
    }
    
    abstract class TokenIterator 
    extends BaseIterator<String> {

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
                int start = index;
                if (limit>=0 && count==limit) {
                    index = str.length();
                    count++;
                    return String.instance(str.substring(start));
                }
                // if we start with a separator, or if we returned a separator the last time
                // and we are still looking at a separator: return an empty token once
                if (((first && start == 0) || lastTokenWasSeparator)
                        && peekSeparator()) {
                    first = false;
                    lastTokenWasSeparator = false;
                    count++;
                    return String.instance("");
                }
                // are we looking at a separator
                if (eatSeparator()) {
                    if (groupSeparators) {
                        // eat them all in one go if we group them
                        do {} while(eatSeparator());
                    }
                    // do we return them?
                    if (keepSeparators) {
                        lastTokenWasSeparator = true;
                        count++;
                        return String.instance(str.substring(start, index));
                    }
                    // keep going and eat the next word
                    start = index;
                }
                // eat until the next separator
                while(!eof() && !peekSeparator()) {
                    eatChar();
                }
                lastTokenWasSeparator = false;
                count++;
                return String.instance(str.substring(start, index));
            }
            else if (lastTokenWasSeparator) {
                // we're missing a last empty token before 
                // the EOF because the string ended with a 
                // returned separator
                lastTokenWasSeparator = false;
                count++;
                return String.instance("");
            }
            else {
                return finished_.get_();
            }
        }
        
        protected boolean eof() {
            return index >= str.length();
        }

        private boolean eatSeparator() {
            boolean ret = peekSeparator();
            if (ret) eatChar();
            return ret;
        }

        private void eatChar() {
            if (java.lang.Character.isHighSurrogate(str.charAt(index))) {
                index += 2;
            }
            else {
                index++;
            }
        }

        protected abstract boolean peekSeparator();

        @Override
        @Ignore
        public TypeDescriptor $getType$() {
            return TypeDescriptor.klass(TokenIterator.class);
        }
    }

    @Override
    public Iterator<? extends String> iterator() {

        return new TokenIterator() {
            protected final boolean peekSeparator() {
                if (eof()) return false;
                int charCodePoint = java.lang.Character.codePointAt(str, index);
                return separator.$call$(Character.instance(charCodePoint)).booleanValue();
            }
        };

    }

    @Override
    public boolean getEmpty() {
        return iterator().next() == finished_.get_();
    }
    
    @Override
    @Ignore
    public TypeDescriptor $getType$() {
        return TypeDescriptor.klass(StringTokens.class);
    }
    
}