package org.acme.kogito.queries;

import org.acme.kogito.model.LoanApplication;
import org.kie.kogito.rules.DataSource;
import org.kie.kogito.rules.DataStore;
import org.kie.kogito.rules.RuleUnitData;

public class LoanUnit implements RuleUnitData {

    private int maxAmount;
    private DataStore<LoanApplication> loanApplications;

    public LoanUnit() {
        this(DataSource.createStore(), 0);
    }

    public LoanUnit(DataStore<LoanApplication> loanApplications, int maxAmount) {
        this.loanApplications = loanApplications;
        this.maxAmount = maxAmount;
    }

    public DataStore<LoanApplication> getLoanApplications() {
        return loanApplications;
    }

    public void setLoanApplications(DataStore<LoanApplication> loanApplications) {
        this.loanApplications = loanApplications;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(int maxAmount) {
        this.maxAmount = maxAmount;
    }
}