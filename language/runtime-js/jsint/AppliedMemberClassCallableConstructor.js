function AppliedMemberClassCallableConstructor$jsint(tipo,$a$,that,myTargs) {
  if ($a$.Type$AppliedMemberClassCallableConstructor===undefined)$a$.Type$AppliedMemberClassCallableConstructor=$a$.Type$MemberClassCallableConstructor;
  if ($a$.Container$AppliedMemberClassCallableConstructor===undefined)$a$.Container$AppliedMemberClassCallableConstructor=$a$.Container$MemberClassCallableConstructor;
  if ($a$.Arguments$AppliedMemberClassCallableConstructor===undefined)$a$.Arguments$AppliedMemberClassCallableConstructor=$a$.Arguments$MemberClassCallableConstructor;
  $i$AppliedMemberClassCallableConstructor$jsint();
  if (that===undefined){
    var mm = getrtmm$$(tipo);
    that=function AppliedMemConstr1(x){
      var rv=tipo.bind(x);
      rv.$m$=tipo.$m$;
      var nt={t:tipo};
      if (x.$a$) {
        nt.a={};
        for (var nta in x.$a$)nt.a[nta]=x.$a$[nta];
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
    },undefined,AppliedMemberClassCallableConstructor$jsint.$$.prototype.$prop$getDeclaration.$m$);
    atr$(that,'container',function(){
        return AppliedMemberClassCallableConstructor$jsint.$$.prototype.$prop$getContainer.get.call(that);
    },undefined,AppliedMemberClassCallableConstructor$jsint.$$.prototype.$prop$getContainer.$m$);
    atr$(that,'type',function(){
        return AppliedMemberClassCallableConstructor$jsint.$$.prototype.$prop$getType.get.call(that);
    },undefined,AppliedMemberClassCallableConstructor$jsint.$$.prototype.$prop$getType.$m$);
    atr$(that,'typeArguments',function(){
        return AppliedMemberClassCallableConstructor$jsint.$$.prototype.$prop$getTypeArguments.get.call(that);
    },undefined,AppliedMemberClassCallableConstructor$jsint.$$.prototype.$prop$getTypeArguments.$m$);
    atr$(that,'typeArgumentList',function(){
        return AppliedMemberClassCallableConstructor$jsint.$$.prototype.$prop$getTypeArgumentList.get.call(that);
    },undefined,AppliedMemberClassCallableConstructor$jsint.$$.prototype.$prop$getTypeArgumentList.$m$);
    atr$(that,'typeArgumentWithVariances',function(){
        return AppliedMemberClassCallableConstructor$jsint.$$.prototype.$prop$getTypeArgumentWithVariances.get.call(that);
    },undefined,AppliedMemberClassCallableConstructor$jsint.$$.prototype.$prop$getTypeArgumentWithVariances.$m$);
    atr$(that,'typeArgumentWithVarianceList',function(){
        return AppliedMemberClassCallableConstructor$jsint.$$.prototype.$prop$getTypeArgumentWithVarianceList.get.call(that);
    },undefined,AppliedMemberClassCallableConstructor$jsint.$$.prototype.$prop$getTypeArgumentWithVarianceList.$m$);
    atr$(that,'parameterTypes',function(){
        return AppliedMemberClassCallableConstructor$jsint.$$.prototype.$prop$getParameterTypes.get.call(that);
    },undefined,AppliedMemberClassCallableConstructor$jsint.$$.prototype.$prop$getParameterTypes.$m$);
    atr$(that,'string',function(){
        return AppliedMemberClassCallableConstructor$jsint.$$.prototype.$prop$getString.get.call(that);
    },undefined,AppliedMemberClassCallableConstructor$jsint.$$.prototype.$prop$getString.$m$);
    atr$(that,'hash',function(){
        return AppliedMemberClassCallableConstructor$jsint.$$.prototype.$prop$getHash.get.call(that);
    },undefined,AppliedMemberClassCallableConstructor$jsint.$$.prototype.$prop$getHash.$m$);
  }
  MemberClassCallableConstructor$meta$model({Type$MemberClassCallableConstructor:$a$.Type$AppliedMemberClassCallableConstructor,
    Arguments$MemberClassCallableConstructor:$a$.Arguments$AppliedMemberClassCallableConstructor,
    Container$MemberClassCallableConstructor:$a$.Container$AppliedMemberClassCallableConstructor},that);
  set_type_args(that,$a$,AppliedMemberClassCallableConstructor$jsint);
  that.$targs=myTargs;
  that.tipo=tipo;
  return that;
}
