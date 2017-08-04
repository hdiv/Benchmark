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

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet(value = "/xxx/BenchmarkTest00007")
public class BenchmarkTest00007 extends SimpleBenchmarkTest {

	@Override
	public void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		try {
			String filePath = request.getParameter("file");

			if (filePath != null) {

				int BUFSIZE = 4096;

				File file = new File(getClass().getClassLoader().getResource(filePath).getFile());

				int length = 0;
				ServletOutputStream outStream = response.getOutputStream();
				response.setContentType("text/html");
				response.setContentLength((int) file.length());
				String fileName = new File(filePath).getName();
				response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

				byte[] byteBuffer = new byte[BUFSIZE];
				DataInputStream in = new DataInputStream(new FileInputStream(file));

				while (in != null && (length = in.read(byteBuffer)) != -1) {
					outStream.write(byteBuffer, 0, length);
				}

				in.close();
				outStream.flush();
				outStream.close();

			}
		}
		catch (Exception e) {
		}
		finally {
		}
	}

}
