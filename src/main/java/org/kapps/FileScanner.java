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
import java.io.InputStream;
import java.nio.file.Files;

public class FileScanner {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileScanner.class);

    public static FileInfo scan(File file) {
        FileInfo info = new FileInfo();
        try {
            info.path = file.getAbsolutePath();
            info.sizeBytes = file.length();
            info.extension = getExtension(file.getName());

            // Tika - detect MIME
            Tika tika = new Tika();
            info.mimeType = tika.detect(file);

            // MD5
            try (InputStream is = Files.newInputStream(file.toPath())) {
                info.md5 = DigestUtils.md5Hex(is);
            }

            // xxHash32
            byte[] data = Files.readAllBytes(file.toPath());
            XXHashFactory factory = XXHashFactory.fastestInstance();
            info.xxHash32 = factory.hash32().hash(data, 0, data.length, 0);

            // Image metadata (JPEG/PNG)
            if (info.mimeType.startsWith("image/")) {
                try {
                    ImageInfo imageInfo = Imaging.getImageInfo(file);
                    info.width = imageInfo.getWidth();
                    info.height = imageInfo.getHeight();
                } catch (Exception e) {
                    LOGGER.info("Failed to image metadata from {}", file, e);
                }
            }

            // Video resolution (if applicable)
            if (info.mimeType.startsWith("video/")) {
                try {
                    FrameGrab grab = FrameGrab.createFrameGrab(NIOUtils.readableChannel(file));
                    Picture frame = grab.getNativeFrame();
                    if (frame != null) {
                        info.width = frame.getWidth();
                        info.height = frame.getHeight();
                    }
                } catch (Exception e) {
                    LOGGER.info("Failed to video metadata from {}", file, e);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Error scanning file: " + file, e);
        }

        return info;
    }

    private static String getExtension(String fileName) {
        int dot = fileName.lastIndexOf('.');
        return (dot == -1) ? "" : fileName.substring(dot + 1).toLowerCase();
    }
}
