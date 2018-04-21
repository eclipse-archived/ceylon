function type$meta(x,$a$) {
  if (x===null){
    return AppliedClass$jsint($i$$_null(),{Type$AppliedClass:{t:$i$$_null()},Arguments$AppliedClass:{t:Empty}});
  }
  if ($a$.Type$type.t===Nothing) {
    return nothingType$meta$model();
  }
  if (x===true||x===false) {
    var cls=x?$_true:$_false;
    return AppliedClass$jsint(cls,{Type$AppliedClass:{t:cls},Arguments$AppliedClass:{t:Empty}});
  }
  var mm=getrtmm$$(x);
  var _t=$a$.Type$type.t;
  if (mm===undefined) {
    if (x.getT$name && x.getT$all) {
      var mmm=x.getT$all()[x.getT$name()];
      if (mmm){mm=mmm.$m$;_t=mmm;}
      if (typeof(mm)==='function') {
        mm=mm(); mmm.$m$=mm;
      }
    }
  }
  if (mm===undefined&&x.rt$)mm=$_Array.$m$;
  if (mm===undefined)throw new Error("Cannot retrieve metamodel for " + x);
  var _cntr=mm.$cont && !is$(x,{t:'u',l:[{t:AppliedCallableConstructor$jsint},{t:AppliedMemberClassCallableConstructor$jsint}]}) ? get_model(getrtmm$$(mm.$cont)) : undefined;
  var _classOrInterfaceMember = _cntr && (_cntr.mt==='c' || _cntr.mt==='i' || _cntr.mt==='o');
  if (mm.$t) { //it's a value
    if (typeof(x)==='function') { //It's a callable
      if (_classOrInterfaceMember) {
        return AppliedMethod$jsint(x,undefined,{Type$AppliedMethod:mm.$t,Arguments$AppliedMethod:{t:Nothing},Container$AppliedMethod:{t:mm.$cont}},x.$a$);
      }
      return AppliedFunction$jsint(x,{Type$Function:mm.$t,Arguments$Function:{t:Nothing}},undefined,x.$a$);
    }
    var rv=_classOrInterfaceMember?AppliedMemberClass$jsint(mm.$t.t, {Type$AppliedMemberClass:mm.$t,Arguments$AppliedMemberClass:{t:Nothing},Container$AppliedMemberClass:{t:mm.$cont}})
           : AppliedClass$jsint(mm.$t.t, {Type$AppliedClass:mm.$t,Arguments$AppliedClass:{t:Nothing}});
    rv.src$=x;
    return rv;
  }
  var c;
  if ($a$.Type$type.t==='T') {
    var rt=x.$a$.l ? x.$a$ : $a$.Type$type;
    c=AppliedClass$jsint(Tuple,{Type$AppliedClass:rt, Arguments$AppliedClass:{t:'T',l:[x.$a$.First$Tuple,x.$a$.Rest$Tuple]}});
  } else {
    var _ta={T:{t:x.getT$all()[x.getT$name()]}, A:{t:Sequential,a:{Element$Iterable:{t:Anything}}}};
    var mytargs;
    if (x.$a$) {
      //Tuple with tuple targs
      if (_ta.T.t===Tuple && x.$a$.t==='T')return type$meta(x,{Type$type:x.$a$});
      _ta.T.a=x.$a$;
    }
    if (x.outer$) {
      _ta.C={t:x.outer$.getT$all()[x.outer$.getT$name()]};
      if (x.outer$.$a$)_ta.C.a=x.outer$.$a$;
      mytargs={};
      for (ta in x.$a$)mytargs[ta]=x.$a$[ta];
      var ou=x.outer$;
      while (ou) {
        if (ou.$a$) {
          for (ta in ou.$a$)mytargs[ta]=ou.$a$[ta];
        }
        ou=ou.outer$;
      }
    }
    if (_classOrInterfaceMember) {
      c=AppliedMemberClass$jsint(_t, {Type$AppliedMemberClass:_ta.T,Arguments$AppliedMemberClass:_ta.A,Container$AppliedMemberClass:_ta.C},undefined,mytargs);
    } else {
      c=AppliedClass$jsint(_t, {Type$AppliedClass:_ta.T,Arguments$AppliedClass:_ta.A},undefined,x.$a$);
    }
  }
  if ($a$.Type$type.a)c.$targs=$a$.Type$type.a;
  c.src$=x;
  return c;
}
