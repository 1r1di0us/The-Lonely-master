package lonelymod.companions;

import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class CompanionMoveInfo {
    public byte nextMove;

    public AbstractMonster.Intent intent;

    public int baseDamage;
    public int baseBlock;

    public int damageMultiplier;
    public int blockMultiplier;

    public boolean isMultiDamage;
    public boolean isMultiBlock;
    public boolean stanceAffected;

    public CompanionMoveInfo(byte nextMove, AbstractMonster.Intent intent, int intentBase, int multiplier, boolean isMulti, boolean isAttack, boolean stanceAffected) {
        if (isAttack) {
            this.nextMove = nextMove;
            this.intent = intent;
            this.baseDamage = intentBase;
            this.damageMultiplier = multiplier;
            this.isMultiDamage = isMulti;
            this.baseBlock = -1;
            this.blockMultiplier = 0;
            this.isMultiBlock = false;
            this.stanceAffected = stanceAffected;
        } else {
            this.nextMove = nextMove;
            this.intent = intent;
            this.baseDamage = -1;
            this.damageMultiplier = 0;
            this.isMultiDamage = false;
            this.baseBlock = intentBase;
            this.blockMultiplier = multiplier;
            this.isMultiBlock = isMulti;
            this.stanceAffected = false; // block is not affected by wrath
        }
    }
}
