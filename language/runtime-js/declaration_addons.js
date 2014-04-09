//Addendum to model.declaration.ClassOrInterfaceDeclaration
ClassOrInterfaceDeclaration$meta$declaration.$$.prototype.getMemberDeclaration=function(name$20,$$$mptypes,noInherit){
  var _m=undefined;
  if (extendsType($$$mptypes.Kind$getMemberDeclaration, {t:ValueDeclaration$meta$declaration})) {
    var propname='$prop$get'+name$20[0].toUpperCase()+name$20.substring(1);
    var _d = this.tipo.$$.prototype[propname];
    if (_d){
      if (noInherit) {
        var mm=getrtmm$$(_d);
        if (mm.$cont!==this.tipo)return null;
      }
      _m=OpenValue(this.containingPackage, _d);
    }
  }
  if (!_m && extendsType($$$mptypes.Kind$getMemberDeclaration, {t:FunctionDeclaration$meta$declaration})) {
    var nom=name$20;
    if (this.tipo===String$ && ['split','replace','filter','every','map','sort','slice'].indexOf(nom)>=0)nom='$'+nom;
    var _d = this.tipo.$$.prototype[nom];
    if (_d===undefined) {
      //Let's just look for this thing everywhere
      for (var $k in this.tipo.$$.prototype) {
        var propname='$prop$get'+$k[0].toUpperCase()+$k.substring(1);
        var m$ = this.tipo.$$.prototype[propname] ? undefined: this.tipo.$$.prototype[$k];
        _d = typeof(m$)==='function' && m$.$$===undefined ? getrtmm$$(m$) : undefined;
        if (_d && _d.d && _d.d[_d.d.length-1]===nom){
          _d = this.tipo.$$.prototype[$k];
          break;
        }else _d=undefined;
      }
    }
    if(_d){
      if (noInherit) {
        var mm=getrtmm$$(_d);
        if (mm.$cont!==this.tipo)return null;
      }
      _m=OpenFunction(this.containingPackage, _d);
    }
  }
  if (!_m && extendsType($$$mptypes.Kind$getMemberDeclaration, {t:ClassOrInterfaceDeclaration$meta$declaration})) {
    var nom=name$20+'$'+this.name;
    var _d = this.tipo.$$.prototype[nom];
    if (_d===undefined) {
      //Let's just look for this thing everywhere
      for (var $k in this.tipo.$$.prototype) {
        var propname='$prop$get'+$k[0].toUpperCase()+$k.substring(1);
        var m$ = this.tipo.$$.prototype[propname] ? undefined: this.tipo.$$.prototype[$k];
        _d = typeof(m$)==='function' && m$.$$ ? getrtmm$$(m$) : undefined;
        if (_d && _d.d && _d.d[_d.d.length-1]===nom){
          _d = this.tipo.$$.prototype[$k];
          break;
        }else _d=undefined;
      }
    }
    if (!_d) {
      if (noInherit)return null;
      var pere=this.tipo.$crtmm$['super'];
      while (!_d && pere) {
        var mm=getrtmm$$(pere.t);
        nom=mm&&mm.d?name$20+'$'+mm.d[mm.d.length-1]:undefined;
        if(nom)_d=this.tipo.$$.prototype[nom];
        if (!_d)pere=mm['super'];
      }
    }
    if(_d){
      var wantsClass=extendsType($$$mptypes.Kind$getMemberDeclaration,{t:ClassDeclaration$meta$declaration});
      var wantsIface=extendsType($$$mptypes.Kind$getMemberDeclaration,{t:InterfaceDeclaration$meta$declaration});
      var _$m = getrtmm$$(_d);
      var _mdl=get_model(_$m);
      if ((wantsClass && _mdl.$mt!=='c') || (wantsIface && _mdl.$mt!=='i'))return null;
      _m=(_mdl.$mt==='c'?OpenClass:OpenInterface)(this.containingPackage, _d);
    }
  }
  if (_m) {
    _m.$parent=this;
    return _m;
  }
  return null;
};
ClassOrInterfaceDeclaration$meta$declaration.$$.prototype.getMemberDeclaration.$crtmm$=function(){return{mod:$CCMM$,$t:{ t:'u', l:[{t:Null},'Kind']},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}}],$cont:OpenClass,$tp:{Kind:{'satisfies':[{t:NestableDeclaration$meta$declaration}]}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassOrInterfaceDeclaration','$m','getMemberDeclaration']};};
ClassOrInterfaceDeclaration$meta$declaration.$$.prototype.getDeclaredMemberDeclaration=function(nm,$mptypes){
  return this.getMemberDeclaration(nm,{Kind$getMemberDeclaration:$mptypes.Kind$getDeclaredMemberDeclaration},1);
}
ClassOrInterfaceDeclaration$meta$declaration.$$.prototype.getDeclaredMemberDeclaration.$crtmm$=function(){return{mod:$CCMM$,$t:{ t:'u', l:[{t:Null},'Kind']},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}}],$cont:OpenClass,$tp:{Kind:{'satisfies':[{t:NestableDeclaration$meta$declaration}]}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassOrInterfaceDeclaration','$m','getDeclaredMemberDeclaration']};};
$defat(FunctionalDeclaration$meta$declaration.$$.prototype,'parameterDeclarations',function(){
  var that=this;
  var parms = that.tipo.$crtmm$.$ps;
  if (!parms || parms.length === 0)return getEmpty();
  var rv = [];
  for (var i=0; i<parms.length;i++) {
    var p = parms[i];
    if (p.$pt === 'f') {
      rv.push(FunParamDecl(this,p));
    } else {
      rv.push(ValParamDecl(this,p));
    }
  }
  return ArraySequence(rv,{Element$Iterable:{t:FunctionOrValueDeclaration$meta$declaration},Absent$Iterable:{t:Null}});
},undefined,function(){return{mod:$CCMM$,$t:{t:Sequential,a:{Element$Sequential:{t:FunctionOrValueDeclaration$meta$declaration}}},$cont:FunctionalDeclaration$meta$declaration,$an:function(){return[shared(),formal()];},d:['ceylon.language.meta.declaration','FunctionalDeclaration','$at','parameterDeclarations']};});
ClassOrInterfaceDeclaration$meta$declaration.$$.prototype.memberDeclarations=function($$$mptypes){
  var filter;
  var defs=[];
  if (extendsType({t:FunctionDeclaration$meta$declaration},$$$mptypes.Kind$memberDeclarations)) {
    for (var df in this.meta['$m']) {
      defs.push(this.meta['$m'][df]);
    }
  }
  if (extendsType({t:ValueDeclaration$meta$declaration},$$$mptypes.Kind$memberDeclarations)) {
    for (var df in this.meta['$at']) {
      defs.push(this.meta['$at'][df]);
    }
  }
  if (extendsType({t:InterfaceDeclaration$meta$declaration},$$$mptypes.Kind$memberDeclarations)) {
    for (var df in this.meta['$i']) {
      defs.push(this.meta['$i'][df]);
    }
  }
  if (extendsType({t:ClassDeclaration$meta$declaration},$$$mptypes.Kind$memberDeclarations)) {
    for (var df in this.meta['$c']) {
      defs.push(this.meta['$c'][df]);
    }
  }
  var pkg = this.containingPackage;
  var r=[];
  for (var i=0; i<defs.length; i++) {
    var m=defs[i];
    var mt = m['$mt'];
    var _d;
    if (mt === 'm') {
      _d=this.getMemberDeclaration(m.$nm, {Kind$getMemberDeclaration:{t:FunctionDeclaration$meta$declaration}});
    } else if (mt==='c') {
      _d=this.getMemberDeclaration(m.$nm, {Kind$getMemberDeclaration:{t:ClassDeclaration$meta$declaration}});
    } else if (mt==='i') {
      _d=this.getMemberDeclaration(m.$nm, {Kind$getMemberDeclaration:{t:InterfaceDeclaration$meta$declaration}});
    } else if (mt==='a'||mt==='g'||mt==='o'||mt==='s') {
      _d=this.getMemberDeclaration(m.$nm, {Kind$getMemberDeclaration:{t:ValueDeclaration$meta$declaration}});
    }
    if (_d) {
      _d.$parent=this;
      r.push(_d);
    }
  }
  return ArraySequence(r,{Element$Iterable:$$$mptypes.Kind$memberDeclarations});
};
ClassOrInterfaceDeclaration$meta$declaration.$$.prototype.memberDeclarations.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Sequential,a:{Element$Sequential:'Kind'}},$ps:[],$cont:OpenInterface,$tp:{Kind:{'satisfies':[{t:NestableDeclaration$meta$declaration}]}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassOrInterfaceDeclaration','$m','memberDeclarations']};};
$defat(ClassOrInterfaceDeclaration$meta$declaration.$$.prototype,'caseTypes',function(){
  var casos = this.tipo.$crtmm$.of;
  if (casos && casos.length > 0) {
    var ct=[]; var ta=[];
    for (var i=0; i < casos.length; i++) {
      if (typeof(casos[i])==='string') {
        ct.push(OpenTvar(OpenTypeParam(this, casos[i])));
        if (!ta.contains(OpenTypeVariable$meta$declaration))ta.push(OpenTypeVariable$meta$declaration);
      } else {
        var ot=_openTypeFromTarg(casos[i]);
        ct.push(ot);
        if ($is(ot,{t:OpenClassType$meta$declaration}) && !ta.contains(OpenClassType$meta$declaration))ta.push(OpenClassType$meta$declaration);
        else if ($is(ot,{t:OpenInterfaceType$meta$declaration})&&!ta.contains(OpenInterfaceType$meta$declaration))ta.push(OpenInterfaceType$meta$declaration);
      }
    }
    if (ta.length===0)ta={t:OpenType$meta$declaration};
    else if (ta.length===1)ta={t:ta[0]};
    else {
      for (var i=0;i<ta.length;i++)ta[i]={t:ta[i]};
      ta={t:'u',l:ta};
    }
    return ArraySequence(ct,{Element$Iterable:ta});
  }
  return getEmpty();
},undefined,function(){return{mod:$CCMM$,$t:{t:Sequential,a:{Element$Sequential:{t:OpenType$meta$declaration}}},$cont:ClassOrInterfaceDeclaration$meta$declaration,$an:function(){return[shared(),formal()];},d:['ceylon.language.meta.declaration','ClassOrInterfaceDeclaration','$at','caseTypes']};});
ClassOrInterfaceDeclaration$meta$declaration.$$.prototype.$apply=function(types,$mptypes){
  var _t = {t:this.tipo};
  var _m = getrtmm$$(this.tipo);
  validate$typeparams(_t,_m.$tp,types);
  if (!extendsType(_t, $mptypes.Type$apply))
    throw IncompatibleTypeException$meta$model(String$("Type argument for 'Type' must be a supertype of " + this));
  var rv=this.meta.$mt==='i'?AppliedInterface(_t.t, {Type$Interface:$mptypes.Type$apply}):
    AppliedClass(_t.t, {Type$Class:$mptypes.Type$apply,Arguments$Class:$mptypes.Arguments$apply});
  if (_t.a)rv.$targs=_t.a;
  return rv;
}
ClassOrInterfaceDeclaration$meta$declaration.$$.prototype.$apply.$crtmm$=function(){return{mod:$CCMM$,d:['ceylon.language.meta.declaration','ClassOrInterfaceDeclaration','$m','apply']};};
ClassOrInterfaceDeclaration$meta$declaration.$$.prototype.memberApply=function(cont, types,$mptypes) {
  var mm=getrtmm$$(this.tipo);
  if (!extendsType({t:cont.tipo},{t:mm.$cont}))throw IncompatibleTypeException$meta$model("Incompatible Container type");
  var _t = {t:this.tipo};
  validate$typeparams(_t,mm.$tp,types);
  if (!extendsType(_t, $mptypes.Type$memberApply))
    throw IncompatibleTypeException$meta$model(String$("Type argument for 'Type' must be a supertype of " + this));
  var rv=this.meta.$mt==='i'?AppliedMemberInterface(_t.t, {Container$MemberInterface:$mptypes.Container$memberApply, Type$MemberInterface:_t})
    :AppliedMemberClass(_t.t, {Container$MemberClass:$mptypes.Container$memberApply, Type$MemberClass:_t, Arguments$MemberClass:$mptypes.Arguments$memberApply});
  if (_t.a)rv.$targs=_t.a;
  return rv;
};ClassOrInterfaceDeclaration$meta$declaration.$$.prototype.memberApply.$crtmm$=function(){return{mod:$CCMM$,d:['ceylon.language.meta.declaration','ClassOrInterfaceDeclaration','$m','memberApply']};};
$defat(ClassOrInterfaceDeclaration$meta$declaration.$$.prototype,'container',function(){
  if (this.$parent)return this.$parent;
  var mm=getrtmm$$(this.tipo);
  if (mm.$cont) {
    return typeLiteral$meta({Type$typeLiteral:mm.$cont});
  }
  return this.containingPackage;
},undefined,function(){return{mod:$CCMM$,$t:{t:'u',l:[{t:NestableDeclaration$meta$declaration},{t:Package$meta$declaration}]},d:['ceylon.language.meta.declaration.NestableDeclaration','$at','container']};});
ClassOrInterfaceDeclaration$meta$declaration.$$.prototype.annotatedMemberDeclarations=function($$$mptypes,list){
  if (list===undefined)list=this.memberDeclarations({Kind$memberDeclarations:$$$mptypes.Kind$annotatedMemberDeclarations});
  if (list.length) {
    var rv=[];
    for (var i=0; i < list.length; i++) {
      var mm = getrtmm$$(list[i].tipo);
      var anns = mm&&mm.$an;
      if (typeof(anns)==='function'){anns=anns();mm.$an=anns;}
      if (anns) for (var j=0; j<anns.length; j++) {
        if ($is(anns[j],$$$mptypes.Annotation$annotatedMemberDeclarations)) {
          rv.push(list[i]);
          break;
        }
      }
    }
    if (rv.length)return ArraySequence(rv,{Element$Iterable:$$$mptypes.Kind$annotatedMemberDeclarations});
  }
  return getEmpty();
};
ClassOrInterfaceDeclaration$meta$declaration.$$.prototype.annotatedMemberDeclarations.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Sequential,a:{Element$Sequential:'Kind'}},$ps:[],$cont:ClassOrInterfaceDeclaration$meta$declarationl,$tp:{Kind:{'satisfies':[{t:NestableDeclaration$meta$declaration}]},Annotation:{}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassOrInterfaceDeclaration','$m','annotatedMemberDeclarations']};};
ClassOrInterfaceDeclaration$meta$declaration.$$.prototype.annotatedDeclaredMemberDeclarations=function ($$$mptypes){
  return this.annotatedMemberDeclarations({Kind$annotatedMemberDeclarations:$$$mptypes.Kind$annotatedDeclaredMemberDeclarations,
    Annotation$annotatedMemberDeclarations:$$$mptypes.Annotation$annotatedDeclaredMemberDeclarations},
    this.memberDeclarations({Kind$memberDeclarations:$$$mptypes.Kind$annotatedDeclaredMemberDeclarations}));
};
ClassOrInterfaceDeclaration$meta$declaration.$$.prototype.annotatedDeclaredMemberDeclarations.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Sequential,a:{Element$Sequential:'Kind'}},$ps:[],$cont:ClassOrInterfaceDeclaration$meta$declarationl,$tp:{Kind:{'satisfies':[{t:NestableDeclaration$meta$declaration}]},Annotation:{}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassOrInterfaceDeclaration','$m','annotatedDeclaredMemberDeclarations']};};

