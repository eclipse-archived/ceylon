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

import ceylon.language.ArraySequence;
import ceylon.language.Callable;
import ceylon.language.Category$impl;
import ceylon.language.Comparison;
import ceylon.language.Entry;
import ceylon.language.Finished;
import ceylon.language.Iterable;
import ceylon.language.Iterable$impl;
import ceylon.language.Iterator;
import ceylon.language.Iterator$impl;
import ceylon.language.List;
import ceylon.language.Null;
import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.finished_;

/*
 * THIS IS A GENERATED FILE - DO NOT EDIT 
 */
/**
 * A Ceylon schema for a Java <code>float[]</code>.
 *
 * This type is never instantiated, it is completely erased to 
 * <code>float[]</code>.
 * 
 * The {@link #get(int)}, {@link #set(int,float)}, {@link #length size} 
 * methods and the constructor are also completely erased to Java array 
 * operators, or {@link Util#fillArray(float[],float)} in the case 
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
public final class FloatArray implements ReifiedType {
    
    @Ignore
    public final static TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(FloatArray.class);

    public FloatArray(@Name("size") int size, @TypeInfo("ceylon.language::Float") @Defaulted @Name("element") float element){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public FloatArray(@Name("size") int size){
        throw Util.makeJavaArrayWrapperException();
    }

    public float get(@Name("index") int index) {
        throw Util.makeJavaArrayWrapperException();
    }

    public void set(@Name("index") int index, @Name("element") float element) {
        throw Util.makeJavaArrayWrapperException();
    }

    @Name("size")
    public final int length = 0;

    @Ignore
    public static ceylon.language.Array<java.lang.Float> getArray(float[] array){
        return Array.instance(array);
    }

    @TypeInfo("ceylon.language::Array<java.lang::Float>")
    public ceylon.language.Array<java.lang.Float> getArray(){
        throw Util.makeJavaArrayWrapperException();
    }
    /*
    @Ignore
    public static ceylon.language.Array<NOT USED> NOT USED(float[] array){
        return Array.NOT USED(array);
    }

    @TypeInfo("ceylon.language::Array<NOT USED>")
    public ceylon.language.Array<NOT USED> NOT USED(){
        throw Util.makeJavaArrayWrapperException();
    }
    */
    @Ignore
    public static void copyTo(float[] array, float[] destination){
        System.arraycopy(array, 0, destination, 0, array.length);
    }

    @Ignore
    public static void copyTo(float[] array, float[] destination, int sourcePosition){
        System.arraycopy(array, sourcePosition, destination, 0, array.length-sourcePosition);
    }

    @Ignore
    public static void copyTo(float[] array, float[] destination, int sourcePosition, int destinationPosition){
        System.arraycopy(array, sourcePosition, destination, destinationPosition, array.length-sourcePosition);
    }

    @Ignore
    public static void copyTo(float[] array, float[] destination, int sourcePosition, int destinationPosition, int length){
        System.arraycopy(array, sourcePosition, destination, destinationPosition, length);
    }

    @Ignore
    public int copyTo$sourcePosition(float[] destination){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public int copyTo$destinationPosition(float[] destination, int sourcePosition){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public int copyTo$length(float[] destination, int sourcePosition, int destinationPosition){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public void copyTo(float[] destination){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public void copyTo(float[] destination, 
                       int sourcePosition){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public void copyTo(float[] destination, 
                       int sourcePosition, 
                       int destinationPosition){
        throw Util.makeJavaArrayWrapperException();
    }

    public void copyTo(@Name("destination") float[] destination, 
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
    public static boolean equals(float[] value, java.lang.Object that) {
        return value.equals(that);
    }

    @Override
    public int hashCode() {
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public static int hashCode(float[] value) {
        return value.hashCode();
    }

    @Override
    public java.lang.String toString() {
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public static java.lang.String toString(float[] value) {
        return value.toString();
    }
    
    public float[] $clone() {
        throw Util.makeJavaArrayWrapperException();
    }
    
    @Ignore
    public static float[] $clone(float[] value) {
        return value.clone();
    }
    
    public FloatArrayIterable getIterable() {
        throw Util.makeJavaArrayWrapperException();
    }
    
    @Ignore
    public static FloatArrayIterable getIterable(float[] value) {
        return new FloatArrayIterable(value);
    }
    
    /* Implement Iterable */

    public static class FloatArrayIterable implements ReifiedType, Iterable<ceylon.language.Float, ceylon.language.Null> {
        private final Category$impl<Object> $ceylon$language$Category$this = new Category$impl<Object>(ceylon.language.Object.$TypeDescriptor$, this);
        private final Iterable$impl<ceylon.language.Float, Null> $ceylon$language$Iterable$this = new Iterable$impl<ceylon.language.Float, Null>(ceylon.language.Float.$TypeDescriptor$, Null.$TypeDescriptor$, this);
        
        /** The array over which we iterate */
        private final float[] array;
        /** The index where iteration starts */
        private final int start;
        /** The step size of iteration */
        private final int step;
        /** The index one beyond where iteration ends */
        private final int end;
        
        @Override
        public TypeDescriptor $getType$() {
            throw Util.makeJavaArrayWrapperException();
        }
        
        @Ignore
        public FloatArrayIterable(float[] array) {
            this(array, 0, array.length, 1);
        }
        
        @Ignore
        private FloatArrayIterable(float[] array, int start, int end, int step) {
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
        public Category$impl<? super Object> $ceylon$language$Category$impl() {
            return $ceylon$language$Category$this;
        }
        
        @Override
        public Iterable$impl<? extends ceylon.language.Float, ? extends Null> $ceylon$language$Iterable$impl() {
            return $ceylon$language$Iterable$this;
        }
        
        @Override
        public boolean containsAny(Iterable<? extends Object, ? extends Object> arg0) {
            Iterator<? extends Object> iter = arg0.iterator();
            Object item;
            while (!((item = iter.next()) instanceof Finished)) {
                if (item instanceof ceylon.language.Float) {
                    for (int ii = this.start; ii < this.end; ii+=this.step) {
                        if (array[ii] == ((ceylon.language.Float)item).doubleValue()) {
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
                if (item instanceof ceylon.language.Float) {
                    for (int ii = this.start; ii < this.end; ii+=this.step) {
                        if (array[ii] == ((ceylon.language.Float)item).doubleValue()) {
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
                if (arg0.$call$(ceylon.language.Float.instance(array[ii])).booleanValue()) {
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public boolean contains(Object item) {
            for (int ii = this.start; ii < this.end; ii+=this.step) {
                if (item instanceof ceylon.language.Float 
                        && array[ii] == ((ceylon.language.Float)item).doubleValue()) {
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
        public Iterable<? extends ceylon.language.Float, ? extends Object> getCoalesced() {
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
        public ceylon.language.Float getFirst() {
            return this.getEmpty() ? null : ceylon.language.Float.instance(this.array[this.start]);
        }
        
        
        @Override
        public ceylon.language.Float getLast() {
            return this.getEmpty() ? null : ceylon.language.Float.instance(this.array[this.end-1]);
        }
        
        @Override
        public FloatArrayIterable getRest() {
            return new FloatArrayIterable(this.array, this.start+1, this.end, this.step);
        }
        
        @Override
        public Sequential<? extends ceylon.language.Float> getSequence() {
            // Note: Sequential is immutable, and we don't know where the array
            // came from, so however we create the sequence we must take a copy
            return this.getEmpty() ? empty_.get_() : new ArraySequence(ceylon.language.Float.$TypeDescriptor$, this);
        }
        
        @Override
        public Iterator<? extends ceylon.language.Float> iterator() {
            if (this.getEmpty()) {
                return (Iterator)ceylon.language.emptyIterator_.get_();
            }
            return new Iterator<ceylon.language.Float>() {
                
                private int index = FloatArrayIterable.this.start;
                
                private final Iterator$impl<ceylon.language.Float> $ceylon$language$Iterator$this = new Iterator$impl<ceylon.language.Float>(ceylon.language.Float.$TypeDescriptor$, this);
                
                @Override
                public Iterator$impl<? extends ceylon.language.Float> $ceylon$language$Iterator$impl() {
                    return $ceylon$language$Iterator$this;
                }
                
                @Override
                public Object next() {
                    if (index < FloatArrayIterable.this.end) {
                        ceylon.language.Float result = ceylon.language.Float.instance(FloatArrayIterable.this.array[index]);
                        index += FloatArrayIterable.this.step;
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
        public FloatArrayIterable by(long step) {
            return new FloatArrayIterable(this.array, 
                    this.start, 
                    this.end, 
                    this.step*(int)step);
        }
        
        @Override
        public FloatArrayIterable skip(long skip) {
            if (skip <= 0) {
                return this;
            }
            return new FloatArrayIterable(this.array, 
                    this.start+(int)skip*this.step, 
                    this.end, 
                    this.step);
        }
        
        @Override
        public FloatArrayIterable take(long take) {
            if (take >= this.getSize()) {
                return this;
            }
            return new FloatArrayIterable(this.array, 
                    this.start, 
                    (int)take*this.step+1, 
                    this.step);
        }
        
        @Override
        public Sequential<? extends ceylon.language.Float> sort(
                final Callable<? extends Comparison> comparing) {
            return $ceylon$language$Iterable$this.sort(comparing);
        }
        
        @Override
        public Iterable<? extends ceylon.language.Float, ? extends Object> skipWhile(
                Callable<? extends ceylon.language.Boolean> skip) {
            return $ceylon$language$Iterable$this.skipWhile(skip);
        }
        
        @Override
        public Iterable<? extends ceylon.language.Float, ? extends Object> takeWhile(
                Callable<? extends ceylon.language.Boolean> take) {
            return $ceylon$language$Iterable$this.takeWhile(take);
        }
        
        @Override
        public Sequential<? extends ceylon.language.Float> select(Callable<? extends ceylon.language.Boolean> selecting) {
            return $ceylon$language$Iterable$this.select(selecting);
        }
        
        @Override
        public <Result> Iterable<? extends Result, ? extends Null> map(
                @Ignore
                TypeDescriptor $reified$Result, 
                Callable<? extends Result> collecting) {
            return $ceylon$language$Iterable$this.map($reified$Result, collecting);
        }
        
        @Override
        public <Result> Object reduce(
                @Ignore
                TypeDescriptor $reified$Result,
                Callable<? extends Result> accumulating) {
            return $ceylon$language$Iterable$this.reduce($reified$Result, accumulating);
        }
        
        @Override
        public List<? extends ceylon.language.Float> repeat(long times) {
            return $ceylon$language$Iterable$this.repeat(times);
        }
        
        @Override
        public <Other, OtherAbsent> Iterable<?,?> chain(
                @Ignore
                TypeDescriptor $reified$Other,
                @Ignore
                TypeDescriptor $reified$OtherAbsent,
                Iterable<? extends Other, ? extends OtherAbsent> other) {
            return $ceylon$language$Iterable$this.chain($reified$Other, $reified$OtherAbsent, other);
        }
        
        @Override
        public <Result> Sequential<? extends Result> collect(
                @Ignore
                TypeDescriptor $reified$Result,
                Callable<? extends Result> collecting) {
            return $ceylon$language$Iterable$this.collect($reified$Result, collecting);
        }
        
        @Override
        public long count(Callable<? extends ceylon.language.Boolean> selecting) {
            return $ceylon$language$Iterable$this.count(selecting);
        }
        
        @Override
        public Iterable<? extends ceylon.language.Float, ? extends Null> cycle(long times) {
            return $ceylon$language$Iterable$this.cycle(times);
        }
        
        @Override
        public boolean every(Callable<? extends ceylon.language.Boolean> selecting) {
            return $ceylon$language$Iterable$this.every(selecting);
        }
        
        @Override
        public Iterable<? extends ceylon.language.Float, ? extends Object> filter(
                Callable<? extends ceylon.language.Boolean> selecting) {
            return $ceylon$language$Iterable$this.filter(selecting);
        }
        
        @Override
        public ceylon.language.Float find(Callable<? extends ceylon.language.Boolean> selecting) {
            return $ceylon$language$Iterable$this.find(selecting);
        }
        
        @Override
        public ceylon.language.Float findLast(Callable<? extends ceylon.language.Boolean> selecting) {
            return $ceylon$language$Iterable$this.findLast(selecting);
        }
        
        @Override
        public <Result> Result fold(
                @Ignore
                TypeDescriptor $reified$Result, 
                Result initial,
                Callable<? extends Result> accumulating) {
            return $ceylon$language$Iterable$this.fold($reified$Result, initial, accumulating);
        }
        
        @Override
        public <Other> Iterable<? extends Object, ? extends Object> following(
                @Ignore
                TypeDescriptor $reified$Other, 
                Other head) {
            return $ceylon$language$Iterable$this.following($reified$Other, head);
        }
        
        @Override
        public Iterable<? extends ceylon.language.Float, ? extends Null> getCycled() {
            return $ceylon$language$Iterable$this.getCycled();
        }
        
        @Override
        public Iterable<? extends Entry<? extends ceylon.language.Integer, ? extends ceylon.language.Float>, ? extends Object> getIndexed() {
            return $ceylon$language$Iterable$this.getIndexed();
        }
    }
    
}
