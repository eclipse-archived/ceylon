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
initExistingTypeProto($JsCallable, Function, 'ceylon.language::JsCallable', $init$Callable());

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
initExistingTypeProto(jsc$2, Function, 'ceylon.language::JsCallable', $init$Callable());

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
  if (a.length===undefined||a.getT$all)console.log("NO ES ARREGLO!",a);
  var arg=noInvoke?[].slice.call(a,0):a;
  //if we get only 1 arg and it's a tuple...
  var spridx=arg.length-1;
  if (arg.length>0 && is$(arg[spridx],{t:Tuple})) {
    var tuple0=arg[spridx];
    //Possible spread, check the metamodel
    var mm=getrtmm$$(f);
    var params = mm && mm.ps || [];
    if (params.length===arg.length && params[spridx].seq>0 && is$(tuple0,{t:Sequence,a:{Element$Sequence:params[spridx].$t}})){
      return arg;
    }
    if (params.size===tuple0.size+spridx) {
      //Simple mapping
      var all=[];
      for (var i=0; i<spridx;i++) {
        all.push(arg[i]);
      }
      var j=0;
      for (var i=spridx; i<params.length;i++){
        var tet=params[i].$t;
        if (typeof(tet)==='string'&&targs)tet=targs[tet];
        if ((params[i].def && tuple0.getFromFirst(j)===undefined) || is$(tuple0.$_get(j),tet)) {
          all.push(tuple0.getFromFirst(j));
        }
        j++
      }
      if (all.length===params.length) {
        return all;
      }
    }
    //If f has only 1 param and it's not sequenced, get its type
    var a1t=params.length===1 && params[spridx].seq===undefined ? params[spridx].$t : undefined;
    //If it's a type param, get the type argument
    if (typeof(a1t)==='string')a1t=targs && targs[a1t];
    //If the tuple type matches the param type, it's NOT a spread
    //(it's just a regular 1-param func which happens to receive a tuple)
    if (!a1t) {
      var _ra=Array(tuple0.size);
      for (var i=0;i<tuple0.size;i++)_ra[i]=tuple0.$_get(i);
      return _ra;
    } else if (is$(tuple0,a1t) && extendsType(a1t,{t:Sequential})) {
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
    }
  }
  if (noInvoke) {
    return arg;
  }
  if (targs)arg.push(targs);
  return f.apply(undefined,arg);
}
ex$.spread$=spread$;

//This is used for spread method references
//Pass it a list (or Iterable, really) and a function to execute on the item with the specified arguments
//$mpt contains the type arguments for the function
//$et is the type of the elements
function JsCallableList(list,fun,$mpt,$et) {
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
    return rval.$sa$($et||{t:Anything});
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
