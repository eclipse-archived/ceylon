function initType(a,b,c,d,e,f,g,h,i,j,k,l){}//IGNORE
function initTypeProto(a,b,c,d,e,f,g){}//IGNORE
function inheritProto(a,b,c){}//IGNORE
function exists(x){}//IGNORE
function Exception(){}//IGNORE
function isOfType(a,b){}//IGNORE
function String$(x,l){}//IGNORE
function TypeCategory(a,b){}//IGNORE
var exports,Container,$finished,Cloneable,smaller,larger,Object$,Basic;//IGNORE
var Iterable,Iterator;//IGNORE

function Map(wat) {
    return wat;
}
function $init$Map() {
    if (Map.$$===undefined) {
        initTypeProto(Map, 'ceylon.language::Map', Collection, $init$Correspondence(), Cloneable);
    }
    return Map;
}
$init$Map();
var Map$proto = Map.$$.prototype;
Map$proto.equals = function(other) {
    if (isOfType(other, {t:Map}) && other.getSize().equals(this.getSize())) {
        var iter = this.getIterator();
        var entry; while ((entry = iter.next()) !== $finished) {
            var oi = other.get(entry.getKey());
            if (oi === null || !entry.getItem().equals(oi)) {
                return false;
            }
        }
        return true;
    }
    return false;
}
Map$proto.getHash = function() {
    var hc=1;
    var iter=this.getIterator();
    var elem; while((elem=iter.next())!=$finished) {
        hc*=31;
        hc += elem.getHash();
    }
    return hc;
}

Map$proto.getValues = function() { return MapValues(this); }
function MapValues(map, $$targs$$) {
    var val = new MapValues.$$;
    val.map = map;
    val.$$targs$$=$$targs$$;
    return val;
}
initTypeProto(MapValues, 'ceylon.language::MapValues', Basic, Collection);
var MapValues$proto = MapValues.$$.prototype;
MapValues$proto.getSize = function() { return this.map.getSize(); }
MapValues$proto.getEmpty = function() { return this.map.getEmpty(); }
MapValues$proto.getClone = function() { return this; }
MapValues$proto.getIterator = function() { return MapValuesIterator(this.map); }
function MapValuesIterator(map) {
    var iter = new MapValuesIterator.$$;
    iter.it = map.getIterator();
    iter.$$targs$$=[map.$$targs$$[1]];
    return iter;
}
initTypeProto(MapValuesIterator, 'ceylon.language::MapValuesIterator', Basic, Iterator);
MapValuesIterator.$$.prototype.next = function() {
    var entry = this.it.next();
    return (entry!==$finished) ? entry.getItem() : $finished;
}

Map$proto.getKeys = function() { return KeySet(this); }
function KeySet(map, $$targs$$) {
    var set = new KeySet.$$;
    set.map = map;
    set.$$targs$$=[map.$$targs$$[0]];
    return set;
}
initTypeProto(KeySet, 'ceylon.language::KeySet', Basic, Set);
var KeySet$proto = KeySet.$$.prototype;
KeySet$proto.getSize = function() { return this.map.getSize(); }
KeySet$proto.getEmpty = function() { return this.map.getEmpty(); }
KeySet$proto.contains = function(elem) { return this.map.defines(elem); }
KeySet$proto.getClone = function() { return this; }
KeySet$proto.getIterator = function() { return KeySetIterator(this.map); }
function KeySetIterator(map) {
    var iter = new KeySetIterator.$$;
    iter.it = map.getIterator();
    iter.$$targs$$=[map.$$targs$$[0]];
    return iter;
}
initTypeProto(KeySetIterator, 'ceylon.language::KeySetIterator', Basic, Iterator);
KeySetIterator.$$.prototype.next = function() {
    var entry = this.it.next();
    return (entry!==$finished) ? entry.getKey() : $finished;
}
KeySet$proto.union = function(other, $$$mptypes) {
    var set = hashSetFromMap(this.map);
    set.addAll(other);
    return set;
}
KeySet$proto.intersection = function(other, $$$mptypes) {
    var set = HashSet(undefined, [{t:'i', l:[this.$$targs$$[0], $$$mptypes[0]]}]);
    var it = this.map.getIterator();
    var entry;
    while ((entry=it.next()) !== $finished) {
        var key = entry.getKey();
        if (other.contains(key)) { set.add(key); }
    }
    return set;
}
KeySet$proto.exclusiveUnion = function(other, $$$mptypes) {
    var set = this.complement(other, $$$mptypes);
    var it = other.getIterator();
    var elem;
    while ((elem=it.next()) !== $finished) {
        if (!this.map.defines(elem)) { set.add(elem); }
    }
    return set;
}
KeySet$proto.complement = function(other, $$$mptypes) {
    var set = HashSet(undefined, [{t:'u', l:[this.$$targs$$[0], $$$mptypes[0]]}]);
    var it = this.map.getIterator();
    var entry;
    while ((entry=it.next()) !== $finished) {
        var key = entry.getKey();
        if (!other.contains(key)) { set.add(key); }
    }
    return set;
}

Map$proto.getInverse = function() {
    var inv = HashMap(undefined, [this.$$targs$$[1], {t:Set, a:[this.$$targs$$[0]]}]);
    var it = this.getIterator();
    var entry;
    var newSet = HashSet(undefined, [this.$$targs$$[0]]);
    while ((entry=it.next()) !== $finished) {
        var item = entry.getItem();
        var set = inv.put(Entry(item, newSet, [this.$$targs$$[1], {t:Set, a:[this.$$targs$$[0]]}]), true);
        if (set === null) {
            set = newSet;
            newSet = HashSet(undefined, [this.$$targs$$[0]]);
        }
        set.add(entry.getKey());
    }
    return inv;
}
Map$proto.mapItems = function(mapping, $$$mptypes) {
    function EmptyMap(orig, $$targs$$) {
        var em = new EmptyMap.$$;
        em.$$targs$$=$$targs$$;
        Basic(em);
        em.orig=orig;
        em.clone=function() { return this; }
        em.getItem=function() { return null; }
        em.getIterator=function() {
            function miter(iter, $$targs1$$) {
                var $i = new miter.$$;
                $i.iter = iter;
                $i.$$targs$$=$$targs1$$;
                $i.next = function() {
                    var e = this.iter.next();
                    return e===$finished ? e : Entry(e.getKey(), mapping(e.getKey(), e.getItem()), this.$$targs$$);
                };
                return $i;
            }
            initTypeProto(miter, 'ceylon.language::MappedIterator', Basic, Iterator);
            return miter(orig.getIterator(), [{t:Entry, a:em.$$targs$$}]);
        }
        em.getSize=function() { return this.orig.getSize(); }
        em.getString=function() { return String$('',0); }
        return em;
    }
    initTypeProto(EmptyMap, 'ceylon.language::EmptyMap', Basic, Map);
    return EmptyMap(this, [this.$$targs$$[0], $$$mptypes[0]]);
}
exports.Map=Map;

function HashMap(entries, $$targs$$, map) {
    if (map===undefined) { map = new HashMap.$$; }
    Basic(map);
    map.$$targs$$=$$targs$$;
    map.map = {};
    map.size = 0;
    if (entries !== undefined) { map.putAll(entries); }
    return map;
}
initTypeProto(HashMap, 'ceylon.language::HashMap', Basic, Map);
function copyHashMap(orig) {
    var map = HashMap(undefined, orig.$$targs$$);
    for (var hash in Object.keys(orig.map)) {
        map.map[hash] = orig.map[hash].slice(0);
    }
    map.size = orig.size;
    return map;
}
var HashMap$proto = HashMap.$$.prototype;
HashMap$proto.put = function(entry, keepOldItem) {
    var key = entry.getKey();
    var hash = key.getHash();
    var arr = this.map[hash];
    if (arr === undefined) {
        arr = [];
        this.map[hash] = arr;
    }
    for (var i=0; i<arr.length; ++i) {
        var e = arr[i];
        if (e.getKey().equals(key)) {
            if (!keepOldItem) { arr[i] = entry; }
            return e.getItem();
        }
    }
    arr.push(entry);
    ++this.size;
    return null;
}
HashMap$proto.putAll = function(entries) {
    var it = entries.getIterator();
    var entry;
    while ((entry=it.next()) !== $finished) { this.put(entry); }
}
HashMap$proto.getSize = function() { return this.size; }
HashMap$proto.getEmpty = function() { return this.size===0; }
HashMap$proto.getLast = function() {
    var hashs = Object.keys(this.map);
    if (hashs.length === 0) { return null; }
    var arr = this.map[hashs[hashs.length - 1]];
    return arr[arr.length - 1];
}
HashMap$proto.getIterator = function() { return HashMapIterator(this.map); }
HashMap$proto.getClone = function() { return this; }
HashMap$proto.get = function(key) {
    var hash = key.getHash();
    var arr = this.map[hash];
    if (arr !== undefined) {
        for (var i=0; i<arr.length; ++i) {
            var entry = arr[i];
            if (entry.getKey().equals(key)) { return entry.getItem(); }
        }
    }
    return null;
}
HashMap$proto.contains = function(elem) {
    if (isOfType(elem, {t:Entry})) {
        var item = this.get(elem.getKey());
        if (item !== null) { return item.equals(elem.getItem()); }
    }
    return false;
}
HashMap$proto.defines = function(key) { return this.get(key) !== null; }

function HashSet(elems, $$targs$$, set) {
    if (set===undefined) { set = new HashSet.$$; }
    Basic(set);
    set.$$targs$$=$$targs$$;
    set.map = HashMap(undefined, [$$targs$$[0], {t:Boolean$}]);
    if (elems !== undefined) { set.addAll(elems); }
    return set;
}
initTypeProto(HashSet, 'ceylon.language::HashSet', Basic, Set);
function hashSetFromMap(map) {
    var set = new HashSet.$$;
    Basic(set);
    set.map = this;
    set.$$targs$$=[map.$$targs$$[0]];
    return set;
}
var HashSet$proto = HashSet.$$.prototype;
HashSet$proto.add = function(elem) { this.map.put(Entry(elem, true, [this.$$targs$$[0], {t:Boolean$}])); }
HashSet$proto.addAll = function(elems) {
    var it = elems.getIterator();
    var elem;
    while ((elem=it.next()) !== $finished) { this.map.put(Entry(elem, true, [this.$$targs$$[0], {t:Boolean$}])); }
}
HashSet$proto.getSize = function() { return this.map.size; }
HashSet$proto.getEmpty = function() { return this.map.size===0; }
HashSet$proto.getLast = function() {
    var entry = this.map.getLast();
    return (entry !== null) ? entry.getKey() : null;
}
HashSet$proto.getIterator = function() { return HashSetIterator(this.map, this.$$targs$$); }
HashSet$proto.getClone = function() { return this; }
HashSet$proto.contains = function(elem) { return this.map.get(elem) !== null; }
HashSet$proto.union = function(other, $$$mptypes) {
    var set = hashSetFromMap(copyHashMap(this.map));
    set.$$targs$$=[{t:'u', l:[this.$$targs$$[0], $$$mptypes[0]]}];
    set.addAll(other);
    return set;
}
HashSet$proto.intersection = function(other, $$$mptypes) {
    var set = HashSet(undefined, [{t:'i', l:[this.$$targs$$[0], $$$mptypes[0]]}]);
    var it = this.getIterator();
    var elem;
    while ((elem=it.next()) !== $finished) {
        if (other.contains(elem)) { set.map.put(Entry(elem, true)); }
    }
    return set;
}
HashSet$proto.exclusiveUnion = function(other) {
    var set = this.complement(other);
    var it = other.getIterator();
    var elem;
    while ((elem=it.next()) !== $finished) {
        if (this.map.get(elem) === null) { set.map.put(Entry(elem, true)); }
    }
    return set;
}
HashSet$proto.complement = function(other) {
    var set = HashSet(undfined, [{t:'u', l:[this.$$targs$$[0], $$$mptypes[0]]}]);
    var it = this.getIterator();
    var elem;
    while ((elem=it.next()) !== $finished) {
        if (!other.contains(elem)) { set.map.put(Entry(elem, true)); }
    }
    return set;
}

function HashMapIterator(map, $$targs$$) {
    var it = new HashMapIterator.$$;
    it.map = map;
    it.$$targs$$=$$targs$$;
    it.hashs = Object.keys(map);
    it.hashIndex = 0;
    it.arrIndex = 0;
    return it;
}
initTypeProto(HashMapIterator, 'ceylon.language::HashMapIterator', Basic, Iterator);
HashMapIterator.$$.prototype.next = function() {
    var hash = this.hashs[this.hashIndex];
    if (hash !== undefined) {
        var arr = this.map[hash];
        var entry = arr[this.arrIndex++];
        if (this.arrIndex >= arr.length) {
            ++this.hashIndex;
            this.arrIndex = 0;
        }
        return entry;
    }
    return $finished;
}
function HashSetIterator(map, $$targs$$) {
    var it = new HashSetIterator.$$;
    it.mapIt = map.getIterator();
    it.$$targs$$=$$targs$$;
    return it;
}
initTypeProto(HashSetIterator, 'ceylon.language::HashSetIterator', Basic, Iterator);
HashSetIterator.$$.prototype.next = function() {
    var entry = this.mapIt.next();
    return (entry !== $finished) ? entry.getKey() : $finished;
}
