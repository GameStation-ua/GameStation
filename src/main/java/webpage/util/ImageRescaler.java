package webpage.util;

import com.mortennobel.imagescaling.ProgressListener;
import com.mortennobel.imagescaling.ResampleOp;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageRescaler {

    public void rescale(String[] args){
        if (args.length!=4){
            System.exit(1);
        }

        try
        {
            String sourcefile = args[0];
            String destfile = args[1];
            int newwidth = Integer.parseInt(args[2]);
            int newheight = Integer.parseInt(args[3]);

            BufferedImage sourceImage = ImageIO.read(new File(sourcefile));
            ResampleOp resizeOp = new ResampleOp(newwidth, newheight);
            BufferedImage resizedImage = resizeOp.filter(sourceImage, null);
            ImageIO.write(resizedImage,"png", new File(destfile));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
