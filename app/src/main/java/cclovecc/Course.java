package cclovecc;

public class Course {
    public String line;
    int j;
    int row;
    public Course(int j,int i,String content) {
        this.line = content;
        this.j = j;
        this.row = i;
    }
    public String getLine(){
        return line;
    }
    public int getJ(){
        return j;
    }
    public int getRow(){
        return row;
    }

}
