package de.leonmoll.gradle;

import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static junit.framework.TestCase.assertEquals;

public class FileUtilsTest {

    @After
    public void tearDown() {
        new File("testFile.txt").delete();
    }

    @Test
    public void should_write_string_to_file() throws IOException {
        String input = "--someFlag --someOtherFlag";
        File tempFile = new File("testFile.txt");

        FileUtils.writeStringToFile(tempFile, input);

        String fileContent = new String(Files.readAllBytes(Paths.get(tempFile.getAbsolutePath())));
        assertEquals(input, fileContent);
    }
}
