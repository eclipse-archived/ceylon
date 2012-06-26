//More functions, related to comprehensions and iterables
var exports=null;//IGNORE
var $finished=null;//IGNORE
var $true=true;//IGNORE
function Integer(x){}//IGNORE
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
    if (truths === undefined) return Integer(0);
    var c=0;
    var iter = truths.getIterator();
    var i; while ((i = iter.next()) !== $finished) {
        if (i === $true) c++;
    }
    return Integer(c);
}
exports.byIncreasing=byIncreasing;
exports.byDecreasing=byDecreasing;
exports.count=count;
