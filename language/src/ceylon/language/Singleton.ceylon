/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"A [[Sequence]] with exactly one [[element]], which may be 
 null."
tagged("Sequences")
shared final serializable class Singleton<out Element>
        (Element element)
        extends Object()
        satisfies [Element+] {
    
    "Returns the element contained in this `Singleton`."
    shared actual Element first => element;
    
    "Returns the element contained in this `Singleton`."
    shared actual Element last => element;
    
    "Returns `0`."
    shared actual Integer lastIndex => 0;
    
    "Returns `1`."
    shared actual Integer size => 1;
    
    "Returns `Empty`."
    shared actual Empty rest => [];
    
    "Returns the contained element, if the specified 
     index is `0`."
    shared actual Element? getFromFirst(Integer index)
            => if (index == 0) then element else null;
    
    "Return this singleton."
    shared actual Singleton<Element> reversed => this;
    
    "Returns a `Singleton` with the same element."
    shared actual 
    Singleton<Element> clone() => this;
    
    "Returns `true` if the specified element is this 
     `Singleton`\'s element."
    shared actual Boolean contains(Object element)
            => if (exists e = this.element) 
            then e == element
            else false;
    
    string => "[``stringify(element)``]";
    
    shared actual Iterator<Element> iterator()
            => object 
            satisfies Iterator<Element> {
        variable Boolean done = false;
        shared actual Element|Finished next() {
            if (done) {
                return finished;
            }
            else {
                done = true;
                return element;
            }
        }
        string => "``outer.string``.iterator()";
    };
    
    "A `Singleton` can be equal to another `List` if 
     that `List` has only one element which is equal to 
     this `Singleton`\'s element."
    shared actual Boolean equals(Object that) {
        if (is List<Anything> that, !is String that,
            that.size == 1) {
            value elem = that.first;
            return if (exists element, exists elem) 
            then elem == element
            else !element exists && !elem exists;
        }
        else {
            return false;
        }
    }
    
    shared actual Integer hash 
            => 31 + (element?.hash else 0);
    
    "Returns a `Singleton` if the given starting index 
     is `0` and the given `length` is greater than `0`.
     Otherwise, returns an instance of `Empty`."
    shared actual 
    []|Singleton<Element> measure
            (Integer from, Integer length)
            => from <= 0 && from + length > 0 
            then this else [];
    
    "Returns a `Singleton` if the given starting index 
     is `0`. Otherwise, returns an instance of `Empty`."
    shared actual 
    []|Singleton<Element> span
            (Integer from, Integer to)
            => from <= 0 && to >= 0 ||
               from >= 0 && to <= 0
            then this else [];
    
    shared actual 
    []|Singleton<Element> spanTo(Integer to) 
            => to < 0 then [] else this;
    
    shared actual 
    []|Singleton<Element> spanFrom(Integer from) 
            => from > 0 then [] else this;
    
    shared actual
    []|Singleton<Element> terminal(Integer length) 
            => length>0 then this else [];
    
    shared actual
    []|Singleton<Element> initial(Integer length) 
            => length>0 then this else [];
    
    shared actual 
    [[],Singleton<Element>] | [Singleton<Element>,[]]
    slice(Integer index) 
            => index>0 then [this,[]] else [[],this];
    
    "Returns `1` if this `Singleton`\'s element
     satisfies the predicate, or `0` otherwise."
    shared actual 
    Integer count(Boolean selecting(Element element))
            => selecting(element) then 1 else 0;
    
    shared actual 
    Singleton<Result> map<Result>
            (Result collecting(Element e))
            => Singleton(collecting(element));
    
    shared actual 
    Singleton<Element>|[] filter
            (Boolean selecting(Element e))
            => selecting(element) then this else [];
    
    shared actual 
    Result fold<Result>(Result initial,
            Result accumulating(Result partial, 
                                 Element e))
            => accumulating(initial, element);
    
    shared actual 
    Element reduce<Result>
            (Result accumulating(Result|Element partial, 
                                 Element e))
            => element;
    
    shared actual 
    Singleton<Result> collect<Result>
            (Result collecting(Element element))
            => Singleton(collecting(element));
    
    shared actual 
    Singleton<Element>|[] select
            (Boolean selecting(Element element))
            => selecting(element) then this else [];
    
    shared actual 
    Element? find(Boolean selecting(Element&Object e))
            => if (exists element, selecting(element))
                    then element else null;
    
    shared actual 
    Element? findLast(Boolean selecting(Element&Object e))
            => find(selecting);
    
    shared actual 
    Singleton<Element> sort
            (Comparison comparing(Element a, Element b))
            => this;
    
    shared actual 
    Boolean any(Boolean selecting(Element e))
            => selecting(element);
    
    shared actual 
    Boolean every(Boolean selecting(Element e))
            => selecting(element);
    
    shared actual 
    Singleton<Element>|Empty skip(Integer skipping)
            => skipping < 1 then this else [];
    
    shared actual 
    Singleton<Element>|Empty take(Integer taking)
            => taking > 0 then this else [];
    
    shared actual 
    Singleton<Element&Object>|Empty coalesced
            => if (exists element)
                    then Singleton(element) else [];
    
    shared actual 
    {Element|Other+} chain<Other,OtherAbsent>
            (Iterable<Other,OtherAbsent> other)
            given OtherAbsent satisfies Null
            => other.follow(element);
    
    each(void step(Element element)) => step(element);
    
    indexed => { 0->element };
    
    "A stream with given [[head]], followed by the [[element]]
     of this singleton.
     
     For example, the expression
     
         Singleton(1).follow(2)
     
     evaluates to the stream `{ 2, 1 }`."
    shared actual {Other|Element+} follow<Other>(Other head) 
            => { head, element };
    
    "An infinite stream that produces the [[element]] of 
     this singleton, repeatedly.
     
     For example, the expression
     
         Singleton(null).cycled.take(4)
     
     evaluates to the stream `{ null, null, null, null }`."
    see (function repeat)
    shared actual 
    {Element+} cycled 
            => object satisfies {Element+} {
        string => outer.string + ".cycled";
        shared actual Integer size {
            "stream is infinite" 
            assert (false); 
        }
        iterator() 
                => object satisfies Iterator<Element> {
            next() => element; 
            string => outer.string + ".iterator()";
        };
    };
    
    "This singleton."
    shared actual
    Singleton<Element> interpose<Other>(
        Other element,
        Integer step) {
        "step must be strictly positive"
        assert (step>=1);
        return this;
    }
    
    "This singleton."
    shared actual Singleton<Element> distinct => this;
    
    "An empty stream."
    shared actual [] paired => [];
    
}
