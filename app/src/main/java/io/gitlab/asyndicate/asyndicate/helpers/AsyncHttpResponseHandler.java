package io.gitlab.asyndicate.asyndicate.helpers;


public abstract class AsyncHttpResponseHandler {
    public abstract void onStart();

    public abstract void onFailure(int i, String s, Throwable throwable);

    public void onFinish() {
    }

    public abstract void onSuccess(int i, String s);
}
