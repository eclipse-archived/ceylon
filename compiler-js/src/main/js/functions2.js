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

function combine(/*Callable<Result,Element,Other>*/f, /*Iterable<Element>*/i1, /*Iterable<Other>*/i2) {
    return Comprehension(function(){
        var ei = i1.getIterator();
        var oi = i2.getIterator();
        return function() {
            var ne = ei.next();
            var no = oi.next();
            if (ne === $finished || no === $finished) {
                return $finished;
            }
            return f(ne, no);
        };
    });
}

function sort(elems, $$$mptypes) {
    return internalSort(byIncreasing(function(e) { return e; }), elems, $$$mptypes);
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
exports.combine=combine;
exports.sort=sort;
