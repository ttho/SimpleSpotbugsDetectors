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

public class Trace {
    public static boolean DEBUG = false;

    public static void log(String message) {
        if (DEBUG) {
            System.out.println(message);
        }
    }

    public static void log(Throwable t) {
        if (DEBUG) {
            t.printStackTrace();
        }
    }
}
