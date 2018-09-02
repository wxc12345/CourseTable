package cclovecc;

/**
 * 简单工厂
 */
public class ZhengFangUrlFactory {
    public static ZhengFangUrl initUrl(String schoolName) throws Exception {
        switch (schoolName) {
            case ("JNU"):
                return new JNUZhengFangUrl();
            case ("JNUD5"):
                return new JNUD5Url();
            default:
                throw new Exception("School does not exist!!");
        }
    }
}
