(function(define) { define(function(require, ex$, module) {
var $$req$ = require; require = (typeof $$ceylon$require == 'undefined') ? $$req$ : function() { return $$ceylon$require('jsonly/1.0.0/jsonly-1.0.0', $$req$, Array.prototype.slice.call(arguments)); }

var _CTM$;function $CCMM$(){if (_CTM$===undefined)_CTM$=require('jsonly/1.0.0/jsonly-1.0.0-model').$CCMM$;return _CTM$;}
ex$.$CCMM$=$CCMM$;
var m$1=require('ceylon/language/1.3.4-SNAPSHOT/ceylon.language-1.3.4-SNAPSHOT');
m$1.$addmod$(m$1,'ceylon.language/1.3.4-SNAPSHOT');
m$1.$addmod$(ex$,'jsonly/1.0.0');
ex$.$mod$ans$=function(){return[m$1.doc$($CCMM$,'','$mod-anns'),m$1.by(m$1.$arr$sa$(["The Ceylon Team"],{t:m$1.$_String})),m$1.license("Apache Software License"),m$1.$_native(m$1.$arr$sa$(["js"],{t:m$1.$_String}))];};
function run(){
m$1.print("jsonly");
}
ex$.run=run;
run.$crtmm$=function(){return{mod:$CCMM$,$t:{t:m$1.Anything},ps:[],pa:1,d:['jsonly','run']};};
ex$.$pkg$ans$jsonly=function(){return[m$1.shared()];};
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
