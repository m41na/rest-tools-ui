package com.jarredweb.rest.tools.ui.resource;

import java.io.File;
import java.io.IOException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

public class UploadClientTest {

    public static void main(String[] args) throws IOException {
        final Client client = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();

        final FileDataBodyPart filePart = new FileDataBodyPart("file", new File("C:/temp/sample.json"));
        final FormDataMultiPart multipart;
        try (FormDataMultiPart formDataMultiPart = new FormDataMultiPart()) {
            multipart = (FormDataMultiPart) formDataMultiPart.field("foo", "bar").bodyPart(filePart);
            final WebTarget target = client.target("http://localhost:8080/ws/user/1/upload");
            final Response response = target.request().post(Entity.entity(multipart, multipart.getMediaType()));
            //Use response object to verify upload success
        }
        multipart.close();
    }
}
