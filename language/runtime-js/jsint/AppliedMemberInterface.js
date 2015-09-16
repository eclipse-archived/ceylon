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
  var _proto=AppliedMemberInterface$jsint.$$.prototype;
  that.getMethod=_proto.getMethod;
  that.getDeclaredMethod=_proto.getDeclaredMethod;
  that.getAttribute=_proto.getAttribute;
  that.getDeclaredAttribute=_proto.getDeclaredAttribute;
  that.getClassOrInterface=_proto.getClassOrInterface;
  that.getDeclaredClassOrInterface=_proto.getDeclaredClassOrInterface;
  that.getClass=_proto.getClass;
  that.getDeclaredClass=_proto.getDeclaredClass;
  that.getInterface=_proto.getInterface;
  that.getDeclaredInterface=_proto.getDeclaredInterface;
  that.typeOf=_proto.typeOf;
  that.supertypeOf=_proto.supertypeOf;
  that.subtypeOf=_proto.subtypeOf;
  that.exactly=_proto.exactly;
  that.union=_proto.union;
  that.intersection=_proto.intersection;
  that.tipo=tipo;
  var dummy = new AppliedMemberInterface$jsint.$$;
  that.$$=AppliedMemberInterface$jsint.$$;
  that.getT$all=function(){return dummy.getT$all();};
  that.getT$name=function(){return dummy.getT$name();};
  that.equals=_proto.equals;
  atr$(that,'string',function(){return coistr$(that); },undefined,$_Object.$$.prototype.$prop$getString.$crtmm$);
  atr$(that,'hash',function(){return coihash$(that);},undefined,Identifiable.$$.prototype.$prop$getHash.$crtmm$);
  atr$(that,'declaration',function(){
    return coimoddcl$(that);
  },undefined,_proto.$prop$getDeclaration.$crtmm$);
  atr$(that,'typeArguments',function(){
    return coitarg$(that);
  },undefined,_proto.$prop$getTypeArguments.$crtmm$);
  atr$(that,'typeArgumentList',function(){
    return coitargl$(that);
  },undefined,_proto.$prop$getTypeArgumentList.$crtmm$);
  atr$(that,'typeArgumentWithVariances',function(){
    return coitargv$(that);
  },undefined,_proto.$prop$getTypeArgumentWithVariances.$crtmm$);
  atr$(that,'typeArgumentWithVarianceList',function(){
    return coitargvl$(that);
  },undefined,_proto.$prop$getTypeArgumentWithVarianceList.$crtmm$);
  atr$(that,'container',function(){
    return coicont$(that);
  },undefined,_proto.$prop$getContainer.$crtmm$);
  atr$(that,'declaringType',function(){return memberDeclaringType$(that);
  },undefined,_proto.$prop$getDeclaringType.$crtmm$);
  atr$(that,'extendedType',function(){return coiexttype$(that);
  },undefined,_proto.$prop$getExtendedType.$crtmm$);
  atr$(that,'satisfiedTypes',function(){return coisattype$(that);
  },undefined,_proto.$prop$getSatisfiedTypes.$crtmm$);
  atr$(that,'caseValues',function(){
    return _proto.$prop$getCaseValues.get.call(that);
  },undefined,_proto.$prop$getCaseValues.$crtmm$);
  that.$_bind=_proto.$_bind;
  return that;
}
