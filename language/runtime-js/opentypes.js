function $findAnnotation(cont,ant) {
  if (typeof(cont.$$metamodel$$)==='function')cont.$$metamodel$$=cont.$$metamodel$$();
  if (typeof(cont.$$metamodel$$.$an)==='function')cont.$$metamodel$$.$an=cont.$$metamodel$$.$an();
  if (!cont.$$metamodel$$.$an)return null;
  for (var i=0; i < cont.$$metamodel$$.$an.length; i++) {
    if (isOfType(cont.$$metamodel$$.$an[i],{t:ant}))return cont.$$metamodel$$.$an[i];
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
    var imm=cont.$$metamodel$$;
    if (typeof(imm)==='function'){imm=imm();cont.$$metamodel$$=imm;}
    if (mt==='cls'||mt==='ifc')nm=nm+'$'+imm.d[imm.d.length-1];
  }else {
    nm+=pkg.suffix;
  }
  var out=cont?cont.$$.prototype:mod.meta;
  var rv=out[nm];
  if (rv===undefined)rv=out['$init$'+nm];
  if (rv===undefined)rv=out['$'+nm];
  return rv;
}
//Pass a {t:Bla} and get a FreeClass,FreeInterface,etc (OpenType).
function _openTypeFromTarg(targ) {
  if (targ.t==='u' || targ.t==='i') {
    var tl=[];
    for (var i=0; i < targ.l.length; i++) {
      tl.push(_openTypeFromTarg(targ.l[i]));
    }
    return (targ.t==='u'?FreeUnion:FreeIntersection)(tl.reifyCeylonType({Element:{t:OpenType$meta$declaration}}));
  } else if (targ.t==='T') {
    targ=$retuple(targ);
  }
  var mm=targ.t.$$metamodel$$;
  if (typeof(mm)==='function') {
    mm=mm(); targ.t.$$metamodel$$=mm;
  }
  var lit = typeLiteral$meta({Type:targ.t});
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
  $$freeClass.$prop$getDeclaration={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:ClassDeclaration$meta$declaration},$cont:FreeClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','OpenClassType','$at','declaration']};}};
  $$freeClass.$prop$getDeclaration.get=function(){return declaration};
  return $$freeClass;
}
FreeClass.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Basic},satisfies:[{t:OpenClassType$meta$declaration}],d:['ceylon.language.meta.declaration','OpenClassType']};};
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
FreeInterface.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Basic},satisfies:[{t:OpenInterfaceType$meta$declaration}],d:['ceylon.language.meta.declaration','OpenInterfaceType']};};
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
    var _mm=meta.$$metamodel$$;
    if (_mm === undefined) {
      //it's a metamodel
      that.meta=meta;
      that.tipo=_findTypeFromModel(pkg,meta);
      _mm=that.tipo.$$metamodel$$;
      if (typeof(_mm)==='function') {
        _mm=_mm();
        that.tipo.$$metamodel$$=_mm;
      }
    } else {
      //it's a type
      that.tipo = meta;
      if (typeof(_mm)==='function') {
        _mm=_mm();
        meta.$$metamodel$$=_mm;
      }
      that.meta = get_model(_mm);
    }
    that.name_=_mm.d[_mm.d.length-1];
    that.toplevel_=_mm.$cont===undefined;
    FunctionDeclaration$meta$declaration(that);
    return that;
}
OpenFunction.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Basic},satisfies:[{t:FunctionDeclaration$meta$declaration}],d:['ceylon.language.meta.declaration','FunctionDeclaration']};};
function $init$OpenFunction(){
  if (OpenFunction.$$===undefined){
    initTypeProto(OpenFunction,'ceylon.language.meta.declaration::OpenFunction',Basic,FunctionDeclaration$meta$declaration);
    (function($$openFunction){
defineAttr($$openFunction,'container',function(){
  if (this.$parent)return this.$parent;
  if (this.toplevel)return this.containingPackage;
  return typeLiteral$meta({Type:this.tipo.$$metamodel$$.$cont});
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:'u',l:[{t:NestableDeclaration$meta$declaration},{t:Package$meta$declaration}]},d:['ceylon.language.meta.declaration','NestableDeclaration','$at','container']};});
defineAttr($$openFunction,'annotation',function(){
  return $findAnnotation(this.tipo,AnnotationAnnotation)!==null;
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionalDeclaration','$at','annotation']};});

      $$openFunction.$apply=function $apply(types,$mptypes){
        var mm=this.tipo.$$metamodel$$;
        var ta={t:this.tipo};
        validate$typeparams(ta,mm.$tp,types);
        validate$params(mm.$ps,$mptypes.Arguments,"Wrong number of arguments when applying function");
        return ta.a?AppliedFunction(this.tipo,{Type:mm.$t,Arguments:tupleize$params(mm.$ps,ta.a)},undefined,ta.a):
          AppliedFunction(this.tipo,{Type:mm.$t,Arguments:tupleize$params(mm.$ps)});
      };$$openFunction.$apply.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Function$meta$model,a:{Arguments:{t:Nothing},Type:{t:Anything}}},$ps:[{$nm:'types',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element:{t:Type$meta$model}}}}],$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$m','apply']};};

      $$openFunction.memberApply=function memberApply(cont,types,$mptypes){
        var mm=this.tipo.$$metamodel$$;
        if (!(cont.tipo && extendsType({t:cont.tipo},{t:mm.$cont}))&&cont!==getNothingType$meta$model())
          throw IncompatibleTypeException$meta$model("Incompatible Container type argument");
        if (!extendsType(mm.$t,$mptypes.Return))throw IncompatibleTypeException$meta$model("Incompatible Return type argument");
        validate$params(mm.$ps,$mptypes.Arguments,"Wrong number of Arguments for memberApply");
        var ta={t:this.tipo};
        validate$typeparams(ta,mm.$tp,types);
        return AppliedMethod(this.tipo,types,{Container:$mptypes.Container,Type:mm.$t,Arguments:tupleize$params(mm.$ps,ta.a)});
      };$$openFunction.memberApply.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Method$meta$model,a:{Arguments:'Arguments',Type:'MethodType',Container:'Container'}},$ps:[{$nm:'types',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element:{t:Type$meta$model}}}}],$cont:OpenFunction,$tp:{Container:{},MethodType:{},Arguments:{'satisfies':[{t:Sequential,a:{Element:{t:Anything}}}]}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$m','memberApply']};};
            
            //AttributeDeclaration defaulted at X (25:4-25:44)
            defineAttr($$openFunction,'defaulted',function(){
                return false;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','defaulted']};});
            
            //AttributeDeclaration variadic at X (26:4-26:43)
            defineAttr($$openFunction,'variadic',function(){
                var $$openFunction=this;
                return false;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','variadic']};});
    $$openFunction.getParameterDeclaration=function (name$6){
      var pd=this.parameterDeclarations;
      for (var i=0; i < pd.length; i++) {
        if (name$6.equals(pd[i].name))return pd[i];
      }
      return null;
    };
    $$openFunction.getParameterDeclaration.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:FunctionOrValueDeclaration$meta$declaration}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}}],$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$m','getParameterDeclaration']};};
            
            //AttributeGetterDefinition openType at X (33:2-33:43)
    defineAttr($$openFunction,'openType',function(){
      var t = this.tipo.$$metamodel$$.$t;
      if (typeof(t)==='string')return OpenTvar(OpenTypeParam(this.tipo,t));
      return _openTypeFromTarg(t);
    },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:OpenType$meta$declaration},$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','openType']};});
            defineAttr($$openFunction,'name',function(){return this.name_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','name']};});
            defineAttr($$openFunction,'containingPackage',function(){return this._pkg;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Package$meta$declaration},$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','containingPackage']};});
            defineAttr($$openFunction,'containingModule',function(){return this._pkg.container;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Package$meta$declaration},$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','containingPackage']};});
            defineAttr($$openFunction,'toplevel',function(){return this.toplevel_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','toplevel']};});

    defineAttr($$openFunction,'string',function(){return String$("function " + this.qualifiedName);},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Declaration','$at','string']};});
    defineAttr($$openFunction,'qualifiedName',function(){
       return String$($qname(this.tipo));
    },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Declaration','$at','qualifiedName']};});
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
    var _mm=meta.$$metamodel$$;
    if (_mm === undefined) {
      //it's a metamodel
      that.meta=meta;
      if (meta['$mt']==='prm') {
        that.tipo={$$metamodel$$:meta};
        //TODO I think we need to do something else here
      } else {
        that.tipo=_findTypeFromModel(pkg,meta);
      }
      _mm = that.tipo.$$metamodel$$;
      if (typeof(_mm)==='function') {
        _mm=_mm();
        that.tipo.$$metamodel$$=_mm;
      }
    } else {
      //it's a type
      that.tipo = meta;
      if (typeof(_mm)==='function') {
        _mm=_mm();
        meta.$$metamodel$$=_mm;
      }
      that.meta = get_model(_mm);
    }
    that.name_=_mm.d===undefined?_mm['$nm']:_mm.d[_mm.d.length-1];
    that.toplevel_=_mm.$cont === undefined;
    ValueDeclaration$meta$declaration(that);
    return that;
}
OpenValue.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Basic},satisfies:[{t:ValueDeclaration$meta$declaration}],d:['ceylon.language.meta.declaration','ValueDeclaration']};};
function $init$OpenValue(){
  if (OpenValue.$$===undefined){
    initTypeProto(OpenValue,'ceylon.language.meta.declaration::OpenValue',Basic,ValueDeclaration$meta$declaration);
    (function($$openValue){

defineAttr($$openValue,'container',function(){
  if (this.$parent)return this.$parent;
  if (this.toplevel)return this.containingPackage;
  return typeLiteral$meta({Type:this.tipo.$$metamodel$$.$cont});
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:'u',l:[{t:NestableDeclaration$meta$declaration},{t:Package$meta$declaration}]},d:['ceylon.language.meta.declaration','NestableDeclaration','$at','container']};});
            
            //MethodDefinition apply at X (39:4-39:68)
      $$openValue.$apply=function $apply($$$mptypes){
        return (this.tipo.set?AppliedVariable:AppliedValue)(undefined,this.tipo,$$$mptypes);
      };$$openValue.$apply.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Value$meta$model,a:{Type:{t:Anything}}},$ps:[{$nm:'instance',$mt:'prm',$def:1,$t:{t:Anything}}],$cont:OpenValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$m','apply']};};

      $$openValue.memberApply=function memberApply(cont,$mptypes) {
        var mm=this.tipo.$$metamodel$$;
        if (typeof(mm)==='function'){
          mm=mm(); this.tipo.$$metamodel$$=mm;
        }
        if (!(cont.tipo && extendsType({t:cont.tipo},{t:mm.$cont}))&&cont!==getNothingType$meta$model())
          throw IncompatibleTypeException$meta$model("Incompatible Container type argument");
        if (!extendsType(mm.$t,$mptypes.Type))throw IncompatibleTypeException$meta$model("Incompatible Type type argument");
        return AppliedAttribute(this.meta.$nm,this.tipo,$mptypes);
      };$$openValue.memberApply.$$metamodel$$=function(){return{mod:$$METAMODEL$$,d:['ceylon.language.meta.declaration','ValueDeclaration','$m','memberApply']};};
            //AttributeDeclaration defaulted at X (40:4-40:44)
            defineAttr($$openValue,'defaulted',function(){
                return false;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:OpenValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','defaulted']};});
            
            //AttributeDeclaration variadic at X (41:4-41:43)
            defineAttr($$openValue,'variadic',function(){
                var $$openValue=this;
                return false;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:OpenValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','variadic']};});
            
  defineAttr($$openValue,'openType',function(){
    if (this.tipo) {
      var mm = this.tipo.$$metamodel$$;
      if (typeof(mm)==='function'){
        mm=mm(); this.tipo.$$metamodel$$=mm;
      }
if (typeof(mm.$t)==='string')return OpenTypeParam(mm.$cont,mm.$t);
      return _openTypeFromTarg(mm.$t);
    }
    throw Error("IMPL OpenValue.openType");
  },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:OpenType$meta$declaration},$cont:OpenValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','openType']};});

defineAttr($$openValue,'name',function(){return this.name_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},$cont:OpenValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','name']};});
defineAttr($$openValue,'containingPackage',function(){return this._pkg;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Package$meta$declaration},$cont:OpenValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','containingPackage']};});
defineAttr($$openValue,'containingModule',function(){return this._pkg.container;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Module$meta$declaration},$cont:OpenValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','containingModule']};});
defineAttr($$openValue,'toplevel',function(){return this.toplevel_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:OpenValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','toplevel']};});

  $$openValue.equals=function(other) {
    if (isOfType(other, {t:OpenValue}) && other.name.equals(this.name) && other.toplevel===this.toplevel && other.containingPackage.equals(this.containingPackage)) {
      return other.meta==this.meta;
    }
    return false;
  }
  defineAttr($$openValue,'string',function(){return String$("value " + this.qualifiedName);},undefined,{$an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','Object','$at','string']});

    defineAttr($$openValue,'qualifiedName',function(){
      return String$($qname(this.tipo));
    },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},$cont:OpenValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Declaration','$at','qualifiedName']};});
        })(OpenValue.$$.prototype);
    }
    return OpenValue;
}
exports.$init$OpenValue=$init$OpenValue;
$init$OpenValue();

//ClassDefinition OpenVariable at opentypes.ceylon (60:0-62:0)
function OpenVariable(pkg, meta, $$openVariable){
    $init$OpenVariable();
    if ($$openVariable===undefined)$$openVariable=new OpenVariable.$$;
    VariableDeclaration$meta$declaration($$openVariable);
    OpenValue(pkg, meta,$$openVariable);
    return $$openVariable;
}
OpenVariable.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:OpenValue},satisfies:[{t:VariableDeclaration$meta$declaration}],d:['ceylon.language.meta.declaration','VariableDeclaration']};};
function $init$OpenVariable(){
  if (OpenVariable.$$===undefined){
    initTypeProto(OpenVariable,'ceylon.language.meta.declaration::OpenVariable',VariableDeclaration$meta$declaration,OpenValue);
    (function($$openVariable){
      $$openVariable.memberSet=function(c,v) {
        if (!isOfType(c,{t:this.tipo.$$metamodel$$.$cont}))throw IncompatibleTypeException$meta$model("Incompatible container type");
        if (!isOfType(v,this.tipo.$$metamodel$$.$t))throw IncompatibleTypeException$meta$model("Incompatible value type");
        c[this.name]=v;
      };
            defineAttr($$openVariable,'setter',function(){
              var $$openVariable=this;
              return OpenSetter(this);
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:SetterDeclaration$meta$declaration},$cont:OpenVariable,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','VariableDeclaration','$at','setter']};});

  $$openVariable.equals=function(other) {
    if (isOfType(other, {t:OpenVariable}) && other.name.equals(this.name) && other.toplevel===this.toplevel && other.containingPackage.equals(this.containingPackage)) {
      return other.meta==this.meta;
    }
    return false;
  }
  defineAttr($$openVariable,'string',function(){return String$("OpenVariable[" + this.containingPackage.name + "::" + this.name_+"]");},undefined,{$an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','Object','$at','string']});

        })(OpenVariable.$$.prototype);
    }
    return OpenVariable;
}
exports.$init$OpenVariable=$init$OpenVariable;
$init$OpenVariable();

//ClassDefinition OpenSetter at opentypes.ceylon (63:0-63:90)
function OpenSetter(v, $$openSetter){
    $init$OpenSetter();
    if ($$openSetter===undefined)$$openSetter=new OpenSetter.$$;
    $$openSetter.variable_=v;
    SetterDeclaration$meta$declaration($$openSetter);
    $$openSetter.tipo=v.tipo.set;
    return $$openSetter;
}
OpenSetter.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Basic},satisfies:[{t:SetterDeclaration$meta$declaration}],d:['ceylon.language.meta.declaration','SetterDeclaration']};};
function $init$OpenSetter(){
    if (OpenSetter.$$===undefined){
        initTypeProto(OpenSetter,'ceylon.language.meta.declaration::OpenSetter',Basic,SetterDeclaration$meta$declaration);
        (function($$openSetter){
            defineAttr($$openSetter,'variable',function(){return this.variable_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:VariableDeclaration$meta$declaration},$cont:OpenSetter,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','SetterDeclaration','$at','variable']};});
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
    var _mm=meta.$$metamodel$$;
    if (_mm === undefined) {
      //it's a metamodel
      that.meta=meta;
      that.tipo=_findTypeFromModel(pkg,meta);
      _mm = that.tipo.$$metamodel$$;
      if (typeof(_mm)==='function') {
        _mm=_mm();
        that.tipo.$$metamodel$$=_mm;
      }
    } else {
      //it's a type
      that.tipo = meta;
      if (typeof(_mm)==='function') {
        _mm=_mm();
        meta.$$metamodel$$=_mm;
      }
      that.meta = get_model(_mm);
    }
    that.name_=_mm.d[_mm.d.length-1];
    that.toplevel_=_mm.$cont===undefined;
    ClassDeclaration$meta$declaration(that);
    return that;
}
OpenClass.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Basic},satisfies:[{t:ClassDeclaration$meta$declaration}],d:['ceylon.language.meta.declaration','ClassDeclaration']};};
function $init$OpenClass(){
  if (OpenClass.$$===undefined){
    initTypeProto(OpenClass,'ceylon.language.meta.declaration::OpenClass',Basic,ClassDeclaration$meta$declaration);
    (function($$openClass){

$$openClass.classApply=function(targs,$mptypes) {
  var mm=this.tipo.$$metamodel$$;
  if (mm.$tp) {
    if (!targs)throw TypeApplicationException$meta$model("This class requires type arguments");
    //TODO generate targs
  }
  validate$params(mm.$ps,$mptypes.Arguments,"Wrong number of Arguments for classApply");
  //TODO this is wrong
  return this.$apply(targs,$mptypes);//TODO tupleize
}
$$openClass.memberClassApply=function(cont,targs,$mptypes){
  var mm=this.tipo.$$metamodel$$;
  if (cont!==getNothingType$meta$model() && !extendsType({t:cont.tipo},{t:mm.$cont}))
    throw IncompatibleTypeException$meta$model("Incompatible Container specified");
  if (!extendsType({t:this.tipo},$mptypes.Type))
    throw IncompatibleTypeException$meta$model("Incompatible Type specified");
  var _t={t:this.tipo};
  validate$typeparams(_t,mm.$tp,targs);
  validate$params(mm.$ps,$mptypes.Arguments,"Wrong number of Arguments for classApply");
  var rv=AppliedMemberClass(this.tipo,{Container:{t:mm.$cont},Type:_t,Arguments:tupleize$params(mm.$ps)});
  if (_t.a)rv.$targs=_t.a;
  return rv;
}

      defineAttr($$openClass,'string',function(){
        return String$("class " + this.qualifiedName);
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language','Object']};}); 
      defineAttr($$openClass,'anonymous',function(){
        return this.meta.$mt==='obj';
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassDeclaration','$at','anonymous']};});
      defineAttr($$openClass,'abstract',function(){
        return $findAnnotation(this.tipo,AbstractAnnotation)!==null;
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassDeclaration','$at','abstract']};});
      defineAttr($$openClass,'$final',function(){
        return $findAnnotation(this.tipo,FinalAnnotation)!==null;
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassDeclaration','$at','final']};});
      defineAttr($$openClass,'annotation',function(){
        return $findAnnotation(this.tipo,AnnotationAnnotation)!==null;
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionalDeclaration','$at','annotation']};});
            //MethodDeclaration annotatedMemberDeclarations at X (55:2-56:66)
            $$openClass.annotatedMemberDeclarations=function ($$$mptypes){
                var $$openClass=this;
console.log("IMPL OpenClass.annotatedMemberDeclarations()");
                return getEmpty();
            };
            $$openClass.annotatedMemberDeclarations.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:'Kind'}},$ps:[],$cont:OpenClass,$tp:{Kind:{'satisfies':[{t:NestableDeclaration$meta$declaration}]},Annotation:{}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassDeclaration','$m','annotatedMemberDeclarations']};};
            
            //AttributeGetterDefinition openType at X (61:2-61:43)
            defineAttr($$openClass,'openType',function(){
              return FreeClass(this);
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:OpenType$meta$declaration},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassDeclaration','$at','openType']};});
            
            $$openClass.getParameterDeclaration=function (name$15){
      var pd=this.parameterDeclarations;
      for (var i=0; i < pd.length; i++) {
        if (name$6.equals(pd[i].name))return pd[i];
      }
      return null;
            };
            $$openClass.getParameterDeclaration.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:FunctionOrValueDeclaration$meta$declaration}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}}],$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassDeclaration','$m','getParameterDeclaration']};};
            
            //AttributeDeclaration superclassDeclaration at X (68:2-68:86)
            defineAttr($$openClass,'extendedType',function(){
              var sc = this.tipo.$$metamodel$$['super'];
              if (sc === undefined)return null;
              var mm = sc.t.$$metamodel$$;
              if (typeof(mm)==='function') {
                mm = mm();
                sc.t.$$metamodel$$=mm;
              }
              var fc=FreeClass(OpenClass(getModules$meta().find(mm.mod['$mod-name'],mm.mod['$mod-version']).findPackage(mm.d[0]), sc.t));
              if (sc.a)fc.declaration._targs=sc.a;
              return fc;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:OpenClassType$meta$declaration}]},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassDeclaration','$at','extendedType']};});
            
            //AttributeDeclaration interfaceDeclarations at X (69:2-69:89)
            defineAttr($$openClass,'satisfiedTypes',function(){
              var ints = this.tipo.$$metamodel$$['satisfies'];
              if (ints && ints.length) {
                var rv = [];
                for (var i=0; i < ints.length; i++) {
                  var ifc = ints[i];
                  var mm = ifc.t.$$metamodel$$;
                  if (typeof(mm)==='function') {
                    mm = mm();
                    ifc.t.$$metamodel$$=mm;
                  }
                  var fi=FreeInterface(OpenInterface(getModules$meta().find(mm.mod['$mod-name'],mm.mod['$mod-version']).findPackage(mm.d[0]), ifc.t));
                  if (ifc.a)fi.declaration._targs=ifc.a;
                  rv.push(fi);
                }
                return rv.reifyCeylonType({Absent:{t:Null},Element:{t:OpenInterfaceType$meta$declaration}});
              }
              return getEmpty();
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:{t:OpenInterfaceType$meta$declaration}}},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassDeclaration','$at','satisfiedTypes']};});
            defineAttr($$openClass,'name',function(){return this.name_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassDeclaration','$at','name']};});
            defineAttr($$openClass,'containingPackage',function(){return this._pkg;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Package$meta$declaration},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassDeclaration','$at','containingPackage']};});
            defineAttr($$openClass,'containingModule',function(){return this._pkg.container;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Module$meta$declaration},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassDeclaration','$at','containingModule']};});
            defineAttr($$openClass,'toplevel',function(){return this.toplevel_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassDeclaration','$at','toplevel']};});

    defineAttr($$openClass,'qualifiedName',function(){
      return String$($qname(this.tipo));
    },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Declaration','$at','qualifiedName']};});

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
    var _mm=meta.$$metamodel$$;
    if (_mm === undefined) {
      //it's a metamodel
      that.meta=meta;
      that.tipo=_findTypeFromModel(pkg,meta);
      _mm = that.tipo.$$metamodel$$;
      if (typeof(_mm)==='function') {
        _mm=_mm();
        that.tipo.$$metamodel$$=_mm;
      }
    } else {
      //it's a type
      that.tipo = meta;
      if (typeof(_mm)==='function') {
        _mm=_mm();
        meta.$$metamodel$$=_mm;
      }
      that.meta = get_model(_mm);
    }
    that.name_=_mm.d[_mm.d.length-1];
    that.toplevel_=_mm.$cont === undefined;
    InterfaceDeclaration$meta$declaration(that);
    return that;
}
OpenInterface.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Basic},satisfies:[{t:InterfaceDeclaration$meta$declaration}],d:['ceylon.language.meta.declaration','InterfaceDeclaration']};};
function $init$OpenInterface(){
  if (OpenInterface.$$===undefined){
    initTypeProto(OpenInterface,'ceylon.language.meta.declaration::OpenInterface',Basic,InterfaceDeclaration$meta$declaration);
    (function($$openInterface){
      $$openInterface.equals=function(other) {
        return isOfType(other, {t:OpenInterface}) && other.tipo==this.tipo;
      }
$$openInterface.memberInterfaceApply=function(cont,targs,$mptypes){
  var mm=this.tipo.$$metamodel$$;
  if (cont!==getNothingType$meta$model() && !extendsType({t:cont.tipo},{t:mm.$cont}))
    throw IncompatibleTypeException$meta$model("Incompatible Container specified");
  var _t={t:this.tipo};
  validate$typeparams(_t,mm.$tp,targs);
  var rv=AppliedMemberInterface(this.tipo,{Container:{t:mm.$cont},Type:_t});
  if (_t.a)rv.$targs=_t.a;
  return rv;
}
$$openInterface.interfaceApply=function(targs,$mptypes) {
  return this.$apply(targs,$mptypes);
}
            
defineAttr($$openInterface,'string',function(){
  return String$("interface " + this.qualifiedName);
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},$cont:OpenInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language','Object']};}); 
           
            //MethodDeclaration annotatedMemberDeclarations at X (80:2-81:66)
            $$openInterface.annotatedMemberDeclarations=function ($$$mptypes){
                var $$openInterface=this;
                return getEmpty();
            };
            $$openInterface.annotatedMemberDeclarations.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:'Kind'}},$ps:[],$cont:OpenInterface,$tp:{Kind:{'satisfies':[{t:NestableDeclaration$meta$declaration}]},Annotation:{}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','InterfaceDeclaration','$m','annotatedMemberDeclarations']};};
            
           
            //AttributeGetterDefinition openType at X (86:2-86:43)
            defineAttr($$openInterface,'openType',function(){
              return FreeInterface(this);
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:OpenType$meta$declaration},$cont:OpenInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','InterfaceDeclaration','$at','openType']};});
           
            //MethodDeclaration getTypeParameterDeclaration at X (88:2-88:79)
            $$openInterface.getTypeParameterDeclaration=function (name$21){
              throw Error("IMPL OpenInterface.getTypeParameterDeclaration");
            };
            $$openInterface.getTypeParameterDeclaration.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:TypeParameter$meta$declaration}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}}],$cont:OpenInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','InterfaceDeclaration','$m','getTypeParameterDeclaration']};};
            
            //AttributeDeclaration superclassDeclaration at X (90:2-90:86)
            defineAttr($$openInterface,'extendedType',function(){
              var sc = this.tipo.$$metamodel$$['super'];
              if (sc === undefined)return null;
              var mm = sc.t.$$metamodel$$;
              if (typeof(mm)==='function') {
                mm = mm();
                sc.t.$$metamodel$$=mm;
              }
              return FreeClass(OpenClass(getModules$meta().find(mm.mod['$mod-name'],mm.mod['$mod-version']).findPackage(mm.d[0]), sc.t));
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:OpenClasType$meta$declaration}]},$cont:OpenInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','InterfaceDeclaration','$at','extendedType']};});
            
            //AttributeDeclaration interfaceDeclarations at X (91:2-91:89)
            defineAttr($$openInterface,'satisfiedTypes',function(){
              var ints = this.tipo.$$metamodel$$['satisfies'];
              if (ints && ints.length) {
                var rv = [];
                for (var i=0; i < ints.length; i++) {
                  var ifc = ints[i].t;
                  var mm = ifc.$$metamodel$$;
                  if (typeof(mm)==='function') {
                    mm = mm();
                    ifc.$$metamodel$$=mm;
                  }
                  rv.push(FreeInterface(OpenInterface(getModules$meta().find(mm.mod['$mod-name'],mm.mod['$mod-version']).findPackage(mm.d[0]), ifc)));
                }
                return rv.reifyCeylonType({Absent:{t:Null},Element:{t:OpenInterfaceType$meta$declaration}});
              }
              return getEmpty();
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:{t:OpenInterfaceType$meta$declaration}}},$cont:OpenInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','InterfaceDeclaration','$at','satisfiedTypes']};});
            defineAttr($$openInterface,'name',function(){return this.name_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},$cont:OpenInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','InterfaceDeclaration','$at','name']};});
            defineAttr($$openInterface,'containingPackage',function(){return this._pkg;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Package$meta$declaration},$cont:OpenInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','InterfaceDeclaration','$at','containingPackage']};});
            defineAttr($$openInterface,'containingModule',function(){return this._pkg.container;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Module$meta$declaration},$cont:OpenInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','InterfaceDeclaration','$at','containingModule']};});
            defineAttr($$openInterface,'toplevel',function(){return this.toplevel_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:OpenInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','InterfaceDeclaration','$at','toplevel']};});

    defineAttr($$openInterface,'qualifiedName',function(){
      return String$($qname(this.tipo));
    },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},$cont:OpenInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Declaration','$at','qualifiedName']};});
        })(OpenInterface.$$.prototype);
    }
    return OpenInterface;
}
exports.$init$OpenInterface=$init$OpenInterface;
$init$OpenInterface();

function OpenAlias(alias, $$openAlias){
  $init$OpenAlias();
  if ($$openAlias===undefined)$$openAlias=new OpenAlias.$$;
  $$openAlias._alias = alias;
  //Get model from path
  var mm=alias.$$metamodel$$;
  if (typeof(mm)==='function'){
    mm=mm(); alias.$$metamodel$$=mm;
  }
  $$openAlias.meta=get_model(mm);
  AliasDeclaration$meta$declaration($$openAlias);
  return $$openAlias;
}
OpenAlias.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Basic},satisfies:[{t:AliasDeclaration$meta$declaration}],d:['ceylon.language.meta.declaration','OpenAlias']};};
function $init$OpenAlias(){
  if (OpenAlias.$$===undefined){
    initTypeProto(OpenAlias,'OpenAlias',Basic,AliasDeclaration$meta$declaration);
    (function($$openAlias){

      defineAttr($$openAlias,'extendedType',function(){
        return _openTypeFromTarg(this._alias);
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:OpenType$meta$declaration},$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$at','extendedType']};});
      defineAttr($$openAlias,'qualifiedName',function(){
        return String$($qname(this._alias.$$metamodel$$));
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$at','qualifiedName']};});
      defineAttr($$openAlias,'toplevel',function(){
        return this._alias.$cont===undefined;
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean},$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$at','toplevel']};});
      defineAttr($$openAlias,'containingPackage',function(){
        throw Error("IMPL OpenAlias.containingPackage");
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Package$meta$declaration},$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$at','containingPackage']};});
      defineAttr($$openAlias,'containingModule',function(){
        throw wrapexc(Exception(String$("OpenAlias.containingModule",26)),'12:42-12:87','caca.ceylon');
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Module$meta$declaration},$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$at','containingModule']};});
      defineAttr($$openAlias,'container',function(){
        var cont=this.$$targs$$.Container;
        if (cont===undefined) {
          cont=this._alias.$$metamodel$$.$cont;
          if (cont)cont={t:cont};
        }
        if (cont) {
          return typeLiteral$meta({Type:cont});
        }
        return null;
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:NestableDeclaration$meta$declaration},{t:Package$meta$declaration}]},$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$at','container']};});
      defineAttr($$openAlias,'openType',function(){
        return this.extendedType;
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:OpenType$meta$declaration},$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$at','openType']};});
      defineAttr($$openAlias,'typeParameterDeclarations',function(){
        var tps=this._alias.$$metamodel$$.$tp;
        if (tps) {
          var rv=[];
          for (var tp in tps) {
            rv.push(OpenTypeParam(this._alias, tp));
          }
          return rv.reifyCeylonType({Element:{t:TypeParameter$meta$declaration},Absent:{t:Null}});
        }
        return getEmpty();
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:{t:TypeParameter$meta$declaration}}},$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$at','typeParameterDeclarations']};});
      $$openAlias.getTypeParameterDeclaration=function getTypeParameterDeclaration(name$2){
        var tp=this._alias.$$metamodel$$;
        tp=tp.$tp ? tp.$tp[name$2] : null;
        return tp ? OpenTypeParam(this._alias, name$2) : null;
      };$$openAlias.getTypeParameterDeclaration.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:TypeParameter$meta$declaration}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$},$an:function(){return[];}}],$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$m','getTypeParameterDeclaration']};};
      defineAttr($$openAlias,'name',function(){
        return String$(this._alias.$$metamodel$$.d[this._alias.$$metamodel$$.d.length-1]);
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$at','name']};});
      defineAttr($$openAlias,'containingPackage',function(){
        throw Error("IMPL OpenAlias.containingPackage");
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Package$meta$declaration},$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$at','containingPackage']};});
      defineAttr($$openAlias,'string',function(){return StringBuilder().append(String$("alias ")).append(this.qualifiedName).string;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$at','string']};});
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
  that._name=nom;
  TypeParameter$meta$declaration(that);
  return that;
}
OpenTypeParam.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Basic},satisfies:[{t:TypeParameter$meta$declaration}],$an:function(){return[shared()];},d:['ceylon.language.meta.declaration','TypeParameter']};};
exports.OpenTypeParam=OpenTypeParam;
function $init$OpenTypeParam(){
  if (OpenTypeParam.$$===undefined){
    initTypeProto(OpenTypeParam,'ceylon.language.meta.declaration::OpenTypeParam',Basic,TypeParameter$meta$declaration);
    (function($$openTypeParam){
$$openTypeParam.equals=function(o) {
  return isOfType(o,{t:OpenTypeParam}) && this._name==o._name && this._cont==o._cont;
}
      //AttributeGetterDefinition container at caca.ceylon (7:4-7:94)
      defineAttr($$openTypeParam,'container',function(){return this._cont;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:NestableDeclaration$meta$declaration},$cont:OpenTypeParam,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','TypeParameter','$at','container']};});
      //AttributeGetterDefinition defaulted at caca.ceylon (8:4-8:82)
      defineAttr($$openTypeParam,'defaulted',function(){
        var tp=this._cont.$$metamodel$$.$tp[this._name];
        if (tp) {
          return tp['def'] !== undefined;
        }
        throw wrapexc(Exception(String$("TypeParameter.defaulted",23)),'8:38-8:80','caca.ceylon');
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean},$cont:OpenTypeParam,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','TypeParameter','$at','defaulted']};});
      //AttributeGetterDefinition defaultTypeArgument at caca.ceylon (9:4-9:104)
      defineAttr($$openTypeParam,'defaultTypeArgument',function(){
        var tp=this._cont.$$metamodel$$.$tp[this._name];
        if (typeof(tp.def)==='string') {
          return OpenTvar(OpenTypeParam(this._cont, tp.def));
        }
        return tp.def?_openTypeFromTarg(tp.def):null;
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:OpenType$meta$declaration}]},$cont:OpenTypeParam,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','TypeParameter','$at','defaultTypeArgument']};});
      //AttributeGetterDefinition variance at caca.ceylon (10:4-10:81)
      defineAttr($$openTypeParam,'variance',function(){
        var tp=this._cont.$$metamodel$$.$tp[this._name];
        if (tp) {
          if (tp['var']==='out')return getCovariant$meta$declaration();
          if (tp['var']=== 'in')return getContravariant$meta$declaration();
          return getInvariant$meta$declaration();
        }
        throw wrapexc(Exception(String$("TypeParameter.variance",22)),'10:38-10:79','caca.ceylon');
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Variance$meta$declaration},$cont:OpenTypeParam,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','TypeParameter','$at','variance']};});
      //AttributeGetterDefinition satisfiedTypes at caca.ceylon (11:4-11:95)
      defineAttr($$openTypeParam,'satisfiedTypes',function(){
        var tp=this._cont.$$metamodel$$.$tp[this._name];
        if (tp.satisfies) {
          var a=[];
          for (var i=0;i<tp.satisfies.length;i++) {
            a.push(_openTypeFromTarg(tp.satisfies[i]));
          }
          return a.reifyCeylonType({Absent:{t:Null},Element:{t:OpenType$meta$declaration}});
        }
        return getEmpty();
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:{t:OpenType$meta$declaration}}},$cont:OpenTypeParam,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','TypeParameter','$at','satisfiedTypes']};});
      //AttributeGetterDefinition caseTypes at caca.ceylon (12:4-12:85)
      defineAttr($$openTypeParam,'caseTypes',function(){
        var tp=this._cont.$$metamodel$$.$tp[this._name];
        if (tp.of) {
          var a=[];
          for (var i=0;i<tp.of.length;i++) {
            a.push(_openTypeFromTarg(tp.of[i]));
          }
          return a.reifyCeylonType({Absent:{t:Null},Element:{t:OpenType$meta$declaration}});
        }
        return getEmpty();
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:{t:OpenType$meta$declaration}}},$cont:OpenTypeParam,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','TypeParameter','$at','caseTypes']};});
      //AttributeGetterDefinition name at caca.ceylon (14:4-14:71)
      defineAttr($$openTypeParam,'name',function(){return String$(this._name);},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},$cont:OpenTypeParam,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Declaration','$at','name']};});
      //AttributeGetterDefinition qualifiedName at caca.ceylon (15:4-15:81)
      defineAttr($$openTypeParam,'qualifiedName',function(){
        return String$($qname(this._cont)+"."+this._name);
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},$cont:OpenTypeParam,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Declaration','$at','qualifiedName']};});
defineAttr($$openTypeParam,'hash',function(){return this.string.hash;},undefined,function(){return {mod:$$METAMODEL$$,$t:{t:Integer},d:['ceylon.language','Object','$at','hash']};});
      defineAttr($$openTypeParam,'string',function(){return String$("given " + this.qualifiedName);},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},$cont:OpenTypeParam,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Declaration','$at','string']};});
      $$openTypeParam.equals=function(o) {
        return isOfType(o,{t:OpenTypeParam}) && o._cont==this._cont && o._name==this._name;
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
    $$openTvar.$prop$getDeclaration={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:TypeParameter$meta$declaration},$cont:OpenTvar,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','OpenTvar','$at','declaration']};}};
    $$openTvar.$prop$getDeclaration.get=function(){return declaration};
    return $$openTvar;
}
OpenTvar.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Basic},satisfies:[{t:OpenTypeVariable$meta$declaration}],$an:function(){return[shared()];},d:['ceylon.language.meta.declaration','OpenTvar']};};
exports.OpenTvar=OpenTvar;
function $init$OpenTvar(){
  if (OpenTvar.$$===undefined){
    initTypeProto(OpenTvar,'ceylon.language.meta.declaration::OpenTvar',Basic,$init$OpenTypeVariable$meta$declaration());
    (function($$openTvar){
      defineAttr($$openTvar,'string',function(){return this.p$2.qualifiedName;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},$cont:OpenTvar,$an:function(){return[shared(),actual()];},d:['ceylon.language','Object','$at','string']};});
            defineAttr($$openTvar,'declaration',function(){
                return this.p$2;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:TypeParameter$meta$declaration},$cont:OpenTvar,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','OpenTypeVariable','$at','declaration']};});
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
    $$freeUnion.$prop$getCaseTypes={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:List,a:{Element:{t:OpenType$meta$declaration}}},$cont:FreeUnion,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','OpenUnion','$at','caseTypes']};}};
    $$freeUnion.$prop$getCaseTypes.get=function(){return caseTypes};
    return $$freeUnion;
}
FreeUnion.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Basic},satisfies:[{t:OpenUnion$meta$declaration}],$an:function(){return[shared()];},d:['ceylon.language.meta.declaration','OpenUnion']};};
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
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},d:['ceylon.language','Object','$at','string']};});
defineAttr($$freeUnion,'hash',function(){
  var s=this.ts$2.size;
  for (var i=0;i<this.ts$2.size;i++) {
    s+=this.ts$2.$get(i).string.hash;
  }
  return s;
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Integer},d:['ceylon.language','Object','$at','hash']};});
            defineAttr($$freeUnion,'caseTypes',function(){
                var $$freeUnion=this;
                return $$freeUnion.ts$2;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:List,a:{Element:{t:OpenType$meta$declaration}}},$cont:FreeUnion,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','OpenUnion','$at','caseTypes']};});
            defineAttr($$freeUnion,'ts$2',function(){return this.ts$2_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:List,a:{Element:{t:OpenType$meta$declaration}}},$cont:FreeUnion,d:['ceylon.language.meta.declaration','OpenUnion','$at','ts']};});
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
    $$freeIntersection.$prop$getSatisfiedTypes={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:List,a:{Element:{t:OpenType$meta$declaration}}},$cont:FreeIntersection,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','OpenIntersection','$at','satisfiedTypes']};}};
    $$freeIntersection.$prop$getSatisfiedTypes.get=function(){return satisfiedTypes};
    return $$freeIntersection;
}
FreeIntersection.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Basic},satisfies:[{t:OpenIntersection$meta$declaration}],$an:function(){return[shared()];},d:['ceylon.language.meta.declaration','OpenIntersection']};};
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
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},d:['ceylon.language','Object','$at','string']};});
defineAttr($$freeIntersection,'hash',function(){
  var s=this.ts$3.size;
  for (var i=0;i<this.ts$3.size;i++) {
    s+=this.ts$3.$get(i).string.hash;
  }
  return s;
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Integer},d:['ceylon.language','Object','$at','hash']};});

            defineAttr($$freeIntersection,'satisfiedTypes',function(){
                var $$freeIntersection=this;
                return $$freeIntersection.ts$3;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:List,a:{Element:{t:OpenType$meta$declaration}}},$cont:FreeIntersection,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','OpenIntersection','$at','satisfiedTypes']};});
            defineAttr($$freeIntersection,'ts$3',function(){return this.ts$3_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:List,a:{Element:{t:OpenType$meta$declaration}}},$cont:FreeIntersection,d:['ceylon.language.meta.declaration','OpenIntersection','$at','ts']};});
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
  $$funParamDecl.tipo={$$metamodel$$:{$cont:cont.tipo,$t:param.$t,$ps:param.$ps,$mt:'prm',d:cont.tipo.$$metamodel$$.d}};

  $$funParamDecl.$prop$getParameter={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','parameter']};}};
  $$funParamDecl.$prop$getParameter.get=function(){return true;};
  $$funParamDecl.$prop$getAnnotation={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','annotation']};}};
  $$funParamDecl.$prop$getAnnotation.get=function(){return false;};
  $$funParamDecl.$prop$getShared={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','shared']};}};
  $$funParamDecl.$prop$getShared.get=function(){return false;};
  $$funParamDecl.$prop$getToplevel={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','toplevel']};}};
  $$funParamDecl.$prop$getToplevel.get=function(){return false;};
  $$funParamDecl.$prop$getFormal={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','formal']};}};
  $$funParamDecl.$prop$getFormal.get=function(){return false;};
  $$funParamDecl.$prop$getDefault={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','default']};}};
  $$funParamDecl.$prop$getDefault.get=function(){return false};
  $$funParamDecl.$prop$getActual={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','actual']};}};
  $$funParamDecl.$prop$getActual.get=function(){return false;};
  return $$funParamDecl;
}
FunParamDecl.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Basic},$ps:[],satisfies:[{t:FunctionDeclaration$meta$declaration}],d:['ceylon.language.meta.declaration','FunParamDecl']};};
function $init$FunParamDecl(){
  if (FunParamDecl.$$===undefined){
    initTypeProto(FunParamDecl,'ceylon.language.meta.declaration::FunParamDecl',Basic,FunctionDeclaration$meta$declaration);
    (function($$funParamDecl){

      defineAttr($$funParamDecl,'parameter',function(){return true;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','parameter']};});
      
      //AttributeGetterDef defaulted at caca.ceylon (8:2-8:71)
      defineAttr($$funParamDecl,'defaulted',function(){
        return this.param.$def!==undefined;
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','defaulted']};});
      //AttributeGetterDef variadic at caca.ceylon (9:2-9:69)
      defineAttr($$funParamDecl,'variadic',function(){
        return this.param.seq!==undefined;
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','variadic']};});
      //AttributeGetterDef container at caca.ceylon (11:2-11:91)
      defineAttr($$funParamDecl,'container',function(){
        return this.cont;
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:'u', l:[{t:Package$meta$declaration},{t:NestableDeclaration$meta$declaration}]},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','container']};});
      //AttributeGetterDef containingPackage at caca.ceylon (12:2-12:87)
      defineAttr($$funParamDecl,'containingPackage',function(){
        return this.cont.containingPackage;
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Package$meta$declaration},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','containingPackage']};});
      //AttributeGetterDef containingModule at caca.ceylon (13:2-13:84)
      defineAttr($$funParamDecl,'containingModule',function(){
        return this.cont.containingModule;
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Module$meta$declaration},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','containingModule']};});
      //AttributeGetterDef openType at caca.ceylon (14:2-14:70)
      defineAttr($$funParamDecl,'openType',function(){
        var t = this.param.$t;
        if (typeof(t)==='string')return OpenTvar(OpenTypeParam(this.cont,t));
        return _openTypeFromTarg(t);
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:OpenType$meta$declaration},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','openType']};});
      //AttributeDecl annotation at caca.ceylon (15:2-15:40)
      defineAttr($$funParamDecl,'annotation',function(){return false;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','annotation']};});
      defineAttr($$funParamDecl,'shared',function(){return false;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','shared']};});
      defineAttr($$funParamDecl,'toplevel',function(){return false;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','toplevel']};});
      defineAttr($$funParamDecl,'formal',function(){return false;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','formal']};});
      defineAttr($$funParamDecl,'$default',function(){return false;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','default']};});
      defineAttr($$funParamDecl,'actual',function(){return false;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','actual']};});
      defineAttr($$funParamDecl,'qualifiedName',function(){
        return String$($qname(cont.tipo.$$metamodel$$)+"."+this.param.$nm);
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','qualifiedName']};});
      defineAttr($$funParamDecl,'name',function(){
        return String$(this.param.$nm);
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','name']};});
      $$funParamDecl.getParameterDeclaration=function getParameterDeclaration(name$10){
      var pd=this.parameterDeclarations;
      for (var i=0; i < pd.length; i++) {
        if (name$6.equals(pd[i].name))return pd[i];
      }
      return null;
      };$$funParamDecl.getParameterDeclaration.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:'u', l:[{t:Null},{t:FunctionOrValueDeclaration$meta$declaration}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$},$an:function(){return[];}}],$cont:FunParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$m','getParameterDeclaration']};};
      
      //MethodDef apply at caca.ceylon (31:2-32:74)
      $$funParamDecl.$apply=function $apply(typeArguments$11,$$$mptypes){
          if(typeArguments$11===undefined){typeArguments$11=getEmpty();}
          throw wrapexc(Exception(String$("IMPL apply",10)),'32:43-32:72','');
      };$$funParamDecl.$apply.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Function$meta$model,a:{Arguments:'Arguments',Type:'Return'}},$ps:[{$nm:'typeArguments',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element:{t:Type$meta$model,a:{Type:{t:Anything}}}}},$an:function(){return[];}}],$cont:FunParamDecl,$tp:{Return:{'def':{t:Anything}},Arguments:{'satisfies':[{t:Sequential,a:{Element:{t:Anything}}}],'def':{t:Nothing}}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$m','apply']};};
      
      //MethodDef memberApply at caca.ceylon (33:2-34:80)
      $$funParamDecl.memberApply=function memberApply(containerType$12,typeArguments$13,$$$mptypes){
          if(typeArguments$13===undefined){typeArguments$13=getEmpty();}
          throw wrapexc(Exception(String$("IMPL memberApply",16)),'34:43-34:78','');
      };$$funParamDecl.memberApply.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Method$meta$model,a:{Arguments:'Arguments',Type:'Return',Container:'Container'}},$ps:[{$nm:'containerType',$mt:'prm',$t:{t:Type$meta$model,a:{Type:'Container'}},$an:function(){return[];}},{$nm:'typeArguments',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element:{t:Type$meta$model,a:{Type:{t:Anything}}}}},$an:function(){return[];}}],$cont:FunParamDecl,$tp:{Container:{'def':{t:Nothing}},Return:{'def':{t:Anything}},Arguments:{'satisfies':[{t:Sequential,a:{Element:{t:Anything}}}],'def':{t:Nothing}}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$m','memberApply']};};
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
  $$valParamDecl.tipo={$$metamodel$$:{$cont:cont.tipo,$t:param.$t,$mt:'prm',d:cont.tipo.$$metamodel$$.d}};

  $$valParamDecl.$prop$getParameter.get=function(){return true;};
  $$valParamDecl.$prop$getShared.get=function(){return false;};
  $$valParamDecl.$prop$getToplevel.get=function(){return false;};
  $$valParamDecl.$prop$getFormal.get=function(){return false;};
  $$valParamDecl.$prop$getDefault.get=function(){return false;};
  $$valParamDecl.$prop$getActual.get=function(){return false;};
  return $$valParamDecl;
}
ValParamDecl.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Basic},$ps:[],satisfies:[{t:ValueDeclaration$meta$declaration}],d:['ceylon.language.meta.declaration','ValParamDecl']};};
function $init$ValParamDecl(){
  if (ValParamDecl.$$===undefined){
    initTypeProto(ValParamDecl,'ceylon.language.meta.declaration::ValParamDecl',Basic,ValueDeclaration$meta$declaration);
    (function($$valParamDecl){

      defineAttr($$valParamDecl,'parameter',function(){return true;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:ValParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','parameter']};});
      defineAttr($$valParamDecl,'defaulted',function(){
        return this.param.$def!==undefined;
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:ValParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','defaulted']};});
      defineAttr($$valParamDecl,'variadic',function(){
        return this.param.seq!==undefined;
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:ValParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','variadic']};});
      defineAttr($$valParamDecl,'variable',function(){
        return this.param['var']!==undefined; //TODO revisar
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:ValParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','variable']};});
      defineAttr($$valParamDecl,'container',function(){
        return this.cont;
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:'u', l:[{t:Package$meta$declaration},{t:NestableDeclaration$meta$declaration}]},$cont:ValParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','container']};});
      defineAttr($$valParamDecl,'containingPackage',function(){
        return this.cont.containingPackage;
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Package$meta$declaration},$cont:ValParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','containingPackage']};});
      defineAttr($$valParamDecl,'containingModule',function(){
        return this.cont.containingModule;
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Module$meta$declaration},$cont:ValParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','containingModule']};});
      defineAttr($$valParamDecl,'openType',function(){
        var t = this.param.$t;
        if (typeof(t)==='string')return OpenTvar(OpenTypeParam(this.cont,t));
        return _openTypeFromTarg(t);
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:OpenType$meta$declaration},$cont:ValParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','openType']};});
      defineAttr($$valParamDecl,'shared',function(){return false;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:ValParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','shared']};});
      defineAttr($$valParamDecl,'toplevel',function(){return false;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:ValParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','toplevel']};});
      defineAttr($$valParamDecl,'formal',function(){return false;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:ValParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','formal']};});
      defineAttr($$valParamDecl,'$default',function(){return false;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:ValParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','default']};});
      defineAttr($$valParamDecl,'actual',function(){return false;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:ValParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','actual']};});
      defineAttr($$valParamDecl,'qualifiedName',function(){
        return String$($qname(this.cont.tipo.$$metamodel$$)+"."+this.param.$nm);
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},$cont:ValParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','qualifiedName']};});
      defineAttr($$valParamDecl,'name',function(){
        return String$(this.param.$nm);
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},$cont:ValParamDecl,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','name']};});
      //MethodDef apply at caca.ceylon (57:2-57:84)
      $$valParamDecl.$apply=function $apply($$$mptypes){
          var $$valParamDecl=this;
          throw wrapexc(Exception(String$("IMPL apply",10)),'57:53-57:82','');
      };$$valParamDecl.$apply.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Value$meta$model,a:{Type:'Type'}},$ps:[],$cont:ValParamDecl,$tp:{Type:{'def':{t:Anything}}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$m','apply']};};
      //MethodDef memberApply at caca.ceylon (58:2-58:166)
      $$valParamDecl.memberApply=function memberApply(containerType$20,$$$mptypes){
          var $$valParamDecl=this;
          throw wrapexc(Exception(String$("IMPL memberApply",16)),'58:129-58:164','');
      };$$valParamDecl.memberApply.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Attribute$meta$model,a:{Type:'Type',Container:'Container'}},$ps:[{$nm:'containerType',$mt:'prm',$t:{t:Type$meta$model,a:{Type:'Container'}},$an:function(){return[];}}],$cont:ValParamDecl,$tp:{Container:{'def':{t:Nothing}},Type:{'def':{t:Anything}}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$m','memberApply']};};
    })(ValParamDecl.$$.prototype);
  }
  return ValParamDecl;
}
exports.$init$ValParamDecl=$init$ValParamDecl;
$init$ValParamDecl();
