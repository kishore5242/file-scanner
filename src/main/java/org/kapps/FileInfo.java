package org.kapps;

public class FileInfo {
    public String path;
    public long sizeBytes;
    public String extension;
    public String mimeType;
    public String md5;
    public int xxHash32;

    public Integer width;     // nullable
    public Integer height;    // nullable

    @Override
    public String toString() {
        return String.format(
                "Path: %s\nSize: %d bytes\nExtension: %s\nMIME: %s\nMD5: %s\nxxHash32: %d\nResolution: %dx%d",
                path, sizeBytes, extension, mimeType, md5, xxHash32,
                width != null ? width : 0,
                height != null ? height : 0
        );
    }
}