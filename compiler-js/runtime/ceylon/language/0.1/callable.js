function Callable(wat) {
    return wat;
}
initType(Callable, 'ceylon.language.Callable');
exports.Callable=Callable;

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


exports.JsCallable=JsCallable;