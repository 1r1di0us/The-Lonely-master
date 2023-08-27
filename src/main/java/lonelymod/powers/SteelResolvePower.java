package lonelymod.powers;

import static lonelymod.LonelyMod.makeID;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import lonelymod.LonelyMod;
import lonelymod.util.TexLoader;


public class SteelResolvePower extends AbstractEasyPower implements CloneablePowerInterface, NonStackablePower {

    public static final String POWER_ID = makeID("SteelResolvePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TexLoader.getTexture(LonelyMod.modID + "Resources/images/powers/SteelResolve84.png");
    private static final Texture tex32 = TexLoader.getTexture(LonelyMod.modID + "Resources/images/powers/SteelResolve32.png");

    private boolean lostHpThisTurn;

    private int dexAmount;
    private int platedArmorAmount;

    public SteelResolvePower(AbstractCreature owner, int amount, int dexAmount, int platedArmorAmount) {
        super(POWER_ID, NAME, AbstractPower.PowerType.BUFF, false, owner, amount);

        this.owner = owner;

        type = PowerType.BUFF;
        isTurnBased = false;
        this.isPostActionPower = true;
        lostHpThisTurn = false;
        this.dexAmount = dexAmount * amount;
        this.platedArmorAmount = platedArmorAmount * amount;

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
    public void atStartOfTurn() {
        this.lostHpThisTurn = false;
    }

    @Override
    public int onLoseHp(int damageAmount) {
        if (damageAmount > 0 && !lostHpThisTurn) {
            flash();
            addToTop(new ApplyPowerAction(this.owner, this.owner, new DexterityPower(this.owner, this.dexAmount)));
            addToTop(new ApplyPowerAction(this.owner, this.owner, new PlatedArmorPower(this.owner, this.platedArmorAmount)));
            lostHpThisTurn = true;
        }
        return damageAmount;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + this.dexAmount + DESCRIPTIONS[1] + this.platedArmorAmount + DESCRIPTIONS[2];
    }

    @Override
    public AbstractPower makeCopy() {
        return new SteelResolvePower(this.owner, this.amount, this.dexAmount, this.platedArmorAmount);
    }
}
