package api.deezer.http.impl;

import java.util.HashMap;
import java.util.Map;

/**
 * Executes Deezer API GET request.
 *
 * @param <Response> response POJO type.
 */
public class DeezerGetRequest<Response> extends DeezerRequest<Response> {
    public DeezerGetRequest(String url, Class<Response> responseClass) {
        this(url, new HashMap<>(), responseClass);
    }

    public DeezerGetRequest(String url, Map<String, String> params, Class<Response> responseClass) {
        super(url, params, responseClass);
        params.put("request_method", "get");
    }
}
