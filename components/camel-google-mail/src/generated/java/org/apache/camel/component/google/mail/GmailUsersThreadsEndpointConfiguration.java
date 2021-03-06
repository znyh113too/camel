
/*
 * Camel EndpointConfiguration generated by camel-api-component-maven-plugin
 */
package org.apache.camel.component.google.mail;

import org.apache.camel.spi.Configurer;
import org.apache.camel.spi.ApiParams;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriParams;

/**
 * Camel EndpointConfiguration for com.google.api.services.gmail.Gmail$Users$Threads
 */
@ApiParams(apiName = "threads", apiMethods = "delete,get,list,modify,trash,untrash")
@UriParams
@Configurer
public final class GmailUsersThreadsEndpointConfiguration extends GoogleMailConfiguration {
    @UriParam(description = "The com.google.api.services.gmail.model.ModifyThreadRequest")
    private com.google.api.services.gmail.model.ModifyThreadRequest content;
    @UriParam(description = "ID of the Thread to delete")
    private String id;
    @UriParam(description = "The user's email address. The special value me can be used to indicate the authenticated user. default: me")
    private String userId;

    public com.google.api.services.gmail.model.ModifyThreadRequest getContent() {
        return content;
    }

    public void setContent(com.google.api.services.gmail.model.ModifyThreadRequest content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
