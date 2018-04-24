/*******************************************************************************
 * Copyright (c) 2013 Andrey Loskutov.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributor:
 *   Andrey Loskutov
 *******************************************************************************/

package de.loskutov.fb.graph;

import edu.umd.cs.findbugs.classfile.MethodDescriptor;
import edu.umd.cs.findbugs.graph.AbstractVertex;

/**
 * Class representing a vertex in the nested method call graph e.g. a method
 */
public class MethodCallGraphVertex extends
        AbstractVertex<MethodCallGraphEdge, MethodCallGraphVertex> {

    private MethodDescriptor methodDesc;

    public MethodDescriptor getMethodDescriptor() {
        return methodDesc;
    }

    public void setMethodDescriptor(MethodDescriptor descriptor) {
        methodDesc = descriptor;
    }
}
