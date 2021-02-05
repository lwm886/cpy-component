package com.cpt.gif.core;

import com.madgag.gif.fmsware.AnimatedGifEncoder;
import com.madgag.gif.fmsware.GifDecoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
//未引用
public class GifUtils {
    public static void main(String[] args) throws Exception {

        long timestart = System.currentTimeMillis();
        InputStream inputStream1 = new FileInputStream("E:\\Workspace\\image\\gif_flag_test.gif");
        String dpath="E:\\Workspace\\image\\gif_flag_test_r.gif";
//InputStream inputStream1 = new FileInputStream("E:\\Workspace\\image\\gif_flag_test_2.gif");
//String dpath="E:\\Workspace\\image\\gif_flag_test_r_2.gif";
        String iconpath="E:\\Workspace\\tomcat\\webapps\\win_case_hover.png";
        GifUtils.getGifSticker(dpath,"测试",inputStream1,iconpath,0,155,156);
        long timeend = System.currentTimeMillis();
    }
    public static void getGifSticker(String desPath, InputStream inputStream, String iconPath)throws FontFormatException, IOException{
        getGifSticker(desPath,"测试",inputStream,iconPath,0,155,156);
    }
    public static void getGifSticker(String url,String markContent, InputStream inputStream, String iconPath,Integer degree, int srcWidth, int srcHeight) throws FontFormatException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            if(srcWidth < 131 || srcHeight < 18 ) {
                System.out.println("图片尺寸小于水印尺寸，跳过！");
                return;
            }
            GifDecoder decoder = new GifDecoder();
//读入gif数据流
            decoder.read(inputStream);
//获取GIF的宽高
            Dimension dimension = decoder.getFrameSize();
            int height = (int)dimension.getHeight();
//获取字体文件数据流，用于规范生成文字的字体格式
//InputStream intput = GifUtils.class.getResourceAsStream(fontName);
//生成字体
            Font font = new Font("宋体",Font.TRUETYPE_FONT,18);//Font.createFont(Font.TRUETYPE_FONT, intput);
//要是想使用deriveFont设置字体大小必须重新指定Font，而且不支持整数型，只能使用浮点类型
//Font font = font1.deriveFont(25.0f);
//读取帧数
            int frameCount = decoder.getFrameCount();
            AnimatedGifEncoder encoder = new AnimatedGifEncoder();
//String url = "C:\\Users\\TMP\\o\\" + + System.currentTimeMillis()+".gif";
            encoder.start(url);
            encoder.setRepeat(0);
            Graphics2D g = null;
            ImageIcon imgIcon = new ImageIcon(iconPath);
// 得到Image对象。
            Image img = imgIcon.getImage();
            float alpha = 1f; // 透明度
/**
 * 对GIF进行拆分
 * 每一帧进行文字处理
 * 组装
 */
            for (int i = 0; i < frameCount; i++) {
                BufferedImage buffImg=decoder.getFrame(i);
//g=buffImg.createGraphics();
                g=(Graphics2D) buffImg.getGraphics();
//初始化图像
//g = (Graphics2D) decoder.getFrame(i).getGraphics();
/**
 * RenderingHint是对图片像素，锯齿等等做的优化，可保证生成的图片放大锯齿点阵也不会很明显
 */
//g.setRenderingHint(SunHints.KEY_ANTIALIASING, SunHints.VALUE_ANTIALIAS_ON);
//g.setRenderingHint(SunHints.KEY_TEXT_ANTIALIASING, SunHints.VALUE_TEXT_ANTIALIAS_DEFAULT);
// g.setRenderingHint(SunHints.KEY_STROKE_CONTROL, SunHints.VALUE_STROKE_PURE);
// g.setRenderingHint(SunHints.KEY_TEXT_ANTIALIAS_LCD_CONTRAST, 100);
// g.setRenderingHint(SunHints.KEY_FRACTIONALMETRICS, SunHints.VALUE_FRACTIONALMETRICS_OFF);
// g.setRenderingHint(SunHints.KEY_RENDERING, SunHints.VALUE_RENDER_DEFAULT);
//g.setColor(Color.black);
//g.setFont(font);
// 设置对线段的锯齿状边缘处理
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g.setComposite(AlphaComposite.Src);
                g.drawImage(buffImg.getScaledInstance(buffImg.getWidth(null), buffImg
                        .getHeight(null), Image.SCALE_SMOOTH), 0, 0, null);
                if (null != degree) {
// 设置水印旋转
                    g.rotate(Math.toRadians(degree),
                            (double) buffImg.getWidth() / 2, (double) buffImg.getHeight() / 2);
                }
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,alpha));
//表示水印图片的位置，叠加图层
                g.drawImage(img, 12, 10, null);;
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
//设置打印文字和坐标,此处应该和图片二选一
                if(StringUtils.isNotBlank(markContent)){
//水印文字
                    g.setColor(Color.black);
                    g.setFont(font);
                    g.drawString(markContent, 12, 22);
                }
                g.dispose();
//组装每一帧
                encoder.addFrame(buffImg);
//设置每帧的切换时间
                if (i != frameCount - 1) {
                    encoder.setDelay(decoder.getDelay(i));
                }
            }
            encoder.finish();
            byte b[] = outputStream.toByteArray();
        }finally {
            try {
                if (null != outputStream)
                    outputStream.close();
            } catch (IOException e) {
            }
        }
    }
}