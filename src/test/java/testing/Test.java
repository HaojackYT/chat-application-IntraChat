package testing;

import com.example.swing.blurHash.BlurHash;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Test {
    
    public static void main(String[] args) {
        try {
            // LLMs_{?d_49GD%WEV@s;-oD$DjRP = sleepy-cat.jpg
            // LEF}l]$k9u-o02~VRj9ZNs56xa%1 = transparent-cat.png
            BufferedImage image = ImageIO.read(new File(""));
            String blurhashStr = BlurHash.encode(image);
            System.out.println(blurhashStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
