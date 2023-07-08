package lonelymod.companions;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;

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
            case DEFAULT:
                addToBot(new GainBlockAction(AbstractDungeon.player, this, basicBlk));
                setMove(MOVES[0], (byte)0, AbstractMonster.Intent.DEFEND);
                break;
            case ATTACK:
                AbstractCreature target = getTarget();
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.4F));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new BiteEffect(target.hb.cX +

                        MathUtils.random(-25.0F, 25.0F) * Settings.scale, target.hb.cY +
                        MathUtils.random(-25.0F, 25.0F) * Settings.scale, Color.GOLD
                        .cpy()), 0.0F));
                addToBot(new DamageAction(getTarget(), this.damage.get(0), AbstractGameAction.AttackEffect.NONE));
                addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, 1), 1));
                break;
        }
    }

    @Override
    protected void getMove(int i) {
        setMove(MOVES[0], DEFAULT, AbstractMonster.Intent.DEFEND);
    }

    @Override
    public void updateIntentTip() {
        switch (nextMove) {
            case DEFAULT:
                this.intentTip.header = TEXT[0];
                if (this.isMultiDmg) {
                    this.intentTip.body = TEXT[12] + this.getIntentDmg() + TEXT[2] + this.intentMultiAmt + TEXT[3];
                } else {
                    this.intentTip.body = TEXT[12] + this.getIntentDmg() + TEXT[5];
                }
                this.intentTip.img = ImageMaster.INTENT_ATTACK_DEFEND;
                return;
            case ATTACK:
                this.intentTip.header = TEXT[6];
                if (this.isMultiDmg) {
                    this.intentTip.body = TEXT[7] + this.intentDmg + TEXT[2] + this.intentMultiAmt + TEXT[8];
                } else {
                    this.intentTip.body = TEXT[9] + this.intentDmg + TEXT[5];
                }
                this.intentTip.img = ImageMaster.INTENT_ATTACK_BUFF;
                return;
        }
    }
}
