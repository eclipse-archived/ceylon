function AppliedMemberInterface(tipo,$$targs$$,that,myTargs){
  if (!$$targs$$.Type$AppliedMemberInterface)$$targs$$.Type$AppliedMemberInterface=$$targs$$.Type$MemberInterface;
  if (!$$targs$$.Container$AppliedMemberInterface)$$targs$$.Container$AppliedMemberInterface=$$targs$$.Container$MemberInterface;
  $init$AppliedMemberInterface();
  if (that===undefined){
    var mm = getrtmm$$(tipo);
    if (mm && mm.$cont) {
      that=function(x){
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
      that=new AppliedMemberInterface.$$;
    }
  }
  MemberInterface$meta$model(that.$$targs$$===undefined?$$targs$$:{Type$MemberInterface:that.$$targs$$.Type$AppliedMemberInterface,
    Container$MemberInterface:that.$$targs$$.Container$AppliedMemberInterface},that);
  set_type_args(that,$$targs$$,AppliedMemberInterface);
  that.$targs=myTargs;
  that.getMethod=ClassOrInterface$meta$model.$$.prototype.getMethod;
  that.getDeclaredMethod=ClassOrInterface$meta$model.$$.prototype.getDeclaredMethod;
  that.getAttribute=ClassOrInterface$meta$model.$$.prototype.getAttribute;
  that.getDeclaredAttribute=ClassOrInterface$meta$model.$$.prototype.getDeclaredAttribute;
  that.getClassOrInterface=ClassOrInterface$meta$model.$$.prototype.getClassOrInterface;
  that.getDeclaredClassOrInterface=ClassOrInterface$meta$model.$$.prototype.getDeclaredClassOrInterface;
  that.getClass=ClassOrInterface$meta$model.$$.prototype.getClass;
  that.getDeclaredClass=ClassOrInterface$meta$model.$$.prototype.getDeclaredClass;
  that.getInterface=ClassOrInterface$meta$model.$$.prototype.getInterface;
  that.getDeclaredInterface=ClassOrInterface$meta$model.$$.prototype.getDeclaredInterface;
  that.typeOf=ClassOrInterface$meta$model.$$.prototype.typeOf;
  that.supertypeOf=ClassOrInterface$meta$model.$$.prototype.supertypeOf;
  that.subtypeOf=ClassOrInterface$meta$model.$$.prototype.subtypeOf;
  that.exactly=ClassOrInterface$meta$model.$$.prototype.exactly;
  that.union=function(t,$mpt){return coiut$(that,t,$mpt.Other$union);};
  that.intersection=function(t,$mpt){return coiit$(that,t,$mpt.Other$intersection);};
  that.tipo=tipo;
  var dummy = new AppliedMemberInterface.$$;
  that.$$=AppliedMemberInterface.$$;
  that.getT$all=function(){return dummy.getT$all();};
  that.getT$name=function(){return dummy.getT$name();};
  that.equals=function(o){
    var eq=is$(o,{t:AppliedMemberInterface}) && (o.tipo$2||o.tipo)==tipo;
    if (that.$bound)eq=eq && o.$bound && o.$bound.equals(that.$bound);else eq=eq && o.$bound===undefined;
    return eq && this.typeArguments.equals(o.typeArguments);
  };
  atr$(that,'string',function(){return coistr$(that); },undefined,$_Object.$$.prototype.$prop$getString.$crtmm$);
  atr$(that,'hash',function(){return coihash$(that);},undefined,Identifiable.$$.prototype.$prop$getHash.$crtmm$);
  atr$(that,'declaration',function(){
    return coimoddcl$(that);
  },undefined,InterfaceModel$meta$model.$$.prototype.$prop$getDeclaration.$crtmm$);
  atr$(that,'typeArguments',function(){
    return ClassOrInterface$meta$model.$$.prototype.$prop$getTypeArguments.get.call(that);
  },undefined,ClassOrInterface$meta$model.$$.prototype.$prop$getTypeArguments.$crtmm$);
  atr$(that,'container',function(){
    return coicont$(that);
  },undefined,ClassOrInterface$meta$model.$$.prototype.$prop$getContainer.$crtmm$);
  that.$_bind=function(x){return AppliedMemberInterface.$$.prototype.$_bind.call(that,x);}
  return that;
}
AppliedMemberInterface.$crtmm$=function(){return{mod:$CCMM$,'super':{t:Basic},ps:[],tp:{Container$MemberInterface:{dv:'in'},Type$MemberInterface:{dv:'out','def':{t:Anything}}},sts:[{t:MemberInterface$meta$model,a:{Type$MemberInterface:'Type$MemberInterface',Container$MemberInterface:'Container$MemberInterface'}}],an:function(){return[shared(),$_abstract()];},d:['ceylon.language.meta.model','MemberInterface']};};
ex$.AppliedMemberInterface=AppliedMemberInterface;
function $init$AppliedMemberInterface(){
  if (AppliedMemberInterface.$$===undefined){
    initTypeProto(AppliedMemberInterface,'ceylon.language.meta.model::AppliedMemberInterface',Basic,MemberInterface$meta$model);
    (function($$appliedMemberInterface){
      $$appliedMemberInterface.$_bind=function $_bind(container$2){
        var $$appliedMemberInterface=this;
        throw Exception("IMPL MemberInterface.bind");
      };$$appliedMemberInterface.$_bind.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Interface$meta$model,a:{Type$Interface:'Type$Interface'}},ps:[{nm:'container',mt:'prm',$t:{t:$_Object},an:function(){return[];}}],$cont:MemberInterface,an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','MemberInterface','$m','bind']};};
    })(AppliedMemberInterface.$$.prototype);
  }
  return AppliedMemberInterface;
}
ex$.$init$AppliedMemberInterface$meta$model=$init$AppliedMemberInterface;
$init$AppliedMemberInterface();
