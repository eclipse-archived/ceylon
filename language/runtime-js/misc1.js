function getNull() { return null }
function Boolean$(value) {return Boolean(value)}
initExistingTypeProto(Boolean$, Boolean, 'ceylon.language::Boolean');
Boolean$.$$metamodel$$={$ps:[],$an:function(){return[shared(),abstract()]},mod:$$METAMODEL$$,d:$$METAMODEL$$['ceylon.language']['Boolean']};
function trueClass() {}
initType(trueClass, "ceylon.language::true", Boolean$);
function falseClass() {}
initType(falseClass, "ceylon.language::false", Boolean$);
Boolean.prototype.getT$name = function() {
    return (this.valueOf()?trueClass:falseClass).$$.T$name;
}
Boolean.prototype.getT$all = function() {
    return (this.valueOf()?trueClass:falseClass).$$.T$all;
}
Boolean.prototype.equals = function(other) {return other.constructor===Boolean && other==this;}
defineAttr(Boolean.prototype, 'hash', function(){ return this.valueOf()?1:0; },
  undefined,{$an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:$$METAMODEL$$['ceylon.language']['Object']['$at']['hash']});
var trueString = String$("true", 4);
var falseString = String$("false", 5);
defineAttr(Boolean.prototype, 'string', function(){ return this.valueOf()?trueString:falseString; },
  undefined,{$an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:$$METAMODEL$$['ceylon.language']['Object']['$at']['string']});
function getTrue() {return true}
function getFalse() {return false}
var $true = true;
var $false = false;

function Comparison(name) {
    var that = new Comparison.$$;
    that.name = String$(name);
    return that;
}
initTypeProto(Comparison, 'ceylon.language::Comparison', $init$Basic());
Comparison.$$metamodel$$={$ps:[{t:String$}],$an:function(){return[shared(),abstract()]},mod:$$METAMODEL$$,d:$$METAMODEL$$['ceylon.language']['Comparison']};
var Comparison$proto = Comparison.$$.prototype;
defineAttr(Comparison$proto, 'string', function(){ return this.name; },
  undefined,{$an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:$$METAMODEL$$['ceylon.language']['Object']['$at']['string']});
