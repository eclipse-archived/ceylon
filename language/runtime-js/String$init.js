initExistingType($_String, String, 'ceylon.language::String', $_Object, $init$SearchableList(), $init$Comparable(),
        $init$Ranged(), $init$Summable());
inheritProto($_String, $_Object, SearchableList, Comparable, Ranged, Summable);
var origStrToString = String.prototype.toString;
var str$proto = $_String.$$.prototype;
str$proto.$$targs$$={Element$Iterable:{t:Character}, Absent$Iterable:{t:Null},
  Element$List:{t:Character}, Other$Summable:{t:$_String}, Other$Comparable:{t:$_String},
  Index$Ranged:{t:Integer}, Element$Ranged:{t:Character}, Subrange$Ranged:{t:$_String},
  Element$Collection:{t:Character}, Key$Correspondence:{t:Integer}, Item$Correspondence:{t:Character}};
str$proto.getT$name = function() {
    return $_String.$$.T$name;
}
str$proto.getT$all = function() {
    return $_String.$$.T$all;
}
str$proto.toString = origStrToString;
