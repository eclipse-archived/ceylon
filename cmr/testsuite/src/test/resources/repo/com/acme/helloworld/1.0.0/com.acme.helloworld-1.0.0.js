(function(define) { define(function(require, ex$, module) {
var $$req$ = require; require = (typeof $$ceylon$require == 'undefined') ? $$req$ : function() { return $$ceylon$require('com/acme/helloworld/1.0.0/com.acme.helloworld-1.0.0', $$req$, Array.prototype.slice.call(arguments)); }

var _CTM$;function $CCMM$(){if (_CTM$===undefined)_CTM$=require('com/acme/helloworld/1.0.0/com.acme.helloworld-1.0.0-model').$CCMM$;return _CTM$;}
ex$.$CCMM$=$CCMM$;
var m$1=require('ceylon/language/1.3.4-SNAPSHOT/ceylon.language-1.3.4-SNAPSHOT');
m$1.$addmod$(m$1,'ceylon.language/1.3.4-SNAPSHOT');
m$1.$addmod$(ex$,'com.acme.helloworld/1.0.0');
ex$.$mod$ans$=function(){return[m$1.doc$($CCMM$,'','$mod-anns'),m$1.by(m$1.$arr$sa$(["Stef Epardaud"],{t:m$1.$_String})),m$1.license("Public domain")];};
function hello($3){
if($3===undefined){$3="World";}
m$1.print(("Hello, "+" name "+"!"));
};hello.$crtmm$=function(){return{mod:$CCMM$,$t:{t:m$1.Anything},ps:[{nm:'name',mt:'prm',def:1,$t:{t:m$1.$_String}}],an:function(){return[m$1.doc$($CCMM$,'com.acme.helloworld:hello')];},d:['com.acme.helloworld','hello']};};
function run(){
var $4;
if(m$1.ne$(($4=m$1.$_process().$_arguments))){
var $6,$7;for(var $5=$4.iterator();($6=$5.next())!==m$1.finished();){
$7=true;hello($6);
}
if(!$7)m$1.throwexc(m$1.AssertionError("nonempty Iterable with initial \'finished\' element"),'9:8-11:8','helloworld.ceylon');
}
else{
hello();
}
};run.$crtmm$=function(){return{mod:$CCMM$,$t:{t:m$1.Anything},ps:[],an:function(){return[m$1.doc$($CCMM$,'com.acme.helloworld:run')];},d:['com.acme.helloworld','run']};};
ex$.$pkgunsh$com$acme$helloworld={'hello':hello,'run':run};
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
