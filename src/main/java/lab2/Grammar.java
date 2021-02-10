package lab2;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

//文法
@Getter
@Setter
@ToString
public class Grammar {
    //文法的名称
    private String name;
    private List<term> terminalsymbols=new ArrayList<>();
    private List<nonterm> nonterminalsymbols=new ArrayList<>();
    private List<production> productions=new ArrayList<>();
    private String startsymbol;
}
