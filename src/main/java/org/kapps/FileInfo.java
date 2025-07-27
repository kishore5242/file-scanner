package org.kapps;

public class FileInfo {
    private final String path;
    private final long sizeBytes;
    private final String extension;
    private final String mimeType;
    private final String md5;
    private final int xxHash32;
    private final Integer width;
    private final Integer height;

    public FileInfo(FileInfoBuilder builder) {
        this.path = builder.getPath();
        this.sizeBytes = builder.getSizeBytes();
        this.extension = builder.getExtension();
        this.mimeType = builder.getMimeType();
        this.md5 = builder.getMd5();
        this.xxHash32 = builder.getXxHash32();
        this.width = builder.getWidth();
        this.height = builder.getHeight();
    }

    public String getPath() {
        return path;
    }

    public long getSizeBytes() {
        return sizeBytes;
    }

    public String getExtension() {
        return extension;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getMd5() {
        return md5;
    }

    public int getXxHash32() {
        return xxHash32;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }
}
