function AppliedConstructor$jsint(tipo,$$targs$$,that,myTargs) {
  if ($$targs$$.Type$AppliedConstructor===undefined)$$targs$$.Type$AppliedConstructor=$$targs$$.Type$Constructor;
  if ($$targs$$.Arguments$AppliedConstructor===undefined)$$targs$$.Arguments$AppliedConstructor=$$targs$$.Type$Constructor;
  $init$AppliedConstructor$jsint();
  if (that===undefined){
    var mm=getrtmm$$(tipo);
    if (mm) {
      that=function AppliedConstr1(){
        var _a=[].slice.call(arguments,0);
        _a=convert$params(mm,_a,that.$targs);
        if (that.$targs) {
          _a.push(that.$targs);
        }
        return tipo.apply(undefined,_a);
      }
      that.$crtmm$=mm;
      var dummy=new AppliedConstructor$jsint.$$;
      that.$$=dummy.$$;
      that.getT$all=function(){return dummy.getT$all();}
      that.getT$name=function(){return dummy.getT$name();}
      that.equals=AppliedConstructor$jsint.$$.prototype.equals;
      that.$_apply=AppliedConstructor$jsint.$$.prototype.$_apply;
      that.namedApply=AppliedConstructor$jsint.$$.prototype.namedApply;
      atr$(that,'declaration',function(){
          return AppliedConstructor$jsint.$$.prototype.$prop$getDeclaration.get.call(this);
      },undefined,AppliedConstructor$jsint.$$.prototype.$prop$getDeclaration.$crtmm$);
      atr$(that,'container',function(){
          return AppliedConstructor$jsint.$$.prototype.$prop$getContainer.get.call(this);
      },undefined,AppliedConstructor$jsint.$$.prototype.$prop$getContainer.$crtmm$);
      atr$(that,'parameterTypes',function(){
          return AppliedConstructor$jsint.$$.prototype.$prop$getParameterTypes.get.call(this);
      },undefined,AppliedConstructor$jsint.$$.prototype.$prop$getParameterTypes.$crtmm$);
      atr$(that,'string',function(){
          return AppliedConstructor$jsint.$$.prototype.$prop$getString.get.call(this);
      },undefined,AppliedConstructor$jsint.$$.prototype.$prop$getString.$crtmm$);
    }
  }
  Constructor$meta$model({Type$Constructor:$$targs$$.Type$AppliedConstructor,Arguments$Constructor:$$targs$$.Arguments$AppliedConstructor},that);
  set_type_args(that,$$targs$$,AppliedConstructor$jsint);
  that.$targs=myTargs;
  that.tipo=tipo;
  return that;
}
