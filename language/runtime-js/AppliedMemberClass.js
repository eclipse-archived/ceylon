function AppliedMemberClass(tipo,$$targs$$,that,myTargs){
  if (!$$targs$$.Type$AppliedMemberClass)$$targs$$.Type$AppliedMemberClass=$$targs$$.Type$MemberClass;
  if (!$$targs$$.Arguments$AppliedMemberClass)$$targs$$.Arguments$AppliedMemberClass=$$targs$$.Arguments$MemberClass;
  if (!$$targs$$.Container$AppliedMemberClass)$$targs$$.Container$AppliedMemberClass=$$targs$$.Container$MemberClass;
  $init$AppliedMemberClass();
  if (that===undefined) {
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
        rv=AppliedClass$jsint(rv,{Type$AppliedClass:nt,Arguments$AppliedClass:{t:Sequential,a:{Element$Iterable:{t:Anything},Absent$Iterable:{t:Null}}}});//TODO generate metamodel for Arguments
        if (nt.a)rv.$targs=nt.a;
        rv.$bound=x;
        return rv;
      }
    } else {
      throw IncompatibleTypeException$meta$model("Invalid metamodel data for MemberClass");
    }
  }
  AppliedClass$jsint(tipo,{Type$AppliedClass:$$targs$$.Type$AppliedMemberClass,Arguments$AppliedClass:$$targs$$.Arguments$AppliedMemberClass},that);
  var dummy = new AppliedMemberClass.$$;
  that.$$=AppliedMemberClass.$$;
  that.getT$all=function(){return dummy.getT$all();};
  that.getT$name=function(){return dummy.getT$name();};
  that.equals=function(o){
    var eq=is$(o,{t:AppliedMemberClass}) && o.tipo===tipo;
    if (that.$bound)eq=eq && o.$bound && o.$bound.equals(that.$bound);else eq=eq && o.$bound===undefined;
    return eq && this.typeArguments.equals(o.typeArguments);
  };
  that.$targs=myTargs;
  atr$(that,'parameterTypes',function(){
    return clsparamtypes(that);
  },undefined,ClassModel$meta$model.$$.prototype.$prop$getParameterTypes.$crtmm$);
  atr$(that,'extendedType',function(){
    return ClassOrInterface$meta$model.$$.prototype.$prop$getExtendedType.get.call(that);
  },undefined,ClassOrInterface$meta$model.$$.prototype.$prop$getExtendedType.$crtmm$);
  atr$(that,'satisfiedTypes',function(){
    return ClassOrInterface$meta$model.$$.prototype.$prop$getSatisfiedTypes.get.call(that);
  },undefined,ClassOrInterface$meta$model.$$.prototype.$prop$getExtendedType.$crtmm$);
  atr$(that,'caseValues',function(){
    return ClassOrInterface$meta$model.$$.prototype.$prop$getCaseValues.get.call(that);
  },undefined,ClassOrInterface$meta$model.$$.prototype.$prop$getCaseValues.$crtmm$);
  atr$(that,'declaration',function(){
    return coimoddcl$(that);
  },undefined,ClassModel$meta$model.$$.prototype.$prop$getDeclaration.$crtmm$);
  that.$_bind=function(){return AppliedMemberClass.$$.prototype.$_bind.apply(that,arguments);}
  atr$(that,'string',function(){return coistr$(that); },undefined,$_Object.$$.prototype.$prop$getString.$crtmm$);
  atr$(that,'hash',function(){return coihash$(that);},undefined,Identifiable.$$.prototype.$prop$getHash.$crtmm$);
  atr$(that,'container',function(){return coicont$(that); },undefined,ClassOrInterface$meta$model.$$.prototype.$prop$getContainer.$crtmm$);
  MemberClass$meta$model(that.$$targs$$===undefined?$$targs$$:{Arguments$MemberClass:that.$$targs$$.Arguments$AppliedMemberClass,
                         Type$MemberClass:that.$$targs$$.Type$AppliedMemberClass,
                         Container$MemberClass:that.$$targs$$.Container$AppliedMemberClass},that);
  set_type_args(that,$$targs$$,AppliedMemberClass);
  that.tipo=tipo;
  return that;
}
AppliedMemberClass.$crtmm$=function(){return{mod:$CCMM$,'super':{t:Basic},ps:[],tp:{Container$MemberClass:{dv:'in'},Type$MemberClass:{dv:'out','def':{t:Anything}},Arguments$MemberClass:{dv:'in',sts:[{t:Sequential,a:{Element$Iterable:{t:Anything}}}],'def':{t:Nothing}}},sts:[{t:MemberClass$meta$model,a:{Arguments$MemberClass:'Arguments$MemberClass',Type$MemberClass:'Type$MemberClass',Container$MemberClass:'Container$MemberClass'}}],an:function(){return[shared(),$_abstract()];},d:['','AppliedMemberClass']};};
ex$.AppliedMemberClass=AppliedMemberClass;
function $init$AppliedMemberClass(){
  if (AppliedMemberClass.$$===undefined){
    initTypeProto(AppliedMemberClass,'ceylon.language.meta.model::AppliedMemberClass',Basic,MemberClass$meta$model);
    (function($$amc){
      
      //MethodDef bind at caca.ceylon (5:4-5:107)
      $$amc.$_bind=function $_bind(cont){
        var ot=cont.getT$name ? cont.getT$all()[cont.getT$name()]:throwexc(IncompatibleTypeException$meta$model("Container does not appear to be a Ceylon object"));
        if (!ot)throw IncompatibleTypeException$meta$model("Incompatible Container (has no metamodel information");
        var omm=getrtmm$$(ot);
        var mm=getrtmm$$(this.tipo);
        if (!extendsType({t:ot},{t:mm.$cont}))throw IncompatibleTypeException$meta$model("Incompatible container type");
        return this(cont);
      };$$amc.$_bind.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Class$meta$model,a:{Arguments$Class:'Arguments',Type$Class:'Type'}},ps:[{nm:'container',mt:'prm',$t:{t:$_Object},an:function(){return[];}}],$cont:MemberClass$meta$model,an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','MemberClass','$m','bind']};};
    })(AppliedMemberClass.$$.prototype);
  }
  return AppliedMemberClass;
}
ex$.$init$AppliedMemberClass$meta$model=$init$AppliedMemberClass;
$init$AppliedMemberClass();
