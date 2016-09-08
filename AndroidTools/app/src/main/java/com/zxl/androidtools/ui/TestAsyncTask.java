package com.zxl.androidtools.ui;

import android.os.AsyncTask;

import com.plugin.utils.base.BaseAppCompatActivity;

/**
 * @Description:
 * @Author: zxl
 * @Date: 30/8/16.
 */
public class TestAsyncTask extends BaseAppCompatActivity {
    
    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void initView() {

    }

    private class MThread extends Thread {
        @Override
        public void run() {
            super.run();
        }
    }

    private class TMThread implements Runnable {

        @Override
        public void run() {

        }
    }

    /**
     * AsyncTask<Params, Progress, Result>
     * Params对应doInBackground(Params...)的参数类型。而new AsyncTask().execute(Params... params)，就是传进来的Params数据，你可以execute(data)来传送一个数据，或者execute(data1, data2, data3)这样多个数据。
     * Progress对应onProgressUpdate(Progress...)的参数类型；
     * Result对应onPostExecute(Result)的参数类型。
     * 当以上的参数类型都不需要指明某个时，则使用Void，注意不是void
     */
    private class MTask extends AsyncTask<String, String, String> {

        /**
         * 该方法将在执行实际的后台操作前被UI thread调用。可以在该方法中做一些准备工作，如在界面上显示一个进度条。
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        /**
         * 执行那些很耗时的后台计算工作。可以调用publishProgress方法来更新实时的任务进度。
         */
        @Override
        protected String doInBackground(String... strings) {
            le("doInBackground----");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 在doInBackground 执行完成后，onPostExecute 方法将被UI thread调用，
         * 后台的计算结果将通过该方法传递到UI thread.
         */
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            le("onPostExecute----");
        }
    }
}
