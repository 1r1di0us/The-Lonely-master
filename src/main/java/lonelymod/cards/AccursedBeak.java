package lonelymod.cards;

import static lonelymod.ModFile.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class AccursedBeak extends AbstractEasyCard {
    public final static String ID = makeID("AccursedBeak");

    public AccursedBeak() {
        super(ID, 1, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = 5;
        baseMagicNumber = magicNumber = 3;
        this.exhaust = true;
    }
    
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AttackEffect.BLUNT_LIGHT);
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new StrengthPower(p, -magicNumber)));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new GainStrengthPower(p, magicNumber)));
        if (p.hasPower("Strength")) {
            if (p.getPower("Strength").amount > 0) {
                AbstractCard tmp = this.makeStatEquivalentCopy();
                tmp.purgeOnUse = true;
                addToBot(new NewQueueCardAction(tmp, m, false, true));
            }
        }
    }

    public void upp() {
        upgradeMagicNumber(-1);
    }
}
