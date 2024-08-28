package lonelymod.cards;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.lastCombatMetricKey;
import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.MonsterRoom;

public class LongShot extends AbstractEasyCard {
    public final static String ID = makeID("LongShot");

    public LongShot() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 7;
        secondDamage = 12;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        boolean bonus = true;
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (m.drawX - mo.drawX < -30 && !(AbstractDungeon.getCurrRoom() instanceof MonsterRoom && lastCombatMetricKey.equals("Shield and Spear"))) {
                bonus = false; // daggers are 30 away from each other
            }
        }
        if (bonus) {
            addToBot(new DamageAction(m, new DamageInfo(p, this.secondDamage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        }
        else {
            dmg(m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
        }
    }

    public void upp() {
        upgradeDamage(3);
        upgradeSecondDamage(3);
    }
}
