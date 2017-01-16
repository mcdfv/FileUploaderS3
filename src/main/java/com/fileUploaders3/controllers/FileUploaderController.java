package com.fileUploaders3.controllers;

import com.fileUploaders3.dto.FileStatistics;
import com.fileUploaders3.dto.FileUploadResult;
import com.fileUploaders3.services.FileUploaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vladimir on 14.01.2017.
 */
@Controller
@RequestMapping(value={"/app"})
public class FileUploaderController {
    private static final Logger LOGGER = LoggerFactory.getLogger("web");

    @Autowired
    private FileUploaderService fileUploaderService;

    @RequestMapping(method = RequestMethod.GET, value = {"/getStartPage"})
    @ResponseBody
    public String getWorkingPage() {
        StringBuilder helloPageBuilder = new StringBuilder();
        helloPageBuilder.append("<!DOCTYPE html>")
                .append("<html><head>")
                .append("<meta charset=\"UTF-8\"><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">")
                .append("</head><body>")
                .append("<h1>File uploader to amazone S3 service is working</h1>")
                .append("</body><html>");
        return helloPageBuilder.toString();
    }


    @RequestMapping(method = RequestMethod.POST, value = {"/uploadFile"})
    @ResponseBody
    public ResponseEntity<FileUploadResult> uploadFile(@RequestParam("name") String name,
                                                       @RequestParam("file") MultipartFile file) {
        String resultMessage;
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                resultMessage = fileUploaderService.uploadToAmazoneS3Service(bytes, name);
            } catch (IOException e) {
                LOGGER.info("An error occurred when reading uploaded file");
                resultMessage = "An error occurred when reading uploaded file";
            }
        } else {
            resultMessage = "File is empty";
        }

        FileUploadResult res = new FileUploadResult();
        res.setResultMessage(resultMessage);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.GET, value = {"/getStatistics"})
    @ResponseBody
    public ResponseEntity<List<FileStatistics>> getStatistics() {
        List<FileStatistics> statistics = fileUploaderService.getStatistics();
        return new ResponseEntity<>(statistics, HttpStatus.OK);
    }
}
