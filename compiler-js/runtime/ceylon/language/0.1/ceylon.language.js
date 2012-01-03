(function (define) {
    define('ceylon.language', function (require, exports) {

//the Ceylon language module
function print(line) { console.log(line.getString()) }

CeylonObject=function CeylonObject() {}

CeylonObject.prototype.getString=function() { String(Object.prototype.toString.apply(this)) };
CeylonObject.prototype.toString=function() { return this.getString().value };

//TODO: we need to distinguish between Objects and IdentifiableObjects
CeylonObject.prototype.equals = function(other) { return Boolean(this===other) }

function Integer(value) {
    var that = new CeylonObject;
    that.value = value;
    that.getString = function() { return value.toString() }
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
    return that;
}

function Float(value) {
    var that = new CeylonObject;
    that.value = value;
    that.getString = function() { return value.toString() }
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
    return that;
}

function String(value) {
    var that = new CeylonObject;
    that.value = value;
    that.getString = function() { return value }
    that.plus = function(other) { return String(value+other.value) }
    that.equals = function(other) { return Boolean(other.value===value) }
    that.compare = function(other) {
        return value===other.value ? equal
                                   : (value<other.value ? smaller:larger);
    }
    return that;
}

function Case(caseName) {
    var that = new CeylonObject;
    var string = String(caseName);
    that.getString = function() { return string }
    return that;
}

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
function min(values) {
    var min = values[0];
    if (values.length > 1) {
        for (i = 1; i < values.length; i++) {
            min = smallest(min, values[i]);
        }
    }
    return min;
}
function max(values) {
    var max = values[0];
    if (values.length > 1) {
        for (i = 1; i < values.length; i++) {
            max = largest(min, values[i]);
        }
    }
    return max;
}
function join(seqs) {
    var builder = [];
    for (i = 0; i < seqs.length; i++) {
        builder = builder.concat(seqs[i])
    }
    return builder;
}

function ArraySequence(value) {
    var that = new CeylonObject();
    that.value = value;
    that.getString = function() { return value.toString() }
    return that;
}

function Entry(key, item) {
    var that = new CeylonObject;
    that.key = key;
    that.item = item;
    that.getString = function() { return key.toString() + "->" + item.toString() }
    that.getKey = function() { return key; }
    that.getItem = function() { return item; }
    that.equals = function(other) {
        return Boolean(other && key.equals(other.getKey()) && item.equals(other.getItem()));
    }
    that.getHash = function() { key.getHash()/2 + item.getHash()/2 }
    return that;
}

function zip(keys, items) {
    var entries = []
    for (i = 0; i < smallest(Integer(keys.length), Integer(items.length)); i++) {
        entries[i] = Entry(keys[i], items[i]);
    }
    return entries;
}

exports.print=print;
exports.Integer=Integer;
exports.Float=Float;
exports.String=String;
exports.Boolean=Boolean;
exports.Case=Case;
exports.getTrue=getTrue;
exports.getFalse=getFalse;
exports.getLarger=getLarger;
exports.getSmaller=getSmaller;
exports.getEqual=getEqual;
exports.ArraySequence=ArraySequence;
exports.Entry=Entry;
exports.largest=largest;
exports.smallest=smallest;
exports.min=min;
exports.max=max;
exports.join=join;
exports.zip=zip;

    });
}(typeof define==='function' && define.amd ? 
    define : function (id, factory) {
    if (typeof exports!=='undefined') {
        factory(require, exports);
    } else {
        throw "no module loader";
    }
}));
