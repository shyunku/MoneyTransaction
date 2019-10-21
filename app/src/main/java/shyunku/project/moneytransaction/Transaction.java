package shyunku.project.moneytransaction;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Transaction implements Serializable {
    public static final transient int WILL_PAY_BACK = 1000;       //-
    public static final transient int LEND = 1001;                            //+
    public static final transient int PAY_BACK = 1002;                   //+

    private long value;
    private long timestamp;
    private int type;
    private String reason;

    public Transaction(long value, long curTime, int type, String reason) {
        if(type == WILL_PAY_BACK)
            this.value = -value;
        else
            this.value = value;
        this.type = type;
        this.timestamp = curTime;
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
