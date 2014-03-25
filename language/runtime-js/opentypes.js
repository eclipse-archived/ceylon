function $findAnnotation(cont,ant) {
  var _m=getrtmm$$(cont);
  if (!(_m && _m.$an))return null;
  if (typeof(_m.$an)==='function')_m.$an=_m.$an();
  for (var i=0; i < _m.$an.length; i++) {
    if (isOfType(_m.$an[i],{t:ant}))return _m.$an[i];
  }
  return null;
}
//Find the real declaration of something from its model definition
function _findTypeFromModel(pkg,mdl,cont) {
  var mod = pkg.container;
  //TODO this is very primitive needs a lot of rules replicated from the JsIdentifierNames
  var nm = mdl.$nm;
  var mt = mdl['$mt'];
  if (mt === 'attr' || mt === 'gttr' || mt === 'obj') {
    nm = '$prop$get' + nm[0].toUpperCase() + nm.substring(1);
  }
  if (cont) {
    var imm=getrtmm$$(cont);
    if (mt==='cls'||mt==='ifc')nm=nm+'$'+imm.d[imm.d.length-1];
  }else {
    nm+=pkg.suffix;
  }
  var out=cont?cont.$$.prototype:mod.meta;
  var rv=out[nm];
  if (rv===undefined)rv=out['$'+nm];
  if (rv===undefined){
    rv=out['$init$'+nm];
    if (typeof(rv)==='function')rv=rv();
  }
  return rv;
}
//Pass a {t:Bla} and get a FreeClass,FreeInterface,etc (OpenType).
function _openTypeFromTarg(targ) {
  if (targ.t==='u' || targ.t==='i') {
    var tl=[];
    for (var i=0; i < targ.l.length; i++) {
      var _ct=targ.l[i];
      tl.push(_ct.t?_openTypeFromTarg(_ct):_ct);
    }
    return (targ.t==='u'?FreeUnion:FreeIntersection)(tl.reifyCeylonType({Element$Iterable:{t:OpenType$meta$declaration}}));
  } else if (targ.t==='T') {
    targ=$retuple(targ);
  }
  var mm=getrtmm$$(targ.t);
  var lit = typeLiteral$meta({Type$typeLiteral:targ.t});
  if (targ.a && lit)lit._targs=targ.a;
  var mdl = get_model(mm);
  if (mdl.$mt==='ifc') {
    return FreeInterface(lit);
  } else if (mdl.$mt==='cls' || mdl.$mt==='obj') {
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
      return isOfType(other,{t:FreeClass}) && other.declaration.equals(this.declaration) && this.typeArguments.equals(other.typeArguments);
    }
   })(FreeClass.$$.prototype);
  }
  return FreeClass;
}
exports.$init$FreeClass=$init$FreeClass;
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
      return isOfType(other,{t:FreeInterface}) && other.declaration.equals(this.declaration) && this.typeArguments.equals(other.typeArguments);
    }
            
    })(FreeInterface.$$.prototype);
  }
  return FreeInterface;
}
exports.$init$FreeInterface=$init$FreeInterface;
$init$FreeInterface();

//ClassDefinition OpenFunction at X (18:0-36:0)
function OpenFunction(pkg, meta, that){
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
    that.name_=(_mm&&_mm.d[_mm.d.length-1])||'?';
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
  if (isOfType(o,{t:OpenFunction})) {
    return o.tipo === this.tipo;
  }
  return false;
}
$$openFunction.equals.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Boolean$},d:['ceylon.language','Object','$m','equals']};}
defineAttr($$openFunction,'container',function(){
  if (this.$parent)return this.$parent;
  if (this.toplevel)return this.containingPackage;
  return typeLiteral$meta({Type$typeLiteral:this.tipo.$crtmm$.$cont});
},undefined,function(){return{mod:$CCMM$,$t:{t:'u',l:[{t:NestableDeclaration$meta$declaration},{t:Package$meta$declaration}]},d:['ceylon.language.meta.declaration','NestableDeclaration','$at','container']};});
defineAttr($$openFunction,'annotation',function(){
  return $findAnnotation(this.tipo,AnnotationAnnotation)!==null;
},undefined,function(){return{mod:$CCMM$,$t:{t:Boolean$},$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionalDeclaration','$at','annotation']};});

      $$openFunction.$apply=function $apply(types,$mptypes){
        var mm=this.tipo.$crtmm$;
        var ta={t:this.tipo};
        validate$typeparams(ta,mm.$tp,types);
        validate$params(mm.$ps,$mptypes.Arguments$apply,"Wrong number of arguments when applying function");
        return ta.a?AppliedFunction(this.tipo,{Type:mm.$t,Arguments:tupleize$params(mm.$ps,ta.a)},undefined,ta.a):
          AppliedFunction(this.tipo,{Type:mm.$t,Arguments:tupleize$params(mm.$ps)});
      };$$openFunction.$apply.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Function$meta$model,a:{Arguments:{t:Nothing},Type:{t:Anything}}},$ps:[{$nm:'types',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element$Sequential:{t:Type$meta$model}}}}],$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$m','apply']};};

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
            defineAttr($$openFunction,'defaulted',function(){
                return false;
            },undefined,function(){return{mod:$CCMM$,$t:{t:Boolean$},$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','defaulted']};});
            
            //AttributeDeclaration variadic at X (26:4-26:43)
            defineAttr($$openFunction,'variadic',function(){
                var $$openFunction=this;
                return false;
            },undefined,function(){return{mod:$CCMM$,$t:{t:Boolean$},$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','variadic']};});
    $$openFunction.getParameterDeclaration=function (name$6){
      var pd=this.parameterDeclarations;
      for (var i=0; i < pd.length; i++) {
        if (name$6.equals(pd[i].name))return pd[i];
      }
      return null;
    };
    $$openFunction.getParameterDeclaration.$crtmm$=function(){return{mod:$CCMM$,$t:{ t:'u', l:[{t:Null},{t:FunctionOrValueDeclaration$meta$declaration}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}}],$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$m','getParameterDeclaration']};};
            
            //AttributeGetterDefinition openType at X (33:2-33:43)
    defineAttr($$openFunction,'openType',function(){
      var t = this.tipo.$crtmm$.$t;
      if (typeof(t)==='string')return OpenTvar(OpenTypeParam(this.tipo,t));
      return _openTypeFromTarg(t);
    },undefined,function(){return{mod:$CCMM$,$t:{t:OpenType$meta$declaration},$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','openType']};});
            defineAttr($$openFunction,'name',function(){return this.name_;},undefined,function(){return{mod:$CCMM$,$t:{t:String$},$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','name']};});
            defineAttr($$openFunction,'containingPackage',function(){return this._pkg;},undefined,function(){return{mod:$CCMM$,$t:{t:Package$meta$declaration},$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','containingPackage']};});
            defineAttr($$openFunction,'containingModule',function(){return this._pkg.container;},undefined,function(){return{mod:$CCMM$,$t:{t:Package$meta$declaration},$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','containingPackage']};});
            defineAttr($$openFunction,'toplevel',function(){return this.toplevel_;},undefined,function(){return{mod:$CCMM$,$t:{t:Boolean$},$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','toplevel']};});

    defineAttr($$openFunction,'string',function(){return String$("function " + this.qualifiedName);},undefined,function(){return{mod:$CCMM$,$t:{t:String$},$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Declaration','$at','string']};});
    defineAttr($$openFunction,'qualifiedName',function(){
       return String$($qname(this.tipo));
    },undefined,function(){return{mod:$CCMM$,$t:{t:String$},$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Declaration','$at','qualifiedName']};});
    })(OpenFunction.$$.prototype);
  }
  return OpenFunction;
}
exports.$init$OpenFunction=$init$OpenFunction;
$init$OpenFunction();

//ClassDefinition OpenValue at X (38:0-45:0)
function OpenValue(pkg, meta, that){
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
  that.name_=_mm.d===undefined?_mm['$nm']:_mm.d[_mm.d.length-1];
  that.toplevel_=_mm.$cont === undefined;
  ValueDeclaration$meta$declaration(that);
  return that;
}
OpenValue.$crtmm$=function(){return{mod:$CCMM$,'super':{t:Basic},satisfies:[{t:ValueDeclaration$meta$declaration}],d:['ceylon.language.meta.declaration','ValueDeclaration']};};
function $init$OpenValue(){
  if (OpenValue.$$===undefined){
    initTypeProto(OpenValue,'ceylon.language.meta.declaration::OpenValue',Basic,ValueDeclaration$meta$declaration);
    (function($$openValue){

defineAttr($$openValue,'container',function(){
  if (this.$parent)return this.$parent;
  if (this.toplevel)return this.containingPackage;
  return typeLiteral$meta({Type$typeLiteral:this.tipo.$crtmm$.$cont});
},undefined,function(){return{mod:$CCMM$,$t:{t:'u',l:[{t:NestableDeclaration$meta$declaration},{t:Package$meta$declaration}]},d:['ceylon.language.meta.declaration','NestableDeclaration','$at','container']};});
            
            //MethodDefinition apply at X (39:4-39:68)
      $$openValue.$apply=function $apply($$$mptypes){
        var mm=getrtmm$$(this.tipo);
        if (!extendsType(mm.$t,$$$mptypes.Get$apply))throw IncompatibleTypeException$meta$model("Incompatible Get type argument");
        if (!extendsType($$$mptypes.Set$apply,this.tipo.set?mm.$t:{t:Nothing}))throw IncompatibleTypeException$meta$model("Incompatible Set type argument");
        return AppliedValue(undefined,this.tipo,{Get$Value:$$$mptypes.Get$apply,Set$Value:$$$mptypes.Set$apply});
      };$$openValue.$apply.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Value$meta$model,a:{Type:{t:Anything}}},$ps:[{$nm:'instance',$mt:'prm',$def:1,$t:{t:Anything}}],$cont:OpenValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$m','apply']};};

      $$openValue.memberApply=function memberApply(cont,$mptypes) {
        var mm=getrtmm$$(this.tipo);
        if (!(cont.tipo && extendsType({t:cont.tipo},{t:mm.$cont}))&&cont!==getNothingType$meta$model())
          throw IncompatibleTypeException$meta$model("Incompatible Container type argument");
        if (!extendsType(mm.$t,$mptypes.Get$memberApply))throw IncompatibleTypeException$meta$model("Incompatible Get type argument");
        if (!extendsType($mptypes.Set$memberApply,this.tipo.set?mm.$t:{t:Nothing}))throw IncompatibleTypeException$meta$model("Incompatible Set type argument");
        return AppliedAttribute(this.meta.$nm,this.tipo,{Get$Attribute:$mptypes.Get$apply,Set$Attribute:$mptypes.Set$apply,
          Container$Attribute:$mptypes.Container$memberApply});
      };$$openValue.memberApply.$crtmm$=function(){return{mod:$CCMM$,d:['ceylon.language.meta.declaration','ValueDeclaration','$m','memberApply']};};
      defineAttr($$openValue,'defaulted',function(){
        return false;
      },undefined,function(){return{mod:$CCMM$,$t:{t:Boolean$},$cont:OpenValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','defaulted']};});
      defineAttr($$openValue,'variadic',function(){
        return false;
      },undefined,function(){return{mod:$CCMM$,$t:{t:Boolean$},$cont:OpenValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','variadic']};});
      defineAttr($$openValue,'variable',function(){
        return $findAnnotation(this.tipo,VariableAnnotation)!==null;
      },undefined,function(){return{mod:$CCMM$,$t:{t:Boolean$},$cont:OpenValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','variadic']};});
            
  defineAttr($$openValue,'openType',function(){
    if (this.tipo) {
      var mm = getrtmm$$(this.tipo);
      if (typeof(mm.$t)==='string') {
        return OpenTypeParam(mm.$cont,mm.$t);
      }
      return _openTypeFromTarg(mm.$t);
    }
    throw Error("OpenValue.openType-we don't have a metamodel!");
  },undefined,function(){return{mod:$CCMM$,$t:{t:OpenType$meta$declaration},$cont:OpenValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','openType']};});

defineAttr($$openValue,'name',function(){return this.name_;},undefined,function(){return{mod:$CCMM$,$t:{t:String$},$cont:OpenValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','name']};});
defineAttr($$openValue,'containingPackage',function(){return this._pkg;},undefined,function(){return{mod:$CCMM$,$t:{t:Package$meta$declaration},$cont:OpenValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','containingPackage']};});
defineAttr($$openValue,'containingModule',function(){return this._pkg.container;},undefined,function(){return{mod:$CCMM$,$t:{t:Module$meta$declaration},$cont:OpenValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','containingModule']};});
defineAttr($$openValue,'toplevel',function(){return this.toplevel_;},undefined,function(){return{mod:$CCMM$,$t:{t:Boolean$},$cont:OpenValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','toplevel']};});

  $$openValue.equals=function(other) {
    if (isOfType(other, {t:OpenValue}) && other.name.equals(this.name) && other.toplevel===this.toplevel && other.containingPackage.equals(this.containingPackage)) {
      return other.meta==this.meta;
    }
    return false;
  }
  defineAttr($$openValue,'string',function(){return String$("value " + this.qualifiedName);},undefined,{$an:function(){return[shared(),actual()]},mod:$CCMM$,d:['ceylon.language','Object','$at','string']});

    defineAttr($$openValue,'qualifiedName',function(){
      return String$($qname(this.tipo));
    },undefined,function(){return{mod:$CCMM$,$t:{t:String$},$cont:OpenValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Declaration','$at','qualifiedName']};});
    $$openValue.memberSet=function(c,v) {
      if (!isOfType(c,{t:this.tipo.$crtmm$.$cont}))throw IncompatibleTypeException$meta$model("Incompatible container type");
      if (!isOfType(v,this.tipo.$crtmm$.$t))throw IncompatibleTypeException$meta$model("Incompatible value type");
      if (!this.tipo.set)throw MutationException$meta$model($qname(this.tipo.$crtmm$)+" is not writable");
      c[this.name]=v;
    };
    defineAttr($$openValue,'setter',function(){
      return OpenSetter(this);
    },undefined,function(){return{mod:$CCMM$,$t:{t:SetterDeclaration$meta$declaration},$cont:OpenValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','setter']};});

    })(OpenValue.$$.prototype);
  }
  return OpenValue;
}
exports.$init$OpenValue=$init$OpenValue;
$init$OpenValue();

//ClassDefinition OpenSetter at opentypes.ceylon (63:0-63:90)
function OpenSetter(v, $$openSetter){
  $init$OpenSetter();
  if ($$openSetter===undefined)$$openSetter=new OpenSetter.$$;
  $$openSetter.variable_=v;
  SetterDeclaration$meta$declaration($$openSetter);
  $$openSetter.tipo=v.tipo.set;
  if (v.tipo.set && v.tipo.set.setter$anns) {
    var mm={};
    var omm=getrtmm$$(v.tipo.set)
    for (var k in omm)mm[k]=omm[k];
    mm.$an=v.tipo.set.setter$anns;
    v.tipo.set.$crtmm$=mm;
  }
  return $$openSetter;
}
OpenSetter.$crtmm$=function(){return{mod:$CCMM$,'super':{t:Basic},satisfies:[{t:SetterDeclaration$meta$declaration}],d:['ceylon.language.meta.declaration','SetterDeclaration']};};
function $init$OpenSetter(){
    if (OpenSetter.$$===undefined){
        initTypeProto(OpenSetter,'ceylon.language.meta.declaration::OpenSetter',Basic,SetterDeclaration$meta$declaration);
        (function($$openSetter){
            defineAttr($$openSetter,'variable',function(){return this.variable_;},undefined,function(){return{mod:$CCMM$,$t:{t:ValueDeclaration$meta$declaration},$cont:OpenSetter,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','SetterDeclaration','$at','variable']};});
        })(OpenSetter.$$.prototype);
    }
    return OpenSetter;
}
exports.$init$OpenSetter=$init$OpenSetter;
$init$OpenSetter();

//ClassDefinition OpenClass at X (47:0-70:0)
function OpenClass(pkg, meta, that){
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
    that.name_=_mm.d[_mm.d.length-1];
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
  return this.$apply(targs,$mptypes);//TODO tupleize, targs $apply
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

      defineAttr($$openClass,'string',function(){
        return String$("class " + this.qualifiedName);
      },undefined,function(){return{mod:$CCMM$,$t:{t:String$},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language','Object']};}); 
      defineAttr($$openClass,'anonymous',function(){
        return this.meta.$mt==='obj';
      },undefined,function(){return{mod:$CCMM$,$t:{t:Boolean$},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassDeclaration','$at','anonymous']};});
      defineAttr($$openClass,'abstract',function(){
        return $findAnnotation(this.tipo,AbstractAnnotation)!==null;
      },undefined,function(){return{mod:$CCMM$,$t:{t:Boolean$},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassDeclaration','$at','abstract']};});
      defineAttr($$openClass,'$final',function(){
        return $findAnnotation(this.tipo,FinalAnnotation)!==null;
      },undefined,function(){return{mod:$CCMM$,$t:{t:Boolean$},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassDeclaration','$at','final']};});
      defineAttr($$openClass,'annotation',function(){
        return $findAnnotation(this.tipo,AnnotationAnnotation)!==null;
      },undefined,function(){return{mod:$CCMM$,$t:{t:Boolean$},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionalDeclaration','$at','annotation']};});
           
            //AttributeGetterDefinition openType at X (61:2-61:43)
            defineAttr($$openClass,'openType',function(){
              return FreeClass(this);
            },undefined,function(){return{mod:$CCMM$,$t:{t:OpenType$meta$declaration},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassDeclaration','$at','openType']};});
            
            $$openClass.getParameterDeclaration=function (name$15){
      var pd=this.parameterDeclarations;
      for (var i=0; i < pd.length; i++) {
        if (name$6.equals(pd[i].name))return pd[i];
      }
      return null;
            };
            $$openClass.getParameterDeclaration.$crtmm$=function(){return{mod:$CCMM$,$t:{ t:'u', l:[{t:Null},{t:FunctionOrValueDeclaration$meta$declaration}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}}],$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassDeclaration','$m','getParameterDeclaration']};};
            
            //AttributeDeclaration superclassDeclaration at X (68:2-68:86)
            defineAttr($$openClass,'extendedType',function(){
              var sc = this.tipo.$crtmm$['super'];
              if (sc === undefined)return null;
              var mm = getrtmm$$(sc.t);
              var fc=FreeClass(OpenClass(getModules$meta().find(mm.mod['$mod-name'],mm.mod['$mod-version']).findPackage(mm.d[0]), sc.t));
              if (sc.a)fc.declaration._targs=sc.a;
              return fc;
            },undefined,function(){return{mod:$CCMM$,$t:{ t:'u', l:[{t:Null},{t:OpenClassType$meta$declaration}]},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassDeclaration','$at','extendedType']};});
            
            //AttributeDeclaration interfaceDeclarations at X (69:2-69:89)
            defineAttr($$openClass,'satisfiedTypes',function(){
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
                return ArraySequence(rv,{Element$Iterable:{t:OpenInterfaceType$meta$declaration}});
              }
              return getEmpty();
            },undefined,function(){return{mod:$CCMM$,$t:{t:Sequential,a:{Element$Sequential:{t:OpenInterfaceType$meta$declaration}}},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassDeclaration','$at','satisfiedTypes']};});
            defineAttr($$openClass,'name',function(){return this.name_;},undefined,function(){return{mod:$CCMM$,$t:{t:String$},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassDeclaration','$at','name']};});
            defineAttr($$openClass,'containingPackage',function(){return this._pkg;},undefined,function(){return{mod:$CCMM$,$t:{t:Package$meta$declaration},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassDeclaration','$at','containingPackage']};});
            defineAttr($$openClass,'containingModule',function(){return this._pkg.container;},undefined,function(){return{mod:$CCMM$,$t:{t:Module$meta$declaration},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassDeclaration','$at','containingModule']};});
            defineAttr($$openClass,'toplevel',function(){return this.toplevel_;},undefined,function(){return{mod:$CCMM$,$t:{t:Boolean$},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassDeclaration','$at','toplevel']};});

    defineAttr($$openClass,'qualifiedName',function(){
      return String$($qname(this.tipo));
    },undefined,function(){return{mod:$CCMM$,$t:{t:String$},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Declaration','$at','qualifiedName']};});

      $$openClass.equals=function(other) {
        return isOfType(other, {t:OpenClass}) && other.tipo===this.tipo;
      }
    })(OpenClass.$$.prototype);
  }
  return OpenClass;
}
exports.$init$OpenClass=$init$OpenClass;
$init$OpenClass();

//ClassDefinition OpenInterface at X (72:0-92:0)
function OpenInterface(pkg, meta, that) {
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
    that.name_=_mm.d[_mm.d.length-1];
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
        return isOfType(other, {t:OpenInterface}) && other.tipo==this.tipo;
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
  return this.$apply(targs,$mptypes); //TODO mptypes $apply
}
            
defineAttr($$openInterface,'string',function(){
  return String$("interface " + this.qualifiedName);
},undefined,function(){return{mod:$CCMM$,$t:{t:String$},$cont:OpenInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language','Object']};}); 
           
            defineAttr($$openInterface,'openType',function(){
              return FreeInterface(this);
            },undefined,function(){return{mod:$CCMM$,$t:{t:OpenType$meta$declaration},$cont:OpenInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','InterfaceDeclaration','$at','openType']};});
            defineAttr($$openInterface,'extendedType',function(){
              var sc = this.tipo.$crtmm$['super'];
              if (sc === undefined)return null;
              var mm = getrtmm$$(sc.t);
              return FreeClass(OpenClass(getModules$meta().find(mm.mod['$mod-name'],mm.mod['$mod-version']).findPackage(mm.d[0]), sc.t));
            },undefined,function(){return{mod:$CCMM$,$t:{ t:'u', l:[{t:Null},{t:OpenClasType$meta$declaration}]},$cont:OpenInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','InterfaceDeclaration','$at','extendedType']};});
            
            //AttributeDeclaration interfaceDeclarations at X (91:2-91:89)
            defineAttr($$openInterface,'satisfiedTypes',function(){
              var ints = this.tipo.$crtmm$['satisfies'];
              if (ints && ints.length) {
                var rv = [];
                for (var i=0; i < ints.length; i++) {
                  var ifc = ints[i].t;
                  var mm = getrtmm$$(ifc);
                  rv.push(FreeInterface(OpenInterface(getModules$meta().find(mm.mod['$mod-name'],mm.mod['$mod-version']).findPackage(mm.d[0]), ifc)));
                }
                return ArraySequence(rv,{Element$Iterable:{t:OpenInterfaceType$meta$declaration}});
              }
              return getEmpty();
            },undefined,function(){return{mod:$CCMM$,$t:{t:Sequential,a:{Element$Sequential:{t:OpenInterfaceType$meta$declaration}}},$cont:OpenInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','InterfaceDeclaration','$at','satisfiedTypes']};});
            defineAttr($$openInterface,'name',function(){return this.name_;},undefined,function(){return{mod:$CCMM$,$t:{t:String$},$cont:OpenInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','InterfaceDeclaration','$at','name']};});
            defineAttr($$openInterface,'containingPackage',function(){return this._pkg;},undefined,function(){return{mod:$CCMM$,$t:{t:Package$meta$declaration},$cont:OpenInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','InterfaceDeclaration','$at','containingPackage']};});
            defineAttr($$openInterface,'containingModule',function(){return this._pkg.container;},undefined,function(){return{mod:$CCMM$,$t:{t:Module$meta$declaration},$cont:OpenInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','InterfaceDeclaration','$at','containingModule']};});
            defineAttr($$openInterface,'toplevel',function(){return this.toplevel_;},undefined,function(){return{mod:$CCMM$,$t:{t:Boolean$},$cont:OpenInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','InterfaceDeclaration','$at','toplevel']};});

    defineAttr($$openInterface,'qualifiedName',function(){
      return String$($qname(this.tipo));
    },undefined,function(){return{mod:$CCMM$,$t:{t:String$},$cont:OpenInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Declaration','$at','qualifiedName']};});
        })(OpenInterface.$$.prototype);
    }
    return OpenInterface;
}
exports.$init$OpenInterface=$init$OpenInterface;
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

      defineAttr($$openAlias,'extendedType',function(){
        return _openTypeFromTarg(this._alias);
      },undefined,function(){return{mod:$CCMM$,$t:{t:OpenType$meta$declaration},$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$at','extendedType']};});
      defineAttr($$openAlias,'qualifiedName',function(){
        return String$($qname(this._alias.$crtmm$));
      },undefined,function(){return{mod:$CCMM$,$t:{t:String$},$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$at','qualifiedName']};});
      defineAttr($$openAlias,'toplevel',function(){
        return this._alias.$cont===undefined;
      },undefined,function(){return{mod:$CCMM$,$t:{t:Boolean},$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$at','toplevel']};});
      defineAttr($$openAlias,'containingPackage',function(){
        throw Error("IMPL OpenAlias.containingPackage");
      },undefined,function(){return{mod:$CCMM$,$t:{t:Package$meta$declaration},$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$at','containingPackage']};});
      defineAttr($$openAlias,'containingModule',function(){
        throw Exception(String$("IMPL OpenAlias.containingModule"));
      },undefined,function(){return{mod:$CCMM$,$t:{t:Module$meta$declaration},$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$at','containingModule']};});
      defineAttr($$openAlias,'container',function(){
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
      defineAttr($$openAlias,'openType',function(){
        return this.extendedType;
      },undefined,function(){return{mod:$CCMM$,$t:{t:OpenType$meta$declaration},$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$at','openType']};});
      defineAttr($$openAlias,'typeParameterDeclarations',function(){
        var tps=this._alias.$crtmm$.$tp;
        if (tps) {
          var rv=[];
          for (var tp in tps) {
            rv.push(OpenTypeParam(this._alias, tp));
          }
          return ArraySequence(rv,{Element$Iterable:{t:TypeParameter$meta$declaration}});
        }
        return getEmpty();
      },undefined,function(){return{mod:$CCMM$,$t:{t:Sequential,a:{Element$Sequential:{t:TypeParameter$meta$declaration}}},$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$at','typeParameterDeclarations']};});
      $$openAlias.getTypeParameterDeclaration=function getTypeParameterDeclaration(name$2){
        var tp=this._alias.$crtmm$;
        if (tp.$tp) {
          var tpn=undefined;
          for (var ftn in tp.$tp) {
            if (ftn.substring(0,name$2.length+1)==name$2+'$') {
              tpn=ftn;
            }
          }
          tp=tpn;
        } else {
          tp=undefined;
        }
        return tp ? OpenTypeParam(this._alias, tp) : null;
      };$$openAlias.getTypeParameterDeclaration.$crtmm$=function(){return{mod:$CCMM$,$t:{ t:'u', l:[{t:Null},{t:TypeParameter$meta$declaration}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$},$an:function(){return[];}}],$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$m','getTypeParameterDeclaration']};};
      defineAttr($$openAlias,'name',function(){
        return String$(this._alias.$crtmm$.d[this._alias.$crtmm$.d.length-1]);
      },undefined,function(){return{mod:$CCMM$,$t:{t:String$},$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$at','name']};});
      defineAttr($$openAlias,'string',function(){return StringBuilder().append(String$("alias ")).append(this.qualifiedName).string;},undefined,function(){return{mod:$CCMM$,$t:{t:String$},$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$at','string']};});
  $$openAlias.equals=function equals(o) {
    if (o && isOfType(o, {t:OpenAlias})) {
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
exports.$init$OpenAlias=$init$OpenAlias;
$init$OpenAlias();

function OpenTypeParam(cont, nom, that){
  $init$OpenTypeParam();
  if (that===undefined)that=new OpenTypeParam.$$;
  that._cont=cont;
  that._fname=nom;
  if (nom.indexOf('$')>0)nom=nom.substring(0,nom.indexOf('$'));
  that._name=nom;
  TypeParameter$meta$declaration(that);
  return that;
}
OpenTypeParam.$crtmm$=function(){return{mod:$CCMM$,'super':{t:Basic},satisfies:[{t:TypeParameter$meta$declaration}],$an:function(){return[shared()];},d:['ceylon.language.meta.declaration','TypeParameter']};};
exports.OpenTypeParam=OpenTypeParam;
function $init$OpenTypeParam(){
  if (OpenTypeParam.$$===undefined){
    initTypeProto(OpenTypeParam,'ceylon.language.meta.declaration::OpenTypeParam',Basic,TypeParameter$meta$declaration);
    (function($$openTypeParam){
      //AttributeGetterDefinition container at caca.ceylon (7:4-7:94)
      defineAttr($$openTypeParam,'container',function(){return this._cont;},undefined,function(){return{mod:$CCMM$,$t:{t:NestableDeclaration$meta$declaration},$cont:OpenTypeParam,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','TypeParameter','$at','container']};});
      //AttributeGetterDefinition defaulted at caca.ceylon (8:4-8:82)
      defineAttr($$openTypeParam,'defaulted',function(){
        var tp=this._cont.$crtmm$.$tp[this._fname];
        if (tp) {
          return tp['def'] !== undefined;
        }
        throw wrapexc(Exception(String$("TypeParameter.defaulted",23)),'8:38-8:80','caca.ceylon');
      },undefined,function(){return{mod:$CCMM$,$t:{t:Boolean},$cont:OpenTypeParam,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','TypeParameter','$at','defaulted']};});
      //AttributeGetterDefinition defaultTypeArgument at caca.ceylon (9:4-9:104)
      defineAttr($$openTypeParam,'defaultTypeArgument',function(){
        var tp=this._cont.$crtmm$.$tp[this._fname];
        if (typeof(tp.def)==='string') {
          return OpenTvar(OpenTypeParam(this._cont, tp.def));
        }
        return tp.def?_openTypeFromTarg(tp.def):null;
      },undefined,function(){return{mod:$CCMM$,$t:{ t:'u', l:[{t:Null},{t:OpenType$meta$declaration}]},$cont:OpenTypeParam,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','TypeParameter','$at','defaultTypeArgument']};});
      //AttributeGetterDefinition variance at caca.ceylon (10:4-10:81)
      defineAttr($$openTypeParam,'variance',function(){
        var tp=this._cont.$crtmm$.$tp[this._fname];
        if (tp) {
          if (tp['var']==='out')return getCovariant$meta$declaration();
          if (tp['var']=== 'in')return getContravariant$meta$declaration();
          return getInvariant$meta$declaration();
        }
        throw wrapexc(Exception(String$("TypeParameter.variance",22)),'10:38-10:79','caca.ceylon');
      },undefined,function(){return{mod:$CCMM$,$t:{t:Variance$meta$declaration},$cont:OpenTypeParam,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','TypeParameter','$at','variance']};});
      //AttributeGetterDefinition satisfiedTypes at caca.ceylon (11:4-11:95)
      defineAttr($$openTypeParam,'satisfiedTypes',function(){
        var tp=this._cont.$crtmm$.$tp[this._fname];
        if (tp.satisfies) {
          var a=[];
          for (var i=0;i<tp.satisfies.length;i++) {
            a.push(_openTypeFromTarg(tp.satisfies[i]));
          }
          return ArraySequence(a,{Element$Iterable:{t:OpenType$meta$declaration}});
        }
        return getEmpty();
      },undefined,function(){return{mod:$CCMM$,$t:{t:Sequential,a:{Element$Sequential:{t:OpenType$meta$declaration}}},$cont:OpenTypeParam,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','TypeParameter','$at','satisfiedTypes']};});
      //AttributeGetterDefinition caseTypes at caca.ceylon (12:4-12:85)
      defineAttr($$openTypeParam,'caseTypes',function(){
        var tp=this._cont.$crtmm$.$tp[this._fname];
        if (tp.of) {
          var a=[];
          for (var i=0;i<tp.of.length;i++) {
            a.push(_openTypeFromTarg(tp.of[i]));
          }
          return ArraySequence(a,{Element$Iterable:{t:OpenType$meta$declaration}});
        }
        return getEmpty();
      },undefined,function(){return{mod:$CCMM$,$t:{t:Sequential,a:{Element$Sequential:{t:OpenType$meta$declaration}}},$cont:OpenTypeParam,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','TypeParameter','$at','caseTypes']};});
      //AttributeGetterDefinition name at caca.ceylon (14:4-14:71)
      defineAttr($$openTypeParam,'name',function(){return String$(this._name);},undefined,function(){return{mod:$CCMM$,$t:{t:String$},$cont:OpenTypeParam,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Declaration','$at','name']};});
      //AttributeGetterDefinition qualifiedName at caca.ceylon (15:4-15:81)
      defineAttr($$openTypeParam,'qualifiedName',function(){
        return String$($qname(this._cont)+"."+this._name);
      },undefined,function(){return{mod:$CCMM$,$t:{t:String$},$cont:OpenTypeParam,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Declaration','$at','qualifiedName']};});
defineAttr($$openTypeParam,'hash',function(){return this.string.hash;},undefined,function(){return {mod:$CCMM$,$t:{t:Integer},d:['ceylon.language','Object','$at','hash']};});
      defineAttr($$openTypeParam,'string',function(){return String$("given " + this.qualifiedName);},undefined,function(){return{mod:$CCMM$,$t:{t:String$},$cont:OpenTypeParam,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Declaration','$at','string']};});
      $$openTypeParam.equals=function(o) {
        return isOfType(o,{t:OpenTypeParam}) && o._cont==this._cont && o._fname==this._fname;
      }
    })(OpenTypeParam.$$.prototype);
  }
  return OpenTypeParam;
}
exports.$init$OpenTypeParam=$init$OpenTypeParam;
$init$OpenTypeParam();

function OpenTvar(p$2, $$openTvar){
    $init$OpenTvar();
    if ($$openTvar===undefined)$$openTvar=new OpenTvar.$$;
    $$openTvar.p$2=p$2;
    OpenTypeVariable$meta$declaration($$openTvar);
    $$openTvar.$prop$getDeclaration={$crtmm$:function(){return{mod:$CCMM$,$t:{t:TypeParameter$meta$declaration},$cont:OpenTvar,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','OpenTvar','$at','declaration']};}};
    $$openTvar.$prop$getDeclaration.get=function(){return declaration};
    return $$openTvar;
}
OpenTvar.$crtmm$=function(){return{mod:$CCMM$,'super':{t:Basic},satisfies:[{t:OpenTypeVariable$meta$declaration}],$an:function(){return[shared()];},d:['ceylon.language.meta.declaration','OpenTvar']};};
exports.OpenTvar=OpenTvar;
function $init$OpenTvar(){
  if (OpenTvar.$$===undefined){
    initTypeProto(OpenTvar,'ceylon.language.meta.declaration::OpenTvar',Basic,$init$OpenTypeVariable$meta$declaration());
    (function($$openTvar){
      defineAttr($$openTvar,'string',function(){return this.p$2.qualifiedName;},undefined,function(){return{mod:$CCMM$,$t:{t:String$},$cont:OpenTvar,$an:function(){return[shared(),actual()];},d:['ceylon.language','Object','$at','string']};});
            defineAttr($$openTvar,'declaration',function(){
                return this.p$2;
            },undefined,function(){return{mod:$CCMM$,$t:{t:TypeParameter$meta$declaration},$cont:OpenTvar,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','OpenTypeVariable','$at','declaration']};});
$$openTvar.equals=function(o) {
  return isOfType(o, {t:OpenTvar}) && this.p$2.equals(o.p$2);
}
        })(OpenTvar.$$.prototype);
    }
    return OpenTvar;
}
exports.$init$OpenTvar=$init$OpenTvar;
$init$OpenTvar();

function FreeUnion(ts$2, $$freeUnion){
    $init$FreeUnion();
    if ($$freeUnion===undefined)$$freeUnion=new FreeUnion.$$;
    $$freeUnion.ts$2_=ts$2;
    OpenUnion$meta$declaration($$freeUnion);
    $$freeUnion.$prop$getCaseTypes={$crtmm$:function(){return{mod:$CCMM$,$t:{t:List,a:{Element$List:{t:OpenType$meta$declaration}}},$cont:FreeUnion,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','OpenUnion','$at','caseTypes']};}};
    $$freeUnion.$prop$getCaseTypes.get=function(){return caseTypes};
    return $$freeUnion;
}
FreeUnion.$crtmm$=function(){return{mod:$CCMM$,'super':{t:Basic},satisfies:[{t:OpenUnion$meta$declaration}],$an:function(){return[shared()];},d:['ceylon.language.meta.declaration','OpenUnion']};};
exports.FreeUnion=FreeUnion;
function $init$FreeUnion(){
  if (FreeUnion.$$===undefined){
    initTypeProto(FreeUnion,'ceylon.language.meta.declaration::FreeUnion',Basic,OpenUnion$meta$declaration);
    (function($$freeUnion){
      $$freeUnion.equals=function(u) {
        if(isOfType(u,{t:FreeUnion})) {
          var mine=this.caseTypes;
          var his=u.caseTypes;
          if (mine.size==his.size) {
            for (var i=0;i<mine.length;i++) {
              if (!his.contains(mine[i]))return false;
            }
            return true;
          }
        }
        return false;
      }
defineAttr($$freeUnion,'string',function(){
  var s="";
  var first=true;
  for (var i=0;i<this.ts$2.size;i++) {
    if (first)first=false;else s+="|";
    s+=this.ts$2.$get(i).string;
  }
  return String$(s);
},undefined,function(){return{mod:$CCMM$,$t:{t:String$},d:['ceylon.language','Object','$at','string']};});
defineAttr($$freeUnion,'hash',function(){
  var s=this.ts$2.size;
  for (var i=0;i<this.ts$2.size;i++) {
    s+=this.ts$2.$get(i).string.hash;
  }
  return s;
},undefined,function(){return{mod:$CCMM$,$t:{t:Integer},d:['ceylon.language','Object','$at','hash']};});
            defineAttr($$freeUnion,'caseTypes',function(){
                var $$freeUnion=this;
                return $$freeUnion.ts$2;
            },undefined,function(){return{mod:$CCMM$,$t:{t:List,a:{Element$List:{t:OpenType$meta$declaration}}},$cont:FreeUnion,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','OpenUnion','$at','caseTypes']};});
            defineAttr($$freeUnion,'ts$2',function(){return this.ts$2_;},undefined,function(){return{mod:$CCMM$,$t:{t:List,a:{Element$List:{t:OpenType$meta$declaration}}},$cont:FreeUnion,d:['ceylon.language.meta.declaration','OpenUnion','$at','ts']};});
        })(FreeUnion.$$.prototype);
    }
    return FreeUnion;
}
exports.$init$FreeUnion=$init$FreeUnion;
$init$FreeUnion();
function FreeIntersection(ts$3, $$freeIntersection){
    $init$FreeIntersection();
    if ($$freeIntersection===undefined)$$freeIntersection=new FreeIntersection.$$;
    $$freeIntersection.ts$3_=ts$3;
    OpenIntersection$meta$declaration($$freeIntersection);
    $$freeIntersection.$prop$getSatisfiedTypes={$crtmm$:function(){return{mod:$CCMM$,$t:{t:List,a:{Element$List:{t:OpenType$meta$declaration}}},$cont:FreeIntersection,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','OpenIntersection','$at','satisfiedTypes']};}};
    $$freeIntersection.$prop$getSatisfiedTypes.get=function(){return satisfiedTypes};
    return $$freeIntersection;
}
FreeIntersection.$crtmm$=function(){return{mod:$CCMM$,'super':{t:Basic},satisfies:[{t:OpenIntersection$meta$declaration}],$an:function(){return[shared()];},d:['ceylon.language.meta.declaration','OpenIntersection']};};
exports.FreeIntersection=FreeIntersection;
function $init$FreeIntersection(){
  if (FreeIntersection.$$===undefined){
    initTypeProto(FreeIntersection,'ceylon.language.meta.declaration::FreeIntersection',Basic,OpenIntersection$meta$declaration);
    (function($$freeIntersection){

$$freeIntersection.equals=function(u) {
  if(isOfType(u,{t:FreeIntersection})) {
    var mine=this.satisfiedTypes;
    var his=u.satisfiedTypes;
    if (mine.size==his.size) {
      for (var i=0;i<mine.length;i++) {
        if (!his.contains(mine[i])){
return false;
}
      }
return true;
    }
  }
return false;
}
defineAttr($$freeIntersection,'string',function(){
  var s="";
  var first=true;
  for (var i=0;i<this.ts$3.size;i++) {
    if (first)first=false;else s+="&";
    s+=this.ts$3.$get(i).string;
  }
  return String$(s);
},undefined,function(){return{mod:$CCMM$,$t:{t:String$},d:['ceylon.language','Object','$at','string']};});
defineAttr($$freeIntersection,'hash',function(){
  var s=this.ts$3.size;
  for (var i=0;i<this.ts$3.size;i++) {
    s+=this.ts$3.$get(i).string.hash;
  }
  return s;
},undefined,function(){return{mod:$CCMM$,$t:{t:Integer},d:['ceylon.language','Object','$at','hash']};});

            defineAttr($$freeIntersection,'satisfiedTypes',function(){
                var $$freeIntersection=this;
                return $$freeIntersection.ts$3;
            },undefined,function(){return{mod:$CCMM$,$t:{t:List,a:{Element$List:{t:OpenType$meta$declaration}}},$cont:FreeIntersection,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','OpenIntersection','$at','satisfiedTypes']};});
            defineAttr($$freeIntersection,'ts$3',function(){return this.ts$3_;},undefined,function(){return{mod:$CCMM$,$t:{t:List,a:{Element$List:{t:OpenType$meta$declaration}}},$cont:FreeIntersection,d:['ceylon.language.meta.declaration','OpenIntersection','$at','ts']};});
        })(FreeIntersection.$$.prototype);
    }
    return FreeIntersection;
}
exports.$init$FreeIntersection=$init$FreeIntersection;
$init$FreeIntersection();

function FunParamDecl(cont,param,$$funParamDecl){
  $init$FunParamDecl();
  if ($$funParamDecl===undefined)$$funParamDecl=new FunParamDecl.$$;
  FunctionDeclaration$meta$declaration($$funParamDecl);
  $$funParamDecl.cont=cont;
  $$funParamDecl.param=param;
  $$funParamDecl.tipo={$crtmm$:{$cont:cont.tipo,$t:param.$t,$ps:param.$ps,$mt:'prm',d:cont.tipo.$crtmm$.d,$an:param.$an}};

  $$funParamDecl.$prop$getParameter={$crtmm$:function(){return{mod:$CCMM$,$t:{t:Boolean$},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','parameter']};}};
  $$funParamDecl.$prop$getParameter.get=function(){return true;};
  $$funParamDecl.$prop$getAnnotation={$crtmm$:function(){return{mod:$CCMM$,$t:{t:Boolean$},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','annotation']};}};
  $$funParamDecl.$prop$getAnnotation.get=function(){return false;};
  $$funParamDecl.$prop$getShared={$crtmm$:function(){return{mod:$CCMM$,$t:{t:Boolean$},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','shared']};}};
  $$funParamDecl.$prop$getShared.get=function(){return false;};
  $$funParamDecl.$prop$getToplevel={$crtmm$:function(){return{mod:$CCMM$,$t:{t:Boolean$},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','toplevel']};}};
  $$funParamDecl.$prop$getToplevel.get=function(){return false;};
  $$funParamDecl.$prop$getFormal={$crtmm$:function(){return{mod:$CCMM$,$t:{t:Boolean$},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','formal']};}};
  $$funParamDecl.$prop$getFormal.get=function(){return false;};
  $$funParamDecl.$prop$getDefault={$crtmm$:function(){return{mod:$CCMM$,$t:{t:Boolean$},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','default']};}};
  $$funParamDecl.$prop$getDefault.get=function(){return false};
  $$funParamDecl.$prop$getActual={$crtmm$:function(){return{mod:$CCMM$,$t:{t:Boolean$},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','actual']};}};
  $$funParamDecl.$prop$getActual.get=function(){return false;};
  return $$funParamDecl;
}
FunParamDecl.$crtmm$=function(){return{mod:$CCMM$,'super':{t:Basic},$ps:[],satisfies:[{t:FunctionDeclaration$meta$declaration}],d:['ceylon.language.meta.declaration','FunParamDecl']};};
function $init$FunParamDecl(){
  if (FunParamDecl.$$===undefined){
    initTypeProto(FunParamDecl,'ceylon.language.meta.declaration::FunParamDecl',Basic,FunctionDeclaration$meta$declaration);
    (function($$funParamDecl){

      defineAttr($$funParamDecl,'parameter',function(){return true;},undefined,function(){return{mod:$CCMM$,$t:{t:Boolean$},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','parameter']};});
      
      //AttributeGetterDef defaulted at caca.ceylon (8:2-8:71)
      defineAttr($$funParamDecl,'defaulted',function(){
        return this.param.$def!==undefined;
      },undefined,function(){return{mod:$CCMM$,$t:{t:Boolean$},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','defaulted']};});
      //AttributeGetterDef variadic at caca.ceylon (9:2-9:69)
      defineAttr($$funParamDecl,'variadic',function(){
        return this.param.seq!==undefined;
      },undefined,function(){return{mod:$CCMM$,$t:{t:Boolean$},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','variadic']};});
      //AttributeGetterDef container at caca.ceylon (11:2-11:91)
      defineAttr($$funParamDecl,'container',function(){
        return this.cont;
      },undefined,function(){return{mod:$CCMM$,$t:{t:'u', l:[{t:Package$meta$declaration},{t:NestableDeclaration$meta$declaration}]},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','container']};});
      //AttributeGetterDef containingPackage at caca.ceylon (12:2-12:87)
      defineAttr($$funParamDecl,'containingPackage',function(){
        return this.cont.containingPackage;
      },undefined,function(){return{mod:$CCMM$,$t:{t:Package$meta$declaration},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','containingPackage']};});
      //AttributeGetterDef containingModule at caca.ceylon (13:2-13:84)
      defineAttr($$funParamDecl,'containingModule',function(){
        return this.cont.containingModule;
      },undefined,function(){return{mod:$CCMM$,$t:{t:Module$meta$declaration},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','containingModule']};});
      //AttributeGetterDef openType at caca.ceylon (14:2-14:70)
      defineAttr($$funParamDecl,'openType',function(){
        var t = this.param.$t;
        if (typeof(t)==='string')return OpenTvar(OpenTypeParam(this.cont,t));
        return _openTypeFromTarg(t);
      },undefined,function(){return{mod:$CCMM$,$t:{t:OpenType$meta$declaration},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','openType']};});
      //AttributeDecl annotation at caca.ceylon (15:2-15:40)
      defineAttr($$funParamDecl,'annotation',function(){return false;},undefined,function(){return{mod:$CCMM$,$t:{t:Boolean$},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','annotation']};});
      defineAttr($$funParamDecl,'shared',function(){return false;},undefined,function(){return{mod:$CCMM$,$t:{t:Boolean$},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','shared']};});
      defineAttr($$funParamDecl,'toplevel',function(){return false;},undefined,function(){return{mod:$CCMM$,$t:{t:Boolean$},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','toplevel']};});
      defineAttr($$funParamDecl,'formal',function(){return false;},undefined,function(){return{mod:$CCMM$,$t:{t:Boolean$},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','formal']};});
      defineAttr($$funParamDecl,'$default',function(){return false;},undefined,function(){return{mod:$CCMM$,$t:{t:Boolean$},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','default']};});
      defineAttr($$funParamDecl,'actual',function(){return false;},undefined,function(){return{mod:$CCMM$,$t:{t:Boolean$},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','actual']};});
      defineAttr($$funParamDecl,'qualifiedName',function(){
        return String$($qname(cont.tipo.$crtmm$)+"."+this.param.$nm);
      },undefined,function(){return{mod:$CCMM$,$t:{t:String$},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','qualifiedName']};});
      defineAttr($$funParamDecl,'name',function(){
        return String$(this.param.$nm);
      },undefined,function(){return{mod:$CCMM$,$t:{t:String$},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','name']};});
      $$funParamDecl.getParameterDeclaration=function getParameterDeclaration(name$10){
      var pd=this.parameterDeclarations;
      for (var i=0; i < pd.length; i++) {
        if (name$6.equals(pd[i].name))return pd[i];
      }
      return null;
      };$$funParamDecl.getParameterDeclaration.$crtmm$=function(){return{mod:$CCMM$,$t:{t:'u', l:[{t:Null},{t:FunctionOrValueDeclaration$meta$declaration}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$},$an:function(){return[];}}],$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$m','getParameterDeclaration']};};
      
      //MethodDef apply at caca.ceylon (31:2-32:74)
      $$funParamDecl.$apply=function $apply(typeArguments$11,$$$mptypes){
          if(typeArguments$11===undefined){typeArguments$11=getEmpty();}
          throw Exception(String$("IMPL FunParamDecl.apply"));
      };$$funParamDecl.$apply.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Function$meta$model,a:{Arguments:'Arguments',Type:'Return'}},$ps:[{$nm:'typeArguments',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element$Sequential:{t:Type$meta$model,a:{Type:{t:Anything}}}}},$an:function(){return[];}}],$cont:FunParamDecl,$tp:{Return:{'def':{t:Anything}},Arguments:{'satisfies':[{t:Sequential,a:{Element$Sequential:{t:Anything}}}],'def':{t:Nothing}}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$m','apply']};};
      
      //MethodDef memberApply at caca.ceylon (33:2-34:80)
      $$funParamDecl.memberApply=function memberApply(containerType$12,typeArguments$13,$$$mptypes){
          if(typeArguments$13===undefined){typeArguments$13=getEmpty();}
          throw Exception(String$("IMPL FunParamDecl.memberApply"));
      };$$funParamDecl.memberApply.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Method$meta$model,a:{Arguments:'Arguments',Type:'Return',Container:'Container'}},$ps:[{$nm:'containerType',$mt:'prm',$t:{t:Type$meta$model,a:{Type:'Container'}},$an:function(){return[];}},{$nm:'typeArguments',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element$Sequential:{t:Type$meta$model,a:{Type:{t:Anything}}}}},$an:function(){return[];}}],$cont:FunParamDecl,$tp:{Container:{'def':{t:Nothing}},Return:{'def':{t:Anything}},Arguments:{'satisfies':[{t:Sequential,a:{Element$Sequential:{t:Anything}}}],'def':{t:Nothing}}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$m','memberApply']};};
    })(FunParamDecl.$$.prototype);
  }
  return FunParamDecl;
}
exports.$init$FunParamDecl=$init$FunParamDecl;
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

      defineAttr($$valParamDecl,'parameter',function(){return true;},undefined,function(){return{mod:$CCMM$,$t:{t:Boolean$},$cont:ValParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','parameter']};});
      defineAttr($$valParamDecl,'defaulted',function(){
        return this.param.$def!==undefined;
      },undefined,function(){return{mod:$CCMM$,$t:{t:Boolean$},$cont:ValParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','defaulted']};});
      defineAttr($$valParamDecl,'variadic',function(){
        return this.param.seq!==undefined;
      },undefined,function(){return{mod:$CCMM$,$t:{t:Boolean$},$cont:ValParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','variadic']};});
      defineAttr($$valParamDecl,'variable',function(){
        return $findAnnotation(this.param,VariableAnnotation)!==null;
      },undefined,function(){return{mod:$CCMM$,$t:{t:Boolean$},$cont:ValParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','variable']};});
      defineAttr($$valParamDecl,'container',function(){
        return this.cont;
      },undefined,function(){return{mod:$CCMM$,$t:{t:'u', l:[{t:Package$meta$declaration},{t:NestableDeclaration$meta$declaration}]},$cont:ValParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','container']};});
      defineAttr($$valParamDecl,'containingPackage',function(){
        return this.cont.containingPackage;
      },undefined,function(){return{mod:$CCMM$,$t:{t:Package$meta$declaration},$cont:ValParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','containingPackage']};});
      defineAttr($$valParamDecl,'containingModule',function(){
        return this.cont.containingModule;
      },undefined,function(){return{mod:$CCMM$,$t:{t:Module$meta$declaration},$cont:ValParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','containingModule']};});
      defineAttr($$valParamDecl,'openType',function(){
        var t = this.param.$t;
        if (typeof(t)==='string')return OpenTvar(OpenTypeParam(this.cont,t));
        return _openTypeFromTarg(t);
      },undefined,function(){return{mod:$CCMM$,$t:{t:OpenType$meta$declaration},$cont:ValParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','openType']};});
      defineAttr($$valParamDecl,'shared',function(){return false;},undefined,function(){return{mod:$CCMM$,$t:{t:Boolean$},$cont:ValParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','shared']};});
      defineAttr($$valParamDecl,'toplevel',function(){return false;},undefined,function(){return{mod:$CCMM$,$t:{t:Boolean$},$cont:ValParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','toplevel']};});
      defineAttr($$valParamDecl,'formal',function(){return false;},undefined,function(){return{mod:$CCMM$,$t:{t:Boolean$},$cont:ValParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','formal']};});
      defineAttr($$valParamDecl,'$default',function(){return false;},undefined,function(){return{mod:$CCMM$,$t:{t:Boolean$},$cont:ValParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','default']};});
      defineAttr($$valParamDecl,'actual',function(){return false;},undefined,function(){return{mod:$CCMM$,$t:{t:Boolean$},$cont:ValParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','actual']};});
      defineAttr($$valParamDecl,'qualifiedName',function(){
        return String$($qname(this.cont.tipo.$crtmm$)+"."+this.param.$nm);
      },undefined,function(){return{mod:$CCMM$,$t:{t:String$},$cont:ValParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','qualifiedName']};});
      defineAttr($$valParamDecl,'name',function(){
        return String$(this.param.$nm);
      },undefined,function(){return{mod:$CCMM$,$t:{t:String$},$cont:ValParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','name']};});
      //MethodDef apply at caca.ceylon (57:2-57:84)
      $$valParamDecl.$apply=function $apply($$$mptypes){
          var $$valParamDecl=this;
          throw Exception(String$("IMPL ValParamDecl.apply"));
      };$$valParamDecl.$apply.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Value$meta$model,a:{Type:'Type'}},$ps:[],$cont:ValParamDecl,$tp:{Type:{'def':{t:Anything}}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$m','apply']};};
      //MethodDef memberApply at caca.ceylon (58:2-58:166)
      $$valParamDecl.memberApply=function memberApply(containerType$20,$$$mptypes){
          var $$valParamDecl=this;
          throw Exception(String$("IMPL ValParamDecl.memberApply"));
      };$$valParamDecl.memberApply.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Attribute$meta$model,a:{Type:'Type',Container:'Container'}},$ps:[{$nm:'containerType',$mt:'prm',$t:{t:Type$meta$model,a:{Type:'Container'}},$an:function(){return[];}}],$cont:ValParamDecl,$tp:{Container:{'def':{t:Nothing}},Type:{'def':{t:Anything}}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$m','memberApply']};};
    })(ValParamDecl.$$.prototype);
  }
  return ValParamDecl;
}
exports.$init$ValParamDecl=$init$ValParamDecl;
$init$ValParamDecl();
