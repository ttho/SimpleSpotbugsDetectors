/*******************************************************************************
 * Copyright (c) 2013 Andrey Loskutov.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributor:
 *   Andrey Loskutov
 *******************************************************************************/
package de.loskutov.fb.matcher;

import org.objectweb.asm.tree.AbstractInsnNode;

/**
 * @author andrei
 *
 */
public class Not extends InstructionMatcher {

    private final InstructionMatcher node;

    public Not(InstructionMatcher matchNode) {
        super();
        this.node = matchNode;
    }

    @Override
    public boolean isInverse() {
        return true;
    }

    @Override
    public boolean matches(AbstractInsnNode n) {
        return !node.matches(n);
    }

}
