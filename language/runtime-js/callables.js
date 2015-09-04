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
    return f$.apply(0,spread$(arguments,f$,targs));
  }
  f.$crtmm$=f$.$crtmm$;
  f.getT$all=f$.getT$all;
  f.jsc$=f$;
  f.equals=function(o) {
    return o===f || o===f$;
  }
  return f;
}
initExistingTypeProto($JsCallable, Function, 'ceylon.language::JsCallable', Callable);

function nop$(){return null;}

//This is used for method references
//o: The object used for "this"
//f: The function itself
//targs: The type arguments to pass in the function call
function JsCallable(o,f,targs) {
  if (o===null || o===undefined) return nop$;
  if (f.jsc$)f=f.jsc$;
  var f2=function c2() {
    var arg=spread$(arguments,f,targs);
    if (targs)arg.push(targs);
    return f.apply(o, arg);
  };
  f2.c2$=f;
  f2.$$targs$$=targs;
  f2.equals=function(x){
    return f===x || f2===x;
  }
  f2.$crtmm$=f.$crtmm$||Callable.$crtmm$;
  return f2;
}
JsCallable.$crtmm$=function(){return{sts:[{t:Callable,a:{Return$Callable:'Return$Callable',Arguments$Callable:'Arguments$Callable'}}],
  tp:{Return$Callable:{dv:'out'}, Arguments$Callable:{dv:'in'}},pa:1,mod:$CCMM$,d:['$','Callable']};}

function spread$(a,f,targs) {
  var arg=[].slice.call(a,0);
  //if we get only 1 arg and it's a tuple...
  if (arg.length===1 && is$(arg[0],{t:Tuple})) {
    //Possible spread, check the metamodel
    var mm=getrtmm$$(f);
    if (arg[0].size===mm.ps.length && mm.ps.length>=1) {
      //Simple mapping
      var all=[];
      for (var i=0; i<mm.ps.length;i++){
        var tet=mm.ps[i].$t;
        if (typeof(tet)==='string'&&targs)tet=targs[tet];
        if (is$(arg[0].$_get(i),tet)) {
          all.push(arg[0].$_get(i));
        }
      }
      if (all.length===mm.ps.length) {
        return all;
      }
    }
    //If f has only 1 param and it's not sequenced, get its type
    var a1t=mm && mm.ps && mm.ps.length===1 && mm.ps[0].seq===undefined ? mm.ps[0].$t : undefined;
    //If it's a type param, get the type argument
    if (typeof(a1t)==='string')a1t=targs && targs[a1t];
    //If the tuple type matches the param type, it's NOT a spread
    //(it's just a regular 1-param func which happens to receive a tuple)
    if (!a1t || is$(arg[0],a1t))return arg;
    var typecheck;
    if (a1t && targs && targs.Arguments$Callable) {
      typecheck=targs.Arguments$Callable;
      if (typecheck && typecheck.t && typecheck.t==='T' && typecheck.l
          && typecheck.l.length===1 && typecheck.l[0].seq) {
        //after all, it is NOT a spread
        return arg;
      }
    } else if (a1t && arg[0].$$targs$$) {
      if (arg[0].$$targs$$.First$Tuple) {
        typecheck={t:Tuple,a:arg[0].$$targs$$};
      } else if (arg[0].$$targs$$.t==='T') {
        typecheck=arg[0].$$targs$$;
      } else if (arg[0].$$targs$$.Element$Iterable) {
        typecheck={t:Iterable,a:arg[0].$$targs$$.Element$Iterable};
      }
    }
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
  return arg;
}

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
