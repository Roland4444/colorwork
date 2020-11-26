package ru.com.avs.scalereader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLConnection;

import ru.com.avs.model.Scale;

public class Scale60EthReader extends HttpReader {

    public Scale60EthReader(Scale scale) {
        super(scale);
    }

    @Override
    public String readWeight() throws IOException {
        try {
            URL url = new URL(this.scale.getIp() + ":" + this.scale.getEthPort());
            URLConnection con = url.openConnection();
            InputStream in = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String weight = br.readLine();
            in.close();
            br.close();
            return weight;
        } catch (ConnectException e) {
            return "0";
        }
        /*URL url = new URL(this.scale.getIp() + ":" + this.scale.getEthPort());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setConnectTimeout(1000);
        con.setReadTimeout(1000);

        int status = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        String weight = content.toString();
        System.out.println(weight);
        return weight;*/
    }
}
