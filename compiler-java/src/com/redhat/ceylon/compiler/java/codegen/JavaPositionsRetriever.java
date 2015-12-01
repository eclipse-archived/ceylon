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

package com.redhat.ceylon.compiler.java.codegen;

import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.FilterWriter;
import java.io.IOException;
import java.io.PipedReader;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;

import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree;
import com.redhat.ceylon.langtools.tools.javac.tree.Pretty;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCAnnotation;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCArrayAccess;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCArrayTypeTree;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCAssert;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCAssign;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCAssignOp;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCBinary;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCBlock;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCBreak;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCCase;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCCatch;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCClassDecl;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCCompilationUnit;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCConditional;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCContinue;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCDoWhileLoop;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCEnhancedForLoop;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCErroneous;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCExpressionStatement;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCFieldAccess;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCForLoop;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCIdent;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCIf;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCImport;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCInstanceOf;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCLabeledStatement;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCLiteral;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCMethodDecl;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCMethodInvocation;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCModifiers;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCNewArray;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCNewClass;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCParens;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCPrimitiveTypeTree;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCReturn;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCSkip;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCSwitch;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCSynchronized;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCThrow;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCTry;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCTypeApply;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCTypeCast;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCTypeParameter;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCUnary;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCVariableDecl;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCWhileLoop;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCWildcard;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.LetExpr;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.TypeBoundKind;
import com.redhat.ceylon.langtools.tools.javac.util.Position;
import com.sun.org.apache.xml.internal.serialize.LineSeparator;

public class JavaPositionsRetriever {

    private class InternalWriter extends CharArrayWriter
    {
        @Override
        public void write(int c) {
            super.write(c);
            currentPosition++;
        }

        @Override
        public void write(char[] chars) throws IOException {
            super.write(chars);
            currentPosition += chars.length;
        }

        @Override
        public void write(String string) throws IOException {
            super.write(string);
            currentPosition += string.length();
        }
        
        
    };
    private InternalWriter writer = null;
    
    private class PrettyToRetrievePositions extends Pretty
    {
        public PrettyToRetrievePositions(Writer out) {
            super(out, true);
        }

        private void storePositions(JCTree tree) {
            int javaPosition = currentPosition;
            javaPositions.put(tree, javaPosition);
            int ceylonPosition = tree.getPreferredPosition();
            Set<Integer> set = null; 
            if (! ceylonToJavaPositions.containsKey(ceylonPosition)) {
                set = new TreeSet<Integer>();
                ceylonToJavaPositions.put(ceylonPosition, set);
            }
            else {
                set = ceylonToJavaPositions.get(ceylonPosition); 
            }
            set.add(currentPosition);
        }

        @Override
        public void visitTopLevel(JCCompilationUnit tree) {
            storePositions(tree);
            super.visitTopLevel(tree);
        }

        @Override
        public void visitImport(JCImport tree) {
            storePositions(tree);
            super.visitImport(tree);
        }

        @Override
        public void visitClassDef(JCClassDecl tree) {
            storePositions(tree);
            super.visitClassDef(tree);
        }

        @Override
        public void visitMethodDef(JCMethodDecl tree) {
            storePositions(tree);
            super.visitMethodDef(tree);
        }

        @Override
        public void visitVarDef(JCVariableDecl tree) {
            storePositions(tree);
            super.visitVarDef(tree);
        }

        @Override
        public void visitSkip(JCSkip tree) {
            storePositions(tree);
            super.visitSkip(tree);
        }

        @Override
        public void visitBlock(JCBlock tree) {
            storePositions(tree);
            super.visitBlock(tree);
            tree.endpos = currentPosition - 1;
        }

        @Override
        public void visitDoLoop(JCDoWhileLoop tree) {
            storePositions(tree);
            super.visitDoLoop(tree);
        }

        @Override
        public void visitWhileLoop(JCWhileLoop tree) {
            storePositions(tree);
            super.visitWhileLoop(tree);
        }

        @Override
        public void visitForLoop(JCForLoop tree) {
            storePositions(tree);
            super.visitForLoop(tree);
        }

        @Override
        public void visitForeachLoop(JCEnhancedForLoop tree) {
            storePositions(tree);
            super.visitForeachLoop(tree);
        }

        @Override
        public void visitLabelled(JCLabeledStatement tree) {
            storePositions(tree);
            super.visitLabelled(tree);
        }

        @Override
        public void visitSwitch(JCSwitch tree) {
            storePositions(tree);
            super.visitSwitch(tree);
        }

        @Override
        public void visitCase(JCCase tree) {
            storePositions(tree);
            super.visitCase(tree);
        }

        @Override
        public void visitSynchronized(JCSynchronized tree) {
            storePositions(tree);
            super.visitSynchronized(tree);
        }

        @Override
        public void visitTry(JCTry tree) {
            storePositions(tree);
            super.visitTry(tree);
        }

        @Override
        public void visitCatch(JCCatch tree) {
            storePositions(tree);
            super.visitCatch(tree);
        }

        @Override
        public void visitConditional(JCConditional tree) {
            storePositions(tree);
            super.visitConditional(tree);
        }

        @Override
        public void visitIf(JCIf tree) {
            storePositions(tree);
            super.visitIf(tree);
        }

        @Override
        public void visitExec(JCExpressionStatement tree) {
            storePositions(tree);
            super.visitExec(tree);
        }

        @Override
        public void visitBreak(JCBreak tree) {
            storePositions(tree);
            super.visitBreak(tree);
        }

        @Override
        public void visitContinue(JCContinue tree) {
            storePositions(tree);
            super.visitContinue(tree);
        }

        @Override
        public void visitReturn(JCReturn tree) {
            storePositions(tree);
            super.visitReturn(tree);
        }

        @Override
        public void visitThrow(JCThrow tree) {
            storePositions(tree);
            super.visitThrow(tree);
        }

        @Override
        public void visitAssert(JCAssert tree) {
            storePositions(tree);
            super.visitAssert(tree);
        }

        @Override
        public void visitApply(JCMethodInvocation tree) {
            storePositions(tree);
            super.visitApply(tree);
        }

        @Override
        public void visitNewClass(JCNewClass tree) {
            storePositions(tree);
            super.visitNewClass(tree);
        }

        @Override
        public void visitNewArray(JCNewArray tree) {
            storePositions(tree);
            super.visitNewArray(tree);
        }

        @Override
        public void visitParens(JCParens tree) {
            storePositions(tree);
            super.visitParens(tree);
        }

        @Override
        public void visitAssign(JCAssign tree) {
            storePositions(tree);
            super.visitAssign(tree);
        }

        @Override
        public void visitAssignop(JCAssignOp tree) {
            storePositions(tree);
            super.visitAssignop(tree);
        }

        @Override
        public void visitUnary(JCUnary tree) {
            storePositions(tree);
            super.visitUnary(tree);
        }

        @Override
        public void visitBinary(JCBinary tree) {
            storePositions(tree);
            super.visitBinary(tree);
        }

        @Override
        public void visitTypeCast(JCTypeCast tree) {
            storePositions(tree);
            super.visitTypeCast(tree);
        }

        @Override
        public void visitTypeTest(JCInstanceOf tree) {
            storePositions(tree);
            super.visitTypeTest(tree);
        }

        @Override
        public void visitIndexed(JCArrayAccess tree) {
            storePositions(tree);
            super.visitIndexed(tree);
        }

        @Override
        public void visitSelect(JCFieldAccess tree) {
            storePositions(tree);
            super.visitSelect(tree);
        }

        @Override
        public void visitIdent(JCIdent tree) {
            storePositions(tree);
            super.visitIdent(tree);
        }

        @Override
        public void visitLiteral(JCLiteral tree) {
            storePositions(tree);
            super.visitLiteral(tree);
        }

        @Override
        public void visitTypeIdent(JCPrimitiveTypeTree tree) {
            storePositions(tree);
            super.visitTypeIdent(tree);
        }

        @Override
        public void visitTypeArray(JCArrayTypeTree tree) {
            storePositions(tree);
            super.visitTypeArray(tree);
        }

        @Override
        public void visitTypeApply(JCTypeApply tree) {
            storePositions(tree);
            super.visitTypeApply(tree);
        }

        @Override
        public void visitTypeParameter(JCTypeParameter tree) {
            storePositions(tree);
            super.visitTypeParameter(tree);
        }

        @Override
        public void visitWildcard(JCWildcard tree) {
            storePositions(tree);
            super.visitWildcard(tree);
        }

        @Override
        public void visitTypeBoundKind(TypeBoundKind tree) {
            storePositions(tree);
            super.visitTypeBoundKind(tree);
        }

        @Override
        public void visitErroneous(JCErroneous tree) {
            storePositions(tree);
            super.visitErroneous(tree);
        }

        @Override
        public void visitLetExpr(LetExpr tree) {
            storePositions(tree);
            super.visitLetExpr(tree);
        }

        @Override
        public void visitModifiers(JCModifiers mods) {
            storePositions(mods);
            super.visitModifiers(mods);
        }

        @Override
        public void visitAnnotation(JCAnnotation tree) {
            storePositions(tree);
            super.visitAnnotation(tree);
        }

        @Override
        public void visitTree(JCTree tree) {
            storePositions(tree);
            super.visitTree(tree);

        }
    };
    private PrettyToRetrievePositions pretty = null;
    
    private int currentPosition = Position.FIRSTPOS;
    private Map<Integer, Set<Integer>> ceylonToJavaPositions = new TreeMap<Integer, Set<Integer>>();
    private Map<Integer, Set<Integer>> ceylonToJavaLines = new TreeMap<Integer, Set<Integer>>();
    private Map<JCTree, Integer> javaPositions = new HashMap<JCTree, Integer>();
    private Map<Integer, Set<Node>> ceylonNodesAtPosition = new TreeMap<Integer, Set<Node>>();
    private Position.LineMap javaLineMap = null;
    private Position.LineMap ceylonLineMap = null;
    private JCCompilationUnit unit = null;
    
    public Position.LineMap getJavaLineMap() {
        return javaLineMap;
    }

    public Position.LineMap getCeylonLineMap() {
        return ceylonLineMap;
    }
    
    public JavaPositionsRetriever() {
        writer = new InternalWriter();
        pretty = new PrettyToRetrievePositions(writer);
        
    }

    public void retrieve(JCCompilationUnit unit)
    {
        this.unit = unit; 
        pretty.visitTopLevel(unit);
        ceylonLineMap = unit.getLineMap();
        javaLineMap = Position.makeLineMap(writer.toCharArray(), writer.toCharArray().length, false);
        for (Integer ceylonPosition : ceylonToJavaPositions.keySet()) {
            if (ceylonPosition != Position.NOPOS) {
                int ceylonLine = ceylonLineMap.getLineNumber(ceylonPosition);
                for (Integer javaPosition : ceylonToJavaPositions.get(ceylonPosition)) {
                    int javaLine = javaLineMap.getLineNumber(javaPosition);
                    Set<Integer> set = null; 
                    if (! ceylonToJavaLines.containsKey(ceylonLine)) {
                        set = new TreeSet<Integer>();
                        ceylonToJavaLines.put(ceylonLine, set);
                    }
                    else {
                        set = ceylonToJavaLines.get(ceylonLine); 
                    }
                    set.add(javaLine);
                }
            }
        }
    }
    
    public String getSourceCode() {
        return writer.toString();
    }
    
    private String addCeylonLinesComments(char[] source) {
        String annotatedSourceCode = "";
        BufferedReader reader = new BufferedReader(new CharArrayReader(source));
        String line = null;
        int javaLine = 1;
        try {
            while((line = reader.readLine()) != null) {
                annotatedSourceCode += line;
                String ceylonLines = getCeylonLinesForJavaLine(revertMap(ceylonToJavaLines), javaLine);
                annotatedSourceCode += ceylonLines.isEmpty() ? "" : " // line " + ceylonLines;
                annotatedSourceCode += "\n";
                javaLine ++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return annotatedSourceCode;
    }
    
    public String getJavaSourceCodeWithCeylonLines() {
        return addCeylonLinesComments(writer.toCharArray());
    }
    
    private String formatCeylonPosition(int position) {
        String result = ceylonLineMap.getLineNumber(position) + " : " + ceylonLineMap.getColumnNumber(position);
        if (ceylonNodesAtPosition != null) {
            if (ceylonNodesAtPosition.containsKey(position)) {
                List<String> nodeLabels = new LinkedList<String>(); 
                for (Node node : ceylonNodesAtPosition.get(position)) {
                    nodeLabels.add(node.getNodeType() + " - " + node.getToken().getText());
                }
                result += "(" + nodeLabels + ")";
            }
        }
        return result;
    }
    
    public String getJavaSourceCodeWithCeylonPositions() {
        final CharArrayWriter writer = new CharArrayWriter(); 
        Pretty printer = new Pretty (writer, true){
            int previousCeylonPosition = -1;
            int previousPositionInString = 0;
            private void outputCeylonPosition(JCTree tree) {
                try {
                    int currentCeylonPosition = tree.getPreferredPosition();
                    int currentPositionInString = writer.size();
                    if (previousCeylonPosition != currentCeylonPosition || previousPositionInString != currentPositionInString) {
                        if (currentCeylonPosition != -1 && currentCeylonPosition != 0) {
                            writer.write("/* " + formatCeylonPosition(currentCeylonPosition) + " */");
                        }
                        previousCeylonPosition = currentCeylonPosition;
                        previousPositionInString = writer.size();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void visitTopLevel(JCCompilationUnit tree) {
                outputCeylonPosition(tree);
                super.visitTopLevel(tree);
            }

            @Override
            public void visitImport(JCImport tree) {
                outputCeylonPosition(tree);
                super.visitImport(tree);
            }

            @Override
            public void visitClassDef(JCClassDecl tree) {
                outputCeylonPosition(tree);
                super.visitClassDef(tree);
            }

            @Override
            public void visitMethodDef(JCMethodDecl tree) {
                outputCeylonPosition(tree);
                super.visitMethodDef(tree);
            }

            @Override
            public void visitVarDef(JCVariableDecl tree) {
                outputCeylonPosition(tree);
                super.visitVarDef(tree);
            }

            @Override
            public void visitSkip(JCSkip tree) {
                outputCeylonPosition(tree);
                super.visitSkip(tree);
            }

            @Override
            public void visitBlock(JCBlock tree) {
                outputCeylonPosition(tree);
                super.visitBlock(tree);
                tree.endpos = currentPosition - 1;
            }

            @Override
            public void visitDoLoop(JCDoWhileLoop tree) {
                outputCeylonPosition(tree);
                super.visitDoLoop(tree);
            }

            @Override
            public void visitWhileLoop(JCWhileLoop tree) {
                outputCeylonPosition(tree);
                super.visitWhileLoop(tree);
            }

            @Override
            public void visitForLoop(JCForLoop tree) {
                outputCeylonPosition(tree);
                super.visitForLoop(tree);
            }

            @Override
            public void visitForeachLoop(JCEnhancedForLoop tree) {
                outputCeylonPosition(tree);
                super.visitForeachLoop(tree);
            }

            @Override
            public void visitLabelled(JCLabeledStatement tree) {
                outputCeylonPosition(tree);
                super.visitLabelled(tree);
            }

            @Override
            public void visitSwitch(JCSwitch tree) {
                outputCeylonPosition(tree);
                super.visitSwitch(tree);
            }

            @Override
            public void visitCase(JCCase tree) {
                outputCeylonPosition(tree);
                super.visitCase(tree);
            }

            @Override
            public void visitSynchronized(JCSynchronized tree) {
                outputCeylonPosition(tree);
                super.visitSynchronized(tree);
            }

            @Override
            public void visitTry(JCTry tree) {
                outputCeylonPosition(tree);
                super.visitTry(tree);
            }

            @Override
            public void visitCatch(JCCatch tree) {
                outputCeylonPosition(tree);
                super.visitCatch(tree);
            }

            @Override
            public void visitConditional(JCConditional tree) {
                outputCeylonPosition(tree);
                super.visitConditional(tree);
            }

            @Override
            public void visitIf(JCIf tree) {
                outputCeylonPosition(tree);
                super.visitIf(tree);
            }

            @Override
            public void visitExec(JCExpressionStatement tree) {
                outputCeylonPosition(tree);
                super.visitExec(tree);
            }

            @Override
            public void visitBreak(JCBreak tree) {
                outputCeylonPosition(tree);
                super.visitBreak(tree);
            }

            @Override
            public void visitContinue(JCContinue tree) {
                outputCeylonPosition(tree);
                super.visitContinue(tree);
            }

            @Override
            public void visitReturn(JCReturn tree) {
                outputCeylonPosition(tree);
                super.visitReturn(tree);
            }

            @Override
            public void visitThrow(JCThrow tree) {
                outputCeylonPosition(tree);
                super.visitThrow(tree);
            }

            @Override
            public void visitAssert(JCAssert tree) {
                outputCeylonPosition(tree);
                super.visitAssert(tree);
            }

            @Override
            public void visitApply(JCMethodInvocation tree) {
                outputCeylonPosition(tree);
                super.visitApply(tree);
            }

            @Override
            public void visitNewClass(JCNewClass tree) {
                outputCeylonPosition(tree);
                super.visitNewClass(tree);
            }

            @Override
            public void visitNewArray(JCNewArray tree) {
                outputCeylonPosition(tree);
                super.visitNewArray(tree);
            }

            @Override
            public void visitParens(JCParens tree) {
                outputCeylonPosition(tree);
                super.visitParens(tree);
            }

            @Override
            public void visitAssign(JCAssign tree) {
                outputCeylonPosition(tree);
                super.visitAssign(tree);
            }

            @Override
            public void visitAssignop(JCAssignOp tree) {
                outputCeylonPosition(tree);
                super.visitAssignop(tree);
            }

            @Override
            public void visitUnary(JCUnary tree) {
                outputCeylonPosition(tree);
                super.visitUnary(tree);
            }

            @Override
            public void visitBinary(JCBinary tree) {
                outputCeylonPosition(tree);
                super.visitBinary(tree);
            }

            @Override
            public void visitTypeCast(JCTypeCast tree) {
                outputCeylonPosition(tree);
                super.visitTypeCast(tree);
            }

            @Override
            public void visitTypeTest(JCInstanceOf tree) {
                outputCeylonPosition(tree);
                super.visitTypeTest(tree);
            }

            @Override
            public void visitIndexed(JCArrayAccess tree) {
                outputCeylonPosition(tree);
                super.visitIndexed(tree);
            }

            @Override
            public void visitSelect(JCFieldAccess tree) {
                outputCeylonPosition(tree);
                super.visitSelect(tree);
            }

            @Override
            public void visitIdent(JCIdent tree) {
                outputCeylonPosition(tree);
                super.visitIdent(tree);
            }

            @Override
            public void visitLiteral(JCLiteral tree) {
                outputCeylonPosition(tree);
                super.visitLiteral(tree);
            }

            @Override
            public void visitTypeIdent(JCPrimitiveTypeTree tree) {
                outputCeylonPosition(tree);
                super.visitTypeIdent(tree);
            }

            @Override
            public void visitTypeArray(JCArrayTypeTree tree) {
                outputCeylonPosition(tree);
                super.visitTypeArray(tree);
            }

            @Override
            public void visitTypeApply(JCTypeApply tree) {
                outputCeylonPosition(tree);
                super.visitTypeApply(tree);
            }

            @Override
            public void visitTypeParameter(JCTypeParameter tree) {
                outputCeylonPosition(tree);
                super.visitTypeParameter(tree);
            }

            @Override
            public void visitWildcard(JCWildcard tree) {
                outputCeylonPosition(tree);
                super.visitWildcard(tree);
            }

            @Override
            public void visitTypeBoundKind(TypeBoundKind tree) {
                outputCeylonPosition(tree);
                super.visitTypeBoundKind(tree);
            }

            @Override
            public void visitErroneous(JCErroneous tree) {
                outputCeylonPosition(tree);
                super.visitErroneous(tree);
            }

            @Override
            public void visitLetExpr(LetExpr tree) {
                outputCeylonPosition(tree);
                super.visitLetExpr(tree);
            }

            @Override
            public void visitModifiers(JCModifiers mods) {
                outputCeylonPosition(mods);
                super.visitModifiers(mods);
            }

            @Override
            public void visitAnnotation(JCAnnotation tree) {
                outputCeylonPosition(tree);
                super.visitAnnotation(tree);
            }

            @Override
            public void visitTree(JCTree tree) {
                outputCeylonPosition(tree);
                super.visitTree(tree);

            }
        };
        printer.visitTopLevel(unit);
        return writer.toString();
    }
    
    private Map<Integer, Set<Integer>> revertMap(Map<Integer, Set<Integer>> ceylonToJava) {
        Map<Integer, Set<Integer>> result = new TreeMap();
        for (int ceylonLine : ceylonToJavaLines.keySet()) {
            for(int javaLine : ceylonToJavaLines.get(ceylonLine)) {
                Set<Integer> set = null; 
                if (! result.containsKey(javaLine)) {
                    set = new TreeSet<Integer>();
                    result.put(javaLine, set);
                }
                else {
                    set = result.get(javaLine); 
                }
                set.add(ceylonLine);
            }
        }
        return result;
    }
    
    private String getCeylonLinesForJavaLine(Map<Integer, Set<Integer>> javaToCeylonLineMap, int javaLine) {
        String result = "";
        Set<Integer> ceylonLines = javaToCeylonLineMap.get(javaLine);
        if (ceylonLines != null) {
            boolean first = true;
            for (Integer ceylonLine : ceylonLines) {
                if (!first) {
                    result += ", ";
                }
                else {
                    first = false;
                }
                result += ceylonLine.toString();
            }
        }
        return result;
    }

    public Map<Integer, Set<Integer>> getCeylonToJavaLineMap() {
        return ceylonToJavaLines;
    }

    public void addCeylonNode(int tokenStartPosition, Node node) {
        Set<Node> set = null; 
        if (! ceylonNodesAtPosition.containsKey(tokenStartPosition)) {
            set = new LinkedHashSet<Node>();
            ceylonNodesAtPosition.put(tokenStartPosition, set);
        }
        else {
            set = ceylonNodesAtPosition.get(tokenStartPosition); 
        }
        set.add(node);
    }
}
