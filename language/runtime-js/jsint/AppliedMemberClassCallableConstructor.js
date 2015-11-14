function AppliedMemberClassCallableConstructor$jsint(tipo,$$targs$$,that,myTargs) {
  if ($$targs$$.Type$AppliedMemberClassCallableConstructor===undefined)$$targs$$.Type$AppliedMemberClassCallableConstructor=$$targs$$.Type$MemberClassCallableConstructor;
  if ($$targs$$.Container$AppliedMemberClassCallableConstructor===undefined)$$targs$$.Container$AppliedMemberClassCallableConstructor=$$targs$$.Container$MemberClassCallableConstructor;
  if ($$targs$$.Arguments$AppliedMemberClassCallableConstructor===undefined)$$targs$$.Arguments$AppliedMemberClassCallableConstructor=$$targs$$.Arguments$MemberClassCallableConstructor;
  $init$AppliedMemberClassCallableConstructor$jsint();
  if (that===undefined){
    var mm = getrtmm$$(tipo);
    that=function AppliedMemConstr1(x){
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
      rv=AppliedCallableConstructor$jsint(rv,{Type$AppliedCallableConstructor:nt,Arguments$AppliedCallableConstructor:{t:Sequential,a:{Element$Iterable:{t:Anything},Absent$Iterable:{t:Null}}}});//TODO generate metamodel for Arguments
      if (nt.a)rv.$targs=nt.a;
      rv.$bound=x;
      return rv;
    }
    var dummy=new AppliedMemberClassCallableConstructor$jsint.$$;
    that.$$=AppliedMemberClassCallableConstructor$jsint.$$;
    that.getT$all=function(){return dummy.getT$all();};
    that.getT$name=function(){return dummy.getT$name();};
    that.equals=AppliedMemberClassCallableConstructor$jsint.$$.prototype.equals;
    that.$_bind=AppliedMemberClassCallableConstructor$jsint.$$.prototype.$_bind;
    atr$(that,'declaration',function(){
        return AppliedMemberClassCallableConstructor$jsint.$$.prototype.$prop$getDeclaration.get.call(that);
    },undefined,AppliedMemberClassCallableConstructor$jsint.$$.prototype.$prop$getDeclaration.$crtmm$);
    atr$(that,'container',function(){
        return AppliedMemberClassCallableConstructor$jsint.$$.prototype.$prop$getContainer.get.call(that);
    },undefined,AppliedMemberClassCallableConstructor$jsint.$$.prototype.$prop$getContainer.$crtmm$);
    atr$(that,'type',function(){
        return AppliedMemberClassCallableConstructor$jsint.$$.prototype.$prop$getType.get.call(that);
    },undefined,AppliedMemberClassCallableConstructor$jsint.$$.prototype.$prop$getType.$crtmm$);
    atr$(that,'typeArguments',function(){
        return AppliedMemberClassCallableConstructor$jsint.$$.prototype.$prop$getTypeArguments.get.call(that);
    },undefined,AppliedMemberClassCallableConstructor$jsint.$$.prototype.$prop$getTypeArguments.$crtmm$);
    atr$(that,'typeArgumentList',function(){
        return AppliedMemberClassCallableConstructor$jsint.$$.prototype.$prop$getTypeArgumentList.get.call(that);
    },undefined,AppliedMemberClassCallableConstructor$jsint.$$.prototype.$prop$getTypeArgumentList.$crtmm$);
    atr$(that,'typeArgumentWithVariances',function(){
        return AppliedMemberClassCallableConstructor$jsint.$$.prototype.$prop$getTypeArgumentWithVariances.get.call(that);
    },undefined,AppliedMemberClassCallableConstructor$jsint.$$.prototype.$prop$getTypeArgumentWithVariances.$crtmm$);
    atr$(that,'typeArgumentWithVarianceList',function(){
        return AppliedMemberClassCallableConstructor$jsint.$$.prototype.$prop$getTypeArgumentWithVarianceList.get.call(that);
    },undefined,AppliedMemberClassCallableConstructor$jsint.$$.prototype.$prop$getTypeArgumentWithVarianceList.$crtmm$);
    atr$(that,'parameterTypes',function(){
        return AppliedMemberClassCallableConstructor$jsint.$$.prototype.$prop$getParameterTypes.get.call(that);
    },undefined,AppliedMemberClassCallableConstructor$jsint.$$.prototype.$prop$getParameterTypes.$crtmm$);
    atr$(that,'string',function(){
        return AppliedMemberClassCallableConstructor$jsint.$$.prototype.$prop$getString.get.call(that);
    },undefined,AppliedMemberClassCallableConstructor$jsint.$$.prototype.$prop$getString.$crtmm$);
  }
  MemberClassCallableConstructor$meta$model({Type$MemberClassCallableConstructor:$$targs$$.Type$AppliedMemberClassCallableConstructor,
    Arguments$MemberClassCallableConstructor:$$targs$$.Arguments$AppliedMemberClassCallableConstructor,
    Container$MemberClassCallableConstructor:$$targs$$.Container$AppliedMemberClassCallableConstructor},that);
  set_type_args(that,$$targs$$,AppliedMemberClassCallableConstructor$jsint);
  that.$targs=myTargs;
  that.tipo=tipo;
  return that;
}
