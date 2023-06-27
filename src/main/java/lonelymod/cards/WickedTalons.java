package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class WickedTalons extends AbstractEasyCard {
    public final static String ID = makeID("WickedTalons");

    public WickedTalons() {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 4;
        baseMagicNumber = magicNumber = 2;
        baseSecondMagic = secondMagic = 4;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i=0; i<magicNumber; i++) {
            dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new StrengthPower(p, secondMagic)));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DexterityPower(p, -2)));
    }

    public void upp() {
        upgradeMagicNumber(1);
    }
}