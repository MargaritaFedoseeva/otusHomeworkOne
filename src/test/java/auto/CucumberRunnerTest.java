package auto;

import org.junit.platform.suite.api.*;

@Suite
@IncludeEngines("cucumber")
@SelectPackages("features")
@ConfigurationParameter(key = "cucumber.glue", value = "auto.steps, auto.hooks, factory")
public class CucumberRunnerTest {
}
