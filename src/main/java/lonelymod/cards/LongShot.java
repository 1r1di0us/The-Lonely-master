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
        baseSecondDamage = 5;
        baseMagicNumber = magicNumber = 12;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        boolean bonus = true;
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (m.drawX - mo.drawX < -30 && !mo.isDeadOrEscaped() && !(AbstractDungeon.getCurrRoom() instanceof MonsterRoom && lastCombatMetricKey.equals("Shield and Spear"))) {
                bonus = false; // daggers are 30 away from each other
            }
        }
        if (bonus) {
            addToBot(new DamageAction(m, new DamageInfo(p, this.damage + this.secondDamage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        }
        else {
            dmg(m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
        }
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        boolean bonus = true;
        for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (mo.drawX - m.drawX < -30 && !m.isDeadOrEscaped() && !(AbstractDungeon.getCurrRoom() instanceof MonsterRoom && lastCombatMetricKey.equals("Shield and Spear"))) {
                bonus = false; // daggers are 30 away from each other
            }
        }
        if (bonus) { //if hovered over the furthest enemy
            this.baseMagicNumber = this.damage + this.secondDamage;
            this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[0];
            this.initializeDescription();
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();

        if (!cardStrings.DESCRIPTION.equals(this.rawDescription)) {
            this.rawDescription = cardStrings.DESCRIPTION;
            initializeDescription();
        }
    }

    public void onMoveToDiscard() {
        this.rawDescription = cardStrings.DESCRIPTION;
        initializeDescription();
    }

    public void upp() {
        upgradeDamage(2);
        upgradeSecondDamage(2);
        upgradeMagicNumber(4);
    }
}
