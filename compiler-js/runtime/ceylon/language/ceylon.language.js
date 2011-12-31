(function (define) {
    define('ceylon.language', function (require, exports) {

//the Ceylon language module
function print(line) { console.log(line.getString()) }

CeylonObject=function CeylonObject() {}

CeylonObject.prototype.getString=Object.prototype.toString;
CeylonObject.prototype.toString=CeylonObject.prototype.getString;

function Integer(value) {
    var that = new CeylonObject();
    that.value = value;
    that.getString = function() { return value.toString() }
    that.plus = function(other) { return Integer(value+other.value) }
    return that;
}

function Float(value) {
    var that = new CeylonObject();
    that.value = value;
    that.getString = function() { return value.toString() }
    that.plus = function(other) { return Float(value+other.value) }
    return that;
}

function String(value) {
    var that = new CeylonObject();
    that.value = value;
    that.getString = function() { return value }
    that.plus = function(other) { return String(value+other.value) }
    return that;
}

function ArraySequence(value) {
    var that = new CeylonObject();
    that.value = value;
    that.getString = function() { return value.toString() }
    return that;
}

exports.print=print;
exports.Integer=Integer;
exports.Float=Float;
exports.String=String;
exports.ArraySequence=ArraySequence;

    });
}(typeof define==='function' && define.amd ? 
    define : function (id, factory) {
    if (typeof exports!=='undefined') {
        factory(require, exports);
    } else {
        throw "no module loader";
    }
}));
