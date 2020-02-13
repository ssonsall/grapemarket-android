package com.bitc502.grapemarket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bitc502.grapemarket.connect2server.Connect2Server;
import com.bitc502.grapemarket.dialog.CustomAnimationDialog;
import com.bitc502.grapemarket.dialog.CustomGoAddressDialog;
import com.bitc502.grapemarket.permission.PermissionsActivity;
import com.bitc502.grapemarket.permission.PermissionsChecker;
import com.bitc502.grapemarket.singleton.Session;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WriteFragment extends Fragment {
    private static final String[] PERMISSIONS_READ_STORAGE = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final String[] PERMISSIONS_WRITE_STORAGE = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private PermissionsChecker checker;
    private boolean permissionRead, permissionWrite;
    private ImageView selectedImage1, selectedImage2, selectedImage3, selectedImage4, selectedImage5;
    private EditText write_title, write_price, write_content;
    private List<String> imagePathList;
    private Context writeContext;
    private Spinner write_category;
    private String category;
    private TextView rangeSet,btnGoAddressSetting;
    private ArrayAdapter spinnerAdpater;
    private FrameLayout frameLayoutImg1, frameLayoutImg2, frameLayoutImg3, frameLayoutImg4, frameLayoutImg5;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_write, container, false);
        writeContext = getContext();



        if(Session.currentUserInfo.getUser().getAddressAuth() != 1){
            if(TextUtils.isEmpty(Session.currentUserInfo.getUser().getAddress()) ||Session.currentUserInfo.getUser().getAddress().equals("")) {
                String message = "주소설정 후 인증이 필요합니다.";
                CustomGoAddressDialog customGoAddressDialog = new CustomGoAddressDialog(writeContext, message, new CustomGoAddressDialog.CustomGoAddressDialogListener() {
                    @Override
                    public void clickConfirm() {
                        Intent intent = new Intent(writeContext, MyLocationSetting.class);
                        startActivity(intent);
                    }

                    @Override
                    public void clickCancel() {

                    }
                });
                customGoAddressDialog.show();

            }else {
                String message = "주소인증이 필요합니다.";
                CustomGoAddressDialog customGoAddressDialog = new CustomGoAddressDialog(writeContext, message, new CustomGoAddressDialog.CustomGoAddressDialogListener() {
                    @Override
                    public void clickConfirm() {
                        Intent intent = new Intent(writeContext, MyLocationAuthActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void clickCancel() {

                    }
                });
                customGoAddressDialog.show();
            }

        }

        checker = new PermissionsChecker(writeContext);
        permissionRead = false;
        permissionWrite = false;

        rangeSet = getActivity().findViewById(R.id.toolbar_range_set);
        rangeSet.setVisibility(View.INVISIBLE);

        frameLayoutImg1 = v.findViewById(R.id.write_selectedImg1_layout);
        frameLayoutImg1.setVisibility(View.INVISIBLE);
        frameLayoutImg2 = v.findViewById(R.id.write_selectedImg2_layout);
        frameLayoutImg2.setVisibility(View.INVISIBLE);
        frameLayoutImg3 = v.findViewById(R.id.write_selectedImg3_layout);
        frameLayoutImg3.setVisibility(View.INVISIBLE);
        frameLayoutImg4 = v.findViewById(R.id.write_selectedImg4_layout);
        frameLayoutImg4.setVisibility(View.INVISIBLE);
        frameLayoutImg5 = v.findViewById(R.id.write_selectedImg5_layout);
        frameLayoutImg5.setVisibility(View.INVISIBLE);

        selectedImage1 = v.findViewById(R.id.wirte_selectedImg1);
        selectedImage2 = v.findViewById(R.id.wirte_selectedImg2);
        selectedImage3 = v.findViewById(R.id.wirte_selectedImg3);
        selectedImage4 = v.findViewById(R.id.wirte_selectedImg4);
        selectedImage5 = v.findViewById(R.id.wirte_selectedImg5);

        selectedImage1.setVisibility(View.INVISIBLE);
        selectedImage2.setVisibility(View.INVISIBLE);
        selectedImage3.setVisibility(View.INVISIBLE);
        selectedImage4.setVisibility(View.INVISIBLE);
        selectedImage5.setVisibility(View.INVISIBLE);

        btnGoAddressSetting = getActivity().findViewById(R.id.toolbar_go_address_setting);

        imagePathList = new ArrayList<>();

        write_category = v.findViewById(R.id.write_category);
        write_title = v.findViewById(R.id.write_title);
        write_price = v.findViewById(R.id.write_price);
        write_content = v.findViewById(R.id.write_content);

        spinnerAdpater = ArrayAdapter.createFromResource(getContext(), R.array.write_category, R.layout.spinner_dialog_layout);
        spinnerAdpater.setDropDownViewResource(R.layout.spinner_text_setting);
        write_category.setAdapter(spinnerAdpater);

        if(TextUtils.isEmpty(Session.currentUserInfo.getUser().getAddress()) ||Session.currentUserInfo.getUser().getAddress().equals("")){
            btnGoAddressSetting.setVisibility(View.VISIBLE);
            rangeSet.setVisibility(View.GONE);
        }else{
            rangeSet.setVisibility(View.VISIBLE);
            btnGoAddressSetting.setVisibility(View.GONE);
        }


        write_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = (String) adapterView.getItemAtPosition(i);
                Log.d("spintest", category);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return v;
    }

    public void btnGoAddressSetting(View v){
        Intent intent = new Intent(writeContext, MyLocationSetting.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(Session.currentUserInfo.getUser().getAddressAuth() != 1){
            if(TextUtils.isEmpty(Session.currentUserInfo.getUser().getAddress()) ||Session.currentUserInfo.getUser().getAddress().equals("")) {
                String message = "주소설정 후 인증이 필요합니다.";
                CustomGoAddressDialog customGoAddressDialog = new CustomGoAddressDialog(writeContext, message, new CustomGoAddressDialog.CustomGoAddressDialogListener() {
                    @Override
                    public void clickConfirm() {
                        Intent intent = new Intent(writeContext, MyLocationSetting.class);
                        startActivity(intent);
                    }

                    @Override
                    public void clickCancel() {
                        Intent intent = new Intent(writeContext, MotherActivity.class);
                        startActivity(intent);
                    }
                });
                customGoAddressDialog.show();

            }else {
                String message = "주소인증이 필요합니다.";
                CustomGoAddressDialog customGoAddressDialog = new CustomGoAddressDialog(writeContext, message, new CustomGoAddressDialog.CustomGoAddressDialogListener() {
                    @Override
                    public void clickConfirm() {
                        Intent intent = new Intent(writeContext, MyLocationAuthActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void clickCancel() {
                        Intent intent = new Intent(writeContext, MotherActivity.class);
                        startActivity(intent);
                    }
                });
                customGoAddressDialog.show();
            }

        }
    }

    //카테고리 옆 화살표 눌렀을때도 카테고리 스피너 뜨도록
    public void write_spinner_arrow_btn_clicked(View v) {
        write_category.performClick();
    }


    public void btnImgDelete1(View v) {
        //imagePathList 하나씩 땡기기 (remove 하면 자동으로 인덱스 정렬되나? 되면 꿀이고 안되면 개고생)
        //imageView 세팅 다시하기 그냥 하나씩 땡기는 거임
        if (imagePathList.size() == 2) {
            Picasso.with(writeContext).load(new File(imagePathList.get(1))).into(selectedImage1);
            frameLayoutImg2.setVisibility(View.INVISIBLE);
        } else if (imagePathList.size() == 3) {
            Picasso.with(writeContext).load(new File(imagePathList.get(1))).into(selectedImage1);
            Picasso.with(writeContext).load(new File(imagePathList.get(2))).into(selectedImage2);
            frameLayoutImg3.setVisibility(View.INVISIBLE);
        } else if (imagePathList.size() == 4) {
            Picasso.with(writeContext).load(new File(imagePathList.get(1))).into(selectedImage1);
            Picasso.with(writeContext).load(new File(imagePathList.get(2))).into(selectedImage2);
            Picasso.with(writeContext).load(new File(imagePathList.get(3))).into(selectedImage3);
            frameLayoutImg4.setVisibility(View.INVISIBLE);
        } else if (imagePathList.size() == 5) {
            Picasso.with(writeContext).load(new File(imagePathList.get(1))).into(selectedImage1);
            Picasso.with(writeContext).load(new File(imagePathList.get(2))).into(selectedImage2);
            Picasso.with(writeContext).load(new File(imagePathList.get(3))).into(selectedImage3);
            Picasso.with(writeContext).load(new File(imagePathList.get(4))).into(selectedImage4);
            frameLayoutImg5.setVisibility(View.INVISIBLE);
        } else if (imagePathList.size() == 1) {
            frameLayoutImg1.setVisibility(View.INVISIBLE);
        }
        imagePathList.remove(0);
    }

    public void btnImgDelete2(View v) {
        if (imagePathList.size() == 3) {
            Picasso.with(writeContext).load(new File(imagePathList.get(2))).into(selectedImage2);
            frameLayoutImg3.setVisibility(View.INVISIBLE);
        } else if (imagePathList.size() == 4) {
            Picasso.with(writeContext).load(new File(imagePathList.get(2))).into(selectedImage2);
            Picasso.with(writeContext).load(new File(imagePathList.get(3))).into(selectedImage3);
            frameLayoutImg4.setVisibility(View.INVISIBLE);
        } else if (imagePathList.size() == 5) {
            Picasso.with(writeContext).load(new File(imagePathList.get(2))).into(selectedImage2);
            Picasso.with(writeContext).load(new File(imagePathList.get(3))).into(selectedImage3);
            Picasso.with(writeContext).load(new File(imagePathList.get(4))).into(selectedImage4);
            frameLayoutImg5.setVisibility(View.INVISIBLE);
        } else if (imagePathList.size() == 2) {
            frameLayoutImg2.setVisibility(View.INVISIBLE);
        }
        imagePathList.remove(1);
    }

    public void btnImgDelete3(View v) {
        if (imagePathList.size() == 4) {
            Picasso.with(writeContext).load(new File(imagePathList.get(3))).into(selectedImage3);
            frameLayoutImg4.setVisibility(View.INVISIBLE);
        } else if (imagePathList.size() == 5) {
            Picasso.with(writeContext).load(new File(imagePathList.get(3))).into(selectedImage3);
            Picasso.with(writeContext).load(new File(imagePathList.get(4))).into(selectedImage4);
            frameLayoutImg5.setVisibility(View.INVISIBLE);
        } else if (imagePathList.size() == 3) {
            frameLayoutImg3.setVisibility(View.INVISIBLE);
        }
        imagePathList.remove(2);
    }

    public void btnImgDelete4(View v) {
        //Picasso.with(writeContext).load(new File(imagePathList.get(i))).into(selectedImage1);
        if (imagePathList.size() == 5) {
            Picasso.with(writeContext).load(new File(imagePathList.get(4))).into(selectedImage4);
            frameLayoutImg5.setVisibility(View.INVISIBLE);
        } else if (imagePathList.size() == 4) {
            frameLayoutImg4.setVisibility(View.INVISIBLE);
        }
        imagePathList.remove(3);
    }

    public void btnImgDelete5(View v) {
        imagePathList.remove(4);
        frameLayoutImg5.setVisibility(View.INVISIBLE);
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
        CustomAnimationDialog podoLoading = new CustomAnimationDialog(writeContext);
        if (resultCode == getActivity().RESULT_OK && requestCode == 1010) {
            try {
                podoLoading.show();
                if (data == null) {
                    podoLoading.dismiss();
                    return;
                }
                ClipData clipData = data.getClipData();
                int userSelectedImageCount = clipData.getItemCount();
                if (userSelectedImageCount > 5) {
                    Toast.makeText(writeContext, "사진은 5장까지만 선택해주세요.", Toast.LENGTH_LONG).show();
                } else if (userSelectedImageCount + imagePathList.size() > 5) {
                    Toast.makeText(writeContext, "사진은 5장까지만 선택해주세요.", Toast.LENGTH_LONG).show();
                } else {
                    List<Uri> imageUriList = new ArrayList<>();
                    for (int i = 0; i < userSelectedImageCount; i++) {
                        imageUriList.add(clipData.getItemAt(i).getUri());
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getActivity().getContentResolver().query(imageUriList.get(i), filePathColumn, null, null, null);
                        if (cursor != null) {
                            cursor.moveToFirst();
                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            imagePathList.add(cursor.getString(columnIndex));
                            cursor.close();
                        }
                    }
                    Log.d("myerror", "imagePathListSize >> " + imagePathList.size());
                    int imagePathListSize = imagePathList.size();
                    for (int i = 0; i < imagePathListSize; i++) {
                        if(imagePathListSize > 0 ) {
                            if (selectedImage1.getVisibility() != View.VISIBLE) {
                                //image1에
                                Picasso.with(writeContext).load(new File(imagePathList.get(0))).into(selectedImage1);
                                selectedImage1.setVisibility(View.VISIBLE);
                                frameLayoutImg1.setVisibility(View.VISIBLE);
                            }
                        }
                        if(imagePathListSize > 1 ) {
                            if (selectedImage2.getVisibility() != View.VISIBLE) {
                                //image2에
                                Picasso.with(writeContext).load(new File(imagePathList.get(1))).into(selectedImage2);
                                selectedImage2.setVisibility(View.VISIBLE);
                                frameLayoutImg2.setVisibility(View.VISIBLE);
                            }
                        }
                        if(imagePathListSize > 2 ) {
                            if (selectedImage3.getVisibility() != View.VISIBLE) {
                                //image3에
                                Picasso.with(writeContext).load(new File(imagePathList.get(2))).into(selectedImage3);
                                selectedImage3.setVisibility(View.VISIBLE);
                                frameLayoutImg3.setVisibility(View.VISIBLE);
                            }
                        }
                        if(imagePathListSize > 3 ) {
                            if (selectedImage4.getVisibility() != View.VISIBLE) {
                                //image4에
                                Picasso.with(writeContext).load(new File(imagePathList.get(3))).into(selectedImage4);
                                selectedImage4.setVisibility(View.VISIBLE);
                                frameLayoutImg4.setVisibility(View.VISIBLE);
                            }
                        }
                        if(imagePathListSize > 4 ) {
                            if (selectedImage5.getVisibility() != View.VISIBLE) {
                                //image5에
                                Picasso.with(writeContext).load(new File(imagePathList.get(4))).into(selectedImage5);
                                selectedImage5.setVisibility(View.VISIBLE);
                                frameLayoutImg5.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
                Log.d("myerror", "onActivityResult: 디스미스 위에");
                podoLoading.dismiss();
            } catch (Exception e) {
                Log.d("myerror", e.toString());
            }

        }
    }

    public void btnWriteComplete(View v) {
        new AsyncTask<Void, Integer, Integer>() {
            CustomAnimationDialog podoLoading = new CustomAnimationDialog(writeContext);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                podoLoading.show();
            }

            @Override
            protected Integer doInBackground(Void... voids) {
                return Connect2Server.sendWriteInfoToServer(imagePathList, category, write_title.getText().toString(), write_price.getText().toString(), write_content.getText().toString());
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
                    //ListFragment로 가는거는 그냥 간단히 MotherActivity 부르면 된다.
                    //왜냐면 리스트 프래그먼트가 첫 화면이기 때문
                    Intent intent = new Intent(writeContext, MotherActivity.class);
                    startActivity(intent);
                } else {
                    Log.d("mywrite", "글쓰기 실패");
                    Toast.makeText(writeContext, "글쓰기에 실패했습니다", Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }

    private void startPermissionsActivity(String[] permission) {
        PermissionsActivity.startActivityForResult(getActivity(), 0, permission);
    }
}
