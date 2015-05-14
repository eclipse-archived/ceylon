(function(define) {
    define(function(rq$, ex$, module) {
//the Ceylon language module
//#METAMODEL
//#COMPILE core_functions.js,is-op.js,exists-nonempty.js,type_arithmetic.js,Anything.js,Object,Null,Identifiable,Callable,callables.js,dynamic_functions.js
//#COMPILE comprehensions.js,Basic,Throwable,printStackTrace,Exception,Comparison,identityHash,impl/rethrow,impl/bases
//#COMPILE Iterable,Correspondence,Finished,Sequential,Sequence,Empty,Boolean.js,tuples.js
//#COMPILE Binary,Usable,Destroyable,Obtainable,Comparable,Invertible,Summable,Ordinal,Enumerable,Numeric,Exponentiable,Integral,Scalable,Number,numbers.js,OverflowException,InitializationError,Resource,Ranged,Iterator,Collection,Category,List,Array,sequences.js,ArraySequence,Tuple,ChainedIterator,CycledIterator,Entry,Byte
//#COMPILE Set,Range,Span,Measure,Singleton,AssertionError,Map,String,strings.js,Character,StringBuilder
//#COMPILE any,byDecreasing,byIncreasing,byItem,byKey,count,emptyOrSingleton,curry,apply,comparing,functions
//#COMPILE every,forItem,forKey,concatenate,expand,interleave,corresponding,loop,largest,max,min,smallest,sum,product,pairs,zip,unzip,print,nothing
//#COMPILE identical,compose,shuffle,flatten,unflatten,plus,times,sort,and,or,not,arrayOfSize,annotations
//#COMPILE process.js,process,language,system,operatingSystem,runtime,parseInteger,parseFloat,formatInteger,parseBoolean,className
//#metamodel
//#COMPILE Annotated,Annotation,ConstrainedAnnotation,OptionalAnnotation,SequencedAnnotation
//#COMPILE modules.js,meta/,meta/model/,ClassOrInterface_addons.js,Member_addons.js,AppliedAttribute.js,meta/declaration/,NestableDeclaration_addons.js,GenericDeclaration_addons.js,declaration_addons.js,OpenSetter.js,FunParamDecl.js,ValParamDecl.js,metamodel_functions_core.js,metamodel_functions2.js,metamodel_functions3.js,metamodel_functions_ocoi.js,metamodel_functions5.js,metamodel_functions_coi.js,metamodel_functions_funs.js,metamodel_functions_anns.js
//Serialization
//#COMPILE serialization/,jsint/
//#COMPILE module,package

function NatErr(e) {
    var that = new NatErr.$$;
    var msg;
    if (typeof e === 'string') {
        msg = e;
    } else if (e) {
        msg = e.toString();
    } else {
        msg = "Native JavaScript Error";
    }
    Throwable(msg,null,that);
    return that;
}
initTypeProto(NatErr, 'ceylon.language::NativeError', $init$Throwable());
NatErr.$crtmm$=function(){return{nm:'NativeError',mt:'c',ps:[{$t:{t:Throwable},nm:'src',mt:'prm'}],pa:1,mod:$CCMM$,d:['$','Throwable']};}
ex$.Nothing=Nothing;
ex$.getTrue=getTrue;
ex$.getFalse=getFalse;
ex$.NatErr=NatErr;
//#UNSHARED
});
}(typeof define==='function' && define.amd ? 
    define : function (factory) {
    if (typeof exports!=='undefined') {
        factory(require, exports, module);
    } else {
        throw "no module loader";
    }
}));
