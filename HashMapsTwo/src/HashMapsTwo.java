/**
 * 
 * Convert cdh.csv file to a JSON file to use with indented tree
 * 
 * Author: Ritwik Gupta
 * Date: 6/25/2013
 * 
 * DBMI - CoSBBI
 * 
 * 
 * TODO: Make it open so any CSV file can be converted to JSON
 * TODO: Convert to Python
 * 
 */

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.IOException;

public class HashMapsTwo {

	public static void main(String[] args) {

		try {
			Map<String, ArrayList<ArrayList<String>>> map = new HashMap<String, ArrayList<ArrayList<String>>>();
			FileReader file = null, fileTwo = null, fileThr = null;
			File output = new File("cdhJSON.json"), outputTwo = new File(
					"cdhCSVTWO.csv");
			FileWriter write = null, writeTwo = null;

			// Instantiate FileWriters and BufferedWriters
			write = new FileWriter(output.getAbsoluteFile());
			writeTwo = new FileWriter(outputTwo.getAbsoluteFile());
			BufferedWriter bufWrite = new BufferedWriter(write);
			BufferedWriter bufWriteTwo = new BufferedWriter(writeTwo);

			// Populate map
			try {

				// Instantiate FileReaders and BufferedReaders
				file = new FileReader("cdh.csv");
				fileTwo = new FileReader("PathwaysTXT.csv");
				fileThr = new FileReader("DiseasesTXT.csv");
				BufferedReader reader = new BufferedReader(file);
				BufferedReader readerTwo = new BufferedReader(fileTwo);
				BufferedReader readerThr = new BufferedReader(fileThr);

				String line = "";

				while ((line = reader.readLine()) != null) {
					int comma = line.indexOf(',');

					if (!map.containsKey(line.substring(0, comma))) {
						map.put(line.substring(0, comma),
								new ArrayList<ArrayList<String>>());
						map.get(line.substring(0, comma)).add(
								new ArrayList<String>());
						map.get(line.substring(0, comma)).add(
								new ArrayList<String>());
						map.get(line.substring(0, comma)).add(
								new ArrayList<String>());
					}

					map.get(line.substring(0, comma)).get(0)
							.add(line.substring(comma + 1, line.length()));
				}

				// Add pathways
				while ((line = readerTwo.readLine()) != null) {
					int comma = line.indexOf(',');

					if (map.containsKey(line.substring(0, comma)))
						map.get(line.substring(0, comma)).get(1)
								.add(line.substring(comma + 1, line.length()));
				}
				/**/

				while ((line = readerThr.readLine()) != null) {
					int comma = line.indexOf(',');

					if (map.containsKey(line.substring(0, comma)))
						map.get(line.substring(0, comma)).get(2)
								.add(line.substring(comma + 1, line.length()));
				}

				for (String s : map.keySet()) {
					if (map.get(s).get(1).size() == 0)
						map.get(s).get(1).add("0");
					if (map.get(s).get(2).size() == 0)
						map.get(s).get(2).add("0");
				}

			} finally {
				if (file != null)
					file.close();
				if (fileTwo != null)
					fileTwo.close();
			}

			Iterator<String> iterator = map.keySet().iterator();

			// Starting line of JSON file
			bufWrite.write("{\n\"name\":\"Proteins\",\n\"children\": ["
					+ "\n\t");

			// Add children and interactions
			while (iterator.hasNext()) {

				String key = iterator.next().toString();

				if (!output.exists())
					output.createNewFile();
				{
				}

				// Write key
				bufWrite.write("\t{\n\t\t\"name\":\""
						+ key
						+ "\",\n\t\t\"children\":"
						+ "[\n\t\t\t{\n\t\t\t\"name\":\"Interactions\",\n\t\t\t\"children\":[\n\t\t\t");

				// Write value
				for (int i = 0; i < map.get(key).get(0).size(); i++) {
					if (i == map.get(key).get(0).size() - 1) {
						bufWrite.write("\t{\"name\": \""
								+ map.get(key).get(0).get(i) + "\"}\n\t\t\t"
								+ "]\n\t\t\t},\n\t\t\t");
						bufWrite.write("{\n\t\t\t\"name\":\"Pathways\",\n\t\t\t\"children\":[\n\t\t\t\t{\"name\":\""
								+ map.get(key).get(1).get(0) + "\"}");
						bufWrite.write("\n\t\t\t]\n\t\t\t},\n\t\t\t");
						bufWrite.write("{\n\t\t\t\"name\":\"Diseases\",\n\t\t\t\"children\":[\n\t\t\t\t{\"name\":\""
								+ map.get(key).get(2).get(0) + "\"}");
					} else
						bufWrite.write("\t{\"name\": \""
								+ map.get(key).get(0).get(i) + "\"},\n\t\t\t");
				}

				bufWrite.write("\n\t\t\t]\n\t\t\t}\n\t\t  ]\n\t\t},\n\t");

			}

			for (String s : map.keySet()) {
				bufWriteTwo.write(s + ",");
				for (int i = 0; i < map.get(s).get(0).size(); i++) {
					if (i == map.get(s).get(0).size() - 1)
						bufWriteTwo.write(map.get(s).get(0).get(i) + ",");
					else
						bufWriteTwo.write(map.get(s).get(0).get(i) + ";");
				}
				bufWriteTwo.write(map.get(s).get(1) + "," + map.get(s).get(2)
						+ "\n");
			}

			// Closing line of JSON file
			bufWrite.write("]\n}");

			bufWrite.close();
			bufWriteTwo.close();
		} catch (IOException e) {
		}
	}
}
