package com.bitc502.grapemarket.connect2server;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;


import com.bitc502.grapemarket.model.Board;
import com.bitc502.grapemarket.model.BoardForDetail;
import com.bitc502.grapemarket.model.BoardForList;
import com.bitc502.grapemarket.model.CommentForDetail;
import com.bitc502.grapemarket.model.CurrentUserInfo;
import com.bitc502.grapemarket.model.UserLocationSetting;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.bitc502.grapemarket.model.User;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.TlsVersion;

public class Connect2Server {
    private static String ip_address = "https://192.168.43.40:8443";
    private static final String LOGIN = ip_address + "/user/loginProc";
    private static final String LOGIN_USER_INFO = ip_address + "/android/getUserInfo";
    private static final String JOIN = ip_address + "/android/join";
    private static final String All_LIST = ip_address + "/android/allList";
    private static final String DETAIL = ip_address + "/android/detail/";
    private static final String WRITE = ip_address + "/android/write";
    private static final String COMMENT_WRITE = ip_address + "/android/commentWrite";
    private static final String USER_ADDRESS_SETTING = ip_address + "/android/saveUserAddress";
    private static final String GET_SAVED_ADDRESS = ip_address + "/android/getSavedAddress";
    private static CurrentUserInfo currentUserInfo = CurrentUserInfo.getInstance();


    //Login
    public static Boolean sendLoginInfoToServer(String username, String password) {
        try {
            //OKHTTP3
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("username", username)
                    .addFormDataPart("password", password)
                    .build();

            Request request = new Request.Builder()
                    .url(LOGIN)
                    .addHeader("DeviceType", "android")
                    .post(requestBody)
                    .build();

            //OkHttpClient client = new OkHttpClient();
            OkHttpClient client = getUnsafeOkHttpClient();
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            String jsessionid = getJsessionId(response.header("Set-Cookie"));
            if (res.equals("ok")) {
                currentUserInfo.setJSessionId(jsessionid);
                return true;
            } else {
                Log.d("myerror", res + "            <<<<<<<<<<<<<<로그인sdfsaf 실패");
                return false;
            }
        } catch (Exception e) {
            Log.d("myerror", e.toString());
            return false;
        }
    }

    public static User getLoginUserInfo(String username) {
        try {
            //OKHTTP3
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("username", username)
                    .build();

            Request request = new Request.Builder()
                    .url(LOGIN_USER_INFO)
                    .post(requestBody)
                    .build();

            OkHttpClient client = getUnsafeOkHttpClient();
            ;
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            User user = new Gson().fromJson(res, User.class);
            return user;
        } catch (Exception e) {
            Log.d("myerror", e.toString());
            return null;
        }
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

            OkHttpClient client = getUnsafeOkHttpClient();
            ;
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
                    .addHeader("Cookie",currentUserInfo.getJSessionId())
                    .url(DETAIL + id)
                    .get()
                    .build();
            OkHttpClient client = getUnsafeOkHttpClient();
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            Log.d("myerror", "requestDetailBoard: >>>" + res);
            Board board = new Gson().fromJson(res, Board.class);

            BoardForDetail boardForDetail = new BoardForDetail();
            boardForDetail.setId(board.getId());
            boardForDetail.setAddressRange(board.getAddressRange());
            boardForDetail.setCategory(board.getCategory());
            //boardForDetail.setComment(board.getComment());

            boardForDetail.setContent(board.getContent());
            boardForDetail.setCreateDate(board.getCreateDate());
            boardForDetail.setLike(board.getLike());
            boardForDetail.setPrice(board.getPrice());
            boardForDetail.setUser(board.getUser());
            boardForDetail.setUpdateDate(board.getUpdateDate());
            boardForDetail.setTitle(board.getTitle());
            boardForDetail.setState(board.getState());

            //이미지 세팅(작성자 프로필 사진 및 상품 이미지)
            //이미지 땡겨오기 위한 HttpURLConnection
            Bitmap bitmap = null;
            for (int i = 0; i < 5; i++) {
                if (i == 0) { // 작성자 유저프로필 땡겨오기
                    //OKHTTP3
                    Request requestForImage = new Request.Builder()
                            .addHeader("Cookie",currentUserInfo.getJSessionId())
                            .url("https://192.168.43.40:8443/upload/" + board.getUser().getUserProfile())
                            .get()
                            .build();
                    OkHttpClient clientForImage = getUnsafeOkHttpClient();
                    Response responseForImage = clientForImage.newCall(requestForImage).execute();
                    InputStream inputStream = responseForImage.body().byteStream();
                    Log.d("ttt111", inputStream.toString());
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    boardForDetail.setUserProfile(bitmap);

                }

                if (i == 0) {
                    if (!TextUtils.isEmpty(board.getImage1())) {
                        //OKHTTP3
                        Request requestForImage = new Request.Builder()
                                .addHeader("Cookie",currentUserInfo.getJSessionId())
                                .url("https://192.168.43.40:8443/upload/" + board.getImage1())
                                .get()
                                .build();
                        OkHttpClient clientForImage = getUnsafeOkHttpClient();
                        Response responseForImage = clientForImage.newCall(requestForImage).execute();
                        InputStream inputStream = responseForImage.body().byteStream();
                        Log.d("ttt111", inputStream.toString());
                        bitmap = BitmapFactory.decodeStream(inputStream);
                        boardForDetail.getImages().add(bitmap);
                    }
                } else if (i == 1) {
                    if (!TextUtils.isEmpty(board.getImage2())) {
                        //OKHTTP3
                        Request requestForImage = new Request.Builder()
                                .addHeader("Cookie",currentUserInfo.getJSessionId())
                                .url("https://192.168.43.40:8443/upload/" + board.getImage2())
                                .get()
                                .build();
                        OkHttpClient clientForImage = getUnsafeOkHttpClient();
                        Response responseForImage = clientForImage.newCall(requestForImage).execute();
                        InputStream inputStream = responseForImage.body().byteStream();
                        Log.d("ttt111", inputStream.toString());
                        bitmap = BitmapFactory.decodeStream(inputStream);
                        boardForDetail.getImages().add(bitmap);
                    }
                } else if (i == 2) {
                    if (!TextUtils.isEmpty(board.getImage3())) {
                        //OKHTTP3
                        Request requestForImage = new Request.Builder()
                                .addHeader("Cookie",currentUserInfo.getJSessionId())
                                .url("https://192.168.43.40:8443/upload/" + board.getImage3())
                                .get()
                                .build();
                        OkHttpClient clientForImage = getUnsafeOkHttpClient();
                        Response responseForImage = clientForImage.newCall(requestForImage).execute();
                        InputStream inputStream = responseForImage.body().byteStream();
                        Log.d("ttt111", inputStream.toString());
                        bitmap = BitmapFactory.decodeStream(inputStream);
                        boardForDetail.getImages().add(bitmap);
                    }
                } else if (i == 3) {
                    if (!TextUtils.isEmpty(board.getImage4())) {
                        //OKHTTP3
                        Request requestForImage = new Request.Builder()
                                .addHeader("Cookie",currentUserInfo.getJSessionId())
                                .url("https://192.168.43.40:8443/upload/" + board.getImage4())
                                .get()
                                .build();
                        OkHttpClient clientForImage = getUnsafeOkHttpClient();
                        Response responseForImage = clientForImage.newCall(requestForImage).execute();
                        InputStream inputStream = responseForImage.body().byteStream();
                        Log.d("ttt111", inputStream.toString());
                        bitmap = BitmapFactory.decodeStream(inputStream);
                        boardForDetail.getImages().add(bitmap);


                    }
                } else if (i == 4) {
                    if (!TextUtils.isEmpty(board.getImage5())) {
                        //OKHTTP3
                        Request requestForImage = new Request.Builder()
                                .addHeader("Cookie",currentUserInfo.getJSessionId())
                                .url("https://192.168.43.40:8443/upload/" + board.getImage5())
                                .get()
                                .build();
                        OkHttpClient clientForImage = getUnsafeOkHttpClient();
                        Response responseForImage = clientForImage.newCall(requestForImage).execute();
                        InputStream inputStream = responseForImage.body().byteStream();
                        Log.d("ttt111", inputStream.toString());
                        bitmap = BitmapFactory.decodeStream(inputStream);
                        boardForDetail.getImages().add(bitmap);
                    }
                }
            }

            //댓글 유저 프로필사진 세팅
            //boardForDetail.setComment(new ArrayList<CommentForDetail>());
            List<CommentForDetail> commentForDetails = new ArrayList<>();

            for (int i = 0; i < board.getComment().size(); i++) {
                //OKHTTP3
                Request requestForImage = new Request.Builder()
                        .addHeader("Cookie",currentUserInfo.getJSessionId())
                        .url("https://192.168.43.40:8443/upload/" + board.getComment().get(i).getUser().getUserProfile())
                        .get()
                        .build();
                OkHttpClient clientForImage = getUnsafeOkHttpClient();
                Response responseForImage = clientForImage.newCall(requestForImage).execute();
                InputStream inputStream = responseForImage.body().byteStream();
                Log.d("ttt111", inputStream.toString());
                bitmap = BitmapFactory.decodeStream(inputStream);

                CommentForDetail commentForDetail = new CommentForDetail();
                commentForDetail.setUser(board.getComment().get(i).getUser());
                commentForDetail.setBoard(board.getComment().get(i).getBoard());
                commentForDetail.setId(board.getComment().get(i).getId());
                commentForDetail.setUpdateDate(board.getComment().get(i).getUpdateDate());
                commentForDetail.setCreateDate(board.getComment().get(i).getCreateDate());
                commentForDetail.setContent(board.getComment().get(i).getContent());
                commentForDetail.setUserProfile(bitmap);
                commentForDetails.add(commentForDetail);
            }
            boardForDetail.setComment(commentForDetails);
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
                    .addHeader("Cookie",currentUserInfo.getJSessionId())
                    .url(All_LIST)
                    .get()
                    .build();

            OkHttpClient client = getUnsafeOkHttpClient();
            Response response = client.newCall(request).execute();
            String res = response.body().string();
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
                //이미지 땡겨오기 위한 OKHTTP3 접속
                Bitmap bitmap = null;
                try {
                    //OKHTTP3
                    Request requestForImage = new Request.Builder()
                            .addHeader("Cookie",currentUserInfo.getJSessionId())
                            .url("https://192.168.43.40:8443/upload/" + boards.get(i).getImage1())
                            .get()
                            .build();
                    OkHttpClient clientForImage = getUnsafeOkHttpClient();
                    Response responseForImage = clientForImage.newCall(requestForImage).execute();
                    InputStream inputStream = responseForImage.body().byteStream();
                    Log.d("ttt111", inputStream.toString());
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

    public static Integer sendWriteInfoToServer(List<String> imagePathList, String categoryString, String title, String price, String content) {
        try {
            int category = 0;
            //카테고리 정보 숫자로 세팅
            if (categoryString.equals("디지털/가전")) {
                category = 3;
            } else if (categoryString.equals("가구/인테리어")) {
                category = 4;
            } else if (categoryString.equals("유아동/유아도서")) {
                category = 5;
            } else if (categoryString.equals("생활/가공식품")) {
                category = 6;
            } else if (categoryString.equals("여성의류")) {
                category = 7;
            } else if (categoryString.equals("여성잡화")) {
                category = 8;
            } else if (categoryString.equals("뷰티/미용")) {
                category = 9;
            } else if (categoryString.equals("남성패션/잡화")) {
                category = 10;
            } else if (categoryString.equals("스포츠/레저")) {
                category = 11;
            } else if (categoryString.equals("게임/취미")) {
                category = 12;
            } else if (categoryString.equals("도서/티켓/음반")) {
                category = 13;
            } else if (categoryString.equals("반려동물용품")) {
                category = 14;
            } else if (categoryString.equals("기타 중고물품")) {
                category = 15;
            } else if (categoryString.equals("삽니다")) {
                category = 16;
            }

            final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");

            File sourceFile1 = new File("");
            File sourceFile2 = new File("");
            File sourceFile3 = new File("");
            File sourceFile4 = new File("");
            File sourceFile5 = new File("");
            String fileName1 = "";
            String fileName2 = "";
            String fileName3 = "";
            String fileName4 = "";
            String fileName5 = "";

            Log.d("mywrite", imagePathList.size() + "");
            Log.d("mywrite", imagePathList.get(0));
            for (int i = 0; i < imagePathList.size(); i++) {
                if (i == 0) {
                    sourceFile1 = new File(imagePathList.get(i));
                    fileName1 = imagePathList.get(i).substring(imagePathList.get(i).lastIndexOf("/") + 1);
                } else if (i == 1) {
                    sourceFile2 = new File(imagePathList.get(i));
                    fileName2 = imagePathList.get(i).substring(imagePathList.get(i).lastIndexOf("/") + 1);
                } else if (i == 2) {
                    sourceFile3 = new File(imagePathList.get(i));
                    fileName3 = imagePathList.get(i).substring(imagePathList.get(i).lastIndexOf("/") + 1);
                } else if (i == 3) {
                    sourceFile4 = new File(imagePathList.get(i));
                    fileName4 = imagePathList.get(i).substring(imagePathList.get(i).lastIndexOf("/") + 1);
                } else if (i == 4) {
                    sourceFile5 = new File(imagePathList.get(i));
                    fileName5 = imagePathList.get(i).substring(imagePathList.get(i).lastIndexOf("/") + 1);
                }
            }
            //OKHTTP3
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("productImage1", fileName1, RequestBody.create(MEDIA_TYPE_PNG, sourceFile1))
                    .addFormDataPart("productImage2", fileName2, RequestBody.create(MEDIA_TYPE_PNG, sourceFile2))
                    .addFormDataPart("productImage3", fileName3, RequestBody.create(MEDIA_TYPE_PNG, sourceFile3))
                    .addFormDataPart("productImage4", fileName4, RequestBody.create(MEDIA_TYPE_PNG, sourceFile4))
                    .addFormDataPart("productImage5", fileName5, RequestBody.create(MEDIA_TYPE_PNG, sourceFile5))
                    .addFormDataPart("category", category + "")
                    .addFormDataPart("state", "0")
                    .addFormDataPart("title", title)
                    .addFormDataPart("price", price)
                    .addFormDataPart("content", content)
                    .build();

            Request request = new Request.Builder()
                    .addHeader("Cookie",currentUserInfo.getJSessionId())
                    .url(WRITE)
                    .post(requestBody)
                    .build();
            OkHttpClient client = getUnsafeOkHttpClient();
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            if (res.equals("success")) {
                return 1;
            } else {
                return -1;
            }
        } catch (Exception e) {
            Log.d("mywrite", e.toString());
            return -1;
        }
    }

    //comment 쓰기
    public static Boolean commentWrite(String content, String boardId) {
        try {
            //OKHTTP3
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("content", content)
                    .addFormDataPart("board", boardId)
                    .build();

            Request request = new Request.Builder()
                    .addHeader("Cookie",currentUserInfo.getJSessionId())
                    .url(COMMENT_WRITE)
                    .post(requestBody)
                    .build();

            OkHttpClient client = getUnsafeOkHttpClient();
            Response response = client.newCall(request).execute();
            String res = response.body().string();

            if (res.equals("success")) {
                Log.d("myerror", "코멘트 성공");
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            Log.d("myerror", e.toString());
            return false;
        }
    }

    //동네설정 저장
    public static Boolean saveUserAddressSetting(String address,String addressX,String addressY,String currentUserId){
        try{
            //OKHTTP3
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("address",address)
                    .addFormDataPart("addressX",addressX)
                    .addFormDataPart("addressY",addressY)
                    .build();

            Request request = new Request.Builder()
                    .addHeader("Cookie",currentUserInfo.getJSessionId())
                    .url(USER_ADDRESS_SETTING)
                    .post(requestBody)
                    .build();

            OkHttpClient client = getUnsafeOkHttpClient();
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            if(res.equals("success")) {
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            Log.d("myerror", e.toString());
            return false;
        }
    }

    //저장된 주소 정보 가져오기
    public static UserLocationSetting getSavedAddress(String currentUserId){
        try{
            //OKHTTP3
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("currentUserId",currentUserId)
                    .build();

            Request request = new Request.Builder()
                    .addHeader("Cookie",currentUserInfo.getJSessionId())
                    .url(GET_SAVED_ADDRESS)
                    .post(requestBody)
                    .build();

            OkHttpClient client = getUnsafeOkHttpClient();
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            UserLocationSetting userLocationSetting = new Gson().fromJson(res,UserLocationSetting.class);
            return userLocationSetting;
        }catch (Exception e){
            Log.d("myerror", e.toString());
            return null;
        }
    }

    //인증서 무시
    public static OkHttpClient getUnsafeOkHttpClient() {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            //Using TLS 1_2 & 1_1 for HTTP/2 Server requests
            // Note : The following is suitable for my Server. Please change accordingly
            ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.COMPATIBLE_TLS)
                    .tlsVersions(TlsVersion.TLS_1_2, TlsVersion.TLS_1_1, TlsVersion.TLS_1_0)
                    .cipherSuites(
                            CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA,
                            CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384,
                            CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                            CipherSuite.TLS_DHE_RSA_WITH_AES_256_GCM_SHA384,
                            CipherSuite.TLS_DHE_RSA_WITH_AES_256_CBC_SHA,
                            CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384,
                            CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256,
                            CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                            CipherSuite.TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256,
                            CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA,
                            CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA)
                    .build();

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory);
            builder.connectionSpecs(Collections.singletonList(spec));
            builder.hostnameVerifier((hostname, session) -> true);
            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getJsessionId(String rawJsessionId){
        String jSessionId = "";
        int rawJsessionIdSize = rawJsessionId.length();
        for(int i = 0; i < rawJsessionIdSize; i++) {
            if(rawJsessionId.charAt(i) == ';'){
                break;
            }
            jSessionId += rawJsessionId.charAt(i);
        }
        Log.d("jsessiontest", "파싱한 >> "+jSessionId);
        return jSessionId;
    }
}
