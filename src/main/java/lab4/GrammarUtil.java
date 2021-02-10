package lab4;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;

//消除左递归
public class GrammarUtil {
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
        for(lab4.nonterm nonterm:g.getNonterminalsymbols())
            System.out.print(nonterm.getName()+" ");
        System.out.println("\n"+g.getTerminalsymbols().size());
        for(lab4.term term:g.getTerminalsymbols())
            System.out.print(term.getSpell()+" ");
        System.out.println("\n"+g.getProductions().size());
        for(lab4.production production:g.getProductions()) {
            System.out.print(production.getLhs() + "-->");
            for(lab4.symbol symbol:production.getRhs())
            {
                System.out.print(symbol.getName()+"  ");
            }
            System.out.println();
        }
        System.out.println(g.getStartsymbol());
    }
}
