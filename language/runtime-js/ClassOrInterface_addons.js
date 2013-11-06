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
ClassOrInterface$meta$model.$$.prototype.getDeclaredMethod=function(name,types,$$$mptypes) {
  var rv=this.getMethod(name,types,$$$mptypes);
  if (rv && rv.tipo) {
    var mm=rv.tipo.$$metamodel$$;
    if (typeof(mm)==='function'){mm=mm();rv.tipo.$$metamodel$$=mm;}
    if (mm && mm.$cont!==this.tipo)return null;
  }
  return rv;
}
ClassOrInterface$meta$model.$$.prototype.getDeclaredMethod.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:Method$meta$model,a:{Arguments:'Arguments',Type:'Type',Container:'Container'}}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$},$an:function(){return[];}},{$nm:'types',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element:{t:Type$meta$model,a:{Type:{t:Anything}}}}},$an:function(){return[];}}],$cont:ClassOrInterface$meta$model,$tp:{Container:{},Type:{},Arguments:{'satisfies':[{t:Sequential,a:{Element:{t:Anything}}}]}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','ClassOrInterface','$m','getDeclaredMethod']};};
ClassOrInterface$meta$model.$$.prototype.getAttribute=function getAttribute(name$15,$$$mptypes){
  if (!extendsType($$$mptypes.Container,{t:this.tipo}))throw IncompatibleTypeException$meta$model("Incompatible Container type argument");
  var nom = '$prop$get' + name$15[0].toUpperCase() + name$15.substring(1);
  var at = this.tipo.$$.prototype[nom];
  if (!at)return null;
  var mm=at.$$metamodel$$;
  if (typeof(mm)==='function') {
    mm=mm();at.$$metamodel$$=mm;
  }
  var _t=$$$mptypes.Get;
  if (mm && mm.$t) {
    if (!extendsType(mm.$t,_t))throw IncompatibleTypeException$meta$model("Incompatible Get type argument");
    if (!extendsType($$$mptypes.Set,at.set?mm.$t:{t:Nothing}))throw IncompatibleTypeException$meta$model("Incompatible Set type argument");
    _t=mm.$t;
  }
  var rv=AppliedAttribute(name$15, at, {Get:_t,Set:at.set?_t:{t:Nothing}, Container:{t:this.tipo}});
  if (this.$targs)rv.$$targs$$.Container.a=this.$targs;
  rv.$parent=this;
  return rv;
};
ClassOrInterface$meta$model.$$.prototype.getAttribute.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:Attribute$meta$model,a:{Get:'Get',Set:'Set',Container:'Container'}}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$},$an:function(){return[];}}],$cont:ClassOrInterface$meta$model,$tp:{Container:{},Type:{}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','ClassOrInterface','$m','getAttribute']};};
ClassOrInterface$meta$model.$$.prototype.getDeclaredAttribute=function getDeclaredAttribute(name$15,$$$mptypes){
  var rv=this.getAttribute(name$15,$$$mptypes);
  if (rv && rv.tipo) {
    var mm=rv.tipo.$$metamodel$$;
    if (typeof(mm)==='function'){mm=mm();rv.tipo.$$metamodel$$=mm;}
    if (mm&&mm.$cont!==this.tipo)return null;
  }
  return rv;
};ClassOrInterface$meta$model.$$.prototype.getDeclaredAttribute.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:Attribute$meta$model,a:{Get:'Get',Set:'Set',Container:'Container'}}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$},$an:function(){return[];}}],$cont:ClassOrInterface$meta$model,$tp:{Container:{},Type:{}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','ClassOrInterface','$m','getDeclaredAttribute']};};
defineAttr(ClassOrInterface$meta$model.$$.prototype,'container',function(){
  if (this.$parent)return this.$parent;
  var cont = this.tipo.$$metamodel$$.$cont;
  if (cont === undefined)return null;
  if (get_model(cont.$$metamodel$$).$mt === 'ifc')
    return AppliedInterface(cont,{Type:{t:cont}});
  return AppliedClass(cont,{Type:{t:cont},Arguments:{t:Sequential,a:{Element:{t:Anything}}}});
},undefined,function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:Type$meta$model,a:{Type:{t:Anything}}}]},$cont:ClassOrInterface$meta$model,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Member','$at','container']};});
ClassOrInterface$meta$model.$$.prototype.getClassOrInterface=function getClassOrInterface(name$2,types$3,$$$mptypes,noInherit){
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
  if (!ic) {
    if (noInherit)return null;
    var pere=mm['super'];
    while (!ic && pere) {
      mm=pere.t.$$metamodel$$;
      if (typeof(mm)==='function'){mm=mm();pere.t.$$metamodel$$=mm;}
      nom=mm&&mm.d?name$2+'$'+mm.d[mm.d.length-1]:undefined;
      if (nom)ic=$$clase.tipo.$$.prototype[nom];
      if (!ic)pere=mm['super'];
    }
  }
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
  return this.getClassOrInterface(name$2,types$3,$$$mptypes,1);
};ClassOrInterface$meta$model.$$.prototype.getDeclaredClassOrInterface.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:Member$meta$model,a:{Type:'Container',Kind:'Kind'}}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$},$an:function(){return[];}},{$nm:'types',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element:{t:Type$meta$model,a:{Type:{t:Anything}}}}},$an:function(){return[];}}],$cont:ClassOrInterface$meta$model,$tp:{Container:{},Kind:{'satisfies':[{t:ClassOrInterface$meta$model,a:{Type:{t:Anything}}}]}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','ClassOrInterface','$m','getDeclaredClassOrInterface']};};

ClassOrInterface$meta$model.$$.prototype.getClass=function getClass(name,types,$mptypes) {
  var rv=this.getClassOrInterface(name,types,{Container:$mptypes.Container, Kind:Class$meta$model});
  if (rv && !isOfType(rv, {t:AppliedMemberClass})) {
    throw IncompatibleTypeException$meta$model("Member " + name + " is not a class");
  }
  return rv;
};ClassOrInterface$meta$model.$$.prototype.getClass.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:MemberClass$meta$model},$ps:[],$cont:ClassOrInterface$meta$model,$tp:{Container:{},Type:{},Arguments:{satisfies:[{t:Sequential,a:{Element:{t:Anything}}}]}},d:['ceylon.language.meta.model','ClassOrInterface','$m','getClass']};};
ClassOrInterface$meta$model.$$.prototype.getDeclaredClass=function getClass(name,types,$mptypes) {
  var rv=this.getDeclaredClassOrInterface(name,types,{Container:$mptypes.Container, Kind:Class$meta$model});
  if (rv && !isOfType(rv, {t:AppliedMemberClass})) {
    throw IncompatibleTypeException$meta$model("Member " + name + " is not a class");
  }
  return rv;
};ClassOrInterface$meta$model.$$.prototype.getDeclaredClass.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:MemberClass$meta$model},$ps:[],$cont:ClassOrInterface$meta$model,$tp:{Container:{},Type:{},Arguments:{satisfies:[{t:Sequential,a:{Element:{t:Anything}}}]}},d:['ceylon.language.meta.model','ClassOrInterface','$m','getDeclaredClass']};};
ClassOrInterface$meta$model.$$.prototype.getInterface=function(name,types,$mptypes) {
  var rv=this.getClassOrInterface(name,types,{Container:$mptypes.Container, Kind:Interface$meta$model});
  if (rv && !isOfType(rv, {t:AppliedMemberInterface})) {
    throw IncompatibleTypeException$meta$model("Member " + name + " is not an interface");
  }
  return rv;
};ClassOrInterface$meta$model.$$.prototype.getInterface.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:MemberInterface$meta$model},$ps:[],$cont:ClassOrInterface$meta$model,$tp:{Container:{},Type:{},Arguments:{satisfies:[{t:Sequential,a:{Element:{t:Anything}}}]}},d:['ceylon.language.meta.model','ClassOrInterface','$m','getInterface']};};
ClassOrInterface$meta$model.$$.prototype.getDeclaredInterface=function(name,types,$mptypes) {
  var rv=this.getDeclaredClassOrInterface(name,types,{Container:$mptypes.Container, Kind:Interface$meta$model});
  if (rv && !isOfType(rv, {t:AppliedMemberInterface})) {
    throw IncompatibleTypeException$meta$model("Member " + name + " is not an interface");
  }
  return rv;
};ClassOrInterface$meta$model.$$.prototype.getDeclaredInterface.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:MemberInterface$meta$model},$ps:[],$cont:ClassOrInterface$meta$model,$tp:{Container:{},Type:{},Arguments:{satisfies:[{t:Sequential,a:{Element:{t:Anything}}}]}},d:['ceylon.language.meta.model','ClassOrInterface','$m','getDeclaredInterface']};};


ClassOrInterface$meta$model.$$.prototype.typeOf=function typeOf(instance$8){
  var _t={t:this.tipo};
  if (this.$targs)_t.a=this.$targs;
  return isOfType(instance$8,_t);
};
ClassOrInterface$meta$model.$$.prototype.typeOf.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$ps:[{$nm:'instance',$mt:'prm',$t:{t:Anything},$an:function(){return[];}}],$cont:ClassOrInterface$meta$model,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Type','$m','typeOf']};};
ClassOrInterface$meta$model.$$.prototype.supertypeOf=function supertypeOf(type$9){
  return extendsType({t:type$9.tipo}, {t:this.tipo});
};
ClassOrInterface$meta$model.$$.prototype.supertypeOf.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$ps:[{$nm:'type',$mt:'prm',$t:{t:Type$meta$model,a:{Type:{t:Anything}}},$an:function(){return[];}}],$cont:ClassOrInterface$meta$model,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Type','$m','supertypeOf']};};
ClassOrInterface$meta$model.$$.prototype.exactly=function exactly(type$10){
  return type$10.tipo && this.tipo === type$10.tipo;
};
ClassOrInterface$meta$model.$$.prototype.exactly.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},$ps:[{$nm:'type',$mt:'prm',$t:{t:Type$meta$model,a:{Type:{t:Anything}}},$an:function(){return[];}}],$cont:ClassOrInterface$meta$model,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Type','$m','exactly']};};
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
  throw Exception(String$("ClassOrInterface.typeArguments-we don't have a metamodel!"));
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Map,a:{Key:{t:TypeParameter$meta$declaration},Item:{t:Type$meta$model,a:{Type:{t:Anything}}}}},$cont:ClassOrInterface$meta$model,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Generic','$at','typeArguments']};});
defineAttr(ClassOrInterface$meta$model.$$.prototype,'string',function(){
    var mm = this.tipo.$$metamodel$$;
    if (typeof(mm)==='function') {
      mm=mm();
      this.tipo.$$metamodel$$=mm;
    }
    var qn=$qname(mm);
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
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},d:['ceylon.language','Object','$at','string']};});
defineAttr(ClassOrInterface$meta$model.$$.prototype,'hash',function(){
  var mm = this.tipo.$$metamodel$$;
  if (typeof(mm)==='function') {
    mm=mm();
    this.tipo.$$metamodel$$=mm;
  }
  var h=String$($qname(mm)).hash;
  if (mm.$tp) {
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
  if (this.$bound)h+=this.$bound.hash;
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


