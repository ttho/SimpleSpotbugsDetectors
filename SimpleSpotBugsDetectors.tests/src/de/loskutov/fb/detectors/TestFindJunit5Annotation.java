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

import de.loskutov.fb.annotations.DetectorUnderTest;
import de.loskutov.fb.detectors.samples.FindJunit5TestSamples;
import de.loskutov.fb.test.DetectorsTest;

@DetectorUnderTest(value=FindJunit5AnnotationDetector.class, samples = { FindJunit5TestSamples.class })
public class TestFindJunit5Annotation extends DetectorsTest {
    //
}
