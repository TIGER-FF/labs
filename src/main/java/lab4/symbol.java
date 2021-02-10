package lab4;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class symbol {
   private String name;
   private String type;

   public symbol(String name, String type) {
      this.name = name;
      this.type = type;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      symbol symbol = (symbol) o;
      return Objects.equals(name, symbol.name) &&
              Objects.equals(type, symbol.type);
   }

   @Override
   public int hashCode() {
      return Objects.hash(name, type);
   }
}
