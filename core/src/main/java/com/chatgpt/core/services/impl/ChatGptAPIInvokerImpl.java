package com.chatgpt.core.services.impl;

import java.io.IOException;

import com.chatgpt.core.bean.SummaryBean;
import com.chatgpt.core.services.APIInvoker;
import com.chatgpt.core.services.ChatGptHttpClientFactory;
import com.chatgpt.core.services.JSONConverter;
import com.chatgpt.core.utils.StringObjectResponseHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(service = APIInvoker.class)
public class ChatGptAPIInvokerImpl implements APIInvoker {

    private static final StringObjectResponseHandler HANDLER = new StringObjectResponseHandler();

    @Reference
    private ChatGptHttpClientFactory httpClientFactory;

    @Reference
    private JSONConverter jsonConverter;

    @Override
    public String invokeAPI(String bodyText, int maxTokens) {
        String responseString = StringUtils.EMPTY;
        try {
            responseString = httpClientFactory.getExecutor()
                    .execute(httpClientFactory.post().bodyString(generatePromot(bodyText, maxTokens), ContentType.APPLICATION_JSON))
                    .handleResponse(HANDLER);
        } catch (IOException e) {
            log.error("Error occured while processing request {}", e.getMessage());
        }
        log.debug("API Request Response {}", responseString);
        return responseString;

    }

    private String generatePromot(String bodyText, int maxTokens) {
        SummaryBean bodyBean = new SummaryBean();
        if(maxTokens != 0) {
        	bodyBean.setMaxTokens(maxTokens);
        }        
        bodyBean.setPrompt(bodyText);
        return jsonConverter.convertToJsonString(bodyBean);
    }
}