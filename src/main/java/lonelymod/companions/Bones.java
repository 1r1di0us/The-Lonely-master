package lonelymod.companions;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static lonelymod.LonelyMod.makeCompanionPath;

public class Bones extends AbstractCompanion {
    public static final String ID = "Bones";

    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Bones");
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String IMG = makeCompanionPath("Bones.png");

    private static final int basicBlk = 3;

    public Bones(float drawX, float drawY) {
        super(NAME, ID, 0.0F, 0.0F, 400.0F, 300.0F, IMG, drawX, drawY);
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case 0:
                addToBot(new GainBlockAction(AbstractDungeon.player, this, basicBlk));
                setMove(MOVES[0], (byte)0, AbstractMonster.Intent.DEFEND);
                break;
        }
    }

    @Override
    protected void getMove(int i) {
        setMove(MOVES[0], (byte)0, AbstractMonster.Intent.DEFEND);
    }
}
