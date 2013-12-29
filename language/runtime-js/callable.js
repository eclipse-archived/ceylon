function Callable(wat) {
    return wat;
}
Callable.$$metamodel$$={mod:$$METAMODEL$$,$an:function(){return[shared()];},$tp:{Arguments:{'var':'out'},Return:{'var':'out'}},d:['ceylon.language','Callable']};
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
    }
    var set_meta = callable.$$metamodel$$ === undefined;
    if (set_meta) {
        callable.$$metamodel$$={$ps:[],mod:$$METAMODEL$$,d:['ceylon.language','Callable']};
        if (parms !== undefined) {
            callable.$$metamodel$$['$ps']=parms;
        }
    }
    if (targs !== undefined && callable.$$targs$$ === undefined) {
        callable.$$targs$$=targs;
        if (set_meta) {
            callable.$$metamodel$$['$t']=targs['Return'];
        }
    }
    return callable;
}
initExistingTypeProto($JsCallable, Function, 'ceylon.language::JsCallable', Callable);

function noop() { return null; }

//This is used for plain method references
function JsCallable(o,f) {
    if (o === null) return noop;
    var f2 = function() { return f.apply(o, arguments); };
    f2.$$metamodel$$=f.$$metamodel$$===undefined?Callable.$$metamodel$$:f.$$metamodel$$;
    return f2;
}
JsCallable.$$metamodel$$=function(){return{ 'satisfies':[{t:Callable,a:{Return:'Return',Arguments:'Arguments'}}],
  $tp:{Return:{'var':'out'}, Arguments:{'var':'in'}},$an:function(){return[shared()];},mod:$$METAMODEL$$,d:['ceylon.language','Callable']};}

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
JsCallableList.$$metamodel$$={$tp:{Return:{'var':'out'}, Arguments:{'var':'in'}},$an:function(){return[shared()];},mod:$$METAMODEL$$,d:['ceylon.language','Callable']};

exports.JsCallableList=JsCallableList;
exports.JsCallable=JsCallable;
exports.$JsCallable=$JsCallable;
