package lonelymod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import lonelymod.LonelyMod;
import lonelymod.companions.AbstractCompanion;
import lonelymod.interfaces.TriggerOnCallMoveInterface;
import lonelymod.util.TexLoader;

import static lonelymod.LonelyMod.makeID;

public class OutcastPower extends AbstractEasyPower implements CloneablePowerInterface, TriggerOnCallMoveInterface {
    public static final String POWER_ID = makeID("OutcastPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TexLoader.getTexture(LonelyMod.modID + "Resources/images/powers/Outcast84.png");
    private static final Texture tex32 = TexLoader.getTexture(LonelyMod.modID + "Resources/images/powers/Outcast32.png");

    private final boolean startOfBattle;
    private int StrAmt = 2;
    private int DexAmt = 2;
    private final int triggerNum = 3;

    public OutcastPower(AbstractCreature owner, int amount, boolean startOfBattle, int StrAmt, int DexAmt) {
        super(POWER_ID, NAME, AbstractPower.PowerType.BUFF, false, owner, amount);

        this.owner = owner;
        this.amount = amount;
        this.startOfBattle = startOfBattle;
        this.StrAmt = StrAmt;
        this.DexAmt = DexAmt;

        type = AbstractPower.PowerType.BUFF;
        isTurnBased = false;

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
        description = DESCRIPTIONS[0] + triggerNum + DESCRIPTIONS[1] + StrAmt + DESCRIPTIONS[2] + DexAmt + DESCRIPTIONS[3];
    }

    @Override
    public void triggerOnCallMove(byte move, byte prevMove) {
        flash();
        if (this.amount == triggerNum-1) {
            this.amount = 0;
            updateDescription();
            addToBot(new ApplyPowerAction(this.owner, this.owner, new EmpoweredPower(this.owner, 1, false)));
            addToBot(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, this.StrAmt)));
            addToBot(new ApplyPowerAction(this.owner, this.owner, new CompanionDexterityPower(this.owner, this.DexAmt)));
        } else {
            this.amount++;
            updateDescription();
        }
    }

    @Override
    public void onSpecificTrigger() { // immediately trigger Empower
        flash();
        updateDescription();
        addToBot(new ApplyPowerAction(this.owner, this.owner, new EmpoweredPower(this.owner, 1, true)));
        addToBot(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, this.StrAmt)));
        addToBot(new ApplyPowerAction(this.owner, this.owner, new CompanionDexterityPower(this.owner, this.DexAmt)));
    }

    public void upgradeEmpower(int StrIncrease, int DexIncrease) {
        flash();
        this.StrAmt += StrIncrease;
        this.DexAmt += DexIncrease;
        updateDescription();
    }

    @Override
    public AbstractPower makeCopy() {
        return new OutcastPower(this.owner, this.amount, this.startOfBattle, this.StrAmt, this.DexAmt);
    }
}
