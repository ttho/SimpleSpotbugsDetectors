/*******************************************************************************
 * Copyright (c) 2013 Andrey Loskutov.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributor:
 *   Andrey Loskutov
 *******************************************************************************/

package de.loskutov.fb.detectors.samples.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import edu.umd.cs.findbugs.annotations.NoWarning;

public class FindPrntlnSamplesNo {

    @NoWarning("PRINTLN")
    static void test1() throws FileNotFoundException {
        PrintStream ps = new PrintStream(new File(""));// System.out;
        ps.println("");
        ps.print("");
        ps.close();
    }

    boolean debug = false;
    boolean dump = false;
    boolean log = false;
    boolean trace = false;
    @NoWarning("PRINTLN")
    void test2() {
        if(debug){
            System.out.println();
            Exception e = null;
            e.printStackTrace();
            e.printStackTrace(System.err);
            e.printStackTrace(System.out);
        }
        if(dump){
            System.out.println();
        }
        if(trace){
            System.out.println();
        }
        if(log){
            System.out.println();
        }
    }

    static boolean isDebug2 = false;
    static boolean isDump2 = false;
    static boolean isTrace2 = false;
    static boolean isLog2 = false;
    @NoWarning("PRINTLN")
    void test3() {
        if(isDebug2){
            System.out.println();
        }
        if(isDump2){
            System.out.println();
        }
        if(isTrace2){
            System.out.println();
        }
        if(isLog2){
            System.out.println();
        }
    }

    private static boolean isDebug() { return false; }
    private static boolean isDump() { return false; }
    private static boolean isTrace() { return false; }
    private static boolean isLog() { return false; }
    @NoWarning("PRINTLN")
    void test4() {
        if(isDebug()){
            System.out.println();
        }
        if(isDump()){
            System.out.println();
        }
        if(isTrace()){
            System.out.println();
        }
        if(isLog()){
            System.out.println();
        }
    }


    private boolean isDebugEnabled() { return debug; }
    private boolean isDumpEnabled() { return debug; }
    private boolean isTraceEnabled() { return debug; }
    private boolean isLogEnabled() { return debug; }
    @NoWarning("PRINTLN")
    void test5() {
        if(isDebugEnabled()){
            System.out.println();
        }
        if(isDumpEnabled()){
            System.out.println();
        }
        if(isTraceEnabled()){
            System.out.println();
        }
        if(isLogEnabled()){
            System.out.println();
        }
    }

    @NoWarning("PRINTLN")
    void test6() {
     // XXX local variable names are not working yet
//        boolean debug = false;
//        if(debug){
//            System.out.println("");
//            System.err.println();
//        }
    }
    static boolean DEBUG_FP;
    @NoWarning("PRINTLN")
    void test7() {
        if(DEBUG_FP){
            System.out.println("" + Integer.valueOf(0));
        }
    }

    @NoWarning("PRINTLN")
    public static void main(String[] args) {
        System.out.println("hello");
    }
}
