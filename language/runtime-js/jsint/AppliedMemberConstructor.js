function AppliedMemberConstructor$jsint(tipo,$$targs$$,that,myTargs) {
  if ($$targs$$.Type$AppliedMemberConstructor===undefined)$$targs$$.Type$AppliedMemberConstructor=$$targs$$.Type$MemberClassConstructor;
  if ($$targs$$.Container$AppliedMemberConstructor===undefined)$$targs$$.Container$AppliedMemberConstructor=$$targs$$.Type$MemberClassConstructor;
  if ($$targs$$.Arguments$AppliedMemberConstructor===undefined)$$targs$$.Arguments$AppliedMemberConstructor=$$targs$$.Type$MemberClassConstructor;
  $init$AppliedMemberConstructor$jsint();
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
      rv=AppliedConstructor$jsint(rv,{Type$AppliedConstructor:nt,Arguments$AppliedConstructor:{t:Sequential,a:{Element$Iterable:{t:Anything},Absent$Iterable:{t:Null}}}});//TODO generate metamodel for Arguments
      if (nt.a)rv.$targs=nt.a;
      rv.$bound=x;
      return rv;
    }
    var dummy=new AppliedMemberConstructor$jsint.$$;
    that.$$=AppliedMemberConstructor$jsint.$$;
    that.getT$all=function(){return dummy.getT$all();};
    that.getT$name=function(){return dummy.getT$name();};
    that.equals=AppliedMemberConstructor$jsint.$$.prototype.equals;
    that.$_bind=AppliedMemberConstructor$jsint.$$.prototype.$_bind;
    atr$(that,'declaration',function(){
        return AppliedMemberConstructor$jsint.$$.prototype.$prop$getDeclaration.get.call(this);
    },undefined,AppliedMemberConstructor$jsint.$$.prototype.$prop$getDeclaration.$crtmm$);
    atr$(that,'container',function(){
        return AppliedMemberConstructor$jsint.$$.prototype.$prop$getContainer.get.call(this);
    },undefined,AppliedMemberConstructor$jsint.$$.prototype.$prop$getContainer.$crtmm$);
    atr$(that,'parameterTypes',function(){
        return AppliedMemberConstructor$jsint.$$.prototype.$prop$getParameterTypes.get.call(this);
    },undefined,AppliedMemberConstructor$jsint.$$.prototype.$prop$getParameterTypes.$crtmm$);
    atr$(that,'string',function(){
        return AppliedMemberConstructor$jsint.$$.prototype.$prop$getString.get.call(this);
    },undefined,AppliedMemberConstructor$jsint.$$.prototype.$prop$getString.$crtmm$);
  }
  MemberClassConstructor$meta$model({Type$MemberClassConstructor:$$targs$$.Type$AppliedMemberConstructor,
    Arguments$MemberClassConstructor:$$targs$$.Arguments$AppliedMemberConstructor,
    Container$MemberClassConstructor:$$targs$$.Container$AppliedMemberConstructor},that);
  set_type_args(that,$$targs$$,AppliedMemberConstructor$jsint);
  that.$targs=myTargs;
  that.tipo=tipo;
  return that;
}
