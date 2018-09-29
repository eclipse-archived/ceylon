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
shared class Comparison 
        of larger | smaller | equal {
	
    shared actual String string;
    
    "The value is exactly equal to the given value."
    since("1.4.0")
    shared new equal {
        string => "equal";
    }
    
    "The value is smaller than the given value."
    since("1.4.0")
    shared new smaller {
        string => "smaller";
    }
    
    "The value is larger than the given value."
    since("1.4.0")
    shared new larger {
        string => "larger";
    }
    
    "The reversed value of this comparison."
    since("1.2.0")
    shared Comparison reversed
    		=> switch (this)
    		case (equal) equal
    		case (smaller) larger
    		case (larger) smaller;

}

"The value is exactly equal to the given value."
tagged("Comparisons")
deprecated("Use [[Comparison.equal]]")
shared Comparison equal => Comparison.equal;

"The value is smaller than the given value."
tagged("Comparisons")
deprecated("Use [[Comparison.smaller]]")
shared Comparison smaller => Comparison.smaller;

"The value is larger than the given value."
tagged("Comparisons")
deprecated("Use [[Comparison.larger]]")
shared Comparison larger => Comparison.larger;
