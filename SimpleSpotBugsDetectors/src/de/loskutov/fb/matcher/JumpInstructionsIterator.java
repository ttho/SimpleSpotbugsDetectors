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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;

public class JumpInstructionsIterator {

    private final InsnList insList;
    private final LinkedList<CodeBlock> blocks;
    private final Map<Integer, CodeBlock> blockMap;
    public JumpInstructionsIterator(InsnList insList) {
        super();
        this.insList = insList;
        blocks = new LinkedList<>();
        blockMap = new HashMap<>();
    }

    private void init(){
        LabelNode prevLabel = null;
        CodeBlock curBlock = null;
//        int prevIdx = 0;
        Map<LabelNode, Integer> indices = new HashMap<>();
        for (int i = 0; i < insList.size(); i++) {
            AbstractInsnNode node = insList.get(i);
            if (node.getType() == LABEL) {
                prevLabel = (LabelNode) node;
//                prevIdx = i;
                indices.put(prevLabel, Integer.valueOf(i));
                if(curBlock != null) {
                    if (curBlock.targetLabel == node) {
//                        curBlock = null;
                        curBlock = new CodeBlock(prevLabel, null);
                    } else {
                        curBlock.indices.add(Integer.valueOf(i));
                    }
                } else {
                    curBlock = new CodeBlock(prevLabel, null);
                    curBlock.indices.add(Integer.valueOf(i));
                }
            } else if(node.getType() == JUMP_INSN){
                LabelNode targetLabel = ((JumpInsnNode) node).label;
                if(curBlock != null && curBlock.startLabel == prevLabel){
                    curBlock.targetLabel = targetLabel;
                } else {
                    curBlock = new CodeBlock(prevLabel, targetLabel);
                }
                int j = i - 1;
                curBlock.indices.add(Integer.valueOf(i));
                if(node.getPrevious() == prevLabel){
                    node = node.getPrevious();
                }
                while(node != null){
                    curBlock.indices.add(Integer.valueOf(j --));
                    if(node.getPrevious() == null || node.getPrevious().getType() == LABEL){
                        break;
                    }
                    node = node.getPrevious();
                }
                blocks.add(curBlock);
            } else {
                if(curBlock != null){
                    curBlock.indices.add(Integer.valueOf(i));
                }
            }
        }

        for (CodeBlock block : blocks) {
            block.nextIdx = indices.get(block.targetLabel).intValue();
            Set<Integer> set = block.indices;
            for (Integer integer : set) {
                blockMap.put(integer, block);
            }
        }
        if(blocks.isEmpty()){
            blocks.add(new CodeBlock(null, null));
        }
    }

    public int next(int start) {
        if(blocks.isEmpty()){
            init();
        }
        CodeBlock codeBlock = blockMap.get(Integer.valueOf(start));
        if(codeBlock == null){
            return -1;
        }
        return codeBlock.nextIdx;
    }



}
