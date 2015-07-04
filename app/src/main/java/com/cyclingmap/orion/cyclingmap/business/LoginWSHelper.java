package com.cyclingmap.orion.cyclingmap.business;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.spec.KeySpec;


import javax.crypto.Cipher;
import javax.crypto.EncryptedPrivateKeyInfo;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Daniel on 25/06/2015.
 */
public class LoginWSHelper extends AsyncTask<String, Integer, Integer> {

    public static int valor=0;

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    @Override
    protected Integer doInBackground(String... params) {
       int resul=0;
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("http://orion-group.azurewebsites.net/Api/Login/" + params[0]+ "/" + params[1]);
        httpGet.setHeader("content-type", "application/json");
        try {
            HttpResponse resp = client.execute(httpGet);
            String respStr = EntityUtils.toString(resp.getEntity());
            resul= Integer.parseInt(respStr);
            valor=resul;
            Log.i("Resultado", " " + resul );

        } catch (Exception ex) {
            Log.e("ServicioRest", "Error!", ex);
            resul = 0;
        }
        return resul;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        Log.i("Valor ",""+integer);
        setValor(integer);
    }

    private static String IV = "IV_VALUE_16_BYTE";
    private static String PASSWORD = "PASSWORD_VALUE";
    private static String SALT = "SALT_VALUE";



    private String getString(byte[] bytes) throws UnsupportedEncodingException {
        return new String(bytes, "UTF-8");
    }

    private byte[] getBytes(String str) throws UnsupportedEncodingException {
        return str.getBytes("UTF-8");
    }

    private Cipher getCipher(int mode) throws Exception {
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] iv = getBytes(IV);
        c.init(mode, generateKey(), new IvParameterSpec(iv));
        return c;
    }

    private Key generateKey() throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        char[] password = PASSWORD.toCharArray();
        byte[] salt = getBytes(SALT);

        KeySpec spec = new PBEKeySpec(password, salt, 65536, 128);
        SecretKey tmp = factory.generateSecret(spec);
        byte[] encoded = tmp.getEncoded();
        return new SecretKeySpec(encoded, "AES");
    }


}
