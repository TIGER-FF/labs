package lab1;

import java.util.HashSet;

public class DFATree {
    private String ch;
    private DFATree fatherCh;
    private DFATree leftChildCh;
    private DFATree rightChildCh;
    private Boolean nullable=false;
    private HashSet<String> firstpos=new HashSet<String>();
    private HashSet<String> lastpos=new HashSet<String>();


    public HashSet<String> getLastpos() {
        return lastpos;
    }

    public void setLastpos(HashSet<String> lastpos) {
        this.lastpos = lastpos;
    }

    public HashSet<String> getFirstpos() {
        return firstpos;
    }

    public void setFirstpos(HashSet<String> firstpos) {
        this.firstpos = firstpos;
    }

    public Boolean getNullable() {
        return nullable;
    }

    public void setNullable(Boolean nullable) {
        this.nullable = nullable;
    }

    public String getCh() {
        return ch;
    }

    public void setCh(String ch) {
        this.ch = ch;
    }

    public DFATree getFatherCh() {
        return fatherCh;
    }

    public void setFatherCh(DFATree fatherCh) {
        this.fatherCh = fatherCh;
    }

    public DFATree getLeftChildCh() {
        return leftChildCh;
    }

    public void setLeftChildCh(DFATree leftChildCh) {
        this.leftChildCh = leftChildCh;
    }

    public DFATree getRightChildCh() {
        return rightChildCh;
    }

    public void setRightChildCh(DFATree rightChildCh) {
        this.rightChildCh = rightChildCh;
    }
}
