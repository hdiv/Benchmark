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
import java.io.ObjectInputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet(value="/xxx/BenchmarkTest00004")
public class BenchmarkTest00004 extends SimpleBenchmarkTest {
	
	private void testDeserialization_Unsafe() throws Exception {
		/**
		 * ATTACK PAYLOAD created with ysoserial
		 * 
		 * 1) Crashes the request 2) Opens a explorer window using Runtime.exec()
		 * 
		 */
		deserialize();
	}

	private Object deserialize() throws Exception {
		ObjectInputStream ois = new ObjectInputStream(getClass().getResourceAsStream("/serialPayload.txt"));
		Object object = ois.readObject();
		ois.close();
		return object;
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			testDeserialization_Unsafe();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		println(response, "Unsafe serialization executed executed");
	}
	
}
