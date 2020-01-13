package com.bitc502.grapemarket;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bitc502.grapemarket.connect2server.Connect2Server;
import com.bitc502.grapemarket.permission.PermissionsActivity;
import com.bitc502.grapemarket.permission.PermissionsChecker;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WriteActivity extends AppCompatActivity {
    private static final String[] PERMISSIONS_READ_STORAGE = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
    PermissionsChecker checker;

    private ImageView selectedImage1,selectedImage2,selectedImage3,selectedImage4,selectedImage5 ;
    private EditText write_title,write_price,write_content;
    private List<String> imagePathList;
    private Context writeContext;
    private Spinner write_category;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        checker = new PermissionsChecker(this);
        writeContext = getApplicationContext();
        selectedImage1 = findViewById(R.id.wirte_selectedImg1);
        selectedImage2 = findViewById(R.id.wirte_selectedImg2);
        selectedImage3 = findViewById(R.id.wirte_selectedImg3);
        selectedImage4 = findViewById(R.id.wirte_selectedImg4);
        selectedImage5 = findViewById(R.id.wirte_selectedImg5);

        selectedImage1.setVisibility(View.INVISIBLE);
        selectedImage2.setVisibility(View.INVISIBLE);
        selectedImage3.setVisibility(View.INVISIBLE);
        selectedImage4.setVisibility(View.INVISIBLE);
        selectedImage5.setVisibility(View.INVISIBLE);

        imagePathList = new ArrayList<>();

        write_category = findViewById(R.id.write_category);
        write_title = findViewById(R.id.write_title);
        write_price = findViewById(R.id.write_price);
        write_content = findViewById(R.id.write_content);


        write_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = (String)adapterView.getItemAtPosition(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void btnWriteSelectImageClicked(View v){
        imagePathList.clear();
        selectedImage1.setVisibility(View.INVISIBLE);
        selectedImage2.setVisibility(View.INVISIBLE);
        selectedImage3.setVisibility(View.INVISIBLE);
        selectedImage4.setVisibility(View.INVISIBLE);
        selectedImage5.setVisibility(View.INVISIBLE);

        if (checker.lacksPermissions(PERMISSIONS_READ_STORAGE)) {
            startPermissionsActivity(PERMISSIONS_READ_STORAGE);
        } else {
            getImageFromUserDevice();
        }
    }

    public void getImageFromUserDevice() {
        final Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_PICK);
        //google photo 앱으로만 사진 선택하도록 강제함.
        //그래서 나중에 google photo 앱이 설치 되어있는지 검사 필요
        //이렇게 한 이유는 갤러리 다중 픽 방법이 폰 제조사, 같은 제조사라도 기종별로
        //전부 다름. EXTRA_ALLOW_MULTIPLE이 일단 내 폰에선 안먹힘(노트10 기준 삼성 갤러리 앱에서 안먹힌다는 뜻)
        //사실 올바른 해결책은 외부라이브러리를 쓰는 것인데 난 안쓰겠음 그냥 구글 포토 다 설치시킴
        //구글 포토 설치되어있는지 확인하는 코드 필요
        intent.setPackage("com.google.android.apps.photos");
        final Intent chooserIntent = Intent.createChooser(intent, "사진을 선택하세요.");
        startActivityForResult(chooserIntent, 1010);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1010) {
            try {
                if (data == null) {
                    return;
                }
                ClipData clipData = data.getClipData();
                if(clipData.getItemCount() > 5){
                    Toast.makeText(writeContext,"사진은 5장까지만 선택해주세요.",Toast.LENGTH_LONG).show();
                }else{
                    List<Uri> imageUriList = new ArrayList<>();
                    int selectedImageCount = clipData.getItemCount();
                    for(int i = 0 ; i < selectedImageCount; i++){
                        imageUriList.add(clipData.getItemAt(i).getUri());
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(imageUriList.get(i), filePathColumn, null, null, null);
                        if (cursor != null) {
                            cursor.moveToFirst();
                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            imagePathList.add(cursor.getString(columnIndex));
                            if(i == 0){
                                Picasso.with(writeContext).load(new File(imagePathList.get(i))).into(selectedImage1);
                                cursor.close();
                                selectedImage1.setVisibility(View.VISIBLE);
                            }else if(i == 1){
                                Picasso.with(writeContext).load(new File(imagePathList.get(i))).into(selectedImage2);
                                cursor.close();
                                selectedImage2.setVisibility(View.VISIBLE);
                            }else if(i == 2){
                                Picasso.with(writeContext).load(new File(imagePathList.get(i))).into(selectedImage3);
                                cursor.close();
                                selectedImage3.setVisibility(View.VISIBLE);
                            }else if(i == 3){
                                Picasso.with(writeContext).load(new File(imagePathList.get(i))).into(selectedImage4);
                                cursor.close();
                                selectedImage4.setVisibility(View.VISIBLE);
                            }else if(i == 4){
                                Picasso.with(writeContext).load(new File(imagePathList.get(i))).into(selectedImage5);
                                cursor.close();
                                selectedImage5.setVisibility(View.VISIBLE);
                            }
                        } else {

                        }
                    }
                }
            }catch (Exception e){
                Log.d("myerror", e.toString());
            }

        }
    }

    public void btnWriteComplete(View v){
        new AsyncTask<Void,Integer,Integer>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Integer doInBackground(Void... voids) {
                return Connect2Server.sendWriteInfoToServer(imagePathList,category,write_title.getText().toString(),write_price.getText().toString(),write_content.getText().toString());
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected void onPostExecute(Integer result) {
                super.onPostExecute(result);
                if(result == 1){
                    Log.d("mywrite", "글쓰기 성공");
                    Intent intent = new Intent(writeContext,ListActivity.class);
                    startActivity(intent);
                }else{
                    Log.d("mywrite", "글쓰기 실패");
                    Toast.makeText(writeContext,"글쓰기에 실패했습니다",Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }

    private void startPermissionsActivity(String[] permission) {
        PermissionsActivity.startActivityForResult(this, 0, permission);
    }
}
