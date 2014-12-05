function AppliedMemberInterface$jsint(tipo,$$targs$$,that,myTargs){
  if (!$$targs$$.Type$AppliedMemberInterface)$$targs$$.Type$AppliedMemberInterface=$$targs$$.Type$MemberInterface;
  if (!$$targs$$.Container$AppliedMemberInterface)$$targs$$.Container$AppliedMemberInterface=$$targs$$.Container$MemberInterface;
  $init$AppliedMemberInterface$jsint();
  if (that===undefined){
    var mm = getrtmm$$(tipo);
    if (mm && mm.$cont) {
      that=function AppliedMemIface1(x){
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
        rv=AppliedInterface$jsint(rv,{Type$AppliedInterface:nt});
        if (nt.a)rv.$targs=nt.a;
        rv.$bound=x;
        return rv;
      }
      that.tipo$2=tipo;
    } else {
      that=new AppliedMemberInterface$jsint.$$;
    }
  }
  MemberInterface$meta$model(that.$$targs$$===undefined?$$targs$$:{Type$MemberInterface:that.$$targs$$.Type$AppliedMemberInterface,
    Container$MemberInterface:that.$$targs$$.Container$AppliedMemberInterface},that);
  set_type_args(that,$$targs$$,AppliedMemberInterface$jsint);
  that.$targs=myTargs;
  that.getMethod=AppliedMemberInterface$jsint.$$.prototype.getMethod;
  that.getDeclaredMethod=AppliedMemberInterface$jsint.$$.prototype.getDeclaredMethod;
  that.getAttribute=AppliedMemberInterface$jsint.$$.prototype.getAttribute;
  that.getDeclaredAttribute=AppliedMemberInterface$jsint.$$.prototype.getDeclaredAttribute;
  that.getClassOrInterface=AppliedMemberInterface$jsint.$$.prototype.getClassOrInterface;
  that.getDeclaredClassOrInterface=AppliedMemberInterface$jsint.$$.prototype.getDeclaredClassOrInterface;
  that.getClass=AppliedMemberInterface$jsint.$$.prototype.getClass;
  that.getDeclaredClass=AppliedMemberInterface$jsint.$$.prototype.getDeclaredClass;
  that.getInterface=AppliedMemberInterface$jsint.$$.prototype.getInterface;
  that.getDeclaredInterface=AppliedMemberInterface$jsint.$$.prototype.getDeclaredInterface;
  that.typeOf=AppliedMemberInterface$jsint.$$.prototype.typeOf;
  that.supertypeOf=AppliedMemberInterface$jsint.$$.prototype.supertypeOf;
  that.subtypeOf=AppliedMemberInterface$jsint.$$.prototype.subtypeOf;
  that.exactly=AppliedMemberInterface$jsint.$$.prototype.exactly;
  that.union=AppliedMemberInterface$jsint.$$.prototype.union;
  that.intersection=AppliedMemberInterface$jsint.$$.prototype.intersection;
  that.tipo=tipo;
  var dummy = new AppliedMemberInterface$jsint.$$;
  that.$$=AppliedMemberInterface$jsint.$$;
  that.getT$all=function(){return dummy.getT$all();};
  that.getT$name=function(){return dummy.getT$name();};
  that.equals=AppliedMemberInterface$jsint.$$.prototype.equals;
  atr$(that,'string',function(){return coistr$(that); },undefined,$_Object.$$.prototype.$prop$getString.$crtmm$);
  atr$(that,'hash',function(){return coihash$(that);},undefined,Identifiable.$$.prototype.$prop$getHash.$crtmm$);
  atr$(that,'declaration',function(){
    return coimoddcl$(that);
  },undefined,AppliedMemberInterface$jsint.$$.prototype.$prop$getDeclaration.$crtmm$);
  atr$(that,'typeArguments',function(){
    return AppliedMemberInterface$jsint.$$.prototype.$prop$getTypeArguments.get.call(that);
  },undefined,AppliedMemberInterface$jsint.$$.prototype.$prop$getTypeArguments.$crtmm$);
  atr$(that,'container',function(){
    return coicont$(that);
  },undefined,AppliedMemberInterface$jsint.$$.prototype.$prop$getContainer.$crtmm$);
  atr$(that,'declaringType',function(){return memberDeclaringType$(that);
  },undefined,AppliedMemberInterface$jsint.$$.prototype.$prop$getDeclaringType.$crtmm$);
  atr$(that,'extendedType',function(){return coiexttype$(that);
  },undefined,AppliedMemberInterface$jsint.$$.prototype.$prop$getExtendedType.$crtmm$);
  atr$(that,'satisfiedTypes',function(){return coisattype$(that);
  },undefined,AppliedMemberInterface$jsint.$$.prototype.$prop$getSatisfiedTypes.$crtmm$);
  atr$(that,'caseValues',function(){
    return AppliedMemberInterface$jsint.$$.prototype.$prop$getCaseValues.get.call(that);
  },undefined,AppliedMemberInterface$jsint.$$.prototype.$prop$getCaseValues.$crtmm$);
  that.$_bind=AppliedMemberInterface$jsint.$$.prototype.$_bind;
  return that;
}
