/**
* OWASP Benchmark v1.2
*
* This file is part of the Open Web Application Security Project (OWASP)
* Benchmark Project. For details, please see
* <a href="https://www.owasp.org/index.php/Benchmark">https://www.owasp.org/index.php/Benchmark</a>.
*
* The OWASP Benchmark is free software: you can redistribute it and/or modify it under the terms
* of the GNU General Public License as published by the Free Software Foundation, version 2.
*
* The OWASP Benchmark is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
* even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* @author Dave Wichers <a href="https://www.aspectsecurity.com">Aspect Security</a>
* @created 2015
*/

package org.owasp.benchmark.testcode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.hdiv.ee.commons.threat.VulnerabilityType;
import org.owasp.benchmark.helpers.Utils;
import org.owasp.benchmark.tools.agent.AgentController;

@SuppressWarnings("serial")
@WebServlet(value = "/xxx/BenchmarkTest01008")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
		maxFileSize = 1024 * 1024 * 10, // 10MB
		maxRequestSize = 1024 * 1024 * 50)
public class BenchmarkTest01008 extends SimpleBenchmarkTest {

	@Override
	public void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

		boolean isMultipartContent = ServletFileUpload.isMultipartContent(request);
		if (!isMultipartContent) {

			System.out.println("No multipart. Upload file...");

			AgentController.enable(VulnerabilityType.MALICIOUS_BINARY, true);
			AgentController.blocking(VulnerabilityType.MALICIOUS_BINARY, true);

			CloseableHttpClient httpClient;
			try {
				httpClient = HttpClients.custom().setSSLSocketFactory(Utils.getSSLFactory()).build();

				HttpPost uploadFile = new HttpPost("https://127.0.0.1:8443/benchmark/xxx/BenchmarkTest01008");
				MultipartEntityBuilder builder = MultipartEntityBuilder.create();
				builder.addTextBody("field1", "yes", ContentType.TEXT_PLAIN);

				// This attaches the file to the POST:
				File f = new File(getClass().getClassLoader().getResource("context.xml").getFile());
				builder.addBinaryBody("file", new FileInputStream(f), ContentType.APPLICATION_OCTET_STREAM, f.getName());

				HttpEntity multipart = builder.build();
				uploadFile.setEntity(multipart);
				CloseableHttpResponse responseExecutor = httpClient.execute(uploadFile);
				responseExecutor.getEntity();

				AgentController.enable(VulnerabilityType.MALICIOUS_BINARY, false);
				AgentController.blocking(VulnerabilityType.MALICIOUS_BINARY, false);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			return;

		}
		else {
			for (Part part : request.getParts()) {
				System.out.println(part.toString());
			}

			System.out.println("File uploaded.");
		}

	}

}
