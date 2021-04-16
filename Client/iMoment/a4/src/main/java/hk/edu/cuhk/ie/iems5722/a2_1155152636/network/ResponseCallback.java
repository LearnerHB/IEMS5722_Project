package hk.edu.cuhk.ie.iems5722.a2_1155152636.network;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * retroft 封装 okhttp
 * @param <T>
 */
public abstract class ResponseCallback<T> implements Callback<T> {

    /**
     * Invoked for a received HTTP response.
     * <p>
     * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
     * Call {@link Response#isSuccessful()} to determine if the response indicates success.
     *
     * @param call
     * @param response
     */
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response != null) {
            Log.d("ResponseCallback", "response code :" + response.code() + "   message:" + response.message());
        }
        success(call, response);
        doAfterTerminate();
    }

    /**
     * Invoked when a network exception occurred talking to the server or when an unexpected
     * exception occurred creating the request or processing the response.
     *
     * @param call
     * @param t
     */
    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Log.w("ResponseCallback", t.getMessage() + "");
        fail(call, t);
        doAfterTerminate();
    }


    protected abstract void fail(Call<T> call, Throwable t);

    protected abstract void success(Call<T> call, Response<T> response);

    protected void doAfterTerminate() {

    }


}
