/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"The result of a comparison between two [[Comparable]] 
 objects: [[larger]], [[smaller]], or [[equal]]."
see (interface Comparable)
by ("Gavin")
tagged("Comparisons")
shared abstract class Comparison(shared actual String string) 
        of larger | smaller | equal {
    "The reversed value of this comparison."
    since("1.2.0")
    shared formal Comparison reversed;
}

"The value is exactly equal to the given value."
tagged("Comparisons")
shared object equal extends Comparison("equal") {
    reversed => this;
}

"The value is smaller than the given value."
tagged("Comparisons")
shared object smaller extends Comparison("smaller") {
    reversed => larger;
}

"The value is larger than the given value."
tagged("Comparisons")
shared object larger extends Comparison("larger") {
    reversed => smaller;
}
