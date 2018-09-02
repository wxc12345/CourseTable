package cclovecc;

public class JNUZhengFangUrl extends ZhengFangUrl {
    public JNUZhengFangUrl() {
        URL_LOGIN = "http://202.195.144.163/jndx/default2.aspx";
        URL_CHECKCODE = "http://202.195.144.163/jndx/CheckCode.aspx";
        REFERER_CHECKCODE = "http://202.195.144.163/jndx/default2.aspx";
        URL_LOGIN_REQUEST = "http://202.195.144.163/jndx/default2.aspx";
        REFERER_LOGIN_REQUEST = "http://202.195.144.163/jndx/default2.aspx";
        URL_BASE = "http://202.195.144.163/jndx/";
        REFERER__MAINPAGE_BASE = "http://202.195.144.163/jndx/xs_main.aspx?xh=";

        instance=this;
    }
}
