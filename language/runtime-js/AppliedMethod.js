function AppliedMethod(tipo,typeArgs,$$targs$$,$$appliedMethod){
  if (!$$targs$$.Type$AppliedMethod)$$targs$$.Type$AppliedMethod=$$targs$$.Type$Method;
  if (!$$targs$$.Arguments$AppliedMethod)$$targs$$.Arguments$AppliedMethod=$$targs$$.Arguments$Method;
  if (!$$targs$$.Container$AppliedMethod)$$targs$$.Container$AppliedMethod=$$targs$$.Container$Method;
  $init$AppliedMethod();
  var mm = getrtmm$$(tipo);
  if (mm.tp) {
    if (typeArgs===undefined || typeArgs.size<1)
      throw TypeApplicationException$meta$model("Missing type arguments in call to FunctionDeclaration.apply");
    var _ta={}; var i=0;
    for (var tp in mm.tp) {
      if (typeArgs.$_get(i)===undefined)
        throw TypeApplicationException$meta$model("Missing type argument for "+tp);
      var _tp = mm.tp[tp];
      var _t = typeArgs.$_get(i).tipo;
      _ta[tp]={t:_t};
      if (_tp.sts) {
        //Must satisty all specified types
        if (!extendsType(_ta[tp],{t:'i',l:_tp.sts})){
          throw TypeApplicationException$meta$model("Type argument for " + mm.d[mm.d.length-1] + "." + tp.substring(0,tp.indexOf('$')) + " violates type parameter constraints (satisfied)");
        }
      } else if (_tp.of) {
        //Must be one of these
        if (!extendsType(_ta[tp],{t:'u',l:_tp.of})){
          throw TypeApplicationException$meta$model("Type argument for " + mm.d[mm.d.length-1] + "." + tp.substring(0,tp.indexOf('$')) + " violates type parameter constraints (enumerated)");
        }
      }
      i++;
    }
  }
  if ($$appliedMethod===undefined){
    $$appliedMethod=function(x){
      return AppliedFunction(tipo,{Type$Function:$$targs$$.Type$Method,Arguments$Function:$$targs$$.Arguments$Method,
        Container$Function:$$targs$$.Container$Method},x,typeArgs);
    }
    var dummy=new AppliedMethod.$$;
    $$appliedMethod.getT$all=function(){return dummy.getT$all();};
    $$appliedMethod.getT$name=function(){return dummy.getT$name();};
  }
  if (_ta)$$appliedMethod.$targs=_ta;
  Method$meta$model($$appliedMethod.$$targs$$===undefined?$$targs$$:{Arguments$Method:$$appliedMethod.$$targs$$.Arguments$Method,
    Type$Method:$$appliedMethod.$$targs$$.Type$Method,Container$Method:$$appliedMethod.$$targs$$.Container$Method},$$appliedMethod);
  set_type_args($$appliedMethod,$$targs$$,AppliedMethod);
  $$appliedMethod.tipo=tipo;

//This was copied from prototype style
  atr$($$appliedMethod,'declaration',function(){
    var _m = typeof(mm.mod)==='function'?mm.mod():mm.mod;
    var _pkg = getModules$meta().find(_m['$mod-name'],_m['$mod-version']).findPackage(mm.d[0]);
    return OpenFunction(_pkg, $$appliedMethod.tipo);
  },undefined,function(){return{mod:$CCMM$,$t:{t:FunctionDeclaration$meta$declaration},$cont:AppliedMethod,an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Method','$at','declaration']};});

  atr$($$appliedMethod,'type',function(){
    return typeLiteral$meta({Type$typeLiteral:restype$(this.$$targs$$.Container$Method,this.$$targs$$.Type$Method||mm.$t)});
  },undefined,function(){return{mod:$CCMM$,$t:{t:Type$meta$model,a:{Type$Type:'Type'}},$cont:AppliedMethod,an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Method','$at','type']};});

  atr$($$appliedMethod,'typeArguments',function(){
    return funtypearg$($$appliedMethod);
  },undefined,FunctionModel$meta$model.$$.prototype.$prop$getTypeArguments.$crtmm$);
  atr$($$appliedMethod,'parameterTypes',function(){
    return funparamtypes($$appliedMethod);
  },undefined,FunctionModel$meta$model.$$.prototype.$prop$getParameterTypes.$crtmm$);

  $$appliedMethod.equals=function(o){
    return is$(o,{t:AppliedMethod}) && o.tipo===tipo && o.typeArguments.equals(this.typeArguments);
  }
  $$appliedMethod.$_bind=function(o){
    if (!is$(o,{t:mm.$cont}))throw IncompatibleTypeException$meta$model("Cannot bind " + $$appliedMethod.string + " to "+o);
    return $$appliedMethod(o);
  }
  atr$($$appliedMethod,'string',function(){
    return funmodstr$($$appliedMethod);
  },undefined,$_Object({}).$prop$getString.$crtmm$);
  atr$($$appliedMethod,'container',function(){
    if (this.toplevel || getrtmm$$(this.tipo).$cont===0)return this.containingPackage;
    if (this.$parent===undefined)this.$parent=typeLiteral$meta({Type$typeLiteral:getrtmm$$(this.tipo).$cont});
    return this.$parent;
  },undefined,function(){return{mod:$CCMM$,$t:{t:'u',l:[{t:Null},{t:Type$meta$model,a:{Type$Type:{t:Anything}}}]},$cont:AppliedMethod,d:['ceylon.language.meta.model','Model','$at','container']};});
  return $$appliedMethod;
}
AppliedMethod.$crtmm$=function(){return{mod:$CCMM$,'super':{t:Basic},tp:{Container$Method:{dv:'in'},Type$Method:{dv:'out','def':{t:Anything}},Arguments$Method:{dv:'in',sts:[{t:Sequential,a:{Element$Iterable:{t:Anything}}}],'def':{t:Nothing}}},sts:[{t:Method$meta$model,a:{Arguments$Method:'Arguments$Method',Type$Method:'Type$Method',Container$Method:'Container$Method'}}],pa:1,d:['ceylon.language.meta.model','Method']};};
ex$.AppliedMethod$meta$model=AppliedMethod;
function $init$AppliedMethod(){
    if (AppliedMethod.$$===undefined){
        initTypeProto(AppliedMethod,'ceylon.language.meta.model::AppliedMethod',Basic,Method$meta$model);
        (function($$appliedMethod){
//this area was moved inside AppliedMethod()            
        })(AppliedMethod.$$.prototype);
    }
    return AppliedMethod;
}
ex$.$init$AppliedMethod$meta$model=$init$AppliedMethod;
$init$AppliedMethod();
