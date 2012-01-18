package com.redhat.ceylon.compiler.loader.model;

import com.redhat.ceylon.compiler.typechecker.model.Value;

/**
 * Marker class to be able to mark class attributes that are not JavaBean properties
 * but simple fields. Used for Java interoperability only.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class FieldValue extends Value {

}
