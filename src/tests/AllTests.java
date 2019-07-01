package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CommandParserTest.class, MovieSelectionTest.class, CommandExecutorTest.class })
public class AllTests {

}
