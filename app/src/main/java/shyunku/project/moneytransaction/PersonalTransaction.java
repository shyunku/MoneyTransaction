package shyunku.project.moneytransaction;

import java.util.ArrayList;

public class PersonalTransaction {
    private String personName;
    private ArrayList<Transaction> transactions;

    public PersonalTransaction(String personName, long value, long curTime, int type, String reason){
        this.personName = personName;
        transactions = new ArrayList<>();
        transactions.add(new Transaction(value, curTime, type, reason));
    }

    public long getTotalProfit(){
        long amount = 0;
        for(Transaction trans : transactions)
            amount += trans.getValue();
        return amount;
    }

    public long getRecentUpdateTime(){
        long ref = 0;
        for(Transaction trans : transactions)
            if (trans.getTimestamp() > ref) {
                ref = trans.getTimestamp();
            }
        return ref;
    }

    public void addTransaction(long value, long curTime, int type, String reason){
        transactions.add(new Transaction(value, curTime, type, reason));
    }

    public void setTransaction(ArrayList<Transaction> trans){
        transactions = trans;
    }

    public String getPersonName(){
        return personName;
    }

    public ArrayList<Transaction> getTransactions(){
        return transactions;
    }
}
