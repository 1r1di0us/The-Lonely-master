package lonelymod.companions;

import com.megacrit.cardcrawl.monsters.AbstractMonster;

public abstract class AbstractCompanion extends AbstractMonster {


    private static final byte BASIC = 0;
    private static final byte ATTACK = 1;
    private static final byte PROTECT = 2;
    private static final byte SPECIAL = 3;

    public AbstractCompanion(String name, String id, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl, float offsetX, float offsetY) {
        super(name, id, 1, hb_x, hb_y, hb_w, hb_h, imgUrl, offsetX, offsetY);
    }
    @Override
    public abstract void takeTurn();

    @Override
    protected abstract void getMove(int i);
}
