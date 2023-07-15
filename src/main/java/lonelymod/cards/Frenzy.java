package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import lonelymod.actions.CallMoveAction;
import lonelymod.actions.EasyXCostAction;
import lonelymod.companions.AbstractCompanion;
import lonelymod.fields.CompanionField;
import lonelymod.powers.FrenzyPower;

public class Frenzy extends AbstractEasyCard {
    public final static String ID = makeID("Frenzy");

    public Frenzy() {
        super(ID, -1, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 0;
        this.tags.add(Enums.COMPANION);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new CallMoveAction(AbstractCompanion.UNKNOWN, CompanionField.currCompanion.get(AbstractDungeon.player)));
        addToBot(new EasyXCostAction(this, (effect, params) -> {
            if (CompanionField.currCompanion.get(AbstractDungeon.player) != null) {
                if (upgraded) {
                    addToBot(new ApplyPowerAction(CompanionField.currCompanion.get(AbstractDungeon.player), p, new FrenzyPower(CompanionField.currCompanion.get(AbstractDungeon.player), effect + 1)));
                } else {
                    if (effect > 0)
                        addToBot(new ApplyPowerAction(CompanionField.currCompanion.get(AbstractDungeon.player), p, new FrenzyPower(CompanionField.currCompanion.get(AbstractDungeon.player), effect)));
                }
            } else {
                AbstractDungeon.effectList.add(new ThoughtBubble(p.dialogX, p.dialogY, 3.0F, CallMoveAction.TEXT[0], true));
            }
            return true;
        }));
    }

    public void upp() {
        uDesc();
    }
}