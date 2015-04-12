var ucstr = codepointToString(this.value).toUpperCase();
return countCodepoints(ucstr)==1 ? Character(codepointFromString(ucstr, 0)) : this;

