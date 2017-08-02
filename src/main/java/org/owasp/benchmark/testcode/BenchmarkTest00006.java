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

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hdiv.ee.commons.threat.VulnerabilityType;
import org.owasp.benchmark.tools.agent.AgentController;

@SuppressWarnings("serial")
@WebServlet(value = "/xxx/BenchmarkTest00006")
public class BenchmarkTest00006 extends SimpleBenchmarkTest {

	@Override
	public void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

		try {
			AgentController.enable(VulnerabilityType.REQUEST_DOS, true);
			AgentController.enable(VulnerabilityType.MALICIOUS_IP, true);
			AgentController.blocking(VulnerabilityType.REQUEST_DOS, true);

			AgentController.setProperty("request_dos.attackBlackList.1",
					URLEncoder.encode("{\"ip\": \"=:.\", \"maxRequestNumber\": 2, \"windowDurationSeconds\": 60}"));

			AgentController.setProperty("malicious_ip.ipExpirationTimeSeconds", "3");

			AgentController.plainCall("https://127.0.0.1:8443/benchmark/xxx/BenchmarkTest00006.html");
			AgentController.plainCall("https://127.0.0.1:8443/benchmark/xxx/BenchmarkTest00006.html");
			AgentController.plainCall("https://127.0.0.1:8443/benchmark/xxx/BenchmarkTest00006.html");

			AgentController.setProperty("request_dos.attackBlackList.1",
					URLEncoder.encode("{\"ip\": \"=:.\", \"maxRequestNumber\": 1000, \"windowDurationSeconds\": 60}"));

			Thread.sleep(3000);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			AgentController.enable(VulnerabilityType.REQUEST_DOS, false);
			AgentController.enable(VulnerabilityType.MALICIOUS_IP, false);
			AgentController.blocking(VulnerabilityType.REQUEST_DOS, false);

		}

		println(response, "REQUEST_DOS executed.");

	}

}
