/*******************************************************************************
 * Copyright (c) 2023 Andrey Loskutov.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributor:
 *   Andrey Loskutov - initial API and implementation
 *******************************************************************************/
package de.loskutov.fb.detectors.samples;


import org.junit.Test;

import edu.umd.cs.findbugs.annotations.Confidence;
import edu.umd.cs.findbugs.annotations.NoWarning;

public class FindJunit4TestSamples2 extends FindJunit4TestSuperClass {

    /* no warning because it extends class annotated with runwith annotation */
    @NoWarning(value = "JUNIT4TEST", rank = 2, confidence = Confidence.MEDIUM, num = 1)
    @Test
    public void annotatedWithJUnit4TestAnnotation() {}
}
