/* Generated by YAKINDU Statechart Tools code generator. */
package org.yakindu.sct.generator.cpp.test;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.yakindu.sct.generator.c.gtest.GTest;
import org.yakindu.sct.generator.c.gtest.GTestRunner;
import org.yakindu.sct.generator.c.gtest.GTestHelper;

@GTest(
	statechartBundle = "org.yakindu.sct.test.models",
	sourceFile = "gtests/EntryChoiceTest/EntryChoiceTest.cc",
	program = "gtests/EntryChoiceTest/EntryChoice",
	model = "testmodels/SCTUnit/EntryChoice.sct",
	additionalFilesToCopy = {
		"libraryTarget/sc_runner.h",
		"libraryTarget/sc_runner.cpp"
	},
	additionalFilesToCompile = {
		"EntryChoice.cpp",
		"sc_runner.cpp"
	}
)
@RunWith(GTestRunner.class)
public class EntryChoiceTest {
protected final GTestHelper helper = new GTestHelper(this);

	@Before
	public void setUp() {
		helper.generate();
		helper.compile();
	}

}
