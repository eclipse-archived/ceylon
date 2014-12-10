function AppliedClass$jsint(tipo,$$targs$$,that,classTargs){
  if (!$$targs$$.Type$AppliedClass)$$targs$$.Type$AppliedClass=$$targs$$.Type$Class;
  if (!$$targs$$.Arguments$AppliedClass)$$targs$$.Arguments$AppliedClass=$$targs$$.Arguments$Class;
  $init$AppliedClass$jsint();
  if (that===undefined){
    var mm = getrtmm$$(tipo);
    if (mm && mm.$cont) {
      that=function AppliedClass1(x){/*Class*/
        if (that.$targs) {
          var _a=[];
          for (var i=0;i<arguments.length;i++)_a.push(arguments[i]);
          _a.push(that.$targs);
          return tipo.apply(x,_a);
        }
        return tipo.apply(x,arguments);
      }
    } else {
      that=function AppliedClass2(){
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
  that.getMethod=AppliedClass$jsint.$$.prototype.getMethod;
  that.getDeclaredMethod=AppliedClass$jsint.$$.prototype.getDeclaredMethod;
  that.getMethods=AppliedClass$jsint.$$.prototype.getMethods;
  that.getDeclaredMethods=AppliedClass$jsint.$$.prototype.getDeclaredMethods;
  that.getAttribute=AppliedClass$jsint.$$.prototype.getAttribute;
  that.getDeclaredAttribute=AppliedClass$jsint.$$.prototype.getDeclaredAttribute;
  that.getAttributes=AppliedClass$jsint.$$.prototype.getAttributes;
  that.getDeclaredAttributes=AppliedClass$jsint.$$.prototype.getDeclaredAttributes;
  that.getClassOrInterface=AppliedClass$jsint.$$.prototype.getClassOrInterface;
  that.getDeclaredClassOrInterface=AppliedClass$jsint.$$.prototype.getDeclaredClassOrInterface;
  that.getClass=AppliedClass$jsint.$$.prototype.getClass;
  that.getDeclaredClass=AppliedClass$jsint.$$.prototype.getDeclaredClass;
  that.getClasses=AppliedClass$jsint.$$.prototype.getClasses;
  that.getDeclaredClasses=AppliedClass$jsint.$$.prototype.getDeclaredClasses;
  that.getInterface=AppliedClass$jsint.$$.prototype.getInterface;
  that.getDeclaredInterface=AppliedClass$jsint.$$.prototype.getDeclaredInterface;
  that.getInterfaces=AppliedClass$jsint.$$.prototype.getInterfaces;
  that.getDeclaredInterfaces=AppliedClass$jsint.$$.prototype.getDeclaredInterfaces;
  that.equals=AppliedClass$jsint.$$.prototype.equals;
  that.typeOf=AppliedClass$jsint.$$.prototype.typeOf;
  that.supertypeOf=AppliedClass$jsint.$$.prototype.supertypeOf;
  that.subtypeOf=AppliedClass$jsint.$$.prototype.subtypeOf;
  that.exactly=AppliedClass$jsint.$$.prototype.exactly;
  that.union=AppliedClass$jsint.$$.prototype.union;
  that.intersection=AppliedClass$jsint.$$.prototype.intersection;
  that.getConstructor=AppliedClass$jsint.$$.prototype.getConstructor;
  that.$targs=classTargs;
  set_type_args(that,$$targs$$,AppliedClass$jsint);
  Class$meta$model({Arguments$Class:that.$$targs$$.Arguments$AppliedClass,
                   Type$Class:that.$$targs$$.Type$AppliedClass},that);
  that.tipo=tipo;
  return that;
}
