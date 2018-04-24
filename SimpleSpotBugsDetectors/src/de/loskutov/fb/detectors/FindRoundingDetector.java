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
import static org.objectweb.asm.Opcodes.*;

import javax.annotation.Nonnull;

import de.loskutov.fb.matcher.InsnNode;
import de.loskutov.fb.matcher.InsnPattern;
import de.loskutov.fb.matcher.InstructionMatcher;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.MethodAnnotation;
import edu.umd.cs.findbugs.SourceLineAnnotation;

public class FindRoundingDetector extends AbstractPatternDetector {


    private final InsnPattern patternPrint;


    public FindRoundingDetector(BugReporter bugReporter) {
        super(bugReporter);
        patternPrint = new InsnPattern();
//        patternPrint.add(sequence (new InsnNode(D2I), new InsnNode(ISTORE)));
        InstructionMatcher float2int = or(new InsnNode(D2I), new InsnNode(F2I));
        InstructionMatcher float2long = or(new InsnNode(D2L), new InsnNode(F2L));
        InstructionMatcher storeInt = or(new InsnNode(ISTORE), new InsnNode(PUTFIELD), new InsnNode(PUTSTATIC));
        InstructionMatcher storeLong = or(new InsnNode(LSTORE), new InsnNode(PUTFIELD), new InsnNode(PUTSTATIC));
        InstructionMatcher int2small = or (new InsnNode(I2S), new InsnNode(I2B));
        patternPrint.add(
                or(
                        and(float2int, storeInt),
                        and(float2int, int2small, storeInt),
                        and(float2long, storeLong)
        ));
    }

    @Override
    public boolean checkClass() {
        return true;
    }

    @Override
    protected BugInstance createBug(String methodName, String methodSign, int line, boolean isStatic) {
        BugInstance bug = new BugInstance(this, "ROUNDING", LOW_PRIORITY);
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
