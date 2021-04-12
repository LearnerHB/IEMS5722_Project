package hk.edu.cuhk.ie.iems5722.a2_1155152636.network;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.Reader;
import java.lang.reflect.Type;

/**
 * json处理类 把接口数据转化为所需数据格式
 */
public class GsonUtils {

    private static Gson sGson = null;
    private static JsonParser sParser = new JsonParser();

    public static Gson getInstance() {
        if (sGson == null) {
            sGson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
        }
        return sGson;
    }

    public static <T> T fromJson(Reader json, Type type) {
        try {
            return getInstance().fromJson(json, type);
        } catch (Exception e) {
        }
        return null;
    }

    public static <T> T parseObject(String json, Type type) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        try {
            return (T) getInstance().fromJson(json, type);
        } catch (Exception e) {
        }
        return null;
    }

    public static String parseJson(Object o) {
        if (o == null) {
            return null;
        }
        try {
            return getInstance().toJson(o);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * convert string to JsonObject
     *
     * @param content
     * @return
     */
    public static JsonObject parseJsonObject(String content) {
        if (TextUtils.isEmpty(content)) {
            return null;
        }
        return sParser.parse(content).getAsJsonObject();
    }

    public static JsonArray parseJsonArray(String content) {
        if (TextUtils.isEmpty(content)) {
            return null;
        }
        return sParser.parse(content).getAsJsonArray();
    }
}
