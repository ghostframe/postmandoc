/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.ghostframe.postmandoc.postman;

import com.ghostframe.postmandoc.http.HttpParser;
import com.ghostframe.postmandoc.postman.domain.PostmanHeader;
import com.ghostframe.postmandoc.postman.domain.PostmanRequest;
import com.ghostframe.postmandoc.postman.domain.PostmanRequestBody;
import java.io.IOException;
import static java.util.Arrays.asList;
import java.util.List;
import static java.util.stream.Collectors.toList;
import java.util.stream.Stream;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpException;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpRequest;

public class PostmanRequestFactory {

    private static final String ADOC_HEADER = "[source,http,options=\"nowrap\"]\n----\n";
    private static final String ADOC_PAYLOAD = "\n----";
    private static final String HTTP_URL_PREFIX = "http://";
    private static final String HTTP_REQUEST_BODY_START_TOKEN_WIN = "\n\r\n";
    private static final String HTTP_REQUEST_BODY_START_TOKEN_LINUX = "\n\n";
    private static final List<String> IGNORED_HTTP_HEADER_NAMES = asList(HttpHeaders.HOST, HttpHeaders.CONTENT_LENGTH);

    public static PostmanRequest fromHttpRequestSnippet(String httpRequestSnippet, String replacementHost) throws IOException, HttpException {
        String httpRequestText = stripAdocHeaderAndPayload(httpRequestSnippet);
        HttpRequest httpRequest = HttpParser.parse(httpRequestText);
        return PostmanRequest.builder()
                .body(createBody(httpRequestText))
                .url(getUrl(httpRequest, replacementHost))
                .method(httpRequest.getRequestLine().getMethod())
                .header(createHeaders(httpRequest))
                .build();
    }

    private static String stripAdocHeaderAndPayload(String httpRequestSnippet) {
        String noHeader = httpRequestSnippet.substring(ADOC_HEADER.length());
        String noHeaderAndNoPayload = noHeader.substring(0, noHeader.length() - ADOC_PAYLOAD.length());
        return noHeaderAndNoPayload;
    }

    private static String getUrl(HttpRequest httpRequest, String replacementHost) {
        String host = replacementHost != null ? replacementHost : httpRequest.getFirstHeader(HttpHeaders.HOST).getValue();
        return HTTP_URL_PREFIX + host + httpRequest.getRequestLine().getUri();
    }

    private static PostmanRequestBody createBody(String httpRequestText) {
        return PostmanRequestBody.builder()
                .mode("raw")
                .raw(StringUtils.substringAfter(httpRequestText, getBodyStartToken(httpRequestText)))
                .build();
    }

    private static String getBodyStartToken(String httpRequestText) {
        if (httpRequestText.contains(HTTP_REQUEST_BODY_START_TOKEN_WIN)) {
            return HTTP_REQUEST_BODY_START_TOKEN_WIN;
        } else {
            return HTTP_REQUEST_BODY_START_TOKEN_LINUX;
        }
    }

    private static List<PostmanHeader> createHeaders(HttpRequest httpRequest) {
        return Stream.of(httpRequest.getAllHeaders())
                .filter(header -> !IGNORED_HTTP_HEADER_NAMES.contains(header.getName()))
                .map(header -> new PostmanHeader(header.getName(), header.getValue(), null))
                .collect(toList());
    }

}
