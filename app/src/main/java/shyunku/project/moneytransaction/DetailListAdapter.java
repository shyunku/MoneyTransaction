package shyunku.project.moneytransaction;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DetailListAdapter extends RecyclerView.Adapter<DetailListAdapter.ViewHolder>{
    private ArrayList<Transaction> transactions = new ArrayList<>();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd a h:mm", Locale.KOREA);
    private String oppName;

    Context context;

    DetailListAdapter(ArrayList<Transaction> transactions, String oppName){
        this.oppName = oppName;
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.transaction_item_by_detail, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Transaction trans = transactions.get(position);
        holder.reasonView.setText(trans.getReason());
        String typeStr = "";
        switch(trans.getType()){
           case Transaction.GET_BACK:// 받을 돈
                typeStr = "나 → "+ oppName;
                break;
            case Transaction.LEND: //빌린 돈
                typeStr = "나 ← "+ oppName;
                break;
        }
        holder.typeView.setText(typeStr);
        long amount = trans.getValue();

        if(amount>0) holder.valueView.setTextColor(ContextCompat.getColor(context, R.color.Plus));
        else if(amount<0) holder.valueView.setTextColor(ContextCompat.getColor(context, R.color.Minus));
        else holder.valueView.setTextColor(ContextCompat.getColor(context, R.color.PureWhite));

        holder.valueView.setText(trans.getValue()+" 원");
        holder.timeView.setText(sdf.format(new Date(trans.getTimestamp())));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] arr = null;
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("");
                switch(trans.getType()){
                    case Transaction.GET_BACK://빌린돈
                    case Transaction.LEND://빌려준 돈
                        arr = new String[] {"수정", "삭제"};
                        break;
                }
                builder.setItems(arr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch(i){
                            case 0:
                                break;
                            case 1:
                                transactions.remove(position);
                                break;
                        }
                        notifyDataSetChanged();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        public TextView reasonView;
        public TextView typeView;
        public TextView timeView;
        public TextView valueView;
        public ConstraintLayout cardView;

        public ViewHolder(@NonNull View view) {
            super(view);
            reasonView = (TextView)view.findViewById(R.id.detail_reason);
            typeView = (TextView)view.findViewById(R.id.detail_type);
            timeView = (TextView)view.findViewById(R.id.detail_time);
            valueView = (TextView)view.findViewById(R.id.detail_amount);
            cardView = (ConstraintLayout)view.findViewById(R.id.detail_card);
        }
    }
}
