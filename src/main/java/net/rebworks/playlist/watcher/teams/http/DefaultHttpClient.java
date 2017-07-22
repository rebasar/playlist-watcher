package net.rebworks.playlist.watcher.teams.http;

import javax.net.ssl.HttpsURLConnection;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class DefaultHttpClient implements HttpClient {
    @Override
    public HTTPResult post(final URI uri, final byte[] body) {
        try {
            final URL url = uri.toURL();
            final HttpsURLConnection httpsURLConnection = prepareConnection(body, url);
            return processResponse(httpsURLConnection);
        } catch (final IOException e) {
            return HTTPResult.exception(e);
        }
    }

    private HTTPResult processResponse(final HttpsURLConnection httpsURLConnection) throws IOException {
        final int responseCode = httpsURLConnection.getResponseCode();
        if(responseCode >= 300){
            final String response = new String(read(httpsURLConnection.getErrorStream()), StandardCharsets.UTF_8);
            return HTTPResult.fail(responseCode, response);
        } else {
            return HTTPResult.success();
        }
    }

    private HttpsURLConnection prepareConnection(final byte[] body, final URL url) throws IOException {
        final URLConnection urlConnection = url.openConnection();
        urlConnection.setRequestProperty("Content-type", "application/json");
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
        final OutputStream outputStream = urlConnection.getOutputStream();
        outputStream.write(body);
        return (HttpsURLConnection) urlConnection;
    }

    private byte[] read(final InputStream inputStream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toByteArray();
    }
}
