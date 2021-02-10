package lab1;


import java.util.*;

//re direct to DFA
public class reToDFA {
    //这个用来存放加上index的字母不存储运算符
    private ArrayList<String> numbers=new ArrayList<>();
    private HashSet<Character> input=new HashSet<>();
    private DFATree dtree;
    private HashSet<DFA> DFASerial=new HashSet<>();
    private Map<Integer,HashSet<String>> dstates=new HashMap<>();
    private HashSet<Integer> endStatus=new HashSet<>();
    private HashSet<Integer> processStatus=new HashSet<>();
    private  Map<String,HashSet<String>> followpos=new HashMap<>();
    public  Integer getState(Character ch)
    {
        switch (ch)
        {
            case ')':return 5;
            case '*':return 4;
            case '.':return 3;
            case '|':return 2;
            case '(':return 1;
        }
        return -1;
    }
    //首先建立一个树状图
    //转化为后缀表达式---返回String对象，里面存储的后缀表达式
    public DFATree reToDFA(String re)
    {

        LinkedList<DFATree> numberList=new LinkedList<>();
        //用于对每一个的值区分
        int index=1;
        //存储运算符
        LinkedList<Character> operationList=new LinkedList<Character>();

        StringBuilder sb=new StringBuilder();
        sb.append("(");
        //进行转化--添加#
        for(int i=0;i<re.length()-1;i++)
        {
            //先进行替换--#
            //先取出第一个和第二个字符
            Character first=re.charAt(i);
            Character second=re.charAt(i+1);
            if(Character.isLetter(first)&&Character.isLetter(second))
            {
                sb.append(first);
                sb.append('.');
            }
            else if((first==')'||first=='*')&&(Character.isLetter(second)||second=='('))
            {
                sb.append(first);
                sb.append('.');
            }

            else if(Character.isLetter(first)&&second=='(')
            {
                sb.append(first);
                sb.append('.');
            }
            else
                sb.append(first);
        }
        sb.append(re.charAt(re.length()-1)+").#");
        //sb.append(re.charAt(re.length()-1));
        System.out.println(sb.toString());
        //正则表达式添加.,添加完毕了--下面进行转化为后缀表达式
        //这里面主要是有一个合并--
        DFATree right=null;
        DFATree left=null;
        DFATree dt=null;
        for(int i=0;i<sb.length();i++)
        {
            Character ch=sb.charAt(i);
            switch (ch)
            {
                case ')':
                    //运算符一直输出，直到碰到（
                    while (true)
                    {
                        Character operation=operationList.pop();
                        if(operation=='(')
                            break;
                        right=numberList.pop();
                        left=numberList.pop();
                        dt=getDt(operation,left,right);
                        numberList.push(dt);
                    }
                    break;
                case '*':

                    //优先级排第二，但是和第一）不会同时出现，所以他也要
                    dt=new DFATree();
                    dt.setCh(ch+"");
                    left=numberList.pop();
                    left.setFatherCh(dt);
                    dt.setLeftChildCh(left);
                    dt.setNullable(true);
                    //这一块不能计算要放在后面遍历时候计算
//                    dt.setFirstpos(left.getFirstpos());
//                    dt.setLastpos(left.getLastpos());
                    numberList.push(dt);
                    break;
                case '.':
                case '|':
                    //去判断--运算符号栈空也是直接入栈的
                    if(operationList.isEmpty()) {
                        operationList.push(ch);
                    }
                    else
                    {
                        //getFirst()和pop()都是获取栈顶的--区别是pop()是获取并出栈，getFirst是获取不出栈
                        while (true) {
                            Character firstOperation = operationList.getFirst();
                            if (getState(firstOperation) >= getState(ch)) {
                                right=numberList.pop();
                                left=numberList.pop();
                                //这一块是不能计算的
                                dt=getDt(operationList.pop(),left,right);

                                numberList.push(dt);
                                if(operationList.isEmpty())
                                    break;
                            } else {

                                break;
                            }
                        }
                        operationList.push(ch);
                    }
                    break;
                case '(':
                    operationList.push(ch);
                    break;
                //这一块就相当是字母了
                default :
                    if(ch!='#')
                        input.add(ch);
                    numbers.add(ch+""+index);
                    dt=new DFATree();
                    dt.setCh(ch+""+index);
                    //firstpos
                    dt.getFirstpos().add(dt.getCh());
                    //lastpos
                    dt.getLastpos().add(dt.getCh());
                    index++;
                    numberList.push(dt);

            }
        }
        //输出在运算符和运算数中剩余的
        while (!operationList.isEmpty())
        {
            right=numberList.pop();
            left=numberList.pop();
            //这一块不能计算
            dt=getDt(operationList.pop(),left,right);
            numberList.push(dt);
        }
       dtree=numberList.pop();
        return dtree;
    }

    //进行遍历改写followpos nullable firstpos lastpos
    public Map<String,HashSet<String>> getFollowpos(DFATree dt)
    {
        //创建一个followpos的map集合
        //Map<String,HashSet<String>> followpos=new HashMap<>();
        //试一下真正的栈，不用LinkedList去模拟了
        LinkedList<DFATree> stack=new LinkedList<>();
        //保留根节点
        for(String n:numbers)
        {
            followpos.put(n,new HashSet<String>());
        }
        //这就初始化好了---followpos---进行遍历对followpos进行赋值
        if(dt==null)
            return null;
        stack.push(dt);
        //对这个二叉树进行深度优先遍历---起始点可能不是根节点
        while (!stack.isEmpty())
        {
            DFATree d=stack.getFirst();
            //到叶子节点后回到父亲节点进行计算
            switch (d.getCh())
            {
                //表示为叶子节点

                //表示为*节点
                case "*":
                    DFATree left=d.getLeftChildCh();
                    if(left.getFirstpos().size()!=0)
                    {
                        d.setFirstpos(left.getFirstpos());
                        d.setLastpos(left.getLastpos());
                        //更新followpos
                        for (String p:left.getLastpos())
                        {
                            followpos.get(p).addAll(left.getFirstpos());
                        }
                        stack.pop();
                    }
                    break;
                //表示为|或者.节点
                case ".":
                case "|":
                    if(updateDt(d))
                        stack.pop();
                    break;
                default :
                    //直接出栈
                    stack.pop();
                    break;

            }
            if (d.getRightChildCh()!=null) {
                stack.push(d.getRightChildCh());
            }
            if(d.getLeftChildCh()!=null) {
                stack.push(d.getLeftChildCh());
            }
//            if("*".equals(d.getCh()))
//            {
//                for(String last:d.getLastpos())
//                {
//                    followpos.get(last).addAll(d.getFirstpos());
//                }
//            }
//            if(".".equals(d.getCh()))
//            {
//                for(String s:d.getLeftChildCh().getLastpos())
//                {
//                    followpos.get(s).addAll(d.getRightChildCh().getFirstpos());
//                }
//            }
        }
        return followpos;
    }
    public HashSet<DFA> toNFA(Map<String,HashSet<String>> followpos)
    {
        //这一块只需要用到根节点的firstpos和所有的followpos
        LinkedList<HashSet<String>> dstates1=new LinkedList<>();
        //初始化dstates

        dstates.put(0,dtree.getFirstpos());
        dstates1.add(dtree.getFirstpos());
        while (dstates1.size()!=0)
        {
            HashSet<String> hs=dstates1.pop();
            int start=isContain(dstates,hs);
            for(Character ch:input)
            {
                HashSet<String> temp=new HashSet<>();
                for(String s:hs)
                {
                    if(s.contains(ch+""))
                    {
                        temp.addAll(followpos.get(s));
                    }
                }
                if(temp.size()!=0)
                {
                    //判断是否已经转化过了
                    int serial=isContain(dstates,temp);
                    if(serial==-1)
                    {
                        dstates1.push(temp);
                        serial=dstates.size();
                        dstates.put(serial,temp);
                    }
                    DFASerial.add(new DFA(start,serial,ch));
                }
            }
        }
        for(Integer i:dstates.keySet())
        {
            HashSet<String> h=dstates.get(i);
            System.out.println(i+"  "+Arrays.toString(h.toArray()));
            if(h.contains("#"+numbers.size()))
            {
                endStatus.add(i);
            }else
            {
                processStatus.add(i);
            }
        }
        System.out.println("start Status");
        System.out.println(0+"  "+Arrays.toString(dtree.getFirstpos().toArray()));
        System.out.println("end Status");
        for (Integer i:endStatus)
        {
            System.out.println(i+"  "+Arrays.toString(dstates.get(i).toArray()));
        }
        System.out.println("------------------------------------------");
        return DFASerial;
    }
    public HashSet<DFA> toMinDFA()
    {
        //对于中止符合在里面添加一个-1,用于和其他的区分开
//        endStatus.add(-1);
         //构建一个LinkedList去存储划分的状态
        LinkedList<HashSet<Integer>> n=new LinkedList<>();
        if(processStatus.size()!=0)
            n.add(processStatus);
        n.add(new HashSet<Integer>(endStatus));
        //进行划分
        HashSet<Integer> inEndSatus=new HashSet<>();
        boolean flag=true;
        while (flag)
        {
           pp:for(HashSet<Integer> hs:n)
            {
                for(Character ch:input) {
                    Map<HashSet<Integer>,HashSet<Integer>> group=new HashMap<>();
                    //初始化分组
                    for(int l=0;l<n.size();l++)
                        group.put(n.get(l),new HashSet<Integer>());
                    for(Integer i:hs)
                    {
                        for(DFA dfa:DFASerial)
                        {
                            if(dfa.getCh()==ch&&dfa.getN()==i)
                            {
                                for(HashSet<Integer> l:group.keySet())
                                    if(l.contains(dfa.getN1()))
                                        group.get(l).add(dfa.getN());
                            }
                        }
                    }
                    boolean isUpdate=false;
                    for(HashSet<Integer> h:group.values())
                        if (h.size()>0&&h.size()!=hs.size())
                        {
                            isUpdate=true;
                            hs.removeAll(h);
                            n.add(h);
                        }
                    if(isUpdate)
                        break pp;
                    else
                        flag=false;
                }
            }
        }
        System.out.println("min DFA---------------");
        ArrayList<Integer> end1=new ArrayList<>();
        ArrayList<Integer> start1=new ArrayList<>();
        Map<Integer,HashSet<Integer>> minMap=new HashMap<>();
        for(HashSet<Integer> hs:n)
        {
            if(endStatus.containsAll(hs))
            {
                for(Integer i:hs) {
                    end1.add(i);
                    minMap.put(i, hs);
                    break;
                }
            }
            else
            {
                for(Integer i:hs) {
                    minMap.put(i, hs);
                    break;
                }
            }
        }
//        for(HashSet<Integer> hs:n)
//        {
//            if(endStatus.containsAll(hs))
//            {
//                for(Integer i:hs) {
//                    minMap.put(i, hs);
//                    break;
//                }
//
//                minMap.put(i,hs);
//                i++;
//            }
//        }
        HashSet<DFA> minDFA=new HashSet<>();
        boolean st=true;
        for(Integer index:minMap.keySet())
        {
            for(Integer j:minMap.get(index))
            {
                for(Character ch:input)
                {
                    for(DFA dfa:DFASerial)
                    {
                        if (dfa.getCh()==ch&&dfa.getN()==j)
                        {
                            if (st) {
                                st=false;
                                System.out.println("start Status");
                                System.out.println(index+"  "+Arrays.toString(minMap.get(index).toArray()));
                            }
                            minDFA.add(new DFA(index,getIndex(minMap,dfa.getN1()),ch));
                        }
                    }
                }
            }
        }
        System.out.println("end Status");
        for(Integer t:end1)
        {
            System.out.println(t+"  "+Arrays.toString(minMap.get(t).toArray()));
        }
        System.out.println("------------------------------ ");
        return minDFA;
    }
    private Integer getIndex(Map<Integer,HashSet<Integer>> map,Integer n )
    {

        for(Integer i:map.keySet())
        {
            if(map.get(i).contains(n))
                return i;
        }
        return null;
    }
    private DFATree getDt(Character ch,DFATree left,DFATree right)
    {
        DFATree dt=new DFATree();
        dt.setCh(ch+"");
        right.setFatherCh(dt);
        left.setFatherCh(dt);
        dt.setLeftChildCh(left);
        dt.setRightChildCh(right);
        return dt;
    }
    private Boolean updateDt(DFATree dt)
    {
        DFATree left=dt.getLeftChildCh();
        DFATree  right=dt.getRightChildCh();
        //计算
        if(left.getFirstpos().size()==0||right.getFirstpos().size()==0)
            return false;
        //这一块一定要这样写，因为传递过来的是地址，下面的变化，会改变传递过来的值
        HashSet<String> leftFirstpos=new HashSet<String>(left.getFirstpos());
        HashSet<String> leftLastpos=new HashSet<String>(left.getLastpos());
        leftFirstpos.addAll(right.getFirstpos());
        leftLastpos.addAll(right.getLastpos());
        if(".".equals(dt.getCh()))
        {
            dt.setNullable(left.getNullable()&&right.getNullable());
            dt.setFirstpos(left.getNullable()?leftFirstpos:left.getFirstpos());
            dt.setLastpos(right.getNullable()?leftLastpos:right.getLastpos());
            //再去计算followpos
            for(String p:left.getLastpos())
            {
                followpos.get(p).addAll(right.getFirstpos());
            }
        }
        else
        {
            dt.setNullable(left.getNullable()||right.getNullable());
            dt.setFirstpos(leftFirstpos);
            dt.setLastpos(leftLastpos);
        }
        return true;
    }
    private Integer isContain(Map<Integer,HashSet<String>> dstates,HashSet<String> temp)
    {
        for(Integer i:dstates.keySet())
        {
            if (dstates.get(i).containsAll(temp)&&dstates.get(i).size()==temp.size())
                return i;
        }
        return -1;
    }
}
