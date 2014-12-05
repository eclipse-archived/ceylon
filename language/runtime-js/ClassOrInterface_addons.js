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
