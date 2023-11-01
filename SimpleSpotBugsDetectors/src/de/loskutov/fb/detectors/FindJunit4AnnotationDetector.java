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

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import org.apache.bcel.Repository;
import org.apache.bcel.classfile.AnnotationEntry;
import org.apache.bcel.classfile.JavaClass;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.classfile.Global;
import edu.umd.cs.findbugs.classfile.IAnalysisCache;

public class FindJunit4AnnotationDetector extends AbstractJunitAnnotationDetector {

    public static class RunWithDatabase {
    	/** key is dotted class name, value is boolean if the class is annotated with "runwith" annotation */
        final Map<String, Boolean> knownClasses = new ConcurrentHashMap<>();
    }
    static final String RUN_WITH = "Lorg/junit/runner/RunWith;";


    public FindJunit4AnnotationDetector(BugReporter bugReporter) {
        super(bugReporter);
        Global.getAnalysisCache().eagerlyPutDatabase(RunWithDatabase.class, new RunWithDatabase());
    }

    @Override
    String getJunitAnnotationSignature() {
        return "Lorg/junit/Test;";
    }

    @Override
    String getBugType() {
        return "JUNIT4TEST";
    }

    @Override
    protected void checkAnnotations(ClassNode clazzNode, MethodNode node, Consumer<BugInstance> reporter) {
        Boolean match = checkAnnotationDatabase(clazzNode.name);
        if(match != null && match.booleanValue()) {
            return;
        }
        List<AnnotationNode> annotations = node.visibleAnnotations;
        if(annotations == null) {
            return;
        }
        // Check @RunWith if we have no match in the database yet
        if(match == null && hasRunWithAnnotation(clazzNode)) {
            return;
        }

        final String junitAnnotationSignature = getJunitAnnotationSignature();
        for (AnnotationNode annotationNode : annotations) {
            if(junitAnnotationSignature.equals(annotationNode.desc)) {
                reporter.accept(createBug(node));
                break;
            }
        }
    }

    private boolean hasRunWithAnnotation(ClassNode clazzNode) {
        List<AnnotationNode> annotations = clazzNode.visibleAnnotations;
        if(annotations == null) {
            boolean hasRunWithAnnotation = hasRunWithAnnotation(clazzNode.superName);
            putAnnotationDatabase(clazzNode.name, hasRunWithAnnotation);
            return hasRunWithAnnotation;
        }
        for (AnnotationNode annotation : annotations) {
            if(RUN_WITH.equals(annotation.desc)) {
                putAnnotationDatabase(clazzNode.name, Boolean.TRUE);
                return true;
            }
        }
        putAnnotationDatabase(clazzNode.name, Boolean.FALSE);
        return false;
    }

    private boolean hasRunWithAnnotation(String superName) {
        Boolean match = checkAnnotationDatabase(superName);
        if(match != null) {
            return match.booleanValue();
        }
        try {
            JavaClass superClass = Repository.lookupClass(superName);
            return hasRunWithAnnotation(superClass);
        } catch (ClassNotFoundException e) {
            Global.getAnalysisCache().getErrorLogger().reportMissingClass(e);
        }
        return false;
    }

    @CheckForNull
    private Boolean checkAnnotationDatabase(String superName) {
        IAnalysisCache analysisCache = Global.getAnalysisCache();
        RunWithDatabase runWithDatabase = analysisCache.getDatabase(RunWithDatabase.class);
        return runWithDatabase.knownClasses.get(superName.replace('/', '.'));
    }

    @CheckForNull
    private void putAnnotationDatabase(String superName, Boolean value) {
        IAnalysisCache analysisCache = Global.getAnalysisCache();
        RunWithDatabase runWithDatabase = analysisCache.getDatabase(RunWithDatabase.class);
        runWithDatabase.knownClasses.put(superName.replace('/', '.'), value);
    }

    private boolean hasRunWithAnnotation(JavaClass clazz) {
        Boolean match = checkAnnotationDatabase(clazz.getClassName());
        if(match != null) {
            return match.booleanValue();
        }
        try {
            AnnotationEntry[] annotations = clazz.getAnnotationEntries();
            for (AnnotationEntry annotation : annotations) {
                if(RUN_WITH.equals(annotation.getAnnotationType())) {
                    putAnnotationDatabase(clazz.getClassName(), Boolean.TRUE);
                    return true;
                }
            }
            JavaClass superClass = clazz.getSuperClass();
            if(superClass == null || superClass.getClassName().equals("java.lang.Object")){
                if(superClass != null) {
                    putAnnotationDatabase(superClass.getClassName(), Boolean.FALSE);
                }
                return false;
            }
            boolean hasRunWithAnnotation = hasRunWithAnnotation(superClass);
            putAnnotationDatabase(clazz.getClassName(), hasRunWithAnnotation);
            return hasRunWithAnnotation;
        } catch (ClassNotFoundException e) {
            Global.getAnalysisCache().getErrorLogger().reportMissingClass(e);
        }
        putAnnotationDatabase(clazz.getClassName(), Boolean.FALSE);
        return false;
    }


}
