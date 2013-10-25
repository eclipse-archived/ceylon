defineAttr(FunctionModel$meta$model.$$.prototype,'parameterTypes',function(){
  var ps=this.tipo.$$metamodel$$.$ps;
  if (!ps || ps.length==0)return getEmpty();
  var r=[];
  for (var i=0; i < ps.length; i++) {
    var pt=ps[i].$t;
    if (typeof(pt)==='string'){
      if (!this.$targs)throw TypeApplicationException$meta$model(String$("This function model needs type parameters"));
      pt=this.$targs[pt];
      if (!pt)throw TypeApplicationException$meta$model(String$("Function model is missing type argument for <" + ps[i].$t + ">"));
    }
    r.push(typeLiteral$meta({Type:pt}));
  }
  return r.reifyCeylonType({Element:{t:Type$meta$model,a:{t:Anything}},Absent:{t:Null}});
},undefined,function(){return{mod:$$METAMODEL$$,$cont:FunctionModel$meta$model,d:['ceylon.language.meta.model','FunctionModel','$at','parameterTypes'],$t:{t:Sequential,a:{Element:{t:Type$meta$model,a:{Type:{t:Anything}}},Absent:{t:Null}}}};});

