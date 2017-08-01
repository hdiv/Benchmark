package org.owasp.benchmark.tools.agent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.hdiv.ee.commons.threat.VulnerabilityType;
import org.owasp.benchmark.helpers.Utils;

public class AgentController {

	public static void main(final String[] args) {
		System.out.println(status("REQUEST_DOS"));
	}

	public static void enable(final VulnerabilityType analyzer, final boolean enable) {
		ctrl("analyzers/status/" + analyzer.name() + "/" + (enable ? "enable" : "disable"));
	}

	public static void blocking(final VulnerabilityType analyzer, final boolean enable) {
		ctrl("analyzers/blocking/" + analyzer.name() + "/" + (enable ? "enable" : "disable"));
	}

	public static void cmd(final String analyzer, final String cmd) {
		ctrl("analyzers/cmd/" + analyzer + "/" + cmd);
	}

	private static void ctrl(final String path) {
		call("control/" + path);
	}

	public static String status(final String analyzer) {
		return call("configuration/" + analyzer);
	}

	public static void setProperty(final String property, final String value) {
		call("control/configuration/" + property + "/" + value);
	}

	private static String call(final String path) {
		try (CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(Utils.getSSLFactory()).build()) {
			HttpGet get = new HttpGet("https://127.0.0.1:8443/benchmark/hdiv-devtool/" + path);
			try (CloseableHttpResponse response = httpclient.execute(get)) {
				System.out.println(get);
				System.out.println(response.getStatusLine());
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				IOUtils.copy(response.getEntity().getContent(), out);
				return new String(out.toByteArray());
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String plainCall(final String path) {
		try (CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(Utils.getSSLFactory()).build()) {
			HttpGet get = new HttpGet(path);
			try (CloseableHttpResponse response = httpclient.execute(get)) {
				System.out.println(get);
				System.out.println(response.getStatusLine());
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				IOUtils.copy(response.getEntity().getContent(), out);
				return new String(out.toByteArray());
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
