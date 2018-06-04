/*******************************************************************************
 * Copyright (c) 2013 Andrey Loskutov.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributor:
 *   Andrey Loskutov
 *******************************************************************************/

package de.loskutov.fb.detectors;

import de.loskutov.fb.annotations.DetectorUnderTest;
import de.loskutov.fb.detectors.samples.FindPrntlnSamples;
import de.loskutov.fb.detectors.samples.FindPrntlnSamplesDebugger;
import de.loskutov.fb.detectors.samples.MainFindPrntlnSamples;
import de.loskutov.fb.test.DetectorsTest;

@DetectorUnderTest(value = FindPrintlnDetector.class, samples = { FindPrntlnSamples.class,
        FindPrntlnSamplesDebugger.class, MainFindPrntlnSamples.class,
        de.loskutov.fb.detectors.samples.main.FindPrntlnSamples.class })
public class TestFindPrintln extends DetectorsTest {
    //
}
