package com.phoenixnap.oss.ramlplugin.raml2code.github;

import org.jsonschema2pojo.AnnotationStyle;
import org.junit.Test;

import com.phoenixnap.oss.ramlplugin.raml2code.plugin.TestConfig;
import com.phoenixnap.oss.ramlplugin.raml2code.rules.GitHubAbstractRuleTestBase;
import com.phoenixnap.oss.ramlplugin.raml2code.rules.Spring4ControllerDecoratorRule;

/**
 * @author aleksandars
 * @since 2.0.5
 */
public class Issue283RulesTest extends GitHubAbstractRuleTestBase {

	@Test
	public void check_query_params_from_trait() throws Exception {
		TestConfig.setAnnotationStyle(AnnotationStyle.JACKSON2);
		loadRaml("issue-283.raml");
		rule = new Spring4ControllerDecoratorRule();
		rule.apply(getControllerMetadata(), jCodeModel);
		verifyGeneratedCode("Issue283Spring4ControllerDecorator");
	}

	@Test
	public void check_query_params_from_trait_gson() throws Exception {
		TestConfig.setAnnotationStyle(AnnotationStyle.GSON);
		loadRaml("issue-283.raml");
		rule = new Spring4ControllerDecoratorRule();
		rule.apply(getControllerMetadata(), jCodeModel);
		verifyGeneratedCode( getFileAnnotationDiscriminator("Issue283Spring4ControllerDecorator"));
	}
}
