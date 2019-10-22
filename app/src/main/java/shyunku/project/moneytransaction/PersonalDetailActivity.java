package shyunku.project.moneytransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;

public class PersonalDetailActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DetailListAdapter adapter;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<Transaction> transactions = new ArrayList<>();
    String oppName = "NULL";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        oppName = intent.getStringExtra("opp_name");
        transactions = MainActivity.engine.getTransactionsByName(oppName);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerview_detail);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);

        recyclerView.setLayoutManager(layoutManager);
        adapter = new DetailListAdapter(transactions, oppName);
        recyclerView.setAdapter(adapter);

        long amount = 0;
        for(int i=0;i<transactions.size();i++)
            amount += transactions.get(i).getValue();


        TextView title = (TextView)findViewById(R.id.detail_title);
        TextView totalValue = (TextView)findViewById(R.id.total_amount_view);

        if(amount>0) {
            title.setText(oppName+"에게 빌려준 돈");
            totalValue.setTextColor(ContextCompat.getColor(this, R.color.Plus));
            totalValue.setText(amount+" 원");
        }
        else if(amount<0) {
            amount = -amount;
            title.setText(oppName+"에게 갚을 돈");
            totalValue.setTextColor(ContextCompat.getColor(this, R.color.Minus));
            totalValue.setText(amount+" 원");
        }
        else {
            title.setText(oppName+"에게 갚을 돈 없음");
            totalValue.setTextColor(ContextCompat.getColor(this, R.color.DarkTheme4));
            totalValue.setText("- 원");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        MainActivity.engine.setTransactionIdByName(oppName, transactions);
    }
}
