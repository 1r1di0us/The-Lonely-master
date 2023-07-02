package lonelymod.companions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static lonelymod.LonelyMod.makeCompanionPath;

public class Bones3 extends AbstractMonster {

    public static final String ID = "Bones";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Bones");
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String IMG = makeCompanionPath("Bones.png");
    private int basicBlockAmt = 3;
    private int attackDmg = 8;
    private int protectBlk = 8;


    private static final byte BASIC = 1;
    private static final byte ATTACK = 2;
    private static final byte PROTECT = 3;
    private static final byte SPECIAL = 4;


    public Bones3(float x, float y) {
        super(NAME, ID, 1, 0.0F, 200.0F, 0.0F, 0.0F, IMG, x, y);
        this.damage.add(new DamageInfo(this, this.attackDmg));
    }
    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case 1:
                addToBot(new GainBlockAction(AbstractDungeon.player, this, this.basicBlockAmt));
                this.setMove(MOVES[0], (byte)1, Intent.DEFEND);
                break;
            case 2:
                addToBot(new AnimateSlowAttackAction(this));
                addToBot(new DamageRandomEnemyAction(this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                //if this attack deals unblocked attack damage:
                addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, 1), 1));
                break;
            case 3:
                addToBot(new GainBlockAction(AbstractDungeon.player, this, this.protectBlk));
                //addToBot(new ApplyPowerAction(this, this, new CompanionVigorPower(this, 3), 3));
                break;
            case 4:
                //I'm not doing this yet
        }
    }

    @Override
    protected void getMove(int num) {
        this.setMove(MOVES[0], (byte) 1, Intent.DEFEND);
    }

    @Override
    public void rollMove() {
        this.getMove(1);
    }
}
