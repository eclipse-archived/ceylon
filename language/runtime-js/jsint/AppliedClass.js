function AppliedClass$jsint(tipo,$$targs$$,that,classTargs){
  if (!$$targs$$.Type$AppliedClass)$$targs$$.Type$AppliedClass=$$targs$$.Type$Class;
  if (!$$targs$$.Arguments$AppliedClass)$$targs$$.Arguments$AppliedClass=$$targs$$.Arguments$Class;
  $init$AppliedClass$jsint();
  var _proto=AppliedClass$jsint.$$.prototype;
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
    that.$_apply=_proto.$_apply;
    that.namedApply=_proto.namedApply;
  }
  //AppliedMemberClass also gets these
  atr$(that,'satisfiedTypes',function(){return coisattype$(that);
  },undefined,_proto.$prop$getExtendedType.$crtmm$);
  atr$(that,'container',function(){ return coicont$(that);
  },undefined,_proto.$prop$getContainer.$crtmm$);
  atr$(that,'string',function(){return coistr$(that);},undefined,_proto.$prop$getString.$crtmm$);
  atr$(that,'hash',function(){return coihash$(that);},undefined,_proto.$prop$getHash.$crtmm$);
  atr$(that,'typeArguments',function(){return coitarg$(that);
  },undefined,_proto.$prop$getTypeArguments.$crtmm$);
  atr$(that,'typeArgumentList',function(){return coitargl$(that);
  },undefined,_proto.$prop$getTypeArgumentList.$crtmm$);
  atr$(that,'extendedType',function(){return coiexttype$(that);
  },undefined,_proto.$prop$getExtendedType.$crtmm$);
  atr$(that,'declaration',function(){return coimoddcl$(that);
  },undefined,_proto.$prop$getDeclaration.$crtmm$);
  atr$(that,'parameterTypes',function(){return clsparamtypes(that);
  },undefined,_proto.$prop$getParameterTypes.$crtmm$);
  atr$(that,'caseValues',function(){return coicase$(that);
  },undefined,_proto.$prop$getCaseValues.$crtmm$);
  that.getMethod=_proto.getMethod;
  that.getDeclaredMethod=_proto.getDeclaredMethod;
  that.getMethods=_proto.getMethods;
  that.getDeclaredMethods=_proto.getDeclaredMethods;
  that.getAttribute=_proto.getAttribute;
  that.getDeclaredAttribute=_proto.getDeclaredAttribute;
  that.getAttributes=_proto.getAttributes;
  that.getDeclaredAttributes=_proto.getDeclaredAttributes;
  that.getClassOrInterface=_proto.getClassOrInterface;
  that.getDeclaredClassOrInterface=_proto.getDeclaredClassOrInterface;
  that.getClass=_proto.getClass;
  that.getDeclaredClass=_proto.getDeclaredClass;
  that.getClasses=_proto.getClasses;
  that.getDeclaredClasses=_proto.getDeclaredClasses;
  that.getInterface=_proto.getInterface;
  that.getDeclaredInterface=_proto.getDeclaredInterface;
  that.getInterfaces=_proto.getInterfaces;
  that.getDeclaredInterfaces=_proto.getDeclaredInterfaces;
  that.equals=_proto.equals;
  that.typeOf=_proto.typeOf;
  that.supertypeOf=_proto.supertypeOf;
  that.subtypeOf=_proto.subtypeOf;
  that.exactly=_proto.exactly;
  that.union=_proto.union;
  that.intersection=_proto.intersection;
  that.getConstructor=_proto.getConstructor;
  that.$targs=classTargs;
  set_type_args(that,$$targs$$,AppliedClass$jsint);
  Class$meta$model({Arguments$Class:that.$$targs$$.Arguments$AppliedClass,
                   Type$Class:that.$$targs$$.Type$AppliedClass},that);
  //This is for serialization
  if (tipo===Tuple && classTargs)that.$$targs$$.Type$Class={t:Tuple,a:classTargs};
  that.tipo=tipo;
  return that;
}
