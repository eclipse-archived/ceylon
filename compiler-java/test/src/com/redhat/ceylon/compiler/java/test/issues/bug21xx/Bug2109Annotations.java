/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package com.redhat.ceylon.compiler.java.test.issues.bug21xx;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@interface AnnoOnType{}

@Target(value = {ElementType.FIELD})
@interface AnnoOnField{}

@Target(value = {ElementType.METHOD})
@interface AnnoOnMethod{}

@Target(value = {ElementType.PARAMETER})
@interface AnnoOnParameter{}

@Target(value = {ElementType.CONSTRUCTOR})
@interface AnnoOnConstructor{}

@Target(value = {ElementType.LOCAL_VARIABLE})
@interface AnnoOnLocalVariable{}

@Target(value = {ElementType.ANNOTATION_TYPE})
@interface AnnoOnAnnotationType{}

@Target(value = {ElementType.PACKAGE})
@interface AnnoOnPackage{}
