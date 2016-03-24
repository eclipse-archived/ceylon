function AppliedInterface$jsint(tipo,$$targs$$,that,myTargs) {
  if (!$$targs$$.Type$AppliedInterface)$$targs$$.Type$AppliedInterface=$$targs$$.Type$Interface;
  $init$AppliedInterface$jsint();
  if (that===undefined){
    var mm = getrtmm$$(tipo);
    if (mm && mm.$cont) {
      that=function AppliedIface1(x){
        that.tipo=function(){return tipo.apply(x,arguments);};
        that.$bound=x;
        return that;
      }
      that.tipo$2=tipo;
      var dummy = new AppliedInterface$jsint.$$;
      that.getClassOrInterface=dummy.getClassOrInterface;
      that.getDeclaredClassOrInterface=dummy.getDeclaredClassOrInterface;
      that.typeOf=dummy.typeOf;
      that.supertypeOf=dummy.supertypeOf;
      that.exactly=dummy.exactly;
      that.getClasses=dummy.getClasses;
      that.getDeclaredClasses=dummy.getDeclaredClasses;
      that.getClass=dummy.getClass;
      that.getDeclaredClass=dummy.getDeclaredClass;
      that.getInterfaces=dummy.getInterfaces;
      that.getDeclaredInterfaces=dummy.getDeclaredInterfaces;
      that.getInterface=dummy.getInterface;
      that.getDeclaredInterface=dummy.getDeclaredInterface;
      that.getAttributes=dummy.getAttributes;
      that.getDeclaredAttributes=dummy.getDeclaredAttributes;
      that.getAttribute=dummy.getAttribute;
      that.getDeclaredAttribute=dummy.getDeclaredAttribute;
      that.getMethods=dummy.getMethods;
      that.getDeclaredMethods=dummy.getDeclaredMethods;
      that.getMethod=dummy.getMethod;
      that.getDeclaredMethod=dummy.getDeclaredMethod;
      that.equals=dummy.equals;
      that.$$=AppliedInterface$jsint.$$;
      that.getT$all=function(){return dummy.getT$all();};
      that.getT$name=function(){return dummy.getT$name();};
      atr$(that,'string',function(){return coistr$(that); },undefined,dummy.$prop$getString.$crtmm$);
      atr$(that,'hash',function(){return coihash$(that);},undefined,dummy.$prop$getHash.$crtmm$);
      atr$(that,'declaration',function(){return coimoddcl$(that);},undefined,dummy.$prop$getDeclaration.$crtmm$);
      atr$(that,'container',function(){return coicont$(that); },undefined,dummy.$prop$getContainer.$crtmm$);
      atr$(that,'typeArguments',function(){return coitarg$(that);},undefined,dummy.$prop$getTypeArguments.$crtmm$);
      atr$(that,'typeArgumentList',function(){return coitargl$(that);},undefined,dummy.$prop$getTypeArgumentList.$crtmm$);
      atr$(that,'typeArgumentWithVariances',function(){return coitargv$(that);},undefined,dummy.$prop$getTypeArgumentWithVariances.$crtmm$);
      atr$(that,'typeArgumentWithVarianceList',function(){return coitargvl$(that);},undefined,dummy.$prop$getTypeArgumentWithVarianceList.$crtmm$);
      atr$(that,'extendedType',function(){return coiexttype$(that); },undefined,dummy.$prop$getExtendedType.$crtmm$);
      atr$(that,'satisfiedTypes',function(){return coisattype$(that); },undefined,dummy.$prop$getSatisfiedTypes.$crtmm$);
      atr$(that,'caseValues',function(){return coicase$(that); },undefined,dummy.$prop$getCaseValues.$crtmm$);
    } else {
      that=new AppliedInterface$jsint.$$;
    }
  }
  Interface$meta$model({Type$Interface:$$targs$$.Type$AppliedInterface},that);
  set_type_args(that,$$targs$$,AppliedInterface$jsint);
  that.$targs=myTargs;
  that.tipo=tipo;
  return that;
}
