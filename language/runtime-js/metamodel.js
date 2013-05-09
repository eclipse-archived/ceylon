function type(x) {
    if (x === null) {
        return getNothingType();
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
        if (mm && mm['$mt']) {
            var metatype = mm['$mt'];
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
    return null;
}
type.$$metamodel$$={$nm:'type',$mt:'mthd',$ps:[{t:Anything}],$an:function(){return[shared()];}};
exports.type=type;
