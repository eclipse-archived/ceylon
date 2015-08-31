function(other) {
  if (is$(other, {t:OpenValueConstructor$jsint})) {
    if (other.tipo===this.tipo)return true;
    return this.container.equals(other.container) &&
      this.name.equals(other.name);
  }
  return false;
}
