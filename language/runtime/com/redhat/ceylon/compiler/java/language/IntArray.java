package com.redhat.ceylon.compiler.java.language;

import javax.annotation.Generated;

import ceylon.language.Array;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Defaulted;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.ValueType;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

import ceylon.language.Callable;
import ceylon.language.Category$impl;
import ceylon.language.Comparison;
import ceylon.language.Entry;
import ceylon.language.Finished;
import ceylon.language.Iterable;
import ceylon.language.Iterable$impl;
import ceylon.language.Iterator;
import ceylon.language.List;
import ceylon.language.Null;
import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.finished_;
import ceylon.language.impl.BaseIterable;

/*
 * THIS IS A GENERATED FILE - DO NOT EDIT 
 */
/**
 * A Ceylon schema for a Java <code>int[]</code>.
 *
 * This type is never instantiated, it is completely erased to 
 * <code>int[]</code>.
 * 
 * The {@link #get(int)}, {@link #set(int,int)}, {@link #length size} 
 * methods and the constructor are also completely erased to Java array 
 * operators, or {@link Util#fillArray(int[],int)} in the case 
 * that an initial element is specified.
 * 
 * Only the value type static methods are really invoked.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
/*
 * THIS IS A GENERATED FILE - DO NOT EDIT 
 */
@Ceylon(major = 7)
@Class
@ValueType
@Generated(value="ant")
public final class IntArray implements ReifiedType {
    
    @Ignore
    public final static TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(IntArray.class);

    public IntArray(@Name("size") int size, @TypeInfo("ceylon.language::Integer") @Defaulted @Name("element") int element){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public IntArray(@Name("size") int size){
        throw Util.makeJavaArrayWrapperException();
    }

    public int get(@Name("index") int index) {
        throw Util.makeJavaArrayWrapperException();
    }

    public void set(@Name("index") int index, @Name("element") int element) {
        throw Util.makeJavaArrayWrapperException();
    }

    @Name("size")
    public final int length = 0;

    @Ignore
    public static ceylon.language.Array<java.lang.Integer> getArray(int[] array){
        return Array.instance(array);
    }

    @TypeInfo("ceylon.language::Array<java.lang::Integer>")
    public ceylon.language.Array<java.lang.Integer> getArray(){
        throw Util.makeJavaArrayWrapperException();
    }
    
    @Ignore
    public static ceylon.language.Array<ceylon.language.Character> getCodePointArray(int[] array){
        return Array.instanceForCodePoints(array);
    }

    @TypeInfo("ceylon.language::Array<ceylon.language::Character>")
    public ceylon.language.Array<ceylon.language.Character> getCodePointArray(){
        throw Util.makeJavaArrayWrapperException();
    }
    
    @Ignore
    public static void copyTo(int[] array, int[] destination){
        System.arraycopy(array, 0, destination, 0, array.length);
    }

    @Ignore
    public static void copyTo(int[] array, int[] destination, int sourcePosition){
        System.arraycopy(array, sourcePosition, destination, 0, array.length-sourcePosition);
    }

    @Ignore
    public static void copyTo(int[] array, int[] destination, int sourcePosition, int destinationPosition){
        System.arraycopy(array, sourcePosition, destination, destinationPosition, array.length-sourcePosition);
    }

    @Ignore
    public static void copyTo(int[] array, int[] destination, int sourcePosition, int destinationPosition, int length){
        System.arraycopy(array, sourcePosition, destination, destinationPosition, length);
    }

    @Ignore
    public int copyTo$sourcePosition(int[] destination){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public int copyTo$destinationPosition(int[] destination, int sourcePosition){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public int copyTo$length(int[] destination, int sourcePosition, int destinationPosition){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public void copyTo(int[] destination){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public void copyTo(int[] destination, 
                       int sourcePosition){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public void copyTo(int[] destination, 
                       int sourcePosition, 
                       int destinationPosition){
        throw Util.makeJavaArrayWrapperException();
    }

    public void copyTo(@Name("destination") int[] destination, 
                       @Name("sourcePosition") @Defaulted int sourcePosition, 
                       @Name("destinationPosition") @Defaulted int destinationPosition, 
                       @Name("length") @Defaulted int length){
        throw Util.makeJavaArrayWrapperException();
    }
    
    @Ignore
    @Override
    public TypeDescriptor $getType$() {
        throw Util.makeJavaArrayWrapperException();
    }

    @Override
    public boolean equals(@Name("that") java.lang.Object that) {
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public static boolean equals(int[] value, java.lang.Object that) {
        return value.equals(that);
    }

    @Override
    public int hashCode() {
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public static int hashCode(int[] value) {
        return value.hashCode();
    }

    @Override
    public java.lang.String toString() {
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public static java.lang.String toString(int[] value) {
        return value.toString();
    }
    
    public int[] $clone() {
        throw Util.makeJavaArrayWrapperException();
    }
    
    @Ignore
    public static int[] $clone(int[] value) {
        return value.clone();
    }
    
    public IntArrayIterable getIterable() {
        throw Util.makeJavaArrayWrapperException();
    }
    
    @Ignore
    public static IntArrayIterable getIterable(int[] value) {
        return new IntArrayIterable(value);
    }
    
    /* Implement Iterable */

    public static class IntArrayIterable extends BaseIterable<ceylon.language.Integer, ceylon.language.Null> {
        
        /** The array over which we iterate */
        private final int[] array;
        /** The index where iteration starts */
        private final int start;
        /** The step size of iteration */
        private final int step;
        /** The index one beyond where iteration ends */
        private final int end;
        
        @Ignore
        public IntArrayIterable(int[] array) {
            this(array, 0, array.length, 1);
        }
        
        @Ignore
        private IntArrayIterable(int[] array, int start, int end, int step) {
        	super(ceylon.language.Integer.$TypeDescriptor$, Null.$TypeDescriptor$);
            if (start < 0) {
                throw new ceylon.language.AssertionError("start must be positive");
            }
            if (end < 0) {
                throw new ceylon.language.AssertionError("end must be positive");
            }
            if (step <= 0) {
                throw new ceylon.language.AssertionError("step size must be greater than zero");
            }
            
            this.array = array;
            this.start = start;
            this.end = end;
            this.step = step;
        }
        
        @Override
        public boolean containsAny(Iterable<? extends Object, ? extends Object> arg0) {
            Iterator<? extends Object> iter = arg0.iterator();
            Object item;
            while (!((item = iter.next()) instanceof Finished)) {
                if (item instanceof ceylon.language.Integer) {
                    for (int ii = this.start; ii < this.end; ii+=this.step) {
                        if (array[ii] == ((ceylon.language.Integer)item).longValue()) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
        
        @Override
        public boolean containsEvery(
                Iterable<? extends Object, ? extends Object> arg0) {
            Iterator<? extends Object> iter = arg0.iterator();
            Object item;
            OUTER: while (!((item = iter.next()) instanceof Finished)) {
                if (item instanceof ceylon.language.Integer) {
                    for (int ii = this.start; ii < this.end; ii+=this.step) {
                        if (array[ii] == ((ceylon.language.Integer)item).longValue()) {
                            continue OUTER;
                        }
                    }
                } 
                return false;
            }
            return true;
        }
        
        @Override
        public boolean any(Callable<? extends ceylon.language.Boolean> arg0) {
            for (int ii=this.start; ii < this.end; ii+=this.step) {
                if (arg0.$call$(ceylon.language.Integer.instance(array[ii])).booleanValue()) {
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public boolean contains(Object item) {
            for (int ii = this.start; ii < this.end; ii+=this.step) {
                if (item instanceof ceylon.language.Integer 
                        && array[ii] == ((ceylon.language.Integer)item).longValue()) {
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public <Default> Iterable<? extends Object, ? extends Null> defaultNullElements(
                @Ignore
                TypeDescriptor $reified$Default, 
                Default defaultValue) {
            return this;
        }
        
        @Override
        public Iterable<? extends ceylon.language.Integer, ? extends Object> getCoalesced() {
            return this;
        }
        
        @Override
        public boolean getEmpty() {
            return this.end <= this.start;
        }
        
        @Override
        public long getSize() {
            return java.lang.Math.max(0, (this.end-this.start+this.step-1)/this.step);
        }
        
        @Override
        public ceylon.language.Integer getFirst() {
            return this.getEmpty() ? null : ceylon.language.Integer.instance(this.array[this.start]);
        }
        
        
        @Override
        public ceylon.language.Integer getLast() {
            return this.getEmpty() ? null : ceylon.language.Integer.instance(this.array[this.end-1]);
        }
        
        @Override
        public IntArrayIterable getRest() {
            return new IntArrayIterable(this.array, this.start+1, this.end, this.step);
        }
        
        @Override
        public Sequential<? extends ceylon.language.Integer> sequence() {
            // Note: Sequential is immutable, and we don't know where the array
            // came from, so however we create the sequence we must take a copy
        	//TODO: use a more efficient imple, like in List.sequence()
            return super.sequence();
        }
        
        @Override
        public Iterator<? extends ceylon.language.Integer> iterator() {
            if (this.getEmpty()) {
                return (Iterator)ceylon.language.emptyIterator_.get_();
            }
            return new Iterator<ceylon.language.Integer>() {
                
                private int index = IntArrayIterable.this.start;
                
                @Override
                public Object next() {
                    if (index < IntArrayIterable.this.end) {
                        ceylon.language.Integer result = ceylon.language.Integer.instance(IntArrayIterable.this.array[index]);
                        index += IntArrayIterable.this.step;
                        return result;
                    } else {
                        return finished_.get_();
                    }
                }
            };
        }
        
        @Override
        public boolean longerThan(long length) {
            return this.getSize() > length;
        }
        
        @Override
        public boolean shorterThan(long length) {
            return this.getSize() < length;
        }
        
        @Override
        public IntArrayIterable by(long step) {
            return new IntArrayIterable(this.array, 
                    this.start, 
                    this.end, 
                    com.redhat.ceylon.compiler.java.Util.toInt(this.step*step));
        }
        
        @Override
        public IntArrayIterable skip(long skip) {
            if (skip <= 0) {
                return this;
            }
            return new IntArrayIterable(this.array, 
                    com.redhat.ceylon.compiler.java.Util.toInt(this.start+skip*this.step), 
                    this.end, 
                    this.step);
        }
        
        @Override
        public IntArrayIterable take(long take) {
            if (take >= this.getSize()) {
                return this;
            }
            return new IntArrayIterable(this.array, 
                    this.start, 
                    com.redhat.ceylon.compiler.java.Util.toInt(take*this.step+1), 
                    this.step);
        }
        
    }
    
}
