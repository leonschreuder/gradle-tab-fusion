package de.leonmoll.gradle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {
    public static void writeStringToFile(File outFile, String fileContentString) {
        BufferedWriter bw = null;
        FileWriter fw = null;

        try {
            fw = new FileWriter(outFile);
            bw = new BufferedWriter(fw);
            bw.write(fileContentString);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) bw.close();
                if (fw != null) fw.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
