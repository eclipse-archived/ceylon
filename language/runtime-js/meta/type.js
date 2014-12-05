function type$meta(x,$$targs$$) {
  if (x===null){
    return AppliedClass$jsint(getNull(),{Type$AppliedClass:{t:Null},Arguments$AppliedClass:{t:Empty}});
  }
  if ($$targs$$.Type$type.t===Nothing) {
    return getNothingType$meta$model();
  }
  if (x===true||x===false) {
    var cls=x?$_true:$_false;
    return AppliedClass$jsint(cls,{Type$AppliedClass:{t:cls},Arguments$AppliedClass:{t:Empty}});
  }
  var mm=getrtmm$$(x);
  var _t=$$targs$$.Type$type.t;
  if (mm===undefined) {
    if (x.getT$name && x.getT$all) {
      var mmm=x.getT$all()[x.getT$name()];
      if (mmm){mm=mmm.$crtmm$;_t=mmm;}
      if (typeof(mm)==='function') {
        mm=mm(); mmm.$crtmm$=mm;
      }
    }
  }
  if (mm===undefined&&x.rt$)mm=$_Array.$crtmm$;
  if (mm===undefined)throw new Error("Cannot retrieve metamodel for " + x);
  if (mm.$t) { //it's a value
    if (typeof(x)==='function') { //It's a callable
      if (mm.$cont) {
        return AppliedMethod$jsint(x,undefined,{Type$AppliedMethod:mm.$t,Arguments$AppliedMethod:{t:Nothing},Container$AppliedMethod:{t:mm.$cont}});
      }
      return AppliedFunction$jsint(x,{Type$Function:mm.$t,Arguments$Function:{t:Nothing}});
    }
    var rv=mm.$cont?AppliedMemberClass$jsint(mm.$t.t, {Type$AppliedMemberClass:mm.$t,Arguments$AppliedMemberClass:{t:Nothing},Container$AppliedMemberClass:{t:mm.$cont}})
           : AppliedClass$jsint(mm.$t.t, {Type$AppliedClass:mm.$t,Arguments$AppliedClass:{t:Nothing}});
    rv.src$=x;
    return rv;
  }
  var c;
  if ($$targs$$.Type$type.t==='T') {
    var rt=retpl$($$targs$$.Type$type);
    c=AppliedClass$jsint(Tuple,{Type$AppliedClass:$$targs$$.Type$type, Arguments$AppliedClass:{t:'T',l:[$$targs$$.Type$type.l[0],rt.Rest$Tuple]}});
  } else {
    var _ta={T:{t:x.getT$all()[x.getT$name()]}, A:{t:Sequential,a:{Element$Iterable:{t:Anything}}}};
    if (x.$$targs$$)_ta.T.a=x.$$targs$$;
    if (x.outer$) {
      _ta.C={t:x.outer$.getT$all()[x.outer$.getT$name()]};
      if (x.outer$.$$targs$$)_ta.C.a=x.outer$.$$targs$$;
    }
    if (mm.$cont) {
      c=AppliedMemberClass$jsint(_t, {Type$AppliedMemberClass:_ta.T,Arguments$AppliedMemberClass:_ta.A,Container$AppliedMemberClass:_ta.C});
    } else {
      c=AppliedClass$jsint(_t, {Type$AppliedClass:_ta.T,Arguments$AppliedClass:_ta.A});
    }
  }
  if ($$targs$$.Type$type.a)c.$targs=$$targs$$.Type$type.a;
  return c;
}
