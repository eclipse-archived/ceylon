(function(define) { define(function(require, exports, module) {
var $$$cl247=require('ceylon/language/0.2/ceylon.language');

//AttributeDeclaration assertionCount at members.ceylon (1:0-1:34)
var assertionCount$248=$$$cl247.Integer(0);
var getAssertionCount=function(){return assertionCount$248;};
exports.getAssertionCount=getAssertionCount;
var setAssertionCount=function(assertionCount$249){assertionCount$248=assertionCount$249; return assertionCount$248;};
exports.setAssertionCount=setAssertionCount;

//AttributeDeclaration failureCount at members.ceylon (2:0-2:32)
var failureCount$250=$$$cl247.Integer(0);
var getFailureCount=function(){return failureCount$250;};
exports.getFailureCount=getFailureCount;
var setFailureCount=function(failureCount$251){failureCount$250=failureCount$251; return failureCount$250;};
exports.setFailureCount=setFailureCount;

//MethodDefinition assert at members.ceylon (4:0-10:0)
function assert(assertion$252,message$253){
    if(message$253===undefined){message$253=$$$cl247.String("",0);}
    (setAssertionCount(getAssertionCount().plus($$$cl247.Integer(1))),getAssertionCount());
    if ((assertion$252.equals($$$cl247.getFalse()))===$$$cl247.getTrue()){
        (setFailureCount(getFailureCount().plus($$$cl247.Integer(1))),getFailureCount());
        $$$cl247.print($$$cl247.StringBuilder().appendAll($$$cl247.ArraySequence([$$$cl247.String("assertion failed \""),message$253.getString(),$$$cl247.String("\"")])).getString());
    }
    
}
exports.assert=assert;

//MethodDefinition fail at members.ceylon (12:0-14:0)
function fail(message$254){
    assert($$$cl247.getFalse(),message$254);
}
exports.fail=fail;

//MethodDefinition results at members.ceylon (16:0-19:0)
function results(){
    $$$cl247.print($$$cl247.StringBuilder().appendAll($$$cl247.ArraySequence([$$$cl247.String("assertions ",11),getAssertionCount().getString(),$$$cl247.String(", failures ",11),getFailureCount().getString(),$$$cl247.String("",0)])).getString());
}
exports.results=results;

//ClassDefinition Counter at members.ceylon (21:0-35:0)
function Counter(initialCount$255$, $$counter){
    if ($$counter===undefined)$$counter=new Counter.$$;
    if(initialCount$255$===undefined){initialCount$255$=$$$cl247.Integer(0);}
    $$counter.initialCount$255$=initialCount$255$;
    
    //AttributeDeclaration currentCount at members.ceylon (22:4-22:45)
    $$counter.currentCount$256$=$$counter.initialCount$255$;
    return $$counter;
}
exports.Counter=Counter;
$$$cl247.initTypeProto(Counter,'members.Counter',$$$cl247.IdentifiableObject);
;(function($proto$){
    
    //AttributeDeclaration currentCount at members.ceylon (22:4-22:45)
    $proto$.getCurrentCount$256$=function getCurrentCount$256$(){
        return this.currentCount$256$;
    }
    $proto$.setCurrentCount$256$=function setCurrentCount$256$(currentCount$257){
        this.currentCount$256$=currentCount$257; return currentCount$257;
    }
    
    //AttributeGetterDefinition count at members.ceylon (23:4-25:4)
    $proto$.getCount=function getCount(){
        var $$counter=this;
        return $$counter.getCurrentCount$256$();
    }
    
    //MethodDefinition inc at members.ceylon (26:4-28:4)
    $proto$.inc=function inc(){
        var $$counter=this;
        $$counter.setCurrentCount$256$($$counter.getCurrentCount$256$().plus($$$cl247.Integer(1)));
    }
    
    //AttributeGetterDefinition initialCount at members.ceylon (29:4-31:4)
    $proto$.getInitialCount=function getInitialCount(){
        var $$counter=this;
        return $$counter.initialCount$255$;
    }
    
    //AttributeGetterDefinition string at members.ceylon (32:4-34:4)
    $proto$.getString=function getString(){
        var $$counter=this;
        return $$$cl247.String("Counter[",8).plus($$counter.getCount().getString()).plus($$$cl247.String("]",1));
    }
    
})(Counter.$$.prototype);

//ClassDefinition Issue10C1 at members.ceylon (37:0-51:0)
function Issue10C1(arg1$258$, $$issue10C1){
    if ($$issue10C1===undefined)$$issue10C1=new Issue10C1.$$;
    $$issue10C1.arg1$258$=arg1$258$;
    
    //AttributeDeclaration i1 at members.ceylon (38:4-38:18)
    $$issue10C1.i1$259$=$$$cl247.Integer(3);
    
    //AttributeDeclaration i2 at members.ceylon (39:4-39:18)
    $$issue10C1.i2$260$=$$$cl247.Integer(5);
    
    //AttributeDeclaration i3 at members.ceylon (40:4-40:33)
    $$issue10C1.i3$261$=$$$cl247.Integer(7);
    return $$issue10C1;
}
$$$cl247.initTypeProto(Issue10C1,'members.Issue10C1',$$$cl247.IdentifiableObject);
;(function($proto$){
    
    //AttributeDeclaration i1 at members.ceylon (38:4-38:18)
    $proto$.getI1$259$=function getI1$259$(){
        return this.i1$259$;
    }
    
    //AttributeDeclaration i2 at members.ceylon (39:4-39:18)
    $proto$.getI2$260$=function getI2$260$(){
        return this.i2$260$;
    }
    
    //AttributeDeclaration i3 at members.ceylon (40:4-40:33)
    $proto$.getI3=function getI3(){
        return this.i3$261$;
    }
    
    //MethodDefinition f1 at members.ceylon (41:4-41:39)
    $proto$.f1=function f1(){
        var $$issue10C1=this;
        return $$issue10C1.arg1$258$;
    }
    
    //MethodDefinition f2 at members.ceylon (42:4-42:37)
    $proto$.f2=function f2(){
        var $$issue10C1=this;
        return $$issue10C1.getI1$259$();
    }
    
    //MethodDefinition f3 at members.ceylon (43:4-43:37)
    $proto$.f3=function f3(){
        var $$issue10C1=this;
        return $$issue10C1.getI2$260$();
    }
    
    //MethodDefinition f4 at members.ceylon (44:4-44:37)
    $proto$.f4=function f4(){
        var $$issue10C1=this;
        return $$issue10C1.getI3();
    }
    
    //MethodDefinition f5 at members.ceylon (45:4-45:29)
    $proto$.f5$262$=function f5$262$(){
        var $$issue10C1=this;
        return $$$cl247.Integer(9);
    }
    
    //MethodDefinition f6 at members.ceylon (46:4-46:39)
    $proto$.f6=function f6(){
        var $$issue10C1=this;
        return $$issue10C1.f5$262$();
    }
    
    //MethodDefinition f7 at members.ceylon (47:4-47:30)
    $proto$.f7$263$=function f7$263$(){
        var $$issue10C1=this;
        return $$$cl247.Integer(11);
    }
    
    //MethodDefinition f8 at members.ceylon (48:4-48:39)
    $proto$.f8=function f8(){
        var $$issue10C1=this;
        return $$issue10C1.f7$263$();
    }
    
    //MethodDefinition f9 at members.ceylon (49:4-49:45)
    $proto$.f9=function f9(){
        var $$issue10C1=this;
        return $$$cl247.Integer(13);
    }
    
    //MethodDefinition f10 at members.ceylon (50:4-50:40)
    $proto$.f10=function f10(){
        var $$issue10C1=this;
        return $$issue10C1.f9();
    }
    
})(Issue10C1.$$.prototype);

//ClassDefinition Issue10C2 at members.ceylon (52:0-62:0)
function Issue10C2(arg1$264$, $$issue10C2){
    if ($$issue10C2===undefined)$$issue10C2=new Issue10C2.$$;
    $$issue10C2.arg1$264$=arg1$264$;
    Issue10C1($$$cl247.Integer(1),$$issue10C2);
    
    //AttributeDeclaration i1 at members.ceylon (53:4-53:18)
    $$issue10C2.i1$265$=$$$cl247.Integer(4);
    
    //AttributeDeclaration i2 at members.ceylon (54:4-54:25)
    $$issue10C2.i2$266$=$$$cl247.Integer(6);
    
    //AttributeDeclaration i3 at members.ceylon (55:4-55:32)
    $$issue10C2.i3$267$=$$$cl247.Integer(8);
    return $$issue10C2;
}
$$$cl247.initTypeProto(Issue10C2,'members.Issue10C2',Issue10C1);
;(function($proto$){
    
    //AttributeDeclaration i1 at members.ceylon (53:4-53:18)
    $proto$.getI1$265$=function getI1$265$(){
        return this.i1$265$;
    }
    
    //AttributeDeclaration i2 at members.ceylon (54:4-54:25)
    $proto$.getI2=function getI2(){
        return this.i2$266$;
    }
    
    //AttributeDeclaration i3 at members.ceylon (55:4-55:32)
    $proto$.getI3=function getI3(){
        return this.i3$267$;
    }
    
    //MethodDefinition f11 at members.ceylon (56:4-56:40)
    $proto$.f11=function f11(){
        var $$issue10C2=this;
        return $$issue10C2.arg1$264$;
    }
    
    //MethodDefinition f12 at members.ceylon (57:4-57:38)
    $proto$.f12=function f12(){
        var $$issue10C2=this;
        return $$issue10C2.getI1$265$();
    }
    
    //MethodDefinition f5 at members.ceylon (58:4-58:30)
    $proto$.f5$268$=function f5$268$(){
        var $$issue10C2=this;
        return $$$cl247.Integer(10);
    }
    
    //MethodDefinition f13 at members.ceylon (59:4-59:40)
    $proto$.f13=function f13(){
        var $$issue10C2=this;
        return $$issue10C2.f5$268$();
    }
    
    //MethodDefinition f7 at members.ceylon (60:4-60:37)
    $proto$.f7=function f7(){
        var $$issue10C2=this;
        return $$$cl247.Integer(12);
    }
    
    //MethodDefinition f9 at members.ceylon (61:4-61:44)
    $proto$.f9=function f9(){
        var $$issue10C2=this;
        return $$$cl247.Integer(14);
    }
    
})(Issue10C2.$$.prototype);

//MethodDefinition testIssue10 at members.ceylon (64:0-80:0)
function testIssue10(){
    
    //AttributeDeclaration obj at members.ceylon (65:4-65:28)
    var obj$269=Issue10C2($$$cl247.Integer(2));
    assert(obj$269.f1().equals($$$cl247.Integer(1)),$$$cl247.String("Issue #10 (parameter)",21));
    assert(obj$269.f11().equals($$$cl247.Integer(2)),$$$cl247.String("Issue #10 (parameter)",21));
    assert(obj$269.f2().equals($$$cl247.Integer(3)),$$$cl247.String("Issue #10 (non-shared attribute)",32));
    assert(obj$269.f12().equals($$$cl247.Integer(4)),$$$cl247.String("Issue #10 (non-shared attribute)",32));
    assert(obj$269.f3().equals($$$cl247.Integer(5)),$$$cl247.String("Issue #10 (non-shared attribute)",32));
    assert(obj$269.getI2().equals($$$cl247.Integer(6)),$$$cl247.String("Issue #10 (shared attribute)",28));
    assert(obj$269.f4().equals($$$cl247.Integer(8)),$$$cl247.String("Issue #10 (shared attribute)",28));
    assert(obj$269.getI3().equals($$$cl247.Integer(8)),$$$cl247.String("Issue #10 (shared attribute)",28));
    assert(obj$269.f6().equals($$$cl247.Integer(9)),$$$cl247.String("Issue #10 (non-shared method)",29));
    assert(obj$269.f13().equals($$$cl247.Integer(10)),$$$cl247.String("Issue #10 (non-shared method)",29));
    assert(obj$269.f8().equals($$$cl247.Integer(11)),$$$cl247.String("Issue #10 (non-shared method)",29));
    assert(obj$269.f7().equals($$$cl247.Integer(12)),$$$cl247.String("Issue #10 (shared method)",25));
    assert(obj$269.f10().equals($$$cl247.Integer(14)),$$$cl247.String("Issue #10 (shared method)",25));
    assert(obj$269.f9().equals($$$cl247.Integer(14)),$$$cl247.String("Issue #10 (shared method)",25));
}

//ClassDefinition AssignTest at members.ceylon (82:0-86:0)
function AssignTest($$assignTest){
    if ($$assignTest===undefined)$$assignTest=new AssignTest.$$;
    
    //AttributeDeclaration x at members.ceylon (83:4-83:34)
    $$assignTest.x$270$=$$$cl247.Integer(1);
    return $$assignTest;
}
$$$cl247.initTypeProto(AssignTest,'members.AssignTest',$$$cl247.IdentifiableObject);
;(function($proto$){
    
    //AttributeDeclaration x at members.ceylon (83:4-83:34)
    $proto$.getX=function getX(){
        return this.x$270$;
    }
    $proto$.setX=function setX(x$271){
        this.x$270$=x$271; return x$271;
    }
    
    //AttributeGetterDefinition y at members.ceylon (84:4-84:33)
    $proto$.getY=function getY(){
        var $$assignTest=this;
        return $$assignTest.getX();
    }
    
    //AttributeSetterDefinition y at members.ceylon (85:4-85:23)
    $proto$.setY=function setY(y$272){
        var $$assignTest=this;
        $$assignTest.setX(y$272);
    }
    
})(AssignTest.$$.prototype);

//MethodDefinition test at members.ceylon (88:0-103:0)
function test(){
    
    //AttributeDeclaration c at members.ceylon (89:4-89:24)
    var c$273=Counter($$$cl247.Integer(0));
    assert(c$273.getCount().equals($$$cl247.Integer(0)),$$$cl247.String("counter 1",9));
    c$273.inc();
    c$273.inc();
    assert(c$273.getCount().equals($$$cl247.Integer(2)),$$$cl247.String("counter 2",9));
    assert(c$273.getString().equals($$$cl247.String("Counter[2]",10)),$$$cl247.String("counter.string",14));
    testIssue10();
    
    //AttributeDeclaration at at members.ceylon (97:4-97:27)
    var at$274=AssignTest();
    at$274.setX($$$cl247.Integer(5));
    assert(at$274.getX().equals($$$cl247.Integer(5)),$$$cl247.String("assign to member",16));
    at$274.setY($$$cl247.Integer(2));
    assert(at$274.getY().equals($$$cl247.Integer(2)),$$$cl247.String("assign using setter",19));
    results();
}
exports.test=test;
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
