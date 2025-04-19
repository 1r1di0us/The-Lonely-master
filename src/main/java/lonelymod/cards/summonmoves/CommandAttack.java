package lonelymod.cards.summonmoves;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.actions.CallMoveAction;
import lonelymod.cards.AbstractEasyCard;
import lonelymod.companions.AbstractCompanion;
import lonelymod.fields.CompanionField;
import lonelymod.powers.TargetPower;

public class CommandAttack extends AbstractEasyCard {
    public final static String ID = makeID("CommandAttack");

    public CommandAttack() {
        super(ID, 1, CardType.ATTACK, CardRarity.SPECIAL, CardTarget.SELF_AND_ENEMY);
        this.baseDamage = 5;
        this.baseMagicNumber = this.magicNumber = 0;
        this.tags.add(Enums.COMPANION);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        if (upgraded) {
            addToBot(new ApplyPowerAction(m, p, new TargetPower(m, this.magicNumber, false), this.magicNumber));
        }
        addToBot(new CallMoveAction(AbstractCompanion.ATTACK, CompanionField.currCompanion.get(AbstractDungeon.player)));
    }

    public void upp() {
        upgradeMagicNumber(1);
        uDesc();
    }
}