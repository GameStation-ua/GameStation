package webpage.entity;

import org.apache.commons.io.FileUtils;
import spark.Request;
import spark.Response;

import javax.imageio.ImageIO;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static webpage.handlers.AbstractHandler.returnJson;
import static webpage.util.ServerInitializer.imagesPath;

public class Images {

    public static final int DEFAULT_BUFFER_SIZE = 8192;

    public static void saveImg(String directory, Request req) throws IOException, ServletException {
        req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
        try (InputStream is = req.raw().getPart("uploaded_file").getInputStream()) {
            File file = new File(directory);
            copyInputStreamToFile(is, file);
        }
    }
    public static void moveImagesFromRequest(File srcDir, File destDir) throws IOException {

//        FileUtils.deleteDirectory(destDir);

//        FileUtils.moveDirectory(srcDir, destDir);
        String[] list = srcDir.list();
        for (String s : list) {
            FileUtils.copyFileToDirectory(new File(srcDir.toString() + "/" + s), destDir);
        }
    }

    public static boolean rescale(String directory, int width, int height) {
        try {
            rescale(new FileInputStream(Paths.get(directory).toFile()), Paths.get(directory), width, height);
        }catch (IOException e){
            return false;
        }
        return true;
    }

    public static void copyInputStreamToFile(InputStream inputStream, File file) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(file, false)) {
            int read;
            byte[] bytes = new byte[DEFAULT_BUFFER_SIZE];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        }
    }

    public static boolean uploadAndRescale(Request req, String directory, int width, int height) throws IOException {
        try {
            saveImg(directory, req);
        if (rescale(directory,width, height)) {
            return true;
        }
        }catch (ServletException e){
            return false;
        }
        return false;
    }

    public static String upload(Request req, Response res, int width, int height, String dir) throws IOException {
            if (uploadAndRescale(req, dir, width, height)) {
                res.status(500);
                return returnJson(res, 200, "Image uploaded");
            }else {
                throw new IOException("Upload error");
            }
    }

    public static void rescale(InputStream input, Path target,
                               int width, int height) throws IOException {


            BufferedImage originalImage = ImageIO.read(input);

            BufferedImage newResizedImage
                    = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = newResizedImage.createGraphics();

            g.setComposite(AlphaComposite.Src);
            g.fillRect(0, 0, width, height);

            Map<RenderingHints.Key,Object> hints = new HashMap<>();
            hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_DEFAULT);
            hints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.addRenderingHints(hints);

            // puts the original image into the newResizedImage
            g.drawImage(originalImage, 0, 0, width, height, null);
            g.dispose();

            // get file extension
            String s = target.getFileName().toString();
            String fileExtension = s.substring(s.lastIndexOf(".") + 1);

            // we want image in png format
            ImageIO.write(newResizedImage, fileExtension, target.toFile());
        }

        public static byte[] findImg(String path) throws IOException {
            BufferedImage bi = ImageIO.read(new File(imagesPath + path));

            byte[] rawImage = null;
            try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                ImageIO.write( bi, "png", baos );

                baos.flush();
                rawImage = baos.toByteArray();
            }
            return rawImage;
        }
}
