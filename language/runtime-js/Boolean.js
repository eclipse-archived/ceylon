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

atr$(Boolean.prototype, 'not', function(){ return !this.valueOf(); },
  undefined,function(){return{an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:Binary,d:['$','Binary','$at','not']};});
Boolean.prototype.leftLogicalShift = function(i) { return this.valueOf(); }
Boolean.prototype.rightLogicalShift = function(i) { return this.valueOf(); }
Boolean.prototype.rightArithmeticShift = function(i) { return this.valueOf(); }
Boolean.prototype.and = function(x) { return this.valueOf() && x.valueOf(); }
Boolean.prototype.or = function(x) { return this.valueOf() || x.valueOf(); }
Boolean.prototype.xor = function(x) { return this.valueOf()?!x.valueOf():x.valueOf(); }
Boolean.prototype.$_get = function(idx) {
    return idx === 0 ? this.valueOf() : false;
}
Boolean.prototype.set = function(idx,bit) {
    if (bit === undefined) { bit = true; }
    return idx === 0 ? bit.valueOf() : this.valueOf();
}
Boolean.prototype.flip = function(idx) {
    return idx === 0 ? !this.valueOf() : this.valueOf();
}
Boolean.prototype.clear = function(index) {
    return index === 0 ? false : this.valueOf();
}

function getTrue() {return true;}
function getFalse() {return false;}
ex$.$prop$getTrue={get:getTrue,$crtmm$:function(){return{mod:$CCMM$,d:['$','true'],$t:{t:$_Boolean}};}};
ex$.$prop$getFalse={get:getFalse,$crtmm$:function(){return{mod:$CCMM$,d:['$','false'],$t:{t:$_Boolean}};}};
