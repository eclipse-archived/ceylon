//the Ceylon language module
function print(line) { console.log(line.toString()) }

function Integer(value) {
    this.value = value;
    this.toString = function() { return value.toString() }
    this.plus = function(other) { return new Integer(value+other.value) }
}

function Float(value) {
    this.value = value;
    this.toString = function() { return value.toString() }
    this.plus = function(other) { return new Float(value+other.value) }
}

function String(value) {
    this.value = value;
    this.toString = function() { return value }
    this.plus = function(other) { return new String(value+other.value) }
}

function ArraySequence(value) {
    this.value = value;
    this.toString = function() { return value.toString() }
}