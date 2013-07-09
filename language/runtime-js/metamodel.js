function type$metamodel(x) {
    if (x === null) {
        return getNothingType$metamodel();
    } else {
        //Search for metamodel
        var mm = x.$$metamodel$$;
        if (mm === undefined && x.constructor && x.constructor.T$name && x.constructor.T$all) {
            //It's probably an instance of a Ceylon type
            var _x = x.constructor.T$all[x.constructor.T$name];
            if (_x) {
                mm = _x.$$metamodel$$;
            }
        }
        if (mm && mm.d['$mt']) {
            var metatype = mm.d['$mt'];
            if (metatype === 'ifc') { //Interface
                //
            } else if (metatype === 'cls') { //Class
            } else if (metatype === 'mthd') { //Method
            } else {
            }
        } else {
            throw Exception(String$("No metamodel available for "+x));
        }
    }
    return "UNIMPLEMENTED";
}
type$metamodel.$$metamodel$$={$ps:[{t:Anything}],$an:function(){return[shared()];},mod:$$METAMODEL$$,d:$$METAMODEL$$['ceylon.language.metamodel']['type']};
exports.type$metamodel=type$metamodel;

function typeLiteral$metamodel($$targs$$) {
    if ($$targs$$ === undefined || $$targs$$.Type === undefined || $$targs$$.Type.t === undefined) {
        throw Exception("JS Interop not supported");
    } else if ($$targs$$.Type.t === 'u' || $$targs$$.Type.t === 'i') {
        //union/intersection type
        console.log("/union type/");
    } else if ($$targs$$.Type.t.$$metamodel$$ === undefined) {
        throw Exception("JS Interop not supported");
    } else {
        var mdl = $$targs$$.Type.t.$$metamodel$$;
        if (mdl.d['$mt'] === 'cls') {
            return AppliedClass$metamodel($$targs$$.Type.t,$$targs$$.Type.t['$$metamodel$$']['$tp']);
        } else if (mdl.d['$mt'] === 'ifc') {
            return AppliedInterface$metamodel($$targs$$.Type.t,$$targs$$.Type.t['$$metamodel$$']['$tp']);
        }
        console.log("typeLiteral<" + $$targs$$.Type.t.$$.T$name + ">");
    }
    throw Exception("typeLiteral UNIMPLEMENTED");
}
typeLiteral$metamodel.$$metamodel$$={$ps:[],$an:function(){return[shared()];},mod:$$METAMODEL$$,d:$$METAMODEL$$['ceylon.language.metamodel']['typeLiteral']};
exports.typeLiteral$metamodel=typeLiteral$metamodel;


