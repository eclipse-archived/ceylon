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
  if (x === null) {
    return getNothingType$meta$model();
  }
  return AppliedClass($$targs$$.Type.t, {Type:$$targs$$.Type, Arguments:{t:Nothing}});
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
        return AppliedClass(t,mdl['$tp']);
      } else if (mdl['$mt'] === 'ifc') {
        return AppliedInterface(t,mdl['$tp']);
      } else if (mdl['$mt'] === 'mthd') {
        return AppliedFunction(t);
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
    var _mod = modules$meta$model.find(mm.mod['$mod-name'],mm.mod['$mod-version']);
    var _pkg = _mod.findPackage(mm.d[0]);
    if (mdl['$mt'] === 'cls') {
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
