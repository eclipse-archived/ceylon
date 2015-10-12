function $JsCallable(f$,parms,targs) {
  //Do not wrap another $JsCallable
  if (f$.jsc$)return f$;
  if (f$.getT$all === undefined) {
    f$.getT$all=Callable.getT$all;
  }
  var set_meta = getrtmm$$(f$) === undefined;
  if (set_meta) {
    f$.$crtmm$={ps:parms||[],mod:$CCMM$,d:['$','Callable']};
  }
  if (targs !== undefined && f$.$$targs$$ === undefined) {
    f$.$$targs$$=targs;
    if (set_meta) {
      f$.$crtmm$.$t=targs.Return$Callable;
    }
  }
  if (f$.$flattened$||f$.$unflattened$)return f$;
  var f=function c1(){
    return f$.apply(undefined,spread$(arguments,f$,targs,1));
  }
  f.$crtmm$=f$.$crtmm$;
  f.getT$all=f$.getT$all;
  f.jsc$=f$;
  f.equals=function(o) {
    return false;//o===f || o===f$;
  }
  return f;
}
initExistingTypeProto($JsCallable, Function, 'ceylon.language::JsCallable', Callable);

function jsc$2(f$,parms,targs) {
  //Do not wrap another $JsCallable
  if (f$.jsc$)return f$;
  if (f$.getT$all === undefined) {
    f$.getT$all=Callable.getT$all;
  }
  var set_meta = getrtmm$$(f$) === undefined;
  if (set_meta) {
    f$.$crtmm$={ps:parms||[],mod:$CCMM$,d:['$','Callable']};
  }
  if (targs !== undefined && f$.$$targs$$ === undefined) {
    f$.$$targs$$=targs;
    if (set_meta) {
      f$.$crtmm$.$t=targs.Return$Callable;
    }
  }
  if (f$.$flattened$||f$.$unflattened$)return f$;
  var f=function c1(){
    return f$.apply(undefined,arguments);
  }
  f.$crtmm$=f$.$crtmm$;
  f.getT$all=f$.getT$all;
  f.jsc$=f$;
  f.equals=function(o) {
    return false;//o===f || o===f$;
  }
  return f;
}
initExistingTypeProto(jsc$2, Function, 'ceylon.language::JsCallable', Callable);

function nop$(){return null;}

//This is used for method references
//o: The object used for "this"
//f: The function itself
//targs: The type arguments to pass in the function call
function JsCallable(o,f,targs) {
  if (o===null || o===undefined) return nop$;
  if (f.jsc$)f=f.jsc$;
  var f2=function c2() {
    var arg=spread$(arguments,f,targs,1);
    if (targs)arg.push(targs);
    return f.apply(o, arg);
  };
  f2.c2$=f;
  f2.$$targs$$=targs;
  f2.equals=function(x){
    return false;//f===x || f2===x;
  }
  f2.$crtmm$=f.$crtmm$||Callable.$crtmm$;
  return f2;
}
JsCallable.$crtmm$=function(){return{sts:[{t:Callable,a:{Return$Callable:'Return$Callable',Arguments$Callable:'Arguments$Callable'}}],
  tp:{Return$Callable:{dv:'out'}, Arguments$Callable:{dv:'in'}},pa:1,mod:$CCMM$,d:['$','Callable']};}

function jsc$3(o,f,targs) {
  if (o===null || o===undefined) return nop$;
  if (f.jsc$)f=f.jsc$;
  var f2=function c2() {
    return f.apply(o, arguments);
  };
  f2.c2$=f;
  f2.$$targs$$=targs;
  f2.equals=function(x){
    return false;//f===x || f2===x;
  }
  f2.$crtmm$=f.$crtmm$||Callable.$crtmm$;
  return f2;
}
jsc$3.$crtmm$=function(){return{sts:[{t:Callable,a:{Return$Callable:'Return$Callable',Arguments$Callable:'Arguments$Callable'}}],
  tp:{Return$Callable:{dv:'out'}, Arguments$Callable:{dv:'in'}},pa:1,mod:$CCMM$,d:['$','Callable']};}

function spread$(a,f,targs,noInvoke) {
  var argIsArray=Array.isArray(a);
  var arg=argIsArray?[].slice.call(a,0):a;
  //if we get only 1 arg and it's a tuple...
  if (arg.size===1 && is$(arg.$_get(0),{t:Tuple})) {
    var tuple0=arg.$_get(0);
    //Possible spread, check the metamodel
    var mm=getrtmm$$(f);
    var params = mm && mm.ps || [];
    if (params.length===1 && params[0].seq>0 && is$(tuple0,{t:Sequence,a:{Element$Sequence:params[0].$t}})){
      return arg;
    }
    if (tuple0.size===params.length && params.length>=1) {
      //Simple mapping
      var all=[];
      for (var i=0; i<params.length;i++){
        var tet=params[i].$t;
        if (typeof(tet)==='string'&&targs)tet=targs[tet];
        if (is$(tuple0.$_get(i),tet)) {
          all.push(tuple0.$_get(i));
        }
      }
      if (all.length===params.length) {
        return all;
      }
    }
    //If f has only 1 param and it's not sequenced, get its type
    var a1t=params.length===1 && params[0].seq===undefined ? params[0].$t : undefined;
    //If it's a type param, get the type argument
    if (typeof(a1t)==='string')a1t=targs && targs[a1t];
    //If the tuple type matches the param type, it's NOT a spread
    //(it's just a regular 1-param func which happens to receive a tuple)
    if (!a1t || is$(tuple0,a1t)) {
      return arg;
    }
    var typecheck;
    if (a1t && targs && targs.Arguments$Callable) {
      typecheck=targs.Arguments$Callable;
      if (typecheck && typecheck.t && typecheck.t==='T' && typecheck.l
          && typecheck.l.length===1 && typecheck.l[0].seq) {
        //after all, it is NOT a spread
        return arg;
      }
    } else if (a1t && tuple0.$$targs$$) {
      if (tuple0.$$targs$$.First$Tuple) {
        typecheck={t:Tuple,a:tuple0.$$targs$$};
      } else if (tuple0.$$targs$$.t==='T') {
        typecheck=tuple0.$$targs$$;
      } else if (tuple0.$$targs$$.Element$Iterable) {
        typecheck={t:Iterable,a:tuple0.$$targs$$.Element$Iterable};
      }
    }
    if (params.length>1 || (params.length===1
        && (params[0].seq || !extendsType(a1t, typecheck)))) {
      var a=tuple0.nativeArray ? tuple0.nativeArray():undefined;
      if (a===undefined) {
        a=[];
        for (var i=0;i<tuple0.size;i++)a.push(tuple0.$_get(i));
      }
      arg=a;
      argIsArray=true;
    }
  }
  if (noInvoke) {
    return arg;
  }
  if (argIsArray&&targs)arg.push(targs);
  return argIsArray?f.apply(undefined,arg):f.call(undefined,arg,targs);
}
ex$.spread$=spread$;

//This is used for spread method references
//Pass it a list (or Iterable, really) and a function to execute on the item with the specified arguments
function JsCallableList(list,fun,$mpt) {
  return function sprop() {
    var len=list.size;
    if (len===0)return empty();
    var a=arguments;
    if ($mpt) {
      a=[].slice.call(arguments,0);
      a.push($mpt);
    }
    var rval=Array(len);
    var iter=list.iterator(),item,i=0;
    while((item=iter.next())!==finished()){
        rval[i++] = fun(item,a);
    }
    return sequence(rval,{Element$sequence:{t:Callable},Absent$sequence:{t:Nothing}});
  };
}
JsCallableList.$crtmm$={tp:{Return$Callable:{dv:'out'}, Arguments$Callable:{dv:'in'}},pa:1,mod:$CCMM$,d:['$','Callable']};
function mkseq$(t,seq) {
  if (seq)t.seq=seq;
  return t;
}
ex$.JsCallableList=JsCallableList;
ex$.JsCallable=JsCallable;
ex$.$JsCallable=$JsCallable;
ex$.mkseq$=mkseq$;
ex$.jsc$2=jsc$2;
ex$.jsc$3=jsc$3;
