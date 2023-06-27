package lonelymod.powers;

import static lonelymod.LonelyMod.makeID;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import basemod.interfaces.CloneablePowerInterface;
import lonelymod.LonelyMod;
import lonelymod.util.TexLoader;

public class ShockAndAwePower extends AbstractEasyPower implements CloneablePowerInterface {
   
    public static final String POWER_ID = makeID("ShockAndAwePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TexLoader.getTexture(LonelyMod.modID + "Resources/images/powers/ExampleTwoAmountPower84.png");
    private static final Texture tex32 = TexLoader.getTexture(LonelyMod.modID + "Resources/images/powers/ExampleTwoAmountPower32.png");

    private AbstractMonster target;

    public ShockAndAwePower(AbstractCreature owner, AbstractMonster target, int amount) {
        super(POWER_ID, NAME, AbstractPower.PowerType.BUFF, true, owner, amount);

        this.owner = owner;
        this.target= target;

        type = PowerType.BUFF;
        isTurnBased = true;
        this.amount = amount;

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
        if (this.amount > 0) {
            flash();
            AbstractDungeon.actionManager.addToBottom(new StunMonsterAction(this.target, this.owner));
            this.amount -= 1;
        }
        if (this.amount == 0)
            addToBot((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }

    @Override
    public void updateDescription() {
        if (target == null || target.name == null && target.id == null) {
            description = DESCRIPTIONS[0] + FontHelper.colorString("target enemy", "r") + DESCRIPTIONS[3];
        } else if (target.name == null) {
            description = DESCRIPTIONS[0] + FontHelper.colorString(target.id, "r") + DESCRIPTIONS[3];
        } else if (amount == 1) {
            description = DESCRIPTIONS[0] + FontHelper.colorString(target.name, "r") + DESCRIPTIONS[3];
        } else if (amount > 1) {
            description = DESCRIPTIONS[1] + amount + DESCRIPTIONS[2] + FontHelper.colorString(target.name, "r") + DESCRIPTIONS[3];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new ShockAndAwePower(this.owner, this.target, this.amount);
    }
}
