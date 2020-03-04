/*******************************************************************************
 * Copyright (c) 2013 Andrey Loskutov.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributor:
 *   Andrey Loskutov
 *******************************************************************************/

package de.loskutov.fb.detectors;
import static de.loskutov.fb.matcher.InsnPattern.*;
import static java.util.regex.Pattern.*;
import static org.objectweb.asm.Opcodes.*;

import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import org.objectweb.asm.MethodVisitor;

import de.loskutov.fb.matcher.DefaultInstructionsIterator;
import de.loskutov.fb.matcher.IfTrue;
import de.loskutov.fb.matcher.InsnPattern;
import de.loskutov.fb.matcher.NamedNode;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.MethodAnnotation;
import edu.umd.cs.findbugs.SourceLineAnnotation;

public class FindPrintlnDetector extends AbstractPatternDetector {


    private final InsnPattern patternPrint;

    private static final IfTrue IF_TRUE = new IfTrue();

    private static final NamedNode.Field OUT = new NamedNode.Field(GETSTATIC,
            "java/lang/System", "out", "Ljava/io/PrintStream;");

    private static final NamedNode.Field ERR = new NamedNode.Field(GETSTATIC,
            "java/lang/System", "err", "Ljava/io/PrintStream;");

    private static final NamedNode.RegExMethod PRINT = new NamedNode.RegExMethod(
            INVOKEVIRTUAL, "java/io/PrintStream", "print(ln|f)?", null);

    private static final NamedNode.Method PRINT_STACK = new NamedNode.Method(
            INVOKEVIRTUAL, null, "printStackTrace", "()V");

    private static final NamedNode.RegExNamedNode ANY_DEBUG = new NamedNode.RegExNamedNode(
            NamedNode.ANY_OPCODE, null,
            ".*(debug|dump|trace|log|report|warn|progress|assert|show|verbose|print|access\\$).*", null);

    private static final Pattern DEBUG_METHOD_NAMES = compile(
            "(debug|dump|trace|log|report|warn|progress|assert|show|print|main).*");

    private static final Pattern DEBUG_CLASS_NAMES = compile(
            ".*(Dump|Trace|Log|Print).*");

    public FindPrintlnDetector(BugReporter bugReporter) {
        super(bugReporter);
        patternPrint = new InsnPattern();
        // "and" order is important!
        patternPrint.add(not (and (ANY_DEBUG, IF_TRUE)));
        patternPrint.add(or (PRINT_STACK, sequence (or(OUT, ERR), PRINT)));
    }

    @Override
    public MethodVisitor visitMethod(int access1, String name1, String desc,
            String signature1, String[] exceptions) {
        if(isDebugMethodName(name1)){
            return null;
        }
        return super.visitMethod(access1, name1, desc, signature1, exceptions);
    }

    private static boolean isDebugMethodName(String name1) {
        return DEBUG_METHOD_NAMES.matcher(name1).matches();
    }
    private static boolean isDebugClassName(String name1) {
        return DEBUG_CLASS_NAMES.matcher(name1).matches();
    }

    @Override
    public boolean checkClass() {
        return !isDebugClassName(name);
    }

    @Override
    protected BugInstance createBug(String methodName, String methodSign,  DefaultInstructionsIterator iterator, boolean isStatic) {
        int line = iterator.getLastLine();
        BugInstance bug = new BugInstance(this, "PRINTLN", NORMAL_PRIORITY);
        MethodAnnotation methodAnnotaion = MethodAnnotation.fromForeignMethod(
                name, methodName, methodSign, isStatic);
        bug.addClass(this).addMethod(methodAnnotaion);
        String sourceFileName = methodAnnotaion.getSourceFileName();
        String qualifiedClassName = methodAnnotaion.getClassName();
        SourceLineAnnotation sourceLine = new SourceLineAnnotation(
                qualifiedClassName, sourceFileName, line, line, 0, 0);
        bug.addSourceLine(sourceLine);
        return bug;
    }

    @Override
    @Nonnull
    protected InsnPattern getPattern() {
        return patternPrint;
    }

}
