/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"Thrown when a mathematical operation caused a number to 
 overflow from its bounds, or when a numeric narrowing
 conversion fails because the number cannot be represented
 within the bounds of the narrower type.
 
 Note that:
 
 - arithmetic operations on [[Integer]]s result in _silent_ 
   overflow (JVM) or loss of precision (JavaScript), and 
   thus never result in an `OverflowException`, and, 
 - likewise, arithmetic operations on [[Float]] produce the 
   value [[infinity]] instead of overflowing."
shared class OverflowException(String message="Numeric overflow")
        extends Exception(message) {}