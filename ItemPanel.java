import javax.swing.JPanel; // JPanelを使用
import java.awt.Graphics;  // paintComponentでGraphicsを使用
import java.awt.Color;     // 色設定でColorを使用
import java.awt.Image;     // 画像表示でImageを使用
import java.io.File;       // 画像ファイル読み込みでFileを使用
import java.io.IOException; // 例外処理でIOExceptionを使用
import javax.imageio.ImageIO; // ImageIOを使用して画像を読み込み

/// アイテムクラス。得るとスコアが上がる的な。
public class ItemPanel extends JPanel{
    // メンバ変数
    int x, y, width, height; // x=-1, 0, 1で位置決定
    Image image;
    
    // コンストラクタ
    public ItemPanel(){
        x = 0; y = 0;
        width = 100; height = 100;
        try{
            image = ImageIO.read(getClass().getResourceAsStream("itemyen.png"));
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

    public void updateItemPos(int itemPosY) {
        this.y = itemPosY;
        repaint();
    }

    public void setItemPos(int itemPosX, int itemPosY) {
        this.x = itemPosX;
        this.y = itemPosY;
        repaint();
    }

    public void hideItem() {
        setVisible(false);
    }
}

