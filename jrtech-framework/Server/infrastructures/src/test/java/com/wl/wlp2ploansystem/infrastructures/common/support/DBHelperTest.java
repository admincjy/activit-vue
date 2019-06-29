package com.wl.wlp2ploansystem.infrastructures.common.support;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DBHelperTest {


    @Test
    public  void testA()
    {
        String script = "mysqldump --opt --single-transaction=TRUE  --host={0} --protocol=tcp --port={1}  --comments --default-character-set=utf8 --single-transaction=TRUE  -R --hex-blob --flush-logs  --routines --events --triggers {2} --result-file={3}";

        String host = "localhost";
        String port = "3306";
        String dbBakName = "/www/app/jrtech/dbbak";
        String fileName = IdGenerator.get()+".sql";
        String saveFilePath = dbBakName+"/"+fileName;

        String newScipt = MessageFormat.format(script, host, port, dbBakName, saveFilePath);
        System.out.println(newScipt);
    }
    @Test
    public  void testCMD()
    {
        List<String> params = new ArrayList<>();
        params.add("mysqldump");

        ProcessBuilder processBuilder = new ProcessBuilder(params);
        processBuilder.redirectErrorStream(true);
        Process process = null;
        try {
            process = processBuilder.start();
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), Charset.forName("UTF-8")));
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            int exitCode = process.waitFor();
            System.out.println("exitCode = "+exitCode);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
           if(process !=null){
               process.destroy();
           }
        }

    }
    @Test
    public void threadBak(){
        Thread td = new Thread() {
            public void  run() {
                bak();
            }
        };
        td.start();
        try {
            td.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Test
    public  void bak(){
        List<String> scripts = Arrays.asList("mysqldump"
                ,"--opt"
                ,"--single-transaction=TRUE"
                ,"--host={0}"
                ,"--protocol=tcp"
                ,"--port={1}"
                ,"--comments"
                ,"--default-character-set=utf8"
                ," --single-transaction=TRUE"
                ,"-R"
                ,"--hex-blob"
                ,"--flush-logs"
                ,"--routines"
                ,"--events"
                ,"--triggers"
                ,"{2}"
                ,">"
                ,"{3}");

       String script = "D:\\mysql\\bin\\mysqldump --opt --single-transaction=TRUE  --host={0} --protocol=tcp --port={1}  --comments --default-character-set=utf8 --single-transaction=TRUE  -R --hex-blob --flush-logs  --routines --events --triggers {2}";
        try {
            DBHelper.bak(script,"localhost","3306","root","qaz.1234","D:\\fileupload\\dbbak",IdGenerator.get()+".sql","jrtech");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
