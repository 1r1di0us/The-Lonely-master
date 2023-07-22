package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Strikeout extends AbstractEasyCard {
    public final static String ID = makeID("Strikeout");

    public static int turnsSinceDamaged = 0;

    public Strikeout() {
        super(ID, 3, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 6;
        baseMagicNumber = magicNumber = 3;
        tags.add(CardTags.STRIKE);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
    }

    @Override
    public void triggerWhenDrawn() {
        super.triggerWhenDrawn();
        if (turnsSinceDamaged >= 3)
            setCostForTurn(0);
        else
            setCostForTurn(this.cost - turnsSinceDamaged);
    }

    public void upp() {
        upgradeDamage(2);
    }
}