function AppliedClass$jsint(tipo,$$targs$$,that,classTargs){
  if (!$$targs$$.Type$AppliedClass)$$targs$$.Type$AppliedClass=$$targs$$.Type$Class;
  if (!$$targs$$.Arguments$AppliedClass)$$targs$$.Arguments$AppliedClass=$$targs$$.Arguments$Class;
  $init$AppliedClass$jsint();
  if (that===undefined){
    var mm = getrtmm$$(tipo);
    if (mm && mm.$cont) {
      that=function(x){/*Class*/
        if (that.$targs) {
          var _a=[];
          for (var i=0;i<arguments.length;i++)_a.push(arguments[i]);
          _a.push(that.$targs);
          return tipo.apply(x,_a);
        }
        return tipo.apply(x,arguments);
      }
    } else {
      that=function(){
        if (that.$targs) {
          var _a=[];
          for (var i=0;i<arguments.length;i++)_a.push(arguments[i]);
          _a.push(that.$targs);
          return tipo.apply(undefined,_a);
        }
        return tipo.apply(undefined,arguments);
      }
    }
    that.$crtmm$=mm;
    var dummy = new AppliedClass$jsint.$$;
    that.$$=AppliedClass$jsint.$$;
    that.getT$all=function(){return dummy.getT$all();};
    that.getT$name=function(){return dummy.getT$name();};
    that.$_apply=AppliedClass$jsint.$$.prototype.$_apply;
    that.namedApply=AppliedClass$jsint.$$.prototype.namedApply;
  }
  //AppliedMemberClass also gets these
  atr$(that,'satisfiedTypes',function(){return coisattype$(that);
  },undefined,AppliedClass$jsint.$$.prototype.$prop$getExtendedType.$crtmm$);
  atr$(that,'container',function(){ return coicont$(that);
  },undefined,AppliedClass$jsint.$$.prototype.$prop$getContainer.$crtmm$);
  atr$(that,'string',function(){return coistr$(that);},undefined,AppliedClass$jsint.$$.prototype.$prop$getString.$crtmm$);
  atr$(that,'hash',function(){return coihash$(that);},undefined,AppliedClass$jsint.$$.prototype.$prop$getHash.$crtmm$);
  atr$(that,'typeArguments',function(){return coitarg$(that);
  },undefined,AppliedClass$jsint.$$.prototype.$prop$getTypeArguments.$crtmm$);
  atr$(that,'extendedType',function(){return coiexttype$(that);
  },undefined,AppliedClass$jsint.$$.prototype.$prop$getExtendedType.$crtmm$);
  atr$(that,'declaration',function(){return coimoddcl$(that);
  },undefined,AppliedClass$jsint.$$.prototype.$prop$getDeclaration.$crtmm$);
  atr$(that,'parameterTypes',function(){return clsparamtypes(that);
  },undefined,AppliedClass$jsint.$$.prototype.$prop$getParameterTypes.$crtmm$);
  atr$(that,'caseValues',function(){return coicase$(that);
  },undefined,AppliedClass$jsint.$$.prototype.$prop$getCaseValues.$crtmm$);
  that.getMethod=ClassOrInterface$meta$model.$$.prototype.getMethod;
  that.getDeclaredMethod=ClassOrInterface$meta$model.$$.prototype.getDeclaredMethod;
  that.getMethods=ClassOrInterface$meta$model.$$.prototype.getMethods;
  that.getDeclaredMethods=ClassOrInterface$meta$model.$$.prototype.getDeclaredMethods;
  that.getAttribute=ClassOrInterface$meta$model.$$.prototype.getAttribute;
  that.getDeclaredAttribute=ClassOrInterface$meta$model.$$.prototype.getDeclaredAttribute;
  that.getAttributes=ClassOrInterface$meta$model.$$.prototype.getAttributes;
  that.getDeclaredAttributes=ClassOrInterface$meta$model.$$.prototype.getDeclaredAttributes;
  that.getClassOrInterface=ClassOrInterface$meta$model.$$.prototype.getClassOrInterface;
  that.getDeclaredClassOrInterface=ClassOrInterface$meta$model.$$.prototype.getDeclaredClassOrInterface;
  that.getClass=ClassOrInterface$meta$model.$$.prototype.getClass;
  that.getDeclaredClass=ClassOrInterface$meta$model.$$.prototype.getDeclaredClass;
  that.getClasses=ClassOrInterface$meta$model.$$.prototype.getClasses;
  that.getDeclaredClasses=ClassOrInterface$meta$model.$$.prototype.getDeclaredClasses;
  that.getInterface=ClassOrInterface$meta$model.$$.prototype.getInterface;
  that.getDeclaredInterface=ClassOrInterface$meta$model.$$.prototype.getDeclaredInterface;
  that.getInterfaces=ClassOrInterface$meta$model.$$.prototype.getInterfaces;
  that.getDeclaredInterfaces=ClassOrInterface$meta$model.$$.prototype.getDeclaredInterfaces;
  that.equals=AppliedClass$jsint.$$.prototype.equals;
  that.typeOf=ClassOrInterface$meta$model.$$.prototype.typeOf;
  that.supertypeOf=ClassOrInterface$meta$model.$$.prototype.supertypeOf;
  that.subtypeOf=ClassOrInterface$meta$model.$$.prototype.subtypeOf;
  that.exactly=ClassOrInterface$meta$model.$$.prototype.exactly;
  that.union=function(t,$mpt){return coiut$(that,t,$mpt.Other$union);}
  that.intersection=function(t,$mpt){return coiit$(that,t,$mpt.Other$intersection);}
  that.getConstructor=AppliedClass$jsint.$$.prototype.getConstructor;
  that.instantiator=AppliedClass$jsint.$$.prototype.instantiator;
  that.$targs=classTargs;
  set_type_args(that,$$targs$$,AppliedClass$jsint);
  Class$meta$model({Arguments$Class:that.$$targs$$.Arguments$AppliedClass,
                   Type$Class:that.$$targs$$.Type$AppliedClass},that);
  that.tipo=tipo;
  return that;
}
