function f2$(f,parms,targs) {
  //Do not wrap another JsCallable
  if (f.jsc$)return f;
  if (f.getT$all === undefined) {
    f.getT$all=Callable.getT$all;
  }
  var set_meta = getrtmm$$(f) === undefined;
  if (set_meta) {
    f.$crtmm$={ps:parms||[],mod:$CCMM$,d:['$','Callable']};
  }
  if (targs !== undefined && f.$$targs$$ === undefined) {
    f.$$targs$$=targs;
    if (set_meta) {
      f.$crtmm$.$t=targs.Return$Callable;
    }
  }
  if (f.$flattened$||f.$unflattened$)return f;
  var fun=function c2(){
    return f.apply(undefined,arguments);
  };
  if (targs) {
    fun.$$targs$$=targs;
  }
  fun.$crtmm$=f.$crtmm$;
  fun.getT$all=f.getT$all;
  fun.jsc$=f;
  fun.equals=function(o) {
    return false;//o===f || o===f;
  }
  return fun;
}
initExistingTypeProto(f2$, Function, 'ceylon.language::JsCallable', $init$Callable());

function nop$(){return null;}

function f3$(o,f,targs) {
  if (o===null || o===undefined) return nop$;
  if (f.jsc$)f=f.jsc$;
  var fun;
  if (targs) {
    fun=function c5() {
      var a=[].slice.call(arguments,0);
      a.push(targs);
      return f.apply(o, a);
    };
  } else {
    fun=function c6() {
      return f.apply(o, arguments);
    };
  }
  fun.c2$=f;
  fun.$$targs$$=targs;
  fun.equals=function(x){
    return false;//f===x || fun===x;
  }
  fun.$crtmm$=f.$crtmm$||Callable.$crtmm$;
  return fun;
}
f3$.$crtmm$=function(){return{sts:[{t:Callable,a:{Return$Callable:'Return$Callable',Arguments$Callable:'Arguments$Callable'}}],
  tp:{Return$Callable:{dv:'out'}, Arguments$Callable:{dv:'in'}},pa:1,mod:$CCMM$,d:['$','Callable']};}

function spread$(a,f,targs,noInvoke) {
  if (a.length===undefined||a.getT$all)console.log("NO ES ARREGLO!",a);
  var arg=noInvoke?[].slice.call(a,0):a;
  //if we get only 1 arg and it's a tuple...
  var spridx=arg.length-1;
  function $checkSpreadArg$() {
    if (arg.length>0 && is$(arg[spridx],{t:Tuple})) {
      return true;
    }
    //We can get a Sequence, not just a Tuple
    if (arg.length>0 && is$(arg[spridx],{t:Sequence})) {
      //In this case we need to check the size of the sequence against the size of the remaining arguments
      //And check that the remaining parameter types match the sequence's element type
      var elemento=arg[spridx].$$targs$$.Element$Sequence;
      var mm=getrtmm$$(f);
      if (mm && mm.ps && mm.ps.length==arg.length-spridx+arg[spridx].size-1) {
        //If the argument is already of the parameter's type, forget about it
        var spreadParmType=mm.ps[spridx].$t;
        if (typeof(spreadParmType)==='string'&&targs[spreadParmType])spreadParmType=targs[spreadParmType];
        if (is$(arg[spridx],spreadParmType))return false;
        //Would it be faster to check against the sequence's actual element instead?
        for (var i=spridx;i<arg.length;i++) {
          if (!extendsType(elemento,mm.ps[i]))return false;
        }
        return true;
      }
    }
    return false;
  }
  if ($checkSpreadArg$()) {
    var tuple0=arg[spridx];
    //Possible spread, check the metamodel
    var mm=getrtmm$$(f);
    var params = mm && mm.ps || [];
    spridx=params.length-1
    if (params[spridx].seq>0) {
      //Last parameter is variadic
      if (params.length===arg.length) {
        //Simple, direct spread
        var tcheck=params[spridx].$t;
        if ((tcheck.t===Sequential||tcheck.t===Sequence)&&is$(tuple0,tcheck)) {
          return arg;
        } else if (is$(tuple0,{t:Sequence,a:{Element$Sequence:tcheck}})){
          return arg;
        }
      } else if (params.length>arg.length && params.length<=tuple0.size) {
        //Map tuple elements to parameters, leave remaining for the sequenced param
        var sparg=new Array(params.length);
        for (var i=0;i<spridx;i++) {
          sparg[i]=tuple0.getFromFirst(i);
        }
        sparg[spridx]=tuple0.spanFrom(spridx);
        return sparg;
      }
    }
    if (params.length===tuple0.size) {
      //Simple mapping
      var all=[];
      for (var i=0; i<params.length;i++) {
        all.push(tuple0.getFromFirst(i));
      }
      return all;
    }
  }
  if (noInvoke) {
    return arg;
  }
  if (targs)arg.push(targs);
  return f.apply(undefined,arg);
}
ex$.spread$=spread$;

function spread$2(f) {
  var args=[].slice.call(arguments,1);
  var mm=getrtmm$$(f);
  if (mm) {
    var params=mm.ps||[];
    var targs=args.length===params.length+1?args[args.length-1]:undefined;
    if (args.length>params.length && !targs) {
      //WTF just call this
      console.log("SPREAD with",args.length,"and just",params.length,"parameters");
      return f.apply(undefined,args);
    }
    var a=[];
    var j=0;
    if (params.length===1 && args.length===(targs?2:1) && is$(args[0],{t:Sequential}) && args[0].size===1) {
      var pt=params[0].$t;
      if (typeof(pt)==='string') {
        if (f.$$targs$$ && f.$$targs$$[pt])pt=f.$$targs$$[pt];
        else if (targs && targs[pt])pt=targs[pt];
        else console.log("SPREAD can't resolve",pt,"with",targs?Object.keys(targs):"no type arguments");
      }
      if (is$(args[0].first,pt)) {
        a.push(args[0].first);
        params=[];
      }
    }
    for (var i=0;i<params.length;i++) {
      var e=args[j];
      var pt=params[i].$t;
      if (typeof(pt)==='string') {
        if (f.$$targs$$ && f.$$targs$$[pt])pt=f.$$targs$$[pt];
        else if (targs && targs[pt])pt=targs[pt];
        else console.log("SPREAD can't resolve",pt,"with",targs?Object.keys(targs):"no type arguments");
      }
      if (params[i].seq>0) {
        a.push(e);
        j++;
      } else if (is$(e,pt) || (pt.t===Callable && is$(e,{t:Callable,a:{Return$Callable:pt.a.Return$Callable}}))) {
        a.push(e);
        j++;
      } else if (is$(e.first,pt)) {
        a.push(e.first);
        args[j]=e.rest;
      } else if (e.size<=i && params[i].def>0) {
        a.push(undefined);
      } else {
        console.trace("SPREAD WTF2",e&&e.string,"vs",params[i]);
      }
    }
    if (targs)a.push(targs);
    return f.apply(undefined,a);
  }
  return f.apply(undefined,args);
}
ex$.spread$2=spread$2;

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
    return $arr$sa$(rval,$et||{t:Anything});
  };
}
JsCallableList.$crtmm$={tp:{Return$Callable:{dv:'out'}, Arguments$Callable:{dv:'in'}},pa:1,mod:$CCMM$,d:['$','Callable']};
function mkseq$(t,seq) {
  if (seq)t.seq=seq;
  return t;
}
ex$.JsCallableList=JsCallableList;
ex$.mkseq$=mkseq$;
ex$.f2$=f2$;
ex$.f3$=f3$;
