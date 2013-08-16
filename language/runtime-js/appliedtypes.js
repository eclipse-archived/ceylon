function AppliedClass(tipo,$$targs$$,that){
    $init$AppliedClass();
    if (that===undefined)that=new AppliedClass.$$;
    set_type_args(that,$$targs$$);
    Class$model($$targs$$,that);
    that.tipo=tipo;
    return that;
}
AppliedClass.$$metamodel$$={mod:$$METAMODEL$$,'super':{t:Basic},$tp:{Type:{'var':'out',},A:{'var':'in','satisfies':[{t:Sequential,a:{Element:{t:Anything}}}]}},satisfies:[{t:Class$model,a:{Arguments:'A',Type:'Type'}}],d:['ceylon.language.model','Class']};
function $init$AppliedClass(){
    if (AppliedClass.$$===undefined){
        initTypeProto(AppliedClass,'ceylon.language.model:: AppliedClass',Basic,Class$model);
        (function($$clase){
            
            //declaration
            defineAttr($$clase,'declaration',function(){
              var $$clase=this;
              if ($$clase._decl)return $$clase._decl;
              throw Exception(String$("decl",4));
            });
            //superclass
            defineAttr($$clase,'superclass',function(){
                var $$clase=this;
                throw Exception(String$("super",5));
            });
            //interfaces
            defineAttr($$clase,'interfaces',function(){
                var $$clase=this;
                throw Exception(String$("ifaces",6));
            });

            $$clase.getFunction=function getFunction(name$2,types$3,$$$mptypes){
                var $$clase=this;
                if(types$3===undefined){types$3=getEmpty();}
                throw Exception(String$("func",4));
            };//$$clase.getFunction.$$metamodel$$={mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:Member$metamodel,a:{Type:'SubType',Kind:'Kind'}}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}},{$nm:'types',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element:{t:Type$metamodel}}}}],$tp:{SubType:{},Kind:{'satisfies':[{t:Function$metamodel,a:{Arguments:{t:Nothing},Type:{t:Anything}}}]}},$an:function(){return[shared(),actual()];},pkg:'ceylon.language.metamodel',d:['ceylon.language.metamodel','ClassOrInterface','$m','getFunction']};

            $$clase.getClassOrInterface=function getClassOrInterface(name$4,types$5,$$$mptypes){
                var $$clase=this;
                if(types$5===undefined){types$5=getEmpty();}
                throw Exception(String$("class/iface",11));
            };//$$clase.getClassOrInterface.$$metamodel$$={mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:Member$metamodel,a:{Type:'SubType',Kind:'Kind'}}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}},{$nm:'types',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element:{t:Type$metamodel}}}}],$tp:{SubType:{},Kind:{'satisfies':[{t:ClassOrInterface$metamodel,a:{Type:{t:Anything}}}]}},$an:function(){return[shared(),actual()];},pkg:'ceylon.language.metamodel',d:['ceylon.language.metamodel','ClassOrInterface','$m','getClassOrInterface']};

            $$clase.getAttribute=function getAttribute(name$6,$$$mptypes){
                var $$clase=this;
                throw Exception(String$("attrib",6));
            };//$$clase.getAttribute.$$metamodel$$={mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:Member$metamodel,a:{Type:'SubType',Kind:'Kind'}}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}}],$tp:{SubType:{},Kind:{'satisfies':[{t:Attribute$metamodel,a:{Type:{t:Anything}}}]}},$an:function(){return[shared(),actual()];},pkg:'ceylon.language.metamodel',d:['ceylon.language.metamodel','ClassOrInterface','$m','getAttribute']};

            defineAttr($$clase,'typeArguments',function(){
                var $$clase=this;
                throw Exception(String$("type args",9));
            });
        })(AppliedClass.$$.prototype);
    }
    return AppliedClass;
}
exports.AppliedClass$model=$init$AppliedClass;
$init$AppliedClass();

function AppliedInterface(tipo,$$targs$$,$$interfaz){
    $init$AppliedInterface();
    if ($$interfaz===undefined)$$interfaz=new AppliedInterface.$$;
    set_type_args($$interfaz,$$targs$$);
    Interface$model($$targs$$,$$interfaz);
    
    //AttributeGetterDefinition declaration at test.ceylon (5:4-5:78)
    defineAttr($$interfaz,'declaration',function() {
        throw Exception(String$("decl",4));
    });
    
    //AttributeGetterDefinition superclass at test.ceylon (6:4-6:83)
    defineAttr($$interfaz,'superclass',function() {
        throw Exception(String$("super",5));
    });
    
    //AttributeGetterDefinition interfaces at test.ceylon (7:4-7:80)
    defineAttr($$interfaz,'interfaces',function() {
        throw Exception(String$("ifaces",6));
    });
    
    //MethodDefinition getFunction at test.ceylon (8:4-9:83)
    function getFunction(name$2,types$3,$$$mptypes){
        if(types$3===undefined){types$3=getEmpty();}
        throw Exception(String$("func",4));
    }
    $$interfaz.getFunction=getFunction;
    getFunction.$$metamodel$$={mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:Member$model,a:{Type:'SubType',Kind:'Kind'}}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}},{$nm:'types',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element:{t:Type$model}}}}],$tp:{SubType:{},Kind:{'satisfies':[{t:Function$model,a:{Arguments:{t:Nothing},Type:{t:Anything}}}]}},$an:function(){return[shared(),actual()];},d:['ceylon.language.model','ClassOrInterface','$m','getFunction']};//getFunction.$$targs$$={Arguments:{t:Tuple,a:{Rest:{t:Empty},First:{t:Sequential,a:{Element:{t:Type$metamodel}}},Element:{t:Sequential,a:{Element:{t:Type$metamodel}}}}},Return:{ t:'u', l:[{t:Null},{t:Member$metamodel,a:{Type:$$$mptypes.SubType,Kind:$$$mptypes.Kind}}]}};
    
    //MethodDefinition getClassOrInterface at test.ceylon (10:4-11:89)
    function getClassOrInterface(name$4,types$5,$$$mptypes){
        if(types$5===undefined){types$5=getEmpty();}
        throw Exception(String$("class/iface",11));
    }
    $$interfaz.getClassOrInterface=getClassOrInterface;
    getClassOrInterface.$$metamodel$$={mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:Member$model,a:{Type:'SubType',Kind:'Kind'}}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}},{$nm:'types',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element:{t:Type$model}}}}],$tp:{SubType:{},Kind:{'satisfies':[{t:ClassOrInterface$model,a:{Type:{t:Anything}}}]}},$an:function(){return[shared(),actual()];},d:['ceylon.language.model','ClassOrInterface','$m','getClassOrInterface']};//getClassOrInterface.$$targs$$={Arguments:{t:Tuple,a:{Rest:{t:Empty},First:{t:Sequential,a:{Element:{t:Type$metamodel}}},Element:{t:Sequential,a:{Element:{t:Type$metamodel}}}}},Return:{ t:'u', l:[{t:Null},{t:Member$metamodel,a:{Type:$$$mptypes.SubType,Kind:$$$mptypes.Kind}}]}};
    
    //MethodDefinition getAttribute at test.ceylon (12:4-13:77)
    function getAttribute(name$6,$$$mptypes){
        throw Exception(String$("attrib",6));
    }
    $$interfaz.getAttribute=getAttribute;
    getAttribute.$$metamodel$$={mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:Member$model,a:{Type:'SubType',Kind:'Kind'}}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}}],$tp:{SubType:{},Kind:{'satisfies':[{t:Attribute$model,a:{Type:{t:Anything}}}]}},$an:function(){return[shared(),actual()];},d:['ceylon.language.model','ClassOrInterface','$m','getAttribute']};//getAttribute.$$targs$$={Arguments:{t:Tuple,a:{Rest:{t:Empty},First:{t:String$},Element:{t:String$}}},Return:{ t:'u', l:[{t:Null},{t:Member$metamodel,a:{Type:$$$mptypes.SubType,Kind:$$$mptypes.Kind}}]}};
    
    //AttributeGetterDefinition typeArguments at test.ceylon (14:4-14:89)
    defineAttr($$interfaz,'typeArguments',function() {
        throw Exception(String$("type args",9));
    });
    $$interfaz.tipo=tipo;
    return $$interfaz;
}
AppliedInterface.$$metamodel$$={mod:$$METAMODEL$$,'super':{t:Basic},$tp:{Type:{'var':'out',}},satisfies:[{t:Interface$model,a:{Type:'Type'}}],d:['ceylon.language.model','Interface']};
function $init$AppliedInterface(){
    if (AppliedInterface.$$===undefined){
        initTypeProto(AppliedInterface,'ceylon.language.model::AppliedInterface',Basic,Interface$model);
    }
    return AppliedInterface;
}
exports.$init$AppliedInterface$model=$init$AppliedInterface;
$init$AppliedInterface();

function AppliedUnionType(tipo,types$2, $$appliedUnionType){
    $init$AppliedUnionType();
    if ($$appliedUnionType===undefined)$$appliedUnionType=new AppliedUnionType.$$;
    $$appliedUnionType.types$2=types$2;
    UnionType$model($$appliedUnionType);
    $$appliedUnionType.tipo=tipo;
    return $$appliedUnionType;
}
AppliedUnionType.$$metamodel$$={mod:$$METAMODEL$$,'super':{t:Basic},satisfies:[{t:UnionType$model}],d:['ceylon.language.model','AppliedUnionType']};
function $init$AppliedUnionType(){
    if (AppliedUnionType.$$===undefined){
        initTypeProto(AppliedUnionType,'AppliedUnionType',Basic,UnionType$model);
        (function($$appliedUnionType){
            
            defineAttr($$appliedUnionType,'caseTypes',function(){
                var $$appliedUnionType=this;
                return $$appliedUnionType.types$2;
            });
        })(AppliedUnionType.$$.prototype);
    }
    return AppliedUnionType;
}
exports.$init$AppliedUnionType$model=$init$AppliedUnionType;
$init$AppliedUnionType();

function AppliedIntersectionType(tipo,types$3, $$appliedIntersectionType){
    $init$AppliedIntersectionType();
    if ($$appliedIntersectionType===undefined)$$appliedIntersectionType=new AppliedIntersectionType.$$;
    $$appliedIntersectionType.types$3=types$3;
    IntersectionType$model($$appliedIntersectionType);
    $$appliedIntersectionType.tipo=tipo;
    return $$appliedIntersectionType;
}
AppliedIntersectionType.$$metamodel$$={mod:$$METAMODEL$$,'super':{t:Basic},satisfies:[{t:IntersectionType$model}],d:['ceylon.language.model','AppliedIntersectionType']};
function $init$AppliedIntersectionType(){
    if (AppliedIntersectionType.$$===undefined){
        initTypeProto(AppliedIntersectionType,'AppliedIntersectionType',Basic,IntersectionType$model);
        (function($$appliedIntersectionType){
            
            defineAttr($$appliedIntersectionType,'satisfiedTypes',function(){
                var $$appliedIntersectionType=this;
                return $$appliedIntersectionType.types$3;
            });
        })(AppliedIntersectionType.$$.prototype);
    }
    return AppliedIntersectionType;
}
exports.$init$AppliedIntersectionType$model=$init$AppliedIntersectionType;
$init$AppliedIntersectionType();

function AppliedFunction(that) {
  return that;
}


function AppliedValue(attr,$$targs$$,$$appliedAttribute){
    $init$AppliedValue();
    if ($$appliedAttribute===undefined)$$appliedAttribute=new AppliedValue.$$;
    set_type_args($$appliedAttribute,$$targs$$);
    Attribute$model($$appliedAttribute.$$targs$$===undefined?$$targs$$:{Type:$$appliedAttribute.$$targs$$.Type,Container:$$appliedAttribute.$$targs$$.Container},$$appliedAttribute);
    $$appliedAttribute.attr=attr;
    return $$appliedAttribute;
}
AppliedValue.$$metamodel$$={mod:$$METAMODEL$$,'super':{t:Basic},$tp:{Container:{'var':'in',},Type:{'var':'out',}},satisfies:[{t:Attribute$model,a:{Type:'Type',Container:'Container'}}],$an:function(){return[shared()];},d:['ceylon.language.model','Attribute']};
exports.AppliedValue$model=AppliedValue;
function $init$AppliedValue(){
    if (AppliedValue.$$===undefined){
        initTypeProto(AppliedValue,'AppliedValue',Basic,Attribute$model);
        (function($$appliedAttribute){
            
            //AttributeGetterDefinition declaration at caca.ceylon (5:2-5:58)
            defineAttr($$appliedAttribute,'declaration',function(){
                var $$appliedAttribute=this;
                throw Exception();
            },undefined,{mod:$$METAMODEL$$,$an:function(){return[shared(),actual()];},d:['ceylon.language.model','AttributeModel','$at','declaration']});
            //AttributeGetterDefinition declaringClassOrInterface at caca.ceylon (6:2-6:76)
            defineAttr($$appliedAttribute,'declaringClassOrInterface',function(){
                var $$appliedAttribute=this;
                throw Exception();
            },undefined,{mod:$$METAMODEL$$,$an:function(){return[shared(),actual()];},d:['ceylon.language.model','AttributeModel','$at','declaringClassOrInterface']});
            //AttributeGetterDefinition type at caca.ceylon (7:2-7:31)
            defineAttr($$appliedAttribute,'type',function(){
                var $$appliedAttribute=this;
                throw Exception();
            },undefined,{mod:$$METAMODEL$$,$an:function(){return[shared(),actual()];},d:['ceylon.language.model','AttributeModel','$at','type']});
        })(AppliedValue.$$.prototype);
    }
    return AppliedValue;
}
exports.$init$AppliedValue$model=$init$AppliedValue;
$init$AppliedValue();
