package com.jarredweb.rest.tools.ui.resource;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;

public class UploadClientTest {

    public static void main(String[] args) throws IOException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            final HttpPost httpPost = new HttpPost("http://localhost:8080/ws/user/1/upload");
            FileBody endpoints = new FileBody(new File("C:/temp/sample.json"));
            StringBody comment = new StringBody("json endpoints", ContentType.TEXT_PLAIN);
            HttpEntity reqEntity = MultipartEntityBuilder.create()
                    .addPart("endpoints", endpoints)
                    .addPart("comment", comment)
                    .build();
            httpPost.setEntity(reqEntity);

            System.out.println("executing request " + httpPost.getRequestLine());
            try (CloseableHttpResponse response = httpclient.execute(httpPost)) {


                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    System.out.println("Response content length: " + resEntity.getContentLength());
                }
                //do something useful with the response
                EntityUtils.consume(resEntity);
            }
        }
    }
}
