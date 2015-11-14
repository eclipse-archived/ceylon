function(other) {
  return is$(other,{t:FreeClass$jsint}) &&
    other.declaration.equals(this.declaration) &&
    this.typeArgumentWithVariances.equals(other.typeArgumentWithVariances);
}
