package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.actions.CallMoveAction;
import lonelymod.companions.AbstractCompanion;
import lonelymod.fields.CompanionField;
import lonelymod.powers.AttackNextTurnPower;


public class Kill extends AbstractEasyCard {
    public final static String ID = makeID("Kill");

    public Kill() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 6;
        baseMagicNumber = magicNumber = 1;
        this.tags.add(Enums.COMPANION);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        addToBot(new CallMoveAction(AbstractCompanion.ATTACK, CompanionField.currCompanion.get(AbstractDungeon.player)));
        if (upgraded)
            addToBot(new ApplyPowerAction(p, p, new AttackNextTurnPower(p, this.magicNumber)));
    }

    public void upp() {
        uDesc();
    }
}