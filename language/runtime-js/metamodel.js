function type$model(x) {
    if (x === null) {
        return getNothingType$model();
    } else {
        //Search for metamodel
        var mm = x.$$metamodel$$;
        if (mm === undefined && x.constructor && x.constructor.T$name && x.constructor.T$all) {
            //It's probably an instance of a Ceylon type
            var _x = x.constructor.T$all[x.constructor.T$name];
            if (_x) {
                mm = _x.$$metamodel$$;
                x=_x;
            }
        }
        if (mm && mm.d['$mt']) {
            var metatype = mm.d['$mt'];
            if (metatype === 'ifc') { //Interface
                //
            } else if (metatype === 'cls') { //Class
                return typeLiteral$model({Type:{t:x}});
            } else if (metatype === 'mthd') { //Method
                return typeLiteral$model({Type:{t:$JsCallable(x)}});
            } else {
                console.log("type(" + metatype + ")WTF?");
            }
        } else {
            throw Exception(String$("No metamodel available for "+x));
        }
    }
    return "UNIMPLEMENTED";
}
type$model.$$metamodel$$={$ps:[{t:Anything}],$an:function(){return[shared()];},mod:$$METAMODEL$$,d:$$METAMODEL$$['ceylon.language.model']['type']};
exports.type$model=type$model;

function typeLiteral$model($$targs$$) {
    if ($$targs$$ === undefined || $$targs$$.Type === undefined || $$targs$$.Type.t === undefined) {
        throw Exception("JS Interop not supported " + require('util').inspect($$targs$$));
    } else if ($$targs$$.Type.t === 'u' || $$targs$$.Type.t === 'i') {
        return $$targs$$.Type.t === 'u' ? applyUnionType($$targs$$.Type) :
            applyIntersectionType($$targs$$.Type);
    } else if ($$targs$$.Type.t.$$metamodel$$ === undefined) {
        throw Exception("JS Interop not supported / incomplete metamodel for " + require('util').inspect($$targs$$.Type.t));
    } else {
        var mdl = $$targs$$.Type.t.$$metamodel$$;
        if (mdl.d['$mt'] === 'cls') {
            return AppliedClass$model($$targs$$.Type.t,$$targs$$.Type.t['$$metamodel$$']['$tp']);
        } else if (mdl.d['$mt'] === 'ifc') {
            return AppliedInterface$model($$targs$$.Type.t,$$targs$$.Type.t['$$metamodel$$']['$tp']);
        } else if (mdl.d['$mt'] === 'mthd') {
            return AppliedFunction$model($$targs$$.Type.t,{Type:mdl.$t,Arguments:mdl.$ps});
        } else if (mdl.d['$mt'] === 'attr' || mdl.d['$mt'] === 'gttr') {
            return AppliedValue$model($$targs$$.Type.t,{Container:{t:mdl.$cont},Type:mdl.$t});
        } else {
console.log("WTF is a metatype " + mdl.d['$mt'] + "???????");
        }
        console.log("typeLiteral<" + $$targs$$.Type.t.getT$name() + ">");
    }
    throw Exception("typeLiteral UNIMPLEMENTED for " + require('util').inspect($$targs$$));
}
typeLiteral$model.$$metamodel$$={$ps:[],$an:function(){return[shared()];},mod:$$METAMODEL$$,d:$$METAMODEL$$['ceylon.language.model']['typeLiteral']};
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
  return AppliedUnionType$model(ut, cases.reifyCeylonType({Absent:{t:Null},Element:{t:Type$model}}));
}
function applyIntersectionType(it) { //return AppliedIntersectionType
  var sats = [];
  pushTypes(sats, it.l);
  return AppliedIntersectionType$model(it, sats.reifyCeylonType({Absent:{t:Null},Element:{t:Type$model}}));
}
function applyType(t) { //return AppliedType
  return typeLiteral$model({Type:t});
}
