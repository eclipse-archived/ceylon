//Addendum to model.declaration.ClassOrInterfaceDeclaration
ClassOrInterfaceDeclaration$meta$declaration.$$.prototype.getMemberDeclaration=function (name$20,$$$mptypes){
  var $$oi=this;
  if (extendsType($$$mptypes.Kind, {t:ValueDeclaration$meta$declaration})) {
    var _d = $$oi.meta.$at ? $$oi.meta.$at[name$20] : undefined;
    return _d ? OpenValue(name$20, $$oi.packageContainer, false, _d) : null;
  } else if (extendsType($$$mptypes.Kind, {t:FunctionDeclaration$meta$declaration})) {
    var _d = $$oi.meta.$m ? $$oi.meta.$m[name$20] : undefined;
    return _d ? OpenFunction(name$20, $$oi.packageContainer, false, _d) : null;
  } else if (extendsType($$$mptypes.Kind, {t:ClassDeclaration$meta$declaration})) {
    var _d = $$oi.meta.$c ? $$oi.meta.$c[name$20] : undefined;
    return _d ? OpenClass(name$20, $$oi.packageContainer, false, _d) : null;
  } else if (extendsType($$$mptypes.Kind, {t:InterfaceDeclaration$meta$declaration})) {
    var _d = $$oi.meta.$i ? $$oi.meta.$i[name$20] : undefined;
    return _d ? OpenInterface(name$20, $$oi.packageContainer, false, _d) : null;
  }
  return null;
};
ClassOrInterfaceDeclaration$meta$declaration.$$.prototype.getMemberDeclaration.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},'Kind']},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}}],$cont:OpenClass,$tp:{Kind:{'satisfies':[{t:NestableDeclaration$meta$declaration}]}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassOrInterfaceDeclaration','$m','getMemberDeclaration']};};

defineAttr(FunctionalDeclaration$meta$declaration.$$.prototype,'parameterDeclarations',function(){
  var that=this;
  var parms = that.tipo.$$metamodel$$['$ps'];
  if (parms === null || parms === undefined || parms.length === 0)return getEmpty();
  var rv = [];
  for (var i=0; i<parms.length;i++) {
    var p = parms[i];
    if (p['$pt'] === 'f') {
      console.log("parametro funcional");
    } else {
      rv.push(OpenValue(p['$nm'], that.packageContainer, false, p));
    }
  }
  return rv.reifyCeylonType({Element:{t:FunctionOrValueDeclaration$meta$declaration},Absent:{t:Null}});
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:{t:FunctionOrValueDeclaration$meta$declaration}}},$cont:FunctionalDeclaration$meta$declaration,$an:function(){return[shared(),formal()];},d:['ceylon.language.meta.declaration','FunctionalDeclaration','$at','parameterDeclarations']};});
