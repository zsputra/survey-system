package com.emerio.surveysystem.service;

import io.minio.MinioClient;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
//import java.io.ByteArrayOutputStream;

@Service
public class FileService {

    @Value("${app.minio.host}")
    private String minioHost;
    //
    @Value("${app.minio.ip}")
    private String minioIp;

    @Value("${app.minio.access.key}")
    private String minioAccessKey;

    @Value("${app.minio.secret.key}")
    private String minioSecretKey;

    private MinioClient client;

    // public FileService() {
    // try {
    // this.client = new MinioClient("http://localhost:8081/",
    // "AKIAIOSFODNN7EXAMPLE",
    // "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY");
    // } catch (InvalidEndpointException | InvalidPortException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // }

    public InputStream getFile(String bucket, String filename) {
        try {
            try {
                this.client = new MinioClient(this.minioHost, this.minioAccessKey, this.minioSecretKey);
            } catch (InvalidEndpointException e) {
                e.printStackTrace();
            } catch (InvalidPortException e) {
                e.printStackTrace();
            }
            return client.getObject(bucket, filename);
        } catch (InvalidKeyException | InvalidBucketNameException | NoSuchAlgorithmException | InsufficientDataException
                | NoResponseException | ErrorResponseException | InternalException | InvalidArgumentException
                | InvalidResponseException | IOException | XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public void uploadFile(byte[] imageBuffer, String objectName) {
        try {
            try {
                this.client = new MinioClient(this.minioHost, this.minioAccessKey, this.minioSecretKey);
            } catch (InvalidEndpointException e) {
                e.printStackTrace();
            } catch (InvalidPortException e) {
                e.printStackTrace();
            }

            String ids = objectName.split("\\(")[0];

            int exp =  Integer.parseInt(objectName.split("\\(")[1].split("\\)")[0]);

            String format = ".jpeg";

            int i = 1;
            while (i<exp){

                client.removeObject("survey-system", ids+"("+i+")"+format);

                i++;
            }

                client.putObject("survey-system", objectName, new ByteArrayInputStream(imageBuffer), imageBuffer.length,
                        "application/octet-stream");

        } catch (InvalidBucketNameException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoResponseException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (ErrorResponseException e) {
            e.printStackTrace();
        } catch (InternalException e) {
            e.printStackTrace();
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        } catch (InsufficientDataException e) {
            e.printStackTrace();
        } catch (InvalidResponseException e) {
            e.printStackTrace();
        }
    }

    // public byte[] convertFile(BufferedImage bufferedImage) throws Exception {
    // //BufferedImage bufferimage = ImageIO.read(new File("myimage.jpg"));
    // ByteArrayOutputStream output = new ByteArrayOutputStream();
    // ImageIO.write(bufferedImage, "jpg", output );
    // byte [] data = output.toByteArray();
    // return data;
    // }

}
