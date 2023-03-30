package com.chatgpt.core.utils;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.BasicResponseHandler;

/**
 * Handling response using Basic response Handler
 */
public class StringObjectResponseHandler implements ResponseHandler<String> {

    private BasicResponseHandler handler = new BasicResponseHandler();

    @Override
    public String handleResponse(HttpResponse httpResponse) throws
            ClientProtocolException, IOException {
        String responseString = handler.handleResponse(httpResponse);
        HttpClientUtils.closeQuietly(httpResponse);
        return responseString;
    }
}