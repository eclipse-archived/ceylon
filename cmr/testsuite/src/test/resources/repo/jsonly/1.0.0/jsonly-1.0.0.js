(function(define) { define(function(require, exports, module) {
var $$METAMODEL$$={"$mod-deps":["ceylon.language\/0.6"],"$mod-name":"jsonly","$mod-version":"1.0.0","jsonly":{"$pkg-shared":"1","run":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"run"}}};
exports.$$METAMODEL$$=function(){return $$METAMODEL$$;};
var $$$cl3887=require('ceylon/language/0.6/ceylon.language-0.6');
$$$cl3887.$addmod$($$$cl3887,'ceylon.language/0.6');
exports.$pkg$ans$jsonly=function(){return[$$$cl3887.shared()];};
exports.$mod$ans$=function(){return[$$$cl3887.doc($$$cl3887.String("A test",6)),$$$cl3887.by([$$$cl3887.String("The Ceylon Team",15)].reifyCeylonType({Absent:{t:$$$cl3887.Null},Element:{t:$$$cl3887.String}})),$$$cl3887.license($$$cl3887.String("Apache Software License",23))];};

//MethodDefinition run at jsonly.ceylon (2:0-4:0)
function run(){
    $$$cl3887.print($$$cl3887.String("jsonly",6));
}
exports.run=run;
run.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl3887.Anything},$ps:[],$an:function(){return[$$$cl3887.shared()];},d:['jsonly','run']};};
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
