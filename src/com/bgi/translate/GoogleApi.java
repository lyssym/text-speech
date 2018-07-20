package com.bgi.translate;

import com.alibaba.fastjson.JSONArray;
import com.bgi.util.SystemConfig;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.StrKit;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URLEncoder;


public class GoogleApi {
    private static final String PATH = SystemConfig.JSSCRIPT;
    private static final String CHARSET = "UTF-8";
    private static ScriptEngine engine = null;

    /**
     * 静态加载JS脚本信息,以便后续进行相应的服务处理
     */
    static {
        ScriptEngineManager maneger = new ScriptEngineManager();
        engine = maneger.getEngineByName("javascript");
        Reader scriptReader = null;
        try {
        	InputStream in = GoogleApi.class.getClassLoader().getResourceAsStream(PATH);
            scriptReader = new InputStreamReader(in, CHARSET);
            engine.eval(scriptReader);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (scriptReader != null) {
                try {
                    scriptReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public GoogleApi() {
        System.setProperty("122.224.227.202", "3128");
    }

    /**
     * 抽取相应的需要用来进行验证的信息
     * @return
     */
    public String getTKK() {
        try {
            String result = HttpKit.get(SystemConfig.GOOGLE);
            if (!StrKit.isBlank(result)) {
                if (result.indexOf("TKK") > -1) {
                    String tkk = result.split("TKK")[1];
                    tkk = tkk.split("\\)\\;")[0];
                    tkk = tkk + ");";
                    tkk = tkk.substring(1, tkk.length());
                    ScriptEngineManager manager = new ScriptEngineManager();
                    ScriptEngine engine = manager.getEngineByName("javascript");
                    return (String) engine.eval(tkk);  // 抽取JS代码并进行相应的处理, 获取结果
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取token值, 以使网络请求能够访问
     * @param word
     * @param tkk
     * @return
     */
    public static String getTK(String word, String tkk) {
        String result = null;
        try {
            if (engine instanceof Invocable) {
                Invocable invocable = (Invocable) engine;
                result = (String) invocable.invokeFunction("tk", new Object[]{word, tkk});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据源端与目标端语种类型, 进行相应翻译服务
     * @param word
     * @param from
     * @param to
     * @return
     */
    public String translate(String word, String from, String to) {
        if (StrKit.isBlank(word)) {
            return null;
        }
        String tkk = getTKK();
        if (StrKit.isBlank(tkk)) {
            return null;
        }
        String tk = getTK(word, tkk);
        try {
            word = URLEncoder.encode(word, "UTF-8");  // 进行相应的编码, 符合网络请求访问
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        StringBuffer buffer = new StringBuffer(SystemConfig.TRANSLATE);
        buffer.append("client=t");
        buffer.append("&sl=" + from);
        buffer.append("&tl=" + to);
        buffer.append("&hl=zh-CN&dt=at&dt=bd&dt=ex&dt=ld&dt=md&dt=qca&dt=rw&dt=rm&dt=ss&dt=t&ie=UTF-8&oe=UTF-8&source=btn&kc=0");
        buffer.append("&tk=" + tk);
        buffer.append("&q=" + word);
        try {
            String result = HttpKit.get(buffer.toString());
            JSONArray array = (JSONArray)JSONArray.parse(result);
            JSONArray rarray = array.getJSONArray(0);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < rarray.size(); i++) {
                String r = rarray.getJSONArray(i).getString(0);
                if (!StrKit.isBlank(r)) {
                    sb.append(r);
                }
            }
            
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    
    public static void main(String[] args) {
        GoogleApi googleApi = new GoogleApi();
        String result = googleApi.translate("有关糖尿病的文章", "zh", "en");
        System.out.println(result);
    }
}
