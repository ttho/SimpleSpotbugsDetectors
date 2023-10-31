/*******************************************************************************
 * Copyright (c) 2023 Andrey Loskutov.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributor:
 *   Andrey Loskutov
 *******************************************************************************/

package de.loskutov.fb.detectors;

import edu.umd.cs.findbugs.BugReporter;

public class FindJunit4AnnotationDetector extends AbstractJunitAnnotationDetector {

    public FindJunit4AnnotationDetector(BugReporter bugReporter) {
        super(bugReporter);
    }

    @Override
    String getJunitAnnotationSignature() {
        return "Lorg/junit/Test;";
    }

    @Override
    String getBugType() {
        return "JUNIT4TEST";
    }

}
