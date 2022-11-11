package lonelymod.powers;

import static lonelymod.ModFile.makeID;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;

import basemod.interfaces.CloneablePowerInterface;
import lonelymod.ModFile;
import lonelymod.util.TexLoader;

public class ResolvePower extends AbstractEasyPower implements CloneablePowerInterface {

    public static final String POWER_ID = makeID("ResolvePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TexLoader.getTexture(ModFile.modID + "Resources/images/powers/ExampleTwoAmountPower84.png");
    private static final Texture tex32 = TexLoader.getTexture(ModFile.modID + "Resources/images/powers/ExampleTwoAmountPower32.png");

    public ResolvePower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, AbstractPower.PowerType.BUFF, false, owner, amount);

        this.owner = owner;

        type = PowerType.BUFF;
        isTurnBased = false;
        this.amount = amount;
        this.isPostActionPower = true;

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
    public int onLoseHp(int damageAmount) {
        if (damageAmount > 0) {
            flash();
            addToBot((AbstractGameAction) new ApplyPowerAction(this.owner, this.owner, new PlatedArmorPower(this.owner, this.amount), this.amount));
        }
        return damageAmount;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new ResolvePower(this.owner, this.amount);
    }
}
