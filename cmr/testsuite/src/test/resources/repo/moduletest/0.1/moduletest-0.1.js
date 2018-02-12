(function(define) { define(function(require, ex$, module) {
var $$req$ = require; require = (typeof $$ceylon$require == 'undefined') ? $$req$ : function() { return $$ceylon$require('moduletest/0.1/moduletest-0.1', $$req$, Array.prototype.slice.call(arguments)); }

var _CTM$;function $CCMM$(){if (_CTM$===undefined)_CTM$=require('moduletest/0.1/moduletest-0.1-model').$CCMM$;return _CTM$;}
ex$.$CCMM$=$CCMM$;
var m$1=require('ceylon/language/1.3.4-SNAPSHOT/ceylon.language-1.3.4-SNAPSHOT');
m$1.$addmod$(m$1,'ceylon.language/1.3.4-SNAPSHOT');
m$1.$addmod$(ex$,'moduletest/0.1');
var m$2=require('hello/1.2.1/hello-1.2.1');
m$1.$addmod$(m$2,'hello/1.2.1');
ex$.$mod$ans$=function(){return[m$1.doc("A test"),m$1.by(m$1.$arr$sa$(["The Ceylon Team"],{t:m$1.$_String})),m$1.license("GPLv2")];};
ex$.$mod$imps=function(){return{
'hello/1.2.1':function(){return[m$1.shared(),m$1.optional()];}
};};
function run(){
m$2.hello();
};run.$crtmm$=function(){return{mod:$CCMM$,$t:{t:m$1.Anything},ps:[],d:['moduletest','run']};};
ex$.$pkgunsh$moduletest={'run':run};
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
