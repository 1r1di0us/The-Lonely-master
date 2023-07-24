package lonelymod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import lonelymod.LonelyMod;
import lonelymod.companions.Oracle;
import lonelymod.util.TexLoader;

import static lonelymod.LonelyMod.makeID;

public class OraclePower extends AbstractEasyPower implements CloneablePowerInterface {

    public static final String POWER_ID = makeID("OraclePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TexLoader.getTexture(LonelyMod.modID + "Resources/images/powers/Oracle84.png");
    private static final Texture tex32 = TexLoader.getTexture(LonelyMod.modID + "Resources/images/powers/Oracle32.png");

    public OraclePower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, AbstractPower.PowerType.BUFF, true, owner, amount);

        this.owner = owner;
        this.amount = amount;

        type = AbstractPower.PowerType.BUFF;
        isTurnBased = true;

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
    public void updateDescription() {
        description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    @Override
    public void atEndOfRound() {
        addToBot(new ApplyPowerAction(this.owner, this.owner, new CompanionVigorPower(this.owner, this.amount)));
    }

    @Override
    public void onSpecificTrigger() {
        addToBot(new ApplyPowerAction(this.owner, this.owner, new CompanionVigorPower(this.owner, this.amount + Oracle.SPECIAL_PWR_AMT)));
        //+5 because the extra amount has not been applied yet.
    }

    @Override
    public AbstractPower makeCopy() {
        return new OraclePower(this.owner, this.amount);
    }
}
