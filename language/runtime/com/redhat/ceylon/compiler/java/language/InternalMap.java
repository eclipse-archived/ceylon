package com.redhat.ceylon.compiler.java.language;

import static java.util.Collections.unmodifiableMap;
import ceylon.language.Entry;
import ceylon.language.Iterator;
import ceylon.language.finished_;
import ceylon.language.impl.BaseIterator;
import ceylon.language.impl.BaseMap;

import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

/** An immutable map returned by certain methods and functions of the language module.
 * This is only to avoid depending on ceylon.collection.
 * 
 * @author Enrique Zamudio
 */
@Ceylon(major = 7)
@Class
public class InternalMap<Key, Item> 
extends BaseMap<Key, Item> {

    private final class MapIterator extends
            BaseIterator<Entry<? extends Key, ? extends Item>> {
    	
	    private final 
	    java.util.Iterator<? extends java.util.Map.Entry<? extends Key, ? extends Item>> 
	    inner = map.entrySet().iterator();

	    private MapIterator(TypeDescriptor $reifiedElement) {
		    super($reifiedElement);
	    }

	    @Override
	    @Annotations(@Annotation("formal"))
	    @TypeInfo(value="ceylon.language::Entry<Key,Item>|ceylon.language::Finished", 
	             erased=true)
	    public Object next() {
	        if (inner.hasNext()) {
	            java.util.Map.Entry<? extends Key, ? extends Item> 
	            entry = inner.next();
	            return new Entry<Key, Item>($reifiedKey, $reifiedItem, 
	            		entry.getKey(), entry.getValue());
	        }
	        return finished_.get_();
	    }
    }

	private final java.util.Map<? extends Key, ? extends Item> map;
    
    @Ignore
    private TypeDescriptor $reifiedItem;

    @Ignore
    private TypeDescriptor $reifiedKey;

    public InternalMap(
    		@Ignore TypeDescriptor $reifiedKey, 
    		@Ignore TypeDescriptor $reifiedItem,
            final java.util.Map<? extends Key, ? extends Item> map) {
        super($reifiedKey, $reifiedItem);
        this.map = map;
        this.$reifiedKey = $reifiedKey;
        this.$reifiedItem = $reifiedItem;
    }
    
    @Override
    @Annotations(@Annotation("formal"))
    @TypeInfo("Item|ceylon.language::Nothing")
    public Item get(Object key) {
        return map.get(key);
    }

    @Override
    @Annotations(@Annotation("default"))
    public boolean defines(Object key) {
        return map.containsKey(key);
    }

    @Override
    @Annotations({ @Annotation("actual"), @Annotation("default") })
    public boolean getEmpty() {
        return map.isEmpty();
    }

    @Override
    @Annotations(@Annotation("formal"))
    @TypeInfo("ceylon.language::Iterator<ceylon.language::Entry<Key,Item>>")
    public Iterator<? extends Entry<? extends Key, ? extends Item>> 
    iterator() {
        return new MapIterator(TypeDescriptor.klass(Entry.class, 
        		$reifiedKey, $reifiedItem));
    }

    @Override
    @Annotations(@Annotation("formal"))
    public long getSize() {
        return map.size();
    }
    
    @Override
    @Annotations(@Annotation("formal"))
    public InternalMap<? extends Key, ? extends Item> $clone() {
        return new InternalMap<Key,Item>($reifiedKey, $reifiedItem, 
        		unmodifiableMap(map));
    }

    @Override
    @Ignore
    public TypeDescriptor $getType$() {
        return TypeDescriptor.klass(InternalMap.class,
        		$reifiedKey, $reifiedItem);
    }
    
}
