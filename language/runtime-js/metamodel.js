//From a runtime metamodel, get the model definition by following the path into the module's model.
function get_model(mm) {
  var map=mm.mod;
  var path=mm.d;
  for (var i=0; i < path.length; i++) {
    var _p=path[i];
    if (i===0 && _p==='$')_p='ceylon.language';
    map = map[_p];
  }
  return map;
}

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
  if (mm===undefined&&x.reifyCeylonType)mm=Array$.$crtmm$;
  if (mm===undefined)throw Error("Cannot retrieve metamodel for " + x);
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
    var rt=$retuple($$targs$$.Type$type);
    c=AppliedClass(Tuple,{Type$Class:$$targs$$.Type$type, Arguments$Class:{t:'T',l:[$$targs$$.Type$type.l[0],rt.Rest$Tuple]}});
  } else {
    var _ta={T:{t:x.getT$all()[x.getT$name()]}, A:{t:Sequential,a:{Element$Iterable:{t:Anything}}}};
    if (x.$$targs$$)_ta.T.a=x.$$targs$$;
    if (x.$$outer) {
      _ta.C={t:x.$$outer.getT$all()[x.$$outer.getT$name()]};
      if (x.$$outer.$$targs$$)_ta.C.a=x.$$outer.$$targs$$;
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
type$meta.$crtmm$=function(){return{
  $ps:[{$nm:'instance',$t:'Type'}],$an:function(){return[shared(),native()];},
  $t:{t:ClassModel$meta$model,a:{Type$Class:'Type', Arguments$Class:{t:Nothing}}}, $tp:{Type$type:{satisfies:{t:Anything}}},
  mod:$CCMM$,d:['ceylon.language.meta','type']};}
exports.type$meta=type$meta;

function typeLiteral$meta($$targs$$) {
  if ($$targs$$ === undefined || $$targs$$.Type$typeLiteral === undefined) {
    throw Exception("Missing type argument 'Type' " + /*require('util').inspect(*/$$targs$$);
  } else if ($$targs$$.Type$typeLiteral.$crtmm$ == undefined) {
    //closed type
    var t = $$targs$$.Type$typeLiteral.t
    if (t === undefined) {
      throw Exception("'Type' argument should be an open or closed type");
    } else if (t === 'u' || t === 'i') {
      return t === 'u' ? applyUnionType($$targs$$.Type$typeLiteral) : applyIntersectionType($$targs$$.Type$typeLiteral);
    } else if (t === 'T') {
      //TODO arguments
      var _tt=$retuple($$targs$$.Type$typeLiteral);
      return AppliedClass(Tuple,{Type$Class:$$targs$$.Type$typeLiteral,Arguments$Class:{t:'T',l:[_tt.a.First$Tuple,_tt.a.Rest$Tuple]}});
    } else if (t.$crtmm$ === undefined) {
      throw Exception("JS Interop not supported / incomplete metamodel for " + /*require('util').inspect(*/t);
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
    } else if (mdl['$mt'] === 'a' || mdl['$mt'] === 'g'||mdl.$mt==='s') {
      return OpenValue(_pkg, t);
    } else {
      console.log("WTF is a metatype " + mdl['$mt'] + " on an open type???????");
    }
    console.log("typeLiteral<" + t.getT$name() + "> (open type)");
  }
  throw Exception("typeLiteral UNIMPLEMENTED for " + /*require('util').inspect(*/$$targs$$);
}
typeLiteral$meta.$crtmm$={$ps:[],$an:function(){return[shared()];},mod:$CCMM$,d:['ceylon.language.meta','typeLiteral']};
exports.typeLiteral$meta=typeLiteral$meta;

function pushTypes(list, types) {
  for (var i=0; i<types.length; i++) {
    var t = types[i];
    if (t.t === 'u') {
      list.push(applyUnionType(t, t.l));
    } else if (t.t === 'i') {
      list.push(applyIntersectionType(t, t.l));
    } else {
      list.push(typeLiteral$meta({Type$typeLiteral:t}));
    }
  }
  return list;
}

function applyUnionType(ut) { //return AppliedUnionType
  var cases = [];
  pushTypes(cases, ut.l);
  return AppliedUnionType(ut, cases.reifyCeylonType({Absent$Iterable:{t:Null},Element$Iterable:{t:Type$meta$model}}), {Union$UnionType:{t:Anything}});
}
function applyIntersectionType(it) { //return AppliedIntersectionType
  var sats = [];
  pushTypes(sats, it.l);
  return AppliedIntersectionType(it, sats.reifyCeylonType({Absent$Iterable:{t:Null},Element$Iterable:{t:Type$meta$model}}), {Intersection$IntersectionType:{t:Anything}});
}
