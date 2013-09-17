function AppliedClass(tipo,$$targs$$,that){
    $init$AppliedClass();
    if (that===undefined)that=new AppliedClass.$$;
    set_type_args(that,$$targs$$);
    Class$meta$model(that.$$targs$$===undefined?$$targs$$:{Arguments:that.$$targs$$.Arguments,Type:that.$$targs$$.Type},that);
    that.tipo=tipo;
    return that;
}
AppliedClass.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Basic},$tp:{Type:{'var':'out','satisfies':[{t:Object$}]},Arguments:{'var':'in','satisfies':[{t:Sequential,a:{Element:{t:Anything}}}]}},satisfies:[{t:Class$meta$model,a:{Arguments:'Arguments',Type:'Type'}}],$an:function(){return[shared()];},d:['ceylon.language.meta.model','Class']};};
function $init$AppliedClass(){
    if (AppliedClass.$$===undefined){
        initTypeProto(AppliedClass,'ceylon.language.meta.model::AppliedClass',Basic,Class$meta$model);
        (function($$clase){
            
            defineAttr($$clase,'declaration',function(){
              var $$clase=this;
              if ($$clase._decl)return $$clase._decl;
              var mm = $$clase.tipo.$$metamodel$$;
              if (typeof(mm)==='function'){
                mm=mm();
                $$clase.tipo.$$metamodel$$=mm;
              }
              $$clase._decl = OpenClass(getModules$meta().find(mm.mod['$mod-name'],mm.mod['$mod-version']).findPackage(mm.d[0]), $$clase.tipo);
              return $$clase._decl;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:ClassDeclaration$meta$declaration},$cont:AppliedClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Class','$at','declaration']};});

            defineAttr($$clase,'extendedType',function(){
              var sc = this.tipo.$$metamodel$$['super'];
              if (sc === undefined)return null;
              var mm = sc.t.$$metamodel$$;
              if (typeof(mm)==='function') {
                mm = mm();
                sc.t.$$metamodel$$=mm;
              }
              return AppliedClass(sc.t, {Type:sc,Arguments:{t:Nothing}});
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:ClassModel$meta$model,a:{Arguments:{t:Nothing},Type:{t:Anything}}}]},$cont:AppliedClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Class','$at','extendedType']};});

            defineAttr($$clase,'typeArguments',function(){
                var $$clase=this;
                throw wrapexc(Exception(String$("Class declaration",17)),'15:63-15:99','caca/caca.ceylon');
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Map,a:{Key:{t:TypeParameter$meta$declaration},Item:{t:Type$meta$model,a:{Type:{t:Anything}}}}},$cont:AppliedClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Class','$at','typeArguments']};});

            defineAttr($$clase,'satisfiedTypes',function(){
                var $$clase=this;
                throw wrapexc(Exception(String$("unimplemented",13)),'17:60-17:92','caca/caca.ceylon');
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:{t:InterfaceModel$meta$model,a:{Type:{t:Anything}}}}},$cont:AppliedClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Class','$at','satisfiedTypes']};});

            $$clase.getClassOrInterface=function getClassOrInterface(name$2,types$3,$$$mptypes){
                var $$clase=this;
                if(types$3===undefined){types$3=getEmpty();}
                throw wrapexc(Exception(String$("unimplemented",13)),'19:54-19:86','caca/caca.ceylon');
            };$$clase.getClassOrInterface.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:Member$meta$model,a:{Type:'SubType',Kind:'Kind'}}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$},$an:function(){return[];}},{$nm:'types',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element:{t:Type$meta$model,a:{Type:{t:Anything}}}}},$an:function(){return[];}}],$cont:AppliedClass,$tp:{SubType:{},Kind:{'satisfies':[{t:ClassOrInterface$meta$model,a:{Type:{t:Anything}}}]}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Class','$m','getClassOrInterface']};};

            $$clase.getMethod=function getMethod(name$4,types$5,$$$mptypes){
                var $$clase=this;
                if(types$5===undefined){types$5=getEmpty();}
                throw wrapexc(Exception(String$("unimplemented",13)),'21:43-21:75','caca/caca.ceylon');
            };$$clase.getMethod.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:Method$meta$model,a:{Arguments:'Arguments',Type:'Type',Container:'SubType'}}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$},$an:function(){return[];}},{$nm:'types',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element:{t:Type$meta$model,a:{Type:{t:Anything}}}}},$an:function(){return[];}}],$cont:AppliedClass,$tp:{SubType:{},Type:{},Arguments:{'satisfies':[{t:Sequential,a:{Element:{t:Anything}}}]}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Class','$m','getMethod']};};

            $$clase.getAttribute=function getAttribute(name$6,$$$mptypes){
                var $$clase=this;
                throw wrapexc(Exception(String$("unimplemented",13)),'22:85-22:117','caca/caca.ceylon');
            };$$clase.getAttribute.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:Attribute$meta$model,a:{Type:'Type',Container:'SubType'}}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$},$an:function(){return[];}}],$cont:AppliedClass,$tp:{SubType:{},Type:{}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Class','$m','getAttribute']};};

            $$clase.getVariableAttribute=function getVariableAttribute(name$7,$$$mptypes){
                var $$clase=this;
                throw wrapexc(Exception(String$("unimplemented",13)),'23:101-23:133','caca/caca.ceylon');
            };$$clase.getVariableAttribute.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:VariableAttribute$meta$model,a:{Type:'Type',Container:'SubType'}}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$},$an:function(){return[];}}],$cont:AppliedClass,$tp:{SubType:{},Type:{}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Class','$m','getVariableAttribute']};};

            $$clase.isTypeOf=function isTypeOf(instance$8){
                var $$clase=this;
                throw wrapexc(Exception(String$("Class declaration",17)),'25:54-25:90','caca/caca.ceylon');
            };$$clase.isTypeOf.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Boolean},$ps:[{$nm:'instance',$mt:'prm',$t:{t:Anything},$an:function(){return[];}}],$cont:AppliedClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Class','$m','isTypeOf']};};

            $$clase.isSuperTypeOf=function isSuperTypeOf(type$9){
                var $$clase=this;
                throw wrapexc(Exception(String$("Class declaration",17)),'26:67-26:103','caca/caca.ceylon');
            };$$clase.isSuperTypeOf.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Boolean},$ps:[{$nm:'type',$mt:'prm',$t:{t:Type$meta$model,a:{Type:{t:Anything}}},$an:function(){return[];}}],$cont:AppliedClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Class','$m','isSuperTypeOf']};};

            $$clase.isExactly=function isExactly(type$10){
                var $$clase=this;
                throw wrapexc(Exception(String$("Class declaration",17)),'27:63-27:99','caca/caca.ceylon');
            };$$clase.isExactly.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Boolean},$ps:[{$nm:'type',$mt:'prm',$t:{t:Type$meta$model,a:{Type:{t:Anything}}},$an:function(){return[];}}],$cont:AppliedClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Class','$m','isExactly']};};

            defineAttr($$clase,'container',function(){
                var $$clase=this;
                throw wrapexc(Exception(String$("Class declaration",17)),'28:50-28:86','caca/caca.ceylon');
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:Type$meta$model,a:{Type:{t:Anything}}}]},$cont:AppliedClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Class','$at','container']};});

        })(AppliedClass.$$.prototype);
    }
    return AppliedClass;
}
exports.$init$AppliedClass$meta$model=$init$AppliedClass;
$init$AppliedClass();

function AppliedInterface(tipo,$$targs$$,$$interfaz){
  $init$AppliedInterface();
  if ($$interfaz===undefined)$$interfaz=new AppliedInterface.$$;
  set_type_args($$interfaz,$$targs$$);
  Interface$meta$model($$targs$$,$$interfaz);
  $$interfaz.tipo=tipo;
  return $$interfaz;
}
AppliedInterface.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Basic},$tp:{Type:{'var':'out','satisfies':[{t:Object$}]}},satisfies:[{t:Interface$meta$model,a:{Type:'Type'}}],$an:function(){return[shared()];},d:['ceylon.language.meta.model','Interface']};};
exports.AppliedInterface=AppliedInterface;

function $init$AppliedInterface(){
    if (AppliedInterface.$$===undefined){
        initTypeProto(AppliedInterface,'caca::AppliedInterface',Basic,Interface$meta$model);
        (function($$appliedInterface){

            defineAttr($$appliedInterface,'declaration',function(){
      if (this._decl)return this._decl;
      var mm = this.tipo.$$metamodel$$;
      if (typeof(mm)==='function') {
        mm = mm();
        this.tipo.$$metamodel$$=mm;
      }
      var _pkg = getModules$meta().find(mm.mod['$mod-name'],mm.mod['$mod-version']).findPackage(mm.d[0]);
      this._decl = OpenInterface(mm.d[mm.d.length-1], _pkg, mm['$cont']===undefined, this.tipo);
      return this._decl;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:InterfaceDeclaration$meta$declaration},$cont:AppliedInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Interface','$at','declaration']};});

            defineAttr($$appliedInterface,'typeArguments',function(){
                var $$appliedInterface=this;
                throw wrapexc(Exception(String$("unimplemented",13)),'34:63-34:95','caca/caca.ceylon');
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Map,a:{Key:{t:TypeParameter$meta$declaration},Item:{t:Type$meta$model,a:{Type:{t:Anything}}}}},$cont:AppliedInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Interface','$at','typeArguments']};});

            defineAttr($$appliedInterface,'extendedType',function(){
                var $$appliedInterface=this;
                throw wrapexc(Exception(String$("unimplemented",13)),'35:62-35:94','caca/caca.ceylon');
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:ClassModel$meta$model,a:{Arguments:{t:Nothing},Type:{t:Anything}}}]},$cont:AppliedInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Interface','$at','extendedType']};});

            defineAttr($$appliedInterface,'satisfiedTypes',function(){
                var $$appliedInterface=this;
                throw wrapexc(Exception(String$("unimplemented",13)),'36:60-36:92','caca/caca.ceylon');
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:{t:InterfaceModel$meta$model,a:{Type:{t:Anything}}}}},$cont:AppliedInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Interface','$at','satisfiedTypes']};});

            $$appliedInterface.getMethod=function getMethod(name$11,types$12,$$$mptypes){
                var $$appliedInterface=this;
                if(types$12===undefined){types$12=getEmpty();}
                throw wrapexc(Exception(String$("unimplemented",13)),'38:43-38:75','caca/caca.ceylon');
            };$$appliedInterface.getMethod.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:Method$meta$model,a:{Arguments:'Arguments',Type:'Type',Container:'SubType'}}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$},$an:function(){return[];}},{$nm:'types',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element:{t:Type$meta$model,a:{Type:{t:Anything}}}}},$an:function(){return[];}}],$cont:AppliedInterface,$tp:{SubType:{},Type:{},Arguments:{'satisfies':[{t:Sequential,a:{Element:{t:Anything}}}]}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Interface','$m','getMethod']};};

            $$appliedInterface.getClassOrInterface=function getClassOrInterface(name$13,types$14,$$$mptypes){
                var $$appliedInterface=this;
                if(types$14===undefined){types$14=getEmpty();}
                throw wrapexc(Exception(String$("unimplemented",13)),'40:54-40:86','caca/caca.ceylon');
            };$$appliedInterface.getClassOrInterface.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:Member$meta$model,a:{Type:'SubType',Kind:'Kind'}}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$},$an:function(){return[];}},{$nm:'types',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element:{t:Type$meta$model,a:{Type:{t:Anything}}}}},$an:function(){return[];}}],$cont:AppliedInterface,$tp:{SubType:{},Kind:{'satisfies':[{t:ClassOrInterface$meta$model,a:{Type:{t:Anything}}}]}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Interface','$m','getClassOrInterface']};};

            $$appliedInterface.getAttribute=function getAttribute(name$15,$$$mptypes){
      var nom = '$prop$get' + name$15[0].toUpperCase() + name$15.substring(1);
      return AppliedValue(this.tipo.$$.prototype[nom], $$$mptypes);
            };$$appliedInterface.getAttribute.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:Attribute$meta$model,a:{Type:'Type',Container:'SubType'}}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$},$an:function(){return[];}}],$cont:AppliedInterface,$tp:{SubType:{},Type:{}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Interface','$m','getAttribute']};};

            $$appliedInterface.getVariableAttribute=function getVariableAttribute(name$16,$$$mptypes){
                var $$appliedInterface=this;
                throw wrapexc(Exception(String$("unimplemented",13)),'42:101-42:133','caca/caca.ceylon');
            };$$appliedInterface.getVariableAttribute.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:VariableAttribute$meta$model,a:{Type:'Type',Container:'SubType'}}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$},$an:function(){return[];}}],$cont:AppliedInterface,$tp:{SubType:{},Type:{}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Interface','$m','getVariableAttribute']};};

            $$appliedInterface.isTypeOf=function isTypeOf(instance$17){
                var $$appliedInterface=this;
                throw wrapexc(Exception(String$("Class declaration",17)),'44:54-44:90','caca/caca.ceylon');
            };$$appliedInterface.isTypeOf.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Boolean},$ps:[{$nm:'instance',$mt:'prm',$t:{t:Anything},$an:function(){return[];}}],$cont:AppliedInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Interface','$m','isTypeOf']};};

            $$appliedInterface.isSuperTypeOf=function isSuperTypeOf(type$18){
                var $$appliedInterface=this;
                throw wrapexc(Exception(String$("Class declaration",17)),'45:67-45:103','caca/caca.ceylon');
            };$$appliedInterface.isSuperTypeOf.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Boolean},$ps:[{$nm:'type',$mt:'prm',$t:{t:Type$meta$model,a:{Type:{t:Anything}}},$an:function(){return[];}}],$cont:AppliedInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Interface','$m','isSuperTypeOf']};};

            $$appliedInterface.isExactly=function isExactly(type$19){
                var $$appliedInterface=this;
                throw wrapexc(Exception(String$("Class declaration",17)),'46:63-46:99','caca/caca.ceylon');
            };$$appliedInterface.isExactly.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Boolean},$ps:[{$nm:'type',$mt:'prm',$t:{t:Type$meta$model,a:{Type:{t:Anything}}},$an:function(){return[];}}],$cont:AppliedInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Interface','$m','isExactly']};};

            defineAttr($$appliedInterface,'container',function(){
                var $$appliedInterface=this;
                throw wrapexc(Exception(String$("Class declaration",17)),'47:50-47:86','caca/caca.ceylon');
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:Type$meta$model,a:{Type:{t:Anything}}}]},$cont:AppliedInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Interface','$at','container']};});

        })(AppliedInterface.$$.prototype);
    }
    return AppliedInterface;
}
exports.$init$AppliedInterface$meta$model=$init$AppliedInterface;
$init$AppliedInterface();

    

function AppliedUnionType(tipo,types$2, $$targs$$, that) {
    $init$AppliedUnionType();
    if (that===undefined)that=new AppliedUnionType.$$;
    set_type_args(that,$$targs$$);
    UnionType$meta$model(that.$$targs$$===undefined?$$targs$$:{Union:that.$$targs$$.Union},that);
    that.tipo=tipo;
    that._types=types$2;
    return that;
}
AppliedUnionType.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Basic},$tp:{Union:{'var':'out','def':{t:Anything}}},satisfies:[{t:UnionType$meta$model,a:{Union:'Union'}}],$an:function(){return[shared()];},d:['ceylon.language.meta.model','UnionType']};};
function $init$AppliedUnionType(){
    if (AppliedUnionType.$$===undefined){
        initTypeProto(AppliedUnionType,'AppliedUnionType',Basic,UnionType$meta$model);
        (function($$appliedUnionType){
            
            defineAttr($$appliedUnionType,'caseTypes',function(){
                var $$appliedUnionType=this;
                return $$appliedUnionType._types; //TODO type
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:List,a:{Element:{t:Type$meta$model,a:{Type:'Union'}}}},$cont:AppliedUnionType,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','UnionType','$at','caseTypes']};});

            $$appliedUnionType.isTypeOf=function isTypeOf(instance$20){
                var $$appliedUnionType=this;
                throw wrapexc(Exception(String$("Class declaration",17)),'69:54-69:90','caca/caca.ceylon');
            };$$appliedUnionType.isTypeOf.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Boolean},$ps:[{$nm:'instance',$mt:'prm',$t:{t:Anything},$an:function(){return[];}}],$cont:AppliedUnionType,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','UnionType','$m','isTypeOf']};};

            $$appliedUnionType.isSuperTypeOf=function isSuperTypeOf(type$21){
                var $$appliedUnionType=this;
                throw wrapexc(Exception(String$("Class declaration",17)),'70:67-70:103','caca/caca.ceylon');
            };$$appliedUnionType.isSuperTypeOf.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Boolean},$ps:[{$nm:'type',$mt:'prm',$t:{t:Type$meta$model,a:{Type:{t:Anything}}},$an:function(){return[];}}],$cont:AppliedUnionType,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','UnionType','$m','isSuperTypeOf']};};

            $$appliedUnionType.isExactly=function isExactly(type$22){
                var $$appliedUnionType=this;
                throw wrapexc(Exception(String$("Class declaration",17)),'71:63-71:99','caca/caca.ceylon');
            };$$appliedUnionType.isExactly.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Boolean},$ps:[{$nm:'type',$mt:'prm',$t:{t:Type$meta$model,a:{Type:{t:Anything}}},$an:function(){return[];}}],$cont:AppliedUnionType,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','UnionType','$m','isExactly']};};

        })(AppliedUnionType.$$.prototype);
    }
    return AppliedUnionType;
}
exports.$init$AppliedUnionType$meta$model=$init$AppliedUnionType;
$init$AppliedUnionType();

function AppliedIntersectionType(tipo,types$3, $$targs$$, that) {
    $init$AppliedIntersectionType();
    if (that===undefined)that=new AppliedIntersectionType.$$;
    set_type_args(that,$$targs$$);
    that._types=types$3;
    IntersectionType$meta$model(that.$$targs$$===undefined?$$targs$$:{Intersection:that.$$targs$$.Union},that);
    that.tipo=tipo;
    return that;
}
AppliedIntersectionType.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Basic},$tp:{Union:{'var':'out','def':{t:Anything}}},satisfies:[{t:IntersectionType$meta$model,a:{Intersection:'Union'}}],$an:function(){return[shared()];},d:['ceylon.language.meta.model','IntersectionType']};};
function $init$AppliedIntersectionType(){
    if (AppliedIntersectionType.$$===undefined){
        initTypeProto(AppliedIntersectionType,'AppliedIntersectionType',Basic,IntersectionType$meta$model);
        (function($$appliedIntersectionType){

            defineAttr($$appliedIntersectionType,'satisfiedTypes',function(){
                var $$appliedIntersectionType=this;
                return $$appliedIntersectionType._types;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:List,a:{Element:{t:Type$meta$model,a:{Type:'Union'}}}},$cont:AppliedIntersectionType,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','IntersectionType','$at','satisfiedTypes']};});

            $$appliedIntersectionType.isTypeOf=function isTypeOf(instance$23){
                var $$appliedIntersectionType=this;
                throw wrapexc(Exception(String$("Class declaration",17)),'77:54-77:90','caca/caca.ceylon');
            };$$appliedIntersectionType.isTypeOf.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Boolean},$ps:[{$nm:'instance',$mt:'prm',$t:{t:Anything},$an:function(){return[];}}],$cont:AppliedIntersectionType,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','IntersectionType','$m','isTypeOf']};};

            $$appliedIntersectionType.isSuperTypeOf=function isSuperTypeOf(type$24){
                var $$appliedIntersectionType=this;
                throw wrapexc(Exception(String$("Class declaration",17)),'78:67-78:103','caca/caca.ceylon');
            };$$appliedIntersectionType.isSuperTypeOf.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Boolean},$ps:[{$nm:'type',$mt:'prm',$t:{t:Type$meta$model,a:{Type:{t:Anything}}},$an:function(){return[];}}],$cont:AppliedIntersectionType,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','IntersectionType','$m','isSuperTypeOf']};};

            $$appliedIntersectionType.isExactly=function isExactly(type$25){
                var $$appliedIntersectionType=this;
                throw wrapexc(Exception(String$("Class declaration",17)),'79:63-79:99','caca/caca.ceylon');
            };$$appliedIntersectionType.isExactly.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Boolean},$ps:[{$nm:'type',$mt:'prm',$t:{t:Type$meta$model,a:{Type:{t:Anything}}},$an:function(){return[];}}],$cont:AppliedIntersectionType,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','IntersectionType','$m','isExactly']};};

        })(AppliedIntersectionType.$$.prototype);
    }
    return AppliedIntersectionType;
}
exports.$init$AppliedIntersectionType$meta$model=$init$AppliedIntersectionType;
$init$AppliedIntersectionType();

function AppliedFunction(m,$$targs$$,o) {
  var f = o===undefined?function(){return m.apply(this,arguments);}:function(){return m.apply(o,arguments);}
  var mm=m.$$metamodel$$;
  if (typeof(mm)==='function') {mm=mm();m.$$metamodel$$=mm;}
  f.$$metamodel$$={mod:$$METAMODEL$$,d:['ceylon.language.model','Function'],$t:mm.$t,$ps:mm.$ps,$an:mm.$an};
  var T$all={'ceylon.language.model::Function':Function$meta$model};
  for (x in f.getT$all()) { T$all[x]=f.getT$all()[x]; }
  f.getT$all=function() {return T$all; };
  if ($$targs$$===undefined) {
    //TODO add type arguments
    var types = {t:Empty};
    var t2s = [];
    for (var i=mm.$ps.length-1; i>=0; i--) {
      var e;
      t2s.push(mm.$ps[i].$t);
      if (t2s.length == 1) {
        e = mm.$ps[i].$t;
      } else {
        var lt=[];
        for (var j=0;j<t2s.legth;j++)lt.push(t2s[j]);
        e = {t:'u', l:lt};
      }
      types = {t:Tuple,a:{Rest:types,First:mm.$ps[i].$t,Element:e}};
    }
    f.$$targs$$={Type:mm.$t,Arguments:types};
  } else {
    f.$$targs$$=$$targs$$;
  }
  return f;
}
exports.AppliedFunction$meta$model=AppliedFunction;

function AppliedValue(attr,$$appliedValue){
    var mm = attr.$$metamodel$$;
    if (typeof(mm)==='function')mm=mm();
    var $$targs$$ = {Container:{t:mm.$cont},Type:mm.$t};
    $init$AppliedValue();
    if ($$appliedValue ===undefined)$$appliedValue =new AppliedValue.$$;
    set_type_args($$appliedValue,$$targs$$);
    Value$meta$model($$appliedValue.$$targs$$===undefined?$$targs$$:{Type:$$appliedValue.$$targs$$.Type},$$appliedValue);
    $$appliedValue=attr;
    return $$appliedValue;
}
AppliedValue.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Basic},$tp:{Type:{'var':'out'}},satisfies:[{t:Value$meta$model,a:{Type:'Type'}}],$an:function(){return[shared()];},d:['ceylon.language.meta.model','Value']};};
exports.AppliedValue$meta$model=AppliedValue;
function $init$AppliedValue(){
    if (AppliedValue.$$===undefined){
        initTypeProto(AppliedValue,'AppliedValue',Basic,Attribute$meta$model);
        (function($$appliedValue){
            
            defineAttr($$appliedValue,'declaration',function(){
                var $$appliedValue=this;
                throw wrapexc(Exception(String$("Value.declaration",17)),'52:46-52:82','caca/caca.ceylon');
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:ValueDeclaration$meta$declaration},$cont:AppliedValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Value','$at','declaration']};});

            $$appliedValue.get=function get(){
                var $$appliedValue=this;
                throw wrapexc(Exception(String$("Implement get",13)),'51:29-51:61','caca/caca.ceylon');
            };$$appliedValue.get.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:'Type',$ps:[],$cont:AppliedValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Value','$m','get']};};

            defineAttr($$appliedValue,'type',function(){
                var $$appliedValue=this;
                throw wrapexc(Exception(String$("Value.type",10)),'53:40-53:69','caca/caca.ceylon');
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Type$meta$model,a:{Type:'Type'}},$cont:AppliedValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Value','$at','type']};});

            defineAttr($$appliedValue,'container',function(){
                var $$appliedValue=this;
                throw wrapexc(Exception(String$("Class declaration",17)),'55:50-55:86','caca/caca.ceylon');
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:Type$meta$model,a:{Type:{t:Anything}}}]},$cont:AppliedValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Value','$at','container']};});

        })(AppliedValue.$$.prototype);
    }
    return AppliedValue;
}
exports.$init$AppliedValue$meta$model=$init$AppliedValue;
$init$AppliedValue();

//ClassDefinition AppliedMethod at caca.ceylon (10:0-21:0)
function AppliedMethod(tipo,fun,$$targs$$,$$appliedMethod){
    $init$AppliedMethod();
    if ($$appliedMethod===undefined)$$appliedMethod=new AppliedMethod.$$;
    set_type_args($$appliedMethod,$$targs$$);
    Method$meta$model($$appliedMethod.$$targs$$===undefined?$$targs$$:{Arguments:$$appliedMethod.$$targs$$.Arguments,Type:$$appliedMethod.$$targs$$.Type,Container:$$appliedMethod.$$targs$$.Container},$$appliedMethod);
    $$appliedMethod.tipo=tipo;
    $$appliedMethod.fun=fun;
    return $$appliedMethod;
}
AppliedMethod.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Basic},$tp:{Container:{'var':'in'},Type:{'var':'out','def':{t:Anything}},Arguments:{'var':'in','satisfies':[{t:Sequential,a:{Element:{t:Anything}}}],'def':{t:Nothing}}},satisfies:[{t:Method$meta$model,a:{Arguments:'Arguments',Type:'Type',Container:'Container'}}],$an:function(){return[shared()];},d:['ceylon.language.meta.model','Method']};};
exports.AppliedMethod=AppliedMethod;
function $init$AppliedMethod(){
    if (AppliedMethod.$$===undefined){
        initTypeProto(AppliedMethod,'caca::AppliedMethod',Basic,Method$meta$model);
        (function($$appliedMethod){
            
            defineAttr($$appliedMethod,'declaration',function(){
              var $$appliedMethod=this;
              var mm = $$appliedMethod.fun.$$metamodel$$;
              if (typeof(mm)==='function') {
                mm = mm();
                $$appliedMethod.fun.$$metamodel$$=mm;
              }
              var _pkg = getModules$meta().find(mm.mod['$mod-name'],mm.mod['$mod-version']).findPackage(mm.d[0]);
              return OpenFunction(_pkg, $$appliedMethod.fun);
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:FunctionDeclaration$meta$declaration},$cont:AppliedMethod,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Method','$at','declaration']};});

            defineAttr($$appliedMethod,'type',function(){
                var $$appliedMethod=this;
              return $$appliedMethod.fun.$$metamodel$$['$t'];
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Type$meta$model,a:{Type:'Type'}},$cont:AppliedMethod,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Method','$at','type']};});

            defineAttr($$appliedMethod,'declaringClassOrInterface',function(){
              var $$appliedMethod=this;
              var mm = $$appliedMethod.tipo.$$metamodel$$;
              if (typeof(mm)==='function') {
                mm = mm();
                $$appliedMethod.tipo.$$metamodel$$=mm;
              }
              var m2 = get_model(mm);
              return (m2['$mt']==='cls'?OpenClass:OpenInterface)(getModules$meta().find(mm.mod['$mod-name'],mm.mod['$mod-version']).findPackage(mm.d[0]), $$appliedMethod.tipo);
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:ClassOrInterface$meta$model,a:{Type:{t:Anything}}},$cont:AppliedMethod,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Method','$at','declaringClassOrInterface']};});

            defineAttr($$appliedMethod,'container',function(){
                var $$appliedMethod=this;
                return $$appliedMethod.declaringClassOrInterface;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:ClassOrInterface$meta$model,a:{Type:{t:Anything}}}]},$cont:AppliedMethod,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Method','$at','container']};});

            defineAttr($$appliedMethod,'typeArguments',function(){
                var $$appliedMethod=this;
                throw wrapexc(Exception(String$("unimplemented",13)),'92:75-92:107','caca/caca.ceylon');
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Map,a:{Key:{t:TypeParameter$meta$declaration},Item:{t:Type$meta$model,a:{Type:{t:Anything}}}}},$cont:AppliedMethod,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Method','$at','typeArguments']};});

        })(AppliedMethod.$$.prototype);
    }
    return AppliedMethod;
}
exports.$init$AppliedMethod$meta$model=$init$AppliedMethod;
$init$AppliedMethod();
