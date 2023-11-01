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
import de.loskutov.fb.annotations.NoWarningsTest;
import de.loskutov.fb.detectors.samples.FindJunit4TestSamples2;
import de.loskutov.fb.detectors.samples.FindJunit4TestSuperClass;
import de.loskutov.fb.detectors.samples.FindJunit4TestSuperClass2;
import de.loskutov.fb.test.DetectorsTest;

@DetectorUnderTest(value = FindJunit4AnnotationDetector.class, samples =
{ FindJunit4TestSamples2.class, FindJunit4TestSuperClass.class, FindJunit4TestSuperClass2.class })
@NoWarningsTest
public class TestFindJunit4AnnotationNo extends DetectorsTest {
    //
}
