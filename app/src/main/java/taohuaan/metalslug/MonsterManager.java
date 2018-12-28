package taohuaan.metalslug;

import java.util.List;
import java.util.ArrayList;
import static taohuaan.metalslug.Monster.TYPE_FLY;
import android.graphics.Canvas;


/**
 * author: Runzhi on 2018/12/19.
 * Manage the random death,production,etc.of monsters.
 */

public class MonsterManager {

    /**
     * Constants defining storage of all dead monster.
     */
    public static final List<Monster> dieMonsterList = new ArrayList<>();
    /**
     * Constants defining storage of all alive monster.
     */
    public static final List<Monster> monsterList = new ArrayList<>();


    /**
     * Generating random monsters.
     */
    public static void generateMonster(){

        if(monsterList.size() < 3 + Util.randomIntRange(3)){
            Monster monster = new Monster(1 + Util.randomIntRange(3));
            monsterList.add(monster);
        }

    }


    /**
     *Left shift all monsters and bullets, first, left shift all the alive monsters,
     * then, left shift all the dead monsters, finally, update coordinate of player's bullet.
     *
     * @param shift     value of left shift value
     */
    public static void updatePosition(int shift){

        Monster monster = null;
        List<Monster> delMonsterList = new ArrayList<>();

        for(int i = 0; i < monsterList.size(); i++){
            monster = monsterList.get(i);
            if(monster == null)
                continue;
            monster.updateShift(shift);

            if(monster.getX() <  0)
                delMonsterList.add(monster);
        }
        monsterList.removeAll(delMonsterList);
        delMonsterList.clear();

        for(int i = 0; i < dieMonsterList.size(); i++){
            monster = dieMonsterList.get(i);
            if(monster == null)
                continue;
            monster.updateShift(shift);

            if(monster.getX() < 0)
                delMonsterList.add(monster);
        }
        dieMonsterList.removeAll(delMonsterList);

        GameView.player.updateBulletShift(shift);

    }


    /**
     *When monsters are going to die, first, traversing all alive monsters, if monsters is bomb,
     * calling Player class's isHurt() function, else traversing all bullets that player lunches, whether
     * to hit monsters.
     */
    public static void checkMonster(){

        Monster monster          = null;
        List<Bullet> delBullet   = new ArrayList<>();
        List<Monster> delMonster = new ArrayList<>();
        List<Bullet> bulletList  = GameView.player.getBulletList();
        if(bulletList == null)
            bulletList = new ArrayList<>();

        for(int i = 0; i < monsterList.size(); i++){
            monster = monsterList.get(i);
            if(monster == null)
                continue;

            if(monster.getType() == Monster.TYPE_BOMB){
                if(GameView.player.isHurt(monster.getStartX(), monster.getStartY(),
                        monster.getEndX(), monster.getEndY())){
                    monster.setDie(true);
                    delMonster.add(monster);
                    ViewManager.soundPool.play();
                    GameView.player.setHp(GameView.player.getHp() - 10);
                }
                continue;
            }

            for(Bullet bullet : bulletList){
                if(bullet == null || !bullet.isEffect()){
                    continue;
                }
                if(monster.isHurt(bullet.getX(), bullet.getY())){
                    bullet.setEffect(false);
                    monster.setDie(false);
                    if(monster.getType() == TYPE_FLY)
                        ViewManager.soudPool.play();
                    else
                        ViewManager.soudPool.play();
                    delMonster.add(monster);
                    delBullet.add(bullet);
                }
            }
            bulletList.removeAll(delBullet);
            monster.checkBullet();
        }

        dieMonsterList.addAll(delMonster);
        monsterList.removeAll(delMonster);

    }


    /**
     * Drawing all monsters's animation, include alive and dead monster.
     *
     * @param canvas    screen background
     */
    public static void drawMonster(Canvas canvas){

        Monster monster = null;
        for(int i = 0; i < monsterList.size(); i++){
            monster = monsterList.get(i);
            if(monster == null)
                continue;
            monster.draw(canvas);
        }

        List<Monster> delList = new ArrayList<>();
        for(int i = 0; i < dieMonsterList.size(); i++){
            monster = dieMonsterList.get(i);
            if(monster == null)
                continue;
            monster.draw(canvas);
            if(monster.getDieMaxDrawCount() <= 0)
                delList.add(monster);
        }
        dieMonsterList.removeAll(delList);

    }

}
