function $JsCallable(callable,parms,targs) {
    if (callable.getT$all === undefined) {
        callable.getT$all=Callable.getT$all;
    }
    var set_meta = callable.$crtmm$ === undefined;
    if (set_meta) {
        callable.$crtmm$={$ps:[],mod:$CCMM$,d:['$','Callable']};
        if (parms !== undefined) {
            callable.$crtmm$['$ps']=parms;
        }
    }
    if (targs !== undefined && callable.$$targs$$ === undefined) {
        callable.$$targs$$=targs;
        if (set_meta) {
            callable.$crtmm$['$t']=targs['Return'];
        }
    }
    return callable;
}
initExistingTypeProto($JsCallable, Function, 'ceylon.language::JsCallable', Callable);

function noop() { return null; }

//This is used for plain method references
function JsCallable(o,f) {
  if (o===null || o===undefined) return noop;
  var f2 = function() {
    var arg=[].slice.call(arguments,0);
    if (arg.length==1 && is$(arg[0],{t:Tuple})) {
      //Possible spread
      var mm=getrtmm$$(f);
      var typecheck={t:Iterable};
      if (arg[0].$$targs$$ && arg[0].$$targs$$.Element$Iterable)typecheck.a={Element$Iterable:arg[0].$$targs$$.Element$Iterable};
      if (mm && mm.$ps!==undefined
          && (mm.$ps.length>1 || (mm.$ps.length==1
          && (mm.$ps[0].seq || !extendsType(mm.$ps[0].$t, typecheck))))) {
        var a=arg[0].elem$;
        if (a===undefined) {
          a=[];
          for (var i=0;i<arg[0].size;i++)a.push(arg[0].$_get(i));
        }
        arg=a;
      }
    }
    return f.apply(o, arg);
  };
  f2.$crtmm$=f.$crtmm$===undefined?Callable.$crtmm$:f.$crtmm$;
  return f2;
}
JsCallable.$crtmm$=function(){return{ 'satisfies':[{t:Callable,a:{Return$Callable:'Return$Callable',Arguments$Callable:'Arguments$Callable'}}],
  $tp:{Return$Callable:{'var':'out'}, Arguments$Callable:{'var':'in'}},$an:function(){return[shared()];},mod:$CCMM$,d:['$','Callable']};}

//This is used for spread method references
function JsCallableList(value) {
    return function() {
        var rval = Array(value.length);
        for (var i = 0; i < value.length; i++) {
            var c = value[i];
            rval[i] = c.f.apply(c.o, arguments);
        }
        return value.length===0?getEmpty():ArraySequence(rval,{Element$ArraySequence:{t:Callable}});
    };
}
JsCallableList.$crtmm$={$tp:{Return$Callable:{'var':'out'}, Arguments$Callable:{'var':'in'}},$an:function(){return[shared()];},mod:$CCMM$,d:['$','Callable']};

ex$.JsCallableList=JsCallableList;
ex$.JsCallable=JsCallable;
ex$.$JsCallable=$JsCallable;
