package com.redhat.ceylon.compiler.java.language;

import ceylon.language.Array;
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

/**
 * A Ceylon schema for a Java <code>T[]</code>
 *
 * This type is never instantiated, it is completely erased to 
 * <code>T[]</code>.
 * 
 * The {@link #get(int)}, {@link #set(int,T)}, {@link #length size} 
 * methods and the constructor are also completely erased to Java 
 * array operators, or {@link Util#fillArray(T[],T)} in the case of 
 * the constructor if the initial element is specified.
 * 
 * Only the value type static methods are really invoked.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
@Ceylon(major = 7)
@Class
@ValueType
public final class ObjectArray<T> implements ReifiedType {
    
    public ObjectArray(@Ignore TypeDescriptor $reifiedT, 
    		@Name("size") int size, 
    		@TypeInfo("T|ceylon.language::Null") @Defaulted 
    		@Name("element") T element){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public ObjectArray(@Name("size") int size){
        throw Util.makeJavaArrayWrapperException();
    }

    @TypeInfo(value = "T", uncheckedNull = true) //for consistency with other Java methods
    public T get(
            @Name("index")
            int index) {
        throw Util.makeJavaArrayWrapperException();
    }

    public void set(
            @Name("index")
            int index,
            @Name("element")
            @TypeInfo("T|ceylon.language::Null") 
            T element) {
        throw Util.makeJavaArrayWrapperException();
    }

    @Name("size")
    public final int length = 0;
    
    @Ignore
    public static <T> ceylon.language.Array<T> getArray(T[] array){
        return Array.instance(array);
    }

    @TypeInfo("ceylon.language::Array<T|ceylon.language::Null>")
    public ceylon.language.Array<T> getArray(){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public static <T> void copyTo(T[] array, T[] destination){
        System.arraycopy(array, 0, destination, 0, array.length);
    }

    @Ignore
    public static <T> void copyTo(T[] array, T[] destination, int sourcePosition){
        System.arraycopy(array, sourcePosition, destination, 0, array.length-sourcePosition);
    }

    @Ignore
    public static <T> void copyTo(T[] array, T[] destination, int sourcePosition, int destinationPosition){
        System.arraycopy(array, sourcePosition, destination, destinationPosition, array.length-sourcePosition);
    }

    @Ignore
    public static <T> void copyTo(T[] array, T[] destination, int sourcePosition, int destinationPosition, int length){
        System.arraycopy(array, sourcePosition, destination, destinationPosition, length);
    }

    @Ignore
    public int copyTo$sourcePosition(T[] destination){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public int copyTo$destinationPosition(T[] destination, int sourcePosition){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public int copyTo$length(T[] destination, int sourcePosition, int destinationPosition){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public void copyTo(T[] destination){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public void copyTo(T[] destination, 
                       int sourcePosition){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public void copyTo(T[] destination, 
                       int sourcePosition, 
                       int destinationPosition){
        throw Util.makeJavaArrayWrapperException();
    }

    public void copyTo(@Name("destination") T[] destination, 
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
    public static <T> boolean equals(T[] value, java.lang.Object that) {
        return value.equals(that);
    }

    @Override
    public int hashCode() {
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public static <T> int hashCode(T[] value) {
        return value.hashCode();
    }

    @Override
    public java.lang.String toString() {
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public static <T> java.lang.String toString(T[] value) {
        return value.toString();
    }
    
    @TypeInfo("java.lang::ObjectArray<T>")
    public Object[] $clone() {
        throw Util.makeJavaArrayWrapperException();
    }
    
    @Ignore
    public static <T> T[] $clone(T[] value) {
        return value.clone();
    }
    
    @TypeInfo("ceylon.language::Iterable<T|ceylon.language::Null,ceylon.language::Null>")
    public ObjectArrayIterable<T> getIterable() {
        throw Util.makeJavaArrayWrapperException();
    }
    
    @Ignore
    public static <T> ObjectArrayIterable<T> getIterable(T[] value) {
        return new ObjectArrayIterable<T>(value);
    }
    
    /* Implement Iterable */

    public static class ObjectArrayIterable<T> implements ReifiedType, Iterable<T, ceylon.language.Null> {
        private final Category$impl<Object> $ceylon$language$Category$this = new Category$impl<Object>(ceylon.language.Object.$TypeDescriptor$, this);
        private final Iterable$impl<T, Null> $ceylon$language$Iterable$this;
        
        private final T[] array;
        private TypeDescriptor $reified$Element;
        
        @Override
        public TypeDescriptor $getType$() {
            return TypeDescriptor.klass(ObjectArrayIterable.class, this.$reified$Element);
        }
        
        @Ignore
        public ObjectArrayIterable(T[] array) {
            this.array = array;
            this.$reified$Element = TypeDescriptor.klass(array.getClass().getComponentType());
            this.$ceylon$language$Iterable$this = new Iterable$impl<T, Null>(
                    this.$reified$Element, Null.$TypeDescriptor$, this);
        }
        
        @Ignore
        public T[] arrayValue() {
            return array;
        }
        
        @Override
        public Category$impl<? super Object> $ceylon$language$Category$impl() {
            return $ceylon$language$Category$this;
        }
    
        @Override
        public Iterable$impl<? extends T, ? extends Null> $ceylon$language$Iterable$impl() {
            return $ceylon$language$Iterable$this;
        }
    
        @Override
        public boolean containsAny(Iterable<? extends Object, ? extends Object> arg0) {
            Iterator<? extends Object> iter = arg0.iterator();
            Object item;
            while (!((item = iter.next()) instanceof Finished)) {
                for (int ii = 0; ii < array.length; ii++) {
                    if (array[ii] == item) {// TODO equals
                        return true;
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
                for (int ii = 0; ii < array.length; ii++) {
                    if (array[ii] == item) { // TODO equals()
                        continue OUTER;
                    }
                }
                return false;
            }
            return true;
        }
    
        @Override
        public boolean any(Callable<? extends ceylon.language.Boolean> arg0) {
            for (int ii=0; ii < array.length; ii++) {
                if (arg0.$call$(array[ii]).booleanValue()) {
                    return true;
                }
            }
            return false;
        }
    
        @Override
        public Iterable<? extends T, ? extends Null> by(long step) {
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
                if (array[ii] == item) {// TODO equals
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
        public Iterable<? extends T, ? extends Null> cycle(long times) {
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
        public Iterable<? extends T, ? extends Object> filter(
                Callable<? extends ceylon.language.Boolean> selecting) {
            return $ceylon$language$Iterable$this.filter(selecting);
        }
    
        @Override
        public T find(Callable<? extends ceylon.language.Boolean> selecting) {
            return $ceylon$language$Iterable$this.find(selecting);
        }
    
        @Override
        public T findLast(Callable<? extends ceylon.language.Boolean> selecting) {
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
        public Iterable<? extends T, ? extends Object> getCoalesced() {
            return this;
        }
    
        @Override
        public Iterable<? extends T, ? extends Null> getCycled() {
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
        public Iterable<? extends Entry<? extends ceylon.language.Integer, ? extends T>, ? extends Object> getIndexed() {
            return $ceylon$language$Iterable$this.getIndexed();
        }
    
        @Override
        public Object getLast() {
            return array.length == 0 ? null : array[array.length-1];
        }
    
        @Override
        public Iterable<? extends T, ? extends Object> getRest() {
            return $ceylon$language$Iterable$this.getRest();
        }
    
        @Override
        public Sequential<? extends T> getSequence() {
            return array.length == 0 ? empty_.get_() : new ArraySequence(ceylon.language.Integer.$TypeDescriptor$, this);
        }
    
        @Ignore
        @Override
        public long getSize() {
            return array.length;
        }
    
        @Override
        public Iterator<? extends T> iterator() {
            return new Iterator<T>() {
    
                private int index = 0;
                
                private final Iterator$impl<T> $ceylon$language$Iterator$this = new Iterator$impl<T>(ceylon.language.Integer.$TypeDescriptor$, this);
                
                @Override
                public Iterator$impl<? extends T> $ceylon$language$Iterator$impl() {
                    return $ceylon$language$Iterator$this;
                }
    
                @Override
                public Object next() {
                    if (index < array.length) {
                        return array[index++];
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
        public List<? extends T> repeat(long times) {
            return $ceylon$language$Iterable$this.repeat(times);
        }
    
        @Override
        public Sequential<? extends T> select(Callable<? extends ceylon.language.Boolean> selecting) {
            return $ceylon$language$Iterable$this.select(selecting);
        }
    
        @Override
        public boolean shorterThan(long length) {
            return array.length < length;
        }
    
        @Override
        public Iterable<? extends T, ? extends Object> skipping(long skip) {
            return $ceylon$language$Iterable$this.skipping(skip);
        }
    
        @Override
        public Iterable<? extends T, ? extends Object> skippingWhile(
                Callable<? extends ceylon.language.Boolean> skip) {
            return $ceylon$language$Iterable$this.skippingWhile(skip);
        }
    
        @Override
        public Sequential<? extends T> sort(
                final Callable<? extends Comparison> comparing) {
            return $ceylon$language$Iterable$this.sort(comparing);
        }
    
        @Override
        public Iterable<? extends T, ? extends Object> taking(long take) {
            return $ceylon$language$Iterable$this.taking(take);
        }
    
        @Override
        public Iterable<? extends T, ? extends Object> takingWhile(
                Callable<? extends ceylon.language.Boolean> take) {
            return $ceylon$language$Iterable$this.takingWhile(take);
        }
    }
   


}
