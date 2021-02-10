package lab2;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import jsonUtil.*;

//消除左递归
public class RemoveLeft {
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
                productions.get(i).setRhs(JSON.parseArray(symbols,symbol.class));
            else
                productions.get(i).getRhs().add(JSONObject.parseObject(symbols,symbol.class));
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
            for(symbol symbol:production.getRhs())
            {
                switch (symbol.getName())
                {
                    case "IDENT":
                        System.out.print("a   ");
                        break;
                    case "LPAREN":
                        System.out.print("(   ");
                        break;
                    case "RPAREN":
                        System.out.print(")   ");
                        break;
                    case "MUL":
                        System.out.print("*   ");
                        break;
                    case "ADD":
                        System.out.print("+   ");
                        break;
                    default:
                        System.out.print(symbol.getName()+"  ");
                }

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
            for(production production:productions)
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
                for(production production:productions)
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
                        for(production production:productionsI)
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
        return g;
    }
    //将上下文无关的文法（英语：CFG  俄语：КС）转化到乔姆斯基文法(CNF)
    public Grammar toCNF(Grammar g)
    {
        //去除无用的生产式
        g= removeUselessProduction(g);
        List<nonterm> nonterminalsymbols = g.getNonterminalsymbols();
        List<production> productions = g.getProductions();
//1.添加一个新的起始点，这样就会保证在文法中起始头不会出现在文法的右边S-->g.startSymbol
        //我在这块就简单的写一下，用S来替代新的启示头不去判断是否出现在已知的非终结符中
        String newStart="Z";
        //创建新的生成式
        production addNew=new production();
        addNew.setLhs(newStart);
        addNew.getRhs().add(new symbol(g.getStartsymbol(),"nonterm"));
        //更改文法新的起始头
        g.setStartsymbol(newStart);
        //将新的非终结符添加进去
        nonterminalsymbols.add(new nonterm(newStart));
        //添加新的操作
        productions.add(addNew);
//2.开始消除含有epsilon的生成式
        //创建一个production的集合用来存储含有epsilon的生产式的坐边的非终结符--因为右边都一样
        //还得创建一个production的集合用来存储含有epsilon的生产式，不然在循环里面改变会出现越界
        List<String> epsilonLhs=new ArrayList<>();
        List<production> epsilonProductions=new ArrayList<>();
        //首先取出来含有epsilon的生成式
        for(production production:productions)
        {
            //在这种文法存储格式中，如果他是含有epsilon的生产式的话，那么epsilon就肯定在右边的第一个
            if(production.getRhs().get(0).getName().equals("epsilon"))
            {
                epsilonProductions.add(production);
                epsilonLhs.add(production.getLhs());
            }
        }
        //让原来的去除含有epsilon生产式
        productions.removeAll(epsilonProductions);
        //然后去遍历去除后的productions，如果说他的右边中有在epsilonRhs集合中那就要改写
        LinkedList<production> tempProductions=new LinkedList<>(productions);//复制一份
        for(production production:tempProductions)
        {
            productions.remove(production);
            List<symbol> rhs = production.getRhs();
            ArrayList<symbol> tempRhs=new ArrayList<>(rhs);
            //判断是否需要改写
            boolean flag=true;
            while (flag)
            {
                int i=0;
                for(;i<tempRhs.size();i++)
                {
                    if(epsilonLhs.contains(tempRhs.get(i).getName()))
                    {
                        break;
                    }

                }
                if(i!=tempRhs.size()) {
                    //相当于要加进入一个
                    production p=new production();
                    p.setLhs(production.getLhs());
                    tempRhs.remove(i);
                    p.setRhs(tempRhs);
                    productions.add(p);
                }
                else {
                    flag = false;
                }
            }
            productions.add(production);
        }
        //到这一步已经去除了带有epsilon的生产式--接下来去除单一生产式（就是左右每边只有一个非终结符）
       return g;
    }
    public Grammar removeSingleProduction(Grammar g)
    {
        List<production> productions = g.getProductions();
        List<production> singleProductions=new ArrayList<>();
        //遍历找出单生产式
        for(production production:productions)
        {
            if(!production.getLhs().equals(g.getStartsymbol())) {
                List<symbol> rhs = production.getRhs();
                if (rhs.size() == 1) {
                    symbol symbol = rhs.get(0);
                    if(!symbol.getName().equals("IDENT")&&symbol.getType().equals("nonterm"))
                            singleProductions.add(production);
                }
            }
        }
        productions.removeAll(singleProductions);
        //再去遍历进行修改
        List<production> tempProductions=new ArrayList<>(productions);
        for(production singleProduction:singleProductions)
        {

            for(production tempProduction:tempProductions)
            {
                if(tempProduction.getLhs().equals(singleProduction.getRhs().get(0).getName()))
                {
                    production p=new production();
                    p.setLhs(singleProduction.getLhs());
                    p.setRhs(tempProduction.getRhs());
                    productions.add(p);
                }
            }
        }
        return  g;
    }
    private Grammar removeUselessProduction(Grammar g)
    {
        List<nonterm> nonterminalsymbols = g.getNonterminalsymbols();
        List<production> productions = g.getProductions();
        //!好像要首先删除无用的生产式--就是没有在右边出现过的非终结符
        //创建以一个列表来存储一会要删除的
        ArrayList<Integer> useless=new ArrayList<>();
        Map<String,Boolean> isAppear=new HashMap<>();
        for(nonterm nonterm:nonterminalsymbols) {
            if(!g.getStartsymbol().equals(nonterm.getName()))
                    isAppear.put(nonterm.getName(), false);
        }
        for(int j=0;j<productions.size();j++)
        {
            List<symbol> rhs = productions.get(j).getRhs();
            for(symbol symbol:rhs)
            {
                if(symbol.getType().equals("nonterm")) {
                    isAppear.put(symbol.getName(),true);
                }
            }
        }
        ArrayList<production> uselessProduction=new ArrayList<>();
        //判断是否有这样的--有的话存起来返回
        for (String key:isAppear.keySet())
        {
            if(!isAppear.get(key)) {
                nonterminalsymbols.remove(new nonterm(key));
                for (production production : productions) {
                    if (production.getLhs().equals(key))
                        uselessProduction.add(production);
                }
            }
        }
        productions.removeAll(uselessProduction);
        return  g;
    }
    public void saveGrammar(Grammar g) throws IOException
    {
        String s=JSON.toJSONString(g, SerializerFeature.DisableCircularReferenceDetect);
        s=FormatUtil.formatJson(s);
        File file=new File("src\\main\\resources\\output.json");
        FileUtils.writeStringToFile(file, s, "UTF-8");
    }
}
