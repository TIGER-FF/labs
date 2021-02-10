package lab3;




import java.util.ArrayList;
import java.util.List;

public class lab3Main {
    public static void main(String[] args) {
//        Grammar g=null;
//        RemoveLeft removeLeft=null;
//        try {
//            removeLeft=new RemoveLeft();
//            g=removeLeft.readJson("src\\main\\resources\\input3.json");
//        }
//        catch (Exception e)
//        {
//            System.out.println(e.toString());
//        }
        Grammar g=new Grammar();
        g.setStartsymbol("E");

        List<nonterm> nontermList=new ArrayList<>();
        nontermList.add(new nonterm("E"));
        nontermList.add(new nonterm("E1"));
        nontermList.add(new nonterm("T"));
        nontermList.add(new nonterm("T1"));
        nontermList.add(new nonterm("F"));
        g.setNonterminalsymbols(nontermList);
        g.setName("G0");



        List<term> termList=new ArrayList<>();
        termList.add(new term("+","+"));
        termList.add(new term("*","*"));
        termList.add(new term("(","("));
        termList.add(new term(")",")"));
        termList.add(new term("id","id"));
        termList.add(new term("epsilon","epsilon"));
        g.setTerminalsymbols(termList);

        List<production> productions=new ArrayList<>();


        List<symbol> symbolList=new ArrayList<>();
        symbolList.add(new symbol("T","nonterm"));
        symbolList.add(new symbol("E1","nonterm"));
        production production=new production();
        production.setLhs("E");
        production.setRhs(symbolList);
        productions.add(production);


        List<symbol> symbolList1=new ArrayList<>();
        symbolList1.add(new symbol("+","term"));
        symbolList1.add(new symbol("T","nonterm"));
        symbolList1.add(new symbol("E1","nonterm"));
        production production1=new production();
        production1.setLhs("E1");
        production1.setRhs(symbolList1);
        productions.add(production1);


        List<symbol> symbolList2=new ArrayList<>();
        symbolList2.add(new symbol("epsilon","term"));
        production production2=new production();
        production2.setLhs("E1");
        production2.setRhs(symbolList2);
        productions.add(production2);


        List<symbol> symbolList3=new ArrayList<>();
        symbolList3.add(new symbol("F","nonterm"));
        symbolList3.add(new symbol("T1","nonterm"));
        production production3=new production();
        production3.setLhs("T");
        production3.setRhs(symbolList3);
        productions.add(production3);


        List<symbol> symbolList4=new ArrayList<>();
        symbolList4.add(new symbol("*","term"));
        symbolList4.add(new symbol("F","nonterm"));
        symbolList4.add(new symbol("T1","nonterm"));
        production production4=new production();
        production4.setLhs("T1");
        production4.setRhs(symbolList4);
        productions.add(production4);

        List<symbol> symbolList5=new ArrayList<>();
        symbolList5.add(new symbol("epsilon","term"));
        production production5=new production();
        production5.setLhs("T1");
        production5.setRhs(symbolList5);
        productions.add(production5);



        List<symbol> symbolList6=new ArrayList<>();
        symbolList6.add(new symbol("(","term"));
        symbolList6.add(new symbol("E","nonterm"));
        symbolList6.add(new symbol(")","term"));
        production production6=new production();
        production6.setLhs("F");
        production6.setRhs(symbolList6);
        productions.add(production6);



        List<symbol> symbolList7=new ArrayList<>();
        symbolList7.add(new symbol("id","term"));
        production production7=new production();
        production7.setLhs("F");
        production7.setRhs(symbolList7);
        productions.add(production7);



        RemoveLeft removeLeft=new RemoveLeft();
        g.setProductions(productions);
        //消除左递归
       // g=removeLeft.removeLeft(g);
        removeLeft.showGrammar(g);
        //-------------------------------------
        String[] in={"(","id","+","id",")","*","id","+","id"};
        removeLeft.GetFirst(g,in);
    }
}
