package lab3;

import com.kitfox.svg.A;
import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;
import lombok.SneakyThrows;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;
import static guru.nidi.graphviz.model.Link.to;

public class test {
    @SneakyThrows
    public static void main(String[] args) {
//        Grammar g=new Grammar();
//        g.setStartsymbol("E");
//        List<nonterm> nontermList=new ArrayList<>();
//        nontermList.add(new nonterm("E"));
//        nontermList.add(new nonterm("T"));
//        nontermList.add(new nonterm("F"));
//        nontermList.add(new nonterm("E1"));
//        nontermList.add(new nonterm("T1"));
//        g.setNonterminalsymbols(nontermList);
//        g.setName("G0");
//        List<term> termList=new ArrayList<>();
//        termList.add(new term("+","+"));
//        termList.add(new term("*","*"));
//        termList.add(new term("(","("));
//        termList.add(new term(")",")"));
//        termList.add(new term("id","id"));
//        g.setTerminalsymbols(termList);
//        List<production> productions=new ArrayList<>();
//        List<symbol> symbolList=new ArrayList<>();
//        symbolList.add(new symbol("T","nonterm"));
//        symbolList.add(new symbol("E1","nonterm"));
//        production production=new production();
//        production.setLhs("E");
//        production.setRhs(symbolList);
//        productions.add(production);
//
//
//        List<symbol> symbolList1=new ArrayList<>();
//        symbolList1.add(new symbol("+","term"));
//        symbolList1.add(new symbol("T","nonterm"));
//        symbolList1.add(new symbol("E1","nonterm"));
//        production production1=new production();
//        production1.setLhs("E1");
//        production1.setRhs(symbolList1);
//        productions.add(production1);
//
//        List<symbol> symbolList2=new ArrayList<>();
//        symbolList2.add(new symbol("epsilon","term"));
//        production production2=new production();
//        production2.setLhs("E1");
//        production2.setRhs(symbolList2);
//        productions.add(production2);
//
//        List<symbol> symbolList3=new ArrayList<>();
//        symbolList3.add(new symbol("F","nonterm"));
//        symbolList3.add(new symbol("T1","nonterm"));
//        production production3=new production();
//        production3.setLhs("T");
//        production3.setRhs(symbolList3);
//        productions.add(production3);
//
//        List<symbol> symbolList4=new ArrayList<>();
//        symbolList4.add(new symbol("*","term"));
//        symbolList4.add(new symbol("F","nonterm"));
//        symbolList4.add(new symbol("T1","nonterm"));
//        production production4=new production();
//        production4.setLhs("T1");
//        production4.setRhs(symbolList4);
//        productions.add(production4);
//
//        List<symbol> symbolList5=new ArrayList<>();
//        symbolList5.add(new symbol("epsilon","term"));
//        production production5=new production();
//        production5.setLhs("T1");
//        production5.setRhs(symbolList5);
//        productions.add(production5);
//
//
//        List<symbol> symbolList6=new ArrayList<>();
//        symbolList6.add(new symbol("(","term"));
//        symbolList6.add(new symbol("E","nonterm"));
//        symbolList6.add(new symbol(")","term"));
//        production production6=new production();
//        production6.setLhs("F");
//        production6.setRhs(symbolList6);
//        productions.add(production6);
//
//        List<symbol> symbolList7=new ArrayList<>();
//        symbolList7.add(new symbol("id","term"));
//        production production7=new production();
//        production7.setLhs("F");
//        production7.setRhs(symbolList7);
//        productions.add(production7);
//
//        g.setProductions(productions);


//        g.setStartsymbol("S");
//        List<nonterm> nontermList=new ArrayList<>();
//        nontermList.add(new nonterm("S"));
//        nontermList.add(new nonterm("A"));
//        nontermList.add(new nonterm("B"));
//
//        g.setNonterminalsymbols(nontermList);
//        g.setName("G0");
//        List<term> termList=new ArrayList<>();
//        termList.add(new term("a","a"));
//        termList.add(new term("b","b"));
//        termList.add(new term("epsilon","epsilon"));
//
//        g.setTerminalsymbols(termList);
//        List<production> productions=new ArrayList<>();
//        List<symbol> symbolList=new ArrayList<>();
//        symbolList.add(new symbol("A","nonterm"));
//        symbolList.add(new symbol("b","term"));
//        production production=new production();
//        production.setLhs("S");
//        production.setRhs(symbolList);
//        productions.add(production);
//
//
//        List<symbol> symbolList1=new ArrayList<>();
//        symbolList1.add(new symbol("a","term"));
//        production production1=new production();
//        production1.setLhs("A");
//        production1.setRhs(symbolList1);
//        productions.add(production1);
//
//        List<symbol> symbolList2=new ArrayList<>();
//        symbolList2.add(new symbol("B","nonterm"));
//        production production2=new production();
//        production2.setLhs("A");
//        production2.setRhs(symbolList2);
//        productions.add(production2);
//
//        List<symbol> symbolList3=new ArrayList<>();
//        symbolList3.add(new symbol("epsilon","term"));
//        production production3=new production();
//        production3.setLhs("A");
//        production3.setRhs(symbolList3);
//        productions.add(production3);
//
//        List<symbol> symbolList4=new ArrayList<>();
//        symbolList4.add(new symbol("b","term"));
//        production production4=new production();
//        production4.setLhs("B");
//        production4.setRhs(symbolList4);
//        productions.add(production4);
//
//        List<symbol> symbolList5=new ArrayList<>();
//        symbolList5.add(new symbol("epsilon","term"));
//        production production5=new production();
//        production5.setLhs("B");
//        production5.setRhs(symbolList5);
//        productions.add(production5);
//
//
//        g.setProductions(productions);
//        RemoveLeft removeLeft=new RemoveLeft();
//        removeLeft.showGrammar(g);
//        removeLeft.GetFirst(g);
//        Node
//                init = node("init"),
//                execute = node("execute"),
//                compare = node("compare").with(Shape.RECTANGLE, Style.FILLED, Color.hsv(.7, .3, 1.0)),
//                mkString = node("mkString").with(Label.of("make a\nstring")),
//                printf = node("printf");
//        Node node=node("ss");
//        Node link = node("main").with(Shape.RECTANGLE).link(
//                to(node("parse").link(execute)).with("weight", 8),
//                to(init).with(Style.DOTTED),
//                node("cleanup"),
//                to(printf).with(Style.BOLD, Label.of("100 times"), Color.RED));
//        Node link1=execute.link(
//                graph().with(mkString, printf),
//                to(compare).with(Color.RED));
//        Node link2 = init.link(mkString);
//        ArrayList<Node> al=new ArrayList<>();
//        al.add(link);
//        al.add(link1);
//        al.add(link2);
//        Node init = node("init");
//        Node execute = node("execute");
//        Node execute1 = node("execute1");
//        init=init.link(execute);
//        init=init.link(execute1);
//
//        Graph g = graph("example2").with(init);
//        Graphviz.fromGraph(g).width(900).render(Format.PNG).toFile(new File("ex2.png"));
//        String[] in={"identifier","&","true","!","false"};
//        System.out.println(in.length);

       StringBuilder sb=new StringBuilder();
       sb.append("i");
       List<nonterm> nontermList=new ArrayList<>();
       nontermList.add(new nonterm("s"));
        nontermList.add(new nonterm("b"));
        System.out.println(nontermList.contains(new nonterm("s")));

    }
}
