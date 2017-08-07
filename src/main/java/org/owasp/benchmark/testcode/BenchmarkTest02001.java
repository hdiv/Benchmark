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
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.owasp.benchmark.tools.agent.AgentController;

@SuppressWarnings("serial")
@WebServlet(value = "/xxx/BenchmarkTest02001")
public class BenchmarkTest02001 extends SimpleBenchmarkTest {

	@Override
	public void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

		List<String> vulnerabilityType = new ArrayList<String>();

		String responsePlainCall = AgentController.plainCall("https://127.0.0.1:8443/benchmark/hdiv-devtool/configuration/");
		if (responsePlainCall != null) {
			boolean passTest = true;

			String[] splitted = responsePlainCall.split("../static/help/");

			for (int i = 1; i < splitted.length; i++) {
				vulnerabilityType.add(splitted[i].split("[.]html")[0]);
			}

			for (String vulnerabilityHelp : vulnerabilityType) {

				String responseCall = AgentController
						.plainCall("https://127.0.0.1:8443/benchmark/hdiv-devtool/static/help/" + vulnerabilityHelp + ".html");

				if (!responseCall.contains("md-typeset__table")) {
					passTest = false;
				}

			}

			// Log successful test
			if (passTest) {

				String message = URLEncoder.encode("SourceCodeVulnerability [type=CHECK_VULNERABILITY_HELP_LINKS, /BenchmarkTest02001 ]",
						"UTF-8");
				message = message.replace("%2F", "/");
				AgentController.plainCall("https://127.0.0.1:8443/benchmark/hdiv-devtool/control/cmd/log/" + message);

			}

		}
	}
}
