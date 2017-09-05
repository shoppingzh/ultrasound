package com.littlezheng.xmlparse_demo;

import android.content.Context;
import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Administrator on 2017/9/5/005.
 */

public class Configuration {

    private static final String TAG = "Configuration";

    private final Context mContext;
    private final String pathname;

    private int receivePort;
    private int receivePacketSize;
    private int receiveBufferSize;

    private String sendAddress;
    private int sendPort;

    private String sampleDataFile;

    public Configuration(Context context, String pathname)
            throws ParserConfigurationException, SAXException, IOException {
        mContext = context;
        this.pathname = pathname;
        parseXML();
    }

    private void parseXML() throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        parser.parse(mContext.getAssets().open(pathname),new DefaultHandler(){

            @Override
            public void startDocument() throws SAXException {
                super.startDocument();
//                Log.d(TAG,"开始文档");
            }

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
//                Log.d(TAG,"开始元素：" + localName + ", " + qName);
//                for(int i=0;i<attributes.getLength();i++){
//                    attributes.getValue(qName);
//                    String value = attributes.getValue(i);
//                    Log.d(TAG,"属性"+i+"：" + value);
//                }

            }

            @Override
            public void characters(char[] ch, int start, int length) throws SAXException {
                super.characters(ch, start, length);
//                Log.d(TAG,"字符内容："+new String(ch, start, length));
            }



            @Override
            public void endElement(String uri, String localName, String qName) throws SAXException {
                super.endElement(uri, localName, qName);
//                Log.d(TAG,"结束元素");
            }

            @Override
            public void endDocument() throws SAXException {
                super.endDocument();
//                Log.d(TAG,"结束文档");
                Log.d(TAG,"读取到的配置信息：" + Configuration.this.toString());
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
