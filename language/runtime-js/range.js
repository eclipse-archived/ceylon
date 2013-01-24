function Range(first, last, $$targs$$, that) {
    if (that === undefined) that = new Range.$$;
    that.first = first;
    that.last = last;
    that.$$targs$$=$$targs$$;
    if (isOfType(first, $init$Enumerable()) && isOfType(last, $init$Enumerable())) {
        that.size=last.getIntegerValue().minus(first.getIntegerValue()).getMagnitude().plus(1);
    }
    return that;
}
initTypeProto(Range, 'ceylon.language::Range', Object$, Sequence, Category);
var Range$proto = Range.$$.prototype;
Range$proto.getFirst = function() { return this.first; }
Range$proto.getLast = function() { return this.last; }
Range$proto.getEmpty = function() { return false; }
Range$proto.getDecreasing = function() {
    return this.first.compare(this.last) === larger;
}
Range$proto.next = function(x) {
    return this.getDecreasing() ? x.getPredecessor() : x.getSuccessor();
}
Range$proto.getSize = function() {
    if (this.size === undefined) {
        var size=1;
        var curr = this.first;
        while (!curr.equals(this.last)) {
            size++;
            curr = this.next(curr);
        }
        this.size=size;
    }
    return this.size;
}
Range$proto.getLastIndex = function() { return this.getSize()-1; }
Range$proto.item = function(index) {
    var idx = 0;
    var x = this.first;
    while (idx < index) {
        if (x.equals(this.last)) { return null; }
        else {
            idx++;
            x = this.next(x);
        }
    }
    return x;
}
Range$proto.includes = function(x) {
    var compf = x.compare(this.first);
    var compl = x.compare(this.last);
    return this.getDecreasing() ? ((compf === equal || compf === smaller) && (compl === equal || compl === larger)) : ((compf === equal || compf === larger) && (compl === equal || compl === smaller));
}
Range$proto.contains = function(x) {
    if (typeof x.compare==='function' || (x.prototype && typeof x.prototype.compare==='function')) {
        return this.includes(x);
    }
    return false;
}
Range$proto.getRest = function() {
    if (this.first.equals(this.last)) return empty;
    var n = this.next(this.first);
    return Range(n, this.last, this.$$targs$$);
}
Range$proto.segment = function(from, len) {
    //only positive length for now
    if (len.compare(0) !== larger) return empty;
    if (!this.defines(from)) return empty;
    var x = this.first;
    for (var i=0; i < from; i++) { x = this.next(x); }
    var y = x;
    for (var i=1; i < len; i++) { y = this.next(y); }
    if (!this.includes(y)) { y = this.last; }
    return Range(x, y, this.$$targs);
}
Range$proto.span = function(from, to) {
    var li = this.getLastIndex();
    if (to<0) {
        if (from<0) {
            return empty;
        }
        to = 0;
    }
    else if (to > li) {
        if (from > li) {
            return empty;
        }
        to = li;
    }
    if (from < 0) {
        from = 0;
    }
    else if (from > li) {
        from = li;
    }
    var x = this.first;
    for (var i=0; i < from; i++) { x = this.next(x); }
    var y = this.first;
    for (var i=0; i < to; i++) { y = this.next(y); }
    return Range(x, y, this.$$targs);
}
Range$proto.spanTo = function(to) {
    return to<0 ? empty : this.span(0, to);
}
Range$proto.spanFrom = function(from) {
    return this.span(from, this.getLastIndex());
}
Range$proto.definesEvery = function(keys) {
    for (var i = 0; i < keys.getSize(); i++) {
        if (!this.defines(keys.get(i))) {
            return false;
        }
    }
    return true;
}
Range$proto.definesAny = function(keys) {
    for (var i = 0; i < keys.getSize(); i++) {
        if (this.defines(keys.get(i))) {
            return true;
        }
    }
    return false;
}
Range$proto.defines = function(idx) { return idx.compare(this.getSize()) === smaller; }
Range$proto.getString = function() { return String$(this.first.getString() + ".." + this.last.getString()); }
Range$proto.equals = function(other) {
    if (!other) { return false; }
    return this.first.equals(other.getFirst()) && this.last.equals(other.getLast());
}
Range$proto.getIterator = function() { return RangeIterator(this); }
Range$proto.getReversed = function() { return Range(this.last, this.first, this.$$targs$$); }
Range$proto.skipping = function(skip) {
    var x=0;
    var e=this.first;
    while (x++<skip) {
        e=this.next(e);
    }
    return this.includes(e) ? new Range(e, this.last, this.$$targs$$) : empty;
}
Range$proto.taking = function(take) {
    if (take == 0) {
        return empty;
    }
    var x=0;
    var e=this.first;
    while (++x<take) {
        e=this.next(e);
    }
    return this.includes(e) ? new Range(this.first, e, this.$$targs$$) : this;
}
Range$proto.getSequence = function() { return this; }
Range$proto.getCoalesced = function() { return this; }
Range$proto.count = function(f) {
    var e = this.getFirst();
    var c = 0;
    while (this.includes(e)) {
        if (f(e)) {
            c++;
        }
        e = this.next(e);
    }
    return c;
}

function RangeIterator(range) {
    var that = new RangeIterator.$$;
    that.range = range;
    that.current = range.getFirst();
    that.$$targs$$ = range.$$targs$$;
    that.next = (range.last>=range.first) ? RangeIterator$forwardNext : RangeIterator$backwardNext;
    return that;
}
initTypeProto(RangeIterator, 'ceylon.language::RangeIterator', Basic, Iterator);
RangeIterator$forwardNext = function() {
    var rval = this.current;
    if (rval === $finished) {
        return rval;
    }
    if (rval.compare(this.range.last) === smaller) {
        this.current = rval.getSuccessor();
    } else {
        this.current = $finished;
    }
    return rval;
}
RangeIterator$backwardNext = function() {
    var rval = this.current;
    if (rval === $finished) {
        return rval;
    }
    if (rval.compare(this.range.last) === larger) {
        this.current = rval.getPredecessor();
    } else {
        this.current = $finished;
    }
    return rval;
}
exports.Range=Range;
