package xyz.iahead.generatorctl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import xyz.iahead.generatorctl.model.Result;

import java.util.List;

@Controller
@RequestMapping("/generator")
public class GeneratorController {

    @PostMapping("/ctl")
    public Result getCtl(List<MultipartFile> files){

        return Result.ok(null);
    }



}
