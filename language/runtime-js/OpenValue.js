function OpenValue(pkg, meta, that){
  if (meta===undefined)throw Exception("Value reference not found. Metamodel doesn't work with modules compiled in lexical scope style");
  $init$OpenValue();
  if (that===undefined)that=new OpenValue.$$;
  that._pkg = pkg;
  var _mm=getrtmm$$(meta);
  if (_mm === undefined) {
    //it's a metamodel
    that.meta=meta;
    if (meta['mt']==='prm') {
      that.tipo={$crtmm$:meta};
      //TODO I think we need to do something else here
    } else {
      that.tipo=_findTypeFromModel(pkg,meta);
    }
    _mm = getrtmm$$(that.tipo);
  } else {
    //it's a type
    that.tipo = meta;
    that.meta = get_model(_mm);
  }
  that.name_=that.meta?that.meta.nm:_mm&&_mm.d&&_mm.d[_mm.d.length-1];
  that.toplevel_=_mm.$cont === undefined;
  ValueDeclaration$meta$declaration(that);
  return that;
}
OpenValue.$crtmm$=function(){return{mod:$CCMM$,'super':{t:Basic},sts:[{t:ValueDeclaration$meta$declaration}],d:['ceylon.language.meta.declaration','ValueDeclaration']};};
function $init$OpenValue(){
  if (OpenValue.$$===undefined){
    initTypeProto(OpenValue,'ceylon.language.meta.declaration::OpenValue',Basic,ValueDeclaration$meta$declaration);
    (function($$openValue){

atr$($$openValue,'container',function(){
  if (this.$parent)return this.$parent;
  if (this.toplevel)return this.containingPackage;
  return typeLiteral$meta({Type$typeLiteral:this.tipo.$crtmm$.$cont});
},undefined,function(){return{mod:$CCMM$,$t:{t:'u',l:[{t:NestableDeclaration$meta$declaration},{t:Package$meta$declaration}]},d:['ceylon.language.meta.declaration','NestableDeclaration','$at','container']};});
      $$openValue.$_apply=function $_apply($$$mptypes){
        var mm=getrtmm$$(this.tipo);
        if (!extendsType(mm.$t,$$$mptypes.Get$apply))throw IncompatibleTypeException$meta$model("Incompatible Get type argument");
        if (!extendsType($$$mptypes.Set$apply,this.tipo.set?mm.$t:{t:Nothing}))throw IncompatibleTypeException$meta$model("Incompatible Set type argument");
        return AppliedValue$jsint(undefined,this.tipo,{Get$Value:$$$mptypes.Get$apply,Set$Value:$$$mptypes.Set$apply});
      };$$openValue.$_apply.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Value$meta$model,a:{Type:{t:Anything}}},ps:[{nm:'instance',mt:'prm',def:1,$t:{t:Anything}}],$cont:OpenValue,pa:3,d:['ceylon.language.meta.declaration','ValueDeclaration','$m','apply']};};

      $$openValue.memberApply=function memberApply(cont,$mptypes) {
        var mm=getrtmm$$(this.tipo);
        if (!(cont.tipo && extendsType({t:cont.tipo},{t:mm.$cont}))&&cont!==getNothingType$meta$model())
          throw IncompatibleTypeException$meta$model("Incompatible Container type argument");
        if (!extendsType(mm.$t,$mptypes.Get$memberApply))throw IncompatibleTypeException$meta$model("Incompatible Get type argument");
        if (!extendsType($mptypes.Set$memberApply,this.tipo.set?mm.$t:{t:Nothing}))throw IncompatibleTypeException$meta$model("Incompatible Set type argument");
        return AppliedAttribute(this.meta.nm,this.tipo,{Get$Attribute:$mptypes.Get$apply,Set$Attribute:$mptypes.Set$apply,
          Container$Attribute:$mptypes.Container$memberApply});
      };$$openValue.memberApply.$crtmm$=function(){return{mod:$CCMM$,d:['ceylon.language.meta.declaration','ValueDeclaration','$m','memberApply']};};
      atr$($$openValue,'defaulted',function(){
        return false;
      },undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:OpenValue,pa:3,d:['ceylon.language.meta.declaration','ValueDeclaration','$at','defaulted']};});
      atr$($$openValue,'variadic',function(){
        return false;
      },undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:OpenValue,pa:3,d:['ceylon.language.meta.declaration','ValueDeclaration','$at','variadic']};});
      atr$($$openValue,'variable',function(){
        return (getrtmm$$(this.tipo).pa & 1024) > 0;
      },undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:OpenValue,pa:3,d:['ceylon.language.meta.declaration','ValueDeclaration','$at','variadic']};});
            
  atr$($$openValue,'openType',function(){
    if (this.tipo) {
      var mm = getrtmm$$(this.tipo);
      if (typeof(mm.$t)==='string') {
        return OpenTvar$jsint(OpenTypeParam$jsint(mm.$cont,mm.$t));
      }
      return _openTypeFromTarg(mm.$t, this.tipo);
    }
    throw Error("OpenValue.openType-we don't have a metamodel!");
  },undefined,function(){return{mod:$CCMM$,$t:{t:OpenType$meta$declaration},$cont:OpenValue,pa:3,d:['ceylon.language.meta.declaration','ValueDeclaration','$at','openType']};});

atr$($$openValue,'name',function(){return this.name_;},undefined,function(){return{mod:$CCMM$,$t:{t:$_String},$cont:OpenValue,pa:3,d:['ceylon.language.meta.declaration','ValueDeclaration','$at','name']};});
atr$($$openValue,'containingPackage',function(){return this._pkg;},undefined,function(){return{mod:$CCMM$,$t:{t:Package$meta$declaration},$cont:OpenValue,pa:3,d:['ceylon.language.meta.declaration','ValueDeclaration','$at','containingPackage']};});
atr$($$openValue,'containingModule',function(){return this._pkg.container;},undefined,function(){return{mod:$CCMM$,$t:{t:Module$meta$declaration},$cont:OpenValue,pa:3,d:['ceylon.language.meta.declaration','ValueDeclaration','$at','containingModule']};});
atr$($$openValue,'toplevel',function(){return this.toplevel_;},undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:OpenValue,pa:3,d:['ceylon.language.meta.declaration','ValueDeclaration','$at','toplevel']};});

  $$openValue.equals=function(o) {
    if (is$(o,{t:OpenValue}))return this.meta===o.meta;
    if (is$(o,{t:ValueDeclaration$meta$declaration})&&o.qualifiedName.equals(this.qualifiedName)&&o.shared==this.shared&&o.actual==this.actual&&o.formal==this.formal&&o.$_default==this.$_default&&o.variable==this.variable&&o.toplevel==this.toplevel&&o.openType.equals(this.openType)) {
      var _mc=this.container,_oc=o.container;
      return _mc===null?_oc===null:_mc.equals(_oc);
    }
    return false;
  }
  atr$($$openValue,'string',function(){return "value " + this.qualifiedName;},undefined,{an:function(){return[shared(),actual()]},mod:$CCMM$,d:['$','Object','$at','string']});
  atr$($$openValue,'qualifiedName',function(){
    return qname$(this.tipo);
  },undefined,function(){return{mod:$CCMM$,$t:{t:$_String},$cont:OpenValue,pa:3,d:['ceylon.language.meta.declaration','Declaration','$at','qualifiedName']};});
    $$openValue.memberSet=function(c,v) {
      if (!is$(c,{t:this.tipo.$crtmm$.$cont}))throw IncompatibleTypeException$meta$model("Incompatible container type");
      if (!is$(v,this.tipo.$crtmm$.$t))throw IncompatibleTypeException$meta$model("Incompatible value type");
      if (!this.tipo.set)throw MutationException$meta$model(qname$(this.tipo.$crtmm$)+" is not writable");
      c[this.name]=v;
    };
    atr$($$openValue,'setter',function(){
      return OpenSetter(this);
    },undefined,function(){return{mod:$CCMM$,$t:{t:SetterDeclaration$meta$declaration},$cont:OpenValue,pa:3,d:['ceylon.language.meta.declaration','ValueDeclaration','$at','setter']};});

    atr$($$openValue,'objectValue',function(){
      return this.meta&&this.meta.mt==='o';
    },undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:OpenValue,pa:3,d:['ceylon.language.meta.declaration','ValueDeclaration','$at','objectValue']};});
    atr$($$openValue,'objectClass',function(){
      if (this.objectValue) {
        var _m=getrtmm$$(this.tipo);
        if (_m && _m.$t && typeof(_m.$t.t)==='function')return OpenClass$jsint(this._pkg,_m.$t.t);
      }
      return null;
    },undefined,function(){return{mod:$CCMM$,$t:{t:'u',l:[{t:ClassDeclaration$meta$declaration},{t:Null}]},$cont:OpenValue,pa:3,d:['ceylon.language.meta.declaration','ValueDeclaration','$at','objectClass']};});

    })(OpenValue.$$.prototype);
  }
  return OpenValue;
}
ex$.OpenValue=OpenValue;
$init$OpenValue();
