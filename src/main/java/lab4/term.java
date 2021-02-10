package lab4;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

//终止符
@Getter
@Setter
@ToString
public class term {
    private  String name;
    private  String spell;

    public term(String name, String spell) {
        this.name = name;
        this.spell = spell;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        term term = (term) o;
        return Objects.equals(name, term.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
