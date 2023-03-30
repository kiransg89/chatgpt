package com.chatgpt.core.services;

import com.fasterxml.jackson.databind.util.JSONPObject;

/**
 * Interface to deal with Json.
 */
public interface JSONConverter {

    /**
     * Convert Json Object to given object
     *
     * @param jsonpObject
     * @param clazz       type of class
     * @return @{@link Object}
     */
    @SuppressWarnings("rawtypes")
    Object convertToObject(JSONPObject jsonpObject, Class clazz);

    /**
     * Convert Json Object to given object
     *
     * @param jsonString
     * @param clazz      type of class
     * @return @{@link Object}
     */
    @SuppressWarnings("rawtypes")
    Object convertToObject(String jsonString, Class clazz);

    /**
     * Convert Json Object to given object
     *
     * @param object
     * @return @{@link String}
     */
    String convertToJsonString(Object object);

    /**
     * Convert Json Object to given object
     *
     * @param object
     * @return @{@link JSONPObject}
     */
    JSONPObject convertToJSONPObject(Object object);
}
