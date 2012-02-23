(function(define) {
    define(function(require, exports, module) {

//the Ceylon language module

function initType(type, typeName) {
    var cons = function() {}
    type.$$ = cons;
    cons.T$name = typeName;
    cons.T$all = {}
    cons.T$all[typeName] = type;
    for (var i=2; i<arguments.length; ++i) {
        var superTypes = arguments[i].$$.T$all;
        for ($ in superTypes) {cons.T$all[$] = superTypes[$]}
    }
}
function initExistingType(type, cons, typeName) {
    type.$$ = cons;
    cons.T$name = typeName;
    cons.T$all = {}
    cons.T$all[typeName] = type;
    for (var i=3; i<arguments.length; ++i) {
        var superTypes = arguments[i].$$.T$all;
        for ($ in superTypes) {cons.T$all[$] = superTypes[$]}
    }
}
function inheritProto(type, superType, suffix) {
    var proto = type.$$.prototype;
    var superProto = superType.$$.prototype;
    for(var $ in superProto){
        var $m = superProto[$];
        proto[$] = $m;
        if($.charAt($.length-1)!=='$') {proto[$+suffix] = $m}
    }
    for (var i=3; i<arguments.length; ++i) {
        superProto = arguments[i].$$.prototype;
        for (var $ in superProto) {proto[$] = superProto[$]}
    }
}
function inheritProtoI(type) {
    var proto = type.$$.prototype;
    for (var i=1; i<arguments.length; ++i) {
        var superProto = arguments[i].$$.prototype;
        for (var $ in superProto) {proto[$] = superProto[$]}
    }
}
exports.initType=initType;
exports.inheritProto=inheritProto;
exports.inheritProtoI=inheritProtoI;

function Void(wat) {
    return wat;
}
initType(Void, 'ceylon.language.Void');
function Object$(wat) {
    return wat;
}
initType(Object$, 'ceylon.language.Object', Void);
var Object$proto = Object$.$$.prototype;
Object$proto.getString=function() { String$(Object.prototype.toString.apply(this)) };
Object$proto.toString=function() { return this.getString().value };
Object$proto.equals = function(other) { return Boolean$(this===other); } //TODO: is this correct?
function IdentifiableObject(obj) {
    return obj;
}
initType(IdentifiableObject, 'ceylon.language.IdentifiableObject', Object$);
inheritProto(IdentifiableObject, Object$, '$Object$');

//INTERFACES
function Cloneable(wat) {
    return wat;
}
initType(Cloneable, 'ceylon.language.Cloneable');
exports.Cloneable=Cloneable;

#include callable.js

function Castable(wat) {
    return wat;
}
initType(Castable, 'ceylon.language.Castable');
exports.Castable=Castable;
function Closeable(wat) {
    return wat;
}
initType(Closeable, 'ceylon.language.Closeable');
exports.Closeable=Closeable;
function Comparable(wat) {
    return wat;
}
initType(Comparable, 'ceylon.language.Comparable');
exports.Comparable=Comparable;
function Container(wat) {
    return wat;
}
initType(Container, 'ceylon.language.Container');
exports.Container=Container;
function Correspondence(wat) {
    return wat;
}
initType(Correspondence, 'ceylon.language.Correspondence');
exports.Correspondence=Correspondence;
function Sized(wat) {
    return wat;
}
initType(Sized, 'ceylon.language.Sized', Container);
exports.Sized=Sized;
function Iterable(wat) {
    return wat;
}
initType(Iterable, 'ceylon.language.Iterable', Container);
exports.Iterable=Iterable;
function Category(wat) {
    return wat;
}
initType(Category, 'ceylon.language.Category');
exports.Category=Category;
function Iterator(wat) {
    return wat;
}
initType(Iterator, 'ceylon.language.Iterator');
exports.Iterator=Iterator;
function Collection(wat) {
    return wat;
}
initType(Collection, 'ceylon.language.Collection', Iterable, Sized, Category, Cloneable);
exports.Collection=Collection;
function FixedSized(wat) {
    return wat;
}
initType(FixedSized, 'ceylon.language.FixedSized', Collection);
exports.FixedSized=FixedSized;
function Some(wat) {
    return wat;
}
initType(Some, 'ceylon.language.Some', FixedSized);
exports.Some=Some;
function Summable(wat) {
    return wat;
}
initType(Summable, 'ceylon.language.Summable');
exports.Summable=Summable;
function Number$(wat) {
    return wat;
}
initType(Number$, 'ceylon.language.Number');
exports.Number=Number$;
function Invertable(wat) {
    return wat;
}
initType(Invertable, 'ceylon.language.Invertable');
exports.Invertable=Invertable;
function Numeric(wat) {
    return wat;
}
initType(Numeric, 'ceylon.language.Numeric', Number$, Comparable, Summable, Invertable);
exports.Numeric=Numeric;
function Ordinal(wat) {
    return wat;
}
initType(Ordinal, 'ceylon.language.Ordinal');
exports.Ordinal=Ordinal;
function Integral(wat) {
    return wat;
}
initType(Integral, 'ceylon.language.Integral', Numeric, Ordinal);
exports.Integral=Integral;
function Ranged(wat) {
    return wat;
}
initType(Ranged, 'ceylon.language.Ranged');
exports.Ranged=Ranged;
function List(wat) {
    return wat;
}
initType(List, 'ceylon.language.List', Collection, Correspondence, Ranged, Cloneable);
exports.List=List;
function Map(wat) {
    return wat;
}
initType(Map, 'ceylon.language.Map', Collection, Correspondence, Cloneable);
exports.Map=Map;
function None(wat) {
    return wat;
}
initType(None, 'ceylon.language.None', FixedSized);
exports.None=None;
function Set(wat) {
    return wat;
}
initType(Set, 'ceylon.language.Set', Collection, Cloneable);
exports.Set=Set;

//Interface methods
var FixedSized$proto = FixedSized.$$.prototype;
FixedSized$proto.getFirst = function() {
    var e = this.getIterator().next();
    return e === $finished ? null : e;
}

var None$proto = None.$$.prototype;
None$proto.getFirst = function() { return null; }
None$proto.getIterator = function() { return emptyIterator; }
None$proto.getSize = function() { return Integer(0); }
None$proto.getEmpty = function() { return $true; }

var $Some = Some.$$;
$Some.prototype.getFirst = function() {
    var e = this.getIterator().next();
    if (e === $finished) throw Exception();
    return e;
}
$Some.prototype.getEmpty = function() { return $false; }

function Exception(description, cause, wat) {
    if (wat===undefined) {wat=new Exception.$$;}
    wat.description = description;
    wat.cause = cause;
    return wat;
}
initType(Exception, 'ceylon.language.Exception', IdentifiableObject);
inheritProto(Exception, IdentifiableObject, '$IdentifiableObject$');
var Exception$proto = Exception.$$.prototype;
Exception$proto.getCause = function() {return this.cause;}
Exception$proto.getMessage = function() {
    return this.description!==null ? this.description
           : (this.cause!==null ? this.cause.getMessage() : String$("", 0));
}
Exception$proto.getString = function() {
    return String$('Exception "' + this.getMessage().value + '"');
}

#include numbers.js
#include strings.js

function getNull() { return null }
//function Boolean$(value) {
//    return value ? $true : $false;
//}
//initType(Boolean$, 'ceylon.language.Boolean', IdentifiableObject);
//inheritProto(Boolean$, IdentifiableObject, '$IdentifiableObject$');
//var $true = new Boolean$.$$;
//$true.string = String$("true");
//$true.getString = function() {return this.string}
//function getTrue() { return $true; }
//var $false = new Boolean$.$$;
//$false.string = String$("false");
//$false.getString = function() {return this.string}
//function getFalse() { return $false; }
function Boolean$(value) {return Boolean(value)}
initExistingType(Boolean$, Boolean, 'ceylon.language.Boolean', IdentifiableObject);
inheritProto(Boolean$, IdentifiableObject, '$IdentifiableObject$');
Boolean.prototype.equals = function(other) {return other.constructor===Boolean && other==this;}
var trueString = String$("true", 4);
var falseString = String$("false", 5);
Boolean.prototype.getString = function() {return this.valueOf()?trueString:falseString;}
function getTrue() {return true}
function getFalse() {return false}
var $true = true;
var $false = false;
function Finished() {}
initType(Finished, 'ceylon.language.Finished', IdentifiableObject);
inheritProto(Finished, IdentifiableObject, '$IdentifiableObject$');
var $finished = new Finished.$$;
$finished.string = String$("exhausted", 9);
$finished.getString = function() { return this.string; }
function getExhausted() { return $finished; }

function Comparison(name) {
    var that = new Comparison.$$;
    that.name = String$(name);
    return that;
}
initType(Comparison, 'ceylon.language.Comparison', IdentifiableObject);
inheritProto(Comparison, IdentifiableObject, '$IdentifiableObject$');
var Comparison$proto = Comparison.$$.prototype;
Comparison$proto.getString = function() { return this.name }

#include functions.js
#include sequences.js

function Range(first, last) {
    var that = new Range.$$;
    that.first = first;
    that.last = last;
    var index = 0;
    var x = first;
    var dec = first.compare(last) === larger;
    while (x.equals(last) === getFalse()) { //some replicated code because we don't yet have the functions here
        index++;
        x = dec ? x.getPredecessor() : x.getSuccessor();
    }
    that.size = Integer(index+1);
    return that;
}
initType(Range, 'ceylon.language.Range', Object$, Sequence, Category);
inheritProto(Range, Object$, '$Object$', Sequence);
var Range$proto = Range.$$.prototype;
Range$proto.getFirst = function() { return this.first; }
Range$proto.getLast = function() { return this.last; }
Range$proto.getEmpty = function() { return getFalse(); }
Range$proto.getDecreasing = function() {
    return Boolean$(this.first.compare(this.last) === larger);
}
Range$proto.next = function(x) {
    return this.getDecreasing() === getTrue() ? x.getPredecessor() : x.getSuccessor();
}
Range$proto.getSize = function() { return this.size; }
Range$proto.getLastIndex = function() { return Integer(this.size-1); }
Range$proto.item = function(index) {
    var idx = 0;
    var x = this.first;
    while (idx < index.value) {
        if (x.equals(this.last) === getTrue()) { return getNull(); }
        else {
            idx++;
            x = this.next(x);
        }
    }
    return x;
}
Range$proto.includes = function(x) {
    var compf = x.compare(this.first);
    var compl = x.compare(this.last);
    var rval = this.getDecreasing() === getTrue() ? ((compf === equal || compf === smaller) && (compl === equal || compl === larger)) : ((compf === equal || compf === larger) && (compl === equal || compl === smaller));
    return Boolean$(rval);
}
Range$proto.contains = function(x) {
    if (typeof x.compare==='function' || (x.prototype && typeof x.prototype.compare==='function')) {
        return this.includes(x);
    }
    return $false;
}
Range$proto.getRest = function() {
    var n = this.next(this.first);
    return (n.equals(this.last) === getTrue()) ? ArraySequence([]) : Range(n, this.last);
}
Range$proto.by = function(step) {
    if (step.compare(Integer(0)) !== larger) {
        //throw
    }
    if (this.first.equals(this.last) === getTrue() || step.equals(Integer(1)) === getTrue()) {
        return this;
    }
    var seq = [];
    var x = this.first;
    while (this.includes(x) === getTrue()) {
        seq.push(x);
        for (var i = 0; i < step.value; i++) { x = this.next(x); }
    }
    return ArraySequence(seq);
}
Range$proto.segment = function(from, len) {
    //only positive length for now
    if (len.compare(Integer(0)) !== larger) return $empty;
    if (this.defines(from) === $false) return $empty;
    var x = this.first;
    for (var i=0; i < from.value; i++) { x = this.next(x); }
    var y = x;
    for (var i=1; i < len.value; i++) { y = this.next(y); }
    if (this.includes(y) === getFalse()) { y = this.last; }
    return Range(x, y);
}
Range$proto.span = function(from, to) {
    from = largest(Integer(0),from);
    if (to === getNull() || to === undefined) {
        to = this.getLastIndex();
    }
    if (this.defines(from) === $false) {
        //If it's an inverse range, adjust the "from" (upper bound)
        if (from.compare(to) === larger && this.defines(to) === $true) {
            //Decrease the upper bound
            while (!this.defines(from)) {
                from = from.getPredecessor();
            }
        } else {
            return $empty;
        }
    } else while (this.defines(to) === $false) {
        //decrease the upper bound
        to = to.getPredecessor();
    }
    return Range(this.item(from), this.item(to));
}
Range$proto.definesEvery = function(keys) {
    for (var i = 0; i < keys.getSize().value; i++) {
        if (this.defines(keys.item(Integer(i))) === getFalse()) {
            return getFalse();
        }
    }
    return getTrue();
}
Range$proto.definesAny = function(keys) {
    for (var i = 0; i < keys.getSize().value; i++) {
        if (this.defines(keys.item(Integer(i))) === getTrue()) {
            return getTrue();
        }
    }
    return getFalse();
}
Range$proto.defines = function(idx) { return Boolean$(idx.compare(this.getSize()) === smaller); }
Range$proto.getString = function() { return String$(this.first.getString().value + ".." + this.last.getString().value); }
Range$proto.equals = function(other) {
    if (!other) { return getFalse(); }
    var eqf = this.first.equals(other.getFirst());
    var eql = this.last.equals(other.getLast());
    return Boolean$(eqf === getTrue() && eql === getTrue());
}
Range$proto.getIterator = function() { return RangeIterator(this); }

function RangeIterator(range) {
    var that = new RangeIterator.$$;
    that.range = range;
    that.current = range.getFirst();
    return that;
}
initType(RangeIterator, 'ceylon.language.RangeIterator', IdentifiableObject, Iterator);
inheritProto(RangeIterator, IdentifiableObject, '$IdentifiableObject$');
var RangeIterator$proto = RangeIterator.$$.prototype;
RangeIterator$proto.next = function() {
    var rval = this.current;
    if (rval.equals($finished) === getTrue()) {
        return rval;
    } else if (rval.equals(this.range.getLast()) === getTrue()) {
        this.current = $finished;
    } else {
        this.current = this.range.next(this.current);
    }
    return rval;
}


function Entry(key, item) {
    var that = new Entry.$$;
    Object$(that);
    Void(that);
    that.key = key;
    that.item = item;
    return that;
}
initType(Entry, 'ceylon.language.Entry', Object$);
inheritProto(Entry, Object$, '$Object$');
var Entry$proto = Entry.$$.prototype;
Entry$proto.getString = function() {
    return String$(this.key.getString().value + "->" + this.item.getString().value);
}
Entry$proto.getKey = function() { return this.key; }
Entry$proto.getItem = function() { return this.item; }
Entry$proto.equals = function(other) {
    return Boolean$(other && this.key.equals(other.key) === getTrue() && this.item.equals(other.item) === getTrue());
}
Entry$proto.getHash = function() { Integer(this.key.getHash().value ^ this.item.getHash().value); }


exports.Exception=Exception; //TODO just to let the compiler finish
exports.IdentifiableObject=IdentifiableObject;
exports.Object=Object$; //TODO just to let the compiler finish
exports.Boolean=Boolean$;
exports.Comparison=Comparison;
exports.getNull=getNull;
exports.getTrue=getTrue;
exports.getFalse=getFalse;
exports.getExhausted=getExhausted;
exports.Range=Range;
exports.Entry=Entry;

    });
}(typeof define==='function' && define.amd ? 
    define : function (factory) {
    if (typeof exports!=='undefined') {
        factory(require, exports, module);
    } else {
        throw "no module loader";
    }
}));
