package com.pravus.postmandoctor.http;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.impl.io.DefaultHttpRequestParser;
import org.apache.http.impl.io.HttpTransportMetricsImpl;
import org.apache.http.impl.io.SessionInputBufferImpl;

public class HttpParser {

    public static HttpRequest parse(String httpRequestText) throws IOException, HttpException {
        try {
            return new DefaultHttpRequestParser(createSessionInputBuffer(httpRequestText)).parse();
        } catch (HttpException | IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static SessionInputBufferImpl createSessionInputBuffer(String string) {
        SessionInputBufferImpl sessionInputBuffer = new SessionInputBufferImpl(new HttpTransportMetricsImpl(), 255);
        sessionInputBuffer.bind(IOUtils.toInputStream(string, StandardCharsets.UTF_8));
        return sessionInputBuffer;
    }

}
