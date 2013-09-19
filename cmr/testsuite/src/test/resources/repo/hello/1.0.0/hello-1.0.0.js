(function(define) { define(function(require, exports, module) {
var $$METAMODEL$$={"hello":{"hello":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"hello"},"$pkg-shared":"1","run":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"run"}},"$mod-deps":["ceylon.language\/0.6"],"$mod-name":"hello","$mod-version":"1.0.0"};
exports.$$METAMODEL$$=function(){return $$METAMODEL$$;};
var $$$cl1=require('ceylon/language/0.6/ceylon.language-0.6');
$$$cl1.$addmod$($$$cl1,'ceylon.language/0.6');
function hello(){
    $$$cl1.print($$$cl1.getProcess().arguments);
    $$$cl1.print($$$cl1.String("Hola niño",9));
}
exports.hello=hello;
hello.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],$an:function(){return[$$$cl1.shared()];},d:['hello','hello']};};
function run(){
    hello();
}
exports.run=run;
run.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],$an:function(){return[$$$cl1.shared()];},d:['hello','run']};};
exports.$pkg$ans$hello=function(){return[$$$cl1.shared()];};
exports.$mod$ans$=function(){return[$$$cl1.doc($$$cl1.String("A test",6)),$$$cl1.by([$$$cl1.String("The Ceylon Team",15)].reifyCeylonType({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.String}})),$$$cl1.license($$$cl1.String("Apache Software License",23))];};
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
