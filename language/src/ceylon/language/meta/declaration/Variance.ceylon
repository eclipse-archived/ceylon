/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"Variance information."
shared interface Variance of invariant | covariant | contravariant {}

"Invariant means that neither subtype nor supertype can be accepted, the
 type has to be exactly that which is declared."
shared object invariant satisfies Variance {
    string => "Invariant";
}

"Covariant means that subtypes of the given type may be returned."
shared object covariant satisfies Variance {
    string => "Covariant";
}

"Contravariant means that supertypes of the given type may be accepted."
shared object contravariant satisfies Variance {
    string => "Contravariant";
}
