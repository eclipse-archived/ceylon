(function(define) { define(function(require, exports, module) {
var $$$cl234=require('ceylon/language/0.2/ceylon.language');
var $$$f235=require('default/functions');

//MethodDefinition test at invocations.ceylon (3:0-18:0)
function test(){
    $$$f235.helloWorld();
    (function (){return $$$f235.helloWorld()}());
    $$$f235.hello($$$cl234.String("world",5));
    (function (){var name$236=$$$cl234.String("world",5);return $$$f235.hello(name$236)}());
    $$$f235.helloAll($$$cl234.ArraySequence([$$$cl234.String("someone",7),$$$cl234.String("someone else",12)]));
    (function (){var names$237=$$$cl234.ArraySequence([$$$cl234.String("someone",7),$$$cl234.String("someone else",12)]);return $$$f235.helloAll(names$237)}());
    
    //AttributeDeclaration s1 at invocations.ceylon (10:4-10:28)
    var s1$238=$$$f235.toString($$$cl234.Integer(99));
    
    //AttributeDeclaration s2 at invocations.ceylon (11:4-11:36)
    var s2$239=(function (){var obj$240=$$$cl234.Integer(99);return $$$f235.toString(obj$240)}());
    
    //AttributeDeclaration f1 at invocations.ceylon (12:4-12:29)
    var f1$241=$$$f235.add($$$cl234.Float(1.0),$$$cl234.Float(1.0).getNegativeValue());
    
    //AttributeDeclaration f2 at invocations.ceylon (13:4-13:37)
    var f2$242=(function (){var x$243=$$$cl234.Float(1.0);var y$244=$$$cl234.Float(1.0).getNegativeValue();return $$$f235.add(x$243,y$244)}());
    
    //MethodDefinition p at invocations.ceylon (14:4-16:4)
    function p$245(i$246){
        $$$cl234.print(i$246);
    }
    $$$f235.repeat($$$cl234.Integer(10),p$245);
}
exports.test=test;
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
