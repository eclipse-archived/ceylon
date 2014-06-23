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
 * @GeneratedWarning@ 
 */
/**
 * A Ceylon schema for a Java <code>@PrimitiveType@[]</code>.
 *
 * This type is never instantiated, it is completely erased to 
 * <code>@PrimitiveType@[]</code>.
 * 
 * The {@link #get(int)}, {@link #set(int,@PrimitiveType@)}, {@link #length size} 
 * methods and the constructor are also completely erased to Java array 
 * operators, or {@link Util#fillArray(@PrimitiveType@[],@PrimitiveType@)} in the case 
 * that an initial element is specified.
 * 
 * Only the value type static methods are really invoked.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
/*
 * @GeneratedWarning@ 
 */
@Ceylon(major = 7)
@Class
@ValueType
@Generated(value="ant")
public final class @Classname@ implements ReifiedType {
    
    @Ignore
    public final static TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(@Classname@.class);

    public @Classname@(@Name("size") int size, @TypeInfo("@BoxedTypeName@") @Defaulted @Name("element") @PrimitiveType@ element){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public @Classname@(@Name("size") int size){
        throw Util.makeJavaArrayWrapperException();
    }

    public @PrimitiveType@ get(@Name("index") int index) {
        throw Util.makeJavaArrayWrapperException();
    }

    public void set(@Name("index") int index, @Name("element") @PrimitiveType@ element) {
        throw Util.makeJavaArrayWrapperException();
    }

    @Name("size")
    public final int length = 0;

    @Ignore
    public static ceylon.language.Array<@JavaBoxedType@> getArray(@PrimitiveType@[] array){
        return Array.instance(array);
    }

    @TypeInfo("ceylon.language::Array<@JavaBoxedTypeName@>")
    public ceylon.language.Array<@JavaBoxedType@> getArray(){
        throw Util.makeJavaArrayWrapperException();
    }
    @OptionalStart@
    @Ignore
    public static ceylon.language.Array<@CeylonArrayGetterType@> @CeylonArrayGetter@(@PrimitiveType@[] array){
        return Array.@ArrayInstanceWrapper@(array);
    }

    @TypeInfo("ceylon.language::Array<@CeylonArrayGetterTypeName@>")
    public ceylon.language.Array<@CeylonArrayGetterType@> @CeylonArrayGetter@(){
        throw Util.makeJavaArrayWrapperException();
    }
    @OptionalEnd@
    @Ignore
    public static void copyTo(@PrimitiveType@[] array, @PrimitiveType@[] destination){
        System.arraycopy(array, 0, destination, 0, array.length);
    }

    @Ignore
    public static void copyTo(@PrimitiveType@[] array, @PrimitiveType@[] destination, int sourcePosition){
        System.arraycopy(array, sourcePosition, destination, 0, array.length-sourcePosition);
    }

    @Ignore
    public static void copyTo(@PrimitiveType@[] array, @PrimitiveType@[] destination, int sourcePosition, int destinationPosition){
        System.arraycopy(array, sourcePosition, destination, destinationPosition, array.length-sourcePosition);
    }

    @Ignore
    public static void copyTo(@PrimitiveType@[] array, @PrimitiveType@[] destination, int sourcePosition, int destinationPosition, int length){
        System.arraycopy(array, sourcePosition, destination, destinationPosition, length);
    }

    @Ignore
    public int copyTo$sourcePosition(@PrimitiveType@[] destination){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public int copyTo$destinationPosition(@PrimitiveType@[] destination, int sourcePosition){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public int copyTo$length(@PrimitiveType@[] destination, int sourcePosition, int destinationPosition){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public void copyTo(@PrimitiveType@[] destination){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public void copyTo(@PrimitiveType@[] destination, 
                       int sourcePosition){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public void copyTo(@PrimitiveType@[] destination, 
                       int sourcePosition, 
                       int destinationPosition){
        throw Util.makeJavaArrayWrapperException();
    }

    public void copyTo(@Name("destination") @PrimitiveType@[] destination, 
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
    public static boolean equals(@PrimitiveType@[] value, java.lang.Object that) {
        return value.equals(that);
    }

    @Override
    public int hashCode() {
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public static int hashCode(@PrimitiveType@[] value) {
        return value.hashCode();
    }

    @Override
    public java.lang.String toString() {
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public static java.lang.String toString(@PrimitiveType@[] value) {
        return value.toString();
    }
    
    public @PrimitiveType@[] $clone() {
        throw Util.makeJavaArrayWrapperException();
    }
    
    @Ignore
    public static @PrimitiveType@[] $clone(@PrimitiveType@[] value) {
        return value.clone();
    }
    
    public @Classname@Iterable getIterable() {
        throw Util.makeJavaArrayWrapperException();
    }
    
    @Ignore
    public static @Classname@Iterable getIterable(@PrimitiveType@[] value) {
        return new @Classname@Iterable(value);
    }
    
    /* Implement Iterable */

    public static class @Classname@Iterable extends BaseIterable<@BoxedType@, ceylon.language.Null> {
        
        /** The array over which we iterate */
        private final @PrimitiveType@[] array;
        /** The index where iteration starts */
        private final int start;
        /** The step size of iteration */
        private final int step;
        /** The index one beyond where iteration ends */
        private final int end;
        
        @Ignore
        public @Classname@Iterable(@PrimitiveType@[] array) {
            this(array, 0, array.length, 1);
        }
        
        @Ignore
        private @Classname@Iterable(@PrimitiveType@[] array, int start, int end, int step) {
        	super(@BoxedType@.$TypeDescriptor$, Null.$TypeDescriptor$);
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
                if (item instanceof @BoxedType@) {
                    for (int ii = this.start; ii < this.end; ii+=this.step) {
                        if (array[ii] == ((@BoxedType@)item).@UnboxMethod@()) {
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
                if (item instanceof @BoxedType@) {
                    for (int ii = this.start; ii < this.end; ii+=this.step) {
                        if (array[ii] == ((@BoxedType@)item).@UnboxMethod@()) {
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
                if (arg0.$call$(@BoxedType@.instance(array[ii])).booleanValue()) {
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public boolean contains(Object item) {
            for (int ii = this.start; ii < this.end; ii+=this.step) {
                if (item instanceof @BoxedType@ 
                        && array[ii] == ((@BoxedType@)item).@UnboxMethod@()) {
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
        public Iterable<? extends @BoxedType@, ? extends Object> getCoalesced() {
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
        public @BoxedType@ getFirst() {
            return this.getEmpty() ? null : @BoxedType@.instance(this.array[this.start]);
        }
        
        
        @Override
        public @BoxedType@ getLast() {
            return this.getEmpty() ? null : @BoxedType@.instance(this.array[this.end-1]);
        }
        
        @Override
        public @Classname@Iterable getRest() {
            return new @Classname@Iterable(this.array, this.start+1, this.end, this.step);
        }
        
        @Override
        public Sequential<? extends @BoxedType@> sequence() {
            // Note: Sequential is immutable, and we don't know where the array
            // came from, so however we create the sequence we must take a copy
        	//TODO: use a more efficient imple, like in List.sequence()
            return super.sequence();
        }
        
        @Override
        public Iterator<? extends @BoxedType@> iterator() {
            if (this.getEmpty()) {
                return (Iterator)ceylon.language.emptyIterator_.get_();
            }
            return new Iterator<@BoxedType@>() {
                
                private int index = @Classname@Iterable.this.start;
                
                @Override
                public Object next() {
                    if (index < @Classname@Iterable.this.end) {
                        @BoxedType@ result = @BoxedType@.instance(@Classname@Iterable.this.array[index]);
                        index += @Classname@Iterable.this.step;
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
        public @Classname@Iterable by(long step) {
            return new @Classname@Iterable(this.array, 
                    this.start, 
                    this.end, 
                    com.redhat.ceylon.compiler.java.Util.toInt(this.step*step));
        }
        
        @Override
        public @Classname@Iterable skip(long skip) {
            if (skip <= 0) {
                return this;
            }
            return new @Classname@Iterable(this.array, 
                    com.redhat.ceylon.compiler.java.Util.toInt(this.start+skip*this.step), 
                    this.end, 
                    this.step);
        }
        
        @Override
        public @Classname@Iterable take(long take) {
            if (take >= this.getSize()) {
                return this;
            }
            return new @Classname@Iterable(this.array, 
                    this.start, 
                    com.redhat.ceylon.compiler.java.Util.toInt(take*this.step+1), 
                    this.step);
        }
        
    }
    
}
