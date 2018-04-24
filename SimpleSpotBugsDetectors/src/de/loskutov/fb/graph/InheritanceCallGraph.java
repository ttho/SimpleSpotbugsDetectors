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

import edu.umd.cs.findbugs.classfile.ClassDescriptor;
import edu.umd.cs.findbugs.graph.AbstractGraph;

/**
 * Class representing the inheritance call graph. Vertices represent classes.
 * Edges represent method calls
 */
public class InheritanceCallGraph extends
        AbstractGraph<InheritanceCallGraphEdge, InheritanceCallGraphVertex> {

    private final Map<ClassDescriptor, InheritanceCallGraphVertex> classDescToVertex;

    public InheritanceCallGraph() {
        this.classDescToVertex = new HashMap<ClassDescriptor, InheritanceCallGraphVertex>();
    }

    @Override
    public void addVertex(InheritanceCallGraphVertex v) {
        super.addVertex(v);
        classDescToVertex.put(v.getClassDescriptor(), v);
    }

    public InheritanceCallGraphVertex lookupVertex(ClassDescriptor classDesc) {
        return classDescToVertex.get(classDesc);
    }

    @Override
    protected InheritanceCallGraphEdge allocateEdge(
            InheritanceCallGraphVertex source, InheritanceCallGraphVertex target) {
        return new InheritanceCallGraphEdge(source, target);
    }
}
