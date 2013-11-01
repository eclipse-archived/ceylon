//Addendum to model.declaration.ClassOrInterfaceDeclaration
ClassOrInterfaceDeclaration$meta$declaration.$$.prototype.getMemberDeclaration=function (name$20,$$$mptypes){
  var $$oi=this;
  var _m=undefined;
  if (extendsType($$$mptypes.Kind, {t:ValueDeclaration$meta$declaration})) {
    var _d = $$oi.meta.$at ? $$oi.meta.$at[name$20] : undefined;
    if (_d)_d=_findTypeFromModel($$oi.containingPackage,_d,$$oi.tipo);
    if (_d)_m=OpenValue($$oi.containingPackage, _d);
  } else if (extendsType($$$mptypes.Kind, {t:FunctionDeclaration$meta$declaration})) {
    var _d = $$oi.meta.$m ? $$oi.meta.$m[name$20] : undefined;
    if (_d)_d=_findTypeFromModel($$oi.containingPackage,_d,$$oi.tipo);
    if(_d)_m=OpenFunction($$oi.packageContainer, _d);
  } else if (extendsType($$$mptypes.Kind, {t:ClassDeclaration$meta$declaration})) {
    var _d = $$oi.meta.$c ? $$oi.meta.$c[name$20] : undefined;
    if (_d)_d=_findTypeFromModel($$oi.containingPackage,_d,$$oi.tipo);
    if(_d)_m=OpenClass($$oi.containingPackage, _d);
  } else if (extendsType($$$mptypes.Kind, {t:InterfaceDeclaration$meta$declaration})) {
    var _d = $$oi.meta.$i ? $$oi.meta.$i[name$20] : undefined;
    if (_d)_d=_findTypeFromModel($$oi.containingPackage,_d,$$oi.tipo);
    if(_d)_m=OpenInterface($$oi.containingPackage, _d);
  }
  if (_m) {
    _m.$parent=this;
    return _m;
  }
  return null;
};
ClassOrInterfaceDeclaration$meta$declaration.$$.prototype.getMemberDeclaration.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},'Kind']},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}}],$cont:OpenClass,$tp:{Kind:{'satisfies':[{t:NestableDeclaration$meta$declaration}]}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassOrInterfaceDeclaration','$m','getMemberDeclaration']};};
ClassOrInterfaceDeclaration$meta$declaration.$$.prototype.getDeclaredMemberDeclaration=function(nm,$mptypes){
  console.log("IMPL ClassOrInterfaceDeclaration.getDeclaredMemberDeclaration("+nm+")");
  return this.getMemberDeclaration(nm,$mptypes);
}
ClassOrInterfaceDeclaration$meta$declaration.$$.prototype.getDeclaredMemberDeclaration.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},'Kind']},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}}],$cont:OpenClass,$tp:{Kind:{'satisfies':[{t:NestableDeclaration$meta$declaration}]}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassOrInterfaceDeclaration','$m','getDeclaredMemberDeclaration']};};
defineAttr(FunctionalDeclaration$meta$declaration.$$.prototype,'parameterDeclarations',function(){
  var that=this;
  var parms = that.tipo.$$metamodel$$.$ps;
  if (!parms || parms.length === 0)return getEmpty();
  var rv = [];
  for (var i=0; i<parms.length;i++) {
    var p = parms[i];
//TODO set "parameter" to true
    if (p.$pt === 'f') {
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
    var _d;
    if (mt === 'mthd') {
      _d=OpenFunction(pkg, m);
    } else if (mt==='cls') {
      _d=OpenClass(pkg, m);
    } else if (mt==='ifc') {
      _d=OpenInterface(pkg, m);
    } else if (mt==='attr'||mt==='gttr'||mt==='obj') {
      _d=OpenValue(pkg, m);
    }
    if (_d) {
      _d.$parent=this;
      r.push(_d);
    }
  }
  return r.reifyCeylonType({Element:$$$mptypes.Kind});
};
ClassOrInterfaceDeclaration$meta$declaration.$$.prototype.memberDeclarations.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:'Kind'}},$ps:[],$cont:OpenInterface,$tp:{Kind:{'satisfies':[{t:NestableDeclaration$meta$declaration}]}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassOrInterfaceDeclaration','$m','memberDeclarations']};};
defineAttr(ClassOrInterfaceDeclaration$meta$declaration.$$.prototype,'caseTypes',function(){
  var casos = this.tipo.$$metamodel$$.of;
  if (casos && casos.length > 0) {
    var ct = [];
    for (var i=0; i < casos.length; i++) {
      if (typeof(casos[i])==='string') {
        ct.push(OpenTvar(OpenTypeParam(this, casos[i])));
      } else {
        ct.push(_openTypeFromTarg(casos[i]));
      }
    }
    return ct.reifyCeylonType({Element:{t:OpenType$meta$declaration}});
  }
  return getEmpty();
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:{t:OpenType$meta$declaration}}},$cont:ClassOrInterfaceDeclaration$meta$declaration,$an:function(){return[shared(),formal()];},d:['ceylon.language.meta.declaration','ClassOrInterfaceDeclaration','$at','caseTypes']};});
ClassOrInterfaceDeclaration$meta$declaration.$$.prototype.$apply=function(types,$mptypes){
  var _t = {t:this.tipo};
  if (typeof(this.tipo.$$metamodel$$)==='function') {
    this.tipo.$$metamodel$$=this.tipo.$$metamodel$$();
  }
  validate$typeparams(_t,this.tipo.$$metamodel$$.$tp,types);
  if (!extendsType(_t, $mptypes.Type))
    throw IncompatibleTypeException$meta$model(String$("Type argument for 'Type' must be a supertype of " + this));
  var rv=this.meta.$mt==='ifc'?AppliedInterface(_t.t, $mptypes):AppliedClass(_t.t, $mptypes);
  if (_t.a)rv.$targs=_t.a;
  return rv;
}
ClassOrInterfaceDeclaration$meta$declaration.$$.prototype.$apply.$$metamodel$$=function(){return{mod:$$METAMODEL$$,d:['ceylon.language.meta.declaration','ClassOrInterfaceDeclaration','$m','apply']};};
ClassOrInterfaceDeclaration$meta$declaration.$$.prototype.memberApply=function(cont, types,$mptypes) {
  var mm=this.tipo.$$metamodel$$;
  if (typeof(mm)==='function') {
    mm=mm(); this.tipo.$$metamodel$$=mm();
  }
  if (!extendsType({t:cont.tipo},{t:mm.$cont}))throw IncompatibleTypeException$meta$model("Incompatible Container type");
  var _t = {t:this.tipo};
  validate$typeparams(_t,mm.$tp,types);
  if (!extendsType(_t, $mptypes.Type))
    throw IncompatibleTypeException$meta$model(String$("Type argument for 'Type' must be a supertype of " + this));
  var rv=this.meta.$mt==='ifc'?AppliedMemberInterface(_t.t, {Container:$mptypes.Container, Type:_t})
    :AppliedMemberClass(_t.t, {Container:$mptypes.Container, Type:_t, Arguments:$mptypes.Arguments});
  if (_t.a)rv.$targs=_t.a;
  return rv;
};ClassOrInterfaceDeclaration$meta$declaration.$$.prototype.memberApply.$$metamodel$$=function(){return{mod:$$METAMODEL$$,d:['ceylon.language.meta.declaration','ClassOrInterfaceDeclaration','$m','memberApply']};};
defineAttr(ClassOrInterfaceDeclaration$meta$declaration.$$.prototype,'container',function(){
  if (this.$parent)return this.$parent;
  var mm=this.tipo.$$metamodel$$;
  if (typeof(mm)==='function'){mm=mm();this.tipo.$$metamodel$$=mm;}
  if (mm.$cont) {
    return typeLiteral$meta({Type:mm.$cont});
  }
  return this.containingPackage;
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:'u',l:[{t:NestableDeclaration$meta$declaration},{t:Package$meta$declaration}]},d:['ceylon.language.meta.declaration.NestableDeclaration','$at','container']};});
