package org.owasp.benchmark.score;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class TestCreator {

	public static void main(String [] args) throws IOException {
		String copy = args[0];
		String newTest = args[1];
		String vulnerability = args[2];
		String type = args[3];

		copy("src/main/java/org/owasp/benchmark/testcode/", copy, newTest, ".java");
		copy("src/main/webapp/xxx/owasp/benchmark/testcode/", copy, newTest, ".html");
		File results = new File("expectedresults-1.2.csv");
		FileUtils.write(results, FileUtils.readFileToString(results)+"\n"+newTest+","+vulnerability+",true,"+vulnerability.hashCode());
		File index = new File("src/main/webapp/"+type+"-Index.html");
		FileUtils.write(index, FileUtils.readFileToString(results).replaceAll("</ul>", "<li><a href='xxx/"+newTest+".html'>"+newTest+" "+vulnerability+"</a></li></ul>"));
		
	}
	
	private static void copy(String location, String origin, String end, String fileType) throws IOException {
		File newFile = new File(location+end+fileType);
		FileOutputStream out = new FileOutputStream(newFile);
		FileInputStream in = new FileInputStream(new File(location+origin+fileType));
		IOUtils.copy(in, out);
		out.close();
		in.close();
		FileUtils.write(newFile, FileUtils.readFileToString(newFile).replaceAll(origin, end));
		
		
	}
	
	
}
