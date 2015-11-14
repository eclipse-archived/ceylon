(function(define) { define(function(require, exports, module) {
var $CCMM$={"$mod-deps":["ceylon.language\/1.0.0"],"$mod-name":"jsonly","$mod-version":"1.0.0","jsonly":{"$pkg-shared":1,"run":{"$t":{"$md":"ceylon.language","$pk":"$","$nm":"Anything"},"$mt":"m","$an":{"shared":[]},"$nm":"run"}},"$mod-bin":"7.0"};
exports.$CCMM$=function(){return $CCMM$;};
var m$1=require('ceylon/language/1.0.0/ceylon.language-1.0.0');
m$1.$addmod$(m$1,'ceylon.language/1.0.0');
exports.$pkg$ans$jsonly=function(){return[m$1.shared()];};
function run(){
    m$1.print("jsonly");
}
exports.run=run;
run.$crtmm$=function(){return{mod:$CCMM$,$t:{t:m$1.Anything},$ps:[],$an:function(){return[m$1.shared()];},d:['jsonly','run']};};
exports.$mod$ans$=function(){return[m$1.doc("A test"),m$1.by(["The Ceylon Team"].reifyCeylonType({Element$Iterable:{t:m$1.String},Absent$Iterable:{t:m$1.Null}})),m$1.license("Apache Software License")];};
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
