function ValParamDecl(cont,param,$$valParamDecl){
  $init$ValParamDecl();
  if ($$valParamDecl===undefined)$$valParamDecl=new ValParamDecl.$$;
  ValueDeclaration$meta$declaration($$valParamDecl);
  $$valParamDecl.cont=cont;
  $$valParamDecl.param=param;
  $$valParamDecl.tipo={$crtmm$:{$cont:cont.tipo,$t:param.$t,mt:'prm',d:cont.tipo.$crtmm$.d,an:param.an,pa:param.pa}};

  return $$valParamDecl;
}
ValParamDecl.$crtmm$=function(){return{mod:$CCMM$,'super':{t:Basic},ps:[],sts:[{t:ValueDeclaration$meta$declaration}],d:['ceylon.language.meta.declaration','ValParamDecl']};};
function $init$ValParamDecl(){
  if (ValParamDecl.$$===undefined){
    initTypeProto(ValParamDecl,'ceylon.language.meta.declaration::ValParamDecl',Basic,ValueDeclaration$meta$declaration);
    (function($$valParamDecl){

      atr$($$valParamDecl,'parameter',function(){return true;},undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:ValParamDecl,pa:3,d:['ceylon.language.meta.declaration','ValueDeclaration','$at','parameter']};});
      atr$($$valParamDecl,'defaulted',function(){
        return this.param.def!==undefined;
      },undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:ValParamDecl,pa:3,d:['ceylon.language.meta.declaration','ValueDeclaration','$at','defaulted']};});
      atr$($$valParamDecl,'variadic',function(){
        return this.param.seq!==undefined;
      },undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:ValParamDecl,pa:3,d:['ceylon.language.meta.declaration','ValueDeclaration','$at','variadic']};});
      atr$($$valParamDecl,'variable',function(){
        return (this.param.pa & 1024) > 0;
      },undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:ValParamDecl,pa:3,d:['ceylon.language.meta.declaration','ValueDeclaration','$at','variable']};});
      atr$($$valParamDecl,'shared',function(){
        return (this.param.pa & 1) > 0;
      },undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:ValParamDecl,pa:3,d:['ceylon.language.meta.declaration','ValueDeclaration','$at','shared']};});
      atr$($$valParamDecl,'formal',function(){
        return (this.param.pa & 4) > 0;
      },undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:ValParamDecl,pa:3,d:['ceylon.language.meta.declaration','ValueDeclaration','$at','formal']};});
      atr$($$valParamDecl,'$_default',function(){
        return (this.param.pa & 8) > 0;
      },undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:ValParamDecl,pa:3,d:['ceylon.language.meta.declaration','ValueDeclaration','$at','default']};});
      atr$($$valParamDecl,'actual',function(){
        return (this.param.pa & 2) > 0;
      },undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:ValParamDecl,pa:3,d:['ceylon.language.meta.declaration','ValueDeclaration','$at','actual']};});
      atr$($$valParamDecl,'container',function(){
        return this.cont;
      },undefined,function(){return{mod:$CCMM$,$t:{t:'u', l:[{t:Package$meta$declaration},{t:NestableDeclaration$meta$declaration}]},$cont:ValParamDecl,pa:3,d:['ceylon.language.meta.declaration','ValueDeclaration','$at','container']};});
      atr$($$valParamDecl,'containingPackage',function(){
        return this.cont.containingPackage;
      },undefined,function(){return{mod:$CCMM$,$t:{t:Package$meta$declaration},$cont:ValParamDecl,pa:3,d:['ceylon.language.meta.declaration','ValueDeclaration','$at','containingPackage']};});
      atr$($$valParamDecl,'containingModule',function(){
        return this.cont.containingModule;
      },undefined,function(){return{mod:$CCMM$,$t:{t:Module$meta$declaration},$cont:ValParamDecl,pa:3,d:['ceylon.language.meta.declaration','ValueDeclaration','$at','containingModule']};});
      atr$($$valParamDecl,'openType',function(){
        var t = this.param.$t;
        if (typeof(t)==='string')return OpenTvar$jsint(OpenTypeParam$jsint(this.cont,t));
        return _openTypeFromTarg(t);
      },undefined,function(){return{mod:$CCMM$,$t:{t:OpenType$meta$declaration},$cont:ValParamDecl,pa:3,d:['ceylon.language.meta.declaration','ValueDeclaration','$at','openType']};});
      atr$($$valParamDecl,'toplevel',function(){return false;},undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:ValParamDecl,pa:3,d:['ceylon.language.meta.declaration','ValueDeclaration','$at','toplevel']};});
      atr$($$valParamDecl,'qualifiedName',function(){
        return qname$(this.tipo.$crtmm$)+"."+this.param.nm;
      },undefined,function(){return{mod:$CCMM$,$t:{t:$_String},$cont:ValParamDecl,pa:3,d:['ceylon.language.meta.declaration','ValueDeclaration','$at','qualifiedName']};});
      atr$($$valParamDecl,'name',function(){
        return this.param.nm;
      },undefined,function(){return{mod:$CCMM$,$t:{t:$_String},$cont:ValParamDecl,pa:3,d:['ceylon.language.meta.declaration','ValueDeclaration','$at','name']};});
      //MethodDef apply at caca.ceylon (57:2-57:84)
      $$valParamDecl.$_apply=function $_apply($$$mptypes){
          var $$valParamDecl=this;
          throw Exception("IMPL ValParamDecl.apply");
      };$$valParamDecl.$_apply.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Value$meta$model,a:{Type:'Type'}},ps:[],$cont:ValParamDecl,tp:{Type:{'def':{t:Anything}}},pa:3,d:['ceylon.language.meta.declaration','ValueDeclaration','$m','apply']};};
      //MethodDef memberApply at caca.ceylon (58:2-58:166)
      $$valParamDecl.memberApply=function memberApply(containerType$20,$$$mptypes){
          var $$valParamDecl=this;
          throw Exception("IMPL ValParamDecl.memberApply");
      };$$valParamDecl.memberApply.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Attribute$meta$model,a:{Type:'Type',Container:'Container'}},ps:[{nm:'containerType',mt:'prm',$t:{t:Type$meta$model,a:{Type:'Container'}},an:function(){return[];}}],$cont:ValParamDecl,tp:{Container:{'def':{t:Nothing}},Type:{'def':{t:Anything}}},pa:3,d:['ceylon.language.meta.declaration','ValueDeclaration','$m','memberApply']};};
$$valParamDecl.equals=function(o){
  if (is$(o,{t:ValParamDecl}))return o.param===this.param;
  var eq=is$(o,{t:ValueDeclaration$meta$declaration})&&o.qualifiedName.equals(this.qualifiedName)&&o.shared==this.shared&&o.actual==this.actual&&o.formal==this.formal&&o.$_default==this.$_default&&o.variable==this.variable&&!o.toplevel&&o.openType.equals(this.openType)&&this.container.equals(o.container);
  if (eq && o.parameter) {
    return o.variadic==this.variadic&&o.defaulted==this.defaulted;
  }
  return eq;
};$$valParamDecl.equals.$crtmm$=function(){return{mod:$CCMM$,$t:{t:$_Boolean},ps:[{nm:'other',mt:'prm',$t:{t:$_Object}}],$cont:ValParamDecl,pa:3,d:['$','Object','$m','equals']};};
atr$($$valParamDecl,'string',function(){return 'value '+this.qualifiedName;},undefined,function(){return{mod:$CCMM$,$t:{t:$_String},pa:3,d:['$','Object','$at','string']};});
$$valParamDecl.annotated=function($mpt){
  var x=annd$annotations(this,{Annotation$annotations:$mpt.Annotation$annotated});
  return x&&x.size>0;
}
$$valParamDecl.annotated.$crtmm$=function(){return{mod:$CCMM$,$t:{t:$_Boolean},ps:[],$cont:ValParamDecl,tp:{Annotation$annotated:{sts:[{t:Annotation}]}},pa:67,d:['ceylon.language.meta.declaration','ValueDeclaration','$m','annotated']};};
$$valParamDecl.annotations=function($mpt){return annd$annotations(this,$mpt);};
$$valParamDecl.annotations.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Sequential,a:{Element$Sequential:'Annotation$annotations'}},ps:[],$cont:ValParamDecl,tp:{Annotation$annotations:{dv:'out',sts:[{t:Annotation}]}},pa:67,d:['ceylon.language.jsint','ValueDeclaration','$m','annotations']};};
    })(ValParamDecl.$$.prototype);
  }
  return ValParamDecl;
}
ex$.$init$ValParamDecl=$init$ValParamDecl;
$init$ValParamDecl();
