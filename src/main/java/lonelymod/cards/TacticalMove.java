package lonelymod.cards;

import static lonelymod.ModFile.makeID;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

import lonelymod.actions.CompanionAttackAbilityAction;
import lonelymod.actions.CompanionProtectAbilityAction;

public class TacticalMove extends AbstractEasyCard{
    public final static String ID = makeID("TacticalMove");

    public TacticalMove() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF_AND_ENEMY);
        baseMagicNumber = magicNumber = 4;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new VigorPower(p, this.magicNumber), this.magicNumber));
        if (!m.isDeadOrEscaped() && m.getIntentBaseDmg() > 0) {
            AbstractDungeon.actionManager.addToBottom(new CompanionProtectAbilityAction());
        }
        else if (!m.isDeadOrEscaped() && m.getIntentBaseDmg() <= 0) {
            AbstractDungeon.actionManager.addToBottom(new CompanionAttackAbilityAction());
        }
    }

    public void upp() {
        upgradeMagicNumber(2);
    }
}