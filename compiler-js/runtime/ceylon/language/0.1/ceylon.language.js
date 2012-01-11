(function (define) {
    define('ceylon.language', function (require, exports) {

//the Ceylon language module
function print(line) { console.log(line.getString().value) }

CeylonObject=function CeylonObject() {}

CeylonObject.prototype.getString=function() { String(Object.prototype.toString.apply(this)) };
CeylonObject.prototype.toString=function() { return this.getString().value };

//TODO: we need to distinguish between Objects and IdentifiableObjects
CeylonObject.prototype.equals = function(other) { return Boolean(this===other) }

function $Integer() {}
function Integer(value) {
    var that = new $Integer;
    that.value = value;
    return that;
}
for(var $ in CeylonObject.prototype){$Integer.prototype[$]=CeylonObject.prototype[$]}
$Integer.prototype.getString = function() { return String(this.value.toString()) }
$Integer.prototype.plus = function(other) { return Integer(this.value+other.value) }
$Integer.prototype.minus = function(other) { return Integer(this.value-other.value) }
$Integer.prototype.times = function(other) { return Integer(this.value*other.value) }
$Integer.prototype.divided = function(other) {
    var exact = this.value/other.value;
    return Integer((exact<0) ? Math.ceil(exact) : Math.floor(exact));
}
$Integer.prototype.remainder = function(other) { return Integer(this.value%other.value) }
$Integer.prototype.power = function(other) {
    var exact = Math.pow(this.value, other.value);
    return Integer((exact<0) ? Math.ceil(exact) : Math.floor(exact));
}
$Integer.prototype.negativeValue = function() { return Integer(-this.value) }
$Integer.prototype.positiveValue = function() { return this }
$Integer.prototype.equals = function(other) { return Boolean(other.value===this.value) }
$Integer.prototype.compare = function(other) {
    return this.value===other.value ? equal
                                    : (this.value<other.value ? smaller:larger);
}
$Integer.prototype.getFloat = function() { return Float(this.value) }
$Integer.prototype.getInteger = function() { return this }
$Integer.prototype.getSuccessor = function() { return Integer(this.value+1) }
$Integer.prototype.getPredecessor = function() { return Integer(this.value-1) }

function $Float() {}
function Float(value) {
    var that = new $Float;
    that.value = value;
    return that;
}
for(var $ in CeylonObject.prototype){$Float.prototype[$]=CeylonObject.prototype[$]}
$Float.prototype.getString = function() { return String(this.value.toString()) }
$Float.prototype.plus = function(other) { return Float(this.value+other.value) }
$Float.prototype.minus = function(other) { return Float(this.value-other.value) }
$Float.prototype.times = function(other) { return Float(this.value*other.value) }
$Float.prototype.divided = function(other) { return Float(this.value/other.value) }
$Float.prototype.power = function(other) { return Float(Math.pow(this.value, other.value)) }
$Float.prototype.negativeValue = function() { return Float(-this.value) }
$Float.prototype.positiveValue = function() { return this }
$Float.prototype.equals = function(other) { return Boolean(other.value===this.value) }
$Float.prototype.compare = function(other) {
    return this.value===other.value ? equal
                                    : (this.value<other.value ? smaller:larger);
}
$Float.prototype.getFloat = function() { return this }

function $String() {}
function String(value) {
    var that = new $String;
    that.value = value;
    return that;
}
for(var $ in CeylonObject.prototype){$String.prototype[$]=CeylonObject.prototype[$]}
$String.prototype.getString = function() { return this }
$String.prototype.toString = function() { return this.value }
$String.prototype.plus = function(other) { return String(this.value+other.value) }
$String.prototype.equals = function(other) { return Boolean(other.value===this.value) }
$String.prototype.compare = function(other) {
    return this.value===other.value ? equal
                                    : (this.value<other.value ? smaller:larger);
}
$String.prototype.getUppercased = function() { return String(this.value.toUpperCase()) }
$String.prototype.getLowercased = function() { return String(this.value.toLowerCase()) }

function $Case() {}
function Case(caseName) {
    var that = new $Case;
    that.string = String(caseName);
    return that;
}
for(var $ in CeylonObject.prototype){$Case.prototype[$]=CeylonObject.prototype[$]}
$Case.prototype.getString = function() { return this.string }

function getNull() { return null }
var $true = Case("true");
function getTrue() { return $true; }
var $false = Case("false");
function getFalse() { return $false; }
function Boolean(value) {
    return value ? $true : $false;
}

//These are operators for handling nulls
function exists(value) { return value === getNull() ? getFalse() : getTrue(); }
function nonempty(value) { return value && value.value && value.value.length > 0 ? getTrue() : getFalse(); }

var larger = Case("larger");
function getLarger() { return larger }
var smaller = Case("smaller");
function getSmaller() { return smaller }
var equal = Case("equal");
function getEqual() { return equal }
function largest(x, y) { return x.compare(y) === larger ? x : y }
function smallest(x, y) { return x.compare(y) === smaller ? x : y }

function $ArraySequence() {}
function ArraySequence(value) {
    var that = new $ArraySequence;
    that.value = value;
    return that;
}
for(var $ in CeylonObject.prototype){$ArraySequence.prototype[$]=CeylonObject.prototype[$]}
$ArraySequence.prototype.getString = function() { return String(this.value.toString()) }
$ArraySequence.prototype.item = function(index) {
    var result = this.value[index.value];
    return result!==undefined ? result:null;
}
$ArraySequence.prototype.getSize = function() { return Integer(this.value.length) }
$ArraySequence.prototype.getEmpty = function() { return this.getSize().compare(Integer(0)) === larger ? getFalse() : getTrue(); }
$ArraySequence.prototype.getLastIndex = function() { return this.getSize().getPredecessor(); }
$ArraySequence.prototype.getFirst = function() { return this.item(Integer(0)); }
$ArraySequence.prototype.getLast = function() { return this.item(this.getLastIndex()); }
$ArraySequence.prototype.defines = function(idx) { return idx.compare(this.getLastIndex()) === smaller ? getTrue() : getFalse(); }
$ArraySequence.prototype.segment = function(from, len) {
    var seq = [];
    if (len.equals(Integer(0)) === getFalse()) {
        var stop = from.plus(len).value;
        for (var i=from.value; i < stop; i++) {
            var x = this.item(Integer(i));
            if (x !== getNull()) { seq.push(x); }
        }
    }
    return ArraySequence(seq);
}
$ArraySequence.prototype.span = function(from, to) {
    var fromIndex = largest(Integer(0),from).value;
    var toIndex = to === getNull() ? this.getLastIndex().value : smallest(to, this.getLastIndex()).value;
    var seq = [];
    if (fromIndex === toIndex) {
        return Singleton(this.item(from));
    } else if (toIndex > fromIndex) {
        for (var i = fromIndex; i <= toIndex; i++) {
            seq.push(this.item(Integer(i)));
        }
    } else {
        //Negative span, reverse seq returned
        for (var i = fromIndex; i >= toIndex; i--) {
            seq.push(this.item(Integer(i)));
        }
    }
    return ArraySequence(seq);
}
function $Range() {}
function Range(first, last) {
    var that = new $Range;
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
for(var $ in CeylonObject.prototype){$Range.prototype[$]=CeylonObject.prototype[$]}
$Range.prototype.getFirst = function() { return this.first; }
$Range.prototype.getLast = function() { return this.last; }
$Range.prototype.getDecreasing = function() {
    return this.first.compare(this.last) === larger ? getTrue() : getFalse();
}
$Range.prototype.next = function(x) {
    return this.getDecreasing() === getTrue() ? x.getPredecessor() : x.getSuccessor();
}
$Range.prototype.getSize = function() { return this.size; }
$Range.prototype.getLastIndex = function() { return Integer(this.size-1); }
$Range.prototype.item = function(index) {
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
$Range.prototype.includes = function(x) {
    var compf = x.compare(this.first);
    var compl = x.compare(this.last);
    var rval = this.getDecreasing() === getTrue() ? ((compf === equal || compf === smaller) && (compl === equal || compl === larger)) : ((compf === equal || compf === larger) && (compl === equal || compl === smaller));
    return rval ? getTrue() : getFalse();
}
$Range.prototype.contains = $Range.prototype.includes;
$Range.prototype.getRest = function() {
    var n = this.next(this.first);
    return (n.equals(this.last) === getTrue()) ? ArraySequence([]) : Range(n, this.last);
}
$Range.prototype.by = function(step) {
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
$Range.prototype.segment = function(from, len) {
    if (len.equals(Integer(0)) === getTrue()) return ArraySequence([])
    var x = this.first;
    for (var i=0; i < from.value; i++) { x = this.next(x); }
    //only positive length for now
    var y = x;
    for (var i=1; i < len.value; i++) { y = this.next(y); }
    if (this.includes(y) === getFalse()) { y = this.last; }
    return Range(x, y);
}
$Range.prototype.span = function(from, to) {
    from = largest(Integer(0),from);
    to = to === getNull() ? this.getLastIndex() : smallest(to, this.getLastIndex());
    return Range(this.item(from), this.item(to));
}
$Range.prototype.getString = function() { return String(this.first.getString().value + ".." + this.last.getString().value); }
$Range.prototype.equals = function(other) {
    var eqf = this.first.equals(other.getFirst());
    var eql = this.last.equals(other.getLast());
    return (eqf === getTrue() && eql === getTrue()) ? getTrue() : getFalse();
}

function $Singleton() {}
function Singleton(elem) {
    var that = new $Singleton;
    that.value = [elem];
    return that;
}
for(var $ in CeylonObject.prototype){$Singleton.prototype[$]=CeylonObject.prototype[$]}
$Singleton.prototype.getString = function() { return String(this.value.toString()) }
$Singleton.prototype.item = function(index) {
    return index.value===0 ? this.value[0] : null;
}
$Singleton.prototype.getSize = function() { return Integer(1) }

function $Entry() {}
function Entry(key, item) {
    var that = new $Entry;
    that.key = key;
    that.item = item;
    return that;
}
for(var $ in CeylonObject.prototype){$Entry.prototype[$]=CeylonObject.prototype[$]}
$Entry.prototype.getString = function() {
    return String(this.key.getString().value + "->" + this.item.getString().value)
}
$Entry.prototype.getKey = function() { return this.key }
$Entry.prototype.getItem = function() { return this.item }
$Entry.prototype.equals = function(other) {
    return Boolean(other && this.key.equals(other.key) && this.item.equals(other.item));
}
$Entry.prototype.getHash = function() { Integer(this.key.getHash().value ^ this.item.getHash().value) }

//receives ArraySequence, returns element
function min(seq) {
    var v = seq.value[0];
    if (seq.value.length > 1) {
        for (i = 1; i < seq.value.length; i++) {
            v = smallest(v, seq.value[i]);
        }
    }
    return v;
}
//receives ArraySequence, returns element 
function max(seq) {
    var v = seq.value[0];
    if (seq.value.length > 1) {
        for (i = 1; i < seq.value.length; i++) {
            v = largest(v, seq.value[i]);
        }
    }
    return v;
}
//receives ArraySequence of ArraySequences, returns flat ArraySequence
function join(seqs) {
    var builder = [];
    for (i = 0; i < seqs.value.length; i++) {
        builder = builder.concat(seqs.value[i].value);
    }
    return ArraySequence(builder);
}
//receives ArraySequences, returns ArraySequence
function zip(keys, items) {
    var entries = []
    var numEntries = Math.min(keys.value.length, items.value.length);
    for (i = 0; i < numEntries; i++) {
        entries[i] = Entry(keys.value[i], items.value[i]);
    }
    return ArraySequence(entries);
}
//receives and returns ArraySequence
function coalesce(seq) {
    var newseq = [];
    for (i = 0; i < seq.value.length; i++) {
        if (seq.value[i]) {
            newseq = newseq.concat(seq.value[i]);
        }
    }
    return ArraySequence(newseq);
}

//receives ArraySequence and CeylonObject, returns new ArraySequence
function append(seq, elem) {
    return ArraySequence(seq.value.concat(elem));
}

//Receives ArraySequence, returns ArraySequence (with Entries)
function entries(seq) {
    var e = [];
    for (i = 0; i < seq.value.length; i++) {
        e.push(Entry(Integer(i), seq.value[i]));
    }
    return ArraySequence(e);
}

exports.print=print;
exports.Integer=Integer;
exports.Float=Float;
exports.String=String;
exports.Boolean=Boolean;
exports.getNull=getNull;
exports.Case=Case;
exports.getTrue=getTrue;
exports.getFalse=getFalse;
exports.getLarger=getLarger;
exports.getSmaller=getSmaller;
exports.getEqual=getEqual;
exports.ArraySequence=ArraySequence;
exports.Range=Range;
exports.Singleton=Singleton;
exports.Entry=Entry;
exports.largest=largest;
exports.smallest=smallest;
exports.min=min;
exports.max=max;
exports.join=join;
exports.zip=zip;
exports.coalesce=coalesce;
exports.append=append;
exports.entries=entries;
exports.exists=exists;
exports.nonempty=nonempty;

    });
}(typeof define==='function' && define.amd ? 
    define : function (id, factory) {
    if (typeof exports!=='undefined') {
        factory(require, exports);
    } else {
        throw "no module loader";
    }
}));
