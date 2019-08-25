/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qz;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author ares
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PrintRequest {

    private String a;
    private String printer;
    private String data;
    private PrintPageSetting pageSetting;
    private String origin;
    private String cookie;
    private String port;

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getPrinter() {
        return printer;
    }

    public void setPrinter(String printer) {
        this.printer = printer;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public PrintPageSetting getPageSetting() {
        return pageSetting;
    }

    public void setPageSetting(PrintPageSetting pageSetting) {
        this.pageSetting = pageSetting;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

}
