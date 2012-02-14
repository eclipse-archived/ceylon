function Callable(wat) {
    return wat;
}
initType(Callable, 'ceylon.language.Callable');
exports.Callable=Callable;

//This is used for plain method references
function JsCallable(o,f) {
    var that = new JsCallable.$$;
    that.o=o;
    that.f = f;
    return that;
}
initType(JsCallable, 'ceylon.language.JsCallable', Callable);
var $JsCallable = JsCallable.$$;
$JsCallable.prototype.getString = function() { return String$('ceylon.language.JsCallable'); }
$JsCallable.prototype.call = function() {
    var al=[];
    if (arguments !== undefined && arguments.length > 0) {
        for (var i=0; i<arguments.length;i++) {
            al.push(arguments[i]);
        }
    }
    return this.f.apply(this.o,al);
}

//This is used for spread method references
function JsCallableList(value) {
    var that = new JsCallableList.$$;
    that.value=value;
    return that;
}
initType(JsCallableList, 'ceylon.language.JsCallableList', Callable);
inheritProto(JsCallableList, Object$, '$Object$');
var $JsCallableList = JsCallableList.$$;
$JsCallableList.prototype.call = function() {
    var al=[];
    if (arguments !== undefined && arguments.length > 0) {
        for (var i=0; i<arguments.length;i++) {
            al.push(arguments[i]);
        }
    }
    var rval = [];
    for (var i = 0; i < this.value.length; i++) {
        var c = this.value[i];
        rval.push(c.f.apply(c.o, al));
    }
    return ArraySequence(rval);
}

exports.JsCallableList=JsCallableList;
exports.JsCallable=JsCallable;