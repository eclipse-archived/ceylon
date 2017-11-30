/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.java.wrapping;

import java.util.AbstractMap;

import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;
import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor.Union;

import org.eclipse.ceylon.compiler.java.wrapping.WrappedCeylonList;
import org.eclipse.ceylon.compiler.java.wrapping.WrappedCeylonMap;
import org.eclipse.ceylon.compiler.java.wrapping.WrappedCeylonSet;
import org.eclipse.ceylon.compiler.java.wrapping.WrappedJavaList;
import org.eclipse.ceylon.compiler.java.wrapping.WrappedJavaMap;
import org.eclipse.ceylon.compiler.java.wrapping.WrappedJavaSet;
import org.eclipse.ceylon.compiler.java.wrapping.Wrapping;

import ceylon.language.Collection;
import ceylon.language.Entry;
import ceylon.language.Integer;
import ceylon.language.List;
import ceylon.language.Null;

@SuppressWarnings({"unchecked", "rawtypes"})
public class Wrappings {
    static void checkNull(Object o, String msg) {
        if (o == null) {
            throw new ceylon.language.AssertionError("null value present in wrapping" + (msg != null ? " "+ msg : ""));
        }
    }
    static class Identity<From> implements Wrapping<From,From> {
        private static final long serialVersionUID = -7765082682145106763L;
        private final boolean allowNull;
        Identity(boolean allowNull) {
            this.allowNull = allowNull;
        }
        @Override
        public From wrap(From from) {
            if (from == null) {
                if (!allowNull) {
                    checkNull(from, null);
                }
                return null;
            }
            return from;
        }
        @Override
        public Wrapping<From,From> inverted() {
            return this;
        }
    };
    
    /** The identity wrapping but throwing on null */
    public static final Identity DEFINITE_IDENTITY = new Identity(false);
    
    /** The identity wrapping */
    public static final Identity MAYBE_IDENTITY = new Identity(true);
    
    static <Java, Ceylon> Wrapping<Java, Ceylon> elementMapping(TypeDescriptor $reified$Element) {
        boolean allowNull = false;
        if ($reified$Element.containsNull()) {
            allowNull = true;
            if ($reified$Element instanceof TypeDescriptor.Union
                    && ((TypeDescriptor.Union)$reified$Element).getMembers().length == 2) {
                Union union = (TypeDescriptor.Union)$reified$Element;
                if (union.getMembers()[0].containsNull()) {
                    $reified$Element = union.getMembers()[1]; 
                } else if (union.getMembers()[1].containsNull()) {
                    $reified$Element = union.getMembers()[0]; 
                }
            }
        }
        Wrapping<Java,Ceylon> elementWrapping;
        if ($reified$Element == ceylon.language.Integer.$TypeDescriptor$) {
            elementWrapping = (Wrapping) (allowNull ? TO_CEYLON_INTEGER_OR_NULL : TO_CEYLON_INTEGER);
        } else if ($reified$Element == ceylon.language.Float.$TypeDescriptor$) {
            elementWrapping = (Wrapping) (allowNull ? TO_CEYLON_FLOAT_OR_NULL : TO_CEYLON_FLOAT);
        } else if ($reified$Element == ceylon.language.Byte.$TypeDescriptor$) {
            elementWrapping = (Wrapping) (allowNull ? TO_CEYLON_BYTE_OR_NULL : TO_CEYLON_BYTE);
        } else if ($reified$Element == ceylon.language.Boolean.$TypeDescriptor$) {
            elementWrapping = (Wrapping) (allowNull ? TO_CEYLON_BOOLEAN_OR_NULL : TO_CEYLON_BOOLEAN);
        } else if ($reified$Element == ceylon.language.Character.$TypeDescriptor$) {
            elementWrapping = (Wrapping) (allowNull ? TO_CEYLON_CHARACTER_OR_NULL : TO_CEYLON_CHARACTER);
        } else if ($reified$Element == ceylon.language.String.$TypeDescriptor$) {
            elementWrapping = (Wrapping) (allowNull ? TO_CEYLON_STRING_OR_NULL : TO_CEYLON_STRING);
        } else if ($reified$Element instanceof TypeDescriptor.Class) {
            TypeDescriptor.Class classDescriptor = (TypeDescriptor.Class)$reified$Element;
            if (classDescriptor.getKlass() == ceylon.language.List.class) {
            elementWrapping = (Wrapping)toCeylonList(classDescriptor.getTypeArgument(0), allowNull);
            } else if (classDescriptor.getKlass() == ceylon.language.Set.class) {
                elementWrapping = (Wrapping)toCeylonSet(classDescriptor.getTypeArgument(0), allowNull);
            } else if (classDescriptor.getKlass() == ceylon.language.Map.class) {
                elementWrapping = (Wrapping)toCeylonMap(classDescriptor.getTypeArgument(0), classDescriptor.getTypeArgument(1), allowNull);
            } else {
                elementWrapping = allowNull ? MAYBE_IDENTITY : DEFINITE_IDENTITY;
            }
        } else {
            elementWrapping = allowNull ? MAYBE_IDENTITY : DEFINITE_IDENTITY;
        }
        return elementWrapping;
    }
    
    /** The wrapping {@code java.lang.Long} → {@code ceylon.language.Integer} */
    static class ToCeylonInteger implements Wrapping<Long, ceylon.language.Integer> {
        private static final long serialVersionUID = -8545077172446381771L;
        private final FromCeylonInteger from;
        private final boolean allowNull;
        public ToCeylonInteger(boolean allowNull) {
            this.allowNull = allowNull;
            from = new FromCeylonInteger(this, allowNull);
        }
        @Override
        public Integer wrap(Long from) {
            if (from == null) {
                if (!allowNull) {
                    checkNull(from, "of java.lang.Long into ceylon.language.Integer");
                }
                return null;
            }
            return Integer.instance(from);
        }
        @Override
        public Wrapping<Integer, Long> inverted() {
            return from;
        }
    }
    /** The wrapping {@code java.lang.Long} ← {@code ceylon.language.Integer} */
    static class FromCeylonInteger implements Wrapping<ceylon.language.Integer, Long> {
        private static final long serialVersionUID = -8490990231904038975L;
        private final ToCeylonInteger to;
        private final boolean allowNull;
        public FromCeylonInteger(ToCeylonInteger to, boolean allowNull) {
            this.to = to;
            this.allowNull = allowNull;
        }
        @Override
        public Long wrap(Integer from) {
            if (from == null) {
                if (!allowNull) {
                    checkNull(from, "of ceylon.language.Integer into java.lang.Long");
                }
                return null;
            }
            return from.longValue();
        }
        @Override
        public Wrapping<Long, Integer> inverted() {
            return to;
        }
    }
    /** The wrapping {@code java.lang.Long} → {@code ceylon.language.Integer} (throws on null) */
    public static final ToCeylonInteger TO_CEYLON_INTEGER = new ToCeylonInteger(false);
    /** The wrapping {@code java.lang.Long?} → {@code ceylon.language.Integer?} (passes null) */
    public static final ToCeylonInteger TO_CEYLON_INTEGER_OR_NULL = new ToCeylonInteger(true);
    
    /** The wrapping {@code java.lang.Double} → {@code ceylon.language.Float} */
    static class ToCeylonFloat implements Wrapping<Double, ceylon.language.Float> {
        private static final long serialVersionUID = 6137133766421110316L;
        private final FromCeylonFloat from;
        private final boolean allowNull;
        public ToCeylonFloat(boolean allowNull) {
            this.allowNull = allowNull;
            from = new FromCeylonFloat(this, allowNull);
        }
        @Override
        public ceylon.language.Float wrap(Double from) {
            if (from == null) {
                if (!allowNull) {
                    checkNull(from, "of java.lang.Double into ceylon.language.Float");
                }
                return null;
            }
            return ceylon.language.Float.instance(from);
        }
        @Override
        public Wrapping<ceylon.language.Float, Double> inverted() {
            return from;
        }
    }
    /** The wrapping {@code java.lang.Double} ← {@code ceylon.language.Float} */
    static class FromCeylonFloat implements Wrapping<ceylon.language.Float, Double> {
        private static final long serialVersionUID = 3749821019398032894L;
        private final ToCeylonFloat to;
        private final boolean allowNull;
        public FromCeylonFloat(ToCeylonFloat to, boolean allowNull) {
            this.to = to;
            this.allowNull = allowNull;
        }
        @Override
        public Double wrap(ceylon.language.Float from) {
            if (from == null) {
                if (!allowNull) {
                    checkNull(from, "of ceylon.language.Float into java.lang.Double");
                }
                return null;
            }
            return from.doubleValue();
        }
        @Override
        public Wrapping<Double, ceylon.language.Float> inverted() {
            return to;
        }
    }
    /** The wrapping {@code java.lang.Double} → {@code ceylon.language.Float} (throws on null) */
    public static final ToCeylonFloat TO_CEYLON_FLOAT = new ToCeylonFloat(false);
    /** The wrapping {@code java.lang.Double?} → {@code ceylon.language.Float?} (alllws null) */
    public static final ToCeylonFloat TO_CEYLON_FLOAT_OR_NULL = new ToCeylonFloat(true);
    
    /** The wrapping {@code java.lang.Byte} → {@code ceylon.language.Byte} */
    static class ToCeylonByte implements Wrapping<java.lang.Byte, ceylon.language.Byte> {
        private static final long serialVersionUID = 7612648551990554992L;
        private final FromCeylonByte from;
        private final boolean allowNull;
        public ToCeylonByte(boolean allowNull) {
            this.allowNull = allowNull;
            from = new FromCeylonByte(this, allowNull);
        }
        @Override
        public ceylon.language.Byte wrap(java.lang.Byte from) {
            if (from == null) {
                if (!allowNull) {
                    checkNull(from, "of java.lang.Byte into ceylon.language.Byte");
                }
                return null;
            }
            return ceylon.language.Byte.instance(from);
        }
        @Override
        public Wrapping<ceylon.language.Byte, java.lang.Byte> inverted() {
            return from;
        }
    }
    /** The wrapping {@code java.lang.Byte} ← {@code ceylon.language.Byte} */
    static class FromCeylonByte implements Wrapping<ceylon.language.Byte, java.lang.Byte> {
        private static final long serialVersionUID = 1122563983602166370L;
        private final ToCeylonByte to;
        private final boolean allowNull;
        public FromCeylonByte(ToCeylonByte to, boolean allowNull) {
            this.to = to;
            this.allowNull = allowNull;
        }
        @Override
        public java.lang.Byte wrap(ceylon.language.Byte from) {
            if (from == null) {
                if (!allowNull) {
                    checkNull(from, "of ceylon.language.Byte into java.lang.Byte");
                }
                return null;
            }
            return from.byteValue();
        }
        @Override
        public Wrapping<java.lang.Byte, ceylon.language.Byte> inverted() {
            return to;
        }
    }
    /** The wrapping {@code java.lang.Byte} → {@code ceylon.language.Byte} (throws on null) */
    public static final ToCeylonByte TO_CEYLON_BYTE = new ToCeylonByte(false);
    /** The wrapping {@code java.lang.Byte?} → {@code ceylon.language.Byte?} (allows null) */
    public static final ToCeylonByte TO_CEYLON_BYTE_OR_NULL = new ToCeylonByte(false);
    
    /** The wrapping {@code java.lang.Boolean} → {@code ceylon.language.Boolean} */
    static class ToCeylonBoolean implements Wrapping<java.lang.Boolean, ceylon.language.Boolean> {
        private static final long serialVersionUID = -5753935452391992481L;
        private final FromCeylonBoolean from;
        private final boolean allowNull;
        public ToCeylonBoolean(boolean allowNull) {
            this.allowNull = allowNull;
            from = new FromCeylonBoolean(this, allowNull);
        }
        @Override
        public ceylon.language.Boolean wrap(java.lang.Boolean from) {
            if (from == null) {
                if (!allowNull) {
                    checkNull(from, "of java.lang.Boolean into ceylon.language.Boolean");
                }
                return null;
            }
            return ceylon.language.Boolean.instance(from);
        }
        @Override
        public Wrapping<ceylon.language.Boolean, java.lang.Boolean> inverted() {
            return from;
        }
    }
    /** The wrapping {@code java.lang.Boolean} ← {@code ceylon.language.Boolean} */
    static class FromCeylonBoolean implements Wrapping<ceylon.language.Boolean, java.lang.Boolean> {
        private static final long serialVersionUID = -5549339150429649814L;
        private final ToCeylonBoolean to;
        private final boolean allowNull;
        public FromCeylonBoolean(ToCeylonBoolean to, boolean allowNull) {
            this.to = to;
            this.allowNull = allowNull;
        }
        @Override
        public java.lang.Boolean wrap(ceylon.language.Boolean from) {
            if (from == null) {
                if (!allowNull) {
                    checkNull(from, "of ceylon.language.Boolean into java.lang.Boolean");
                }
                return null;
            }
            return from.booleanValue();
        }
        @Override
        public Wrapping<java.lang.Boolean, ceylon.language.Boolean> inverted() {
            return to;
        }
    }
    /** The wrapping {@code java.lang.Boolean} → {@code ceylon.language.Boolean} (throws on null*/
    public static final ToCeylonBoolean TO_CEYLON_BOOLEAN = new ToCeylonBoolean(false);
    /** The wrapping {@code java.lang.Boolean?} → {@code ceylon.language.Boolean?} (allows null)*/
    public static final ToCeylonBoolean TO_CEYLON_BOOLEAN_OR_NULL = new ToCeylonBoolean(true);
    
    /** The wrapping {@code java.lang.Integer} → {@code ceylon.language.Character} */
    static class ToCeylonCharacter implements Wrapping<java.lang.Integer, ceylon.language.Character> {
        private static final long serialVersionUID = -8772225234908795616L;
        private final FromCeylonCharacter from;
        private final boolean allowNull;
        public ToCeylonCharacter(boolean allowNull) {
            this.allowNull = allowNull;
            from = new FromCeylonCharacter(this, allowNull);
        }
        @Override
        public ceylon.language.Character wrap(java.lang.Integer from) {
            if (from == null) {
                if (!allowNull) {
                    checkNull(from, "of java.lang.Character into ceylon.language.Character");
                }
                return null;
            }
            return ceylon.language.Character.instance(from);
        }
        @Override
        public Wrapping<ceylon.language.Character, java.lang.Integer> inverted() {
            return from;
        }
    }
    /** The wrapping {@code java.lang.Integer} ← {@code ceylon.language.Character} */
    static class FromCeylonCharacter implements Wrapping<ceylon.language.Character, java.lang.Integer> {
        private static final long serialVersionUID = 3285319452086713548L;
        private final ToCeylonCharacter to;
        private final boolean allowNull;
        public FromCeylonCharacter(ToCeylonCharacter to, boolean allowNull) {
            this.to = to;
            this.allowNull = allowNull;
        }
        @Override
        public java.lang.Integer wrap(ceylon.language.Character from) {
            if (from == null) {
                if (!allowNull) {
                    checkNull(from, "of ceylon.language.Character into java.lang.Character");
                }
                return null;
            }
            return from.codePoint;
        }
        @Override
        public Wrapping<java.lang.Integer, ceylon.language.Character> inverted() {
            return to;
        }
    }
    /** The wrapping {@code java.lang.Integer} → {@code ceylon.language.Character} (throws on null) */
    public static final ToCeylonCharacter TO_CEYLON_CHARACTER = new ToCeylonCharacter(false);
    /** The wrapping {@code java.lang.Integer} → {@code ceylon.language.Character} (allows null) */
    public static final ToCeylonCharacter TO_CEYLON_CHARACTER_OR_NULL = new ToCeylonCharacter(true);
    
    /** The wrapping {@code java.lang.String} → {@code ceylon.language.String} */
    static class ToCeylonString implements Wrapping<java.lang.String, ceylon.language.String> {
        private static final long serialVersionUID = -454959661466022465L;
        private final FromCeylonString from;
        private final boolean allowNull;
        public ToCeylonString(boolean allowNull) {
            this.allowNull = allowNull;
            from = new FromCeylonString(this, allowNull);
        }
        @Override
        public ceylon.language.String wrap(java.lang.String from) {
            if (from == null) {
                if (!allowNull) {
                    checkNull(from, "of java.lang.String into ceylon.language.String");
                }
                return null;
            }
            return ceylon.language.String.instance(from);
        }
        @Override
        public Wrapping<ceylon.language.String, java.lang.String> inverted() {
            return from;
        }
    }
    /** The wrapping {@code java.lang.String} ← {@code ceylon.language.String} */
    static class FromCeylonString implements Wrapping<ceylon.language.String, java.lang.String> {
        private static final long serialVersionUID = -2239577658438069244L;
        private final ToCeylonString to;
        private final boolean allowNull;
        public FromCeylonString(ToCeylonString to, boolean allowNull) {
            this.to = to;
            this.allowNull = allowNull;
        }
        @Override
        public java.lang.String wrap(ceylon.language.String from) {
            if (from == null) {
                if (!allowNull) {
                    checkNull(from, "of ceylon.language.String into java.lang.String");
                }
                return null;
            }
            return from.value;
        }
        @Override
        public Wrapping<java.lang.String, ceylon.language.String> inverted() {
            return to;
        }
    }
    /** The wrapping {@code java.lang.Long} → {@code ceylon.language.Integer} (throws on null) */
    public static final ToCeylonString TO_CEYLON_STRING = new ToCeylonString(false);
    /** The wrapping {@code java.lang.Long?} → {@code ceylon.language.Integer?} (allows null) */
    public static final ToCeylonString TO_CEYLON_STRING_OR_NULL = new ToCeylonString(true);
    
    /** The wrapping {@code java.util.Map.Entry} → {@code ceylon.language.Entry} */
    static <JavaKey,JavaItem,CeylonKey,CeylonItem> Wrapping<java.util.Map.Entry<JavaKey, JavaItem>, ceylon.language.Entry<CeylonKey, CeylonItem>> toCeylonEntry(
            final TypeDescriptor $reified$Key, final TypeDescriptor $reified$Item,
            final Wrapping<JavaKey,CeylonKey> keyWrapping, 
            final Wrapping<JavaItem,CeylonItem> itemWrapping) {
        /** The wrapping {@code java.util.Map.Entry} → {@code ceylon.language.Entry} */
        class ToCeylonEntry implements Wrapping<java.util.Map.Entry<JavaKey, JavaItem>, ceylon.language.Entry<CeylonKey, CeylonItem>> {
            private static final long serialVersionUID = -8985242707013915308L;
            private final FromCeylonEntry<CeylonKey, CeylonItem, JavaKey, JavaItem> from;
            
            public ToCeylonEntry() {
                this.from = new FromCeylonEntry<CeylonKey, CeylonItem, JavaKey, JavaItem>(
                        this, keyWrapping.inverted(), itemWrapping.inverted());
            }
            
            @Override
            public Entry<CeylonKey, CeylonItem> wrap(java.util.Map.Entry<JavaKey, JavaItem> from) {
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
    static class FromCeylonEntry<CeylonKey,CeylonItem,JavaKey,JavaItem> implements Wrapping<ceylon.language.Entry<CeylonKey, CeylonItem>, java.util.Map.Entry<JavaKey, JavaItem>> {
        private static final long serialVersionUID = 3225748480205040653L;
        private final Wrapping<java.util.Map.Entry<JavaKey, JavaItem>, Entry<CeylonKey, CeylonItem>> from;
        private final Wrapping<CeylonKey,JavaKey> keyWrapping;
        private final Wrapping<CeylonItem,JavaItem> itemWrapping;
        public FromCeylonEntry(Wrapping<java.util.Map.Entry<JavaKey, JavaItem>, Entry<CeylonKey, CeylonItem>> from,
                Wrapping<CeylonKey,JavaKey> keyWrapping, Wrapping<CeylonItem,JavaItem> itemWrapping) {
            this.from = from;
            this.keyWrapping = keyWrapping;
            this.itemWrapping = itemWrapping;
        }
        @Override
        public java.util.Map.Entry<JavaKey, JavaItem> wrap(Entry<CeylonKey, CeylonItem> from) {
            return new AbstractMap.SimpleEntry<JavaKey,JavaItem>(
                    keyWrapping.wrap((CeylonKey)from.getKey()), 
                    itemWrapping.wrap((CeylonItem)from.getItem()));
        }
        @Override
        public Wrapping<java.util.Map.Entry<JavaKey, JavaItem>, Entry<CeylonKey, CeylonItem>> inverted() {
            return from;
        }
    }
    
    static class ToCeylonList<Java,Ceylon> implements Wrapping<java.util.List<Java>, ceylon.language.List<Ceylon>> {

        private static final long serialVersionUID = 8713948240008574093L;
        private final FromCeylonList<Ceylon,Java> from;
        private TypeDescriptor $reified$Element;
        private Wrapping<Java, Ceylon> elementWrapping;
        private boolean allowNull;
        
        public ToCeylonList(TypeDescriptor $reified$Element, Wrapping<Java, Ceylon> elementWrapping, boolean allowNull) {
            this.$reified$Element = $reified$Element;
            this.elementWrapping = elementWrapping;
            this.allowNull = allowNull;
            from = new FromCeylonList<Ceylon,Java>(this, elementWrapping.inverted(), allowNull);
        }
        
        @Override
        public List<Ceylon> wrap(java.util.List<Java> from) {
            if (from == null) {
                if (!allowNull) {
                    checkNull(from, "of java.util.List into ceylon.language.List");
                }
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
    
    static class FromCeylonList<Ceylon,Java> implements Wrapping<ceylon.language.List<Ceylon>, java.util.List<Java>> {

        private static final long serialVersionUID = -6243360806548129492L;
        private final ToCeylonList<Java,Ceylon> to;
        private final Wrapping<Ceylon,Java> elementWrapping;
        private final boolean allowNull;

        public FromCeylonList(ToCeylonList<Java,Ceylon> to, Wrapping<Ceylon,Java> elementWrapping, boolean allowNull) {
            this.to = to;
            this.elementWrapping = elementWrapping;
            this.allowNull = allowNull;
        }
        
        @Override
        public java.util.List<Java> wrap(List<Ceylon> from) {
            if (from == null) {
                if (!allowNull) {
                    checkNull(from, "of ceylon.language.List into java.util.List");
                }
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
    
    /** 
     * Returns a Wrapping to convert a java.util.List into a ceylon.language.List
     * 
     * If the given {@code $reified$Element} represents:
     * <ul><li>Boolean, Byte, Integer, Float, Character or String or</li>
     * <li>List, Set or Map</li>
     * <li>or optional types of these</li>
     * </ul>
     * the elements will also be wrapped/unwrapped as needed. 
     * 
     * The given {@code allowNull} determines whether the toplevel list is allowed to be null 
     */
    public static <Java,Ceylon> Wrapping<java.util.List<Java>, ceylon.language.List<Ceylon>> toCeylonList(TypeDescriptor $reified$Element, boolean allowNull) {
        Wrapping<Java, Ceylon> elementWrapping = elementMapping($reified$Element);
        return new ToCeylonList<Java,Ceylon>($reified$Element, elementWrapping, allowNull);
    }
    
    static class ToCeylonSet<Java,Ceylon> implements Wrapping<java.util.Set<Java>, ceylon.language.Set<Ceylon>> {

        private static final long serialVersionUID = -2127631972330618405L;
        private final FromCeylonSet<Ceylon,Java> from;
        private final TypeDescriptor $reified$Element;
        private final Wrapping<Java, Ceylon> elementWrapping;
        private final boolean allowNull;
        
        public ToCeylonSet(TypeDescriptor $reified$Element, Wrapping<Java, Ceylon> elementWrapping, boolean allowNull) {
            this.$reified$Element = $reified$Element;
            this.elementWrapping = elementWrapping;
            this.allowNull = allowNull;
            from = new FromCeylonSet<Ceylon,Java>(this, elementWrapping.inverted(), allowNull);
        }
        
        @Override
        public ceylon.language.Set<Ceylon> wrap(java.util.Set<Java> from) {
            if (from == null) {
                if (!allowNull) {
                    checkNull(from, "of java.util.Set into ceylon.language.Set");
                }
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
    
    static class FromCeylonSet<Ceylon,Java> implements Wrapping<ceylon.language.Set<Ceylon>, java.util.Set<Java>> {

        private static final long serialVersionUID = -5269048676830359439L;
        private final ToCeylonSet<Java,Ceylon> to;
        private final Wrapping<Ceylon,Java> elementWrapping;
        private final boolean allowNull;

        public FromCeylonSet(ToCeylonSet<Java,Ceylon> to, Wrapping<Ceylon,Java> elementWrapping, boolean allowNull) {
            this.to = to;
            this.elementWrapping = elementWrapping;
            this.allowNull = allowNull;
        }
        
        @Override
        public java.util.Set<Java> wrap(ceylon.language.Set<Ceylon> from) {
            if (from == null) {
                if (!allowNull) {
                    checkNull(from, "of ceylon.language.Set into java.util.Set");
                }
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
    
    /** 
     * Returns a Wrapping to convert a java.util.Set into a ceylon.language.Set
     * 
     * If the given {@code $reified$Element} represents:
     * <ul><li>Boolean, Byte, Integer, Float, Character or String or</li>
     * <li>List, Set or Map</li>
     * <li>or optional types of these</li>
     * </ul>
     * the elements will also be wrapped/unwrapped as needed. 
     * 
     * The given {@code allowNull} determines whether the toplevel set is allowed to be null 
     */
    public static<Java,Ceylon> Wrapping<java.util.Set<Java>, ceylon.language.Set<Ceylon>> toCeylonSet(TypeDescriptor $reified$Element, boolean allowNull) {
        Wrapping<Java, Ceylon> elementWrapping = elementMapping($reified$Element);
        return new ToCeylonSet<Java,Ceylon>($reified$Element, elementWrapping, allowNull);
    }
    
    static class ToCeylonMap<JavaKey,JavaItem,CeylonKey,CeylonItem> implements Wrapping<java.util.Map<JavaKey,JavaItem>, ceylon.language.Map<CeylonKey,CeylonItem>> {

        private static final long serialVersionUID = -1840889262296370101L;
        private final FromCeylonMap<CeylonKey,CeylonItem,JavaKey,JavaItem> from;
        private final TypeDescriptor $reified$Key;
        private final TypeDescriptor $reified$Item;
        private final Wrapping<JavaKey, CeylonKey> keyWrapping;
        private final Wrapping<JavaItem, CeylonItem> itemWrapping;
        private boolean allowNull;
        
        public ToCeylonMap(TypeDescriptor $reified$Key, TypeDescriptor $reified$Item, Wrapping<JavaKey, CeylonKey> keyWrapping, Wrapping<JavaItem, CeylonItem> itemWrapping, boolean allowNull) {
            this.$reified$Key = $reified$Key;
            this.$reified$Item = $reified$Item;
            this.keyWrapping = keyWrapping;
            this.itemWrapping = itemWrapping;
            this.allowNull = allowNull;
            from = new FromCeylonMap<CeylonKey,CeylonItem,JavaKey,JavaItem>($reified$Key, $reified$Item, this, keyWrapping.inverted(), itemWrapping.inverted(), allowNull);
        }
        
        @Override
        public ceylon.language.Map<CeylonKey,CeylonItem> wrap(java.util.Map<JavaKey, JavaItem> from) {
            if (from == null) {
                if (!allowNull) {
                    checkNull(from, "of java.util.Map into ceylon.language.Map");
                }
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
    
    static class FromCeylonMap<CeylonKey,CeylonItem,JavaKey,JavaItem> implements Wrapping<ceylon.language.Map<CeylonKey,CeylonItem>, java.util.Map<JavaKey,JavaItem>> {

        private static final long serialVersionUID = -4156672676280526053L;
        private final TypeDescriptor $reified$Key;
        private final TypeDescriptor $reified$Item;
        private final ToCeylonMap<JavaKey,JavaItem,CeylonKey,CeylonItem> to;
        private final Wrapping<CeylonKey,JavaKey> keyWrapping;
        private final Wrapping<CeylonItem,JavaItem> itemWrapping;
        private final boolean allowNull;

        public FromCeylonMap(
                TypeDescriptor $reified$Key,
                TypeDescriptor $reified$Item,
                ToCeylonMap<JavaKey,JavaItem,CeylonKey,CeylonItem> to, 
                Wrapping<CeylonKey,JavaKey> keyWrapping, 
                Wrapping<CeylonItem,JavaItem> itemWrapping, boolean allowNull) {
            this.$reified$Key = $reified$Key;
            this.$reified$Item = $reified$Item;
            this.to = to;
            this.keyWrapping = keyWrapping;
            this.itemWrapping = itemWrapping;
            this.allowNull = allowNull;
        }
        
        @Override
        public java.util.Map<JavaKey, JavaItem> wrap(ceylon.language.Map<CeylonKey, CeylonItem> from) {
            if (from == null) {
                if (!allowNull) {
                    checkNull(from, "of ceylon.language.Map into java.util.Map");
                }
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
    
    /** 
     * Returns a Wrapping to convert a java.util.Map into a ceylon.language.Map
     * 
     * If the given {@code $reified$Key} and {@code $reified$Item} represents:
     * <ul><li>Boolean, Byte, Integer, Float, Character or String or</li>
     * <li>List, Set or Map</li>
     * <li>or optional types of these</li>
     * </ul>
     * the elements will also be wrapped/unwrapped as needed. 
     * 
     * The given {@code allowNull} determines whether the toplevel map is allowed to be null 
     */
    public static<JavaKey,JavaItem,CeylonKey,CeylonItem> Wrapping<java.util.Map<JavaKey,JavaItem>, ceylon.language.Map<CeylonKey,CeylonItem>> toCeylonMap(TypeDescriptor $reified$Key, TypeDescriptor $reified$Item, boolean allowNull) {
        Wrapping<JavaKey, CeylonKey> keyWrapping = elementMapping($reified$Key);
        // Because you get a null value when looking for a not-present key
        // we must ensure that the item wrapping admits null
        Wrapping<JavaItem, CeylonItem> itemWrapping = elementMapping($reified$Item.containsNull() ? $reified$Item : TypeDescriptor.union($reified$Item, Null.$TypeDescriptor$));
        return new ToCeylonMap<JavaKey,JavaItem,CeylonKey,CeylonItem>($reified$Key, $reified$Item, keyWrapping, itemWrapping, allowNull);
    }
}