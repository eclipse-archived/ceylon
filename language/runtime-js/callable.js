function initType(a,b,c,d,e,f){}//IGNORE
function String$(x){}//IGNORE
function ArraySequence(x){}//IGNORE
var exports;//IGNORE

function Callable(wat) {
    return wat;
}
Callable.$$metamodel$$={$nm:'Callable',$mt:'ifc',$an:function(){return[shared()];}};
exports.Callable=Callable;
function $init$Callable() {
    if (Callable.$$===undefined) {
        initType(Callable, 'ceylon.language::Callable');
    }
    return Callable;
}
exports.$init$Callable=$init$Callable;
$init$Callable();

function $JsCallable(callable,parms,targs) {
    if (callable.getT$all === undefined) {
        callable.getT$all=Callable.getT$all;
        var set_meta = callable.$$metamodel$$ === undefined;
        if (set_meta) {
            callable.$$metamodel$$={$mt:'mthd',$ps:[]};
            if (parms !== undefined) {
                callable.$$metamodel$$['$ps']=parms;
            }
        }
        if (targs !== undefined) {
            callable.$$targs$$=targs;
            if (set_meta) {
                callable.$$metamodel$$['$t']=targs['Return'];
            }
        }
    }
    return callable;
}
initExistingTypeProto($JsCallable, Function, 'ceylon.language::JsCallable', Callable);

function noop() { return null; }

//This is used for plain method references
function JsCallable(o,f) {
    Callable(o);
	return (o !== null) ? function() { return f.apply(o, arguments); }
	                    : noop;
}
JsCallable.$$metamodel$$={$nm:'Callable',$tp:{Return:{'var':'out'}, Arguments:{'var':'in'}},$an:function(){return[shared()];}};

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
JsCallableList.$$metamodel$$={$nm:'Callable',$tp:{Return:{'var':'out'}, Arguments:{'var':'in'}},$an:function(){return[shared()];}};

exports.JsCallableList=JsCallableList;
exports.JsCallable=JsCallable;
exports.$JsCallable=$JsCallable;
