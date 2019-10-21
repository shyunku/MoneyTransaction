package shyunku.project.moneytransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;

public class PersonalDetailActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DetailListAdapter adapter;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<Transaction> transactions = new ArrayList<>();
    String oppName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        oppName = intent.getStringExtra("opp_name");
        transactions = (ArrayList<Transaction>)intent.getSerializableExtra("data");

        recyclerView = (RecyclerView)findViewById(R.id.recyclerview_detail);
        recyclerView.setHasFixedSize(true);

        layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new DetailListAdapter(transactions, oppName);
        recyclerView.setAdapter(adapter);

        long amount = 0;
        for(int i=0;i<transactions.size();i++)
            amount += transactions.get(i).getValue();


        TextView title = (TextView)findViewById(R.id.personal_title);
        TextView totalValue = (TextView)findViewById(R.id.total_amount_view);

        if(amount>0) {
            title.setText("나 ← "+oppName);
            totalValue.setTextColor(ContextCompat.getColor(this, R.color.Plus));
            totalValue.setText(amount+" 원");
        }
        else if(amount<0) {
            amount = -amount;
            title.setText("나 → "+oppName);
            totalValue.setTextColor(ContextCompat.getColor(this, R.color.Minus));
            totalValue.setText(amount+" 원");
        }
        else {
            title.setText("나 = "+oppName);
            totalValue.setTextColor(ContextCompat.getColor(this, R.color.DarkTheme4));
            totalValue.setText("- 원");
        }
    }
}
