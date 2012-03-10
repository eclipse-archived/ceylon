function Callable(wat) {
    return wat;
}
initType(Callable, 'ceylon.language.Callable');
exports.Callable=Callable;

//This is used for plain method references
function JsCallable(o,f) {
    var c = function() {
        if (o === null) return null;
        var al=[];
        if (arguments !== undefined && arguments.length > 0) {
            for (var i=0; i<arguments.length;i++) {
                al.push(arguments[i]);
            }
        }
        return f.apply(o,al);
    };
    c.getString = function() { return String$('ceylon.language.Callable'); }
    initType(c, 'ceylon.language.JsCallable', Callable);
    return c;
}

//This is used for spread method references
function JsCallableList(value) {
    var c = function() {
        var al=[];
        if (arguments !== undefined && arguments.length > 0) {
            for (var i=0; i<arguments.length;i++) {
                al.push(arguments[i]);
            }
        }
        var rval = [];
        for (var i = 0; i < value.length; i++) {
            var c = value[i];
            rval.push(c.f.apply(c.o, al));
        }
        return ArraySequence(rval);
    };
    c.getString = function() { return String$('ceylon.language.JsCallable'); }
    initType(c, 'ceylon.language.JsCallableList', Callable);
    return c;
}

exports.JsCallableList=JsCallableList;
exports.Callable=JsCallable;
