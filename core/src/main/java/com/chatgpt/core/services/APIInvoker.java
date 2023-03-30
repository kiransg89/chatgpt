package com.chatgpt.core.services;

public interface APIInvoker {

    /**
     * authenticate user with provided details
     *
     * @param bodyText     bodytext to be summarized
     * @param maxTokens    maximum words to be summarized
     * @return @{@link String}
     */
    public String invokeAPI(String bodyText, int maxTokens);

}