package de.leonmoll.gradle;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static junit.framework.TestCase.assertEquals;

public class FileUtilsTest {

    @Test
    public void should_write_string_to_file() throws IOException {
        String input = "--someFlag --someOtherFlag";
        File tempFile = File.createTempFile("testFile", "txt");

        FileUtils.writeStringToFile(tempFile, input);

        String fileContent = new String(Files.readAllBytes(Paths.get(tempFile.getAbsolutePath())));
        assertEquals(input, fileContent);
    }
}
