package util;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

public class XmlParser {
    public void paserXml(InputStream in){
        SAXReader reader=new SAXReader();
        try {
            Document read = reader.read(in);
            Element rootElement = read.getRootElement();
            List<Element> elements = rootElement.elements();
            for (Element e : elements) {
                System.out.println("element-->"+e.getName());
                List<Attribute> attributes = e.attributes();
                for (Attribute attribute : attributes) {
                    System.out.println(attribute.getName());
                    System.out.println(attribute.getValue());
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testPaserXml(){
        String xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "\n" +
                "<students name=\"zhangsan\">\n" +
                "    <hello name=\"lisi\">hello Text1</hello>\n" +
                "    <hello name=\"lisi2\">hello Text2</hello>\n" +
                "    <hello name=\"lisi3\">hello Text3</hello>\n" +
                "    <world name=\"wangwu\">world text1</world>\n" +
                "    <world name=\"wangwu2\">world text2</world>\n" +
                "    <world >world text3</world>\n" +
                "</students>";

        InputStream streamFromString = StreamHelper.getStreamFromString(xml);
        paserXml(streamFromString);
    }

}
