package org.acme.infinispanclient.model;

import java.util.Objects;

/**
 * Author and Spell magic object
 */
public class HPMagic {
   private final String id;
   private final String author;
   private final String spell;
   private final boolean hogwarts;

   public HPMagic(String id, String author, String spell, boolean hogwarts) {
      this.id = id;
      this.author = author;
      this.spell = spell;
      this.hogwarts = hogwarts;
   }

   public String getId() {
      return id;
   }

   public String getAuthor() {
      return author;
   }

   public String getSpell() {
      return spell;
   }

   public boolean isHogwarts() {
      return hogwarts;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      HPMagic that = (HPMagic) o;
      return Objects.equals(id, that.id) &&
            Objects.equals(author, that.author) &&
            Objects.equals(spell, that.spell) &&
            Objects.equals(hogwarts, that.hogwarts);
   }

   @Override
   public int hashCode() {
      return Objects.hash(id, author, spell, hogwarts);
   }

   @Override
   public String toString() {
      return "HarryPotterMagic{" +
            "id='" + id + '\'' +
            ", author='" + author + '\'' +
            ", spell='" + spell + '\'' +
            ", hogwarts='" + hogwarts + '\'' +
            '}';
   }
}
