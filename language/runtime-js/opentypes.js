//Find the real declaration of something from its model definition
function _findTypeFromModel(pkg,mdl) {
  var mod = pkg.container;
  //TODO this is very primitive needs a lot of rules replicated from the JsIdentifierNames
  var nm = mdl.$nm;
  var mt = mdl['$mt'];
  if (mt === 'attr' || mt === 'gttr' || mt === 'obj') {
    nm = '$prop$get' + nm[0].toUpperCase() + nm.substring(1);
  }
  var nm=nm + pkg.suffix;
  var rv=mod.meta[nm];
  if (rv===undefined)rv=mod.meta['$init$'+nm];
  if (rv===undefined)rv=mod.meta['$'+nm];
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

      $$openFunction.$apply=function $apply(types,$mptypes){
        if (typeof(this.tipo.$$metamodel$$)==='function') {
          this.tipo.$$metamodel$$=this.tipo.$$metamodel$$();
        }
        var mm=this.tipo.$$metamodel$$;
        var tps=mm.$tp;
        var ta;
        if (tps) {
          if (types===undefined)
            throw TypeApplicationException$meta$model(String$("Missing type arguments in call to FunctionDeclaration.apply"));
          ta={}; var i=0;
          for (var tp in tps) {
            if (types[i]===undefined)
              throw TypeApplicationException$meta$model(String$("Missing type argument for " + tp));
            var _tp = tps[tp];
            var _t = types[i].tipo;
            ta[tp]={t:_t};
            if ((_tp.satisfies && _tp.satisfies.length>0) || (_tp.of && _tp.of.length > 0)) {
              var restraints=(_tp.satisfies && _tp.satisfies.length>0)?_tp.satisfies:_tp.of;
              for (var j=0; j<restraints.length;j++) {
                if (!extendsType(ta[tp],restraints[j]))
                  throw TypeApplicationException$meta$model(String$("Type argument for " + tp + " violates type parameter constraints"));
              }
            }
            i++;
          }
        }
        validate$params(mm.$ps,$mptypes.Arguments,"Wrong number of arguments when applying function");
        return ta?AppliedFunction(this.tipo,$mptypes,undefined,ta):AppliedFunction(this.tipo,$mptypes);
      };$$openFunction.$apply.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Function$meta$model,a:{Arguments:{t:Nothing},Type:{t:Anything}}},$ps:[{$nm:'types',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element:{t:Type$meta$model}}}}],$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$m','apply']};};

      $$openFunction.memberApply=function memberApply(cont,types,$mptypes){
        var mm=this.tipo.$$metamodel$$;
        if (!(cont.tipo && extendsType({t:cont.tipo},{t:mm.$cont}))&&cont!==getNothingType$meta$model())
          throw IncompatibleTypeException$meta$model("Incompatible Container type argument");
        if (!extendsType(mm.$t,$mptypes.Return))throw IncompatibleTypeException$meta$model("Incompatible Return type argument");
        validate$params(mm.$ps,$mptypes.Arguments,"Wrong number of Arguments for memberApply");
        var tps=mm.$tp;
        var ta;
        if (tps) {
          if (types===undefined)
            throw TypeApplicationException$meta$model(String$("Missing type arguments in call to "+this.string+".memberApply"));
          ta={}; var i=0;
          for (var tp in tps) {
            var _t=types.$get(i);
            if (_t===undefined)
              throw TypeApplicationException$meta$model(String$("Missing type argument for "+this.string+"<"+ tp+">"));
            var _tp = tps[tp];
            ta[tp]={t:_t.tipo};
            if ((_tp.satisfies && _tp.satisfies.length>0) || (_tp.of && _tp.of.length > 0)) {
              var restraints=(_tp.satisfies && _tp.satisfies.length>0)?_tp.satisfies:_tp.of;
              for (var j=0; j<restraints.length;j++) {
                if (!extendsType(ta[tp],restraints[j]))
                  throw TypeApplicationException$meta$model(String$("Type argument for " + tp + " violates type parameter constraints"));
              }
            }
            i++;
          }
        }
        return AppliedMethod(this.tipo,types,$mptypes);
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
            
            //MethodDeclaration getParameterDeclaration at X (29:4-29:90)
    $$openFunction.getParameterDeclaration=function (name$6){
      throw Error("IMPL OpenFunction.getParameterDeclaration");
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
       return String$($qname(this.tipo.$$metamodel$$));
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
      return String$($qname(this.tipo.$$metamodel$$));
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
  return this.$apply(targs,$mptypes);
}
$$openClass.memberClassApply=function(cont,targs,$mptypes){
  var mm=this.tipo.$$metamodel$$;
  if (cont!==getNothingType$meta$model() && !extendsType({t:cont.tipo},{t:mm.$cont}))
    throw IncompatibleTypeException$meta$model("Incompatible Container specified");
  if (!extendsType({t:this.tipo},$mptypes.Type))
    throw IncompatibleTypeException$meta$model("Incompatible Type specified");
  var _t={t:this.tipo};
  if (mm.$tp) {
    if (!targs)throw TypeApplicationException$meta$model("This class requires type arguments");
    //TODO generate targs
  }
  validate$params(mm.$ps,$mptypes.Arguments,"Wrong number of Arguments for classApply");
  return AppliedMemberClass(this.tipo,{Container:{t:mm.$cont},Type:_t,Arguments:$mptypes.Arguments});
}

      defineAttr($$openClass,'string',function(){
        return String$("class " + this.qualifiedName);
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language','Object']};}); 
      defineAttr($$openClass,'anonymous',function(){
        return this.meta.$mt==='obj';
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassDeclaration','$at','anonymous']};});
      defineAttr($$openClass,'abstract',function(){
        var _m=this.meta;
        return (_m && (_m['abstract'] || (_m.$an&&_m.$an['abstract'])))!==undefined;
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassDeclaration','$at','abstract']};});
      defineAttr($$openClass,'$final',function(){
        var _m=this.meta;
        return (_m && _m.$an && _m.$an['final'])!==undefined;
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassDeclaration','$at','final']};});
      defineAttr($$openClass,'annotation',function(){
        var _m=this.meta;
        return (_m && _m.$an && _m.$an['annotation'])!==undefined;
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','NestableDeclaration','$at','annotation']};});
      defineAttr($$openClass,'actual',function(){
        var _m=this.meta;
        return (_m && _m.$an && _m.$an.actual)!==undefined;
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','NestableDeclaration','$at','actual']};});
      defineAttr($$openClass,'formal',function(){
        var _m=this.meta;
        return (_m && _m.$an && _m.$an.formal)!==undefined;
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','NestableDeclaration','$at','formal']};});
      defineAttr($$openClass,'shared',function(){
        var _m=this.meta;
        return (_m && _m.$an && _m.$an.shared)!==undefined;
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','NestableDeclaration','$at','shared']};});
      defineAttr($$openClass,'$default',function(){
        var _m=this.meta;
        return (_m && _m.$an && _m.$an['default'])!==undefined;
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','NestableDeclaration','$at','default']};});

            //MethodDeclaration annotatedMemberDeclarations at X (55:2-56:66)
            $$openClass.annotatedMemberDeclarations=function ($$$mptypes){
                var $$openClass=this;
                return getEmpty();
            };
            $$openClass.annotatedMemberDeclarations.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:'Kind'}},$ps:[],$cont:OpenClass,$tp:{Kind:{'satisfies':[{t:NestableDeclaration$meta$declaration}]},Annotation:{}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassDeclaration','$m','annotatedMemberDeclarations']};};
            
            //AttributeGetterDefinition openType at X (61:2-61:43)
            defineAttr($$openClass,'openType',function(){
              return FreeClass(this);
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:OpenType$meta$declaration},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassDeclaration','$at','openType']};});
            
           
           //MethodDeclaration getParameterDeclaration at X (66:2-66:88)
            $$openClass.getParameterDeclaration=function (name$15){
                var $$openClass=this;
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
      return String$($qname(this.tipo.$$metamodel$$));
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
  if (mm.$tp) {
    if (!targs)throw TypeApplicationException$meta$model("This class requires type arguments");
    //TODO generate targs
  }
  return AppliedMemberInterface(this.tipo,{Container:{t:mm.$cont},Type:_t});
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
      return String$($qname(this.tipo.$$metamodel$$));
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
      defineAttr($$openAlias,'$default',function(){
        throw wrapexc(Exception(String$("OpenAlias.default",17)),'8:34-8:70','caca.ceylon');
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean},$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$at','default']};});
      defineAttr($$openAlias,'actual',function(){
        throw wrapexc(Exception(String$("OpenAlias.actual",16)),'9:33-9:68','caca.ceylon');
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean},$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$at','actual']};});
      defineAttr($$openAlias,'formal',function(){
        throw wrapexc(Exception(String$("OpenAlias.formal",16)),'10:33-10:68','caca.ceylon');
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean},$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$at','formal']};});
      defineAttr($$openAlias,'shared',function(){
        throw wrapexc(Exception(String$("OpenAlias.shared",16)),'11:33-11:68','caca.ceylon');
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean},$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$at','shared']};});
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
      $$openAlias.annotations=function annotations($$$mptypes){
        throw wrapexc(Exception(String$("OpenAlias.annotations()",23)),'16:60-16:102','caca.ceylon');
      };$$openAlias.annotations.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:'Annotation'}},$ps:[],$cont:OpenAlias,$tp:{Annotation:{'var':'out','satisfies':[{t:Annotation,a:{Value:'Annotation'}}]}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$m','annotations']};};
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
        return String$($qname(this._cont.$$metamodel$$)+"."+this._name);
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
