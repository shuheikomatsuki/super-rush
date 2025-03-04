import javax.swing.JFrame;//アプリケーションのメインウィンドウ。
import javax.swing.JPanel;//画面の分割やコンポーネントの配置用パネル。
import javax.swing.JLabel;//テキストや画像を表示するためのラベル
import javax.swing.JButton;//ボタンを作成してクリック操作を受け取るため
import javax.swing.JLayeredPane;//パネルを重ねて表示するためのコンポーネント。
import java.awt.Graphics;//カスタム描画で使用。paintComponent メソッド内でグラフィックス処理を行う。
import java.awt.Color;//カラー設定
import java.awt.Font;//テキストのフォント設定に使用。
import java.awt.BorderLayout;  // BorderLayoutのため
import java.awt.Component;      // Componentクラスのため
import java.awt.Dimension;//コンポーネントのサイズを指定するため。
import javax.swing.border.LineBorder;//パネルやコンポーネントに境界線を設定するため
import java.awt.event.*;//はるとの実装終わったら消す．

//こっちをいじる。
// Vはユーザーに情報を表示する(主にJ~~関係)
// Vは、Mからデータを取得、Cの入力によって表示を変える

public class View extends JFrame {
    private LanePanel l;
    private PlayerPanel playerPanel;
    private RockPanel rockPanel;
    private JLayeredPane layeredPane;
    private ScorePanel scorePanel;
    private JPanel homePanel;
    private JPanel gameOverPanel;
    private ShieldLifePanel shieldLifePanel;
    public JButton startButton, retryButton, homeButton;
    public ShieldPanel shieldPanel;
    public ItemPanel itemPanel;

    public View() {

        //フレーム設定
        setTitle("A Thief Rush");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 1000);
        setLayout(new BorderLayout());

        // JLayeredPaneを使用．パネルを重ねて描画するときに使うらしい．
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(600, 1000));
        add(layeredPane, BorderLayout.CENTER);

        // ホーム画面パネル
        homePanel = createHomeScreen();
        homePanel.setBounds(0, 0, 600, 1000);
        layeredPane.add(homePanel, JLayeredPane.DEFAULT_LAYER); // 一番下のレイヤー

        // ゲームオーバーパネル
        gameOverPanel = createGameOverScreen();
        gameOverPanel.setBounds(0, 0, 600, 1000);
        layeredPane.add(gameOverPanel, JLayeredPane.DEFAULT_LAYER); // 一番下のレイヤー
        gameOverPanel.setVisible(false); // 最初は非表示

        //背景
        JPanel lanePanel = new JPanel();
        lanePanel.setLayout(null); // nullにすると配置とサイズを手動で制御できる
        lanePanel.setOpaque(false);//背景を透過するやつ。各JPanelに入れる。
        lanePanel.setBounds(0, 0, 600, 1000);
        layeredPane.add(lanePanel, JLayeredPane.DEFAULT_LAYER);
                
        for (int i = 0; i <= 2; i++) {
            l = new LanePanel();
            l.setBounds(i * 200, 0, 200, 1000); // 3つの列として配置
            l.setBorder(new LineBorder(Color.white, 3));
            lanePanel.add(l);
        }
        
        //プレーヤー
        playerPanel = new PlayerPanel();
        playerPanel.setBounds(0, 0, 600, 1000); // フルサイズに調整
        layeredPane.add(playerPanel, JLayeredPane.PALETTE_LAYER); // プレイヤーレイヤー

        //スコアパネル
        scorePanel = new ScorePanel();
        scorePanel.setBounds(0,0,600,50); // スコアは600☓50のサイズに固定
        layeredPane.add(scorePanel, JLayeredPane.DRAG_LAYER);//DRAG_LAYERによってほかのすべての要素より全面にスコアパネルが表示される。
        
        //盾残機
        shieldLifePanel = new ShieldLifePanel();
        shieldLifePanel.setBounds(0,0,100,100); // スコアは50☓50のサイズに固定
        layeredPane.add(shieldLifePanel, JLayeredPane.DRAG_LAYER);

        // 初期表示はホーム画面
        setHomeScreenVisible(true);
        setGameOverScreenVisible(false);
        shieldLifePanel.hideShieldLife();

        //デフォルトでwindow自体をfocusする(キー入力のため)
        this.setFocusable(true);
        this.requestFocusInWindow();
        
        setVisible(true);
    }

    ////////////////////////////////////////////////////////////////

    // ホーム画面作成
    public JPanel createHomeScreen() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setOpaque(false); // 背景透過

        JLabel titleLabel = new JLabel("A Thief Rush", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 70));
        titleLabel.setBounds(0, 200, 600, 100);
        panel.add(titleLabel);

        startButton = new JButton("Start Game");
        startButton.setBounds(200, 400, 200, 50);

        // ActionListenerはControllerで一括管理(はまぶー)
        // startButton.addActionListener(new ActionListener() {
        //     @Override
        //     public void actionPerformed(ActionEvent e) {
        //         startGame();  // ゲーム開始処理
        //     }
        // });
        panel.add(startButton);

        return panel;
    }

    // ゲームオーバー画面作成
    public JPanel createGameOverScreen() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setOpaque(false);

        JLabel gameOverLabel = new JLabel("You Lose...", JLabel.CENTER);
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 70));
        gameOverLabel.setBounds(0, 200, 600, 100);
        panel.add(gameOverLabel);

        retryButton = new JButton("Retry");
        retryButton.setBounds(200, 350, 200, 50);
        retryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                retryGame();  // ゲームリトライ処理
            }
        });
        panel.add(retryButton);

        homeButton = new JButton("End Game");
        homeButton.setBounds(200, 450, 200, 50);

        // ActionListenerはControllerで一括管理(はまぶー)
        // homeButton.addActionListener(new ActionListener() {
        //     @Override
        //     public void actionPerformed(ActionEvent e) {
        //         backToTitle();
        //     }
        // });
        panel.add(homeButton);

        return panel;
    }

    // ゲーム開始処理
    public void startGame() {
        setHomeScreenVisible(false);  // ホーム画面非表示
        setGameOverScreenVisible(false);  // ゲームオーバー画面非表示
    }

    // ゲームリトライ処理
    public void retryGame() {
        initialize();
        setHomeScreenVisible(false);
        setGameOverScreenVisible(false);
    }

    // タイトルへ戻る処理
    public void backToTitle() {
        setHomeScreenVisible(true);
        setGameOverScreenVisible(false);
        initialize();
    }

    // ホーム画面の表示・非表示
    public void setHomeScreenVisible(boolean visible) {
        homePanel.setVisible(visible);
    }

    // ゲームオーバー画面の表示・非表示
    public void setGameOverScreenVisible(boolean visible) {
        layeredPane.add(gameOverPanel, JLayeredPane.DRAG_LAYER); // 最前面のレイヤー
        gameOverPanel.setVisible(visible);
    }



    ///////////////////////////////////////////////////////////////////
    public ShieldLifePanel getShieldLifePanel() {
        return shieldLifePanel;
    }

    public RockPanel getRockPanel() {
        return rockPanel;
    }

    public PlayerPanel getPlayerPanel() {
        return playerPanel;
    }

    public ScorePanel getScorePanel() {
        return scorePanel;
    }

    public ShieldPanel getShieldPanel() {
        return shieldPanel;
    }

    public ItemPanel getItemPanel() {
        return itemPanel;
    }

    public JLayeredPane getJLayeredPane() {
        return layeredPane;
    }

    public JButton getStartButton(){
        return startButton;
    }

    public JButton getRetryButton(){
        return retryButton;
    }

    public JButton getHomeButton(){
        return homeButton;
    }

    public RockPanel addRock(int posX, int posY){
        rockPanel = new RockPanel();
        rockPanel.setBounds(0, 0, 600, 1000); // フルサイズに調整
        rockPanel.setRockPos(posX, posY);
        layeredPane.add(rockPanel, JLayeredPane.PALETTE_LAYER);
        return rockPanel;
    }

    public ItemPanel addItem(int posX, int posY){
        itemPanel = new ItemPanel();
        itemPanel.setBounds(0, 0, 600, 1000); // フルサイズに調整
        itemPanel.setItemPos(posX, posY);
        layeredPane.add(itemPanel, JLayeredPane.PALETTE_LAYER);
        return itemPanel;
    }

    public ShieldPanel addShield(int posX, int posY){
        shieldPanel = new ShieldPanel();
        shieldPanel.setBounds(0, 0, 600, 1000); // フルサイズに調整
        shieldPanel.setShieldPos(posX, posY);
        layeredPane.add(shieldPanel, JLayeredPane.PALETTE_LAYER);
        return shieldPanel;
    }

///////////////////////////////////////////////////////////////////////////

    public void initializePanel() {
        //ShieldLifePanel, PlayerPanel ScorePanelを初期化
        shieldLifePanel.hideShieldLife();
        layeredPane.remove(playerPanel);
        layeredPane.remove(scorePanel);

        // これらのパネルを再初期化

        playerPanel = new PlayerPanel();
        playerPanel.setBounds(0, 0, 600, 1000); // サイズを調整
        layeredPane.add(playerPanel, JLayeredPane.PALETTE_LAYER); // プレイヤーレイヤー

        scorePanel = new ScorePanel();
        scorePanel.setBounds(0,0,600,50); // スコアは600☓50のサイズに固定
        layeredPane.add(scorePanel, JLayeredPane.DRAG_LAYER);//DRAG_LAYERによってほかのすべての要素より全面にスコアパネルが表示される。

        // 再描画
        layeredPane.revalidate();
        layeredPane.repaint();
    }

    public void initializeRock() {
        // 現在のレイヤードペイン内の全コンポーネントを取得
        Component[] components = layeredPane.getComponents();
        // コンポーネントを逆順にチェックして削除（安全に削除するため）
        for (int i = components.length - 1; i >= 0; i--) {
            if (components[i] instanceof RockPanel) {
                layeredPane.remove(components[i]);
            }
        }
        // レイヤードペインを再描画して変更を反映
        layeredPane.revalidate();
        layeredPane.repaint();
    }

    public void initializeItem() {
        // 現在のレイヤードペイン内の全コンポーネントを取得
        Component[] components = layeredPane.getComponents();
        // コンポーネントを逆順にチェックして削除（安全に削除するため）
        for (int i = components.length - 1; i >= 0; i--) {
            if (components[i] instanceof ItemPanel) {
                layeredPane.remove(components[i]);
            }
        }
        // レイヤードペインを再描画して変更を反映
        layeredPane.revalidate();
        layeredPane.repaint();
    }

    public void initializeShield() {
        // 現在のレイヤードペイン内の全コンポーネントを取得
        Component[] components = layeredPane.getComponents();
        // コンポーネントを逆順にチェックして削除（安全に削除するため）
        for (int i = components.length - 1; i >= 0; i--) {
            if (components[i] instanceof ShieldPanel) {
                layeredPane.remove(components[i]);
            }
        }
        // レイヤードペインを再描画して変更を反映
        layeredPane.revalidate();
        layeredPane.repaint();
    }

    public void initialize() {//上記の初期化をまとめたもの
        initializePanel();
        initializeRock();
        initializeItem();
        initializeShield();
    }
}
