function $_Boolean(value) {return Boolean(value)}
initExistingTypeProto($_Boolean, Boolean, 'ceylon.language::Boolean');
$_Boolean.$crtmm$={ps:[],an:function(){return[shared(),abstract()]},mod:$CCMM$,d:['$','Boolean']};
ex$.$_Boolean=$_Boolean;
function trueClass() {}
initType(trueClass, "ceylon.language::true", $_Boolean);
function falseClass() {}
initType(falseClass, "ceylon.language::false", $_Boolean);
Boolean.prototype.getT$name = function() {
    return (this.valueOf()?trueClass:falseClass).$$.T$name;
}
Boolean.prototype.getT$all = function() {
    return (this.valueOf()?trueClass:falseClass).$$.T$all;
}
Boolean.prototype.equals = function(other) {return other.constructor===Boolean && other==this;}
atr$(Boolean.prototype, 'hash', function(){ return this.valueOf()?1:0; },
  undefined,{an:function(){return[shared(),actual()]},mod:$CCMM$,d:['$','Object','$at','hash']});
atr$(Boolean.prototype, 'string', function(){ return this.valueOf()?"true":"false"; },
  undefined,{an:function(){return[shared(),actual()]},mod:$CCMM$,d:['$','Object','$at','string']});

function getTrue() {return true;}
function getFalse() {return false;}
ex$.$prop$getTrue={get:getTrue,$crtmm$:function(){return{mod:$CCMM$,d:['$','true'],$t:{t:$_Boolean}};}};
ex$.$prop$getFalse={get:getFalse,$crtmm$:function(){return{mod:$CCMM$,d:['$','false'],$t:{t:$_Boolean}};}};
