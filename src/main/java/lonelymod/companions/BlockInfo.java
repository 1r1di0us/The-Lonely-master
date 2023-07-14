package lonelymod.companions;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import lonelymod.fields.CompanionField;
import lonelymod.interfaces.ModifyCompanionBlockInterface;

public class BlockInfo {

    public AbstractCompanion owner;

    public int base;
    public int output;
    public boolean isModified = false;

    public BlockInfo(AbstractCompanion blockSource, int base) {
        this.owner = blockSource;
        this.base = base;
        this.output = base;
    }

    public void applyPowers(AbstractCompanion owner, AbstractCreature target) {
        this.isModified = false;
        float tmp = this.output;
        for (AbstractPower p : owner.powers)
            if (p instanceof ModifyCompanionBlockInterface)
                tmp = ((ModifyCompanionBlockInterface) p).modifyBlock(tmp, owner);
        for (AbstractPower p : target.powers)
            if (p instanceof ModifyCompanionBlockInterface)
                tmp = ((ModifyCompanionBlockInterface) p).modifyBlock(tmp, owner);
        if (this.base != MathUtils.floor(tmp))
            this.isModified = true;
        if (tmp < 0.0F)
            tmp = 0.0F;
        this.output = MathUtils.floor(tmp);
    }
}
