function AppliedMethod$jsint(tipo,typeArgs,$a$,$$appliedMethod){
  if (!$a$.Type$AppliedMethod)$a$.Type$AppliedMethod=$a$.Type$Method;
  if (!$a$.Arguments$AppliedMethod)$a$.Arguments$AppliedMethod=$a$.Arguments$Method;
  if (!$a$.Container$AppliedMethod)$a$.Container$AppliedMethod=$a$.Container$Method;
  $i$AppliedMethod$jsint();
  var mm = getrtmm$$(tipo);
  if (mm.tp) {
    if (typeArgs===undefined || typeArgs.length<1) {
      throw TypeApplicationException$meta$model("Missing type arguments in call to FunctionDeclaration.apply");
    }
    var _ta={}; var i=0;
    for (var tp in mm.tp) {
      if (typeArgs[i]===undefined)
        throw TypeApplicationException$meta$model("Missing type argument for "+tp);
      var _tp = mm.tp[tp];
      var _t = typeArgs[i].tipo;
      _ta[tp]={t:_t};
      if (_tp.sts) {
        //Must satisty all specified types
        if (!extendsType(_ta[tp],{t:'i',l:_tp.sts})){
          throw TypeApplicationException$meta$model("Type argument for " + mm.d[mm.d.length-1] + "." + tp.substring(0,tp.indexOf('$')) + " violates type parameter constraints (satisfied)");
        }
      } else if (_tp.of) {
        //Must be one of these
        if (!extendsType(_ta[tp],{t:'u',l:_tp.of})){
          throw TypeApplicationException$meta$model("Type argument for " + mm.d[mm.d.length-1] + "." + tp.substring(0,tp.indexOf('$')) + " violates type parameter constraints (enumerated)");
        }
      }
      i++;
    }
  }
  if ($$appliedMethod===undefined){
    $$appliedMethod=function AppliedMtd1(x){
      return AppliedFunction$jsint(tipo,{Type$Function:$a$.Type$Method,Arguments$Function:$a$.Arguments$Method,
        Container$Function:$a$.Container$Method},x,typeArgs);
    }
    var dummy=new AppliedMethod$jsint.$$;
    $$appliedMethod.getT$all=function(){return dummy.getT$all();};
    $$appliedMethod.getT$name=function(){return dummy.getT$name();};
    $$appliedMethod.$$=AppliedMethod$jsint.$$;
  }
  if (_ta)$$appliedMethod.$targs=_ta;
  Method$meta$model($$appliedMethod.$a$===undefined?$a$:{Arguments$Method:$$appliedMethod.$a$.Arguments$Method,
    Type$Method:$$appliedMethod.$a$.Type$Method,Container$Method:$$appliedMethod.$a$.Container$Method},$$appliedMethod);
  set_type_args($$appliedMethod,$a$,AppliedMethod$jsint);
  $$appliedMethod.tipo=tipo;

  var _proto=AppliedMethod$jsint.$$.prototype;
  atr$($$appliedMethod,'declaration',function(){
    return _proto.$prop$getDeclaration.get.call($$appliedMethod);
  },undefined,_proto.$prop$getDeclaration.$m$);
  atr$($$appliedMethod,'type',function(){
    return _proto.$prop$getType.get.call($$appliedMethod);
  },undefined,_proto.$prop$getType.$m$);
  atr$($$appliedMethod,'typeArguments',function(){
    return funtypearg$($$appliedMethod);
  },undefined,_proto.$prop$getTypeArguments.$m$);
  atr$($$appliedMethod,'typeArgumentList',function(){
    return funtypeargl$($$appliedMethod);
  },undefined,_proto.$prop$getTypeArgumentList.$m$);
  atr$($$appliedMethod,'typeArgumentWithVariances',function(){
    return funtypeargv$($$appliedMethod);
  },undefined,_proto.$prop$getTypeArgumentWithVariances.$m$);
  atr$($$appliedMethod,'typeArgumentWithVarianceList',function(){
    return funtypeargvl$($$appliedMethod);
  },undefined,_proto.$prop$getTypeArgumentWithVarianceList.$m$);
  atr$($$appliedMethod,'parameterTypes',function(){
    return funparamtypes($$appliedMethod);
  },undefined,_proto.$prop$getParameterTypes.$m$);
  $$appliedMethod.equals=_proto.equals;
  $$appliedMethod.$_bind=_proto.$_bind;
  atr$($$appliedMethod,'string',function(){
    return funmodstr$($$appliedMethod);
  },undefined,_proto.$prop$getString.$m$);
  atr$($$appliedMethod,'hash',function(){
    return _proto.$prop$getHash.get.call($$appliedMethod);
  },undefined,_proto.$prop$getHash.$m$);
  atr$($$appliedMethod,'declaringType',function(){
    return _proto.$prop$getDeclaringType.get.call($$appliedMethod);
  },undefined,_proto.$prop$getDeclaringType.$m$);
  atr$($$appliedMethod,'container',function(){
    return _proto.$prop$getContainer.get.call($$appliedMethod);
  },undefined,_proto.$prop$getContainer.$m$);
  return $$appliedMethod;
}
