function Sequence($$sequence) {
    return $$sequence;
}
initType(Sequence, 'ceylon.language.Sequence', List, Some, Cloneable, Ranged);
var Sequence$proto = Sequence.$$.prototype;
Sequence$proto.getEmpty = function() { return $false }
Sequence$proto.getSize = function() { return Integer(this.getLastIndex()+1) }
Sequence$proto.defines = function(index) { return Boolean$(index.value<=this.getLastIndex().value) }

function Empty() {
    var that = new Empty.$$;
    that.value = [];
    return that;
}
initType(Empty, 'ceylon.language.Empty', List, None, Ranged, Cloneable);
var Empty$proto = Empty.$$.prototype;
Empty$proto.getEmpty = function() { return $true; }
Empty$proto.defines = function(x) { return $false; }
Empty$proto.getKeys = function() { return IntCategory(this); }
Empty$proto.definesEvery = function(x) { return $false; }
Empty$proto.definesAny = function(x) { return $false; }
Empty$proto.items = function(x) { return this; }
Empty$proto.getSize = function() { return Integer(0); }
Empty$proto.item = function(x) { return null; }
Empty$proto.getFirst = function() { return null; }
Empty$proto.segment = function(a,b) { return this; }
Empty$proto.span = function(a,b) { return this; }
Empty$proto.getIterator = function() { return emptyIterator; }
Empty$proto.getString = function() { return String$("{}"); }
Empty$proto.contains = function(x) { return $false; }
Empty$proto.getLastIndex = function() { return null; }
Empty$proto.getClone = function() { return this; }
Empty$proto.count = function(x) { return Integer(0); }

$empty = Empty();

function EmptyIterator() {
    var that = new EmptyIterator.$$;
    return that;
}
initType(EmptyIterator, 'ceylon.language.EmptyIterator', IdentifiableObject, Iterator);
inheritProto(EmptyIterator, IdentifiableObject, '$IdentifiableObject$');
var EmptyIterator$proto = EmptyIterator.$$.prototype;
EmptyIterator$proto.next = function() { return $finished; }
emptyIterator=EmptyIterator();

function ArraySequence(value) {
    var that = new ArraySequence.$$;
    that.value = value;
    return that;
}
initType(ArraySequence, 'ceylon.language.ArraySequence', IdentifiableObject, Sequence);
inheritProto(ArraySequence, IdentifiableObject, '$IdentifiableObject$', Sequence);
var ArraySequence$proto = ArraySequence.$$.prototype;
ArraySequence$proto.getString = function() {
	if (this.value.length === 0) {
		return String$("{}");
	}
    var desc = "{ ";
    var first = true;
    for (var i = 0; i < this.value.length; i++) {
        if (first) {
            first = false;
		} else {
            desc += ", ";
        }
        var item = this.value[i];
        desc += exports.exists(item) === $true ? item.getString().value : "null";
    }
    return String$(desc +" }");
}
ArraySequence$proto.item = function(index) {
    var result = this.value[index.value];
    return result!==undefined ? result:null;
}
ArraySequence$proto.getSize = function() { return Integer(this.value.length) }
ArraySequence$proto.getEmpty = function() { return this.value.length > 0 ? getFalse() : getTrue(); }
ArraySequence$proto.getLastIndex = function() { return this.getSize().getPredecessor(); }
ArraySequence$proto.getFirst = function() { return this.item(Integer(0)); }
ArraySequence$proto.getLast = function() { return this.item(this.getLastIndex()); }
ArraySequence$proto.defines = function(idx) { return Boolean$(idx.compare(this.getSize()) === smaller); }
ArraySequence$proto.segment = function(from, len) {
    var seq = [];
    if (len.compare(Integer(0)) === larger) {
        var stop = from.plus(len).value;
        for (var i=from.value; i < stop; i++) {
            var x = this.item(Integer(i));
            if (x !== getNull()) { seq.push(x); }
        }
    }
    return ArraySequence(seq);
}
ArraySequence$proto.span = function(from, to) {
    var fromIndex = largest(Integer(0),from).value;
    var toIndex = to === getNull() || to === undefined ? this.getLastIndex().value : smallest(to, this.getLastIndex()).value;
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
ArraySequence$proto.getRest = function() { return ArraySequence(this.value.slice(1)); }
ArraySequence$proto.items = function(keys) {
    var seq = [];
    for (var i = 0; i < keys.getSize().value; i++) {
        var key = keys.item(Integer(i));
        seq.push(this.item(key));
    }
    return ArraySequence(seq);
}
ArraySequence$proto.definesEvery = function(keys) {
    for (var i = 0; i < keys.getSize().value; i++) {
        if (this.defines(keys.item(Integer(i))) === getFalse()) {
            return getFalse();
        }
    }
    return getTrue();
}
ArraySequence$proto.definesAny = function(keys) {
    for (var i = 0; i < keys.getSize().value; i++) {
        if (this.defines(keys.item(Integer(i))) === getTrue()) {
            return getTrue();
        }
    }
    return getFalse();
}
ArraySequence$proto.equals = function(other) {
    if (other && other.getSize().equals(this.getSize()) === getTrue()) {
        for (var i = 0; i < this.getSize().value; i++) {
            var mine = this.item(Integer(i));
            var theirs = other.item(Integer(i));
            if (((mine === null) && theirs) || !(mine && mine.equals(theirs) === getTrue())) {
                return getFalse();
            }
        }
        return getTrue();
    }
    return getFalse();
}
ArraySequence$proto.getIterator = function() { return ArrayIterator(this.value); }
ArraySequence$proto.getKeys = function() { return IntCategory(this); }
ArraySequence$proto.contains = function(elem) {
    for (var i=0; i<this.value.length; i++) {
        if (elem.equals(this.value[i])) {
            return $true;
        }
    }
    return $false;
}

function IntCategory(seq) {
    var that = new IntCategory.$$;
    that.seq = seq;
    return that;
}
initType(IntCategory, 'ceylon.language.IntCategory', IdentifiableObject, Category);
inheritProto(IntCategory, IdentifiableObject, '$IdentifiableObject$');
var IntCategory$proto = IntCategory.$$.prototype;
IntCategory$proto.contains = function(k) {
    return this.seq.defines(k);
}
IntCategory$proto.containsEvery = function(keys) {
    var all = true;
    for (var i = 0; i < this.seq.value.length; i++) {
        all = all && this.seq.defines(keys.item(Integer(i))).value;
    }
    return Boolean$(all);
}
IntCategory$proto.containsAny = function(keys) {
    for (var i = 0; i < this.seq.value.length; i++) {
        if (this.seq.defines(keys.item(Integer(i))) == $true) {
            return $true;
        }
    }
    return $false;
}

function ArrayIterator(arr) {
    var that = new ArrayIterator.$$;
    that.array = arr;
    that.current = arr && arr.length ? arr[0] : $finished;
    that.idx = 0;
    return that;
}
initType(ArrayIterator, 'ceylon.language.ArrayIterator', IdentifiableObject, Iterator);
inheritProto(ArrayIterator, IdentifiableObject, '$IdentifiableObject$');
var ArrayIterator$proto = ArrayIterator.$$.prototype;
ArrayIterator$proto.next = function() {
    if (this.current === $finished) {
        return $finished;
    }
    this.current = this.idx < this.array.length ? this.array[this.idx] : $finished;
    this.idx++;
    return this.current;
}

function SequenceBuilder() {
    var that = new SequenceBuilder.$$;
    that.seq = [];
    return that;
}
initType(SequenceBuilder, 'ceylon.language.SequenceBuilder', IdentifiableObject, Sized);
inheritProto(SequenceBuilder, IdentifiableObject, '$IdentifiableObject$');
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
SequenceBuilder$proto.getEmpty = function() { return Boolean$(this.seq.length === 0); }

function SequenceAppender(other) {
	var that = new SequenceAppender.$$;
	that.seq = [];
	that.appendAll(other);
	return that;
}
initType(SequenceAppender, 'ceylon.language.SequenceAppender', SequenceBuilder);
inheritProto(SequenceAppender, SequenceBuilder, '$SequenceBuilder$');

function Singleton(elem) {
    var that = new Singleton.$$;
    that.value = [elem];
    that.elem = elem;
    return that;
}
initType(Singleton, 'ceylon.language.Singleton', Object$, Sequence);
inheritProto(Singleton, Object$, '$Object$', Sequence);
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
Singleton$proto.getKeys = function() { return IntCategory(this); }
Singleton$proto.span = function(from, to) {
	if (to === undefined || to === null) to = from;
    return (from.equals(Integer(0)) === getTrue() || to.equals(Integer(0)) === getTrue()) ? this : $empty;
}
Singleton$proto.segment = function(idx, len) {
    if (idx.equals(Integer(0)) === getTrue() && len.compare(Integer(0)) === larger) {
        return this;
    }
    return $empty;
}
Singleton$proto.getIterator = function() { return SingletonIterator(this.elem); }

function SingletonIterator(elem) {
    var that = new SingletonIterator.$$;
    that.elem = elem;
    that.done = false;
    return that;
}
initType(SingletonIterator, 'ceylon.language.SingletonIterator', IdentifiableObject, Iterator);
inheritProto(SingletonIterator, IdentifiableObject, '$IdentifiableObject$');
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
exports.empty=$empty;
exports.emptyIterator=emptyIterator;
