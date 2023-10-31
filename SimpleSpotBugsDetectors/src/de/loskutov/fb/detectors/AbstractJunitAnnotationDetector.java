/*******************************************************************************
 * Copyright (c) 2023 Andrey Loskutov.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributor:
 *   Andrey Loskutov
 *******************************************************************************/

package de.loskutov.fb.detectors;

import java.util.List;
import java.util.ListIterator;
import java.util.function.Consumer;

import javax.annotation.Nonnull;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodNode;

import de.loskutov.fb.matcher.DefaultInstructionsIterator;
import de.loskutov.fb.matcher.InsnPattern;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.MethodAnnotation;
import edu.umd.cs.findbugs.SourceLineAnnotation;

public abstract class AbstractJunitAnnotationDetector extends AbstractPatternDetector {

    public AbstractJunitAnnotationDetector(BugReporter bugReporter) {
        super(bugReporter);
    }

    @Override
    @Nonnull
    protected InsnPattern getPattern() {
        return InsnPattern.EMPTY_PATTERN;
    }

    abstract String getJunitAnnotationSignature();
    abstract String getBugType();

    @Override
    protected void checkAnnotations(MethodNode node, Consumer<BugInstance> reporter) {
        List<AnnotationNode> annotations = node.visibleAnnotations;
        if(annotations == null) {
            return;
        }
        final String junitAnnotationSignature = getJunitAnnotationSignature();
        for (AnnotationNode annotationNode : annotations) {
            if(junitAnnotationSignature.equals(annotationNode.desc)) {
                reporter.accept(createBug(node));
            }
        }
    }

    protected BugInstance createBug(MethodNode method) {
        BugInstance bug = new BugInstance(this, getBugType(), NORMAL_PRIORITY);
        MethodAnnotation methodAnnotaion = MethodAnnotation.fromForeignMethod(
                name, method.name, method.desc, (method.access & Opcodes.ACC_STATIC) != 0);
        bug.addClass(this).addMethod(methodAnnotaion);
        int line = getFirstLine(method);
        if(line <= 0) {
            return bug;
        }
        // Note, the first line we get is the first instruction line of the method.
        // So it might be 100 lines below after method declaration, for example
        // if method starts with an inline comment
        // Decrease one line, we want to show it at method declaration (in best case)
        line --;
        String sourceFileName = methodAnnotaion.getSourceFileName();
        String qualifiedClassName = methodAnnotaion.getClassName();
        SourceLineAnnotation sourceLine = new SourceLineAnnotation(
                qualifiedClassName, sourceFileName, line, line, 0, 0);
        bug.addSourceLine(sourceLine);
        return bug;
    }

    private static int getFirstLine(MethodNode method) {
        ListIterator<AbstractInsnNode> iterator = method.instructions.iterator();
        while (iterator.hasNext()) {
            AbstractInsnNode abstractInsnNode = (AbstractInsnNode) iterator.next();
            if(abstractInsnNode instanceof LineNumberNode) {
                LineNumberNode lineNumberNode = (LineNumberNode) abstractInsnNode;
                return lineNumberNode.line;
            }
        }
        return -1;
    }

    @Override
    protected BugInstance createBug(String methodName, String methodSign, DefaultInstructionsIterator iterator,
            boolean isStatic) {
        return null;
    }

}
