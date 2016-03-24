function $_Boolean(value) {return Boolean(value)}
initExistingTypeProto($_Boolean, Boolean, 'ceylon.language::Boolean');
$_Boolean.$crtmm$=function(){
  return{ps:[],pa:257,of:[getTrue,getFalse],
  mod:$CCMM$,d:['$','Boolean']};
};
ex$.$_Boolean=$_Boolean;
function $_true() {return true;}
initType($_true, "ceylon.language::true", $_Boolean);
$_true.$crtmm$=function(){return{'super':{t:$_Boolean},mod:$CCMM$,pa:65,d:['$','true']}};
function $_false() {return false;}
initType($_false, "ceylon.language::false", $_Boolean);
$_false.$crtmm$=function(){return{'super':{t:$_Boolean},mod:$CCMM$,pa:65,d:['$','false']}};
Boolean.prototype.getT$name = function() {
    return 'ceylon.language::Boolean';
}
Boolean.prototype.getT$all = function() {
    return (this.valueOf()?$_true:$_false).$$.T$all;
}
Boolean.prototype.equals = function(other) {return other.constructor===Boolean && other==this;}
atr$(Boolean.prototype, 'hash', function(){ return this.valueOf()?1231:1237; },
  undefined,{$t:{t:Integer},pa:3,mod:$CCMM$,d:['$','Object','$at','hash']});
atr$(Boolean.prototype, 'string', function(){ return this.valueOf()?"true":"false"; },
  undefined,{$t:{t:$_String},pa:3,mod:$CCMM$,d:['$','Object','$at','string']});

function getTrue() {return true;}
getTrue.$crtmm$=function(){return{mod:$CCMM$,pa:65,d:['$','true'],$t:{t:$_true}};};
function getFalse() {return false;}
getFalse.$crtmm$=function(){return{mod:$CCMM$,pa:65,d:['$','false'],$t:{t:$_false}};};
ex$.$prop$getTrue=$_true;
ex$.$prop$getFalse=$_false;
ex$.getTrue=getTrue;
ex$.getFalse=getFalse;
function $init$$_true(){return $_true;}
function $init$$_false(){return $_false;}
ex$.$init$$_true=$init$$_true;
ex$.$init$$_false=$init$$_false;
