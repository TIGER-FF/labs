package lab4;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class nonterm {
    private String name;

    public nonterm(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        nonterm nonterm = (nonterm) o;
        return Objects.equals(name, nonterm.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
