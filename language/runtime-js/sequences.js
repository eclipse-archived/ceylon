function Array$(elems,$$targs$$) {
    var e=[];
    if (!(elems === null || elems === undefined)) {
        var iter=elems.iterator();
        var item;while((item=iter.next())!==getFinished()) {
            e.push(item);
        }
    }
    e.$$targs$$=$$targs$$;
    List({Element:$$targs$$.Element}, e);
    return e;
}
Array$.$$metamodel$$={$ps:[{$nm:'elements',$mt:'prm',$t:{t:Iterable,a:{Absent:{t:Null},Element:'Element'}}}],$an:function(){return[shared(),final(),native()];},mod:$$METAMODEL$$,d:['ceylon.language','Array'],
  'super':{t:Object$}, $tp:{Element:{}}, satisfies:[{t:List,a:{Element:'Element'}},{t:Cloneable,a:{Other:{t:Array$,a:{Element:'Element'}}}},
    {t:Ranged,a:{Index:{t:Integer},Span:{t:Array$,a:{Element:'Element'}}}}]};

initExistingType(Array$, Array, 'ceylon.language::Array', Object$,
        Cloneable, Ranged, $init$List());
var Array$proto = Array.prototype;
var origArrToString = Array$proto.toString;
inheritProto(Array$, Object$, Cloneable, Ranged, $init$List());
Array$proto.toString = origArrToString;
Array$proto.reifyCeylonType = function(typeParameters) {
    this.$$targs$$ = typeParameters;
    return this;
}
exports.Array=Array$;

function EmptyArray() {
    return [];
}
EmptyArray.$$metamodel$$=$init$Empty().$$metamodel$$;
initTypeProto(EmptyArray, 'ceylon.language::EmptyArray', Array$);
function ArrayList(items) {
    return items;
}
ArrayList.$$metamodel$$=Array$.$$metamodel$$;
initTypeProto(ArrayList, 'ceylon.language::ArrayList', Array$, $init$List());
function ArraySequence(/* js array */value, $$targs$$) {
    value.$seq = true;
    value.$$targs$$=$$targs$$;
    this.$$targs$$=$$targs$$;
    return value;
}
ArraySequence.$$metamodel$$=function(){return{mod:$$METAMODEL$$,d:['ceylon.language','ArraySequence'],$ps:[{$nm:'elements',$t:{t:Sequence,a:{Element:'Element'}}}],$tp:{Element:{'var':'out'}},satisfies:[{t:Sequence,a:{Element:'Element'}}]};};
initTypeProto(ArraySequence, 'ceylon.language::ArraySequence', $init$Basic(), $init$Sequence());
Array$proto.getT$name = function() {
    return (this.$seq ? ArraySequence : (this.length>0?ArrayList:EmptyArray)).$$.T$name;
}
Array$proto.getT$all = function() {
    return (this.$seq ? ArraySequence : (this.length>0?ArrayList:EmptyArray)).$$.T$all;
}

exports.EmptyArray=EmptyArray;

defineAttr(Array$proto, 'size', function(){ return this.length; },undefined,function(){return{mod:$$METAMODEL$$,d:['ceylon.language','Iterable','$at','size'],$cont:Array$proto,$t:{t:Integer}};});
defineAttr(Array$proto,'string',function(){
    return (opt$181=(this.empty?String$("[]",2):null),opt$181!==null?opt$181:StringBuilder().appendAll([String$("[",1),commaList(this).string,String$("]",1)]).string);
},undefined,function(){return{mod:$$METAMODEL$$,d:['ceylon.language','Object','$at','string'],$t:{t:String},$cont:Array$proto};});
Array$proto.set = function(idx,elem) {
    if (idx >= 0 && idx < this.length) {
        this[idx] = elem;
    }
}
Array$proto.set.$$metamodel$$=function(){return{mod:$$METAMODEL$$,d:['ceylon.language','Array','$m','set'],$t:{t:Anything},$ps:[{$nm:'index',$t:{t:Integer},$mt:'prm'},{$nm:'element',$mt:'prm',$t:'Element'}]};}
Array$proto.$get = function(idx) {
    var result = this[idx];
    return result!==undefined ? result:null;
}
Array$proto.$get.$$metamodel$$=function(){
  return{mod:$$METAMODEL$$,d:['ceylon.language','List','$m','get'],$t:{t:'u',l:[{t:Null},'Element']},$ps:[{$nm:'index',$t:{t:Integer},$mt:'prm'}]};
}
defineAttr(Array$proto, 'lastIndex', function() {
    return this.length>0 ? (this.length-1) : null;
},undefined,function(){return{mod:$$METAMODEL$$,d:['ceylon.language','List','$at','lastIndex'],$t:{t:'u',l:[{t:Null},{t:Integer}]}};});
defineAttr(Array$proto, 'reversed', function() {
    if (this.length === 0) { return this; }
    var arr = this.slice(0);
    arr.reverse();
    return this.$seq ? ArraySequence(arr,this.$$targs$$) : arr.reifyCeylonType(this.$$targs$$);
},undefined,function(){return{mod:$$METAMODEL$$,d:['ceylon.language','List','$at','reversed'],$t:{t:List,a:{Element:'Element'}}};});
Array$proto.chain = function(other, $$$mptypes) {
    if (this.length === 0) { return other; }
    return Iterable.$$.prototype.chain.call(this, other, $$$mptypes);
}
defineAttr(Array$proto, 'first', function(){ return this.length>0 ? this[0] : null; },
  undefined,function(){return{mod:$$METAMODEL$$,d:['ceylon.language','Iterable','$at','first'],$t:{t:'u',l:[{t:Null},'Element']}};});
defineAttr(Array$proto, 'last', function() { return this.length>0 ? this[this.length-1] : null; },
  undefined,function(){return{mod:$$METAMODEL$$,d:['ceylon.language','List','$at','last'],$t:{t:'u',l:[{t:Null},'Element']}};});
Array$proto.segment = function(from, len) {
    if (len <= 0) { return getEmpty(); }
    var stop = from + len;
    var seq = this.slice((from>=0)?from:0, (stop>=0)?stop:0);
    return (seq.length > 0) ? ArraySequence(seq,this.$$targs$$) : getEmpty();
}
Array$proto.span = function(from, to) {
    if (from > to) {
        var arr = this.segment(to, from-to+1);
        arr.reverse();
        return arr.reifyCeylonType(this.$$targs$$);
    }
    return this.segment(from, to-from+1);
}
Array$proto.spanTo = function(to) {
    return to < 0 ? getEmpty() : this.span(0, to);
}
Array$proto.spanFrom = function(from) {
    return this.span(from, 0x7fffffff);
}
defineAttr(Array$proto, 'rest', function() {
    return this.length<=1 ? getEmpty() : ArraySequence(this.slice(1),this.$$targs$$);
});
Array$proto.items = function(keys) {
    if (keys === undefined) return getEmpty();
    var seq = [];
    for (var i = 0; i < keys.size; i++) {
        var key = keys.$get(i);
        seq.push(this.$get(key));
    }
    return ArraySequence(seq,this.$$targs$$);
}
defineAttr(Array$proto, 'keys', function(){ return TypeCategory(this, {t:Integer}); });
Array$proto.contains = function(elem) {
    for (var i=0; i<this.length; i++) {
        if (elem.equals(this[i])) {
            return true;
        }
    }
    return false;
}
Array$proto.iterator = function() {
    var $$$index$$$ = 0;
    var $$$arr$$$ = this;
    return new ComprehensionIterator(function() {
        return ($$$index$$$ === $$$arr$$$.length) ? getFinished() : $$$arr$$$[$$$index$$$++];
    }, this.$$targs$$);
}
Array$proto.copyTo = function(other, srcpos, dstpos, length) {
    if (length === undefined) length = this.size;
    if (srcpos === undefined) srcpos = 0;
    if (dstpos === undefined) dstpos = 0;
    var endpos = srcpos+length;
    //TODO validate range?
    for (var i=srcpos; i<endpos; i++) {
        other[dstpos]=this[i];
        dstpos++;
    }
}
Array$proto.shorterThan = function(len) {
  return this.size < len;
}
Array$proto.shorterThan.$$metamodel$$={mod:$$METAMODEL$$,d:['ceylon.language','Iterable','$m','shorterThan']};
Array$proto.longerThan = function(len) {
  return this.size > len;
}
Array$proto.longerThan.$$metamodel$$={mod:$$METAMODEL$$,d:['ceylon.language','Iterable','$m','longerThan']};

exports.ArrayList=ArrayList;
exports.arrayOfSize=function(size, elem, $$$mptypes) {
    if (size > 0) {
        var elems = [];
        for (var i = 0; i < size; i++) {
            elems.push(elem);
        }
        elems.$$targs$$=$$$mptypes;
        return elems;
    } else return [];
}
exports.arrayOfSize.$$metamodel$$={$an:function(){return[shared()];},mod:$$METAMODEL$$,d:['ceylon.language','arrayOfSize']};

function TypeCategory(seq, type) {
    var that = new TypeCategory.$$;
    that.type = type;
    that.seq = seq;
    return that;
}
initTypeProto(TypeCategory, 'ceylon.language::TypeCategory', $init$Basic(), Category);
var TypeCategory$proto = TypeCategory.$$.prototype;
TypeCategory$proto.contains = function(k) {
    return isOfType(k, this.type) && this.seq.defines(k);
}

function SequenceBuilder($$targs$$) {
    var that = new SequenceBuilder.$$;
    that.seq = [];
    that.$$targs$$=$$targs$$;
    return that;
}
SequenceBuilder.$$metamodel$$={$ps:[],$an:function(){return[shared()];},mod:$$METAMODEL$$,d:['ceylon.language','SequenceBuilder']};

initTypeProto(SequenceBuilder, 'ceylon.language::SequenceBuilder', $init$Basic());
var SequenceBuilder$proto = SequenceBuilder.$$.prototype;
defineAttr(SequenceBuilder$proto, 'sequence', function() {
    return (this.seq.length > 0) ? ArraySequence(this.seq,this.$$targs$$) : getEmpty();
});
SequenceBuilder$proto.append = function(e) { this.seq.push(e); }
SequenceBuilder$proto.appendAll = function(/*Iterable*/arr) {
    if (arr === undefined) return;
    var iter = arr.iterator();
    var e; while ((e = iter.next()) !== getFinished()) {
        this.seq.push(e);
    }
}
defineAttr(SequenceBuilder$proto, 'size', function(){ return this.seq.length; });

function SequenceAppender(other, $$targs$$) {
    var that = new SequenceAppender.$$;
    that.seq = [];
    that.$$targs$$=$$targs$$;
    that.appendAll(other);
    return that;
}
SequenceAppender.$$metamodel$$={$ps:[],$an:function(){return[shared()];},mod:$$METAMODEL$$,d:['ceylon.language','SequenceAppender']};

initTypeProto(SequenceAppender, 'ceylon.language::SequenceAppender', SequenceBuilder);

exports.Sequence=Sequence;
exports.SequenceBuilder=SequenceBuilder;
exports.SequenceAppender=SequenceAppender;
exports.ArraySequence=ArraySequence;
