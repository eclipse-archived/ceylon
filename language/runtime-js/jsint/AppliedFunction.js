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
  var f = o===undefined&&mm.$cont?function(x){
    return AppliedFunction$jsint(m,$$targs$$,x,mptypes);
  }:function(){
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
  atr$(f,'typeArguments',function(){
    return AppliedFunction$jsint.$$.prototype.$prop$getTypeArguments.get.call(f);
  },undefined,AppliedFunction$jsint.$$.prototype.$prop$getTypeArguments.$crtmm$);
  f.equals=AppliedFunction$jsint.$$.prototype.equals;
  atr$(f,'string',function(){
    return AppliedFunction$jsint.$$.prototype.$prop$getString.get.call(f);
  },undefined,AppliedFunction$jsint.$$.prototype.$prop$getString.$crtmm$);
  atr$(f,'hash',function(){
    return AppliedFunction$jsint.$$.prototype.$prop$getHash.get.call(f);
  },undefined,AppliedFunction$jsint.$$.prototype.$prop$getHash.$crtmm$);
  atr$(f,'parameterTypes',function(){
    return AppliedFunction$jsint.$$.prototype.$prop$getParameterTypes.get.call(f);
  },undefined,AppliedFunction$jsint.$$.prototype.$prop$getParameterTypes.$crtmm$);
  atr$(f,'declaration',function(){
    return AppliedFunction$jsint.$$.prototype.$prop$getDeclaration.get.call(f);
  },undefined,AppliedFunction$jsint.$$.prototype.$prop$getDeclaration.$crtmm$);
  atr$(f,'container',function(){
    return AppliedFunction$jsint.$$.prototype.$prop$getContainer.get.call(f);
  },undefined,AppliedFunction$jsint.$$.prototype.$prop$getContainer.$crtmm$);
  f.$_apply=AppliedFunction$jsint.$$.prototype.$_apply;
  f.namedApply=AppliedFunction$jsint.$$.prototype.namedApply;
  return f;
}
