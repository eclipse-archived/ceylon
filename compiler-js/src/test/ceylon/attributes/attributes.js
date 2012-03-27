(function(define) { define(function(require, exports, module) {
var $$$cl15=require('ceylon/language/0.2/ceylon.language');

//AttributeDeclaration firstName at attributes.ceylon (1:0-1:26)
var tmpvar$1=$$$cl15.String("Gavin",5);

//AttributeGetterDefinition lastName at attributes.ceylon (3:0-5:0)
var getLastName=function(){
    var tmpvar$2=(function(){
        return $$$cl15.String("King",4);
    }
    ());
    getLastName=function(){return tmpvar$2;};
    return tmpvar$2;
}

//AttributeSetterDefinition lastName at attributes.ceylon (7:0-9:0)
function setLastName(lastName){
    $$$cl15.print(lastName);
}

//MethodDefinition test at attributes.ceylon (11:0-11:20)
function test(){
    
}
exports.test=test;
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
