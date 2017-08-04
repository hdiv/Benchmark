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
@WebServlet(value = "/xxx/BenchmarkTest01009")
public class BenchmarkTest01009 extends SimpleBenchmarkTest {

	@Override
	public void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		try {
			AgentController.blocking(VulnerabilityType.SQL_INJECTION, true);
			String sql = "INSERT INTO users (username, password) VALUES ('foo','" + request.getParameter("url") + "')";

			java.sql.Statement statement = org.owasp.benchmark.helpers.DatabaseHelper.getSqlStatement();
			int count = statement.executeUpdate(sql);
			org.owasp.benchmark.helpers.DatabaseHelper.outputUpdateComplete(sql, response);

		}
		catch (Exception e) {
		}
		finally {
			AgentController.blocking(VulnerabilityType.SQL_INJECTION, false);
		}
		println(response, "SQL_INJECTION attack protection executed");
	}

}
