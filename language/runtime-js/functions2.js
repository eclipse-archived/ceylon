var exports=null;//IGNORE
function String$(a,b){}//IGNORE
function StringBuilder(){}//IGNORE

function string(/*Iterable<Character>*/chars) {
    if (chars === undefined) return String$('',0);
    var s = StringBuilder();
    var iter = chars.getIterator();
    var c; while ((c = iter.next()) !== getFinished()) {
        s.appendCharacter(c);
    }
    return s.getString();
}

function internalSort(comp, elems, $$$mptypes) {
    if (elems===undefined) {return getEmpty();}
    var arr = [];
    var it = elems.getIterator();
    var e;
    while ((e=it.next()) !== getFinished()) {arr.push(e);}
    if (arr.length === 0) {return getEmpty();}
    arr.sort(function(a, b) {
        var cmp = comp(a,b);
        return (cmp===larger) ? 1 : ((cmp===smaller) ? -1 : 0);
    });
    return ArraySequence(arr, $$$mptypes);
}

exports.string=string;

function flatten(tf, $$$mptypes) {
    var rf = function() {
        var t = getEmpty();
        var e = null;
        var argc = arguments.length;
        var last = argc>0 ? arguments[argc-1] : undefined;
        if (typeof(last) === 'object' && typeof(last.Args) === 'object' && typeof(last.Args.t) === 'function') {
            argc--;
        }
        for (var i=0; i < argc; i++) {
            var c = arguments[i]===null ? Null :
                arguments[i] === undefined ? Empty :
                arguments[i].getT$all ? arguments[i].getT$all() :
                Anything;
            if (e === null) {
                e = c;
            } else if (e.t === 'u' && e.l.length > 0) {
                var l = [c];
                for (var j=0; j < e.l.length; j++) {
                    l[j+1] = e.l[j];
                }
            } else {
                e = {t:'u', l:[e, c]};
            }
            var rest;
            if (t === getEmpty()) {
                rest={t:Empty};
            } else {
                rest={t:Tuple, a:t.$$$targs$$$};
            }
            t = Tuple(arguments[i], t, {First:c, Element:e, Rest:rest});
        }
        return tf(t, t.$$targs$$);
    };
    rf.$$targs$$=$$$mptypes;
    return rf;
}
function unflatten(ff, $$$mptypes) {
    var ru = function ru(seq) {
        if (seq===undefined || seq.getSize() === 0) { return ff(); }
        var a = [];
        for (var i = 0; i < seq.getSize(); i++) {
            a[i] = seq.get(i);
        }
        a[i]=ru.$$targs$$;
        return ff.apply(ru, a);
    }
    ru.$$targs$$=$$$mptypes;
    return ru;
}

//internal
function toTuple(iterable) {
  var seq = iterable.getSequence();
  return Tuple(seq.getFirst(), seq.getRest().getSequence(),
    {First:seq.$$targs$$.Element, Element:seq.$$targs$$.Element, Rest:{t:Sequential, a:seq.$$targs$$}});
}
exports.toTuple=toTuple;

function integerRangeByIterable(range, step, $$$mptypes) {
    return Comprehension(function(){
        var a = range.getFirst();
        var b = range.getLast();
        if (a>b) {
            a += step;
            return function() {
                a -= step;
                return a<b ? getFinished() : a;
            }
        }
        a-=step;
        return function() {
            a += step;
            return a>b ? getFinished() : a;
        }
    }, {Element:range.$$targs$$.Element, Absent:range.$$targs$$.Absent});
}
exports.integerRangeByIterable=integerRangeByIterable;
