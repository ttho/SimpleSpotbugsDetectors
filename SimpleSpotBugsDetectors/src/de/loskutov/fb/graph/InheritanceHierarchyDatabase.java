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

import static de.loskutov.fb.detectors.Trace.*;

import java.util.Iterator;

import edu.umd.cs.findbugs.classfile.ClassDescriptor;

/**
 * Database which stores all classes with the associated super class
 */
public class InheritanceHierarchyDatabase {

    private final InheritanceCallGraph inheritanceGraph;

    public InheritanceHierarchyDatabase() {
        inheritanceGraph = new InheritanceCallGraph();
    }

    /**
     *
     * Look up vertex and add the vertex to the actual method call graph
     *
     * @param classDesc
     *            actual class descriptor
     * @return actual vertex
     */
    public InheritanceCallGraphVertex findVertex(ClassDescriptor classDesc) {
        InheritanceCallGraphVertex vertex;
        vertex = inheritanceGraph.lookupVertex(classDesc);
        if (vertex == null) {
            vertex = new InheritanceCallGraphVertex();
            vertex.setClassDescriptor(classDesc);
            inheritanceGraph.addVertex(vertex);
        }
        return vertex;
    }

    public void createInheritanceEdge(InheritanceCallGraphVertex source,
            InheritanceCallGraphVertex target) {
        inheritanceGraph.createEdge(source, target);
    }

    public void printGraphToConsole() {
        Iterator<InheritanceCallGraphVertex> totalIterator = inheritanceGraph
                .vertexIterator();

        log("--------------------- PRINTING OUT INHERITANCE GRAPH ---------------------");
        while (totalIterator.hasNext()) {
            InheritanceCallGraphVertex actualVertex = totalIterator.next();
            log("ActualVertex: "
                    + actualVertex.getClassDescriptor().toString());

            Iterator<InheritanceCallGraphVertex> iterator_Predecessor = inheritanceGraph
                    .predecessorIterator(actualVertex);
            while (iterator_Predecessor.hasNext()) {
                log("Predecessors: "
                        + iterator_Predecessor.next().getClassDescriptor()
                                .toString());
            }

            Iterator<InheritanceCallGraphVertex> iterator_Successor = inheritanceGraph
                    .successorIterator(actualVertex);
            while (iterator_Successor.hasNext()) {

                log("Sucessors: "
                        + iterator_Successor.next().getClassDescriptor()
                                .toString());
            }
        }
        log("--------------------- FINISHED PRINTING ---------------------");
    }
}
