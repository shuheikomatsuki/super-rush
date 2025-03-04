import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/// アイテムクラス。得るとスコアが上がる的な。
public class ShieldPanel extends JPanel{
    // メンバ変数
    int x, y, width, height; // x=-1, 0, 1で位置決定
    Image image;
    
    // コンストラクタ
    public ShieldPanel(){
        x = 0; y = 0;
        width = 100; height = 100;
        try{
            image = ImageIO.read(getClass().getResourceAsStream("shield(1).png"));
        }catch(IOException e){
            e.printStackTrace();
        }
        setOpaque(false);//背景を透過するやつ。
    }
    
    public void paintComponent(Graphics g){
        int offsetX = 250, offsetY = -100;
        super.paintComponent(g);
        if(image!=null){
            g.drawImage(image, x*200+offsetX-40, y*1+offsetY, getFocusCycleRootAncestor());
        }else{
            g.setColor(Color.PINK);
            g.fillOval(x*200+offsetX, (int)(y*1.00+offsetY), width, height);

        }
    }

    public void updateShieldPos(int shieldPosY) {
        this.y = shieldPosY;
        repaint();
    }

    public void setShieldPos(int shieldPosX, int shieldPosY) {
        this.x = shieldPosX;
        this.y = shieldPosY;
        repaint();
    }

        public void hideShield() {
        setVisible(false);
    }
}

