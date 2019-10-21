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

    public int getAmountProfit(){
        int amount = 0;
        for(PersonalTransaction ptrans : ptransactions)
            amount += ptrans.getTotalProfit();
        return amount;
    }
}
