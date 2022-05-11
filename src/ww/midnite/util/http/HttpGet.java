package ww.midnite.util.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;


public class HttpGet {

	private final URL url;
	private final Logger log;

	private HttpURLConnection connection = null;
	private InputStream in = null;


	public HttpGet(final URL url, final Logger log) {
		this.url = url;
		this.log = log;
	}


	public synchronized HttpGetResponse get(final long timeoutMillis, final int retries) {

		final HttpGetResponse response = get(timeoutMillis);
		if (response.getCode() != 200 && retries > 0) {
			log.warning("Received a not proper HTTP response code (" + response.getCode() + "). Send the request again.");
			return get(timeoutMillis, retries - 1);
		}

		return response;
	}


	private synchronized HttpGetResponse get(final long timeoutMillis) {
		final long start = System.currentTimeMillis();
		try {
			connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout((int) timeoutMillis);
			connection.setInstanceFollowRedirects(true);
			connection.setUseCaches(false);

			connection.setRequestMethod("GET");
			connection.setRequestProperty("Connection", "keep-alive");
			connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
			connection.setRequestProperty("Cache-Control", "no-cache");
			connection.setRequestProperty("Pragma", "no-cache");

			connection.connect();

			try {
				/* wait for answer here */
				in = connection.getInputStream();

			} catch (final IOException e) {
				if (connection == null) {
					throw new IOException("disconnected");
				}
				in = connection.getErrorStream();
			}

			if (in == null) {
				throw new IOException("stream is closed");
			}

			if ("gzip".equalsIgnoreCase(connection.getContentEncoding())) {
				in = new GZIPInputStream(in);
			}

			final String type = connection.getContentType();
			final int code = connection.getResponseCode();

			final ByteArrayOutputStream byteContainer = new ByteArrayOutputStream();

			final byte[] readBuffer = new byte[4096];
			int read;
			while ((read = in.read(readBuffer)) > 0) {
				byteContainer.write(readBuffer, 0, read);
			}

			final long responseTime = System.currentTimeMillis() - start;

			byteContainer.flush();
			final byte[] binary = byteContainer.toByteArray();

			return new HttpGetResponse(responseTime, binary, type, code);

		} catch (final Exception e) {
			return new HttpGetResponse(new HttpGetException(e));

		} finally {
			cancel();
		}
	}


	public synchronized void cancel() {
		if (in != null) {
			try {
				in.close();
			} catch (final IOException ignore) {
				/* NoOp */
			}
			in = null;
		}
		if (connection != null) {
			connection.disconnect();
			connection = null;
		}
	}


	public static String urlencode(final String str) {
		try {
			return URLEncoder.encode(str, "UTF-8");

		} catch (final UnsupportedEncodingException e) {
			e.printStackTrace();

			return str;
		}
	}

}
