package com.example.administrator.jkbd.Utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by yao on 2016/9/16.
 *
 */
public class OkHttpUtils<T> {
    private static String UTF_8 = "utf-8";
    public static final int RESULT_SUCCESS = 0;
    public static final int RESULT_ERROR = 1;
    public static final int DOWNLOAD_START=2;
    public static final int DOWNLOADING=3;
    public static final int DOWNLOAD_FINISH=4;

    private static OkHttpClient mOkHttpClient;
    private Handler mHandler;

    private String SERVER_ROOT;
    private Context mContext;

    public void setSERVER_ROOT(String SERVER_ROOT) {
        this.SERVER_ROOT = SERVER_ROOT;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    /**
     * 存放post请求的实体，实体中存放File类型的文件
     */
    RequestBody mFileBody;
    FormBody.Builder mFormBodyBuilder;
    MultipartBody.Builder mMultipartBodyBuilder;

    public interface OnCompleteListener<T> {
        void onSuccess(T result);

        void onError(String error);
    }

    private OnCompleteListener<T> mListener;

    OkHttpClient.Builder mBuilder;
    /**
     * 构造器，mOkHttpClient必须单例，无论创建多少个OkHttpUtils的实例。
     * 都由mOkHttpClient一个对象处理所有的网络请求。
     */
    public OkHttpUtils(Context context) {
        mContext = context;
        if (mOkHttpClient == null) {//线程安全的单例
            synchronized (OkHttpUtils.class) {
                if (mOkHttpClient == null) {
                    mBuilder = new OkHttpClient.Builder();
                    //获取sd卡的缓存文件夹
                    File cacheDir = context.getExternalCacheDir();
                    mOkHttpClient = mBuilder
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .writeTimeout(20,TimeUnit.SECONDS)
                            .readTimeout(10,TimeUnit.SECONDS)
                            .cache(new Cache(cacheDir,10*(1<<20)))//设置缓存位置和缓存大小
                            .build();
                }
            }
        }
        initHandler();
    }

    /**
     * 设置与服务端连接的时限
     * @param connectTime:连接的时限
     * @return
     */
    public OkHttpUtils<T> connectTimeout(int connectTime) {
        if (mBuilder == null) {
            return this;
        }
        mBuilder.connectTimeout(connectTime, TimeUnit.SECONDS);
        return this;
    }

    /**
     * 设置写数据的时限
     * @param writeTimeout：写数据的时限
     * @return
     */
    public OkHttpUtils<T> writeTimeout(int writeTimeout) {
        if (mBuilder == null) {
            return this;
        }
        mBuilder.writeTimeout(writeTimeout, TimeUnit.SECONDS);
        return this;
    }

    /**
     * 设置读取数据的时限
     * @param readTimeout：读取数据的时限
     * @return
     */
    public OkHttpUtils<T> readTimeout(int readTimeout) {
        if (mBuilder == null) {
            return this;
        }
        mBuilder.readTimeout(readTimeout, TimeUnit.SECONDS);
        return this;
    }

    /**
     * 设置缓存
     * 第一次请求会请求网络得到数据，第二次以及后面的请求则会从缓存中取出数据
     * @param file:缓存的路径
     * @param fileSize：缓存的容量
     * @return
     */
    public OkHttpUtils<T> cache(File file, int fileSize) {
        if (mBuilder == null) {
            return this;
        }
        mBuilder.cache(new Cache(file, fileSize));
        return this;
    }


    private void initHandler() {
        mHandler = new Handler(mContext.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case RESULT_ERROR:
                        mListener.onError(msg.obj==null?msg.toString():msg.obj.toString());
                        break;
                    case RESULT_SUCCESS:
                        T result = (T) msg.obj;
                        mListener.onSuccess(result);
                        break;
                }
            }
        };
    }

    /**
     * 用post请求，添加一个文件
     * @param file:添加至form的文件
     * @return
     */
    public OkHttpUtils<T> addFile(File file) {
        mFileBody = RequestBody.create(null, file);
        return this;
    }

    /**
     * 支持设置媒体文件类型的addFile
     * @param type：媒体类型
     * @param file：添加至form的文件
     * @return
     */
    public OkHttpUtils<T> addFile(String type, File file) {
        mFileBody = RequestBody.create(MediaType.parse(type), file);
        return this;
    }

    public OkHttpUtils<T> addFile2(File file) {
        if (mUrl == null) {
            return this;
        }
        RequestBody fileBody = RequestBody.create(MediaType.parse(guessMimeType(file.getName())), file);
        mFileBody = new MultipartBody.Builder().addFormDataPart("filename", file.getName(), fileBody).build();
        return this;
    }
    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null)
        {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    /**
     * 设置为post的请求
     * @return
     */
    public OkHttpUtils<T> post() {
        mFormBodyBuilder = new FormBody.Builder();
        return this;
    }

    StringBuilder mUrl;

    public OkHttpUtils<T> url(String url) {
        mUrl = new StringBuilder(url);
        return this;
    }

    public OkHttpUtils<T> setRequestUrl(String request) {
        //http://101.251.196.90:8080/JztkServer/examInfo
        //http://120.26.242.249:8080/SuperWeChatServerV2.0/register?m_user_name=aaaaaa&m_user_nick=aaaaaa&m_user_password=aaaaaa
        mUrl = new StringBuilder(SERVER_ROOT);
        mUrl.append(request);
//        Log.e("okhttp","1 murl="+ mUrl.toString());
        return this;
    }

    /**
     * 用于json解析的类对象
     */
    Class<T> mClazz;

    /**
     * 设置json解析的目标类对象
     * @param clazz:解析的类对象
     * @return
     */
    public OkHttpUtils<T> targetClass(Class<T> clazz) {
        mClazz = clazz;
        return this;
    }

    /**
     * 添加请求参数至url，包括GET和POST请求
     * 不包括POST请求中上传文件的同时向Form中添加其它参数的情况
     * @param key:键
     * @param value：值
     */
    public OkHttpUtils<T> addParam(String key, String value) {
        try {
            //post请求的request参数也要拼接到url中
            if (mFormBodyBuilder != null) {//post请求的参数添加方式
                mFormBodyBuilder.add(key, URLEncoder.encode(value, UTF_8));
            } else {//get请求的参数添加方式
                if (mUrl.indexOf("?") == -1) {
                    mUrl.append("?");
                } else {
                    mUrl.append("&");
                }
                mUrl.append(key).append("=").append(URLEncoder.encode(value, UTF_8));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * * post请求,上传文件的同时允许在Form中添加多个参数
     * @param key:参数的键
     * @param value：参数的值
     * @return
     */
    public OkHttpUtils<T> addFormParam(String key, String value) {
        if (mMultipartBodyBuilder == null) {
            mMultipartBodyBuilder =new MultipartBody.Builder();
            mMultipartBodyBuilder.setType(MultipartBody.FORM);
            try {
                mUrl.append("?")
                        .append(key)
                        .append("=")
                        .append(URLEncoder.encode(value, UTF_8));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else if (mUrl.indexOf("?") > -1) {
            mMultipartBodyBuilder.addFormDataPart(key, value);
        }
        return this;
    }

    /**
     * post请求中在Form中添加包含上传文件的多个参数
     * @param name:文件的大类型
     * @param fileName：文件名包括扩展名
     * @param mediaType：文件的媒体类型
     * @param file：文件
     * @return
     */
    public OkHttpUtils<T> addFormParam(String name, String fileName, String mediaType, File file) {
        if (mMultipartBodyBuilder == null) {
            return this;
        }
        mMultipartBodyBuilder.addFormDataPart(name, fileName, RequestBody.create(MediaType.parse(mediaType), file));
        return this;
    }

    /**
     * 发送请求
     * @param listener：处理服务端返回结果的代码
     */
    public void execute(OnCompleteListener<T> listener) {
        if (listener != null) {
            mListener = listener;
        }
        Request.Builder builder = new Request.Builder().url(mUrl.toString());
        Log.e("OKHttp","url="+mUrl);
        if (mFormBodyBuilder != null) {
            RequestBody body = mFormBodyBuilder.build();
            builder.post(body);
        }
        if (mFileBody != null) {
            builder.post(mFileBody);
        }
        //如果是post请求向Form中添加多个参数
        if (mMultipartBodyBuilder != null) {
            MultipartBody multipartBody = mMultipartBodyBuilder.build();
            builder.post(multipartBody);
        }
        //创建请求
        Request request = builder.build();
        Call call = mOkHttpClient.newCall(request);
        if (mCallback != null) {
            call.enqueue(mCallback);
            return;
        }
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg = Message.obtain();
                msg.what = RESULT_ERROR;
                msg.obj = e.getMessage();
                mHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                if (mClazz==null){
                    targetClass((Class<T>) String.class);
                }
                if(mClazz.equals(String.class)){
                    Message msg = Message.obtain();
                    msg.what = RESULT_SUCCESS;
                    msg.obj = json;
                    mHandler.sendMessage(msg);
                }else {
                    Gson gson = new Gson();
                    T value = gson.fromJson(json, mClazz);
                    Message msg = Message.obtain();
                    msg.what = RESULT_SUCCESS;
                    msg.obj = value;
                    mHandler.sendMessage(msg);
                }
            }
        });
    }

    Callback mCallback;
    /**
     * 在OkHttp创建的工作线程中执行一段代码,
     * @param callback
     * @return
     */
    public OkHttpUtils<T> doInBackground(Callback callback) {
        mCallback=callback;
        return this;
    }

    /**
     * 在主线程中执行的代码，doInBackground方法之后调用
     * @param listener
     * @return
     */
    public OkHttpUtils<T> onPostExecute(OnCompleteListener<T> listener) {
        mListener=listener;
        return this;
    }

    /**doInBackground()之前在主线程中执行的方法，类似与AsyncTask中的onPreExecute()
     * @param r:运行的代码
     * @return
     */
    public OkHttpUtils<T> onPreExecute(Runnable r) {
        r.run();
        return this;
    }

    /**
     * 工作线程向主线程发送消息
     * @param msg
     */
    public void sendMessage(Message msg) {
        mHandler.sendMessage(msg);
    }

    /**
     * 重载的sendMessage方法，用于发送空消息
     * @param what
     */
    public void sendMessage(int what) {
        mHandler.sendEmptyMessage(what);
    }

    public <T> T parseJson(String json, Class<?> clazz) {
        Gson gson=new Gson();
        T t = (T) gson.fromJson(json, clazz);
        return t;
    }

    /**
     * 专门针对Result类的json解析方法，不具有通用性，属性定制、专用的方法
     * @param result
     * @param clazz
     * @param <T>
     * @return
     */
//    public <T> T parseJson(Result result, Class<?> clazz) {
//        if (result.getRetCode() == 0) {
//            String json = result.getRetData().toString();
//            T t = parseJson(json, clazz);
//            return t;
//        }
//        return null;
//    }

    /**
     * 下载文件，支持更新下载进度
     * @param response：服务端返回的响应类对象
     * @param file：保存下载文件的File
     * @throws Exception：IO异常
     */
    public void downloadFile(Response response, File file) throws Exception {
        FileOutputStream out = new FileOutputStream(file);

        InputStream in = response.body().byteStream();
        int len;
        byte[] buffer = new byte[1024 * 5];
        //获取文件的字节数
        long fileSize = response.body().contentLength();
        int  total=0;//累加下载的字节数
        int percent=1;//下载的预期百分比
        int currentPer;//当前下载的百分比
        mHandler.sendEmptyMessage(DOWNLOAD_START);
        while ((len=in.read(buffer)) != -1) {
            out.write(buffer,0,len);
            total+=len;
            //计算下载的百分比
            currentPer= (int) (total*100L/fileSize);
            if (currentPer >= percent) {
                Message msg = Message.obtain();
                msg.what= OkHttpUtils.DOWNLOADING;
                msg.arg1=percent;
                sendMessage(msg);
                percent=currentPer+1;
            }
        }
        sendMessage(OkHttpUtils.DOWNLOAD_FINISH);
    }

    public <T>  ArrayList<T> array2List(T[] array) {
        List<T> list = Arrays.asList(array);
        ArrayList<T> arrayList = new ArrayList<>(list);
        return arrayList;
    }

    /**
     * 释放mClient的资源
     */
    public static void release() {
        if (mOkHttpClient != null) {
            //取消所有请求
            mOkHttpClient.dispatcher().cancelAll();
            mOkHttpClient=null;
        }
    }

}
