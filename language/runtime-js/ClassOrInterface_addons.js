//Addendum to ClassOrInterface
ClassOrInterface$meta$model.$$.prototype.typeOf=function typeOf(o){
  var _t={t:this.tipo};
  if (this.$targs)_t.a=this.$targs;
  return is$(o,_t);
};
ClassOrInterface$meta$model.$$.prototype.typeOf.$m$=function(){return{mod:$CCMM$,$t:{t:$_Boolean},ps:[{nm:'instance',mt:'prm',$t:{t:Anything},an:function(){return[];}}],$cont:ClassOrInterface$meta$model,an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Type','$m','typeOf']};};
ClassOrInterface$meta$model.$$.prototype.supertypeOf=function supertypeOf(t){
  if (is$(t,{t:AppliedUnionType$jsint}) || is$(t,{t:AppliedIntersectionType$jsint})) {
    return extendsType(t.tipo, this.$$targs$$.Type$ClassOrInterface);
  }
  return extendsType(t.$$targs$$.Target$Type,this.$$targs$$.Type$ClassOrInterface);
};
ClassOrInterface$meta$model.$$.prototype.supertypeOf.$m$=function(){return{mod:$CCMM$,$t:{t:$_Boolean},ps:[{nm:'type',mt:'prm',$t:{t:Type$meta$model,a:{Type:{t:Anything}}},an:function(){return[];}}],$cont:ClassOrInterface$meta$model,an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Type','$m','supertypeOf']};};
ClassOrInterface$meta$model.$$.prototype.exactly=function exactly(t){
  return t.tipo && this.tipo === t.tipo;
};
ClassOrInterface$meta$model.$$.prototype.exactly.$m$=function(){return{mod:$CCMM$,$t:{t:$_Boolean},ps:[{nm:'type',mt:'prm',$t:{t:Type$meta$model,a:{Type:{t:Anything}}},an:function(){return[];}}],$cont:ClassOrInterface$meta$model,an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Type','$m','exactly']};};
