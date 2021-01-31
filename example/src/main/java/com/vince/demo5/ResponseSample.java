package com.vince.demo5;

/**
 * @author vince
 * @since v1.0.0
 */
public class ResponseSample {

    private String code;
    private String data;
    private long timestamp;


    public ResponseSample() {
    }

    public ResponseSample(String code, String data, long timestamp) {
        this.code = code;
        this.data = data;
        this.timestamp = timestamp;
    }



    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ResponseSample{" +
                "code='" + code + '\'' +
                ", data='" + data + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
