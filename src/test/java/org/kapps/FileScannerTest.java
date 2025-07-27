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
        assertEquals("jpg", info.getExtension());
        assertTrue(info.getSizeBytes() > 0);
        assertNotNull(info.getMimeType());
        assertNotNull(info.getMd5());
        assertTrue(info.getXxHash32() != 0);
        assertNotNull(info.getWidth());
        assertNotNull(info.getHeight());
    }

    @Test
    void testScanNonImageFile() {
        File testFile = getResourceFile("files/test.txt");
        assertNotNull(testFile, "Test file not found");

        FileInfo info = FileScanner.scan(testFile);

        assertNotNull(info);
        assertEquals("txt", info.getExtension());
        assertTrue(info.getSizeBytes() > 0);
        assertNotNull(info.getMimeType());
        assertNotNull(info.getMd5());
        assertTrue(info.getXxHash32() != 0);

        assertNull(info.getWidth());  // no dimensions for text
        assertNull(info.getHeight());
    }

    private File getResourceFile(String name) {
        URL url = getClass().getClassLoader().getResource(name);
        return (url != null) ? new File(url.getFile()) : null;
    }
}
