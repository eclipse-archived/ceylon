function $_Boolean(value) {return Boolean(value)}
$_Boolean.$st$={
  parse:function(s){
    var b=parseBoolean(s);
    return b==null?ParseException('illegal format for Boolean'):b;
  }
};
$_Boolean.$st$.parse.$m$=function(){return{mod:$M$,$t:{t:'u',l:[{t:$_Boolean},{t:ParseException}]},ps:[{nm:'string',mt:'prm',$t:{t:$_String}}],$cont:$_Boolean,pa:4097,an:function(){return[tagged($arr$sa$(["Basic types"],{t:$_String})),since("1.3.1")];},d:['$','Boolean','$m','parse']};};
initExistingTypeProto($_Boolean, Boolean, 'ceylon.language::Boolean');
$_Boolean.$m$=function(){
  return{ps:[],pa:257,of:[getTrue,getFalse],
  mod:$M$,d:['$','Boolean']};
};
x$.$_Boolean=$_Boolean;
function $i$$_Boolean(){return $_Boolean;}
x$.$i$$_Boolean=$i$$_Boolean;
function $_true() {return true;}
initType($_true, "ceylon.language::true", $_Boolean);
$_true.$m$=function(){return{'super':{t:$_Boolean},mod:$M$,pa:65,d:['$','true']}};
function $_false() {return false;}
initType($_false, "ceylon.language::false", $_Boolean);
$_false.$m$=function(){return{'super':{t:$_Boolean},mod:$M$,pa:65,d:['$','false']}};
Boolean.prototype.getT$name = function() {
    return 'ceylon.language::Boolean';
}
Boolean.prototype.getT$all = function() {
    return (this.valueOf()?$_true:$_false).$$.T$all;
}
Boolean.prototype.equals = function(other) {return other.constructor===Boolean && other==this;}
atr$(Boolean.prototype, 'hash', function(){ return this.valueOf()?1231:1237; },
  undefined,{$t:{t:Integer},pa:3,mod:$M$,d:['$','Object','$at','hash']});
atr$(Boolean.prototype, 'string', function(){ return this.valueOf()?"true":"false"; },
  undefined,{$t:{t:$_String},pa:3,mod:$M$,d:['$','Object','$at','string']});

function getTrue() {return true;}
getTrue.$m$=function(){return{mod:$M$,pa:65,d:['$','true'],$t:{t:$_true}};};
function getFalse() {return false;}
getFalse.$m$=function(){return{mod:$M$,pa:65,d:['$','false'],$t:{t:$_false}};};
x$.$prop$getTrue=$_true;
x$.$prop$getFalse=$_false;
x$.getTrue=getTrue;
x$.getFalse=getFalse;
function $i$$_true(){return $_true;}
function $i$$_false(){return $_false;}
x$.$i$$_true=$i$$_true;
x$.$i$$_false=$i$$_false;
