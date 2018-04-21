function AppliedMemberClass$jsint(tipo,$$targs$$,that,myTargs){
  if (!$$targs$$.Type$AppliedMemberClass)$$targs$$.Type$AppliedMemberClass=$$targs$$.Type$MemberClass;
  if (!$$targs$$.Arguments$AppliedMemberClass)$$targs$$.Arguments$AppliedMemberClass=$$targs$$.Arguments$MemberClass;
  if (!$$targs$$.Container$AppliedMemberClass)$$targs$$.Container$AppliedMemberClass=$$targs$$.Container$MemberClass;
  $init$AppliedMemberClass$jsint();
  if (that===undefined) {
    var mm = getrtmm$$(tipo);
    if (mm && mm.$cont) {
      that=function AppliedMemClass1(x){
        var rv=tipo.bind(x);
        rv.$m$=tipo.$m$;
        var nt={t:tipo};
        if (x && x.$$targs$$) {
          nt.a={};
          for (var nta in x.$$targs$$)nt.a[nta]=x.$$targs$$[nta];
        }
        if (that.$targs) {
          if (!nt.a)nt.a={};
          for (var nta in that.$targs)nt.a[nta]=that.$targs[nta];
        }
        rv=AppliedClass$jsint(rv,{Type$AppliedClass:nt,Arguments$AppliedClass:{t:Sequential,a:{Element$Iterable:{t:Anything},Absent$Iterable:{t:Null}}}});//TODO generate metamodel for Arguments
        if (nt.a)rv.$targs=nt.a;
        rv.$bound=x;
        return rv;
      }
    } else {
      throw IncompatibleTypeException$meta$model("Invalid metamodel data for MemberClass");
    }
  }
  AppliedClass$jsint(tipo,{Type$AppliedClass:$$targs$$.Type$AppliedMemberClass,Arguments$AppliedClass:$$targs$$.Arguments$AppliedMemberClass},that);
  var dummy = new AppliedMemberClass$jsint.$$;
  that.$$=AppliedMemberClass$jsint.$$;
  that.getT$all=function(){return dummy.getT$all();};
  that.getT$name=function(){return dummy.getT$name();};
  var _proto=AppliedMemberClass$jsint.$$.prototype;

  that.$targs=myTargs;
  atr$(that,'extendedType',function(){
    return coiexttype$(that);
  },undefined,_proto.$prop$getExtendedType.$m$);
  atr$(that,'satisfiedTypes',function(){
    return coisattype$(that);
  },undefined,_proto.$prop$getExtendedType.$m$);
  atr$(that,'caseValues',function(){
    return coicase$(that);
  },undefined,_proto.$prop$getCaseValues.$m$);
  atr$(that,'declaration',function(){
    return coimoddcl$(that);
  },undefined,_proto.$prop$getDeclaration.$m$);
  atr$(that,'typeArguments',function(){
    return coitarg$(that);
  },undefined,_proto.$prop$getTypeArguments.$m$);
  atr$(that,'typeArgumentList',function(){
    return coitargl$(that);
  },undefined,_proto.$prop$getTypeArgumentList.$m$);
  atr$(that,'typeArgumentWithVariances',function(){
    return coitargv$(that);
  },undefined,_proto.$prop$getTypeArgumentWithVariances.$m$);
  atr$(that,'typeArgumentWithVarianceList',function(){
    return coitargvl$(that);
  },undefined,_proto.$prop$getTypeArgumentWithVarianceList.$m$);
  atr$(that,'defaultConstructor',function(){
    return _proto.$prop$getDefaultConstructor.get.call(that);
  },undefined,_proto.$prop$getDefaultConstructor.$m$);
  that.$_bind=_proto.$_bind;
  that.equals=_proto.equals;
  that.getAttribute=_proto.getAttribute;
  that.getAttributes=_proto.getAttributes;
  that.getClass=_proto.getClass;
  that.getClassOrInterface=_proto.getClassOrInterface;
  that.getClasses=_proto.getClasses;
  that.getDeclaredAttribute=_proto.getDeclaredAttribute;
  that.getDeclaredAttributes=_proto.getDeclaredAttributes;
  that.getDeclaredClass=_proto.getDeclaredClass;
  that.getDeclaredClassOrInterface=_proto.getDeclaredClassOrInterface;
  that.getDeclaredClasses=_proto.getDeclaredClasses;
  that.getDeclaredInterface=_proto.getDeclaredInterface;
  that.getDeclaredInterfaces=_proto.getDeclaredInterfaces;
  that.getDeclaredMethod=_proto.getDeclaredMethod;
  that.getDeclaredMethods=_proto.getDeclaredMethods;
  that.getInterface=_proto.getInterface;
  that.getInterfaces=_proto.getInterfaces;
  that.getConstructor=_proto.getConstructor;
  that.getCallableConstructors=_proto.getCallableConstructors;
  that.getDeclaredCallableConstructors=_proto.getDeclaredCallableConstructors;
  that.getDeclaredConstructor=_proto.getDeclaredConstructor;
  that.getDeclaredValueConstructors=_proto.getDeclaredValueConstructors;
  that.getValueConstructors=_proto.getValueConstructors;
  that.getMethod=_proto.getMethod;
  that.getMethods=_proto.getMethods;
  that.typeOf=_proto.typeOf;
  that.supertypeOf=_proto.supertypeOf;
  that.union=_proto.union;
  that.intersection=_proto.intersection;
  that.exactly=_proto.exactly;
  atr$(that,'string',function(){return coistr$(that); },undefined,_proto.$prop$getString.$m$);
  atr$(that,'hash',function(){return coihash$(that);},undefined,_proto.$prop$getHash.$m$);
  atr$(that,'container',function(){return coicont$(that); },undefined,_proto.$prop$getContainer.$m$);
  atr$(that,'declaringType',function(){return coicont$(that); },undefined,_proto.$prop$getDeclaringType.$m$);
  set_type_args(that,$$targs$$,AppliedMemberClass$jsint);
  MemberClass$meta$model(that.$$targs$$===undefined?$$targs$$:{Arguments$MemberClass:that.$$targs$$.Arguments$AppliedMemberClass,
                         Type$MemberClass:that.$$targs$$.Type$AppliedMemberClass,
                         Container$MemberClass:that.$$targs$$.Container$AppliedMemberClass},that);
  that.tipo=tipo;
  return that;
}
