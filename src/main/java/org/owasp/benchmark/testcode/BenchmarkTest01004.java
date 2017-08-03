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

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hdiv.ee.commons.threat.VulnerabilityType;
import org.owasp.benchmark.tools.agent.AgentController;

@SuppressWarnings("serial")
@WebServlet(value = "/xxx/BenchmarkTest01004")
public class BenchmarkTest01004 extends SimpleBenchmarkTest {

	@Override
	public void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		String param = null;

		AgentController.blocking(VulnerabilityType.XSS, true);

		param = request.getParameterValues("param")[0];

		println(response, "The param is: " + param);

		AgentController.blocking(VulnerabilityType.XSS, false);

	}

}
