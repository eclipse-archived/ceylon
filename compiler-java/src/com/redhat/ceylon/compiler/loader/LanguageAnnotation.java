package com.redhat.ceylon.compiler.loader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.redhat.ceylon.compiler.loader.mirror.AnnotationMirror;
import com.redhat.ceylon.model.typechecker.model.Annotation;

/** 
 * Enumerates the language module annotations and allows them to be loaded from mirrors
 */
public enum LanguageAnnotation {
    /* modifier annotations */
    ANNOTATION("annotation", 1L<<0, AbstractModelLoader.CEYLON_LANGUAGE_ANNOTATION_ANNOTATION),
    SHARED("shared", 1L<<1, AbstractModelLoader.CEYLON_LANGUAGE_SHARED_ANNOTATION),
    VARIABLE("variable", 1L<<2, AbstractModelLoader.CEYLON_LANGUAGE_VARIABLE_ANNOTATION),
    ABSTRACT("abstract", 1L<<3, AbstractModelLoader.CEYLON_LANGUAGE_ABSTRACT_ANNOTATION),
    SEALED("sealed", 1L<<4, AbstractModelLoader.CEYLON_LANGUAGE_SEALED_ANNOTATION),
    FINAL("final", 1L<<5, AbstractModelLoader.CEYLON_LANGUAGE_FINAL_ANNOTATION),
    ACTUAL("actual", 1L<<6, AbstractModelLoader.CEYLON_LANGUAGE_ACTUAL_ANNOTATION),
    FORMAL("formal", 1L<<7, AbstractModelLoader.CEYLON_LANGUAGE_FORMAL_ANNOTATION),
    DEFAULT("default", 1L<<8, AbstractModelLoader.CEYLON_LANGUAGE_DEFAULT_ANNOTATION),
    LATE("late", 1L<<9, AbstractModelLoader.CEYLON_LANGUAGE_LATE_ANNOTATION),
    NATIVE("native", 1L<<10, AbstractModelLoader.CEYLON_LANGUAGE_NATIVE_ANNOTATION),
    OPTIONAL("optional", 1L<<11, AbstractModelLoader.CEYLON_LANGUAGE_OPTIONAL_ANNOTATION),
    SERIALIZABLE("serializable", 1L<<12, AbstractModelLoader.CEYLON_LANGUAGE_SERIALIZABLE_ANNOTATION),
    
    /* documentation annotations */
    DOC("doc", 0, AbstractModelLoader.CEYLON_LANGUAGE_DOC_ANNOTATION) {
        public List<Annotation> makeFromCeylonAnnotation(AnnotationMirror mirror) {
            Annotation anno = new Annotation(name);
            anno.addPositionalArgment((String)mirror.getValue("description"));
            return Collections.singletonList(anno);
        }
    },
    THROWS("throws", 0, AbstractModelLoader.CEYLON_LANGUAGE_THROWS_ANNOTATIONS) {
        public List<Annotation> makeFromCeylonAnnotation(AnnotationMirror mirror) {
            List<AnnotationMirror> thrownExceptions = (List<AnnotationMirror>)mirror.getValue("value");
            List<Annotation> result = new ArrayList<Annotation>(thrownExceptions.size());
            for (AnnotationMirror thrown : thrownExceptions) {
                Annotation anno = new Annotation(name);
                // can't decode the declaration
                anno.addPositionalArgment(parseMetamodelReference((String)thrown.getValue("type")));
                anno.addPositionalArgment((String)thrown.getValue("when"));
                result.add(anno);
            }
            return result;
        }
    },
    BY("by", 0, AbstractModelLoader.CEYLON_LANGUAGE_AUTHORS_ANNOTATION) {
        public List<Annotation> makeFromCeylonAnnotation(AnnotationMirror mirror) {
            Annotation anno = new Annotation(name);
            for (String author : (List<String>)mirror.getValue("authors")) {
                anno.addPositionalArgment(author);
            }
            return Collections.singletonList(anno);
        }
    },
    SEE("see", 0, AbstractModelLoader.CEYLON_LANGUAGE_SEE_ANNOTATIONS) {
        public List<Annotation> makeFromCeylonAnnotation(AnnotationMirror mirror) {
            List<AnnotationMirror> sees = (List<AnnotationMirror>)mirror.getValue("value");
            List<Annotation> result = new ArrayList<Annotation>(sees.size());
            for (AnnotationMirror see : sees) {
                Annotation anno = new Annotation(name);
                // can't decode the declaration
                for (String s : (List<String>)see.getValue("programElements")) {
                    anno.addPositionalArgment(parseMetamodelReference(s));
                }
                result.add(anno);
            }
            return result;
        }

    },
    LICENSE("license", 0, AbstractModelLoader.CEYLON_LANGUAGE_LICENSE_ANNOTATION) {
        public List<Annotation> makeFromCeylonAnnotation(AnnotationMirror mirror) {
            Annotation anno = new Annotation(name);
            anno.addPositionalArgment((String)mirror.getValue("description"));
            return Collections.singletonList(anno);
        }
    },
    DEPRECATED("deprecated", 0, AbstractModelLoader.CEYLON_LANGUAGE_DEPRECATED_ANNOTATION) {
        public List<Annotation> makeFromCeylonAnnotation(AnnotationMirror mirror) {
            Annotation anno = new Annotation(name);
            anno.addPositionalArgment((String)mirror.getValue("description"));
            return Collections.singletonList(anno);
        }
    },
    TAGGED("tagged", 0, AbstractModelLoader.CEYLON_LANGUAGE_TAGS_ANNOTATION) {
        public List<Annotation> makeFromCeylonAnnotation(AnnotationMirror mirror) {
            Annotation anno = new Annotation(name);
            for (String tag : (List<String>)mirror.getValue("tags")) {
                anno.addPositionalArgment(tag);
            }
            return Collections.singletonList(anno);
        }
    },
    SUPPRESS_WARNINGS("suppressWarnings", 0, AbstractModelLoader.CEYLON_LANGUAGE_SUPPRESS_WARNINGS_ANNOTATION) {
        public List<Annotation> makeFromCeylonAnnotation(AnnotationMirror mirror) {
            Annotation anno = new Annotation(name);
            for (String tag : (List<String>)mirror.getValue("warnings")) {
                anno.addPositionalArgment(tag);
            }
            return Collections.singletonList(anno);
        }
    };
    
    /**
     * The unqualified Ceylon name of the annotation, e.g. {@code shared}
     */
    public final String name;
    
    /** 
     * The mask used to address this modifier when encoded as a long. 
     * zero if this annotation is not a model annotation 
     */
    public final long mask;
    /**
     * The qualifier Java name of the annotation type, e.g. {@code ceylon.language.Shared$annotation$}
     */
    public final String annotationType;
    
    LanguageAnnotation(String name, long mask, String annotationType) {
        this.name = name;
        this.mask = mask;
        this.annotationType = annotationType;
    }
    
    /** 
     * Whether this annotation is a <em>modifier annotation</em> 
     * (i.e. one whose annotation class is nullary, and 
     * therefore whose presence/absence is all that matters) 
     */
    public boolean isModifier() {
        return mask != 0;
    }
    
    /**
     * Construct a Ceylon annotation model from the information stored in 
     * the mirror of the Ceylon annotation. 
     * @param mirror The mirror of the Ceylon annotation.
     * @return
     */
    public List<Annotation> makeFromCeylonAnnotation(AnnotationMirror mirror) {
        return Collections.singletonList(new Annotation(name));
    }
    
    protected String parseMetamodelReference(String s) {
        // Ugly hack to save us having to actually parse the output of
        // ExpressionTransfomer.serializeReferenceable() in order to get the 
        // unqualified name
        return s.replaceAll("^.*::[CIAVFP]", "").replaceAll("^.*\\.[CIAVFP]", "");
    }
}
