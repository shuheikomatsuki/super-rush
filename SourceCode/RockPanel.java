import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/// 岩クラス
public class RockPanel extends JPanel{
    // メンバ変数
    int x, y, width, height; // x=-1, 0, 1で位置決定
    Image image;
    
    // コンストラクタ
    public RockPanel(){
        x = 0; y = 0;
        width = 100; height = 100;
        try{
            image = ImageIO.read(getClass().getResourceAsStream("Stone(1).png"));
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
            //顔挿入時はoffsetX-20
        }else{
            g.setColor(Color.ORANGE);
            g.fillOval(x*200+offsetX, (int)(y*1.00+offsetY), width, height);
        }
    }

    public void updateRockPos(int rockPosY) {
        this.y = rockPosY;
        repaint();
    }

    public void setRockPos(int rockPosX, int rockPosY) {
        this.x = rockPosX;
        this.y = rockPosY;
        repaint();
    }

    public void hideRock() {
        setVisible(false);
    }
}


