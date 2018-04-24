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

import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector2;
import edu.umd.cs.findbugs.classfile.ClassDescriptor;
import edu.umd.cs.findbugs.classfile.Global;

public class NativeMethodsDatabase implements Detector2 {

    public NativeMethodsDatabase(BugReporter br) {
        super();
    }

    @Override
    public void visitClass(ClassDescriptor classDescriptor) {
        // TODO Auto-generated method stub

    }

    @Override
    public void finishPass() {
        NativeMethodsDatabase database = Global.getAnalysisCache().getOptionalDatabase(NativeMethodsDatabase.class);
        if(database == null) {
            Global.getAnalysisCache().eagerlyPutDatabase(NativeMethodsDatabase.class, this);
        }
    }

    @Override
    public String getDetectorClassName() {
        return getClass().getName();
    }

}
