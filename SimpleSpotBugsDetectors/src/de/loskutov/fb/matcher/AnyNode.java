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

public class AnyNode extends InstructionMatcher {

    private final int count;
    private final Integer[] types;

    /**
     *
     * @param count
     * @param types instruction types, as in {@link AbstractInsnNode#getType()}
     */
    public AnyNode(int count, Integer ... types) {
        super();
        this.count = count;
        this.types = types;
    }

    @Override
    public boolean matches(AbstractInsnNode n) {
        if(count <= 0 || types == null || types.length == 0){
            return true;
        }
        for (Integer type : types) {
            if(type.intValue() == n.getType()) {
                return true;
            }
        }
        return false;
    }

    public int getCount() {
        return count;
    }

}
