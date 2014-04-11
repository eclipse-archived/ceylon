package com.redhat.ceylon.compiler.java.language;

import ceylon.language.Boolean;
import ceylon.language.Callable;
import ceylon.language.Category$impl;
import ceylon.language.Character;
import ceylon.language.Comparison;
import ceylon.language.Entry;
import ceylon.language.Integer;
import ceylon.language.Iterable;
import ceylon.language.Iterable$impl;
import ceylon.language.Iterator;
import ceylon.language.List;
import ceylon.language.Null;
import ceylon.language.Object;
import ceylon.language.Sequential;
import ceylon.language.String;
import ceylon.language.finished_;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 7)
@Class
@SatisfiedTypes("ceylon.language::Iterable<ceylon.language::String,ceylon.language::Null>")
public class StringTokens 
        implements Iterable<String,java.lang.Object>, ReifiedType {
    @Ignore
    private final Iterable$impl<String,java.lang.Object> 
    $ceylon$language$Iterable$this;
    @Ignore
    private final Category$impl<java.lang.Object> 
    $ceylon$language$Category$this;
    
    private final java.lang.String str;
    private final Callable<? extends Boolean> separator;
    private final boolean keepSeparators;
    private final boolean groupSeparators;

    public StringTokens(java.lang.String str, 
            @TypeInfo("ceylon.language::Callable<ceylon.language::Boolean,ceylon.language::Tuple<ceylon.language::Character,ceylon.language::Character,ceylon.language::Empty>>")
            Callable<? extends Boolean> separator,
            boolean keepSeparators, boolean groupSeparators) {
        this.$ceylon$language$Iterable$this = 
        		new Iterable$impl<String,java.lang.Object>(String.$TypeDescriptor$, 
        		Null.$TypeDescriptor$, this);
        this.$ceylon$language$Category$this = 
        		new Category$impl<java.lang.Object>(Object.$TypeDescriptor$,this);
        this.str = str;
        this.separator = separator;
        this.keepSeparators = keepSeparators;
        this.groupSeparators = groupSeparators;
    }

    // this one is just here to satisfy the runtime Declaration otherwise the type of separator is lost
    @TypeInfo("ceylon.language::Callable<ceylon.language::Boolean,ceylon.language::Tuple<ceylon.language::Character,ceylon.language::Character,ceylon.language::Empty>>")
    private final Callable<? extends Boolean> getSeparator$priv(){
        return separator;
    }
    
    @Ignore
    @Override
    public Category$impl<java.lang.Object> 
    $ceylon$language$Category$impl(){
        return $ceylon$language$Category$this;
    }

    @Ignore
    @Override
    public Iterable$impl<String,java.lang.Object> 
    $ceylon$language$Iterable$impl(){
        return $ceylon$language$Iterable$this;
    }


    @Override
    public Iterator<? extends String> iterator() {
        abstract class TokenIterator 
                extends AbstractIterator<String> 
                implements ReifiedType {
            
            public TokenIterator() {
                super(String.$TypeDescriptor$);
            }

            protected final char[] chars = str.toCharArray();
            protected int index = 0;
            private boolean first = true;
            private boolean lastTokenWasSeparator = false;

            @Override
            public java.lang.Object next() {
                if (!eof()) {
                    int start = index;
                    // if we start with a separator, or if we returned a separator the last time
                    // and we are still looking at a separator: return an empty token once
                    if(((first && start == 0)
                            || lastTokenWasSeparator)
                            && peekSeparator()){
                        first = false;
                        lastTokenWasSeparator = false;
                        return String.instance("");
                    }
                    // are we looking at a separator
                    if(eatSeparator()){
                        if(groupSeparators){
                            // eat them all in one go if we group them
                            do{}while(eatSeparator());
                        }
                        // do we return them?
                        if(keepSeparators){
                            lastTokenWasSeparator = true;
                            return String.instance(new java.lang.String(chars, 
                            		start, index-start));
                        }
                        // keep going and eat the next word
                        start = index;
                    }
                    // eat until the next separator
                    while(!eof() && !peekSeparator()){
                        eatChar();
                    }
                    lastTokenWasSeparator = false;
                    return String.instance(new java.lang.String(chars, 
                    		start, index-start));
                } else if (lastTokenWasSeparator){
                    // we're missing a last empty token before the EOF because the string ended
                    // with a returned separator
                    lastTokenWasSeparator = false;
                    return String.instance("");
                } else {
                    return finished_.get_();
                }
            }

            protected boolean eof(){
                return index >= chars.length;
            }

            private boolean eatSeparator() {
                boolean ret = peekSeparator();
                if(ret)
                    eatChar();
                return ret;
            }

            private void eatChar() {
                if(java.lang.Character.isHighSurrogate(chars[index]))
                    index += 2;
                else
                    index++;
            }

            protected abstract boolean peekSeparator();

            @Override
            @Ignore
            public TypeDescriptor $getType$() {
                return TypeDescriptor.klass(TokenIterator.class);
            }
        }

        //if (separator instanceof Callable) {
            return new TokenIterator() {
                protected final boolean peekSeparator() {
                    if(eof())
                        return false;
                    int charCodePoint = java.lang.Character.codePointAt(chars, index);
                    return separator.$call$(Character.instance(charCodePoint)).booleanValue();
                }
            };
        /*} else if (separator instanceof java.lang.String) {
            return new TokenIterator() {
                protected final boolean peekSeparator() {
                    if(eof())
                        return false;
                    int charCodePoint = java.lang.Character.codePointAt(chars, index);
                    return ((java.lang.String)separator).indexOf(charCodePoint) >= 0;
                }
            };
        } else if (separator instanceof String) {
            return new TokenIterator() {
                protected final boolean peekSeparator() {
                    if(eof())
                        return false;
                    int charCodePoint = java.lang.Character.codePointAt(chars, index);
                    return ((String)separator).value.indexOf(charCodePoint) >= 0;
                }
            };
        } else {
            return new TokenIterator() {
                @SuppressWarnings("unchecked")
                protected final boolean peekSeparator() {
                    if(eof())
                        return false;
                    int charCodePoint = java.lang.Character.codePointAt(chars, index);
                    java.lang.Object $tmp;
                    for (Iterator<? extends Character> iter = ((Iterable<? extends Character, ?>)separator).iterator();
                            !(($tmp = iter.next()) instanceof Finished);) {
                        if (((Character)$tmp).getInteger() == charCodePoint) {
                            return true;
                        }
                    }
                    return false;
                }
            };
        }*/
    }

    @Override
    public long getSize() {
        return $ceylon$language$Iterable$this.getSize();
    }

    @Override
    public boolean longerThan(long length) {
        return $ceylon$language$Iterable$this.longerThan(length);
    }

    @Override
    public boolean shorterThan(long length) {
        return $ceylon$language$Iterable$this.shorterThan(length);
    }

    @Override
    public boolean getEmpty() {
        return iterator().next() == finished_.get_();
    }

    @Override
    @Ignore
    public String getFirst() {
    	return (String) $ceylon$language$Iterable$this.getFirst();
    }
    @Override @Ignore
    public String getLast() {
        return (String) $ceylon$language$Iterable$this.getLast();
    }

    @Override
    @Ignore
    public Iterable<? extends String, ?> 
    getRest() {
    	return $ceylon$language$Iterable$this.getRest();
    }

    @Override @Ignore
    public Iterable<? extends String, ?> 
    takingWhile(Callable<? extends Boolean> take) {
        return $ceylon$language$Iterable$this.takingWhile(take);
    }
    
    @Override @Ignore
    public Iterable<? extends String, ?> 
    skippingWhile(Callable<? extends Boolean> skip) {
        return $ceylon$language$Iterable$this.skippingWhile(skip);
    }
    
    @Override
    @Ignore
    public Iterable<? extends String,?> getCycled() {
        return $ceylon$language$Iterable$this.getCycled();
    }

    @Override
    @Ignore
    public Iterable<? extends String,?> cycle(long times) {
        return $ceylon$language$Iterable$this.cycle(times);
    }
    
    @Override
    @Ignore
    public List<? extends String> repeat(long times) {
        return $ceylon$language$Iterable$this.repeat(times);
    }
    
    @Override
    @Ignore
    public Sequential<? extends String> getSequence() {
        return $ceylon$language$Iterable$this.getSequence();
    }
    @Override @Ignore
    public String find(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.find(f);
    }
    @Override @Ignore
    public String findLast(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.findLast(f);
    }
    @Override
    @Ignore
    public Sequential<? extends String> sort(Callable<? extends Comparison> f) {
        return $ceylon$language$Iterable$this.sort(f);
    }
    @Override
    @Ignore
    public <Result> Iterable<? extends Result, ?> 
    map(@Ignore TypeDescriptor $reifiedResult, Callable<? extends Result> f) {
        return $ceylon$language$Iterable$this.map($reifiedResult, f);
    }
    @Override
    @Ignore
    public Iterable<? extends String, ?> 
    filter(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.filter(f);
    }
    @Override
    @Ignore
    public <Result> Sequential<? extends Result> 
    collect(@Ignore TypeDescriptor $reifiedResult, Callable<? extends Result> f) {
        return $ceylon$language$Iterable$this.collect($reifiedResult,f);
    }
    @Override
    @Ignore
    public Sequential<? extends String> 
    select(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.select(f);
    }
    @Override
    @Ignore
    public <Result> Result 
    fold(@Ignore TypeDescriptor $reifiedResult, 
    		Result ini, Callable<? extends Result> f) {
        return $ceylon$language$Iterable$this.fold($reifiedResult, ini, f);
    }
    @Override
    @Ignore
    public <Result> java.lang.Object 
    reduce(@Ignore TypeDescriptor $reifiedResult, 
    		Callable<? extends Result> f) {
        return $ceylon$language$Iterable$this.reduce($reifiedResult, f);
    }
    @Override @Ignore
    public boolean any(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.any(f);
    }
    @Override @Ignore
    public boolean every(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.every(f);
    }
    @Override @Ignore
    public Iterable<? extends String, ?> 
    skipping(long n) {
        return $ceylon$language$Iterable$this.skipping(n);
    }
    @Override @Ignore
    public Iterable<? extends String, ?> 
    taking(long n) {
        return $ceylon$language$Iterable$this.taking(n);
    }
    @Override @Ignore
    public Iterable<? extends String, ?> 
    by(long n) {
        return $ceylon$language$Iterable$this.by(n);
    }
    @Override @Ignore
    public long count(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.count(f);
    }
    @Override @Ignore
    public Iterable<? extends String, ?> 
    getCoalesced() {
        return $ceylon$language$Iterable$this.getCoalesced();
    }
    @Override @Ignore
    public Iterable<? extends Entry<? extends Integer, ? extends String>, 
    		?> 
    getIndexed() {
        return $ceylon$language$Iterable$this.getIndexed();
    }
    @Override @Ignore public <Other,Absent> Iterable 
    chain(@Ignore TypeDescriptor $reifiedOther, 
    		@Ignore TypeDescriptor $reifiedOtherAbsent, 
    		Iterable<? extends Other, ? extends Absent> other) {
        return $ceylon$language$Iterable$this.chain($reifiedOther, 
        		$reifiedOtherAbsent, other);
    }
    @Override @Ignore 
    public <Other> Iterable 
    following(@Ignore TypeDescriptor $reifiedOther, Other other) {
        return $ceylon$language$Iterable$this.following($reifiedOther, other);
    }
    @Override @Ignore
    public <Default> Iterable<?,?> 
    defaultNullElements(@Ignore TypeDescriptor $reifiedDefault, 
    		Default defaultValue) {
        return $ceylon$language$Iterable$this.defaultNullElements($reifiedDefault, 
        		defaultValue);
    }
    @Override @Ignore
    public boolean contains(@Name("element") java.lang.Object element) {
        return $ceylon$language$Iterable$this.contains(element);
    }
    @Override @Ignore
    public boolean containsEvery(
            @Name("elements") 
            @TypeInfo("ceylon.language::Iterable<ceylon.language::Object,ceylon.language::Null>") 
            Iterable<?,?> elements) {
        return $ceylon$language$Category$this.containsEvery(elements);
    }
    @Override @Ignore
    public boolean containsAny(
            @Name("elements") 
            @TypeInfo("ceylon.language::Iterable<ceylon.language::Object,ceylon.language::Null>") 
            Iterable<?,?> elements) {
        return $ceylon$language$Category$this.containsAny(elements);
    }
    
    @Override
    public java.lang.String toString() {
        return $ceylon$language$Iterable$this.toString();
    }
    
    @Override
    @Ignore
    public TypeDescriptor $getType$() {
        return TypeDescriptor.klass(StringTokens.class);
    }
}