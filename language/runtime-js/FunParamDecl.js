function FunParamDecl(cont,param,$$funParamDecl){
  $init$FunParamDecl();
  if ($$funParamDecl===undefined)$$funParamDecl=new FunParamDecl.$$;
  FunctionDeclaration$meta$declaration($$funParamDecl);
  $$funParamDecl.cont=cont;
  $$funParamDecl.param=param;
  $$funParamDecl.tipo={$crtmm$:{$cont:cont.tipo,$t:param.$rt,ps:param.ps,mt:'prm',d:cont.tipo.$crtmm$.d,an:param.an,pa:param.pa}};

  $$funParamDecl.$prop$getParameter={$crtmm$:function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:FunParamDecl,pa:3,d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','parameter']};}};
  $$funParamDecl.$prop$getParameter.get=function(){return true;};
  $$funParamDecl.$prop$getAnnotation={$crtmm$:function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:FunParamDecl,pa:3,d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','annotation']};}};
  $$funParamDecl.$prop$getAnnotation.get=function(){return false;};
  $$funParamDecl.$prop$getShared={$crtmm$:function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:FunParamDecl,pa:3,d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','shared']};}};
  $$funParamDecl.$prop$getShared.get=function(){return false;};
  $$funParamDecl.$prop$getToplevel={$crtmm$:function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:FunParamDecl,pa:3,d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','toplevel']};}};
  $$funParamDecl.$prop$getToplevel.get=function(){return false;};
  $$funParamDecl.$prop$getFormal={$crtmm$:function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:FunParamDecl,pa:3,d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','formal']};}};
  $$funParamDecl.$prop$getFormal.get=function(){return false;};
  $$funParamDecl.$prop$getDefault={$crtmm$:function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:FunParamDecl,pa:3,d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','default']};}};
  $$funParamDecl.$prop$getDefault.get=function(){return false};
  $$funParamDecl.$prop$getActual={$crtmm$:function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:FunParamDecl,pa:3,d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','actual']};}};
  $$funParamDecl.$prop$getActual.get=function(){return false;};
  return $$funParamDecl;
}
FunParamDecl.$crtmm$=function(){return{mod:$CCMM$,'super':{t:Basic},ps:[],sts:[{t:FunctionDeclaration$meta$declaration}],d:['ceylon.language.meta.declaration','FunParamDecl']};};
function $init$FunParamDecl(){
  if (FunParamDecl.$$===undefined){
    initTypeProto(FunParamDecl,'ceylon.language.meta.declaration::FunParamDecl',Basic,FunctionDeclaration$meta$declaration);
    (function($$funParamDecl){

      atr$($$funParamDecl,'parameter',function(){return true;},undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:FunParamDecl,pa:3,d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','parameter']};});
      
      //AttributeGetterDef defaulted at caca.ceylon (8:2-8:71)
      atr$($$funParamDecl,'defaulted',function(){
        return this.param.def!==undefined;
      },undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:FunParamDecl,pa:3,d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','defaulted']};});
      //AttributeGetterDef variadic at caca.ceylon (9:2-9:69)
      atr$($$funParamDecl,'variadic',function(){
        return this.param.seq!==undefined;
      },undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:FunParamDecl,pa:3,d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','variadic']};});
      //AttributeGetterDef container at caca.ceylon (11:2-11:91)
      atr$($$funParamDecl,'container',function(){
        return this.cont;
      },undefined,function(){return{mod:$CCMM$,$t:{t:'u', l:[{t:Package$meta$declaration},{t:NestableDeclaration$meta$declaration}]},$cont:FunParamDecl,pa:3,d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','container']};});
      //AttributeGetterDef containingPackage at caca.ceylon (12:2-12:87)
      atr$($$funParamDecl,'containingPackage',function(){
        return this.cont.containingPackage;
      },undefined,function(){return{mod:$CCMM$,$t:{t:Package$meta$declaration},$cont:FunParamDecl,pa:3,d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','containingPackage']};});
      //AttributeGetterDef containingModule at caca.ceylon (13:2-13:84)
      atr$($$funParamDecl,'containingModule',function(){
        return this.cont.containingModule;
      },undefined,function(){return{mod:$CCMM$,$t:{t:Module$meta$declaration},$cont:FunParamDecl,pa:3,d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','containingModule']};});
      //AttributeGetterDef openType at caca.ceylon (14:2-14:70)
      atr$($$funParamDecl,'openType',function(){
        var t = this.param.$rt;
        if (typeof(t)==='string')return OpenTvar$jsint(OpenTypeParam$jsint(this.cont,t));
        return _openTypeFromTarg(t);
      },undefined,function(){return{mod:$CCMM$,$t:{t:OpenType$meta$declaration},$cont:FunParamDecl,pa:3,d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','openType']};});
      //AttributeDecl annotation at caca.ceylon (15:2-15:40)
      atr$($$funParamDecl,'annotation',function(){return false;},undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:FunParamDecl,pa:3,d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','annotation']};});
      atr$($$funParamDecl,'shared',function(){return (this.param.pa&1)>0;},undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:FunParamDecl,pa:3,d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','shared']};});
      atr$($$funParamDecl,'toplevel',function(){return false;},undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:FunParamDecl,pa:3,d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','toplevel']};});
      atr$($$funParamDecl,'formal',function(){return (this.param.pa&4)>0;},undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:FunParamDecl,pa:3,d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','formal']};});
      atr$($$funParamDecl,'$_default',function(){return (this.param.pa&8)>0;},undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:FunParamDecl,pa:3,d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','default']};});
      atr$($$funParamDecl,'actual',function(){return (this.param.pa&2)>0;},undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:FunParamDecl,pa:3,d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','actual']};});
      atr$($$funParamDecl,'qualifiedName',function(){
        return qname$(this.tipo.$crtmm$)+"."+this.param.nm;
      },undefined,function(){return{mod:$CCMM$,$t:{t:$_String},$cont:FunParamDecl,pa:3,d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','qualifiedName']};});
      atr$($$funParamDecl,'string',function(){return "function " + this.qualifiedName;
      },undefined,$_Object.$$.prototype.$prop$getString.$crtmm$);
      atr$($$funParamDecl,'name',function(){
        return this.param.nm;
      },undefined,function(){return{mod:$CCMM$,$t:{t:$_String},$cont:FunParamDecl,pa:3,d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','name']};});
      $$funParamDecl.getParameterDeclaration=function getParameterDeclaration(name$10){
      var pd=this.parameterDeclarations;
      for (var i=0; i < pd.size; i++) {
        if (name$6.equals(pd[i].name))return pd[i];
      }
      return null;
      };$$funParamDecl.getParameterDeclaration.$crtmm$=function(){return{mod:$CCMM$,$t:{t:'u', l:[{t:Null},{t:FunctionOrValueDeclaration$meta$declaration}]},ps:[{nm:'name',mt:'prm',$t:{t:$_String},an:function(){return[];}}],$cont:FunParamDecl,pa:3,d:['ceylon.language.meta.declaration','FunctionDeclaration','$m','getParameterDeclaration']};};
      
      //MethodDef apply at caca.ceylon (31:2-32:74)
      $$funParamDecl.$_apply=function $_apply(typeArguments$11,$$$mptypes){
          if(typeArguments$11===undefined){typeArguments$11=empty();}
          throw Exception("IMPL FunParamDecl.apply");
      };$$funParamDecl.$_apply.$crtmm$=function(){return{mod:$CCMM$,$t:{t:$_Function$meta$model,a:{Arguments:'Arguments',Type:'Return'}},ps:[{nm:'typeArguments',mt:'prm',seq:1,$t:{t:Sequential,a:{Element$Sequential:{t:Type$meta$model,a:{Type:{t:Anything}}}}},an:function(){return[];}}],$cont:FunParamDecl,tp:{Return:{'def':{t:Anything}},Arguments:{sts:[{t:Sequential,a:{Element$Sequential:{t:Anything}}}],'def':{t:Nothing}}},pa:3,d:['ceylon.language.meta.declaration','FunctionDeclaration','$m','apply']};};
      
      //MethodDef memberApply at caca.ceylon (33:2-34:80)
      $$funParamDecl.memberApply=function memberApply(containerType$12,typeArguments$13,$$$mptypes){
          if(typeArguments$13===undefined){typeArguments$13=empty();}
          throw Exception("IMPL FunParamDecl.memberApply");
      };$$funParamDecl.memberApply.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Method$meta$model,a:{Arguments:'Arguments',Type:'Return',Container:'Container'}},ps:[{nm:'containerType',mt:'prm',$t:{t:Type$meta$model,a:{Type:'Container'}},an:function(){return[];}},{nm:'typeArguments',mt:'prm',seq:1,$t:{t:Sequential,a:{Element$Sequential:{t:Type$meta$model,a:{Type:{t:Anything}}}}},an:function(){return[];}}],$cont:FunParamDecl,tp:{Container:{'def':{t:Nothing}},Return:{'def':{t:Anything}},Arguments:{sts:[{t:Sequential,a:{Element$Sequential:{t:Anything}}}],'def':{t:Nothing}}},pa:3,d:['ceylon.language.meta.declaration','FunctionDeclaration','$m','memberApply']};};
$$funParamDecl.annotated=function($mpt){
  var x=annd$annotations(this,{Annotation$annotations:$mpt.Annotation$annotated});
  return x&&x.size>0;
}
$$funParamDecl.annotated.$crtmm$=function(){return{mod:$CCMM$,$t:{t:$_Boolean},ps:[],$cont:FunParamDecl,tp:{Annotation$annotated:{sts:[{t:Annotation}]}},pa:67,d:['ceylon.language.meta.declaration','FunctionDeclaration','$m','annotated']};};
$$funParamDecl.annotations=function($mpt){return annd$annotations(this,$mpt);};
$$funParamDecl.annotations.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Sequential,a:{Element$Sequential:'Annotation$annotations'}},ps:[],$cont:FunParamDecl,tp:{Annotation$annotations:{dv:'out',sts:[{t:Annotation}]}},pa:67,d:['ceylon.language.jsint','FunctionDeclaration','$m','annotations']};};
    })(FunParamDecl.$$.prototype);
  }
  return FunParamDecl;
}
ex$.$init$FunParamDecl=$init$FunParamDecl;
$init$FunParamDecl();
