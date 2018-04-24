/*******************************************************************************
 * Copyright (c) 2012 Andrey Loskutov.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributor:
 *   Andrey Loskutov - initial API and implementation
 *******************************************************************************/
package de.loskutov.fb.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.loskutov.fb.detectors.TestFindAssert;
import de.loskutov.fb.detectors.TestFindPrintln;
import de.loskutov.fb.detectors.TestFindPrintlnInner;
import de.loskutov.fb.detectors.TestFindPrintlnNoWarnings;
import de.loskutov.fb.detectors.TestFindPrintlnPrinter;
import de.loskutov.fb.detectors.TestFindPrintlnTricky;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    TestFindAssert.class,
    TestFindPrintln.class,
    TestFindPrintlnNoWarnings.class,
    TestFindPrintlnTricky.class,
    TestFindPrintlnInner.class,
    TestFindPrintlnPrinter.class,
})

public class DetectorTestSuite {

}
