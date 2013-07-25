/*Native Implementation of annotations() */
function annotations$metamodel(anntype, progelem, $$targs$$) {
  if (isOfType({t:OptionalAnnotation$metamodel}, progelem.tipo)) {
    return null;
  }
  return getEmpty();
}
exports.annotations$metamodel=annotations$metamodel;
