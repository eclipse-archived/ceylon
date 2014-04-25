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

    public static class IntArrayIterable implements ReifiedType, Iterable<ceylon.language.Integer, ceylon.language.Null> {
        private final Category$impl<Object> $ceylon$language$Category$this = new Category$impl<Object>(ceylon.language.Object.$TypeDescriptor$, this);
        private final Iterable$impl<ceylon.language.Integer, Null> $ceylon$language$Iterable$this = new Iterable$impl<ceylon.language.Integer, Null>(ceylon.language.Integer.$TypeDescriptor$, Null.$TypeDescriptor$, this);
        //private final Correspondence$impl<ceylon.language.Integer, ceylon.language.Integer> $ceylon$language$Correspondence$this = new Correspondence$impl<ceylon.language.Integer, ceylon.language.Integer>(ceylon.language.Integer.$TypeDescriptor$, ceylon.language.Integer.$TypeDescriptor$, this);
        
        private final int[] array;
        
        @Override
        public TypeDescriptor $getType$() {
            throw Util.makeJavaArrayWrapperException();
        }
        
        @Ignore
        public IntArrayIterable(int[] array) {
            this.array = array;
        }
        
        @Ignore
        public int[] arrayValue() {
            return array;
        }
        
        @Override
        public Category$impl<? super Object> $ceylon$language$Category$impl() {
            return $ceylon$language$Category$this;
        }
    
        @Override
        public Iterable$impl<? extends ceylon.language.Integer, ? extends Null> $ceylon$language$Iterable$impl() {
            return $ceylon$language$Iterable$this;
        }
        
        //@Override
        //public Correspondence$impl<ceylon.language.Integer, ceylon.language.Integer> $ceylon$language$Correspondence$impl() {
        //    return $ceylon$language$Correspondence$this;
        //}
    
        @Override
        public boolean containsAny(Iterable<? extends Object, ? extends Object> arg0) {
            Iterator<? extends Object> iter = arg0.iterator();
            Object item;
            while (!((item = iter.next()) instanceof Finished)) {
                if (item instanceof ceylon.language.Integer) {
                    for (int ii = 0; ii < array.length; ii++) {
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
                    for (int ii = 0; ii < array.length; ii++) {
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
            for (int ii=0; ii < array.length; ii++) {
                if (arg0.$call$(ceylon.language.Integer.instance(array[ii])).booleanValue()) {
                    return true;
                }
            }
            return false;
        }
    
        @Override
        public Iterable<? extends ceylon.language.Integer, ? extends Null> by(long step) {
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
                if (item instanceof ceylon.language.Integer 
                        && array[ii] == ((ceylon.language.Integer)item).longValue()) {
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
        public Iterable<? extends ceylon.language.Integer, ? extends Null> cycle(long times) {
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
        public Iterable<? extends ceylon.language.Integer, ? extends Object> filter(
                Callable<? extends ceylon.language.Boolean> selecting) {
            return $ceylon$language$Iterable$this.filter(selecting);
        }
    
        @Override
        public ceylon.language.Integer find(Callable<? extends ceylon.language.Boolean> selecting) {
            return $ceylon$language$Iterable$this.find(selecting);
        }
    
        @Override
        public ceylon.language.Integer findLast(Callable<? extends ceylon.language.Boolean> selecting) {
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
        public Iterable<? extends ceylon.language.Integer, ? extends Object> getCoalesced() {
            return this;
        }
    
        @Override
        public Iterable<? extends ceylon.language.Integer, ? extends Null> getCycled() {
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
        public Iterable<? extends Entry<? extends ceylon.language.Integer, ? extends ceylon.language.Integer>, ? extends Object> getIndexed() {
            return $ceylon$language$Iterable$this.getIndexed();
        }
    
        @Override
        public Object getLast() {
            return array.length == 0 ? null : array[array.length-1];
        }
    
        @Override
        public Iterable<? extends ceylon.language.Integer, ? extends Object> getRest() {
            return $ceylon$language$Iterable$this.getRest();
        }
    
        @Override
        public Sequential<? extends ceylon.language.Integer> getSequence() {
            return array.length == 0 ? empty_.get_() : new ArraySequence(ceylon.language.Integer.$TypeDescriptor$, this);
        }
    
        @Ignore
        @Override
        public long getSize() {
            return array.length;
        }
    
        @Override
        public Iterator<? extends ceylon.language.Integer> iterator() {
            return new Iterator<ceylon.language.Integer>() {
    
                private int index = 0;
                
                private final Iterator$impl<ceylon.language.Integer> $ceylon$language$Iterator$this = new Iterator$impl<ceylon.language.Integer>(ceylon.language.Integer.$TypeDescriptor$, this);
                
                @Override
                public Iterator$impl<? extends ceylon.language.Integer> $ceylon$language$Iterator$impl() {
                    return $ceylon$language$Iterator$this;
                }
    
                @Override
                public Object next() {
                    if (index < array.length) {
                        return ceylon.language.Integer.instance(array[index++]);
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
        public List<? extends ceylon.language.Integer> repeat(long times) {
            return $ceylon$language$Iterable$this.repeat(times);
        }
    
        @Override
        public Sequential<? extends ceylon.language.Integer> select(Callable<? extends ceylon.language.Boolean> selecting) {
            return $ceylon$language$Iterable$this.select(selecting);
        }
    
        @Override
        public boolean shorterThan(long length) {
            return array.length < length;
        }
    
        @Override
        public Iterable<? extends ceylon.language.Integer, ? extends Object> skipping(long skip) {
            return $ceylon$language$Iterable$this.skipping(skip);
        }
    
        @Override
        public Iterable<? extends ceylon.language.Integer, ? extends Object> skippingWhile(
                Callable<? extends ceylon.language.Boolean> skip) {
            return $ceylon$language$Iterable$this.skippingWhile(skip);
        }
    
        @Override
        public Sequential<? extends ceylon.language.Integer> sort(
                final Callable<? extends Comparison> comparing) {
            return $ceylon$language$Iterable$this.sort(comparing);
        }
    
        @Override
        public Iterable<? extends ceylon.language.Integer, ? extends Object> taking(long take) {
            return $ceylon$language$Iterable$this.taking(take);
        }
    
        @Override
        public Iterable<? extends ceylon.language.Integer, ? extends Object> takingWhile(
                Callable<? extends ceylon.language.Boolean> take) {
            return $ceylon$language$Iterable$this.takingWhile(take);
        }
        
        /* Implement Correspondence */
        /*
        @Override
        @Ignore
        @TypeInfo("ceylon.language::Integer|ceylon.language::Null")
        public ceylon.language.Integer get(
                @Name("key")
                ceylon.language.Integer key) {
            return ceylon.language.Integer.instance(array[(int)key.longValue()]);
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
        @TypeInfo("ceylon.language::Sequential<ceylon.language::Integer|ceylon.language.Null>")
        public ceylon.language.Sequential<? extends ceylon.language.Integer> items(ceylon.language.Iterable<? extends ceylon.language.Integer, ? extends Object> keys) {
            return $ceylon$language$Correspondence$this.items(keys);
        }*/
    }
   

}
