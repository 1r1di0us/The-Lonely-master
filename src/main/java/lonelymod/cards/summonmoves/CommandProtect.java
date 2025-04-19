package lonelymod.cards.summonmoves;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.actions.CallMoveAction;
import lonelymod.cards.AbstractEasyCard;
import lonelymod.companions.AbstractCompanion;
import lonelymod.fields.CompanionField;

public class CommandProtect extends AbstractEasyCard {
    public final static String ID = makeID("CommandProtect");

    public CommandProtect() {
        super(ID, 1, CardType.SKILL, CardRarity.SPECIAL, CardTarget.SELF);
        this.baseBlock = 0;
        this.tags.add(Enums.COMPANION);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (upgraded) {
            blck();
        }
        addToBot(new CallMoveAction(AbstractCompanion.PROTECT, CompanionField.currCompanion.get(AbstractDungeon.player)));
    }

    public void upp() {
        upgradeBlock(3);
        uDesc();
    }
}