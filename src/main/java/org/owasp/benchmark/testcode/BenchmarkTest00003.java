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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.BitSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet(value="/xxx/BenchmarkTest00003")
public class BenchmarkTest00003 extends SimpleBenchmarkTest {
	
	private void testDeserialization_Safe() throws Exception {
		BitSet bitset = new BitSet();
		bitset.set(1, 2);
		File serializedFile = serialize(bitset);
		deserialize(serializedFile);
	}
	

	private File serialize(final Serializable serializable) throws IOException {
		File tmpFile = File.createTempFile("serial", ".ser");
		FileOutputStream fos = new FileOutputStream(tmpFile);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(serializable);
		oos.close();
		fos.close();
		return tmpFile;
	}

	private static Object deserialize(final File serializable) throws Exception {
		FileInputStream fis = new FileInputStream(serializable);
		ObjectInputStream ois = new ObjectInputStream(fis);
		Object object = ois.readObject();
		ois.close();
		fis.close();
		return object;
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			testDeserialization_Safe();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		println(response, "Safe serialization executed executed");
	}
	
}
