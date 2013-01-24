function initType(a,b,c,d,e,f){}//IGNORE
function String$(x){}//IGNORE
function ArraySequence(x){}//IGNORE
var exports;//IGNORE

function Callable(wat) {
    return wat;
}
initType(Callable, 'ceylon.language::Callable');
exports.Callable=Callable;

function $JsCallable(callable) {
    return callable;
}
initExistingTypeProto($JsCallable, Function, 'ceylon.language::JsCallable', Callable);

function noop() { return null; }

//This is used for plain method references
function JsCallable(o,f) {
	return (o !== null) ? function() { return f.apply(o, arguments); }
	                    : noop;
}

//This is used for spread method references
function JsCallableList(value) {
    return function() {
        var rval = Array(value.length);
        for (var i = 0; i < value.length; i++) {
            var c = value[i];
            rval[i] = c.f.apply(c.o, arguments);
        }
        return ArraySequence(rval);
    };
}

exports.JsCallableList=JsCallableList;
exports.JsCallable=JsCallable;
