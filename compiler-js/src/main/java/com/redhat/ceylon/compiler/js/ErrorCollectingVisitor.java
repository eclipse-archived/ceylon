package com.redhat.ceylon.compiler.js;

import static com.redhat.ceylon.compiler.typechecker.tree.TreeUtil.isForBackend;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.analyzer.AnalysisError;
import com.redhat.ceylon.compiler.typechecker.analyzer.UsageWarning;
import com.redhat.ceylon.compiler.typechecker.io.VirtualFile;
import com.redhat.ceylon.compiler.typechecker.parser.RecognitionError;
import com.redhat.ceylon.compiler.typechecker.tree.AnalysisMessage;
import com.redhat.ceylon.compiler.typechecker.tree.Message;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.TreeUtil;
import com.redhat.ceylon.compiler.typechecker.tree.UnexpectedError;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

public class ErrorCollectingVisitor extends Visitor {
    private TypeChecker tc;
    
    private List<PositionedMessage> analErrors = new ArrayList<PositionedMessage>();
    private List<PositionedMessage> recogErrors = new ArrayList<PositionedMessage>();
    
    public static class PositionedMessage {
        public Node node;
        public Message message;
        
        PositionedMessage(Node that, RecognitionError err) {
            node = that;
            message = err;
        }
        
        PositionedMessage(AnalysisMessage msg) {
            node = msg.getTreeNode();
            message = msg;
        }
    }
    
    public ErrorCollectingVisitor(TypeChecker tc) {
        this.tc = tc;
    }

    public int getErrorCount() {
        int errCount = 0;
        for (PositionedMessage pm : analErrors) {
            if (pm.message instanceof AnalysisError ||
                pm.message instanceof UnexpectedError) {
                errCount++;
            }
        }
        return errCount;
    }
    
    public Set<Message> getErrors() {
        Set<Message> result = new LinkedHashSet<Message>();
        if (!recogErrors.isEmpty()) {
            for (PositionedMessage pm : recogErrors) {
                result.add(pm.message);
            }
        } else {
            for (PositionedMessage pm : analErrors) {
                result.add(pm.message);
            }
        }
        return result;
    }

    public void clear() {
        recogErrors.clear();
        analErrors.clear();
    }
    
    private void addErrors(Node that) {
        for (Message m: that.getErrors()) {
            if (m instanceof AnalysisMessage) {
                analErrors.add(new PositionedMessage((AnalysisMessage)m));
            } else {
                recogErrors.add(new PositionedMessage(that, (RecognitionError)m));
            }
        }
    }
    @Override
    public void visitAny(Node that) {
        super.visitAny(that);
        addErrors(that);
    }
    @Override
    public void visit(Tree.Declaration that) {
        if (isForBackend(that.getAnnotationList(), Backend.JavaScript, that.getUnit())
                || isForBackend(that.getAnnotationList(), Backend.Header, that.getUnit())) {
            super.visit(that);
        }
    }
    @Override
    public void visit(Tree.ModuleDescriptor that) {
        if (isForBackend(that.getAnnotationList(), Backend.JavaScript, that.getUnit())
                || isForBackend(that.getAnnotationList(), Backend.Header, that.getUnit())) {
            super.visit(that);
        } else {
            addErrors(that);
        }
    }
    @Override
    public void visit(Tree.ImportModule that) {
        if (isForBackend(that.getAnnotationList(), Backend.JavaScript, that.getUnit())
                || isForBackend(that.getAnnotationList(), Backend.Header, that.getUnit())) {
            super.visit(that);
        }
    }

    public int printErrors(boolean printWarnings, boolean printCount) throws IOException {
        return printErrors(new OutputStreamWriter(System.out), null, printWarnings, printCount);
    }

    public int printErrors(Writer out, DiagnosticListener diagnosticListener, boolean printWarnings, boolean printCount) throws IOException {
        int warnings = 0;
        int count = 0;
        List<PositionedMessage> errors = (!recogErrors.isEmpty()) ? recogErrors : analErrors;
        for (PositionedMessage pm : errors) {
            Message err = pm.message;
            if (err instanceof UsageWarning) {
                if (!printWarnings || ((UsageWarning)err).isSuppressed()) {
                    continue;
                }
            }
            Node node = TreeUtil.getIdentifyingNode(pm.node);
            int line = err.getLine();
            int position = -1;
            if(err instanceof AnalysisMessage){
                if(node != null && node.getToken() != null)
                    position = node.getToken().getCharPositionInLine();
            }else if(err instanceof RecognitionError){
                position = ((RecognitionError) err).getCharacterInLine();
            }
            String fileName = (node.getUnit() != null) ? node.getUnit().getFullPath() : "unknown";
            out.write(fileName);
            out.write(":");
            out.write(String.format("%d", line));
            out.write(": ");
            if (err instanceof UsageWarning) {
                out.write("warning");
                warnings++;
            } else {
                out.write("error");
                count++;
            }
            out.write(": ");
            out.write(err.getMessage());
            out.write(System.lineSeparator());
            String ln = getErrorSourceLine(pm);
            if (ln != null) {
                out.write(ln);
                out.write(System.lineSeparator());
                out.write(getErrorMarkerLine(position));
                out.write(System.lineSeparator());
            }
            
            if(diagnosticListener != null){
                File file = null;
                boolean warning = err instanceof UsageWarning;
                if(node.getUnit() != null && node.getUnit().getFullPath() != null)
                    file = new File(node.getUnit().getFullPath()).getAbsoluteFile();
                if(position != -1)
                    position++; // make it 1-based
                if(warning)
                    diagnosticListener.warning(file, line, position, err.getMessage());
                else
                    diagnosticListener.error(file, line, position, err.getMessage());
            }
        }
        if (printCount) {
            if (count > 0)
                out.write(String.format("%d %s%n", count, count==1?"error":"errors"));
            if (warnings > 0)
                out.write(String.format("%d %s%n", warnings, warnings==1?"warning":"warnings"));
        }
        out.flush();
        return count;
    }

    private String getErrorSourceLine(PositionedMessage pm) {
        if (pm.node.getUnit() != null) {
            int lineNr = pm.message.getLine();
            File file = new File(pm.node.getUnit().getFullPath());
            VirtualFile vfile = tc.getContext().getVfs().getFromFile(file);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(vfile.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (--lineNr <= 0) {
                        return line;
                    }
                }
            } catch (IOException e) {
                // Ignore
            }
        }
        return null;
    }

    private String getErrorMarkerLine(int position) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < position; i++) {
            str.append(" ");
        }
        str.append("^");
        return str.toString();
    }

}
