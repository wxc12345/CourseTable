package cclovecc;

/**
 * Url抽象工厂
 */
public abstract class ZhengFangUrl {
    public String URL_LOGIN;
    public String URL_CHECKCODE;
    public String REFERER_CHECKCODE;
    public String URL_LOGIN_REQUEST;
    public String REFERER_LOGIN_REQUEST;
    public String URL_BASE;
    public String REFERER__MAINPAGE_BASE;

    protected static ZhengFangUrl instance;
}
