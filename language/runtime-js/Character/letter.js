//TODO: this captures only letters that have case
var str = codepointToString(this.value);
return str.toUpperCase()!==str || str.toLowerCase()!==str || (this.value in Character.titlecase$);
