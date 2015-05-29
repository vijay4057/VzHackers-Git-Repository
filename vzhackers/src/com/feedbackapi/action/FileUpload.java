package com.feedbackapi.action;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileUpload {
	public static void main(String[] args) throws Exception {
		BufferedReader br;
		BufferedWriter output = null;
		try {
			br = new BufferedReader(new FileReader(
					"D:\\Vz Hackers\\HackathonInput.txt"));
			File file = new File("D:\\vz Hackers\\VzHackers_Out.txt");
			output = new BufferedWriter(new FileWriter(file));

			String line;
			Dictionary dictionary = new Dictionary();
			dictionary.getWords();

			while ((line = br.readLine()) != null) {

				FeedbackEngineNew feedbackEngineNew = new FeedbackEngineNew();
				String feedBackResult = feedbackEngineNew.findFeedBack(line);

				output.write(feedBackResult+"\n");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (output != null)
				output.close();
		}

	}
}
