function Sequential($$sequential) {
    return $$sequential;
}
function $init$Sequential() {
    if (Sequential.$$===undefined) {
        initTypeProto(Sequential, 'ceylon.language::Sequential', $init$List(), $init$Ranged(), $init$Cloneable());
    }
    return Sequential;
}
$init$Sequential();
Sequential.$$.prototype.getString=function() {
    return this.getEmpty()?String$("[]",2) :
        StringBuilder().appendAll([String$("[ ",2),commaList(this),String$(" ]",2)]).getString();
}
exports.Sequential=Sequential;

function Empty() {
    var that = new Empty.$$;
    that.value = [];
    that.$$targs$$={Element:{t:Nothing}};
    return that;
}
initTypeProto(Empty, 'ceylon.language::Empty', Sequential, $init$Ranged(), $init$Cloneable());
var Empty$proto = Empty.$$.prototype;
Empty$proto.getEmpty = function() { return true; }
Empty$proto.defines = function(x) { return false; }
Empty$proto.getKeys = function() { return TypeCategory(this, {t:Integer}); }
Empty$proto.definesEvery = function(x) { return false; }
Empty$proto.definesAny = function(x) { return false; }
Empty$proto.items = function(x) { return this; }
Empty$proto.getSize = function() { return 0; }
Empty$proto.get = function(x) { return null; }
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
    return new ArraySequence([other], [{t:other.getT$all()[other.getT$name()]}]);
}
Empty$proto.withTrailing = function(other) {
    return new ArraySequence([other], [{t:other.getT$all()[other.getT$name()]}]);
}
Empty$proto.chain = function(other) { return other; }

var empty = Empty();
empty.$$targs$$={Element:{t:Nothing},Absent:{t:Null}};
exports.empty=empty;
exports.Empty=Empty;

function emptyIterator(){
    var $$emptyIterator=new emptyIterator.$$;
    Iterator($$emptyIterator);
    $$emptyIterator.$$targs$$={Element:{t:Nothing}};
    return $$emptyIterator;
}
function $init$emptyIterator(){
    if (emptyIterator.$$===undefined){
        initTypeProto(emptyIterator,'ceylon.language::emptyIterator',$init$Basic(),$init$Iterator());
    }
    return emptyIterator;
}
exports.$init$emptyIterator=$init$emptyIterator;
$init$emptyIterator();
(function($$emptyIterator){
    $$emptyIterator.next=function (){
    var $$emptyIterator=this;
    return getFinished();
};
})(emptyIterator.$$.prototype);
var emptyIterator$2=emptyIterator();
var getEmptyIterator=function(){
    return emptyIterator$2;
}
exports.getEmptyIterator=getEmptyIterator;


function Sequence($$sequence) {
    List($$sequence);
    return $$sequence;
}
function $init$Sequence() {
    if (Sequence.$$===undefined) {
        initTypeProto(Sequence, 'ceylon.language::Sequence', $init$Sequential(),
            $init$Container(), $init$Cloneable());
    }
    return Sequence;
}
$init$Sequence();
var Sequence$proto = Sequence.$$.prototype;
Sequence$proto.getEmpty = function() { return false; }
Sequence$proto.sort = function(comp) {
    return internalSort(comp, this, this.$$targs$$);
}
Sequence$proto.collect = function(f, $$$mptypes) {
    return this.map(f, $$$mptypes).getSequence();
}
Sequence$proto.getClone = function() { return this; }
