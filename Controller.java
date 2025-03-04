import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.sound.sampled.*;
// import javax.sound.sampledAudioSystem.getAudioInputStream;//: 音声ファイルを読み込むため。
// import javax.sound.sampled.Clip;// 音声を再生・停止するためのオブジェクト。
// import javax.sound.sampled.AudioInputStream;//: 音声データを読み込むためのストリーム。
import javax.swing.*;
// import javax.swing.JFrame;
// import javax.swing.JPanel;
// import javax.swing.JLabel;
// import javax.swing.JButton;

// Cはユーザー入力を処理する(Listener関係)
// Cは、Mの更新をし、Vにイベントを伝える
public class Controller {
    private Model model;
    private View view;
    private ArrayList<RockPanel> rocks; // ArrayListで実装
    private ArrayList<ItemPanel> items;
    private ArrayList<ShieldPanel> armors;
    private int deletedRock, deletedItem, deletedArmor, generateCounter, rockSpawnInterval, itemSpawnInterval, armorSpawnInterval, offsetY;
    private Clip gameoverClip, gameBgmClip, titleBgmClip, armorEquipClip, armorBreakClip, itemCollectClip, startbuttonClip, screamingClip;

    public Controller(Model model, View view) {
        // 初期設定
        this.model = model;
        this.view = view;
        rocks = new ArrayList<>();
        items = new ArrayList<>();
        armors = new ArrayList<>();
        deletedRock = 0;
        deletedItem = 0;
        deletedArmor = 0;
        generateCounter = 50;
        rockSpawnInterval = 35; // ゲームの難易度で数値変更可
        itemSpawnInterval = 110; // ゲームの難易度で数値変更可
        armorSpawnInterval = 550; // ゲームの難易度で数値変更可
        offsetY = -100;

        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(Controller.class.getResource("Explosion.wav"));
            gameoverClip = AudioSystem.getClip();
            gameoverClip.open(audioIn);

            AudioInputStream audioIn2 = AudioSystem.getAudioInputStream(Controller.class.getResource("usi.wav"));
            gameBgmClip = AudioSystem.getClip();
            gameBgmClip.open(audioIn2);

            AudioInputStream audioIn3 = AudioSystem.getAudioInputStream(Controller.class.getResource("TitleBGM.wav"));
            titleBgmClip = AudioSystem.getClip();
            titleBgmClip.open(audioIn3);

            AudioInputStream audioIn4 = AudioSystem.getAudioInputStream(Controller.class.getResource("ArmorEquip.wav"));
            armorEquipClip = AudioSystem.getClip();
            armorEquipClip.open(audioIn4);

            AudioInputStream audioIn5 = AudioSystem.getAudioInputStream(Controller.class.getResource("ArmorBreak.wav"));
            armorBreakClip = AudioSystem.getClip();
            armorBreakClip.open(audioIn5);

            AudioInputStream audioIn6 = AudioSystem.getAudioInputStream(Controller.class.getResource("ItemCollect.wav"));
            itemCollectClip = AudioSystem.getClip();
            itemCollectClip.open(audioIn6);

            AudioInputStream audioIn7 = AudioSystem.getAudioInputStream(Controller.class.getResource("startbutton.wav"));
            startbuttonClip = AudioSystem.getClip();
            startbuttonClip.open(audioIn7);

            AudioInputStream audioIn8 = AudioSystem.getAudioInputStream(Controller.class.getResource("endgame.wav"));
            screamingClip = AudioSystem.getClip();
            screamingClip.open(audioIn8);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (titleBgmClip != null) {
            titleBgmClip.setFramePosition(0);
            titleBgmClip.start();
        }

        model.startGame();

        view.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                // Playerの移動
                if (e.getKeyCode() == KeyEvent.VK_RIGHT && model.isPlayScene() && !model.isGameOver()) {
                    model.moveToRight();
                    view.getPlayerPanel().updatePlayerPos(model.getPlayerPosX());
                } else if (e.getKeyCode() == KeyEvent.VK_LEFT && model.isPlayScene() && !model.isGameOver()) {
                    model.moveToLeft();
                    view.getPlayerPanel().updatePlayerPos(model.getPlayerPosX());
                    // ここのデバッグ関数は削除
                }

                // アーマー付与(デバッグ用)
                // if (e.getKeyCode() == KeyEvent.VK_ENTER && model.isPlayScene()) {
                //     model.getArmor();
                //     view.getShieldLifePanel().showShieldLife(); // 盾所持表示
                //     System.out.println("You got armored");
                // }
            }

            public void keyReleased(KeyEvent e) {}

            public void keyTyped(KeyEvent e) {}
        });

        // 各ボタンのLietener
        view.getStartButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                titleBgmClip.stop();
                
                // ここにボタン効果音
                if(startbuttonClip!=null){
                    startbuttonClip.setFramePosition(0);
                    startbuttonClip.start();
                }
                
                // 1秒の遅延
                Timer timer = new Timer(1500, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (gameBgmClip != null) {
                            gameBgmClip.setFramePosition(0);
                            gameBgmClip.start();
                        }
        
                        model.goToPlayScene();
                        view.startGame();
                        System.out.println("Title->Play");
                    }
                });
                timer.setRepeats(false); // 一回だけ実行
                timer.start();
            }
        });

        view.getHomeButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.backToTitleScene();
                model.resetScore();
                model.setPlayerPositionZero();

                generateCounter = 50;
                deletedRock = 0;
                deletedItem = 0;
                deletedArmor = 0;
                rocks.clear();
                items.clear();
                armors.clear();

                model.resetRock();
                model.resetItem();
                model.resetArmor();
                view.backToTitle();
                if (gameBgmClip != null) {
                    gameBgmClip.stop();
                }
                if (titleBgmClip != null) {
                    titleBgmClip.start();
                }
                System.out.println("Play->Title");
            }
        });

        view.getRetryButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.backToTitleScene();

                generateCounter = 50;
                deletedRock = 0;
                deletedItem = 0;
                deletedArmor = 0;
                rocks.clear();
                items.clear();
                armors.clear();

                model.setPlayerPositionZero();
                model.resetScore();
                model.resetRock();
                model.resetItem();
                model.resetArmor();
                model.goToPlayScene();
                view.retryGame();
                System.out.println("Retry");
            }
        });

        // Timer基準->フレーム基準
        new Thread(() -> {
            final int frameRate = 60; // 1秒間のフレーム数(増やしすぎると重くなる?)
            final long frameTime = 1000 / frameRate; // 1フレームにかかる時間(ms)

            while (true) {
                long startTime = System.currentTimeMillis();

                if (model.isPlayScene() && !model.isGameOver()) {
                    // 岩生成
                    int num1=0, num2=0, num3=0;
                    if (generateCounter % rockSpawnInterval == 0) {
                        num1 = new Random().nextInt(3) - 1;
                        num2 = num1;
                        while (num1 == num2) {
                            num2 = new Random().nextInt(3) - 1;
                        }
                        generateRock(num1, offsetY);
                        if (1 < new Random().nextInt(5)) {
                            generateRock(num2, offsetY);
                        }
                    }

                    // 鎧生成
                    if(generateCounter % armorSpawnInterval == 0){
                        num3 = new Random().nextInt(3) - 1;
                        while(num3 == num1 || num3 == num2){
                            num3 = new Random().nextInt(3) - 1;
                        }
                        //System.out.println("generateArmor: "+num3);
                        generateArmor(num3, offsetY);
                    }

                    // アイテム生成
                    else if (generateCounter % itemSpawnInterval == 0) {
                        num3 = new Random().nextInt(3) - 1; // -1, 0, 1
                        while(num3 == num1 || num3 == num2){
                            num3 = new Random().nextInt(3) - 1;
                        }
                        generateItem(num3, offsetY);
                    }

                    generateCounter++;

                    // 岩移動と削除
                    model.increaseRockPosY(); // ゲームの難易度で呼び出し回数変更可
                    for (int i = deletedRock; i < model.getRockPosY().size(); i++) {
                        if (model.getRockPosY().get(i) > 1100) {
                            deletedRock++;
                        }
                    }
                    // System.out.println("size(model): " + model.getRockPosY().size() + ", deletedRock: " + deletedRock + ", size(rocks): " + rocks.size());

                    // アイテム移動と削除
                    model.increaseItemPosY(); // ゲームの難易度で呼び出し回数変更可
                    for (int i = deletedItem; i < model.getItemPosY().size(); i++) {
                        if (model.getItemPosY().get(i) > 1100) {
                            deletedItem++;
                        }
                    }
                    // System.out.println("size(model): " + model.getItemPosY().size() + ", deletedItem: " + deletedItem + ", size(items): " + items.size());

                    // 鎧移動と削除
                    model.increaseArmorPosY();
                    for(int i=deletedArmor; i<model.getArmorPosY().size(); i++){
                        if(model.getArmorPosY().get(i) > 1100){
                            deletedArmor++;
                        }
                    }
                    // System.out.println("size(model): " + model.getArmorPosY().size() + ", deletedArmor: " + deletedArmor + ", size(armors): " + armors.size());


                    // 岩の位置更新
                    for (int i = deletedRock; i < model.getRockPosY().size(); i++) {
                        rocks.get(i).updateRockPos(model.getRockPosY().get(i));
                    }

                    // 衝突判定
                    int isCollided = model.checkCollision();
                    if (isCollided != -1) {
                        if (model.hasArmor()) {
                            model.breakArmor();
                            view.getShieldLifePanel().hideShieldLife();
                            rocks.get(isCollided).hideRock();
                            if (armorBreakClip != null) {
                                System.out.println("Armor sound(break)");
                                armorBreakClip.setFramePosition(0);
                                armorBreakClip.start();
                            }
                            System.out.println("Armor has broken!");
                        } else {
                            if (gameoverClip != null) {
                                gameoverClip.setFramePosition(0);
                                gameoverClip.start();
                            }
                            if(screamingClip!=null){
                                screamingClip.setFramePosition(0);
                                screamingClip.start();
                            }

                            model.stopGame();
                            view.setGameOverScreenVisible(true);
                            System.out.println("You Lose...");
                        }
                    }

                    // アイテムの位置更新
                    for (int i = deletedItem; i < model.getItemPosY().size(); i++) {
                        items.get(i).updateItemPos(model.getItemPosY().get(i)); // 再描画を含む
                    }

                    // アイテム取得判定
                    int tmp = model.handleItemCollecting();
                    if(tmp!=-1){
                        // スコアを増加
                        model.increaseScore();
                        view.getScorePanel().updateScore(model.getScore());
                        items.get(tmp).hideItem();
                        if(itemCollectClip != null){
                            System.out.println("Item sound");
                            itemCollectClip.setFramePosition(0);
                            itemCollectClip.start();
                        }
                    }

                    // 鎧の位置更新
                    for(int i=deletedArmor; i<model.getArmorPosY().size(); i++){
                        armors.get(i).updateShieldPos(model.getArmorPosY().get(i));
                    }

                    // 鎧との衝突判定
                    int getArmor = model.handleArmorCollecting();
                    if(getArmor != -1){
                        model.getArmor();
                        view.getShieldLifePanel().showShieldLife();
                        armors.get(getArmor).hideShield();
                        if(armorEquipClip != null){
                            System.out.println("Armor sound");
                            armorEquipClip.setFramePosition(0);
                            armorEquipClip.start();
                        }
                    }
                }

                long elapsedTime = System.currentTimeMillis() - startTime;
                try {
                    Thread.sleep(Math.max(0, frameTime - elapsedTime));
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    private void generateRock(int posX, int posY) {
        model.setRockInfo(posX, posY);
        RockPanel newRock = view.addRock(posX, posY);
        rocks.add(newRock);
    }

    private void generateItem(int posX, int posY) {
        model.setItemInfo(posX, posY);
        ItemPanel newItem = view.addItem(posX, posY);
        items.add(newItem);
    }

    private void generateArmor(int posX, int posY){
        model.setArmorInfo(posX, posY);
        ShieldPanel newArmor = view.addShield(posX, posY);
        armors.add(newArmor);
    }
}
