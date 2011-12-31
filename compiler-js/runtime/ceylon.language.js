//the Ceylon language module
function print(line) { console.log(line.toString()) }

function Integer(value) {
    var that = {};
    that.value = value;
    that.toString = function() { return value.toString() }
    that.plus = function(other) { return Integer(value+other.value) }
    return that;
}

function Float(value) {
    var that = {};
    that.value = value;
    that.toString = function() { return value.toString() }
    that.plus = function(other) { return Float(value+other.value) }
    return that;
}

function String(value) {
    var that = {};
    that.value = value;
    that.toString = function() { return value }
    that.plus = function(other) { return String(value+other.value) }
    return that;
}

function ArraySequence(value) {
    var that = {};
    that.value = value;
    that.toString = function() { return value.toString() }
    return that;
}

this.print=print;
this.Integer=Integer;
this.Float=Float;
this.String=String;
this.ArraySequence=ArraySequence;