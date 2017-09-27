package org.eclipse.ceylon.langtools.tools.javac.processing.wrappers;

import java.io.IOException;

import org.eclipse.ceylon.javax.annotation.processing.Filer;
import org.eclipse.ceylon.javax.annotation.processing.FilerException;

public class FilerFacade implements javax.annotation.processing.Filer {

    private Filer f;

    public FilerFacade(Filer f) {
        this.f = f;
    }

    @Override
    public javax.tools.JavaFileObject createClassFile(CharSequence arg0, javax.lang.model.element.Element... arg1) throws IOException {
        try{
            return Facades.facade(f.createClassFile(arg0, Facades.unfacade(arg1)));
        }catch(FilerException x){
            throw Facades.facade(x);
        }
    }

    @Override
    public javax.tools.FileObject createResource(javax.tools.JavaFileManager.Location arg0, CharSequence arg1, CharSequence arg2, javax.lang.model.element.Element... arg3) throws IOException {
        try{
            return Facades.facade(f.createResource(Facades.unfacade(arg0), arg1, arg2, Facades.unfacade(arg3)));
        }catch(FilerException x){
            throw Facades.facade(x);
        }
    }

    @Override
    public javax.tools.JavaFileObject createSourceFile(CharSequence arg0, javax.lang.model.element.Element... arg1) throws IOException {
        try{
            return Facades.facade(f.createSourceFile(arg0, Facades.unfacade(arg1)));
        }catch(FilerException x){
            throw Facades.facade(x);
        }
    }

    @Override
    public javax.tools.FileObject getResource(javax.tools.JavaFileManager.Location arg0, CharSequence arg1, CharSequence arg2) throws IOException {
        try{
            return Facades.facade(f.getResource(Facades.unfacade(arg0), arg1, arg2));
        }catch(FilerException x){
            throw Facades.facade(x);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof FilerFacade == false)
            return false;
        return f.equals(((FilerFacade)obj).f);
    }
    
    @Override
    public int hashCode() {
        return f.hashCode();
    }
    
    @Override
    public String toString() {
        return f.toString();
    }
}
