package lab1;

public class DFA {
    private Integer n;
    private Integer n1;
    private Character ch;
    DFA(Integer n,Integer n1,Character ch)
    {
        this.n=n;
        this.n1=n1;
        this.ch=ch;
    }
    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public Integer getN1() {
        return n1;
    }

    public void setN1(Integer n1) {
        this.n1 = n1;
    }

    public Character getCh() {
        return ch;
    }

    public void setCh(Character ch) {
        this.ch = ch;
    }
    @Override
    public String toString()
    {

        return this.n+"---->"+this.n1+": "+this.ch;
    }
    @Override
    public boolean equals(Object dfa)
    {
        if (this == dfa) {
            return true;
        }
            if (dfa instanceof DFA)
            if(this.getN()==((DFA)dfa).getN()&&this.getN1()==((DFA)dfa).getN1()&&this.getCh()==((DFA)dfa).getCh())
                return true;
        return false;
    }
    @Override
    public int hashCode()
    {
        return (this.getN()+""+this.getN1()+""+this.getCh()).hashCode();
    }
}
