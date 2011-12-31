//the Ceylon language module
this.print=function print(line) { console.log(line.toString()) }

this.Integer=function Integer(value) {
    this.value = value;
    this.toString = function() { return value.toString() }
    this.plus = function(other) { return new Integer(value+other.value) }
}

this.Float=function Float(value) {
    this.value = value;
    this.toString = function() { return value.toString() }
    this.plus = function(other) { return new Float(value+other.value) }
}

this.String=function String(value) {
    this.value = value;
    this.toString = function() { return value }
    this.plus = function(other) { return new String(value+other.value) }
}

this.ArraySequence=function ArraySequence(value) {
    this.value = value;
    this.toString = function() { return value.toString() }
}