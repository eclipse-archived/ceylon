function OpenClassWithInitializer$jsint(pkg, meta, that){
    if (meta===undefined)throw new Error("Class reference not found. Metamodel doesn't work with modules compiled in lexical scope style");
    $init$OpenClassWithInitializer$jsint();
    if (that===undefined)that=new OpenClassWithInitializer$jsint.$$;
    that.pkg_ = pkg;
    var _mm=getrtmm$$(meta);
    if (_mm === undefined) {
      //it's a metamodel
      that.meta_=meta;
      that.tipo=_findTypeFromModel(pkg,meta);
      _mm = getrtmm$$(that.tipo);
    } else {
      //it's a type
      that.tipo = meta;
      that.meta_ = get_model(_mm);
    }
    var nm=(that.meta&&that.meta.nm)||_mm.d[_mm.d.length-1];
    if (nm && nm.indexOf('$')>0) {
      nm=nm.substring(0,nm.indexOf('$'));
    }
    that.name_=nm;
    that.toplevel_=_mm.$cont===undefined;
    ClassWithInitializerDeclaration$meta$declaration(that);
    return that;
}
