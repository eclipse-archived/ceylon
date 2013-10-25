//From a runtime metamodel, get the model definition by following the path into the module's model.
function get_model(mm) {
  var map=mm.mod;
  var path=mm.d;
  for (var i=0; i < path.length; i++) {
    map = map[path[i]];
  }
  return map;
}

function type$meta(x,$$targs$$) {
  if (x === null || $$targs$$.Type.t===Nothing) {
    return getNothingType$meta$model();
  }
  if (x.$$metamodel$$) {
    //Could be an object (or a callable)
    if (typeof(x.$$metamodel$$)==='function') {
      x.$$metamodel$$=x.$$metamodel$$();
    }
    if (x.$$metamodel$$.$t) //it's an object
      return AppliedValue(undefined,x.$$metamodel$$.$t.t, {Type:x.$$metamodel$$.$t});
  }
  var c;
  if ($$targs$$.Type.t==='T') {
    var rt=$retuple($$targs$$.Type);
    c=AppliedClass(Tuple,{Type:$$targs$$.Type, Arguments:{t:'T',l:[$$targs$$.Type.l[0],rt.Rest]}});
  } else {
    var mm=$$targs$$.Type.t.$$metamodel$$;
    if (typeof(mm)==='function'){mm=mm();$$targs$$.Type.t.$$metamodel$$=mm;}
    c=(mm.$cont?AppliedMemberClass:AppliedClass)($$targs$$.Type.t, {Type:$$targs$$.Type, Arguments:{t:Sequential,a:{Element:{t:Anything}}}});
  }
  if ($$targs$$.Type.a)c.$targs=$$targs$$.Type.a;
  return c;
}
type$meta.$$metamodel$$={$ps:[{t:Anything}],$an:function(){return[shared()];},mod:$$METAMODEL$$,d:['ceylon.language.meta','type']};
exports.type$meta=type$meta;

function typeLiteral$meta($$targs$$) {
  if ($$targs$$ === undefined || $$targs$$.Type === undefined) {
    throw Exception("Missing type argument 'Type' " + /*require('util').inspect(*/$$targs$$);
  } else if ($$targs$$.Type.$$metamodel$$ == undefined) {
    //closed type
    var t = $$targs$$.Type.t
    if (t === undefined) {
      throw Exception("'Type' argument should be an open or closed type");
    } else if (t === 'u' || t === 'i') {
      return t === 'u' ? applyUnionType($$targs$$.Type) : applyIntersectionType($$targs$$.Type);
    } else if (t === 'T') {
      //TODO arguments
      var _tt=$retuple($$targs$$.Type);
      return AppliedClass(Tuple,{Type:$$targs$$.Type,Arguments:{t:'T',l:[_tt.a.First,_tt.a.Rest]}});
    } else if (t.$$metamodel$$ === undefined) {
      throw Exception("JS Interop not supported / incomplete metamodel for " + /*require('util').inspect(*/t);
    } else {
      var mm = t.$$metamodel$$;
      if (typeof(mm)==='function') {
        mm=mm();
        t.$$metamodel$$=mm;
      }
      var mdl = get_model(mm);
      if (mdl['$mt'] === 'cls') {
        var r=AppliedClass(t,{Type:$$targs$$.Type,Arguments:{t:Sequential,a:{Element:{t:Anything}}}});
        if ($$targs$$.Type.a)r.$targs=$$targs$$.Type.a;
        return r;
      } else if (mdl['$mt'] === 'ifc') {
        var r=AppliedInterface(t,$$targs$$);
        if ($$targs$$.Type.a)r.$targs=$$targs$$.Type.a;
        return r;
      } else if (mdl['$mt'] === 'mthd') {
        return AppliedFunction(t,{Type:$$targs$$.Type,Arguments:{t:Sequential,a:{Element:{t:Anything}}}});
      } else if (mdl['$mt'] === 'attr' || mdl['$mt'] === 'gttr' || mdl['$mt'] === 'obj') {
        return AppliedValue(undefined,t,{Container:{t:mm.$cont},Type:mm.$t});
      } else {
        console.log("WTF is a metatype " + mdl['$mt'] + " on a closed type???????");
      }
      console.log("typeLiteral<" + t.getT$name() + "> (closed type)");
    }
  } else {
    //open type
    var t = $$targs$$.Type;
    var mm = t.$$metamodel$$;
    if (typeof(mm)==='function') {
      mm=mm();
      t.$$metamodel$$=mm;
    }
    var mdl = get_model(mm);
    //We need the module
    var _mod = getModules$meta().find(mm.mod['$mod-name'],mm.mod['$mod-version']);
    var _pkg = _mod.findPackage(mm.d[0]);
    if (mdl.$mt==='cls' || mdl.$mt==='obj') {
      return OpenClass(_pkg, t);
    } else if (mdl['$mt'] === 'ifc') {
      return OpenInterface(_pkg, t);
    } else if (mdl['$mt'] === 'mthd') {
      return OpenFunction(_pkg, t);
    } else if (mdl['$mt'] === 'attr' || mdl['$mt'] === 'gttr') {
      return OpenValue(_pkg, t);
    } else {
      console.log("WTF is a metatype " + mdl['$mt'] + " on an open type???????");
    }
    console.log("typeLiteral<" + t.getT$name() + "> (open type)");
  }
  throw Exception("typeLiteral UNIMPLEMENTED for " + /*require('util').inspect(*/$$targs$$);
}
typeLiteral$meta.$$metamodel$$={$ps:[],$an:function(){return[shared()];},mod:$$METAMODEL$$,d:['ceylon.language.meta','typeLiteral']};
exports.typeLiteral$meta=typeLiteral$meta;

function pushTypes(list, types) {
  for (var i=0; i<types.length; i++) {
    var t = types[i];
    if (t.t === 'u') {
      list.push(applyUnionType(t, t.l));
    } else if (t.t === 'i') {
      list.push(applyIntersectionType(t, t.l));
    } else {
      list.push(typeLiteral$meta({Type:t}));
    }
  }
  return list;
}

function applyUnionType(ut) { //return AppliedUnionType
  var cases = [];
  pushTypes(cases, ut.l);
  return AppliedUnionType(ut, cases.reifyCeylonType({Absent:{t:Null},Element:{t:Type$meta$model}}), {Union:{t:Anything}});
}
function applyIntersectionType(it) { //return AppliedIntersectionType
  var sats = [];
  pushTypes(sats, it.l);
  return AppliedIntersectionType(it, sats.reifyCeylonType({Absent:{t:Null},Element:{t:Type$meta$model}}), {Union:{t:Anything}});
}
