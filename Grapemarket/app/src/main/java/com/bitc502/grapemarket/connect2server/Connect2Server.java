package com.bitc502.grapemarket.connect2server;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.bitc502.grapemarket.model.Likes;
import com.bitc502.grapemarket.singleton.Session;
import com.bitc502.grapemarket.model.Board;
import com.bitc502.grapemarket.model.BoardForDetail;
import com.bitc502.grapemarket.model.BoardForList;
import com.bitc502.grapemarket.model.Chat;
import com.bitc502.grapemarket.model.ChatList;
import com.bitc502.grapemarket.model.CommentForDetail;
import com.bitc502.grapemarket.model.CurrentUserInfoForProfile;
import com.bitc502.grapemarket.model.User;
import com.bitc502.grapemarket.model.UserLocationSetting;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

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
    private static final String ALL_LIST_PAGEABLE = ip_address + "/android/allListPageable";
    private static final String ALL_LIST_PAGEABLE_WITH_RANGE = ip_address + "/android/allListPageableWithRange";
    private static final String DETAIL = ip_address + "/android/detail/";
    private static final String WRITE = ip_address + "/android/write";
    private static final String COMMENT_WRITE = ip_address + "/android/commentWrite";
    private static final String USER_ADDRESS_SETTING = ip_address + "/android/saveUserAddress";
    private static final String GET_SAVED_ADDRESS = ip_address + "/android/getSavedAddress";
    private static final String SAVE_ADDRESS_AUTH = ip_address + "/android/saveAddressAuth";
    private static final String SEARCH_WITH_RANGE = ip_address + "/android/searchWithRange";
    private static final String CHAT_LIST = ip_address + "/android/chatList";
    private static final String CURRENT_MY_PROFILE = ip_address + "/android/currentmyinfo";
    private static final String CHANGE_PASSWORD = ip_address + "/android/changepassword";
    private static final String CHANGE_PROFILE = ip_address + "/android/changeprofile";
    private static final String DELETE_BOARD = ip_address + "/android/deleteBoard/";
    private static final String MODIFY_BOARD = ip_address + "/android/modifyBoard";
    private static final String REQUEST_CHATTING = ip_address + "/android/requestChat";
    private static final String DELETE_LIKE = ip_address + "/android/deleteLike";
    private static final String SAVE_LIKE = ip_address + "/android/saveLike";

    //Login
    public static Boolean sendLoginInfoToServer(String username, String password) {
        try {
            OkHttpClient client = getUnsafeOkHttpClient();
            //OKHTTP3
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("username", username)
                    .addFormDataPart("password", password)
                    .build();

            Request request = new Request.Builder()
                    .url(LOGIN)
                    .addHeader("DeviceType", "android")
                    .addHeader("User-Agent", String.format("%s/%s (Android %s; %s; %s %s; %s)",
                            "GrapeMarket",
                            "1.0",
                            Build.VERSION.RELEASE,
                            Build.MODEL,
                            Build.BRAND,
                            Build.DEVICE,
                            Locale.getDefault().getLanguage()))
                    .post(requestBody)
                    .build();

            Response response = client.newCall(request).execute();
            String res = response.body().string();
            String jsessionid = getJsessionId(response.header("Set-Cookie"));
            if (res.equals("ok")) {
                Session.currentUserInfo.setJSessionId(jsessionid);
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

    //채팅으로 거래하기
    public static Chat requestChatting(Integer boardId) {
        try {
            //OKHTTP3
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("boardId", boardId.toString())
                    .build();

            Request request = new Request.Builder()
                    .addHeader("Cookie", Session.currentUserInfo.getJSessionId())
                    .url(REQUEST_CHATTING)
                    .post(requestBody)
                    .build();

            OkHttpClient client = getUnsafeOkHttpClient();
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            Chat chat = new Gson().fromJson(res, Chat.class);
            Log.d("mychattest", chat.getBuyerId().getName());
            return chat;
        } catch (Exception e) {
            Log.d("myChatting", e.toString());
            return null;
        }
    }

    //Detail
    public static BoardForDetail requestDetailBoard(int id) {
        //OKHTTP3
        try {
            Request request = new Request.Builder()
                    .addHeader("Cookie", Session.currentUserInfo.getJSessionId())
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

            boardForDetail.setCurrentImage1(board.getImage1());
            boardForDetail.setCurrentImage2(board.getImage2());
            boardForDetail.setCurrentImage3(board.getImage3());
            boardForDetail.setCurrentImage4(board.getImage4());
            boardForDetail.setCurrentImage5(board.getImage5());

            //이미지 세팅(작성자 프로필 사진 및 상품 이미지)
            //이미지 땡겨오기 위한 HttpURLConnection
            Bitmap bitmap = null;
            for (int i = 0; i < 5; i++) {
                if (i == 0) { // 작성자 유저프로필 땡겨오기
                    //OKHTTP3
                    Request requestForImage = new Request.Builder()
                            .addHeader("Cookie", Session.currentUserInfo.getJSessionId())
                            .url("https://192.168.43.40:8443/upload/" + board.getUser().getUserProfile())
                            .get()
                            .build();
                    OkHttpClient clientForImage = getUnsafeOkHttpClient();
                    Response responseForImage = clientForImage.newCall(requestForImage).execute();
                    InputStream inputStream = responseForImage.body().byteStream();
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    boardForDetail.setUserProfile(bitmap);
                }

                if (i == 0) {
                    if (!TextUtils.isEmpty(board.getImage1())) {
                        //OKHTTP3
                        Request requestForImage = new Request.Builder()
                                .addHeader("Cookie", Session.currentUserInfo.getJSessionId())
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
                                .addHeader("Cookie", Session.currentUserInfo.getJSessionId())
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
                                .addHeader("Cookie", Session.currentUserInfo.getJSessionId())
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
                                .addHeader("Cookie", Session.currentUserInfo.getJSessionId())
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
                                .addHeader("Cookie", Session.currentUserInfo.getJSessionId())
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
                        .addHeader("Cookie", Session.currentUserInfo.getJSessionId())
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
    public static List<BoardForList> requestAllBoard(Integer pageNumber, Integer range) {
        //OKHTTP3
        try {
            Request request = new Request.Builder()
                    .addHeader("Cookie", Session.currentUserInfo.getJSessionId())
                    .url(ALL_LIST_PAGEABLE_WITH_RANGE + "?page=" + pageNumber.toString() + "&range=" + range.toString())
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
                            .addHeader("Cookie", Session.currentUserInfo.getJSessionId())
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

            File sourceFile1 = null;
            File sourceFile2 = null;
            File sourceFile3 = null;
            File sourceFile4 = null;
            File sourceFile5 = null;

            String fileName1 = "";
            String fileName2 = "";
            String fileName3 = "";
            String fileName4 = "";
            String fileName5 = "";

            Log.d("mywrite", imagePathList.size() + "");

            int imagePathListSize = imagePathList.size();
            for (int i = 0; i < imagePathListSize; i++) {
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

            /*
             * 사용자가 사진을 1개만 선택했다면 나머지 4개는
             * null 파일(size가 0인 파일)이라도 넘겨야 되는데
             * sourceFile1 = new File("") 이런 식으로 처리하면 FileNotFound Exception에 걸리고
             * sourceFile1 = null 이렇게 넘기면 NullPoint Exception에 걸림
             * 그래서 그냥 더미 파일(사이즈 0)을 생성해서 일단 넘기고
             * 데이터 전송과 결과받기가 끝나면 만들었던 더미 파일을 삭제하는 식으로 구현함.
             */

            //사진이 5개 미만일 경우 dummy 파일 생성 (0개인 경우는 아예 이쪽으로 오지 못하게 해야함)
            for (int i = 0; i < 5; i++) {
                if (imagePathListSize == 1) {
                    sourceFile2 = new File(Environment.getExternalStorageDirectory() + "/" + File.separator + "dummy2.podo");
                    sourceFile2.createNewFile();
                    sourceFile3 = new File(Environment.getExternalStorageDirectory() + "/" + File.separator + "dummy3.podo");
                    sourceFile3.createNewFile();
                    sourceFile4 = new File(Environment.getExternalStorageDirectory() + "/" + File.separator + "dummy4.podo");
                    sourceFile4.createNewFile();
                    sourceFile5 = new File(Environment.getExternalStorageDirectory() + "/" + File.separator + "dummy5.podo");
                    sourceFile5.createNewFile();
                } else if (imagePathListSize == 2) {
                    sourceFile3 = new File(Environment.getExternalStorageDirectory() + "/" + File.separator + "dummy3.podo");
                    sourceFile3.createNewFile();
                    sourceFile4 = new File(Environment.getExternalStorageDirectory() + "/" + File.separator + "dummy4.podo");
                    sourceFile4.createNewFile();
                    sourceFile5 = new File(Environment.getExternalStorageDirectory() + "/" + File.separator + "dummy5.podo");
                    sourceFile5.createNewFile();
                } else if (imagePathListSize == 3) {
                    sourceFile4 = new File(Environment.getExternalStorageDirectory() + "/" + File.separator + "dummy4.podo");
                    sourceFile4.createNewFile();
                    sourceFile5 = new File(Environment.getExternalStorageDirectory() + "/" + File.separator + "dummy5.podo");
                    sourceFile5.createNewFile();
                } else if (imagePathListSize == 4) {
                    sourceFile5 = new File(Environment.getExternalStorageDirectory() + "/" + File.separator + "dummy5.podo");
                    sourceFile5.createNewFile();
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
                    .addHeader("Cookie", Session.currentUserInfo.getJSessionId())
                    .url(WRITE)
                    .post(requestBody)
                    .build();
            OkHttpClient client = getUnsafeOkHttpClient();
            Response response = client.newCall(request).execute();
            String res = response.body().string();

            //dummy 파일 삭제
            for (int i = 0; i < 5; i++) {
                if (imagePathListSize == 1) {
                    sourceFile2.delete();
                    sourceFile3.delete();
                    sourceFile4.delete();
                    sourceFile5.delete();
                } else if (imagePathListSize == 2) {
                    sourceFile3.delete();
                    sourceFile4.delete();
                    sourceFile5.delete();
                } else if (imagePathListSize == 3) {
                    sourceFile4.delete();
                    sourceFile5.delete();
                } else if (imagePathListSize == 4) {
                    sourceFile5.delete();
                }
            }

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
                    .addHeader("Cookie", Session.currentUserInfo.getJSessionId())
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
    public static Boolean saveUserAddressSetting(String address, String addressX, String addressY, String currentUserId) {
        try {
            //OKHTTP3
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("address", address)
                    .addFormDataPart("addressX", addressX)
                    .addFormDataPart("addressY", addressY)
                    .build();

            Request request = new Request.Builder()
                    .addHeader("Cookie", Session.currentUserInfo.getJSessionId())
                    .url(USER_ADDRESS_SETTING)
                    .post(requestBody)
                    .build();

            OkHttpClient client = getUnsafeOkHttpClient();
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            if (res.equals("success")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Log.d("myerror", e.toString());
            return false;
        }
    }

    //저장된 주소 정보 가져오기
    public static UserLocationSetting getSavedAddress() {
        try {
            //OKHTTP3
            Request request = new Request.Builder()
                    .addHeader("Cookie", Session.currentUserInfo.getJSessionId())
                    .url(GET_SAVED_ADDRESS)
                    .get()
                    .build();

            OkHttpClient client = getUnsafeOkHttpClient();
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            UserLocationSetting userLocationSetting = new Gson().fromJson(res, UserLocationSetting.class);
            return userLocationSetting;
        } catch (Exception e) {
            Log.d("myerror", e.toString());
            return null;
        }
    }

    public static boolean saveAddressAuth() {
        try {
            //OKHTTP3
            Request request = new Request.Builder()
                    .addHeader("Cookie", Session.currentUserInfo.getJSessionId())
                    .url(SAVE_ADDRESS_AUTH)
                    .get()
                    .build();

            OkHttpClient client = getUnsafeOkHttpClient();
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            if (res.equals("success")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Log.d("myerror", e.toString());
            return false;
        }
    }

    public static Likes saveLike(Integer boardId) {
        try {
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("boardId", boardId.toString())
                    .build();

            Request request = new Request.Builder()
                    .addHeader("Cookie", Session.currentUserInfo.getJSessionId())
                    .url(SAVE_LIKE)
                    .post(requestBody)
                    .build();

            OkHttpClient client = getUnsafeOkHttpClient();
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            Likes like = new Gson().fromJson(res, Likes.class);
            return like;
        } catch (Exception e) {
            Log.d("myerror", e.toString());
            return null;
        }
    }

    public static Boolean deleteLike(Integer likeId) {
        try {
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("likeId", likeId.toString())
                    .build();

            Request request = new Request.Builder()
                    .addHeader("Cookie", Session.currentUserInfo.getJSessionId())
                    .url(DELETE_LIKE)
                    .post(requestBody)
                    .build();

            OkHttpClient client = getUnsafeOkHttpClient();
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            if (res.equals("success")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Log.d("myerror", e.toString());
            return false;
        }
    }

    //검색
    public static List<BoardForList> search(String categoryString, String userInput, Integer range, Integer page) {
        try {
            Integer category = 0;
            //카테고리 정보 숫자로 세팅
            if (categoryString.equals("전체")) {
                category = 1;
            } else if (categoryString.equals("인기매물")) {
                category = 2;
            } else if (categoryString.equals("디지털/가전")) {
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

            //OKHTTP3
//            RequestBody requestBody = new MultipartBody.Builder()
//                    .setType(MultipartBody.FORM)
//                    .addFormDataPart("category", category + "")
//                    .addFormDataPart("userInput", userInput)
//                    .addFormDataPart("range",range.toString())
//                    .addFormDataPart("page",page.toString())
//                    .build();

            Log.d("crazy", "userInput>> " + userInput);
            Log.d("crazy", "category>> " + category.toString());
            Log.d("crazy", "range>> " + range.toString());
            Log.d("crazy", "page>> " + page.toString());

            Request request = new Request.Builder()
                    .addHeader("Cookie", Session.currentUserInfo.getJSessionId())
                    .url(SEARCH_WITH_RANGE + "?page=" + page.toString() + "&range=" + range.toString() + "&userInput=" + userInput + "&category=" + category.toString())
                    .get()
                    .build();

            OkHttpClient client = getUnsafeOkHttpClient();
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            Type collectionType = new TypeToken<List<Board>>() {
            }.getType();
            List<Board> boards = new Gson().fromJson(res, collectionType);
            Log.d("searcht", "ssssiiizzzz >>>>    " + boards.size());
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
                            .addHeader("Cookie", Session.currentUserInfo.getJSessionId())
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
                    Log.d("searcht", e.toString());
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

    //채팅리스트 가져오기
    public static ChatList getChatList() {
        try {
            Request request = new Request.Builder()
                    .addHeader("Cookie", Session.currentUserInfo.getJSessionId())
                    .url(CHAT_LIST)
                    .get()
                    .build();

            OkHttpClient client = getUnsafeOkHttpClient();
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            ChatList chatList = new Gson().fromJson(res, ChatList.class);
            return chatList;
        } catch (Exception e) {
            Log.d("mychatlist", e.toString());
            return null;
        }
    }

    //현재 프로필 정보 가져오기
    public static CurrentUserInfoForProfile getCurrentProfileInfo() {
        try {
            Request request = new Request.Builder()
                    .addHeader("Cookie", Session.currentUserInfo.getJSessionId())
                    .url(CURRENT_MY_PROFILE)
                    .get()
                    .build();

            OkHttpClient client = getUnsafeOkHttpClient();
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            CurrentUserInfoForProfile user = new Gson().fromJson(res, CurrentUserInfoForProfile.class);

            //UserProfileImage가져오기
            Bitmap bitmap = null;
            try {
                //OKHTTP3
                Request requestForImage = new Request.Builder()
                        .addHeader("Cookie", Session.currentUserInfo.getJSessionId())
                        .url("https://192.168.43.40:8443/upload/" + user.getUserProfile())
                        .get()
                        .build();
                OkHttpClient clientForImage = getUnsafeOkHttpClient();
                Response responseForImage = clientForImage.newCall(requestForImage).execute();
                InputStream inputStream = responseForImage.body().byteStream();
                Log.d("ttt111", inputStream.toString());
                bitmap = BitmapFactory.decodeStream(inputStream);
                user.setUserProfilePhoto(bitmap);
            } catch (Exception e) {
                Log.d("searcht", e.toString());
            }

            return user;
        } catch (Exception e) {
            Log.d("myerror", e.toString());
            return null;
        }
    }

    public static Boolean changePassword(String newPassword) {
        try {
            //OKHTTP3
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("newPassword", newPassword)
                    .build();

            Request request = new Request.Builder()
                    .addHeader("Cookie", Session.currentUserInfo.getJSessionId())
                    .url(CHANGE_PASSWORD)
                    .post(requestBody)
                    .build();

            OkHttpClient client = getUnsafeOkHttpClient();
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            if (res.equals("success")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Log.d("myerror", e.toString());
            return false;
        }
    }

    //프로필 업데이트
    public static Boolean updateProfile(User user) {
        try {
            //OKHTTP3
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("user", new Gson().toJson(user))
                    .build();

            Request request = new Request.Builder()
                    .addHeader("Cookie", Session.currentUserInfo.getJSessionId())
                    .url(CHANGE_PROFILE)
                    .post(requestBody)
                    .build();

            OkHttpClient client = getUnsafeOkHttpClient();
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            if (res.equals("success")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Log.d("myerror", e.toString());
            return false;
        }
    }

    //게시글 수정
    public static Boolean modifyBoard(Integer boardId, List<String> imagePathList, String categoryString, String title, String price, String content,
                                      String currentImage1, String currentImage2, String currentImage3, String currentImage4, String currentImage5) {
        try {
            if (TextUtils.isEmpty(currentImage1)) {
                currentImage1 = "";
            }
            if (TextUtils.isEmpty(currentImage2)) {
                currentImage2 = "";
            }
            if (TextUtils.isEmpty(currentImage3)) {
                currentImage3 = "";
            }
            if (TextUtils.isEmpty(currentImage4)) {
                currentImage4 = "";
            }
            if (TextUtils.isEmpty(currentImage5)) {
                currentImage5 = "";
            }

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

            File sourceFile1 = null;
            File sourceFile2 = null;
            File sourceFile3 = null;
            File sourceFile4 = null;
            File sourceFile5 = null;

            String fileName1 = "";
            String fileName2 = "";
            String fileName3 = "";
            String fileName4 = "";
            String fileName5 = "";

            Log.d("mywrite", imagePathList.size() + "");

            int imagePathListSize = imagePathList.size();
            for (int i = 0; i < imagePathListSize; i++) {
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

            /*
             * 사용자가 사진을 1개만 선택했다면 나머지 4개는
             * null 파일(size가 0인 파일)이라도 넘겨야 되는데
             * sourceFile1 = new File("") 이런 식으로 처리하면 FileNotFound Exception에 걸리고
             * sourceFile1 = null 이렇게 넘기면 NullPoint Exception에 걸림
             * 그래서 그냥 더미 파일(사이즈 0)을 생성해서 일단 넘기고
             * 데이터 전송과 결과받기가 끝나면 만들었던 더미 파일을 삭제하는 식으로 구현함.
             */

            //사진이 5개 미만일 경우 dummy 파일 생성 (0개인 경우는 아예 이쪽으로 오지 못하게 해야함)
            for (int i = 0; i < 5; i++) {
                if (imagePathListSize == 1) {
                    sourceFile2 = new File(Environment.getExternalStorageDirectory() + "/" + File.separator + "dummy2.podo");
                    sourceFile2.createNewFile();
                    sourceFile3 = new File(Environment.getExternalStorageDirectory() + "/" + File.separator + "dummy3.podo");
                    sourceFile3.createNewFile();
                    sourceFile4 = new File(Environment.getExternalStorageDirectory() + "/" + File.separator + "dummy4.podo");
                    sourceFile4.createNewFile();
                    sourceFile5 = new File(Environment.getExternalStorageDirectory() + "/" + File.separator + "dummy5.podo");
                    sourceFile5.createNewFile();
                } else if (imagePathListSize == 2) {
                    sourceFile3 = new File(Environment.getExternalStorageDirectory() + "/" + File.separator + "dummy3.podo");
                    sourceFile3.createNewFile();
                    sourceFile4 = new File(Environment.getExternalStorageDirectory() + "/" + File.separator + "dummy4.podo");
                    sourceFile4.createNewFile();
                    sourceFile5 = new File(Environment.getExternalStorageDirectory() + "/" + File.separator + "dummy5.podo");
                    sourceFile5.createNewFile();
                } else if (imagePathListSize == 3) {
                    sourceFile4 = new File(Environment.getExternalStorageDirectory() + "/" + File.separator + "dummy4.podo");
                    sourceFile4.createNewFile();
                    sourceFile5 = new File(Environment.getExternalStorageDirectory() + "/" + File.separator + "dummy5.podo");
                    sourceFile5.createNewFile();
                } else if (imagePathListSize == 4) {
                    sourceFile5 = new File(Environment.getExternalStorageDirectory() + "/" + File.separator + "dummy5.podo");
                    sourceFile5.createNewFile();
                }
            }

            //위에서는 imagepathList가 아예 널인거 찾아서 즉 아예 선택안한거 찾아서 더미만들고
            //그 후에 이미지는 있는데 그게 기존 이미지라서 그냥 이미지패쓰가 내가 지정한 previousImage인거 찾아서
            //더미로 만들어서 넘김

            for (int i = 0; i < imagePathListSize; i++) {
                if (i == 0) {
                    if (imagePathList.get(0).equals("previousImage")) {
                        sourceFile1 = new File(Environment.getExternalStorageDirectory() + "/" + File.separator + "dummy1.podo");
                        sourceFile1.createNewFile();
                    }
                }
                if (i == 1) {
                    if (imagePathList.get(1).equals("previousImage")) {
                        sourceFile2 = new File(Environment.getExternalStorageDirectory() + "/" + File.separator + "dummy2.podo");
                        sourceFile2.createNewFile();
                    }
                }
                if (i == 2) {
                    if (imagePathList.get(2).equals("previousImage")) {
                        sourceFile3 = new File(Environment.getExternalStorageDirectory() + "/" + File.separator + "dummy3.podo");
                        sourceFile3.createNewFile();
                    }
                }
                if (i == 3) {
                    if (imagePathList.get(3).equals("previousImage")) {
                        sourceFile4 = new File(Environment.getExternalStorageDirectory() + "/" + File.separator + "dummy4.podo");
                        sourceFile4.createNewFile();
                    }
                }
                if (i == 4) {
                    if (imagePathList.get(4).equals("previousImage")) {
                        sourceFile5 = new File(Environment.getExternalStorageDirectory() + "/" + File.separator + "dummy5.podo");
                        sourceFile5.createNewFile();
                    }
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
                    .addFormDataPart("currentImage1", currentImage1)
                    .addFormDataPart("currentImage2", currentImage2)
                    .addFormDataPart("currentImage3", currentImage3)
                    .addFormDataPart("currentImage4", currentImage4)
                    .addFormDataPart("currentImage5", currentImage5)
                    .addFormDataPart("category", category + "")
                    .addFormDataPart("state", "0")
                    .addFormDataPart("title", title)
                    .addFormDataPart("price", price)
                    .addFormDataPart("content", content)
                    .addFormDataPart("boardId", boardId.toString())
                    .build();

            Request request = new Request.Builder()
                    .addHeader("Cookie", Session.currentUserInfo.getJSessionId())
                    .url(MODIFY_BOARD)
                    .post(requestBody)
                    .build();
            OkHttpClient client = getUnsafeOkHttpClient();
            Response response = client.newCall(request).execute();
            String res = response.body().string();

            //dummy 파일 삭제
            for (int i = 0; i < 5; i++) {
                if (imagePathListSize == 1) {
                    sourceFile2.delete();
                    sourceFile3.delete();
                    sourceFile4.delete();
                    sourceFile5.delete();
                } else if (imagePathListSize == 2) {
                    sourceFile3.delete();
                    sourceFile4.delete();
                    sourceFile5.delete();
                } else if (imagePathListSize == 3) {
                    sourceFile4.delete();
                    sourceFile5.delete();
                } else if (imagePathListSize == 4) {
                    sourceFile5.delete();
                }
            }

            //dummy delete
            for (int i = 0; i < imagePathListSize; i++) {
                if (i == 0) {
                    if (imagePathList.get(0).equals("previousImage")) {
                        sourceFile1.delete();
                    }
                }
                if (i == 1) {
                    if (imagePathList.get(1).equals("previousImage")) {
                        sourceFile2.delete();
                    }
                }
                if (i == 2) {
                    if (imagePathList.get(2).equals("previousImage")) {
                        sourceFile3.delete();
                    }
                }
                if (i == 3) {
                    if (imagePathList.get(3).equals("previousImage")) {
                        sourceFile4.delete();
                    }
                }
                if (i == 4) {
                    if (imagePathList.get(4).equals("previousImage")) {
                        sourceFile5.delete();
                    }
                }
            }


            if (res.equals("success")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Log.d("modifyt", e.toString());
            return false;
        }
    }

    //게시글 삭제
    public static Boolean deleteBoard(int boardId) {
        try {
            Request request = new Request.Builder()
                    .addHeader("Cookie", Session.currentUserInfo.getJSessionId())
                    .url(DELETE_BOARD + boardId)
                    .get()
                    .build();

            OkHttpClient client = getUnsafeOkHttpClient();
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            if (res.equals("success")) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            Log.d("myerror", e.toString());
            return false;
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

    public static String getJsessionId(String rawJsessionId) {
        String jSessionId = "";
        int rawJsessionIdSize = rawJsessionId.length();
        for (int i = 0; i < rawJsessionIdSize; i++) {
            if (rawJsessionId.charAt(i) == ';') {
                break;
            }
            jSessionId += rawJsessionId.charAt(i);
        }
        Log.d("jsessiontest", "파싱한 >> " + jSessionId);
        return jSessionId;
    }
}
