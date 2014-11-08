function $JsCallable(callable,parms,targs) {
    if (callable.getT$all === undefined) {
        callable.getT$all=Callable.getT$all;
    }
    var set_meta = callable.$crtmm$ === undefined;
    if (set_meta) {
        callable.$crtmm$={ps:[],mod:$CCMM$,d:['$','Callable']};
        if (parms !== undefined) {
            callable.$crtmm$['ps']=parms;
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

function nop$() { return null; }

//This is used for plain method references
function JsCallable(o,f,targs) {
  if (o===null || o===undefined) return nop$;
  var f2 = function() {
    var arg=[].slice.call(arguments,0);
    if (arg.length===1 && is$(arg[0],{t:Tuple})) {
      //Possible spread
      var mm=getrtmm$$(f);
      //If f has only 1 param and it's not sequenced, get its type
      var a1t=mm && mm.ps && mm.ps.length===1 && mm.ps[0].seq===undefined ? mm.ps[0].$t : undefined;
      var typecheck;
      if (a1t && arg[0].$$targs$$) {
        if (arg[0].$$targs$$.First$Tuple) {
          typecheck=arg[0].$$targs$$.First$Tuple;
        } else if (arg[0].$$targs$$.t==='T') {
          typecheck=arg[0].$$targs$$.l[0];
        }
      } else {
        typecheck={t:Iterable};
        if (arg[0].$$targs$$ && arg[0].$$targs$$.Element$Iterable)typecheck.a={Element$Iterable:arg[0].$$targs$$.Element$Iterable};
      }
      //If its type is a type param, get the type arg
      if (typeof(a1t)==='string' && targs && targs[a1t])a1t=targs[a1t];
      if (mm && mm.ps && (mm.ps.length>1 || (mm.ps.length===1
          && (mm.ps[0].seq || !extendsType(a1t, typecheck))))) {
        var a=arg[0].nativeArray ? arg[0].nativeArray():undefined;
        if (a===undefined) {
          a=[];
          for (var i=0;i<arg[0].size;i++)a.push(arg[0].$_get(i));
        }
        arg=a;
      }
    }
    if (targs)arg.push(targs);
    return f.apply(o, arg);
  };
  f2.$crtmm$=f.$crtmm$===undefined?Callable.$crtmm$:f.$crtmm$;
  return f2;
}
JsCallable.$crtmm$=function(){return{ sts:[{t:Callable,a:{Return$Callable:'Return$Callable',Arguments$Callable:'Arguments$Callable'}}],
  tp:{Return$Callable:{dv:'out'}, Arguments$Callable:{dv:'in'}},pa:1,mod:$CCMM$,d:['$','Callable']};}

//This is used for spread method references
function JsCallableList(value,$mpt) {
    return function() {
        var a=arguments;
        if ($mpt) {
          a=[].slice.call(arguments,0);
          a.push($mpt);
        }
        var rval = Array(value.length);
        for (var i = 0; i < value.length; i++) {
            var c = value[i];
            rval[i] = c.f.apply(c.o, a);
        }
        return value.length===0?getEmpty():sequence(rval,{Element$sequence:{t:Callable},Absent$sequence:{t:Nothing}});
    };
}
JsCallableList.$crtmm$={tp:{Return$Callable:{dv:'out'}, Arguments$Callable:{dv:'in'}},pa:1,mod:$CCMM$,d:['$','Callable']};

ex$.JsCallableList=JsCallableList;
ex$.JsCallable=JsCallable;
ex$.$JsCallable=$JsCallable;
