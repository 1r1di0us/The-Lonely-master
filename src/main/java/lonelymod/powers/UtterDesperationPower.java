package lonelymod.powers;

import static lonelymod.LonelyMod.makeID;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import basemod.interfaces.CloneablePowerInterface;
import lonelymod.LonelyMod;
import lonelymod.actions.PlayCardAction;
import lonelymod.util.TexLoader;

public class UtterDesperationPower extends AbstractEasyPower implements CloneablePowerInterface {

    public static final String POWER_ID = makeID("UtterDesperationPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TexLoader.getTexture(LonelyMod.modID + "Resources/images/powers/UtterDesperation84.png");
    private static final Texture tex32 = TexLoader.getTexture(LonelyMod.modID + "Resources/images/powers/UtterDesperation32.png");

    public UtterDesperationPower(AbstractCreature owner, int amount) {
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

    public void atEndOfTurnPreEndTurnCards(boolean isPlayer) {
        if (isPlayer && !AbstractDungeon.player.discardPile.isEmpty()) {
            for (int i = 0; i < this.amount; i++) {
                if (i >= AbstractDungeon.player.discardPile.size()) break;
                else if (AbstractDungeon.player.discardPile.getNCardFromTop(i).costForTurn != -2) {
                    addToBot(new PlayCardAction(AbstractDungeon.player.discardPile, AbstractDungeon.player.discardPile.getNCardFromTop(i),
                            AbstractDungeon.getCurrRoom().monsters.getRandomMonster(null, true, AbstractDungeon.cardRandomRng), true));
                }
            }
            /*for (int i = 0; i < this.amount; i++) { // old effect
                if (!AbstractDungeon.player.hand.isEmpty()) {
                    CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                    for (AbstractCard c : AbstractDungeon.player.hand.group) {
                        if (c.costForTurn != -2 && !c.isEthereal) {
                            tmp.addToRandomSpot(c);
                        }
                    }
                    if (!tmp.isEmpty()) {
                        flash();
                        AbstractCard cardToPlay = tmp.getRandomCard(true);
                        addToBot(new PlayCardAction(AbstractDungeon.player.hand, cardToPlay, AbstractDungeon.getCurrRoom().monsters.getRandomMonster(null, true, AbstractDungeon.cardRandomRng), false));
                    }
                }
            }*/
        }
    }

    @Override
    public void updateDescription() {
        if (this.amount == 1) {
            description = DESCRIPTIONS[0];
        } else if (this.amount > 1) {
            description = DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new UtterDesperationPower(this.owner, this.amount);
    }

}
