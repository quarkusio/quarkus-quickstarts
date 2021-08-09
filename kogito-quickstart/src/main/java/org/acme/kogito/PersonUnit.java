package org.acme.kogito;

import org.acme.kogito.model.Person;
import org.kie.kogito.rules.DataSource;
import org.kie.kogito.rules.RuleUnitData;
import org.kie.kogito.rules.SingletonStore;

public class PersonUnit implements RuleUnitData {

    private SingletonStore<Person> person;
 
    public PersonUnit() {
        this(DataSource.createSingleton());
    }
 
    public PersonUnit(SingletonStore<Person> person) {
        this.person = person;
    }

    public SingletonStore<Person> getPerson() {
        return person;
    }

    public void setPerson(SingletonStore<Person> person) {
        this.person = person;
    }
}
 