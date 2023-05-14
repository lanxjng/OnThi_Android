package com.example.qly_hocphan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    // 1. khai báo các thành phần
    private EditText edtMaHp, edtTenHp, edtSoTc, edtHpTq, edtHpSs, edtKhoaQly, edtTk;
    private Button btnThem, btnSua, btnXoa, btnTk, btnThoat;
    private ListView lvhp;

    // CSDL
    private HPdatabase database;
    private HPAdapter adapter;
    private List<HocPhan> listHP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        anhxa();
        // khởi tạo
        database=new HPdatabase(this);
        listHP=database.InformationHP();
        adapter=new HPAdapter(this,R.layout.item_list_hp,listHP);
        lvhp.setAdapter(adapter);
//        setAdapter();
        // bắt sự kiện cho các button

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtMaHp.getText().toString().equals("")||edtTenHp.getText().toString().equals("")
                        ||edtSoTc.getText().toString().equals("")||edtHpTq.getText().toString().equals("")
                        ||edtHpSs.getText().toString().equals("")||edtKhoaQly.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this, "Thông tin không được để trống !!!", Toast.LENGTH_LONG).show();
                }else {
                    HocPhan hp = CreateHP();
                    if(hp!=null){
                        database.AddHP(hp);
                        listHP.clear();
                        listHP.addAll(database.InformationHP());
                       // setAdapter();
                        adapter.notifyDataSetChanged();
                        //Xóa DL nhập vào
                        edtMaHp.setText("");
                        edtTenHp.setText("");
                        edtSoTc.setText("");
                        edtHpTq.setText("");
                        edtHpSs.setText("");
                        edtKhoaQly.setText("");
                    }
                }
            }
        });

        // bắt sự kiện cho từng Item trong ListView
        lvhp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HocPhan hp  = listHP.get(position);
                edtMaHp.setText(hp.getMaHp());
                edtTenHp.setText(hp.getTenHp());
                edtSoTc.setText(hp.getSoTc());
                edtHpTq.setText(hp.getHpTq());
                edtHpSs.setText(hp.getHpSs());
                edtKhoaQly.setText(hp.getKhoaQly());
            }
        });
        //btnSua
        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // edtMaHp.setEnabled(false);
                HocPhan hp = new HocPhan();
                hp.setMaHp(String.valueOf(edtMaHp.getText()));
                hp.setTenHp(edtTenHp.getText()+" ");
                hp.setSoTc(edtSoTc.getText()+"");
                hp.setHpTq(edtHpTq.getText()+"");
                hp.setHpSs(edtHpSs.getText()+"");
                hp.setKhoaQly(edtKhoaQly.getText()+"");
                int kq=database.UpdateHP(hp);
                if(kq>0){
                    listHP.clear();
                    listHP.addAll(database.InformationHP());
                    if(adapter!=null) {
                        adapter.notifyDataSetChanged();
                    }
                }
                //Xóa DL nhập vào
                edtMaHp.setText("");
                edtTenHp.setText("");
                edtSoTc.setText("");
                edtHpTq.setText("");
                edtHpSs.setText("");
                edtKhoaQly.setText("");

            }
        });

        //btnXoa
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HocPhan hp = new HocPhan();
                hp.setMaHp(String.valueOf(edtMaHp.getText()));
                int kq=database.DeleteHP(hp.getMaHp());
                if(kq>0){
                    Toast.makeText(MainActivity.this,"Xóa HP thành công", Toast.LENGTH_LONG).show();
                    listHP.clear();
                    listHP.addAll(database.InformationHP());
                    if(adapter!=null){
                        adapter.notifyDataSetChanged();
                    }
                }else {
                    Toast.makeText(MainActivity.this,"Không xóa được HP ", Toast.LENGTH_LONG).show();
                }
                edtMaHp.setText("");
                edtTenHp.setText("");
                edtSoTc.setText("");
                edtHpTq.setText("");
                edtHpSs.setText("");
                edtKhoaQly.setText("");

            }
        });
        btnThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Xác nhận Thoát chương trình !!!");
                builder.setMessage("Bạn có muốn Thoát chương trình không ???");
                builder.setPositiveButton("Có ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                builder.setNegativeButton("Không ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                //hien thi
                AlertDialog d = builder.create();
                d.show();
            }
        });

        btnTk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtTk.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this,"Nhập Mã Hp cần tìm kiếm!!!",Toast.LENGTH_LONG).show();
                }
                else{
                    HocPhan hp = database.findById(edtTk.getText().toString());
                    if(hp !=null){
                        listHP.clear();
                        listHP.add(hp);
                        setAdapter();
                    }
                    else
                        Toast.makeText(MainActivity.this, "Không tìm thấy !!!", Toast.LENGTH_LONG).show();
                }
            }
        });
        // gán menu_context vào listview
        registerForContextMenu(lvhp);
    }
    private HocPhan CreateHP() {
        String MaHp =edtMaHp.getText().toString();
        String TenHp =edtTenHp.getText().toString();
        String SoTc =edtSoTc.getText().toString();
        String HpTq =edtHpTq.getText().toString();
        String HpSs =edtHpSs.getText().toString();
        String KhoaQly = edtKhoaQly.getText().toString();
        HocPhan hp =new HocPhan(MaHp, TenHp, SoTc, HpTq, HpSs, KhoaQly);
        return hp;
    }

    private void setAdapter() {
        if(adapter==null){
            // tạo mới
            adapter=new HPAdapter(this,R.layout.item_list_hp,listHP);
            lvhp.setAdapter(adapter);
        }else {
            // cập nhật lại DL
            adapter.notifyDataSetChanged();
        }
    }


    private void anhxa() {
        edtMaHp = findViewById(R.id.edtMaHp);
        edtTenHp = findViewById(R.id.edtTenHp);
        edtSoTc = findViewById(R.id.edtSoTc);
        edtHpTq = findViewById(R.id.edtHpTq);
        edtHpSs = findViewById(R.id.edtHpSs);
        edtKhoaQly = findViewById(R.id.edtKhoaQl);
        edtTk = findViewById(R.id.edtTk);
        btnThem = findViewById(R.id.btnThem);
        btnSua = findViewById(R.id.btnSua);
        btnXoa = findViewById(R.id.btnXoa);
        btnThoat = findViewById(R.id.btnThoat);
        btnTk = findViewById(R.id.btnTk);
        lvhp = findViewById(R.id.LvHp);
    }

    //Khởi tạo menu_option
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_option, menu);
        return true;
    }
    // Xử lý các lựa chọn menu

    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        switch (item.getItemId()){
            case R.id.mnu_Them:{
                //code cho them
                if(edtMaHp.getText().toString().equals("")||edtTenHp.getText().toString().equals("")
                        ||edtSoTc.getText().toString().equals("")||edtHpTq.getText().toString().equals("")
                        ||edtHpSs.getText().toString().equals("")||edtKhoaQly.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this, "Thông tin không được để trống !!!", Toast.LENGTH_LONG).show();
                }else {
                    HocPhan hp = CreateHP();
                    if(hp!=null){
                        database.AddHP(hp);
                        listHP.clear();
                        listHP.addAll(database.InformationHP());
                        // setAdapter();
                        adapter.notifyDataSetChanged();
                        //Xóa DL nhập vào
                        edtMaHp.setText("");
                        edtTenHp.setText("");
                        edtSoTc.setText("");
                        edtHpTq.setText("");
                        edtHpSs.setText("");
                        edtKhoaQly.setText("");
                    }
                }
                break;
            }
            case R.id.mnu_Sua:{
                HocPhan hp = new HocPhan();
                hp.setMaHp(String.valueOf(edtMaHp.getText()));
                hp.setTenHp(edtTenHp.getText()+" ");
                hp.setSoTc(edtSoTc.getText()+"");
                hp.setHpTq(edtHpTq.getText()+"");
                hp.setHpSs(edtHpSs.getText()+"");
                hp.setKhoaQly(edtKhoaQly.getText()+"");
                int kq=database.UpdateHP(hp);
                if(kq>0){
                    listHP.clear();
                    listHP.addAll(database.InformationHP());
                    if(adapter!=null) {
                        adapter.notifyDataSetChanged();
                    }
                }
                //Xóa DL nhập vào
                edtMaHp.setText("");
                edtTenHp.setText("");
                edtSoTc.setText("");
                edtHpTq.setText("");
                edtHpSs.setText("");
                edtKhoaQly.setText("");
                break;
            }
            case R.id.mnu_Xoa:{
                HocPhan hp = new HocPhan();
                hp.setMaHp(String.valueOf(edtMaHp.getText()));
                int kq=database.DeleteHP(hp.getMaHp());
                if(kq>0){
                    Toast.makeText(MainActivity.this,"Xóa HP thành công", Toast.LENGTH_LONG).show();
                    listHP.clear();
                    listHP.addAll(database.InformationHP());
                    if(adapter!=null){
                        adapter.notifyDataSetChanged();
                    }
                }else {
                    Toast.makeText(MainActivity.this,"Không xóa được HP ", Toast.LENGTH_LONG).show();
                }
                edtMaHp.setText("");
                edtTenHp.setText("");
                edtSoTc.setText("");
                edtHpTq.setText("");
                edtHpSs.setText("");
                edtKhoaQly.setText("");

                break;
            }
            case R.id.mnu_Thoat:{

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Xác nhận Thoát chương trình !!!");
                builder.setMessage("Bạn có muốn Thoát chương trình không ???");
                builder.setPositiveButton("Có ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                builder.setNegativeButton("Không ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                //hien thi
                AlertDialog d = builder.create();
                d.show();
            }
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_context, menu);
    }
    // xử lý

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.mnu_Sua1:{

                break;
            }
            case R.id.mnu_Xoa1:{
                HocPhan hp = new HocPhan();
                hp.setMaHp(String.valueOf(edtMaHp.getText()));
                int kq=database.DeleteHP(hp.getMaHp());
                if(kq>0){
                    Toast.makeText(MainActivity.this,"Xóa HP thành công", Toast.LENGTH_LONG).show();
                    listHP.clear();
                    listHP.addAll(database.InformationHP());
                    if(adapter!=null){
                        adapter.notifyDataSetChanged();
                    }
                }else {
                    Toast.makeText(MainActivity.this,"Không xóa được HP ", Toast.LENGTH_LONG).show();
                }
                edtMaHp.setText("");
                edtTenHp.setText("");
                edtSoTc.setText("");
                edtHpTq.setText("");
                edtHpSs.setText("");
                edtKhoaQly.setText("");

                break;
            }
            default: break;
        }
        return super.onContextItemSelected(item);
    }
}