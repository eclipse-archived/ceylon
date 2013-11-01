//Addendum to ClassOrInterface
defineAttr(ClassOrInterface$meta$model.$$.prototype,'satisfiedTypes',function(){
  var ints = this.tipo.$$metamodel$$['satisfies'];
  if (ints && ints.length) {
    var rv = [];
    function resolveTypeArguments(root,type) {
      if (type.a) {
        var t2 = {t:type.t, a:{}};
        for (var targ in type.a) {
          t2.a[targ]=typeof(type.a[targ])==='string' ?
            t2.a[targ]=root.$$targs$$.Type.a[type.a[targ]]
            : t2.a[targ]=type.a[targ];
          if (t2.a[targ] && t2.a[targ].a) {
            t2.a[targ]=resolveTypeArguments(root,t2.a[targ]);
          }
        }
        type=t2;
      }
      return type;
    }
    for (var i=0; i < ints.length; i++) {
      var ifc = resolveTypeArguments(this,ints[i]);
      var mm=ifc.t.$$metamodel$$;
      if (typeof(mm)==='function'){mm=mm();ifc.t.$$metamodel$$=mm;}
      rv.push((mm.$cont?AppliedMemberInterface:AppliedInterface)(ifc.t, {Type:ifc}));
    }
    return rv.reifyCeylonType({Absent:{t:Null},Element:{t:InterfaceModel$meta$model,a:{Type:{t:Anything}}}});
  }
  return getEmpty();
},undefined,function(){
  return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:{t:InterfaceModel$meta$model,a:{Type:{t:Anything}}}}},$cont:ClassOrInterface$meta$model,
  $an:function(){return[shared(),formal()];},d:['ceylon.language.meta.model','ClassOrInterface','$at','satisfiedTypes']};
});
ClassOrInterface$meta$model.$$.prototype.getMethod=function(name,types,$$$mptypes) {
  if (!extendsType($$$mptypes.Container,{t:this.tipo}))throw IncompatibleTypeException$meta$model("Incompatible Container type argument");
  var fun = this.tipo[name];
  if (!fun) fun = this.tipo.$$.prototype[name];
  if (!fun) return null;
  if (typeof(fun)!=='function')return null;
  var mm=fun.$$metamodel$$;
  if (typeof(mm)==='function') {
    mm=mm();fun.$$metamodel$$=mm;
  }
  var _t=$$$mptypes.Type;
  var _a=$$$mptypes.Arguments;
  if (mm) {
    if (mm.$t){
      _t=mm.$t;
      if (!extendsType(_t,$$$mptypes.Type))throw IncompatibleTypeException$meta$model("Incompatible Type argument");
    }
    validate$params(mm.$ps,_a,"Wrong number of Arguments for getMethod");
    _a=tupleize$params(mm.$ps);
  }
  return AppliedMethod(fun, types, {Container:{t:this.tipo},Type:_t,Arguments:_a});
}
ClassOrInterface$meta$model.$$.prototype.getMethod.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:Method$meta$model,a:{Arguments:'Arguments',Type:'Type',Container:'Container'}}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$},$an:function(){return[];}},{$nm:'types',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element:{t:Type$meta$model,a:{Type:{t:Anything}}}}},$an:function(){return[];}}],$cont:ClassOrInterface$meta$model,$tp:{Container:{},Type:{},Arguments:{'satisfies':[{t:Sequential,a:{Element:{t:Anything}}}]}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','ClassOrInterface','$m','getMethod']};};
ClassOrInterface$meta$model.$$.prototype.getAttribute=function getAttribute(name$15,$$$mptypes){
  if (!extendsType($$$mptypes.Container,{t:this.tipo}))throw IncompatibleTypeException$meta$model("Incompatible Container type argument");
  var nom = '$prop$get' + name$15[0].toUpperCase() + name$15.substring(1);
  var at = this.tipo.$$.prototype[nom];
  if (!at)return null;
  var mm=at.$$metamodel$$;
  if (typeof(mm)==='function') {
    mm=mm();at.$$metamodel$$=mm;
  }
  var _t=$$$mptypes.Type;
  if (mm && mm.$t) {
    if (!extendsType(mm.$t,_t))throw IncompatibleTypeException$meta$model("Incompatible Type type argument");
    _t=mm.$t;
  }
  var rv=(at.set?AppliedVariableAttribute:AppliedAttribute)(name$15, at, {Type:_t, Container:{t:this.tipo}});
  if (this.$targs)rv.$$targs$$.Container.a=this.$targs;
  rv.$parent=this;
  return rv;
};
ClassOrInterface$meta$model.$$.prototype.getAttribute.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:Attribute$meta$model,a:{Type:'Type',Container:'Container'}}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$},$an:function(){return[];}}],$cont:ClassOrInterface$meta$model,$tp:{Container:{},Type:{}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','ClassOrInterface','$m','getAttribute']};};
defineAttr(ClassOrInterface$meta$model.$$.prototype,'container',function(){
  if (this.$parent)return this.$parent;
  var cont = this.tipo.$$metamodel$$.$cont;
  if (cont === undefined)return null;
  if (get_model(cont.$$metamodel$$).$mt === 'ifc')
    return AppliedInterface(cont,{Type:{t:cont}});
  return AppliedClass(cont,{Type:{t:cont},Arguments:{t:Sequential,a:{Element:{t:Anything}}}});
},undefined,function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:Type$meta$model,a:{Type:{t:Anything}}}]},$cont:ClassOrInterface$meta$model,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Member','$at','container']};});

ClassOrInterface$meta$model.$$.prototype.getVariableAttribute=function getVariableAttribute(name$16,$$$mptypes){
  var nom = '$prop$get' + name$15[0].toUpperCase() + name$15.substring(1);
  var at=this.tipo.$$.prototype[nom];
  if (!at)return null;
  if (at.set===undefined)throw IncompatibleTypeException$meta$model("Attribute " + name$16 + " is not variable");
  if (typeof(at.$$metamodel$$)==='function')at.$$metamodel$$=at.$$metamodel$$();
  var _t=$$$mptypes.Type;
  if (at.$$metamodel$$ && at.$$metamodel$$.$t)_t=at.$$metamodel$$.$t;
  var _cont={t:this.tipo};
  if (this.$targs)_cont.a=this.$targs;
  return AppliedVariableAttribute(nom, at, {Type:_t,Container:_cont});
};
ClassOrInterface$meta$model.$$.prototype.getVariableAttribute.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:VariableAttribute$meta$model,a:{Type:'Type',Container:'Container'}}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$},$an:function(){return[];}}],$cont:ClassOrInterface$meta$model,$tp:{Container:{},Type:{}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','ClassOrInterface','$m','getVariableAttribute']};};
ClassOrInterface$meta$model.$$.prototype.getClassOrInterface=function getClassOrInterface(name$2,types$3,$$$mptypes){
  if (!extendsType($$$mptypes.Kind, {t:ClassOrInterface$meta$model}))throw IncompatibleTypeException$meta$model("Kind must be ClassOrInterface");
  if (!extendsType($$$mptypes.Container,{t:this.tipo}))throw IncompatibleTypeException$meta$model("Incompatible type specified in Container");
  var $$clase=this;
  if(types$3===undefined){types$3=getEmpty();}
  var mm = $$clase.tipo.$$metamodel$$;
  if (typeof(mm)==='function') {
    mm=mm(); $$clase.tipo.$$metamodel$$=mm;
  }
  var nom = name$2 + '$' + mm.d[mm.d.length-1];
  var ic = $$clase.tipo.$$.prototype[nom];
  if (ic) {
    mm = ic.$$metamodel$$;
    if (typeof(mm)==='function') {
      mm=mm(); ic.$$metamodel$$=mm;
    }
    var md = get_model(mm);
    var rv;
    var ict={t:ic};
    var _cont={t:this.tipo};
    if (this.$targs)_cont.a=this.$targs;
    if (md.$mt==='ifc') {
      if (!extendsType({t:Interface$meta$model},{t:$$$mptypes.Kind.t}))throw IncompatibleTypeException$meta$model("Member " + name$2 + " is an interface");
      validate$typeparams(ict,ic.$$metamodel$$.$tp,types$3);
      rv=AppliedMemberInterface(ic, {Container:_cont,Type:ict});
    } else if (md.$mt==='cls'){
      if (!extendsType({t:Class$meta$model},{t:$$$mptypes.Kind.t}))throw IncompatibleTypeException$meta$model("Member " + name$2 + " is a class");
      validate$typeparams(ict,ic.$$metamodel$$.$tp,types$3);
      rv=AppliedMemberClass(ic, {Container:_cont,Type:ict, Arguments:$$$mptypes.Arguments});
    } else {
      throw IncompatibleTypeException$meta$model("Member " + name$2 + " is not a class or interface");
    }
    if (ict.a)rv.$targs=ict.a;
    rv.$parent=this;
    return rv;
  }
  return null;
};ClassOrInterface$meta$model.$$.prototype.getClassOrInterface.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:Member$meta$model,a:{Type:'Container',Kind:'Kind'}}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$},$an:function(){return[];}},{$nm:'types',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element:{t:Type$meta$model,a:{Type:{t:Anything}}}}},$an:function(){return[];}}],$cont:ClassOrInterface$meta$model,$tp:{Container:{},Kind:{'satisfies':[{t:ClassOrInterface$meta$model,a:{Type:{t:Anything}}}]}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','ClassOrInterface','$m','getClassOrInterface']};};
ClassOrInterface$meta$model.$$.prototype.getDeclaredClassOrInterface=function getDeclaredClassOrInterface(name$2,types$3,$$$mptypes){
  return this.getClassOrInterface(name$2,types$3,$$$mptypes);
};ClassOrInterface$meta$model.$$.prototype.getDeclaredClassOrInterface.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:Member$meta$model,a:{Type:'Container',Kind:'Kind'}}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$},$an:function(){return[];}},{$nm:'types',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element:{t:Type$meta$model,a:{Type:{t:Anything}}}}},$an:function(){return[];}}],$cont:ClassOrInterface$meta$model,$tp:{Container:{},Kind:{'satisfies':[{t:ClassOrInterface$meta$model,a:{Type:{t:Anything}}}]}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','ClassOrInterface','$m','getDeclaredClassOrInterface']};};

ClassOrInterface$meta$model.$$.prototype.getClass=function getClass(name,types,$mptypes) {
  var rv=this.getClassOrInterface(name,types,{Container:$mptypes.Container, Kind:Class$meta$model});
  if (rv && !isOfType(rv, {t:AppliedMemberClass})) {
    throw IncompatibleTypeException$meta$model("Member " + name + " is not a class");
  }
  return rv;
};ClassOrInterface$meta$model.$$.prototype.getClass.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:MemberClass$meta$model},$ps:[],$cont:ClassOrInterface$meta$model,$tp:{Container:{},Type:{},Arguments:{satisfies:[{t:Sequential,a:{Element:{t:Anything}}}]}}};};
ClassOrInterface$meta$model.$$.prototype.getInterface=function(name,types,$mptypes) {
  var rv=this.getClassOrInterface(name,types,{Container:$mptypes.Container, Kind:Interface$meta$model});
  if (rv && !isOfType(rv, {t:AppliedMemberInterface})) {
    throw IncompatibleTypeException$meta$model("Member " + name + " is not an interface");
  }
  return rv;
};ClassOrInterface$meta$model.$$.prototype.getInterface.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:MemberInterface$meta$model},$ps:[],$cont:ClassOrInterface$meta$model,$tp:{Container:{},Type:{},Arguments:{satisfies:[{t:Sequential,a:{Element:{t:Anything}}}]}}};};

ClassOrInterface$meta$model.$$.prototype.isTypeOf=function isTypeOf(instance$8){
  var _t={t:this.tipo};
  if (this.$targs)_t.a=this.$targs;
  return isOfType(instance$8,_t);
};
ClassOrInterface$meta$model.$$.prototype.isTypeOf.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$ps:[{$nm:'instance',$mt:'prm',$t:{t:Anything},$an:function(){return[];}}],$cont:ClassOrInterface$meta$model,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Type','$m','isTypeOf']};};
ClassOrInterface$meta$model.$$.prototype.isSuperTypeOf=function isSuperTypeOf(type$9){
  return extendsType({t:type$9.tipo}, {t:this.tipo});
};
ClassOrInterface$meta$model.$$.prototype.isSuperTypeOf.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$ps:[{$nm:'type',$mt:'prm',$t:{t:Type$meta$model,a:{Type:{t:Anything}}},$an:function(){return[];}}],$cont:ClassOrInterface$meta$model,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Type','$m','isSuperTypeOf']};};
ClassOrInterface$meta$model.$$.prototype.isExactly=function isExactly(type$10){
  return type$10.tipo && this.tipo === type$10.tipo;
};
ClassOrInterface$meta$model.$$.prototype.isExactly.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$ps:[{$nm:'type',$mt:'prm',$t:{t:Type$meta$model,a:{Type:{t:Anything}}},$an:function(){return[];}}],$cont:ClassOrInterface$meta$model,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Type','$m','isExactly']};};
defineAttr(ClassOrInterface$meta$model.$$.prototype,'typeArguments',function(){
  var mm = this.tipo.$$metamodel$$;
  if (typeof(mm)==='function') {
    mm=mm(); this.tipo.$$metamodel$$=mm;
  }
  if (mm) {
    if (mm.$tp) {
      var targs=[];
      for (var tp in mm.$tp) {
        var param = OpenTypeParam(this.tipo,tp);
        var targ;
        if (this.$$targs$$ && this.$$targs$$.Type && this.$$targs$$.Type.a && this.$$targs$$.Type.a[tp]) {
          var _targ=this.$$targs$$.Type.a[tp];
          if (typeof(_targ)==='string') {
            console.log("TODO buscar " + tp + "->" + _targ + " para " + this.declaration.qualifiedName);
            _targ={t:Anything};
          }
          targ=typeLiteral$meta({Type:_targ});
        } else {
          targ=typeLiteral$meta({Type:{t:Anything}});
        }
        targs.push(Entry(param,targ,{Key:{t:TypeParameter$meta$declaration},Item:{t:Type$meta$model,a:{Type:{t:Anything}}}}));
      }
      return LazyMap(targs.reifyCeylonType({Absent:{t:Null},Element:{t:Entry,a:{Key:{t:TypeParameter$meta$declaration},Item:{t:Type$meta$model,a:{Type:{t:Anything}}}}}}), {Key:{t:TypeParameter$meta$declaration},Item:{t:Type$meta$model,a:{Type:{t:Anything}}}});
    }
    return getEmpty();
  }
  throw wrapexc(Exception(String$("IMPL ClassOrInterface.typeArguments ")),'15:63-15:99','?');
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Map,a:{Key:{t:TypeParameter$meta$declaration},Item:{t:Type$meta$model,a:{Type:{t:Anything}}}}},$cont:ClassOrInterface$meta$model,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Generic','$at','typeArguments']};});
defineAttr(ClassOrInterface$meta$model.$$.prototype,'string',function(){
    var mm = this.tipo.$$metamodel$$;
    if (typeof(mm)==='function') {
      mm=mm();
      this.tipo.$$metamodel$$=mm;
    }
    var qn=mm.d[0];
    for (var i=1; i<mm.d.length; i++)if(mm.d[i][0]!=='$')qn+=(i==1?"::":".")+mm.d[i];
    if (mm.$tp) {
      qn+="<";
      var first=true;
      for (var tp in mm.$tp) {
        var targ;
        if (this.$$targs$$ && this.$$targs$$.Type && this.$$targs$$.Type.a && this.$$targs$$.Type.a[tp]) {
          var _targ=this.$$targs$$.Type.a[tp];
          if (typeof(_targ)==='string') {
            console.log("TODO buscar " + tp + "->" + _targ + " para " + this.declaration.qualifiedName);
            _targ={t:Anything};
          }
          targ=typeLiteral$meta({Type:_targ});
        } else {
          targ=typeLiteral$meta({Type:{t:Anything}});
        }
        if (first)first=false; else qn+=",";
        if (targ.declaration) {
          qn+=targ.declaration.qualifiedName;
        } else {
          qn+=targ.string;
        }
      }
      qn+=">";
    }
    return String$(qn);
},undefined,function(){return{mod:$$METMODEL$$,$t:{t:String$},d:['ceylon.language','Object','$at','string']};});
defineAttr(ClassOrInterface$meta$model.$$.prototype,'hash',function(){
  var mm = this.tipo.$$metamodel$$;
  if (typeof(mm)==='function') {
    mm=mm();
    this.tipo.$$metamodel$$=mm;
  }
  var qn=mm.d[0];
  for (var i=1; i<mm.d.length; i++)if(mm.d[i][0]!=='$')qn+=(i==1?"::":".")+mm.d[i];
  var h=String$(qn).hash;
  if (mm.$tp) {
    var first=true;
    for (var tp in mm.$tp) {
      var targ;
      if (this.$$targs$$ && this.$$targs$$.Type && this.$$targs$$.Type.a && this.$$targs$$.Type.a[tp]) {
        var _targ=this.$$targs$$.Type.a[tp];
        if (typeof(_targ)==='string') {
          console.log("TODO buscar " + tp + "->" + _targ + " para " + this.declaration.qualifiedName);
          _targ={t:Anything};
        }
        targ=typeLiteral$meta({Type:_targ});
      } else {
        targ=typeLiteral$meta({Type:{t:Anything}});
      }
      h+=targ.hash;
    }
  }
  return h;
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Integer},d:['ceylon.language','Object','$at','hash']};});
defineAttr(ClassOrInterface$meta$model.$$.prototype,'extendedType',function(){
  var sc = this.tipo.$$metamodel$$['super'];
  if (sc === undefined)return null;
  var mm = sc.t.$$metamodel$$;
  if (typeof(mm)==='function') {
    mm = mm();
    sc.t.$$metamodel$$=mm;
  }
  var ac=(mm.$cont?AppliedMemberClass:AppliedClass)(sc.t, {Type:sc,Arguments:{t:Sequential,a:{Element:{t:Anything}}}});
  if (sc.a)ac.$targs=sc.a;
  return ac;
},undefined,function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:ClassModel$meta$model,a:{Arguments:{t:Nothing},Type:{t:Anything}}}]},$cont:AppliedClass,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Class','$at','extendedType']};});


