package com.gtech.Ecommerce.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

@Service
public class S3Service {

    private static Logger LOG = LoggerFactory.getLogger(S3Service.class);

    @Autowired
    private AmazonS3 s3client;

    @Value("${s3.bucket}")
    private String bucketName;

    public URL uploadFile(MultipartFile file, String folder) {
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = FilenameUtils.getExtension(originalFilename);
            String filename = UUID.randomUUID().toString() + "." + extension;

            InputStream is = file.getInputStream();
            String contentType = file.getContentType();
            String key = folder + "/" + filename; // pasta com nome arquivo

            return uploadFile(is, key, contentType);
        }
        catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private URL uploadFile(InputStream is, String filename, String contentType) {
        ObjectMetadata meta = new ObjectMetadata();
        LOG.info("Upload started!");
        s3client.putObject(bucketName, filename, is, meta);
        LOG.info("Upload finished!");
        return s3client.getUrl(bucketName, filename);
    }
}
