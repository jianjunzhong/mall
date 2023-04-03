package com.jjzhong.mall.service.impl;

import com.jjzhong.mall.common.Constant;
import com.jjzhong.mall.exception.MallException;
import com.jjzhong.mall.exception.MallExceptionEnum;
import com.jjzhong.mall.service.UploadService;
import com.jjzhong.mall.util.FileUtils;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * 上传服务
 */
@Service
public class UploadServiceImpl implements UploadService {
    /**
     * 上传图片并返回图片的 url
     * @param file 接收到的文件
     * @param context 上下文（图像保存的父目录）
     * @return 图片的 url
     * @throws IOException IO 异常
     */
    @Override
    public String uploadImageReturnUrl(MultipartFile file, String context) throws IOException {
        File newFile = uploadFile(file, context);
        // 缩小图片
        Thumbnails.of(newFile)
                .size(Constant.IMAGE_SIZE, Constant.IMAGE_SIZE)
//                .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(Constant.FILE_UPLOAD_DIR + Constant.WATER_MARK_JPG)), Constant.IMAGE_OPACITY)
                .toFile(newFile);
        return getURI(context) + "/" + newFile.getName();
    }

    /**
     * 获取 URI
     * @param context 上下文
     * @return URI
     */
    @Override
    public URI getURI(String context) {
        URI effectiveURI;
        try {
            effectiveURI = new URI(Constant.UPLOAD_SCHEME, null, Constant.UPLOAD_HOST, Constant.UPLOAD_PORT, "/" + context, null, null);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return effectiveURI;
    }

    /**
     * 上传文件
     * @param file 传输的文件
     * @param context 上下文
     * @return File
     */
    @Override
    public File uploadFile(MultipartFile file, String context) {
        String newFilename = FileUtils.generateNewFileName(file);
        String dstDir = Constant.FILE_UPLOAD_DIR + context + "/";
        FileUtils.makeDirsIfNotExists(dstDir);
        String newFilePath = dstDir + newFilename;
        File newFile = new File(newFilePath);
        try {
            file.transferTo(newFile);
        } catch (IOException e) {
            throw new MallException(MallExceptionEnum.UPLOAD_FAILED);
        }
        return newFile;
    }

}
