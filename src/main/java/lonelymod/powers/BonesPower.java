package lonelymod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import lonelymod.LonelyMod;
import lonelymod.interfaces.TriggerOnCallMoveInterface;
import lonelymod.util.TexLoader;

import static lonelymod.LonelyMod.makeID;

public class BonesPower extends AbstractEasyPower implements CloneablePowerInterface, NonStackablePower, TriggerOnCallMoveInterface {

    public static final String POWER_ID = makeID("BonesPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TexLoader.getTexture(LonelyMod.modID + "Resources/images/powers/Bones84.png");
    private static final Texture tex32 = TexLoader.getTexture(LonelyMod.modID + "Resources/images/powers/Bones32.png");

//    private static final int StrengthAmt = 2;
//    private static final int DexAmt = 1;
    private static final int VigAmt = 2;
    private static final int StamAmt = 1;

    public BonesPower(AbstractCreature owner) {
        super(POWER_ID, NAME, AbstractPower.PowerType.BUFF, true, owner, 0);

        this.owner = owner;

        type = PowerType.BUFF;
        isTurnBased = true;
        this.amount = 0;

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

    /*public void onMonsterDeath(AbstractMonster m) {
        if (m.currentHealth == 0 && !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            addToBot(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, StrengthAmt), StrengthAmt));
            addToBot(new ApplyPowerAction(this.owner, this.owner, new CompanionDexterityPower(this.owner, DexAmt), DexAmt));
        }
    }*/

    @Override
    public void atEndOfRound() {
        if (this.amount > 0) {
            flash();
            addToBot(new ApplyPowerAction(this.owner, this.owner, new CompanionVigorPower(this.owner, this.amount * VigAmt)));
            addToBot(new ApplyPowerAction(this.owner, this.owner, new StaminaPower(this.owner, this.amount * StamAmt)));
            this.amount = 0;
            updateDescription();
        }
    }

    @Override
    public void triggerOnCallMove(byte move, byte prevMove) {
        flash();
        this.amount++;
        updateDescription();
    }

    @Override
    public void onSpecificTrigger() {
        flash();
        this.amount++;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        //description = DESCRIPTIONS[0] + StrengthAmt + DESCRIPTIONS[1] + DexAmt + DESCRIPTIONS[2];
        description = DESCRIPTIONS[0] + VigAmt + DESCRIPTIONS[1] + StamAmt + DESCRIPTIONS[2];
    }

    @Override
    public AbstractPower makeCopy() {
        return new BonesPower(this.owner);
    }
}
