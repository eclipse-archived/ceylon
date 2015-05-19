package ceylon.language.serialization;

import java.util.HashMap;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

import ceylon.language.Iterator;
import ceylon.language.Null;
import ceylon.language.finished_;
import ceylon.language.impl.BaseIterable;
import ceylon.language.impl.BaseIterator;

@Ceylon(major=8, minor=0)
@Class
@TypeParameters({
    @TypeParameter(value="Key"),
    @TypeParameter(value="Element")
    })
final class NativeMapImpl<Key,Element> implements NativeMap<Key, Element> {
    private final HashMap<Key, Element> map = new HashMap<Key, Element>();
    private final TypeDescriptor $reified$Key;
    private final TypeDescriptor $reified$Item;
    
    public NativeMapImpl(@Ignore TypeDescriptor $reified$Key, @Ignore TypeDescriptor $reified$Item) {
        this.$reified$Key = $reified$Key ;
        this.$reified$Item= $reified$Item;
    }
    
    public final java.lang.Object put(Key id, Element instance) {
        map.put(id, instance);
        return null;
    }
    
    public final Element get(Key id) {
        return map.get(id);
    }
    
    public final boolean contains(Key id) {
        return map.containsKey(id);
    }
    
    public final long getSize() {
        return map.size();
    }
    
    @TypeInfo("ceylon.language::Iterable<Element,ceylon.language::Null>")
    public final ceylon.language.Iterable<Element, java.lang.Object> getItems() {
        return new BaseIterable<Element,java.lang.Object>($reified$Item, Null.$TypeDescriptor$) {

            @Override
            public Iterator<? extends Element> iterator() {
                final java.util.Iterator<Element> it = map.values().iterator();
                return new BaseIterator<Element>($reified$Item) {

                    @Override
                    public Object next() {
                        if (it.hasNext()) {
                            return it.next();
                        } else { 
                            return finished_.get_();
                        }
                    }
                    
                };
            }
            
        };
    }
    
    @TypeInfo("ceylon.language::Iterable<Key,ceylon.language::Null>")
    public final ceylon.language.Iterable<Key, java.lang.Object> getKeys() {
        return new BaseIterable<Key,java.lang.Object>($reified$Key, Null.$TypeDescriptor$) {

            @Override
            public Iterator<? extends Key> iterator() {
                final java.util.Iterator<Key> it = map.keySet().iterator();
                return new BaseIterator<Key>($reified$Key) {

                    @Override
                    public Object next() {
                        if (it.hasNext()) {
                            return it.next();
                        } else { 
                            return finished_.get_();
                        }
                    }
                    
                };
            }
            
        };
    }
    
}
