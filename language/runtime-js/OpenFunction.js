function OpenFunction(pkg,meta,that){
    if (meta===undefined)throw Exception("Function reference not found. Metamodel doesn't work with modules compiled in lexical scope style");
    $init$OpenFunction();
    if (that===undefined)that=new OpenFunction.$$;
    that._pkg=pkg;
    var _mm=getrtmm$$(meta);
    if (_mm === undefined) {
      //it's a metamodel
      that.meta=meta;
      that.tipo=_findTypeFromModel(pkg,meta);
      _mm=getrtmm$$(that.tipo);
    } else {
      //it's a type
      that.tipo = meta;
      that.meta = get_model(_mm);
    }
    that.name_=(that.meta&&that.meta.nm)||'?';
    that.toplevel_=_mm===undefined||_mm.$cont===undefined;
    FunctionDeclaration$meta$declaration(that);
    return that;
}
OpenFunction.$crtmm$=function(){return{mod:$CCMM$,'super':{t:Basic},sts:[{t:FunctionDeclaration$meta$declaration}],d:['ceylon.language.meta.declaration','FunctionDeclaration']};};
function $init$OpenFunction(){
  if (OpenFunction.$$===undefined){
    initTypeProto(OpenFunction,'ceylon.language.meta.declaration::OpenFunction',Basic,FunctionDeclaration$meta$declaration);
    (function($$openFunction){
$$openFunction.equals=function(o) {
  if (is$(o,{t:OpenFunction})) {
    return o.tipo === this.tipo;
  }
  return false;
}
$$openFunction.equals.$crtmm$=function(){return{mod:$CCMM$,$t:{t:$_Boolean},d:['$','Object','$m','equals']};}
atr$($$openFunction,'container',function(){
  if (this.$parent)return this.$parent;
  if (this.toplevel || this.tipo.$crtmm$.$cont===0)return this.containingPackage;
  return typeLiteral$meta({Type$typeLiteral:this.tipo.$crtmm$.$cont});
},undefined,function(){return{mod:$CCMM$,$t:{t:'u',l:[{t:NestableDeclaration$meta$declaration},{t:Package$meta$declaration}]},d:['ceylon.language.meta.declaration','NestableDeclaration','$at','container']};});
atr$($$openFunction,'annotation',function(){
  return (getrtmm$$(this.tipo).pa & 512) > 0;
},undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:OpenFunction,pa:3,d:['ceylon.language.meta.declaration','FunctionalDeclaration','$at','annotation']};});

      $$openFunction.$_apply=function $_apply(types,$mptypes){
        var mm=this.tipo.$crtmm$;
        var ta={t:this.tipo};
        validate$typeparams(ta,mm.tp,types);
        validate$params(mm.ps,$mptypes.Arguments$apply,"Wrong number of arguments when applying function");
        return ta.a?AppliedFunction$jsint(this.tipo,{Type$AppliedFunction:mm.$t,Arguments$AppliedFunction:tupleize$params(mm.ps,ta.a)},undefined,ta.a):
          AppliedFunction$jsint(this.tipo,{Type$AppliedFunction:mm.$t,Arguments$AppliedFunction:tupleize$params(mm.ps)});
      };$$openFunction.$_apply.$crtmm$=function(){return{mod:$CCMM$,$t:{t:$_Function$meta$model,a:{Arguments:{t:Nothing},Type:{t:Anything}}},ps:[{nm:'types',mt:'prm',seq:1,$t:{t:Sequential,a:{Element$Sequential:{t:Type$meta$model}}}}],$cont:OpenFunction,pa:3,d:['ceylon.language.meta.declaration','FunctionDeclaration','$m','apply']};};

      $$openFunction.memberApply=function memberApply(cont,types,$mptypes){
        var mm=this.tipo.$crtmm$;
        if (!(cont.tipo && extendsType({t:cont.tipo},{t:mm.$cont}))&&cont!==nothingType$meta$model())
          throw IncompatibleTypeException$meta$model("Incompatible Container type argument");
        if (!extendsType(mm.$t,$mptypes.Return$memberApply))throw IncompatibleTypeException$meta$model("Incompatible Return type argument");
        validate$params(mm.ps,$mptypes.Arguments$memberApply,"Wrong number of Arguments for memberApply");
        var ta={t:this.tipo};
        validate$typeparams(ta,mm.tp,types);
        return AppliedMethod$jsint(this.tipo,types,{Container$AppliedMethod:$mptypes.Container$memberApply,
          Type$AppliedMethod:mm.$t,Arguments$AppliedMethod:tupleize$params(mm.ps,ta.a)});
      };$$openFunction.memberApply.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Method$meta$model,a:{Arguments:'Arguments',Type:'MethodType',Container:'Container'}},ps:[{nm:'types',mt:'prm',seq:1,$t:{t:Sequential,a:{Element$Sequential:{t:Type$meta$model}}}}],$cont:OpenFunction,tp:{Container:{},MethodType:{},Arguments:{'sts':[{t:Sequential,a:{Element$Sequential:{t:Anything}}}]}},pa:3,d:['ceylon.language.meta.declaration','FunctionDeclaration','$m','memberApply']};};
            
            //AttributeDeclaration defaulted at X (25:4-25:44)
            atr$($$openFunction,'defaulted',function(){
                return false;
            },undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:OpenFunction,pa:3,d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','defaulted']};});
            
            //AttributeDeclaration variadic at X (26:4-26:43)
            atr$($$openFunction,'variadic',function(){
                var $$openFunction=this;
                return false;
            },undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:OpenFunction,pa:3,d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','variadic']};});
    $$openFunction.getParameterDeclaration=function (name$6){
      var pd=this.parameterDeclarations;
      for (var i=0; i < pd.size; i++) {
        if (name$6.equals(pd[i].name))return pd[i];
      }
      return null;
    };
    $$openFunction.getParameterDeclaration.$crtmm$=function(){return{mod:$CCMM$,$t:{ t:'u', l:[{t:Null},{t:FunctionOrValueDeclaration$meta$declaration}]},ps:[{nm:'name',mt:'prm',$t:{t:$_String}}],$cont:OpenFunction,pa:3,d:['ceylon.language.meta.declaration','FunctionDeclaration','$m','getParameterDeclaration']};};
            
            //AttributeGetterDefinition openType at X (33:2-33:43)
    atr$($$openFunction,'openType',function(){
      var t = this.tipo.$crtmm$.$t;
      if (typeof(t)==='string')return OpenTvar$jsint(OpenTypeParam$jsint(this.tipo,t));
      return _openTypeFromTarg(t, this.tipo);
    },undefined,function(){return{mod:$CCMM$,$t:{t:OpenType$meta$declaration},$cont:OpenFunction,pa:3,d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','openType']};});
            atr$($$openFunction,'name',function(){return this.name_;},undefined,function(){return{mod:$CCMM$,$t:{t:$_String},$cont:OpenFunction,pa:3,d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','name']};});
            atr$($$openFunction,'containingPackage',function(){return this._pkg;},undefined,function(){return{mod:$CCMM$,$t:{t:Package$meta$declaration},$cont:OpenFunction,pa:3,d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','containingPackage']};});
            atr$($$openFunction,'containingModule',function(){return this._pkg.container;},undefined,function(){return{mod:$CCMM$,$t:{t:Package$meta$declaration},$cont:OpenFunction,pa:3,d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','containingPackage']};});
            atr$($$openFunction,'toplevel',function(){return this.toplevel_;},undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},$cont:OpenFunction,pa:3,d:['ceylon.language.meta.declaration','FunctionDeclaration','$at','toplevel']};});

    atr$($$openFunction,'string',function(){return "function " + this.qualifiedName;},undefined,function(){return{mod:$CCMM$,$t:{t:$_String},$cont:OpenFunction,pa:3,d:['ceylon.language.meta.declaration','Declaration','$at','string']};});
    atr$($$openFunction,'qualifiedName',function(){
       return qname$(this.tipo);
    },undefined,function(){return{mod:$CCMM$,$t:{t:$_String},$cont:OpenFunction,pa:3,d:['ceylon.language.meta.declaration','Declaration','$at','qualifiedName']};});
    })(OpenFunction.$$.prototype);
  }
  return OpenFunction;
}
ex$.OpenFunction=OpenFunction;
$init$OpenFunction();
