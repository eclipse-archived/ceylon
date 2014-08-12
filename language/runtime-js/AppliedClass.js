function AppliedClass(tipo,$$targs$$,that,classTargs){
  $init$AppliedClass();
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
    var dummy = new AppliedClass.$$;
    that.$$=AppliedClass.$$;
    that.getT$all=function(){return dummy.getT$all();};
    that.getT$name=function(){return dummy.getT$name();};
    that.equals=function(o){
      var eq=is$(o,{t:AppliedClass}) && o.tipo===tipo;
      return eq;
    };
    that.$_apply=function(x){return AppliedClass.$$.prototype.$_apply.call(that,x);};
    that.$_apply.$crtmm$=AppliedClass.$$.prototype.$_apply.$crtmm$;
    that.namedApply=function(x){return AppliedClass.$$.prototype.namedApply.call(that,x);};
    that.namedApply.$crtmm$=AppliedClass.$$.prototype.namedApply.$crtmm$;
  }
  that.$targs=classTargs;
  atr$(that,'satisfiedTypes',function(){
    return ClassOrInterface$meta$model.$$.prototype.$prop$getSatisfiedTypes.get.call(that);
  },undefined,ClassOrInterface$meta$model.$$.prototype.$prop$getExtendedType.$crtmm$);
  atr$(that,'container',function(){
    return ClassOrInterface$meta$model.$$.prototype.$prop$getContainer.get.call(that);
  },undefined,ClassOrInterface$meta$model.$$.prototype.$prop$getContainer.$crtmm$);
  atr$(that,'string',function(){
    return ClassOrInterface$meta$model.$$.prototype.$prop$getString.get.call(that);
  },undefined,ClassOrInterface$meta$model.$$.prototype.$prop$getString.$crtmm$);
  atr$(that,'hash',function(){
    return ClassOrInterface$meta$model.$$.prototype.$prop$getHash.get.call(that);
  },undefined,ClassOrInterface$meta$model.$$.prototype.$prop$getHash.$crtmm$);
  atr$(that,'typeArguments',function(){
    return ClassOrInterface$meta$model.$$.prototype.$prop$getTypeArguments.get.call(that);
  },undefined,ClassOrInterface$meta$model.$$.prototype.$prop$getTypeArguments.$crtmm$);
  atr$(that,'extendedType',function(){
    return ClassOrInterface$meta$model.$$.prototype.$prop$getExtendedType.get.call(that);
  },undefined,ClassOrInterface$meta$model.$$.prototype.$prop$getExtendedType.$crtmm$);
  atr$(that,'declaration',function(){
    return ClassModel$meta$model.$$.prototype.$prop$getDeclaration.get.call(that);
  },undefined,ClassModel$meta$model.$$.prototype.$prop$getDeclaration.$crtmm$);
  atr$(that,'parameterTypes',function(){
    return ClassModel$meta$model.$$.prototype.$prop$getParameterTypes.get.call(that);
  },undefined,ClassModel$meta$model.$$.prototype.$prop$getParameterTypes.$crtmm$);
  atr$(that,'caseValues',function(){
    return ClassOrInterface$meta$model.$$.prototype.$prop$getCaseValues.get.call(that);
  },undefined,ClassOrInterface$meta$model.$$.prototype.$prop$getCaseValues.$crtmm$);
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
  that.equals=ClassModel$meta$model.$$.prototype.equals;
  that.typeOf=ClassOrInterface$meta$model.$$.prototype.typeOf;
  that.supertypeOf=ClassOrInterface$meta$model.$$.prototype.supertypeOf;
  that.subtypeOf=ClassOrInterface$meta$model.$$.prototype.subtypeOf;
  that.exactly=ClassOrInterface$meta$model.$$.prototype.exactly;
  set_type_args(that,$$targs$$);
  Class$meta$model(that.$$targs$$===undefined?$$targs$$:{Arguments$Class:that.$$targs$$.Arguments$Class,Type$Class:that.$$targs$$.Type$Class},that);
  that.tipo=tipo;
  return that;
}
AppliedClass.$crtmm$=function(){return{mod:$CCMM$,'super':{t:Basic},tp:{Type$Class:{dv:'out','def':{t:Anything}},Arguments$Class:{dv:'in',sts:[{t:Sequential,a:{Element$Iterable:{t:Anything}}}],'def':{t:Nothing}}},sts:[{t:Class$meta$model,a:{Arguments$Class:'Arguments$Class',Type$Class:'Type$Class'}}],pa:1,d:['ceylon.language.meta.model','Class']};};
function $init$AppliedClass(){
  if (AppliedClass.$$===undefined){
    initTypeProto(AppliedClass,'ceylon.language.meta.model::AppliedClass',Basic,Class$meta$model);
    (function($$clase){

      $$clase.$_apply=function(a){
        var mdl=get_model(this.tipo.$crtmm$);
        if (mdl&&mdl.mt==='o')throw InvocationException$meta$model("Cannot instantiate anonymous class");
        a=convert$params(this.tipo.$crtmm$,a);
        if (this.$targs)a.push(this.$targs);
        return this.tipo.apply(undefined,a);
      };$$clase.$_apply.$crtmm$=function(){return{mod:$CCMM$,$t:'Type$Applicable',ps:[{nm:'arguments',mt:'prm',seq:1,$t:{t:Sequential,a:{Element$Sequential:{t:Anything}}},an:function(){return[];}}],$cont:Applicable$meta$model,an:function(){return[doc$('ceylon.language.meta.model:Applicable:$m:apply'),$throws("IncompatibleTypeException",""),$throws("InvocationException",""),shared(),formal()];},d:['ceylon.language.meta.model','Applicable','$m','apply']};};

      $$clase.namedApply=function(args){
        var mdl=get_model(this.tipo.$crtmm$);
        if (mdl&&mdl.mt==='o')throw InvocationException$meta$model("Cannot instantiate anonymous class");
        var mm=getrtmm$$(this.tipo);
        if (mm.ps===undefined)throw InvocationException$meta$model("Applied function does not have metamodel parameter info for named args call");
        var mapped={};
        var iter=args.iterator();var a;while((a=iter.next())!==getFinished()) {
          mapped[a.key]=a.item===getNullArgument$meta$model()?null:a.item;
        }
        var ordered=[];
        for (var i=0; i<mm.ps.length; i++) {
          var p=mm.ps[i];
          var v=mapped[p.nm];
          if (v===undefined && p.def===undefined) {
            throw InvocationException$meta$model("Required argument " + p.nm + " missing in named-argument invocation");
          } else if (v!==undefined) {
              var t=p.$t;
              if(typeof(t)==='string'&&this.$targs)t=this.$targs[t];
              if (t&&!is$(v,t))throw IncompatibleTypeException$meta$model("Argument " + p.nm + "="+v+" is not of the expected type.");
          } 
          delete mapped[p.nm];
          ordered.push(v);
        }
        if (Object.keys(mapped).length>0)throw new InvocationException$meta$model("No arguments with names " + Object.keys(mapped));
        if (this.$targs) {
          ordered.push(this.$targs);
        }
        return this.tipo.apply(undefined,ordered);





      };$$clase.namedApply.$crtmm$=function(){return{mod:$CCMM$,$t:'Type$Applicable',ps:[{nm:'arguments',mt:'prm',$t:{t:Iterable,a:{Element$Iterable:{t:Entry,a:{Item$Entry:{t:$Object},Key$Entry:{t:$_String}}},Absent$Iterable:{t:Null}}},an:function(){return[];}}],$cont:Applicable$meta$model,an:function(){return[doc$('ceylon.language.meta.model:Applicable:$m:namedApply'),$throws("IncompatibleTypeException",""),$throws("InvocationException",""),shared(),formal()];},d:['ceylon.language.meta.model','Applicable','$m','namedApply']};};
    })(AppliedClass.$$.prototype);
  }
  return AppliedClass;
}
ex$.$init$AppliedClass$meta$model=$init$AppliedClass;
$init$AppliedClass();
