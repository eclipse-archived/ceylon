(function(define) { define(function(require, exports, module) {
var $$$cl1=require('ceylon/language/0.5/ceylon.language');
var $$$f227=require('default/functions');
var $$$a12=require('default/assert');

//MethodDefinition test at invocations.ceylon (4:0-21:0)
function test(){
    $$$f227.helloWorld();
    (function (){return $$$f227.helloWorld()}());
    $$$f227.hello($$$cl1.String("world",5));
    (function (){var name$134=$$$cl1.String("world",5);return $$$f227.hello(name$134)}());
    $$$f227.helloAll($$$cl1.ArraySequence([$$$cl1.String("someone",7),$$$cl1.String("someone else",12)]));
    (function (){var names$135=$$$cl1.ArraySequence([$$$cl1.String("someone",7),$$$cl1.String("someone else",12)]);return $$$f227.helloAll(names$135)}());
    
    //AttributeDeclaration s1 at invocations.ceylon (11:4-11:28)
    var s1$228=$$$f227.toString($$$cl1.Integer(99));
    
    //AttributeDeclaration s2 at invocations.ceylon (12:4-12:36)
    var s2$229=(function (){var obj$136=$$$cl1.Integer(99);return $$$f227.toString(obj$136)}());
    
    //AttributeDeclaration f1 at invocations.ceylon (13:4-13:29)
    var f1$230=$$$f227.add($$$cl1.Float(1.0),$$$cl1.Float(1.0).getNegativeValue());
    
    //AttributeDeclaration f2 at invocations.ceylon (14:4-14:37)
    var f2$231=(function (){var x$137=$$$cl1.Float(1.0);var y$138=$$$cl1.Float(1.0).getNegativeValue();return $$$f227.add(x$137,y$138)}());
    
    //MethodDefinition p at invocations.ceylon (15:4-17:4)
    function p$232(i$233){
        $$$cl1.print(i$233);
    }
    $$$f227.repeat($$$cl1.Integer(10),p$232);
    testNamedArguments();
    $$$a12.results();
}
exports.test=test;
var $$$cl1=require('ceylon/language/0.5/ceylon.language');
var $$$a12=require('default/assert');

//MethodDefinition namedFunc at named.ceylon (4:0-11:0)
function namedFunc(iter$234,desc$235,match$236){
    //'for' statement at named.ceylon (5:2-9:2)
    var it$237 = iter$234.getIterator();
    var i$238;while ((i$238=it$237.next())!==$$$cl1.getExhausted()){
        
        if ((match$236(i$238))===$$$cl1.getTrue()){
            return $$$cl1.StringBuilder().appendAll($$$cl1.ArraySequence([$$$cl1.String("",0),desc$235.getString(),$$$cl1.String(": ",2),i$238.getString(),$$$cl1.String("",0)])).getString();
        }
        
    }
    return $$$cl1.String("[NOT FOUND]",11);
}

//MethodDefinition testNamedArguments at named.ceylon (13:0-38:0)
function testNamedArguments(){
    $$$a12.assert((function (){var iter$234=(function(){
        //ObjectArgument iter at named.ceylon (15:4-19:4)
        function iter$239(){
            var $$iter$239=new iter$239.$$;
            $$$cl1.Iterable($$iter$239);
            
            //AttributeGetterDefinition iterator at named.ceylon (16:6-18:6)
            var getIterator=function(){
                return $$$cl1.ArraySequence([$$$cl1.Integer(1),$$$cl1.Integer(3),$$$cl1.Integer(5),$$$cl1.Integer(8),$$$cl1.Integer(9)]).getIterator();
            }
            $$iter$239.getIterator=getIterator;
            return $$iter$239;
        }
        function $init$iter$239(){
            if (iter$239.$$===undefined){
                $$$cl1.initTypeProto(iter$239,'invocations.testNamedArguments.iter',$$$cl1.IdentifiableObject,$$$cl1.Iterable);
            }
            return iter$239;
        }
        $init$iter$239();
        return iter$239(new iter$239.$$);
    }
    ());;var desc$235=(function(){
        //AttributeArgument desc at named.ceylon (20:4-22:4)
        return $$$cl1.String("Even",4);
    }
    ());;var match$236=function (i$240){
        return i$240.remainder($$$cl1.Integer(2)).equals($$$cl1.Integer(0));
    }
    ;return namedFunc(iter$234,desc$235,match$236)}()).equals($$$cl1.String("Even: 8",7)),$$$cl1.String("named arguments 1",17));
    $$$a12.assert((function (){var iter$234=(function(){
        //ObjectArgument iter at named.ceylon (28:4-32:4)
        function iter$241(){
            var $$iter$241=new iter$241.$$;
            $$$cl1.Iterable($$iter$241);
            
            //AttributeGetterDefinition iterator at named.ceylon (29:6-31:6)
            var getIterator=function(){
                return $$$cl1.ArraySequence([$$$cl1.Integer(2),$$$cl1.Integer(4),$$$cl1.Integer(6),$$$cl1.Integer(8),$$$cl1.Integer(9),$$$cl1.Integer(10)]).getIterator();
            }
            $$iter$241.getIterator=getIterator;
            return $$iter$241;
        }
        function $init$iter$241(){
            if (iter$241.$$===undefined){
                $$$cl1.initTypeProto(iter$241,'invocations.testNamedArguments.iter',$$$cl1.IdentifiableObject,$$$cl1.Iterable);
            }
            return iter$241;
        }
        $init$iter$241();
        return iter$241(new iter$241.$$);
    }
    ());;var desc$235=(function(){
        //AttributeArgument desc at named.ceylon (33:4-35:4)
        return $$$cl1.String("Odd",3);
    }
    ());;var match$236=function (x$242){return x$242.remainder($$$cl1.Integer(2)).equals($$$cl1.Integer(1))};return namedFunc(iter$234,desc$235,match$236)}()).equals($$$cl1.String("Odd: 9",6)),$$$cl1.String("named arguments 2",17));
}
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
