function FreeInterface(declaration, $$freeInterface){
    $init$FreeInterface();
    if ($$freeInterface===undefined)$$freeInterface=new FreeInterface.$$;
    OpenInterfaceType$meta$declaration($$freeInterface);
    
    //AttributeDeclaration declaration at X (173:4-173:50)
    $$freeInterface._decl=declaration;
    $$freeInterface.$prop$getDeclaration.get=function(){return declaration};
    return $$freeInterface;
}
FreeInterface.$crtmm$=function(){return{mod:$CCMM$,'super':{t:Basic},sts:[{t:OpenInterfaceType$meta$declaration}],d:['ceylon.language.meta.declaration','OpenInterfaceType']};};
function $init$FreeInterface(){
  if (FreeInterface.$$===undefined){
    initTypeProto(FreeInterface,'ceylon.language.meta.declaration::FreeInterface',Basic,OpenInterfaceType$meta$declaration);
    (function($$freeInterface){
    $$freeInterface.equals=function(other) {
      return is$(other,{t:FreeInterface}) && other.declaration.equals(this.declaration) && this.typeArguments.equals(other.typeArguments);
    }
            
    })(FreeInterface.$$.prototype);
  }
  return FreeInterface;
}
ex$.$init$FreeInterface=$init$FreeInterface;
$init$FreeInterface();
