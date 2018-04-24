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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.CheckForNull;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LineNumberNode;

/**
 * Pattern matching for bytecode.
 *
 * Input:
 * <li> list of nodes representing class file
 * <li> list of {@link InstructionMatcher}'s representing a bytecode "pattern"
 *
 * Output:
 *
 */
public class InsnPattern {

    static final int TERMINATE = -1;
    static final int CONTINUE = 0;
    static final int MATCH = 1;
    static final int SKIP_TO_NEXT = 2;

    private final List<InstructionMatcher> pattern;


    public InsnPattern() {
        super();
        pattern = new ArrayList<InstructionMatcher>();
    }

    public InsnPattern add(InstructionMatcher node){
        pattern.add(node);
        return this;
    }

    public InsnMatcher matcher(){
        return new InsnMatcher();
    }

    public class InsnMatcher {
        private int current;
        private LineNumberNode currentLine;

        public int matches(AbstractInsnNode insnNode){
            if(current >= pattern.size()){
                return TERMINATE;
            }
            switch (insnNode.getType()) {
            case AbstractInsnNode.LINE:
                currentLine = (LineNumberNode) insnNode;
                return CONTINUE;
            case AbstractInsnNode.FRAME: //$FALL-THROUGH$
            case AbstractInsnNode.LABEL:
                return CONTINUE;
            case AbstractInsnNode.FIELD_INSN: //$FALL-THROUGH$
            case AbstractInsnNode.JUMP_INSN: //$FALL-THROUGH$
            case AbstractInsnNode.METHOD_INSN:  //$FALL-THROUGH$
            case AbstractInsnNode.TYPE_INSN:
            case AbstractInsnNode.INSN:
            case AbstractInsnNode.VAR_INSN:
            case AbstractInsnNode.LDC_INSN:
            case AbstractInsnNode.IINC_INSN:
            case AbstractInsnNode.INVOKE_DYNAMIC_INSN:
            case AbstractInsnNode.INT_INSN:
                InstructionMatcher matcher = pattern.get(current);
                boolean matches = matcher.matches(insnNode);
                if(!matcher.isInverse()){
                    if(matches){
                        return match(matcher);
                    }
                    return noMatch(matcher);
                }
                if(!matches){
                    return inverseNoMatch(insnNode);
                }
                int result = inverseMatch(matcher);
                if(result == CONTINUE){
                    return matches(insnNode);
                }
                return result;
            default:
                return CONTINUE;
            }
        }


        private int match(InstructionMatcher matcher) {
            current ++;
            if(current < pattern.size()){
                return CONTINUE;
            }
            current = 0;
            return MATCH;
        }

        private int noMatch(InstructionMatcher matcher) {
            if(current > 0) {
                current --;
            }
            if(current < pattern.size()){
                return CONTINUE;
            }
            current = 0;
            return TERMINATE;
        }

        /**
         * it IS a negative match - restart matching with next instruction
         */
        private int inverseNoMatch(AbstractInsnNode insnNode) {
            current ++;
            if(current < pattern.size()){
                current = 0;
                return SKIP_TO_NEXT;
            }
            current = 0;
            return TERMINATE;
        }

        /**
         * it is not a negative match - continue with current instruction on next pattern
         */
        private int inverseMatch(InstructionMatcher matcher) {
            current ++;
            if(current < pattern.size()){
                return CONTINUE;
            }
            current = 0;
            return TERMINATE;
        }

        public void reset() {
            current = 0;
        }

        @CheckForNull
        public LineNumberNode getLastLine() {
            return currentLine;
        }
    }

    public static InstructionMatcher or(InstructionMatcher ... nodes){
        return new Or(nodes);
    }
    public static InstructionMatcher and(InstructionMatcher ... nodes){
        return new And(nodes);
    }
    public static InstructionMatcher not(InstructionMatcher node){
        return new Not(node);
    }
    public static InstructionMatcher sequence(InstructionMatcher... nodes){
        return new Sequence(nodes);
    }

}
