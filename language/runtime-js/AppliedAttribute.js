function AppliedAttribute(pname, atr,$a$,that){
  if (!$a$.Get$AppliedAttribute)$a$.Get$AppliedAttribute=$a$.Get$Attribute;
  if (!$a$.Set$AppliedAttribute)$a$.Set$AppliedAttribute=$a$.Set$Attribute;
  if (!$a$.Container$AppliedAttribute)$a$.Container$AppliedAttribute=$a$.Container$Attribute;
  $i$AppliedAttribute();
  if (that===undefined) {
    that=function(x){return AppliedValue$jsint(x,atr,
      {Get$AppliedValue:$a$.Get$AppliedAttribute,Set$AppliedValue:$a$.Set$AppliedAttribute,
      Container$AppliedValue:$a$.Container$AppliedAttribute});};
    that.$$=AppliedAttribute.$$;
    var dummy=new AppliedAttribute.$$;
    that.getT$all=function(){return dummy.getT$all();};
    that.getT$name=function(){return dummy.getT$name();};
  }
  Attribute$meta$model(that.$a$===undefined?$a$:{Get$Attribute:that.$a$.Get$AppliedAttribute,
    Set$Attribute:that.$a$.Set$AppliedAttribute,
    Container$Attribute:that.$a$.Container$AppliedAttribute},that);
  set_type_args(that,$a$,AppliedAttribute);
  that.tipo=atr;
  that.pname=pname;
  atr$(that,'declaringType',function(){return memberDeclaringType$(that);
  },undefined,Member$meta$model.$$.prototype.$prop$getDeclaringType.$m$);
  atr$(that,'type',function(){
    var t = getrtmm$$(atr);
    if (t===undefined)throw Exception("Attribute reference not found. Metamodel doesn't work in modules compiled in lexical scope style.");
    return typeLiteral$meta({Type$typeLiteral:restype$($a$.Container$Attribute,t.$t)});
  },undefined,function(){return{mod:$CCMM$,$t:{t:Type$meta$model,a:{Type$Type:'Get$Attribute'}},$cont:AppliedAttribute,an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Attribute','$at','type']};});
  //AttributeGetterDefinition declaration at X (100:4-100:83)
  atr$(that,'declaration',function(){
    var mm = getrtmm$$(atr);
    var _m = typeof(mm.mod)==='function'?mm.mod():mm.mod;
    var pkg = modules$meta().find(_m['$mod-name'],_m['$mod-version']).findPackage(mm.d[0]);
    return OpenValue$jsint(pkg, atr);
  },undefined,function(){return{mod:$CCMM$,$t:{t:ValueDeclaration$meta$declaration},$cont:AppliedAttribute,an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Attribute','$at','declaration']};});
  that.$_bind=function(cont){
    return AppliedValue$jsint(cont==null?1:cont,atr,{Get$AppliedValue:$a$.Get$AppliedAttribute,
      Set$AppliedValue:$a$.Set$AppliedAttribute,
      Container$AppliedValue:$a$.Container$AppliedAttribute});
  }
  atr$(that,'string',function(){
    var c=getrtmm$$(atr).$cont;
    if (typeof(c.$m$)==='function')c.$m$=c.$m$();
    if (!c)return qname$(atr);
    c=c.$m$;
    var qn=qname$(c);
    if (c.tp) {
      qn+="<"; var first=true;
      var cnt=$a$&&$a$.Container$AppliedAttribute&&$a$.Container$AppliedAttribute.a;
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
  atr$(that,'container',function(){
    return AppliedValue$jsint.$$.prototype.$prop$getContainer.get.call(this);
  },undefined,function(){return{mod:$CCMM$,$t:{t:Type$meta$model,a:{Type$Type:{t:Anything}}},$cont:AppliedAttribute,pa:67,d:['ceylon.language.jsint','Member','$at','container']};});
  that.equals=function(o) {
    return is$(o,{t:AppliedAttribute}) && o.tipo===atr;
  }
  atr$(that,'hash',function(){
    return this.string.hash;
  },undefined,function(){return{mod:$CCMM$,$t:{t:Integer},$cont:AppliedAttribute,pa:67,d:['ceylon.language','Object','$at','hash']};});
  return that;
}
AppliedAttribute.$m$=function(){return{mod:$CCMM$,'super':{t:Basic},tp:{Get:{dv:'out','def':{t:Anything}},Set:{dv:'in','def':{t:Nothing}},Container:{dv:'in'}},sts:[{t:Attribute$meta$model,a:{Get:'Get',Set:'Set',Container:'Container'}}],pa:1,d:['ceylon.language.meta.model','Attribute']};};
ex$.AppliedAttribute=AppliedAttribute;
function $i$AppliedAttribute(){
    if (AppliedAttribute.$$===undefined){
        initTypeProto(AppliedAttribute,'ceylon.language.meta.model::AppliedAttribute',Basic,Attribute$meta$model);
        (function($$appliedAttribute){
//moved to initializer            
        })(AppliedAttribute.$$.prototype);
    }
    return AppliedAttribute;
}
ex$.$i$AppliedAttribute$meta$model=$i$AppliedAttribute;
$i$AppliedAttribute();
