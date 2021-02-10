package lab3;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.kitfox.svg.A;
import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;
import jsonUtil.FormatUtil;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;
import static guru.nidi.graphviz.model.Link.to;

//消除左递归
public class RemoveLeft {
    Map<String,HashSet<String>> first=new HashMap<>();
    Map<String,HashSet<String>> follow=new HashMap<>();
    Map<production,HashSet<String>> selected=new HashMap<>();
    public Grammar readJson(String path) throws Exception {
        //首先要读取json文件，并进行解析
        InputStream inputStream = new FileInputStream(path);
        String text = IOUtils.toString(inputStream,"utf8");
        text=JSONObject.parseObject(text).get("grammar").toString().replace("-","");
        JSONObject g=JSONObject.parseObject(text);
        //System.out.println(text);
        Grammar grammar=new Grammar();
        grammar.setName(g.getString("name"));
        grammar.setTerminalsymbols(g.getJSONObject("terminalsymbols").getJSONArray("term").toJavaList(term.class));
        grammar.setNonterminalsymbols(g.getJSONObject("nonterminalsymbols").getJSONArray("nonterm").toJavaList(nonterm.class));
        List<production> productions=grammar.getProductions();
        JSONArray jsonArray = g.getJSONObject("productions").getJSONArray("production");
        for(int i=0;i<jsonArray.size();i++)
        {
            productions.add(new production());
            productions.get(i).setLhs(jsonArray.getJSONObject(i).getJSONObject("lhs").getString("name"));
            String symbols = jsonArray.getJSONObject(i).getJSONObject("rhs").getString("symbol");
            if(symbols.contains("["))
                productions.get(i).setRhs(JSON.parseArray(symbols, symbol.class));
            else
                productions.get(i).getRhs().add(JSONObject.parseObject(symbols, symbol.class));
        }
        grammar.setStartsymbol(g.getJSONObject("startsymbol").getString("name"));
        return grammar;
    }
    public void showGrammar(Grammar g)
    {
        System.out.println(g.getNonterminalsymbols().size());
        for(nonterm nonterm:g.getNonterminalsymbols())
            System.out.print(nonterm.getName()+" ");
        System.out.println("\n"+g.getTerminalsymbols().size());
        for(term term:g.getTerminalsymbols())
            System.out.print(term.getSpell()+" ");
        System.out.println("\n"+g.getProductions().size());
        for(production production:g.getProductions()) {
            System.out.print(production.getLhs() + "-->");
            for(lab3.symbol symbol:production.getRhs())
            {
                System.out.print(symbol.getName()+"  ");
            }
            System.out.println();
        }
        System.out.println(g.getStartsymbol());
    }
    //消除直接左递归和间接左递归
    /*
    for （i＝1；i<=n；i++）
   for （j＝1；j<=i－1；j++）
   { 把形如Ai→Ajγ的产生式改写成Ai→δ1γ /δ2γ /…/δkγ
       其中Aj→δ1 /δ2 /…/δk是关于的Aj全部规则；
       消除Ai规则中的直接左递归；
   }
     */
    public Grammar removeLeft(Grammar g)
    {
        List<nonterm> nonterminalsymbols = g.getNonterminalsymbols();
        List<production> productions = g.getProductions();
        int index=1;
        for(int i=0;i<nonterminalsymbols.size();i++)
        {
            nonterm nontermI = nonterminalsymbols.get(i);
            ArrayList<production> productionsI=new ArrayList<>();
            //找出所有的以I开头的
            for(lab3.production production:productions)
            {
                if(production.getLhs().equals(nontermI.getName())) {
                    productionsI.add(production);
                }
            }
            //进行替换i-->j
            for(int j=0;j<i-1;j++)
            {
                nonterm nontermJ = nonterminalsymbols.get(j);
                //先找出j开头的所有表达式
                ArrayList<production> productionsJ=new ArrayList<>();
                for(lab3.production production:productions)
                {
                    if(production.getLhs().equals(nontermJ.getName()))
                        productionsJ.add(production);
                }
                //找出形如Ai→Ajγ的产生式改
                for(production pi:productionsI)
                {
                    List<symbol> rhs = pi.getRhs();
                    //在这一块判断由i开头的右边第一个非终结符是不是j
                    if(rhs.get(0).getName().equals(nontermJ.getName()))
                    {
                        //相等后就要进行替换
                        for(production pj:productionsJ)
                        {
                            rhs.remove(0);
                            List<symbol> temp=new ArrayList<>(rhs);
                            rhs.clear();
                            rhs.addAll(pj.getRhs().subList(1,pj.getRhs().size()));
                            rhs.addAll(temp);
                        }
                    }
                }

            }
            //e
            //消除表达式的直接左递归
            //先判断是否存在直接左递归---会重复进行判断
            boolean flag=true;
            while (flag)
            {
                for(int m=0;m<productionsI.size();m++)
                {
                    List<symbol> rhs = productionsI.get(m).getRhs();
                    if(rhs.get(0).getName().equals(nontermI.getName()))
                    {
                        productions.removeAll(productionsI);
                        //表示存在直接左递归，所以要对这productionsI里面的进行改写
                        productionsI.remove(m);
                        production p=new production();
                        String newLhs=nontermI.getName()+""+index;
                        index++;
                        p.setLhs(newLhs);
                        nonterminalsymbols.add(new nonterm(newLhs));
                        //从1开始
                        for(int n=1;n<rhs.size();n++)
                            p.getRhs().add(rhs.get(n));
                        p.getRhs().add(rhs.get(0));
                        p.getRhs().get(rhs.size()-1).setName(newLhs);
                        //形成了一个新的production
                        for(lab3.production production:productionsI)
                        {
                            //在之前的productionsI每一个rhs后面加上一个i1
                            production.getRhs().add(p.getRhs().get(rhs.size()-1));
                        }
                        productionsI.add(p);
                        //这个是指向空的
                        production p1=new production();
                        symbol emptySymbol=new symbol("epsilon","term");
                        p1.setLhs(newLhs);
                        p1.getRhs().add(emptySymbol);
                        //这次添加的是空
                        productionsI.add(p1);
                        //将更新后的加入--更新productions
                        productions.addAll(productionsI);
                        //删除之前的含有直接左递归的
                        break;
                    }
                }
                //将下标复原
                index=1;
                flag=false;
            }

        }
        g.getTerminalsymbols().add(new term("epsilon","epsilon"));
        return g;
    }
    @SneakyThrows
    public void GetFirst(Grammar g,String[] in)
    {

        Map<String,Integer> firstLen=new HashMap<>();
        Map<String,Integer> followLen=new HashMap<>();
        List<nonterm> nonterminalsymbols = g.getNonterminalsymbols();
        List<production> productions = g.getProductions();
        //对first进行初始化
        for(nonterm nonterm:nonterminalsymbols)
        {
            firstLen.put(nonterm.getName(),0);
            followLen.put(nonterm.getName(),0);
            first.put(nonterm.getName(),new HashSet<String>());
            follow.put(nonterm.getName(),new HashSet<String>());
        }
        //计算first
        for(production production:productions)
        {
            List<symbol> rhs = production.getRhs();
            for(symbol symbol:rhs)
            {
                if(symbol.getType().equals("term"))
                    first.get(production.getLhs()).add(symbol.getName());
                else
                    break;
            }
            firstLen.put(production.getLhs(),first.get(production.getLhs()).size());
        }
        //已经将非终结符和空加入了first中
        boolean flag=true;
        while (flag)
        {
            flag=false;
            for(production production:productions)
            {
                List<symbol> rhs = production.getRhs();
                for(int i=0;i<rhs.size();i++)
                {
                    if(rhs.get(i).getType().equals("nonterm"))
                    {
                        first.get(production.getLhs()).addAll(first.get(rhs.get(i).getName()));
                        if(i!=rhs.size()-1)
                        {
                            if(first.get(rhs.get(i).getName()).contains("epsilon"))
                            {
                                first.get(production.getLhs()).remove("epsilon");
                            }
                            else
                            {
                                break;
                            }
                        }
                    }
                    else
                        break;

                }
                if(firstLen.get(production.getLhs())!=first.get(production.getLhs()).size())
                {
                    flag=true;
                    firstLen.put(production.getLhs(),first.get(production.getLhs()).size());
                }
            }
        }
        //循环完了
        //到这一步已经获取到了first--接下来要加上终结符的first--终结符的first是他本身
        for(term term:g.getTerminalsymbols())
        {
            HashSet<String> hs=new HashSet<>();
            hs.add(term.getName());
            first.put(term.getName(),hs);
        }
        flag=true;
        //循环完了
        //到这一步已经获取到了first--接下来获取follow
        //给文法的开始赋值$
        follow.get(g.getStartsymbol()).add("$");
        //再次循环去计算follow
        while (flag)
        {
            flag=false;
            for(production production:productions)
            {
                List<symbol> rhs = production.getRhs();
                for(int i=0;i<rhs.size();i++)
                {
                    if(rhs.get(i).getType().equals("nonterm"))
                    {
                        HashSet<String> firstB=getFirstB(i,rhs);

                        follow.get(rhs.get(i).getName()).addAll(firstB);
                        if(firstB.contains("epsilon"))
                        {
                            follow.get(rhs.get(i).getName()).remove("epsilon");
                        }
                        if(i==rhs.size()-1||firstB.contains("epsilon"))
                        {
                            follow.get(rhs.get(i).getName()).addAll(follow.get(production.getLhs()));
                        }
                        if((followLen.get(rhs.get(i).getName())!=follow.get(rhs.get(i).getName()).size()))
                        {
                            flag=true;
                            followLen.put(rhs.get(i).getName(),follow.get(rhs.get(i).getName()).size());
                        }
                    }
                }

            }
        }
        //现在就是算完了了first和follow--接下来求selected

        for(production production:productions)
        {
            HashSet<String> hs = getFirstB(-1, production.getRhs());
            if(hs.contains("epsilon"))
            {
                hs.remove("epsilon");
                hs.addAll(follow.get(production.getLhs()));
            }
            selected.put(production,hs);
        }
        //selected求取完毕---求取画图过程---输出
        System.out.println("------first-------\n");
        for(String f:first.keySet())
        {
            if(f.equals("epsilon")||g.getTerminalsymbols().contains(new term(f,f)))
                continue;
            System.out.print(f+":  ");
            for(String v:first.get(f))
            {
                System.out.print(v+"  ");
            }
            System.out.println();
        }
        System.out.println("------follow-------\n");
        for(String f:follow.keySet())
        {
            System.out.print(f+":  ");
            for(String v:follow.get(f))
            {
                System.out.print(v+"  ");
            }
            System.out.println();
        }

        System.out.println("------selected-------\n");
        for(production p:selected.keySet())
        {
            System.out.print(p.getLhs()+"-->");
            for(symbol s:p.getRhs())
            {
                System.out.print(s.getName()+" ");
            }
            System.out.print(": ");
            for(String v:selected.get(p))
            {
                System.out.print(v+"  ");
            }
            System.out.println();
        }


        //_______________________上面的是输出first follow selected
        ArrayList<Node> nodes=new ArrayList<>();
        LinkedList<String> stack=new LinkedList<>();
        int indexIn=0;
        stack.push(g.getStartsymbol());
        List<term> terminalsymbols = g.getTerminalsymbols();
        ArrayList<String> process=new ArrayList<>();
        Node head=node("0").with(Shape.RECTANGLE,Label.of("blok"));
        nodes.add(head.link(node(1+"").with(Style.BOLD,Color.RED,Label.of("begin"))));

        int j=2;
        LinkedList<Integer> indexStack=new LinkedList<>();
        while(!stack.isEmpty()&&indexIn<in.length)
        {
            String s=in[indexIn];
            String pop = stack.pop();
            if(terminalsymbols.contains(new term(pop,pop)))
            {
                nodes.add(head.link(node(j+"").with(Style.BOLD,Color.RED,Label.of(pop))));
                if(pop.equals(s))
                    indexIn++;
                int h=indexStack.pop();
                head=node(h+"").with(Label.of(process.get(h-2)));
            }
            else
            {
                nodes.add(head.link(node(j+"").with(Shape.RECTANGLE,Label.of(pop))));
                head=node(j+"").with(Label.of(pop));
            }
            j++;
            process.add(pop);
            ArrayList<production> productions1 = getProductions(pop, productions, s);
            for(production production:productions1)
            {
                if(selected.get(production).contains(s))
                {
                    List<symbol> rhs = production.getRhs();
                    if(rhs.size()>1)
                        for(int k=0;k<rhs.size()-1;k++)
                            indexStack.push(j-1);
                    for(int i=rhs.size()-1;i>=0;i--)
                    {
                        stack.push(rhs.get(i).getName());
                    }
                }
            }
        }
        nodes.add(node("0").with(Shape.RECTANGLE,Label.of("blok")).link(node(j+"").with(Style.BOLD,Color.RED,Label.of("end"))));
        Graph g1 = graph(g.getName()).with(nodes);
        Graphviz.fromGraph(g1).width(900).render(Format.SVG).toFile(new File("ex3.svg"));

    }
    private HashSet<String> getFirstB(int i,List<symbol> rhs)
    {
        HashSet<String> firstB=new HashSet<>();
        //求取后面的first
        for(int j=i+1;j<rhs.size();j++)
        {
            firstB.addAll(first.get(rhs.get(j).getName()));
            if(!first.get(rhs.get(j).getName()).contains("epsilon"))
            {
                break;
            }else
            {
                if(j!=rhs.size()-1)
                    firstB.remove("epsilon");
            }
        }
        return firstB;
    }
    private HashSet<String> getHs(symbol symbol,String l)
    {

        HashSet<String> hs=new HashSet<>();
        if(!symbol.getName().equals("epsilon"))
        {
            hs.add(symbol.getName());
            return hs;
        }
        else
        {
            hs.addAll(follow.get(l));
            return hs;
        }
    }
    private ArrayList<production> getProductions(String l,List<production> productions,String in)
    {
        ArrayList<production> productions1=new ArrayList<>();
        for(production production:productions)
        {
            if(production.getLhs().equals(l))
            {
                productions1.add(production);
            }
        }
        return productions1;
    }
}
