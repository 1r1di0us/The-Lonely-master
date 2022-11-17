package lonelymod.cards;

import static lonelymod.ModFile.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SpareTheWeak extends AbstractEasyCard {
    public final static String ID = makeID("SpareTheWeak");

    public SpareTheWeak() {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        baseDamage = 16;
        this.exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractMonster mon: AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (mon.hasPower("Minion")) {
                mon.escape();
            }
            else {
                dmg(mon, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
            }
        }
    }

    public void upp() {
        upgradeDamage(4);
    }
}
