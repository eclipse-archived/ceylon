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
        //private final Correspondence$impl<ceylon.language.Integer, ceylon.language.Float> $ceylon$language$Correspondence$this = new Correspondence$impl<ceylon.language.Integer, ceylon.language.Float>(ceylon.language.Integer.$TypeDescriptor$, ceylon.language.Float.$TypeDescriptor$, this);
        
        private final float[] array;
        
        @Override
        public TypeDescriptor $getType$() {
            throw Util.makeJavaArrayWrapperException();
        }
        
        @Ignore
        public FloatArrayIterable(float[] array) {
            this.array = array;
        }
        
        @Ignore
        public float[] arrayValue() {
            return array;
        }
        
        @Override
        public Category$impl<? super Object> $ceylon$language$Category$impl() {
            return $ceylon$language$Category$this;
        }
    
        @Override
        public Iterable$impl<? extends ceylon.language.Float, ? extends Null> $ceylon$language$Iterable$impl() {
            return $ceylon$language$Iterable$this;
        }
        
        //@Override
        //public Correspondence$impl<ceylon.language.Integer, ceylon.language.Float> $ceylon$language$Correspondence$impl() {
        //    return $ceylon$language$Correspondence$this;
        //}
    
        @Override
        public boolean containsAny(Iterable<? extends Object, ? extends Object> arg0) {
            Iterator<? extends Object> iter = arg0.iterator();
            Object item;
            while (!((item = iter.next()) instanceof Finished)) {
                if (item instanceof ceylon.language.Float) {
                    for (int ii = 0; ii < array.length; ii++) {
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
                    for (int ii = 0; ii < array.length; ii++) {
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
            for (int ii=0; ii < array.length; ii++) {
                if (arg0.$call$(ceylon.language.Float.instance(array[ii])).booleanValue()) {
                    return true;
                }
            }
            return false;
        }
    
        @Override
        public Iterable<? extends ceylon.language.Float, ? extends Null> by(long step) {
            // TODO optimizable by allocating a new array, and filling it in a for loop
            return $ceylon$language$Iterable$this.by(step);
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
        public boolean contains(Object item) {
            for (int ii = 0; ii < array.length; ii++) {
                if (item instanceof ceylon.language.Float 
                        && array[ii] == ((ceylon.language.Float)item).doubleValue()) {
                    return true;
                }
            }
            return false;
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
        public <Default> Iterable<? extends Object, ? extends Null> defaultNullElements(
                @Ignore
                TypeDescriptor $reified$Default, 
                Default defaultValue) {
            return this;
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
        public Iterable<? extends ceylon.language.Float, ? extends Object> getCoalesced() {
            return this;
        }
    
        @Override
        public Iterable<? extends ceylon.language.Float, ? extends Null> getCycled() {
            return $ceylon$language$Iterable$this.getCycled();
        }
    
        @Override
        public boolean getEmpty() {
            return array.length == 0;
        }
    
        @Override
        public Object getFirst() {
            return array.length == 0 ? null : array[0];
        }
    
        @Override
        public Iterable<? extends Entry<? extends ceylon.language.Integer, ? extends ceylon.language.Float>, ? extends Object> getIndexed() {
            return $ceylon$language$Iterable$this.getIndexed();
        }
    
        @Override
        public Object getLast() {
            return array.length == 0 ? null : array[array.length-1];
        }
    
        @Override
        public Iterable<? extends ceylon.language.Float, ? extends Object> getRest() {
            return $ceylon$language$Iterable$this.getRest();
        }
    
        @Override
        public Sequential<? extends ceylon.language.Float> getSequence() {
            return array.length == 0 ? empty_.get_() : new ArraySequence(ceylon.language.Float.$TypeDescriptor$, this);
        }
    
        @Ignore
        @Override
        public long getSize() {
            return array.length;
        }
    
        @Override
        public Iterator<? extends ceylon.language.Float> iterator() {
            return new Iterator<ceylon.language.Float>() {
    
                private int index = 0;
                
                private final Iterator$impl<ceylon.language.Float> $ceylon$language$Iterator$this = new Iterator$impl<ceylon.language.Float>(ceylon.language.Float.$TypeDescriptor$, this);
                
                @Override
                public Iterator$impl<? extends ceylon.language.Float> $ceylon$language$Iterator$impl() {
                    return $ceylon$language$Iterator$this;
                }
    
                @Override
                public Object next() {
                    if (index < array.length) {
                        return ceylon.language.Float.instance(array[index++]);
                    } else {
                        return finished_.get_();
                    }
                }
            };
        }
    
        @Override
        public boolean longerThan(long length) {
            return array.length > length;
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
        public Sequential<? extends ceylon.language.Float> select(Callable<? extends ceylon.language.Boolean> selecting) {
            return $ceylon$language$Iterable$this.select(selecting);
        }
    
        @Override
        public boolean shorterThan(long length) {
            return array.length < length;
        }
    
        @Override
        public Iterable<? extends ceylon.language.Float, ? extends Object> skipping(long skip) {
            return $ceylon$language$Iterable$this.skipping(skip);
        }
    
        @Override
        public Iterable<? extends ceylon.language.Float, ? extends Object> skippingWhile(
                Callable<? extends ceylon.language.Boolean> skip) {
            return $ceylon$language$Iterable$this.skippingWhile(skip);
        }
    
        @Override
        public Sequential<? extends ceylon.language.Float> sort(
                final Callable<? extends Comparison> comparing) {
            return $ceylon$language$Iterable$this.sort(comparing);
        }
    
        @Override
        public Iterable<? extends ceylon.language.Float, ? extends Object> taking(long take) {
            return $ceylon$language$Iterable$this.taking(take);
        }
    
        @Override
        public Iterable<? extends ceylon.language.Float, ? extends Object> takingWhile(
                Callable<? extends ceylon.language.Boolean> take) {
            return $ceylon$language$Iterable$this.takingWhile(take);
        }
        
        /* Implement Correspondence */
        /*
        @Override
        @Ignore
        @TypeInfo("ceylon.language::Float|ceylon.language::Null")
        public ceylon.language.Float get(
                @Name("key")
                ceylon.language.Integer key) {
            return ceylon.language.Float.instance(array[(int)key.longValue()]);
        }
        
        @Override
        public boolean defines(
                @Name("key")
                ceylon.language.Integer key) {
            int index = (int)key.longValue();
            return 0 <= index && index < array.length;
        }
       
        @Override
        @TypeInfo("ceylon.language::Sequential<ceylon.language.Integer>")
        public ceylon.language.Sequential<ceylon.language.Integer> getKeys() {
            if (array.length == 0) {
                return (ceylon.language.Sequential)ceylon.language.empty_.get_();
            } else {
                return new ceylon.language.Range(ceylon.language.Integer.$TypeDescriptor$, 
                        ceylon.language.Integer.instance(0), 
                        ceylon.language.Integer.instance(array.length-1));
            }
        }
        
        @Override
        public boolean definesEvery(ceylon.language.Iterable<? extends ceylon.language.Integer, ? extends Object> keys) {
            return $ceylon$language$Correspondence$this.definesEvery(keys);
        }
        
        @Override
        public boolean definesAny(ceylon.language.Iterable<? extends ceylon.language.Integer, ? extends Object> keys) {
            return $ceylon$language$Correspondence$this.definesAny(keys);
        }
        
        @Override
        @TypeInfo("ceylon.language::Sequential<ceylon.language::Float|ceylon.language.Null>")
        public ceylon.language.Sequential<? extends ceylon.language.Float> items(ceylon.language.Iterable<? extends ceylon.language.Integer, ? extends Object> keys) {
            return $ceylon$language$Correspondence$this.items(keys);
        }*/
    }
   

}
