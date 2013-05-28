var exports=null;//IGNORE
function String$(a,b){}//IGNORE
function StringBuilder(){}//IGNORE

function string(/*Iterable<Character>*/chars) {
    if (chars === undefined) return String$('',0);
    var s = StringBuilder();
    var iter = chars.iterator();
    var c; while ((c = iter.next()) !== getFinished()) {
        s.appendCharacter(c);
    }
    return s.string;
}
string.$$metamodel$$={$an:function(){return[shared()];},mod:$$METAMODEL$$,d:$$METAMODEL$$['ceylon.language']['string']};

function internalSort(comp, elems, $$$mptypes) {
    if (elems===undefined) {return getEmpty();}
    var arr = [];
    var it = elems.iterator();
    var e;
    while ((e=it.next()) !== getFinished()) {arr.push(e);}
    if (arr.length === 0) {return getEmpty();}
    arr.sort(function(a, b) {
        var cmp = comp(a,b);
        return (cmp===larger) ? 1 : ((cmp===smaller) ? -1 : 0);
    });
    return ArraySequence(arr, $$$mptypes);
}
internalSort.$$metamodel$$={$an:function(){return[shared()];},mod:$$METAMODEL$$,d:$$METAMODEL$$['ceylon.language']['internalSort']};
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
flatten.$$metamodel$$={$an:function(){return[shared()];},mod:$$METAMODEL$$,d:$$METAMODEL$$['ceylon.language']['flatten']};

function unflatten(ff, $$$mptypes) {
    if (ff.$$metamodel$$ && ff.$$metamodel$$['$ps']) {
        var ru = function ru(seq) {
            if (seq===undefined || seq.size === 0) { return ff(); }
            var pmeta = ff.$$metamodel$$['$ps'];
            var a = [];
            for (var i = 0; i < pmeta.length; i++) {
                if (pmeta[i]['seq'] == 1) {
                    a[i] = seq.skipping(i).sequence;
                } else if (seq.size > i) {
                    a[i] = seq.get(i);
                } else {
                    a[i] = undefined;
                }
            }
            a[i]=ru.$$targs$$;
            return ff.apply(ru, a);
        }
    } else {
        var ru = function ru(seq) {
            if (seq===undefined || seq.size === 0) { return ff(); }
            var a = [];
            for (var i = 0; i < seq.size; i++) {
                a[i] = seq.get(i);
            }
            a[i]=ru.$$targs$$;
            return ff.apply(ru, a);
        }
    }
    ru.$$targs$$=$$$mptypes;
    return ru;
}
unflatten.$$metamodel$$={$an:function(){return[shared()];},mod:$$METAMODEL$$,d:$$METAMODEL$$['ceylon.language']['unflatten']};
exports.flatten=flatten;
exports.unflatten=unflatten;

//internal
function toTuple(iterable) {
  var seq = iterable.sequence;
  return Tuple(seq.first, seq.rest.sequence,
    {First:seq.$$targs$$.Element, Element:seq.$$targs$$.Element, Rest:{t:Sequential, a:seq.$$targs$$}});
}
exports.toTuple=toTuple;

function integerRangeByIterable(range, step, $$$mptypes) {
    return Comprehension(function(){
        var a = range.first;
        var b = range.last;
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
integerRangeByIterable.$$metamodel$$={$an:function(){return[shared()];},mod:$$METAMODEL$$,d:$$METAMODEL$$['ceylon.language']['integerRangeByIterable']};
exports.integerRangeByIterable=integerRangeByIterable;
