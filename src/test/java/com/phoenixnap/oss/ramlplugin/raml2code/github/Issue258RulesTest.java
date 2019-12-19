package com.phoenixnap.oss.ramlplugin.raml2code.github;

import com.phoenixnap.oss.ramlplugin.raml2code.plugin.TestConfig;
import com.phoenixnap.oss.ramlplugin.raml2code.rules.GitHubAbstractRuleTestBase;
import com.phoenixnap.oss.ramlplugin.raml2code.rules.Spring4ControllerInterfaceRule;

import org.jsonschema2pojo.AnnotationStyle;
import org.junit.Test;

/**
 * @author vpashynskyi
 * @since 2.0.2
 */
public class Issue258RulesTest extends GitHubAbstractRuleTestBase {

	@Test
	public void testValidInQueryParam() throws Exception {
		TestConfig.setAnnotationStyle(AnnotationStyle.JACKSON2);
		loadRaml("issue-258.raml");
		rule = new Spring4ControllerInterfaceRule();
		rule.apply(getControllerMetadata(), jCodeModel);
		verifyGeneratedCode("Issue258-retainPropertyName");
	}

	@Test
	public void testValidInQueryParam_gson() throws Exception {
		TestConfig.setAnnotationStyle(AnnotationStyle.GSON);
		loadRaml("issue-258.raml");
		rule = new Spring4ControllerInterfaceRule();
		rule.apply(getControllerMetadata(), jCodeModel);
		verifyGeneratedCode( getFileAnnotationDiscriminator("Issue258-retainPropertyName"));
	}

}
