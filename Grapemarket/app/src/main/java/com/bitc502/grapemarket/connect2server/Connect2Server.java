package com.bitc502.grapemarket.connect2server;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.bitc502.grapemarket.model.Board;
import com.bitc502.grapemarket.model.BoardForDetail;
import com.bitc502.grapemarket.model.BoardForList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Connect2Server {
    private static String ip_address = "http://192.168.43.40:8080";

    private static final String LOGIN = ip_address + "/android/login";
    private static final String JOIN = ip_address + "/android/join";
    private static final String All_LIST = ip_address + "/android/allList";
    private static final String DETAIL = ip_address + "/android/detail/";

    //Login
    public static Integer sendLoginInfoToServer(String username, String password) {
        return 1;
    }

    //Join
    public static Integer sendJoinInfoToServer(String sourceImageFile, String username, String password,
                                               String name, String email, String phone) {
        try {
            File sourceFile = new File(sourceImageFile);
            final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
            String fileName = sourceImageFile.substring(sourceImageFile.lastIndexOf("/") + 1);

            //OKHTTP3
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("userProfile", fileName, RequestBody.create(MEDIA_TYPE_PNG, sourceFile))
                    .addFormDataPart("username", username)
                    .addFormDataPart("password", password)
                    .addFormDataPart("name", name)
                    .addFormDataPart("email", email)
                    .addFormDataPart("phone", phone)
                    .build();

            Request request = new Request.Builder()
                    .url(JOIN)
                    .post(requestBody)
                    .build();

            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();
            String res = response.body().string();

            if (res.equals("success")) {
                return 1;
            } else {
                return -1;
            }

        } catch (Exception e) {
            Log.d("myerror", e.toString());
            return -1;
        }
    }

    //Detail
    public static BoardForDetail requestDetailBoard(int id) {
        //OKHTTP3
        try {
            Request request = new Request.Builder()
                    .url(DETAIL + id)
                    .get()
                    .build();
            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            Board board = new Gson().fromJson(res, Board.class);
            BoardForDetail boardForDetail = new BoardForDetail();
            boardForDetail.setId(board.getId());
            boardForDetail.setAddressRange(board.getAddressRange());
            boardForDetail.setCategory(board.getCategory());
            boardForDetail.setComment(board.getComment());
            boardForDetail.setContent(board.getContent());
            boardForDetail.setCreateDate(board.getCreateDate());
            boardForDetail.setLike(board.getLike());
            boardForDetail.setPrice(board.getPrice());
            boardForDetail.setUser(board.getUser());
            boardForDetail.setUpdateDate(board.getUpdateDate());
            boardForDetail.setTitle(board.getTitle());
            boardForDetail.setState(board.getState());

            //이미지 세팅
            //이미지 땡겨오기 위한 HttpURLConnection
            Bitmap bitmap = null;
            try {



                for (int i = 0; i < 5; i++) {
                    if(i == 0){
                        URL urlForGetImage = new URL("http://192.168.43.40:8080/upload/" + board.getUser().getUserProfile());
                        HttpURLConnection conn = (HttpURLConnection) urlForGetImage.openConnection();
                        conn.setDoInput(true);
                        conn.connect();
                        InputStream inputStream = conn.getInputStream();
                        bitmap = BitmapFactory.decodeStream(inputStream);
                        boardForDetail.setUserProfile(bitmap);
                    }
                    if (i == 0) {
                        if (!board.getImage1().equals("") || !board.getImage1().isEmpty()) {
                            URL urlForGetImage = new URL("http://192.168.43.40:8080/upload/" + board.getImage1());
                            HttpURLConnection conn = (HttpURLConnection) urlForGetImage.openConnection();
                            conn.setDoInput(true);
                            conn.connect();
                            InputStream inputStream = conn.getInputStream();
                            bitmap = BitmapFactory.decodeStream(inputStream);
                            boardForDetail.getImages().add(bitmap);
                        }
                    } else if (i == 1) {
                        if (!board.getImage2().equals("") || !board.getImage2().isEmpty()) {
                            URL urlForGetImage = new URL("http://192.168.43.40:8080/upload/" + board.getImage2());
                            HttpURLConnection conn = (HttpURLConnection) urlForGetImage.openConnection();
                            conn.setDoInput(true);
                            conn.connect();

                            InputStream inputStream = conn.getInputStream();
                            bitmap = BitmapFactory.decodeStream(inputStream);
                            boardForDetail.getImages().add(bitmap);
                        }
                    } else if (i == 2) {
                        if (!board.getImage3().equals("") || !board.getImage3().isEmpty()) {
                            URL urlForGetImage = new URL("http://192.168.43.40:8080/upload/" + board.getImage3());
                            HttpURLConnection conn = (HttpURLConnection) urlForGetImage.openConnection();
                            conn.setDoInput(true);
                            conn.connect();

                            InputStream inputStream = conn.getInputStream();
                            bitmap = BitmapFactory.decodeStream(inputStream);
                            boardForDetail.getImages().add(bitmap);
                        }
                    } else if (i == 3) {
                        if (!board.getImage4().equals("") || !board.getImage4().isEmpty()) {
                            URL urlForGetImage = new URL("http://192.168.43.40:8080/upload/" + board.getImage4());
                            HttpURLConnection conn = (HttpURLConnection) urlForGetImage.openConnection();
                            conn.setDoInput(true);
                            conn.connect();

                            InputStream inputStream = conn.getInputStream();
                            bitmap = BitmapFactory.decodeStream(inputStream);
                            boardForDetail.getImages().add(bitmap);
                        }
                    } else if (i == 4) {
                        if (!board.getImage5().equals("") || !board.getImage5().isEmpty()) {
                            URL urlForGetImage = new URL("http://192.168.43.40:8080/upload/" + board.getImage5());
                            HttpURLConnection conn = (HttpURLConnection) urlForGetImage.openConnection();
                            conn.setDoInput(true);
                            conn.connect();

                            InputStream inputStream = conn.getInputStream();
                            bitmap = BitmapFactory.decodeStream(inputStream);
                            boardForDetail.getImages().add(bitmap);
                        }
                    }
                }



            } catch (Exception e) {
                Log.d("myerror", e.toString());
            }

            return boardForDetail;
        } catch (Exception e) {
            Log.d("myerror", e.toString());
            return null;
        }
    }

    //All List
    public static List<BoardForList> requestAllBoard() {
        //OKHTTP3
        try {
            Request request = new Request.Builder()
                    .url(All_LIST)
                    .get()
                    .build();

            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            Log.d("myerror", res);
            Type collectionType = new TypeToken<List<Board>>() {
            }.getType();
            List<Board> boards = new Gson().fromJson(res, collectionType);

            //////
            List<BoardForList> boardForLists = new ArrayList<>();
            for (int i = 0; i < boards.size(); i++) {
                //boardList.add(new Board(R.mipmap.ic_launcher,"제목","위치","유저네임","가격"));
                BoardForList boardForList = new BoardForList();
                boardForList.setAddressRange(boards.get(i).getAddressRange());
                boardForList.setCategory(boards.get(i).getCategory());
                boardForList.setContent(boards.get(i).getContent());
                boardForList.setId(boards.get(i).getId());
                boardForList.setCreateDate(boards.get(i).getCreateDate());
                boardForList.setUpdateDate(boards.get(i).getUpdateDate());
                boardForList.setPrice(boards.get(i).getPrice());
                boardForList.setState(boards.get(i).getState());
                boardForList.setTitle(boards.get(i).getTitle());
                boardForList.setComment(boards.get(i).getComment());
                boardForList.setLike(boards.get(i).getLike());
                boardForList.setUser(boards.get(i).getUser());

                //이미지 세팅
                //이미지 땡겨오기 위한 HttpURLConnection
                Bitmap bitmap = null;
                try {
                    URL urlForGetImage = new URL("http://192.168.43.40:8080/upload/" + boards.get(i).getImage1());
                    HttpURLConnection conn = (HttpURLConnection) urlForGetImage.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream inputStream = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    boardForList.setImage1(bitmap);
                } catch (Exception e) {
                    Log.d("myerror", e.toString());
                }
                boardForLists.add(boardForList);
            }

            ////////////
            return boardForLists;
        } catch (Exception e) {
            Log.d("myerror", e.toString());
            return null;
        }
    }
}
