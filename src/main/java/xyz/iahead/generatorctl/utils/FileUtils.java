package xyz.iahead.generatorctl.utils;

import org.springframework.util.StringUtils;

import java.io.*;
import java.util.Map;

public class FileUtils {

    public static File generatorCtl(File file, String separator, String tableName, Map<String, String> fieldType, String filePath, String newFileName) {
        FileReader fileReader;
        String[] split = null;
        try {
            fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            split = line.split(separator);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String oldFileName = file.getName();
        //新文件名
        if (StringUtils.isEmpty(newFileName)) {
            newFileName = oldFileName + ".ctl";
        }
        if (!newFileName.endsWith(".ctl")) {
            newFileName += ".ctl";
        }

        File ctlFile = new File(filePath + newFileName);
        BufferedWriter writer = null;
        try {
            FileWriter fileWriter = new FileWriter(ctlFile);
            writer = new BufferedWriter(fileWriter);
            writer.write("OPTIONS (skip=1)LOAD DATA");
            writer.newLine();
            writer.write("INFILE '");
            writer.write(oldFileName);
            writer.write("'");
            writer.newLine();
            writer.write("APPEND INTO TABLE ");
            writer.write(tableName);
            writer.newLine();
            writer.write("FIELDS TERMINATED BY '");
            writer.write(separator);
            writer.write("'");
            writer.newLine();
            writer.write("TRAILING NULLCOLS(");
            writer.newLine();
            StringBuffer sb = new StringBuffer();
            //拼接字段
            for (String s : split) {
                if (fieldType.containsKey(s)) {
                    switch (fieldType.get(s)) {
                        case "CHAR":

                            break;
                        case "DATE":

                            break;
                        case "DECIMAL EXTERNAL":

                            break;
                        case "DECIMAL":

                            break;
                        case "DOUBLE":

                            break;
                        case "FLOAT":

                            break;
                        case "FLOAT EXTERNAL":

                            break;
                        case "GRAPHIC EXTERNAL":

                            break;
                        case "INTEGER":

                            break;
                        case "INTEGER EXTERNAL":

                            break;
                        case "SMALLINT":

                            break;
                        case "VARCHAR":

                            break;
                        case "VARGRAPHIC":

                            break;

                    }
                }
                sb.append(s);
                sb.append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            writer.write(sb.toString());
            writer.write(")");
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }


}
