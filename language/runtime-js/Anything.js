function Anything(anything$){
  $init$Anything();
  if(anything$===undefined)throwexc(InvocationException$meta$model("Cannot instantiate abstract class"),'?','?')
  return anything$;
}
Anything.$crtmm$=function(){return{mod:$CCMM$,ps:[],of:[{t:$_Object},{t:Null}],pa:257,an:function(){return[doc$($CCMM$,'$:Anything'),by($arr$(["Gavin"],{t:$_String}))];},d:['$','Anything']};};
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
Nothing.$crtmm$=function(){return{ps:[],pa:1,mod:$CCMM$,d:['$','Nothing']};}
ex$.Nothing=Nothing;
