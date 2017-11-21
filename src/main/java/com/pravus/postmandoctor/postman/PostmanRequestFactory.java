package com.pravus.postmandoctor.postman;

import com.pravus.postmandoctor.http.HttpParser;
import com.pravus.postmandoctor.postman.domain.PostmanHeader;
import com.pravus.postmandoctor.postman.domain.PostmanRequest;
import com.pravus.postmandoctor.postman.domain.PostmanRequestBody;
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
    private static final String HTTP_REQUEST_BODY_START_TOKEN = "\n\r\n";
    private static final List<String> IGNORED_HTTP_HEADER_NAMES = asList(HttpHeaders.HOST, HttpHeaders.CONTENT_LENGTH);
    private static final String DEFAULT_DESCRIPTION = "";

    public static PostmanRequest fromHttpRequestSnippet(String httpRequestSnippet) throws IOException, HttpException {
        String httpRequestText = stripAdocHeaderAndPayload(httpRequestSnippet);
        HttpRequest httpRequest = HttpParser.parse(httpRequestText);
        return PostmanRequest.builder()
                .body(createBody(httpRequestText))
                .url(getUrl(httpRequest))
                .method(httpRequest.getRequestLine().getMethod())
                .header(createHeaders(httpRequest))
                .description(DEFAULT_DESCRIPTION)
                .build();
    }

    private static String stripAdocHeaderAndPayload(String httpRequestSnippet) {
        String noHeader = httpRequestSnippet.substring(ADOC_HEADER.length());
        String noHeaderAndNoPayload = noHeader.substring(0, noHeader.length() - ADOC_PAYLOAD.length());
        return noHeaderAndNoPayload;
    }

    private static String getUrl(HttpRequest httpRequest) {
        return HTTP_URL_PREFIX + httpRequest.getFirstHeader(HttpHeaders.HOST).getValue() + httpRequest.getRequestLine().getUri();
    }

    private static PostmanRequestBody createBody(String httpRequestText) {
        return PostmanRequestBody.builder()
                .mode("raw")
                .raw(StringUtils.substringAfter(httpRequestText, HTTP_REQUEST_BODY_START_TOKEN))
                .build();
    }

    private static List<PostmanHeader> createHeaders(HttpRequest httpRequest) {
        return Stream.of(httpRequest.getAllHeaders())
                .filter(header -> !IGNORED_HTTP_HEADER_NAMES.contains(header.getName()))
                .map(header -> new PostmanHeader(header.getName(), header.getValue(), DEFAULT_DESCRIPTION))
                .collect(toList());
    }

}
