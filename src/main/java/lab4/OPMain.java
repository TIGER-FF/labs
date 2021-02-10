package lab4;
//算术符优先

import java.util.*;

public class OPMain {
    Map<String, HashSet<String>> firstVt=new HashMap<>();
    Map<String, HashSet<String>> lastVt=new HashMap<>();
    Map<String,String[]> opMatrix=new HashMap<>();
    LinkedList<String> stack=new LinkedList<>();
    Grammar g=new Grammar();
    //得到终结符的优先级矩阵
    public void getMatrix(Grammar g)
    {
        this.g=g;
        //变量firstVt和lastVt的长度
        Map<String, Integer> firstVtLen=new HashMap<>();
        Map<String, Integer> lastVtLen=new HashMap<>();
        //首先求出firstVt和lastVt
        List<production> productions = g.getProductions();
        List<nonterm> nonterminalsymbols = g.getNonterminalsymbols();
        //首先初始化firstVt和lastVt
        //首先初始化firstVtLen和lastVtLen
        for(nonterm nonterm:nonterminalsymbols)
        {
            firstVtLen.put(nonterm.getName(),0);
            firstVt.put(nonterm.getName(),new HashSet<String>());
            lastVt.put(nonterm.getName(),new HashSet<String>());
            lastVtLen.put(nonterm.getName(),0);
        }
        //求取firstVt和lastVt
        boolean flag=true;
        while (flag)
        {
            for(production production:productions)
            {
                List<symbol> rhs = production.getRhs();
                int tempLen=firstVt.get(production.getLhs()).size();
                if(rhs.size()==1)
                {
                    symbol symbol=rhs.get(0);
                    if(symbol.getType().equals("term"))
                    {
                        firstVt.get(production.getLhs()).add(symbol.getName());
                        lastVt.get(production.getLhs()).add(symbol.getName());
                    }
                    else
                    {
                        firstVt.get(production.getLhs()).addAll(firstVt.get(symbol.getName()));
                        lastVt.get(production.getLhs()).addAll(lastVt.get(symbol.getName()));
                    }
                    continue;
                }
                symbol firstSymbol=rhs.get(0);
                symbol secondSymbol=rhs.get(1);
                symbol lastSymbol1=rhs.get(rhs.size()-1);
                symbol lastSymbol2=rhs.get(rhs.size()-2);

                if(firstSymbol.getType().equals("term"))
                {
                    firstVt.get(production.getLhs()).add(firstSymbol.getName());
                }
                else
                {
                    firstVt.get(production.getLhs()).addAll(firstVt.get(firstSymbol.getName()));
                    if(secondSymbol.getType().equals("term")) {
                        firstVt.get(production.getLhs()).add(secondSymbol.getName());
                    }
                }
                if(lastSymbol1.getType().equals("term"))
                {
                    lastVt.get(production.getLhs()).add(lastSymbol1.getName());
                }
                else
                {
                    lastVt.get(production.getLhs()).addAll(lastVt.get(lastSymbol1.getName()));
                    if(lastSymbol2.getType().equals("term"))
                    {
                        lastVt.get(production.getLhs()).add(lastSymbol2.getName());
                    }
                }
            }
            flag=false;
            for(String r:firstVtLen.keySet())
            {
                if(firstVtLen.get(r)!=firstVt.get(r).size())
                {
                    firstVtLen.put(r,firstVt.get(r).size());
                    flag=true;
                }
                if(lastVtLen.get(r)!=lastVt.get(r).size())
                {
                    lastVtLen.put(r,lastVt.get(r).size());
                    flag=true;
                }
            }
        }
        //到这一步已经求出来来了firstVt和lastVt---求取终结符的优先顺序矩阵
        //输出：：：：：：
        System.out.println("\nfirstVt:-------------------------------------------\n");
        for(String s:firstVt.keySet())
        {
            System.out.print(s+"  :");
            for(String v:firstVt.get(s))
            {
                System.out.print(v+" ");
            }
            System.out.println();
        }
        System.out.println("\nlastVt:---------------------------------------------\n");
        for(String s:lastVt.keySet())
        {
            System.out.print(s+"  :");
            for(String v:lastVt.get(s))
            {
                System.out.print(v+" ");
            }
            System.out.println();
        }
        //
        //首先给文法中添加E-->#E#
        List<symbol> symbolList=new ArrayList<>();
        symbolList.add(new symbol("#","term"));
        symbolList.add(new symbol(g.getStartsymbol(),"nonterm"));
        symbolList.add(new symbol("#","term"));
        production p=new production();
        p.setLhs(g.getStartsymbol());
        p.setRhs(symbolList);
        productions.add(p);
        g.getTerminalsymbols().add(new term("#","#"));
        //添加完毕
        //创建二个以终结符为key的map用来存储矩阵关系

        List<term> terminalsymbols = g.getTerminalsymbols();
        //初始化这个map
        for(term term:terminalsymbols)
        {
            opMatrix.put(term.getName(),new String[terminalsymbols.size()]);
        }
        //初始化完毕进行赋值---遍历生产式
        for(production production:productions)
        {
            List<symbol> rhs = production.getRhs();
            if(rhs.size()<=1)
                continue;
            symbol symbol = rhs.get(0);
            String first=symbol.getName();
            boolean isEqual=false;
            boolean isTerm=true;
            if(symbol.getType().equals("nonterm"))
                isTerm=false;
            //遍历到倒数第二个就行了
            for(int i=1;i<rhs.size();i++)
            {
                if(isEqual)
                {
                    if(rhs.get(i).getType().equals("term"))
                    {
                        int i2 = terminalsymbols.indexOf(new term(rhs.get(i).getName(), rhs.get(i).getName()));
                        opMatrix.get(rhs.get(i-2).getName())[i2]="=";
                    }
                    isEqual=false;
                }
                if(isTerm)
                {
                    if(rhs.get(i).getType().equals("nonterm"))
                    {
                        addOp("<",first,firstVt.get(rhs.get(i).getName()));
                        isTerm=false;
                        isEqual=true;
                    }
                    else
                    {
                        HashSet<String> hs=new HashSet<>();
                        hs.add(rhs.get(i).getName());
                        addOp("=",first,hs);
                        isTerm=true;
                    }
                    first=rhs.get(i).getName();
                }
                else
                {
                    if(rhs.get(i).getType().equals("term"))
                    {
                        int i1 = terminalsymbols.indexOf(new term(rhs.get(i).getName(), rhs.get(i).getName()));
                        for(String s:lastVt.get(first))
                        {
                            opMatrix.get(s)[i1]=">";
                        }
                        isTerm=true;

                    }
                    first=rhs.get(i).getName();
                }
            }
        }
        //已经得到了优先符的矩阵--进行规约--要求取最左素短语
        //输出优先矩阵：
        System.out.println("\nopMatrix:------------------------------\n");
        System.out.printf("%10s","");
        for(term term:g.getTerminalsymbols())
        {
            System.out.printf("%10s",term.getName()+"  ");
        }
        System.out.println("");
        for(String s:opMatrix.keySet())
        {
            System.out.printf("%10s",s+":");
            for(String v:opMatrix.get(s))
            {
                System.out.printf("%10s",v);
            }
            System.out.println("");
        }
        System.out.println("-------------------------------------------------");
        String[] input={"a",",","(","(","a",",","a",")",",","(","a",",","a",")",")",")","#"};
        //先创建一个栈用来存储移入的---初始化的话就是在栈里面压入一个#

        stack.push("#");
        int index=0;
        do {
            String nontermTemp=null;
            String firstTemp = stack.getFirst();
            if(nonterminalsymbols.contains(new nonterm(firstTemp)))
            {
                nontermTemp=stack.pop();
            }
            String priority=getPriority(stack.getFirst(),input[index]);
            if(nontermTemp!=null)
                stack.push(nontermTemp);
            if(priority==null)
            {
                System.out.println("error-------");
                return;
            }
            else
            {
                //移入
                if (priority.equals("<")||priority.equals("="))
                {
                    stack.push(input[index]);
                    index++;
                    //进行输出
                    for(int n=stack.size()-1;n>=0;n--)
                    {
                        System.out.print(stack.get(n));
                    }
                    System.out.print("         ");
                    for(int i=index;i<input.length;i++)
                    {
                        System.out.print(input[i]);
                    }
                    System.out.println("");
                }
                //规约
                else
                {
                    //规约时候要找最左素短语
                    production pTemp=statute();
                    for(int n=stack.size()-1;n>=0;n--)
                    {
                        System.out.print(stack.get(n));
                    }
                    System.out.print("         ");
                    for(int i=index;i<input.length;i++)
                    {
                        System.out.print(input[i]);
                    }
                    System.out.print("         ");
                    System.out.print(pTemp.getLhs()+"-->");
                    for(symbol symbol:pTemp.getRhs())
                    {
                        System.out.print(symbol.getName());
                    }
                    System.out.println();

                }
            }
        }while (index<=input.length-1);
        System.out.println("ss");
    }
    private void addOp(String op,String r,HashSet<String> l)
    {
        String[] strings = opMatrix.get(r);
        for(String s:l)
        {
            strings[g.getTerminalsymbols().indexOf(new term(s,s))]=op;
        }
    }
    private String getPriority(String l,String r)
    {
        int i = g.getTerminalsymbols().indexOf(new term(r, r));
        return  opMatrix.get(l)[i];
    }
    private production statute()//进行规约返回规约后的，和用来规约的产生式
    {

        ArrayList<String> al=new ArrayList<>();
        if(stack.size()<=2)
        {
            al.add(stack.pop());
        }
        else
        {
            if(g.getNonterminalsymbols().contains(new nonterm(stack.getFirst())))
            {
                al.add(stack.pop());
            }
            String pop = stack.pop();
            al.add(pop);
            while (true)
            {
                String first = stack.getFirst();
                //终结符
                if(g.getNonterminalsymbols().contains(new nonterm(first)))
                {
                    al.add(stack.pop());
                }
                //非终结符
                else
                {
                    if(!getPriority(first,pop).equals("="))
                        break;
                    else
                    {
                        pop=stack.pop();

                        al.add(pop);

                    }
                }
            }
            Collections.reverse(al);
        }
        //得到了一个需要规约的最左素短语--进行规约
        List<production> productions = g.getProductions();
        for(production production:productions)
        {
            List<symbol> rhs = production.getRhs();
            if(rhs.size()==al.size())
            {
                int i=0;
                for(;i<rhs.size();i++)
                {
                    if(rhs.get(i).getType().equals("nonterm"))
                    {
                        if(!g.getNonterminalsymbols().contains(new nonterm(al.get(i))))
                        {
                            break;
                        }
                    }else
                    {
                        if(!g.getTerminalsymbols().contains(new term(al.get(i),al.get(i))))
                        {
                            break;
                        }
                    }
                }
                if(i==al.size()) {
                    stack.push(production.getLhs());
                    return production;
                }
            }
        }
        return null;
    }
}
