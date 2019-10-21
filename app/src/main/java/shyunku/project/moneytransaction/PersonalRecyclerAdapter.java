package shyunku.project.moneytransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PersonalRecyclerAdapter extends RecyclerView.Adapter<PersonalRecyclerAdapter.ViewHolder> {
    public TransactionEngine engine = new TransactionEngine();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd a h:mm", Locale.KOREA);
    Context mContext;

    public PersonalRecyclerAdapter(TransactionEngine engine){
        this.engine = engine;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
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
    }

    @Override
    public int getItemCount() {
        return engine.ptransactions.size();
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
}
