function AppliedValue$jsint(obj,attr,$$targs$$,$$appliedValue){
  if (attr===undefined)throw Exception("Value reference not found. Metamodel doesn't work with modules compiled in lexical scope style.");
  if (!$$targs$$.Get$AppliedValue)$$targs$$.Get$AppliedValue=$$targs$$.Get$Value;
  if (!$$targs$$.Set$AppliedValue)$$targs$$.Set$AppliedValue=$$targs$$.Set$Value;
  if (!$$targs$$.Container$AppliedValue && $$targs$$.Container$Value)$$targs$$.Container$AppliedValue=$$targs$$.Container$Value;
  var mm = getrtmm$$(attr);
  $init$AppliedValue$jsint();
  if ($$appliedValue===undefined){
    if (obj||mm.$cont===undefined)$$appliedValue=new AppliedValue$jsint.$$;
    else {
      $$appliedValue=function AppliedVal1(x){return AppliedValue$jsint(x,attr,$$targs$$);};
      $$appliedValue.$$=AppliedValue$jsint.$$;
      var dummy=new AppliedValue$jsint.$$;
      $$appliedValue.getT$all=function(){return dummy.getT$all();};
      $$appliedValue.getT$name=function(){return dummy.getT$name();};
      atr$($$appliedValue,'string',function(){
        var qn;
        if ($$targs$$ && $$targs$$.Container$AppliedValue) {
          qn = typeLiteral$meta({Type$typeLiteral:$$targs$$.Container$AppliedValue}).string + "." + mm.d[mm.d.length-1];
        } else if (mm.$cont) {
          qn = typeLiteral$meta({Type$typeLiteral:{t:mm.$cont}}).string + "." + mm.d[mm.d.length-1];
        } else {
          qn=qname$(mm);
        }
        return qn;
      },undefined,dummy.$prop$getString.$crtmm$);
    }
  }
  Value$meta$model({Get$Value:$$targs$$.Get$AppliedValue,
    Set$Value:$$targs$$.Set$AppliedValue},$$appliedValue);
  if($$targs$$.Container$AppliedValue)Attribute$meta$model({Get$Attribute:$$targs$$.Get$AppliedValue,
    Set$Attribute:$$targs$$.Set$AppliedValue,Container$Attribute:$$targs$$.Container$AppliedValue},$$appliedValue);//TODO checar si no es if Container$Attribute
  set_type_args($$appliedValue,$$targs$$,AppliedValue$jsint);
  $$appliedValue.obj=obj;
  $$appliedValue.tipo=attr;
  return $$appliedValue;
}
