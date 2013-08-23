//From a runtime metamodel, get the model definition by following the path into the module's model.
function get_model(mm) {
  var map=mm.mod;
  var path=mm.d;
  for (var i=0; i < path.length; i++) {
    map = map[path[i]];
  }
  return map;
}

function type$model(x) {
    if (x === null) {
        return getNothingType$model();
    } else {
        //Search for metamodel
        var mm = typeof(x.$$metamodel$$)==='function'?x.$$metamodel$$():x.$$metamodel$$;
        if (mm === undefined && x.constructor && x.constructor.T$name && x.constructor.T$all) {
            //It's probably an instance of a Ceylon type
            var _x = x.constructor.T$all[x.constructor.T$name];
            if (_x) {
                mm = _x.$$metamodel$$;
                x=_x;
            }
        }
        if (typeof(mm) == 'function') mm = mm();
        if (mm) {
            var metatype = get_model(mm)['$mt'];
            if (metatype === 'ifc' || metatype === 'cls') { //Interface or Class
                return typeLiteral$model({Type:x});
            } else if (metatype === 'mthd') { //Method
                return typeLiteral$model({Type:$JsCallable(x)});
            } else {
                console.log("type(" + metatype + ")WTF?");
            }
        } else {
            throw Exception(String$("No metamodel available for "+x));
        }
    }
    return "UNIMPLEMENTED";
}
type$model.$$metamodel$$={$ps:[{t:Anything}],$an:function(){return[shared()];},mod:$$METAMODEL$$,d:['ceylon.language.model','type']};
exports.type$model=type$model;

function typeLiteral$model($$targs$$) {
  if ($$targs$$ === undefined || $$targs$$.Type === undefined) {
    throw Exception("Missing type argument 'Type' " + require('util').inspect($$targs$$));
  } else if ($$targs$$.Type.$$metamodel$$ == undefined) {
    //closed type
    var t = $$targs$$.Type.t
    if (t === undefined) {
      throw Exception("'Type' argument should be an open or closed type");
    } else if (t === 'u' || t === 'i') {
      return t === 'u' ? applyUnionType($$targs$$.Type) :
      applyIntersectionType($$targs$$.Type);
    } else if (t.$$metamodel$$ === undefined) {
      throw Exception("JS Interop not supported / incomplete metamodel for " + require('util').inspect(t));
    } else {
      var mm = t.$$metamodel$$;
      if (typeof(mm)==='function')mm=mm();
      var mdl = get_model(mm);
      if (mdl['$mt'] === 'cls') {
        return AppliedClass(t,mdl['$tp']);
      } else if (mdl['$mt'] === 'ifc') {
        return AppliedInterface(t,mdl['$tp']);
      } else if (mdl['$mt'] === 'mthd') {
        return AppliedFunction(t);
      } else if (mdl['$mt'] === 'attr' || mdl['$mt'] === 'gttr') {
        return AppliedValue(t,{Container:{t:mm.$cont},Type:mm.$t});
      } else {
        console.log("WTF is a metatype " + mdl['$mt'] + " on a closed type???????");
      }
      console.log("typeLiteral<" + t.getT$name() + "> (closed type)");
    }
  } else {
    //open type
    var t = $$targs$$.Type;
    var mm = t.$$metamodel$$;
    if (typeof(mm)==='function')mm=mm();
    var mdl = get_model(mm);
    //We need the module
    var _mod = modules$model.find(mm.mod['$mod-name'],mm.mod['$mod-version']);
    var _pkg = _mod.findPackage(mm.d[0]);
    if (mdl['$mt'] === 'cls') {
      return OpenClass(mdl['$nm'], _pkg, mm.$cont===undefined, t);
    } else if (mdl['$mt'] === 'ifc') {
      return OpenInterface(mdl['$nm'], _pkg, mm.$cont===undefined, t);
    } else if (mdl['$mt'] === 'mthd') {
      return OpenFunction(mdl['$nm'], _pkg, mm.$cont===undefined, t);
    } else if (mdl['$mt'] === 'attr' || mdl['$mt'] === 'gttr') {
      return OpenValue(mdl['$nm'], _pkg, mm.$cont===undefined, t);
    } else {
      console.log("WTF is a metatype " + mdl['$mt'] + " on an open type???????");
    }
    console.log("typeLiteral<" + t.getT$name() + "> (open type)");
  }
  throw Exception("typeLiteral UNIMPLEMENTED for " + require('util').inspect($$targs$$));
}
typeLiteral$model.$$metamodel$$={$ps:[],$an:function(){return[shared()];},mod:$$METAMODEL$$,d:['ceylon.language.model','typeLiteral']};
exports.typeLiteral$model=typeLiteral$model;

function pushTypes(list, types) {
  for (var i=0; i<types.length; i++) {
    var t = types[i];
    if (t.t === 'u') {
      list.push(applyUnionType(t, t.l));
    } else if (t.t === 'i') {
      list.push(applyIntersectionType(t, t.l));
    } else {
      list.push(typeLiteral$model({Type:t}));
    }
  }
  return list;
}

function applyUnionType(ut) { //return AppliedUnionType
  var cases = [];
  pushTypes(cases, ut.l);
  return AppliedUnionType(ut, cases.reifyCeylonType({Absent:{t:Null},Element:{t:Type$model}}));
}
function applyIntersectionType(it) { //return AppliedIntersectionType
  var sats = [];
  pushTypes(sats, it.l);
  return AppliedIntersectionType(it, sats.reifyCeylonType({Absent:{t:Null},Element:{t:Type$model}}));
}
function applyType(t) { //return AppliedType
  return typeLiteral$model({Type:t});
}
