package org.acme.cxf;

import java.io.IOException;
import java.util.function.Supplier;

import javax.xml.namespace.QName;
import javax.xml.parsers.*;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.acme.cxf.impl.FruitWebServiceImpl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;

@QuarkusTest
public class FruitWebServiceTest {

    private static QName SERVICE_NAME = new QName("http://cxf.acme.org/", "FruitWebServiceImpl");
    private static QName PORT_NAME = new QName("http://cxf.acme.org/", "FruitWebServiceImplPortType");

    private Service service;
    private FruitWebService fruitProxy;
    private FruitWebServiceImpl fruitImpl;

    {
        service = Service.create(SERVICE_NAME);
        final String endpointAddress = "http://localhost:8080/fruit";
        service.addPort(PORT_NAME, SOAPBinding.SOAP11HTTP_BINDING, endpointAddress);
    }

    @BeforeEach
    public void reinstantiateBaeldungInstances() {
        fruitImpl = new FruitWebServiceImpl();
        fruitProxy = service.getPort(PORT_NAME, FruitWebService.class);
    }

    @Test
    public void testAddFruitEndpoint() {
        String xml = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n"
                +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <tem:count>\n" +
                "      </tem:count>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";
        String cnt = "";
        try {
            Response response = RestAssured.given().header("Content-Type", "text/xml").and().body(xml).when().post("/fruit");
            response.then().statusCode(200);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(response.body().asInputStream());
            doc.getDocumentElement().normalize();
            XPath xpath = XPathFactory.newInstance().newXPath();

            cnt = xpath.compile("/Envelope/Body/countResponse/return").evaluate(doc);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals("2", cnt);
    }

}