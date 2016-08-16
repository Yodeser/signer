package imaxct.signer.bean;

import imaxct.signer.misc.Lib;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Sign parameters
 * Created by maxct on 2016/8/11.
 */
public abstract class BaseParams implements Serializable{
    private String BDUSS;

    static final String _client_id = "03-00-DA-59-05-00-72-96-06-00-01-00-04-00-4C-43-01-00-34-F4-02-00-BC-25-09-00-4E-36";
    static final String _client_type = "4";
    static final String _client_version = "1.2.1.17";
    static final String _phone_imei = "540b43b59d21b7a4824e1fd31b08e9a6";
    static final String net_type = "3";
    //private String cuid = "4D019303413FECD5A3B9796AEC757297|255932920456668";
    //static final String from = "baidu_appstore";
    //static final String model = "MX4";
    //static final String stErrorNums = "1";
    //static final String stMethod = "1";
    //static final String stMode = "1";
    //static final String timestamp = Long.toString(System.currentTimeMillis());

    protected Map<String, String> map = new TreeMap<String, String>();

    public BaseParams(String BDUSS){
        this.BDUSS = BDUSS;
        //this.cuid = Lib.urlEncode(cuid);
        setParams();
    }

    protected void setSIGN(){
        Set<String> keySet = map.keySet();
        StringBuilder SIGN = new StringBuilder();
        for (String key : keySet)
            SIGN.append(key + '=' + map.get(key));
        SIGN.append("tiebaclient!!!");
        map.put("sign", Lib.md5(SIGN.toString()).toUpperCase());
    }

    private void setParams(){
        map.put("BDUSS", BDUSS);
        map.put("_client_id", _client_id);
        map.put("_client_type", _client_type);
        map.put("_client_version", _client_version);
        map.put("_phone_imei", _phone_imei);
        map.put("net_type", net_type);
        //map.put("cuid", cuid);
        //map.put("from", from);
        //map.put("model", model);
        //map.put("stErrorNums", stErrorNums);
        //map.put("stMethod", stMethod);
        //map.put("stMode", stMode);
        //map.put("timestamp", timestamp);
    }

    abstract void addParams();

    /*public String getCuid(){
        return this.cuid;
    }*/

    public String toString(){
        StringBuilder s = new StringBuilder();
        Set<String>keySet = map.keySet();
        for (String key : keySet)
            s.append(key + '=' + map.get(key) + '&');
        return s.toString().substring(0, s.length()-1);
    }
}
