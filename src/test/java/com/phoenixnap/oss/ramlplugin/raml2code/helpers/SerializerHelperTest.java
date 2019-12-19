package com.phoenixnap.oss.ramlplugin.raml2code.helpers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.jsonschema2pojo.AnnotationStyle;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.gson.annotations.SerializedName;
import com.phoenixnap.oss.ramlplugin.raml2code.data.ApiBodyMetadata;
import com.phoenixnap.oss.ramlplugin.raml2code.plugin.TestConfig;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.writer.SingleStreamCodeWriter;

/**
 * Tests relating to POJO Generation
 *
 * @author kurtpa
 * @since 0.5.2
 */
public class SerializerHelperTest {

	protected static final Logger logger = LoggerFactory.getLogger(SchemaHelperTest.class);

	@Before
	public void before() {
		TestConfig.resetConfig();
	}

	@After
	public void after() {
	}


	@Test
	public void getCorrectPropertyFilterTest() throws Exception {
		Class jsonClass = SerializerHelper.getFieldAnnotationClass();

		assertSame(jsonClass, JsonProperty.class);
	}

	@Test
	public void getCorrectPropertyFilterTestGSON() throws Exception {
		TestConfig.setAnnotationStyle(AnnotationStyle.GSON);
		Class jsonClass = SerializerHelper.getFieldAnnotationClass();

		assertSame(jsonClass, SerializedName.class);
	}


}
