package cn.basicPLY.animals.controller;

import cn.basicPLY.animals.entity.StrayAnimalsFile;
import cn.basicPLY.animals.entity.VO.FileReturnParameterVO;
import cn.basicPLY.animals.enumerate.FileEnum;
import cn.basicPLY.animals.enumerate.UniversalColumnEnum;
import cn.basicPLY.animals.enumerate.UserEnum;
import cn.basicPLY.animals.service.StrayAnimalsFileService;
import cn.basicPLY.animals.utils.AjaxResult;
import cn.basicPLY.animals.utils.UserUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import java.util.*;

/**
 * purpose:文件相关Controller
 *
 * @author Pan Liuyang
 * 2022/4/23 16:40
 */
@Api(tags = "文件相关接口")
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${file.upload.path}")
    private String fileUploadPath;

    @Autowired
    private StrayAnimalsFileService fileService;

    private final List<String> uploadImageTypes = Arrays.asList("image/png", "image/jpg", "image/jpeg", "image/gif", "image/bmp");

    @ApiOperation("上传图片接口-Content-Type设置成multipart/form-data")
    @PostMapping("/uploadImage")
    public ResponseEntity<AjaxResult> uploadImage(@RequestParam("multipartFile") MultipartFile multipartFile,
                                                  @RequestParam("fileCategory") String fileCategory) {

        if (multipartFile.isEmpty()) {
            return new ResponseEntity<>(AjaxResult.error("请选择图片"), HttpStatus.BAD_REQUEST);
        }
        //转换文件类别为绝对路径
        String relativePath = FileEnum.getPathByTypeCode(fileCategory);
        if (StringUtils.isBlank(relativePath)) {
            return new ResponseEntity<>(AjaxResult.error("文件类别错误"), HttpStatus.BAD_REQUEST);
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

        String path = fileUploadPath + relativePath + "/" + format; //文件存储位置
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
            //文件名
            strayAnimalsFile.setFileName(multipartFile.getOriginalFilename());
            //文件路径
            strayAnimalsFile.setFilePath(relativePath + "/" + format + "/" + fileName);
            //文件类别代码
            strayAnimalsFile.setFileCategory(fileCategory);
            //创建人
            strayAnimalsFile.setCreateBy(ObjectUtils.isNotEmpty(UserUtils.getUserDetails()) && StringUtils.isNotEmpty(UserUtils.getUserDetails().getNickName()) ?
                    UserUtils.getUserDetails().getNickName() : UserEnum.USER_ADMIN.getName());
            //创建时间
            strayAnimalsFile.setCreateDate(new Date());
            if (fileService.getBaseMapper().insert(strayAnimalsFile) > 0) {
                //构建返回实体
                FileReturnParameterVO fileReturnParameterVO = new FileReturnParameterVO();
                fileReturnParameterVO.setFileId(strayAnimalsFile.getKeyId());
                fileReturnParameterVO.setFileName(strayAnimalsFile.getFileName());
                fileReturnParameterVO.setRelativePath(strayAnimalsFile.getFilePath());
                return new ResponseEntity<>(AjaxResult.success("上传成功", fileReturnParameterVO), HttpStatus.OK);
            }
            return new ResponseEntity<>(AjaxResult.error("上传失败"), HttpStatus.BAD_REQUEST);
        } catch (IOException ignored) {
        }
        return new ResponseEntity<>(AjaxResult.error("上传失败"), HttpStatus.BAD_REQUEST);
    }

    @ApiOperation("获取图片接口")
    @GetMapping("/getImage")
    public void getImage(@RequestParam String imageUrl, HttpServletResponse response) {
        if (imageUrl != null) {
            log.info("请求的图片URL：" + imageUrl);
            FileInputStream in = null;
            OutputStream out = null;
            try {
//                File file = new File(fileUploadPath + imageUrl);
//                if (!file.exists()) {
//                    throw new RuntimeException("文件不存在");
//                }
                in = new FileInputStream(fileUploadPath + imageUrl);

                //设置输出流内容格式为图片格式
                response.setContentType("image/jpeg");
                //response的响应的编码方式为utf-8
                response.setCharacterEncoding("utf-8");
                out = response.getOutputStream();

                int hasRead = 0;
                byte[] buffer = new byte[8];
                while ((hasRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, hasRead);
                }
                in.close();
                out.close();
            } catch (Exception e) {
                throw new RuntimeException("网络错误", e);
            } finally {
                try {
                    if (ObjectUtils.isNotEmpty(in)) {
                        in.close();
                    }
                    if (ObjectUtils.isNotEmpty(out)) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @ApiOperation("获取首页Banner图")
    @GetMapping("/getBanner")
    public ResponseEntity<AjaxResult> getBanner() {
        //构建首页Banner图查询条件
        QueryWrapper<StrayAnimalsFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("file_category", FileEnum.STRAY_ANIMALS_BANNER.getTypeCode()).eq(UniversalColumnEnum.DELETE_MARK.getColumn(), 1);
        //获取Banner图
        List<StrayAnimalsFile> strayAnimalsFiles = fileService.getBaseMapper().selectList(queryWrapper);
        queryWrapper.clear();
        return new ResponseEntity<>(AjaxResult.success("获取成功", strayAnimalsFiles), HttpStatus.OK);
    }
}
