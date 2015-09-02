function AppliedCallableConstructor$jsint(tipo,$$targs$$,that,myTargs) {
  if ($$targs$$.Type$AppliedCallableConstructor===undefined)$$targs$$.Type$AppliedCallableConstructor=$$targs$$.Type$Constructor;
  if ($$targs$$.Arguments$AppliedCallableConstructor===undefined)$$targs$$.Arguments$AppliedCallableConstructor=$$targs$$.Type$Constructor;
  $init$AppliedCallableConstructor$jsint();
  if (that===undefined){
    var mm=getrtmm$$(tipo);
    if (mm) {
      that=function AppliedCallableConstr1(){
        var _a=[].slice.call(arguments,0);
        _a=convert$params(mm,_a,that.$targs);
        if (that.$targs) {
          _a.push(that.$targs);
        }
        return tipo.apply(undefined,_a);
      }
      that.$crtmm$=mm;
      var dummy=new AppliedCallableConstructor$jsint.$$;
      that.$$=dummy.$$;
      that.getT$all=function(){return dummy.getT$all();}
      that.getT$name=function(){return dummy.getT$name();}
      that.equals=AppliedCallableConstructor$jsint.$$.prototype.equals;
      that.$_apply=AppliedCallableConstructor$jsint.$$.prototype.$_apply;
      that.namedApply=AppliedCallableConstructor$jsint.$$.prototype.namedApply;
      atr$(that,'declaration',function(){
          return AppliedCallableConstructor$jsint.$$.prototype.$prop$getDeclaration.get.call(this);
      },undefined,AppliedCallableConstructor$jsint.$$.prototype.$prop$getDeclaration.$crtmm$);
      atr$(that,'container',function(){
          return AppliedCallableConstructor$jsint.$$.prototype.$prop$getContainer.get.call(this);
      },undefined,AppliedCallableConstructor$jsint.$$.prototype.$prop$getContainer.$crtmm$);
      atr$(that,'parameterTypes',function(){
          return AppliedCallableConstructor$jsint.$$.prototype.$prop$getParameterTypes.get.call(this);
      },undefined,AppliedCallableConstructor$jsint.$$.prototype.$prop$getParameterTypes.$crtmm$);
      atr$(that,'string',function(){
          return AppliedCallableConstructor$jsint.$$.prototype.$prop$getString.get.call(this);
      },undefined,AppliedCallableConstructor$jsint.$$.prototype.$prop$getString.$crtmm$);
    }
  }
  $_Function$meta$model({Arguments$Function:$$targs$$.Arguments$AppliedCallableConstructor,
    Type$Function:$$targs$$.Type$AppliedCallableConstructor},that);
  CallableConstructor$meta$model({Type$CallableConstructor:$$targs$$.Type$AppliedCallableConstructor,Arguments$CallableConstructor:$$targs$$.Arguments$AppliedCallableConstructor},that);
  set_type_args(that,$$targs$$,AppliedCallableConstructor$jsint);
  that.$targs=myTargs;
  that.tipo=tipo;
  return that;
}
