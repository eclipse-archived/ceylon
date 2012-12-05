//More functions, related to comprehensions and iterables
var exports=null;//IGNORE
var $finished=null;//IGNORE
function String$(a,b){}//IGNORE
function StringBuilder(){}//IGNORE

function byIncreasing/*<Element,Value>*/(/*Callable<Value?,Element>*/comp) {
    return function(x, y) {
        var a = comp(x);
        if (a !== null) {
            var b = comp(y);
            if (b !== null) {
                return a.compare(b);
            }
        }
        return null;
    };
}

function byDecreasing/*<Element,Value>*/(/*Callable<Value?,Element>*/comp) {
    return function(x, y) {
        var a = comp(x);
        if (a !== null) {
            var b = comp(y);
            if (b !== null) {
                return b.compare(a);
            }
        }
        return null;
    };
}

function count(/*Iterable<Boolean>*/truths) {
    if (truths === undefined) return 0;
    var c=0;
    var iter = truths.getIterator();
    var i; while ((i = iter.next()) !== $finished) {
        if (i) c++;
    }
    return c;
}

function string(/*Iterable<Character>*/chars) {
    if (chars === undefined) return String$('',0);
    var s = StringBuilder();
    var iter = chars.getIterator();
    var c; while ((c = iter.next()) !== $finished) {
        s.appendCharacter(c);
    }
    return s.getString();
}

function equalTo(v) {
    return function(e) {
        return v.equals(e);
    };
}
function greaterThan(v) {
    return function(e) {
        return e.compare(v) === larger;
    };
}
function lessThan(v) {
    return function(e) {
        return e.compare(v) === smaller;
    };
}

function byKey(/*Callable<Comparison,Key,Key>*/f) {
    return function(a,b) {
        return f(a.getKey(), b.getKey());
    }
}
function byItem(/*Callable<Comparison,Key,Key*/f) {
    return function(a,b) {
        return f(a.getItem(), b.getItem());
    }
}

function emptyOrSingleton(/*Element?*/elem) {
    return elem===null ? empty : Singleton(elem);
}

function forKey(/*Callable<Result,Key>*/f) {
    return function(/*Key->Object*/ e) {
        return f(e.getKey());
    }
}
function forItem(/*Callable<Result,Item>*/f) {
    return function(/*Key->Item*/e) {
        return f(e.getItem());
    }
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

function sort(elems) {
    if (elems===undefined) {return empty;}
    var arr = [];
    var it = elems.getIterator();
    var e;
    while ((e=it.next()) !== $finished) {arr.push(e);}
    if (arr.length === 0) {return empty;}
    arr.sort(function(a, b) {
        var cmp = a.compare(b);
        return (cmp===larger) ? 1 : ((cmp===smaller) ? -1 : 0);
    });
    return ArraySequence(arr);
}

exports.forKey=forKey;
exports.forItem=forItem;
exports.emptyOrSingleton=emptyOrSingleton;
exports.equalTo=equalTo;
exports.greaterThan=greaterThan;
exports.lessThan=lessThan;
exports.byIncreasing=byIncreasing;
exports.byDecreasing=byDecreasing;
exports.count=count;
exports.string=string;
exports.byKey=byKey;
exports.byItem=byItem;
exports.combine=combine;
exports.sort=sort;
