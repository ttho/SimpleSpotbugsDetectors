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

import java.util.function.Consumer;

import javax.annotation.Nonnull;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import de.loskutov.fb.matcher.DefaultInstructionsIterator;
import de.loskutov.fb.matcher.InsnPattern;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.asm.ClassNodeDetector;

public abstract class AbstractPatternDetector extends ClassNodeDetector {


    public AbstractPatternDetector(BugReporter bugReporter) {
        super(bugReporter);
    }

    abstract protected BugInstance createBug(String methodName, String methodSign, DefaultInstructionsIterator iterator, boolean isStatic);

    @Nonnull
    abstract protected InsnPattern getPattern();

    @Nonnull
    protected void checkAnnotations(ClassNode clazzNode, MethodNode node, Consumer<BugInstance> reporter) {
        // no op by default
    }

    @Override
    public MethodVisitor visitMethod(final int access1, final String name1,
            final String desc, final String signature1,
            final String[] exceptions) {
        return new MyMethodNode(access1, name1, desc, signature1, exceptions);
    }

    private final class MyMethodNode extends MethodNode {
        public MyMethodNode(int access, String name, String desc, String signature, String[] exceptions) {
            super(Opcodes.ASM9, access, name, desc, signature, exceptions);
        }

        @Override
        public void visitEnd() {
            if(!checkClass()){
                return;
            }
            InsnPattern pattern = getPattern();
            if (!pattern.isEmpty()) {
                DefaultInstructionsIterator it = new DefaultInstructionsIterator(pattern, instructions);
                for(int insNmbr = it.next(0); insNmbr != -1; insNmbr = it.next(insNmbr)){
                    if(it.hasMatch()) {
                        BugInstance bug = createBug(name, desc, it, isStatic());
                        bugReporter.reportBug(bug);
                    }
                }
            }
            checkAnnotations(AbstractPatternDetector.this, this, bug -> {
                bugReporter.reportBug(bug);
            });
        }

        boolean isStatic(){
            return (access & Opcodes.ACC_STATIC) != 0;
        }

        @Override
        public String toString() {
            return "Method: " + this.name + this.desc;
        }
    }

    public boolean checkClass() {
        return true;
    }

    @Override
    public String toString() {
        return "Class: " + this.name;
    }
}
