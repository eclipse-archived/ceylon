function $_Boolean(value) {return Boolean(value)}
initExistingTypeProto($_Boolean, Boolean, 'ceylon.language::Boolean');
$_Boolean.$crtmm$=function(){return{ps:[],pa:257,mod:$CCMM$,d:['$','Boolean']};};
ex$.$_Boolean=$_Boolean;
function trueClass() {}
initType(trueClass, "ceylon.language::true", $_Boolean);
trueClass.$crtmm$=function(){return{mod:$CCMM$,pa:65,d:['$','true'],$t:{t:trueClass}};};
function falseClass() {}
initType(falseClass, "ceylon.language::false", $_Boolean);
falseClass.$crtmm$=function(){return{mod:$CCMM$,pa:65,d:['$','false'],$t:{t:falseClass}};};
Boolean.prototype.getT$name = function() {
    return (this.valueOf()?trueClass:falseClass).$$.T$name;
}
Boolean.prototype.getT$all = function() {
    return (this.valueOf()?trueClass:falseClass).$$.T$all;
}
Boolean.prototype.equals = function(other) {return other.constructor===Boolean && other==this;}
atr$(Boolean.prototype, 'hash', function(){ return this.valueOf()?1:0; },
  undefined,{$t:{t:Integer},pa:3,mod:$CCMM$,d:['$','Object','$at','hash']});
atr$(Boolean.prototype, 'string', function(){ return this.valueOf()?"true":"false"; },
  undefined,{$t:{t:$_String},pa:3,mod:$CCMM$,d:['$','Object','$at','string']});

function getTrue() {return true;}
function getFalse() {return false;}
ex$.$prop$getTrue={get:getTrue,$crtmm$:trueClass.$crtmm$};
ex$.$prop$getFalse={get:getFalse,$crtmm$:falseClass.$crtmm$};
