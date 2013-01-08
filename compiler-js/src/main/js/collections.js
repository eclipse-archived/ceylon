function initType(a,b,c,d,e,f,g,h,i,j,k,l){}//IGNORE
function initTypeProtoI(a,b,c,d,e,f){}//IGNORE
function initTypeProto(a,b,c,d,e,f,g){}//IGNORE
function inheritProto(a,b,c){}//IGNORE
function exists(x){}//IGNORE
function Exception(){}//IGNORE
function isOfType(a,b){}//IGNORE
function getNothing(){}//IGNORE
function String$(x,l){}//IGNORE
function TypeCategory(a,b){}//IGNORE
function ArraySequence(x){}//IGNORE
var exports,Container,$finished,Cloneable,smaller,larger,Object$,Basic;//IGNORE
var Iterable,Iterator;//IGNORE

function List(wat) {
    return wat;
}
function $init$List() {
    if (List.$$===undefined) {
        initTypeProtoI(List, 'ceylon.language::List', $init$Collection(), $init$Correspondence(), $init$Ranged(), $init$Cloneable());
    }
    return List;
}
$init$List();
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
    if (isOfType(other, {t:List}) && other.getSize().equals(this.getSize())) {
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
initTypeProtoI(ListIterator, 'ceylon.language::ListIterator', $init$Iterator());
ListIterator.$$.prototype.next = function() {
    if (this.index <= this.lastIndex) {
        return this.list.item(this.index++);
    }
    return $finished;
}

function Sequential($$sequential) {
    return $$sequential;
}
function $init$Sequential() {
    if (Sequential.$$===undefined) {
        initTypeProtoI(Sequential, 'ceylon.language::Sequential', $init$List(), $init$Ranged(), $init$Cloneable());
    }
    return Sequential;
}
$init$Sequential();
exports.Sequential=Sequential;

function Empty() {
    var that = new Empty.$$;
    that.value = [];
    return that;
}
initTypeProtoI(Empty, 'ceylon.language::Empty', Sequential, $init$Ranged(), $init$Cloneable());
var Empty$proto = Empty.$$.prototype;
Empty$proto.getEmpty = function() { return true; }
Empty$proto.defines = function(x) { return false; }
Empty$proto.getKeys = function() { return TypeCategory(this, {t:Integer}); }
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
Empty$proto.getIterator = function() { return getEmptyIterator(); }
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

var empty = Empty();

exports.empty=empty;
exports.Empty=Empty;

function emptyIterator(){
    var $$emptyIterator=new emptyIterator.$$;
    Iterator($$emptyIterator);
    return $$emptyIterator;
}
function $init$emptyIterator(){
    if (emptyIterator.$$===undefined){
        initTypeProto(emptyIterator,'ceylon.language::emptyIterator',Basic,$init$Iterator());
    }
    return emptyIterator;
}
exports.$init$emptyIterator=$init$emptyIterator;
$init$emptyIterator();
(function($$emptyIterator){
    $$emptyIterator.next=function (){
    var $$emptyIterator=this;
    return $finished;
};
})(emptyIterator.$$.prototype);
var emptyIterator$2=emptyIterator(new emptyIterator.$$);
var getEmptyIterator=function(){
    return emptyIterator$2;
}

function Comprehension(makeNextFunc, compr) {
    if (compr===undefined) {compr = new Comprehension.$$;}
    Basic(compr);
    compr.makeNextFunc = makeNextFunc;
    return compr;
}
initTypeProto(Comprehension, 'ceylon.language::Comprehension', Basic, Iterable);
var Comprehension$proto = Comprehension.$$.prototype;
Comprehension$proto.getIterator = function() {
    return ComprehensionIterator(this.makeNextFunc());
}
exports.Comprehension=Comprehension;

function ComprehensionIterator(nextFunc, it) {
    if (it===undefined) {it = new ComprehensionIterator.$$;}
    Basic(it);
    it.next = nextFunc;
    return it;
}
initTypeProto(ComprehensionIterator, 'ceylon.language::ComprehensionIterator',
        Basic, $init$Iterator());
