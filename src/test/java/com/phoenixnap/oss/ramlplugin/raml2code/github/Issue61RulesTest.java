/*
 * Copyright (c) 2016 SAP SE or an SAP affiliate company. All rights reserved.
 */

package com.phoenixnap.oss.ramlplugin.raml2code.github;

import org.jsonschema2pojo.AnnotationStyle;
import org.junit.Test;

import com.phoenixnap.oss.ramlplugin.raml2code.plugin.TestConfig;
import com.phoenixnap.oss.ramlplugin.raml2code.rules.GitHubAbstractRuleTestBase;
import com.phoenixnap.oss.ramlplugin.raml2code.rules.Spring4RestTemplateClientRule;

public class Issue61RulesTest extends GitHubAbstractRuleTestBase {

	@Test
	public void applySpring4RestTemplateClientRule_shouldCreate_validCode() throws Exception {
		TestConfig.setAnnotationStyle(AnnotationStyle.JACKSON2);
		loadRaml("issue-61.raml");
		rule = new Spring4RestTemplateClientRule();
		rule.apply(getControllerMetadata(), jCodeModel);
		String fileName = "Issue61BaseClient";

		verifyGeneratedCode(getFileAnnotationDiscriminator(fileName));
	}


	@Test
	public void applySpring4RestTemplateClientRule_shouldCreate_validCode_gson() throws Exception {
		TestConfig.setAnnotationStyle(AnnotationStyle.GSON);
		loadRaml("issue-61.raml");
		rule = new Spring4RestTemplateClientRule();
		rule.apply(getControllerMetadata(), jCodeModel);
		String fileName = "Issue61BaseClient";

		verifyGeneratedCode(getFileAnnotationDiscriminator(fileName));
	}

}
