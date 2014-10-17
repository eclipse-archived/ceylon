if (this.state_===undefined)return "DeserializableReference("+this.id.string+")";
switch(this.state_) {
  case 1:return "RealizableReference(" + this.id.string + "~>~" + this.deconstructed.string + ")";
  case 2:return "RealizableReference(" + this.id.string + "~>" + this.deconstructed.string + ")";
  case 3:return "RealizableReference(" + this.id.string + "->" + this.instance.string + ")";
}
throw AssertionError("Illegal state " + this.state_);
