package br.com.starfusion.useful;

import android.support.annotation.StringRes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;

import br.com.starfusion.App;

/**
 * Created by Henrique on 15/06/2016.
 */
public class Resources {
    public static CharSequence getChar(@StringRes int id){
        return App.context().getResources().getText(id);
    }

    public static String getString(@StringRes int id){
        return getChar(id).toString();
    }

    public static CharSequence getCharSequence(@StringRes int id){
        return getChar(id);
    }

    public static Date getDateTimeNow() throws ParseException {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat date = new SimpleDateFormat("dd/MMMM/yyyy HH:mm:ss a");
        String dt = date.format(c.getTime());
        return date.parse(dt);
    }

    public static byte[] getLocalIPAddress () {
        byte ip[]=null;
        try {
            for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = (NetworkInterface)en.nextElement();
                for (Enumeration enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = (InetAddress)enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        ip= inetAddress.getAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ip;
    }

    public static String getExternalIP() {
        try {
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
            return in.readLine();
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }
}
