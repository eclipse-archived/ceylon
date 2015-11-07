function() {
    var $idx$=0;
    var $arr$=this;
    return for$iter(function() {
        return ($idx$===$arr$.length) ? finished() : $arr$[$idx$++];
    },{Element$Iterable:this._elemTarg(),Absent$Iterable:{t:Null}});
}
