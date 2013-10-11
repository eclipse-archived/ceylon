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
      rv.push(AppliedInterface(ifc.t, {Type:ifc}));
    }
    return rv.reifyCeylonType({Absent:{t:Null},Element:{t:InterfaceModel$meta$model,a:{Type:{t:Anything}}}});
  }
  return getEmpty();
},undefined,function(){
  return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:{t:InterfaceModel$meta$model,a:{Type:{t:Anything}}}}},$cont:ClassOrInterface$meta$model,
  $an:function(){return[shared(),formal()];},d:['ceylon.language.meta.model','ClassOrInterface','$at','satisfiedTypes']};
});
ClassOrInterface$meta$model.$$.prototype.getMethod=function(name,types,$$$mptypes) {
  var fun = this.tipo[name];
  if (!fun) fun = this.tipo.$$.prototype[name];
  if (!fun) return null;
  if (typeof(fun)!=='function')return null;
  return AppliedMethod(fun, types, {Container:$$$mptypes.Container,Type:$$$mptypes.Type,Arguments:$$$mptypes.Arguments});
}
ClassOrInterface$meta$model.$$.prototype.getMethod.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:Method$meta$model,a:{Arguments:'Arguments',Type:'Type',Container:'Container'}}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$},$an:function(){return[];}},{$nm:'types',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element:{t:Type$meta$model,a:{Type:{t:Anything}}}}},$an:function(){return[];}}],$cont:ClassOrInterface$meta$model,$tp:{Container:{},Type:{},Arguments:{'satisfies':[{t:Sequential,a:{Element:{t:Anything}}}]}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','ClassOrInterface','$m','getMethod']};};
ClassOrInterface$meta$model.$$.prototype.getAttribute=function getAttribute(name$15,$$$mptypes){
  var nom = '$prop$get' + name$15[0].toUpperCase() + name$15.substring(1);
  var at = this.tipo.$$.prototype[nom];
  if (!at)return null;
  return (at.set?AppliedVariableAttribute:AppliedAttribute)(name$15, at, $$$mptypes);
};
ClassOrInterface$meta$model.$$.prototype.getAttribute.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:Attribute$meta$model,a:{Type:'Type',Container:'Container'}}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$},$an:function(){return[];}}],$cont:ClassOrInterface$meta$model,$tp:{Container:{},Type:{}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','ClassOrInterface','$m','getAttribute']};};
defineAttr(ClassOrInterface$meta$model.$$.prototype,'container',function(){
  var $$coi=this;
  var cont = $$coi.tipo.$$metamodel$$.$cont;
  if (cont === undefined)return null;
  return (get_model(cont.$$metamodel$$).$mt === 'ifc' ? AppliedInterface : AppliedClass)(cont,{Type:{t:cont},Arguments:{t:Nothing}});
},undefined,function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:Type$meta$model,a:{Type:{t:Anything}}}]},$cont:ClassOrInterface$meta$model,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Member','$at','container']};});

ClassOrInterface$meta$model.$$.prototype.getVariableAttribute=function getVariableAttribute(name$16,$$$mptypes){
  var nom = '$prop$get' + name$15[0].toUpperCase() + name$15.substring(1);
  if (nom.set == undefined)throw Exception("Attribute " + name$16 + " is not variable");
  return AppliedVariableAttribute(nom, this.tipo.$$.prototype[nom], $$$mptypes);
};
ClassOrInterface$meta$model.$$.prototype.getVariableAttribute.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:VariableAttribute$meta$model,a:{Type:'Type',Container:'Container'}}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$},$an:function(){return[];}}],$cont:ClassOrInterface$meta$model,$tp:{Container:{},Type:{}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','ClassOrInterface','$m','getVariableAttribute']};};
ClassOrInterface$meta$model.$$.prototype.getClassOrInterface=function getClassOrInterface(name$2,types$3,$$$mptypes){
  var $$clase=this;
  if(types$3===undefined){types$3=getEmpty();}
  var mm = $$clase.tipo.$$metamodel$$;
  if (typeof(mm)==='function') {
    mm=mm(); $$clase.tipo.$$metamodel$$=mm;
  }
  var nom = name$2 + '$' + mm.d[mm.d.length-1];
  var ic = $$clase.tipo.$$.prototype[nom];
  if (ic) {
    if (types$3) {
      //TODO type arguments for ic
    }
    mm = ic.$$metamodel$$;
    if (typeof(mm)==='function') {
      mm=mm(); ic.$$metamodel$$=mm;
    }
    var md = get_model(mm);
    if (md.$mt==='ifc') {
      return AppliedInterface(ic, {Type:{t:ic}});
    } else if (md.$mt==='cls'){
      return AppliedClass(ic, {Type:{t:ic}, Arguments:{t:Nothing}});
    }
  }
  return null;
};ClassOrInterface$meta$model.$$.prototype.getClassOrInterface.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:Member$meta$model,a:{Type:'Container',Kind:'Kind'}}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$},$an:function(){return[];}},{$nm:'types',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element:{t:Type$meta$model,a:{Type:{t:Anything}}}}},$an:function(){return[];}}],$cont:ClassOrInterface$meta$model,$tp:{Container:{},Kind:{'satisfies':[{t:ClassOrInterface$meta$model,a:{Type:{t:Anything}}}]}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','ClassOrInterface','$m','getClassOrInterface']};};
ClassOrInterface$meta$model.$$.prototype.isTypeOf=function isTypeOf(instance$8){
  var coi=this;
  return isOfType(instance$8,{t:coi.tipo});
};
ClassOrInterface$meta$model.$$.prototype.isTypeOf.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Boolean},$ps:[{$nm:'instance',$mt:'prm',$t:{t:Anything},$an:function(){return[];}}],$cont:ClassOrInterface$meta$model,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Type','$m','isTypeOf']};};
ClassOrInterface$meta$model.$$.prototype.isSuperTypeOf=function isSuperTypeOf(type$9){
  throw wrapexc(Exception(String$("IMPL ClassOrInterface.isSuperTypeOf")),'26:67-26:103','?');
};
ClassOrInterface$meta$model.$$.prototype.isSuperTypeOf.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Boolean},$ps:[{$nm:'type',$mt:'prm',$t:{t:Type$meta$model,a:{Type:{t:Anything}}},$an:function(){return[];}}],$cont:ClassOrInterface$meta$model,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Type','$m','isSuperTypeOf']};};
ClassOrInterface$meta$model.$$.prototype.isExactly=function isExactly(type$10){
  throw wrapexc(Exception(String$("IMPL ClassOrInterface.isExactly")),'27:63-27:99','?');
};
ClassOrInterface$meta$model.$$.prototype.isExactly.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Boolean},$ps:[{$nm:'type',$mt:'prm',$t:{t:Type$meta$model,a:{Type:{t:Anything}}},$an:function(){return[];}}],$cont:ClassOrInterface$meta$model,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Type','$m','isExactly']};};
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
            console.log("TODO buscar " + tp + "->" + _targ + " para " + this.declaration.qualifedName);
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


