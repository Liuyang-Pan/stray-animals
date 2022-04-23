package cn.basicPLY.animals.controller;

import cn.basicPLY.animals.entity.StrayAnimalsFile;
import cn.basicPLY.animals.enumerate.FileEnum;
import cn.basicPLY.animals.enumerate.UserEnum;
import cn.basicPLY.animals.service.StrayAnimalsFileService;
import cn.basicPLY.animals.units.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * purpose:文件相关Controller
 *
 * @author Pan Liuyang
 * 2022/4/23 16:40
 */
@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${file.upload.path}")
    private String fileUploadPath;

    @Autowired
    private StrayAnimalsFileService fileService;

    private final List<String> uploadImageTypes = Arrays.asList("image/png", "image/jpg", "image/jpeg", "image/gif", "image/bmp");

    @PostMapping("/uploadImage")
    public ResponseEntity<AjaxResult> uploadImage(@RequestParam("multipartFile") MultipartFile multipartFile,
                                                  @RequestParam("fileCategory") String fileCategory) {

        if (multipartFile.isEmpty()) {
            return new ResponseEntity<>(AjaxResult.error("请选择图片"), HttpStatus.BAD_REQUEST);
        }

        //检查格式
        String fileType = multipartFile.getContentType();
        log.info("文件类型：" + fileType);
        boolean flag = false;
        for (String type : uploadImageTypes) {
            if (fileType.equals(type)) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            return new ResponseEntity<>(AjaxResult.error("请选择 png、jpg、jpeg、gif、bmp 格式的图片"), HttpStatus.BAD_REQUEST);
        }

        //检查文件大小
        double imageSize = (double) multipartFile.getSize() / 1024 / 1024;
        NumberFormat nf = new DecimalFormat("0.00");
        log.info("图片大小：" + Double.parseDouble(nf.format(imageSize)) + "M");
        if (imageSize > 20) {
            return new ResponseEntity<>(AjaxResult.error("请上传" + 20 + "M以内图片"), HttpStatus.BAD_REQUEST);
        }

        String fileName = multipartFile.getOriginalFilename();//文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));//后缀名

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
        String format = simpleDateFormat.format(new Date());

        String path = fileUploadPath + "/" + format; //文件存储位置
        fileName = UUID.randomUUID() + suffixName;//图片名
        String imageUrl = path + "/" + fileName;//图片的url
        log.info("图片URL：" + imageUrl);
        File dest = new File(imageUrl);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            multipartFile.transferTo(dest);
            //上传文件相关数据写入数据库
            StrayAnimalsFile strayAnimalsFile = new StrayAnimalsFile();
            strayAnimalsFile.setFileName(multipartFile.getOriginalFilename());
            strayAnimalsFile.setFilePath(FileEnum.STRAY_ANIMALS_ADOPTION.getRelativePath() + format + fileName);
            strayAnimalsFile.setCreateBy(UserEnum.USER_ADMIN.getName());
            strayAnimalsFile.setCreateDate(new Date());
            if (fileService.getBaseMapper().insert(strayAnimalsFile) > 0) {
                return new ResponseEntity<>(AjaxResult.success("上传成功", imageUrl), HttpStatus.OK);
            }
            return new ResponseEntity<>(AjaxResult.error("上传失败"), HttpStatus.BAD_REQUEST);
        } catch (IOException ignored) {
        }
        return new ResponseEntity<>(AjaxResult.error("上传失败", imageUrl), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/getImage")
    public void getImage(@RequestParam String imageUrl, HttpServletResponse response) {
        if (imageUrl != null) {
            log.info("请求的图片URL：" + imageUrl);
            FileInputStream in = null;
            OutputStream out = null;
            try {
                File file = new File(imageUrl);
                if (!file.exists()) {
                    throw new RuntimeException("文件不存在");
                }
                in = new FileInputStream(imageUrl);
                int i = in.available();
                byte[] buffer = new byte[i];
                in.read(buffer);
                //设置输出流内容格式为图片格式
                response.setContentType("image/jpeg");
                //response的响应的编码方式为utf-8
                response.setCharacterEncoding("utf-8");
                out = response.getOutputStream();
                out.write(buffer);
            } catch (Exception e) {
                throw new RuntimeException("网络错误", e);
            } finally {
                try {
                    in.close();
                    out.close();
                } catch (IOException e) {
                    throw new RuntimeException("网络错误", e);
                }
            }
        }
    }
}
