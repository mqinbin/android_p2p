package com.qinbin.p2p.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

/**
 * Created by teacher on 2016/4/23.
 */
public interface HttpWrapper {
    InputStream getInputStreamResponse(String url, HttpMethod method, Map<String, String> params) throws IOException;

    Reader getReaderResponse(String url, HttpMethod method, Map<String, String> params) throws IOException;

    String getStringResponse(String url, HttpMethod method, Map<String, String> params) throws IOException;
}
