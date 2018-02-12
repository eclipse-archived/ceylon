(function(define) { define(function(require, ex$, module) {
var $$req$ = require; require = (typeof $$ceylon$require == 'undefined') ? $$req$ : function() { return $$ceylon$require('hello/1.2.1/hello-1.2.1', $$req$, Array.prototype.slice.call(arguments)); }

var _CTM$;function $CCMM$(){if (_CTM$===undefined)_CTM$=require('hello/1.2.1/hello-1.2.1-model').$CCMM$;return _CTM$;}
ex$.$CCMM$=$CCMM$;
var m$1=require('ceylon/language/1.3.4-SNAPSHOT/ceylon.language-1.3.4-SNAPSHOT');
m$1.$addmod$(m$1,'ceylon.language/1.3.4-SNAPSHOT');
m$1.$addmod$(ex$,'hello/1.2.1');
ex$.$mod$ans$=function(){return[m$1.doc$($CCMM$,'','$mod-anns'),m$1.by(m$1.$arr$sa$(["The Ceylon Team"],{t:m$1.$_String})),m$1.license("Apache Software License")];};
function hello(){
m$1.print(m$1.$_process().$_arguments);
m$1.print("Hola ni\u00f1o");
}
ex$.hello=hello;
hello.$crtmm$=function(){return{mod:$CCMM$,$t:{t:m$1.Anything},ps:[],pa:1,d:['hello','hello']};};
function run(){
hello();
}
ex$.run=run;
run.$crtmm$=function(){return{mod:$CCMM$,$t:{t:m$1.Anything},ps:[],pa:1,d:['hello','run']};};
function run2(){
hello();
}
ex$.run2=run2;
run2.$crtmm$=function(){return{mod:$CCMM$,$t:{t:m$1.Anything},ps:[],pa:1,d:['hello','run2']};};
ex$.$pkg$ans$hello=function(){return[m$1.shared()];};
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
