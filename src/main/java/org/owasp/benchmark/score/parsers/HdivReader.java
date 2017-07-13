package org.owasp.benchmark.score.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.owasp.benchmark.score.BenchmarkScore;

public class HdivReader extends Reader {

	Set<String> invalid = new HashSet<>();

	public static void main(final String[] args) throws Exception {
		File f = new File("hdivAgentLog.hlg");
		HdivReader cr = new HdivReader();
		System.out.println(cr.parse(f));
	}

	public TestResults parse(final File f) throws Exception {
		TestResults tr = new TestResults("Hdiv", true, TestResults.ToolType.IAST);

		BufferedReader reader = new BufferedReader(new FileReader(f));
		String firstLine = reader.readLine();
		String lastLine = "";
		String line = "";
		ArrayList<String> chunk = new ArrayList<>();
		String testNumber = "00001";
		while (line != null) {
			try {
				line = reader.readLine();
				if (line != null) {
					if (line.contains("SourceCodeVulnerability [")||line.contains("Attack [")) {
						boolean attack = line.contains("Attack [");
						String preffix = "";
						if(line.contains("Attack [")) {
							if(line.contains("blocking request")) {
								preffix = "ATTACK_BLOCKED";
							} else {
								preffix = "ATTACK";
							}
						}
						// ok, we're starting a new URL, so process this one and start the next chunk
						process(tr, testNumber, Arrays.asList(line), line.contains("Attack [")?"ATTACK_":"");
						chunk.clear();
						testNumber = "00000";
						String fname = "/" + BenchmarkScore.BENCHMARKTESTNAME;
						int idx = line.indexOf(fname);
						if (idx != -1) {
							testNumber = line.substring(idx + fname.length(), idx + fname.length() + 5);
						}
						lastLine = line;
					}
					else if (line.contains("Product version:")) {
						String version = line.substring(line.indexOf("version:")+"version:".length());
						tr.setToolVersion(version);
					}
				}
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		reader.close();
		tr.setTime(calculateTime(firstLine, lastLine));
		return tr;
	}

	private String calculateTime(final String firstLine, final String lastLine) {
		try {
			String start = firstLine.split(" ")[0];
			String stop = lastLine.split(" ")[0];
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss,SSS");
			Date startTime = sdf.parse(start);
			Date stopTime = sdf.parse(stop);
			long startMillis = startTime.getTime();
			long stopMillis = stopTime.getTime();
			return (stopMillis - startMillis) / 1000 + " seconds";
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void process(final TestResults tr, String testNumber, final List<String> chunk, String preffix) throws Exception {
		for (String line : chunk) {
			TestCaseResult tcr = new TestCaseResult();

			String fname = "/" + BenchmarkScore.BENCHMARKTESTNAME;
			int idx = line.indexOf(fname);
			if (idx != -1) {
				testNumber = line.substring(idx + fname.length(), idx + fname.length() + 5);
			}

			String type = preffix + line.substring(line.indexOf("type=") + 5, line.indexOf(',', line.indexOf("type=")));

			try {
				tcr.setCWE(type.hashCode());
				tcr.setCategory(type);

				try {
					tcr.setNumber(Integer.parseInt(testNumber));
				}
				catch (NumberFormatException e) {
					System.out.println("> Parse error: " + line);
				}

				System.out.println(tcr);


					// System.out.println( tcr.getNumber() + "\t" + tcr.getCWE() + "\t" + tcr.getCategory() );
					tr.put(tcr);

			}
			catch (Exception e) {
				if (invalid.add(type)) {
					System.out.println("Invalid type:" + type);
				}
			}

		}
	}


}
