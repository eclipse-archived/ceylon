/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"Determine if the arguments are [[identical]]. Equivalent to
 `x===y`. Only instances of [[Identifiable]] have 
 well-defined identity."
see (function identityHash)
tagged("Comparisons")
shared Boolean identical(
        "An object with well-defined identity."
        Identifiable x, 
        "A second object with well-defined identity."
        Identifiable y) 
                => x===y;