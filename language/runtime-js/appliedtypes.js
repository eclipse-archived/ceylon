function AppliedClass(tipo,$$targs$$,that){
  $init$AppliedClass();
  if (that===undefined){
    var mm = getrtmm$$(tipo);
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
    } else {
      that=function(){
        if (that.$targs) {
          var _a=[];
          for (var i=0;i<arguments.length;i++)_a.push(arguments[i]);
          _a.push(that.$targs);
          return tipo.apply(undefined,_a);
        }
        return tipo.apply(undefined,arguments);
      }
    }
    that.$crtmm$=mm;
    var dummy = new AppliedClass.$$;
    that.$$=AppliedClass.$$;
    that.getT$all=function(){return dummy.getT$all();};
    that.getT$name=function(){return dummy.getT$name();};
    that.equals=function(o){
      var eq=$is(o,{t:AppliedClass}) && o.tipo===tipo;
      return eq;
    };
    that.$apply=function(x){return AppliedClass.$$.prototype.$apply.call(that,x);};
    that.$apply.$crtmm$=AppliedClass.$$.prototype.$apply.$crtmm$;
    $defat(that,'satisfiedTypes',function(){
      return ClassOrInterface$meta$model.$$.prototype.$prop$getSatisfiedTypes.get.call(that);
    },undefined,ClassOrInterface$meta$model.$$.prototype.$prop$getExtendedType.$crtmm$);
    $defat(that,'container',function(){
      return ClassOrInterface$meta$model.$$.prototype.$prop$getContainer.get.call(that);
    },undefined,ClassOrInterface$meta$model.$$.prototype.$prop$getContainer.$crtmm$);
    $defat(that,'string',function(){
      return ClassOrInterface$meta$model.$$.prototype.$prop$getString.get.call(that);
    },undefined,ClassOrInterface$meta$model.$$.prototype.$prop$getString.$crtmm$);
    $defat(that,'hash',function(){
      return ClassOrInterface$meta$model.$$.prototype.$prop$getHash.get.call(that);
    },undefined,ClassOrInterface$meta$model.$$.prototype.$prop$getHash.$crtmm$);
    $defat(that,'typeArguments',function(){
      return ClassOrInterface$meta$model.$$.prototype.$prop$getTypeArguments.get.call(that);
    },undefined,ClassOrInterface$meta$model.$$.prototype.$prop$getTypeArguments.$crtmm$);
    $defat(that,'extendedType',function(){
      return ClassOrInterface$meta$model.$$.prototype.$prop$getExtendedType.get.call(that);
    },undefined,ClassOrInterface$meta$model.$$.prototype.$prop$getExtendedType.$crtmm$);
    $defat(that,'declaration',function(){
      return ClassModel$meta$model.$$.prototype.$prop$getDeclaration.get.call(that);
    },undefined,ClassModel$meta$model.$$.prototype.$prop$getDeclaration.$crtmm$);
    $defat(that,'parameterTypes',function(){
      return ClassModel$meta$model.$$.prototype.$prop$getParameterTypes.get.call(that);
    },undefined,ClassModel$meta$model.$$.prototype.$prop$getParameterTypes.$crtmm$);
    $defat(that,'declaration',function(){
      return ClassModel$meta$model.$$.prototype.$prop$getDeclaration.get.call(that);
    },undefined,ClassModel$meta$model.$$.prototype.$prop$getDeclaration.$crtmm$);
    that.getMethod=ClassOrInterface$meta$model.$$.prototype.getMethod;
    that.getDeclaredMethod=ClassOrInterface$meta$model.$$.prototype.getDeclaredMethod;
    that.getAttribute=ClassOrInterface$meta$model.$$.prototype.getAttribute;
    that.getDeclaredAttribute=ClassOrInterface$meta$model.$$.prototype.getDeclaredAttribute;
    that.getClassOrInterface=ClassOrInterface$meta$model.$$.prototype.getClassOrInterface;
    that.getDeclaredClassOrInterface=ClassOrInterface$meta$model.$$.prototype.getDeclaredClassOrInterface;
    that.getClass=ClassOrInterface$meta$model.$$.prototype.getClass;
    that.getDeclaredClass=ClassOrInterface$meta$model.$$.prototype.getDeclaredClass;
    that.getInterface=ClassOrInterface$meta$model.$$.prototype.getInterface;
    that.getDeclaredInterface=ClassOrInterface$meta$model.$$.prototype.getDeclaredInterface;
    that.equals=ClassModel$meta$model.$$.prototype.equals;
    that.typeOf=ClassOrInterface$meta$model.$$.prototype.typeOf;
    that.supertypeOf=ClassOrInterface$meta$model.$$.prototype.supertypeOf;
    that.subtypeOf=ClassOrInterface$meta$model.$$.prototype.subtypeOf;
    that.exactly=ClassOrInterface$meta$model.$$.prototype.exactly;
  }
  set_type_args(that,$$targs$$);
  Class$meta$model(that.$$targs$$===undefined?$$targs$$:{Arguments$Class:that.$$targs$$.Arguments$Class,Type$Class:that.$$targs$$.Type$Class},that);
  that.tipo=tipo;
  return that;
}
AppliedClass.$crtmm$=function(){return{mod:$CCMM$,'super':{t:Basic},$tp:{Type$Class:{'var':'out','def':{t:Anything}},Arguments$Class:{'var':'in','satisfies':[{t:Sequential,a:{Element$Iterable:{t:Anything}}}],'def':{t:Nothing}}},satisfies:[{t:Class$meta$model,a:{Arguments$Class:'Arguments$Class',Type$Class:'Type$Class'}}],$an:function(){return[shared()];},d:['ceylon.language.meta.model','Class']};};
function $init$AppliedClass(){
  if (AppliedClass.$$===undefined){
    initTypeProto(AppliedClass,'ceylon.language.meta.model::AppliedClass',Basic,Class$meta$model);
    (function($$clase){

      $$clase.$apply=function(a){
        var mdl=get_model(this.tipo.$crtmm$);
        if (mdl&&mdl.$mt==='o')throw InvocationException$meta$model("Cannot instantiate anonymous class");
        a=convert$params(this.tipo.$crtmm$,a);
        if (this.$targs)a.push(this.$targs);
        return this.tipo.apply(undefined,a);
      };$$clase.$apply.$crtmm$=function(){return{mod:$CCMM$,d:['ceylon.language.meta.model','Class','$m','apply'],$t:'Type'};};

    })(AppliedClass.$$.prototype);
  }
  return AppliedClass;
}
exports.$init$AppliedClass$meta$model=$init$AppliedClass;
$init$AppliedClass();

function AppliedMemberClass(tipo,$$targs$$,that){
  $init$AppliedMemberClass();
  if (that===undefined) {
    var mm = getrtmm$$(tipo);
    if (mm && mm.$cont) {
      that=function(x){
        var rv=tipo.bind(x);
        rv.$crtmm$=tipo.$crtmm$;
        var nt={t:tipo};
        if (x.$$targs$$) {
          nt.a={};
          for (var nta in x.$$targs$$)nt.a[nta]=x.$$targs$$[nta];
        }
        if (that.$targs) {
          if (!nt.a)nt.a={};
          for (var nta in that.$targs)nt.a[nta]=that.$targs[nta];
        }
        rv=AppliedClass(rv,{Type$Class:nt,Arguments$Class:{t:Sequential,a:{Element$Iterable:{t:Anything},Absent$Iterable:{t:Null}}}});//TODO generate metamodel for Arguments
        if (nt.a)rv.$targs=nt.a;
        rv.$bound=x;
        return rv;
      }
      var dummy = new AppliedMemberClass.$$;
      that.$$=AppliedMemberClass.$$;
      that.getT$all=function(){return dummy.getT$all();};
      that.getT$name=function(){return dummy.getT$name();};
      that.equals=function(o){
        var eq=$is(o,{t:AppliedMemberClass}) && o.tipo===tipo;
        if (that.$bound)eq=eq && o.$bound && o.$bound.equals(that.$bound);else eq=eq && o.$bound===undefined;
        return eq;
      };
      $defat(that,'parameterTypes',function(){
        return ClassModel$meta$model.$$.prototype.$prop$getParameterTypes.get.call(that);
      },undefined,ClassModel$meta$model.$$.prototype.$prop$getParameterTypes.$crtmm$);
      $defat(that,'extendedType',function(){
        return ClassOrInterface$meta$model.$$.prototype.$prop$getExtendedType.get.call(that);
      },undefined,ClassOrInterface$meta$model.$$.prototype.$prop$getExtendedType.$crtmm$);
      $defat(that,'satisfiedTypes',function(){
        return ClassOrInterface$meta$model.$$.prototype.$prop$getSatisfiedTypes.get.call(that);
      },undefined,ClassOrInterface$meta$model.$$.prototype.$prop$getExtendedType.$crtmm$);
      $defat(that,'declaration',function(){
        return ClassModel$meta$model.$$.prototype.$prop$getDeclaration.get.call(that);
      },undefined,ClassModel$meta$model.$$.prototype.$prop$getDeclaration.$crtmm$);
      that.$bind=function(){return AppliedMemberClass.$$.prototype.$bind.apply(that,arguments);}
      $defat(that,'string',function(){
        return String$($qname(mm));
},undefined,function(){return{mod:$CCMM$,$t:{t:String$},d:['ceylon.language','Object','$at','string']};});
    } else {
      throw IncompatibleTypeException$meta$model("Invalid metamodel data for MemberClass");
    }
  }
  set_type_args(that,$$targs$$);
  MemberClass$meta$model(that.$$targs$$===undefined?$$targs$$:{Arguments$MemberClass:that.$$targs$$.Arguments$MemberClass,Type$MemberClass:that.$$targs$$.Type$MemberClass,Container$MemberClass:that.$$targs$$.Container$MemberClass},that);
  that.tipo=tipo;
  return that;
}
AppliedMemberClass.$crtmm$=function(){return{mod:$CCMM$,'super':{t:Basic},$ps:[],$tp:{Container$MemberClass:{'var':'in'},Type$MemberClass:{'var':'out','def':{t:Anything}},Arguments$MemberClass:{'var':'in','satisfies':[{t:Sequential,a:{Element$Iterable:{t:Anything}}}],'def':{t:Nothing}}},satisfies:[{t:MemberClass$meta$model,a:{Arguments$MemberClass:'Arguments$MemberClass',Type$MemberClass:'Type$MemberClass',Container$MemberClass:'Container$MemberClass'}}],$an:function(){return[shared(),abstract()];},d:['','AppliedMemberClass']};};
exports.AppliedMemberClass=AppliedMemberClass;
function $init$AppliedMemberClass(){
  if (AppliedMemberClass.$$===undefined){
    initTypeProto(AppliedMemberClass,'ceylon.language.meta.model::AppliedMemberClass',Basic,MemberClass$meta$model);
    (function($$amc){
      
      //MethodDef bind at caca.ceylon (5:4-5:107)
      $$amc.$bind=function $bind(cont){
        var ot=cont.getT$name ? cont.getT$all()[cont.getT$name()]:throwexc(IncompatibleTypeException$meta$model("Container does not appear to be a Ceylon object"));
        if (!ot)throw IncompatibleTypeException$meta$model("Incompatible Container (has no metamodel information");
        var omm=getrtmm$$(ot);
        var mm=getrtmm$$(this.tipo);
        if (!extendsType({t:ot},{t:mm.$cont}))throw IncompatibleTypeException$meta$model("Incompatible container type");
        return this(cont);
      };$$amc.$bind.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Class$meta$model,a:{Arguments$Class:'Arguments',Type$Class:'Type'}},$ps:[{$nm:'container',$mt:'prm',$t:{t:Object$},$an:function(){return[];}}],$cont:MemberClass$meta$model,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','MemberClass','$m','bind']};};
    })(AppliedMemberClass.$$.prototype);
  }
  return AppliedMemberClass;
}
exports.$init$AppliedMemberClass$meta$model=$init$AppliedMemberClass;
$init$AppliedMemberClass();

function AppliedInterface(tipo,$$targs$$,that) {
  $init$AppliedInterface();
  if (that===undefined){
    var mm = getrtmm$$(tipo);
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
        var eq=$is(o,{t:AppliedInterface}) && (o.tipo$2||o.tipo)==tipo;
        if (that.$bound)eq=eq && o.$bound && o.$bound.equals(that.$bound);else eq=eq && o.$bound===undefined;
        return eq;
      };
      $defat(that,'string',function(){
        return String$($qname(mm));
},undefined,function(){return{mod:$CCMM$,$t:{t:String$},d:['ceylon.language','Object','$at','string']};});
      $defat(that,'declaration',function(){
        return InterfaceModel$meta$model.$$.prototype.$prop$getDeclaration.get.call(that);
      },undefined,InterfaceModel$meta$model.$$.prototype.$prop$getDeclaration.$crtmm$);
    } else {
      that=new AppliedInterface.$$;
    }
  }
  set_type_args(that,$$targs$$);
  Interface$meta$model($$targs$$,that);
  that.tipo=tipo;
  return that;
}
AppliedInterface.$crtmm$=function(){return{mod:$CCMM$,'super':{t:Basic},$tp:{Type$Interface:{'var':'out','def':{t:Anything}}},satisfies:[{t:Interface$meta$model,a:{Type$Interface:'Type$Interface'}}],$an:function(){return[shared()];},d:['ceylon.language.meta.model','Interface']};};
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
    var mm = getrtmm$$(tipo);
    if (mm && mm.$cont) {
      that=function(x){
        var rv=tipo.bind(x);
        rv.$crtmm$=tipo.$crtmm$;
        var nt={t:tipo};
        if (x.$$targs$$) {
          nt.a={};
          for (var nta in x.$$targs$$)nt.a[nta]=x.$$targs$$[nta];
        }
        if (that.$targs) {
          if (!nt.a)nt.a={};
          for (var nta in that.$targs)nt.a[nta]=that.$targs[nta];
        }
        rv=AppliedInterface(rv,{Type$Interface:nt});
        if (nt.a)rv.$targs=nt.a;
        rv.$bound=x;
        return rv;
      }
      that.tipo$2=tipo;
      var dummy = new AppliedMemberInterface.$$;
      that.$$=AppliedMemberInterface.$$;
      that.getT$all=function(){return dummy.getT$all();};
      that.getT$name=function(){return dummy.getT$name();};
      that.equals=function(o){
        var eq=$is(o,{t:AppliedMemberInterface}) && (o.tipo$2||o.tipo)==tipo;
        if (that.$bound)eq=eq && o.$bound && o.$bound.equals(that.$bound);else eq=eq && o.$bound===undefined;
        return eq;
      };
      $defat(that,'string',function(){
        return String$($qname(mm));
},undefined,function(){return{mod:$CCMM$,$t:{t:String$},d:['ceylon.language','Object','$at','string']};});
      $defat(that,'declaration',function(){
        return InterfaceModel$meta$model.$$.prototype.$prop$getDeclaration.get.call(that);
      },undefined,InterfaceModel$meta$model.$$.prototype.$prop$getDeclaration.$crtmm$);
      that.$bind=function(x){return AppliedMemberInterface.$$.prototype.$bind.call(that,x);}
    } else {
      that=new AppliedMemberInterface.$$;
    }
  }
  set_type_args(that,$$targs$$);
  MemberInterface$meta$model(that.$$targs$$===undefined?$$targs$$:{Type$MemberInterface:that.$$targs$$.Type$MemberInterface,
    Container$MemberInterface:that.$$targs$$.Container$MemberInterface},that);
  that.tipo=tipo;
  return that;
}
AppliedMemberInterface.$crtmm$=function(){return{mod:$CCMM$,'super':{t:Basic},$ps:[],$tp:{Container$MemberInterface:{'var':'in'},Type$MemberInterface:{'var':'out','def':{t:Anything}}},satisfies:[{t:MemberInterface$meta$model,a:{Type$MemberInterface:'Type$MemberInterface',Container$MemberInterface:'Container$MemberInterface'}}],$an:function(){return[shared(),abstract()];},d:['ceylon.language.meta.model','MemberInterface']};};
exports.AppliedMemberInterface=AppliedMemberInterface;
function $init$AppliedMemberInterface(){
  if (AppliedMemberInterface.$$===undefined){
    initTypeProto(AppliedMemberInterface,'ceylon.language.meta.model::AppliedMemberInterface',Basic,MemberInterface$meta$model);
    (function($$appliedMemberInterface){
      $$appliedMemberInterface.$bind=function $bind(container$2){
        var $$appliedMemberInterface=this;
        throw Exception(String$("IMPL MemberInterface.bind"));
      };$$appliedMemberInterface.$bind.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Interface$meta$model,a:{Type$Interface:'Type$Interface'}},$ps:[{$nm:'container',$mt:'prm',$t:{t:Object$},$an:function(){return[];}}],$cont:MemberInterface,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','MemberInterface','$m','bind']};};
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
    UnionType$meta$model(that.$$targs$$===undefined?$$targs$$:{Union$UnionType:that.$$targs$$.Union$UnionType},that);
    that.tipo=tipo;
    that._types=types$2;
    return that;
}
AppliedUnionType.$crtmm$=function(){return{mod:$CCMM$,'super':{t:Basic},$tp:{Union:{'var':'out','def':{t:Anything}}},satisfies:[{t:UnionType$meta$model,a:{Union:'Union'}}],$an:function(){return[shared()];},d:['ceylon.language.meta.model','UnionType']};};
function $init$AppliedUnionType(){
  if (AppliedUnionType.$$===undefined){
    initTypeProto(AppliedUnionType,'ceylon.language.meta.model::AppliedUnionType',Basic,UnionType$meta$model);
    (function($$appliedUnionType){

$defat($$appliedUnionType,'string',function(){
  var qn="";
  var first=true;
  for (var i=0;i<this._types.length;i++) {
    if (first)first=false;else qn+="|";
    qn+=this._types[i].string;
  }
  return String$(qn);
},undefined,function(){return{mod:$CCMM$,$t:{t:String$},d:['ceylon.language','Object','$at','string']};});
$defat($$appliedUnionType,'hash',function(){
  var h=this._types.length;
  for (var i=0; i<this._types.length;i++) {
    h+=this._types[i].hash;
  }
  return h;
},undefined,function(){return{mod:$CCMM$,$t:{t:Integer},d:['ceylon.language','Object','$at','hash']};});

      $$appliedUnionType.equals=function(u) {
        if($is(u,{t:AppliedUnionType})) {
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
            
            $defat($$appliedUnionType,'caseTypes',function(){
                var $$appliedUnionType=this;
                return $$appliedUnionType._types; //TODO type
            },undefined,function(){return{mod:$CCMM$,$t:{t:List,a:{Element$List:{t:Type$meta$model,a:{Type$Type:'Union$UnionType'}}}},$cont:AppliedUnionType,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','UnionType','$at','caseTypes']};});

      $$appliedUnionType.typeOf=function typeOf(instance$20){
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
        return $is(instance$20,{t:'u',l:tipos});
      };$$appliedUnionType.typeOf.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Boolean},$ps:[{$nm:'instance',$mt:'prm',$t:{t:Anything},$an:function(){return[];}}],$cont:AppliedUnionType,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','UnionType','$m','typeOf']};};

            $$appliedUnionType.supertypeOf=function supertypeOf(type$21){
                var $$appliedUnionType=this;
                throw Exception(String$("IMPL AppliedUnionType.supertypeOf"));
            };$$appliedUnionType.supertypeOf.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Boolean},$ps:[{$nm:'type',$mt:'prm',$t:{t:Type$meta$model,a:{Type$Type:{t:Anything}}},$an:function(){return[];}}],$cont:AppliedUnionType,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','UnionType','$m','supertypeOf']};};

      $$appliedUnionType.exactly=function exactly(type$22){
        if ($is(type$25, {t:AppliedUnionType})) {
          for (var i=0; i<this._types.length;i++) {
            var myt = this._types[i];
            var was=false;
            for (var j=0; j<type$25._types.length;j++) {
              was |= myt.exactly(type$25._types[j]);
            }
            if (!was)return false;
          }
          //Now the other way around
          for (var i=0; i<type$25._types.length;i++) {
            var myt = type$25._types[i];
            var was=false;
            for (var j=0; j<this._types.length;j++) {
              was |= myt.exactly(this._types[j]);
            }
            if (!was)return false;
          }
          return true;
        }
        return false;
      };$$appliedUnionType.exactly.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Boolean},$ps:[{$nm:'type',$mt:'prm',$t:{t:Type$meta$model,a:{Type$Type:{t:Anything}}},$an:function(){return[];}}],$cont:AppliedUnionType,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','UnionType','$m','exactly']};};

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
    IntersectionType$meta$model(that.$$targs$$===undefined?$$targs$$:{Intersection$IntersectionType:that.$$targs$$.Intersection$IntersectionType},that);
    that.tipo=tipo;
    return that;
}
AppliedIntersectionType.$crtmm$=function(){return{mod:$CCMM$,'super':{t:Basic},$tp:{Intersection$IntersectionType:{'var':'out','def':{t:Anything}}},satisfies:[{t:IntersectionType$meta$model,a:{Intersection$IntersectionType:'Intersection$IntersectionType'}}],$an:function(){return[shared()];},d:['ceylon.language.meta.model','IntersectionType']};};
function $init$AppliedIntersectionType(){
  if (AppliedIntersectionType.$$===undefined){
    initTypeProto(AppliedIntersectionType,'ceylon.language.meta.model::AppliedIntersectionType',Basic,IntersectionType$meta$model);
    (function($$appliedIntersectionType){

$defat($$appliedIntersectionType,'string',function(){
  var qn="";
  var first=true;
  for (var i=0;i<this._types.length;i++) {
    if (first)first=false;else qn+="&";
    qn+=this._types[i].string;
  }
  return String$(qn);
},undefined,function(){return{mod:$CCMM$,$t:{t:String$},d:['ceylon.language','Object','$at','string']};});
$defat($$appliedIntersectionType,'hash',function(){
  var h=this._types.length;
  for (var i=0; i<this._types.length;i++) {
    h+=this._types[i].hash;
  }
  return h;
},undefined,function(){return{mod:$CCMM$,$t:{t:Integer},d:['ceylon.language','Object','$at','hash']};});
      $$appliedIntersectionType.equals=function(u) {
        if($is(u,{t:AppliedIntersectionType})) {
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
 
            $defat($$appliedIntersectionType,'satisfiedTypes',function(){
                var $$appliedIntersectionType=this;
                return $$appliedIntersectionType._types;
            },undefined,function(){return{mod:$CCMM$,$t:{t:List,a:{Element$List:{t:Type$meta$model,a:{Type$Type:'Intersection$IntersectionType'}}}},$cont:AppliedIntersectionType,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','IntersectionType','$at','satisfiedTypes']};});

      $$appliedIntersectionType.typeOf=function typeOf(instance$23){
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
        return $is(instance$23,{t:'i',l:tipos});
      };$$appliedIntersectionType.typeOf.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Boolean},$ps:[{$nm:'instance',$mt:'prm',$t:{t:Anything},$an:function(){return[];}}],$cont:AppliedIntersectionType,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','IntersectionType','$m','typeOf']};};

            $$appliedIntersectionType.supertypeOf=function supertypeOf(type$24){
                var $$appliedIntersectionType=this;
                throw Exception(String$("IMPL AppliedIntersectionType.supertypeOf"));
            };$$appliedIntersectionType.supertypeOf.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Boolean},$ps:[{$nm:'type',$mt:'prm',$t:{t:Type$meta$model,a:{Type$Type:{t:Anything}}},$an:function(){return[];}}],$cont:AppliedIntersectionType,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','IntersectionType','$m','supertypeOf']};};

      $$appliedIntersectionType.exactly=function exactly(type$25){
        if ($is(type$25, {t:AppliedIntersectionType})) {
          for (var i=0; i<this._types.length;i++) {
            var myt = this._types[i];
            var was=false;
            for (var j=0; j<type$25._types.length;j++) {
              was |= myt.exactly(type$25._types[j]);
            }
            if (!was)return false;
          }
          //Now the other way around
          for (var i=0; i<type$25._types.length;i++) {
            var myt = type$25._types[i];
            var was=false;
            for (var j=0; j<this._types.length;j++) {
              was |= myt.exactly(this._types[j]);
            }
            if (!was)return false;
          }
          return true;
        }
        return false;
      };$$appliedIntersectionType.exactly.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Boolean},$ps:[{$nm:'type',$mt:'prm',$t:{t:Type$meta$model,a:{Type$Type:{t:Anything}}},$an:function(){return[];}}],$cont:AppliedIntersectionType,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','IntersectionType','$m','exactly']};};

    })(AppliedIntersectionType.$$.prototype);
  }
  return AppliedIntersectionType;
}
exports.$init$AppliedIntersectionType$meta$model=$init$AppliedIntersectionType;
$init$AppliedIntersectionType();

function AppliedFunction(m,$$targs$$,o,mptypes) {
  var mm=getrtmm$$(m);
  var ttargs;
  if (mm.$tp) {
    if (!mptypes || mptypes.size<1)throw TypeApplicationException$meta$model("Missing type arguments for AppliedFunction");
    var i=0;ttargs={};
    for (var tp in mm.$tp) {
      var _ta=mptypes.$get?mptypes.$get(i):mptypes[i];
      if(_ta&&_ta.tipo)ttargs[tp]={t:_ta.tipo};
      else if (_ta) console.log("TODO assign type arg " + _ta + " to " + tp);
      else if (mptypes[tp])ttargs[tp]=mptypes[tp];
      else throw new Error("No more type arguments in AppliedFunction!");
      i++;
    }
  }
  var f = o===undefined&&mm.$cont?function(x){
    return AppliedFunction(m,$$targs$$,x,mptypes);
  }:function(){
    var _fu=(o&&o[mm.d[mm.d.length-1]])||m;//Get the object's method if possible
    if (mm.$tp) {
      var _a=[];
      for (var i=0;i<arguments.length;i++)_a.push(arguments[i]);
      _a.push(ttargs);
      return _fu.apply(o,_a);
    }
    return _fu.apply(o,arguments);
  }
  f.$crtmm$={mod:$CCMM$,d:['ceylon.language.meta.model','Function'],$t:mm.$t,$ps:mm.$ps,$an:mm.$an};
  var dummy=new AppliedFunction.$$;
  f.getT$all=function(){return dummy.getT$all();}
  f.getT$name=function(){return dummy.getT$name();}
  if ($$targs$$===undefined) {
    throw TypeApplicationException$meta$model("Missing type arguments for AppliedFunction");
  }
  Function$meta$model($$targs$$,f);
  f.tipo=m;
  f.$targs=ttargs;
  if (o)f.$bound=o;
  $defat(f,'typeArguments',function(){
    return FunctionModel$meta$model.$$.prototype.$prop$getTypeArguments.get.call(f);
  },undefined,function(){return{mod:$CCMM$,$t:{t:Map,a:{Key$Map:{t:TypeParameter$meta$declaration},Item$Map:{t:Type$meta$model,a:{Type$Type:{t:Anything}}}}},$cont:AppliedFunction,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Generic','$at','typeArguments']};});
  f.equals=function(oo){
    return $is(oo,{t:AppliedFunction}) && oo.tipo===m && oo.typeArguments.equals(this.typeArguments) && (o?o.equals(oo.$bound):oo.$bound===o);
  }
  $defat(f,'string',function(){
    return FunctionModel$meta$model.$$.prototype.$prop$getString.get.call(f);
  },undefined,function(){return{mod:$CCMM$,$t:{t:String$},d:['ceylon.language','Object','$at','string'],$cont:AppliedFunction};});
  $defat(f,'parameterTypes',function(){
    return FunctionModel$meta$model.$$.prototype.$prop$getParameterTypes.get.call(f);
  },undefined,FunctionModel$meta$model.$$.prototype.$prop$getParameterTypes.$crtmm$);
$defat(f,'declaration',function(){
  if (f._decl)return f._decl;
  f._decl = OpenFunction(getModules$meta().find(mm.mod['$mod-name'],mm.mod['$mod-version']).findPackage(mm.d[0]), m);
  return f._decl;
},undefined,function(){return{mod:$CCMM$,$t:{t:FunctionDeclaration$meta$declaration},d:['ceylon.language.meta.model','FunctionModel','$at','declaration']};});
  f.$apply=function(a){
    a=convert$params(mm,a);
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
AppliedFunction.$crtmm$=function(){return{mod:$CCMM$,d:['ceylon.language.meta.model','Function'],satisfies:{t:Function$meta$model,a:{Type$Function:'Type$Function',Arguments$Function:'Arguments$Function'}},$an:function(){return [shared(),actual()];}};};
exports.AppliedFunction$meta$model=AppliedFunction;
initTypeProto(AppliedFunction,'ceylon.language.meta.model::AppliedFunction',Basic,Function$meta$model);

function AppliedValue(obj,attr,$$targs$$,$$appliedValue){
  var mm = getrtmm$$(attr);
  $init$AppliedValue();
  if ($$appliedValue===undefined){
    if (obj||mm.$cont===undefined)$$appliedValue=new AppliedValue.$$;
    else {
      $$appliedValue=function(x){return AppliedValue(x,attr,$$targs$$);};
      $$appliedValue.$$=AppliedValue.$$;
      var dummy=new AppliedValue.$$;
      $$appliedValue.getT$all=function(){return dummy.getT$all();};
      $$appliedValue.getT$name=function(){return dummy.getT$name();};
$defat($$appliedValue,'string',function(){
  var qn;
  if ($$targs$$ && $$targs$$.Container$Value) {
    qn = typeLiteral$meta({Type$typeLiteral:$$targs$$.Container$Value}).string + "." + mm.d[mm.d.length-1];
  } else if (mm.$cont) {
    qn = typeLiteral$meta({Type$typeLiteral:{t:mm.$cont}}).string + "." + mm.d[mm.d.length-1];
  } else {
    qn=$qname(mm);
  }
  return String$(qn);
},undefined,function(){return{mod:$CCMM$,$t:{t:String$},d:['ceylon.language','Object','$at','string']};});
    }
  }
  set_type_args($$appliedValue,$$targs$$);
  Value$meta$model($$appliedValue.$$targs$$===undefined?$$targs$$:{Get$Value:$$appliedValue.$$targs$$.Get$Value,Set$Value:$$appliedValue.$$targs$$.Set$Value},$$appliedValue);
  if($$targs$$.Container$Value)Attribute$meta$model({Get$Attribute:$$targs$$.Get$Value,
    Set$Attribute:$$targs$$.Set$Value,Container$Attribute:$$targs$$.Container$Value},$$appliedValue);//TODO checar si no es if Container$Attribute
  $$appliedValue.obj=obj;
  $$appliedValue.tipo=attr;
  return $$appliedValue;
}
AppliedValue.$crtmm$=function(){return{mod:$CCMM$,'super':{t:Basic},$tp:{Get:{'var':'out'},Set:{'var':'in'}},
  satisfies:[{t:Value$meta$model,a:{Get:'Get',Set:'Set'}}],$an:function(){return[shared()];},d:['ceylon.language.meta.model','Value']};};
exports.AppliedValue$meta$model=AppliedValue;
function $init$AppliedValue(){
  if (AppliedValue.$$===undefined){
    initTypeProto(AppliedValue,'ceylon.language.meta.model::AppliedValue',Basic,Value$meta$model);
    (function($$appliedValue){
$defat($$appliedValue,'string',function(){
  return String$($qname(this.tipo));
},undefined,function(){return{mod:$CCMM$,$t:{t:String$},d:['ceylon.language','Object','$at','string']};});
      $defat($$appliedValue,'declaration',function(){
        var $$av=this;
        var mm = $$av.tipo.$crtmm$;
        var _pkg = getModules$meta().find(mm.mod['$mod-name'],mm.mod['$mod-version']).findPackage(mm.d[0]);
        return OpenValue(_pkg, $$av.tipo);
      },undefined,function(){return{mod:$CCMM$,$t:{t:ValueDeclaration$meta$declaration},$cont:AppliedValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Value','$at','declaration']};});

      $$appliedValue.$get=function $get(){
        if (this.obj) {
          var mm=this.tipo.$crtmm$;
          return (mm&&mm.d&&this.obj[mm.d[mm.d.length-1]])||this.tipo.get.call(this.obj);
        }
        return this.tipo.get();
      };$$appliedValue.$get.$crtmm$=function(){return{mod:$CCMM$,$t:'Get',$ps:[],$cont:AppliedValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Value','$m','get']};};
      $$appliedValue.set=function set(newValue$26){
        if (!this.tipo.set)throw MutationException$meta$model("Value is not writable");
        return this.obj?this.tipo.set.call(this.obj,newValue$26):this.tipo.set(newValue$26);
      };$$appliedValue.set.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Anything},$ps:[{$nm:'newValue',$mt:'prm',$t:'Set',$an:function(){return[];}}],$cont:AppliedValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Value','$m','set']};};
 
$$appliedValue.setIfAssignable=function(v) {
  var mm = this.tipo.$crtmm$;
  if (!$is(v,mm.$t))throw IncompatibleTypeException$meta$model("The specified value has the wrong type");
  var mdl=get_model(mm);
  if (!(mdl &&mdl['var']))throw MutationException$meta$model("Attempt to modify a value that is not variable");
  this.obj?this.tipo.set.call(this.obj,v):this.tipo.set(v);
};$$appliedValue.setIfAssignable.$crtmm$=function(){return{mod:$CCMM$,$ps:[],$cont:AppliedValue,d:['ceylon.language.meta.model','Value','$m','setIfAssignable']};};

      $defat($$appliedValue,'type',function(){
          var $$atr=this;
          var t = $$atr.tipo.$crtmm$;
          return typeLiteral$meta({Type$typeLiteral:t.$t});
      },undefined,function(){return{mod:$CCMM$,$t:{t:Type$meta$model,a:{Type$Type:'Get$Value'}},$cont:AppliedValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Value','$at','type']};});

      $defat($$appliedValue,'container',function(){
          if (this.$$targs$$.Container$Value) {
            return typeLiteral$meta({Type$typeLiteral:this.$$targs$$.Container$Value});
          } else if (this.$$targs$$.Container$Attribute) {
            return typeLiteral$meta({Type$typeLiteral:this.$$targs$$.Container$Attribute});
          }
          var mm=this.tipo.$crtmm$;
          if (mm.$cont) {
            return typeLiteral$meta({Type$typeLiteral:{t:mm.$cont}});
          }
          return null;
      },undefined,function(){return{mod:$CCMM$,$t:{ t:'u', l:[{t:Null},{t:Type$meta$model,a:{Type$Type:{t:Anything}}}]},$cont:AppliedValue,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Value','$at','container']};});

    })(AppliedValue.$$.prototype);
  }
  return AppliedValue;
}
exports.$init$AppliedValue$meta$model=$init$AppliedValue;
$init$AppliedValue();

//ClassDefinition AppliedMethod at X (10:0-21:0)
function AppliedMethod(tipo,typeArgs,$$targs$$,$$appliedMethod){
  $init$AppliedMethod();
  var mm = getrtmm$$(tipo);
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
      return AppliedFunction(tipo,{Type$Function:$$targs$$.Type$Method,Arguments$Function:$$targs$$.Arguments$Method,
        Container$Function:$$targs$$.Container$Method},x,typeArgs);
    }
    var dummy=new AppliedMethod.$$;
    $$appliedMethod.getT$all=function(){return dummy.getT$all();};
    $$appliedMethod.getT$name=function(){return dummy.getT$name();};
  }
  if (_ta)$$appliedMethod.$targs=_ta;
  set_type_args($$appliedMethod,$$targs$$);
  Method$meta$model($$appliedMethod.$$targs$$===undefined?$$targs$$:{Arguments$Method:$$appliedMethod.$$targs$$.Arguments$Method,
    Type$Method:$$appliedMethod.$$targs$$.Type$Method,Container$Method:$$appliedMethod.$$targs$$.Container$Method},$$appliedMethod);
  $$appliedMethod.tipo=tipo;

//This was copied from prototype style
  $defat($$appliedMethod,'declaration',function(){
    var _pkg = getModules$meta().find(mm.mod['$mod-name'],mm.mod['$mod-version']).findPackage(mm.d[0]);
    return OpenFunction(_pkg, $$appliedMethod.tipo);
  },undefined,function(){return{mod:$CCMM$,$t:{t:FunctionDeclaration$meta$declaration},$cont:AppliedMethod,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Method','$at','declaration']};});

  $defat($$appliedMethod,'type',function(){
    return typeLiteral$meta({Type$typeLiteral:mm.$t});
  },undefined,function(){return{mod:$CCMM$,$t:{t:Type$meta$model,a:{Type$Type:'Type'}},$cont:AppliedMethod,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Method','$at','type']};});

  $defat($$appliedMethod,'typeArguments',function(){
    return FunctionModel$meta$model.$$.prototype.$prop$getTypeArguments.get.call($$appliedMethod);
  },undefined,function(){return{mod:$CCMM$,$t:{t:Map,a:{Key:{t:TypeParameter$meta$declaration},Item:{t:Type$meta$model,a:{Type$Type:{t:Anything}}}}},$cont:AppliedMethod,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Generic','$at','typeArguments']};});
  $defat($$appliedMethod,'parameterTypes',function(){
    return FunctionModel$meta$model.$$.prototype.$prop$getParameterTypes.get.call($$appliedMethod);
  },undefined,FunctionModel$meta$model.$$.prototype.$prop$getParameterTypes.$crtmm$);

  $$appliedMethod.equals=function(o){
    return $is(o,{t:AppliedMethod}) && o.tipo===tipo && o.typeArguments.equals(this.typeArguments);
  }
  $$appliedMethod.$bind=function(o){
    if (!$is(o,{t:mm.$cont}))throw IncompatibleTypeException$meta$model("Cannot bind " + $$appliedMethod.string + " to "+o);
    return $$appliedMethod(o);
  }
  $defat($$appliedMethod,'string',function(){
    return FunctionModel$meta$model.$$.prototype.$prop$getString.get.call($$appliedMethod);
  },undefined,function(){return{mod:$CCMM$,$t:{t:String$},d:['ceylon.language','Object','$at','string'],$cont:AppliedMethod};});
  return $$appliedMethod;
}
AppliedMethod.$crtmm$=function(){return{mod:$CCMM$,'super':{t:Basic},$tp:{Container$Method:{'var':'in'},Type$Method:{'var':'out','def':{t:Anything}},Arguments$Method:{'var':'in','satisfies':[{t:Sequential,a:{Element$Iterable:{t:Anything}}}],'def':{t:Nothing}}},satisfies:[{t:Method$meta$model,a:{Arguments$Method:'Arguments$Method',Type$Method:'Type$Method',Container$Method:'Container$Method'}}],$an:function(){return[shared()];},d:['ceylon.language.meta.model','Method']};};
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
    $$appliedAttribute=function(x){return AppliedValue(x,atr, {Get$Value:$$targs$$.Get$Attribute,Set$Value:$$targs$$.Set$Attribute,
      Container$Value:$$targs$$.Container$Attribute});};
    $$appliedAttribute.$$=AppliedAttribute.$$;
    var dummy=new AppliedAttribute.$$;
    $$appliedAttribute.getT$all=function(){return dummy.getT$all();};
    $$appliedAttribute.getT$name=function(){return dummy.getT$name();};
  }
  set_type_args($$appliedAttribute,$$targs$$);
  Attribute$meta$model($$appliedAttribute.$$targs$$===undefined?$$targs$$:{Get$Attribute:$$appliedAttribute.$$targs$$.Get$Attribute,Set$Attribute:$$appliedAttribute.$$targs$$.Set$Attribute,Container$Attribute:$$appliedAttribute.$$targs$$.Container$Attribute},$$appliedAttribute);
  $$appliedAttribute.tipo=atr;
  $$appliedAttribute.pname=pname;
  $defat($$appliedAttribute,'type',function(){
    var t = getrtmm$$(atr);
    t=t.$t;
    return typeLiteral$meta({Type$typeLiteral:t});
  },undefined,function(){return{mod:$CCMM$,$t:{t:Type$meta$model,a:{Type$Type:'Get$Attribute'}},$cont:AppliedAttribute,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Attribute','$at','type']};});
  //AttributeGetterDefinition declaration at X (100:4-100:83)
  $defat($$appliedAttribute,'declaration',function(){
    var mm = getrtmm$$(atr);
    var pkg = getModules$meta().find(mm.mod['$mod-name'],mm.mod['$mod-version']).findPackage(mm.d[0]);
    return OpenValue(pkg, atr);
  },undefined,function(){return{mod:$CCMM$,$t:{t:ValueDeclaration$meta$declaration},$cont:AppliedAttribute,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Attribute','$at','declaration']};});
  $$appliedAttribute.$bind=function(cont){
    return AppliedValue(cont,atr,{Get$Value:$$targs$$.Get$Attribute,Set$Value:$$targs$$.Set$Attribute,
      Container$Value:$$targs$$.Container$Attribute});
  }
  $defat($$appliedAttribute,'string',function(){
    var c=getrtmm$$(atr).$cont;
    if (typeof(c.$crtmm$)==='function')c.$crtmm$=c.$crtmm$();
    if (!c)return String$($qname(atr));
    c=c.$crtmm$;
    var qn=$qname(c);
    if (c.$tp) {
      qn+="<"; var first=true;
      var cnt=$$targs$$&&$$targs$$.Container$Attribute&&$$targs$$.Container$Attribute.a;
      for (var tp in c.$tp) {
        if (first)first=false;else qn+=",";
        var _ta=cnt&&cnt[tp];
        if (_ta) {
          qn+=$qname(_ta);
        } else qn+=$qname(Anything);
      }
      qn+=">";
    }
    qn+="."+pname;
    return String$(qn);
  },undefined,function(){return{mod:$CCMM$,$t:{t:String$},d:['ceylon.language','Object','$at','string']};});
  $$appliedAttribute.equals=function(o) {
    return $is(o,{t:AppliedAttribute}) && o.tipo===atr;
  }
  return $$appliedAttribute;
}
AppliedAttribute.$crtmm$=function(){return{mod:$CCMM$,'super':{t:Basic},$tp:{Get:{'var':'out','def':{t:Anything}},Set:{'var':'in','def':{t:Nothing}},Container:{'var':'in'}},satisfies:[{t:Attribute$meta$model,a:{Get:'Get',Set:'Set',Container:'Container'}}],$an:function(){return[shared()];},d:['ceylon.language.meta.model','Attribute']};};
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
exports.$init$AppliedAttribute$meta$model=$init$AppliedAttribute;
$init$AppliedAttribute();

