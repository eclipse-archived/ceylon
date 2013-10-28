function AppliedClass(tipo,$$targs$$,that){
  $init$AppliedClass();
  if (that===undefined){
    var mm = tipo.$$metamodel$$;
    if (typeof(mm)==='function') {
      mm=mm(); tipo.$$metamodel$$=mm;
    }
    if (mm && mm.$cont) {
      that=function(x){/*Class*/
        if (that.$targs) {
          var _a=[];
          for (var i=0;i<arguments.length;i++)_a.push(arguments[i]);
          _a.push(that.$targs);
          return tipo.apply(x,_a);
        }
        return tipo.apply(x,arguments);
      }
      var dummy = new AppliedClass.$$;
      that.$$=AppliedClass.$$;
      that.getT$all=function(){return dummy.getT$all();};
      that.getT$name=function(){return dummy.getT$name();};
      that.equals=function(o){
        var eq=isOfType(o,{t:AppliedClass}) && o.tipo===tipo;
        return eq;
      };
      that.$apply=function(x){return AppliedClass.$$.prototype.$apply.call(that,x);};
      that.$apply.$$metamodel$$=AppliedClass.$$.prototype.$apply.$$metamodel$$;
      defineAttr(that,'string',function(){
        return String$($qname(mm));
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},d:['ceylon.language','Object','$at','string']};});
      defineAttr(that,'declaration',function(){
        return ClassModel$meta$model.$$.prototype.$prop$getDeclaration.get.call(that);
      },undefined,ClassModel$meta$model.$$.prototype.$prop$getDeclaration.$$metamodel$$);
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

      $$clase.$apply=function(a){
        var ps = this.tipo.$$metamodel$$.$ps;
        if (ps===undefined || ps.length===0){
          if (a && a.size>0)
            throw InvocationException$meta$model(String$("Wrong number of arguments (should be 0)"));
          return this._targs?this.tipo(_targs) : this.tipo();
        }
        if (a===undefined)a=[];
        if (a.size!=ps.length || ps[ps.length-1].seq) {
          var fa=[];
          var sarg;
          for (var i=0; i<ps.length;i++) { //check def/seq params
            var p=ps[i];
            if (a[i]===undefined) {
              if (p.$def||p.seq)fa.push(undefined);
              else {
                throw InvocationException$meta$model(String$("Wrong number of arguments (should be " + ps.length + ")"));
              }
            } else if (sarg) {
              sarg.push(a[i]);
            } else if (p.seq) {
              sarg=[]; fa.push(sarg);
              for (var j=i; j<a.size;j++)sarg.push(a[j]);
            } else {
              fa.push(a[i]);
            }
          }
          a = fa;
        }
        if (this.tipo._targs)a.push(this.tipo._targs);
        return this.tipo.apply(undefined,a);
      };$$clase.$apply.$$metamodel$$=function(){return{mod:$$METAMODEL$$,d:['ceylon.language.meta.model','Class','$m','apply'],$t:'Type'};};

    })(AppliedClass.$$.prototype);
  }
  return AppliedClass;
}
exports.$init$AppliedClass$meta$model=$init$AppliedClass;
$init$AppliedClass();

function AppliedMemberClass(tipo,$$targs$$,that){
  $init$AppliedMemberClass();
  if (that===undefined) {
    var mm = tipo.$$metamodel$$;
    if (typeof(mm)==='function') {
      mm=mm(); tipo.$$metamodel$$=mm;
    }
    if (mm && mm.$cont) {
      that=function(x){
        var rv=tipo.bind(x);
        rv.$$metamodel$$=tipo.$$metamodel$$;
        rv=AppliedClass(rv,{Type:{t:mm.$cont},Arguments:{t:Nothing}});//TODO Arguments
        if (that.$targs)rv.$targs=that.$targs;
        return rv;
      }
      var dummy = new AppliedMemberClass.$$;
      that.$$=AppliedMemberClass.$$;
      that.getT$all=function(){return dummy.getT$all();};
      that.getT$name=function(){return dummy.getT$name();};
      that.equals=function(o){
        var eq=isOfType(o,{t:AppliedMemberClass}) && o.tipo===tipo;
        if (that.$bound)eq=eq && o.$bound && o.$bound.equals(that.$bound);else eq=eq && o.$bound===undefined;
        return eq;
      };
      defineAttr(that,'parameterTypes',function(){
        return ClassModel$meta$model.$$.prototype.$prop$getParameterTypes.get.call(that);
      },undefined,ClassModel$meta$model.$$.prototype.$prop$getParameterTypes.$$metamodel$$);
      defineAttr(that,'declaration',function(){
        return ClassModel$meta$model.$$.prototype.$prop$getDeclaration.get.call(that);
      },undefined,ClassModel$meta$model.$$.prototype.$prop$getDeclaration.$$metamodel$$);
      that.$bind=function(){return AppliedMemberClass.$$.prototype.$bind.apply(that,arguments);}
      defineAttr(that,'string',function(){
        return String$($qname(mm));
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},d:['ceylon.language','Object','$at','string']};});
    } else {
      throw IncompatibleTypeException("Invalid metamodel data for MemberClass");
    }
  }
  set_type_args(that,$$targs$$);
  MemberClass$meta$model(that.$$targs$$===undefined?$$targs$$:{Arguments:that.$$targs$$.Arguments,Type:that.$$targs$$.Type,Container:that.$$targs$$.Container},that);
  that.tipo=tipo;
  return that;
}
AppliedMemberClass.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Basic},$ps:[],$tp:{Container:{'var':'in'},Type:{'var':'out','def':{t:Anything}},Arguments:{'var':'in','satisfies':[{t:Sequential,a:{Element:{t:Anything}}}],'def':{t:Nothing}}},satisfies:[{t:MemberClass$meta$model,a:{Arguments:'Arguments',Type:'Type',Container:'Container'}}],$an:function(){return[shared(),abstract()];},d:['','AppliedMemberClass']};};
exports.AppliedMemberClass=AppliedMemberClass;
function $init$AppliedMemberClass(){
  if (AppliedMemberClass.$$===undefined){
    initTypeProto(AppliedMemberClass,'ceylon.language.meta.model::AppliedMemberClass',Basic,MemberClass$meta$model);
    (function($$amc){
      
      //MethodDef bind at caca.ceylon (5:4-5:107)
      $$amc.$bind=function $bind(cont){
        var ot=cont.getT$name ? cont.getT$all()[cont.getT$name()]:throwexc(IncompatibleTypeException$meta$model("Container does not appear to be a Ceylon object"));
        if (!ot)throw IncompatibleTypeException$meta$model("Incompatible Container (has no metamodel information");
        var omm=ot.$$metamodel$$;
        if (typeof(omm)==='function'){omm=omm();ot.$$metamodel$$=omm;}
        var mm=this.tipo.$$metamodel$$;
        if (typeof(mm)==='function'){mm=mm();this.tipo.$$metamodel$$=mm;}
        if (!extendsType({t:ot},{t:mm.$cont}))throw IncompatibleTypeException$meta$model("Incompatible container type");
        return this(cont);
      };$$amc.$bind.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Class$meta$model,a:{Arguments:'Arguments',Type:'Type'}},$ps:[{$nm:'container',$mt:'prm',$t:{t:Object$},$an:function(){return[];}}],$cont:MemberClass$meta$model,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','MemberClass','$m','bind']};};
    })(AppliedMemberClass.$$.prototype);
  }
  return AppliedMemberClass;
}
exports.$init$AppliedMemberClass$meta$model=$init$AppliedMemberClass;
$init$AppliedMemberClass();

function AppliedInterface(tipo,$$targs$$,that) {
  $init$AppliedInterface();
  if (that===undefined){
    var mm = tipo.$$metamodel$$;
    if (typeof(mm)==='function') {
      mm=mm(); tipo.$$metamodel$$=mm;
    }
    if (mm && mm.$cont) {
      that=function(x){
        that.tipo=function(){return tipo.apply(x,arguments);};
        that.$bound=x;
        return that;
      }
      that.tipo$2=tipo;
      var dummy = new AppliedInterface.$$;
      that.$$=AppliedInterface.$$;
      that.getT$all=function(){return dummy.getT$all();};
      that.getT$name=function(){return dummy.getT$name();};
      that.equals=function(o){
        var eq=isOfType(o,{t:AppliedInterface}) && (o.tipo$2||o.tipo)==tipo;
        if (that.$bound)eq=eq && o.$bound && o.$bound.equals(that.$bound);else eq=eq && o.$bound===undefined;
        return eq;
      };
      defineAttr(that,'string',function(){
        return String$($qname(mm));
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},d:['ceylon.language','Object','$at','string']};});
      defineAttr(that,'declaration',function(){
        return InterfaceModel$meta$model.$$.prototype.$prop$getDeclaration.get.call(that);
      },undefined,InterfaceModel$meta$model.$$.prototype.$prop$getDeclaration.$$metamodel$$);
    } else {
      that=new AppliedInterface.$$;
    }
  }
  set_type_args(that,$$targs$$);
  Interface$meta$model($$targs$$,that);
  that.tipo=tipo;
  return that;
}
AppliedInterface.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Basic},$tp:{Type:{'var':'out','def':{t:Anything}}},satisfies:[{t:Interface$meta$model,a:{Type:'Type'}}],$an:function(){return[shared()];},d:['ceylon.language.meta.model','Interface']};};
exports.AppliedInterface=AppliedInterface;

function $init$AppliedInterface(){
  if (AppliedInterface.$$===undefined){
    initTypeProto(AppliedInterface,'ceylon.language.meta.model::AppliedInterface',Basic,Interface$meta$model);
    (function($$appliedInterface){

        })(AppliedInterface.$$.prototype);
    }
    return AppliedInterface;
}
exports.$init$AppliedInterface$meta$model=$init$AppliedInterface;
$init$AppliedInterface();

function AppliedMemberInterface(tipo,$$targs$$,that){
  $init$AppliedMemberInterface();
  if (that===undefined){
    var mm = tipo.$$metamodel$$;
    if (typeof(mm)==='function') {
      mm=mm(); tipo.$$metamodel$$=mm;
    }
    if (mm && mm.$cont) {
      that=function(x){
        that.tipo=function(){return tipo.apply(x,arguments);};
        that.$bound=x;
        return that;
      }
      that.tipo$2=tipo;
      var dummy = new AppliedMemberInterface.$$;
      that.$$=AppliedMemberInterface.$$;
      that.getT$all=function(){return dummy.getT$all();};
      that.getT$name=function(){return dummy.getT$name();};
      that.equals=function(o){
        var eq=isOfType(o,{t:AppliedMemberInterface}) && (o.tipo$2||o.tipo)==tipo;
        if (that.$bound)eq=eq && o.$bound && o.$bound.equals(that.$bound);else eq=eq && o.$bound===undefined;
        return eq;
      };
      defineAttr(that,'string',function(){
        return String$($qname(mm));
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},d:['ceylon.language','Object','$at','string']};});
      defineAttr(that,'declaration',function(){
        return InterfaceModel$meta$model.$$.prototype.$prop$getDeclaration.get.call(that);
      },undefined,InterfaceModel$meta$model.$$.prototype.$prop$getDeclaration.$$metamodel$$);
      that.$bind=function(x){return AppliedMemberInterface.$$.prototype.$bind.call(that,x);}
    } else {
      that=new AppliedMemberInterface.$$;
    }
  }
  set_type_args(that,$$targs$$);
  MemberInterface$meta$model(that.$$targs$$===undefined?$$targs$$:{Type:that.$$targs$$.Type,Container:that.$$targs$$.Container},that);
  that.tipo=tipo;
  return that;
}
AppliedMemberInterface.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Basic},$ps:[],$tp:{Container:{'var':'in'},Type:{'var':'out','def':{t:Anything}}},satisfies:[{t:MemberInterface$meta$model,a:{Type:'Type',Container:'Container'}}],$an:function(){return[shared(),abstract()];},d:['ceylon.language.meta.model','MemberInterface']};};
exports.AppliedMemberInterface=AppliedMemberInterface;
function $init$AppliedMemberInterface(){
  if (AppliedMemberInterface.$$===undefined){
    initTypeProto(AppliedMemberInterface,'ceylon.language.meta.model::AppliedMemberInterface',Basic,MemberInterface$meta$model);
    (function($$appliedMemberInterface){
      $$appliedMemberInterface.$bind=function $bind(container$2){
        var $$appliedMemberInterface=this;
        throw wrapexc(Exception(String$("IMPL MemberInterface.bind",25)),'5:59-5:103','caca.ceylon');
      };$$appliedMemberInterface.$bind.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Interface$meta$model,a:{Type:'Type'}},$ps:[{$nm:'container',$mt:'prm',$t:{t:Object$},$an:function(){return[];}}],$cont:MemberInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','MemberInterface','$m','bind']};};
    })(AppliedMemberInterface.$$.prototype);
  }
  return AppliedMemberInterface;
}
exports.$init$AppliedMemberInterface$meta$model=$init$AppliedMemberInterface;
$init$AppliedMemberInterface();
    

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

defineAttr($$appliedUnionType,'string',function(){
  var qn="";
  var first=true;
  for (var i=0;i<this._types.length;i++) {
    if (first)first=false;else qn+="|";
    qn+=this._types[i].string;
  }
  return String$(qn);
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},d:['ceylon.language','Object','$at','string']};});
defineAttr($$appliedUnionType,'hash',function(){
  var h=this._types.length;
  for (var i=0; i<this._types.length;i++) {
    h+=this._types[i].hash;
  }
  return h;
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Integer},d:['ceylon.language','Object','$at','hash']};});

      $$appliedUnionType.equals=function(u) {
        if(isOfType(u,{t:AppliedUnionType})) {
          var mine=this._types;
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

defineAttr($$appliedIntersectionType,'string',function(){
  var qn="";
  var first=true;
  for (var i=0;i<this._types.length;i++) {
    if (first)first=false;else qn+="&";
    qn+=this._types[i].string;
  }
  return String$(qn);
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},d:['ceylon.language','Object','$at','string']};});
defineAttr($$appliedIntersectionType,'hash',function(){
  var h=this._types.length;
  for (var i=0; i<this._types.length;i++) {
    h+=this._types[i].hash;
  }
  return h;
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Integer},d:['ceylon.language','Object','$at','hash']};});
      $$appliedIntersectionType.equals=function(u) {
        if(isOfType(u,{t:AppliedIntersectionType})) {
          var mine=this._types;
          var his=u.satisfiedTypes;
          if (mine.size==his.size) {
            for (var i=0;i<mine.length;i++) {
              if (!his.contains(mine[i]))return false;
            }
            return true;
          }
        }
        return false;
      }
 
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

function AppliedFunction(m,$$targs$$,o,mptypes) {
  var mm=m.$$metamodel$$;
  if (typeof(mm)==='function') {mm=mm();m.$$metamodel$$=mm;}
  var ttargs;
  if (mm.$tp) {
    if (!mptypes || mptypes.size<1)throw TypeApplicationException$meta$model("Missing type arguments for AppliedFunction");
    var i=0;ttargs={};
    for (var tp in mm.$tp) {
      var _ta=mptypes.$get?mptypes.$get(i):mptypes[i];
      if(_ta&&_ta.tipo)ttargs[tp]={t:_ta.tipo};
      else if (_ta) console.log("TODO assign type arg " + _ta + " to " + tp);
      else if (mptypes[tp])ttargs[tp]=mptypes[tp];
      else throw Error("TODO no more type arguments in AppliedFunction");
      i++;
    }
  }
  var f = o===undefined?function(x){
    return AppliedFunction(m,$$targs$$,x,mptypes);
  }:function(){
    if (mm.$tp) {
      var _a=[];
      for (var i=0;i<arguments.length;i++)_a.push(arguments[i]);
      _a.push(ttargs);//AQUI
      return m.apply(o,_a);
    }
    return m.apply(o,arguments);
  }
  f.$$metamodel$$={mod:$$METAMODEL$$,d:['ceylon.language.model','Function'],$t:mm.$t,$ps:mm.$ps,$an:mm.$an};
  var dummy=new AppliedFunction.$$;
  f.getT$all=function(){return dummy.getT$all();}
  f.getT$name=function(){return dummy.getT$name();}
  if ($$targs$$===undefined) {
    throw TypeApplicationException$meta$model("Missing type arguments for AppliedFunction");
  } else {
    f.$$targs$$=$$targs$$;
  }
  Function$meta$model(f,$$targs$$);
  f.tipo=m;
  f.$targs=ttargs;
  if (o)f.$bound=o;
  defineAttr(f,'typeArguments',function(){
    return FunctionModel$meta$model.$$.prototype.$prop$getTypeArguments.get.call(f);
  },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Map,a:{Key:{t:TypeParameter$meta$declaration},Item:{t:Type$meta$model,a:{Type:{t:Anything}}}}},$cont:AppliedFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Generic','$at','typeArguments']};});
  f.equals=function(oo){
    return isOfType(oo,{t:AppliedFunction}) && oo.tipo===m && oo.typeArguments.equals(this.typeArguments) && (o?o.equals(oo.$bound):oo.$bound===o);
  }
  defineAttr(f,'string',function(){
    return FunctionModel$meta$model.$$.prototype.$prop$getString.get.call(f);
  },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},d:['ceylon.language','Object','$at','string'],$cont:AppliedFunction};});
  defineAttr(f,'parameterTypes',function(){
    return FunctionModel$meta$model.$$.prototype.$prop$getParameterTypes.get.call(f);
  },undefined,FunctionModel$meta$model.$$.prototype.$prop$getParameterTypes.$$metamodel$$);
defineAttr(f,'declaration',function(){
  if (f._decl)return f._decl;
  f._decl = OpenFunction(getModules$meta().find(mm.mod['$mod-name'],mm.mod['$mod-version']).findPackage(mm.d[0]), m);
  return f._decl;
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:FunctionDeclaration$meta$declaration},d:['ceylon.language.meta.model','FunctionModel','$at','declaration']};});
  f.$apply=function(a){
    if (ttargs) {
      var _a=[];
      for (var i=0;i<a.size;i++)_a.push(a.$get(i));
      _a.push(ttargs);
      a=_a;
    }
    return m.apply(o,a);
  }
  return f;
}
AppliedFunction.$$metamodel$$=function(){return{mod:$$METAMODEL$$,d:['ceylon.language.meta.model','Function'],satisfies:{t:Function$meta$model,a:{Type:'Type',Arguments:'Arguments'}},$an:function(){return [shared(),actual()];}};};
exports.AppliedFunction$meta$model=AppliedFunction;
initTypeProto(AppliedFunction,'ceylon.language.meta.model::AppliedFunction',Basic,Function$meta$model);

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
defineAttr($$appliedValue,'string',function(){
  var qn;
  if ($$targs$$ && $$targs$$.Container) {
    qn = typeLiteral$meta({Type:$$targs$$.Container}).string + "." + mm.d[mm.d.length-1];
  } else if (mm.$cont) {
    qn = typeLiteral$meta({Type:{t:mm.$cont}}).string + "." + mm.d[mm.d.length-1];
  } else {
    qn=$qname(mm);
  }
  return String$(qn);
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},d:['ceylon.language','Object','$at','string']};});
    }
  }
  set_type_args($$appliedValue,$$targs$$);
  Value$meta$model($$appliedValue.$$targs$$===undefined?$$targs$$:{Type:$$appliedValue.$$targs$$.Type},$$appliedValue);
  if($$targs$$.Container)Attribute$meta$model($$targs$$,$$appliedValue);
  $$appliedValue.obj=obj;
  $$appliedValue.tipo=attr;
  return $$appliedValue;
}
AppliedValue.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Basic},$tp:{Type:{'var':'out'}},satisfies:[{t:Value$meta$model,a:{Type:'Type'}}],$an:function(){return[shared()];},d:['ceylon.language.meta.model','Value']};};
exports.AppliedValue$meta$model=AppliedValue;
function $init$AppliedValue(){
  if (AppliedValue.$$===undefined){
    initTypeProto(AppliedValue,'ceylon.language.meta.model::AppliedValue',Basic,Value$meta$model,Attribute$meta$model);
    (function($$appliedValue){
defineAttr($$appliedValue,'string',function(){
  return String$($qname(this.tipo.$$metamodel$$));
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},d:['ceylon.language','Object','$at','string']};});
      defineAttr($$appliedValue,'declaration',function(){
        var $$av=this;
        var mm = $$av.tipo.$$metamodel$$;
        var _pkg = getModules$meta().find(mm.mod['$mod-name'],mm.mod['$mod-version']).findPackage(mm.d[0]);
        return OpenValue(_pkg, $$av.tipo);
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:ValueDeclaration$meta$declaration},$cont:AppliedValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Value','$at','declaration']};});

      $$appliedValue.$get=function $get(){
        var $$av=this;
        return $$av.obj?$$av.tipo.get.apply($$av.obj):$$av.tipo.get();
      };$$appliedValue.$get.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:'Type',$ps:[],$cont:AppliedValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Value','$m','get']};};

$$appliedValue.unsafeSet=function(v) {
  var mm = this.tipo.$$metamodel$$;
  if (!isOfType(v,mm.$t))throw IncompatibleTypeException$meta$model("The specified value has the wrong type");
  var mdl=get_model(mm);
  if (!(mdl &&mdl['var']))throw MutationException$meta$model("Attempt to modify a value that is not variable");
  this.obj?this.tipo.set.call(this.obj,v):this.tipo.set(v);
};$$appliedValue.unsafeSet.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$ps:[],$cont:AppliedValue,d:['ceylon.language.meta.model','Value','$m','unsafeSet']};};

      defineAttr($$appliedValue,'type',function(){
          var $$atr=this;
          var t = $$atr.tipo.$$metamodel$$;
          return typeLiteral$meta({Type:t.$t});
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Type$meta$model,a:{Type:'Type'}},$cont:AppliedValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Value','$at','type']};});

      defineAttr($$appliedValue,'container',function(){
          if (this.$$targs$$.Container) {
            return typeLiteral$meta({Type:this.$$targs$$.Container});
          }
          var mm=this.tipo.$$metamodel$$;
          if (mm.$cont) {
            return typeLiteral$meta({Type:{t:mm.$cont}});
          }
          return null;
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
  var mm = tipo.$$metamodel$$;
  if (typeof(mm)==='function') {
    mm = mm();
    tipo.$$metamodel$$=mm;
  }
  if (mm.$tp) {
    if (typeArgs===undefined || typeArgs.size<1)
      throw TypeApplicationException$meta$model(String$("Missing type arguments in call to FunctionDeclaration.apply"));
    var _ta={}; var i=0;
    for (var tp in mm.$tp) {
      if (typeArgs.$get(i)===undefined)
        throw TypeApplicationException$meta$model(String$("Missing type argument for "+tp));
      var _tp = mm.$tp[tp];
      var _t = typeArgs.$get(i).tipo;
      _ta[tp]={t:_t};
      if ((_tp.satisfies && _tp.satisfies.length>0) || (_tp.of && _tp.of.length > 0)) {
        var restraints=(_tp.satisfies && _tp.satisfies.length>0)?_tp.satisfies:_tp.of;
        for (var j=0; j<restraints.length;j++) {
          if (!extendsType(_ta[tp],restraints[j]))
            throw TypeApplicationException$meta$model(String$("Type argument for " + tp + " violates type parameter constraints"));
        }
      }
      i++;
    }
  }
  if ($$appliedMethod===undefined){
    $$appliedMethod=function(x){
      return AppliedFunction(tipo,$$targs$$,x,typeArgs);
    }
    var dummy=new AppliedMethod.$$;
    $$appliedMethod.getT$all=function(){return dummy.getT$all();};
    $$appliedMethod.getT$name=function(){return dummy.getT$name();};
  }
  if (_ta)$$appliedMethod.$targs=_ta;
  set_type_args($$appliedMethod,$$targs$$);
  Method$meta$model($$appliedMethod.$$targs$$===undefined?$$targs$$:{Arguments:$$appliedMethod.$$targs$$.Arguments,Type:$$appliedMethod.$$targs$$.Type,Container:$$appliedMethod.$$targs$$.Container},$$appliedMethod);
  $$appliedMethod.tipo=tipo;

//This was copied from prototype style
  defineAttr($$appliedMethod,'declaration',function(){
    var _pkg = getModules$meta().find(mm.mod['$mod-name'],mm.mod['$mod-version']).findPackage(mm.d[0]);
    return OpenFunction(_pkg, $$appliedMethod.tipo);
  },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:FunctionDeclaration$meta$declaration},$cont:AppliedMethod,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Method','$at','declaration']};});

  defineAttr($$appliedMethod,'type',function(){
    return typeLiteral$meta({Type:mm.$t});
  },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Type$meta$model,a:{Type:'Type'}},$cont:AppliedMethod,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Method','$at','type']};});

  defineAttr($$appliedMethod,'typeArguments',function(){
    return FunctionModel$meta$model.$$.prototype.$prop$getTypeArguments.get.call($$appliedMethod);
  },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Map,a:{Key:{t:TypeParameter$meta$declaration},Item:{t:Type$meta$model,a:{Type:{t:Anything}}}}},$cont:AppliedMethod,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Generic','$at','typeArguments']};});
  defineAttr($$appliedMethod,'parameterTypes',function(){
    return FunctionModel$meta$model.$$.prototype.$prop$getParameterTypes.get.call($$appliedMethod);
  },undefined,FunctionModel$meta$model.$$.prototype.$prop$getParameterTypes.$$metamodel$$);

  $$appliedMethod.equals=function(o){
    return isOfType(o,{t:AppliedMethod}) && o.tipo===tipo && o.typeArguments.equals(this.typeArguments);
  }
  $$appliedMethod.$bind=function(o){
    if (!isOfType(o,{t:mm.$cont}))throw IncompatibleTypeException$meta$model("Cannot bind " + $$appliedMethod.string + " to "+o);
    return $$appliedMethod(o);
  }
  defineAttr($$appliedMethod,'string',function(){
    return FunctionModel$meta$model.$$.prototype.$prop$getString.get.call($$appliedMethod);
  },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},d:['ceylon.language','Object','$at','string'],$cont:AppliedMethod};});
  return $$appliedMethod;
}
AppliedMethod.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Basic},$tp:{Container:{'var':'in'},Type:{'var':'out','def':{t:Anything}},Arguments:{'var':'in','satisfies':[{t:Sequential,a:{Element:{t:Anything}}}],'def':{t:Nothing}}},satisfies:[{t:Method$meta$model,a:{Arguments:'Arguments',Type:'Type',Container:'Container'}}],$an:function(){return[shared()];},d:['ceylon.language.meta.model','Method']};};
exports.AppliedMethod$meta$model=AppliedMethod;
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
    var t = atr.$$metamodel$$;
    if (typeof(t)==='function'){
      t=t();
      atr.$$metamodel$$=t;
    }
    t=t.$t;
    return typeLiteral$meta({Type:t});
  },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Type$meta$model,a:{Type:'Type'}},$cont:AppliedAttribute,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Attribute','$at','type']};});
  //AttributeGetterDefinition declaration at X (100:4-100:83)
  defineAttr($$appliedAttribute,'declaration',function(){
    var mm = atr.$$metamodel$$;
    if (typeof(mm)==='function') {
      mm=mm();
      atr.$$metamodel$$=mm;
    }
    var pkg = getModules$meta().find(mm.mod['$mod-name'],mm.mod['$mod-version']).findPackage(mm.d[0]);
    return (atr.set ? OpenVariable:OpenValue)(pkg, atr);
  },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:ValueDeclaration$meta$declaration},$cont:AppliedAttribute,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Attribute','$at','declaration']};});
  $$appliedAttribute.$bind=function(cont){
    return AppliedValue(cont,atr,{Type:$$targs$$.Type});
  }
  defineAttr($$appliedAttribute,'string',function(){
    return String$($qname(atr.$$metamodel$$));
  },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},d:['ceylon.language','Object','$at','string']};});
  $$appliedAttribute.equals=function(o) {
    return isOfType(o,{t:AppliedAttribute}) && o.string.equals(this.string);
  }
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
