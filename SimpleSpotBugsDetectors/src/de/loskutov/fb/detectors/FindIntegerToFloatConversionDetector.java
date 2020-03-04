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

import static de.loskutov.fb.matcher.InsnPattern.or;

import javax.annotation.Nonnull;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;

import de.loskutov.fb.matcher.DefaultInstructionsIterator;
import de.loskutov.fb.matcher.InsnNode;
import de.loskutov.fb.matcher.InsnPattern;
import de.loskutov.fb.matcher.InstructionMatcher;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.MethodAnnotation;
import edu.umd.cs.findbugs.SourceLineAnnotation;

public class FindIntegerToFloatConversionDetector extends AbstractPatternDetector {

    private final InsnPattern l2dPattern;

    // L2D or I2F detection
    InstructionMatcher longToDouble = or(new InsnNode(Opcodes.L2D), new InsnNode(Opcodes.I2F), new InsnNode(Opcodes.L2F));

    public FindIntegerToFloatConversionDetector(BugReporter bugReporter) {
        super(bugReporter);
        l2dPattern = new InsnPattern();
        l2dPattern.add(longToDouble);
    }

    @Override
    @Nonnull
    protected InsnPattern getPattern() {
        return l2dPattern;
    }

    @Override
    protected BugInstance createBug(String methodName, String methodSign,  DefaultInstructionsIterator iterator, boolean isStatic) {
        int line = iterator.getLastLine();
        AbstractInsnNode node = iterator.getMatcher().getLastNode();
        String type = "INT2FLOAT";
        if(node != null) {
            int opcode = node.getOpcode();
            if (opcode == Opcodes.L2D) {
                type = "LONG2DOUBLE";
            } else
            if (opcode == Opcodes.L2F) {
                type = "LONG2FLOAT";
            }
        }

        BugInstance bug = new BugInstance(this, type, NORMAL_PRIORITY);
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
}
