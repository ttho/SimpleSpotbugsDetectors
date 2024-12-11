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
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LineNumberNode;

import de.loskutov.fb.matcher.InsnPattern.InsnMatcher;

/**
 * @author andrei
 */
public class DefaultInstructionsIterator {

    private final InsnList insList;
    private final InsnMatcher matcher;
    private final JumpInstructionsIterator jumps;

    public DefaultInstructionsIterator(InsnPattern pattern, InsnList insList) {
        super();
        this.insList = insList;
        matcher = pattern.matcher();
        jumps = new JumpInstructionsIterator(insList);
    }

    public int next(int start) {
        matcher.reset();
        for (int i = start; i < insList.size(); i++) {
            AbstractInsnNode insnNode = insList.get(i);
            int result = matcher.matches(insnNode);
            if(result == InsnPattern.TERMINATE){
                return -1;
            }
            if(result == InsnPattern.CONTINUE){
                continue;
            }
            if(result == InsnPattern.SKIP_TO_NEXT){
                i = skipToNext(i);
                continue;
            }
            // match! return instruction nr + 1
            return i+1;
        }
        return -1;
    }

    public int skipToNext(int start) {
        int next = jumps.next(start);
        if(next < 0 || next <= start) {
            return start + 2;
        }
        return next;
    }

    public int getLastLine(){
        LineNumberNode lvn = matcher.getLastLine();
        if(lvn == null){
            return -1;
        }
        return lvn.line;
    }

    public InsnMatcher getMatcher() {
        return matcher;
    }

    public boolean hasMatch(){
        return getLastLine() != -1;
    }
}
