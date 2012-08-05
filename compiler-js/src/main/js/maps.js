function initType(a,b,c,d,e,f,g,h,i,j,k,l){}//IGNORE
function initTypeProtoI(a,b,c,d,e,f){}//IGNORE
function initTypeProto(a,b,c,d,e,f,g){}//IGNORE
function inheritProto(a,b,c){}//IGNORE
function exists(x){}//IGNORE
function Exception(){}//IGNORE
function isOfType(a,b){}//IGNORE
function getBottom(){}//IGNORE
function String$(x,l){}//IGNORE
function TypeCategory(a,b){}//IGNORE
function ArraySequence(x){}//IGNORE
var exports,Container,$finished,Cloneable,smaller,larger,Correspondence,Object$,IdentifiableObject;//IGNORE
var Iterable,Iterator;//IGNORE

function Map(wat) {
    return wat;
}
initTypeProtoI(Map, 'ceylon.language.Map', Collection, Correspondence, Cloneable);
var Map$proto = Map.$$.prototype;
Map$proto.equals = function(other) {
    if (isOfType(other, 'ceylon.language.Map') && other.getSize().equals(this.getSize())) {
        var iter = this.getIterator();
        var entry; while ((entry = iter.next()) !== $finished) {
            var oi = other.item(entry.getKey());
            if (oi === null || !entry.getItem().equals(oi.getItem())) {
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
Map$proto.getValues = function() {
    function $map$values(outer) {
        var mv = new $map$values.$$;
        mv.outer=outer;
        IdentifiableObject(mv);
        Collection(mv);
        mv.clone=function() { return this; }
        mv.equals=function() { return false; }
        mv.getHash=function() { return outer.getHash(); }
        mv.getIterator=function() { return getBottom(); }
        mv.getSize=function() { return outer.getSize(); }
        mv.getString=function() { return String$('',0); }
        return mv;
    }
    initTypeProto($map$values, 'ceylon.language.MapValues', IdentifiableObject, Collection);
    return $map$values(this);
}
Map$proto.getKeys = function() {
    function $map$keys(outer) {
        var mk = new $map$keys.$$;
        mk.outer=outer;
        IdentifiableObject(mk);
        Set(mk);
        mk.clone=function() { return this; }
        mk.equals=function() { return false; }
        mk.getHash=function() { return outer.getHash(); }
        mk.getIterator=function() { return getBottom(); }
        mk.getSize=function() { return outer.getSize(); }
        mk.getString=function() { return String$('',0); }
        return mk;
    }
    initTypeProto($map$keys, 'ceylon.language.MapKeys', IdentifiableObject, Set);
    return $map$keys(this);
}
Map$proto.getInverse = function() {
    function $map$inv(outer) {
        var inv = new $map$inv.$$;
        inv.outer=outer;
        IdentifiableObject(inv);
        Map(inv);
        inv.clone=function() { return this; }
        inv.equals=function() { return false; }
        inv.getHash=function() { return outer.getHash(); }
        inv.getItem=function() { return getBottom(); }
        inv.getIterator=function() { return getBottom(); }
        inv.getSize=function() { return outer.getSize(); }
        inv.getString=function() { return String$('',0); }
        return inv;
    }
    initTypeProto($map$inv, 'ceylon.language.InverseMap', IdentifiableObject, Map);
    return $map$inv(this);
}
Map$proto.mapItems = function(mapping) {
    function EmptyMap(orig) {
        var em = new EmptyMap.$$;
        IdentifiableObject(em);
        em.orig=orig;
        em.clone=function() { return this; }
        em.getItem=function() { return null; }
        em.getIterator=function() {
            function miter(iter) {
                var $i = new miter.$$;
                $i.iter = iter;
                $i.next = function() {
                    var e = this.iter.next();
                    return e===$finished ? e : Entry(e.getKey(), mapping(e.getKey(), e.getItem()));
                };
                return $i;
            }
            initTypeProto(miter, 'ceylon.language.MappedIterator', IdentifiableObject, Iterator);
            return miter(orig.getIterator());
        }
        em.getSize=function() { return this.orig.getSize(); }
        em.getString=function() { return String$('',0); }
        return em;
    }
    initTypeProto(EmptyMap, 'ceylon.language.EmptyMap', IdentifiableObject, Map);
    return EmptyMap(this);
}
exports.Map=Map;

function Set(wat) {
    return wat;
}
initTypeProtoI(Set, 'ceylon.language.Set', Collection, Cloneable);
var Set$proto = Set.$$.prototype;
Set$proto.superset = function(set) {
    var iter = set.getIterator();
    var elem; while ((elem = iter.next()) !== $finished) {
        if (!this.contains(elem)) {
            return false;
        }
    }
    return true;
}
Set$proto.subset = function(set) {
    var iter = this.getIterator();
    var elem; while ((elem = iter.next()) !== $finished) {
        if (!set.contains(elem)) {
            return false;
        }
    }
    return true;
}
Set$proto.equals = function(other) {
    if (isOfType(other, 'ceylon.language.Set')) {
        if (other.getSize().equals(this.getSize())) {
            var iter = this.getIterator();
            var elem; while ((elem = iter.next()) !== $finished) {
                if (!other.contains(elem)) {
                    return false;
                }
            }
            return true;
        }
    }
    return false;
}
Set$proto.getHash = function() {
    var hc = 1;
    var iter=this.getIterator();
    var elem;while((elem=iter.next())!=$finished) {
        hc*=31;
        hc+=elem.getHash();
    }
    return hc;
}
exports.Set=Set;

function HashMap(entries, map) {
    if (map===undefined) { map = new HashMap.$$; }
    IdentifiableObject(map);
    map.map = {};
    map.size = 0;
    if (entries !== undefined) { map.putAll(entries); }
    return map;
}
initTypeProto(HashMap, 'ceylon.language.HashMap', IdentifiableObject, Map);
function copyHashMap(orig) {
    var map = HashMap();
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
HashMap$proto.item = function(key) {
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
    if (isOfType(elem, 'ceylon.language.Entry')) {
        var item = this.item(elem.getKey());
        if (item !== null) { return item.equals(elem.getItem()); }
    }
    return false;
}
HashMap$proto.defines = function(key) { return this.item(key) !== null; }
HashMap$proto.getKeys = function() { return hashSetFromMap(this); }
HashMap$proto.getValues = function() {
    //TODO: return a view instead of a copy
    if (this.size === 0) { return $empty; }
    var items = Array(this.size);
    var it = getIterator();
    var entry;
    var index = 0;
    while ((entry=it.next()) !== $finished) { items[index++] = entry; }
    return ArraySequence(items);
}

function HashSet(elems, set) {
    if (set===undefined) { set = new HashSet.$$; }
    IdentifiableObject(set);
    set.map = HashMap();
    if (elems !== undefined) { set.addAll(elems); }
    return set;
}
initTypeProto(HashSet, 'ceylon.language.HashSet', IdentifiableObject, Set);
function hashSetFromMap(map) {
    var set = new HashSet.$$;
    IdentifiableObject(set);
    set.map = this;
    return set;
}
var HashSet$proto = HashSet.$$.prototype;
HashSet$proto.add = function(elem) { this.map.put(Entry(elem, true)); }
HashSet$proto.addAll = function(elems) {
    var it = elems.getIterator();
    var elem;
    while ((elem=it.next()) !== $finished) { this.map.put(Entry(elem, true)); }
}
HashSet$proto.getSize = function() { return this.map.size; }
HashSet$proto.getEmpty = function() { return this.map.size===0; }
HashSet$proto.getLast = function() {
    var entry = this.map.getLast();
    return (entry !== null) ? entry.getKey() : null;
}
HashSet$proto.getIterator = function() { return HashSetIterator(this.map); }
HashSet$proto.getClone = function() { return this; }
HashSet$proto.contains = function(elem) { return this.map.item(elem) !== null; }
HashSet$proto.union = function(other) {
    var set = hashSetFromMap(copyHashMap(this.map));
    set.addAll(other);
    return set;
}
HashSet$proto.intersection = function(other) {
    var set = HashSet();
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
        if (this.map.item(elem) === null) { set.map.put(Entry(elem, true)); }
    }
    return set;
}
HashSet$proto.complement = function(other) {
    var set = HashSet();
    var it = this.getIterator();
    var elem;
    while ((elem=it.next()) !== $finished) {
        if (!other.contains(elem)) { set.map.put(Entry(elem, true)); }
    }
    return set;
}

function HashMapIterator(map) {
    var it = new HashMapIterator.$$;
    it.map = map;
    it.hashs = Object.keys(map);
    it.hashIndex = 0;
    it.arrIndex = 0;
    return it;
}
initTypeProto(HashMapIterator, 'ceylon.language.HashMapIterator', IdentifiableObject, Iterator);
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
function HashSetIterator(map) {
    var it = new HashSetIterator.$$;
    it.mapIt = map.getIterator();
    return it;
}
initTypeProto(HashSetIterator, 'ceylon.language.HashSetIterator', IdentifiableObject, Iterator);
HashSetIterator.$$.prototype.next = function() {
    var entry = this.mapIt.next();
    return (entry !== $finished) ? entry.getKey() : $finished;
}

function LazyMap(entries, map) {
    if (map===undefined) {map = new LazyMap.$$;}
    IdentifiableObject(map);
    map.entries = entries;
    return map;
}
initTypeProto(LazyMap, 'ceylon.language.LazyMap', IdentifiableObject, Map);
var LazyMap$proto = LazyMap.$$.prototype;
LazyMap$proto.getEmpty = function() { return this.entries.getEmpty(); }
LazyMap$proto.getSize = function() {
    var it = this.entries.getIterator();
    var count = 0;
    while (it.next() !== $finished) { ++count; }
    return count;
}
LazyMap$proto.getClone = function() { return this; }
LazyMap$proto.getIterator = function() { return this.entries.getIterator(); }
LazyMap$proto.item = function(key) {
    var it = this.entries.getIterator();
    var entry;
    while ((entry=it.next()) !== $finished) {
        if (entry.getKey().equals(key)) { return entry.getItem(); }
    }
    return null;
}
LazyMap$proto.equals = function(other) {
    var hmap = HashMap(this.entries);
    var it = other.getIterator();
    var count = 0;
    var entry;
    while ((entry=it.next()) !== $finished) {
        if (!hmap.contains(entry)) { return false; }
        ++count;
    }
    return count==hmap.getSize();
}
exports.LazyMap=LazyMap;

function LazySet(elems, set) {
    if (set===undefined) {set = new LazySet.$$;}
    IdentifiableObject(set);
    set.elems = elems;
    return set;
}
initTypeProto(LazySet, 'ceylon.language.LazySet', IdentifiableObject, Set);
var LazySet$proto = LazySet.$$.prototype;
LazySet$proto.getEmpty = function() { return this.elems.getEmpty(); }
LazySet$proto.getSize = function() { return HashSet(this.elems).getSize(); }
LazySet$proto.getClone = function() { return this; }
LazySet$proto.getIterator = function() { return HashSet(this.elems).getIterator(); }
LazySet$proto.equals = function(other) {
    var hset = HashSet(this.elems);
    var it = other.getIterator();
    var elem;
    var count = 0;
    while ((elem=it.next()) !== $finished) {
        if (!hset.contains(elem)) { return false; }
        ++count;
    }
    return count==hset.getSize();
}
LazySet$proto.union = function(other) {
    var set = HashSet(this.elems);
    set.addAll(other);
    return set;
}
LazySet$proto.intersection = function(other) {
    var set = HashSet(this.elems);
    var result = HashSet();
    var it = other.getIterator();
    var elem;
    while ((elem=it.next()) !== $finished) {
        if (set.contains(elem)) { result.add(elem); }
    }
    return result;
}
LazySet$proto.exclusiveUnion = function(other) {
    return other.exclusiveUnion(HashSet(this.elems));
}
LazySet$proto.complement = function(other) {
    return other.complement(HashSet(this.elems));
}
exports.LazySet=LazySet;
