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

import static de.loskutov.fb.detectors.Trace.log;

import java.util.HashSet;
import java.util.Set;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.MethodAnnotation;
import edu.umd.cs.findbugs.asm.ClassNodeDetector;
import edu.umd.cs.findbugs.classfile.CheckedAnalysisException;
import edu.umd.cs.findbugs.classfile.ClassDescriptor;
import edu.umd.cs.findbugs.classfile.DescriptorFactory;
import edu.umd.cs.findbugs.classfile.Global;
import edu.umd.cs.findbugs.classfile.MethodDescriptor;

/**
 *
 *
 */
public class DangerousNativeMethods extends ClassNodeDetector {

    private final NativeMethodsDatabase caDatabase;

    private ClassDescriptor classDesc;

    public MethodDescriptor methodToAnalyse;

    public DangerousNativeMethods(BugReporter bugReporter) {
        super(bugReporter);
        caDatabase = Global.getAnalysisCache().getDatabase(NativeMethodsDatabase.class);
    }

    @Override
    public void visitClass(ClassDescriptor classDescriptor)
            throws CheckedAnalysisException {
        this.classDesc = classDescriptor;
        super.visitClass(classDescriptor);
    }

    private final class MyMethodNode extends MethodNode {

        // current method descriptor
        private final MethodDescriptor actualMethodDesc;

        public MyMethodNode(final int access, final String name,
                final String desc, final String signature,
                final String[] exceptions) {
            super(access, name, desc, signature, exceptions);
            boolean isStatic = (access & Opcodes.ACC_STATIC) != 0;
            // create MethodAnnotation to make the class name, method name,
            // transfer parameter available
            MethodAnnotation methodAnnotaion = MethodAnnotation
                    .fromForeignMethod(classDesc.getClassName(), name, desc,
                            isStatic);

            // convert the MethodAnnotation to type method descriptor
            actualMethodDesc = DescriptorFactory.instance()
                    .getMethodDescriptor(methodAnnotaion);
        }

        @Override
        public void visitEnd() {

            // new called method list
            Set<MethodDescriptor> calledMethods = new HashSet<MethodDescriptor>();

            // get all called methods
            for (int i = 0; i < instructions.size(); i++) {
                AbstractInsnNode node = instructions.get(i);

                // eliminate the constructor name "init"
                if (name.contains("init")) {
                    continue;
                }

                // method call instruction
                if (node.getType() == AbstractInsnNode.METHOD_INSN
                        || node.getOpcode() == Opcodes.INVOKESPECIAL
                        || node.getOpcode() == Opcodes.INVOKEVIRTUAL) {

                    // actual method node
                    MethodInsnNode mn = (MethodInsnNode) node;

                    // create MethodAnnotation to make the class name,
                    // method name, transfer parameter available
                    MethodAnnotation methodAnnotation = MethodAnnotation
                            .fromForeignMethod(mn.owner, mn.name,
                                    mn.desc, false);

                    // convert the MethodAnnotation to type method descriptor
                    MethodDescriptor md = DescriptorFactory.instance()
                            .getMethodDescriptor(methodAnnotation);

                    // eliminate the constructor name "init", "access"
                    if (!md.getName().contains("init")
                            && !md.getName().contains("access")) {

                        log("Added Method: " + md.getClassDescriptor().getClassName() + "." + md.getName());

                        // add to list "calledMethods"
                        calledMethods.add(md);
                    }
                }
            }

            super.visitEnd();
        }
    }

    @Override
    public void finishPass() {
//
    }

    @Override
    public MethodVisitor visitMethod(final int access1, final String name1,
            final String desc, final String signature1,
            final String[] exceptions) {
        return new MyMethodNode(access1, name1, desc, signature1, exceptions);
    }
}
