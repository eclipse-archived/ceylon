function typeLiteral$meta($a$,targ$2) {
  if ($a$ === undefined || $a$.Type$typeLiteral === undefined) {
    throw new Error("Missing type argument 'Type' " + /*require('util').inspect(*/$a$);
  } else if ($a$.Type$typeLiteral.$m$ == undefined) {
    //closed type
    var t = $a$.Type$typeLiteral.t
    if (t === undefined) {
      t = $a$.Type$typeLiteral.setter;
      if (t && t.$m$) {
        var mm = getrtmm$$(t);
        var _m=typeof(mm.mod)==='function'?mm.mod():mm.mod;
        var _mod = modules$meta().find(_m['$mod-name'],_m['$mod-version']);
        return OpenSetter(OpenValue$jsint(_mod.findPackage(mm.d[0]), t));
      }
      throw new Error("'Type' argument should be an open or closed type");
    } else if (t==='u' || t==='i') {
      return (t==='u'?applyUnionType:applyIntersectionType)($a$.Type$typeLiteral,targ$2);
    } else if (t==='T') {
      //TODO arguments
      var _tt=retpl$($a$.Type$typeLiteral);
      if (_tt.t===Empty) {
        return AppliedClass$jsint(Tuple,{Type$AppliedClass:$a$.Type$typeLiteral,Arguments$AppliedClass:{t:Empty}});
      }
      return AppliedClass$jsint(Tuple,{Type$AppliedClass:$a$.Type$typeLiteral,Arguments$AppliedClass:{t:'T',l:[_tt.a.First$Tuple,_tt.a.Rest$Tuple]}});
    } else if (t.$m$ === undefined) {
      throw new Error("JS Interop not supported / incomplete metamodel for " + /*require('util').inspect(*/t);
    } else {
      var mm = getrtmm$$(t);
      var mdl = get_model(mm);
      var mtype=mdl.mt;
      if (mtype==='c' || mtype==='o') {
        //TODO tupleize Arguments
        var acargs={t:Sequential,a:{Element$Iterable:{t:Anything}}};
        var r=mm.$cont?AppliedMemberClass$jsint(t,{Container$AppliedMemberClass:{t:mm.$cont},Type$AppliedMemberClass:$a$.Type$typeLiteral,Arguments$AppliedMemberClass:acargs})
          : AppliedClass$jsint(t,{Type$AppliedClass:$a$.Type$typeLiteral,Arguments$AppliedClass:acargs});
        if ($a$.Type$typeLiteral.a)r.$targs=$a$.Type$typeLiteral.a;
        return r;
      } else if (mtype==='i') {
        var r=AppliedInterface$jsint(t,{Type$Interface:$a$.Type$typeLiteral});
        if ($a$.Type$typeLiteral.a)r.$targs=$a$.Type$typeLiteral.a;
        return r;
      } else if (mtype==='m') {
        return AppliedFunction$jsint(t,{Type$AppliedFunction:$a$.Type$typeLiteral,
               Arguments$AppliedFunction:{t:Sequential,a:{Element$Iterable:{t:Anything}}}});
      } else if (mtype==='a' || type==='g' || mtype==='s') {
        return AppliedValue$jsint(undefined,t,{Container$Value:{t:mm.$cont},Get$Value:mm.$t,Set$Value:mdl['var']?mm.$t:{t:Nothing}});
      } else {
        console.log("WTF is a metatype " + mtype + " on a closed type???????");
      }
      console.log("typeLiteral<" + t.getT$name() + "> (closed type)");
    }
  } else {
    //open type
    var t = $a$.Type$typeLiteral;
    var mm = getrtmm$$(t);
    var mdl = get_model(mm);
    var mtype=mdl.mt;
    if (mtype==='als') {
      //resolve type alias
      t=t.t;
      mm=getrtmm$$(t);
      mdl=get_model(mm);
    }
    var _m = typeof(mm.mod)==='function'?mm.mod():mm.mod;
    //We need the module
    var _mod = modules$meta().find(_m['$mod-name'],_m['$mod-version']);
    var _pkg = _mod.findPackage(mm.d[0]);
    if (mdl.mt==='c' || mdl.mt==='o') {
      return openClass$jsint(_pkg, t);
    } else if (mtype==='i') {
      return OpenInterface$jsint(_pkg, t);
    } else if (mtype==='m') {
      return OpenFunction$jsint(_pkg, t);
    } else if (mtype==='a' || mtype==='g') {
      return OpenValue$jsint(_pkg, t);
    } else if (mtype==='s') {
      return OpenSetter(OpenValue$jsint(_pkg, t));
    } else {
      console.log("WTF is a metatype " + mtype + " on an open type???????");
    }
    console.log("typeLiteral<" + t.getT$name() + "> (open type)");
  }
  throw new Error("typeLiteral UNIMPLEMENTED for " + /*require('util').inspect(*/$a$);
}
