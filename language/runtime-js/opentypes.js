function find$ann(cont,ant) {
  var _m=getrtmm$$(cont);
  if (!(_m && _m.$an))return null;
  if (typeof(_m.$an)==='function')_m.$an=_m.$an();
  for (var i=0; i < _m.$an.length; i++) {
    if (is$(_m.$an[i],{t:ant}))return _m.$an[i];
  }
  return null;
}
//Find the real declaration of something from its model definition
function _findTypeFromModel(pkg,mdl,cont) {
  var mod = pkg.container;
  //TODO this is very primitive needs a lot of rules replicated from the JsIdentifierNames
  var nm = mdl.$nm;
  var mt = mdl['$mt'];
  if (mt === 'a' || mt === 'g' || mt === 'o' || mt === 's') {
    nm = '$prop$get' + nm[0].toUpperCase() + nm.substring(1);
  }
  if (cont) {
    var imm=getrtmm$$(cont);
    if (mt==='c'||mt==='i')nm=nm+'$'+imm.d[imm.d.length-1];
  }else if (pkg.suffix) {
    nm+=pkg.suffix;
  }
  var out=cont?cont.$$.prototype:mod.meta;
  var rv=out[nm];
  if (rv===undefined)rv=out['$_'+nm];
  if (rv===undefined){
    rv=out['$init$'+nm];
    if (typeof(rv)==='function')rv=rv();
  }
  return rv;
}
//Pass a {t:Bla} and get a FreeClass,FreeInterface,etc (OpenType).
//second arg is the container for the second argument, if any
function _openTypeFromTarg(targ,o) {
  if (targ.t==='u' || targ.t==='i') {
    var tl=[];
    for (var i=0; i < targ.l.length; i++) {
      var _ct=targ.l[i];
      if (typeof(_ct)==='string') {
        tl.push(OpenTvar$jsint(OpenTypeParam$jsint(o,_ct)));
      } else {
        tl.push(_ct.t?_openTypeFromTarg(_ct,o):_ct);
      }
    }
    return (targ.t==='u'?FreeUnion$jsint:FreeIntersection$jsint)(tl.reifyCeylonType({t:OpenType$meta$declaration}));
  } else if (targ.t==='T') {
    var mm=getrtmm$$(Tuple);
    var lit = typeLiteral$meta({Type$typeLiteral:targ});
  } else {
    var mm=getrtmm$$(targ.t);
    var lit = typeLiteral$meta({Type$typeLiteral:targ.t});
  }
  if (targ.a && lit)lit._targs=targ.a;
  var mdl = get_model(mm);
  if (mdl.$mt==='i') {
    return FreeInterface(lit);
  } else if (mdl.$mt==='c' || mdl.$mt==='o') {
    return FreeClass(lit);
  }
  console.log("Don't know WTF to return for " + lit + " metatype " + mdl.$mt);
}

//ClassDefinition FreeClass at X (161:0-168:0)
function FreeClass(declaration, $$freeClass){
  $init$FreeClass();
  if ($$freeClass===undefined)$$freeClass=new FreeClass.$$;
  OpenClassType$meta$declaration($$freeClass);
  $$freeClass._decl=declaration;
  $$freeClass.$prop$getDeclaration={$crtmm$:function(){return{mod:$CCMM$,$t:{t:ClassDeclaration$meta$declaration},$cont:FreeClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','OpenClassType','$at','declaration']};}};
  $$freeClass.$prop$getDeclaration.get=function(){return declaration};
  return $$freeClass;
}
FreeClass.$crtmm$=function(){return{mod:$CCMM$,'super':{t:Basic},satisfies:[{t:OpenClassType$meta$declaration}],d:['ceylon.language.meta.declaration','OpenClassType']};};
function $init$FreeClass(){
  if (FreeClass.$$===undefined){
   initTypeProto(FreeClass,'ceylon.language.meta.declaration::FreeClass',Basic,OpenClassType$meta$declaration);
   (function($$freeClass){
    $$freeClass.equals=function(other) {
      return is$(other,{t:FreeClass}) && other.declaration.equals(this.declaration) && this.typeArguments.equals(other.typeArguments);
    }
   })(FreeClass.$$.prototype);
  }
  return FreeClass;
}
ex$.$init$FreeClass=$init$FreeClass;
$init$FreeClass();

//ClassDefinition FreeInterface at X (170:0-177:0)
function FreeInterface(declaration, $$freeInterface){
    $init$FreeInterface();
    if ($$freeInterface===undefined)$$freeInterface=new FreeInterface.$$;
    OpenInterfaceType$meta$declaration($$freeInterface);
    
    //AttributeDeclaration declaration at X (173:4-173:50)
    $$freeInterface._decl=declaration;
    $$freeInterface.$prop$getDeclaration.get=function(){return declaration};
    return $$freeInterface;
}
FreeInterface.$crtmm$=function(){return{mod:$CCMM$,'super':{t:Basic},satisfies:[{t:OpenInterfaceType$meta$declaration}],d:['ceylon.language.meta.declaration','OpenInterfaceType']};};
function $init$FreeInterface(){
  if (FreeInterface.$$===undefined){
    initTypeProto(FreeInterface,'ceylon.language.meta.declaration::FreeInterface',Basic,OpenInterfaceType$meta$declaration);
    (function($$freeInterface){
    $$freeInterface.equals=function(other) {
      return is$(other,{t:FreeInterface}) && other.declaration.equals(this.declaration) && this.typeArguments.equals(other.typeArguments);
    }
            
    })(FreeInterface.$$.prototype);
  }
  return FreeInterface;
}
ex$.$init$FreeInterface=$init$FreeInterface;
$init$FreeInterface();

//ClassDefinition OpenFunction at X (18:0-36:0)
function OpenFunction(pkg,meta,that){
    if (meta===undefined)throw Exception("Function reference not found. Metamodel doesn't work with modules compiled in lexical scope style");
    $init$OpenFunction();
    if (that===undefined)that=new OpenFunction.$$;
    that._pkg=pkg;
    var _mm=getrtmm$$(meta);
    if (_mm === undefined) {
      //it's a metamodel
      that.meta=meta;
      that.tipo=_findTypeFromModel(pkg,meta);
      _mm=getrtmm$$(that.tipo);
    } else {
      //it's a type
      that.tipo = meta;
      that.meta = get_model(_mm);
    }
    that.name_=(that.meta&&that.meta.$nm)||'?';
    that.toplevel_=_mm===undefined||_mm.$cont===undefined;
    FunctionDeclaration$meta$declaration(that);
    return that;
}
OpenFunction.$crtmm$=function(){return{mod:$CCMM$,'super':{t:Basic},satisfies:[{t:FunctionDeclaration$meta$declaration}],d:['ceylon.language.meta.declaration','FunctionDeclaration']};};
function $init$OpenFunction(){
  if (OpenFunction.$$===undefined){
    initTypeProto(OpenFunction,'ceylon.language.meta.declaration::OpenFunction',Basic,FunctionDeclaration$meta$declaration);
    (function($$openFunction){
$$openFunction.equals=function(o) {
  if (is$(o,{t:OpenFunction})) {
    return o.tipo === this.tipo;
  }
  return false;
}
$$openFunction.equals.$crtmm$=function(){return{mod:$CCMM$,$t:{t:$_Boolean},d:['$','Object','$m','equals']};}
atr$($$openFunction,'container',function(){
  if (this.$parent)return this.$parent;
  if (this.toplevel)return this.containingPackage;
  return typeLiteral$meta({Type$typeLiteral:this.tipo.$crtmm$.$cont});
},undefined,function(){return{mod:$CCMM$,$t:{t:'u',l:[{t:NestableDeclaration$meta$declaration},{t:Package$meta$declaration}]},d:['ceylon.language.meta.declaration','NestableDeclaration','$at','container']};});
atr$($$openFunction,'annotation',function(){
  return find$ann(this.tipo,AnnotationAnnotation)!==null;
},undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionalDeclaration','$at','annotation']};});

      $$openFunction.$_apply=function $_apply(types,$mptypes){
        var mm=this.tipo.$crtmm$;
        var ta={t:this.tipo};
        validate$typeparams(ta,mm.$tp,types);
        validate$params(mm.$ps,$mptypes.Arguments$apply,"Wrong number of arguments when applying function");
        return ta.a?AppliedFunction(this.tipo,{Type:mm.$t,Arguments:tupleize$params(mm.$ps,ta.a)},undefined,ta.a):
          AppliedFunction(this.tipo,{Type:mm.$t,Arguments:tupleize$params(mm.$ps)});
      };$$openFunction.$_apply.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Function$meta$model,a:{Arguments:{t:Nothing},Type:{t:Anything}}},$ps:[{$nm:'types',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element$Sequential:{t:Type$meta$model}}}}],$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$m','apply']};};

      $$openFunction.memberApply=function memberApply(cont,types,$mptypes){
        var mm=this.tipo.$crtmm$;
        if (!(cont.tipo && extendsType({t:cont.tipo},{t:mm.$cont}))&&cont!==getNothingType$meta$model())
          throw IncompatibleTypeException$meta$model("Incompatible Container type argument");
        if (!extendsType(mm.$t,$mptypes.Return$memberApply))throw IncompatibleTypeException$meta$model("Incompatible Return type argument");
        validate$params(mm.$ps,$mptypes.Arguments$memberApply,"Wrong number of Arguments for memberApply");
        var ta={t:this.tipo};
        validate$typeparams(ta,mm.$tp,types);
        return AppliedMethod(this.tipo,types,{Container$Method:$mptypes.Container$memberApply,
          Type$Method:mm.$t,Arguments$Method:tupleize$params(mm.$ps,ta.a)});
      };$$openFunction.memberApply.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Method$meta$model,a:{Arguments:'Arguments',Type:'MethodType',Container:'Container'}},$ps:[{$nm:'types',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element$Sequential:{t:Type$meta$model}}}}],$cont:OpenFunction,$tp:{Container:{},MethodType:{},Arguments:{'satisfies':[{t:Sequential,a:{Element$Sequential:{t:Anything}}}]}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$m','memberApply']};};
            
            //AttributeDeclaration defaulted at X (25:4-25:44)
            atr$($$openFunction,'defaulted',function(){
                return false;
            },undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','defaulted']};});
            
            //AttributeDeclaration variadic at X (26:4-26:43)
            atr$($$openFunction,'variadic',function(){
                var $$openFunction=this;
                return false;
            },undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','variadic']};});
    $$openFunction.getParameterDeclaration=function (name$6){
      var pd=this.parameterDeclarations;
      for (var i=0; i < pd.size; i++) {
        if (name$6.equals(pd[i].name))return pd[i];
      }
      return null;
    };
    $$openFunction.getParameterDeclaration.$crtmm$=function(){return{mod:$CCMM$,$t:{ t:'u', l:[{t:Null},{t:FunctionOrValueDeclaration$meta$declaration}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:$_String}}],$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$m','getParameterDeclaration']};};
            
            //AttributeGetterDefinition openType at X (33:2-33:43)
    atr$($$openFunction,'openType',function(){
      var t = this.tipo.$crtmm$.$t;
      if (typeof(t)==='string')return OpenTvar$jsint(OpenTypeParam$jsint(this.tipo,t));
      return _openTypeFromTarg(t, this.tipo);
    },undefined,function(){return{mod:$CCMM$,$t:{t:OpenType$meta$declaration},$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','openType']};});
            atr$($$openFunction,'name',function(){return this.name_;},undefined,function(){return{mod:$CCMM$,$t:{t:$_String},$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','name']};});
            atr$($$openFunction,'containingPackage',function(){return this._pkg;},undefined,function(){return{mod:$CCMM$,$t:{t:Package$meta$declaration},$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','containingPackage']};});
            atr$($$openFunction,'containingModule',function(){return this._pkg.container;},undefined,function(){return{mod:$CCMM$,$t:{t:Package$meta$declaration},$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','containingPackage']};});
            atr$($$openFunction,'toplevel',function(){return this.toplevel_;},undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','toplevel']};});

    atr$($$openFunction,'string',function(){return "function " + this.qualifiedName;},undefined,function(){return{mod:$CCMM$,$t:{t:$_String},$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Declaration','$at','string']};});
    atr$($$openFunction,'qualifiedName',function(){
       return $qname(this.tipo);
    },undefined,function(){return{mod:$CCMM$,$t:{t:$_String},$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Declaration','$at','qualifiedName']};});
    })(OpenFunction.$$.prototype);
  }
  return OpenFunction;
}
ex$.$init$OpenFunction=$init$OpenFunction;
$init$OpenFunction();

//ClassDefinition OpenValue at X (38:0-45:0)
function OpenValue(pkg, meta, that){
  if (meta===undefined)throw Exception("Value reference not found. Metamodel doesn't work with modules compiled in lexical scope style");
  $init$OpenValue();
  if (that===undefined)that=new OpenValue.$$;
  that._pkg = pkg;
  var _mm=getrtmm$$(meta);
  if (_mm === undefined) {
    //it's a metamodel
    that.meta=meta;
    if (meta['$mt']==='prm') {
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
  that.name_=that.meta?that.meta.$nm:_mm&&_mm.d&&_mm.d[_mm.d.length-1];
  that.toplevel_=_mm.$cont === undefined;
  ValueDeclaration$meta$declaration(that);
  return that;
}
OpenValue.$crtmm$=function(){return{mod:$CCMM$,'super':{t:Basic},satisfies:[{t:ValueDeclaration$meta$declaration}],d:['ceylon.language.meta.declaration','ValueDeclaration']};};
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
        return AppliedValue(undefined,this.tipo,{Get$Value:$$$mptypes.Get$apply,Set$Value:$$$mptypes.Set$apply});
      };$$openValue.$_apply.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Value$meta$model,a:{Type:{t:Anything}}},$ps:[{$nm:'instance',$mt:'prm',$def:1,$t:{t:Anything}}],$cont:OpenValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$m','apply']};};

      $$openValue.memberApply=function memberApply(cont,$mptypes) {
        var mm=getrtmm$$(this.tipo);
        if (!(cont.tipo && extendsType({t:cont.tipo},{t:mm.$cont}))&&cont!==getNothingType$meta$model())
          throw IncompatibleTypeException$meta$model("Incompatible Container type argument");
        if (!extendsType(mm.$t,$mptypes.Get$memberApply))throw IncompatibleTypeException$meta$model("Incompatible Get type argument");
        if (!extendsType($mptypes.Set$memberApply,this.tipo.set?mm.$t:{t:Nothing}))throw IncompatibleTypeException$meta$model("Incompatible Set type argument");
        return AppliedAttribute(this.meta.$nm,this.tipo,{Get$Attribute:$mptypes.Get$apply,Set$Attribute:$mptypes.Set$apply,
          Container$Attribute:$mptypes.Container$memberApply});
      };$$openValue.memberApply.$crtmm$=function(){return{mod:$CCMM$,d:['ceylon.language.meta.declaration','ValueDeclaration','$m','memberApply']};};
      atr$($$openValue,'defaulted',function(){
        return false;
      },undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:OpenValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','defaulted']};});
      atr$($$openValue,'variadic',function(){
        return false;
      },undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:OpenValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','variadic']};});
      atr$($$openValue,'variable',function(){
        var _m = get_model(getrtmm$$(this.tipo));
        if (_m && _m['var']) return true;
        return find$ann(this.tipo,VariableAnnotation)!==null;
      },undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:OpenValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','variadic']};});
            
  atr$($$openValue,'openType',function(){
    if (this.tipo) {
      var mm = getrtmm$$(this.tipo);
      if (typeof(mm.$t)==='string') {
        return OpenTvar$jsint(OpenTypeParam$jsint(mm.$cont,mm.$t));
      }
      return _openTypeFromTarg(mm.$t, this.tipo);
    }
    throw Error("OpenValue.openType-we don't have a metamodel!");
  },undefined,function(){return{mod:$CCMM$,$t:{t:OpenType$meta$declaration},$cont:OpenValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','openType']};});

atr$($$openValue,'name',function(){return this.name_;},undefined,function(){return{mod:$CCMM$,$t:{t:$_String},$cont:OpenValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','name']};});
atr$($$openValue,'containingPackage',function(){return this._pkg;},undefined,function(){return{mod:$CCMM$,$t:{t:Package$meta$declaration},$cont:OpenValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','containingPackage']};});
atr$($$openValue,'containingModule',function(){return this._pkg.container;},undefined,function(){return{mod:$CCMM$,$t:{t:Module$meta$declaration},$cont:OpenValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','containingModule']};});
atr$($$openValue,'toplevel',function(){return this.toplevel_;},undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:OpenValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','toplevel']};});

  $$openValue.equals=function(other) {
    if (is$(other, {t:OpenValue}) && other.name.equals(this.name) && other.toplevel===this.toplevel && other.containingPackage.equals(this.containingPackage)) {
      return other.meta==this.meta;
    }
    return false;
  }
  atr$($$openValue,'string',function(){return "value " + this.qualifiedName;},undefined,{$an:function(){return[shared(),actual()]},mod:$CCMM$,d:['$','Object','$at','string']});
  atr$($$openValue,'qualifiedName',function(){
    return $qname(this.tipo);
  },undefined,function(){return{mod:$CCMM$,$t:{t:$_String},$cont:OpenValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Declaration','$at','qualifiedName']};});
    $$openValue.memberSet=function(c,v) {
      if (!is$(c,{t:this.tipo.$crtmm$.$cont}))throw IncompatibleTypeException$meta$model("Incompatible container type");
      if (!is$(v,this.tipo.$crtmm$.$t))throw IncompatibleTypeException$meta$model("Incompatible value type");
      if (!this.tipo.set)throw MutationException$meta$model($qname(this.tipo.$crtmm$)+" is not writable");
      c[this.name]=v;
    };
    atr$($$openValue,'setter',function(){
      return OpenSetter(this);
    },undefined,function(){return{mod:$CCMM$,$t:{t:SetterDeclaration$meta$declaration},$cont:OpenValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','setter']};});

    atr$($$openValue,'objectValue',function(){
      return this.meta&&this.meta.$mt==='o';
    },undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:OpenValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','objectValue']};});
    atr$($$openValue,'objectClass',function(){
      if (this.objectValue) {
        var _m=getrtmm$$(this.tipo);
        if (_m && _m.$t && typeof(_m.$t.t)==='function')return OpenClass(this._pkg,_m.$t.t);
      }
      return null;
    },undefined,function(){return{mod:$CCMM$,$t:{t:'u',l:[{t:ClassDeclaration$meta$declaration},{t:Null}]},$cont:OpenValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','objectClass']};});

    })(OpenValue.$$.prototype);
  }
  return OpenValue;
}
ex$.$init$OpenValue=$init$OpenValue;
$init$OpenValue();

//ClassDefinition OpenSetter at opentypes.ceylon (63:0-63:90)
function OpenSetter(v, $$openSetter){
  $init$OpenSetter();
  if ($$openSetter===undefined)$$openSetter=new OpenSetter.$$;
  $$openSetter.variable_=v;
  SetterDeclaration$meta$declaration($$openSetter);
  $$openSetter.tipo=v.tipo.set;
  if (v.tipo.set && getrtmm$$(v.tipo.set)) {
    var mm=getrtmm$$(v.tipo.set)
    if (typeof(mm.$an)==='function')mm.$an=mm.$an();
    v.tipo.set.$crtmm$=mm;
  }
  return $$openSetter;
}
OpenSetter.$crtmm$=function(){return{mod:$CCMM$,'super':{t:Basic},satisfies:[{t:SetterDeclaration$meta$declaration}],d:['ceylon.language.meta.declaration','SetterDeclaration']};};
function $init$OpenSetter(){
  if (OpenSetter.$$===undefined){
    initTypeProto(OpenSetter,'ceylon.language.meta.declaration::OpenSetter',Basic,SetterDeclaration$meta$declaration);
    (function($$openSetter){
      atr$($$openSetter,'variable',function(){return this.variable_;},undefined,function(){return{mod:$CCMM$,$t:{t:ValueDeclaration$meta$declaration},$cont:OpenSetter,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','SetterDeclaration','$at','variable']};});
      atr$($$openSetter,'name',function(){return this.variable_.name;},undefined,function(){return{mod:$CCMM$,$t:{t:$_String},$cont:OpenSetter,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','SetterDeclaration','$at','name']};});
atr$($$openSetter,'name',function(){return this.variable.name;},undefined,function(){return{mod:$CCMM$,$t:{t:$_String},$cont:OpenSetter,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','name']};});
  atr$($$openSetter,'string',function(){return "setter " + this.qualifiedName;},undefined,function(){return{$t:{t:$_String},$cont:OpenSetter,$an:function(){return[shared(),actual()]},mod:$CCMM$,d:['$','Object','$at','string']};});
  atr$($$openSetter,'qualifiedName',function(){return this.variable.qualifiedName;},undefined,function(){return{mod:$CCMM$,$t:{t:$_String},$cont:OpenSetter,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Declaration','$at','qualifiedName']};});
      $$openSetter.equals=function(o) {
        return is$(o,{t:OpenSetter}) && o.variable.equals(this.variable);
      }
    })(OpenSetter.$$.prototype);
  }
  return OpenSetter;
}
ex$.$init$OpenSetter=$init$OpenSetter;
$init$OpenSetter();

//ClassDefinition OpenClass at X (47:0-70:0)
function OpenClass(pkg, meta, that){
    if (meta===undefined)throw Exception("Class reference not found. Metamodel doesn't work with modules compiled in lexical scope style");
    $init$OpenClass();
    if (that===undefined)that=new OpenClass.$$;
    that._pkg = pkg;
    var _mm=getrtmm$$(meta);
    if (_mm === undefined) {
      //it's a metamodel
      that.meta=meta;
      that.tipo=_findTypeFromModel(pkg,meta);
      _mm = getrtmm$$(that.tipo);
    } else {
      //it's a type
      that.tipo = meta;
      that.meta = get_model(_mm);
    }
    that.name_=(that.meta&&that.meta.$nm)||_mm.d[_mm.d.length-1];
    that.toplevel_=_mm.$cont===undefined;
    ClassDeclaration$meta$declaration(that);
    return that;
}
OpenClass.$crtmm$=function(){return{mod:$CCMM$,'super':{t:Basic},satisfies:[{t:ClassDeclaration$meta$declaration}],d:['ceylon.language.meta.declaration','ClassDeclaration']};};
function $init$OpenClass(){
  if (OpenClass.$$===undefined){
    initTypeProto(OpenClass,'ceylon.language.meta.declaration::OpenClass',Basic,ClassDeclaration$meta$declaration);
    (function($$openClass){

$$openClass.classApply=function(targs,$mptypes) {
  var mm=this.tipo.$crtmm$;
  if (mm.$tp) {
    if (!targs)throw TypeApplicationException$meta$model("This class requires type arguments");
    //TODO generate targs
  }
  validate$params(mm.$ps,$mptypes.Arguments$classApply,"Wrong number of Arguments for classApply");
  return this.$_apply(targs,$mptypes);
}
$$openClass.memberClassApply=function(cont,targs,$mptypes){
  var mm=this.tipo.$crtmm$;
  if (cont!==getNothingType$meta$model() && !extendsType({t:cont.tipo},{t:mm.$cont}))
    throw IncompatibleTypeException$meta$model("Incompatible Container specified");
  if (!extendsType({t:this.tipo},$mptypes.Type$memberClassApply))
    throw IncompatibleTypeException$meta$model("Incompatible Type specified");
  var _t={t:this.tipo};
  validate$typeparams(_t,mm.$tp,targs);
  validate$params(mm.$ps,$mptypes.Arguments$memberClassApply,"Wrong number of Arguments for classApply");
  var rv=AppliedMemberClass(this.tipo,{Container$MemberClass:{t:mm.$cont},Type$MemberClass:_t,Arguments$MemberClass:tupleize$params(mm.$ps)});
  if (_t.a)rv.$targs=_t.a;
  return rv;
}

      atr$($$openClass,'string',function(){
        return "class " + this.qualifiedName;
      },undefined,function(){return{mod:$CCMM$,$t:{t:$_String},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['$','Object']};}); 
      atr$($$openClass,'anonymous',function(){
        return this.meta.$mt==='o';
      },undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassDeclaration','$at','anonymous']};});
      atr$($$openClass,'abstract',function(){
        return find$ann(this.tipo,AbstractAnnotation)!==null;
      },undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassDeclaration','$at','abstract']};});
      atr$($$openClass,'$_final',function(){
        return find$ann(this.tipo,FinalAnnotation)!==null;
      },undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassDeclaration','$at','final']};});
      atr$($$openClass,'annotation',function(){
        return find$ann(this.tipo,AnnotationAnnotation)!==null;
      },undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionalDeclaration','$at','annotation']};});
           
            //AttributeGetterDefinition openType at X (61:2-61:43)
            atr$($$openClass,'openType',function(){
              return FreeClass(this);
            },undefined,function(){return{mod:$CCMM$,$t:{t:OpenType$meta$declaration},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassDeclaration','$at','openType']};});
            
            $$openClass.getParameterDeclaration=function (name$15){
      var pd=this.parameterDeclarations;
      for (var i=0; i < pd.size; i++) {
        if (name$6.equals(pd[i].name))return pd[i];
      }
      return null;
            };
            $$openClass.getParameterDeclaration.$crtmm$=function(){return{mod:$CCMM$,$t:{ t:'u', l:[{t:Null},{t:FunctionOrValueDeclaration$meta$declaration}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:$_String}}],$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassDeclaration','$m','getParameterDeclaration']};};
            
            //AttributeDeclaration superclassDeclaration at X (68:2-68:86)
            atr$($$openClass,'extendedType',function(){
              var sc = this.tipo.$crtmm$['super'];
              if (sc === undefined)return null;
              var mm = getrtmm$$(sc.t);
              var fc=FreeClass(OpenClass(getModules$meta().find(mm.mod['$mod-name'],mm.mod['$mod-version']).findPackage(mm.d[0]), sc.t));
              if (sc.a)fc.declaration._targs=sc.a;
              return fc;
            },undefined,function(){return{mod:$CCMM$,$t:{ t:'u', l:[{t:Null},{t:OpenClassType$meta$declaration}]},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassDeclaration','$at','extendedType']};});
            
            //AttributeDeclaration interfaceDeclarations at X (69:2-69:89)
            atr$($$openClass,'satisfiedTypes',function(){
              var ints = this.tipo.$crtmm$['satisfies'];
              if (ints && ints.length) {
                var rv = [];
                for (var i=0; i < ints.length; i++) {
                  var ifc = ints[i];
                  var mm = getrtmm$$(ifc.t);
                  var fi=FreeInterface(OpenInterface(getModules$meta().find(mm.mod['$mod-name'],mm.mod['$mod-version']).findPackage(mm.d[0]), ifc.t));
                  if (ifc.a)fi.declaration._targs=ifc.a;
                  rv.push(fi);
                }
                return rv.length===0?getEmpty():ArraySequence(rv,{Element$ArraySequence:{t:OpenInterfaceType$meta$declaration}});
              }
              return getEmpty();
            },undefined,function(){return{mod:$CCMM$,$t:{t:Sequential,a:{Element$Sequential:{t:OpenInterfaceType$meta$declaration}}},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassDeclaration','$at','satisfiedTypes']};});
            atr$($$openClass,'name',function(){return this.name_;},undefined,function(){return{mod:$CCMM$,$t:{t:$_String},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassDeclaration','$at','name']};});
            atr$($$openClass,'containingPackage',function(){return this._pkg;},undefined,function(){return{mod:$CCMM$,$t:{t:Package$meta$declaration},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassDeclaration','$at','containingPackage']};});
            atr$($$openClass,'containingModule',function(){return this._pkg.container;},undefined,function(){return{mod:$CCMM$,$t:{t:Module$meta$declaration},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassDeclaration','$at','containingModule']};});
            atr$($$openClass,'toplevel',function(){return this.toplevel_;},undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassDeclaration','$at','toplevel']};});

    atr$($$openClass,'qualifiedName',function(){
      return $qname(this.tipo);
    },undefined,function(){return{mod:$CCMM$,$t:{t:$_String},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Declaration','$at','qualifiedName']};});

      $$openClass.equals=function(other) {
        return is$(other, {t:OpenClass}) && other.tipo===this.tipo;
      }
    })(OpenClass.$$.prototype);
  }
  return OpenClass;
}
ex$.$init$OpenClass=$init$OpenClass;
$init$OpenClass();

//ClassDefinition OpenInterface at X (72:0-92:0)
function OpenInterface(pkg, meta, that) {
    if (meta===undefined)throw Exception("Interface reference not found. Metamodel doesn't work with modules compiled in lexical scope style");
    $init$OpenInterface();
    if (that===undefined)that=new OpenInterface.$$;
    that._pkg = pkg;
    var _mm=getrtmm$$(meta);
    if (_mm === undefined) {
      //it's a metamodel
      that.meta=meta;
      that.tipo=_findTypeFromModel(pkg,meta);
      _mm = getrtmm$$(that.tipo);
    } else {
      //it's a type
      that.tipo = meta;
      that.meta = get_model(_mm);
    }
    that.name_=(that.meta&&that.meta.$nm)||_mm.d[_mm.d.length-1];
    that.toplevel_=_mm.$cont === undefined;
    InterfaceDeclaration$meta$declaration(that);
    return that;
}
OpenInterface.$crtmm$=function(){return{mod:$CCMM$,'super':{t:Basic},satisfies:[{t:InterfaceDeclaration$meta$declaration}],d:['ceylon.language.meta.declaration','InterfaceDeclaration']};};
function $init$OpenInterface(){
  if (OpenInterface.$$===undefined){
    initTypeProto(OpenInterface,'ceylon.language.meta.declaration::OpenInterface',Basic,InterfaceDeclaration$meta$declaration);
    (function($$openInterface){
      $$openInterface.equals=function(other) {
        return is$(other, {t:OpenInterface}) && other.tipo==this.tipo;
      }
$$openInterface.memberInterfaceApply=function(cont,targs,$mptypes){
  var mm=this.tipo.$crtmm$;
  if (cont!==getNothingType$meta$model() && !extendsType({t:cont.tipo},{t:mm.$cont}))
    throw IncompatibleTypeException$meta$model("Incompatible Container specified");
  var _t={t:this.tipo};
  validate$typeparams(_t,mm.$tp,targs);
  var rv=AppliedMemberInterface(this.tipo,{Container$MemberInterface:{t:mm.$cont},Type$MemberInterface:_t});
  if (_t.a)rv.$targs=_t.a;
  return rv;
}
$$openInterface.interfaceApply=function(targs,$mptypes) {
  return this.$_apply(targs,$mptypes);
}
            
atr$($$openInterface,'string',function(){
  return "interface " + this.qualifiedName;
},undefined,function(){return{mod:$CCMM$,$t:{t:$_String},$cont:OpenInterface,$an:function(){return[shared(),actual()];},d:['$','Object']};}); 
           
            atr$($$openInterface,'openType',function(){
              return FreeInterface(this);
            },undefined,function(){return{mod:$CCMM$,$t:{t:OpenType$meta$declaration},$cont:OpenInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','InterfaceDeclaration','$at','openType']};});
            atr$($$openInterface,'extendedType',function(){
              var sc = this.tipo.$crtmm$['super'];
              if (sc === undefined)return null;
              var mm = getrtmm$$(sc.t);
              return FreeClass(OpenClass(getModules$meta().find(mm.mod['$mod-name'],mm.mod['$mod-version']).findPackage(mm.d[0]), sc.t));
            },undefined,function(){return{mod:$CCMM$,$t:{ t:'u', l:[{t:Null},{t:OpenClasType$meta$declaration}]},$cont:OpenInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','InterfaceDeclaration','$at','extendedType']};});
            
            //AttributeDeclaration interfaceDeclarations at X (91:2-91:89)
            atr$($$openInterface,'satisfiedTypes',function(){
              var ints = this.tipo.$crtmm$['satisfies'];
              if (ints && ints.length) {
                var rv = [];
                for (var i=0; i < ints.length; i++) {
                  var ifc = ints[i].t;
                  var mm = getrtmm$$(ifc);
                  rv.push(FreeInterface(OpenInterface(getModules$meta().find(mm.mod['$mod-name'],mm.mod['$mod-version']).findPackage(mm.d[0]), ifc)));
                }
                return rv.length===0?getEmpty():ArraySequence(rv,{Element$ArraySequence:{t:OpenInterfaceType$meta$declaration}});
              }
              return getEmpty();
            },undefined,function(){return{mod:$CCMM$,$t:{t:Sequential,a:{Element$Sequential:{t:OpenInterfaceType$meta$declaration}}},$cont:OpenInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','InterfaceDeclaration','$at','satisfiedTypes']};});
            atr$($$openInterface,'name',function(){return this.name_;},undefined,function(){return{mod:$CCMM$,$t:{t:$_String},$cont:OpenInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','InterfaceDeclaration','$at','name']};});
            atr$($$openInterface,'containingPackage',function(){return this._pkg;},undefined,function(){return{mod:$CCMM$,$t:{t:Package$meta$declaration},$cont:OpenInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','InterfaceDeclaration','$at','containingPackage']};});
            atr$($$openInterface,'containingModule',function(){return this._pkg.container;},undefined,function(){return{mod:$CCMM$,$t:{t:Module$meta$declaration},$cont:OpenInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','InterfaceDeclaration','$at','containingModule']};});
            atr$($$openInterface,'toplevel',function(){return this.toplevel_;},undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:OpenInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','InterfaceDeclaration','$at','toplevel']};});

    atr$($$openInterface,'qualifiedName',function(){
      return $qname(this.tipo);
    },undefined,function(){return{mod:$CCMM$,$t:{t:$_String},$cont:OpenInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Declaration','$at','qualifiedName']};});
        })(OpenInterface.$$.prototype);
    }
    return OpenInterface;
}
ex$.$init$OpenInterface=$init$OpenInterface;
$init$OpenInterface();

function OpenAlias(alias, $$openAlias){
  $init$OpenAlias();
  if ($$openAlias===undefined)$$openAlias=new OpenAlias.$$;
  if (typeof(alias)==='function')alias=alias();
  $$openAlias._alias = alias;
  //Get model from path
  var mm=getrtmm$$(alias);
  $$openAlias.meta=get_model(mm);
  AliasDeclaration$meta$declaration($$openAlias);
  return $$openAlias;
}
OpenAlias.$crtmm$=function(){return{mod:$CCMM$,'super':{t:Basic},satisfies:[{t:AliasDeclaration$meta$declaration}],d:['ceylon.language.meta.declaration','OpenAlias']};};
function $init$OpenAlias(){
  if (OpenAlias.$$===undefined){
    initTypeProto(OpenAlias,'OpenAlias',Basic,AliasDeclaration$meta$declaration);
    (function($$openAlias){

      atr$($$openAlias,'extendedType',function(){
        return _openTypeFromTarg(this._alias);
      },undefined,function(){return{mod:$CCMM$,$t:{t:OpenType$meta$declaration},$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$at','extendedType']};});
      atr$($$openAlias,'qualifiedName',function(){
        return $qname(this._alias.$crtmm$);
      },undefined,function(){return{mod:$CCMM$,$t:{t:$_String},$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$at','qualifiedName']};});
      atr$($$openAlias,'toplevel',function(){
        return this._alias.$cont===undefined;
      },undefined,function(){return{mod:$CCMM$,$t:{t:Boolean},$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$at','toplevel']};});
      atr$($$openAlias,'containingPackage',function(){
        throw Error("IMPL OpenAlias.containingPackage");
      },undefined,function(){return{mod:$CCMM$,$t:{t:Package$meta$declaration},$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$at','containingPackage']};});
      atr$($$openAlias,'containingModule',function(){
        throw Exception("IMPL OpenAlias.containingModule");
      },undefined,function(){return{mod:$CCMM$,$t:{t:Module$meta$declaration},$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$at','containingModule']};});
      atr$($$openAlias,'container',function(){
        var cont=this.$$targs$$.Container;
        if (cont===undefined) {
          cont=this._alias.$crtmm$.$cont;
          if (cont)cont={t:cont};
        }
        if (cont) {
          return typeLiteral$meta({Type$typeLiteral:cont});
        }
        return null;
      },undefined,function(){return{mod:$CCMM$,$t:{ t:'u', l:[{t:NestableDeclaration$meta$declaration},{t:Package$meta$declaration}]},$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$at','container']};});
      atr$($$openAlias,'openType',function(){
        return this.extendedType;
      },undefined,function(){return{mod:$CCMM$,$t:{t:OpenType$meta$declaration},$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$at','openType']};});
      atr$($$openAlias,'typeParameterDeclarations',function(){
        var tps=this._alias.$crtmm$.$tp;
        if (tps) {
          var rv=[];
          for (var tp in tps) {
            rv.push(OpenTypeParam$jsint(this._alias, tp));
          }
          return rv.length===0?getEmpty():ArraySequence(rv,{Element$ArraySequence:{t:TypeParameter$meta$declaration}});
        }
        return getEmpty();
      },undefined,function(){return{mod:$CCMM$,$t:{t:Sequential,a:{Element$Sequential:{t:TypeParameter$meta$declaration}}},$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$at','typeParameterDeclarations']};});
      $$openAlias.getTypeParameterDeclaration=function getTypeParameterDeclaration(name$2){
        var tp=this._alias.$crtmm$;
        if (tp.$tp) {
          var tpn=undefined;
          for (var ftn in tp.$tp) {
            if (ftn.substring(0,name$2.size+1)==name$2+'$') {
              tpn=ftn;
            }
          }
          tp=tpn;
        } else {
          tp=undefined;
        }
        return tp ? OpenTypeParam$jsint(this._alias, tp) : null;
      };$$openAlias.getTypeParameterDeclaration.$crtmm$=function(){return{mod:$CCMM$,$t:{ t:'u', l:[{t:Null},{t:TypeParameter$meta$declaration}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:$_String},$an:function(){return[];}}],$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$m','getTypeParameterDeclaration']};};
      atr$($$openAlias,'name',function(){
        return this._alias.$crtmm$.d[this._alias.$crtmm$.d.length-1];
      },undefined,function(){return{mod:$CCMM$,$t:{t:$_String},$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$at','name']};});
      atr$($$openAlias,'string',function(){return "alias "+this.qualifiedName;},undefined,function(){return{mod:$CCMM$,$t:{t:$_String},$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$at','string']};});
  $$openAlias.equals=function equals(o) {
    if (o && is$(o, {t:OpenAlias})) {
      if (o._alias.a) {
        return o._alias.a == this._alias.a;
      }
      return o._alias.t === this._alias.t;
    }
    return false;
  }
    })(OpenAlias.$$.prototype);
  }
  return OpenAlias;
}
ex$.$init$OpenAlias=$init$OpenAlias;
$init$OpenAlias();

function FunParamDecl(cont,param,$$funParamDecl){
  $init$FunParamDecl();
  if ($$funParamDecl===undefined)$$funParamDecl=new FunParamDecl.$$;
  FunctionDeclaration$meta$declaration($$funParamDecl);
  $$funParamDecl.cont=cont;
  $$funParamDecl.param=param;
  $$funParamDecl.tipo={$crtmm$:{$cont:cont.tipo,$t:param.$t,$ps:param.$ps,$mt:'prm',d:cont.tipo.$crtmm$.d,$an:param.$an}};

  $$funParamDecl.$prop$getParameter={$crtmm$:function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','parameter']};}};
  $$funParamDecl.$prop$getParameter.get=function(){return true;};
  $$funParamDecl.$prop$getAnnotation={$crtmm$:function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','annotation']};}};
  $$funParamDecl.$prop$getAnnotation.get=function(){return false;};
  $$funParamDecl.$prop$getShared={$crtmm$:function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','shared']};}};
  $$funParamDecl.$prop$getShared.get=function(){return false;};
  $$funParamDecl.$prop$getToplevel={$crtmm$:function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','toplevel']};}};
  $$funParamDecl.$prop$getToplevel.get=function(){return false;};
  $$funParamDecl.$prop$getFormal={$crtmm$:function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','formal']};}};
  $$funParamDecl.$prop$getFormal.get=function(){return false;};
  $$funParamDecl.$prop$getDefault={$crtmm$:function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','default']};}};
  $$funParamDecl.$prop$getDefault.get=function(){return false};
  $$funParamDecl.$prop$getActual={$crtmm$:function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','actual']};}};
  $$funParamDecl.$prop$getActual.get=function(){return false;};
  return $$funParamDecl;
}
FunParamDecl.$crtmm$=function(){return{mod:$CCMM$,'super':{t:Basic},$ps:[],satisfies:[{t:FunctionDeclaration$meta$declaration}],d:['ceylon.language.meta.declaration','FunParamDecl']};};
function $init$FunParamDecl(){
  if (FunParamDecl.$$===undefined){
    initTypeProto(FunParamDecl,'ceylon.language.meta.declaration::FunParamDecl',Basic,FunctionDeclaration$meta$declaration);
    (function($$funParamDecl){

      atr$($$funParamDecl,'parameter',function(){return true;},undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','parameter']};});
      
      //AttributeGetterDef defaulted at caca.ceylon (8:2-8:71)
      atr$($$funParamDecl,'defaulted',function(){
        return this.param.$def!==undefined;
      },undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','defaulted']};});
      //AttributeGetterDef variadic at caca.ceylon (9:2-9:69)
      atr$($$funParamDecl,'variadic',function(){
        return this.param.seq!==undefined;
      },undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','variadic']};});
      //AttributeGetterDef container at caca.ceylon (11:2-11:91)
      atr$($$funParamDecl,'container',function(){
        return this.cont;
      },undefined,function(){return{mod:$CCMM$,$t:{t:'u', l:[{t:Package$meta$declaration},{t:NestableDeclaration$meta$declaration}]},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','container']};});
      //AttributeGetterDef containingPackage at caca.ceylon (12:2-12:87)
      atr$($$funParamDecl,'containingPackage',function(){
        return this.cont.containingPackage;
      },undefined,function(){return{mod:$CCMM$,$t:{t:Package$meta$declaration},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','containingPackage']};});
      //AttributeGetterDef containingModule at caca.ceylon (13:2-13:84)
      atr$($$funParamDecl,'containingModule',function(){
        return this.cont.containingModule;
      },undefined,function(){return{mod:$CCMM$,$t:{t:Module$meta$declaration},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','containingModule']};});
      //AttributeGetterDef openType at caca.ceylon (14:2-14:70)
      atr$($$funParamDecl,'openType',function(){
        var t = this.param.$t;
        if (typeof(t)==='string')return OpenTvar$jsint(OpenTypeParam$jsint(this.cont,t));
        return _openTypeFromTarg(t);
      },undefined,function(){return{mod:$CCMM$,$t:{t:OpenType$meta$declaration},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','openType']};});
      //AttributeDecl annotation at caca.ceylon (15:2-15:40)
      atr$($$funParamDecl,'annotation',function(){return false;},undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','annotation']};});
      atr$($$funParamDecl,'shared',function(){return false;},undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','shared']};});
      atr$($$funParamDecl,'toplevel',function(){return false;},undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','toplevel']};});
      atr$($$funParamDecl,'formal',function(){return false;},undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','formal']};});
      atr$($$funParamDecl,'$_default',function(){return false;},undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','default']};});
      atr$($$funParamDecl,'actual',function(){return false;},undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','actual']};});
      atr$($$funParamDecl,'qualifiedName',function(){
        return $qname(cont.tipo.$crtmm$)+"."+this.param.$nm;
      },undefined,function(){return{mod:$CCMM$,$t:{t:$_String},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','qualifiedName']};});
      atr$($$funParamDecl,'name',function(){
        return this.param.$nm;
      },undefined,function(){return{mod:$CCMM$,$t:{t:$_String},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','name']};});
      $$funParamDecl.getParameterDeclaration=function getParameterDeclaration(name$10){
      var pd=this.parameterDeclarations;
      for (var i=0; i < pd.size; i++) {
        if (name$6.equals(pd[i].name))return pd[i];
      }
      return null;
      };$$funParamDecl.getParameterDeclaration.$crtmm$=function(){return{mod:$CCMM$,$t:{t:'u', l:[{t:Null},{t:FunctionOrValueDeclaration$meta$declaration}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:$_String},$an:function(){return[];}}],$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$m','getParameterDeclaration']};};
      
      //MethodDef apply at caca.ceylon (31:2-32:74)
      $$funParamDecl.$_apply=function $_apply(typeArguments$11,$$$mptypes){
          if(typeArguments$11===undefined){typeArguments$11=getEmpty();}
          throw Exception("IMPL FunParamDecl.apply");
      };$$funParamDecl.$_apply.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Function$meta$model,a:{Arguments:'Arguments',Type:'Return'}},$ps:[{$nm:'typeArguments',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element$Sequential:{t:Type$meta$model,a:{Type:{t:Anything}}}}},$an:function(){return[];}}],$cont:FunParamDecl,$tp:{Return:{'def':{t:Anything}},Arguments:{'satisfies':[{t:Sequential,a:{Element$Sequential:{t:Anything}}}],'def':{t:Nothing}}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$m','apply']};};
      
      //MethodDef memberApply at caca.ceylon (33:2-34:80)
      $$funParamDecl.memberApply=function memberApply(containerType$12,typeArguments$13,$$$mptypes){
          if(typeArguments$13===undefined){typeArguments$13=getEmpty();}
          throw Exception("IMPL FunParamDecl.memberApply");
      };$$funParamDecl.memberApply.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Method$meta$model,a:{Arguments:'Arguments',Type:'Return',Container:'Container'}},$ps:[{$nm:'containerType',$mt:'prm',$t:{t:Type$meta$model,a:{Type:'Container'}},$an:function(){return[];}},{$nm:'typeArguments',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element$Sequential:{t:Type$meta$model,a:{Type:{t:Anything}}}}},$an:function(){return[];}}],$cont:FunParamDecl,$tp:{Container:{'def':{t:Nothing}},Return:{'def':{t:Anything}},Arguments:{'satisfies':[{t:Sequential,a:{Element$Sequential:{t:Anything}}}],'def':{t:Nothing}}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$m','memberApply']};};
    })(FunParamDecl.$$.prototype);
  }
  return FunParamDecl;
}
ex$.$init$FunParamDecl=$init$FunParamDecl;
$init$FunParamDecl();

function ValParamDecl(cont,param,$$valParamDecl){
  $init$ValParamDecl();
  if ($$valParamDecl===undefined)$$valParamDecl=new ValParamDecl.$$;
  ValueDeclaration$meta$declaration($$valParamDecl);
  $$valParamDecl.cont=cont;
  $$valParamDecl.param=param;
  $$valParamDecl.tipo={$crtmm$:{$cont:cont.tipo,$t:param.$t,$mt:'prm',d:cont.tipo.$crtmm$.d,$an:param.$an}};

  $$valParamDecl.$prop$getParameter.get=function(){return true;};
  $$valParamDecl.$prop$getShared.get=function(){return false;};
  $$valParamDecl.$prop$getToplevel.get=function(){return false;};
  $$valParamDecl.$prop$getFormal.get=function(){return false;};
  $$valParamDecl.$prop$getDefault.get=function(){return false;};
  $$valParamDecl.$prop$getActual.get=function(){return false;};
  return $$valParamDecl;
}
ValParamDecl.$crtmm$=function(){return{mod:$CCMM$,'super':{t:Basic},$ps:[],satisfies:[{t:ValueDeclaration$meta$declaration}],d:['ceylon.language.meta.declaration','ValParamDecl']};};
function $init$ValParamDecl(){
  if (ValParamDecl.$$===undefined){
    initTypeProto(ValParamDecl,'ceylon.language.meta.declaration::ValParamDecl',Basic,ValueDeclaration$meta$declaration);
    (function($$valParamDecl){

      atr$($$valParamDecl,'parameter',function(){return true;},undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:ValParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','parameter']};});
      atr$($$valParamDecl,'defaulted',function(){
        return this.param.$def!==undefined;
      },undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:ValParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','defaulted']};});
      atr$($$valParamDecl,'variadic',function(){
        return this.param.seq!==undefined;
      },undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:ValParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','variadic']};});
      atr$($$valParamDecl,'variable',function(){
        return find$ann(this.param,VariableAnnotation)!==null;
      },undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:ValParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','variable']};});
      atr$($$valParamDecl,'container',function(){
        return this.cont;
      },undefined,function(){return{mod:$CCMM$,$t:{t:'u', l:[{t:Package$meta$declaration},{t:NestableDeclaration$meta$declaration}]},$cont:ValParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','container']};});
      atr$($$valParamDecl,'containingPackage',function(){
        return this.cont.containingPackage;
      },undefined,function(){return{mod:$CCMM$,$t:{t:Package$meta$declaration},$cont:ValParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','containingPackage']};});
      atr$($$valParamDecl,'containingModule',function(){
        return this.cont.containingModule;
      },undefined,function(){return{mod:$CCMM$,$t:{t:Module$meta$declaration},$cont:ValParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','containingModule']};});
      atr$($$valParamDecl,'openType',function(){
        var t = this.param.$t;
        if (typeof(t)==='string')return OpenTvar$jsint(OpenTypeParam$jsint(this.cont,t));
        return _openTypeFromTarg(t);
      },undefined,function(){return{mod:$CCMM$,$t:{t:OpenType$meta$declaration},$cont:ValParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','openType']};});
      atr$($$valParamDecl,'shared',function(){return false;},undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:ValParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','shared']};});
      atr$($$valParamDecl,'toplevel',function(){return false;},undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:ValParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','toplevel']};});
      atr$($$valParamDecl,'formal',function(){return false;},undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:ValParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','formal']};});
      atr$($$valParamDecl,'$_default',function(){return false;},undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:ValParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','default']};});
      atr$($$valParamDecl,'actual',function(){return false;},undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:ValParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','actual']};});
      atr$($$valParamDecl,'qualifiedName',function(){
        return $qname(this.cont.tipo.$crtmm$)+"."+this.param.$nm;
      },undefined,function(){return{mod:$CCMM$,$t:{t:$_String},$cont:ValParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','qualifiedName']};});
      atr$($$valParamDecl,'name',function(){
        return this.param.$nm;
      },undefined,function(){return{mod:$CCMM$,$t:{t:$_String},$cont:ValParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','name']};});
      //MethodDef apply at caca.ceylon (57:2-57:84)
      $$valParamDecl.$_apply=function $_apply($$$mptypes){
          var $$valParamDecl=this;
          throw Exception("IMPL ValParamDecl.apply");
      };$$valParamDecl.$_apply.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Value$meta$model,a:{Type:'Type'}},$ps:[],$cont:ValParamDecl,$tp:{Type:{'def':{t:Anything}}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$m','apply']};};
      //MethodDef memberApply at caca.ceylon (58:2-58:166)
      $$valParamDecl.memberApply=function memberApply(containerType$20,$$$mptypes){
          var $$valParamDecl=this;
          throw Exception("IMPL ValParamDecl.memberApply");
      };$$valParamDecl.memberApply.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Attribute$meta$model,a:{Type:'Type',Container:'Container'}},$ps:[{$nm:'containerType',$mt:'prm',$t:{t:Type$meta$model,a:{Type:'Container'}},$an:function(){return[];}}],$cont:ValParamDecl,$tp:{Container:{'def':{t:Nothing}},Type:{'def':{t:Anything}}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$m','memberApply']};};
    })(ValParamDecl.$$.prototype);
  }
  return ValParamDecl;
}
ex$.$init$ValParamDecl=$init$ValParamDecl;
$init$ValParamDecl();
