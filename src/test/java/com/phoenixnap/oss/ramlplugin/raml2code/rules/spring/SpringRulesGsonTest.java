package com.phoenixnap.oss.ramlplugin.raml2code.rules.spring;

import static com.phoenixnap.oss.ramlplugin.raml2code.helpers.CodeModelHelper.ext;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.Serializable;

import org.jsonschema2pojo.AnnotationStyle;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import com.phoenixnap.oss.ramlplugin.raml2code.exception.InvalidRamlResourceException;
import com.phoenixnap.oss.ramlplugin.raml2code.helpers.CodeModelHelper;
import com.phoenixnap.oss.ramlplugin.raml2code.plugin.TestConfig;
import com.phoenixnap.oss.ramlplugin.raml2code.rules.AbstractRuleTestBase;
import com.phoenixnap.oss.ramlplugin.raml2code.rules.RamlLoader;
import com.phoenixnap.oss.ramlplugin.raml2code.rules.Spring4ControllerInterfaceRule;
import com.phoenixnap.oss.ramlplugin.raml2code.rules.basic.ClassFieldDeclarationRule;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JPackage;

/**
 * @author armin.weisser
 * @author kris galea
 * @since 0.4.1
 */
public class SpringRulesGsonTest extends AbstractRuleTestBase {

	@BeforeClass
	public static void initRaml() throws InvalidRamlResourceException {
		TestConfig.resetConfig();
		TestConfig.setAnnotationStyle(AnnotationStyle.GSON);

		RAML = RamlLoader.loadRamlFromFile(RESOURCE_BASE + "complex" + File.separator +"api"+ File.separator +"api.raml");
	}


	@Test
	public void applyRestControllerAnnotationRule_shouldCreate_validClassAnnotation() throws JClassAlreadyExistsException {
		SpringRestControllerAnnotationRule rule = new SpringRestControllerAnnotationRule();

		JPackage jPackage = jCodeModel.rootPackage();
		JDefinedClass jClass = jPackage._class(JMod.PUBLIC, "MyClass");
		rule.apply(getControllerMetadata(), jClass);

		assertThat(jClass, is(notNullValue()));
		assertThat(jClass.name(), equalTo("MyClass"));
		assertThat(serializeModel(), containsString("import org.springframework.web.bind.annotation.RestController;"));
		assertThat(serializeModel(), containsString("@RestController"));
	}

	@Test
	public void applyRequestMappingAnnotationRule_shouldCreate_validClassAnnotation() throws JClassAlreadyExistsException {
		SpringRequestMappingClassAnnotationRule rule = new SpringRequestMappingClassAnnotationRule();

		JPackage jPackage = jCodeModel.rootPackage();
		JDefinedClass jClass = jPackage._class(JMod.PUBLIC, "MyClass");
		rule.apply(getControllerMetadata(), jClass);

		assertThat(jClass, is(notNullValue()));
		assertThat(jClass.name(), equalTo("MyClass"));
		assertThat(serializeModel(), containsString("import org.springframework.web.bind.annotation.RequestMapping;"));
		assertThat(serializeModel(), containsString("@RequestMapping(\"/api/endpoints\")"));
	}

	@Test
	public void applyRequestMappingAnnotationRule_shouldCreate_validMethodAnnotation() throws JClassAlreadyExistsException {
		SpringRequestMappingMethodAnnotationRule rule = new SpringRequestMappingMethodAnnotationRule();

		JPackage jPackage = jCodeModel.rootPackage();
		JDefinedClass jClass = jPackage._class(JMod.PUBLIC, "MyClass");
		JMethod jMethod = jClass.method(JMod.PUBLIC, Object.class, "getBase");
		rule.apply(getEndpointMetadata(), jMethod);

		assertThat(serializeModel(), containsString("import org.springframework.web.bind.annotation.RequestMapping;"));
		assertThat(serializeModel(), containsString("@RequestMapping(value = \"\", method = RequestMethod.GET)"));
	}

	@Test
	public void applyDelegateFieldDeclarationRule_shouldCreate_validAutowiredField() throws JClassAlreadyExistsException {
		SpringDelegateFieldDeclerationRule rule = new SpringDelegateFieldDeclerationRule("delegate");

		JPackage jPackage = jCodeModel.rootPackage();
		JDefinedClass jClass = jPackage._class(JMod.PUBLIC, "MyClass");
		jClass._implements(Serializable.class);
		rule.apply(getControllerMetadata(), jClass);

		assertThat(serializeModel(), containsString("import org.springframework.beans.factory.annotation.Autowired;"));
		assertThat(serializeModel(), containsString("@Autowired"));
		assertThat(serializeModel(), containsString("private Serializable delegate;"));
		assertThat(serializeModel(), is(not(containsString("jackson"))));
	}




}
