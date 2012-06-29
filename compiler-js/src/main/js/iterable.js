function initTypeProtoI(a, b, c){} //IGNORE
function initTypeProto(a,b,c,d){}//IGNORE
function initType(a,b){}//IGNORE
function Boolean$(x){}//IGNORE
function IdentifiableObject(x){}//IGNORE
function ArraySequence(x){}//IGNORE
function Comprehension(x){}//IGNORE
function Exception(x){}//IGNORE
function String$(x){}//IGNORE
var Container,$finished,$true,$false,$empty,larger,smaller,exports;//IGNORE

function Iterator(wat) {
    return wat;
}
initType(Iterator, 'ceylon.language.Iterator');
exports.Iterator=Iterator;

function Iterable(wat) {
    return wat;
}
initTypeProtoI(Iterable, 'ceylon.language.Iterable', Container);
var Iterable$proto=Iterable.$$.prototype;
Iterable$proto.getEmpty = function() {
    return Boolean$(this.getIterator().next() === $finished);
}
Iterable$proto.getSequence = function() {
    var a = [];
    var iter = this.getIterator();
    var next;
    while ((next = iter.next()) !== $finished) {
        a.push(next);
    }
    return ArraySequence(a);
}
Iterable$proto.map = function(mapper) {
    var iter = this;
    function mapped$iter(){
        var $cmp$=new mapped$iter.$$;
        IdentifiableObject(mapped$iter);
        $cmp$.iter=iter.getIterator();
        $cmp$.mapper=mapper;
        $cmp$.next=function(){
            var e = this.iter.next();
            if(e !== $finished){
                return this.mapper(e);
            }else return $finished;
        };
        return $cmp$;
    }
    initTypeProto(mapped$iter, 'ceylon.language.MappedIterator', IdentifiableObject, Iterator);
    return Comprehension(mapped$iter);
}
Iterable$proto.filter = function(select) {
    var iter = this;
    function filtered$iter(){
        var $cmp$=new filtered$iter.$$;
        IdentifiableObject(filtered$iter);
        $cmp$.iter=iter.getIterator();
        $cmp$.select=select;
        $cmp$.next=function(){
            var e = this.iter.next();
            var flag = e === $finished ? true : this.select(e) === $true;
            while (!flag) {
                e = this.iter.next();
                flag = e === $finished ? true : this.select(e) === $true;
            }
            return e;
        };
        return $cmp$;
    }
    initTypeProto(filtered$iter, 'ceylon.language.FilteredIterator', IdentifiableObject, Iterator);
    return Comprehension(filtered$iter);
}
Iterable$proto.fold = function(ini, accum) {
    var r = ini;
    var iter = this.getIterator();
    var e; while ((e = iter.next()) !== $finished) {
        r = accum(r, e);
    }
    return r;
}
Iterable$proto.find = function(select) {
    var iter = this.getIterator();
    var e; while ((e = iter.next()) !== $finished) {
        if (select(e) === $true) {
            return e;
        }
    }
    return null;
}
Iterable$proto.findLast = function(select) {
    var iter = this.getIterator();
    var last = null;
    var e; while ((e = iter.next()) !== $finished) {
        if (select(e) === $true) {
            last = e;
        }
    }
    return last;
}
Iterable$proto.sorted = function(/*Callable<Comparison?,Element,Element>*/comparing) {
    var a = [];
    var iter = this.getIterator();
    var e; while ((e = iter.next()) !== $finished) {
        a.push(e);
    }
    a.sort(function(x,y) {
        var r = comparing(x,y);
        if (r === larger) return 1;
        if (r === smaller) return -1;
        return 0;
    });
    return ArraySequence(a);
}
Iterable$proto.any = function(/*Callable<Boolean,Element>*/selecting) {
    var iter = this.getIterator();
    var e; while ((e = iter.next()) !== $finished) {
        if (selecting(e) === $true) {
            return $true;
        }
    }
    return $false;
}
Iterable$proto.every = function(/*Callable<Boolean,Element>*/selecting) {
    var iter = this.getIterator();
    var e; while ((e = iter.next()) !== $finished) {
        if (selecting(e) !== $true) {
            return $false;
        }
    }
    return $true;
}
Iterable$proto.skipping = function(skip) {
    function skip$iter(iter,skip){
        var $cmp$=new skip$iter.$$;
        IdentifiableObject(skip$iter);
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
    initTypeProto(skip$iter, 'ceylon.language.SkipIterable', IdentifiableObject, Iterable);
    return skip$iter(this,skip.value);
}
Iterable$proto.taking = function(take) {
    if (take.value <= 0) return $empty;
    var iter = this;
    function take$iter(){
        var $cmp$=new take$iter.$$;
        IdentifiableObject(take$iter);
        $cmp$.iter=iter.getIterator();
        $cmp$.take=take.value;
        $cmp$.i=0;
        $cmp$.next=function(){
            if (this.i++>=this.take) {
                return $finished;
            }
            return this.iter.next();
        };
        return $cmp$;
    }
    initTypeProto(take$iter, 'ceylon.language.TakeIterator', IdentifiableObject, Iterator);
    return Comprehension(take$iter);
}
Iterable$proto.by = function(step) {
    if (step.value == 1) return this;
    if (step.value < 1) throw Exception(String$("Step must be positive"));
    var iter = this;
    function by$iter(){
        var $cmp$=new by$iter.$$;
        IdentifiableObject(by$iter);
        $cmp$.iter=iter.getIterator();
        $cmp$.step=step.value;
        $cmp$.next=function(){
            var e = this.iter.next();
            for (var i=1; i < this.step; i++) {
                this.iter.next();
            }
            return e;
        };
        return $cmp$;
    }
    initTypeProto(by$iter, 'ceylon.language.SteppedIterator', IdentifiableObject, Iterator);
    return Comprehension(by$iter);
}
exports.Iterable=Iterable;
