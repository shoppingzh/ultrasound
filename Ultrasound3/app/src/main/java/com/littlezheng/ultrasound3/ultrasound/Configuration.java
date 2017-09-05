package com.littlezheng.ultrasound3.ultrasound;

import android.content.Context;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Administrator on 2017/9/5/005.
 */

public class Configuration {

    private static final String TAG = "Configuration";

    //配置信息
    private int receivePort;
    private int receivePacketSize;
    private int receiveBufferSize;
    private String sendAddress;
    private int sendPort;
    private String sampleDataFile;

    private final Context context;
    private final InputStream in;

    public Configuration(Context context, String pathname){
        this.context = context;
        try {
            in = context.getAssets().open(pathname);
            parseXML();
        } catch (Exception e) {
            throw new RuntimeException("配置信息加载出错！");
        }
    }

    /**
     * 解析xml文件将配置信息加载到该类中
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    private void parseXML() throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        parser.parse(in, new DefaultHandler(){
            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                super.startElement(uri, localName, qName, attributes);
                switch (localName){
                    case "receiver":
                        setReceivePort(Integer.valueOf(attributes.getValue("port")));
                        setReceivePacketSize(Integer.valueOf(attributes.getValue("packet-size")));
                        setReceiveBufferSize(Integer.valueOf(attributes.getValue("buffer-size")));
                        break;
                    case "sender":
                        setSendAddress(attributes.getValue("address"));
                        setSendPort(Integer.valueOf(attributes.getValue("port")));
                        break;
                    case "sample-data":
                        setSampleDataFile(attributes.getValue("assets-file"));
                        break;
                }
            }
        });
    }

    /****************************GETTERS/SETTERS***************************/

    public int getReceivePort() {
        return receivePort;
    }

    public void setReceivePort(int receivePort) {
        this.receivePort = receivePort;
    }

    public int getReceivePacketSize() {
        return receivePacketSize;
    }

    public void setReceivePacketSize(int receivePacketSize) {
        this.receivePacketSize = receivePacketSize;
    }

    public int getReceiveBufferSize() {
        return receiveBufferSize;
    }

    public void setReceiveBufferSize(int receiveBufferSize) {
        this.receiveBufferSize = receiveBufferSize;
    }

    public String getSendAddress() {
        return sendAddress;
    }

    public void setSendAddress(String sendAddress) {
        this.sendAddress = sendAddress;
    }

    public int getSendPort() {
        return sendPort;
    }

    public void setSendPort(int sendPort) {
        this.sendPort = sendPort;
    }

    public String getSampleDataFile() {
        return sampleDataFile;
    }

    public void setSampleDataFile(String sampleDataFile) {
        this.sampleDataFile = sampleDataFile;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "receivePort=" + receivePort +
                ", receivePacketSize=" + receivePacketSize +
                ", receiveBufferSize=" + receiveBufferSize +
                ", sendAddress='" + sendAddress + '\'' +
                ", sendPort=" + sendPort +
                ", sampleDataFile='" + sampleDataFile + '\'' +
                '}';
    }
}
