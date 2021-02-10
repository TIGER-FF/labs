package lab4;

import java.util.ArrayList;
import java.util.List;

public class lab4Main {
    public static void main(String[] args) throws Exception {
        Grammar g=new Grammar();
        g.setStartsymbol("S");
        List<nonterm> nontermList=new ArrayList<>();
        nontermList.add(new nonterm("S"));
        nontermList.add(new nonterm("L"));

        g.setNonterminalsymbols(nontermList);
        g.setName("G0");
        List<term> termList=new ArrayList<>();
        termList.add(new term("a","a"));
        termList.add(new term("(","("));
        termList.add(new term(")",")"));
        termList.add(new term(",",","));
        g.setTerminalsymbols(termList);

        List<production> productions=new ArrayList<>();


        List<symbol> symbolList=new ArrayList<>();
        symbolList.add(new symbol("(","term"));
        symbolList.add(new symbol("L","nonterm"));
        symbolList.add(new symbol(")","term"));
        production production=new production();
        production.setLhs("S");
        production.setRhs(symbolList);
        productions.add(production);


        List<symbol> symbolList1=new ArrayList<>();
        symbolList1.add(new symbol("a","term"));
        production production1=new production();
        production1.setLhs("S");
        production1.setRhs(symbolList1);
        productions.add(production1);


        List<symbol> symbolList2=new ArrayList<>();
        symbolList2.add(new symbol("L","nonterm"));
        symbolList2.add(new symbol(",","term"));
        symbolList2.add(new symbol("S","nonterm"));
        production production2=new production();
        production2.setLhs("L");
        production2.setRhs(symbolList2);
        productions.add(production2);



        List<symbol> symbolList3=new ArrayList<>();
        symbolList3.add(new symbol("S","nonterm"));
        production production3=new production();
        production3.setLhs("L");
        production3.setRhs(symbolList3);
        productions.add(production3);

//
//        List<symbol> symbolList4=new ArrayList<>();
//        symbolList4.add(new symbol("true","term"));
//        production production4=new production();
//        production4.setLhs("B");
//        production4.setRhs(symbolList4);
//        productions.add(production4);
//
//        List<symbol> symbolList5=new ArrayList<>();
//        symbolList5.add(new symbol("(","term"));
//        symbolList5.add(new symbol("B","nonterm"));
//        symbolList5.add(new symbol(")","term"));
//        production production5=new production();
//        production5.setLhs("B");
//        production5.setRhs(symbolList5);
//        productions.add(production5);


        g.setProductions(productions);


        GrammarUtil grammarUtil=new GrammarUtil();
//        Grammar g = grammarUtil.readJson("src\\main\\resources\\input3.json");
        grammarUtil.showGrammar(g);

        OPMain opMain=new OPMain();
        opMain.getMatrix(g);
    }
}
