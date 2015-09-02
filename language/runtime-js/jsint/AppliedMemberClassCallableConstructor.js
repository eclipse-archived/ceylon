function AppliedMemberClassCallableConstructor$jsint(tipo,$$targs$$,that,myTargs) {
  if ($$targs$$.Type$AppliedMemberClassCallableConstructor===undefined)$$targs$$.Type$AppliedMemberClassCallableConstructor=$$targs$$.Type$MemberClassConstructor;
  if ($$targs$$.Container$AppliedMemberClassCallableConstructor===undefined)$$targs$$.Container$AppliedMemberClassCallableConstructor=$$targs$$.Type$MemberClassConstructor;
  if ($$targs$$.Arguments$AppliedMemberClassCallableConstructor===undefined)$$targs$$.Arguments$AppliedMemberClassCallableConstructor=$$targs$$.Type$MemberClassConstructor;
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
        return AppliedMemberClassCallableConstructor$jsint.$$.prototype.$prop$getDeclaration.get.call(this);
    },undefined,AppliedMemberClassCallableConstructor$jsint.$$.prototype.$prop$getDeclaration.$crtmm$);
    atr$(that,'container',function(){
        return AppliedMemberClassCallableConstructor$jsint.$$.prototype.$prop$getContainer.get.call(this);
    },undefined,AppliedMemberClassCallableConstructor$jsint.$$.prototype.$prop$getContainer.$crtmm$);
    atr$(that,'parameterTypes',function(){
        return AppliedMemberClassCallableConstructor$jsint.$$.prototype.$prop$getParameterTypes.get.call(this);
    },undefined,AppliedMemberClassCallableConstructor$jsint.$$.prototype.$prop$getParameterTypes.$crtmm$);
    atr$(that,'string',function(){
        return AppliedMemberClassCallableConstructor$jsint.$$.prototype.$prop$getString.get.call(this);
    },undefined,AppliedMemberClassCallableConstructor$jsint.$$.prototype.$prop$getString.$crtmm$);
  }
  MemberClassConstructor$meta$model({Type$MemberClassConstructor:$$targs$$.Type$AppliedMemberClassCallableConstructor,
    Arguments$MemberClassConstructor:$$targs$$.Arguments$AppliedMemberClassCallableConstructor,
    Container$MemberClassConstructor:$$targs$$.Container$AppliedMemberClassCallableConstructor},that);
  set_type_args(that,$$targs$$,AppliedMemberClassCallableConstructor$jsint);
  that.$targs=myTargs;
  that.tipo=tipo;
  return that;
}
