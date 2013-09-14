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
  return mod.meta[nm];
}

//ClassDefinition FreeParameterisedType at o.ceylon (136:0-147:0)
function FreeParameterisedType(declaration, $$targs$$,that){
    $init$FreeParameterisedType();
    if (that===undefined)that=new FreeParameterisedType.$$;
    set_type_args(that,$$targs$$);
    OpenParameterisedType$model$declaration(that.$$targs$$===undefined?$$targs$$:{DeclarationType:that.$$targs$$.DeclarationType},that);
    
    //AttributeDeclaration declaration at o.ceylon (140:4-140:45)
    that.decl=declaration;
    return that;
}
FreeParameterisedType.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Basic},$tp:{DeclarationType:{'var':'out','satisfies':[{t:ClassOrInterfaceDeclaration$model$declaration}]}},satisfies:[{t:OpenParameterisedType$model$declaration,a:{DeclarationType:'DeclarationType'}}],d:['ceylon.language.model.declaration','OpenParameterisedType']};};
function $init$FreeParameterisedType(){
    if (FreeParameterisedType.$$===undefined){
        initTypeProto(FreeParameterisedType,'FreeParameterisedType',Basic,OpenParameterisedType$model$declaration);
        (function(that){
            
            //AttributeDeclaration declaration at o.ceylon (140:4-140:45)
            defineAttr(that,'declaration',function(){return this.decl;},undefined,function(){return{mod:$$METAMODEL$$,$t:'DeclarationType',$cont:FreeParameterisedType,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','OpenParameterisedType','$at','declaration']};});
            
            //AttributeGetterDefinition superclass at o.ceylon (142:4-142:106)
            defineAttr(that,'superclass',function(){
                return this.declaration.superclassDeclaration;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:OpenParameterisedType$model$declaration,a:{DeclarationType:{t:ClassDeclaration$model$declaration}}}]},$cont:FreeParameterisedType,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','OpenParameterisedType','$at','superclass']};});
            //AttributeGetterDefinition interfaces at o.ceylon (144:4-144:111)
            defineAttr(that,'interfaces',function(){
                return this.declaration.interfaceDeclarations;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:{t:OpenParameterisedType$model$declaration,a:{DeclarationType:{t:InterfaceDeclaration$model$declaration}}}}},$cont:FreeParameterisedType,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','OpenParameterisedType','$at','interfaces']};});
            //AttributeGetterDefinition typeArguments at o.ceylon (146:4-146:97)
            defineAttr(that,'typeArguments',function(){
                var that=this;
                throw wrapexc(Exception(String$("unimplemented",13)),'146:63-146:95','o.ceylon');
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Map,a:{Key:{t:TypeParameter$model$declaration},Item:{t:OpenType$model$declaration}}},$cont:FreeParameterisedType,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','OpenParameterisedType','$at','typeArguments']};});
        })(FreeParameterisedType.$$.prototype);
    }
    return FreeParameterisedType;
}
exports.$init$FreeParameterisedType=$init$FreeParameterisedType;
$init$FreeParameterisedType();

//ClassDefinition OpenFunction at caca.ceylon (18:0-36:0)
function OpenFunction(name, packageContainer, toplevel, meta, that){
    $init$OpenFunction();
    if (that===undefined)that=new OpenFunction.$$;
    that.name_=name;
    that.packageContainer_=packageContainer;
    that.toplevel_=toplevel;
    if (meta.$$metamodel$$ === undefined) {
      //it's a metamodel
      that.meta=meta;
      that.tipo=_findTypeFromModel(packageContainer,meta);
    } else {
      //it's a type
      that.tipo = meta;
      var _mm=meta.$$metamodel$$;
      if (typeof(_mm)==='function') {
        _mm=_mm();
        meta.$$metamodel$$=_mm;
      }
      that.meta = get_model(_mm);
    }
    FunctionDeclaration$model$declaration(that);
    return that;
}
OpenFunction.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Basic},satisfies:[{t:FunctionDeclaration$model$declaration}],d:['ceylon.language.model.declaration','FunctionDeclaration']};};
function $init$OpenFunction(){
    if (OpenFunction.$$===undefined){
        initTypeProto(OpenFunction,'OpenFunction',Basic,FunctionDeclaration$model$declaration);
        (function($$openFunction){
            
            //MethodDefinition apply at caca.ceylon (20:4-20:81)
            $$openFunction.apply=function apply(types$2){
                var $$openFunction=this;
                if(types$2===undefined){types$2=getEmpty();}
                throw Exception();
            };$$openFunction.apply.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Function$model,a:{Arguments:{t:Nothing},Type:{t:Anything}}},$ps:[{$nm:'types',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element:{t:Type$model}}}}],$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','FunctionDeclaration','$m','apply']};};
            
            //MethodDefinition bindAndApply at caca.ceylon (21:4-21:105)
            $$openFunction.bindAndApply=function bindAndApply(instance$3,types$4){
              var $$openFunction=this;
              if(types$4===undefined){types$4=getEmpty();}
              //TODO check for naming rules
              //WTF is types argument for?
              return AppliedFunction(instance$3[this.name],undefined,instance$3);
            };$$openFunction.bindAndApply.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Function$model,a:{Arguments:{t:Nothing},Type:{t:Anything}}},$ps:[{$nm:'instance',$mt:'prm',$t:{t:Object$}},{$nm:'types',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element:{t:Type$model}}}}],$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','FunctionDeclaration','$m','bindAndApply']};};
            
            //MethodDefinition memberApply at caca.ceylon (22:4-23:54)
            $$openFunction.memberApply=function memberApply(types$5,$$$mptypes){
                var $$openFunction=this;
                if(types$5===undefined){types$5=getEmpty();}
                throw Exception();
            };$$openFunction.memberApply.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Method$model,a:{Arguments:'Arguments',Type:'MethodType',Container:'Container'}},$ps:[{$nm:'types',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element:{t:Type$model}}}}],$cont:OpenFunction,$tp:{Container:{},MethodType:{},Arguments:{'satisfies':[{t:Sequential,a:{Element:{t:Anything}}}]}},$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','FunctionDeclaration','$m','memberApply']};};
            
            //AttributeDeclaration defaulted at caca.ceylon (25:4-25:44)
            defineAttr($$openFunction,'defaulted',function(){
                var $$openFunction=this;
                return false;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','FunctionDeclaration','$at','defaulted']};});
            
            //AttributeDeclaration variadic at caca.ceylon (26:4-26:43)
            defineAttr($$openFunction,'variadic',function(){
                var $$openFunction=this;
                return false;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','FunctionDeclaration','$at','variadic']};});
            
            //MethodDeclaration getParameterDeclaration at caca.ceylon (29:4-29:90)
            $$openFunction.getParameterDeclaration=function (name$6){
                var $$openFunction=this;
                return null;
            };
            $$openFunction.getParameterDeclaration.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:FunctionOrValueDeclaration$model$declaration}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}}],$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','FunctionDeclaration','$m','getParameterDeclaration']};};
            
            //AttributeGetterDefinition openType at caca.ceylon (33:2-33:43)
            defineAttr($$openFunction,'openType',function(){
                var $$openFunction=this;
                throw Exception();
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:OpenType$model$declaration},$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','FunctionDeclaration','$at','openType']};});
            //AttributeDeclaration typeParameterDeclarations at caca.ceylon (34:2-34:63)
            defineAttr($$openFunction,'typeParameterDeclarations',function(){
                var $$openFunction=this;
                return getEmpty();
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:{t:TypeParameter$model$declaration}}},$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','FunctionDeclaration','$at','typeParameterDeclarations']};});
            
            //MethodDeclaration getTypeParameterDeclaration at caca.ceylon (35:2-35:79)
            $$openFunction.getTypeParameterDeclaration=function (name$7){
                var $$openFunction=this;
                return null;
            };
            $$openFunction.getTypeParameterDeclaration.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:TypeParameter$model$declaration}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}}],$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','FunctionDeclaration','$m','getTypeParameterDeclaration']};};
            defineAttr($$openFunction,'name',function(){return this.name_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','FunctionDeclaration','$at','name']};});
            defineAttr($$openFunction,'packageContainer',function(){return this.packageContainer_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Package$model$declaration},$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','FunctionDeclaration','$at','packageContainer']};});
            defineAttr($$openFunction,'toplevel',function(){return this.toplevel_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','FunctionDeclaration','$at','toplevel']};});

    defineAttr($$openFunction,'qualifiedName',function(){
       if (this.toplevel) {
         return String$(this.packageContainer.name + "::" + this.name);
       } else {
         var qn = this.packageContainer.name + "::";
         for (var i=1; i<this.tipo.$$metamodel$$.d.length;i++) {
           var part = this.tipo.$$metamodel$$.d[i];
           qn += part[0]=='$'?'.':part;
         }
         return String$(qn);
       }
    },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},$cont:OpenFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','Declaration','$at','qualifiedName']};});
        })(OpenFunction.$$.prototype);
    }
    return OpenFunction;
}
exports.$init$OpenFunction=$init$OpenFunction;
$init$OpenFunction();

//ClassDefinition OpenValue at caca.ceylon (38:0-45:0)
function OpenValue(name, packageContainer, toplevel, meta, that){
    $init$OpenValue();
    if (that===undefined)that=new OpenValue.$$;
    that.name_=name;
    that.packageContainer_=packageContainer;
    that.toplevel_=toplevel;
    if (meta.$$metamodel$$ === undefined) {
      //it's a metamodel
      that.meta=meta;
      if (meta['$mt']==='prm') {
        that.tipo={$$metamodel$$:meta};
      } else {
        that.tipo=_findTypeFromModel(packageContainer,meta);
      }
    } else {
      //it's a type
      that.tipo = meta;
      var _mm=meta.$$metamodel$$;
      if (typeof(_mm)==='function') {
        _mm=_mm();
        meta.$$metamodel$$=_mm;
      }
      that.meta = get_model(_mm);
    }
    ValueDeclaration$model$declaration(that);
    return that;
}
OpenValue.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Basic},satisfies:[{t:ValueDeclaration$model$declaration}],d:['ceylon.language.model.declaration','ValueDeclaration']};};
function $init$OpenValue(){
    if (OpenValue.$$===undefined){
        initTypeProto(OpenValue,'OpenValue',Basic,ValueDeclaration$model$declaration);
        (function($$openValue){
            
            //MethodDefinition apply at caca.ceylon (39:4-39:68)
            $$openValue.apply=function apply(instance$8){
              if (instance$8===null || instance$8===undefined)return null;
              return instance$8[this.name];
            };$$openValue.apply.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Value$model,a:{Type:{t:Anything}}},$ps:[{$nm:'instance',$mt:'prm',$def:1,$t:{t:Anything}}],$cont:OpenValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','ValueDeclaration','$m','apply']};};
            
            //AttributeDeclaration defaulted at caca.ceylon (40:4-40:44)
            defineAttr($$openValue,'defaulted',function(){
                var $$openValue=this;
                return false;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:OpenValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','ValueDeclaration','$at','defaulted']};});
            
            //AttributeDeclaration variadic at caca.ceylon (41:4-41:43)
            defineAttr($$openValue,'variadic',function(){
                var $$openValue=this;
                return false;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:OpenValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','ValueDeclaration','$at','variadic']};});
            
            //AttributeGetterDefinition openType at caca.ceylon (44:2-44:43)
            defineAttr($$openValue,'openType',function(){
                var $$openValue=this;
                throw Exception();
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:OpenType$model$declaration},$cont:OpenValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','ValueDeclaration','$at','openType']};});

defineAttr($$openValue,'name',function(){return this.name_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},$cont:OpenValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','ValueDeclaration','$at','name']};});
defineAttr($$openValue,'packageContainer',function(){return this.packageContainer_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Package$model$declaration},$cont:OpenValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','ValueDeclaration','$at','packageContainer']};});
defineAttr($$openValue,'toplevel',function(){return this.toplevel_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:OpenValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','ValueDeclaration','$at','toplevel']};});

  $$openValue.equals=function(other) {
    if (isOfType(other, {t:OpenValue}) && other.name.equals(this.name) && other.toplevel===this.toplevel && other.packageContainer.equals(this.packageContainer)) {
      return other.meta==this.meta;
    }
    return false;
  }
  defineAttr($$openValue,'string',function(){return String$("OpenValue[" + this.packageContainer.name + "::" + this.name_+"]");},undefined,{$an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','Object','$at','string']});

    defineAttr($$openValue,'qualifiedName',function(){
       if (this.toplevel) {
         return String$(this.packageContainer.name + "::" + this.name);
       } else {
         var qn = this.packageContainer.name + "::";
         for (var i=1; i<this.tipo.$$metamodel$$.d.length;i++) {
           var part = this.tipo.$$metamodel$$.d[i];
           qn += part[0]=='$'?'.':part;
         }
         return String$(qn);
       }
    },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},$cont:OpenValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','Declaration','$at','qualifiedName']};});
        })(OpenValue.$$.prototype);
    }
    return OpenValue;
}
exports.$init$OpenValue=$init$OpenValue;
$init$OpenValue();

//ClassDefinition OpenVariable at opentypes.ceylon (60:0-62:0)
function OpenVariable(name, packageContainer, toplevel, meta, $$openVariable){
    $init$OpenVariable();
    if ($$openVariable===undefined)$$openVariable=new OpenVariable.$$;
    $$openVariable.name_=name;
    $$openVariable.packageContainer_=packageContainer;
    $$openVariable.toplevel_=toplevel;
    OpenValue($$openVariable.name,$$openVariable.packageContainer,$$openVariable.toplevel,meta,$$openVariable);
    VariableDeclaration$model$declaration($$openVariable);
    return $$openVariable;
}
OpenVariable.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:OpenValue},satisfies:[{t:VariableDeclaration$model$declaration}],d:['ceylon.language.model.declaration','VariableDeclaration']};};
function $init$OpenVariable(){
    if (OpenVariable.$$===undefined){
        initTypeProto(OpenVariable,'OpenVariable',OpenValue,VariableDeclaration$model$declaration);
        (function($$openVariable){
            
            //AttributeGetterDefinition setter at opentypes.ceylon (61:4-61:52)
            defineAttr($$openVariable,'setter',function(){
              var $$openVariable=this;
              return OpenSetter(this);
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:SetterDeclaration$model$declaration},$cont:OpenVariable,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','VariableDeclaration','$at','setter']};});

defineAttr($$openVariable,'name',function(){return this.name_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},$cont:OpenVariable,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','VariableDeclaration','$at','name']};});
defineAttr($$openVariable,'packageContainer',function(){return this.packageContainer_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Package$model$declaration},$cont:OpenVariable,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','VariableDeclaration','$at','packageContainer']};});
defineAttr($$openVariable,'toplevel',function(){return this.toplevel_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:OpenVariable,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','VariableDeclaration','$at','toplevel']};});

  $$openVariable.equals=function(other) {
    if (isOfType(other, {t:OpenVariable}) && other.name.equals(this.name) && other.toplevel===this.toplevel && other.packageContainer.equals(this.packageContainer)) {
      return other.meta==this.meta;
    }
    return false;
  }
  defineAttr($$openVariable,'string',function(){return String$("OpenVariable[" + this.packageContainer.name + "::" + this.name_+"]");},undefined,{$an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','Object','$at','string']});

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
    SetterDeclaration$model$declaration($$openSetter);
    $$openSetter.tipo=v.tipo.set;
    return $$openSetter;
}
OpenSetter.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Basic},satisfies:[{t:SetterDeclaration$model$declaration}],d:['ceylon.language.model.declaration','SetterDeclaration']};};
function $init$OpenSetter(){
    if (OpenSetter.$$===undefined){
        initTypeProto(OpenSetter,'OpenSetter',Basic,SetterDeclaration$model$declaration);
        (function($$openSetter){
            defineAttr($$openSetter,'variable',function(){return this.variable_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:VariableDeclaration$model$declaration},$cont:OpenSetter,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','SetterDeclaration','$at','variable']};});
        })(OpenSetter.$$.prototype);
    }
    return OpenSetter;
}
exports.$init$OpenSetter=$init$OpenSetter;
$init$OpenSetter();

//ClassDefinition OpenClass at caca.ceylon (47:0-70:0)
function OpenClass(name, packageContainer, toplevel, meta, that){
    $init$OpenClass();
    if (that===undefined)that=new OpenClass.$$;
    that.name_=name;
    that.packageContainer_=packageContainer;
    that.toplevel_=toplevel;
    if (meta.$$metamodel$$ === undefined) {
      //it's a metamodel
      that.meta=meta;
      that.tipo=_findTypeFromModel(packageContainer,meta);
    } else {
      //it's a type
      that.tipo = meta;
      var _mm=meta.$$metamodel$$;
      if (typeof(_mm)==='function') {
        _mm=_mm();
        meta.$$metamodel$$=_mm;
      }
      that.meta = get_model(_mm);
    }
    ClassDeclaration$model$declaration(that);
    return that;
}
OpenClass.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Basic},satisfies:[{t:ClassDeclaration$model$declaration}],d:['ceylon.language.model.declaration','ClassDeclaration']};};
function $init$OpenClass(){
    if (OpenClass.$$===undefined){
        initTypeProto(OpenClass,'OpenClass',Basic,ClassDeclaration$model$declaration);
        (function($$openClass){
            
            //AttributeDeclaration anonymous at caca.ceylon (48:2-48:42)
            defineAttr($$openClass,'anonymous',function(){
                var $$openClass=this;
                return false;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','ClassDeclaration','$at','anonymous']};});
            
            //MethodDefinition apply at caca.ceylon (49:2-49:76)
            $$openClass.apply=function apply(types$9){
                var $$openClass=this;
                if(types$9===undefined){types$9=getEmpty();}
                throw Exception();
            };$$openClass.apply.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Class$model,a:{Arguments:{t:Nothing},Type:{t:Anything}}},$ps:[{$nm:'types',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element:{t:Type$model}}}}],$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','ClassDeclaration','$m','apply']};};
            
            //MethodDefinition bindAndApply at caca.ceylon (50:2-50:100)
            $$openClass.bindAndApply=function bindAndApply(instance$10,types$11){
                var $$openClass=this;
                if(types$11===undefined){types$11=getEmpty();}
                throw Exception();
            };$$openClass.bindAndApply.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Class$model,a:{Arguments:{t:Nothing},Type:{t:Anything}}},$ps:[{$nm:'instance',$mt:'prm',$t:{t:Object$}},{$nm:'types',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element:{t:Type$model}}}}],$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','ClassDeclaration','$m','bindAndApply']};};
            
            //MethodDefinition memberApply at caca.ceylon (51:2-52:72)
            $$openClass.memberApply=function memberApply(types$12,$$$mptypes){
                var $$openClass=this;
                if(types$12===undefined){types$12=getEmpty();}
                throw Exception();
            };$$openClass.memberApply.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Member$model,a:{Type:'Container',Kind:'Kind'}},$ps:[{$nm:'types',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element:{t:Type$model}}}}],$cont:OpenClass,$tp:{Container:{},Kind:{'satisfies':[{t:ClassOrInterface$model,a:{Type:{t:Anything}}}]}},$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','ClassDeclaration','$m','memberApply']};};
            
            //MethodDeclaration memberDeclarations at caca.ceylon (53:2-54:66)
            $$openClass.memberDeclarations=function ($$$mptypes){
                var $$openClass=this;
                return getEmpty();
            };
            $$openClass.memberDeclarations.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:'Kind'}},$ps:[],$cont:OpenClass,$tp:{Kind:{'satisfies':[{t:TopLevelOrMemberDeclaration$model$declaration}]}},$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','ClassDeclaration','$m','memberDeclarations']};};
            
            //MethodDeclaration annotatedMemberDeclarations at caca.ceylon (55:2-56:66)
            $$openClass.annotatedMemberDeclarations=function ($$$mptypes){
                var $$openClass=this;
                return getEmpty();
            };
            $$openClass.annotatedMemberDeclarations.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:'Kind'}},$ps:[],$cont:OpenClass,$tp:{Kind:{'satisfies':[{t:TopLevelOrMemberDeclaration$model$declaration}]},Annotation:{}},$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','ClassDeclaration','$m','annotatedMemberDeclarations']};};
            
            //AttributeGetterDefinition openType at caca.ceylon (61:2-61:43)
            defineAttr($$openClass,'openType',function(){
                var $$openClass=this;
                throw Exception();
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:OpenType$model$declaration},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','ClassDeclaration','$at','openType']};});
            //AttributeDeclaration typeParameterDeclarations at caca.ceylon (62:2-62:63)
            defineAttr($$openClass,'typeParameterDeclarations',function(){
                var $$openClass=this;
                return getEmpty();
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:{t:TypeParameter$model$declaration}}},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','ClassDeclaration','$at','typeParameterDeclarations']};});
            
            //MethodDeclaration getTypeParameterDeclaration at caca.ceylon (63:2-63:79)
            $$openClass.getTypeParameterDeclaration=function (name$14){
                var $$openClass=this;
                return null;
            };
            $$openClass.getTypeParameterDeclaration.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:TypeParameter$model$declaration}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}}],$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','ClassDeclaration','$m','getTypeParameterDeclaration']};};
            
           //MethodDeclaration getParameterDeclaration at caca.ceylon (66:2-66:88)
            $$openClass.getParameterDeclaration=function (name$15){
                var $$openClass=this;
                return null;
            };
            $$openClass.getParameterDeclaration.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:FunctionOrValueDeclaration$model$declaration}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}}],$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','ClassDeclaration','$m','getParameterDeclaration']};};
            
            //AttributeDeclaration superclassDeclaration at caca.ceylon (68:2-68:86)
            defineAttr($$openClass,'superclassDeclaration',function(){
              var sc = this.tipo.$$metamodel$$['super'];
              if (sc === undefined)return null;
              var mm = sc.t.$$metamodel$$;
              if (typeof(mm)==='function') {
                mm = mm();
                sc.t.$$metamodel$$=mm;
              }
              var pkg = getModules$model().find(mm.mod['$mod-name'],mm.mod['$mod-version']).findPackage(mm.d[0]);
              return FreeParameterisedType(OpenClass(mm.d[mm.d.length-1], pkg, mm['$cont']===undefined, sc.t));
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:OpenParameterisedType$model$declaration,a:{DeclarationType:{t:ClassDeclaration$model$declaration}}}]},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','ClassDeclaration','$at','superclassDeclaration']};});
            
            //AttributeDeclaration interfaceDeclarations at caca.ceylon (69:2-69:89)
            defineAttr($$openClass,'interfaceDeclarations',function(){
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
                  var pkg = getModules$model().find(mm.mod['$mod-name'],mm.mod['$mod-version']).findPackage(mm.d[0]);
                  rv.push(FreeParameterisedType(OpenInterface(mm.d[mm.d.length-1], pkg, mm['$cont']===undefined, ifc)));
                }
                return rv.reifyCeylonType({Absent:{t:Null},Element:{t:OpenParameterisedType$model$declaration,a:{DeclarationType:{t:InterfaceDeclaration$model$declaration}}}});
              }
              return getEmpty();
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:{t:OpenParameterisedType$model$declaration,a:{DeclarationType:{t:InterfaceDeclaration$model$declaration}}}}},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','ClassDeclaration','$at','interfaceDeclarations']};});
            defineAttr($$openClass,'name',function(){return this.name_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','ClassDeclaration','$at','name']};});
            defineAttr($$openClass,'packageContainer',function(){return this.packageContainer_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Package$model$declaration},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','ClassDeclaration','$at','packageContainer']};});
            defineAttr($$openClass,'toplevel',function(){return this.toplevel_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','ClassDeclaration','$at','toplevel']};});

    defineAttr($$openClass,'qualifiedName',function(){
       if (this.toplevel) {
         return String$(this.packageContainer.name + "::" + this.name);
       } else {
         var qn = this.packageContainer.name + "::";
         for (var i=1; i<this.tipo.$$metamodel$$.d.length;i++) {
           var part = this.tipo.$$metamodel$$.d[i];
           qn += part[0]=='$'?'.':part;
         }
         return String$(qn);
       }
    },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},$cont:OpenClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','Declaration','$at','qualifiedName']};});
        })(OpenClass.$$.prototype);
    }
    return OpenClass;
}
exports.$init$OpenClass=$init$OpenClass;
$init$OpenClass();

//ClassDefinition OpenInterface at caca.ceylon (72:0-92:0)
function OpenInterface(name, packageContainer, toplevel, meta, that) {
    $init$OpenInterface();
    if (that===undefined)that=new OpenInterface.$$;
    that.name_=name;
    that.packageContainer_=packageContainer;
    that.toplevel_=toplevel;
    if (meta.$$metamodel$$ === undefined) {
      //it's a metamodel
      that.meta=meta;
      that.tipo=_findTypeFromModel(packageContainer,meta);
    } else {
      //it's a type
      that.tipo = meta;
      var _mm=meta.$$metamodel$$;
      if (typeof(_mm)==='function') {
        _mm=_mm();
        meta.$$metamodel$$=_mm;
      }
      that.meta = get_model(_mm);
    }
    InterfaceDeclaration$model$declaration(that);
    return that;
}
OpenInterface.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Basic},satisfies:[{t:InterfaceDeclaration$model$declaration}],d:['ceylon.language.model.declaration','InterfaceDeclaration']};};
function $init$OpenInterface(){
    if (OpenInterface.$$===undefined){
        initTypeProto(OpenInterface,'OpenInterface',Basic,InterfaceDeclaration$model$declaration);
        (function($$openInterface){
            
            //MethodDefinition apply at caca.ceylon (73:2-73:71)
            $$openInterface.apply=function apply(types$16){
              return AppliedInterface(this.tipo,{Type:{t:this.tipo}});
            };$$openInterface.apply.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Interface$model,a:{Type:{t:Anything}}},$ps:[{$nm:'types',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element:{t:Type$model}}}}],$cont:OpenInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','InterfaceDeclaration','$m','apply']};};
            
            //MethodDefinition bindAndApply at caca.ceylon (74:2-74:95)
            $$openInterface.bindAndApply=function bindAndApply(instance$17,types$18){
                var $$openInterface=this;
                if(types$18===undefined){types$18=getEmpty();}
                throw Exception();
            };$$openInterface.bindAndApply.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Interface$model,a:{Type:{t:Anything}}},$ps:[{$nm:'instance',$mt:'prm',$t:{t:Object$}},{$nm:'types',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element:{t:Type$model}}}}],$cont:OpenInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','InterfaceDeclaration','$m','bindAndApply']};};
            
            //MethodDefinition memberApply at caca.ceylon (75:2-76:72)
            $$openInterface.memberApply=function memberApply(types$19,$$$mptypes){
                var $$openInterface=this;
                if(types$19===undefined){types$19=getEmpty();}
                throw Exception();
            };$$openInterface.memberApply.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Member$model,a:{Type:'Container',Kind:'Kind'}},$ps:[{$nm:'types',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element:{t:Type$model}}}}],$cont:OpenInterface,$tp:{Container:{},Kind:{'satisfies':[{t:ClassOrInterface$model,a:{Type:{t:Anything}}}]}},$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','InterfaceDeclaration','$m','memberApply']};};
            
            //MethodDeclaration memberDeclarations at caca.ceylon (78:2-79:66)
            $$openInterface.memberDeclarations=function ($$$mptypes){
                var $$openInterface=this;
                return getEmpty();
            };
            $$openInterface.memberDeclarations.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:'Kind'}},$ps:[],$cont:OpenInterface,$tp:{Kind:{'satisfies':[{t:TopLevelOrMemberDeclaration$model$declaration}]}},$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','InterfaceDeclaration','$m','memberDeclarations']};};
            
            //MethodDeclaration annotatedMemberDeclarations at caca.ceylon (80:2-81:66)
            $$openInterface.annotatedMemberDeclarations=function ($$$mptypes){
                var $$openInterface=this;
                return getEmpty();
            };
            $$openInterface.annotatedMemberDeclarations.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:'Kind'}},$ps:[],$cont:OpenInterface,$tp:{Kind:{'satisfies':[{t:TopLevelOrMemberDeclaration$model$declaration}]},Annotation:{}},$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','InterfaceDeclaration','$m','annotatedMemberDeclarations']};};
            
           
            //AttributeGetterDefinition openType at caca.ceylon (86:2-86:43)
            defineAttr($$openInterface,'openType',function(){
                var $$openInterface=this;
                throw Exception();
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:OpenType$model$declaration},$cont:OpenInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','InterfaceDeclaration','$at','openType']};});
            //AttributeDeclaration typeParameterDeclarations at caca.ceylon (87:2-87:63)
            defineAttr($$openInterface,'typeParameterDeclarations',function(){
                var $$openInterface=this;
                return getEmpty();
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:{t:TypeParameter$model$declaration}}},$cont:OpenInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','InterfaceDeclaration','$at','typeParameterDeclarations']};});
            
            //MethodDeclaration getTypeParameterDeclaration at caca.ceylon (88:2-88:79)
            $$openInterface.getTypeParameterDeclaration=function (name$21){
                var $$openInterface=this;
                return null;
            };
            $$openInterface.getTypeParameterDeclaration.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:TypeParameter$model$declaration}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}}],$cont:OpenInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','InterfaceDeclaration','$m','getTypeParameterDeclaration']};};
            
            //AttributeDeclaration superclassDeclaration at caca.ceylon (90:2-90:86)
            defineAttr($$openInterface,'superclassDeclaration',function(){
                var $$openInterface=this;
                return null;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:OpenParameterisedType$model$declaration,a:{DeclarationType:{t:ClassDeclaration$model$declaration}}}]},$cont:OpenInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','InterfaceDeclaration','$at','superclassDeclaration']};});
            
            //AttributeDeclaration interfaceDeclarations at caca.ceylon (91:2-91:89)
            defineAttr($$openInterface,'interfaceDeclarations',function(){
                var $$openInterface=this;
                return getEmpty();
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:{t:OpenParameterisedType$model$declaration,a:{DeclarationType:{t:InterfaceDeclaration$model$declaration}}}}},$cont:OpenInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','InterfaceDeclaration','$at','interfaceDeclarations']};});
            defineAttr($$openInterface,'name',function(){return this.name_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},$cont:OpenInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','InterfaceDeclaration','$at','name']};});
            defineAttr($$openInterface,'packageContainer',function(){return this.packageContainer_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Package$model$declaration},$cont:OpenInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','InterfaceDeclaration','$at','packageContainer']};});
            defineAttr($$openInterface,'toplevel',function(){return this.toplevel_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:OpenInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','InterfaceDeclaration','$at','toplevel']};});

    defineAttr($$openInterface,'qualifiedName',function(){
       if (this.toplevel) {
         return String$(this.packageContainer.name + "::" + this.name);
       } else {
         var qn = this.packageContainer.name + "::";
         for (var i=1; i<this.tipo.$$metamodel$$.d.length;i++) {
           var part = this.tipo.$$metamodel$$.d[i];
           qn += part[0]=='$'?'.':part;
         }
         return String$(qn);
       }
    },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},$cont:OpenInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.model.declaration','Declaration','$at','qualifiedName']};});
        })(OpenInterface.$$.prototype);
    }
    return OpenInterface;
}
exports.$init$OpenInterface=$init$OpenInterface;
$init$OpenInterface();

