package org.acme.sns.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import java.util.Objects;

@RegisterForReflection
public class Quark {

    private String flavor;
    private String spin;

    public Quark() {
    }

    public String getFlavor() {
        return flavor;
    }

    public void setFlavor(String flavor) {
        this.flavor = flavor;
    }

    public String getSpin() {
        return spin;
    }

    public void setSpin(String spin) {
        this.spin = spin;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Quark)) {
            return false;
        }

        Quark other = (Quark) obj;

        return Objects.equals(other.flavor, this.flavor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.flavor);
    }
}