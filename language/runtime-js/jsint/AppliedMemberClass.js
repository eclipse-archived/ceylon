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
        rv.$crtmm$=tipo.$crtmm$;
        var nt={t:tipo};
        if (x.$$targs$$) {
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
  that.equals=AppliedMemberClass$jsint.$$.prototype.equals;

  that.$targs=myTargs;
  atr$(that,'parameterTypes',function(){
    return clsparamtypes(that);
  },undefined,AppliedMemberClass$jsint.$$.prototype.$prop$getParameterTypes.$crtmm$);
  atr$(that,'extendedType',function(){
    return coiexttype$(that);
  },undefined,AppliedMemberClass$jsint.$$.prototype.$prop$getExtendedType.$crtmm$);
  atr$(that,'satisfiedTypes',function(){
    return coisattype$(that);
  },undefined,AppliedMemberClass$jsint.$$.prototype.$prop$getExtendedType.$crtmm$);
  atr$(that,'caseValues',function(){
    return coicase$(that);
  },undefined,AppliedMemberClass$jsint.$$.prototype.$prop$getCaseValues.$crtmm$);
  atr$(that,'declaration',function(){
    return coimoddcl$(that);
  },undefined,AppliedMemberClass$jsint.$$.prototype.$prop$getDeclaration.$crtmm$);
  that.$_bind=AppliedMemberClass$jsint.$$.prototype.$_bind;
  that.equals=AppliedMemberClass$jsint.$$.prototype.equals;
  that.getAttribute=AppliedMemberClass$jsint.$$.prototype.getAttribute;
  that.getAttributes=AppliedMemberClass$jsint.$$.prototype.getAttributes;
  that.getClass=AppliedMemberClass$jsint.$$.prototype.getClass;
  that.getClassOrInterface=AppliedMemberClass$jsint.$$.prototype.getClassOrInterface;
  that.getClasses=AppliedMemberClass$jsint.$$.prototype.getClasses;
  that.getConstructor=AppliedMemberClass$jsint.$$.prototype.getConstructor;
  that.getDeclaredAttribute=AppliedMemberClass$jsint.$$.prototype.getDeclaredAttribute;
  that.getDeclaredAttributes=AppliedMemberClass$jsint.$$.prototype.getDeclaredAttributes;
  that.getDeclaredClass=AppliedMemberClass$jsint.$$.prototype.getDeclaredClass;
  that.getDeclaredClassOrInterface=AppliedMemberClass$jsint.$$.prototype.getDeclaredClassOrInterface;
  that.getDeclaredClasses=AppliedMemberClass$jsint.$$.prototype.getDeclaredClasses;
  that.getDeclaredInterface=AppliedMemberClass$jsint.$$.prototype.getDeclaredInterface;
  that.getDeclaredInterfaces=AppliedMemberClass$jsint.$$.prototype.getDeclaredInterfaces;
  that.getDeclaredMethod=AppliedMemberClass$jsint.$$.prototype.getDeclaredMethod;
  that.getDeclaredMethods=AppliedMemberClass$jsint.$$.prototype.getDeclaredMethods;
  that.getInterface=AppliedMemberClass$jsint.$$.prototype.getInterface;
  that.getInterfaces=AppliedMemberClass$jsint.$$.prototype.getInterfaces;
  that.getMethod=AppliedMemberClass$jsint.$$.prototype.getMethod;
  that.getMethods=AppliedMemberClass$jsint.$$.prototype.getMethods;
  that.typeOf=AppliedMemberClass$jsint.$$.prototype.typeOf;
  that.supertypeOf=AppliedMemberClass$jsint.$$.prototype.supertypeOf;
  that.union=AppliedMemberClass$jsint.$$.prototype.union;
  that.intersection=AppliedMemberClass$jsint.$$.prototype.intersection;
  that.exactly=AppliedMemberClass$jsint.$$.prototype.exactly;
  atr$(that,'string',function(){return coistr$(that); },undefined,AppliedMemberClass$jsint.$$.prototype.$prop$getString.$crtmm$);
  atr$(that,'hash',function(){return coihash$(that);},undefined,AppliedMemberClass$jsint.$$.prototype.$prop$getHash.$crtmm$);
  atr$(that,'container',function(){return coicont$(that); },undefined,AppliedMemberClass$jsint.$$.prototype.$prop$getContainer.$crtmm$);
  MemberClass$meta$model(that.$$targs$$===undefined?$$targs$$:{Arguments$MemberClass:that.$$targs$$.Arguments$AppliedMemberClass,
                         Type$MemberClass:that.$$targs$$.Type$AppliedMemberClass,
                         Container$MemberClass:that.$$targs$$.Container$AppliedMemberClass},that);
  set_type_args(that,$$targs$$,AppliedMemberClass$jsint);
  that.tipo=tipo;
  return that;
}
