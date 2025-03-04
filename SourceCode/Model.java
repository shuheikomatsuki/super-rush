import java.util.*;

// Mはゲームの状態やデータの保持を行う(スコア、ゲームオーバーとか)
// Mは、VやCに依存しない

public class Model {
    private int score;
    private boolean isTitleScene;
    private boolean isPlayScene;
    private boolean isGameStarted;
    private boolean isGameOver;
    private int playerPosX;
    private int speed;
    private boolean isArmored;
    private final int WINDOW_SIZE_Y = 1000;
    private final int PLAYER_POS_Y = 700;
    private final int ROCK_RADIUS = 50;
    private boolean isRockSameLane;
    private boolean isRockInsideTopContact;
    private boolean isRockInsideBottomContact;
    private boolean isItemSameLane;
    private boolean isItemInsideTopContact;
    private boolean isItemInsideBottomContact;
    private boolean isArmorSameLane;
    private boolean isArmorInsideTopContact;
    private boolean isArmorInsideBottomContact;
    
    ArrayList<Integer> arrRockPosX;
    ArrayList<Integer> arrRockPosY;
    ArrayList<Integer> arrItemPosX;
    ArrayList<Integer> arrItemPosY;
    ArrayList<Integer> arrArmorPosX;
    ArrayList<Integer> arrArmorPosY;
    ArrayList<Boolean> arrItemCollected;
    ArrayList<Boolean> arrArmorCollected;
    ArrayList<Boolean> arrRockDestroyed;

    public Model() {
        score = 0; // 初期スコアは0
        playerPosX = 0;   //初期位置は0 (中央のレーン)
        speed = 22;
        isTitleScene = true;
        isGameStarted = false;
        isGameOver = false;
        isArmored = false;

        arrRockPosX = new ArrayList<Integer>();
        arrRockPosY = new ArrayList<Integer>();
        arrItemPosX = new ArrayList<Integer>();
        arrItemPosY = new ArrayList<Integer>();
        arrArmorPosX = new ArrayList<Integer>();
        arrArmorPosY = new ArrayList<Integer>();
        arrItemCollected = new ArrayList<Boolean>();
        arrArmorCollected = new ArrayList<Boolean>();
        arrRockDestroyed = new ArrayList<Boolean>();
    }
    // +-----------+-----+------+-----+
    // | position  | -1  |  0   | 1   |
    // +-----------+-----+------+-----+
    // | 位置       | 左  | 中央  | 右  |
    // +-----------+-----+------+-----+

    public void setPlayerPositionZero() {
        playerPosX = 0;
    }

    public void moveToRight() {
        if (playerPosX != 1) playerPosX += 1;
    }

    public void moveToLeft() {
        if (playerPosX != -1) playerPosX -= 1;
    }

    public int getPlayerPosX() {
        return playerPosX;
    }

    // +------------------------------------------------------------------+
    //  岩関係
    // +------------------------------------------------------------------+

    public void setRockInfo(int rockPosX, int rockPosY) {
        arrRockPosX.add(rockPosX);  //  -1, 0, 1
        arrRockPosY.add(rockPosY);  //  岩のy座標
        arrRockDestroyed.add(false);
    }

    public void deleteOffScreenRock() {
        for (int i=arrRockPosY.size()-1; i>=0; i--) {
            if (arrRockPosY.get(i) > WINDOW_SIZE_Y) {
                arrRockPosX.remove(i);
                arrRockPosY.remove(i);
                arrRockDestroyed.remove(i);
            }
        }
    }

    //  不要かも
    public void deleteSpecificRock(int i) {
        arrRockPosX.remove(i);
        arrRockPosY.remove(i);
        arrRockDestroyed.remove(i);
    }

    //  岩を全削除
    public void resetRock() {
        for (int i=arrRockPosY.size()-1; i>=0; i--) {
            arrRockPosX.remove(i);
            arrRockPosY.remove(i);
            arrRockDestroyed.remove(i);
        }
    }

    //  すべての岩の座標を speed の値だけ増加させる (岩が画面下側へ移動する)
    public void increaseRockPosY() {
        for (int i=0; i<arrRockPosY.size(); i++) {
            arrRockPosY.set(i, arrRockPosY.get(i) + speed);
        }
    }

    //  すべての岩に対してプレイヤーのindexとy座標が一致するかをチェック
    //  1つでも一致するなら衝突とみなす (trueを返す)
    public int checkCollision() {
        for (int i=0; i<arrRockPosY.size(); i++) {
            isRockSameLane            = arrRockPosX.get(i) == playerPosX;
            isRockInsideBottomContact = arrRockPosY.get(i) <= PLAYER_POS_Y + 2 * ROCK_RADIUS + 100;
            isRockInsideTopContact    = arrRockPosY.get(i) >= PLAYER_POS_Y - 2 * ROCK_RADIUS + 100;
            //  岩とプレイヤーが同じレーン & プレイヤーと岩が少しでも重なっているならば
            if (isRockSameLane && isRockInsideBottomContact && isRockInsideTopContact && !arrRockDestroyed.get(i)) {
                if (hasArmor()) {
                    arrRockDestroyed.set(i, true);
                }
                return i;
            }
        }
        return -1;
    }

    public ArrayList<Integer> getRockPosY() {
        return arrRockPosY;
    }

    // +------------------------------------------------------------------+
    //  アイテム関係
    // +------------------------------------------------------------------+

    public void setItemInfo(int itemPosX, int itemPosY) {
        arrItemPosX.add(itemPosX);
        arrItemPosY.add(itemPosY);
        arrItemCollected.add(false);
    }

    //  アイテムを取得するとスコア増加
    public int handleItemCollecting() {
        for (int i=0; i<arrItemPosY.size(); i++) {
            isItemSameLane            = arrItemPosX.get(i) == playerPosX;
            isItemInsideTopContact    = arrItemPosY.get(i) <= PLAYER_POS_Y + 100;
            isItemInsideBottomContact = arrItemPosY.get(i) >= PLAYER_POS_Y - 100;
            if (isItemSameLane && isItemInsideTopContact && isItemInsideBottomContact && !arrItemCollected.get(i)) {
                arrItemCollected.set(i, true);
                return i;
            }
        }
        return -1;
    }

    public void deleteOffScreenItem() {
        for (int i=arrItemPosY.size()-1; i>=0; i--) {
            if (arrItemPosY.get(i) > WINDOW_SIZE_Y) {
                arrItemPosX.remove(i);
                arrItemPosY.remove(i);
                arrItemCollected.remove(i);
            }
        }
    }

    //  不要かも?
    public void deleteSpecificItem(int i) {
        arrItemPosX.remove(i);
        arrItemPosY.remove(i);
        arrItemCollected.remove(i);
    }

    //  アイテムを全削除
    public void resetItem() {
        for (int i=arrItemPosY.size()-1; i>=0; i--) {
            arrItemPosX.remove(i);
            arrItemPosY.remove(i);
            arrItemCollected.remove(i);
        }
    }

    //  すべてのアイテムの座標を speed の値だけ増加させる (アイテムが画面下側へ移動する)
    public void increaseItemPosY() {
        for (int i=0; i<arrItemPosY.size(); i++) {
            arrItemPosY.set(i, arrItemPosY.get(i) + speed);
        }
    }

    public ArrayList<Integer> getItemPosY() {
        return arrItemPosY;
    }

    // +------------------------------------------------------------------+
    //  システム
    // +------------------------------------------------------------------+

    public void increaseScore() {
        score += 10000;
    }

    public void resetScore() {
        score = 0;
    }

    public int getScore() {
        return score;
    }

    //  岩やアイテムの速度変更
    public void changeSpeed(int speed) {
        this.speed = speed;
    }

    // +------------------------------------------------------------------+
    //  シーン管理
    // +------------------------------------------------------------------+

    public void goToPlayScene() {
        isTitleScene = false;
        isPlayScene = true;
        isArmored = false;
    }

    public void backToTitleScene() {
        isPlayScene = false;
        isTitleScene = true;
        isGameOver = false;
        isArmored = false;
    }

    public boolean isTitleScene() {
        return isTitleScene;
    }

    public boolean isPlayScene() {
        return isPlayScene;
    }

    public boolean isGameStarted() {
        return isGameStarted;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void startGame() {
        isGameStarted = true;
    }

    public void stopGame() {
        isGameOver = true;
    }

    // +------------------------------------------------------------------+
    //  鎧関係
    // +------------------------------------------------------------------+

    //  Armor(鎧)アイテムに触れると、1回までなら岩に当たっても死なない。
    //  その後、鎧は壊れる。複数取得不可。

    public void setArmorInfo(int armorPosX, int armorPosY) {
        arrArmorPosX.add(armorPosX);
        arrArmorPosY.add(armorPosY);
        arrArmorCollected.add(false);
    }

    //  鎧を取得する
    public int handleArmorCollecting() {
        for (int i=0; i<arrArmorPosY.size(); i++) {
            isArmorSameLane            = arrArmorPosX.get(i) == playerPosX;
            isArmorInsideTopContact    = arrArmorPosY.get(i) <= PLAYER_POS_Y + 100;
            isArmorInsideBottomContact = arrArmorPosY.get(i) >= PLAYER_POS_Y - 100;
            if (isArmorSameLane && isArmorInsideTopContact && isArmorInsideBottomContact && !arrArmorCollected.get(i)) {
                arrArmorCollected.set(i, true);
                return i;
            }
        }
        return -1;
    }

    public void deleteOffScreenArmor() {
        for (int i=arrArmorPosY.size()-1; i>=0; i--) {
            if (arrArmorPosY.get(i) > WINDOW_SIZE_Y) {
                arrArmorPosX.remove(i);
                arrArmorPosY.remove(i);
                arrArmorCollected.remove(i);
            }
        }
    }

    //  不要かも?
    public void deleteSpecificArmor(int i) {
        arrArmorPosX.remove(i);
        arrArmorPosY.remove(i);
        arrArmorCollected.remove(i);
    }

    //  鎧を全削除
    public void resetArmor() {
        for (int i=arrArmorPosY.size()-1; i>=0; i--) {
            arrArmorPosX.remove(i);
            arrArmorPosY.remove(i);
            arrArmorCollected.remove(i);
        }
    }

    //  すべての鎧の座標を speed の値だけ増加させる (鎧が画面下側へ移動する)
    public void increaseArmorPosY() {
        for (int i=0; i<arrArmorPosY.size(); i++) {
            arrArmorPosY.set(i, arrArmorPosY.get(i) + speed);
        }
    }

    public void getArmor() {
        if (isGameStarted && !isGameOver) {
            isArmored = true;
        }
    }
    
    public void breakArmor() {
        if (isGameStarted && !isGameOver) {
            isArmored = false;
        }
    }

    public boolean hasArmor() {
        return isArmored;
    }

    public ArrayList<Integer> getArmorPosY() {
        return arrArmorPosY;
    }
}
