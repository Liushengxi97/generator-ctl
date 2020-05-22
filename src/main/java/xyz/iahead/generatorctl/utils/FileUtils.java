package xyz.iahead.generatorctl.utils;

import org.springframework.util.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileUtils {

    private static List<String> SpecialChar = null;

    static {
        if (SpecialChar == null) {
            SpecialChar = new ArrayList<>();
            SpecialChar.add("|");
            SpecialChar.add(".");
            SpecialChar.add("\\");
            SpecialChar.add("^");
            SpecialChar.add("&");
        }
    }

    /**
     * @param file        生成ctl文件的依据
     * @param separator   字段之间的分隔符
     * @param tableName   需要入库的表名
     * @param fieldType   {key:{type:"date", remark:"YYYY-MM-DD HH24:MI:SS"}}
     * @param filePath    ctl文件存储位置
     * @param newFileName ctl文件名
     * @return
     */
    public static String generatorCtl(File file, String separator, String tableName, Map<String, Map<String, String>> fieldType, String filePath, String newFileName) {
        UnicodeReader ur = null;
        FileInputStream fis = null;
        String[] split = null;
        try {
            //去除BOM标识
            fis = new FileInputStream(file);
            ur = new UnicodeReader(fis, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(ur);
            String line = bufferedReader.readLine();
            if (SpecialChar.contains(separator)) {
                String newSeparator = "\\" + separator;
                split = line.split(newSeparator);
            } else {
                split = line.split(separator);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                ur.close();
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //获取不带后缀的文件名 example.csv
        String oldFileName = file.getName();
        int i = oldFileName.indexOf(".");
        String realName = null;
        if (i > 0) {
            realName = oldFileName.substring(0, i);
        }
        //新文件名，如果位null，与旧文件同名
        if (StringUtils.isEmpty(newFileName)) {
            newFileName = realName + ".ctl";
        }
        if (!newFileName.endsWith(".ctl")) {
            newFileName += ".ctl";
        }
        String ctlFilePath = filePath + newFileName;
        File ctlFile = new File(ctlFilePath);
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
            //若表名为空，以无后缀文件名作为表名
            if (StringUtils.isEmpty(tableName)) {
                writer.write(realName);
            } else {
                writer.write(tableName);
            }
            writer.newLine();
            writer.write("FIELDS TERMINATED BY '");
            writer.write(separator);
            writer.write("'");
            writer.newLine();
            writer.write("TRAILING NULLCOLS");
            writer.newLine();
            writer.write("(");
            StringBuffer sb = new StringBuffer();

            //拼接字段
            for (String s : split) {
                if (fieldType.containsKey(s)) {
                    Map<String, String> fieldInfo = fieldType.get(s);
                    String type = fieldInfo.get("type");
                    switch (type) {
                        case "DATE":
                            String remark = fieldInfo.get("remark");
                            sb.append(s);
                            sb.append(' ');
                            sb.append("\"");
                            sb.append(remark);
                            sb.append("\"");
                            break;
                        default:
                            sb.append(s);
                            sb.append(' ');
                            sb.append(type);
                            sb.append(',');
                    }
                } else {
                    sb.append(s);
                    sb.append(",");
                }
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(" TERMINATED BY WHITESPACE");
            writer.write(sb.toString());
            writer.write(")");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return ctlFilePath;
    }

}
