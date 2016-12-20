package com.andrey.tasktesthandh.video;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Environment;

import com.andrey.tasktesthandh.VideoActivity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public class Task extends AsyncTask<String, String, String> {
    String StorezipFileLocation = Environment.getExternalStorageDirectory() +"/DownloadedZip";
    String DirectoryName=Environment.getExternalStorageDirectory() + "/unzipFolder/";
    private VideoActivity activity;
    private ProgressDialogFragment progressDialog;
    String result="";

    @Override protected void onPreExecute() {
        super.onPreExecute();

        progressDialog = new ProgressDialogFragment.Builder()
                .setMessage("Working...")
                .build();
        progressDialog.show(activity.getFragmentManager(), "task_progress");
    }

    @Override
    protected String doInBackground(String... aurl) {
        int count;
        try {
            URL url = new URL(aurl[0]);
            URLConnection conection = url.openConnection();
            conection.connect();
            int lenghtOfFile = conection.getContentLength();

            InputStream input = new BufferedInputStream(url.openStream());
            OutputStream output = new FileOutputStream(StorezipFileLocation);

            byte data[] = new byte[1024];
            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                publishProgress(""+(int)((total*100)/lenghtOfFile));
                output.write(data, 0, count);
            }
            output.close();
            input.close();
            result = "true";
        }
        catch (Exception e) {
            result = "false";
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(String... progress) {

        progressDialog.setProgress(activity.getFragmentManager(), Integer.parseInt(progress[0]));
    }

    @Override protected void onPostExecute(String unused) {

        if (activity != null){
            progressDialog.dismiss(activity.getFragmentManager());
        }

        TaskHelper.getInstance().removeTask("task");

        if(result.equalsIgnoreCase("true")) {
            try {
                unzip();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else {

        }
    }


    public void attach(Activity a) {
        this.activity = (VideoActivity) a;
    }


    public void detach() {
        this.activity = null;
    }
    public void unzip() throws IOException {
        new UnZipTask().execute(StorezipFileLocation, DirectoryName);
    }

    private class UnZipTask extends AsyncTask<String, Void, Boolean> {
        @SuppressWarnings("rawtypes")
        @Override
        protected Boolean doInBackground(String... params) {
            String filePath = params[0];
            String destinationPath = params[1];

            File archive = new File(filePath);
            try {
                ZipFile zipfile = new ZipFile(archive);
                for (Enumeration e = zipfile.entries(); e.hasMoreElements();) {
                    ZipEntry entry = (ZipEntry) e.nextElement();
                    unzipEntry(zipfile, entry, destinationPath);
                }
                UnzipUtil d = new UnzipUtil(StorezipFileLocation, DirectoryName);
                d.unzip();

            }
            catch (Exception e) {
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
        }


        private void unzipEntry(ZipFile zipfile, ZipEntry entry,String outputDir) throws IOException {

            if (entry.isDirectory()) {
                createDir(new File(outputDir, entry.getName()));
                return;
            }

            File outputFile = new File(outputDir, entry.getName());
            if (!outputFile.getParentFile().exists()) {
                createDir(outputFile.getParentFile());
            }
            BufferedInputStream inputStream = new BufferedInputStream(zipfile.getInputStream(entry));
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));

            try {

            }
            finally {
                outputStream.flush();
                outputStream.close();
                inputStream.close();
            }
        }

        private void createDir(File dir) {
            if (dir.exists()) {
                return;
            }
            if (!dir.mkdirs()) {
                throw new RuntimeException("Can not create dir " + dir);
            }
        }}

}