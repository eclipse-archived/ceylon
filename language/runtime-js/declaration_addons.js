//Addendum to model.declaration.ClassOrInterfaceDeclaration
ClassOrInterfaceDeclaration$meta$declaration.$$.prototype.getMemberDeclaration=function (name$20,$$$mptypes){
  var $$oi=this;
  if (extendsType($$$mptypes.Kind, {t:ValueDeclaration$meta$declaration})) {
    var _d = $$oi.meta.$at ? $$oi.meta.$at[name$20] : undefined;
    return _d ? OpenValue($$oi.containingPackage, _d) : null;
  } else if (extendsType($$$mptypes.Kind, {t:FunctionDeclaration$meta$declaration})) {
    var _d = $$oi.meta.$m ? $$oi.meta.$m[name$20] : undefined;
    return _d ? OpenFunction($$oi.packageContainer, _d) : null;
  } else if (extendsType($$$mptypes.Kind, {t:ClassDeclaration$meta$declaration})) {
    var _d = $$oi.meta.$c ? $$oi.meta.$c[name$20] : undefined;
    return _d ? OpenClass($$oi.containingPackage, _d) : null;
  } else if (extendsType($$$mptypes.Kind, {t:InterfaceDeclaration$meta$declaration})) {
    var _d = $$oi.meta.$i ? $$oi.meta.$i[name$20] : undefined;
    return _d ? OpenInterface($$oi.containingPackage, _d) : null;
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
//TODO set "parameter" to true
    if (p['$pt'] === 'f') {
      console.log("parametro funcional");
    } else {
      rv.push(OpenValue(that.containingPackage, p));
    }
  }
  return rv.reifyCeylonType({Element:{t:FunctionOrValueDeclaration$meta$declaration},Absent:{t:Null}});
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:{t:FunctionOrValueDeclaration$meta$declaration}}},$cont:FunctionalDeclaration$meta$declaration,$an:function(){return[shared(),formal()];},d:['ceylon.language.meta.declaration','FunctionalDeclaration','$at','parameterDeclarations']};});
ClassOrInterfaceDeclaration$meta$declaration.$$.prototype.memberDeclarations=function($$$mptypes){
  var filter;
  var defs=[];
  if (extendsType($$$mptypes.Kind,{t:FunctionDeclaration$meta$declaration})||extendsType($$$mptypes.Kind,{t:FunctionOrValueDeclaration$meta$declaration})) {
    for (var df in this.meta['$m']) {
      defs.push(this.meta['$m'][df]);
    }
  }
  if (extendsType($$$mptypes.Kind,{t:ValueDeclaration$meta$declaration})||extendsType($$$mptypes.Kind,{t:FunctionOrValueDeclaration$meta$declaration})) {
    for (var df in this.meta['$at']) {
      defs.push(this.meta['$at'][df]);
    }
  }
  if (extendsType($$$mptypes.Kind,{t:ClassDeclaration$meta$declaration})||extendsType($$$mptypes.Kind,{t:ClassOrInterfaceDeclaration$meta$declaration})) {
    for (var df in this.meta['$c']) {
      defs.push(this.meta['$c'][df]);
    }
  }
  if (extendsType($$$mptypes.Kind,{t:InterfaceDeclaration$meta$declaration})||extendsType($$$mptypes.Kind,{t:ClassOrInterfaceDeclaration$meta$declaration})) {
    for (var df in this.meta['$i']) {
      defs.push(this.meta['$i'][df]);
    }
  }
  var pkg = this.containingPackage;
  var r=[];
  for (var i=0; i<defs.length; i++) {
    var m=defs[i];
    var mt = m['$mt'];
    if (mt === 'mthd') {
      r.push(OpenFunction(pkg, m));
    } else if (mt==='cls') {
      r.push(OpenClass(pkg, m));
    } else if (mt==='ifc') {
      r.push(OpenInterface(pkg, m));
    } else if (mt==='attr'||mt==='gttr'||mt==='obj') {
      r.push(OpenValue(pkg, m));
    }
  }
  return r.reifyCeylonType({Element:$$$mptypes.Kind});
};
ClassOrInterfaceDeclaration$meta$declaration.$$.prototype.memberDeclarations.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:'Kind'}},$ps:[],$cont:OpenInterface,$tp:{Kind:{'satisfies':[{t:NestableDeclaration$meta$declaration}]}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','InterfaceDeclaration','$m','memberDeclarations']};};
