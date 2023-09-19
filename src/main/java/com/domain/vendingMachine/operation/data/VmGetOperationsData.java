package com.domain.vendingMachine.operation.data;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VmGetOperationsData implements Serializable {

    private int vendingMachine;
    private String totalValue;
    private List<VmGetOperationData> operations = new ArrayList<>();

    public int getVendingMachine() {
        return vendingMachine;
    }

    public void setVendingMachine(int vendingMachine) {
        this.vendingMachine = vendingMachine;
    }

    public String getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(String totalValue) {
        this.totalValue = totalValue;
    }

    public List<VmGetOperationData> getOperations() {
        return operations;
    }

    public void setOperations(List<VmGetOperationData> operations) {
        this.operations = operations;
    }
}
