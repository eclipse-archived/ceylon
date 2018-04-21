function AppliedMemberInterface$jsint(tipo,$a$,that,myTargs){
  if (!$a$.Type$AppliedMemberInterface)$a$.Type$AppliedMemberInterface=$a$.Type$MemberInterface;
  if (!$a$.Container$AppliedMemberInterface)$a$.Container$AppliedMemberInterface=$a$.Container$MemberInterface;
  $i$AppliedMemberInterface$jsint();
  if (that===undefined){
    var mm = getrtmm$$(tipo);
    if (mm && mm.$cont) {
      that=function AppliedMemIface1(x){
        var rv=tipo.bind(x);
        rv.$m$=tipo.$m$;
        var nt={t:tipo};
        if (x && x.$a$) {
          nt.a={};
          for (var nta in x.$a$)nt.a[nta]=x.$a$[nta];
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
  set_type_args(that,$a$,AppliedMemberInterface$jsint);
  MemberInterface$meta$model(that.$a$===undefined?$a$:{Type$MemberInterface:that.$a$.Type$AppliedMemberInterface,
    Container$MemberInterface:that.$a$.Container$AppliedMemberInterface},that);
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
  atr$(that,'string',function(){return coistr$(that); },undefined,$_Object.$$.prototype.$prop$getString.$m$);
  atr$(that,'hash',function(){return coihash$(that);},undefined,Identifiable.$$.prototype.$prop$getHash.$m$);
  atr$(that,'declaration',function(){
    return coimoddcl$(that);
  },undefined,_proto.$prop$getDeclaration.$m$);
  atr$(that,'typeArguments',function(){
    return coitarg$(that);
  },undefined,_proto.$prop$getTypeArguments.$m$);
  atr$(that,'typeArgumentList',function(){
    return coitargl$(that);
  },undefined,_proto.$prop$getTypeArgumentList.$m$);
  atr$(that,'typeArgumentWithVariances',function(){
    return coitargv$(that);
  },undefined,_proto.$prop$getTypeArgumentWithVariances.$m$);
  atr$(that,'typeArgumentWithVarianceList',function(){
    return coitargvl$(that);
  },undefined,_proto.$prop$getTypeArgumentWithVarianceList.$m$);
  atr$(that,'container',function(){
    return coicont$(that);
  },undefined,_proto.$prop$getContainer.$m$);
  atr$(that,'declaringType',function(){return memberDeclaringType$(that);
  },undefined,_proto.$prop$getDeclaringType.$m$);
  atr$(that,'extendedType',function(){return coiexttype$(that);
  },undefined,_proto.$prop$getExtendedType.$m$);
  atr$(that,'satisfiedTypes',function(){return coisattype$(that);
  },undefined,_proto.$prop$getSatisfiedTypes.$m$);
  atr$(that,'caseValues',function(){
    return _proto.$prop$getCaseValues.get.call(that);
  },undefined,_proto.$prop$getCaseValues.$m$);
  that.$_bind=_proto.$_bind;
  return that;
}
