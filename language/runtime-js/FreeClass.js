function FreeClass(declaration, $$freeClass){
  $init$FreeClass();
  if ($$freeClass===undefined)$$freeClass=new FreeClass.$$;
  OpenClassType$meta$declaration($$freeClass);
  $$freeClass._decl=declaration;
  $$freeClass.$prop$getDeclaration={$crtmm$:function(){return{mod:$CCMM$,$t:{t:ClassDeclaration$meta$declaration},$cont:FreeClass,pa:3,d:['ceylon.language.meta.declaration','OpenClassType','$at','declaration']};}};
  $$freeClass.$prop$getDeclaration.get=function(){return declaration};
  return $$freeClass;
}
FreeClass.$crtmm$=function(){return{mod:$CCMM$,'super':{t:Basic},sts:[{t:OpenClassType$meta$declaration}],d:['ceylon.language.meta.declaration','OpenClassType']};};
function $init$FreeClass(){
  if (FreeClass.$$===undefined){
   initTypeProto(FreeClass,'ceylon.language.meta.declaration::FreeClass',Basic,OpenClassType$meta$declaration);
   (function($$freeClass){
    $$freeClass.equals=function(other) {
      return is$(other,{t:FreeClass}) && other.declaration.equals(this.declaration) && this.typeArguments.equals(other.typeArguments);
    }
   })(FreeClass.$$.prototype);
  }
  return FreeClass;
}
ex$.$init$FreeClass=$init$FreeClass;
$init$FreeClass();
