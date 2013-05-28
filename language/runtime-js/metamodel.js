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
    if ($$targs$$ === undefined || $$targs$$.Type === undefined) {
        throw Exception("JS Interop not supported");
    }
console.log("typeLiteral<" + className($$targs$$.Type) + ">");
    throw Exception("typeLiteral UNIMPLEMENTED");
}
typeLiteral$metamodel.$$metamodel$$={$ps:[],$an:function(){return[shared()];},mod:$$METAMODEL$$,d:$$METAMODEL$$['ceylon.language.metamodel']['typeLiteral']};
exports.typeLiteral$metamodel=typeLiteral$metamodel;
