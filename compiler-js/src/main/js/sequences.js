function initTypeProto(a,b,c,d,e,f,g,h,i,j,k,l);//IGNORE
function initTypeProtoI(a,b,c,d,e,f,g,h,i,j,k,l);//IGNORE
function String$(x){}//IGNORE
function Boolean$(x){}//IGNORE
function Character(x){}//IGNORE
function isOfType(a,b){}//IGNORE
function Integer(x){}//IGNORE
function smallest(x,y){}//IGNORE
function largest(x,y){}//IGNORE
function Exception(){}//IGNORE
var List,Some,Cloneable,Ranged,exports,$true,$false,larger,smaller,equal,Object$,$empty,$finished,Iterator;//IGNORE
var IdentifiableObject,Category,Sized;//IGNORE

function Sequence($$sequence) {
    return $$sequence;
}
initTypeProtoI(Sequence, 'ceylon.language.Sequence', List, Some, Cloneable, Ranged);
var Sequence$proto = Sequence.$$.prototype;
Sequence$proto.getLast = function() {
    var last = this.item(this.getLastIndex());
    if (last === null) throw Exception();
    return last;
}


function ArraySequence(value) {
    var that = new ArraySequence.$$;
    that.value = value;
    return that;
}
initTypeProto(ArraySequence, 'ceylon.language.ArraySequence', IdentifiableObject, Sequence);
var ArraySequence$proto = ArraySequence.$$.prototype;
ArraySequence$proto.item = function(index) {
    var result = this.value[index.value];
    return result!==undefined ? result:null;
}
ArraySequence$proto.getSize = function() { return Integer(this.value.length) }
ArraySequence$proto.getEmpty = function() { return this.value.length > 0 ? $false : $true; }
ArraySequence$proto.getLastIndex = function() { return this.getSize().getPredecessor(); }
ArraySequence$proto.getFirst = function() { return this.item(Integer(0)); }
ArraySequence$proto.getLast = function() { return this.item(this.getLastIndex()); }
ArraySequence$proto.segment = function(from, len) {
    var seq = [];
    if (len.compare(Integer(0)) === larger) {
        var stop = from.plus(len).value;
        for (var i=from.value; i < stop; i++) {
            var x = this.item(Integer(i));
            if (x !== null) { seq.push(x); }
        }
    }
    return ArraySequence(seq);
}
ArraySequence$proto.span = function(from, to) {
    var fromIndex = largest(Integer(0),from).value;
    var toIndex = to === null || to === undefined ? this.getLastIndex().value : smallest(to, this.getLastIndex()).value;
    var seq = [];
    if (fromIndex === toIndex) {
        return Singleton(this.item(from));
    } else if (toIndex > fromIndex) {
        for (var i = fromIndex; i <= toIndex && this.defines(Integer(i)) === $true; i++) {
            seq.push(this.item(Integer(i)));
        }
    } else {
        //Negative span, reverse seq returned
        for (var i = fromIndex; i >= toIndex && this.defines(Integer(i)) === $true; i--) {
            seq.push(this.item(Integer(i)));
        }
    }
    return ArraySequence(seq);
}
ArraySequence$proto.getRest = function() {
    return this.getSize().equals(Integer(1)) === $true ? $empty : ArraySequence(this.value.slice(1));
}
ArraySequence$proto.items = function(keys) {
    var seq = [];
    for (var i = 0; i < keys.getSize().value; i++) {
        var key = keys.item(Integer(i));
        seq.push(this.item(key));
    }
    return ArraySequence(seq);
}
ArraySequence$proto.getKeys = function() { return TypeCategory(this, 'ceylon.language.Integer'); }
ArraySequence$proto.contains = function(elem) {
    for (var i=0; i<this.value.length; i++) {
        if (elem.equals(this.value[i])) {
            return $true;
        }
    }
    return $false;
}
ArraySequence$proto.getReversed = function() {
    var arr = this.value.slice(0);
    arr.reverse();
    return ArraySequence(arr);
}

function TypeCategory(seq, type) {
    var that = new TypeCategory.$$;
    that.type = type;
    that.seq = seq;
    return that;
}
initTypeProto(TypeCategory, 'ceylon.language.TypeCategory', IdentifiableObject, Category);
var TypeCategory$proto = TypeCategory.$$.prototype;
TypeCategory$proto.contains = function(k) {
    return Boolean$(isOfType(k, this.type) === $true && this.seq.defines(k) === $true);
}

function SequenceBuilder() {
    var that = new SequenceBuilder.$$;
    that.seq = [];
    return that;
}
initTypeProto(SequenceBuilder, 'ceylon.language.SequenceBuilder', IdentifiableObject, Sized);
var SequenceBuilder$proto = SequenceBuilder.$$.prototype;
SequenceBuilder$proto.getSequence = function() { return ArraySequence(this.seq); }
SequenceBuilder$proto.append = function(e) { this.seq.push(e); }
SequenceBuilder$proto.appendAll = function(arr) {
	if (arr && arr.value && arr.value.length) {
        for (var i = 0; i < arr.value.length; i++) {
            this.seq.push(arr.value[i]);
        }
    }
}
SequenceBuilder$proto.getSize = function() { return Integer(this.seq.length); }

function SequenceAppender(other) {
	var that = new SequenceAppender.$$;
	that.seq = [];
	that.appendAll(other);
	return that;
}
initTypeProto(SequenceAppender, 'ceylon.language.SequenceAppender', SequenceBuilder);

function Singleton(elem) {
    var that = new Singleton.$$;
    that.value = [elem];
    that.elem = elem;
    return that;
}
initTypeProto(Singleton, 'ceylon.language.Singleton', Object$, Sequence);
var Singleton$proto = Singleton.$$.prototype;
Singleton$proto.getString = function() { return String$("{ " + this.elem.getString().value + " }") }
Singleton$proto.item = function(index) {
    return index.value===0 ? this.value[0] : null;
}
Singleton$proto.getSize = function() { return Integer(1); }
Singleton$proto.getLastIndex = function() { return Integer(0); }
Singleton$proto.getFirst = function() { return this.elem; }
Singleton$proto.getLast = function() { return this.elem; }
Singleton$proto.getEmpty = function() { return $false; }
Singleton$proto.getRest = function() { return $empty; }
Singleton$proto.defines = function(idx) { return idx.equals(Integer(0)); }
Singleton$proto.getKeys = function() { return TypeCategory(this, 'ceylon.language.Integer'); }
Singleton$proto.span = function(from, to) {
	if (to === undefined || to === null) to = from;
    return (from.equals(Integer(0)) === $true || to.equals(Integer(0)) === $true) ? this : $empty;
}
Singleton$proto.segment = function(idx, len) {
    if (idx.equals(Integer(0)) === $true && len.compare(Integer(0)) === larger) {
        return this;
    }
    return $empty;
}
Singleton$proto.getIterator = function() { return SingletonIterator(this.elem); }
Singleton$proto.getReversed = function() { return this; }

function SingletonIterator(elem) {
    var that = new SingletonIterator.$$;
    that.elem = elem;
    that.done = false;
    return that;
}
initTypeProto(SingletonIterator, 'ceylon.language.SingletonIterator', IdentifiableObject, Iterator);
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
