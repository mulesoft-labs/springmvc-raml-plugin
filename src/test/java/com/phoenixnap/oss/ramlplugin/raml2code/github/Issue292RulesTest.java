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
public class Issue292RulesTest extends GitHubAbstractRuleTestBase {

	@Test
	public void check_inline_enums_are_not_generated() throws Exception {
		TestConfig.setAnnotationStyle(AnnotationStyle.JACKSON2);
		loadRaml("issue-292-1.raml");
		rule = new Spring4ControllerDecoratorRule();
		rule.apply(getControllerMetadata(), jCodeModel);
		verifyGeneratedCode( getFileAnnotationDiscriminator("Issue292-1Spring4ControllerDecorator") );
	}

	@Test
	public void check_inline_enums_are_merged() throws Exception {
		TestConfig.setAnnotationStyle(AnnotationStyle.JACKSON2);
		loadRaml("issue-292-2.raml");
		rule = new Spring4ControllerDecoratorRule();
		rule.apply(getControllerMetadata(), jCodeModel);
		verifyGeneratedCode( getFileAnnotationDiscriminator("Issue292-2Spring4ControllerDecorator"));
	}

	@Test
	public void check_inline_enums_are_not_generated_gson() throws Exception {
		TestConfig.setAnnotationStyle(AnnotationStyle.GSON);
		loadRaml("issue-292-1.raml");
		rule = new Spring4ControllerDecoratorRule();
		rule.apply(getControllerMetadata(), jCodeModel);
		verifyGeneratedCode(getFileAnnotationDiscriminator("Issue292-1Spring4ControllerDecorator"));
	}

	@Test
	public void check_inline_enums_are_merged_gson() throws Exception {
		TestConfig.setAnnotationStyle(AnnotationStyle.GSON);
		loadRaml("issue-292-2.raml");
		rule = new Spring4ControllerDecoratorRule();
		rule.apply(getControllerMetadata(), jCodeModel);
		verifyGeneratedCode(getFileAnnotationDiscriminator("Issue292-2Spring4ControllerDecorator"));
	}


}
