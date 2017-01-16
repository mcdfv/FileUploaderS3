package com.fileUploaders3.services;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.fileUploaders3.dto.FileStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Vladimir on 14.01.2017.
 */
@Service
public class FileUploaderService {

    private static final Logger LOGGER = LoggerFactory.getLogger("service");

    @Autowired
    private String bucketName;

    private AmazonS3 s3Client = null;

    @PostConstruct
    public void init() {
        s3Client = AmazonS3ClientBuilder.standard()
                //.withCredentials(new ProfileCredentialsProvider("default"))
                .withCredentials(new EnvironmentVariableCredentialsProvider())
                .withRegion(Regions.EU_WEST_1)
                .build();
        LOGGER.info("Amazon S3 client was initialized");
    }

    public String uploadToAmazoneS3Service(byte[] bytes, String filename) {
        try {
            InputStream fileInputStream  = new ByteArrayInputStream(bytes);
            ObjectMetadata objMetaData = new ObjectMetadata();
            objMetaData.setContentLength(bytes.length);
            objMetaData.setCacheControl("no-cache");
            PutObjectResult result = s3Client.putObject(bucketName, filename, fileInputStream, new ObjectMetadata());
            return "File name was successfully uploaded to amazon S3 service. File`s E TAG = " + result.getETag();
        } catch (AmazonServiceException e) {
            LOGGER.info("Error occurred while uploading file. Error message = {}", e.getErrorMessage());
            return "Error occurred while uploading file. Error message = " + e.getErrorMessage();
        }
    }

    public List<FileStatistics> getStatistics() {
        ObjectListing objectListing = s3Client.listObjects(bucketName);
        return objectListing.getObjectSummaries().stream().map((S3ObjectSummary os) -> {
            FileStatistics fileStatistics = new FileStatistics();
            fileStatistics.setKey(os.getKey());
            fileStatistics.setSize(os.getSize());
            fileStatistics.seteTag(os.getETag());
            return fileStatistics;
        }).collect(Collectors.toList());
    }

}
