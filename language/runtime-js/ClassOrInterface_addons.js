//Addendum to ClassOrInterface
ClassOrInterface$meta$model.$$.prototype.typeOf=function typeOf(instance$8){
  var _t={t:this.tipo};
  if (this.$targs)_t.a=this.$targs;
  return is$(instance$8,_t);
};
ClassOrInterface$meta$model.$$.prototype.typeOf.$crtmm$=function(){return{mod:$CCMM$,$t:{t:$_Boolean},ps:[{nm:'instance',mt:'prm',$t:{t:Anything},an:function(){return[];}}],$cont:ClassOrInterface$meta$model,an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Type','$m','typeOf']};};
ClassOrInterface$meta$model.$$.prototype.supertypeOf=function supertypeOf(t){
  if (is$(t,{t:AppliedUnionType$jsint}) || is$(t,{t:AppliedIntersectionType$jsint})) {
    return extendsType(t.tipo, this.$$targs$$.Type$ClassOrInterface);
  }
  return extendsType(t.$$targs$$.Target$Type,this.$$targs$$.Type$ClassOrInterface);
};
ClassOrInterface$meta$model.$$.prototype.supertypeOf.$crtmm$=function(){return{mod:$CCMM$,$t:{t:$_Boolean},ps:[{nm:'type',mt:'prm',$t:{t:Type$meta$model,a:{Type:{t:Anything}}},an:function(){return[];}}],$cont:ClassOrInterface$meta$model,an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Type','$m','supertypeOf']};};
ClassOrInterface$meta$model.$$.prototype.exactly=function exactly(type$10){
  return type$10.tipo && this.tipo === type$10.tipo;
};
ClassOrInterface$meta$model.$$.prototype.exactly.$crtmm$=function(){return{mod:$CCMM$,$t:{t:$_Boolean},ps:[{nm:'type',mt:'prm',$t:{t:Type$meta$model,a:{Type:{t:Anything}}},an:function(){return[];}}],$cont:ClassOrInterface$meta$model,an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Type','$m','exactly']};};
//This is the new shit May 2014
ClassOrInterface$meta$model.$$.prototype.getClasses=function(a,b){return coiclasse$(this,a,b);}
ClassOrInterface$meta$model.$$.prototype.getClasses.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Sequential,a:{Element$Sequential:{t:MemberClass$meta$model,a:{Arguments$MemberClass:'Arguments$getClasses',Container$MemberClass:'Container$getClasses',Type$MemberClass:'Type$getClasses'}}}},ps:[{nm:'annotationTypes',mt:'prm',seq:1,$t:{t:Sequential,a:{Element$Sequential:{t:Type$meta$model,a:{Type$Type:{t:Annotation}}}}},an:function(){return[];}}],$cont:ClassOrInterface$meta$model,tp:{Container$getClasses:{'def':{t:Nothing}},Type$getClasses:{'def':{t:Anything}},Arguments$getClasses:{sts:[{t:Sequential,a:{Element$Sequential:{t:Anything}}}],'def':{t:Nothing}}},an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','ClassOrInterface','$m','getClasses']};};
ClassOrInterface$meta$model.$$.prototype.getDeclaredClasses=function(anntypes,$$$mptypes) {
  return coiclasse$(this,anntypes,{Container$getClasses:$$$mptypes.Container$getDeclaredClasses,Type$getClasses:$$$mptypes.Type$getDeclaredClasses,Arguments$getClasses:$$$mptypes.Arguments$getDeclaredClasses},1);
}
ClassOrInterface$meta$model.$$.prototype.getDeclaredClasses.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Sequential,a:{Element$Sequential:{t:MemberClass$meta$model,a:{Arguments$MemberClass:'Arguments$getDeclaredClasses',Container$MemberClass:'Container$getDeclaredClasses',Type$MemberClass:'Type$getDeclaredClasses'}}}},ps:[{nm:'annotationTypes',mt:'prm',seq:1,$t:{t:Sequential,a:{Element$Sequential:{t:Type$meta$model,a:{Type$Type:{t:Annotation}}}}},an:function(){return[];}}],$cont:ClassOrInterface$meta$model,tp:{Container$getDeclaredClasses:{'def':{t:Nothing}},Type$getDeclaredClasses:{'def':{t:Anything}},Arguments$getDeclaredClasses:{sts:[{t:Sequential,a:{Element$Sequential:{t:Anything}}}],'def':{t:Nothing}}},an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','ClassOrInterface','$m','getDeclaredClasses']};};
ClassOrInterface$meta$model.$$.prototype.getInterfaces=function(anntypes,$$$mptypes){return coigetifc$(this,anntypes,$$$mptypes);}
ClassOrInterface$meta$model.$$.prototype.getInterfaces.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Sequential,a:{Element$Sequential:{t:MemberInterface$meta$model,a:{Container$MemberInterface:'Container$getInterfaces',Type$MemberInterface:'Type$getInterfaces'}}}},ps:[{nm:'annotationTypes',mt:'prm',seq:1,$t:{t:Sequential,a:{Element$Sequential:{t:Type$meta$model,a:{Type$Type:{t:Annotation}}}}},an:function(){return[];}}],$cont:ClassOrInterface$meta$model,tp:{Container$getInterfaces:{'def':{t:Nothing}},Type$getInterfaces:{'def':{t:Anything}}},an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','ClassOrInterface','$m','getInterfaces']};};
ClassOrInterface$meta$model.$$.prototype.getDeclaredInterfaces=function(anntypes,$$$mptypes){
return coigetifc$(this,anntypes,{Container$getInterfaces:$$$mptypes.Container$getDeclaredInterfaces,Type$getInterfaces:$$$mptypes.Type$getDeclaredInterfaces},1);
}
ClassOrInterface$meta$model.$$.prototype.getDeclaredInterfaces.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Sequential,a:{Element$Sequential:{t:MemberInterface$meta$model,a:{Container$MemberInterface:'Container$getDeclaredInterfaces',Type$MemberInterface:'Type$getDeclaredInterfaces'}}}},ps:[{nm:'annotationTypes',mt:'prm',seq:1,$t:{t:Sequential,a:{Element$Sequential:{t:Type$meta$model,a:{Type$Type:{t:Annotation}}}}},an:function(){return[];}}],$cont:ClassOrInterface$meta$model,tp:{Container$getDeclaredInterfaces:{'def':{t:Nothing}},Type$getDeclaredInterfaces:{'def':{t:Anything}}},an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','ClassOrInterface','$m','getDeclaredInterfaces']};};
ClassOrInterface$meta$model.$$.prototype.getAttributes=function(a,m){return coigetatr$(this,a,m);};

ClassOrInterface$meta$model.$$.prototype.getAttributes.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Sequential,a:{Element$Sequential:{t:Attribute$meta$model,a:{Set$Attribute:'Set$getAttributes',Container$Attribute:'Container$getAttributes',Get$Attribute:'Get$getAttributes'}}}},ps:[{nm:'annotationTypes',mt:'prm',seq:1,$t:{t:Sequential,a:{Element$Sequential:{t:Type$meta$model,a:{Type$Type:{t:Annotation}}}}},an:function(){return[];}}],$cont:ClassOrInterface$meta$model,tp:{Container$getAttributes:{'def':{t:Nothing}},Get$getAttributes:{'def':{t:Anything}},Set$getAttributes:{'def':{t:Nothing}}},an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','ClassOrInterface','$m','getAttributes']};};
ClassOrInterface$meta$model.$$.prototype.getDeclaredAttributes=function(anntypes,$$$mptypes) {
  return coigetatr$(this,anntypes,{Container$getAttributes:$$$mptypes.Container$getDeclaredAttributes,Get$getAttributes:$$$mptypes.Get$getDeclaredAttributes,Set$getAttributes:$$$mptypes.Set$getDeclaredAttributes},1);
}
ClassOrInterface$meta$model.$$.prototype.getDeclaredAttributes.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Sequential,a:{Element$Sequential:{t:Attribute$meta$model,a:{Set$Attribute:'Set$getDeclaredAttributes',Container$Attribute:'Container$getDeclaredAttributes',Get$Attribute:'Get$getDeclaredAttributes'}}}},ps:[{nm:'annotationTypes',mt:'prm',seq:1,$t:{t:Sequential,a:{Element$Sequential:{t:Type$meta$model,a:{Type$Type:{t:Annotation}}}}},an:function(){return[];}}],$cont:ClassOrInterface$meta$model,tp:{Container$getDeclaredAttributes:{'def':{t:Nothing}},Get$getDeclaredAttributes:{'def':{t:Anything}},Set$getDeclaredAttributes:{'def':{t:Nothing}}},an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','ClassOrInterface','$m','getDeclaredAttributes']};};
ClassOrInterface$meta$model.$$.prototype.getMethods=function(anntypes,$$$mptypes) {return coigetmtd$(this,anntypes,$$$mptypes);};
ClassOrInterface$meta$model.$$.prototype.getMethods.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Sequential,a:{Element$Sequential:{t:Method$meta$model,a:{Container$Method:'Container$getMethods',Arguments$Method:'Arguments$getMethods',Type$Method:'Type$getMethods'}}}},ps:[{nm:'annotationTypes',mt:'prm',seq:1,$t:{t:Sequential,a:{Element$Sequential:{t:Type$meta$model,a:{Type$Type:{t:Annotation}}}}},an:function(){return[];}}],$cont:ClassOrInterface$meta$model,tp:{Container$getMethods:{'def':{t:Nothing}},Type$getMethods:{'def':{t:Anything}},Arguments$getMethods:{sts:[{t:Sequential,a:{Element$Sequential:{t:Anything}}}],'def':{t:Nothing}}},an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','ClassOrInterface','$m','getMethods']};};
ClassOrInterface$meta$model.$$.prototype.getDeclaredMethods=function(anntypes,$$$mptypes){
  return coigetmtd$(this,anntypes,{Container$getMethods:$$$mptypes.Container$getDeclaredMethods,Type$getMethods:$$$mptypes.Type$getDeclaredMethods,Arguments$getMethods:$$$mptypes.Arguments$getDeclaredMethods},1);
}
ClassOrInterface$meta$model.$$.prototype.getDeclaredMethods.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Sequential,a:{Element$Sequential:{t:Method$meta$model,a:{Container$Method:'Container$getDeclaredMethods',Arguments$Method:'Arguments$getDeclaredMethods',Type$Method:'Type$getDeclaredMethods'}}}},ps:[{nm:'annotationTypes',mt:'prm',seq:1,$t:{t:Sequential,a:{Element$Sequential:{t:Type$meta$model,a:{Type$Type:{t:Annotation}}}}},an:function(){return[];}}],$cont:ClassOrInterface$meta$model,tp:{Container$getDeclaredMethods:{'def':{t:Nothing}},Type$getDeclaredMethods:{'def':{t:Anything}},Arguments$getDeclaredMethods:{sts:[{t:Sequential,a:{Element$Sequential:{t:Anything}}}],'def':{t:Nothing}}},an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','ClassOrInterface','$m','getDeclaredMethods']};};

function coi$get$anns(anntypes) {
  var ats=[];
  if (!anntypes)return ats;
  var iter=anntypes.iterator();
  var a;while((a=iter.next())!==getFinished()){
    ats.push({t:a.tipo});
  }
  return ats;
}
function coi$is$anns(anns,ats) {
  for (var i=0;i<ats.length;i++) {
    var f=false;
    for (var j=0;j<anns.length;j++) {
      f|=(is$(anns[j],ats[i]));
    }
    if (!f)return false;
  }
  return true;
}
