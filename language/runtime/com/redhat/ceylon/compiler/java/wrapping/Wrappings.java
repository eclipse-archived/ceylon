package com.redhat.ceylon.compiler.java.wrapping;

import java.util.AbstractMap;

import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

import ceylon.language.Collection;
import ceylon.language.Entry;
import ceylon.language.Integer;
import ceylon.language.List;

public class Wrappings {
    public static class Identity<From> implements Wrapping<From,From> {
        private Identity() {}
        @Override
        public From wrap(From from) {
            return from;
        }
        @Override
        public Wrapping<From,From> inverted() {
            return this;
        }
    };
    /** The identity wrapping */
    public static final Identity IDENTITY  = new Identity();
    /** The identity wrapping */
    public static <From> Wrapping<From,From> identity() {
        return IDENTITY;
    }
    
    private static <Java, Ceylon> Wrapping<Java, Ceylon> elementMapping(TypeDescriptor $reified$Element) {
        // TODO recursive lists,sets,maps
        Wrapping<Java,Ceylon> elementWrapping;
        if ($reified$Element == ceylon.language.Integer.$TypeDescriptor$) {
            elementWrapping = (Wrapping)TO_CEYLON_INTEGER;
        } else if ($reified$Element == ceylon.language.Float.$TypeDescriptor$) {
            elementWrapping = (Wrapping)TO_CEYLON_FLOAT;
        } else if ($reified$Element == ceylon.language.Byte.$TypeDescriptor$) {
            elementWrapping = (Wrapping)TO_CEYLON_BYTE;
        } else if ($reified$Element == ceylon.language.Boolean.$TypeDescriptor$) {
            elementWrapping = (Wrapping)TO_CEYLON_BOOLEAN;
        } else if ($reified$Element == ceylon.language.Character.$TypeDescriptor$) {
            elementWrapping = (Wrapping)TO_CEYLON_CHARACTER;
        } else if ($reified$Element == ceylon.language.String.$TypeDescriptor$) {
            elementWrapping = (Wrapping)TO_CEYLON_STRING;
        } else {
          elementWrapping = IDENTITY;  
        }
        return elementWrapping;
    }
    
    /** The wrapping {@code java.lang.Long} → {@code ceylon.language.Integer} */
    private static class ToCeylonInteger implements Wrapping<Long, ceylon.language.Integer> {
        private FromCeylonInteger from = new FromCeylonInteger(this);
        private ToCeylonInteger() {}
        @Override
        public Integer wrap(Long from) {
            return from == null ? null : Integer.instance(from);
        }
        @Override
        public Wrapping<Integer, Long> inverted() {
            return from;
        }
    }
    /** The wrapping {@code java.lang.Long} ← {@code ceylon.language.Integer} */
    private static class FromCeylonInteger implements Wrapping<ceylon.language.Integer, Long> {
        private ToCeylonInteger to;
        private FromCeylonInteger(ToCeylonInteger to) {
            this.to = to;
        }
        @Override
        public Long wrap(Integer from) {
            return from == null ? null : from.longValue();
        }
        @Override
        public Wrapping<Long, Integer> inverted() {
            return to;
        }
    }
    /** The wrapping {@code java.lang.Long} → {@code ceylon.language.Integer} */
    public static final ToCeylonInteger TO_CEYLON_INTEGER = new ToCeylonInteger();
    
    /** The wrapping {@code java.lang.Double} → {@code ceylon.language.Float} */
    private static class ToCeylonFloat implements Wrapping<Double, ceylon.language.Float> {
        private FromCeylonFloat from = new FromCeylonFloat(this);
        private ToCeylonFloat() {}
        @Override
        public ceylon.language.Float wrap(Double from) {
            return from == null ? null : ceylon.language.Float.instance(from);
        }
        @Override
        public Wrapping<ceylon.language.Float, Double> inverted() {
            return from;
        }
    }
    /** The wrapping {@code java.lang.Double} ← {@code ceylon.language.Float} */
    private static class FromCeylonFloat implements Wrapping<ceylon.language.Float, Double> {
        private ToCeylonFloat to;
        private FromCeylonFloat(ToCeylonFloat to) {
            this.to = to;
        }
        @Override
        public Double wrap(ceylon.language.Float from) {
            return from == null ? null : from.doubleValue();
        }
        @Override
        public Wrapping<Double, ceylon.language.Float> inverted() {
            return to;
        }
    }
    /** The wrapping {@code java.lang.Double} → {@code ceylon.language.Float} */
    public static final ToCeylonFloat TO_CEYLON_FLOAT = new ToCeylonFloat();
    
    /** The wrapping {@code java.lang.Byte} → {@code ceylon.language.Byte} */
    private static class ToCeylonByte implements Wrapping<java.lang.Byte, ceylon.language.Byte> {
        private FromCeylonByte from = new FromCeylonByte(this);
        private ToCeylonByte() {}
        @Override
        public ceylon.language.Byte wrap(java.lang.Byte from) {
            return from == null ? null : ceylon.language.Byte.instance(from);
        }
        @Override
        public Wrapping<ceylon.language.Byte, java.lang.Byte> inverted() {
            return from;
        }
    }
    /** The wrapping {@code java.lang.Byte} ← {@code ceylon.language.Byte} */
    private static class FromCeylonByte implements Wrapping<ceylon.language.Byte, java.lang.Byte> {
        private ToCeylonByte to;
        private FromCeylonByte(ToCeylonByte to) {
            this.to = to;
        }
        @Override
        public java.lang.Byte wrap(ceylon.language.Byte from) {
            return from == null ? null : from.byteValue();
        }
        @Override
        public Wrapping<java.lang.Byte, ceylon.language.Byte> inverted() {
            return to;
        }
    }
    /** The wrapping {@code java.lang.Long} → {@code ceylon.language.Integer} */
    public static final ToCeylonByte TO_CEYLON_BYTE = new ToCeylonByte();
    
    /** The wrapping {@code java.lang.Boolean} → {@code ceylon.language.Boolean} */
    private static class ToCeylonBoolean implements Wrapping<java.lang.Boolean, ceylon.language.Boolean> {
        private FromCeylonBoolean from = new FromCeylonBoolean(this);
        private ToCeylonBoolean() {}
        @Override
        public ceylon.language.Boolean wrap(java.lang.Boolean from) {
            return from == null ? null : ceylon.language.Boolean.instance(from);
        }
        @Override
        public Wrapping<ceylon.language.Boolean, java.lang.Boolean> inverted() {
            return from;
        }
    }
    /** The wrapping {@code java.lang.Boolean} ← {@code ceylon.language.Boolean} */
    private static class FromCeylonBoolean implements Wrapping<ceylon.language.Boolean, java.lang.Boolean> {
        private ToCeylonBoolean to;
        private FromCeylonBoolean(ToCeylonBoolean to) {
            this.to = to;
        }
        @Override
        public java.lang.Boolean wrap(ceylon.language.Boolean from) {
            return from == null ? null : from.booleanValue();
        }
        @Override
        public Wrapping<java.lang.Boolean, ceylon.language.Boolean> inverted() {
            return to;
        }
    }
    /** The wrapping {@code java.lang.Boolean} → {@code ceylon.language.Boolean} */
    public static final ToCeylonBoolean TO_CEYLON_BOOLEAN = new ToCeylonBoolean();
    
    /** The wrapping {@code java.lang.Integer} → {@code ceylon.language.Character} */
    private static class ToCeylonCharacter implements Wrapping<java.lang.Integer, ceylon.language.Character> {
        private FromCeylonCharacter from = new FromCeylonCharacter(this);
        private ToCeylonCharacter() {}
        @Override
        public ceylon.language.Character wrap(java.lang.Integer from) {
            return from == null ? null : ceylon.language.Character.instance(from);
        }
        @Override
        public Wrapping<ceylon.language.Character, java.lang.Integer> inverted() {
            return from;
        }
    }
    /** The wrapping {@code java.lang.Integer} ← {@code ceylon.language.Character} */
    private static class FromCeylonCharacter implements Wrapping<ceylon.language.Character, java.lang.Integer> {
        private ToCeylonCharacter to;
        private FromCeylonCharacter(ToCeylonCharacter to) {
            this.to = to;
        }
        @Override
        public java.lang.Integer wrap(ceylon.language.Character from) {
            return from == null ? null : from.codePoint;
        }
        @Override
        public Wrapping<java.lang.Integer, ceylon.language.Character> inverted() {
            return to;
        }
    }
    /** The wrapping {@code java.lang.Integer} → {@code ceylon.language.Character} */
    public static final ToCeylonCharacter TO_CEYLON_CHARACTER = new ToCeylonCharacter();
    
    /** The wrapping {@code java.lang.String} → {@code ceylon.language.String} */
    private static class ToCeylonString implements Wrapping<java.lang.String, ceylon.language.String> {
        private FromCeylonString from = new FromCeylonString(this);
        private ToCeylonString() {}
        @Override
        public ceylon.language.String wrap(java.lang.String from) {
            return from == null ? null : ceylon.language.String.instance(from);
        }
        @Override
        public Wrapping<ceylon.language.String, java.lang.String> inverted() {
            return from;
        }
    }
    /** The wrapping {@code java.lang.String} ← {@code ceylon.language.String} */
    private static class FromCeylonString implements Wrapping<ceylon.language.String, java.lang.String> {
        private ToCeylonString to;
        private FromCeylonString(ToCeylonString to) {
            this.to = to;
        }
        @Override
        public java.lang.String wrap(ceylon.language.String from) {
            return from == null ? null : from.value;
        }
        @Override
        public Wrapping<java.lang.String, ceylon.language.String> inverted() {
            return to;
        }
    }
    /** The wrapping {@code java.lang.Long} → {@code ceylon.language.Integer} */
    public static final ToCeylonString TO_CEYLON_STRING = new ToCeylonString();
    
    /** The wrapping {@code java.util.Map.Entry} → {@code ceylon.language.Entry} */
    public static <JavaKey,JavaItem,CeylonKey,CeylonItem> Wrapping<java.util.Map.Entry<JavaKey, JavaItem>, ceylon.language.Entry<CeylonKey, CeylonItem>> toCeylonEntry(
            final TypeDescriptor $reified$Key, final TypeDescriptor $reified$Item,
            final Wrapping<JavaKey,CeylonKey> keyWrapping, 
            final Wrapping<JavaItem,CeylonItem> itemWrapping) {
        /** The wrapping {@code java.util.Map.Entry} → {@code ceylon.language.Entry} */
        class ToCeylonEntry implements Wrapping<java.util.Map.Entry<JavaKey, JavaItem>, ceylon.language.Entry<CeylonKey, CeylonItem>> {
            private final FromCeylonEntry<CeylonKey, CeylonItem, JavaKey, JavaItem> from;
            
            public ToCeylonEntry() {
                this.from = new FromCeylonEntry<CeylonKey, CeylonItem, JavaKey, JavaItem>(
                        this, keyWrapping.inverted(), itemWrapping.inverted());
            }
            
            @Override
            public Entry<CeylonKey, CeylonItem> wrap(java.util.Map.Entry<JavaKey, JavaItem> from) {
                if (from == null) {
                    return null;
                }
                return new ceylon.language.Entry<CeylonKey, CeylonItem>($reified$Key, $reified$Item, 
                        keyWrapping.wrap(from.getKey()), 
                        itemWrapping.wrap(from.getValue()));
            }
            @Override
            public Wrapping<Entry<CeylonKey, CeylonItem>, java.util.Map.Entry<JavaKey, JavaItem>> inverted() {
                return from;
            }
        }
        return new ToCeylonEntry();
    }
    /** The wrapping {@code java.util.Map.Entry} ← {@code ceylon.language.Entry} */
    private static class FromCeylonEntry<CeylonKey,CeylonItem,JavaKey,JavaItem> implements Wrapping<ceylon.language.Entry<CeylonKey, CeylonItem>, java.util.Map.Entry<JavaKey, JavaItem>> {
        private final Wrapping<java.util.Map.Entry<JavaKey, JavaItem>, Entry<CeylonKey, CeylonItem>> from;
        private final Wrapping<CeylonKey,JavaKey> keyWrapping;
        private final Wrapping<CeylonItem,JavaItem> itemWrapping;
        FromCeylonEntry(Wrapping<java.util.Map.Entry<JavaKey, JavaItem>, Entry<CeylonKey, CeylonItem>> from,
                Wrapping<CeylonKey,JavaKey> keyWrapping, Wrapping<CeylonItem,JavaItem> itemWrapping) {
            this.from = from;
            this.keyWrapping = keyWrapping;
            this.itemWrapping = itemWrapping;
        }
        @Override
        public java.util.Map.Entry<JavaKey, JavaItem> wrap(Entry<CeylonKey, CeylonItem> from) {
            if (from == null) {
                return null;
            }
            return new AbstractMap.SimpleEntry<JavaKey,JavaItem>(
                    keyWrapping.wrap((CeylonKey)from.getKey()), 
                    itemWrapping.wrap((CeylonItem)from.getItem()));
        }
        @Override
        public Wrapping<java.util.Map.Entry<JavaKey, JavaItem>, Entry<CeylonKey, CeylonItem>> inverted() {
            return from;
        }
    }
    
    private static class ToCeylonList<Java,Ceylon> implements Wrapping<java.util.List<Java>, ceylon.language.List<Ceylon>> {

        private final FromCeylonList<Ceylon,Java> from;
        private TypeDescriptor $reified$Element;
        private Wrapping<Java, Ceylon> elementWrapping;
        
        public ToCeylonList(TypeDescriptor $reified$Element, Wrapping<Java, Ceylon> elementWrapping) {
            this.$reified$Element = $reified$Element;
            this.elementWrapping = elementWrapping;
            from = new FromCeylonList<Ceylon,Java>(this, elementWrapping.inverted());
        }
        
        @Override
        public List<Ceylon> wrap(java.util.List<Java> from) {
            if (from == null) {
                return null;
            }
            if (from instanceof WrappedCeylonList) {
                return ((WrappedCeylonList)from).unwrap();
            }
            
            return new WrappedJavaList<>($reified$Element, from, elementWrapping);
        }

        @Override
        public Wrapping<ceylon.language.List<Ceylon>, java.util.List<Java>> inverted() {
            return from;
        }
        
    }
    
    private static class FromCeylonList<Ceylon,Java> implements Wrapping<ceylon.language.List<Ceylon>, java.util.List<Java>> {

        private final ToCeylonList<Java,Ceylon> to;
        private final Wrapping<Ceylon,Java> elementWrapping;

        public FromCeylonList(ToCeylonList<Java,Ceylon> to, Wrapping<Ceylon,Java> elementWrapping) {
            this.to = to;
            this.elementWrapping = elementWrapping;
        }
        
        @Override
        public java.util.List<Java> wrap(List<Ceylon> from) {
            if (from == null) {
                return null;
            }
            if (from instanceof WrappedJavaList) {
                return ((WrappedJavaList)from).unwrap();
            }
            
            return new WrappedCeylonList<Ceylon,Java>(from,elementWrapping);
        }

        @Override
        public Wrapping<java.util.List<Java>, ceylon.language.List<Ceylon>> inverted() {
            return to;
        }
    }
    
    public static <Java,Ceylon> Wrapping<java.util.List<Java>, ceylon.language.List<Ceylon>> toCeylonList(TypeDescriptor $reified$Element) {
        Wrapping<Java, Ceylon> elementWrapping = elementMapping($reified$Element);
        return new ToCeylonList<Java,Ceylon>($reified$Element, elementWrapping);
    }
    
    private static class ToCeylonSet<Java,Ceylon> implements Wrapping<java.util.Set<Java>, ceylon.language.Set<Ceylon>> {

        private final FromCeylonSet<Ceylon,Java> from;
        private TypeDescriptor $reified$Element;
        private Wrapping<Java, Ceylon> elementWrapping;
        
        public ToCeylonSet(TypeDescriptor $reified$Element, Wrapping<Java, Ceylon> elementWrapping) {
            this.$reified$Element = $reified$Element;
            this.elementWrapping = elementWrapping;
            from = new FromCeylonSet<Ceylon,Java>(this, elementWrapping.inverted());
        }
        
        @Override
        public ceylon.language.Set<Ceylon> wrap(java.util.Set<Java> from) {
            if (from == null) {
                return null;
            }
            if (from instanceof WrappedCeylonSet) {
                Collection unwrap = ((WrappedCeylonSet)from).unwrap();
                if (unwrap instanceof ceylon.language.Set) {
                    return (ceylon.language.Set)unwrap;
                }
            }
            return new WrappedJavaSet<Java,Ceylon>($reified$Element, from, elementWrapping);
        }

        @Override
        public Wrapping<ceylon.language.Set<Ceylon>, java.util.Set<Java>> inverted() {
            return from;
        }
        
    }
    
    private static class FromCeylonSet<Ceylon,Java> implements Wrapping<ceylon.language.Set<Ceylon>, java.util.Set<Java>> {

        private final ToCeylonSet<Java,Ceylon> to;
        private final Wrapping<Ceylon,Java> elementWrapping;

        public FromCeylonSet(ToCeylonSet<Java,Ceylon> to, Wrapping<Ceylon,Java> elementWrapping) {
            this.to = to;
            this.elementWrapping = elementWrapping;
        }
        
        @Override
        public java.util.Set<Java> wrap(ceylon.language.Set<Ceylon> from) {
            if (from == null) {
                return null;
            }
            if (from instanceof WrappedJavaSet) {
                return ((WrappedJavaSet)from).unwrap();
            }
            return new WrappedCeylonSet<Ceylon,Java>(from, elementWrapping);
        }

        @Override
        public Wrapping<java.util.Set<Java>, ceylon.language.Set<Ceylon>> inverted() {
            return to;
        }
    }
    
    public static<Java,Ceylon> Wrapping<java.util.Set<Java>, ceylon.language.Set<Ceylon>> toCeylonSet(TypeDescriptor $reified$Element) {
        Wrapping<Java, Ceylon> elementWrapping = elementMapping($reified$Element);
        return new ToCeylonSet<Java,Ceylon>($reified$Element, elementWrapping);
    }
    
    private static class ToCeylonMap<JavaKey,JavaItem,CeylonKey,CeylonItem> implements Wrapping<java.util.Map<JavaKey,JavaItem>, ceylon.language.Map<CeylonKey,CeylonItem>> {

        private final FromCeylonMap<CeylonKey,CeylonItem,JavaKey,JavaItem> from;
        private TypeDescriptor $reified$Key;
        private TypeDescriptor $reified$Item;
        private Wrapping<JavaKey, CeylonKey> keyWrapping;
        private Wrapping<JavaItem, CeylonItem> itemWrapping;
        
        public ToCeylonMap(TypeDescriptor $reified$Key, TypeDescriptor $reified$Item, Wrapping<JavaKey, CeylonKey> keyWrapping, Wrapping<JavaItem, CeylonItem> itemWrapping) {
            this.$reified$Key = $reified$Key;
            this.$reified$Item = $reified$Item;
            this.keyWrapping = keyWrapping;
            this.itemWrapping = itemWrapping;
            from = new FromCeylonMap<CeylonKey,CeylonItem,JavaKey,JavaItem>($reified$Key, $reified$Item, this, keyWrapping.inverted(), itemWrapping.inverted());
        }
        
        @Override
        public ceylon.language.Map<CeylonKey,CeylonItem> wrap(java.util.Map<JavaKey, JavaItem> from) {
            if (from == null) {
                return null;
            }
            if (from instanceof WrappedCeylonMap) {
                return ((WrappedCeylonMap)from).unwrap();
            }
            return new WrappedJavaMap<JavaKey,JavaItem,CeylonKey,CeylonItem>($reified$Key, $reified$Item, 
                    from, 
                    keyWrapping, itemWrapping);
        }

        @Override
        public Wrapping<ceylon.language.Map<CeylonKey, CeylonItem>, java.util.Map<JavaKey, JavaItem>> inverted() {
            return from;
        }
        
    }
    
    private static class FromCeylonMap<CeylonKey,CeylonItem,JavaKey,JavaItem> implements Wrapping<ceylon.language.Map<CeylonKey,CeylonItem>, java.util.Map<JavaKey,JavaItem>> {

        private final TypeDescriptor $reified$Key;
        private final TypeDescriptor $reified$Item;
        private final ToCeylonMap<JavaKey,JavaItem,CeylonKey,CeylonItem> to;
        private final Wrapping<CeylonKey,JavaKey> keyWrapping;
        private final Wrapping<CeylonItem,JavaItem> itemWrapping;

        public FromCeylonMap(
                TypeDescriptor $reified$Key,
                TypeDescriptor $reified$Item,
                ToCeylonMap<JavaKey,JavaItem,CeylonKey,CeylonItem> to, 
                Wrapping<CeylonKey,JavaKey> keyWrapping, 
                Wrapping<CeylonItem,JavaItem> itemWrapping) {
            this.$reified$Key = $reified$Key;
            this.$reified$Item = $reified$Item;
            this.to = to;
            this.keyWrapping = keyWrapping;
            this.itemWrapping = itemWrapping;
        }
        
        @Override
        public java.util.Map<JavaKey, JavaItem> wrap(ceylon.language.Map<CeylonKey, CeylonItem> from) {
            if (from == null) {
                return null;
            }
            if (from instanceof WrappedJavaMap<?,?,?,?>) {
                return (java.util.Map<JavaKey, JavaItem>)((WrappedJavaMap<?,?,?,?>)from).unwrap();
            }
            return new WrappedCeylonMap<CeylonKey,CeylonItem,JavaKey,JavaItem>(
                    $reified$Key, $reified$Item,
                    from,
                    keyWrapping, itemWrapping);
        }

        @Override
        public Wrapping<java.util.Map<JavaKey,JavaItem>, ceylon.language.Map<CeylonKey,CeylonItem>> inverted() {
            return to;
        }
    }
    
    public static<JavaKey,JavaItem,CeylonKey,CeylonItem> Wrapping<java.util.Map<JavaKey,JavaItem>, ceylon.language.Map<CeylonKey,CeylonItem>> toCeylonMap(TypeDescriptor $reified$Key, TypeDescriptor $reified$Item) {
        Wrapping<JavaKey, CeylonKey> keyWrapping = elementMapping($reified$Key);
        Wrapping<JavaItem, CeylonItem> itemWrapping = elementMapping($reified$Item);
        return new ToCeylonMap<JavaKey,JavaItem,CeylonKey,CeylonItem>($reified$Key, $reified$Item, keyWrapping, itemWrapping);
    }
}