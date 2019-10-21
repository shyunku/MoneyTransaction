package shyunku.project.moneytransaction;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public static final String Version = "beta 0.7.1.2v";
    RecyclerView recyclerView;
    PersonalRecyclerAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    Context context;

    private final TransactionEngine engine = new TransactionEngine();
    TextView totalAmountView, titleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        totalAmountView = (TextView)findViewById(R.id.total_amount_view);
        titleView = (TextView)findViewById(R.id.personal_title);
        updateAmount();

        context = this;
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_detail);
        recyclerView.setHasFixedSize(true);

        layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PersonalRecyclerAdapter(engine);
        recyclerView.setAdapter(adapter);

        TextView ver = (TextView)findViewById(R.id.version);
        ver.setText(Version);

        TextView addTransactionBtn = (TextView) findViewById(R.id.add_transaction_button);
        addTransactionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View curv = inflater.inflate(R.layout.add_transatcion_dialog, (ViewGroup)findViewById(R.id.add_transaction_layout));

                builder.setView(curv);
                final AlertDialog alertDialog = builder.create();

                final EditText oppName = (EditText) curv.findViewById(R.id.add_transaction_opp_name);
                final EditText evalue = (EditText)curv.findViewById(R.id.add_transaction_value);
                final CheckBox checkBox = (CheckBox)curv.findViewById(R.id.fix_to_current);
                final TextView date = (TextView)curv.findViewById(R.id.register_date_pick);
                final TextView time = (TextView)curv.findViewById(R.id.register_time_pick);
                Button addBtn = (Button)curv.findViewById(R.id.add_transaction_final_button);
                Button cancelBtn = (Button)curv.findViewById(R.id.add_transaction_cancel_button);
                final RadioGroup radioGroup = (RadioGroup) curv.findViewById(R.id.TransactionTypeRadioGroup);
                final TextView errorMsg = (TextView) curv.findViewById(R.id.error_message);
                final EditText reasonView = (EditText)curv.findViewById(R.id.add_transaction_reason);


                evalue.setInputType(InputType.TYPE_CLASS_NUMBER| InputType.TYPE_NUMBER_FLAG_DECIMAL);
                evalue.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy년 M월 d일", Locale.KOREA);
                SimpleDateFormat sdf2 = new SimpleDateFormat("a h시 m분", Locale.KOREA);

                date.setText(sdf1.format(new Date(System.currentTimeMillis())));
                time.setText(sdf2.format(new Date(System.currentTimeMillis())));

                date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(System.currentTimeMillis());
                        DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                date.setText(i+"년 "+(i1+1)+"월 "+i2+"일");
                            }
                        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                        dialog.show();
                    }
                });
                time.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(System.currentTimeMillis());
                        TimePickerDialog dialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                Calendar cal = Calendar.getInstance();
                                cal.setTimeInMillis(System.currentTimeMillis());
                                cal.set(Calendar.HOUR_OF_DAY, i);
                                cal.set(Calendar.MINUTE, i1);
                                SimpleDateFormat sdf = new SimpleDateFormat("a h시 m분", Locale.KOREA);
                                time.setText(sdf.format(cal.getTime()));
                            }
                        }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false);
                        dialog.show();
                    }
                });
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if(b){
                            //checked
                            date.setBackgroundColor(ContextCompat.getColor(context, R.color.Translucent));
                            time.setBackgroundColor(ContextCompat.getColor(context, R.color.Translucent));
                            date.setTextColor(ContextCompat.getColor(context, R.color.DisabledHighlight));
                            time.setTextColor(ContextCompat.getColor(context, R.color.DisabledHighlight));
                            date.setEnabled(false);
                            time.setEnabled(false);
                        }else{
                            //unchecked
                            date.setBackgroundColor(ContextCompat.getColor(context, R.color.DarkTheme6));
                            time.setBackgroundColor(ContextCompat.getColor(context, R.color.DarkTheme6));
                            date.setTextColor(ContextCompat.getColor(context, R.color.Highlighted));
                            time.setTextColor(ContextCompat.getColor(context, R.color.Highlighted));
                            date.setEnabled(true);
                            time.setEnabled(true);
                        }
                    }
                });
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

                addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        errorMsg.setText("");
                        long times = 0;
                        if(checkBox.isChecked()) times = System.currentTimeMillis();
                        else{
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 M월 d일 a h시 m분", Locale.KOREA);
                            String get = date.getText()+" "+time.getText();
                            try {
                                times = sdf.parse(get).getTime();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        if(times > System.currentTimeMillis()){
                            errorMsg.setText("설정한 날짜/시간이  현재 시간보다 앞서 있습니다.");
                            return;
                        }
                        if(oppName.getText().length()==0){
                            errorMsg.setText("상대 이름을 적어주세요.");
                            return;
                        }
                        if(evalue.getText().length()==0){
                            errorMsg.setText("금액을 입력해주세요.");
                            return;
                        }

                        int idx = radioGroup.indexOfChild(radioGroup.findViewById(radioGroup.getCheckedRadioButtonId()));
                        int types = Transaction.WILL_PAY_BACK;
                        switch(idx){
                            case 0:types = Transaction.WILL_PAY_BACK;break;
                            case 1:types = Transaction.LEND;break;
                            case 2:types = Transaction.PAY_BACK;break;
                        }
                        engine.add(oppName.getText()+"", Integer.parseInt(evalue.getText()+""), times,  types, reasonView.getText()+"");
                        adapter.notifyDataSetChanged();
                        updateAmount();
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
            }
        });
    }

    public void updateAmount(){
        int amount = engine.getAmountProfit();
        if(amount>0) {
            titleView.setText("받을 돈 (+)");
            totalAmountView.setTextColor(ContextCompat.getColor(this, R.color.Plus));
            totalAmountView.setText(amount+" 원");
        }
        else if(amount<0) {
            amount = -amount;
            titleView.setText("갚을 돈 (-)");
            totalAmountView.setTextColor(ContextCompat.getColor(this, R.color.Minus));
            totalAmountView.setText(amount+" 원");
        }
        else{
            titleView.setText("받을 돈 = 갚을 돈");
            totalAmountView.setTextColor(ContextCompat.getColor(this, R.color.DarkTheme4));
            totalAmountView.setText("- 원");
        }
    }
}
