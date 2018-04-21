function AppliedCallableConstructor$jsint(tipo,$a$,that,myTargs) {
  if ($a$.Type$AppliedCallableConstructor===undefined)$a$.Type$AppliedCallableConstructor=$a$.Type$CallableConstructor;
  if ($a$.Arguments$AppliedCallableConstructor===undefined)$a$.Arguments$AppliedCallableConstructor=$a$.Type$CallableConstructor;
  $i$AppliedCallableConstructor$jsint();
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
      that.$m$=mm;
      var dummy=new AppliedCallableConstructor$jsint.$$;
      that.$$=dummy.$$;
      that.getT$all=function(){return dummy.getT$all();}
      that.getT$name=function(){return dummy.getT$name();}
      that.equals=AppliedCallableConstructor$jsint.$$.prototype.equals;
      that.$_apply=AppliedCallableConstructor$jsint.$$.prototype.$_apply;
      that.namedApply=AppliedCallableConstructor$jsint.$$.prototype.namedApply;
      atr$(that,'declaration',function(){
          return AppliedCallableConstructor$jsint.$$.prototype.$prop$getDeclaration.get.call(this);
      },undefined,AppliedCallableConstructor$jsint.$$.prototype.$prop$getDeclaration.$m$);
      atr$(that,'container',function(){
          return AppliedCallableConstructor$jsint.$$.prototype.$prop$getContainer.get.call(this);
      },undefined,AppliedCallableConstructor$jsint.$$.prototype.$prop$getContainer.$m$);
      atr$(that,'type',function(){
          return AppliedCallableConstructor$jsint.$$.prototype.$prop$getType.get.call(this);
      },undefined,AppliedCallableConstructor$jsint.$$.prototype.$prop$getType.$m$);
      atr$(that,'typeArguments',function(){
          return AppliedCallableConstructor$jsint.$$.prototype.$prop$getTypeArguments.get.call(this);
      },undefined,AppliedCallableConstructor$jsint.$$.prototype.$prop$getTypeArguments.$m$);
      atr$(that,'typeArgumentList',function(){
          return AppliedCallableConstructor$jsint.$$.prototype.$prop$getTypeArgumentList.get.call(this);
      },undefined,AppliedCallableConstructor$jsint.$$.prototype.$prop$getTypeArgumentList.$m$);
      atr$(that,'typeArgumentWithVariances',function(){
          return AppliedCallableConstructor$jsint.$$.prototype.$prop$getTypeArgumentWithVariances.get.call(this);
      },undefined,AppliedCallableConstructor$jsint.$$.prototype.$prop$getTypeArgumentWithVariances.$m$);
      atr$(that,'typeArgumentWithVarianceList',function(){
          return AppliedCallableConstructor$jsint.$$.prototype.$prop$getTypeArgumentWithVarianceList.get.call(this);
      },undefined,AppliedCallableConstructor$jsint.$$.prototype.$prop$getTypeArgumentWithVarianceList.$m$);
      atr$(that,'parameterTypes',function(){
          return AppliedCallableConstructor$jsint.$$.prototype.$prop$getParameterTypes.get.call(this);
      },undefined,AppliedCallableConstructor$jsint.$$.prototype.$prop$getParameterTypes.$m$);
      atr$(that,'string',function(){
          return AppliedCallableConstructor$jsint.$$.prototype.$prop$getString.get.call(this);
      },undefined,AppliedCallableConstructor$jsint.$$.prototype.$prop$getString.$m$);
      atr$(that,'hash',function(){
          return AppliedCallableConstructor$jsint.$$.prototype.$prop$getHash.get.call(this);
      },undefined,AppliedCallableConstructor$jsint.$$.prototype.$prop$getHash.$m$);
    }
  }
  $_Function$meta$model({Arguments$Function:$a$.Arguments$AppliedCallableConstructor,
    Type$Function:$a$.Type$AppliedCallableConstructor},that);
  CallableConstructor$meta$model({Type$CallableConstructor:$a$.Type$AppliedCallableConstructor,Arguments$CallableConstructor:$a$.Arguments$AppliedCallableConstructor},that);
  set_type_args(that,$a$,AppliedCallableConstructor$jsint);
  that.$targs=myTargs;
  that.tipo=tipo;
  return that;
}
