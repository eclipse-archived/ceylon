function AppliedValue$jsint(obj,attr,$a$,$$appliedValue){
  if (attr===undefined)throw Exception("Value reference not found. Metamodel doesn't work with modules compiled in lexical scope style.");
  if (!$a$.Get$AppliedValue)$a$.Get$AppliedValue=$a$.Get$Value;
  if (!$a$.Set$AppliedValue)$a$.Set$AppliedValue=$a$.Set$Value;
  if (!$a$.Container$AppliedValue && $a$.Container$Value)$a$.Container$AppliedValue=$a$.Container$Value;
  var mm = getrtmm$$(attr);
  $i$AppliedValue$jsint();
  if ($$appliedValue===undefined){
    if (obj||mm.$cont===undefined)$$appliedValue=new AppliedValue$jsint.$$;
    else {
      $$appliedValue=function AppliedVal1(x){return AppliedValue$jsint(x,attr,$a$);};
      $$appliedValue.$$=AppliedValue$jsint.$$;
      var dummy=new AppliedValue$jsint.$$;
      $$appliedValue.getT$all=function(){return dummy.getT$all();};
      $$appliedValue.getT$name=function(){return dummy.getT$name();};
      atr$($$appliedValue,'string',function(){
        var qn;
        if ($a$ && $a$.Container$AppliedValue) {
          qn = typeLiteral$meta({Type$typeLiteral:$a$.Container$AppliedValue}).string + "." + mm.d[mm.d.length-1];
        } else if (mm.$cont) {
          qn = typeLiteral$meta({Type$typeLiteral:{t:mm.$cont}}).string + "." + mm.d[mm.d.length-1];
        } else {
          qn=qname$(mm);
        }
        return qn;
      },undefined,dummy.$prop$getString.$m$);
    }
  }
  Value$meta$model({Get$Value:$a$.Get$AppliedValue,
    Set$Value:$a$.Set$AppliedValue},$$appliedValue);
  if($a$.Container$AppliedValue)Attribute$meta$model({Get$Attribute:$a$.Get$AppliedValue,
    Set$Attribute:$a$.Set$AppliedValue,Container$Attribute:$a$.Container$AppliedValue},$$appliedValue);//TODO checar si no es if Container$Attribute
  set_type_args($$appliedValue,$a$,AppliedValue$jsint);
  $$appliedValue.obj=obj;
  $$appliedValue.tipo=attr;
  return $$appliedValue;
}
