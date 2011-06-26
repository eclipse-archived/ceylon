package com.redhat.ceylon.compiler.typechecker.context;

import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleBuilder;
import com.redhat.ceylon.compiler.typechecker.io.VirtualFile;
import com.redhat.ceylon.compiler.typechecker.parser.CeylonLexer;
import com.redhat.ceylon.compiler.typechecker.parser.CeylonParser;
import com.redhat.ceylon.compiler.typechecker.parser.LexError;
import com.redhat.ceylon.compiler.typechecker.parser.ParseError;
import com.redhat.ceylon.compiler.typechecker.tree.CustomBuilder;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.CommonTree;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contains phased units
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class PhasedUnits {
    private List<PhasedUnit> phasedUnits = new ArrayList<PhasedUnit>();
    private Map<VirtualFile,PhasedUnit> phasedUnitPerFile = new HashMap<VirtualFile,PhasedUnit>();
    private Map<String,PhasedUnit> phasedUnitPerRelativePath = new HashMap<String,PhasedUnit>();
    private final Context context;
    private final ModuleBuilder moduleBuilder;

    public PhasedUnits(Context context) {
        this.context = context;
        this.moduleBuilder = new ModuleBuilder(context);
        this.moduleBuilder.initCoreModules();
    }

    public void addPhasedUnit(VirtualFile unitFile, PhasedUnit phasedUnit) {
        //TODO do we need the ordering??, we could get rid of the List and use map.valueSet()
        this.phasedUnits.add(phasedUnit);
        this.phasedUnitPerFile.put(unitFile, phasedUnit);
        this.phasedUnitPerRelativePath.put( phasedUnit.getPathRelativeToSrcDir(), phasedUnit );
    }

    public ModuleBuilder getModuleBuilder() {
        return moduleBuilder;
    }

    public List<PhasedUnit> getPhasedUnits() {
        return phasedUnits;
    }

    public PhasedUnit getPhasedUnit(VirtualFile file) {
        return phasedUnitPerFile.get(file);
    }

    public PhasedUnit getPhasedUnitFromRelativePath(String relativePath) {
        if ( relativePath.startsWith("/") ) {
            relativePath = relativePath.substring(1);
        }
        return phasedUnitPerRelativePath.get(relativePath);
    }

    public void parseUnits(List<VirtualFile> srcDirectories) {
        for ( VirtualFile file : srcDirectories ) {
            parseUnit(file, file);
        }
    }

    public void parseUnit(VirtualFile srcDir) {
        parseUnit(srcDir, srcDir);
    }

    public void parseUnit(VirtualFile file, VirtualFile srcDir) {
        try {
            if ( file.isFolder() ) {
                //root directory is the src dir => start from here
                for ( VirtualFile subfile : file.getChildren() ) {
                    parseFileOrDirectory(subfile, srcDir);
                }
            }
            else {
                //simple file compilation
                //TODO is that really valid?
                parseFileOrDirectory(file, srcDir);
            }
        }
        catch (RuntimeException e) {
            //let it go
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException("Error while parsing the source directory: " + file.toString() ,e);
        }
    }

    private void parseFile(VirtualFile file, VirtualFile srcDir) throws Exception {
        if ( file.getName().endsWith(".ceylon") ) {

            //System.out.println("Parsing " + file.getName());
            InputStream is = file.getInputStream();
            ANTLRInputStream input = new ANTLRInputStream(is);
            CeylonLexer lexer = new CeylonLexer(input);

            CommonTokenStream tokens = new CommonTokenStream(lexer);

            CeylonParser parser = new CeylonParser(tokens);
            CeylonParser.compilationUnit_return r = parser.compilationUnit();

        	List<LexError> lexerErrors = lexer.getErrors();
        	for (LexError le: lexerErrors) {
                System.out.println("Lexer error in " + file.getName() + ": " + le.getMessage(lexer));
        	}

        	List<ParseError> parserErrors = parser.getErrors();
        	for (ParseError pe: parserErrors) {
                System.out.println("Parser error in " + file.getName() + ": " + pe.getMessage(parser));
        	}

        	com.redhat.ceylon.compiler.typechecker.model.Package p = moduleBuilder.getCurrentPackage();
            CommonTree t = (CommonTree) r.getTree();
            Tree.CompilationUnit cu = new CustomBuilder().buildCompilationUnit(t);
            PhasedUnit phasedUnit = new PhasedUnit(file, srcDir, cu, p, moduleBuilder, context);
            addPhasedUnit(file, phasedUnit);

        }
    }

    private void parseFileOrDirectory(VirtualFile file, VirtualFile srcDir) throws Exception {
        if (file.isFolder()) {
            processDirectory(file, srcDir);
        }
        else {
            parseFile(file, srcDir);
        }
    }

    private void processDirectory(VirtualFile dir, VirtualFile srcDir) throws Exception {
        moduleBuilder.push( dir.getName() );
        final List<VirtualFile> files = dir.getChildren();
        for (VirtualFile file: files) {
            if ( ModuleBuilder.MODULE_FILE.equals( file.getName() ) ) {
                moduleBuilder.visitModuleFile();
            }
        }
        for (VirtualFile file: files) {
            parseFileOrDirectory(file, srcDir);
        }
        moduleBuilder.pop();
    }
}
