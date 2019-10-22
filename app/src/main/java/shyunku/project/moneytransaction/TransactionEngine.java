package shyunku.project.moneytransaction;

import java.util.ArrayList;

public class TransactionEngine {
    public ArrayList<PersonalTransaction> ptransactions = new ArrayList<>();
    public void add(String name, long value, long curTime, int type, String reason){
        for(PersonalTransaction ptrans : ptransactions)
            if(ptrans.getPersonName().equals(name)) {
                ptrans.addTransaction(value, curTime, type, reason);
                return;
            }
        ptransactions.add(new PersonalTransaction(name, value, curTime, type, reason));
    }

    public void removeByName(String name){
        for(PersonalTransaction ptrans : ptransactions) {
            if (ptrans.getPersonName().equals(name)){
                ptransactions.remove(ptrans);
                return;
            }
        }
    }

    public int getAmountProfit(){
        int amount = 0;
        for(PersonalTransaction ptrans : ptransactions)
            amount += ptrans.getTotalProfit();
        return amount;
    }

    public ArrayList<Transaction> getTransactionsByName(String name){
        for(PersonalTransaction ptrans : ptransactions) {
            if (ptrans.getPersonName().equals(name))
                return ptrans.getTransactions();
        }
        return null;
    }

    public int getTransactionIdByName(String name){
        int i=0;
        for(PersonalTransaction ptrans : ptransactions) {
            if (ptrans.getPersonName().equals(name))
                break;
            i++;
        }
        return i;
    }

    public void setTransactionIdByName(String name, ArrayList<Transaction> transactions){
        for(PersonalTransaction ptrans : ptransactions) {
            if (ptrans.getPersonName().equals(name)){
                ptrans.setTransaction(transactions);
                return;
            }
        }
    }
}
