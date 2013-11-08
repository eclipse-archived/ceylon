defineAttr(InterfaceModel$meta$model.$$.prototype,'declaration',function(){
if (this._decl)return this._decl;
var mm = this.tipo.$$metamodel$$;
if (typeof(mm)==='function') {
  mm = mm();
  this.tipo.$$metamodel$$=mm;
}
this._decl = OpenInterface(getModules$meta().find(mm.mod['$mod-name'],mm.mod['$mod-version']).findPackage(mm.d[0]), this.tipo);
return this._decl;
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:InterfaceDeclaration$meta$declaration},$cont:InterfaceModel$meta$model,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','InterfaceModel','$at','declaration']};});

InterfaceModel$meta$model.$$.prototype.equals=function(o){
return isOfType(o,{t:AppliedInterface}) && (o.tipo$2||o.tipo)==this.tipo && this.typeArguments.equals(o.typeArguments);
};
InterfaceModel$meta$model.$$.prototype.equals.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},d:['ceylon.language','Object','$m','equals'],$ps:[{$nm:'other',$t:{t:Object$}}],$cont:InterfaceModel$meta$model};}
