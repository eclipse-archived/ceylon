

package org.eclipse.ceylon.tools.p2;


class Category {
    final String name, label, description;
    Feature feature;
    boolean allJars;
    
    Category(String name, String label, String description){
        this.name = name;
        this.label = label;
        this.description = description;
    }
    void setFeature(Feature feature){
        this.feature = feature;
    }
    void setAllJars() {
        this.allJars = true;
    }
}