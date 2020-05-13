package com.huc.android_ble_monitor.util;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;

public class ServiceIDXmlResolver extends AsyncTask<String, String, String> {
    private static final String TAG ="BLEM_NetUtil";
    private static String baseUrl = "https://www.bluetooth.com/wp-content/uploads/Sitecore-Media-Library/Gatt/Xml/Services/";

    public IAsyncDownload delegate = null;

    public ServiceIDXmlResolver(IAsyncDownload delegate) {
        this.delegate = delegate;
    }

    /**
     * This function can be used to retrieve a URL to the xml definition of a
     * SIG standardized service.
     *
     * @param identifier
     * @return URL to xml definition
     */
    public static String getXmlLink(String identifier){
        return baseUrl + identifier + ".xml";
    }

    @Override
    protected String doInBackground(String... identifier) {
        try {
            URL url = new URL(getXmlLink(identifier[0]));

            // download the file
            InputStream input = new BufferedInputStream(url.openStream(),
                    8192);

            Writer writer = new StringWriter();
            char[] buffer = new char[1024];
            Reader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
            int n;
            long totalRead = 0;

            while ((n = reader.read(buffer)) != -1)
            {
                totalRead += n;
                writer.write(buffer, 0, n);
            }
            input.close();
            return writer.toString();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.fileDownloaded(result);
    }
}
