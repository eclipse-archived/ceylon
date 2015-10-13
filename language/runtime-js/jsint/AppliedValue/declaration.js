var $$av=this;
var mm = $$av.tipo.$crtmm$;
var _m = typeof(mm.mod)==='function'?mm.mod():mm.mod;
var _pkg = modules$meta().find(_m['$mod-name'],_m['$mod-version']).findPackage(mm.d[0]);
var mdl = get_model(mm);
return (mdl.mt==='g'||mdl.mt==='o'?OpenValue$jsint:OpenReference$jsint)(_pkg, $$av.tipo);
