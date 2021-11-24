package api.deezer.http;

import api.deezer.http.utils.URLParamsEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Default implementation of {@link HttpClient}. Based on {@link HttpURLConnection}.
 */
public class DefaultHttpClient implements HttpClient {
    /**
     * {@link Logger} object.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultHttpClient.class);

    @Override
    public String execute(HttpRequest httpRequest) throws IOException {
        LOGGER.debug(
                "Executing request: request_method={}; url={}; params={}",
                httpRequest.getRequestMethod(),
                httpRequest.getUrl(),
                httpRequest.getParams()
        );
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(httpRequest.getUrl()).openConnection();
            connection.setRequestMethod(httpRequest.getRequestMethod());
            connection.setDoOutput(true);

            Map<String, String> params = httpRequest.getParams();
            if (params != null && !params.isEmpty()) {
                try (DataOutputStream output = new DataOutputStream(connection.getOutputStream())) {
                    output.writeBytes(URLParamsEncoder.encode(params));
                    output.flush();
                }
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String response = reader.lines().collect(Collectors.joining());
                LOGGER.debug("Response: {}", response);
                return response;
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
