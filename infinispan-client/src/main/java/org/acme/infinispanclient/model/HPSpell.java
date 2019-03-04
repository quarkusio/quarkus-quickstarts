package org.acme.infinispanclient.model;

import java.util.Objects;

/**
 * Harry Potter saga spell
 */
public class HPSpell {
   private final int id;
   private final String name;
   private final String type;
   private final String description;

   public HPSpell(int id, String name, String type, String description) {
      this.id = id;
      this.name = name;
      this.type = type;
      this.description = description;
   }

   public int getId() {
      return id;
   }

   public String getName() {
      return name;
   }

   public String getType() {
      return type;
   }

   public String getDescription() {
      return description;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      HPSpell that = (HPSpell) o;
      return id == that.id &&
            Objects.equals(name, that.name) &&
            Objects.equals(type, that.type) &&
            Objects.equals(description, that.description);
   }

   @Override
   public int hashCode() {
      return Objects.hash(id, name, type, description);
   }

   @Override
   public String toString() {
      return "HarryPotterSpell{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", type='" + type + '\'' +
            ", description='" + description + '\'' +
            '}';
   }
}
