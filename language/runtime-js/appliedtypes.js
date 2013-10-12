function AppliedClass(tipo,$$targs$$,that){
  $init$AppliedClass();
  if (that===undefined){
    var mm = tipo.$$metamodel$$;
    if (typeof(mm)==='function') {
      mm=mm(); tipo.$$metamodel$$=mm;
    }
    if (mm && mm.$cont) {
      that=function(x){
        that.tipo=function(){return tipo.apply(x,arguments);};
        return that;
      }
      that.tipo$2=tipo;
      var dummy = new AppliedClass.$$;
      that.$$=AppliedClass.$$;
      that.getT$all=function(){return dummy.getT$all();};
      that.getT$name=function(){return dummy.getT$name();};
      that.equals=function(o){return o && (o.tipo$2||o.tipo)==tipo;};
    } else {
      that=new AppliedClass.$$;
    }
  }
  set_type_args(that,$$targs$$);
  Class$meta$model(that.$$targs$$===undefined?$$targs$$:{Arguments:that.$$targs$$.Arguments,Type:that.$$targs$$.Type},that);
  that.tipo=tipo;
  return that;
}
AppliedClass.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Basic},$tp:{Type:{'var':'out','def':{t:Anything}},Arguments:{'var':'in','satisfies':[{t:Sequential,a:{Element:{t:Anything}}}],'def':{t:Nothing}}},satisfies:[{t:Class$meta$model,a:{Arguments:'Arguments',Type:'Type'}}],$an:function(){return[shared()];},d:['ceylon.language.meta.model','Class']};};
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
              return AppliedClass(sc.t, {Type:sc,Arguments:{t:Sequential,a:{Element:{t:Anything}}}});
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:ClassModel$meta$model,a:{Arguments:{t:Nothing},Type:{t:Anything}}}]},$cont:AppliedClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Class','$at','extendedType']};});


  $$clase.equals=function(o){return o && (o.tipo$2||o.tipo)==tipo; };
//TODO equals metamodel

  defineAttr($$clase,'string',function(){
    var mm = this.tipo.$$metamodel$$;
    if (typeof(mm)==='function') {
      mm=mm();
      this.tipo.$$metamodel$$=mm;
    }
    var qn=mm.d[0];
    for (var i=1; i<mm.d.length; i++)if(mm.d[i][0]!=='$')qn+=(i==1?"::":".")+mm.d[i];
    return String$("AppliedClass[" + qn + "]");
  },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},$cont:AppliedClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Class','$at','string']};});

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
AppliedInterface.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Basic},$tp:{Type:{'var':'out','def':{t:Anything}}},satisfies:[{t:Interface$meta$model,a:{Type:'Type'}}],$an:function(){return[shared()];},d:['ceylon.language.meta.model','Interface']};};
exports.AppliedInterface=AppliedInterface;

function $init$AppliedInterface(){
    if (AppliedInterface.$$===undefined){
        initTypeProto(AppliedInterface,'ceylon.language.meta.model::AppliedInterface',Basic,Interface$meta$model);
        (function($$appliedInterface){

            defineAttr($$appliedInterface,'declaration',function(){
      if (this._decl)return this._decl;
      var mm = this.tipo.$$metamodel$$;
      if (typeof(mm)==='function') {
        mm = mm();
        this.tipo.$$metamodel$$=mm;
      }
      this._decl = OpenInterface(getModules$meta().find(mm.mod['$mod-name'],mm.mod['$mod-version']).findPackage(mm.d[0]), this.tipo);
      return this._decl;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:InterfaceDeclaration$meta$declaration},$cont:AppliedInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Interface','$at','declaration']};});

            defineAttr($$appliedInterface,'extendedType',function(){
                var $$appliedInterface=this;
                throw wrapexc(Exception(String$("IMPL AppliedInterface.extendedType")),'35:62-35:94','?');
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:ClassModel$meta$model,a:{Arguments:{t:Nothing},Type:{t:Anything}}}]},$cont:AppliedInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Interface','$at','extendedType']};});

            defineAttr($$appliedInterface,'container',function(){
                var $$appliedInterface=this;
                throw wrapexc(Exception(String$("IMPL AppliedInterface.container")),'47:50-47:86','?');
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:Type$meta$model,a:{Type:{t:Anything}}}]},$cont:AppliedInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Interface','$at','container']};});

  defineAttr($$appliedInterface,'string',function(){
    var mm = this.tipo.$$metamodel$$;
    if (typeof(mm)==='function') {
      mm=mm();
      this.tipo.$$metamodel$$=mm;
    }
    var qn=mm.d[0];
    for (var i=1; i<mm.d.length; i++)if(mm.d[i][0]!=='$')qn+=(i==1?"::":".")+mm.d[i];
    return String$("AppliedInterface[" + qn + "]");
  },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},$cont:AppliedInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Interface','$at','string']};});

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
    initTypeProto(AppliedUnionType,'ceylon.language.meta.model::AppliedUnionType',Basic,UnionType$meta$model);
    (function($$appliedUnionType){
            
            defineAttr($$appliedUnionType,'caseTypes',function(){
                var $$appliedUnionType=this;
                return $$appliedUnionType._types; //TODO type
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:List,a:{Element:{t:Type$meta$model,a:{Type:'Union'}}}},$cont:AppliedUnionType,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','UnionType','$at','caseTypes']};});

      $$appliedUnionType.isTypeOf=function isTypeOf(instance$20){
        var tipos=[];
        for (var i=0; i < this._types.length;i++) {
          var _t = this._types[i];
          if (_t.tipo) {
            _t={t:_t.tipo};
          } else if (_t.t===undefined) {
          _t={t:_t};
          }
          tipos.push(_t);
        }
        return isOfType(instance$20,{t:'u',l:tipos});
      };$$appliedUnionType.isTypeOf.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Boolean},$ps:[{$nm:'instance',$mt:'prm',$t:{t:Anything},$an:function(){return[];}}],$cont:AppliedUnionType,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','UnionType','$m','isTypeOf']};};

            $$appliedUnionType.isSuperTypeOf=function isSuperTypeOf(type$21){
                var $$appliedUnionType=this;
                throw wrapexc(Exception(String$("IMPL AppliedUnionType.isSuperTypeOf")),'70:67-70:103','?');
            };$$appliedUnionType.isSuperTypeOf.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Boolean},$ps:[{$nm:'type',$mt:'prm',$t:{t:Type$meta$model,a:{Type:{t:Anything}}},$an:function(){return[];}}],$cont:AppliedUnionType,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','UnionType','$m','isSuperTypeOf']};};

      $$appliedUnionType.isExactly=function isExactly(type$22){
        if (isOfType(type$25, {t:AppliedUnionType})) {
          for (var i=0; i<this._types.length;i++) {
            var myt = this._types[i];
            var was=false;
            for (var j=0; j<type$25._types.length;j++) {
              was |= myt.isExactly(type$25._types[j]);
            }
            if (!was)return false;
          }
          //Now the other way around
          for (var i=0; i<type$25._types.length;i++) {
            var myt = type$25._types[i];
            var was=false;
            for (var j=0; j<this._types.length;j++) {
              was |= myt.isExactly(this._types[j]);
            }
            if (!was)return false;
          }
          return true;
        }
        return false;
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
    initTypeProto(AppliedIntersectionType,'ceylon.language.meta.model::AppliedIntersectionType',Basic,IntersectionType$meta$model);
    (function($$appliedIntersectionType){

            defineAttr($$appliedIntersectionType,'satisfiedTypes',function(){
                var $$appliedIntersectionType=this;
                return $$appliedIntersectionType._types;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:List,a:{Element:{t:Type$meta$model,a:{Type:'Union'}}}},$cont:AppliedIntersectionType,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','IntersectionType','$at','satisfiedTypes']};});

      $$appliedIntersectionType.isTypeOf=function isTypeOf(instance$23){
        var tipos=[];
        for (var i=0; i < this._types.length;i++) {
          var _t = this._types[i];
          if (_t.tipo) {
            _t={t:_t.tipo};
          } else if (_t.t===undefined) {
          _t={t:_t};
          }
          tipos.push(_t);
        }
        return isOfType(instance$23,{t:'i',l:tipos});
      };$$appliedIntersectionType.isTypeOf.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Boolean},$ps:[{$nm:'instance',$mt:'prm',$t:{t:Anything},$an:function(){return[];}}],$cont:AppliedIntersectionType,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','IntersectionType','$m','isTypeOf']};};

            $$appliedIntersectionType.isSuperTypeOf=function isSuperTypeOf(type$24){
                var $$appliedIntersectionType=this;
                throw wrapexc(Exception(String$("IMPL AppliedIntersectionType.isSuperTypeOf")),'78:67-78:103','?');
            };$$appliedIntersectionType.isSuperTypeOf.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Boolean},$ps:[{$nm:'type',$mt:'prm',$t:{t:Type$meta$model,a:{Type:{t:Anything}}},$an:function(){return[];}}],$cont:AppliedIntersectionType,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','IntersectionType','$m','isSuperTypeOf']};};

      $$appliedIntersectionType.isExactly=function isExactly(type$25){
        if (isOfType(type$25, {t:AppliedIntersectionType})) {
          for (var i=0; i<this._types.length;i++) {
            var myt = this._types[i];
            var was=false;
            for (var j=0; j<type$25._types.length;j++) {
              was |= myt.isExactly(type$25._types[j]);
            }
            if (!was)return false;
          }
          //Now the other way around
          for (var i=0; i<type$25._types.length;i++) {
            var myt = type$25._types[i];
            var was=false;
            for (var j=0; j<this._types.length;j++) {
              was |= myt.isExactly(this._types[j]);
            }
            if (!was)return false;
          }
          return true;
        }
        return false;
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
AppliedFunction.$$metamodel$$=function(){return{mod:$$METAMODEL$$,d:['ceylon.language.meta.model','Function'],satisfies:{t:Function$meta$model,a:{Type:'Type',Arguments:'Arguments'}},$an:function(){return [shared(),actual()];}};};
exports.AppliedFunction$meta$model=AppliedFunction;

function AppliedValue(obj,attr,$$targs$$,$$appliedValue){
  var mm = attr.$$metamodel$$;
  if (typeof(mm)==='function'){
    mm=mm();
    attr.$$metamodel$$=mm;
  }
  $init$AppliedValue();
  if ($$appliedValue===undefined){
    if (obj||mm.$cont===undefined)$$appliedValue=new AppliedValue.$$;
    else {
      $$appliedValue=function(x){return AppliedValue(x,attr,$$targs$$);};
      $$appliedValue.$$=AppliedValue.$$;
      var dummy=new AppliedValue.$$;
      $$appliedValue.getT$all=function(){return dummy.getT$all();};
      $$appliedValue.getT$name=function(){return dummy.getT$name();};
    }
  }
  set_type_args($$appliedValue,$$targs$$);
  Value$meta$model($$appliedValue.$$targs$$===undefined?$$targs$$:{Type:$$appliedValue.$$targs$$.Type},$$appliedValue);
  $$appliedValue.obj=obj;
  $$appliedValue.tipo=attr;
  return $$appliedValue;
}
AppliedValue.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Basic},$tp:{Type:{'var':'out'}},satisfies:[{t:Value$meta$model,a:{Type:'Type'}}],$an:function(){return[shared()];},d:['ceylon.language.meta.model','Value']};};
exports.AppliedValue$meta$model=AppliedValue;
function $init$AppliedValue(){
  if (AppliedValue.$$===undefined){
    initTypeProto(AppliedValue,'ceylon.language.meta.model::AppliedValue',Basic,Attribute$meta$model);
    (function($$appliedValue){
      defineAttr($$appliedValue,'declaration',function(){
        var $$av=this;
        var mm = $$av.tipo.$$metamodel$$;
        if (typeof(mm)==='function') {
          mm = mm();
          $$av.tipo.$$metamodel$$=mm;
        }
        var _pkg = getModules$meta().find(mm.mod['$mod-name'],mm.mod['$mod-version']).findPackage(mm.d[0]);
        return OpenValue(_pkg, $$av.tipo);
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:ValueDeclaration$meta$declaration},$cont:AppliedValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Value','$at','declaration']};});

      $$appliedValue.get=function get(){
        var $$av=this;
        return $$av.obj?$$av.tipo.get.apply($$av.obj):$$av.tipo.get();
      };$$appliedValue.get.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:'Type',$ps:[],$cont:AppliedValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Value','$m','get']};};

      defineAttr($$appliedValue,'type',function(){
          var $$atr=this;
          var t = $$atr.tipo.$$metamodel$$;
          if (typeof(t)==='function') {
            t=t(); $$atr.tipo.$$metamodel$$=t;
          }
          return typeLiteral$meta({Type:t.$t});
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Type$meta$model,a:{Type:'Type'}},$cont:AppliedValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Value','$at','type']};});

      defineAttr($$appliedValue,'container',function(){
          var $$av=this;
          var mm=$$av.tipo.$$metamodel$$;
          if (typeof(t)==='function') {
            t=t(); $$atr.tipo.$$metamodel$$=t;
          }
          //TODO determine if t is a class, interface, etc to return the apropriate type
          throw wrapexc(Exception(String$("IMPL AppliedValue.container",17)),'55:50-55:86','?');
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:Type$meta$model,a:{Type:{t:Anything}}}]},$cont:AppliedValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Value','$at','container']};});

    })(AppliedValue.$$.prototype);
  }
  return AppliedValue;
}
exports.$init$AppliedValue$meta$model=$init$AppliedValue;
$init$AppliedValue();

//ClassDefinition AppliedVariable at X (103:0-106:0)
function AppliedVariable(obj,attr,$$targs$$,$$appliedVariable){
  var mm = attr.$$metamodel$$;
  if (typeof(mm)==='function'){
    mm=mm();
    attr.$$metamodel$$=mm;
  }
  $init$AppliedVariable$meta$model();
  if ($$appliedVariable===undefined) {
    if (obj||mm.$cont===undefined)$$appliedVariable=new AppliedVariable.$$;
    else {
      $$appliedVariable=function(x){return AppliedVariable(x,attr,$$targs$$);};
      $$appliedVariable.$$=AppliedVariable.$$;
      var dummy=new AppliedVariable.$$;
      $$appliedVariable.getT$all=function(){return dummy.getT$all();};
      $$appliedVariable.getT$name=function(){return dummy.getT$name();};
    }
  }
  set_type_args($$appliedVariable,$$targs$$);
  AppliedValue(obj,attr,$$appliedVariable.$$targs$$===undefined?$$targs$$:{Type:$$appliedVariable.$$targs$$.Type},$$appliedVariable);
  Variable$meta$model($$appliedVariable.$$targs$$===undefined?$$targs$$:{Type:$$appliedVariable.$$targs$$.Type},$$appliedVariable);
  return $$appliedVariable;
}
AppliedVariable.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:AppliedValue,a:{Type:'Type'}},$tp:{Type:{}},satisfies:[{t:Variable$meta$model,a:{Type:'Type'}}],$an:function(){return[shared()];},d:['ceylon.language.meta.model','Variable']};};
exports.AppliedVariable=AppliedVariable;
function $init$AppliedVariable$meta$model(){
  if (AppliedVariable.$$===undefined){
    initTypeProto(AppliedVariable,'ceylon.language.meta.model::AppliedVariable',AppliedValue,Variable$meta$model);
    (function($$appliedVariable){
            
      //MethodDefinition set at X (104:2-104:76)
      $$appliedVariable.set=function set(newValue$26){
        var $$av=this;
        return $$av.obj?$$av.tipo.set.call($$av.obj,newValue$26):$$av.tipo.set(newValue$26);
      };$$appliedVariable.set.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Anything},$ps:[{$nm:'newValue',$mt:'prm',$t:'Type',$an:function(){return[];}}],$cont:AppliedVariable,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Variable','$m','set']};};
            
      //AttributeGetterDefinition declaration at X (105:2-105:92)
      defineAttr($$appliedVariable,'declaration',function(){
        var $$av=this;
        var mm = $$av.tipo.$$metamodel$$;
        if (typeof(mm)==='function') {
          mm = mm();
          $$av.tipo.$$metamodel$$=mm;
        }
        var _pkg = getModules$meta().find(mm.mod['$mod-name'],mm.mod['$mod-version']).findPackage(mm.d[0]);
        return OpenVariable(_pkg, $$av.tipo);
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:VariableDeclaration$meta$declaration},$cont:AppliedVariable,$an:function(){return[shared(),actual(),$default()];},d:['ceylon.language.meta.model','Variable','$at','declaration']};});
    })(AppliedVariable.$$.prototype);
  }
  return AppliedVariable;
}
exports.$init$AppliedVariable$meta$model=$init$AppliedVariable$meta$model;
$init$AppliedVariable$meta$model();

//ClassDefinition AppliedMethod at X (10:0-21:0)
function AppliedMethod(tipo,typeArgs,$$targs$$,$$appliedMethod){
  $init$AppliedMethod();
  var _mptypes=undefined;
  if (typeArgs && tipo && tipo.$$metamodel$$) {
    var _tmm=tipo.$$metamodel$$;
    if (typeof(_tmm)==='function'){_tmm=_tmm();tipo.$$metamodel$$=_tmm;}
    _mptypes={};
    var _count=0;
    for (var type_arg in _tmm.$tp) {
      _mptypes[type_arg]=typeArgs[_count++].$$targs$$.Type;
    }
  }
  if ($$appliedMethod===undefined)$$appliedMethod=function(x){return function(){
    if (_mptypes) {
      var nargs = [];
      for (var i=0; i < arguments.length; i++)nargs.push(arguments[i]);
      nargs.push(_mptypes);
      return tipo.apply(x,nargs);
    }
    return tipo.apply(x,arguments);
  }};
  set_type_args($$appliedMethod,$$targs$$);
  Method$meta$model($$appliedMethod.$$targs$$===undefined?$$targs$$:{Arguments:$$appliedMethod.$$targs$$.Arguments,Type:$$appliedMethod.$$targs$$.Type,Container:$$appliedMethod.$$targs$$.Container},$$appliedMethod);
  $$appliedMethod.tipo=tipo;

//This was copied from prototype style
  defineAttr($$appliedMethod,'declaration',function(){
    var $$appliedMethod=this;
    var mm = $$appliedMethod.tipo.$$metamodel$$;
    if (typeof(mm)==='function') {
      mm = mm();
      $$appliedMethod.tipo.$$metamodel$$=mm;
    }
    var _pkg = getModules$meta().find(mm.mod['$mod-name'],mm.mod['$mod-version']).findPackage(mm.d[0]);
    return OpenFunction(_pkg, $$appliedMethod.tipo);
  },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:FunctionDeclaration$meta$declaration},$cont:AppliedMethod,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Method','$at','declaration']};});

  defineAttr($$appliedMethod,'type',function(){
    return typeLiteral$meta({Type:this.tipo.$$metamodel$$['$t']});
  },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Type$meta$model,a:{Type:'Type'}},$cont:AppliedMethod,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Method','$at','type']};});

  defineAttr($$appliedMethod,'typeArguments',function(){
      var $$appliedMethod=this;
      throw wrapexc(Exception(String$("IMPL AppliedMethod.typeArguments")),'92:75-92:107','?');
  },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Map,a:{Key:{t:TypeParameter$meta$declaration},Item:{t:Type$meta$model,a:{Type:{t:Anything}}}}},$cont:AppliedMethod,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Method','$at','typeArguments']};});

  return $$appliedMethod;
}
AppliedMethod.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Basic},$tp:{Container:{'var':'in'},Type:{'var':'out','def':{t:Anything}},Arguments:{'var':'in','satisfies':[{t:Sequential,a:{Element:{t:Anything}}}],'def':{t:Nothing}}},satisfies:[{t:Method$meta$model,a:{Arguments:'Arguments',Type:'Type',Container:'Container'}}],$an:function(){return[shared()];},d:['ceylon.language.meta.model','Method']};};
exports.AppliedMethod=AppliedMethod;
function $init$AppliedMethod(){
    if (AppliedMethod.$$===undefined){
        initTypeProto(AppliedMethod,'ceylon.language.meta.model::AppliedMethod',Basic,Method$meta$model);
        (function($$appliedMethod){
//this area was moved inside AppliedMethod()            
        })(AppliedMethod.$$.prototype);
    }
    return AppliedMethod;
}
exports.$init$AppliedMethod$meta$model=$init$AppliedMethod;
$init$AppliedMethod();

//ClassDefinition AppliedAttribute at X (96:0-101:0)
function AppliedAttribute(pname, atr,$$targs$$,$$appliedAttribute){
    $init$AppliedAttribute();
    if ($$appliedAttribute===undefined) {
      $$appliedAttribute=function(x){return AppliedValue(x,atr, $$targs$$);};
      $$appliedAttribute.$$=AppliedAttribute.$$;
      var dummy=new AppliedAttribute.$$;
      $$appliedAttribute.getT$all=function(){return dummy.getT$all();};
      $$appliedAttribute.getT$name=function(){return dummy.getT$name();};
    }
    set_type_args($$appliedAttribute,$$targs$$);
    Attribute$meta$model($$appliedAttribute.$$targs$$===undefined?$$targs$$:{Type:$$appliedAttribute.$$targs$$.Type,Container:$$appliedAttribute.$$targs$$.Container},$$appliedAttribute);
    $$appliedAttribute.tipo=atr;
    $$appliedAttribute.pname=pname;
  defineAttr($$appliedAttribute,'type',function(){
    var $$atr=this;
    var t = $$atr.tipo.$$metamodel$$;
    if (typeof(t)==='function'){
      t=t();
      $$atr.tipo.$$metamodel$$=t;
      t=t.$t;
    }
    return typeLiteral$meta({Type:t});
  },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Type$meta$model,a:{Type:'Type'}},$cont:AppliedAttribute,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Attribute','$at','type']};});
  //AttributeGetterDefinition declaration at X (100:4-100:83)
  defineAttr($$appliedAttribute,'declaration',function(){
    var $$atr=this;
    var mm = $$atr.tipo.$$metamodel$$;
    if (typeof(mm)==='function') {
      mm=mm();
      $$atr.tipo.$$metamodel$$=mm;
    }
    var pkg = getModules$meta().find(mm.mod['$mod-name'],mm.mod['$mod-version']).findPackage(mm.d[0]);
    return ($$atr.tipo.set ? OpenVariable:OpenValue)(pkg, $$atr.tipo);
  },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:ValueDeclaration$meta$declaration},$cont:AppliedAttribute,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Attribute','$at','declaration']};});
    return $$appliedAttribute;
}
AppliedAttribute.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Basic},$tp:{Container:{'var':'in'},Type:{'var':'out','def':{t:Anything}}},satisfies:[{t:Attribute$meta$model,a:{Type:'Type',Container:'Container'}}],$an:function(){return[shared()];},d:['ceylon.language.meta.model','Attribute']};};
exports.AppliedAttribute=AppliedAttribute;
function $init$AppliedAttribute(){
    if (AppliedAttribute.$$===undefined){
        initTypeProto(AppliedAttribute,'ceylon.language.meta.model::AppliedAttribute',Basic,Attribute$meta$model);
        (function($$appliedAttribute){
//moved to initializer            
        })(AppliedAttribute.$$.prototype);
    }
    return AppliedAttribute;
}
exports.$init$AppliedAttribute=$init$AppliedAttribute;
$init$AppliedAttribute();



//ClassDefinition AppliedVariableAttribute at X (107:0-111:0)
function AppliedVariableAttribute(pname,atr,$$targs$$,that) {
  $init$AppliedVariableAttribute();
  if (that===undefined) {
    that=function(x){return AppliedVariable(x,atr, $$targs$$);};
    that.$$=AppliedVariableAttribute.$$;
    var dummy=new AppliedVariableAttribute.$$;
    that.getT$all=function(){return dummy.getT$all();};
    that.getT$name=function(){return dummy.getT$name();};
  }
  set_type_args(that,$$targs$$);
  AppliedAttribute(pname,atr,{Type:that.$$targs$$.Type,Container:that.$$targs$$.Container},that);
  VariableAttribute$meta$model(that.$$targs$$===undefined?$$targs$$:{Type:that.$$targs$$.Type,Container:that.$$targs$$.Container},that);
  return that;
}
AppliedVariableAttribute.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:AppliedAttribute,a:{Type:'Type',Container:'Container'}},$tp:{Container:{'var':'in'},Type:{}},satisfies:[{t:VariableAttribute$meta$model,a:{Type:'Type',Container:'Container'}}],$an:function(){return[shared()];},d:['ceylon.language.meta.model','VariableAttribute']};};
exports.AppliedVariableAttribute=AppliedVariableAttribute;
function $init$AppliedVariableAttribute(){
    if (AppliedVariableAttribute.$$===undefined){
        initTypeProto(AppliedVariableAttribute,'ceylon.language.meta.model::AppliedVariableAttribute',AppliedAttribute,VariableAttribute$meta$model);
        (function($$appliedVariableAttribute){

        })(AppliedVariableAttribute.$$.prototype);
    }
    return AppliedVariableAttribute;
}
exports.$init$AppliedVariableAttribute=$init$AppliedVariableAttribute;
$init$AppliedVariableAttribute();
