/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
since("1.1.0")
shared abstract class BaseIterable<Element,Absent>()
        extends Object()
        satisfies Iterable<Element,Absent>
        given Absent satisfies Null {}

since("1.1.0")
shared abstract class BaseIterator<Element>()
        extends Object()
        satisfies Iterator<Element> {}

since("1.1.0")
shared abstract class BaseMap<Key,Item>()
        extends Object()
        satisfies Map<Key,Item>
        given Key satisfies Object {}

since("1.1.0")
shared abstract serializable
class BaseList<Element>()
        extends Object()
        satisfies SearchableList<Element> {}

since("1.3.1")
shared abstract serializable
class BaseSet<Element>()
        extends Object()
        satisfies Set<Element> 
        given Element satisfies Object {}

since("1.1.0")
shared abstract class BaseCharacterList()
        extends Object()
        satisfies SearchableList<Character> {}

since("1.1.0")
shared abstract serializable
class BaseSequence<Element>()
        extends Object()
        satisfies [Element+] {}
