(function (define) {
    define('ceylon.language', function (require, exports) {

//the Ceylon language module
function print(line) { console.log(line.getString().value) }

CeylonObject=function CeylonObject() {}

CeylonObject.prototype.getString=function() { String(Object.prototype.toString.apply(this)) };
CeylonObject.prototype.toString=function() { return this.getString().value };

//TODO: we need to distinguish between Objects and IdentifiableObjects
CeylonObject.prototype.equals = function(other) { return Boolean(this===other) }

function Integer(value) {
    var that = new CeylonObject;
    that.value = value;
    that.getString = function() { return String(value.toString()) }
    that.plus = function(other) { return Integer(value+other.value) }
    that.minus = function(other) { return Integer(value-other.value) }
    that.times = function(other) { return Integer(value*other.value) }
    that.divided = function(other) {
        var exact = value/other.value;
        return Integer((exact<0) ? Math.ceil(exact) : Math.floor(exact));
    }
    that.remainder = function(other) { return Integer(value%other.value) }
    that.power = function(other) {
        var exact = Math.pow(value, other.value);
        return Integer((exact<0) ? Math.ceil(exact) : Math.floor(exact));
    }
    that.negativeValue = function() { return Integer(-value) }
    that.positiveValue = function() { return that }
    that.equals = function(other) { return Boolean(other.value===value) }
    that.compare = function(other) {
        return value===other.value ? equal
                                   : (value<other.value ? smaller:larger);
    }
    that.getFloat = function() { return Float(value) }
    that.getInteger = function() { return that }
    return that;
}

function Float(value) {
    var that = new CeylonObject;
    that.value = value;
    that.getString = function() { return String(value.toString()) }
    that.plus = function(other) { return Float(value+other.value) }
    that.minus = function(other) { return Float(value-other.value) }
    that.times = function(other) { return Float(value*other.value) }
    that.divided = function(other) { return Float(value/other.value) }
    that.power = function(other) { return Integer(Math.pow(value,other.value)) }
    that.negativeValue = function() { return Float(-value) }
    that.positiveValue = function() { return that }
    that.equals = function(other) { return Boolean(other.value===value) }
    that.compare = function(other) {
        return value===other.value ? equal
                                   : (value<other.value ? smaller:larger);
    }
    that.getFloat = function() { return that }
    return that;
}

function String(value) {
    var that = new CeylonObject;
    that.value = value;
    that.getString = function() { return that }
    that.toString = function() { return value }
    that.plus = function(other) { return String(value+other.value) }
    that.equals = function(other) { return Boolean(other.value===value) }
    that.compare = function(other) {
        return value===other.value ? equal
                                   : (value<other.value ? smaller:larger);
    }
    that.getUppercased = function() { return value.toUpperCase() }
    that.getLowercased = function() { return value.toLowerCase() }
    return that;
}

function Case(caseName) {
    var that = new CeylonObject;
    var string = String(caseName);
    that.getString = function() { return string }
    return that;
}

function getNull() { return null }
var $true = Case("true");
function getTrue() { return $true; }
var $false = Case("false");
function getFalse() { return $false; }
function Boolean(value) {
    return value ? $true : $false;
}

var larger = Case("larger");
function getLarger() { return larger }
var smaller = Case("smaller");
function getSmaller() { return smaller }
var equal = Case("equal");
function getEqual() { return equal }
function largest(x, y) { return x.compare(y) === larger ? x : y }
function smallest(x, y) { return x.compare(y) === smaller ? x : y }

function ArraySequence(value) {
    var that = new CeylonObject;
    that.value = value;
    that.getString = function() { return String(value.toString()) }
    that.item = function(index) {
        var result = value[index.value];
        return result!==undefined ? result:null;
    }
    return that;
}

function Singleton(elem) {
    var that = new CeylonObject;
    that.value = [elem];
    that.getString = function() { return String(elem.toString()) }
    that.item = function(index) {
        return index.value === 0 ? elem : null;
    }
    return that;
}

function Entry(key, item) {
    var that = new CeylonObject;
    that.key = key;
    that.item = item;
    that.getString = function() { return String(key.getString().value + "->" + item.getString().value) }
    that.getKey = function() { return key; }
    that.getItem = function() { return item; }
    that.equals = function(other) {
        return Boolean(other && key.equals(other.getKey()) && item.equals(other.getItem()));
    }
    that.getHash = function() { key.getHash()/2 + item.getHash()/2 }
    return that;
}

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

    });
}(typeof define==='function' && define.amd ? 
    define : function (id, factory) {
    if (typeof exports!=='undefined') {
        factory(require, exports);
    } else {
        throw "no module loader";
    }
}));
