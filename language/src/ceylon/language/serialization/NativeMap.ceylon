/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
import ceylon.language.impl {
    BaseIterable,
    BaseIterator
}

"Need a map-like thing, but can't use java.util.HashMap directly, 
 not ceylon.collection::HashMap"
native class NativeMap<Key,Element>() {
    shared native Element? get(Key id);
    shared native void put(Key id, Element instanceOrPartial);
    shared native Boolean contains(Key id);
    shared native {Key*} keys;
    shared native {Element*} items;
    shared native Integer size;
    shared native actual String string;
}


native("jvm") class NativeMap<Key,Element>() {
    
    import java.util {
        HashMap,
        JavaIterator=Iterator
    }
    native("jvm") HashMap<Key, Element> m = HashMap<Key, Element>();
    
    shared native("jvm") void put(Key id, Element instanceOrPartial) {
        m.put(id, instanceOrPartial);
    }
    
    shared native("jvm") Element? get(Key id) {
        return m.get(id of Object?);
    }
    
    shared native("jvm") Boolean contains(Key id) {
        return m.containsKey(id of Object?);
    }
    
    shared native("jvm") Integer size {
        return m.size();
    }
    
    shared native("jvm") Iterable<Element, Null> items {
        return object extends BaseIterable<Element,Null>() satisfies Identifiable {
            
            shared actual Iterator<Element> iterator() {
                JavaIterator<Element> it = m.values().iterator();
                return object extends BaseIterator<Element>() satisfies Identifiable {
                    
                    shared actual Element|Finished next() {
                        if (it.hasNext()) {
                            return it.next();
                        } else { 
                            return finished;
                        }
                    }
                };
            }
        };
    }
    
    
    shared native("jvm") Iterable<Key, Null> keys {
        return object extends BaseIterable<Key,Null>() satisfies Identifiable {
            
            shared actual Iterator<Key> iterator() {
                JavaIterator<Key> it = m.keySet().iterator();
                return object extends BaseIterator<Key>() satisfies Identifiable {
                    
                    shared actual Key|Finished next() {
                        if (it.hasNext()) {
                            return it.next();
                        } else { 
                            return finished;
                        }
                    }
                };
            }
        };
    }
    
    shared native("jvm") actual String string {
        return m.string;
    }
}

native("js") class NativeMap<Key,Element>() {

    dynamic ks;
    dynamic vs;
    dynamic {
        ks = dynamic[null];
        vs = dynamic[null];
    }
    "Find the index of the specified key, or -1 if it isn't in the map"
    Integer find(Key id) {
        if (id exists) {
            dynamic {
                Integer len=ks.length;
                if (len < 2) {
                    return -1;
                }
                for (i in 1:len-1) {
                    if (id == ks[i]) {
                        return i;
                    }
                }
            }
            return -1;
        }
        return 0;
    }

    shared native("js") void put(Key id, Element instanceOrPartial) {
        value i = find(id);
        if (i >= 0) {
            //replace
            dynamic {
                setObjectProperty(vs, i, instanceOrPartial);
            }
        } else if (i < 0) {
            //new entry
            dynamic {
                ks.push(id);
                vs.push(instanceOrPartial);
            }
        }
    }
    
    shared native("js") Element? get(Key id) {
        value i = find(id);
        dynamic {
            return i >= 0 then vs[i] else null;
        }
    }
    
    shared native("js") Boolean contains(Key id) =>
        find(id) >= 0;
    
    shared native("js") Integer size {
        dynamic {
            return ks.length-1;
        }
    }
    
    shared native("js") Iterable<Element, Null> items {
        return object extends BaseIterable<Element,Null>() satisfies Identifiable {
            
            shared actual Iterator<Element> iterator() {
                variable value i=1;
                return object extends BaseIterator<Element>() satisfies Identifiable {
                    
                    shared actual Element|Finished next() {
                        dynamic {
                            if (i >= vs.length) {
                                return finished;
                            }
                            Element e = vs[i];
                            i++;
                            return e;
                        }
                    }
                };
            }
        };
    }
    
    
    shared native("js") Iterable<Key, Null> keys {
        return object extends BaseIterable<Key,Null>() satisfies Identifiable {
            
            shared actual Iterator<Key> iterator() {
                variable value i=1;
                return object extends BaseIterator<Key>() satisfies Identifiable {
                    
                    shared actual Key|Finished next() {
                        dynamic {
                            if (i >= ks.length) {
                                return finished;
                            }
                            Key e = ks[i];
                            i++;
                            return e;
                        }
                    }
                };
            }
        };
    }
    
    shared native("js") actual String string {
        value sb=StringBuilder();
        sb.append("{");
        dynamic {
            if (vs[0] exists) {
                sb.append("<null> ->").append(vs[0].string);
            }
            if (ks.length>1) {
                for (i in 1:ks.length-1) {
                    if (sb.size > 1) {
                        sb.append(", ");
                    }
                    sb.append(ks[i]).append("->");
                    if (vs[i] exists) {
                        sb.append(vs[i].string);
                    } else {
                        sb.append("[null]");
                    }
                }
            }
        }
        sb.append("}");
        return sb.string;
    }
}
