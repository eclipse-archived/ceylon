function AppliedInterface$jsint(tipo,$a$,that,myTargs) {
  if (!$a$.Type$AppliedInterface)$a$.Type$AppliedInterface=$a$.Type$Interface;
  $i$AppliedInterface$jsint();
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
      atr$(that,'string',function(){return coistr$(that); },undefined,dummy.$prop$getString.$m$);
      atr$(that,'hash',function(){return coihash$(that);},undefined,dummy.$prop$getHash.$m$);
      atr$(that,'declaration',function(){return coimoddcl$(that);},undefined,dummy.$prop$getDeclaration.$m$);
      atr$(that,'container',function(){return coicont$(that); },undefined,dummy.$prop$getContainer.$m$);
      atr$(that,'typeArguments',function(){return coitarg$(that);},undefined,dummy.$prop$getTypeArguments.$m$);
      atr$(that,'typeArgumentList',function(){return coitargl$(that);},undefined,dummy.$prop$getTypeArgumentList.$m$);
      atr$(that,'typeArgumentWithVariances',function(){return coitargv$(that);},undefined,dummy.$prop$getTypeArgumentWithVariances.$m$);
      atr$(that,'typeArgumentWithVarianceList',function(){return coitargvl$(that);},undefined,dummy.$prop$getTypeArgumentWithVarianceList.$m$);
      atr$(that,'extendedType',function(){return coiexttype$(that); },undefined,dummy.$prop$getExtendedType.$m$);
      atr$(that,'satisfiedTypes',function(){return coisattype$(that); },undefined,dummy.$prop$getSatisfiedTypes.$m$);
      atr$(that,'caseValues',function(){return coicase$(that); },undefined,dummy.$prop$getCaseValues.$m$);
    } else {
      that=new AppliedInterface$jsint.$$;
    }
  }
  set_type_args(that,$a$,AppliedInterface$jsint);
  Interface$meta$model({Type$Interface:$a$.Type$AppliedInterface},that);
  that.$targs=myTargs;
  that.tipo=tipo;
  return that;
}
