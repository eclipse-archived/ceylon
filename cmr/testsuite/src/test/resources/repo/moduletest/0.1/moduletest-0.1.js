(function(define) { define(function(require, exports, module) {
var $$METAMODEL$$={"$mod-deps":["ceylon.language\/0.6","hello\/1.0.0"],"$mod-name":"moduletest","$mod-version":"0.1","moduletest":{"run":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"run"}}};
exports.$$METAMODEL$$=function(){return $$METAMODEL$$;};
var $$$cl1=require('ceylon/language/0.6/ceylon.language-0.6');
$$$cl1.$addmod$($$$cl1,'ceylon.language/0.6');
var $$$h2=require('hello/1.0.0/hello-1.0.0');
$$$cl1.$addmod$($$$h2,'hello/1.0.0');
function run(){
    $$$h2.hello();
};run.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],d:['moduletest','run']};};
exports.$mod$ans$=function(){return[$$$cl1.doc($$$cl1.String("A test",6)),$$$cl1.by([$$$cl1.String("The Ceylon Team",15)].reifyCeylonType({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.String}})),$$$cl1.license($$$cl1.String("GPLv2",5))];};
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
