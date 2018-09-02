package cclovecc;

public enum LoginState {
    SUCCESS,
    CHECKCODE_ERROR,
    USER_DOESNOT_EXIST,//18届用户名不存在，bug正在寻找中
    PASSWORD_ERRER,
    UNKNOWN_ERROR
}
