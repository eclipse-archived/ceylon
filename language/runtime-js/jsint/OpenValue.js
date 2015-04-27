function OpenValue$jsint(pkg, meta, that){
  if (meta===undefined)throw Exception("Value reference not found. Metamodel doesn't work with modules compiled in lexical scope style");
  $init$OpenValue$jsint();
  if (that===undefined)that=new OpenValue$jsint.$$;
  that.pkg_ = pkg;
  var _mm=getrtmm$$(meta);
  if (_mm === undefined) {
    //it's a metamodel
    that.meta_=meta;
    if (meta['mt']==='prm') {
      that.tipo={$crtmm$:meta};
      //TODO I think we need to do something else here
    } else {
      that.tipo=_findTypeFromModel(pkg,meta);
    }
    _mm = getrtmm$$(that.tipo);
  } else {
    //it's a type
    that.tipo = meta;
    that.meta_ = get_model(_mm);
  }
  var nm=that.meta?that.meta.nm:_mm&&_mm.d&&_mm.d[_mm.d.length-1];
  if (nm && nm.indexOf('$')>0) {
    nm=nm.substring(0,nm.indexOf('$'));
  }
  that.name_=nm;
  that.toplevel_=_mm.$cont === undefined;
  ValueDeclaration$meta$declaration(that);
  return that;
}
