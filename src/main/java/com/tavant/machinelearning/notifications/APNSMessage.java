
package com.tavant.machinelearning.notifications;




import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;



public class APNSMessage implements Comparable {

    public enum DESTINATION {
        DEFAULT,
        TEST
    }

    public static final byte COMMAND = 0;

    private static Short deviceTokenLenght;

    private static byte[] deviceToken;
    private static Short payloadLength;
    private static byte[] payLoad;
    private String destination;


    /**
     * Message constructor takes the deviceToken as string and the payload
     *
     * @param deviceToken
     * @param payload
     * @throws UnsupportedEncodingException
     * @throws DecoderException
     */
    public APNSMessage(String deviceToken, String payload) throws UnsupportedEncodingException, DecoderException {
        if (payload.length() > 256) Logger.getAnonymousLogger().warning("payload length > 256 ");
        this.deviceToken    =   Hex.decodeHex(deviceToken.replace(" ", "").toCharArray());
        deviceTokenLenght   =   (short) this.deviceToken.length;
        payLoad             =   payload.getBytes("UTF-8");
        payloadLength       =   (short) this.payLoad.length;
    }


    public APNSMessage(String deviceToken, String payLoad, String connection) throws DecoderException, UnsupportedEncodingException {
        this(deviceToken, payLoad);
        this.destination = connection;
    }

    public static  Short getDeviceTokenLenght() {
        return deviceTokenLenght;
    }

    public void setDeviceTokenLenght(Short deviceTokenLenght) {
        this.deviceTokenLenght = deviceTokenLenght;
    }

    public static  byte[] getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(byte[] deviceToken) {
        this.deviceToken = deviceToken;
    }

    public static short getPayloadLength() {
        return payloadLength;
    }

    public void setPayloadLength(short payloadLength) {
        this.payloadLength = payloadLength;
    }

    public static byte[] getPayLoad() {
        return payLoad;
    }

    public void setPayLoad(byte[] payLoad) {

        this.payLoad = payLoad;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int compareTo(Object o) {
        APNSMessage APNSMessage = (APNSMessage) o;
        if (APNSMessage.deviceTokenLenght == this.deviceTokenLenght && APNSMessage.payloadLength == this.getPayloadLength()) {
            return 0;
        } else {
            return 1;
        }
    }
}
