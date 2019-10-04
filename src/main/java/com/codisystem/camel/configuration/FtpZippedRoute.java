package com.codisystem.camel.configuration;

import com.codisystem.camel.configuration.addr.Addresses;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ValidationException;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.dataformat.zipfile.ZipSplitter;
import org.apache.camel.model.dataformat.ZipFileDataFormat;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.JAXBContext;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Component
public class FtpZippedRoute extends RouteBuilder {

    private final Map<String, String> checksumList = Map.of(
            "resources.zip", "99E71F1F16ADB25B6C4C9615DC42107D",
            "sample2.zip", "a1c198fa6d76840d33a8a68b075b4e26");

    @Override
    public void configure() throws Exception {

        ZipFileDataFormat zipFile = new ZipFileDataFormat();
        zipFile.setUsingIterator(true);
        //filter only zip files
        from("sftp:localhost:22/upload?username=user1&password=pass&delete=true&antInclude=*.zip")

                .filter(this::checksum)

                .split(new ZipSplitter()).streaming()
                .convertBodyTo(String.class)
                .doTry()
                .to("validator:sample.xsd")

                .process(new XmlProcessor())
                .log("file without exception:${body}")
                .to("file:///home/tmp/file")

                .setHeader(KafkaConstants.KEY, constant("Camel"))
                .to("kafka:Topic2?brokers=192.168.1.61:9092")

                .endDoTry()
                .doCatch(ValidationException.class)
                .log("file with exception:${body}")
                .end();
        ;
    }

    private boolean checksum(Exchange exchange) {
        final var body = exchange.getIn().getBody(byte[].class);
        final var head = (String) exchange.getIn().getHeader("CamelFileName");

        if(!checksumList.containsKey(head)){
            return false;
        }

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(body);
            byte[] digest = md.digest();
            var myChecksum = DatatypeConverter.printHexBinary(digest).toUpperCase();
            System.out.println("this is checksum:" + myChecksum);
            final var blue = checksumList.get(head);
            if (blue != null && blue.equals(myChecksum)) {
                System.out.println("checksum match");
                return true;
            } else {
                System.out.println("checksum not match");
                return false;
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
    }


    private static class XmlProcessor implements Processor {

        @Override
        public void process(Exchange exchange) throws Exception {
            var input = exchange.getIn().getBody(InputStream.class);
            var jaxbContext = JAXBContext.newInstance(Addresses.class);
            var jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            var x = (Addresses) jaxbUnmarshaller.unmarshal(input);
            x.getAddressList().forEach(System.out::println);
        }
    }

}
