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

import javax.annotation.Nonnull;

import org.objectweb.asm.Opcodes;

import de.loskutov.fb.matcher.InsnPattern;
import de.loskutov.fb.matcher.NamedNode;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.MethodAnnotation;
import edu.umd.cs.findbugs.SourceLineAnnotation;

public class FindAssertDetector extends AbstractPatternDetector {

    private final InsnPattern assertPattern;

//    GETSTATIC de/loskutov/fb/detectors/samples/FindAssertSamples.$assertionsDisabled : Z
//    IFNE L1
//    NEW java/lang/AssertionError
    private static final NamedNode.Field F_ASSERT =
            new NamedNode.Field(Opcodes.GETSTATIC, null, "$assertionsDisabled", "Z");


    public FindAssertDetector(BugReporter bugReporter) {
        super(bugReporter);
        assertPattern = new InsnPattern();
        assertPattern.add(F_ASSERT);
    }

    @Override
    @Nonnull
    protected InsnPattern getPattern() {
        return assertPattern;
    }

    @Override
    protected BugInstance createBug(String methodName, String methodSign, int line, boolean isStatic) {
        BugInstance bug = new BugInstance(this, "ASSERT", NORMAL_PRIORITY);
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
