package org.acme.sns.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@RegisterForReflection
public class Quark {

    private String flavor;
    private String spin;

}
