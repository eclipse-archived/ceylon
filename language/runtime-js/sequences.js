function initTypeProto(a,b,c,d,e,f,g,h,i,j,k,l);//IGNORE
function String$(x){}//IGNORE
function Character(x){}//IGNORE
function isOfType(a,b){}//IGNORE
function smallest(x,y){}//IGNORE
function largest(x,y){}//IGNORE
function Exception(){}//IGNORE
var List,Cloneable,Ranged,exports,larger,smaller,equal,Object$,Iterator;//IGNORE
var Category;//IGNORE

function Array$() {
    var that = new Array$.$$;
    List(that);
    return that;
}
Array$.$$metamodel$$={$nm:'Array',$mt:'cls',$ps:[],$an:function(){return[shared(),abstract(),native()];}};

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
initTypeProto(EmptyArray, 'ceylon.language::EmptyArray', Array$);
function ArrayList(items) {
    return items;
}
initTypeProto(ArrayList, 'ceylon.language::ArrayList', Array$, $init$List());
function ArraySequence(/* js array */value, $$targs$$) {
    value.$seq = true;
    value.$$targs$$=$$targs$$;
    this.$$targs$$=$$targs$$;
    return value;
}
initTypeProto(ArraySequence, 'ceylon.language::ArraySequence', $init$Basic(), $init$Sequence());
Array$proto.getT$name = function() {
    return (this.$seq ? ArraySequence : (this.length>0?ArrayList:EmptyArray)).$$.T$name;
}
Array$proto.getT$all = function() {
    return (this.$seq ? ArraySequence : (this.length>0?ArrayList:EmptyArray)).$$.T$all;
}

exports.EmptyArray=EmptyArray;

defineAttr(Array$proto, 'size', function(){ return this.length; });
defineAttr(Array$proto,'string',function(){
    return (opt$181=(this.empty?String$("[]",2):null),opt$181!==null?opt$181:StringBuilder().appendAll([String$("[",1),commaList(this).string,String$("]",1)]).string);
});
Array$proto.set = function(idx,elem) {
    if (idx >= 0 && idx < this.length) {
        this[idx] = elem;
    }
}
Array$proto.get = function(idx) {
    var result = this[idx];
    return result!==undefined ? result:null;
}
defineAttr(Array$proto, 'lastIndex', function() {
    return this.length>0 ? (this.length-1) : null;
});
defineAttr(Array$proto, 'reversed', function() {
    if (this.length === 0) { return this; }
    var arr = this.slice(0);
    arr.reverse();
    return this.$seq ? ArraySequence(arr,this.$$targs$$) : arr.reifyCeylonType(this.$$targs$$);
});
Array$proto.chain = function(other, $$$mptypes) {
    if (this.length === 0) { return other; }
    return Iterable.$$.prototype.chain.call(this, other, $$$mptypes);
}
defineAttr(Array$proto, 'first', function(){ return this.length>0 ? this[0] : null; });
defineAttr(Array$proto, 'last', function() { return this.length>0 ? this[this.length-1] : null; });
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
        var key = keys.get(i);
        seq.push(this.get(key));
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

exports.ArrayList=ArrayList;
exports.array=function(elems, $$$ptypes) {
    var e=[];
    if (!(elems === null || elems === undefined)) {
        var iter=elems.iterator();
        var item;while((item=iter.next())!==getFinished()) {
            e.push(item);
        }
    }
    e.$$targs$$=$$$ptypes;
    return e;
}
exports.array.$$metamodel$$={$nm:'array',$mt:'mthd',$an:function(){return[shared()];}};

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
exports.arrayOfSize.$$metamodel$$={$nm:'arrayOfSize',$mt:'mthd',$an:function(){return[shared()];}};

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
SequenceBuilder.$$metamodel$$={$nm:'SequenceBuilder',$mt:'cls',$ps:[],$an:function(){return[shared()];}};

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
SequenceAppender.$$metamodel$$={$nm:'SequenceAppender',$mt:'cls',$ps:[],$an:function(){return[shared()];}};

initTypeProto(SequenceAppender, 'ceylon.language::SequenceAppender', SequenceBuilder);

exports.Sequence=Sequence;
exports.SequenceBuilder=SequenceBuilder;
exports.SequenceAppender=SequenceAppender;
exports.ArraySequence=ArraySequence;
