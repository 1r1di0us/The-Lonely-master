package lonelymod.companions;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.vfx.*;
import com.megacrit.cardcrawl.vfx.combat.BuffParticleEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashIntentEffect;
import com.megacrit.cardcrawl.vfx.combat.StunStarEffect;
import com.megacrit.cardcrawl.vfx.combat.UnknownParticleEffect;
import lonelymod.CompanionStrings;
import lonelymod.LonelyMod;
import lonelymod.actions.CallMoveAction;
import lonelymod.fields.CompanionField;
import lonelymod.interfaces.ModifyCompanionBlockInterface;
import lonelymod.interfaces.TriggerOnCompanionTurnEndPowerInterface;
import lonelymod.interfaces.TriggerOnPerformMoveInterface;
import lonelymod.powers.TargetPower;
import lonelymod.relics.PaperDaug;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.lastCombatMetricKey;

public abstract class AbstractCompanion extends AbstractMonster {

    private static final Logger logger = LogManager.getLogger(AbstractMonster.class.getName());

    private static final float INTENT_HB_W = 64.0F * Settings.scale;

    private float intentParticleTimer = 0.0F;
    private BobEffect bobEffect = new BobEffect();
    private ArrayList<AbstractGameEffect> intentVfx = new ArrayList<>();

    public AbstractCreature targetEnemy;
    public boolean isTargeted;
    public int targetAmount = 0;
    public static Random companionRng = null;
    public CompanionMoveInfo move;

    private Color nameColor = new Color();
    private Color nameBgColor = new Color(0.0F, 0.0F, 0.0F, 0.0F);
    private float hoverTimer = 0.0F;
    private float intentAngle = 0.0F;

    public PowerTip intentTip = new PowerTip();
    public Texture intentImg = null;
    public ArrayList<BlockInfo> block = new ArrayList<>();
    public int intentDmg = -1;
    public int intentBaseDmg = -1;
    public int intentMultiAmt = 0;
    public boolean isMultiDmg = false;
    public int intentBlk = -1;
    public int intentBaseBlk = -1;
    public int intentMultiBlkAmt = 0;
    public boolean isMultiBlk = false;
    private Color intentColor = Color.WHITE.cpy();

    protected final String ID;
    protected String NAME;
    protected String[] MOVES;
    protected String[] INTENTS;
    protected String[] INTENT_TOOLTIPS;
    protected String[] DIALOG;

    protected int lastDialog = -1;

    public static final byte DEFAULT = 0;
    public static final byte ATTACK = 1;
    public static final byte PROTECT = 2;
    public static final byte SPECIAL = 3;
    public static final byte UNKNOWN = 4;
    public static final byte NONE = 5;

    public static final float INIT_Y = -40;
    private boolean isBlockModified = false;

    //card preview stuff
    protected float cardDrawScale = 0.7F;
    protected AbstractCard cardsToPreview = null; //naming them opposite since that's the way they are in AbstractCard
    protected ArrayList<AbstractCard> cardToPreview = new ArrayList<>();
    protected int previewIndex = 0;
    protected float rotationTimer = 0.f;

    public AbstractCompanion(String name, String id, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl) {
        super(name, id, 1, hb_x, hb_y, hb_w, hb_h, imgUrl,0, 0); // we override drawX anyway
        ID = id;
        CompanionStrings companionStrings = LonelyMod.getCompanionStrings(ID);
        NAME = companionStrings.NAME;
        MOVES = companionStrings.MOVES;
        INTENTS = companionStrings.INTENTS;
        INTENT_TOOLTIPS = companionStrings.INTENT_TOOLTIPS;
        DIALOG = companionStrings.DIALOG;
        if (AbstractDungeon.getCurrRoom() instanceof MonsterRoom && lastCombatMetricKey.equals("Shield and Spear")) {
            this.drawX = Settings.WIDTH * 0.5F + AbstractDungeon.player.hb_w;
        } else {
            this.drawX = AbstractDungeon.player.drawX + AbstractDungeon.player.hb_w;
        }
        this.drawY = AbstractDungeon.floorY + INIT_Y * Settings.yScale;
        this.dialogX = this.drawX + this.hb_w * 0.4F;
        this.dialogY = this.drawY + this.hb_h * 0.8F;
    }

    // move methods:

    public void performTurn(boolean callDefault) {
        if (callDefault) AbstractDungeon.actionManager.turnHasEnded = true; // this way vulnerable and target don't immediately go down if the companion applies them
        if (!(targetEnemy != null && !targetEnemy.isDeadOrEscaped())) {
            getTarget(); //I have no clue if this solves anything, but this used to call getTarget();
        }
        takeTurn();
        for (AbstractPower p : this.powers)
            if (p instanceof TriggerOnPerformMoveInterface)
                ((TriggerOnPerformMoveInterface) p).triggerOnPerformMove(this.nextMove);
        for (AbstractPower p : AbstractDungeon.player.powers)
            if (p instanceof TriggerOnPerformMoveInterface) //nothing currently
                ((TriggerOnPerformMoveInterface) p).triggerOnPerformMove(this.nextMove);
        if (callDefault) {
            for (AbstractPower p : this.powers)
                if (p instanceof TriggerOnCompanionTurnEndPowerInterface) //frenzy
                    ((TriggerOnCompanionTurnEndPowerInterface) p).triggerOnCompanionTurnEnd();
            for (AbstractPower p : AbstractDungeon.player.powers)
                if (p instanceof TriggerOnCompanionTurnEndPowerInterface) //nothing currently
                    ((TriggerOnCompanionTurnEndPowerInterface) p).triggerOnCompanionTurnEnd();
            addToBot(new CallMoveAction(DEFAULT, this));
        }
    }

    @Override
    public abstract void takeTurn();

    public abstract void performMove(byte move);

    public abstract void callDefault();

    public abstract void talk();

    public void callMainMove(byte move, boolean flashIntent, boolean makeIntent) {
        if (flashIntent) flashIntent();
        switch (move) {
            case ATTACK:
                callAttack();
                break;
            case PROTECT:
                callProtect();
                break;
            case SPECIAL:
                callSpecial();
                break;
        }
        if (makeIntent) createIntent();
    }
    protected abstract void callAttack();
    protected abstract void callProtect();
    protected abstract void callSpecial();

    public void callUnknown() {
        flashIntent();
        setMove(UNKNOWN, Intent.UNKNOWN);
        createIntent();
    }
    public void callNone() {
        if (nextMove != NONE) {
            flashIntent();
        }
        setMove(NONE, Intent.NONE);
    }

    public abstract void updateIntentTip();

    public abstract String getKeywordMoveTip(byte move, boolean head);

    //in order to target the companion with a card you must add the card to CompanionField.playableCards.get(AbstractDungeon.player) in your summon
    //then you must Use useTheCard to implement the seperate functionality when the card is played on the companion.
    public abstract void useTheCard(AbstractCard card, AbstractPlayer p, AbstractMonster m);

    @Override
    public void init() {
        this.getMove();
        this.healthBarUpdatedEvent();
        this.createIntent();
    }

    @Override
    protected void getMove(int paramInt) {};

    protected void getMove() {
        this.callDefault();
    }

    @Override
    public void createIntent() {
        this.intent = this.move.intent;
        this.intentParticleTimer = 0.5F;
        this.nextMove = this.move.nextMove;
        this.intentBaseDmg = this.move.baseDamage;
        this.intentBaseBlk = this.move.baseBlock;
        if (this.move.baseDamage > -1) {
            if (targetEnemy == null)
                getTarget();
            calculateDamage(this.intentBaseDmg);
            if (this.move.isMultiDamage) {
                this.intentMultiAmt = this.move.damageMultiplier;
                this.isMultiDmg = true;
            } else {
                this.intentMultiAmt = -1;
                this.isMultiDmg = false;
            }
        }
        if (this.move.baseBlock > -1) {
            calculateBlock(this.intentBaseBlk);
            if (this.move.isMultiBlock) {
                this.intentMultiBlkAmt = this.move.blockMultiplier;
                this.isMultiBlk = true;
            } else {
                this.intentMultiBlkAmt = -1;
                this.isMultiBlk = false;
            }

        }
        this.intentImg = getIntentImg();
        this.tipIntent = this.intent;
        this.intentAlpha = 0.0F;
        this.intentAlphaTarget = 1.0F;
        updateIntentTip();
    }

    public void setMove(String moveName, byte nextMove, Intent intent, int base, int multiplier, boolean isMulti, boolean isAttack) {
        this.moveName = moveName;
        if (nextMove != -1) {
            this.moveHistory.add(nextMove);
        }
        this.move = new CompanionMoveInfo(nextMove, intent, base, multiplier, isMulti, isAttack);
    }

    @Override
    public void setMove(String moveName, byte nextMove, Intent intent) {
        if (intent == AbstractMonster.Intent.ATTACK || intent == AbstractMonster.Intent.ATTACK_BUFF || intent == AbstractMonster.Intent.ATTACK_DEFEND || intent == AbstractMonster.Intent.ATTACK_DEBUFF || intent == AbstractMonster.Intent.DEFEND || intent == AbstractMonster.Intent.DEFEND_BUFF || intent == AbstractMonster.Intent.DEFEND_DEBUFF) {
            for(int i = 0; i < 8; ++i) {
                AbstractDungeon.effectsQueue.add(new TextAboveCreatureEffect(MathUtils.random((float)Settings.WIDTH * 0.25F, (float)Settings.WIDTH * 0.75F), MathUtils.random((float)Settings.HEIGHT * 0.25F, (float)Settings.HEIGHT * 0.75F), "ENEMY MOVE " + moveName + " IS SET INCORRECTLY! REPORT TO DEV", Color.RED.cpy()));
            }

            logger.info("COMPANION MOVE " + moveName + " IS SET INCORRECTLY! REPORT TO DEV");
        }

        this.setMove(moveName, nextMove, intent, -1, 0, false, false);
    }

    public void setMove(byte nextMove, Intent intent, int base, int multiplier, boolean isMulti, boolean isAttack) {
        this.setMove(null, nextMove, intent, base, multiplier, isMulti, isAttack);
    }

    public void setMove(String moveName, byte nextMove, Intent intent, int base, boolean isAttack) {
        this.setMove(moveName, nextMove, intent, base, 0, false, isAttack);
    }

    public void setMove(byte nextMove, Intent intent, int base, boolean isAttack) {
        this.setMove(null, nextMove, intent, base, 0, false, isAttack);
    }

    public void setMove(byte nextMove, Intent intent) {
        this.setMove(null, nextMove, intent, -1, 0, false, false);
    }

    //you should override the other setMove() functions, so they do the funny when you set the move wrong

    //important update methods:

    @Override
    public void update() {
        for (AbstractPower p : this.powers)
            p.updateParticles();
        updateReticle();
        updateHealthBar();
        updateAnimations();
        updateEscapeAnimation();
        updateIntent();
        this.tint.update();
        if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.DEATH) {
            CompanionField.hoveredCompanion.set(AbstractDungeon.player, null);
            this.hb.update();
            this.intentHb.update();
            this.healthHb.update();
            if ((this.hb.hovered || this.intentHb.hovered || this.healthHb.hovered) && !AbstractDungeon.player.isDraggingCard) {
                CompanionField.hoveredCompanion.set(AbstractDungeon.player, this);
            }
            if (CompanionField.hoveredCompanion.get(AbstractDungeon.player) == null)
                AbstractDungeon.player.hoverEnemyWaitTimer = -1.0F;
        } else {
            CompanionField.hoveredCompanion.set(AbstractDungeon.player, null);
        }

        if (!cardToPreview.isEmpty()) {
            if (hb.hovered) {
                if (rotationTimer <= 0F) {
                    rotationTimer = getRotationTimeNeeded();
                    cardsToPreview = cardToPreview.get(previewIndex);
                    if (previewIndex == cardToPreview.size() - 1) {
                        previewIndex = 0;
                    } else {
                        previewIndex++;
                    }
                } else {
                    rotationTimer -= Gdx.graphics.getDeltaTime();
                }
            }
        }

        //if name is being displayed and you click, talk!
        if ((!AbstractDungeon.player.isDraggingCard
                || AbstractDungeon.player.hoveredCard == null)
                && !this.isDying
                && this.hb.hovered
                && (InputHelper.justClickedLeft
                || CInputActionSet.select.isJustReleased())) {
            talk();
        }
    }

    public float getRotationTimeNeeded() {
        return 2.5f;
    }


    private void updateIntent() {
        this.bobEffect.update();
        if (this.intentAlpha != this.intentAlphaTarget && this.intentAlphaTarget == 1.0F) {
            this.intentAlpha += Gdx.graphics.getDeltaTime();
            if (this.intentAlpha > this.intentAlphaTarget)
                this.intentAlpha = this.intentAlphaTarget;
        } else if (this.intentAlphaTarget == 0.0F) {
            this.intentAlpha -= Gdx.graphics.getDeltaTime() / 1.5F;
            if (this.intentAlpha < 0.0F)
                this.intentAlpha = 0.0F;
        }
        if (!this.isDying && !this.isEscaping)
            updateIntentVFX();
        for (Iterator<AbstractGameEffect> i = this.intentVfx.iterator(); i.hasNext(); ) {
            AbstractGameEffect e = i.next();
            e.update();
            if (e.isDone)
                i.remove();
        }
    }

    private void updateIntentVFX() {
        if (this.intentAlpha > 0.0F)
            if (this.intent == Intent.ATTACK_DEBUFF || this.intent == Intent.DEBUFF || this.intent == Intent.STRONG_DEBUFF || this.intent == Intent.DEFEND_DEBUFF) {
                this.intentParticleTimer -= Gdx.graphics.getDeltaTime();
                if (this.intentParticleTimer < 0.0F) {
                    this.intentParticleTimer = 1.0F;
                    this.intentVfx.add(new DebuffParticleEffect(this.intentHb.cX, this.intentHb.cY));
                }
            } else if (this.intent == Intent.ATTACK_BUFF || this.intent == Intent.BUFF || this.intent == Intent.DEFEND_BUFF) {
                this.intentParticleTimer -= Gdx.graphics.getDeltaTime();
                if (this.intentParticleTimer < 0.0F) {
                    this.intentParticleTimer = 0.1F;
                    this.intentVfx.add(new BuffParticleEffect(this.intentHb.cX, this.intentHb.cY));
                }
            } else if (this.intent == Intent.ATTACK_DEFEND) {
                this.intentParticleTimer -= Gdx.graphics.getDeltaTime();
                if (this.intentParticleTimer < 0.0F) {
                    this.intentParticleTimer = 0.5F;
                    this.intentVfx.add(new ShieldParticleEffect(this.intentHb.cX, this.intentHb.cY));
                }
            } else if (this.intent == Intent.UNKNOWN) {
                this.intentParticleTimer -= Gdx.graphics.getDeltaTime();
                if (this.intentParticleTimer < 0.0F) {
                    this.intentParticleTimer = 0.5F;
                    this.intentVfx.add(new UnknownParticleEffect(this.intentHb.cX, this.intentHb.cY));
                }
            } else if (this.intent == Intent.STUN) {
                this.intentParticleTimer -= Gdx.graphics.getDeltaTime();
                if (this.intentParticleTimer < 0.0F) {
                    this.intentParticleTimer = 0.67F;
                    this.intentVfx.add(new StunStarEffect(this.intentHb.cX, this.intentHb.cY));
                }
            }
    }

    @Override
    public void flashIntent() {
        if (this.intentImg != null) {
            AbstractDungeon.effectList.add(new FlashIntentEffect(this.intentImg, this));
        }
        this.intentAlphaTarget = 0.0F;
    }

    public void refreshIntentHbLocation() {
        this.intentHb.move(this.hb.cX + this.intentOffsetX, this.hb.cY + this.hb_h / 2.0F + INTENT_HB_W / 2.0F);
    }

    //calculate methods

    private void calculateDamage(int dmg) {
        float tmp = dmg;
        for (AbstractPower p : this.powers)
            tmp = p.atDamageGive(tmp, DamageInfo.DamageType.NORMAL);
        if (targetEnemy != null) {
            for (AbstractPower p : targetEnemy.powers) {
                if (!(p instanceof VulnerablePower))
                    tmp = p.atDamageReceive(tmp, DamageInfo.DamageType.NORMAL);
            }
        }
        //tmp = AbstractDungeon.player.stance.atDamageReceive(tmp, DamageInfo.DamageType.NORMAL);
        if (isTargeted) {
            if (AbstractDungeon.player.hasRelic(PaperDaug.ID))
                tmp = (int) (tmp * 2.0F);
            else
                tmp = (int) (tmp * 1.5F);
        }
        for (AbstractPower p : this.powers)
            tmp = p.atDamageFinalGive(tmp, DamageInfo.DamageType.NORMAL);
        if (targetEnemy != null) {
            for (AbstractPower p : targetEnemy.powers)
                tmp = p.atDamageFinalReceive(tmp, DamageInfo.DamageType.NORMAL);
        }
        dmg = MathUtils.floor(tmp);
        if (dmg < 0)
            dmg = 0;
        this.intentDmg = dmg;
    }

    private void calculateBlock(int blk) {
        float tmp = blk;
        for (AbstractPower p : CompanionField.currCompanion.get(AbstractDungeon.player).powers) {
            if (p instanceof ModifyCompanionBlockInterface)
                tmp = ((ModifyCompanionBlockInterface) p).modifyCompanionBlock(tmp, this);
        }
        if (this.intentBaseBlk != MathUtils.floor(tmp))
            this.isBlockModified = true;
        if (tmp < 0.0F)
            tmp = 0.0F;
        this.intentBlk = MathUtils.floor(tmp);
    }

    @Override
    public void applyPowers() {
        //used to call getTarget() if targetEnemy is null or dead or escaped.
        if (targetEnemy != null && !targetEnemy.isDeadOrEscaped()) {
            for (DamageInfo dmg : this.damage) {
                dmg.type = DamageInfo.DamageType.NORMAL;
                applyPowersToDamage(dmg, targetEnemy);
                dmg.type = DamageInfo.DamageType.THORNS;
            }
        }
        for (BlockInfo blk : this.block) {
            blk.applyPowers(this, AbstractDungeon.player);
        }
        if (this.move.baseDamage > -1 && targetEnemy != null && !targetEnemy.isDeadOrEscaped())
            calculateDamage(this.move.baseDamage);
        if (this.move.baseBlock > -1)
            calculateBlock(this.move.baseBlock);
        this.intentImg = getIntentImg();
        updateIntentTip();
    }

    public void getTarget() {
        AbstractMonster currTarget = AbstractDungeon.getCurrRoom().monsters.getRandomMonster(null, true, companionRng);
        targetAmount = 0;
        isTargeted = false;
        MonsterGroup targetGroup = new MonsterGroup(currTarget);
        for (AbstractMonster mon : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (mon.hasPower(TargetPower.POWER_ID) && !mon.isDeadOrEscaped()) {
                if (targetAmount < mon.getPower(TargetPower.POWER_ID).amount) {
                    targetAmount = mon.getPower(TargetPower.POWER_ID).amount;
                    targetGroup = new MonsterGroup(mon);
                }
                else if (targetAmount == mon.getPower(TargetPower.POWER_ID).amount) {
                    targetGroup.add(mon);
                }
            }
        }
        if (targetAmount > 0) {
            currTarget = targetGroup.getRandomMonster(null, true, companionRng);
            isTargeted = true;
        }
        if (currTarget != null && !currTarget.isDeadOrEscaped()) {
            targetEnemy = currTarget;
        } else {
            targetEnemy = null;
        }
    }

    protected void applyPowersToDamage(DamageInfo dmg, AbstractCreature target) {
        dmg.output = dmg.base;
        dmg.isModified = false;
        float tmp = dmg.output;
        for (AbstractPower p : this.powers) {
            tmp = p.atDamageGive(tmp, dmg.type);
            if (dmg.base != (int)tmp)
                dmg.isModified = true;
        }
        for (AbstractPower p : target.powers) {
            if (!(p instanceof VulnerablePower)) {
                tmp = p.atDamageReceive(tmp, dmg.type);
                if (dmg.base != (int) tmp)
                    dmg.isModified = true;
            }
        }
        if (dmg.base != (int)tmp)
            dmg.isModified = true;
        for (AbstractPower p : this.powers) {
            tmp = p.atDamageFinalGive(tmp, dmg.type);
            if (dmg.base != (int)tmp)
                dmg.isModified = true;
        }
        for (AbstractPower p : target.powers) {
            tmp = p.atDamageFinalReceive(tmp, dmg.type);
            if (dmg.base != (int)tmp)
                dmg.isModified = true;
        }
        if (isTargeted && target.hasPower(TargetPower.POWER_ID))
            if (AbstractDungeon.player.hasRelic(PaperDaug.ID))
                tmp = (int)(tmp * 2.0F);
            else
                tmp = (int)(tmp * 1.5F);
        if (dmg.base != (int)tmp)
            dmg.isModified = true;
        dmg.output = MathUtils.floor(tmp);
        if (dmg.output < 0)
            dmg.output = 0;
    }

    public Texture getIntentImg() {
        switch (this.intent) {
            case ATTACK:
                return getAttackIntent();
            case ATTACK_BUFF:
                return getAttackIntent();
            case ATTACK_DEBUFF:
                return getAttackIntent();
            case ATTACK_DEFEND:
                return getAttackIntent();
            case BUFF:
                return ImageMaster.INTENT_BUFF_L;
            case DEBUFF:
                return ImageMaster.INTENT_DEBUFF_L;
            case STRONG_DEBUFF:
                return ImageMaster.INTENT_DEBUFF2_L;
            case DEFEND:
                return ImageMaster.INTENT_DEFEND_L;
            case DEFEND_DEBUFF:
                return ImageMaster.INTENT_DEFEND_L;
            case DEFEND_BUFF:
                return ImageMaster.INTENT_DEFEND_BUFF_L;
            case ESCAPE:
                return ImageMaster.INTENT_ESCAPE_L;
            case MAGIC:
                return ImageMaster.INTENT_MAGIC_L;
            case SLEEP:
                return ImageMaster.INTENT_SLEEP_L;
            case STUN:
                return null;
            case UNKNOWN:
                return ImageMaster.INTENT_UNKNOWN_L;
        }
        return ImageMaster.INTENT_UNKNOWN_L;
    }

    public Texture getIntentTipImg() {
        switch (this.intent) {
            case ATTACK:
                return getAttackIntentTipImg();
            case ATTACK_BUFF:
                return ImageMaster.INTENT_ATTACK_BUFF;
            case ATTACK_DEBUFF:
                return ImageMaster.INTENT_ATTACK_DEBUFF;
            case ATTACK_DEFEND:
                return ImageMaster.INTENT_ATTACK_DEFEND;
            case BUFF:
                return ImageMaster.INTENT_BUFF;
            case DEBUFF:
                return ImageMaster.INTENT_DEBUFF;
            case STRONG_DEBUFF:
                return ImageMaster.INTENT_DEBUFF2;
            case DEFEND:
                return ImageMaster.INTENT_DEFEND;
            case DEFEND_DEBUFF:
                return ImageMaster.INTENT_DEFEND;
            case DEFEND_BUFF:
                return ImageMaster.INTENT_DEFEND_BUFF;
            case ESCAPE:
                return ImageMaster.INTENT_ESCAPE;
            case MAGIC:
                return ImageMaster.INTENT_MAGIC;
            case SLEEP:
                return ImageMaster.INTENT_SLEEP;
            case STUN:
                return ImageMaster.INTENT_STUN;
            case UNKNOWN:
                return ImageMaster.INTENT_UNKNOWN;
        }
        return ImageMaster.INTENT_UNKNOWN;
    }

    private Texture getAttackIntentTipImg() {
        int tmp;
        if (this.isMultiDmg) {
            tmp = this.intentDmg * this.intentMultiAmt;
        } else {
            tmp = this.intentDmg;
        }
        if (tmp < 5)
            return ImageMaster.INTENT_ATK_TIP_1;
        if (tmp < 10)
            return ImageMaster.INTENT_ATK_TIP_2;
        if (tmp < 15)
            return ImageMaster.INTENT_ATK_TIP_3;
        if (tmp < 20)
            return ImageMaster.INTENT_ATK_TIP_4;
        if (tmp < 25)
            return ImageMaster.INTENT_ATK_TIP_5;
        if (tmp < 30)
            return ImageMaster.INTENT_ATK_TIP_6;
        return ImageMaster.INTENT_ATK_TIP_7;
    }

    //important render functions:
    //the render functions are copied from AbstractMonster.
    //the only change I did is remove the interaction with Runic Dome for both renderTip and render

    @Override
    public void renderTip(SpriteBatch sb) {

        this.tips.clear();
        if (this.intentAlphaTarget == 1.0F && this.intent != AbstractMonster.Intent.NONE) {
            this.tips.add(this.intentTip);
        }

        Iterator var2 = this.powers.iterator();

        while (var2.hasNext()) {
            AbstractPower p = (AbstractPower) var2.next();
            if (p.region48 != null) {
                this.tips.add(new PowerTip(p.name, p.description, p.region48));
            } else {
                this.tips.add(new PowerTip(p.name, p.description, p.img));
            }
        }

        if (!this.tips.isEmpty()) {
            if (this.hb.cX + this.hb.width / 2.0F < TIP_X_THRESHOLD) {
                TipHelper.queuePowerTips(this.hb.cX + this.hb.width / 2.0F + TIP_OFFSET_R_X, this.hb.cY + TipHelper.calculateAdditionalOffset(this.tips, this.hb.cY), this.tips);
            } else {
                TipHelper.queuePowerTips(this.hb.cX - this.hb.width / 2.0F + TIP_OFFSET_L_X, this.hb.cY + TipHelper.calculateAdditionalOffset(this.tips, this.hb.cY), this.tips);
            }
        }

        if (this.cardsToPreview != null) {
            renderCardPreview(sb);
        }
    }

    public void renderCardPreview(SpriteBatch sb) {
        if (AbstractDungeon.player != null && AbstractDungeon.player.isDraggingCard)
            return;
        float tmpScale = cardDrawScale * 0.8F;
        cardsToPreview.current_x = drawX - (AbstractCard.IMG_WIDTH / 2.0F + AbstractCard.IMG_WIDTH / 2.0F * 0.8F + 16.0F) * cardDrawScale;
        cardsToPreview.current_y = drawY + (AbstractCard.IMG_HEIGHT / 2.0F - AbstractCard.IMG_HEIGHT / 2.0F * 0.8F) * cardDrawScale;

        this.cardsToPreview.drawScale = tmpScale;
        this.cardsToPreview.render(sb);
    }

    @Override
    public void render(SpriteBatch sb) {
        if (!this.isDead && !this.escaped) {
            if (this.atlas == null) {
                sb.setColor(this.tint.color);
                if (this.img != null) {
                    sb.draw(this.img, this.drawX - (float)this.img.getWidth() * Settings.scale / 2.0F + this.animX, this.drawY + this.animY, (float)this.img.getWidth() * Settings.scale, (float)this.img.getHeight() * Settings.scale, 0, 0, this.img.getWidth(), this.img.getHeight(), this.flipHorizontal, this.flipVertical);
                }
            } else {
                this.state.update(Gdx.graphics.getDeltaTime());
                this.state.apply(this.skeleton);
                this.skeleton.updateWorldTransform();
                this.skeleton.setPosition(this.drawX + this.animX, this.drawY + this.animY);
                this.skeleton.setColor(this.tint.color);
                this.skeleton.setFlip(this.flipHorizontal, this.flipVertical);
                sb.end();
                CardCrawlGame.psb.begin();
                sr.draw(CardCrawlGame.psb, this.skeleton);
                CardCrawlGame.psb.end();
                sb.begin();
                sb.setBlendFunction(770, 771);
            }

            if (this == AbstractDungeon.getCurrRoom().monsters.hoveredMonster && this.atlas == null) {
                sb.setBlendFunction(770, 1);
                sb.setColor(new Color(1.0F, 1.0F, 1.0F, 0.1F));
                if (this.img != null) {// 898
                    sb.draw(this.img, this.drawX - (float)this.img.getWidth() * Settings.scale / 2.0F + this.animX, this.drawY + this.animY, (float)this.img.getWidth() * Settings.scale, (float)this.img.getHeight() * Settings.scale, 0, 0, this.img.getWidth(), this.img.getHeight(), this.flipHorizontal, this.flipVertical);
                    sb.setBlendFunction(770, 771);
                }
            }

            if (!this.isDying && !this.isEscaping && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.player.isDead && this.intent != AbstractMonster.Intent.NONE && !Settings.hideCombatElements) {
                this.renderIntentVfxBehind(sb);
                this.renderIntent(sb);
                this.renderIntentVfxAfter(sb);
                this.renderDamageRange(sb);
            }

            this.hb.render(sb);
            this.intentHb.render(sb);
            this.healthHb.render(sb);
        }

        if (!AbstractDungeon.player.isDead) {
            this.renderHealth(sb);
            this.renderName(sb);
        }
    }

    private void renderDamageRange(SpriteBatch sb) {
        if (this.intent.name().contains("ATTACK")) {
            if (this.isMultiDmg) {
                FontHelper.renderFontLeftTopAligned(sb, FontHelper.topPanelInfoFont, Integer.toString(this.intentDmg) + "x" + Integer.toString(this.intentMultiAmt), this.intentHb.cX - 30.0F * Settings.scale, this.intentHb.cY + this.bobEffect.y - 12.0F * Settings.scale, this.intentColor);
            } else {
                FontHelper.renderFontLeftTopAligned(sb, FontHelper.topPanelInfoFont, Integer.toString(this.intentDmg), this.intentHb.cX - 30.0F * Settings.scale, this.intentHb.cY + this.bobEffect.y - 12.0F * Settings.scale, this.intentColor);
            }
        }
        if (this.intent.name().contains("DEFEND")) {
            if (this.isMultiBlk) {
                FontHelper.renderFontLeftTopAligned(sb, FontHelper.topPanelInfoFont, Integer.toString(this.intentBlk) + "x" + Integer.toString(this.intentMultiBlkAmt), this.intentHb.cX - 30.0F * Settings.scale, this.intentHb.cY + this.bobEffect.y - 12.0F * Settings.scale, this.intentColor);
            } else {
                FontHelper.renderFontLeftTopAligned(sb, FontHelper.topPanelInfoFont, Integer.toString(this.intentBlk), this.intentHb.cX - 30.0F * Settings.scale, this.intentHb.cY + this.bobEffect.y - 12.0F * Settings.scale, this.intentColor);
            }
        }
    }

    private void renderIntentVfxBehind(SpriteBatch sb) {
        Iterator var2 = this.intentVfx.iterator();

        while(var2.hasNext()) {
            AbstractGameEffect e = (AbstractGameEffect)var2.next();
            if (e.renderBehind) {
                e.render(sb);
            }
        }
    }

    private void renderIntentVfxAfter(SpriteBatch sb) {
        Iterator var2 = this.intentVfx.iterator();

        while(var2.hasNext()) {
            AbstractGameEffect e = (AbstractGameEffect)var2.next();
            if (!e.renderBehind) {
                e.render(sb);
            }
        }
    }

    private void renderName(SpriteBatch sb) {
        if (!this.hb.hovered) {
            this.hoverTimer = MathHelper.fadeLerpSnap(this.hoverTimer, 0.0F);
        } else {
            this.hoverTimer += Gdx.graphics.getDeltaTime();
        }

        if ((!AbstractDungeon.player.isDraggingCard || AbstractDungeon.player.hoveredCard == null || AbstractDungeon.player.hoveredCard.target == AbstractCard.CardTarget.ENEMY) && !this.isDying) {
            if (this.hoverTimer != 0.0F) {
                if (this.hoverTimer * 2.0F > 1.0F) {
                    this.nameColor.a = 1.0F;
                } else {
                    this.nameColor.a = this.hoverTimer * 2.0F;
                }
            } else {
                this.nameColor.a = MathHelper.slowColorLerpSnap(this.nameColor.a, 0.0F);
            }

            float tmp = Interpolation.exp5Out.apply(1.5F, 2.0F, this.hoverTimer);
            this.nameColor.r = Interpolation.fade.apply(Color.DARK_GRAY.r, Settings.CREAM_COLOR.r, this.hoverTimer * 10.0F);
            this.nameColor.g = Interpolation.fade.apply(Color.DARK_GRAY.g, Settings.CREAM_COLOR.g, this.hoverTimer * 3.0F);
            this.nameColor.b = Interpolation.fade.apply(Color.DARK_GRAY.b, Settings.CREAM_COLOR.b, this.hoverTimer * 3.0F);
            float y = Interpolation.exp10Out.apply(this.healthHb.cY, this.healthHb.cY - 8.0F * Settings.scale, this.nameColor.a);
            float x = this.hb.cX - this.animX;
            this.nameBgColor.a = this.nameColor.a / 2.0F * this.hbAlpha;
            sb.setColor(this.nameBgColor);
            TextureAtlas.AtlasRegion img = ImageMaster.MOVE_NAME_BG;
            sb.draw(img, x - (float)img.packedWidth / 2.0F, y - (float)img.packedHeight / 2.0F, (float)img.packedWidth / 2.0F, (float)img.packedHeight / 2.0F, (float)img.packedWidth, (float)img.packedHeight, Settings.scale * tmp, Settings.scale * 2.0F, 0.0F);
            Color var10000 = this.nameColor;
            var10000.a *= this.hbAlpha;
            FontHelper.renderFontCentered(sb, FontHelper.tipHeaderFont, this.name, x, y, this.nameColor);
        }
    }

    private void renderIntent(SpriteBatch sb) {
        this.intentColor.a = this.intentAlpha;
        sb.setColor(this.intentColor);
        /*if (this.intentBg != null) {
            sb.setColor(new Color(1.0F, 1.0F, 1.0F, this.intentAlpha / 2.0F));
            if (Settings.isMobile) {
                sb.draw(this.intentBg, this.intentHb.cX - 64.0F, this.intentHb.cY - 64.0F + this.bobEffect.y, 64.0F, 64.0F, 128.0F, 128.0F, Settings.scale * 1.2F, Settings.scale * 1.2F, 0.0F, 0, 0, 128, 128, false, false);
            } else {
                sb.draw(this.intentBg, this.intentHb.cX - 64.0F, this.intentHb.cY - 64.0F + this.bobEffect.y, 64.0F, 64.0F, 128.0F, 128.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 128, 128, false, false);
            }
        }*/

        if (this.intentImg != null && this.intent != AbstractMonster.Intent.UNKNOWN && this.intent != AbstractMonster.Intent.STUN) {
            if (this.intent != AbstractMonster.Intent.DEBUFF && this.intent != AbstractMonster.Intent.STRONG_DEBUFF) {
                this.intentAngle = 0.0F;
            } else {
                this.intentAngle += Gdx.graphics.getDeltaTime() * 150.0F;
            }

            sb.setColor(this.intentColor);
            if (Settings.isMobile) {
                sb.draw(this.intentImg, this.intentHb.cX - 64.0F, this.intentHb.cY - 64.0F + this.bobEffect.y, 64.0F, 64.0F, 128.0F, 128.0F, Settings.scale * 1.2F, Settings.scale * 1.2F, this.intentAngle, 0, 0, 128, 128, false, false);
            } else {
                sb.draw(this.intentImg, this.intentHb.cX - 64.0F, this.intentHb.cY - 64.0F + this.bobEffect.y, 64.0F, 64.0F, 128.0F, 128.0F, Settings.scale, Settings.scale, this.intentAngle, 0, 0, 128, 128, false, false);
            }
        }
    }

    @Override
    public void renderHealth(SpriteBatch sb) {
        float hbOff = ReflectionHacks.getPrivateInherited(this, AbstractCompanion.class, "hbYOffset");
        float x = this.hb.cX - this.hb.width / 2.0F;
        float y = this.hb.cY - this.hb.height / 2.0F + hbOff;
        ReflectionHacks.privateMethod(AbstractCreature.class, "renderPowerIcons", SpriteBatch.class, float.class, float.class).invoke(this, sb, x, y);
    }

    public void movePosition(float x, float y) {
        this.drawX = x + AbstractDungeon.player.hb_w;
        this.drawY = y + INIT_Y;
        this.dialogX = this.drawX + this.hb_w - 80.F * Settings.scale;
        this.dialogY = this.drawY + this.hb_h - 25.F * Settings.scale;
        this.animX = 0.0F;
        this.animY = 0.0F;
        refreshHitboxLocation();
    }

    private void updateEscapeAnimation() {
        if (this.escapeTimer != 0.0F) {
            this.flipHorizontal = true;
            this.escapeTimer -= Gdx.graphics.getDeltaTime();
            this.drawX -= Gdx.graphics.getDeltaTime() * 800.0F * Settings.scale;
        }
        if (this.escapeTimer < 0.0F) {
            this.escaped = true;
        }
    }

    public void escape() {
        this.isEscaping = true;
        this.escapeTimer = 3.0F;
    }

    /*methods to redo:
    constructor
    more constructors?
    refreshIntentHbLocation
    unhover
    damage
    setHp (X)
    updateHitbox
    updateDeathAnimation
    dispose
    updateEscapeAnimation
    escapeNext
    deathReact (X)
    die (x2)
    usePreBattleAction
    changeState(?)
    getLocStrings
    getIntentDamage
    getIntentBaseDamage
    setIntentBaseDamage
    * */

    //useless methods I have destroyed:

    @Override
    public void damage(DamageInfo info) {}
    @Override
    public void heal(int amount) {}

    @Override
    public void useUniversalPreBattleAction() {}
}
