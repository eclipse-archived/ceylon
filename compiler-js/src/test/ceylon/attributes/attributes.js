(function(define) { define(function(require, exports, module) {
var $$$cl1=require('ceylon/language/0.4/ceylon.language');
var $$$a12=require('default/assert');

//AttributeDeclaration firstName at attributes.ceylon (3:0-3:26)
var firstName$13=$$$cl1.String("Gavin",5);

//AttributeGetterDefinition lastName at attributes.ceylon (5:0-7:0)
var getLastName=function(){
    return $$$cl1.String("King",4);
}

//AttributeDeclaration flag at attributes.ceylon (9:0-9:26)
var flag$14=$$$cl1.Integer(0);
var getFlag=function(){return flag$14;};
exports.getFlag=getFlag;
var setFlag=function(flag$15){flag$14=flag$15; return flag$14;};
exports.setFlag=setFlag;

//AttributeSetterDefinition lastName at attributes.ceylon (10:0-13:0)
var setLastName=function(lastName$16){
    setFlag($$$cl1.Integer(1));
}

//MethodDefinition test at attributes.ceylon (15:0-27:0)
function test(){
    $$$a12.assertEqual(getLastName(),$$$cl1.String("King",4),$$$cl1.String("toplevel getter",15));
    setLastName($$$cl1.String("Duke",4));
    $$$a12.assertEqual(getFlag(),$$$cl1.Integer(1),$$$cl1.String("toplevel setter",15));
    
    //AttributeGetterDefinition x at attributes.ceylon (20:4-20:26)
    var getX$17=function(){
        return $$$cl1.Integer(5);
    }
    
    //AttributeSetterDefinition x at attributes.ceylon (21:4-21:26)
    var setX$17=function(x$18){
        setFlag($$$cl1.Integer(2));
    }
    $$$a12.assertEqual(getX$17(),$$$cl1.Integer(5),$$$cl1.String("local getter",12));
    setX$17($$$cl1.Integer(7));
    $$$a12.assertEqual(getFlag(),$$$cl1.Integer(2),$$$cl1.String("local setter",12));
    $$$a12.results();
}
exports.test=test;
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
