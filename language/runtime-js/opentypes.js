//Add-on to AnnotatedDeclaration            //MethodDeclaration annotations at caca.ceylon (82:2-83:72)
AnnotatedDeclaration$model$declaration.$$.prototype.annotations=function ($$$mptypes) {
    var $$openInterface=this;
    var ans = [];
    var _ans = $$openInterface._type.$$metamodel$$.$an;
    if (typeof(_ans)==='function') {
      _ans = _ans();
      $$openInterface._type.$$metamodel$$.$an=_ans;
    }
    for (var i=0; i<_ans.length;i++) {
      var __a = _ans[i]();
      if (isOfType(_ans[i], $$$mptypes.Annotation)) {
        ans.push(__a);
      }
    }
    return ans.length == 0 ? getEmpty() : ans.reifyCeylonType({Element:$$$mptypes.Annotation});
};
AnnotatedDeclaration$model$declaration.$$.prototype.annotations.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:'Annotation'}},$ps:[],$cont:AnnotatedDeclaration$model$declaration,$tp:{Annotation:{'var':'out','satisfies':[{t:Annotation$model,a:{Value:'Annotation'}}]}},$an:function(){return[shared(),formal()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['AnnotatedDeclaration']['$m']['annotations']};};

function _findTypeFromModel(mdl) {
  console.log("IMPLEMENT ME!!!!!! _findTypeFromModel (native)");
  return undefined;
}

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
      that._type=_findTypeFromModel(meta);
    } else {
      //it's a type
      that._type = meta;
      that.meta = meta.$$metamodel$$.d;
    }
    FunctionDeclaration$model$declaration(that);
    return that;
}
OpenFunction.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Basic},satisfies:[{t:FunctionDeclaration$model$declaration}],pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['FunctionDeclaration']};};
function $init$OpenFunction(){
    if (OpenFunction.$$===undefined){
        initTypeProto(OpenFunction,'OpenFunction',Basic,FunctionDeclaration$model$declaration);
        (function($$openFunction){
            
            //MethodDefinition apply at caca.ceylon (20:4-20:81)
            $$openFunction.apply=function apply(types$2){
                var $$openFunction=this;
                if(types$2===undefined){types$2=getEmpty();}
                throw Exception();
            };$$openFunction.apply.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Function$model,a:{Arguments:{t:Nothing},Type:{t:Anything}}},$ps:[{$nm:'types',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element:{t:Type$model}}}}],$cont:OpenFunction,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['FunctionDeclaration']['$m']['apply']};};
            
            //MethodDefinition bindAndApply at caca.ceylon (21:4-21:105)
            $$openFunction.bindAndApply=function bindAndApply(instance$3,types$4){
                var $$openFunction=this;
                if(types$4===undefined){types$4=getEmpty();}
                throw Exception();
            };$$openFunction.bindAndApply.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Function$model,a:{Arguments:{t:Nothing},Type:{t:Anything}}},$ps:[{$nm:'instance',$mt:'prm',$t:{t:Object$}},{$nm:'types',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element:{t:Type$model}}}}],$cont:OpenFunction,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['FunctionDeclaration']['$m']['bindAndApply']};};
            
            //MethodDefinition memberApply at caca.ceylon (22:4-23:54)
            $$openFunction.memberApply=function memberApply(types$5,$$$mptypes){
                var $$openFunction=this;
                if(types$5===undefined){types$5=getEmpty();}
                throw Exception();
            };$$openFunction.memberApply.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Method$model,a:{Arguments:'Arguments',Type:'MethodType',Container:'Container'}},$ps:[{$nm:'types',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element:{t:Type$model}}}}],$cont:OpenFunction,$tp:{Container:{},MethodType:{},Arguments:{'satisfies':[{t:Sequential,a:{Element:{t:Anything}}}]}},$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['FunctionDeclaration']['$m']['memberApply']};};
            
            //AttributeDeclaration defaulted at caca.ceylon (25:4-25:44)
            defineAttr($$openFunction,'defaulted',function(){
                var $$openFunction=this;
                return false;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:OpenFunction,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['FunctionDeclaration']['$at']['defaulted']};});
            
            //AttributeDeclaration variadic at caca.ceylon (26:4-26:43)
            defineAttr($$openFunction,'variadic',function(){
                var $$openFunction=this;
                return false;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:OpenFunction,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['FunctionDeclaration']['$at']['variadic']};});
            
            //AttributeDeclaration parameterDeclarations at caca.ceylon (28:4-28:74)
            defineAttr($$openFunction,'parameterDeclarations',function(){
                var $$openFunction=this;
                return getEmpty();
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:{t:FunctionOrValueDeclaration$model$declaration}}},$cont:OpenFunction,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['FunctionDeclaration']['$at']['parameterDeclarations']};});
            
            //MethodDeclaration getParameterDeclaration at caca.ceylon (29:4-29:90)
            $$openFunction.getParameterDeclaration=function (name$6){
                var $$openFunction=this;
                return null;
            };
            $$openFunction.getParameterDeclaration.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:FunctionOrValueDeclaration$model$declaration}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}}],$cont:OpenFunction,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['FunctionDeclaration']['$m']['getParameterDeclaration']};};
            
            //AttributeGetterDefinition openType at caca.ceylon (33:2-33:43)
            defineAttr($$openFunction,'openType',function(){
                var $$openFunction=this;
                throw Exception();
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:OpenType$model$declaration},$cont:OpenFunction,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['FunctionDeclaration']['$at']['openType']};});
            //AttributeDeclaration typeParameterDeclarations at caca.ceylon (34:2-34:63)
            defineAttr($$openFunction,'typeParameterDeclarations',function(){
                var $$openFunction=this;
                return getEmpty();
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:{t:TypeParameter$model$declaration}}},$cont:OpenFunction,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['FunctionDeclaration']['$at']['typeParameterDeclarations']};});
            
            //MethodDeclaration getTypeParameterDeclaration at caca.ceylon (35:2-35:79)
            $$openFunction.getTypeParameterDeclaration=function (name$7){
                var $$openFunction=this;
                return null;
            };
            $$openFunction.getTypeParameterDeclaration.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:TypeParameter$model$declaration}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}}],$cont:OpenFunction,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['FunctionDeclaration']['$m']['getTypeParameterDeclaration']};};
            defineAttr($$openFunction,'name',function(){return this.name_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},$cont:OpenFunction,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['FunctionDeclaration']['$at']['name']};});
            defineAttr($$openFunction,'packageContainer',function(){return this.packageContainer_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Package$model$declaration},$cont:OpenFunction,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['FunctionDeclaration']['$at']['packageContainer']};});
            defineAttr($$openFunction,'toplevel',function(){return this.toplevel_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:OpenFunction,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['FunctionDeclaration']['$at']['toplevel']};});
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
      that._type=_findTypeFromModel(meta);
    } else {
      //it's a type
      that._type = meta;
      that.meta = meta.$$metamodel$$.d;
    }
    ValueDeclaration$model$declaration(that);
    return that;
}
OpenValue.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Basic},satisfies:[{t:ValueDeclaration$model$declaration}],pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['ValueDeclaration']};};
function $init$OpenValue(){
    if (OpenValue.$$===undefined){
        initTypeProto(OpenValue,'OpenValue',Basic,ValueDeclaration$model$declaration);
        (function($$openValue){
            
            //MethodDefinition apply at caca.ceylon (39:4-39:68)
            $$openValue.apply=function apply(instance$8){
                var $$openValue=this;
                if(instance$8===undefined){instance$8=$$openValue.apply$defs$instance(instance$8);}
                throw Exception();
            };$$openValue.apply.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Value$model,a:{Type:{t:Anything}}},$ps:[{$nm:'instance',$mt:'prm',$def:1,$t:{t:Anything}}],$cont:OpenValue,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['ValueDeclaration']['$m']['apply']};};
            
            //AttributeDeclaration defaulted at caca.ceylon (40:4-40:44)
            defineAttr($$openValue,'defaulted',function(){
                var $$openValue=this;
                return false;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:OpenValue,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['ValueDeclaration']['$at']['defaulted']};});
            
            //AttributeDeclaration variadic at caca.ceylon (41:4-41:43)
            defineAttr($$openValue,'variadic',function(){
                var $$openValue=this;
                return false;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:OpenValue,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['ValueDeclaration']['$at']['variadic']};});
            
            //AttributeGetterDefinition openType at caca.ceylon (44:2-44:43)
            defineAttr($$openValue,'openType',function(){
                var $$openValue=this;
                throw Exception();
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:OpenType$model$declaration},$cont:OpenValue,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['ValueDeclaration']['$at']['openType']};});defineAttr($$openValue,'name',function(){return this.name_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},$cont:OpenValue,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['ValueDeclaration']['$at']['name']};});
            defineAttr($$openValue,'packageContainer',function(){return this.packageContainer_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Package$model$declaration},$cont:OpenValue,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['ValueDeclaration']['$at']['packageContainer']};});
            defineAttr($$openValue,'toplevel',function(){return this.toplevel_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:OpenValue,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['ValueDeclaration']['$at']['toplevel']};});
        })(OpenValue.$$.prototype);
    }
    return OpenValue;
}
exports.$init$OpenValue=$init$OpenValue;
$init$OpenValue();

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
      that._type=_findTypeFromModel(meta);
    } else {
      //it's a type
      that._type = meta;
      that.meta = meta.$$metamodel$$.d;
    }
    ClassDeclaration$model$declaration(that);
    return that;
}
OpenClass.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Basic},satisfies:[{t:ClassDeclaration$model$declaration}],pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['ClassDeclaration']};};
function $init$OpenClass(){
    if (OpenClass.$$===undefined){
        initTypeProto(OpenClass,'OpenClass',Basic,ClassDeclaration$model$declaration);
        (function($$openClass){
            
            //AttributeDeclaration anonymous at caca.ceylon (48:2-48:42)
            defineAttr($$openClass,'anonymous',function(){
                var $$openClass=this;
                return false;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:OpenClass,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['ClassDeclaration']['$at']['anonymous']};});
            
            //MethodDefinition apply at caca.ceylon (49:2-49:76)
            $$openClass.apply=function apply(types$9){
                var $$openClass=this;
                if(types$9===undefined){types$9=getEmpty();}
                throw Exception();
            };$$openClass.apply.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Class$model,a:{Arguments:{t:Nothing},Type:{t:Anything}}},$ps:[{$nm:'types',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element:{t:Type$model}}}}],$cont:OpenClass,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['ClassDeclaration']['$m']['apply']};};
            
            //MethodDefinition bindAndApply at caca.ceylon (50:2-50:100)
            $$openClass.bindAndApply=function bindAndApply(instance$10,types$11){
                var $$openClass=this;
                if(types$11===undefined){types$11=getEmpty();}
                throw Exception();
            };$$openClass.bindAndApply.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Class$model,a:{Arguments:{t:Nothing},Type:{t:Anything}}},$ps:[{$nm:'instance',$mt:'prm',$t:{t:Object$}},{$nm:'types',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element:{t:Type$model}}}}],$cont:OpenClass,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['ClassDeclaration']['$m']['bindAndApply']};};
            
            //MethodDefinition memberApply at caca.ceylon (51:2-52:72)
            $$openClass.memberApply=function memberApply(types$12,$$$mptypes){
                var $$openClass=this;
                if(types$12===undefined){types$12=getEmpty();}
                throw Exception();
            };$$openClass.memberApply.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Member$model,a:{Type:'Container',Kind:'Kind'}},$ps:[{$nm:'types',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element:{t:Type$model}}}}],$cont:OpenClass,$tp:{Container:{},Kind:{'satisfies':[{t:ClassOrInterface$model,a:{Type:{t:Anything}}}]}},$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['ClassDeclaration']['$m']['memberApply']};};
            
            //MethodDeclaration memberDeclarations at caca.ceylon (53:2-54:66)
            $$openClass.memberDeclarations=function ($$$mptypes){
                var $$openClass=this;
                return getEmpty();
            };
            $$openClass.memberDeclarations.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:'Kind'}},$ps:[],$cont:OpenClass,$tp:{Kind:{'satisfies':[{t:TopLevelOrMemberDeclaration$model$declaration}]}},$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['ClassDeclaration']['$m']['memberDeclarations']};};
            
            //MethodDeclaration annotatedMemberDeclarations at caca.ceylon (55:2-56:66)
            $$openClass.annotatedMemberDeclarations=function ($$$mptypes){
                var $$openClass=this;
                return getEmpty();
            };
            $$openClass.annotatedMemberDeclarations.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:'Kind'}},$ps:[],$cont:OpenClass,$tp:{Kind:{'satisfies':[{t:TopLevelOrMemberDeclaration$model$declaration}]},Annotation:{}},$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['ClassDeclaration']['$m']['annotatedMemberDeclarations']};};
            
            //MethodDeclaration getMemberDeclaration at caca.ceylon (59:2-60:68)
            $$openClass.getMemberDeclaration=function (name$13,$$$mptypes){
                var $$openClass=this;
                return null;
            };
            $$openClass.getMemberDeclaration.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},'Kind']},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}}],$cont:OpenClass,$tp:{Kind:{'satisfies':[{t:TopLevelOrMemberDeclaration$model$declaration}]}},$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['ClassDeclaration']['$m']['getMemberDeclaration']};};
            
            //AttributeGetterDefinition openType at caca.ceylon (61:2-61:43)
            defineAttr($$openClass,'openType',function(){
                var $$openClass=this;
                throw Exception();
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:OpenType$model$declaration},$cont:OpenClass,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['ClassDeclaration']['$at']['openType']};});
            //AttributeDeclaration typeParameterDeclarations at caca.ceylon (62:2-62:63)
            defineAttr($$openClass,'typeParameterDeclarations',function(){
                var $$openClass=this;
                return getEmpty();
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:{t:TypeParameter$model$declaration}}},$cont:OpenClass,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['ClassDeclaration']['$at']['typeParameterDeclarations']};});
            
            //MethodDeclaration getTypeParameterDeclaration at caca.ceylon (63:2-63:79)
            $$openClass.getTypeParameterDeclaration=function (name$14){
                var $$openClass=this;
                return null;
            };
            $$openClass.getTypeParameterDeclaration.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:TypeParameter$model$declaration}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}}],$cont:OpenClass,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['ClassDeclaration']['$m']['getTypeParameterDeclaration']};};
            
            //AttributeDeclaration parameterDeclarations at caca.ceylon (65:2-65:72)
            defineAttr($$openClass,'parameterDeclarations',function(){
                var $$openClass=this;
                return getEmpty();
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:{t:FunctionOrValueDeclaration$model$declaration}}},$cont:OpenClass,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['ClassDeclaration']['$at']['parameterDeclarations']};});
            
            //MethodDeclaration getParameterDeclaration at caca.ceylon (66:2-66:88)
            $$openClass.getParameterDeclaration=function (name$15){
                var $$openClass=this;
                return null;
            };
            $$openClass.getParameterDeclaration.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:FunctionOrValueDeclaration$model$declaration}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}}],$cont:OpenClass,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['ClassDeclaration']['$m']['getParameterDeclaration']};};
            
            //AttributeDeclaration superclassDeclaration at caca.ceylon (68:2-68:86)
            defineAttr($$openClass,'superclassDeclaration',function(){
                var $$openClass=this;
                return null;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:OpenParameterisedType$model$declaration,a:{DeclarationType:{t:ClassDeclaration$model$declaration}}}]},$cont:OpenClass,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['ClassDeclaration']['$at']['superclassDeclaration']};});
            
            //AttributeDeclaration interfaceDeclarations at caca.ceylon (69:2-69:89)
            defineAttr($$openClass,'interfaceDeclarations',function(){
                var $$openClass=this;
                return getEmpty();
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:{t:OpenParameterisedType$model$declaration,a:{DeclarationType:{t:InterfaceDeclaration$model$declaration}}}}},$cont:OpenClass,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['ClassDeclaration']['$at']['interfaceDeclarations']};});
            defineAttr($$openClass,'name',function(){return this.name_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},$cont:OpenClass,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['ClassDeclaration']['$at']['name']};});
            defineAttr($$openClass,'packageContainer',function(){return this.packageContainer_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Package$model$declaration},$cont:OpenClass,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['ClassDeclaration']['$at']['packageContainer']};});
            defineAttr($$openClass,'toplevel',function(){return this.toplevel_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:OpenClass,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['ClassDeclaration']['$at']['toplevel']};});
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
      that._type=_findTypeFromModel(meta);
    } else {
      //it's a type
      that._type = meta;
      that.meta = meta.$$metamodel$$.d;
    }
    InterfaceDeclaration$model$declaration(that);
    return that;
}
OpenInterface.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Basic},satisfies:[{t:InterfaceDeclaration$model$declaration}],pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['InterfaceDeclaration']};};
function $init$OpenInterface(){
    if (OpenInterface.$$===undefined){
        initTypeProto(OpenInterface,'OpenInterface',Basic,InterfaceDeclaration$model$declaration);
        (function($$openInterface){
            
            //MethodDefinition apply at caca.ceylon (73:2-73:71)
            $$openInterface.apply=function apply(types$16){
                var $$openInterface=this;
                if(types$16===undefined){types$16=getEmpty();}
                throw Exception();
            };$$openInterface.apply.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Interface$model,a:{Type:{t:Anything}}},$ps:[{$nm:'types',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element:{t:Type$model}}}}],$cont:OpenInterface,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['InterfaceDeclaration']['$m']['apply']};};
            
            //MethodDefinition bindAndApply at caca.ceylon (74:2-74:95)
            $$openInterface.bindAndApply=function bindAndApply(instance$17,types$18){
                var $$openInterface=this;
                if(types$18===undefined){types$18=getEmpty();}
                throw Exception();
            };$$openInterface.bindAndApply.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Interface$model,a:{Type:{t:Anything}}},$ps:[{$nm:'instance',$mt:'prm',$t:{t:Object$}},{$nm:'types',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element:{t:Type$model}}}}],$cont:OpenInterface,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['InterfaceDeclaration']['$m']['bindAndApply']};};
            
            //MethodDefinition memberApply at caca.ceylon (75:2-76:72)
            $$openInterface.memberApply=function memberApply(types$19,$$$mptypes){
                var $$openInterface=this;
                if(types$19===undefined){types$19=getEmpty();}
                throw Exception();
            };$$openInterface.memberApply.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Member$model,a:{Type:'Container',Kind:'Kind'}},$ps:[{$nm:'types',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element:{t:Type$model}}}}],$cont:OpenInterface,$tp:{Container:{},Kind:{'satisfies':[{t:ClassOrInterface$model,a:{Type:{t:Anything}}}]}},$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['InterfaceDeclaration']['$m']['memberApply']};};
            
            //MethodDeclaration memberDeclarations at caca.ceylon (78:2-79:66)
            $$openInterface.memberDeclarations=function ($$$mptypes){
                var $$openInterface=this;
                return getEmpty();
            };
            $$openInterface.memberDeclarations.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:'Kind'}},$ps:[],$cont:OpenInterface,$tp:{Kind:{'satisfies':[{t:TopLevelOrMemberDeclaration$model$declaration}]}},$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['InterfaceDeclaration']['$m']['memberDeclarations']};};
            
            //MethodDeclaration annotatedMemberDeclarations at caca.ceylon (80:2-81:66)
            $$openInterface.annotatedMemberDeclarations=function ($$$mptypes){
                var $$openInterface=this;
                return getEmpty();
            };
            $$openInterface.annotatedMemberDeclarations.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:'Kind'}},$ps:[],$cont:OpenInterface,$tp:{Kind:{'satisfies':[{t:TopLevelOrMemberDeclaration$model$declaration}]},Annotation:{}},$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['InterfaceDeclaration']['$m']['annotatedMemberDeclarations']};};
            
           
            //MethodDeclaration getMemberDeclaration at caca.ceylon (84:2-85:68)
            $$openInterface.getMemberDeclaration=function (name$20,$$$mptypes){
                var $$openInterface=this;
                return null;
            };
            $$openInterface.getMemberDeclaration.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},'Kind']},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}}],$cont:OpenInterface,$tp:{Kind:{'satisfies':[{t:TopLevelOrMemberDeclaration$model$declaration}]}},$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['InterfaceDeclaration']['$m']['getMemberDeclaration']};};
            
            //AttributeGetterDefinition openType at caca.ceylon (86:2-86:43)
            defineAttr($$openInterface,'openType',function(){
                var $$openInterface=this;
                throw Exception();
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:OpenType$model$declaration},$cont:OpenInterface,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['InterfaceDeclaration']['$at']['openType']};});
            //AttributeDeclaration typeParameterDeclarations at caca.ceylon (87:2-87:63)
            defineAttr($$openInterface,'typeParameterDeclarations',function(){
                var $$openInterface=this;
                return getEmpty();
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:{t:TypeParameter$model$declaration}}},$cont:OpenInterface,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['InterfaceDeclaration']['$at']['typeParameterDeclarations']};});
            
            //MethodDeclaration getTypeParameterDeclaration at caca.ceylon (88:2-88:79)
            $$openInterface.getTypeParameterDeclaration=function (name$21){
                var $$openInterface=this;
                return null;
            };
            $$openInterface.getTypeParameterDeclaration.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:TypeParameter$model$declaration}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}}],$cont:OpenInterface,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['InterfaceDeclaration']['$m']['getTypeParameterDeclaration']};};
            
            //AttributeDeclaration superclassDeclaration at caca.ceylon (90:2-90:86)
            defineAttr($$openInterface,'superclassDeclaration',function(){
                var $$openInterface=this;
                return null;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:OpenParameterisedType$model$declaration,a:{DeclarationType:{t:ClassDeclaration$model$declaration}}}]},$cont:OpenInterface,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['InterfaceDeclaration']['$at']['superclassDeclaration']};});
            
            //AttributeDeclaration interfaceDeclarations at caca.ceylon (91:2-91:89)
            defineAttr($$openInterface,'interfaceDeclarations',function(){
                var $$openInterface=this;
                return getEmpty();
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:{t:OpenParameterisedType$model$declaration,a:{DeclarationType:{t:InterfaceDeclaration$model$declaration}}}}},$cont:OpenInterface,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['InterfaceDeclaration']['$at']['interfaceDeclarations']};});
            defineAttr($$openInterface,'name',function(){return this.name_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},$cont:OpenInterface,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['InterfaceDeclaration']['$at']['name']};});
            defineAttr($$openInterface,'packageContainer',function(){return this.packageContainer_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Package$model$declaration},$cont:OpenInterface,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['InterfaceDeclaration']['$at']['packageContainer']};});
            defineAttr($$openInterface,'toplevel',function(){return this.toplevel_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:OpenInterface,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['InterfaceDeclaration']['$at']['toplevel']};});
        })(OpenInterface.$$.prototype);
    }
    return OpenInterface;
}
exports.$init$OpenInterface=$init$OpenInterface;
$init$OpenInterface();
