/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"A singleton [[Tuple]] with the given [[element]] if the 
 given element is non-null, or the [[empty sequence|Empty]] 
 otherwise. This operation transforms an optional type `T?`
 to a sequence type `[]|[T]` allowing optional values to be
 the subject of operations defined for [[streams|Iterable]].
 
 For example, [[flat mapping|Iterable.flatMap]] 
 `emptyOrSingleton()` reproduces the behavior of 
 [[Iterable.coalesced]]. The expression
 
     { \"1.23\", \"foo\", \"5.67\", \"-1\", \"\" }
             .map(parseFloat)
             .flatMap(emptyOrSingleton)
 
 produces the stream:
  
     { 1.23, 5.67, -1.0 }"
see (class Tuple, interface Empty)
tagged("Sequences")
shared []|[Element&Object] emptyOrSingleton<Element>
        (Element element)
        => if (exists element) then [element] else [];