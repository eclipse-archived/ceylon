function initType(a,b,c,d,e,f,g,h,i,j,k,l){}//IGNORE
function inheritProto(a,b,c){}//IGNORE
function exists(x){}//IGNORE
function Boolean$(x){}//IGNORE
function Exception(){}//IGNORE
function Integer(x){}//IGNORE
function isOfType(a,b){}//IGNORE
function String$(x){}//IGNORE
function TypeCategory(a,b){}//IGNORE
var exports,Container,$finished,$false,$true,Cloneable,smaller,larger,Correspondence,Object$,IdentifiableObject;//IGNORE

function Sized(wat) {
    return wat;
}
initTypeProtoI(Sized, 'ceylon.language.Sized', Container);
Sized.$$.prototype.getEmpty = function() {
    return Boolean$(this.getSize().value === 0);
}
exports.Sized=Sized;

function Iterable(wat) {
    return wat;
}
initTypeProtoI(Iterable, 'ceylon.language.Iterable', Container);
Iterable.$$.prototype.getEmpty = function() {
    return Boolean$(this.getIterator().next() === $finished);
}
exports.Iterable=Iterable;

function Category(wat) {
    return wat;
}
initType(Category, 'ceylon.language.Category');
Category.$$.prototype.containsEvery = function(keys) {
    for (var i = 0; i < keys.value.length; i++) {
        if (this.contains(keys.value[i]) === $false) {
            return $false;
        }
    }
    return $true;
}
Category.$$.prototype.containsAny = function(keys) {
    for (var i = 0; i < keys.value.length; i++) {
        if (this.contains(keys.value[i]) === $true) {
            return $true;
        }
    }
    return $false;
}
exports.Category=Category;

function Iterator(wat) {
    return wat;
}
initType(Iterator, 'ceylon.language.Iterator');
exports.Iterator=Iterator;

function Collection(wat) {
    return wat;
}
initTypeProtoI(Collection, 'ceylon.language.Collection', Iterable, Sized, Category, Cloneable);
var Collection$proto = Collection.$$.prototype;
Collection$proto.contains = function(obj) {
    var iter = this.getIterator();
    var item;
    while ((item = iter.next()) !== $finished) {
        if (exists(item) === $true && item.equals(obj) === $true) {
            return $true;
        }
    }
    return $false;
}
Collection$proto.count = function(obj) {
    var iter = this.getIterator();
    var item;
    var count = 0;
    while ((item = iter.next()) !== $finished) {
        if (exists(item) === $true && item.equals(obj) === $true) {
            count++;
        }
    }
    return Integer(count);
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
initTypeProtoI(Some, 'ceylon.language.Some', FixedSized);
var $Some = Some.$$;
$Some.prototype.getFirst = function() {
    var e = this.getIterator().next();
    if (e === $finished) throw Exception();
    return e;
}
$Some.prototype.getEmpty = function() { return $false; }
exports.Some=Some;

function None(wat) {
    return wat;
}
initTypeProtoI(None, 'ceylon.language.None', FixedSized);
var None$proto = None.$$.prototype;
None$proto.getFirst = function() { return null; }
None$proto.getIterator = function() { return emptyIterator; }
None$proto.getSize = function() { return Integer(0); }
None$proto.getEmpty = function() { return $true; }
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
    return Boolean$(li.compare(idx) !== smaller);
}
List$proto.getIterator = function() {
    return ListIterator(this);
}
List$proto.equals = function(other) {
    if (isOfType(other, 'ceylon.language.List') === $true && other.getSize().equals(this.getSize()) === $true) {
        for (var i = 0; i < this.getSize().value; i++) {
            var mine = this.item(Integer(i));
            var theirs = other.item(Integer(i));
            if (((mine === null) && theirs) || !(mine && mine.equals(theirs) === $true)) {
                return $false;
            }
        }
        return $true;
    }
    return $false;
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
        if (exists(item) === $true) {
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
Map$proto.count = function(elem) {
    if (isOfType(elem,'ceylon.language.Entry') === $true) {
        var item = this.item(elem.getKey());
        if (item !== null && item.equals(elem.getItem()) === $true) {
            return Integer(1);
        }
    }
    return Integer(0);
}
Map$proto.equals = function(other) {
    if (isOfType(other, 'ceylon.language.Map') === $true && other.getSize().equals(this.getSize())) {
        var iter = this.getIterator();
        var entry; while ((entry = iter.next()) !== $finished) {
            var oi = other.item(entry.getKey());
            if (oi === null || entry.getItem().equals(oi.getItem()) === $false) {
                return $false;
            }
        }
        return $true;
    }
    return $false;
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
//TODO implement methods: getKeys, getValues, getInverse
exports.Map=Map;

function Set(wat) {
    return wat;
}
initTypeProtoI(Set, 'ceylon.language.Set', Collection, Cloneable);
var Set$proto = Set.$$.prototype;
Set$proto.count = function(elem) {
    return this.contains(elem) === $true ? Integer(1) : Integer(0);
}
Set$proto.superset = function(set) {
    var iter = set.getIterator();
    var elem; while ((elem = iter.next()) !== $finished) {
        if (this.contains(elem) === $false) {
            return $false;
        }
    }
    return $true;
}
Set$proto.subset = function(set) {
    var iter = this.getIterator();
    var elem; while ((elem = iter.next()) !== $finished) {
        if (set.contains(elem) === $false) {
            return $false;
        }
    }
    return $true;
}
Set$proto.equals = function(other) {
    if (isOfType(other, 'ceylon.language.Set') === $true) {
        if (other.getSize().equals(this.getSize())) {
            var iter = this.getIterator();
            var elem; while ((elem = iter.next()) !== $finished) {
                if (other.contains(elem) === $false) {
                    return $false;
                }
            }
            return $true;
        }
    }
    return $false;
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
initTypeProto(Array$, 'ceylon.language.Array', Object$, List, FixedSized, Cloneable, Ranged);
exports.Array=Array$;

function Empty() {
    var that = new Empty.$$;
    that.value = [];
    return that;
}
initTypeProtoI(Empty, 'ceylon.language.Empty', List, None, Ranged, Cloneable);
var Empty$proto = Empty.$$.prototype;
Empty$proto.getEmpty = function() { return $true; }
Empty$proto.defines = function(x) { return $false; }
Empty$proto.getKeys = function() { return TypeCategory(this, 'ceylon.language.Integer'); }
Empty$proto.definesEvery = function(x) { return $false; }
Empty$proto.definesAny = function(x) { return $false; }
Empty$proto.items = function(x) { return this; }
Empty$proto.getSize = function() { return Integer(0); }
Empty$proto.item = function(x) { return null; }
Empty$proto.getFirst = function() { return null; }
Empty$proto.segment = function(a,b) { return this; }
Empty$proto.span = function(a,b) { return this; }
Empty$proto.getIterator = function() { return emptyIterator; }
Empty$proto.getString = function() { return String$("{}"); }
Empty$proto.contains = function(x) { return $false; }
Empty$proto.getLastIndex = function() { return null; }
Empty$proto.getClone = function() { return this; }
Empty$proto.count = function(x) { return Integer(0); }

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
exports.emptyIterator=emptyIterator;

function EmptyArray() {
    var that = new EmptyArray.$$;
    return that;
}
initTypeProto(EmptyArray, 'ceylon.language.EmptyArray', Array$, None);
EmptyArray.$$.prototype.setItem = function(i,e) {}
EmptyArray.$$.prototype.item = function(x) { return null; }
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

exports.ArrayList=ArrayList;
exports.arrayOfNone=function() { return EmptyArray(); }
exports.arrayOfSome=function(elems) { //receives an ArraySequence
    return ArrayList(elems.value);
}
exports.array=function(elems) {
    if (elems === null || elems === undefined || elems.getSize().value === 0) {
        return EmptyArray();
    } else {
        return ArrayList(elems.value);
    }
}
