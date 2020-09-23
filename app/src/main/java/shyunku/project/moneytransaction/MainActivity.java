package shyunku.project.moneytransaction;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public static final String Version = "beta v0.14.1-31";
    RecyclerView recyclerView;
    PersonalRecyclerAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    Context context;

    public static TransactionEngine engine = new TransactionEngine();
    TextView totalAmountView;
    int sortMethod = 0;
    //TextView titleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FileManager.fileManageContext = this;


        checkPermission();

        TransactionEngine e = new FileManager().loadFile();
        if(e != null) engine = e;

        totalAmountView = (TextView)findViewById(R.id.total_amount_view);
        //titleView = (TextView)findViewById(R.id.personal_title);
        updateAmount();

        context = this;
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_detail);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);

        recyclerView.setLayoutManager(layoutManager);
        adapter = new PersonalRecyclerAdapter(engine);
        recyclerView.setAdapter(adapter);

        update();

        TextView ver = (TextView)findViewById(R.id.version);
        ver.setText(Version);

        final Button sortAll = (Button)findViewById(R.id.sort_button);
        sortAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] arr = new String[] {
                        "최근 거래 순 (기본)",
                        "갚을 돈 많은 순",
                        "받을 돈 많은 순",
                        "이름순 (오름차순 : ㄱ~ㅎ)",
                        "이름순 (내림차순 : ㅎ~ㄱ)",
                        "거래 많은 순",};
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
                builder.setTitle("정렬/필터 선택");
                builder.setItems(arr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sortMethod = i;
                        update();
                    }
                });
                android.app.AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        final TextView addTransactionBtn = (TextView) findViewById(R.id.add_transaction_button);
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
                        if(reasonView.getText().length()==0){
                            reasonView.setText("이름 없음");
                        }

                        int idx = radioGroup.indexOfChild(radioGroup.findViewById(radioGroup.getCheckedRadioButtonId()));
                        int types = Transaction.GET_BACK;
                        switch(idx){
                            case 0:types = Transaction.GET_BACK;break;
                            case 1:types = Transaction.LEND;break;
                        }
                        engine.add(oppName.getText()+"", Integer.parseInt(evalue.getText()+""), times,  types, reasonView.getText()+"");
                        update();
                        adapter.update();
                        alertDialog.dismiss();

                        new FileManager().saveFile(engine);
                    }
                });

                alertDialog.show();
            }
        });

        final EditText searchTile = (EditText)findViewById(R.id.search_filter);
        searchTile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = searchTile.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
                if(text.equals(""))addTransactionBtn.setEnabled(true);
                else addTransactionBtn.setEnabled(false);
                adapter.sortThis(sortMethod);
            }
        });
    }

    private void update(){
        adapter.sortThis(sortMethod);
        adapter.notifyDataSetChanged();
        layoutManager.scrollToPosition(engine.ptransactions.size()-1);
        updateAmount();
    }

    @Override
    protected void onResume() {
        super.onResume();
        update();
    }

    public void updateAmount(){
        int amount = engine.getAmountProfit();
        if(amount>0) {
            totalAmountView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.Plus));
            totalAmountView.setText(amount+" 원");
        }
        else if(amount<0) {
            amount = -amount;
            totalAmountView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.Minus));
            totalAmountView.setText(amount+" 원");
        }
        else{
            totalAmountView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.DarkTheme4));
            totalAmountView.setText("- 원");
        }
    }
    public void checkPermission(){
        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        new FileManager().saveFile(engine);
    }
}
