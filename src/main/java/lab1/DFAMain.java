package lab1;

import java.util.HashSet;
import java.util.Map;

public class DFAMain {
    public static void main(String[] args) {
        String re="(a|b)*abb";
        reToDFA reToDFA=new reToDFA();
        DFATree dt=reToDFA.reToDFA(re);
        Map<String, HashSet<String>> followpos=reToDFA.getFollowpos(dt);
        HashSet<DFA> DFASerial=reToDFA.toNFA(followpos);
        for(DFA dfa:DFASerial)
            System.out.println(dfa.toString());
        for(DFA dfa:reToDFA.toMinDFA())
        {
            System.out.println(dfa.toString());
        }
    }
}
