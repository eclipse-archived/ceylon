(function(define) { define(function(require, exports, module) {
var $$$cl15=require('ceylon/language/0.2/ceylon.language');
var $$$f9=require('default/functions');

//MethodDefinition test at invocations.ceylon (3:0-18:0)
function test(){
    $$$f9.helloWorld();
    (function (){return $$$f9.helloWorld()}());
    $$$f9.hello($$$cl15.String("world",5));
    (function (){var $name=$$$cl15.String("world",5);return $$$f9.hello($name)}());
    $$$f9.helloAll($$$cl15.ArraySequence([$$$cl15.String("someone",7),$$$cl15.String("someone else",12)]));
    (function (){var $names=$$$cl15.ArraySequence([$$$cl15.String("someone",7),$$$cl15.String("someone else",12)]);return $$$f9.helloAll($names)}());
    
    //AttributeDeclaration s1 at invocations.ceylon (10:4-10:28)
    var tmpvar$81=$$$f9.toString($$$cl15.Integer(99));
    
    //AttributeDeclaration s2 at invocations.ceylon (11:4-11:36)
    var tmpvar$82=(function (){var $obj=$$$cl15.Integer(99);return $$$f9.toString($obj)}());
    
    //AttributeDeclaration f1 at invocations.ceylon (12:4-12:29)
    var tmpvar$83=$$$f9.add($$$cl15.Float(1.0),$$$cl15.Float(1.0).getNegativeValue());
    
    //AttributeDeclaration f2 at invocations.ceylon (13:4-13:37)
    var tmpvar$84=(function (){var $x=$$$cl15.Float(1.0);var $y=$$$cl15.Float(1.0).getNegativeValue();return $$$f9.add($x,$y)}());
    
    //MethodDefinition p at invocations.ceylon (14:4-16:4)
    function p(i){
        $$$cl15.print(i);
    }
    $$$f9.repeat($$$cl15.Integer(10),p);
}
exports.test=test;
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
