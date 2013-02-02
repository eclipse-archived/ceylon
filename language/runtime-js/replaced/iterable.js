function initTypeProto(a,b,c,d){}//IGNORE
function initType(a,b){}//IGNORE
function ArraySequence(x){}//IGNORE
function Comprehension(x){}//IGNORE
function Exception(x){}//IGNORE
function String$(x){}//IGNORE
var Container,empty,larger,smaller,exports;//IGNORE

function Iterable(wat) {
    return wat;
}
initTypeProto(Iterable, 'ceylon.language::Iterable', $init$Container());
function $init$Iterable() { return Iterable; }
var Iterable$proto=Iterable.$$.prototype;
Iterable$proto.getEmpty = function() {
    return this.getIterator().next() === getFinished();
}
Iterable$proto.getFirst = function() {
    var e = this.getIterator().next();
    return e === getFinished() ? null : e;
}
Iterable$proto.getRest = function() {
    return this.skipping(1);
}
Iterable$proto.getSequence = function() {
    var a = [];
    var iter = this.getIterator();
    var next;
    while ((next = iter.next()) !== getFinished()) {
        a.push(next);
    }
    return ArraySequence(a, this.$$targs$$);
}
Iterable$proto.$map = function(mapper) {
    var iter = this;
    return Comprehension(function() {
        var it = iter.getIterator();
        return function() {
            var e = it.next();
            if(e !== getFinished()) {return mapper(e);}
            return getFinished();
        }
    });
}
Iterable$proto.$filter = function(select) {
    var iter = this;
    return Comprehension(function() {
        var it = iter.getIterator();
        return function() {
            do {
                var e = it.next();
            } while ((e !== getFinished()) && !select(e));
            return e;
        }
    });
}
Iterable$proto.fold = function(ini, accum) {
    var r = ini;
    var iter = this.getIterator();
    var e; while ((e = iter.next()) !== getFinished()) {
        r = accum(r, e);
    }
    return r;
}
Iterable$proto.find = function(select) {
    var iter = this.getIterator();
    var e; while ((e = iter.next()) !== getFinished()) {
        if (select(e)) {
            return e;
        }
    }
    return null;
}
Iterable$proto.findLast = function(select) {
    var iter = this.getIterator();
    var last = null;
    var e; while ((e = iter.next()) !== getFinished()) {
        if (select(e)) {
            last = e;
        }
    }
    return last;
}
Iterable$proto.$sort = function(/*Callable<Comparison?,Element,Element>*/comparing) {
    var a = [];
    var iter = this.getIterator();
    var e; while ((e = iter.next()) !== getFinished()) {
        a.push(e);
    }
    a.sort(function(x,y) {
        var r = comparing(x,y);
        if (r === larger) return 1;
        if (r === smaller) return -1;
        return 0;
    });
    return ArraySequence(a, this.$$targs$$);
}
Iterable$proto.any = function(/*Callable<Boolean,Element>*/selecting) {
    var iter = this.getIterator();
    var e; while ((e = iter.next()) !== getFinished()) {
        if (selecting(e)) {
            return true;
        }
    }
    return false;
}
Iterable$proto.$every = function(/*Callable<Boolean,Element>*/selecting) {
    var iter = this.getIterator();
    var e; while ((e = iter.next()) !== getFinished()) {
        if (!selecting(e)) {
            return false;
        }
    }
    return true;
}
Iterable$proto.skipping = function(skip) {
    function skip$iter(iter,skip){
        var $cmp$=new skip$iter.$$;
        Basic($cmp$);
        $cmp$.iter=iter;
        $cmp$.skip=skip;
        $cmp$.getIterator=function(){
            var iter = this.iter.getIterator();
            for (var i=0; i < this.skip; i++) {
                iter.next();
            }
            return iter;
        };
        return $cmp$;
    }
    initTypeProto(skip$iter, 'ceylon.language::SkipIterable', $init$Basic(), Iterable);
    return skip$iter(this,skip);
}
Iterable$proto.taking = function(take) {
    if (take <= 0) return empty;
    var iter = this;
    return Comprehension(function() {
        var it = iter.getIterator();
        var i = 0;
        return function() {
            if (i >= take) {return getFinished();}
            ++i;
            return it.next();
        }
    });
}
Iterable$proto.by = function(step) {
    if (step == 1) return this;
    if (step < 1) throw Exception(String$("Step must be positive"));
    var iter = this;
    return Comprehension(function() {
        var it = iter.getIterator();
        return function() {
            var e = it.next();
            for (var i=1; i<step && (it.next()!==getFinished()); i++);
            return e;
        }
    });
}
Iterable$proto.count = function(sel) {
    var c = 0;
    var iter = this.getIterator();
    var e; while ((e = iter.next()) !== getFinished()) {
        if (sel(e)) c++;
    }
    return c;
}
Iterable$proto.getCoalesced = function() {
    var iter = this;
    return Comprehension(function() {
        var it = iter.getIterator();
        return function() {
            var e;
            while ((e = it.next()) === null);
            return e;            
        }
    });
}
Iterable$proto.getIndexed = function() {
    var iter = this;
    return Comprehension(function() {
        var it = iter.getIterator();
        var idx = 0;
        return function() {
            var e;
            while ((e = it.next()) === null) {idx++;}
            return e === getFinished() ? e : Entry(idx++, e);
        }
    });
}
Iterable$proto.getLast = function() {
    var iter = this.getIterator();
    var l=null;
    var e; while ((e = iter.next()) !== getFinished()) {
        l=e;
    }
    return l;
}
Iterable$proto.collect = function(collecting) {
    return this.$map(collecting).getSequence();
}
Iterable$proto.select = function(selecting) {
    return this.$filter(selecting).getSequence();
}
Iterable$proto.group = function(grouping, $$$mptypes) {
    var map = HashMap();
    var it = this.getIterator();
    var elem;
    var newSeq = ArraySequence([], this.$$targs$$);
    while ((elem=it.next()) !== getFinished()) {
        var key = grouping(elem);
        var seq = map.put(Entry(key, newSeq, {Key:$$$mptypes.Grouping, Item:{t:Sequence, a:this.$$targs$$}}), true);
        if (seq === null) {
            seq = newSeq;
            newSeq = ArraySequence([], this.$$targs$$);
        }
        seq.push(elem);
    }
    return map;
}
Iterable$proto.chain = function(other, $$$mptypes) {
    return ChainedIterable(this, other, {Element:this.$$targs$$.Element, Other:$$$mptypes.Other});
}
Iterable$proto.defaultNullElements = function(defaultValue) {
    var iter = this;
    return Comprehension(function() {
        var it = iter.getIterator();
        return function() {
            var value = it.next();
            if (value === null) {return defaultValue;}
            return value;
        }
    });
}
Iterable$proto.getSize = function() {
    return this.count(function() { return true; });
}
exports.Iterable=Iterable;

function ChainedIterable(first, second, $$targs$$, chained) {
    if (chained===undefined) {chained = new ChainedIterable.$$;}
    Basic(chained);
    chained.first = first;
    chained.second = second;
    chained.$$targs$$=$$targs$$;
    return chained;
}
initTypeProto(ChainedIterable, "ceylon.language::ChainedIterable",
        $init$Basic(), Iterable);
var ChainedIterable$proto = ChainedIterable.$$.prototype;
ChainedIterable$proto.getIterator = function() {
    return ChainedIterator(this.first, this.second);
}
