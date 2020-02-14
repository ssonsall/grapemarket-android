package com.bitc502.grapemarket;


import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bitc502.grapemarket.connect2server.Connect2Server;
import com.bitc502.grapemarket.dialog.CustomAnimationDialog;
import com.bitc502.grapemarket.model.BoardForDetail;
import com.bitc502.grapemarket.model.NullCheckState;
import com.bitc502.grapemarket.permission.PermissionsActivity;
import com.bitc502.grapemarket.permission.PermissionsChecker;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BoardModifyActivity extends AppCompatActivity {
    private static final String[] PERMISSIONS_READ_STORAGE = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final String[] PERMISSIONS_WRITE_STORAGE = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private PermissionsChecker checker;
    private boolean permissionRead, permissionWrite;
    private ImageView selectedImage1, selectedImage2, selectedImage3, selectedImage4, selectedImage5;
    private EditText write_title, write_price, write_content;
    private List<String> imagePathList;
    private Context boardModifyContext;
    private Spinner write_category;
    private String category;
    private ArrayAdapter spinnerAdpater;
    private Integer boardId;
    private BoardForDetail boardForDetail;
    private FrameLayout frameLayoutImg1, frameLayoutImg2, frameLayoutImg3, frameLayoutImg4, frameLayoutImg5;
    private NullCheckState nullCheckState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_modify);

        boardModifyContext = getApplicationContext();
        checker = new PermissionsChecker(boardModifyContext);
        permissionRead = false;
        permissionWrite = false;
        nullCheckState = new NullCheckState();
        boardId = getIntent().getExtras().getInt("boardId");


        frameLayoutImg1 = findViewById(R.id.write_selectedImg1_layout);
        frameLayoutImg1.setVisibility(View.INVISIBLE);
        frameLayoutImg2 = findViewById(R.id.write_selectedImg2_layout);
        frameLayoutImg2.setVisibility(View.INVISIBLE);
        frameLayoutImg3 = findViewById(R.id.write_selectedImg3_layout);
        frameLayoutImg3.setVisibility(View.INVISIBLE);
        frameLayoutImg4 = findViewById(R.id.write_selectedImg4_layout);
        frameLayoutImg4.setVisibility(View.INVISIBLE);
        frameLayoutImg5 = findViewById(R.id.write_selectedImg5_layout);
        frameLayoutImg5.setVisibility(View.INVISIBLE);

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

        spinnerAdpater = ArrayAdapter.createFromResource(boardModifyContext, R.array.write_category, R.layout.spinner_dialog_layout);
        spinnerAdpater.setDropDownViewResource(R.layout.spinner_text_setting);
        write_category.setAdapter(spinnerAdpater);

        write_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = (String) adapterView.getItemAtPosition(i);
                Log.d("spintest2", category);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        setSavedBoardDetailData();

    }

    public void modify_spinner_arrow_btn_clicked(View v) {
        write_category.performClick();
    }

    public void btnImgDelete1(View v) {
        //imagePathList 하나씩 땡기기 (remove 하면 자동으로 인덱스 정렬되나? 되면 꿀이고 안되면 개고생)
        //imageView 세팅 다시하기 그냥 하나씩 땡기는 거임
        if (imagePathList.size() == 2) {
            boardForDetail.setCurrentImage1(boardForDetail.getCurrentImage2());
            boardForDetail.setCurrentImage2("");
            Picasso.with(boardModifyContext).load(new File(imagePathList.get(1))).into(selectedImage1);
            if (imagePathList.get(1).equals("previousImage")) {
                selectedImage1.setImageBitmap(boardForDetail.getImages().get(1));
            }
            frameLayoutImg2.setVisibility(View.INVISIBLE);
        } else if (imagePathList.size() == 3) {
            boardForDetail.setCurrentImage1(boardForDetail.getCurrentImage2());
            boardForDetail.setCurrentImage2(boardForDetail.getCurrentImage3());
            boardForDetail.setCurrentImage3("");
            Picasso.with(boardModifyContext).load(new File(imagePathList.get(1))).into(selectedImage1);
            Picasso.with(boardModifyContext).load(new File(imagePathList.get(2))).into(selectedImage2);
            if (imagePathList.get(1).equals("previousImage")) {
                selectedImage1.setImageBitmap(boardForDetail.getImages().get(1));
            }
            if (imagePathList.get(2).equals("previousImage")) {
                selectedImage2.setImageBitmap(boardForDetail.getImages().get(2));
            }
            frameLayoutImg3.setVisibility(View.INVISIBLE);
        } else if (imagePathList.size() == 4) {
            boardForDetail.setCurrentImage1(boardForDetail.getCurrentImage2());
            boardForDetail.setCurrentImage2(boardForDetail.getCurrentImage3());
            boardForDetail.setCurrentImage3(boardForDetail.getCurrentImage4());
            boardForDetail.setCurrentImage4("");
            Picasso.with(boardModifyContext).load(new File(imagePathList.get(1))).into(selectedImage1);
            Picasso.with(boardModifyContext).load(new File(imagePathList.get(2))).into(selectedImage2);
            Picasso.with(boardModifyContext).load(new File(imagePathList.get(3))).into(selectedImage3);
            if (imagePathList.get(1).equals("previousImage")) {
                selectedImage1.setImageBitmap(boardForDetail.getImages().get(1));
            }
            if (imagePathList.get(2).equals("previousImage")) {
                selectedImage2.setImageBitmap(boardForDetail.getImages().get(2));
            }
            if (imagePathList.get(3).equals("previousImage")) {
                selectedImage3.setImageBitmap(boardForDetail.getImages().get(3));
            }
            frameLayoutImg4.setVisibility(View.INVISIBLE);
        } else if (imagePathList.size() == 5) {
            boardForDetail.setCurrentImage1(boardForDetail.getCurrentImage2());
            boardForDetail.setCurrentImage2(boardForDetail.getCurrentImage3());
            boardForDetail.setCurrentImage3(boardForDetail.getCurrentImage4());
            boardForDetail.setCurrentImage4(boardForDetail.getCurrentImage5());
            boardForDetail.setCurrentImage5("");
            Picasso.with(boardModifyContext).load(new File(imagePathList.get(1))).into(selectedImage1);
            Picasso.with(boardModifyContext).load(new File(imagePathList.get(2))).into(selectedImage2);
            Picasso.with(boardModifyContext).load(new File(imagePathList.get(3))).into(selectedImage3);
            Picasso.with(boardModifyContext).load(new File(imagePathList.get(4))).into(selectedImage4);
            if (imagePathList.get(1).equals("previousImage")) {
                selectedImage1.setImageBitmap(boardForDetail.getImages().get(1));
            }
            if (imagePathList.get(2).equals("previousImage")) {
                selectedImage2.setImageBitmap(boardForDetail.getImages().get(2));
            }
            if (imagePathList.get(3).equals("previousImage")) {
                selectedImage3.setImageBitmap(boardForDetail.getImages().get(3));
            }
            if (imagePathList.get(4).equals("previousImage")) {
                selectedImage4.setImageBitmap(boardForDetail.getImages().get(4));
            }
            frameLayoutImg5.setVisibility(View.INVISIBLE);
        } else if (imagePathList.size() == 1) {
            boardForDetail.setCurrentImage1("");
            frameLayoutImg1.setVisibility(View.INVISIBLE);
        }
        imagePathList.remove(0);
        if (boardForDetail.getImages().size() > 0) {
            boardForDetail.getImages().remove(0);
        }
    }

    public void btnImgDelete2(View v) {
        if (imagePathList.size() == 3) {
            boardForDetail.setCurrentImage2(boardForDetail.getCurrentImage3());
            boardForDetail.setCurrentImage3("");
            Picasso.with(boardModifyContext).load(new File(imagePathList.get(2))).into(selectedImage2);
            if (imagePathList.get(2).equals("previousImage")) {
                selectedImage2.setImageBitmap(boardForDetail.getImages().get(2));
            }
            frameLayoutImg3.setVisibility(View.INVISIBLE);
        } else if (imagePathList.size() == 4) {
            boardForDetail.setCurrentImage2(boardForDetail.getCurrentImage3());
            boardForDetail.setCurrentImage3(boardForDetail.getCurrentImage4());
            boardForDetail.setCurrentImage4("");
            Picasso.with(boardModifyContext).load(new File(imagePathList.get(2))).into(selectedImage2);
            Picasso.with(boardModifyContext).load(new File(imagePathList.get(3))).into(selectedImage3);
            if (imagePathList.get(2).equals("previousImage")) {
                selectedImage2.setImageBitmap(boardForDetail.getImages().get(2));
            }
            if (imagePathList.get(3).equals("previousImage")) {
                selectedImage3.setImageBitmap(boardForDetail.getImages().get(3));
            }
            frameLayoutImg4.setVisibility(View.INVISIBLE);
        } else if (imagePathList.size() == 5) {
            boardForDetail.setCurrentImage2(boardForDetail.getCurrentImage3());
            boardForDetail.setCurrentImage3(boardForDetail.getCurrentImage4());
            boardForDetail.setCurrentImage4(boardForDetail.getCurrentImage5());
            boardForDetail.setCurrentImage5("");
            Picasso.with(boardModifyContext).load(new File(imagePathList.get(2))).into(selectedImage2);
            Picasso.with(boardModifyContext).load(new File(imagePathList.get(3))).into(selectedImage3);
            Picasso.with(boardModifyContext).load(new File(imagePathList.get(4))).into(selectedImage4);
            if (imagePathList.get(2).equals("previousImage")) {
                selectedImage2.setImageBitmap(boardForDetail.getImages().get(2));
            }
            if (imagePathList.get(3).equals("previousImage")) {
                selectedImage3.setImageBitmap(boardForDetail.getImages().get(3));
            }
            if (imagePathList.get(4).equals("previousImage")) {
                selectedImage4.setImageBitmap(boardForDetail.getImages().get(4));
            }
            frameLayoutImg5.setVisibility(View.INVISIBLE);
        } else if (imagePathList.size() == 2) {
            boardForDetail.setCurrentImage2("");
            frameLayoutImg2.setVisibility(View.INVISIBLE);
        }
        imagePathList.remove(1);
        if (boardForDetail.getImages().size() > 1) {
            boardForDetail.getImages().remove(1);
        }
    }

    public void btnImgDelete3(View v) {
        if (imagePathList.size() == 4) {
            boardForDetail.setCurrentImage3(boardForDetail.getCurrentImage4());
            boardForDetail.setCurrentImage4("");
            Picasso.with(boardModifyContext).load(new File(imagePathList.get(3))).into(selectedImage3);
            if (imagePathList.get(3).equals("previousImage")) {
                selectedImage3.setImageBitmap(boardForDetail.getImages().get(3));
            }
            frameLayoutImg4.setVisibility(View.INVISIBLE);
        } else if (imagePathList.size() == 5) {
            boardForDetail.setCurrentImage3(boardForDetail.getCurrentImage4());
            boardForDetail.setCurrentImage4(boardForDetail.getCurrentImage5());
            boardForDetail.setCurrentImage5("");
            Picasso.with(boardModifyContext).load(new File(imagePathList.get(3))).into(selectedImage3);
            Picasso.with(boardModifyContext).load(new File(imagePathList.get(4))).into(selectedImage4);
            if (imagePathList.get(3).equals("previousImage")) {
                selectedImage3.setImageBitmap(boardForDetail.getImages().get(3));
            }
            if (imagePathList.get(4).equals("previousImage")) {
                selectedImage4.setImageBitmap(boardForDetail.getImages().get(4));
            }
            frameLayoutImg5.setVisibility(View.INVISIBLE);
        } else if (imagePathList.size() == 3) {
            boardForDetail.setCurrentImage3("");
            frameLayoutImg3.setVisibility(View.INVISIBLE);
        }
        imagePathList.remove(2);
        if (boardForDetail.getImages().size() > 2) {
            boardForDetail.getImages().remove(2);
        }
    }

    public void btnImgDelete4(View v) {
        if (imagePathList.size() == 5) {
            boardForDetail.setCurrentImage4(boardForDetail.getCurrentImage5());
            boardForDetail.setCurrentImage5("");
            Picasso.with(boardModifyContext).load(new File(imagePathList.get(4))).into(selectedImage4);
            if (imagePathList.get(4).equals("previousImage")) {
                selectedImage4.setImageBitmap(boardForDetail.getImages().get(4));
            }
            frameLayoutImg5.setVisibility(View.INVISIBLE);
        } else if (imagePathList.size() == 4) {
            boardForDetail.setCurrentImage4("");
            frameLayoutImg4.setVisibility(View.INVISIBLE);
        }
        imagePathList.remove(3);
        if (boardForDetail.getImages().size() > 3) {
            boardForDetail.getImages().remove(3);
        }
    }

    public void btnImgDelete5(View v) {
        boardForDetail.setCurrentImage5("");
        imagePathList.remove(4);
        if (boardForDetail.getImages().size() > 4) {
            boardForDetail.getImages().remove(4);
        }
        frameLayoutImg5.setVisibility(View.INVISIBLE);
    }

    public void setSavedBoardDetailData() {
        new AsyncTask<Void, Boolean, BoardForDetail>() {
            CustomAnimationDialog podoLoading = new CustomAnimationDialog(BoardModifyActivity.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                podoLoading.show();
            }

            @Override
            protected BoardForDetail doInBackground(Void... voids) {
                return Connect2Server.requestDetailBoard(boardId);
            }

            @Override
            protected void onProgressUpdate(Boolean... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected void onPostExecute(BoardForDetail result) {
                super.onPostExecute(result);
                boardForDetail = result;
                int imageCount = result.getImages().size();
                Log.d("tesssttt", imageCount + "");
                for (int i = 0; i < imageCount; i++) {
                    if (i == 0) {
                        selectedImage1.setImageBitmap(result.getImages().get(i));
                        imagePathList.add("previousImage");
                        selectedImage1.setVisibility(View.VISIBLE);
                        frameLayoutImg1.setVisibility(View.VISIBLE);
                    } else if (i == 1) {
                        selectedImage2.setImageBitmap(result.getImages().get(i));
                        imagePathList.add("previousImage");
                        selectedImage2.setVisibility(View.VISIBLE);
                        frameLayoutImg2.setVisibility(View.VISIBLE);
                    } else if (i == 2) {
                        selectedImage3.setImageBitmap(result.getImages().get(i));
                        imagePathList.add("previousImage");
                        selectedImage3.setVisibility(View.VISIBLE);
                        frameLayoutImg3.setVisibility(View.VISIBLE);
                    } else if (i == 3) {
                        selectedImage4.setImageBitmap(result.getImages().get(i));
                        imagePathList.add("previousImage");
                        selectedImage4.setVisibility(View.VISIBLE);
                        frameLayoutImg4.setVisibility(View.VISIBLE);
                    } else if (i == 4) {
                        selectedImage5.setImageBitmap(result.getImages().get(i));
                        imagePathList.add("previousImage");
                        selectedImage5.setVisibility(View.VISIBLE);
                        frameLayoutImg5.setVisibility(View.VISIBLE);
                    }
                }
                write_title.setText(result.getTitle());
                //스피너 엔트리 세팅하면서 어떤 곳은 전체, 인기매물 있고 없고 이런거 차이로 넘버링 차이 발생
                write_category.setSelection(Integer.parseInt(result.getCategory()) - 2);
                write_price.setText(result.getPrice());
                write_content.setText(result.getContent());
                podoLoading.dismiss();
            }
        }.execute();
    }

    public void btnWriteSelectImageClicked(View v) {
        if (checker.lacksPermissions(PERMISSIONS_READ_STORAGE)) {
            startPermissionsActivity(PERMISSIONS_READ_STORAGE);
            permissionRead = true;
        } else {
            permissionRead = true;
        }

        if (checker.lacksPermissions(PERMISSIONS_WRITE_STORAGE)) {
            startPermissionsActivity(PERMISSIONS_WRITE_STORAGE);
            permissionWrite = true;
        } else {
            permissionWrite = true;
        }

        if (permissionRead && permissionWrite) {
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
        this.startActivityForResult(chooserIntent, 1010);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1010) {
            try {
                if (data == null) {
                    return;
                }
                ClipData clipData = data.getClipData();
                int userSelectedImageCount = clipData.getItemCount();
                if (userSelectedImageCount > 5) {
                    Toast.makeText(boardModifyContext, "사진은 5장까지만 선택해주세요.", Toast.LENGTH_LONG).show();
                } else if (userSelectedImageCount + imagePathList.size() > 5) {
                    Toast.makeText(boardModifyContext, "사진은 5장까지만 선택해주세요.", Toast.LENGTH_LONG).show();
                } else {
                    List<Uri> imageUriList = new ArrayList<>();
                    for (int i = 0; i < userSelectedImageCount; i++) {
                        imageUriList.add(clipData.getItemAt(i).getUri());
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(imageUriList.get(i), filePathColumn, null, null, null);
                        if (cursor != null) {
                            cursor.moveToFirst();
                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            imagePathList.add(cursor.getString(columnIndex));
                            cursor.close();
                        }
                    }
                    for (int i = 0; i < imagePathList.size(); i++) {
                        if (selectedImage1.getVisibility() != View.VISIBLE) {
                            //image1에
                            Log.d("what111", "진입1111");
                            Picasso.with(boardModifyContext).load(new File(imagePathList.get(0))).into(selectedImage1);
                            selectedImage1.setVisibility(View.VISIBLE);
                            frameLayoutImg1.setVisibility(View.VISIBLE);
                        }
                        if (selectedImage2.getVisibility() != View.VISIBLE) {
                            //image2에
                            Log.d("what111", "진입33333");
                            Picasso.with(boardModifyContext).load(new File(imagePathList.get(1))).into(selectedImage2);
                            selectedImage2.setVisibility(View.VISIBLE);
                            frameLayoutImg2.setVisibility(View.VISIBLE);
                        }
                        if (selectedImage3.getVisibility() != View.VISIBLE) {
                            //image3에
                            Log.d("what111", "진입44444");
                            Picasso.with(boardModifyContext).load(new File(imagePathList.get(2))).into(selectedImage3);
                            selectedImage3.setVisibility(View.VISIBLE);
                            frameLayoutImg3.setVisibility(View.VISIBLE);
                        }
                        if (selectedImage4.getVisibility() != View.VISIBLE) {
                            //image4에
                            Log.d("what111", "진입5555");
                            Picasso.with(boardModifyContext).load(new File(imagePathList.get(3))).into(selectedImage4);
                            selectedImage4.setVisibility(View.VISIBLE);
                            frameLayoutImg4.setVisibility(View.VISIBLE);
                        }
                        if (selectedImage5.getVisibility() != View.VISIBLE) {
                            //image5에
                            Log.d("what111", "진입66666");
                            Picasso.with(boardModifyContext).load(new File(imagePathList.get(4))).into(selectedImage5);
                            selectedImage5.setVisibility(View.VISIBLE);
                            frameLayoutImg5.setVisibility(View.VISIBLE);
                        }
                    }
                }
            } catch (Exception e) {
                Log.d("myerror", e.toString());
            }

        }
    }

    public void btnWriteComplete(View v) {
        new AsyncTask<Void, Integer, Integer>() {
            CustomAnimationDialog podoLoading = new CustomAnimationDialog(BoardModifyActivity.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                podoLoading.show();
            }

            @Override
            protected Integer doInBackground(Void... voids) {
                checkNullBlank();
                if (!nullCheckState.getIsValidate()) {
                    return -3;
                }

                return Connect2Server.modifyBoard(boardId, imagePathList, category, write_title.getText().toString(), write_price.getText().toString(), write_content.getText().toString()
                        , boardForDetail.getCurrentImage1(), boardForDetail.getCurrentImage2(), boardForDetail.getCurrentImage3(), boardForDetail.getCurrentImage4(), boardForDetail.getCurrentImage5());
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected void onPostExecute(Integer result) {
                super.onPostExecute(result);
                podoLoading.dismiss();
                if (result == 1) {
                    Log.d("mywrite", "글쓰기 성공");
                    Intent intent = new Intent(boardModifyContext, MotherActivity.class);
                    startActivity(intent);
                } else if (result == -3) {
                    Toast.makeText(boardModifyContext,nullCheckState.getMessage(),Toast.LENGTH_LONG).show();
                } else {
                    Log.d("mywrite", "글쓰기 실패");
                    Toast.makeText(boardModifyContext, "글쓰기에 실패했습니다", Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }

    private void startPermissionsActivity(String[] permission) {
        PermissionsActivity.startActivityForResult(this, 0, permission);
    }

    private NullCheckState checkNullBlank() {

        if (TextUtils.isEmpty(write_title.getText()) || write_title.getText().toString().equals("")) {
            nullCheckState.setIsValidate(false);
            nullCheckState.setMessage("제목을 입력하세요.");
            return nullCheckState;
        } else if (TextUtils.isEmpty(write_content.getText()) || write_content.getText().toString().equals("")) {
            nullCheckState.setIsValidate(false);
            nullCheckState.setMessage("내용을 입력하세요.");
            return nullCheckState;
        } else if (TextUtils.isEmpty(write_price.getText()) || write_price.getText().toString().equals("")) {
            nullCheckState.setIsValidate(false);
            nullCheckState.setMessage("가격을 입력하세요.");
            return nullCheckState;
        }
        nullCheckState.setIsValidate(true);
        nullCheckState.setMessage("ok");
        return nullCheckState;
    }
}
