function initTypeProto(a,b,c,d,e,f,g,h,i,j,k,l);//IGNORE
function initTypeProtoI(a,b,c,d,e,f,g,h,i,j,k,l);//IGNORE
function String$(x){}//IGNORE
function Character(x){}//IGNORE
function isOfType(a,b){}//IGNORE
function smallest(x,y){}//IGNORE
function largest(x,y){}//IGNORE
function Exception(){}//IGNORE
var List,Cloneable,Ranged,exports,larger,smaller,equal,Object$,empty,$finished,Iterator;//IGNORE
var Basic,Category;//IGNORE

function Sequence($$sequence) {
    return $$sequence;
}
initTypeProtoI(Sequence, 'ceylon.language::Sequence', Sequential, Cloneable);
function $init$Sequence() {
    if (Sequence.$$===undefined) {
        initTypeProtoI(Sequence, 'ceylon.language::Sequence', $init$Sequential(), $init$Cloneable());
    }
    return Sequence;
}
var Sequence$proto = Sequence.$$.prototype;
Sequence$proto.getLast = function() {
    var last = this.item(this.getLastIndex());
    if (last === null) throw Exception();
    return last;
}

function Array$() {
    var that = new Array$.$$;
    return that;
}
initExistingType(Array$, Array, 'ceylon.language::Array', Object$,
        Cloneable, Ranged, $init$List());
var Array$proto = Array.prototype;
var origArrToString = Array$proto.toString;
inheritProtoI(Array$, Object$, Cloneable, Ranged, $init$List());
Array$proto.toString = origArrToString;
exports.Array=Array$;

function EmptyArray() {
    return [];
}
initTypeProto(EmptyArray, 'ceylon.language::EmptyArray', Array$);
function ArrayList(items) {
    return items;
}
initTypeProto(ArrayList, 'ceylon.language::ArrayList', Array$, $init$List());
function ArraySequence(/* js array */value) {
    value.$seq = true;
    return value;
}
initTypeProto(ArraySequence, 'ceylon.language::ArraySequence', Basic, Sequence);

Array$proto.getT$name = function() {
    return (this.$seq ? ArraySequence : (this.length>0?ArrayList:EmptyArray)).$$.T$name;
}
Array$proto.getT$all = function() {
    return (this.$seq ? ArraySequence : (this.length>0?ArrayList:EmptyArray)).$$.T$all;
}

exports.EmptyArray=EmptyArray;

Array$proto.getSize = function() { return this.length; }
Array$proto.setItem = function(idx,elem) {
    if (idx >= 0 && idx < this.length) {
        this[idx] = elem;
    }
}
Array$proto.item = function(idx) {
    var result = this[idx];
    return result!==undefined ? result:null;
}
Array$proto.getLastIndex = function() {
    return this.length>0 ? (this.length-1) : null;
}
Array$proto.getReversed = function() {
    if (this.length === 0) { return this; }
    var arr = this.slice(0);
    arr.reverse();
    return this.$seq ? ArraySequence(arr) : arr;
}
Array$proto.chain = function(other) {
    if (this.length === 0) { return other; }
    return Iterable.$$.prototype.chain.call(this, other);
}
Array$proto.getFirst = function() { return this.length>0 ? this[0] : null; }
Array$proto.getLast = function() { return this.length>0 ? this[this.length-1] : null; }
Array$proto.segment = function(from, len) {
    if (len <= 0) { return empty; }
    var stop = from + len;
    var seq = this.slice((from>=0)?from:0, (stop>=0)?stop:0);
    return (seq.length > 0) ? ArraySequence(seq) : empty;
}
Array$proto.span = function(from, to) {
    if (from > to) {
        var arr = this.segment(to, from-to+1);
        arr.reverse();
        return arr;
    }
    return this.segment(from, to-from+1);
}
Array$proto.spanTo = function(to) {
    return to < 0 ? empty : this.span(0, to);
}
Array$proto.spanFrom = function(from) {
    return this.span(from, 0x7fffffff);
}
Array$proto.getRest = function() {
    return this.length<=1 ? empty : ArraySequence(this.slice(1));
}
Array$proto.items = function(keys) {
    if (keys === undefined) return empty;
    var seq = [];
    for (var i = 0; i < keys.getSize(); i++) {
        var key = keys.item(i);
        seq.push(this.item(key));
    }
    return ArraySequence(seq);
}
Array$proto.getKeys = function() { return TypeCategory(this, {t:Integer}); }
Array$proto.contains = function(elem) {
    for (var i=0; i<this.length; i++) {
        if (elem.equals(this[i])) {
            return true;
        }
    }
    return false;
}

exports.ArrayList=ArrayList;
exports.array=function(elems, $$$ptypes) {
    var e=[];
    if (!(elems === null || elems === undefined)) {
        var iter=elems.getIterator();
        var item;while((item=iter.next())!==$finished) {
            e.push(item);
        }
    }
    e.$$targs$$=$$$ptypes;
    return e;
}
exports.arrayOfSize=function(size, elem) {
    if (size > 0) {
        var elems = [];
        for (var i = 0; i < size; i++) {
            elems.push(elem);
        }
        return elems;
    } else return [];
}

function TypeCategory(seq, type) {
    var that = new TypeCategory.$$;
    that.type = type;
    that.seq = seq;
    return that;
}
initTypeProto(TypeCategory, 'ceylon.language::TypeCategory', Basic, Category);
var TypeCategory$proto = TypeCategory.$$.prototype;
TypeCategory$proto.contains = function(k) {
    return isOfType(k, this.type) && this.seq.defines(k);
}

function SequenceBuilder() {
    var that = new SequenceBuilder.$$;
    that.seq = [];
    return that;
}
initTypeProto(SequenceBuilder, 'ceylon.language::SequenceBuilder', Basic);
var SequenceBuilder$proto = SequenceBuilder.$$.prototype;
SequenceBuilder$proto.getSequence = function() {
    return (this.seq.length > 0) ? ArraySequence(this.seq) : empty;
}
SequenceBuilder$proto.append = function(e) { this.seq.push(e); }
SequenceBuilder$proto.appendAll = function(/*Iterable*/arr) {
    if (arr === undefined) return;
    var iter = arr.getIterator();
    var e; while ((e = iter.next()) !== $finished) {
        this.seq.push(e);
    }
}
SequenceBuilder$proto.getSize = function() { return this.seq.length; }

function SequenceAppender(other) {
    var that = new SequenceAppender.$$;
    that.seq = [];
    that.appendAll(other);
    return that;
}
initTypeProto(SequenceAppender, 'ceylon.language::SequenceAppender', SequenceBuilder);

function Singleton(elem) {
    var that = new Singleton.$$;
    that.value = [elem];
    that.elem = elem;
    return that;
}
initTypeProto(Singleton, 'ceylon.language::Singleton', Object$, Sequence);
var Singleton$proto = Singleton.$$.prototype;
Singleton$proto.getString = function() { return String$("{ " + this.elem.getString() + " }") }
Singleton$proto.item = function(index) {
    return index===0 ? this.value[0] : null;
}
Singleton$proto.getSize = function() { return 1; }
Singleton$proto.getLastIndex = function() { return 0; }
Singleton$proto.getFirst = function() { return this.elem; }
Singleton$proto.getLast = function() { return this.elem; }
Singleton$proto.getEmpty = function() { return false; }
Singleton$proto.getRest = function() { return empty; }
Singleton$proto.defines = function(idx) { return idx.equals(0); }
Singleton$proto.getKeys = function() { return TypeCategory(this, {t:Integer}); }
Singleton$proto.span = function(from, to) {
    return (((from <= 0) && (to >= 0)) || ((from >= 0) && (to <= 0))) ? this : empty;
}
Singleton$proto.spanTo = function(to) {
    return to < 0 ? empty : this;
}
Singleton$proto.spanFrom = function(from) {
    return from > 0 ? empty : this;
}
Singleton$proto.segment = function(idx, len) {
    return ((idx <= 0) && ((idx+len) > 0)) ? this : empty; 
}
Singleton$proto.getIterator = function() { return SingletonIterator(this.elem); }
Singleton$proto.getReversed = function() { return this; }
Singleton$proto.equals = function(other) {
    if (isOfType(other, {t:List})) {
        if (other.getSize() !== 1) {
            return false;
        }
        var o = other.item(0);
        return o !== null && o.equals(this.elem);
    }
    return false;
}
Singleton$proto.$map = function(f) { return ArraySequence([ f(this.elem) ]); }
Singleton$proto.$filter = function(f) {
    return f(this.elem) ? this : empty;
}
Singleton$proto.fold = function(v,f) {
    return f(v, this.elem);
}
Singleton$proto.find = function(f) {
    return f(this.elem) ? this.elem : null;
}
Singleton$proto.findLast = function(f) {
    return f(this.elem) ? this.elem : null;
}
Singleton$proto.any = function(f) {
    return f(this.elem);
}
Singleton$proto.$every = function(f) {
    return f(this.elem);
}
Singleton$proto.skipping = function(skip) {
    return skip==0 ? this : empty;
}
Singleton$proto.taking = function(take) {
    return take>0 ? this : empty;
}
Singleton$proto.by = function(step) {
    return this;
}
Singleton$proto.$sort = function(f) { return this; }
Singleton$proto.count = function(f) {
	return f(this.elem) ? 1 : 0;
}
Singleton$proto.contains = function(o) {
	return this.elem.equals(o);
}
Singleton$proto.getCoalesced = function() { return this; }

function SingletonIterator(elem) {
    var that = new SingletonIterator.$$;
    that.elem = elem;
    that.done = false;
    return that;
}
initTypeProto(SingletonIterator, 'ceylon.language::SingletonIterator', Basic, Iterator);
var $SingletonIterator$proto = SingletonIterator.$$.prototype;
$SingletonIterator$proto.next = function() {
    if (this.done) {
        return $finished;
    }
    this.done = true;
    return this.elem;
}

exports.Sequence=Sequence;
exports.SequenceBuilder=SequenceBuilder;
exports.SequenceAppender=SequenceAppender;
exports.ArraySequence=ArraySequence;
exports.Singleton=Singleton;
