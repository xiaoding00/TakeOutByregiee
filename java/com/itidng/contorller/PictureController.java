package com.itidng.contorller;


import com.itidng.result.R;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.util.IOTools;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;


@Slf4j
@RestController
@RequestMapping("common")
public class PictureController {

    private String pathName = "D:\\code\\NewIdeaCode\\kunTakeOut\\src\\main\\resources\\image\\";

    /**
     * 保存页面上传的图片
     *
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("upload")
    public R<String> uploadPicture(MultipartFile file) throws IOException {
        log.info("图片上传：-------" + file.toString());
        //获取后缀名并且使用UUID随机生成图片名称防止覆盖
        String pictureName = UUID.randomUUID().toString() + (file.getName().lastIndexOf("."));
        //创建图片存储地点
        File dir = new File(pathName);
        if (!dir.exists()) {
            dir.mkdir();
        }
        //将其存储在指定位置
        file.transferTo(new File(pathName + pictureName));
        //需要返回图片信息
        return new R<>(1, pictureName, "测试上传");
    }


    /**
     * 回显图片到浏览器上
     *
     * @param name
     * @param response
     * @throws IOException
     */
    @GetMapping("download")
    public void downloadPicture(@PathParam("name") String name, HttpServletResponse response) throws IOException {
        //输入流将图片写入内存中
        FileInputStream fileInputStream = new FileInputStream(pathName + name);
        //设置浏览器输出流将图片输出到浏览器中
        ServletOutputStream outputStream = response.getOutputStream();
        response.setContentType("image/jpg");
        //用工具栏将内存中的图片写进输出流中
        IOTools.flow(fileInputStream, outputStream);
    }
}
