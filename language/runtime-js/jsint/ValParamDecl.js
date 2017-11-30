function ValParamDecl$jsint(cont,param,vpd$){
  $init$ValParamDecl$jsint();
  if (vpd$===undefined)vpd$=new ValParamDecl$jsint.$$;
  vpd$.cont_=cont;
  vpd$.param_=param;
  OpenValue$jsint(cont.containingPackage,param,vpd$);
  if (is$(cont,{t:ClassDeclaration$meta$declaration})) {
    vpd$.at$=cont.getMemberDeclaration(vpd$.tipo.$crtmm$.nm,{Kind$getMemberDeclaration:{t:ValueDeclaration$meta$declaration}});
  }
  return vpd$;
}
