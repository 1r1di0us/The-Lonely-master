package lonelymod.powers;

import static lonelymod.LonelyMod.makeID;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import basemod.interfaces.CloneablePowerInterface;
import lonelymod.LonelyMod;
import lonelymod.companions.AbstractCompanion;
import lonelymod.interfaces.TriggerOnCallMoveInterface;
import lonelymod.util.TexLoader;

public class RecoveryPower extends AbstractEasyPower implements CloneablePowerInterface, TriggerOnCallMoveInterface {
   
    public static final String POWER_ID = makeID("RecoveryPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TexLoader.getTexture(LonelyMod.modID + "Resources/images/powers/Recovery84.png");
    private static final Texture tex32 = TexLoader.getTexture(LonelyMod.modID + "Resources/images/powers/Recovery32.png");

    public RecoveryPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, AbstractPower.PowerType.BUFF, true, owner, amount);

        this.owner = owner;

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
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    public void triggerOnCallMove(byte move, byte prevMove) {
        if (prevMove == AbstractCompanion.PROTECT) {
            flash();
            addToBot(new GainEnergyAction(this.amount));
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new RecoveryPower(this.owner, this.amount);
    }
}
