function OpenFunction$jsint(pkg,meta,that){
    if (meta===undefined)throw Exception("Function reference not found. Metamodel doesn't work with modules compiled in lexical scope style");
    $init$OpenFunction$jsint();
    if (that===undefined)that=new OpenFunction$jsint.$$;
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
