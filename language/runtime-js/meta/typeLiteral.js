function typeLiteral$meta($$targs$$) {
  if ($$targs$$ === undefined || $$targs$$.Type$typeLiteral === undefined) {
    throw new Error("Missing type argument 'Type' " + /*require('util').inspect(*/$$targs$$);
  } else if ($$targs$$.Type$typeLiteral.$crtmm$ == undefined) {
    //closed type
    var t = $$targs$$.Type$typeLiteral.t
    if (t === undefined) {
      t = $$targs$$.Type$typeLiteral.setter;
      if (t && t.$crtmm$) {
        var mm = getrtmm$$(t);
        var _mod = getModules$meta().find(mm.mod['$mod-name'],mm.mod['$mod-version']);
        return OpenSetter(OpenValue(_mod.findPackage(mm.d[0]), t));
      }
      throw new Error("'Type' argument should be an open or closed type");
    } else if (t === 'u' || t === 'i') {
      return t === 'u' ? applyUnionType($$targs$$.Type$typeLiteral) : applyIntersectionType($$targs$$.Type$typeLiteral);
    } else if (t === 'T') {
      //TODO arguments
      var _tt=retpl$($$targs$$.Type$typeLiteral);
      return AppliedClass(Tuple,{Type$Class:$$targs$$.Type$typeLiteral,Arguments$Class:{t:'T',l:[_tt.a.First$Tuple,_tt.a.Rest$Tuple]}});
    } else if (t.$crtmm$ === undefined) {
      throw new Error("JS Interop not supported / incomplete metamodel for " + /*require('util').inspect(*/t);
    } else {
      var mm = getrtmm$$(t);
      var mdl = get_model(mm);
      if (mdl['$mt'] === 'c') {
        //TODO tupleize Arguments
        var r=AppliedClass(t,{Type$Class:$$targs$$.Type$typeLiteral,Arguments$Class:{t:Sequential,a:{Element$Iterable:{t:Anything}}}});
        if ($$targs$$.Type$typeLiteral.a)r.$targs=$$targs$$.Type$typeLiteral.a;
        return r;
      } else if (mdl['$mt'] === 'i') {
        var r=AppliedInterface(t,{Type$Interface:$$targs$$.Type$typeLiteral});
        if ($$targs$$.Type$typeLiteral.a)r.$targs=$$targs$$.Type$typeLiteral.a;
        return r;
      } else if (mdl['$mt'] === 'm') {
        return AppliedFunction(t,{Type$Function:$$targs$$.Type$typeLiteral,Arguments$Function:{t:Sequential,a:{Element$Iterable:{t:Anything}}}});
      } else if (mdl['$mt'] === 'a' || mdl['$mt'] === 'g' || mdl['$mt'] === 'o'||mdl.$mt==='s') {
        return AppliedValue(undefined,t,{Container$Value:{t:mm.$cont},Get$Value:mm.$t,Set$Value:mdl['var']?mm.$t:{t:Nothing}});
      } else {
        console.log("WTF is a metatype " + mdl['$mt'] + " on a closed type???????");
      }
      console.log("typeLiteral<" + t.getT$name() + "> (closed type)");
    }
  } else {
    //open type
    var t = $$targs$$.Type$typeLiteral;
    var mm = getrtmm$$(t);
    var mdl = get_model(mm);
    //We need the module
    var _mod = getModules$meta().find(mm.mod['$mod-name'],mm.mod['$mod-version']);
    var _pkg = _mod.findPackage(mm.d[0]);
    if (mdl.$mt==='c' || mdl.$mt==='o') {
      return OpenClass(_pkg, t);
    } else if (mdl['$mt'] === 'i') {
      return OpenInterface(_pkg, t);
    } else if (mdl['$mt'] === 'm') {
      return OpenFunction(_pkg, t);
    } else if (mdl['$mt'] === 'a' || mdl['$mt'] === 'g') {
      return OpenValue(_pkg, t);
    } else if (mdl.$mt==='s') {
      return OpenSetter(OpenValue(_pkg, t));
    } else {
      console.log("WTF is a metatype " + mdl['$mt'] + " on an open type???????");
    }
    console.log("typeLiteral<" + t.getT$name() + "> (open type)");
  }
  throw new Error("typeLiteral UNIMPLEMENTED for " + /*require('util').inspect(*/$$targs$$);
}
