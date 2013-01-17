package com.redhat.ceylon.compiler.typechecker.model;

import java.util.List;

public interface Annotated {

    List<Annotation> getAnnotations();

}