/*******************************************************************************
 * Copyright (c) 2013 Andrey Loskutov.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributor:
 *   Andrey Loskutov
 *******************************************************************************/

package de.loskutov.fb.detectors.samples;

import edu.umd.cs.findbugs.annotations.Confidence;
import edu.umd.cs.findbugs.annotations.ExpectWarning;
import edu.umd.cs.findbugs.annotations.NoWarning;

public class FindPrntlnSamplesTricky {

    // LaunchBrowser.desktopException.printStackTrace(writer);
//    if (SystemProperties.ASSERTIONS_ENABLED)
//        e.printStackTrace();
    // if(REPORT)
//    if (PROGRESS)
    //if (PROGRESS || LIST_ORDER) {

    // Project.java:
//    public boolean add(WorkListItem item) {
//        if (DEBUG) {
//            System.out.println("Adding " + item.getURL().toString());
//        }
//        if (!addedSet.add(item.getURL().toString())) {
//            if (DEBUG) {
//                System.out.println("\t==> Already processed");
//            }
//            return false;
//        }
//
//        itemList.add(item);
//        return true;
//    }

//    public void analyzeMethod() throws CheckedAnalysisException {
//        if (DEBUG_METHOD != null && !methodDescriptor.getName().equals(DEBUG_METHOD)) {
//            return;
//        }
//
//        if (DEBUG) {
//            System.out.println("*** Analyzing method " + methodDescriptor);
//        }

//    private void applyPossibleObligationTransfers() {
//        for (PossibleObligationTransfer transfer : transferList) {
//            if (DEBUG_FP) {
//                System.out.println("Checking possible transfer " + transfer + "...");
//            }
//class SourceFinder
//    private static final boolean DEBUG = SystemProperties.getBoolean("srcfinder.debug");
//    private static class DirectorySourceRepository implements SourceRepository {
//        public boolean contains(String fileName) {
//            File file = new File(getFullFileName(fileName));
//            boolean exists = file.exists();
//            if (DEBUG)
//    L2
//    LINENUMBER 114 L2
//    INVOKESTATIC SourceFinder.access$0() : boolean
//    IFEQ L3
//   L4

    native boolean isDebug();
    native boolean isDeFug();

    private static boolean DEBUG = true;
    private static boolean DEBUG_1 = true;

    @NoWarning("PRINTLN")
    void test0() {
        if(isDebug()){
            System.err.println(); // warn
            if(isDeFug()) {
                System.out.println(); // warn
                if(isDebug()){
                    System.out.println();
                }
            }
            System.out.println(""); // warn
            if(isDebug()){
                System.out.println();
            }
            Exception e = null;
            e.printStackTrace(System.err); // warn
            System.err.println(); // warn
        }
    }

    @ExpectWarning(value = "PRINTLN", rank = 2, confidence = Confidence.MEDIUM, num = 5)
    void test1() {
        if(isDeFug()){
            System.err.println(); // warn
            if(isDeFug()) {
                System.out.println(); // warn
                if(isDebug()){
                    System.out.println();
                }
            }
            System.out.println(""); // warn
            if(isDebug()){
                System.out.println();
            }
            Exception e = null;
            e.printStackTrace(System.err); // warn
            System.err.println(); // warn
        }
    }

    @ExpectWarning(value = "PRINTLN", rank = 2, confidence = Confidence.MEDIUM, num = 5)
    void test2() {
        if(isDebug()){
            System.out.println();
        }
        System.err.println(); // warn
        if(isDeFug()) {
            System.out.println(); // warn
            if(isDebug()){
                System.out.println();
            }
        }
        System.out.println(""); // warn
        if(isDebug()){
            System.out.println();
        }
        Exception e = null;
        e.printStackTrace(System.err); // warn
        System.err.println(); // warn
    }

    public static class Inner {

        @NoWarning("PRINTLN")
        void test3() {
            if(DEBUG) {
                System.out.println("");
            }
            if(DEBUG_1) {
                System.out.println("");
            }
        }
    }

    public static class Printer {

        @NoWarning("PRINTLN")
        void test3() {
            System.out.println("");
        }
    }

}
