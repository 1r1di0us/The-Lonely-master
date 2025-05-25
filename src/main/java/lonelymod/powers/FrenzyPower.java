package lonelymod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import lonelymod.LonelyMod;
import lonelymod.actions.CallMoveAction;
import lonelymod.actions.PerformMoveAction;
import lonelymod.companions.AbstractCompanion;
import lonelymod.fields.CompanionField;
import lonelymod.interfaces.TriggerOnCompanionTurnEndPowerInterface;
import lonelymod.interfaces.TriggerOnPerformMoveInterface;
import lonelymod.util.TexLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static lonelymod.LonelyMod.makeID;

public class FrenzyPower extends AbstractEasyPower implements CloneablePowerInterface, TriggerOnCompanionTurnEndPowerInterface {

    private static final Logger logger = LogManager.getLogger(AbstractEasyPower.class.getName());

    public static final String POWER_ID = makeID("FrenzyPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TexLoader.getTexture(LonelyMod.modID + "Resources/images/powers/Frenzy84.png");
    private static final Texture tex32 = TexLoader.getTexture(LonelyMod.modID + "Resources/images/powers/Frenzy32.png");

    private AbstractCompanion compOwner;

    public FrenzyPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, AbstractPower.PowerType.BUFF, true, owner, amount);
        this.owner = owner;
        this.amount = amount;
        this.type = AbstractPower.PowerType.BUFF;
        this.isTurnBased = false;
        if (tex84 != null) {
            region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, tex84.getWidth(), tex84.getHeight());
            if (tex32 != null)
                region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, tex32.getWidth(), tex32.getHeight());
        } else if (tex32 != null) {
            this.img = tex32;
            region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, tex32.getWidth(), tex32.getHeight());
        }

        if (CompanionField.currCompanion.get(AbstractDungeon.player) != null) {
            if (this.owner == CompanionField.currCompanion.get(AbstractDungeon.player)) {
                compOwner = (AbstractCompanion) owner;
            } else {
                logger.info("Frenzy power applied incorrectly!");
                compOwner = CompanionField.currCompanion.get(AbstractDungeon.player);
            }
        } else {
            logger.info("Frenzy applied without a companion!");
        }

        updateDescription();
    }

    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0] + this.amount + powerStrings.DESCRIPTIONS[1];
    }

    @Override
    public void triggerOnCompanionTurnEnd() {
        flash();
        for (int i = 0; i < this.amount; i++) {
            addToBot(new PerformMoveAction(AbstractCompanion.ATTACK, compOwner));
            addToBot(new WaitAction(0.1F));
        }
        for (AbstractPower p : AbstractDungeon.player.powers)
            if (p instanceof TriggerOnPerformMoveInterface) //only fetch
                ((TriggerOnPerformMoveInterface) p).triggerOnPerformMove(AbstractCompanion.ATTACK);
        addToBot(new CallMoveAction(AbstractCompanion.NONE, compOwner, false)); // do we need to call none?
        addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }

    @Override
    public AbstractPower makeCopy() {
        return new FrenzyPower(this.owner, this.amount);
    }
}
