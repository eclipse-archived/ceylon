function AppliedValue(obj,attr,$$targs$$,$$appliedValue){
  if (attr===undefined)throw Exception("Value reference not found. Metamodel doesn't work with modules compiled in lexical scope style.");
  var mm = getrtmm$$(attr);
  $init$AppliedValue();
  if ($$appliedValue===undefined){
    if (obj||mm.$cont===undefined)$$appliedValue=new AppliedValue.$$;
    else {
      $$appliedValue=function(x){return AppliedValue(x,attr,$$targs$$);};
      $$appliedValue.$$=AppliedValue.$$;
      var dummy=new AppliedValue.$$;
      $$appliedValue.getT$all=function(){return dummy.getT$all();};
      $$appliedValue.getT$name=function(){return dummy.getT$name();};
atr$($$appliedValue,'string',function(){
  var qn;
  if ($$targs$$ && $$targs$$.Container$Value) {
    qn = typeLiteral$meta({Type$typeLiteral:$$targs$$.Container$Value}).string + "." + mm.d[mm.d.length-1];
  } else if (mm.$cont) {
    qn = typeLiteral$meta({Type$typeLiteral:{t:mm.$cont}}).string + "." + mm.d[mm.d.length-1];
  } else {
    qn=qname$(mm);
  }
  return qn;
},undefined,function(){return{mod:$CCMM$,$t:{t:$_String},d:['$','Object','$at','string']};});
    }
  }
  set_type_args($$appliedValue,$$targs$$);
  Value$meta$model($$appliedValue.$$targs$$===undefined?$$targs$$:{Get$Value:$$appliedValue.$$targs$$.Get$Value,Set$Value:$$appliedValue.$$targs$$.Set$Value},$$appliedValue);
  if($$targs$$.Container$Value)Attribute$meta$model({Get$Attribute:$$targs$$.Get$Value,
    Set$Attribute:$$targs$$.Set$Value,Container$Attribute:$$targs$$.Container$Value},$$appliedValue);//TODO checar si no es if Container$Attribute
  $$appliedValue.obj=obj;
  $$appliedValue.tipo=attr;
  return $$appliedValue;
}
AppliedValue.$crtmm$=function(){return{mod:$CCMM$,'super':{t:Basic},tp:{Get:{dv:'out'},Set:{dv:'in'}},
  sts:[{t:Value$meta$model,a:{Get:'Get',Set:'Set'}}],pa:1,d:['ceylon.language.meta.model','Value']};};
ex$.AppliedValue$meta$model=AppliedValue;
function $init$AppliedValue(){
  if (AppliedValue.$$===undefined){
    initTypeProto(AppliedValue,'ceylon.language.meta.model::AppliedValue',Basic,Value$meta$model);
    (function($$appliedValue){
atr$($$appliedValue,'string',function(){
  return qname$(this.tipo);
},undefined,function(){return{mod:$CCMM$,$t:{t:$_String},d:['$','Object','$at','string']};});
      atr$($$appliedValue,'declaration',function(){
        var $$av=this;
        var mm = $$av.tipo.$crtmm$;
        var _m = typeof(mm.mod)==='function'?mm.mod():mm.mod;
        var _pkg = getModules$meta().find(_m['$mod-name'],_m['$mod-version']).findPackage(mm.d[0]);
        return OpenValue(_pkg, $$av.tipo);
      },undefined,function(){return{mod:$CCMM$,$t:{t:ValueDeclaration$meta$declaration},$cont:AppliedValue,an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Value','$at','declaration']};});

      $$appliedValue.$_get=function $_get(){
        if (this.obj) {
          var mm=this.tipo.$crtmm$;
          return (mm&&mm.d&&this.obj[mm.d[mm.d.length-1]])||this.tipo.get.call(this.obj);
        }
        return this.tipo.get();
      };$$appliedValue.$_get.$crtmm$=function(){return{mod:$CCMM$,$t:'Get',ps:[],$cont:AppliedValue,an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Value','$m','get']};};
      $$appliedValue.set=function set(newValue$26){
        if (!this.tipo.set)throw MutationException$meta$model("Value is not writable");
        return this.obj?this.tipo.set.call(this.obj,newValue$26):this.tipo.set(newValue$26);
      };$$appliedValue.set.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Anything},ps:[{nm:'newValue',mt:'prm',$t:'Set',an:function(){return[];}}],$cont:AppliedValue,an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Value','$m','set']};};
 
$$appliedValue.setIfAssignable=function(v) {
  var mm = this.tipo.$crtmm$;
  if (!is$(v,mm.$t))throw IncompatibleTypeException$meta$model("The specified value has the wrong type");
  var mdl=get_model(mm);
  if (!mdl || (mdl.pa & 1024)===0)throw MutationException$meta$model("Attempt to modify a value that is not variable");
  this.obj?this.tipo.set.call(this.obj,v):this.tipo.set(v);
};$$appliedValue.setIfAssignable.$crtmm$=function(){return{mod:$CCMM$,ps:[],$cont:AppliedValue,d:['ceylon.language.meta.model','Value','$m','setIfAssignable']};};

      atr$($$appliedValue,'type',function(){
          var $$atr=this;
          var t = getrtmm$$($$atr.tipo);
          return typeLiteral$meta({Type$typeLiteral:t.$t});
      },undefined,function(){return{mod:$CCMM$,$t:{t:Type$meta$model,a:{Type$Type:'Get$Value'}},$cont:AppliedValue,an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Value','$at','type']};});

      atr$($$appliedValue,'container',function(){
          if (this.obj) {
            var mm=getrtmm$$(this.tipo);
            if (mm.$cont)return type$meta(this.obj,{Type$type:{t:mm.$cont}});
          }
          if (this.$$targs$$.Container$Value) {
            return typeLiteral$meta({Type$typeLiteral:this.$$targs$$.Container$Value});
          } else if (this.$$targs$$.Container$Attribute) {
            return typeLiteral$meta({Type$typeLiteral:this.$$targs$$.Container$Attribute});
          }
          return null;
      },undefined,function(){return{mod:$CCMM$,$t:{ t:'u', l:[{t:Null},{t:Type$meta$model,a:{Type$Type:{t:Anything}}}]},$cont:AppliedValue,an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Value','$at','container']};});

    })(AppliedValue.$$.prototype);
  }
  return AppliedValue;
}
ex$.$init$AppliedValue$meta$model=$init$AppliedValue;
$init$AppliedValue();
