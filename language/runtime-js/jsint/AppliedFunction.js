function AppliedFunction$jsint(m,$$targs$$,o,mptypes) {
  if (!$$targs$$.Type$AppliedFunction)$$targs$$.Type$AppliedFunction=$$targs$$.Type$Function;
  if (!$$targs$$.Arguments$AppliedFunction)$$targs$$.Arguments$AppliedFunction=$$targs$$.Arguments$Function;
  var mm=getrtmm$$(m);
  var ttargs;
  if (mm.tp) {
    if (!mptypes || mptypes.size<1)throw TypeApplicationException$meta$model("Missing type arguments for AppliedFunction");
    var i=0;ttargs={};
    for (var tp in mm.tp) {
      var _ta=mptypes.$_get?mptypes.$_get(i):mptypes[i];
      if(_ta&&_ta.tipo)ttargs[tp]={t:_ta.tipo};
      else if (_ta) console.log("TODO assign type arg " + _ta + " to " + tp);
      else if (mptypes[tp])ttargs[tp]=mptypes[tp];
      else throw new Error("No more type arguments in AppliedFunction!");
      i++;
    }
  }
  var f = o===undefined&&mm.$cont?function AppliedFun1(x){
    return AppliedFunction$jsint(m,$$targs$$,x,mptypes);
  }:function AppliedFun2(){
    var _fu=(o&&o[mm.d[mm.d.length-1]])||m;//Get the object's method if possible
    if (mm.tp) {
      var _a=[];
      for (var i=0;i<arguments.length;i++)_a.push(arguments[i]);
      _a.push(ttargs);
      return _fu.apply(o,_a);
    }
    return _fu.apply(o,arguments);
  }
  f.$crtmm$={mod:$CCMM$,d:['ceylon.language.meta.model','Function'],$t:mm.$t,ps:mm.ps,an:mm.an};
  var dummy=new AppliedFunction$jsint.$$;
  f.$$=AppliedFunction$jsint.$$;
  f.getT$all=function(){return dummy.getT$all();}
  f.getT$name=function(){return dummy.getT$name();}
  if ($$targs$$===undefined) {
    throw TypeApplicationException$meta$model("Missing type arguments for AppliedFunction");
  }
  $_Function$meta$model($$targs$$,f);
  f.tipo=m;
  f.$targs=ttargs;
  if (o)f.$bound=o;
  var _proto=AppliedFunction$jsint.$$.prototype;
  atr$(f,'typeArguments',function(){
    return funtypearg$(f);
  },undefined,_proto.$prop$getTypeArguments.$crtmm$);
  atr$(f,'typeArgumentList',function(){
    return funtypeargl$(f);
  },undefined,_proto.$prop$getTypeArgumentList.$crtmm$);
  f.equals=_proto.equals;
  atr$(f,'string',function(){
    return _proto.$prop$getString.get.call(f);
  },undefined,_proto.$prop$getString.$crtmm$);
  atr$(f,'hash',function(){
    return _proto.$prop$getHash.get.call(f);
  },undefined,_proto.$prop$getHash.$crtmm$);
  atr$(f,'parameterTypes',function(){
    return _proto.$prop$getParameterTypes.get.call(f);
  },undefined,_proto.$prop$getParameterTypes.$crtmm$);
  atr$(f,'declaration',function(){
    return _proto.$prop$getDeclaration.get.call(f);
  },undefined,_proto.$prop$getDeclaration.$crtmm$);
  atr$(f,'container',function(){
    return _proto.$prop$getContainer.get.call(f);
  },undefined,_proto.$prop$getContainer.$crtmm$);
  atr$(f,'type',function(){
    return _proto.$prop$getType.get.call(f);
  },undefined,_proto.$prop$getType.$crtmm$);
  f.$_apply=_proto.$_apply;
  f.namedApply=_proto.namedApply;
  return f;
}
