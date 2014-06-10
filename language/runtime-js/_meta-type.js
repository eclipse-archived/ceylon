function type$meta(x,$$targs$$) {
  if (x === null || $$targs$$.Type$type.t===Nothing) {
    return getNothingType$meta$model();
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
  if (mm===undefined&&x.reifyCeylonType)mm=$_Array.$crtmm$;
  if (mm===undefined)throw new Error("Cannot retrieve metamodel for " + x);
  if (mm.$t) { //it's a value
    if (typeof(x)==='function') { //It's a callable
      if (mm.$cont) {
        return AppliedMethod(x,undefined,{Type$Method:mm.$t,Arguments$Method:{t:Nothing}});
      }
      return AppliedFunction(x,{Type$Function:mm.$t,Arguments$Function:{t:Nothing}});
    }
    return AppliedClass(mm.$t.t, {Type$Class:mm.$t,Arguments$Class:{t:Nothing}});
  }
  var c;
  if ($$targs$$.Type$type.t==='T') {
    var rt=retpl$($$targs$$.Type$type);
    c=AppliedClass(Tuple,{Type$Class:$$targs$$.Type$type, Arguments$Class:{t:'T',l:[$$targs$$.Type$type.l[0],rt.Rest$Tuple]}});
  } else {
    var _ta={T:{t:x.getT$all()[x.getT$name()]}, A:{t:Sequential,a:{Element$Iterable:{t:Anything}}}};
    if (x.$$targs$$)_ta.T.a=x.$$targs$$;
    if (x.outer$) {
      _ta.C={t:x.outer$.getT$all()[x.outer$.getT$name()]};
      if (x.outer$.$$targs$$)_ta.C.a=x.outer$.$$targs$$;
    }
    if (mm.$cont) {
      c=AppliedMemberClass(_t, {Type$MemberClass:_ta.T,Arguments$MemberClass:_ta.A,Container$MemberClass:_ta.C});
    } else {
      c=AppliedClass(_t, {Type$Class:_ta.T,Arguments$Class:_ta.A});
    }
  }
  if ($$targs$$.Type$type.a)c.$targs=$$targs$$.Type$type.a;
  return c;
}
