function OpenSetter(v, $$openSetter){
  $init$OpenSetter();
  if ($$openSetter===undefined)$$openSetter=new OpenSetter.$$;
  $$openSetter.variable_=v;
  SetterDeclaration$meta$declaration($$openSetter);
  $$openSetter.tipo=v.tipo.set;
  if (v.tipo.set && getrtmm$$(v.tipo.set)) {
    var mm=getrtmm$$(v.tipo.set)
    if (typeof(mm.an)==='function')mm.an=mm.an();
    v.tipo.set.$crtmm$=mm;
  }
  return $$openSetter;
}
OpenSetter.$crtmm$=function(){return{mod:$CCMM$,'super':{t:Basic},sts:[{t:SetterDeclaration$meta$declaration}],d:['ceylon.language.meta.declaration','SetterDeclaration']};};
function $init$OpenSetter(){
  if (OpenSetter.$$===undefined){
    initTypeProto(OpenSetter,'ceylon.language.meta.declaration::OpenSetter',Basic,SetterDeclaration$meta$declaration);
    (function($$openSetter){
      atr$($$openSetter,'variable',function(){return this.variable_;},undefined,function(){return{mod:$CCMM$,$t:{t:ValueDeclaration$meta$declaration},$cont:OpenSetter,pa:3,d:['ceylon.language.meta.declaration','SetterDeclaration','$at','variable']};});
      atr$($$openSetter,'name',function(){return this.variable_.name;},undefined,function(){return{mod:$CCMM$,$t:{t:$_String},$cont:OpenSetter,pa:3,d:['ceylon.language.meta.declaration','SetterDeclaration','$at','name']};});
atr$($$openSetter,'name',function(){return this.variable.name;},undefined,function(){return{mod:$CCMM$,$t:{t:$_String},$cont:OpenSetter,pa:3,d:['ceylon.language.meta.declaration','ValueDeclaration','$at','name']};});
  atr$($$openSetter,'string',function(){return "setter " + this.qualifiedName;},undefined,function(){return{$t:{t:$_String},$cont:OpenSetter,an:function(){return[shared(),actual()]},mod:$CCMM$,d:['$','Object','$at','string']};});
  atr$($$openSetter,'qualifiedName',function(){return this.variable.qualifiedName;},undefined,function(){return{mod:$CCMM$,$t:{t:$_String},$cont:OpenSetter,pa:3,d:['ceylon.language.meta.declaration','Declaration','$at','qualifiedName']};});
      $$openSetter.equals=function(o) {
        return is$(o,{t:OpenSetter}) && o.variable.equals(this.variable);
      }
    })(OpenSetter.$$.prototype);
  }
  return OpenSetter;
}
ex$.OpenSetter=OpenSetter;
$init$OpenSetter();
