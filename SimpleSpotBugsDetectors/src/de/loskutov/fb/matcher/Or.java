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

public class Or extends InstructionMatcher {

    private final InstructionMatcher[] nodes;

    public Or(InstructionMatcher ...matchNodes) {
        super();
        this.nodes = matchNodes;
    }

    @Override
    public boolean matches(AbstractInsnNode n) {
        if(nodes == null || nodes.length == 0){
            return false;
        }
        for (InstructionMatcher node : nodes) {
            if(node.matches(n)){
                return true;
            }
        }
        return false;
    }
}
