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

import static org.junit.Assert.assertFalse;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.loskutov.fb.annotations.DetectorUnderTest;
import de.loskutov.fb.annotations.NoWarningsTest;
import edu.umd.cs.findbugs.BugAnnotation;
import edu.umd.cs.findbugs.BugCollectionBugReporter;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugRanker;
import edu.umd.cs.findbugs.DetectorFactory;
import edu.umd.cs.findbugs.DetectorFactoryCollection;
import edu.umd.cs.findbugs.FindBugs2;
import edu.umd.cs.findbugs.Plugin;
import edu.umd.cs.findbugs.PluginException;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.Project;
import edu.umd.cs.findbugs.ProjectStats;
import edu.umd.cs.findbugs.StringAnnotation;
import edu.umd.cs.findbugs.annotations.ExpectWarning;
import edu.umd.cs.findbugs.annotations.NoWarning;
import edu.umd.cs.findbugs.config.UserPreferences;
import edu.umd.cs.findbugs.detect.CheckExpectedWarnings;
import edu.umd.cs.findbugs.log.Profiler;
import edu.umd.cs.findbugs.plugins.DuplicatePluginIdException;

/**
 * checks if there are any unexpected bugs for given detector.
 *
 * The results are checked for the unexpected bugs of type
 * FB_MISSING_EXPECTED_WARNING or FB_UNEXPECTED_WARNING.
 *
 * @see ExpectWarning
 * @see NoWarning
 */
public abstract class DetectorsTest {

    private static final String FB_UNEXPECTED_WARNING = "FB_UNEXPECTED_WARNING";
    private static final String FB_MISSING_EXPECTED_WARNING = "FB_MISSING_EXPECTED_WARNING";

    private BugCollectionBugReporter bugReporter;

    private FindBugs2 engine;

    private static boolean pluginLoaded;

    @BeforeClass
    public static void beforeClass() {
        if(pluginLoaded) {
            return;
        }
        loadFindbugsPlugin();
        pluginLoaded = true;
    }

    /**
     * Loads the default detectors from findbugs.jar, to isolate the test from
     * others that use fake plugins.
     */
    private static void loadFindbugsPlugin() {
        addCustomPlugin(new File("../SimpleSpotBugsDetectors/bin").toURI());
    }

    private static void addCustomPlugin(URI uri) {
        try {
            Plugin fbPlugin = Plugin.addCustomPlugin(uri, DetectorTestSuite.class.getClassLoader());
            if(fbPlugin != null) {
                // TODO line below required to enable this *optional* plugin
                // but it should be taken by FB core from the findbugs.xml,
                // which currently only works for *core* plugins only
                fbPlugin.setGloballyEnabled(true);
            }
        } catch (PluginException e) {
            System.err.println("Failed to load plugin for custom detector: " + uri);
        } catch (DuplicatePluginIdException e) {
            System.err.println(e.getPluginId() + " already loaded from " + e.getPreviouslyLoadedFrom()
                    + ", ignoring: " + uri);
        }
    }

    @Before
    public void setUpForTest() throws Exception {
        Project project = new Project();
        DetectorFactoryCollection.resetInstance(null);
        DetectorFactoryCollection dfc = DetectorFactoryCollection.instance();

        engine = new FindBugs2();
        project.setProjectName(getClass().getSimpleName());
        engine.setProject(project);
        engine.setDetectorFactoryCollection(dfc);

        bugReporter = new BugCollectionBugReporter(project);
        bugReporter.setPriorityThreshold(Priorities.LOW_PRIORITY);

        engine.setBugReporter(bugReporter);

        UserPreferences preferences = UserPreferences.createDefaultUserPreferences();

        Iterable<DetectorFactory> factories = dfc.getFactories();
        for (DetectorFactory factory : factories) {
            if(isUnrelatedDetector(factory)){
                preferences.enableDetector(factory, false);
            }
        }

        preferences.getFilterSettings().clearAllCategories();
        preferences.getFilterSettings().setMinRank(BugRanker.VISIBLE_RANK_MAX);
        preferences.setUserDetectorThreshold(Priorities.LOW_PRIORITY);
        engine.setUserPreferences(preferences);
        DetectorUnderTest detectorUnderTest = getClass().getAnnotation(DetectorUnderTest.class);

        DetectorFactory testDetector = dfc.getFactory(detectorUnderTest.value().getSimpleName());
        preferences.enableDetector(testDetector, true);

        DetectorFactory checkExpectedWarnings = dfc.getFactory(CheckExpectedWarnings.class.getSimpleName());
        preferences.enableDetector(checkExpectedWarnings, true);

        Class<?>[] samples = detectorUnderTest.samples();
        for (Class<?> class1 : samples) {
            String simpleName = class1.getSimpleName();
            URL resource = class1.getResource(simpleName + ".class");
            if(resource == null){
                String fname = class1.getName();
                simpleName = fname.substring(class1.getPackage().getName().length() + 1);
                String[] names = simpleName.split("\\$");
                StringBuilder name = new StringBuilder();
                for (int i = 0; i < names.length; i++) {
                    String cname = names[i];
                    if(i > 0){
                        name.append("$");
                    }
                    name.append(cname);
                    resource = class1.getResource(name + ".class");
                    if(resource != null) {
                        if(i == names.length - 1) {
                            project.addFile(new File(resource.getPath()).getAbsolutePath());
                        } else {
                            project.addAuxClasspathEntry(new File(resource.getPath()).getAbsolutePath());
                        }
                    }
                }
            } else {
                project.addFile(new File(resource.getPath()).getAbsolutePath());
            }
        }
    }

    private static boolean isUnrelatedDetector(DetectorFactory factory) {
        boolean unrelated = factory.isReportingDetector();
//        if(!unrelated){
//            if(factory.getShortName().equals("ExplicitSerialization")
//                    || factory.getShortName().equals("EqualsOperandShouldHaveClassCompatibleWithThis")
//                    || factory.getShortName().equals("FunctionsThatMightBeMistakenForProcedures")
//                    || factory.getShortName().equals("OverridingEqualsNotSymmetrical")
//                    ){
//                return true;
//            }
//        }
        return unrelated;
    }

    @Test
    public void testAllRegressionFiles() throws IOException, InterruptedException {
        engine.execute();

        boolean noExpected = getClass().getAnnotation(NoWarningsTest.class) != null;
        Collection<BugInstance> bugs = bugReporter.getBugCollection().getCollection();
        boolean empty = bugs.isEmpty();
        if(noExpected) {
            // If there are too many bugs, then something's wrong
            dumpBugs(bugs);
            assertFalse("Some bugs were reported. Something is wrong!", !empty);
        } else {
            // If there are zero bugs, then something's wrong
            assertFalse("No bugs were reported. Something is wrong!", empty);
        }
    }

    @After
    public void tearDown() {
        if(false) {
            printStatistics();
        }
        checkForUnexpectedBugs();
        engine.dispose();
    }

    private void printStatistics() {
        ProjectStats stats = bugReporter.getBugCollection().getProjectStats();
        Profiler profiler = stats.getProfiler();
        PrintStream printStream;
        try {
            printStream = new PrintStream(System.out, false, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            // can never happen with UTF-8
            return;
        }

        printToStream("\n" + bugReporter.getProject().getProjectName());
        printToStream("\nTotal time:");
        profiler.report(new Profiler.TotalTimeComparator(profiler), new Profiler.FilterByTime(10000000), printStream);

        printToStream("\nTotal calls:");
        int numClasses = stats.getNumClasses();
        if(numClasses > 0) {
            profiler.report(new Profiler.TotalCallsComparator(profiler), new Profiler.FilterByCalls(numClasses),
                    printStream);

            printToStream("\nTime per call:");
            profiler.report(new Profiler.TimePerCallComparator(profiler),
                    new Profiler.FilterByTimePerCall(10000000 / numClasses), printStream);
        }

    }

    void printToStream(String message) {
        System.out.print(message);
        System.out.print("\n");
    }

    public void checkForUnexpectedBugs() {
        List<BugInstance> unexpectedBugs = new ArrayList<BugInstance>();
        for (BugInstance bug : bugReporter.getBugCollection()) {
            if (isUnexpectedBug(bug)) {
                unexpectedBugs.add(bug);
            }
        }

        if (!unexpectedBugs.isEmpty()) {
            dumpBugs(unexpectedBugs);
            Assert.fail("Unexpected bugs (" + unexpectedBugs.size() + "):" + getBugsLocations(unexpectedBugs));
        }
    }

    private static void dumpBugs(Collection<BugInstance> bugs) {
        for (BugInstance bug : bugs) {
            System.out.println(bug.getMessage());
            List<? extends BugAnnotation> annotations = bug.getAnnotations();
            for (BugAnnotation annotation : annotations) {
                System.out.println(annotation);
            }
        }
    }

    /**
     * Returns a printable String concatenating bug locations.
     */
    private static String getBugsLocations(List<BugInstance> unexpectedBugs) {
        StringBuilder message = new StringBuilder();
        for (BugInstance bugInstance : unexpectedBugs) {
            message.append("\n");
            if (bugInstance.getBugPattern().getType().equals(FB_MISSING_EXPECTED_WARNING)) {
                message.append("missing ");
            } else {
                message.append("unexpected ");
            }
            StringAnnotation pattern = (StringAnnotation) bugInstance.getAnnotations().get(2);
            message.append(pattern.getValue());
            message.append(" ");
            message.append(bugInstance.getPrimarySourceLineAnnotation());
        }
        return message.toString();
    }

    /**
     * Returns if a bug instance is unexpected for this test.
     */
    private static boolean isUnexpectedBug(BugInstance bug) {
        return FB_MISSING_EXPECTED_WARNING.equals(bug.getType()) || FB_UNEXPECTED_WARNING.equals(bug.getType());
    }



}
