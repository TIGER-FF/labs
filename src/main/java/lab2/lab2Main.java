package lab2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class lab2Main {
    public static void main(String[] args) {
        Grammar g=null;
        RemoveLeft removeLeft=null;
        try {
            removeLeft=new RemoveLeft();
            g=removeLeft.readJson("src\\main\\resources\\input.json");
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }
        removeLeft.showGrammar(g);
        //消除左递归
        g=removeLeft.removeLeft(g);
        System.out.println("\nremove left----------------\n");
        removeLeft.showGrammar(g);
        //转化到CNF
        g=removeLeft.toCNF(g);
        System.out.println("\nto  CNF  1----------------\n");
        removeLeft.showGrammar(g);
        System.out.println("\nto  CNF  2----------------\n");
        g=removeLeft.removeSingleProduction(g);
        removeLeft.showGrammar(g);
        try {
            removeLeft.saveGrammar(g);
        }
        catch (IOException ioe)
        {
            System.out.println(ioe.toString());
        }

    }
}
