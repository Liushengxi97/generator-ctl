package xyz.iahead.generatorctl.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import xyz.iahead.generatorctl.model.Result;
import xyz.iahead.generatorctl.utils.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/generator")
public class GeneratorController {

    @Value("${file.path}")
    private String filePath;
    @Value("${file.temp}")
    private String fileTemp;

    @PostMapping("/ctl")
    @ResponseBody
    public Result getCtl(List<MultipartFile> files){
        List<String> filesUrl = new ArrayList<>();
        for (MultipartFile file : files) {
            //转成file,暂存在临时存储路径
            String tempFile = null;
            InputStream is = null;
            FileOutputStream fileOutputStream = null;
            try {
                is = file.getInputStream();
                String tempFilePath = filePath + fileTemp;
                tempFile = tempFilePath + file.getOriginalFilename();
                File tempPath = new File(tempFilePath);
                if(!tempPath.exists()){
                    tempPath.mkdirs();
                }
                fileOutputStream = new FileOutputStream(tempFile);
                byte[] bytes = new byte[1024 * 1024];
                while (is.read(bytes) > 0){
                    fileOutputStream.write(bytes);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }finally {
                try {
                    is.close();
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Map<String, Map<String, String>> fieldType = new HashMap<>();
            Map<String, String> field1 = new HashMap<>();
            field1.put("type", "DECIMAL EXTERNAL");
            fieldType.put("SCMJ", field1);
            Map<String, String> field2 = new HashMap<>();
            field2.put("type", "DECIMAL EXTERNAL");
            fieldType.put("SCMJM", field2);
            String fileUrl = FileUtils.generatorCtl(new File(tempFile),"|", null, fieldType, filePath, null);
            filesUrl.add(fileUrl);
            //删除临时胜场的文件
            new File(tempFile).delete();
        }

        return Result.ok(filesUrl);
    }

}
