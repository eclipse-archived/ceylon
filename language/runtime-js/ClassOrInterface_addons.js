defineAttr(ClassOrInterface$meta$model.$$.prototype,'satisfiedTypes',function(){
  var ints = this.tipo.$$metamodel$$['satisfies'];
  if (ints && ints.length) {
    var rv = [];
    for (var i=0; i < ints.length; i++) {
      var ifc = ints[i].t;
      var mm = ifc.$$metamodel$$;
      if (typeof(mm)==='function') {
        mm = mm();
        ifc.$$metamodel$$=mm;
      }
      rv.push(AppliedInterface(ifc, {Type:{t:Anything}}));
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
  return AppliedMethod(fun, {Container:$$$mptypes.SubType,Type:$$$mptypes.Type,Arguments:$$$mptypes.Arguments});
}
ClassOrInterface$meta$model.$$.prototype.getMethod.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Method$meta$model,a:{SubType:'SubType',Type:'Type',Arguments:'Arguments'}},$an:function(){return[shared(),formal()];},d:['ceylon.language.meta.model','ClassOrInterface']};};
ClassOrInterface$meta$model.$$.prototype.getMethod.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:Method$meta$model,a:{Arguments:'Arguments',Type:'Type',Container:'SubType'}}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$},$an:function(){return[];}},{$nm:'types',$mt:'prm',seq:1,$t:{t:Sequential,a:{Element:{t:Type$meta$model,a:{Type:{t:Anything}}}}},$an:function(){return[];}}],$cont:ClassOrInterface$meta$model,$tp:{SubType:{},Type:{},Arguments:{'satisfies':[{t:Sequential,a:{Element:{t:Anything}}}]}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','ClassOrInterface','$m','getMethod']};};
ClassOrInterface$meta$model.$$.prototype.getAttribute=function getAttribute(name$15,$$$mptypes){
  var nom = '$prop$get' + name$15[0].toUpperCase() + name$15.substring(1);
  var at = this.tipo.$$.prototype[nom];
  if (!at)return null;
  return (at.set?AppliedVariableAttribute:AppliedAttribute)(nom, at, $$$mptypes);
};
ClassOrInterface$meta$model.$$.prototype.getAttribute.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:Attribute$meta$model,a:{Type:'Type',Container:'SubType'}}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$},$an:function(){return[];}}],$cont:ClassOrInterface$meta$model,$tp:{SubType:{},Type:{}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','ClassOrInterface','$m','getAttribute']};};
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
ClassOrInterface$meta$model.$$.prototype.getVariableAttribute.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:VariableAttribute$meta$model,a:{Type:'Type',Container:'SubType'}}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$},$an:function(){return[];}}],$cont:ClassOrInterface$meta$model,$tp:{SubType:{},Type:{}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','ClassOrInterface','$m','getVariableAttribute']};};

