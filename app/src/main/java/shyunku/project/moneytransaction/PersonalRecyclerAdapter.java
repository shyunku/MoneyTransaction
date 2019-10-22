package shyunku.project.moneytransaction;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class PersonalRecyclerAdapter extends RecyclerView.Adapter<PersonalRecyclerAdapter.ViewHolder> {
    public TransactionEngine engine = new TransactionEngine();
    public TransactionEngine realEngine = new TransactionEngine();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd a h:mm", Locale.KOREA);
    Context mContext;
    ViewGroup parents;

    public PersonalRecyclerAdapter(TransactionEngine engine){
        realEngine = engine;
        this.engine.ptransactions.addAll(realEngine.ptransactions);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        parents = parent;
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.transaction_item_by_person, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final PersonalTransaction curTrans = engine.ptransactions.get(position);
        holder.recentTimeView.setText("가장 최근 항목 : "+sdf.format(new Date(curTrans.getRecentUpdateTime())));
        long amount = curTrans.getTotalProfit();

        if(amount>0) holder.valueView.setTextColor(ContextCompat.getColor(mContext, R.color.Plus));
        else if(amount<0) holder.valueView.setTextColor(ContextCompat.getColor(mContext, R.color.Minus));
        else holder.valueView.setTextColor(ContextCompat.getColor(mContext, R.color.PureWhite));

        holder.valueView.setText(curTrans.getTotalProfit()+" 원");
        holder.nameView.setText(curTrans.getPersonName());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PersonalDetailActivity.class);
                intent.putExtra("opp_name", curTrans.getPersonName());

                Bundle bundle = new Bundle();
                bundle.putSerializable("data", curTrans.getTransactions());
                intent.putExtras(bundle);

                mContext.startActivity(intent);
            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final String[] arr = new String[] {"결산하기",  curTrans.getPersonName()+" 거래 기록 전체 삭제", "이 항목 삭제"};
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("");
                builder.setItems(arr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch(i){
                            case 0:
                                break;
                            case 1:
                                break;
                            case 2:
                                realEngine.removeByName(curTrans.getPersonName());
                                engine.removeByName(curTrans.getPersonName());
                                break;
                        }
                        updateAmount();
                        notifyDataSetChanged();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return engine.ptransactions.size();
    }

    public void filter(String charText){
        charText = charText.toLowerCase();
        engine.ptransactions.clear();

        if(charText.length() == 0){
            engine.ptransactions.addAll(realEngine.ptransactions);
        }else{
            for(PersonalTransaction t : realEngine.ptransactions){
                String name = t.getPersonName();
                if(name.toLowerCase().contains(charText))
                    engine.ptransactions.add(t);
            }
        }
        notifyDataSetChanged();
    }

    public void updateAmount(){
        int amount = engine.getAmountProfit();
        totalAmountView = (TextView) ((ViewGroup)parents.getParent()).findViewById(R.id.total_amount_view);
        Log.e("a", totalAmountView.getText().toString());
        if(amount>0) {
            totalAmountView.setTextColor(ContextCompat.getColor(mContext, R.color.Plus));
            totalAmountView.setText(amount+" 원");
        }
        else if(amount<0) {
            amount = -amount;
            totalAmountView.setTextColor(ContextCompat.getColor(mContext, R.color.Minus));
            totalAmountView.setText(amount+" 원");
        }
        else{
            totalAmountView.setTextColor(ContextCompat.getColor(mContext, R.color.DarkTheme4));
            totalAmountView.setText("- 원");
        }
    }

    public void sortThis(int method){
        Collections.sort(engine.ptransactions, new ComparatorEngine().getTransactionComparator(method));
    }

    public void update(){
        engine.ptransactions.clear();
        engine.ptransactions.addAll(realEngine.ptransactions);
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        public TextView nameView;
        public TextView valueView;
        public TextView recentTimeView;
        public CardView cardView;

        public ViewHolder(@NonNull View view) {
            super(view);
            nameView = (TextView)view.findViewById(R.id.detail_type);
            valueView = (TextView)view.findViewById(R.id.detail_amount);
            recentTimeView = (TextView)view.findViewById(R.id.detail_time);
            cardView = (CardView)view.findViewById(R.id.detail_card);
        }
    }

    TextView totalAmountView;
}
