package org.kapps;

public class FileInfoBuilder {
    private String path;
    private long sizeBytes;
    private String extension;
    private String mimeType;
    private String md5;
    private int xxHash32;
    private Integer width;
    private Integer height;

    public FileInfoBuilder path(String path) { this.path = path; return this; }
    public FileInfoBuilder sizeBytes(long size) { this.sizeBytes = size; return this; }
    public FileInfoBuilder extension(String ext) { this.extension = ext; return this; }
    public FileInfoBuilder mimeType(String mime) { this.mimeType = mime; return this; }
    public FileInfoBuilder md5(String md5) { this.md5 = md5; return this; }
    public FileInfoBuilder xxHash32(int hash) { this.xxHash32 = hash; return this; }
    public FileInfoBuilder width(Integer width) { this.width = width; return this; }
    public FileInfoBuilder height(Integer height) { this.height = height; return this; }

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

    public FileInfo build() {
        return new FileInfo(this);
    }
}
