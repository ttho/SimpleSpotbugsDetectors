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

import java.util.HashMap;
import java.util.Map;

import edu.umd.cs.findbugs.classfile.MethodDescriptor;
import edu.umd.cs.findbugs.graph.AbstractGraph;

/**
 * Class representing the nested method call graph. Vertices represent methods.
 * Edges represent method calls
 */
public class MethodCallGraph extends
        AbstractGraph<MethodCallGraphEdge, MethodCallGraphVertex> {

    private final Map<MethodDescriptor, MethodCallGraphVertex> methodDescToVertex;

    public MethodCallGraph() {
        this.methodDescToVertex = new HashMap<MethodDescriptor, MethodCallGraphVertex>();
    }

    @Override
    public void addVertex(MethodCallGraphVertex v) {
        super.addVertex(v);
        methodDescToVertex.put(v.getMethodDescriptor(), v);
    }

    public MethodCallGraphVertex lookupVertex(MethodDescriptor methDesc) {
        return methodDescToVertex.get(methDesc);

    }

    @Override
    protected MethodCallGraphEdge allocateEdge(MethodCallGraphVertex source,
            MethodCallGraphVertex target) {
        return new MethodCallGraphEdge(source, target);
    }
}
