package org.kapps;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.net.URL;
import static org.junit.jupiter.api.Assertions.*;

public class FileScannerTest {

    @Test
    void testScanImageFile() {
        File testFile = getResourceFile("files/test-image.jpg");
        assertNotNull(testFile, "Test file not found");

        FileInfo info = FileScanner.scan(testFile);

        assertNotNull(info);
        assertEquals("jpg", info.extension);
        assertTrue(info.sizeBytes > 0);
        assertNotNull(info.mimeType);
        assertNotNull(info.md5);
        assertTrue(info.xxHash32 != 0);
        assertNotNull(info.width);
        assertNotNull(info.height);

        System.out.println(info);
    }

    @Test
    void testScanNonImageFile() {
        File testFile = getResourceFile("files/test.txt");
        assertNotNull(testFile, "Test file not found");

        FileInfo info = FileScanner.scan(testFile);

        assertNotNull(info);
        assertEquals("txt", info.extension);
        assertTrue(info.sizeBytes > 0);
        assertNotNull(info.mimeType);
        assertNotNull(info.md5);
        assertTrue(info.xxHash32 != 0);

        assertNull(info.width);  // no dimensions for text
        assertNull(info.height);
    }

    private File getResourceFile(String name) {
        URL url = getClass().getClassLoader().getResource(name);
        return (url != null) ? new File(url.getFile()) : null;
    }
}
