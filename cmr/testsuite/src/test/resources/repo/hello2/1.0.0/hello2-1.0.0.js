(function(define) { define(function(require, ex$, module) {

var _CTM$;function $CCMM$(){if (_CTM$===undefined)_CTM$=require('hello2/1.0.0/hello2-1.0.0-model').$CCMM$;return _CTM$;}
ex$.$CCMM$=$CCMM$;
var m$1=require('ceylon/language/1.2.0/ceylon.language-1.2.0');
m$1.$addmod$(m$1,'ceylon.language/1.2.0');
m$1.$addmod$(ex$,'hello2/1.0.0');
function hello(){
m$1.print(m$1.$_process().$_arguments);
m$1.print("Hola niño");
}
ex$.hello=hello;
hello.$crtmm$=function(){return{mod:$CCMM$,$t:{t:m$1.Anything},ps:[],pa:1,d:['hello2','hello']};};
function run(){
hello();
}
ex$.run=run;
run.$crtmm$=function(){return{mod:$CCMM$,$t:{t:m$1.Anything},ps:[],pa:1,d:['hello2','run']};};
ex$.$mod$ans$=function(){return[m$1.doc$($CCMM$,'','$mod-anns'),m$1.by(["The Ceylon Team"].$sa$({t:m$1.$_String})),m$1.license("Apache Software License")];};
ex$.$pkg$ans$hello2=function(){return[m$1.shared()];};
function run2(){
hello();
}
ex$.run2=run2;
run2.$crtmm$=function(){return{mod:$CCMM$,$t:{t:m$1.Anything},ps:[],pa:1,d:['hello2','run2']};};
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
