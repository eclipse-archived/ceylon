function AppliedAttribute(pname, atr,$$targs$$,$$appliedAttribute){
  $init$AppliedAttribute();
  if ($$appliedAttribute===undefined) {
    $$appliedAttribute=function(x){return AppliedValue$jsint(x,atr, {Get$Value:$$targs$$.Get$Attribute,Set$Value:$$targs$$.Set$Attribute,
      Container$Value:$$targs$$.Container$Attribute});};
    $$appliedAttribute.$$=AppliedAttribute.$$;
    var dummy=new AppliedAttribute.$$;
    $$appliedAttribute.getT$all=function(){return dummy.getT$all();};
    $$appliedAttribute.getT$name=function(){return dummy.getT$name();};
  }
  set_type_args($$appliedAttribute,$$targs$$);
  Attribute$meta$model($$appliedAttribute.$$targs$$===undefined?$$targs$$:{Get$Attribute:$$appliedAttribute.$$targs$$.Get$Attribute,Set$Attribute:$$appliedAttribute.$$targs$$.Set$Attribute,Container$Attribute:$$appliedAttribute.$$targs$$.Container$Attribute},$$appliedAttribute);
  $$appliedAttribute.tipo=atr;
  $$appliedAttribute.pname=pname;
  atr$($$appliedAttribute,'type',function(){
    var t = getrtmm$$(atr);
    if (t===undefined)throw Exception("Attribute reference not found. Metamodel doesn't work in modules compiled in lexical scope style.");
    t=t.$t;
    return typeLiteral$meta({Type$typeLiteral:t});
  },undefined,function(){return{mod:$CCMM$,$t:{t:Type$meta$model,a:{Type$Type:'Get$Attribute'}},$cont:AppliedAttribute,an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Attribute','$at','type']};});
  //AttributeGetterDefinition declaration at X (100:4-100:83)
  atr$($$appliedAttribute,'declaration',function(){
    var mm = getrtmm$$(atr);
    var _m = typeof(mm.mod)==='function'?mm.mod():mm.mod;
    var pkg = getModules$meta().find(_m['$mod-name'],_m['$mod-version']).findPackage(mm.d[0]);
    return OpenValue$jsint(pkg, atr);
  },undefined,function(){return{mod:$CCMM$,$t:{t:ValueDeclaration$meta$declaration},$cont:AppliedAttribute,an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Attribute','$at','declaration']};});
  $$appliedAttribute.$_bind=function(cont){
    return AppliedValue$jsint(cont,atr,{Get$Value:$$targs$$.Get$Attribute,Set$Value:$$targs$$.Set$Attribute,
      Container$Value:$$targs$$.Container$Attribute});
  }
  atr$($$appliedAttribute,'string',function(){
    var c=getrtmm$$(atr).$cont;
    if (typeof(c.$crtmm$)==='function')c.$crtmm$=c.$crtmm$();
    if (!c)return qname$(atr);
    c=c.$crtmm$;
    var qn=qname$(c);
    if (c.tp) {
      qn+="<"; var first=true;
      var cnt=$$targs$$&&$$targs$$.Container$Attribute&&$$targs$$.Container$Attribute.a;
      for (var tp in c.tp) {
        if (first)first=false;else qn+=",";
        var _ta=cnt&&cnt[tp];
        if (_ta) {
          qn+=qname$(_ta);
        } else qn+=qname$(Anything);
      }
      qn+=">";
    }
    qn+="."+pname;
    return qn;
  },undefined,function(){return{mod:$CCMM$,$t:{t:$_String},d:['$','Object','$at','string']};});
  $$appliedAttribute.equals=function(o) {
    return is$(o,{t:AppliedAttribute}) && o.tipo===atr;
  }
  return $$appliedAttribute;
}
AppliedAttribute.$crtmm$=function(){return{mod:$CCMM$,'super':{t:Basic},tp:{Get:{dv:'out','def':{t:Anything}},Set:{dv:'in','def':{t:Nothing}},Container:{dv:'in'}},sts:[{t:Attribute$meta$model,a:{Get:'Get',Set:'Set',Container:'Container'}}],pa:1,d:['ceylon.language.meta.model','Attribute']};};
ex$.AppliedAttribute=AppliedAttribute;
function $init$AppliedAttribute(){
    if (AppliedAttribute.$$===undefined){
        initTypeProto(AppliedAttribute,'ceylon.language.meta.model::AppliedAttribute',Basic,Attribute$meta$model);
        (function($$appliedAttribute){
//moved to initializer            
        })(AppliedAttribute.$$.prototype);
    }
    return AppliedAttribute;
}
ex$.$init$AppliedAttribute$meta$model=$init$AppliedAttribute;
$init$AppliedAttribute();
