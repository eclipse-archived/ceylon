//Find the real declaration of something from its model definition
function _findTypeFromModel(pkg,mdl) {
  var mod = pkg.container;
  //TODO this is very primitive needs a lot of rules replicated from the JsIdentifierNames
  var nm = mdl.$nm;
  var mt = mdl['$mt'];
  if (mt === 'attr' || mt === 'gttr' || mt === 'obj') {
    nm = '$prop$get' + nm[0].toUpperCase() + nm.substring(1);
  }
  var nm = nm + pkg.suffix;
  var rv = mod.meta[nm];
  if (rv === undefined) {
    rv = mod.meta['$init$'+nm];
    if (typeof(rv)==='function')return rv();
  }
  return rv;
}

//ClassDefinition FreeClass at X (161:0-168:0)
function FreeClass(declaration, $$freeClass){
    $init$FreeClass();
    if ($$freeClass===undefined)$$freeClass=new FreeClass.$$;
    OpenClassType$meta$declaration($$freeClass);
    
    //AttributeDeclaration declaration at X (164:4-164:46)
    $$freeClass.declaration$26_=declaration;
    $$freeClass.$prop$getDeclaration={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:ClassDeclaration$meta$declaration},$cont:FreeClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','OpenClassType','$at','declaration']};}};
    $$freeClass.$prop$getDeclaration.get=function(){return declaration};
    return $$freeClass;
}
FreeClass.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Basic},satisfies:[{t:OpenClassType$meta$declaration}],d:['ceylon.language.meta.declaration','OpenClassType']};};
function $init$FreeClass(){
    if (FreeClass.$$===undefined){
        initTypeProto(FreeClass,'ceylon.language.meta.declaration::FreeClass',Basic,OpenClassType$meta$declaration);
        (function($$freeClass){
            
            //AttributeDeclaration declaration at X (164:4-164:46)
            defineAttr($$freeClass,'declaration',function(){return this.declaration$26_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:ClassDeclaration$meta$declaration},$cont:FreeClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','OpenClassType','$at','declaration']};});
            
            //AttributeGetterDefinition extendedType at X (165:4-165:82)
            defineAttr($$freeClass,'extendedType',function(){
              return this.declaration.extendedType;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:OpenClassType$meta$declaration}]},$cont:FreeClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','OpenClassType','$at','extendedType']};});
            //AttributeGetterDefinition satisfiedTypes at X (166:4-166:89)
            defineAttr($$freeClass,'satisfiedTypes',function(){
              return this.declaration.satisfiedTypes;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:{t:OpenInterfaceType$meta$declaration}}},$cont:FreeClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','OpenClassType','$at','satisfiedTypes']};});
            //AttributeGetterDefinition typeArguments at X (167:4-167:97)
            defineAttr($$freeClass,'typeArguments',function(){
                var $$freeClass=this;
                throw wrapexc(Exception(String$("IMPL FreeClass.typeArguments")),'167:63-167:95','X');
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Map,a:{Key:{t:TypeParameter$meta$declaration},Item:{t:OpenType$meta$declaration}}},$cont:FreeClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','OpenClassType','$at','typeArguments']};});

      $$freeClass.equals=function(other) {
        return isOfType(other,{t:FreeClass}) && other.declaration.equals(this.declaration);
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
    $$freeInterface.declaration$27_=declaration;
    $$freeInterface.$prop$getDeclaration.get=function(){return declaration};
    return $$freeInterface;
}
FreeInterface.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Basic},satisfies:[{t:OpenInterfaceType$meta$declaration}],d:['ceylon.language.meta.declaration','OpenInterfaceType']};};
function $init$FreeInterface(){
    if (FreeInterface.$$===undefined){
        initTypeProto(FreeInterface,'ceylon.language.meta.declaration::FreeInterface',Basic,OpenInterfaceType$meta$declaration);
        (function($$freeInterface){
            
            //AttributeDeclaration declaration at X (173:4-173:50)
            defineAttr($$freeInterface,'declaration',function(){return this.declaration$27_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:InterfaceDeclaration$meta$declaration},$cont:FreeInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','OpenInterfaceType','$at','declaration']};});
            
            //AttributeGetterDefinition extendedType at X (174:4-174:82)
            defineAttr($$freeInterface,'extendedType',function(){
              return this.declaration.extendedType;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:OpenClassType$meta$declaration}]},$cont:FreeInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','OpenInterfaceType','$at','extendedType']};});
            //AttributeGetterDefinition satisfiedTypes at X (175:4-175:89)
            defineAttr($$freeInterface,'satisfiedTypes',function(){
              return this.declaration.satisfiedTypes;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:{t:OpenInterfaceType$meta$declaration}}},$cont:FreeInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','OpenInterfaceType','$at','satisfiedTypes']};});
            //AttributeGetterDefinition typeArguments at X (176:4-176:97)
            defineAttr($$freeInterface,'typeArguments',function(){
                var $$freeInterface=this;
                throw wrapexc(Exception(String$("IMPL FreeInterface.typeArguments")),'176:63-176:95','X');
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Map,a:{Key:{t:TypeParameter$meta$declaration},Item:{t:OpenType$meta$declaration}}},$cont:FreeInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','OpenInterfaceType','$at','typeArguments']};});
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
            
            //MethodDefinition apply at X (20:4-20:81)
            $$openFunction.$apply=function $apply(types$2){
                var $$openFunction=this;
                if(types$2===undefined){types$2=getEmpty();}
                throw Exception("IMPL OpenFunction.apply");
            };$$openFunction.$apply.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Function$meta$model,a:{Arguments:{t:Nothing},Type:{t:Anything}}},$ps:[{$nm:'types',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element:{t:Type$meta$model}}}}],$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$m','apply']};};
            
            //MethodDefinition memberApply at X (21:4-21:105)
            $$openFunction.memberApply=function memberApply(instance$3,types$4,$$$mptypes){
              var $$openFunction=this;
              if(types$4===undefined){types$4=getEmpty();}
              //TODO check for naming rules
              //WTF is types argument for?
              return AppliedFunction(instance$3[this.name],undefined,instance$3);
            };$$openFunction.memberApply.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Function$meta$model,a:{Arguments:{t:Nothing},Type:{t:Anything}}},$ps:[{$nm:'instance',$mt:'prm',$t:{t:Object$}},{$nm:'types',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element:{t:Type$meta$model}}}}],$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$m','memberApply']};};
            
            //MethodDefinition memberApply at X (22:4-23:54)
            $$openFunction.memberApply=function memberApply(instance$3,types$5,$$$mptypes){
                var $$openFunction=this;
                if(types$5===undefined){types$5=getEmpty();}
                throw Exception("IMPL OpenFunction.memberApply");
            };$$openFunction.memberApply.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Method$meta$model,a:{Arguments:'Arguments',Type:'MethodType',Container:'Container'}},$ps:[{$nm:'types',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element:{t:Type$meta$model}}}}],$cont:OpenFunction,$tp:{Container:{},MethodType:{},Arguments:{'satisfies':[{t:Sequential,a:{Element:{t:Anything}}}]}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$m','memberApply']};};
            
            //AttributeDeclaration defaulted at X (25:4-25:44)
            defineAttr($$openFunction,'defaulted',function(){
                var $$openFunction=this;
                return false;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','defaulted']};});
            
            //AttributeDeclaration variadic at X (26:4-26:43)
            defineAttr($$openFunction,'variadic',function(){
                var $$openFunction=this;
                return false;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','variadic']};});
            
            //MethodDeclaration getParameterDeclaration at X (29:4-29:90)
            $$openFunction.getParameterDeclaration=function (name$6){
                var $$openFunction=this;
                return null;
            };
            $$openFunction.getParameterDeclaration.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:FunctionOrValueDeclaration$meta$declaration}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}}],$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$m','getParameterDeclaration']};};
            
            //AttributeGetterDefinition openType at X (33:2-33:43)
            defineAttr($$openFunction,'openType',function(){
                var $$openFunction=this;
                throw Exception("IMPL OpenFunction.openType");
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:OpenType$meta$declaration},$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','openType']};});
            //AttributeDeclaration typeParameterDeclarations at X (34:2-34:63)
            defineAttr($$openFunction,'typeParameterDeclarations',function(){
                var $$openFunction=this;
                return getEmpty();
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:{t:TypeParameter$meta$declaration}}},$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','typeParameterDeclarations']};});
            
            //MethodDeclaration getTypeParameterDeclaration at X (35:2-35:79)
            $$openFunction.getTypeParameterDeclaration=function (name$7){
                var $$openFunction=this;
                return null;
            };
            $$openFunction.getTypeParameterDeclaration.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:TypeParameter$meta$declaration}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}}],$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$m','getTypeParameterDeclaration']};};
            defineAttr($$openFunction,'name',function(){return this.name_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','name']};});
            defineAttr($$openFunction,'containingPackage',function(){return this._pkg;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Package$meta$declaration},$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','containingPackage']};});
            defineAttr($$openFunction,'containingModule',function(){return this._pkg.container;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Package$meta$declaration},$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','containingPackage']};});
            defineAttr($$openFunction,'toplevel',function(){return this.toplevel_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','toplevel']};});

    defineAttr($$openFunction,'qualifiedName',function(){
       if (this.toplevel) {
         return String$(this.containingPackage.name + "::" + this.name);
       } else {
         var qn = this.containingPackage.name + "::";
         for (var i=1; i<this.tipo.$$metamodel$$.d.length;i++) {
           var part = this.tipo.$$metamodel$$.d[i];
           qn += part[0]=='$'?'.':part;
         }
         return String$(qn);
       }
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
            
            //MethodDefinition apply at X (39:4-39:68)
            $$openValue.$apply=function $apply($$$mptypes){
              return (this.tipo.set?AppliedVariable:AppliedValue)(undefined,this.tipo,$$$mptypes);
            };$$openValue.$apply.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Value$meta$model,a:{Type:{t:Anything}}},$ps:[{$nm:'instance',$mt:'prm',$def:1,$t:{t:Anything}}],$cont:OpenValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$m','apply']};};
            
            //AttributeDeclaration defaulted at X (40:4-40:44)
            defineAttr($$openValue,'defaulted',function(){
                var $$openValue=this;
                return false;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:OpenValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','defaulted']};});
            
            //AttributeDeclaration variadic at X (41:4-41:43)
            defineAttr($$openValue,'variadic',function(){
                var $$openValue=this;
                return false;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:OpenValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ValueDeclaration','$at','variadic']};});
            
            //AttributeGetterDefinition openType at X (44:2-44:43)
            defineAttr($$openValue,'openType',function(){
                var $$openValue=this;
                throw Exception("IMPL OpenValue.openType");
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
  defineAttr($$openValue,'string',function(){return String$("OpenValue[" + this.containingPackage.name + "::" + this.name_+"]");},undefined,{$an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','Object','$at','string']});

    defineAttr($$openValue,'qualifiedName',function(){
       if (this.toplevel) {
         return String$(this.containingPackage.name + "::" + this.name);
       } else {
         var qn = this.containingPackage.name + "::";
         for (var i=1; i<this.tipo.$$metamodel$$.d.length;i++) {
           var part = this.tipo.$$metamodel$$.d[i];
           qn += part[0]=='$'?'.':part;
         }
         return String$(qn);
       }
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
    OpenValue(pkg, meta,$$openVariable);
    VariableDeclaration$meta$declaration($$openVariable);
    return $$openVariable;
}
OpenVariable.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:OpenValue},satisfies:[{t:VariableDeclaration$meta$declaration}],d:['ceylon.language.meta.declaration','VariableDeclaration']};};
function $init$OpenVariable(){
    if (OpenVariable.$$===undefined){
        initTypeProto(OpenVariable,'ceylon.language.meta.declaration::OpenVariable',OpenValue,VariableDeclaration$meta$declaration);
        (function($$openVariable){
            
            //AttributeGetterDefinition setter at opentypes.ceylon (61:4-61:52)
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

      defineAttr($$openClass,'string',function(){
        return String$("class " + this.qualifiedName);
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language','Object']};}); 
      defineAttr($$openClass,'anonymous',function(){
        var $$openClass=this;
        return false; //TODO
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

            //MethodDefinition apply at X (49:2-49:76)
            $$openClass.$apply=function $apply(types$9){
                var $$openClass=this;
                if(types$9===undefined){types$9=getEmpty();}
                throw Exception("IMPL OpenClass.apply");
            };$$openClass.$apply.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Class$meta$model,a:{Arguments:{t:Nothing},Type:{t:Anything}}},$ps:[{$nm:'types',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element:{t:Type$meta$model}}}}],$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassDeclaration','$m','apply']};};
            
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
            //AttributeDeclaration typeParameterDeclarations at X (62:2-62:63)
            defineAttr($$openClass,'typeParameterDeclarations',function(){
                var $$openClass=this;
                return getEmpty();
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:{t:TypeParameter$meta$declaration}}},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassDeclaration','$at','typeParameterDeclarations']};});
            
            //MethodDeclaration getTypeParameterDeclaration at X (63:2-63:79)
            $$openClass.getTypeParameterDeclaration=function (name$14){
                var $$openClass=this;
                return null;
            };
            $$openClass.getTypeParameterDeclaration.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:TypeParameter$meta$declaration}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}}],$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassDeclaration','$m','getTypeParameterDeclaration']};};
            
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
              return FreeClass(OpenClass(getModules$meta().find(mm.mod['$mod-name'],mm.mod['$mod-version']).findPackage(mm.d[0]), sc.t));
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:OpenClassType$meta$declaration}]},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassDeclaration','$at','extendedType']};});
            
            //AttributeDeclaration interfaceDeclarations at X (69:2-69:89)
            defineAttr($$openClass,'satisfiedTypes',function(){
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
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:{t:OpenInterfaceType$meta$declaration}}},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassDeclaration','$at','satisfiedTypes']};});
            defineAttr($$openClass,'name',function(){return this.name_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassDeclaration','$at','name']};});
            defineAttr($$openClass,'containingPackage',function(){return this._pkg;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Package$meta$declaration},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassDeclaration','$at','containingPackage']};});
            defineAttr($$openClass,'containingModule',function(){return this._pkg.container;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Module$meta$declaration},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassDeclaration','$at','containingModule']};});
            defineAttr($$openClass,'toplevel',function(){return this.toplevel_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','ClassDeclaration','$at','toplevel']};});

    defineAttr($$openClass,'qualifiedName',function(){
       if (this.toplevel) {
         return String$(this.containingPackage.name + "::" + this.name);
       } else {
         var qn = this.containingPackage.name + "::";
         for (var i=1; i<this.tipo.$$metamodel$$.d.length;i++) {
           var part = this.tipo.$$metamodel$$.d[i];
           qn += part[0]=='$'?'.':part;
         }
         return String$(qn);
       }
    },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Declaration','$at','qualifiedName']};});

      $$openClass.equals=function(other) {
        return isOfType(other, {t:OpenClass}) && other.tipo==this.tipo;
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
            
defineAttr($$openInterface,'string',function(){
  return String$("interface " + this.qualifiedName);
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},$cont:OpenInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language','Object']};}); 
            //MethodDefinition apply at X (73:2-73:71)
            $$openInterface.$apply=function $apply(types$16){
              return AppliedInterface(this.tipo,{Type:{t:this.tipo}});
            };$$openInterface.$apply.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Interface$meta,a:{Type:{t:Anything}}},$ps:[{$nm:'types',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element:{t:Type$meta$model}}}}],$cont:OpenInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','InterfaceDeclaration','$m','apply']};};
            
            //MethodDeclaration annotatedMemberDeclarations at X (80:2-81:66)
            $$openInterface.annotatedMemberDeclarations=function ($$$mptypes){
                var $$openInterface=this;
                return getEmpty();
            };
            $$openInterface.annotatedMemberDeclarations.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:'Kind'}},$ps:[],$cont:OpenInterface,$tp:{Kind:{'satisfies':[{t:NestableDeclaration$meta$declaration}]},Annotation:{}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','InterfaceDeclaration','$m','annotatedMemberDeclarations']};};
            
           
            //AttributeGetterDefinition openType at X (86:2-86:43)
            defineAttr($$openInterface,'openType',function(){
                var $$openInterface=this;
                throw Exception("IMPL OpenInterface.openType");
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:OpenType$meta$declaration},$cont:OpenInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','InterfaceDeclaration','$at','openType']};});
            //AttributeDeclaration typeParameterDeclarations at X (87:2-87:63)
            defineAttr($$openInterface,'typeParameterDeclarations',function(){
                var $$openInterface=this;
                return getEmpty();
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:{t:TypeParameter$meta$declaration}}},$cont:OpenInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','InterfaceDeclaration','$at','typeParameterDeclarations']};});
            
            //MethodDeclaration getTypeParameterDeclaration at X (88:2-88:79)
            $$openInterface.getTypeParameterDeclaration=function (name$21){
                var $$openInterface=this;
                return null;
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
       if (this.toplevel) {
         return String$(this.containingPackage.name + "::" + this.name);
       } else {
         var qn = this.containingPackage.name + "::";
         for (var i=1; i<this.tipo.$$metamodel$$.d.length;i++) {
           var part = this.tipo.$$metamodel$$.d[i];
           qn += part[0]=='$'?'.':part;
         }
         return String$(qn);
       }
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
  var mm=alias.$mod;
  for (var i=0; i < alias.$d.length; i++) {
    var p = alias.$d[i];
    mm = mm[p];
  }
  $$openAlias.meta=mm;
  AliasDeclaration$meta$declaration($$openAlias);
  return $$openAlias;
}
OpenAlias.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Basic},satisfies:[{t:AliasDeclaration$meta$declaration}],d:['ceylon.language.meta.declaration','OpenAlias']};};
function $init$OpenAlias(){
  if (OpenAlias.$$===undefined){
    initTypeProto(OpenAlias,'OpenAlias',Basic,AliasDeclaration$meta$declaration);
    (function($$openAlias){

      defineAttr($$openAlias,'extendedType',function(){
        var $$openAlias=this;
        var _t = $$openAlias._alias.t;
        if (_t==='i' || _t==='u') {
          console.log("TODO union/intersection aliased type");
        } else {
          var mm = _t.$$metamodel$$;
          if (typeof(mm)==='function') {
            mm=mm(); _t.$$metamodel$$=mm;
          }
          var meta=get_model(mm);
          if (meta.$mt==='cls') {
            return FreeInterface(OpenClass(getModules$meta().find(this._alias.$mod['$mod-name'],this._alias.$mod['$mod-version']).findPackage(this._alias.$d[0]), _t));
          } else if (meta.$mt==='ifc') {
            return FreeInterface(OpenInterface(getModules$meta().find(this._alias.$mod['$mod-name'],this._alias.$mod['$mod-version']).findPackage(this._alias.$d[0]), _t));
          }
        }
        throw wrapexc(Exception(String$("OpenAlias.extendedType",22)),'5:40-5:81','caca.ceylon');
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:OpenType$meta$declaration},$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$at','extendedType']};});
      defineAttr($$openAlias,'qualifiedName',function(){
        var path=this._alias.$d;
        var qn = path[0] + "::";
        for (var i=1; i<path.length;i++) {
          var part = path[i];
          qn += part[0]=='$'?'.':part;
        }
        return String$(qn);
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$at','qualifiedName']};});
      defineAttr($$openAlias,'toplevel',function(){
        return this._alias.$cont===undefined;
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean},$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$at','toplevel']};});
      defineAttr($$openAlias,'$default',function(){
        var $$openAlias=this;
        throw wrapexc(Exception(String$("OpenAlias.default",17)),'8:34-8:70','caca.ceylon');
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean},$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$at','default']};});
      defineAttr($$openAlias,'actual',function(){
        var $$openAlias=this;
        throw wrapexc(Exception(String$("OpenAlias.actual",16)),'9:33-9:68','caca.ceylon');
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean},$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$at','actual']};});
      defineAttr($$openAlias,'formal',function(){
        var $$openAlias=this;
        throw wrapexc(Exception(String$("OpenAlias.formal",16)),'10:33-10:68','caca.ceylon');
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean},$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$at','formal']};});
      defineAttr($$openAlias,'shared',function(){
        var $$openAlias=this;
        throw wrapexc(Exception(String$("OpenAlias.shared",16)),'11:33-11:68','caca.ceylon');
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean},$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$at','shared']};});
      defineAttr($$openAlias,'containingPackage',function(){
        throw Exception("IMPL OpenAlias.containingPackage");
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Package$meta$declaration},$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$at','containingPackage']};});
      defineAttr($$openAlias,'containingModule',function(){
        var $$openAlias=this;
        throw wrapexc(Exception(String$("OpenAlias.containingModule",26)),'12:42-12:87','caca.ceylon');
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Module$meta$declaration},$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$at','containingModule']};});
      defineAttr($$openAlias,'container',function(){
        if (this._alias.$cont===undefined)return null;
        throw wrapexc(Exception(String$("OpenAlias.container",19)),'13:56-13:94','caca.ceylon');
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:NestableDeclaration$meta$declaration},{t:Package$meta$declaration}]},$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$at','container']};});
      defineAttr($$openAlias,'openType',function(){
        var $$openAlias=this;
        throw wrapexc(Exception(String$("OpenAlias.openType",18)),'14:36-14:73','caca.ceylon');
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:OpenType$meta$declaration},$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$at','openType']};});
      $$openAlias.annotations=function annotations($$$mptypes){
        var $$openAlias=this;
        throw wrapexc(Exception(String$("OpenAlias.annotations()",23)),'16:60-16:102','caca.ceylon');
      };$$openAlias.annotations.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:'Annotation'}},$ps:[],$cont:OpenAlias,$tp:{Annotation:{'var':'out','satisfies':[{t:Annotation,a:{Value:'Annotation'}}]}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$m','annotations']};};
      defineAttr($$openAlias,'typeParameterDeclarations',function(){
        var tps=this.meta.$tp;
        if (tps && tps.length>0) {
          var rv=[];
          for (var i=0; i < tps.length; i++) {
            rv.push(tps[i]); //TODO TypeParameter
          }
          return rv.reifyCeylonType({Element:{t:TypeParameter$meta$declaration},Absent:{t:Null}});
        }
        return getEmpty();
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:{t:TypeParameter$meta$declaration}}},$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$at','typeParameterDeclarations']};});
      $$openAlias.getTypeParameterDeclaration=function getTypeParameterDeclaration(name$2){
        var tps=this.meta.$tp;
        if (tps && tps.length>0) {
          for (var i=0; i < tps.length; i++) {
            if (tps[i].$nm === name$2) {
              return tps[i]; //TODO TypeParameter
            }
          }
        }
        return null;
      };$$openAlias.getTypeParameterDeclaration.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:TypeParameter$meta$declaration}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$},$an:function(){return[];}}],$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$m','getTypeParameterDeclaration']};};
      defineAttr($$openAlias,'name',function(){
        return String$(this._alias.$d[this._alias.$d.length-1]);
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$at','name']};});
      defineAttr($$openAlias,'containingPackage',function(){
        throw Exception("IMPL OpenAlias.containingPackage");
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Package$meta$declaration},$cont:OpenAlias,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','AliasDeclaration','$at','containingPackage']};});
    })(OpenAlias.$$.prototype);
  }
  return OpenAlias;
}
exports.$init$OpenAlias=$init$OpenAlias;
$init$OpenAlias();
