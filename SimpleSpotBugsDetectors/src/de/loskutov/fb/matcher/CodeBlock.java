/*******************************************************************************
 * Copyright (c) 20133 Andrey Loskutov.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributor:
 *   Andrey Loskutov
 *******************************************************************************/
package de.loskutov.fb.matcher;

import java.util.HashSet;
import java.util.Set;

import org.objectweb.asm.tree.LabelNode;

public class CodeBlock {
    public int nextIdx;
    public LabelNode startLabel;
    public LabelNode targetLabel;
    public final Set<Integer> indices;

    public CodeBlock(LabelNode startLabel, LabelNode targetLabel) {
        super();
        this.startLabel = startLabel;
        this.targetLabel = targetLabel;
        indices = new HashSet<>();
    }
}
