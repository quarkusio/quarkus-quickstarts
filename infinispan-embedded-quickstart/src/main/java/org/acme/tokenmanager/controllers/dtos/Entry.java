package org.acme.tokenmanager.controllers.dtos;

import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoField;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

public class Entry implements Serializable {

    @NotEmpty
    @Size(min = 16, max = 16)
    @ProtoField(number = 1)
    String taxCode;

    @ProtoFactory
    public Entry(@NotEmpty @Size(min = 16, max = 16) String taxCode) {

        this.taxCode = taxCode;
    }

    public Entry() {
    }

    public Entry setTaxCode(String taxCode) {
        this.taxCode = taxCode;
        return this;
    }

    public String getTaxCode() {
        return taxCode;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entry entry = (Entry) o;
        return taxCode.equals(entry.taxCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taxCode);
    }
}
