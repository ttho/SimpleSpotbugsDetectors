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
import static org.objectweb.asm.tree.AbstractInsnNode.*;

import org.objectweb.asm.tree.AbstractInsnNode;

public class And extends InstructionMatcher {

    private final InstructionMatcher[] matchers;

    public And(InstructionMatcher ...matchNodes) {
        super();
        this.matchers = matchNodes;
    }

    @Override
    public boolean matches(AbstractInsnNode n) {
        if(matchers == null || matchers.length == 0){
            return false;
        }
        for (int i = 0; i < matchers.length; i++, n = next(n)) {
            if(n == null){
                return false;
            }
            InstructionMatcher matcher = matchers[i];
            if(!matcher.matches(n)){
                return false;
            }
        }
        return true;
    }

    private AbstractInsnNode next(AbstractInsnNode n) {
        AbstractInsnNode next = n.getNext();
        if(next == null){
            return next;
        }
        int type = next.getType();
        if(type == FRAME || type == LABEL || type == LINE){
            return next(next);
        }
        return next;
    }

}
