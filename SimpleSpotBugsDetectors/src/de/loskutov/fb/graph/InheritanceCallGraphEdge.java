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

import edu.umd.cs.findbugs.graph.AbstractEdge;

/**
 * Class representing a edge in the inheritance call graph e.g. a super class
 */
public class InheritanceCallGraphEdge extends
        AbstractEdge<InheritanceCallGraphEdge, InheritanceCallGraphVertex> {

    public InheritanceCallGraphEdge(InheritanceCallGraphVertex source,
            InheritanceCallGraphVertex target) {
        super(source, target);
    }
}
