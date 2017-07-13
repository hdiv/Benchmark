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

	public static void main(String[] args) {
		enable("UNVALIDATED_REDIRECT", false);
		System.out.println(status("REQUEST_DOS"));
	}

	public static void enable(String analyzer, boolean enable) {
		ctrl("analyzers/status/" + analyzer + "/" + (enable?"enable":"disable"));
	}
	
	public static void blocking(VulnerabilityType analyzer, boolean enable) {
		ctrl("analyzers/blocking/" + analyzer.name().toLowerCase() + "/" + (enable?"enable":"disable"));
	}
	
	public static void cmd(String analyzer, String cmd) {
		ctrl("analyzers/cmd/" + analyzer + "/" + cmd);
	}
	
	private static void ctrl(String path) {
		call("control/"+path);
	}
	
	public static String status(String analyzer) {
		return call("configuration/" + analyzer);
	}
	
	public void setProperty(String property, String value) {
		call("configuration/"+property+"/"+value);
	}

	private static String call(String path) {
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

}
