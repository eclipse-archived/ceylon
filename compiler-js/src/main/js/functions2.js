//More functions, related to comprehensions and iterables
var exports=null;//IGNORE
var $finished=null;//IGNORE
function String$(a,b){}//IGNORE
function StringBuilder(){}//IGNORE

function string(/*Iterable<Character>*/chars) {
    if (chars === undefined) return String$('',0);
    var s = StringBuilder();
    var iter = chars.getIterator();
    var c; while ((c = iter.next()) !== $finished) {
        s.appendCharacter(c);
    }
    return s.getString();
}

function internalSort(comp, elems, $$$mptypes) {
    if (elems===undefined) {return empty;}
    var arr = [];
    var it = elems.getIterator();
    var e;
    while ((e=it.next()) !== $finished) {arr.push(e);}
    if (arr.length === 0) {return empty;}
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
        for (var i=0; i < arguments.length; i++) {
            t = Tuple(arguments[i], t);
        }
        return tf(t);
    }
    rf.$$targs$$=$$$mptypes;
    return rf;
}
function unflatten(ff, $$$mptypes) {
    var rf = function() {
        return ff(arguments);
    }
    rf.$$targs$$=$$$mptypes;
    return rf;
}
