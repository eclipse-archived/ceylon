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

this.print=print;
this.Integer=Integer;
this.Float=Float;
this.String=String;
this.ArraySequence=ArraySequence;