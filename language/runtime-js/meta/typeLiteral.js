function typeLiteral$meta($$targs$$,targ$2) {
  if ($$targs$$ === undefined || $$targs$$.Type$typeLiteral === undefined) {
    throw new Error("Missing type argument 'Type' " + /*require('util').inspect(*/$$targs$$);
  } else if ($$targs$$.Type$typeLiteral.$crtmm$ == undefined) {
    //closed type
    var t = $$targs$$.Type$typeLiteral.t
    if (t === undefined) {
      t = $$targs$$.Type$typeLiteral.setter;
      if (t && t.$crtmm$) {
        var mm = getrtmm$$(t);
        var _m=typeof(mm.mod)==='function'?mm.mod():mm.mod;
        var _mod = modules$meta().find(_m['$mod-name'],_m['$mod-version']);
        return OpenSetter(OpenValue$jsint(_mod.findPackage(mm.d[0]), t));
      }
      throw new Error("'Type' argument should be an open or closed type");
    } else if (t==='u' || t==='i') {
      return (t==='u'?applyUnionType:applyIntersectionType)($$targs$$.Type$typeLiteral,targ$2);
    } else if (t==='T') {
      //TODO arguments
      var _tt=retpl$($$targs$$.Type$typeLiteral);
      if (_tt.t===Empty) {
        return AppliedClass$jsint(Tuple,{Type$AppliedClass:$$targs$$.Type$typeLiteral,Arguments$AppliedClass:{t:Empty}});
      }
      return AppliedClass$jsint(Tuple,{Type$AppliedClass:$$targs$$.Type$typeLiteral,Arguments$AppliedClass:{t:'T',l:[_tt.a.First$Tuple,_tt.a.Rest$Tuple]}});
    } else if (t.$crtmm$ === undefined) {
      throw new Error("JS Interop not supported / incomplete metamodel for " + /*require('util').inspect(*/t);
    } else {
      var mm = getrtmm$$(t);
      var mdl = get_model(mm);
      if (mdl.mt==='c' || mdl.mt==='o') {
        //TODO tupleize Arguments
        var acargs={t:Sequential,a:{Element$Iterable:{t:Anything}}};
        var r=mm.$cont?AppliedMemberClass$jsint(t,{Container$AppliedMemberClass:{t:mm.$cont},Type$AppliedMemberClass:$$targs$$.Type$typeLiteral,Arguments$AppliedMemberClass:acargs})
          : AppliedClass$jsint(t,{Type$AppliedClass:$$targs$$.Type$typeLiteral,Arguments$AppliedClass:acargs});
        if ($$targs$$.Type$typeLiteral.a)r.$targs=$$targs$$.Type$typeLiteral.a;
        return r;
      } else if (mdl['mt'] === 'i') {
        var r=AppliedInterface$jsint(t,{Type$Interface:$$targs$$.Type$typeLiteral});
        if ($$targs$$.Type$typeLiteral.a)r.$targs=$$targs$$.Type$typeLiteral.a;
        return r;
      } else if (mdl['mt'] === 'm') {
        return AppliedFunction$jsint(t,{Type$AppliedFunction:$$targs$$.Type$typeLiteral,
               Arguments$AppliedFunction:{t:Sequential,a:{Element$Iterable:{t:Anything}}}});
      } else if (mdl['mt'] === 'a' || mdl['mt'] === 'g' || mdl.mt==='s') {
        return AppliedValue$jsint(undefined,t,{Container$Value:{t:mm.$cont},Get$Value:mm.$t,Set$Value:mdl['var']?mm.$t:{t:Nothing}});
      } else {
        console.log("WTF is a metatype " + mdl['mt'] + " on a closed type???????");
      }
      console.log("typeLiteral<" + t.getT$name() + "> (closed type)");
    }
  } else {
    //open type
    var t = $$targs$$.Type$typeLiteral;
    var mm = getrtmm$$(t);
    var mdl = get_model(mm);
    var _m = typeof(mm.mod)==='function'?mm.mod():mm.mod;
    //We need the module
    var _mod = modules$meta().find(_m['$mod-name'],_m['$mod-version']);
    var _pkg = _mod.findPackage(mm.d[0]);
    if (mdl.mt==='c' || mdl.mt==='o') {
      return openClass$jsint(_pkg, t);
    } else if (mdl['mt'] === 'i') {
      return OpenInterface$jsint(_pkg, t);
    } else if (mdl['mt'] === 'm') {
      return OpenFunction$jsint(_pkg, t);
    } else if (mdl['mt'] === 'a' || mdl['mt'] === 'g') {
      return OpenValue$jsint(_pkg, t);
    } else if (mdl.mt==='s') {
      return OpenSetter(OpenValue$jsint(_pkg, t));
    } else {
      console.log("WTF is a metatype " + mdl['mt'] + " on an open type???????");
    }
    console.log("typeLiteral<" + t.getT$name() + "> (open type)");
  }
  throw new Error("typeLiteral UNIMPLEMENTED for " + /*require('util').inspect(*/$$targs$$);
}
