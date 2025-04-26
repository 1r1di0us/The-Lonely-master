package lonelymod.powers;

import static lonelymod.LonelyMod.makeID;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import lonelymod.LonelyMod;
import lonelymod.util.TexLoader;

public class FearlessBraveryPower extends AbstractEasyPower implements CloneablePowerInterface {
   
    public static final String POWER_ID = makeID("FearlessBraveryPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TexLoader.getTexture(LonelyMod.modID + "Resources/images/powers/FearlessBravery84.png");
    private static final Texture tex32 = TexLoader.getTexture(LonelyMod.modID + "Resources/images/powers/FearlessBravery32.png");

    private final int vigAmount;
    private final int drawAmount;

    public FearlessBraveryPower(AbstractCreature owner, int amount, int vigAmount, int drawAmount) {
        super(POWER_ID, NAME, AbstractPower.PowerType.BUFF, true, owner, amount);

        this.owner = owner;

        type = PowerType.BUFF;
        isTurnBased = true;
        this.amount = amount;
        this.vigAmount = vigAmount;
        this.drawAmount = drawAmount;

        if (tex84 != null) {
            region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, tex84.getWidth(), tex84.getHeight());
            if (tex32 != null)
                region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, tex32.getWidth(), tex32.getHeight());
        } else if (tex32 != null) {
            this.img = tex32;
            region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, tex32.getWidth(), tex32.getHeight());
        }

        updateDescription();
    }

    @Override
    public void atStartOfTurnPostDraw() {
        boolean gainVigor = true;
        for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
            if (!m.isDeadOrEscaped() && m.getIntentBaseDmg() < 0) {
                gainVigor = false;
            }
        }
        if (gainVigor) {
            flash();
            addToBot(new ApplyPowerAction(this.owner, this.owner, new VigorPower(this.owner, this.vigAmount * this.amount)));
            addToBot(new DrawCardAction(this.owner, this.drawAmount * this.amount));
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + this.vigAmount * this.amount + DESCRIPTIONS[1] + this.drawAmount * this.amount + DESCRIPTIONS[2];
    }

    @Override
    public AbstractPower makeCopy() {
        return new FearlessBraveryPower(this.owner, this.amount, this.vigAmount, this.drawAmount);
    }
}
