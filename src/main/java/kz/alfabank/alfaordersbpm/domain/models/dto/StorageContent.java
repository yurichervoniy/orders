package kz.alfabank.alfaordersbpm.domain.models.dto;

import java.util.Optional;

public class StorageContent {
    private final byte[] bytes;
    private final String contentType;
    private final String fileName;

    public StorageContent(byte[] bytes, String contentType, String fileName) {
        this.bytes = bytes;
        this.contentType = contentType;
        this.fileName = fileName;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public Optional<String> getContentType() {
        return Optional.ofNullable(contentType);
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public String toString() {
        return "StorageContent{" +
                "bytesLength=" + bytes.length +
                ", contentType='" + contentType + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
