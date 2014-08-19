function AppliedValue$jsint(obj,attr,$$targs$$,$$appliedValue){
  if (attr===undefined)throw Exception("Value reference not found. Metamodel doesn't work with modules compiled in lexical scope style.");
  var mm = getrtmm$$(attr);
  $init$AppliedValue$jsint();
  if ($$appliedValue===undefined){
    if (obj||mm.$cont===undefined)$$appliedValue=new AppliedValue$jsint.$$;
    else {
      $$appliedValue=function(x){return AppliedValue$jsint(x,attr,$$targs$$);};
      $$appliedValue.$$=AppliedValue$jsint.$$;
      var dummy=new AppliedValue$jsint.$$;
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
      },undefined,dummy.$prop$getString.$crtmm$);
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
