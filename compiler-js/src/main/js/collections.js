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

function Sized(wat) {
    return wat;
}
initTypeProtoI(Sized, 'ceylon.language::Sized', Container);
Sized.$$.prototype.getEmpty = function() {
    return this.getSize() == 0;
}
exports.Sized=Sized;

function Category(wat) {
    return wat;
}
initType(Category, 'ceylon.language::Category');
Category.$$.prototype.containsEvery = function(keys) {
    if (keys === undefined) return true;
    for (var i = 0; i < keys.length; i++) {
        if (!this.contains(keys[i])) {
            return false;
        }
    }
    return true;
}
Category.$$.prototype.containsAny = function(keys) {
    if (keys === undefined) return true;
    for (var i = 0; i < keys.length; i++) {
        if (this.contains(keys[i])) {
            return true;
        }
    }
    return false;
}
exports.Category=Category;

function Collection(wat) {
    return wat;
}
initTypeProtoI(Collection, 'ceylon.language::Collection', Iterable, Sized, Category, Cloneable);
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
Collection$proto.getString = function() {
    var s = '{';
    var first = true;
    var iter = this.getIterator();
    var item;
    while ((item = iter.next()) !== $finished) {
        s += first ? ' ' : ', ';
        if (exists(item)) {
            s += item.getString();
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
exports.Collection=Collection;

function FixedSized(wat) {
    return wat;
}
initTypeProtoI(FixedSized, 'ceylon.language::FixedSized', Collection);
var FixedSized$proto = FixedSized.$$.prototype;
FixedSized$proto.getFirst = function() {
    var e = this.getIterator().next();
    return e === $finished ? null : e;
}
exports.FixedSized=FixedSized;

function Some(wat) {
    return wat;
}
initTypeProtoI(Some, 'ceylon.language::Some', FixedSized, ContainerWithFirstElement);
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
initTypeProtoI(None, 'ceylon.language::None', FixedSized, ContainerWithFirstElement);
var None$proto = None.$$.prototype;
None$proto.getFirst = function() { return null; }
None$proto.getIterator = function() { return emptyIterator; }
None$proto.getSize = function() { return 0; }
None$proto.getEmpty = function() { return true; }
None$proto.getFirst = function() { return null; }
exports.None=None;

function Ranged(wat) {
    return wat;
}
initType(Ranged, 'ceylon.language::Ranged');
exports.Ranged=Ranged;

function List(wat) {
    return wat;
}
initTypeProtoI(List, 'ceylon.language::List', Collection, Correspondence, Ranged, Cloneable);
var List$proto = List.$$.prototype;
List$proto.getSize = function() {
    var li = this.getLastIndex();
    return li === null ? 0 : li.getSuccessor();
}
List$proto.defines = function(idx) {
    var li = this.getLastIndex();
    if (li === null) li = -1;
    return li.compare(idx) !== smaller;
}
List$proto.getIterator = function() {
    return ListIterator(this);
}
List$proto.equals = function(other) {
    if (isOfType(other, 'ceylon.language::List') && other.getSize().equals(this.getSize())) {
        for (var i = 0; i < this.getSize(); i++) {
            var mine = this.item(i);
            var theirs = other.item(i);
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
            hc += e.getHash();
        }
    }
    return hc;
}
List$proto.findLast = function(select) {
    var li = this.getLastIndex();
    if (li !== null) {
        while (li>=0) {
            var e = this.item(li);
            if (e !== null && select(e)) {
                return e;
            }
            li = li.getPredecessor();
        }
    }
    return null;
}
List$proto.withLeading = function(other) {
    var sb = SequenceBuilder();
    sb.append(other);
    sb.appendAll(this);
    return sb.getSequence();
}
List$proto.withTrailing = function(other) {
    var sb = SequenceBuilder();
    sb.appendAll(this);
    sb.append(other);
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
        that.lastIndex = that.lastIndex;
    }
    return that;
}
initTypeProtoI(ListIterator, 'ceylon.language::ListIterator', Iterator);
ListIterator.$$.prototype.next = function() {
    if (this.index <= this.lastIndex) {
        return this.list.item(this.index++);
    }
    return $finished;
}

function Sequential($$sequential) {
    return $$sequential;
}
initTypeProtoI(Sequential, 'ceylon.language::Sequential', List, FixedSized, Ranged, Cloneable);
exports.Sequential=Sequential;

function Empty() {
    var that = new Empty.$$;
    that.value = [];
    return that;
}
initTypeProtoI(Empty, 'ceylon.language::Empty', Sequential, None, Ranged, Cloneable);
var Empty$proto = Empty.$$.prototype;
Empty$proto.getEmpty = function() { return true; }
Empty$proto.defines = function(x) { return false; }
Empty$proto.getKeys = function() { return TypeCategory(this, 'ceylon.language::Integer'); }
Empty$proto.definesEvery = function(x) { return false; }
Empty$proto.definesAny = function(x) { return false; }
Empty$proto.items = function(x) { return this; }
Empty$proto.getSize = function() { return 0; }
Empty$proto.item = function(x) { return null; }
Empty$proto.getFirst = function() { return null; }
Empty$proto.segment = function(a,b) { return this; }
Empty$proto.span = function(a,b) { return this; }
Empty$proto.spanTo = function(a) { return this; }
Empty$proto.spanFrom = function(a) { return this; }
Empty$proto.getIterator = function() { return emptyIterator; }
Empty$proto.getString = function() { return String$("{}"); }
Empty$proto.contains = function(x) { return false; }
Empty$proto.getLastIndex = function() { return null; }
Empty$proto.getClone = function() { return this; }
Empty$proto.count = function(x) { return 0; }
Empty$proto.getReversed = function() { return this; }
Empty$proto.skipping = function(skip) { return this; }
Empty$proto.taking = function(take) { return this; }
Empty$proto.by = function(step) { return this; }
Empty$proto.$every = function(f) { return false; }
Empty$proto.any = function(f) { return false; }
Empty$proto.$sort = function(f) { return this; }
Empty$proto.$map = function(f) { return this; }
Empty$proto.fold = function(i,r) { return i; }
Empty$proto.find = function(f) { return null; }
Empty$proto.findLast = function(f) { return null; }
Empty$proto.$filter = function(f) { return this; }
Empty$proto.getCoalesced = function() { return this; }
Empty$proto.getIndexed = function() { return this; }
Empty$proto.withLeading = function(other) {
    return new ArraySequence([other]);
}
Empty$proto.withTrailing = function(other) {
    return new ArraySequence([other]);
}
Empty$proto.chain = function(other) { return other; }

var $empty = Empty();

function EmptyIterator() {
    var that = new EmptyIterator.$$;
    return that;
}
initTypeProto(EmptyIterator, 'ceylon.language::EmptyIterator', IdentifiableObject, Iterator);
var EmptyIterator$proto = EmptyIterator.$$.prototype;
EmptyIterator$proto.next = function() { return $finished; }
var emptyIterator=EmptyIterator();

exports.empty=$empty;
exports.Empty=Empty;
exports.emptyIterator=emptyIterator;

function Comprehension(makeNextFunc, compr) {
    if (compr===undefined) {compr = new Comprehension.$$;}
    IdentifiableObject(compr);
    compr.makeNextFunc = makeNextFunc;
    return compr;
}
initTypeProto(Comprehension, 'ceylon.language::Comprehension', IdentifiableObject, Iterable);
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
initTypeProto(ComprehensionIterator, 'ceylon.language::ComprehensionIterator',
        IdentifiableObject, Iterator);

function ChainedIterator(first, second, chained) {
    if (chained===undefined) {chained = new ChainedIterator.$$;}
    IdentifiableObject(chained);
    chained.it = first.getIterator();
    chained.second = second;
    return chained;
}
initTypeProto(ChainedIterator, 'ceylon.language::ChainedIterator',
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

function LazyList(elems, lst) {
    if (lst===undefined) {lst = new LazyList.$$;}
    IdentifiableObject(lst);
    lst.elems = elems===undefined?$empty:elems;
    return lst;
}
initTypeProto(LazyList, 'ceylon.language::LazyList', IdentifiableObject, List);
var LazyList$proto = LazyList.$$.prototype;
LazyList$proto.getIterator = function() { return this.elems.getIterator(); }
LazyList$proto.getClone = function() { return this; }
LazyList$proto.getFirst = function() { return this.elems.getFirst(); }
LazyList$proto.getLast = function() { return this.elems.getLast(); }
LazyList$proto.findLast = function(selecting) {
    return this.elems.findLast(selecting);
}
LazyList$proto.getSize = function() {
    if (isOfType(this.elems, 'ceylon.language::Sized')) {
        return this.elems.getSize();
    }
    var it = this.elems.getIterator();
    var count = 0;
    while (it.next() !== $finished) {++count;}
    return count;
}
LazyList$proto.getLastIndex = function() {
    var size = this.getSize();
    return size>0 ? (size-1) : null;
}
LazyList$proto.getEmpty = function() { return this.elems.getEmpty(); }
LazyList$proto.item = function(index) {
    var it = this.elems.getIterator();
    var elem;
    var count = 0;
    while ((elem=it.next()) !== $finished) {
        if (count++ == index) { return elem; }
    }
    return null;
}
LazyList$proto.getReversed = function() { return this.elems.getSequence().getReversed(); }
LazyList$proto.equals = function(other) {
    if (!isOfType(other, 'ceylon.language::List')) { return false; }
    var it1 = this.elems.getIterator();
    var it2 = other.getIterator();
    var e1;
    while ((e1=it1.next()) !== $finished) {
        var e2 = it2.next();
        var n1 = e1===null;
        if ((n1 ^ (e2===null)) || !(n1 || e1.equals(e2))) { return false; }
    }
    return it2.next()===$finished;
}
LazyList$proto.segment = function(from, length) {
    if (length <= 0) { return $empty; }
    var seg = (from > 0) ? this.elems.skipping(from) : this.elems;
    return LazyList(seg.taking(length));
}
LazyList$proto.span = function(from, to) {
    if (from < 0 && to < 0) {
        return $empty;
    } else if (to < 0) {
        to = 0;
    } else if (from < 0) {
        from = 0;
    }
    if (to < from) {
        var seq = (to > 0) ? this.elems.skipping(to) : this.elems;
        return seq.taking(from-to+1).getSequence().getReversed();
    }
    var seq = (from > 0) ? this.elems.skipping(from) : this.elems;
    return LazyList(seq.taking(to-from+1));
}
LazyList$proto.spanFrom = function(from) {
    return (from > 0) ? LazyList(this.elems.skipping(from)) : this;
}
LazyList$proto.spanTo = function(to) {
    return (to < 0) ? $empty : LazyList(this.elems.taking(to+1));
}
exports.LazyList=LazyList;