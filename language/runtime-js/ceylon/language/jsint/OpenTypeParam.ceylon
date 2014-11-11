import ceylon.language.meta.declaration {
  TypeParameter, OpenType, Variance, NestableDeclaration
}

shared native class OpenTypeParam(cntnr, _fname) satisfies TypeParameter {
    shared Object cntnr;
    shared actual native NestableDeclaration container;
    shared String _fname;
    shared native actual Boolean defaulted;
    shared native actual OpenType? defaultTypeArgument;
    shared native actual Variance variance;
    shared native actual OpenType[] satisfiedTypes;
    shared native actual OpenType[] caseTypes;
    shared native actual Boolean equals(Object other);
    shared native actual String qualifiedName;
    name=_fname[0:(_fname.firstOccurrence('$') else _fname.size)];
    shared actual String string=>"given " + qualifiedName;
    shared actual Integer hash =>string.hash;
}
