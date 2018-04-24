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
 */
public abstract class InstructionMatcher {
    private int times;

    private int type;

    protected int opcode;

    public InstructionMatcher() {
        super();
        times = 1;
        setType(-1);
    }

    public boolean isInverse(){
        return false;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getOpcode() {
        return opcode;
    }

    public void setOpcode(int opcode) {
        this.opcode = opcode;
    }

    public boolean matches(AbstractInsnNode n1) {
        int type2 = getType();
        if (type2 != -1 && n1.getType() != type2) {
            return false;
        }
        int opcode2 = getOpcode();
        if (opcode2 != -1 && n1.getOpcode() != opcode2) {
            return false;
        }
        return true;
    }

    static boolean nullOrEquals(String s1, String s2) {
        if (s1 == null || s2 == null) {
            return true;
        }
        return s1.equals(s2);
    }

}
