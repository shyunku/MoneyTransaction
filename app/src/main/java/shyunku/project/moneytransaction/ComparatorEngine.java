package shyunku.project.moneytransaction;

import java.util.Comparator;

public class ComparatorEngine {
    private final int DEFAULT_METHOD = 0;
    public Comparator<PersonalTransaction > getTransactionComparator(int i){
        switch (i){
            case 0: return recentTcomparator;
            case 1: return amountBcomparator;
            case 2: return amountScomparator;
            case 3: return nameBcomparator;
            case 4: return nameScomparator;
            case 5: return transactionComparator;
            default: return recentTcomparator;
        }
    }


    private Comparator<PersonalTransaction> nameBcomparator = new Comparator<PersonalTransaction>() {
        @Override
        public int compare(PersonalTransaction t1 ,PersonalTransaction t2) {
            return t2.getPersonName().compareTo(t1.getPersonName());
        }
    };
    private Comparator<PersonalTransaction> nameScomparator = new Comparator<PersonalTransaction>() {
        @Override
        public int compare(PersonalTransaction t1 ,PersonalTransaction t2) {
            return t1.getPersonName().compareTo(t2.getPersonName());
        }
    };
    private Comparator<PersonalTransaction> recentTcomparator = new Comparator<PersonalTransaction>() {
        @Override
        public int compare(PersonalTransaction t1 ,PersonalTransaction t2) {
            long x2 = t2.getRecentUpdateTime();
            long x1 = t1.getRecentUpdateTime();
            if(x1>x2)return 1;
            else if(x1==x2)return 0;
            else return -1;
        }
    };
    private Comparator<PersonalTransaction> amountBcomparator = new Comparator<PersonalTransaction>() {
        @Override
        public int compare(PersonalTransaction t1 ,PersonalTransaction t2) {
            long x2 = t2.getTotalProfit();
            long x1 = t1.getTotalProfit();
            if(x1<x2)return 1;
            else if(x1==x2)return 0;
            else return -1;
        }
    };
    private Comparator<PersonalTransaction> amountScomparator = new Comparator<PersonalTransaction>() {
        @Override
        public int compare(PersonalTransaction t1 ,PersonalTransaction t2) {
            long x2 = t2.getTotalProfit();
            long x1 = t1.getTotalProfit();
            if(x1<x2)return -1;
            else if(x1==x2)return 0;
            else return 1;
        }
    };
    private Comparator<PersonalTransaction> transactionComparator = new Comparator<PersonalTransaction>() {
        @Override
        public int compare(PersonalTransaction t1 ,PersonalTransaction t2) {
            long x2 = t2.getTransactions().size();
            long x1 = t1.getTransactions().size();
            if(x1<x2)return 1;
            else if(x1==x2)return 0;
            else return -1;
        }
    };
}
