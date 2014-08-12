function AppliedFunction(m,$$targs$$,o,mptypes) {
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
    return AppliedFunction(m,$$targs$$,x,mptypes);
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
  var dummy=new AppliedFunction.$$;
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
    return FunctionModel$meta$model.$$.prototype.$prop$getTypeArguments.get.call(f);
  },undefined,function(){return{mod:$CCMM$,$t:{t:Map,a:{Key$Map:{t:TypeParameter$meta$declaration},Item$Map:{t:Type$meta$model,a:{Type$Type:{t:Anything}}}}},$cont:AppliedFunction,an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Generic','$at','typeArguments']};});
  f.equals=function(oo){
    return is$(oo,{t:AppliedFunction}) && oo.tipo===m && oo.typeArguments.equals(this.typeArguments) && (o?o.equals(oo.$bound):oo.$bound===o);
  }
  atr$(f,'string',function(){
    return FunctionModel$meta$model.$$.prototype.$prop$getString.get.call(f);
  },undefined,function(){return{mod:$CCMM$,$t:{t:$_String},d:['$','Object','$at','string'],$cont:AppliedFunction};});
  atr$(f,'parameterTypes',function(){
    return FunctionModel$meta$model.$$.prototype.$prop$getParameterTypes.get.call(f);
  },undefined,FunctionModel$meta$model.$$.prototype.$prop$getParameterTypes.$crtmm$);
atr$(f,'declaration',function(){
  if (f._decl)return f._decl;
  var _m=typeof(mm.mod)==='function'?mm.mod():mm.mod;
  f._decl = OpenFunction(getModules$meta().find(_m['$mod-name'],_m['$mod-version']).findPackage(mm.d[0]), m);
  return f._decl;
},undefined,function(){return{mod:$CCMM$,$t:{t:FunctionDeclaration$meta$declaration},d:['ceylon.language.meta.model','FunctionModel','$at','declaration']};});
  atr$(f,'container',function(){
    if (o===undefined)return f.containingPackage;
    if (f.$parent===undefined) {
      f.$parent=type$meta(o,{Type$type:mm.$cont});
    }
    return f.$parent;
  },undefined,function(){return{mod:$CCMM$,$t:{t:'u',l:[{t:Null},{t:Type$meta$model,a:{Type$Type:{t:Anything}}}]},$cont:AppliedFunction,d:['ceylon.language.meta.model','Model','$at','container']};});
  f.$_apply=function(a){
    a=convert$params(mm,a);
    if (ttargs) {
      var _a=[];
      for (var i=0;i<a.size;i++)_a.push(a.$_get(i));
      _a.push(ttargs);
      a=_a;
    }
    return m.apply(o,a);
  }

f.namedApply=function(args) {
  if (mm.ps===undefined)throw InvocationException$meta$model("Applied function does not have metamodel parameter info for named args call");
  var mapped={};
  var iter=args.iterator();var a;while((a=iter.next())!==getFinished()) {
    mapped[a.key]=a.item===getNullArgument$meta$model()?null:a.item;
  }
  var ordered=[];
  for (var i=0; i<mm.ps.length; i++) {
    var p=mm.ps[i];
    var v=mapped[p.nm];
    if (v===undefined && p.def===undefined) {
      throw InvocationException$meta$model("Required argument " + p.nm + " missing in named-argument invocation");
    } else if (v!==undefined) {
        var t=p.$t;
        if(typeof(t)==='string'&&ttargs)t=ttargs[t];
        if (t&&!is$(v,t))throw IncompatibleTypeException$meta$model("Argument " + p.nm + "="+v+" is not of the expected type.");
    }
    delete mapped[p.nm];
    ordered.push(v);
  }
  if (Object.keys(mapped).length>0)throw new InvocationException$meta$model("No arguments with names " + Object.keys(mapped));
  if (ttargs) {
    ordered.push(ttargs);
  }
  return m.apply(o,ordered);
}

  return f;
}
AppliedFunction.$crtmm$=function(){return{mod:$CCMM$,d:['ceylon.language.meta.model','Function'],sts:{t:$_Function$meta$model,a:{Type$Function:'Type$Function',Arguments$Function:'Arguments$Function'}},an:function(){return [shared(),actual()];}};};
ex$.AppliedFunction$meta$model=AppliedFunction;
initTypeProto(AppliedFunction,'ceylon.language.meta.model::AppliedFunction',Basic,$_Function$meta$model);
