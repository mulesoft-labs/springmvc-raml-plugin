/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.phoenixnap.oss.ramlplugin.raml2code.helpers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.phoenixnap.oss.ramlplugin.raml2code.interpreters.PojoBuilder;
import com.phoenixnap.oss.ramlplugin.raml2code.plugin.Config;
import com.phoenixnap.oss.ramlplugin.raml2code.raml.RamlDataType;

import org.jsonschema2pojo.Annotator;
import org.jsonschema2pojo.GenerationConfig;
import org.jsonschema2pojo.GsonAnnotator;
import org.jsonschema2pojo.Jackson2Annotator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang.NotImplementedException;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.function.Function;

/**
 * This class provide static method to get correct JSON serializer objects to build code model
 *
 * @author Andres Ivaldi
 * @since 2.1.1
 */
public abstract class SerializerHelper {

    protected static final Logger logger = LoggerFactory.getLogger(SerializerHelper.class);

    /**
     * Get class from corresponding serializer to annotate POJO fieds
     * @return annotation class based on the configuration.
     */
    public static Class<? extends Annotation> getFieldAnnotationClass() {
        Class<? extends Annotation> clazz = null;
        logger.debug("getFieldAnnotationClass.getFieldAnnotationClass start");

        GenerationConfig config = getConfiguration();

        switch( config.getAnnotationStyle() ){
            case GSON : clazz =  SerializedName.class;
                break;
            case JACKSON:
            case JACKSON1:
            case JACKSON2: clazz =  JsonProperty.class;
                break;
            default: throw new NotImplementedException("Serialization "+ config.getAnnotationStyle().toString() +" not suported, use GSON and JACKSON2");
        }

        logger.debug("getFieldAnnotationClass.getFieldAnnotationClass end" );
        return clazz;
    }


    /**
     * Get correct Annotator given serializator
     * @return the annotator instance based on the configuration.
     */
    public static Annotator getAnnotator(){
        logger.debug("SerializerHelper.getAnnotator start");
        Annotator annotator = null;

        GenerationConfig config = getConfiguration();

        switch( config.getAnnotationStyle() ){
            case GSON : annotator  = new GsonAnnotator(config);
                break;
            case JACKSON:
            case JACKSON1:
            case JACKSON2: annotator =  new Jackson2Annotator(config); //TODO: review if we can use annotator for each above or this is enough
                break;
            default: throw new NotImplementedException("Serialization "+ config.getAnnotationStyle().toString() +" not suported, use GSON and JACKSON2");
        }

        logger.debug("SerializerHelper.getAnnotator end" );
        return annotator;
    }

    public static boolean shouldAnnotateDate(){

        logger.debug("SerializerHelper.shouldAnnotateDate start");
        boolean should = false;

        GenerationConfig config = getConfiguration();

        switch( config.getAnnotationStyle() ){
            case JACKSON:
            case JACKSON1:
            case JACKSON2: should=true;
                break;
            default: break;
        }

        logger.debug("SerializerHelper.shouldAnnotateDate end" );
        return should;
    }


    private static GenerationConfig getConfiguration(){

        GenerationConfig config = Config.getPojoConfig();
		if (config == null) {
			config = SchemaHelper.getDefaultGenerationConfig();
        }
        return config;
    }




}