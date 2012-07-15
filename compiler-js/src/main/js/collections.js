function initType(a,b,c,d,e,f,g,h,i,j,k,l){}//IGNORE
function initTypeProtoI(a,b,c,d,e,f){}//IGNORE
function initTypeProto(a,b,c,d,e,f,g){}//IGNORE
function inheritProto(a,b,c){}//IGNORE
function exists(x){}//IGNORE
function Exception(){}//IGNORE
function Integer(x){}//IGNORE
function isOfType(a,b){}//IGNORE
function getBottom(){}//IGNORE
function String$(x,l){}//IGNORE
function TypeCategory(a,b){}//IGNORE
function ArraySequence(x){}//IGNORE
var exports,Container,$finished,Cloneable,smaller,larger,Correspondence,Object$,IdentifiableObject;//IGNORE
var Iterable,Iterator;//IGNORE

function Sized(wat) {
    return wat;
}
initTypeProtoI(Sized, 'ceylon.language.Sized', Container);
Sized.$$.prototype.getEmpty = function() {
    return this.getSize().value === 0;
}
exports.Sized=Sized;

function Category(wat) {
    return wat;
}
initType(Category, 'ceylon.language.Category');
Category.$$.prototype.containsEvery = function(keys) {
    for (var i = 0; i < keys.value.length; i++) {
        if (!this.contains(keys.value[i])) {
            return false;
        }
    }
    return true;
}
Category.$$.prototype.containsAny = function(keys) {
    for (var i = 0; i < keys.value.length; i++) {
        if (this.contains(keys.value[i])) {
            return true;
        }
    }
    return false;
}
exports.Category=Category;

function Collection(wat) {
    return wat;
}
initTypeProtoI(Collection, 'ceylon.language.Collection', Iterable, Sized, Category, Cloneable);
var Collection$proto = Collection.$$.prototype;
Collection$proto.contains = function(obj) {
    var iter = this.getIterator();
    var item;
    while ((item = iter.next()) !== $finished) {
        if (exists(item) && item.equals(obj)) {
            return true;
        }
    }
    return false;
}
exports.Collection=Collection;

function FixedSized(wat) {
    return wat;
}
initTypeProtoI(FixedSized, 'ceylon.language.FixedSized', Collection);
var FixedSized$proto = FixedSized.$$.prototype;
FixedSized$proto.getFirst = function() {
    var e = this.getIterator().next();
    return e === $finished ? null : e;
}
exports.FixedSized=FixedSized;

function Some(wat) {
    return wat;
}
initTypeProtoI(Some, 'ceylon.language.Some', FixedSized, ContainerWithFirstElement);
var $Some = Some.$$;
$Some.prototype.getFirst = function() {
    var e = this.getIterator().next();
    if (e === $finished) throw Exception();
    return e;
}
$Some.prototype.getEmpty = function() { return false; }
$Some.prototype.getFirst = function() {
    var _e = this.getIterator().next();
    if (_e === $finished) throw Exception(String$("Some.first should never get Finished!"));
    return _e;
}
exports.Some=Some;

function None(wat) {
    return wat;
}
initTypeProtoI(None, 'ceylon.language.None', FixedSized, ContainerWithFirstElement);
var None$proto = None.$$.prototype;
None$proto.getFirst = function() { return null; }
None$proto.getIterator = function() { return emptyIterator; }
None$proto.getSize = function() { return Integer(0); }
None$proto.getEmpty = function() { return true; }
None$proto.getFirst = function() { return null; }
exports.None=None;

function Ranged(wat) {
    return wat;
}
initType(Ranged, 'ceylon.language.Ranged');
exports.Ranged=Ranged;

function List(wat) {
    return wat;
}
initTypeProtoI(List, 'ceylon.language.List', Collection, Correspondence, Ranged, Cloneable);
var List$proto = List.$$.prototype;
List$proto.getSize = function() {
    var li = this.getLastIndex();
    return li === null ? Integer(0) : li.getSuccessor();
}
List$proto.defines = function(idx) {
    var li = this.getLastIndex();
    if (li === null) li = Integer(-1);
    return li.compare(idx) !== smaller;
}
List$proto.getIterator = function() {
    return ListIterator(this);
}
List$proto.equals = function(other) {
    if (isOfType(other, 'ceylon.language.List') && other.getSize().equals(this.getSize())) {
        for (var i = 0; i < this.getSize().value; i++) {
            var mine = this.item(Integer(i));
            var theirs = other.item(Integer(i));
            if (((mine === null) && theirs) || !(mine && mine.equals(theirs))) {
                return false;
            }
        }
        return true;
    }
    return false;
}
List$proto.getHash = function() {
    var hc=1;
    var iter=this.getIterator();
    var e; while ((e = iter.next()) != $finished) {
        hc*=31;
        if (e !== null) {
            hc += e.getHash().value;
        }
    }
    return Integer(hc);
}
List$proto.getString = function() {
    var s = '{';
    var first = true;
    var iter = this.getIterator();
    var item;
    while ((item = iter.next()) !== $finished) {
        s += first ? ' ' : ', ';
        if (exists(item)) {
            s += item.getString().value;
        } else {
            s += 'null';
        }
        first = false;
    }
    if (!first) {
        s += ' ';
    }
    s += '}';
    return String$(s);
}
List$proto.findLast = function(select) {
    var li = this.getLastIndex();
    if (li !== null) {
        while (li.value>=0) {
            var e = this.item(li);
            if (e !== null && select(e)) {
                return e;
            }
            li = li.getPredecessor();
        }
    }
    return null;
}
List$proto.withLeading = function(others) {
    var sb = SequenceBuilder();
    sb.appendAll(others);
    sb.appendAll(this);
    return sb.getSequence();
}
List$proto.withTrailing = function(others) {
    var sb = SequenceBuilder();
    sb.appendAll(this);
    sb.appendAll(others);
    return sb.getSequence();
}
exports.List=List;

function ListIterator(list) {
    var that = new ListIterator.$$;
    that.list=list;
    that.index=0;
    that.lastIndex=list.getLastIndex();
    if (that.lastIndex === null) {
        that.lastIndex = -1;
    } else {
        that.lastIndex = that.lastIndex.value;
    }
    return that;
}
initTypeProtoI(ListIterator, 'ceylon.language.ListIterator', Iterator);
ListIterator.$$.prototype.next = function() {
    if (this.index <= this.lastIndex) {
        return this.list.item(Integer(this.index++));
    }
    return $finished;
}

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
        hc += elem.getHash().value;
    }
    return Integer(hc);
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
        hc+=elem.getHash().value;
    }
    return Integer(hc);
}
exports.Set=Set;

function Array$() {
    var that = new Array$.$$;
    return that;
}
initTypeProto(Array$, 'ceylon.language.Array', Object$, FixedSized, Cloneable, Ranged, List);
exports.Array=Array$;

function Empty() {
    var that = new Empty.$$;
    that.value = [];
    return that;
}
initTypeProtoI(Empty, 'ceylon.language.Empty', None, Ranged, Cloneable, List);
var Empty$proto = Empty.$$.prototype;
Empty$proto.getEmpty = function() { return true; }
Empty$proto.defines = function(x) { return false; }
Empty$proto.getKeys = function() { return TypeCategory(this, 'ceylon.language.Integer'); }
Empty$proto.definesEvery = function(x) { return false; }
Empty$proto.definesAny = function(x) { return false; }
Empty$proto.items = function(x) { return this; }
Empty$proto.getSize = function() { return Integer(0); }
Empty$proto.item = function(x) { return null; }
Empty$proto.getFirst = function() { return null; }
Empty$proto.segment = function(a,b) { return this; }
Empty$proto.span = function(a,b) { return this; }
Empty$proto.getIterator = function() { return emptyIterator; }
Empty$proto.getString = function() { return String$("{}"); }
Empty$proto.contains = function(x) { return false; }
Empty$proto.getLastIndex = function() { return null; }
Empty$proto.getClone = function() { return this; }
Empty$proto.count = function(x) { return Integer(0); }
Empty$proto.getReversed = function() { return this; }
Empty$proto.skipping = function(skip) { return this; }
Empty$proto.taking = function(take) { return this; }
Empty$proto.by = function(step) { return this; }
Empty$proto.every = function(f) { return false; }
Empty$proto.any = function(f) { return false; }
Empty$proto.sorted = function(f) { return this; }
Empty$proto.map = function(f) { return this; }
Empty$proto.fold = function(i,r) { return i; }
Empty$proto.find = function(f) { return null; }
Empty$proto.findLast = function(f) { return null; }
Empty$proto.filter = function(f) { return this; }
Empty$proto.getCoalesced = function() { return this; }
Empty$proto.getIndexed = function() { return this; }
Empty$proto.withLeading = function(other) {
    return other === undefined ? this : other;
}
Empty$proto.withTrailing = function(other) {
    return other === undefined ? this : other;
}
Empty$proto.chain = function(other) { return other; }

var $empty = Empty();

function EmptyIterator() {
    var that = new EmptyIterator.$$;
    return that;
}
initTypeProto(EmptyIterator, 'ceylon.language.EmptyIterator', IdentifiableObject, Iterator);
var EmptyIterator$proto = EmptyIterator.$$.prototype;
EmptyIterator$proto.next = function() { return $finished; }
var emptyIterator=EmptyIterator();

exports.empty=$empty;
exports.Empty=Empty;
exports.emptyIterator=emptyIterator;

function EmptyArray() {
    var that = new EmptyArray.$$;
    return that;
}
initTypeProto(EmptyArray, 'ceylon.language.EmptyArray', Array$, None);
EmptyArray.$$.prototype.setItem = function(i,e) {}
EmptyArray.$$.prototype.item = function(x) { return null; }
EmptyArray.$$.prototype.getReversed = function() { return this; }
EmptyArray.$$.prototype.chain = function(other) { return other; }

exports.EmptyArray=EmptyArray;

function ArrayList(items) {
    var that = new ArrayList.$$;
    that.value=items;
    that.size=new Integer(items.length);
    that.lastIndex=new Integer(items.length-1);
    return that;
}
initTypeProto(ArrayList, 'ceylon.language.ArrayList', Array$, List);
var ArrayList$proto = ArrayList.$$.prototype;
ArrayList$proto.getSize = function() { return this.size; }
ArrayList$proto.setItem = function(idx,elem) {
    if (idx.value >= 0 && idx.value < this.size.value) {
        this.value[idx.value] = elem;
    }
}
ArrayList$proto.item = function(idx) {
    if (idx.value >= 0 && idx.value < this.size.value) {
        return this.value[idx.value];
    }
    return null;
}
ArrayList$proto.getLastIndex = function() {
    return this.lastIndex;
}
ArrayList$proto.getReversed = function() {
    var arr = this.value.slice(0);
    arr.reverse();
    return ArrayList(arr);
}

exports.ArrayList=ArrayList;
exports.arrayOfNone=function() { return EmptyArray(); }
exports.arrayOfSome=function(/*Sequence*/elems) { //In practice it's an ArraySequence
    return ArrayList(elems.value);
}
exports.array=function(elems) {
    if (elems === null || elems === undefined) {
        return EmptyArray();
    } else {
        var e=[];
        var iter=elems.getIterator();
        var item;while((item=iter.next())!==$finished) {
            e.push(item);
        }
        return e.length==0 ? EmptyArray() : ArrayList(e);
    }
}
exports.arrayOfSize=function(size, elem) {
    if (size.value > 0) {
        var elems = [];
        for (var i = 0; i < size.value; i++) {
            elems.push(elem);
        }
        return ArrayList(elems);
    } else return EmptyArray();
}

function Comprehension(makeNextFunc, compr) {
    if (compr===undefined) {compr = new Comprehension.$$;}
    IdentifiableObject(compr);
    compr.makeNextFunc = makeNextFunc;
    return compr;
}
initTypeProto(Comprehension, 'ceylon.language.Comprehension', IdentifiableObject, Iterable);
var Comprehension$proto = Comprehension.$$.prototype;
Comprehension$proto.getIterator = function() {
    return ComprehensionIterator(this.makeNextFunc());
}
exports.Comprehension=Comprehension;

function ComprehensionIterator(nextFunc, it) {
    if (it===undefined) {it = new ComprehensionIterator.$$;}
    IdentifiableObject(it);
    it.next = nextFunc;
    return it;
}
initTypeProto(ComprehensionIterator, 'ceylon.language.ComprehensionIterator',
        IdentifiableObject, Iterator);

function ChainedIterator(first, second, chained) {
    if (chained===undefined) {chained = new ChainedIterator.$$;}
    IdentifiableObject(chained);
    chained.it = first.getIterator();
    chained.second = second;
    return chained;
}
initTypeProto(ChainedIterator, 'ceylon.language.ChainedIterator',
        IdentifiableObject, Iterator);
var ChainedIterator$proto = ChainedIterator.$$.prototype;
ChainedIterator$proto.next = function() {
    var elem = this.it.next();
    if ((elem===$finished) && (this.second!==undefined)) {
        this.it = this.second.getIterator();
        this.second = undefined;
        elem = this.it.next();
    }
    return elem;
}
exports.ChainedIterator=ChainedIterator;
