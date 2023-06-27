package lonelymod.companions;

public class CompanionMoveInfo {
    public byte nextMove;
  
    public AbstractCompanion.Intent intent;
    
    public int baseDamage;
    
    public int multiplier;
    
    public boolean isMultiDamage;
    
    public CompanionMoveInfo(byte nextMove, AbstractCompanion.Intent intent, int intentBaseDmg, int multiplier, boolean isMultiDamage) {
        this.nextMove = nextMove;
        this.intent = intent;
        this.baseDamage = intentBaseDmg;
        this.multiplier = multiplier;
        this.isMultiDamage = isMultiDamage;
    }
}
