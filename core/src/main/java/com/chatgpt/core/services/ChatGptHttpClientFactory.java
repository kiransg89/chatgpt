package com.chatgpt.core.services;

import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;

/**
 * Factory for building pre-configured HttpClient Fluent Executor and Request objects
 * based a configure host, port and (optionally) username/password.
 *
 * Factories will generally be accessed by service lookup using the factory.name property.
 */
public interface ChatGptHttpClientFactory {

    /**
     * Get the configured Executor object from this factory.
     *
     * @return an Executor object
     */
    Executor getExecutor();

    /**
     * Create a POST request using the base hostname and port defined in the factory configuration.
     *
     * @param partialUrl the portion of the URL after the port (and slash)
     *
     * @return a fluent Request object
     */
    Request post();
}