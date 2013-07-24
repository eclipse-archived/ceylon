function AppliedClass$metamodel(tipo,$$targs$$,that){
    $init$AppliedClass$metamodel();
    if (that===undefined)that=new AppliedClass$metamodel.$$;
    set_type_args(that,$$targs$$);
    Class$metamodel($$targs$$,that);
    that.tipo=tipo;
    return that;
}
AppliedClass$metamodel.$$metamodel$$={mod:$$METAMODEL$$,'super':{t:Basic},$tp:{T:{'var':'out',},A:{'var':'in','satisfies':[{t:Sequential,a:{Element:{t:Anything}}}]}},satisfies:[{t:Class$metamodel,a:{Arguments:'A',Type:'T'}}],pkg:'ceylon.language.metamodel',d:$$METAMODEL$$['ceylon.language.metamodel']['Class']};
function $init$AppliedClass$metamodel(){
    if (AppliedClass$metamodel.$$===undefined){
        initTypeProto(AppliedClass$metamodel,'ceylon.language.metamodel:: AppliedClass',Basic,Class$metamodel);
        (function($$clase){
            
            //declaration
            defineAttr($$clase,'declaration',function(){
                var $$clase=this;
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
            };//$$clase.getFunction.$$metamodel$$={mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:Member$metamodel,a:{Type:'SubType',Kind:'Kind'}}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}},{$nm:'types',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element:{t:Type$metamodel}}}}],$tp:{SubType:{},Kind:{'satisfies':[{t:Function$metamodel,a:{Arguments:{t:Nothing},Type:{t:Anything}}}]}},$an:function(){return[shared(),actual()];},pkg:'ceylon.language.metamodel',d:$$METAMODEL$$['ceylon.language.metamodel']['ClassOrInterface']['$m']['getFunction']};

            $$clase.getClassOrInterface=function getClassOrInterface(name$4,types$5,$$$mptypes){
                var $$clase=this;
                if(types$5===undefined){types$5=getEmpty();}
                throw Exception(String$("class/iface",11));
            };//$$clase.getClassOrInterface.$$metamodel$$={mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:Member$metamodel,a:{Type:'SubType',Kind:'Kind'}}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}},{$nm:'types',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element:{t:Type$metamodel}}}}],$tp:{SubType:{},Kind:{'satisfies':[{t:ClassOrInterface$metamodel,a:{Type:{t:Anything}}}]}},$an:function(){return[shared(),actual()];},pkg:'ceylon.language.metamodel',d:$$METAMODEL$$['ceylon.language.metamodel']['ClassOrInterface']['$m']['getClassOrInterface']};

            $$clase.getAttribute=function getAttribute(name$6,$$$mptypes){
                var $$clase=this;
                throw Exception(String$("attrib",6));
            };//$$clase.getAttribute.$$metamodel$$={mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:Member$metamodel,a:{Type:'SubType',Kind:'Kind'}}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}}],$tp:{SubType:{},Kind:{'satisfies':[{t:Attribute$metamodel,a:{Type:{t:Anything}}}]}},$an:function(){return[shared(),actual()];},pkg:'ceylon.language.metamodel',d:$$METAMODEL$$['ceylon.language.metamodel']['ClassOrInterface']['$m']['getAttribute']};

            defineAttr($$clase,'typeArguments',function(){
                var $$clase=this;
                throw Exception(String$("type args",9));
            });
        })(AppliedClass$metamodel.$$.prototype);
    }
    return AppliedClass$metamodel;
}
exports.AppliedClass$metamodel=$init$AppliedClass$metamodel;
$init$AppliedClass$metamodel();

function AppliedInterface$metamodel($$targs$$,$$interfaz){
    $init$AppliedInterface$metamodel();
    if ($$interfaz===undefined)$$interfaz=new AppliedInterface$metamodel.$$;
    set_type_args($$interfaz,$$targs$$);
    Interface$metamodel($$targs$$,$$interfaz);
    
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
    getFunction.$$metamodel$$={mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:Member$metamodel,a:{Type:'SubType',Kind:'Kind'}}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}},{$nm:'types',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element:{t:Type$metamodel}}}}],$tp:{SubType:{},Kind:{'satisfies':[{t:Function$metamodel,a:{Arguments:{t:Nothing},Type:{t:Anything}}}]}},$an:function(){return[shared(),actual()];},pkg:'ceylon.language.metamodel',d:$$METAMODEL$$['ceylon.language.metamodel']['ClassOrInterface']['$m']['getFunction']};//getFunction.$$targs$$={Arguments:{t:Tuple,a:{Rest:{t:Empty},First:{t:Sequential,a:{Element:{t:Type$metamodel}}},Element:{t:Sequential,a:{Element:{t:Type$metamodel}}}}},Return:{ t:'u', l:[{t:Null},{t:Member$metamodel,a:{Type:$$$mptypes.SubType,Kind:$$$mptypes.Kind}}]}};
    
    //MethodDefinition getClassOrInterface at test.ceylon (10:4-11:89)
    function getClassOrInterface(name$4,types$5,$$$mptypes){
        if(types$5===undefined){types$5=getEmpty();}
        throw Exception(String$("class/iface",11));
    }
    $$interfaz.getClassOrInterface=getClassOrInterface;
    getClassOrInterface.$$metamodel$$={mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:Member$metamodel,a:{Type:'SubType',Kind:'Kind'}}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}},{$nm:'types',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element:{t:Type$metamodel}}}}],$tp:{SubType:{},Kind:{'satisfies':[{t:ClassOrInterface$metamodel,a:{Type:{t:Anything}}}]}},$an:function(){return[shared(),actual()];},pkg:'ceylon.language.metamodel',d:$$METAMODEL$$['ceylon.language.metamodel']['ClassOrInterface']['$m']['getClassOrInterface']};//getClassOrInterface.$$targs$$={Arguments:{t:Tuple,a:{Rest:{t:Empty},First:{t:Sequential,a:{Element:{t:Type$metamodel}}},Element:{t:Sequential,a:{Element:{t:Type$metamodel}}}}},Return:{ t:'u', l:[{t:Null},{t:Member$metamodel,a:{Type:$$$mptypes.SubType,Kind:$$$mptypes.Kind}}]}};
    
    //MethodDefinition getAttribute at test.ceylon (12:4-13:77)
    function getAttribute(name$6,$$$mptypes){
        throw Exception(String$("attrib",6));
    }
    $$interfaz.getAttribute=getAttribute;
    getAttribute.$$metamodel$$={mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:Member$metamodel,a:{Type:'SubType',Kind:'Kind'}}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}}],$tp:{SubType:{},Kind:{'satisfies':[{t:Attribute$metamodel,a:{Type:{t:Anything}}}]}},$an:function(){return[shared(),actual()];},pkg:'ceylon.language.metamodel',d:$$METAMODEL$$['ceylon.language.metamodel']['ClassOrInterface']['$m']['getAttribute']};//getAttribute.$$targs$$={Arguments:{t:Tuple,a:{Rest:{t:Empty},First:{t:String$},Element:{t:String$}}},Return:{ t:'u', l:[{t:Null},{t:Member$metamodel,a:{Type:$$$mptypes.SubType,Kind:$$$mptypes.Kind}}]}};
    
    //AttributeGetterDefinition typeArguments at test.ceylon (14:4-14:89)
    defineAttr($$interfaz,'typeArguments',function() {
        throw Exception(String$("type args",9));
    });
    return $$interfaz;
}
AppliedInterface$metamodel.$$metamodel$$={mod:$$METAMODEL$$,'super':{t:Basic},$tp:{T:{'var':'out',}},satisfies:[{t:Interface$metamodel,a:{Type:'T'}}],pkg:'ceylon.language.metamodel',d:$$METAMODEL$$['ceylon.language.metamodel']['Interface']};
function $init$AppliedInterface$metamodel(){
    if (AppliedInterface$metamodel.$$===undefined){
        initTypeProto(AppliedInterface$metamodel,'ceylon.language.metamodel::AppliedInterface',Basic,Interface$metamodel);
    }
    return AppliedInterface$metamodel;
}
exports.$init$AppliedInterface$metamodel=$init$AppliedInterface$metamodel;
$init$AppliedInterface$metamodel();

function AppliedUnionType$metamodel(types$2, $$appliedUnionType){
    $init$AppliedUnionType$metamodel();
    if ($$appliedUnionType===undefined)$$appliedUnionType=new AppliedUnionType$metamodel.$$;
    $$appliedUnionType.types$2=types$2;
    UnionType$metamodel($$appliedUnionType);
    return $$appliedUnionType;
}
AppliedUnionType$metamodel.$$metamodel$$={mod:$$METAMODEL$$,'super':{t:Basic},satisfies:[{t:UnionType$metamodel}],pkg:'',d:$$METAMODEL$$['ceylon.language.metamodel']['AppliedUnionType']};
function $init$AppliedUnionType$metamodel(){
    if (AppliedUnionType$metamodel.$$===undefined){
        initTypeProto(AppliedUnionType$metamodel,'AppliedUnionType',Basic,UnionType$metamodel);
        (function($$appliedUnionType){
            
            defineAttr($$appliedUnionType,'caseTypes',function(){
                var $$appliedUnionType=this;
                return $$appliedUnionType.types$2;
            });
        })(AppliedUnionType$metamodel.$$.prototype);
    }
    return AppliedUnionType$metamodel;
}
exports.$init$AppliedUnionType$metamodel=$init$AppliedUnionType$metamodel;
$init$AppliedUnionType$metamodel();

function AppliedIntersectionType$metamodel(types$3, $$appliedIntersectionType){
    $init$AppliedIntersectionType$metamodel();
    if ($$appliedIntersectionType===undefined)$$appliedIntersectionType=new AppliedIntersectionType$metamodel.$$;
    $$appliedIntersectionType.types$3=types$3;
    IntersectionType$metamodel($$appliedIntersectionType);
    
    return $$appliedIntersectionType;
}
AppliedIntersectionType$metamodel.$$metamodel$$={mod:$$METAMODEL$$,'super':{t:Basic},satisfies:[{t:IntersectionType$metamodel}],pkg:'',d:$$METAMODEL$$['ceylon.language.metamodel']['AppliedIntersectionType']};
function $init$AppliedIntersectionType$metamodel(){
    if (AppliedIntersectionType$metamodel.$$===undefined){
        initTypeProto(AppliedIntersectionType$metamodel,'AppliedIntersectionType',Basic,IntersectionType$metamodel);
        (function($$appliedIntersectionType){
            
            defineAttr($$appliedIntersectionType,'satisfiedTypes',function(){
                var $$appliedIntersectionType=this;
                return $$appliedIntersectionType.types$3;
            });
        })(AppliedIntersectionType$metamodel.$$.prototype);
    }
    return AppliedIntersectionType$metamodel;
}
exports.$init$AppliedIntersectionType$metamodel=$init$AppliedIntersectionType$metamodel;
$init$AppliedIntersectionType$metamodel();

