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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import edu.umd.cs.findbugs.annotations.Confidence;
import edu.umd.cs.findbugs.annotations.ExpectWarning;

/*
 * Just tests to make sure that "Main" is not threated as "main"
 *
 */
public class MainFindPrntlnSamples {

    @ExpectWarning(value = "PRINTLN", rank = 2, confidence = Confidence.MEDIUM, num = 38)
    void test1() throws FileNotFoundException {
        System.out.println();
        System.out.print("");
        System.out.print(true);
        System.out.print('c');
        System.out.print(new char[]{'c'});
        System.out.print(5d);
        System.out.print(5F);
        System.out.print(5);
        System.out.print(5L);
        System.out.print((Object)"");
        System.out.println("");
        System.out.println(true);
        System.out.println('c');
        System.out.println(new char[]{'c'});
        System.out.println(5d);
        System.out.println(5F);
        System.out.println(5);
        System.out.println(5L);
        System.out.println((Object)"");

        System.err.println();
        System.err.print("");
        System.err.print(true);
        System.err.print('c');
        System.err.print(new char[]{'c'});
        System.err.print(5d);
        System.err.print(5F);
        System.err.print(5);
        System.err.print(5L);
        System.err.print((Object)"");
        System.err.println("");
        System.err.println(true);
        System.err.println('c');
        System.err.println(new char[]{'c'});
        System.err.println(5d);
        System.err.println(5F);
        System.err.println(5);
        System.err.println(5L);
        System.err.println((Object)"");
//        e.printStackTrace(System.err);
//        e.printStackTrace(System.out);
//        e.printStackTrace(new PrintWriter(""));
    }

    @ExpectWarning(value = "PRINTLN", rank = 2, confidence = Confidence.MEDIUM, num = 3)
    void test11() throws FileNotFoundException {
        Exception e = null;
        e.printStackTrace();
        System.err.printf("");
        System.out.printf("");
    }

    @ExpectWarning(value = "PRINTLN", rank = 2, confidence = Confidence.MEDIUM, num = 2)
    void test2() throws FileNotFoundException {
        PrintStream ps = System.out;
        ps.println("");
        ps = System.err;
        ps.println("");

        // no warning
        ps = new PrintStream(new File(""));
        ps.println("");
    }

//    @ExpectWarning(value = "PRINTLN", rank = 2, confidence = Confidence.MEDIUM, num = 3)
//    void test3() {
//        System.out.println("dd" + getClass());
//        System.err.println("dd" + new Integer(1));
//        System.err.println(new Integer(1).intValue());
//    }

    @ExpectWarning(value = "PRINTLN", rank = 2, confidence = Confidence.MEDIUM, num = 4)
    static void test4() {
        System.out.println();
        System.out.println("");
        System.err.println();
        System.err.println("");
    }

    static boolean debFug = false;
    @ExpectWarning(value = "PRINTLN", rank = 2, confidence = Confidence.MEDIUM, num = 4)
    static void test5() {
        if(debFug){
            System.out.println();
            System.out.println("");
            System.err.println();
            System.err.println("");
        }
     // XXX local variable names are not working yet
//        boolean debug = false;
//        if(debug){
//            System.out.println("");
//            System.err.println();
//        }
    }

    private static boolean isDebFug() { return false; }
    @ExpectWarning(value = "PRINTLN", rank = 2, confidence = Confidence.MEDIUM, num = 5)
    void test6() {
        // XXX local variable names are not working yet
//        boolean debug = false;
//        if(debug){
//            System.out.println("");
//            System.err.println();
//        }
        if(isDebFug()){
            System.out.println();
            System.out.println("");
            System.err.println();
            System.err.println("");
            Exception e = null;
            e.printStackTrace();
//            e.printStackTrace(System.err);
//            e.printStackTrace(System.out);
        }
    }
}
