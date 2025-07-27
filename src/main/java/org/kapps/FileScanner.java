package org.kapps;

import net.jpountz.xxhash.XXHashFactory;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.Imaging;
import org.apache.tika.Tika;
import org.jcodec.api.FrameGrab;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Picture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class FileScanner {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileScanner.class);

    public static FileInfo scan(File file) {
        FileInfoBuilder builder = new FileInfoBuilder();
        try {
            loadBasicInfo(file, builder);

            loadMimeInfo(file, builder);

            loadHashInfo(file, builder);

            if (builder.getMimeType().startsWith("image/")) {
                loadImageInfo(file, builder);
            }

            if (builder.getMimeType().startsWith("video/")) {
                loadVideoInfo(file, builder);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error scanning file: " + file, e);
        }

        return builder.build();
    }

    private static void loadBasicInfo(File file, FileInfoBuilder builder) {
        builder.path(file.getAbsolutePath()).sizeBytes(file.length())
                .extension(getExtension(file.getName()));
    }

    private static void loadVideoInfo(File file, FileInfoBuilder builder) {
        try {
            FrameGrab grab = FrameGrab.createFrameGrab(NIOUtils.readableChannel(file));
            Picture frame = grab.getNativeFrame();
            if (frame != null) {
                builder.width(frame.getWidth()).height(frame.getHeight());
            }
        } catch (Exception e) {
            LOGGER.info("Failed to video metadata from {}", file, e);
        }
    }

    private static void loadImageInfo(File file, FileInfoBuilder builder) {
        try {
            ImageInfo imageInfo = Imaging.getImageInfo(file);
            builder.width(imageInfo.getWidth()).height(imageInfo.getHeight());
        } catch (Exception e) {
            LOGGER.info("Failed to image metadata from {}", file, e);
        }
    }

    private static void loadHashInfo(File file, FileInfoBuilder builder) throws IOException {
        try (InputStream is = Files.newInputStream(file.toPath())) {
            builder.md5(DigestUtils.md5Hex(is));
        }

        // xxHash32
        byte[] data = Files.readAllBytes(file.toPath());
        XXHashFactory factory = XXHashFactory.fastestInstance();
        builder.xxHash32(factory.hash32().hash(data, 0, data.length, 0));
    }

    private static void loadMimeInfo(File file, FileInfoBuilder info) throws IOException {
        Tika tika = new Tika();
        info.mimeType(tika.detect(file));
    }

    private static String getExtension(String fileName) {
        int dot = fileName.lastIndexOf('.');
        return (dot == -1) ? "" : fileName.substring(dot + 1).toLowerCase();
    }
}
