function Anything(anything$){
  $init$Anything();
  if(anything$===undefined)throwexc(InvocationException$meta$model("Cannot instantiate abstract class"),'?','?')
  return anything$;
}
Anything.$crtmm$=function(){return{mod:$CCMM$,ps:[],of:[{t:$_Object},{t:Null}],pa:257,an:function(){return[doc($CCMM$['ceylon.language'].Anything.an.doc[0]),by(["Gavin"].rt$({t:$_String}))];},d:['$','Anything']};};
ex$.Anything=Anything;
function $init$Anything(){
  if(Anything.$$===undefined){initTypeProto(Anything,'ceylon.language::Anything');}
  return Anything;
}
ex$.$init$Anything=$init$Anything;$init$Anything();
function Nothing(wat) {
    throw "Nothing";
}
initType(Nothing, 'ceylon.language::Nothing');
//This is quite a special case, since Nothing is not in the model, we need to insert it there
$CCMM$['ceylon.language']["Nothing"]={mt:"c",an:{"shared":[]},nm:"Nothing"};
Nothing.$crtmm$=function(){return{ps:[],pa:1,mod:$CCMM$,d:['$','Nothing']};}
