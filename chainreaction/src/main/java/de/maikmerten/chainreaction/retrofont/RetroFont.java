package de.maikmerten.chainreaction.retrofont;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author maik
 */
public class RetroFont {
	
	BufferedImage font;
	
	public RetroFont() {
		try {
			BufferedImage fontimg = ImageIO.read(this.getClass().getResourceAsStream("/retrofont.png"));
			font = new BufferedImage(fontimg.getWidth(), fontimg.getHeight(), BufferedImage.TYPE_INT_ARGB);
			font.getGraphics().drawImage(fontimg, 0, 0, null);
			
			for(int x = 0; x < font.getWidth(); ++x) {
				for(int y = 0; y < font.getHeight(); ++y) {
					// punch all black pixels transparent
					if(font.getRGB(x, y) == (0xFF << 24)) {
						font.setRGB(x, y, 0);
					}
				}
			}
		
		} catch (IOException ex) {
			throw new RuntimeException("cannot read retro font", ex);
		}
	}
	
	
	public BufferedImage getRetroChar(char c) {
		int code = (int)c;
		if(code < 0 || c > 255) {
			code = (int)'?';
		}
		
		int row = code >> 4;
		int col = code % 16;
		
		return font.getSubimage(col << 3, row << 3, 8, 8);
	}
	
	public BufferedImage getRetroChar(char c, int scalefactor) {
		scalefactor = scalefactor < 1 ? 1 : scalefactor;
		
		Image img = getRetroChar(c).getScaledInstance(8 * scalefactor, 8 * scalefactor, Image.SCALE_REPLICATE);
		BufferedImage bimg = new BufferedImage(img.getWidth(null), img.getHeight(null), font.getType());
		bimg.getGraphics().drawImage(img, 0, 0, null);
		return bimg;
	}
	
	
	public static void main(String[] args) throws Exception {
		RetroFont rf = new RetroFont();
		ImageIO.write(rf.getRetroChar('A', 8), "png", new File("/tmp/test.png"));
	}
	
	
}